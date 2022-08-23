package com.telcel.sapi.mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.SeguimientoService;
import com.telcel.sapi.service.impl.SeguimientoServiceImpl;

@ManagedBean (name = "superFollowMB")
@ViewScoped
public class SuperFollowMB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID 	= -7051486251605199373L;
	static final String PAGINA_SEGUIMIENTO 		= "seguimiento-super-user.html";
	static final Logger LOG 					= Logger.getLogger(SuperFollowMB.class);
	
	SeguimientoService 	seguimientoService;
	UsuarioDTO 			usuarioDTO;
	
	private List<ProyectoDTO> 	listProyectosAsignados;
	private ProyectoDTO			proyectoSelected;
	
	private String busNomProyecto;
	private String busIntegrador;
	private String busPorEmpresarial;
	private String busPorHostName;
	private String busPorIP;
	
	private String msgSuccess;
	private String msgWarning;

	@PostConstruct
	public void init() {

		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		
		msgSuccess = "";
		msgSuccess = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mensajeEliminacion");
		
		if(msgSuccess == null) {
			msgSuccess = new String("");
		}
		
		seguimientoService = new SeguimientoServiceImpl();
		listProyectosAsignados = seguimientoService.FindAllProyectsAsigned();
		iniciaMsg();
		iniciaParametros();
	}
	
	public void seguimientoProyecto() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proyectoSeguimiento", proyectoSelected);
		FacesContext contex = FacesContext.getCurrentInstance();
		try {
			contex.getExternalContext().redirect(PAGINA_SEGUIMIENTO);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de actualización del proyecto: " + e);
		}
 	}
	
	public void searchProyectosByParameters() {
		iniciaMsg();
		listProyectosAsignados = seguimientoService.BuscaAllAsignedProyectosByParametros(busNomProyecto, busIntegrador, busPorEmpresarial, busPorIP, busPorHostName);
		
		if(listProyectosAsignados.size() == 0) {
			msgWarning = Mensajes.SIN_RESULTADOS_WARNING;
		}
	}
	
	public void iniciaMsg() {
		msgWarning	= "";
	}
	
	public void iniciaParametros() {
		busNomProyecto	= "";
		busIntegrador	= "";
		busPorEmpresarial	= "";
	}

	public List<ProyectoDTO> getListProyectosAsignados() {
		return listProyectosAsignados;
	}

	public void setListProyectosAsignados(List<ProyectoDTO> listProyectosAsignados) {
		this.listProyectosAsignados = listProyectosAsignados;
	}

	public String getBusNomProyecto() {
		return busNomProyecto;
	}

	public void setBusNomProyecto(String busNomProyecto) {
		this.busNomProyecto = busNomProyecto;
	}

	public String getBusIntegrador() {
		return busIntegrador;
	}

	public void setBusIntegrador(String busIntegrador) {
		this.busIntegrador = busIntegrador;
	}

	public String getBusPorEmpresarial() {
		return busPorEmpresarial;
	}

	public void setBusPorEmpresarial(String busPorEmpresarial) {
		this.busPorEmpresarial = busPorEmpresarial;
	}

	public String getMsgWarning() {
		return msgWarning;
	}

	public void setMsgWarning(String msgWarning) {
		this.msgWarning = msgWarning;
	}

	public String getBusPorHostName() {
		return busPorHostName;
	}

	public void setBusPorHostName(String busPorHostName) {
		this.busPorHostName = busPorHostName;
	}

	public String getBusPorIP() {
		return busPorIP;
	}

	public void setBusPorIP(String busPorIP) {
		this.busPorIP = busPorIP;
	}

	public ProyectoDTO getProyectoSelected() {
		return proyectoSelected;
	}

	public void setProyectoSelected(ProyectoDTO proyectoSelected) {
		this.proyectoSelected = proyectoSelected;
	}

	public String getMsgSuccess() {
		return msgSuccess;
	}

	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
	}
}
