package com.telcel.sapi.mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Actividades;
import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.dao.impl.BitacoraDAOImpl;
import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.Conversiones;
import com.telcel.sapi.service.ProyectoService;
import com.telcel.sapi.service.impl.ConversionesImpl;
import com.telcel.sapi.service.impl.ProyectoServiceImpl;

@ManagedBean (name = "addPortafolioMB")
@ViewScoped
public class AddPortafolioMB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6882088082646306050L;
	static final Logger LOG = Logger.getLogger(AddPortafolioMB.class);
	static final String PAGINA_PROYECTOS = "portafolio-empresarial.html";
	static final String SI				 = "Si";
	
	Conversiones		conversiones;
	ProyectoService 	proyectoService;
	BitacoraDAO 		bitacoraService;
	UsuarioDTO 			usuarioDTO;

	private Date 		FecEntrada;
	private Date 		FecSalida;
	private ProyectoDTO proyectoGetter;	

	private String 		msgError;
	private String 		msgSuccess;
	private String 		msgWarnning;
	private String 		msgWarnningFolio;
	
	private List<FoliosRegistradosDTO> listFoliosRegistrados;
	private int numFoliosExistente;
	
	@PostConstruct
	private void init() {
		/**
		 * Valida el rol de acceso
		 */
		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");

		bitacoraService	= new BitacoraDAOImpl();
		inicializa();
		rebootMsg();
		
		listFoliosRegistrados = new ArrayList<FoliosRegistradosDTO>();
	}
	
	public void GuardarPortafolio() {
		rebootMsg();
		
		if(FecEntrada != null) {
			proyectoGetter.getPortafolioEmp().setEntrada(conversiones.DateToString(FecEntrada));
		}else {
			proyectoGetter.getPortafolioEmp().setEntrada(null);
		}
		
		if(FecSalida != null) {
			proyectoGetter.getPortafolioEmp().setSalida(conversiones.DateToString(FecSalida));
		}else {
			proyectoGetter.getPortafolioEmp().setSalida(null);
		}
		
		if(fechasNoVacias()) {
			
			if(folioNoVacio()) {
				if(proyectoGetter.getPortafolioEmp().getIdSeguimiento() != 0) {
					
					if(proyectoService.ActualizaPortafolioEmpresarial(proyectoGetter) == 1) {
						msgSuccess = Mensajes.ACTUALIZA_PORTAFOLIO_EXITO;
						bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.ACTUALIZA_PORTAFOLIO, "Actualización de portafolio", "El usuario ha actualizado el portafolio empresarial", proyectoGetter.getIdProyecto());
					}else {
						msgError = Mensajes.ERROR_ACTUALIZA_PORTAFOLIO;
					}
					
				}else {
					
					if(proyectoService.AsignaPortafolioEmpresarial(proyectoGetter) == 1) {
						msgSuccess = Mensajes.REGISTRO_PROYECTO_EXITOSO;
						bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.REGISTRO_PORTAFOLIO, "Alta de portafolio", "Registro de portafolio empresarial", proyectoGetter.getIdProyecto());
					}else {
						msgError = Mensajes.ERROR_OCURRIDO;
					}
					
				}
			}else {
				msgError = Mensajes.ERROR_FOLIO_VACIO;
			}			
			
		}else {
			msgError = Mensajes.ERROR_FECHAS_VACIAS;
		}
			
		
	}
	
	public boolean fechasNoVacias() {
		boolean noVacias = true;
		
		if(proyectoGetter.getPortafolioEmp().getAplica().equals(SI)) {
			if(FecEntrada == null || FecSalida == null) {
				noVacias = false;
			}
		}
		
		return noVacias;
	}
	
	public boolean folioNoVacio() {
		boolean noVacias = true;
		
		if(proyectoGetter.getPortafolioEmp().getAplica().equals(SI)) {
			if(proyectoGetter.getPortafolioEmp().getFolio().isEmpty() || proyectoGetter.getPortafolioEmp().getFolio() == "") {
				noVacias = false;
			}
		}
		
		return noVacias;
	}
	
	public void verificaFolioExistente() {rebootMsg();
		listFoliosRegistrados = proyectoService.foliosRegistrados(proyectoGetter.getPortafolioEmp().getFolio());
		numFoliosExistente = listFoliosRegistrados.size();
		
		if(numFoliosExistente != 0) {
			msgWarnningFolio = Mensajes.ERROR_FOLIO_REPETIDO + numFoliosExistente + Mensajes.PORYECTOS;
			 if(numFoliosExistente > 1) {
				 msgWarnningFolio = msgWarnningFolio + "s";
			 }
			 msgWarnning = Mensajes.PROYECTOS_FOLIO_REGISTRADOS;			
		}else {
			msgWarnningFolio = "";
		}
	}
	
	public void rebootMsg() {
		msgError 	= "";
		msgSuccess 	= "";
		msgWarnning = "";
	}
	
	public void inicializa() {
		conversiones 	= new ConversionesImpl();
		proyectoService = new ProyectoServiceImpl();		
		proyectoGetter 	= (ProyectoDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("proyectoAdd");
		
		if(proyectoGetter.getPortafolioEmp().getIdSeguimiento() != 0) {
			
			if(proyectoGetter.getPortafolioEmp().getEntrada() != null) {
				FecEntrada 	= conversiones.StringToDate(proyectoGetter.getPortafolioEmp().getEntrada());
			}
			
			if(proyectoGetter.getPortafolioEmp().getSalida() != null) {
				FecSalida 	= conversiones.StringToDate(proyectoGetter.getPortafolioEmp().getSalida());
			}
			
		}
	}
	
	public void RegresaToTableProyects() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(PAGINA_PROYECTOS);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de actualización del proyecto: " + e);
		}
	}

	public ProyectoDTO getProyectoGetter() {
		return proyectoGetter;
	}

	public void setProyectoGetter(ProyectoDTO proyectoGetter) {
		this.proyectoGetter = proyectoGetter;
	}

	public Date getFecEntrada() {
		return FecEntrada;
	}

	public void setFecEntrada(Date fecEntrada) {
		FecEntrada = fecEntrada;
	}

	public Date getFecSalida() {
		return FecSalida;
	}

	public void setFecSalida(Date fecSalida) {
		FecSalida = fecSalida;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgSuccess() {
		return msgSuccess;
	}

	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
	}

	public int getNumFoliosExistente() {
		return numFoliosExistente;
	}

	public void setNumFoliosExistente(int numFoliosExistente) {
		this.numFoliosExistente = numFoliosExistente;
	}

	public String getMsgWarnningFolio() {
		return msgWarnningFolio;
	}

	public void setMsgWarnningFolio(String msgWarnningFolio) {
		this.msgWarnningFolio = msgWarnningFolio;
	}

	public String getMsgWarnning() {
		return msgWarnning;
	}

	public void setMsgWarnning(String msgWarnning) {
		this.msgWarnning = msgWarnning;
	}

	public List<FoliosRegistradosDTO> getListFoliosRegistrados() {
		return listFoliosRegistrados;
	}
}
