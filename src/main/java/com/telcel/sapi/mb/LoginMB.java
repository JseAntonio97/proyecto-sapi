package com.telcel.sapi.mb;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Actividades;
import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.dao.impl.BitacoraDAOImpl;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.LoginService;
import com.telcel.sapi.service.impl.LoginServiceImpl;

@ManagedBean (name = "loginMB")
@ViewScoped
public class LoginMB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4332707982049043830L;  
	static final Logger LOG = Logger.getLogger(LoginMB.class);
	
	LoginService loginService;
	BitacoraDAO bitacoraService;
	
	UsuarioDTO usuario;
	
	private String numeroEmpleado;
	private String password;

	private String msgError;
	private String msgWarn;
	
	@PostConstruct
	public void init() {
		usuario			= new UsuarioDTO();
		bitacoraService	= new BitacoraDAOImpl();
		loginService	= new LoginServiceImpl();
		inicializaMgs();
	}
	
	public void login() {
		inicializaMgs();
		
		if(numeroEmpleado.isEmpty() && password.isEmpty()) {
			msgWarn = Mensajes.USUARIO_PWD_VACIOS;
		}else {			
			if(numeroEmpleado.isEmpty()) {
				msgWarn = Mensajes.USUARIO_VACIO;
			}else if(password.isEmpty()){
				msgWarn = Mensajes.PASSWORD_VACIO;
			}else {
				usuario = new UsuarioDTO();		
				usuario = loginService.cargaUsuario(numeroEmpleado);
				
				if(usuario.getEstatus().equals(Estatus.INACTIVO.toString())) {
					msgError = Mensajes.ERROR_USUARIO_INHABILITADO;
				}else {
					
					if(usuario != null) {
						
						if(password.equals(usuario.getPassword())) {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioSesion", usuario);						
							try {							
								bitacoraService.insertBitacora(usuario.getIdUsuario(), Actividades.INICIO_SESION, "Se inició sesión en el sistema", "Login", 0L);
								FacesContext.getCurrentInstance().getExternalContext().redirect("pages/bandeja.html");
							} catch (IOException e) {
								LOG.error(e);
							}
						}else {
							msgError = Mensajes.ERROR_CONSTRASENIA_INVALIDA;
						}
						
					}else {
						msgError = Mensajes.ERROR_USUARIO_INEXISTENTE;
					}
					
				}
			}			
		}
		
	}
	
	public void CierraSesion() {
		
		if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion") == null){
			try {					
				FacesContext.getCurrentInstance().getExternalContext().redirect("/SAPI");
			} catch (IOException e) {
				LOG.error(e);
			}
		}else {
			bitacoraInvocade();
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/SAPI");
			} catch (IOException e) {
				LOG.error("Error al redireccionar, cerrar sesión: " + e);
			}
		}
	}
	
	public void bitacoraInvocade() {
		usuario = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		
		bitacoraService	= new BitacoraDAOImpl();
		bitacoraService.insertBitacora(usuario.getIdUsuario(), Actividades.CERRAR_SESION, "Se cerró sesión en el sistema", "Login", 0L);		
	}
	
	public void inicializaVariables() {
		numeroEmpleado 	= "";
		password		= "";
	}
	
	public void inicializaMgs() {
		msgError	= "";
		msgWarn		= "";
	}

	public void ValidaSesion() {
		try {
			usuario = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
			if(usuario == null){		
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				try {					
					FacesContext.getCurrentInstance().getExternalContext().redirect("/SAPI");
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			
		} catch (Exception e) {
			LOG.error("Error al obtener informació de la sesión - " + e);
		}
	}

	public void ValidaLogin() {
		usuario = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
			
		if(usuario != null){
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("pages/bandeja.html");
			} catch (IOException e) {
				LOG.error("Error al redireccionar - " + e);
			}
		}
	}
	

	public String getNumeroEmpleado() {
		return numeroEmpleado;
	}

	public void setNumeroEmpleado(String numeroEmpleado) {
		this.numeroEmpleado = numeroEmpleado;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgWarn() {
		return msgWarn;
	}

	public void setMsgWarn(String msgWarn) {
		this.msgWarn = msgWarn;
	}
}
