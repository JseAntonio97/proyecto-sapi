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


@ManagedBean (name = "infraestructuraMB")
@ViewScoped
public class InfrestructuraMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 484336464261593147L;
	static final Logger LOG = Logger.getLogger(InfrestructuraMB.class);
	static final String PAGINA_INFRESTRUCTURA = "add-Infrestructura.html";
	
	SeguimientoService seguimientoService;
	
	private List<ProyectoDTO> 	listProyectosAsignados;
	private ProyectoDTO			proyectoSelected;
	
	UsuarioDTO usuarioDTO;

	private String busNomProyect;
	private String busIntegrador;
	private String busPortEmpr;
	private String busDireccionIp;
	private String busHostName;
	
	private String msgWarning;
	
	@PostConstruct
	public void init() {

		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		
		seguimientoService = new SeguimientoServiceImpl();
		listProyectosAsignados = seguimientoService.FindAllProyects(usuarioDTO.getIdUsuario());
		
		resetMsg();
		iniciaParametros();
	}
	
	public void seguimientoProyecto() {
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proyectoAddInfra", proyectoSelected);
		FacesContext contex = FacesContext.getCurrentInstance();
		try {
			contex.getExternalContext().redirect(PAGINA_INFRESTRUCTURA);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de actualización del proyecto: " + e);
		}
 	}
	
	public void searchProyectosByParameters() {
		resetMsg();
		listProyectosAsignados = seguimientoService.BuscaProyectosByParametrosInfra(busNomProyect, busIntegrador, busPortEmpr, busDireccionIp, busHostName, usuarioDTO.getIdUsuario());
		
		if(listProyectosAsignados.size() == 0) {
			msgWarning = Mensajes.SIN_RESULTADOS_WARNING;
		}
	}
	
	public void resetMsg() {
		msgWarning = "";
	}
	
	public void iniciaParametros() {
		busNomProyect	= "";
		busIntegrador	= "";
		busPortEmpr		= "";
		busDireccionIp	= "";
		busHostName		= "";
	}

	public List<ProyectoDTO> getListProyectosAsignados() {
		return listProyectosAsignados;
	}

	public void setListProyectosAsignados(List<ProyectoDTO> listProyectosAsignados) {
		this.listProyectosAsignados = listProyectosAsignados;
	}

	public ProyectoDTO getProyectoSelected() {
		return proyectoSelected;
	}

	public void setProyectoSelected(ProyectoDTO proyectoSelected) {
		this.proyectoSelected = proyectoSelected;
	}

	public String getBusNomProyect() {
		return busNomProyect;
	}

	public void setBusNomProyect(String busNomProyect) {
		this.busNomProyect = busNomProyect;
	}

	public String getBusIntegrador() {
		return busIntegrador;
	}

	public void setBusIntegrador(String busIntegrador) {
		this.busIntegrador = busIntegrador;
	}

	public String getBusPortEmpr() {
		return busPortEmpr;
	}

	public void setBusPortEmpr(String busPortEmpr) {
		this.busPortEmpr = busPortEmpr;
	}

	public String getBusDireccionIp() {
		return busDireccionIp;
	}

	public void setBusDireccionIp(String busDireccionIp) {
		this.busDireccionIp = busDireccionIp;
	}

	public String getBusHostName() {
		return busHostName;
	}

	public void setBusHostName(String busHostName) {
		this.busHostName = busHostName;
	}

	public String getMsgWarning() {
		return msgWarning;
	}

	public void setMsgWarning(String msgWarning) {
		this.msgWarning = msgWarning;
	}
	
}
