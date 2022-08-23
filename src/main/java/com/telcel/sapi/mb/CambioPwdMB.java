package com.telcel.sapi.mb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CambioPwdService;
import com.telcel.sapi.service.LoginService;
import com.telcel.sapi.service.impl.CambioPwdServiceImpl;
import com.telcel.sapi.service.impl.LoginServiceImpl;

@ManagedBean (name = "cambiopwdMB")
@ViewScoped
public class CambioPwdMB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4311283575858458484L;
	static final Logger LOG = Logger.getLogger(CambioPwdMB.class);
	
	CambioPwdService pwdService;
	LoginService 	loginService;
	
	private String pwdOld;
	private String pwdNew;
	private String pwdNewRep;
	
	UsuarioDTO 	   usuario;
	private String msgSuc;
	private String msgErrPwdOld;
	private String msgErrPwdNew;
	private String msgErrPwdRptNew;
	
	@PostConstruct
	public void init() {
		usuario = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		pwdService 		= new CambioPwdServiceImpl();
		loginService	= new LoginServiceImpl();
		inicializaMsg();
		iniciaVar();
	}
	
	public void cambioPwd() {
		inicializaMsg();
		
		if(pwdOld.isEmpty()) {
			msgErrPwdOld 		= Mensajes.ERROR_PWD_OLD_VACIA;
		}		
		if(pwdNew.isEmpty()) {
			msgErrPwdNew 		= Mensajes.ERROR_PWD_NEW_VACIA;
		}		
		if(pwdNewRep.isEmpty()) {
			msgErrPwdRptNew 	= Mensajes.ERROR_PWD_NEW_REPEAT_VACIA;
		}		
		else {
			
			if(usuario.getPassword().equals(pwdOld)) {
				
				if(pwdNew.equals(pwdNewRep)) {
					
					if(pwdService.CambioPwd(usuario, pwdNew)) {
						msgSuc = Mensajes.PASSWORD_ACTUALIZADA_EXITO;
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioSesion", loginService.cargaUsuario(usuario.getNombreUsuario()));
						usuario = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
					}
					
				}else {
					msgErrPwdRptNew = Mensajes.ERROR_PWDS_NO_COINCIDEN;
				}
				
			}else {
				msgErrPwdOld = Mensajes.ERROR_PWD_OLD_ERRONEA;
			}
		}
		
	}
	
	public void inicializaMsg() {
		msgSuc 			= "";
		msgErrPwdOld 	= "";
		msgErrPwdNew 	= "";
		msgErrPwdRptNew = "";
	}
	
	public void iniciaVar() {
		pwdOld 	= "";
		pwdNew 	= "";
		pwdNewRep = "";
	}
	
	public void validanewpwd() {
		if(!pwdNew.equals(pwdNewRep)) {
			msgErrPwdRptNew = Mensajes.ERROR_PWDS_NO_COINCIDEN;
		}
	}
	
	public String getPwdOld() {
		return pwdOld;
	}

	public void setPwdOld(String pwdOld) {
		this.pwdOld = pwdOld;
	}

	public String getPwdNew() {
		return pwdNew;
	}

	public void setPwdNew(String pwdNew) {
		this.pwdNew = pwdNew;
	}

	public String getPwdNewRep() {
		return pwdNewRep;
	}

	public void setPwdNewRep(String pwdNewRep) {
		this.pwdNewRep = pwdNewRep;
	}

	public String getMsgSuc() {
		return msgSuc;
	}

	public String getMsgErrPwdOld() {
		return msgErrPwdOld;
	}

	public String getMsgErrPwdNew() {
		return msgErrPwdNew;
	}

	public String getMsgErrPwdRptNew() {
		return msgErrPwdRptNew;
	}
}
