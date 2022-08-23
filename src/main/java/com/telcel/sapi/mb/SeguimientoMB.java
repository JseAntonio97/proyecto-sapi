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

@ManagedBean (name = "seguimientoMB")
@ViewScoped
public class SeguimientoMB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2320248980018800510L;
	static final Logger LOG = Logger.getLogger(SeguimientoMB.class);
	static final String PAGINA_SEGUIMIENTO = "proyecto-seguimiento.html";
	
	SeguimientoService seguimientoService;
	
	private List<ProyectoDTO> 	listProyectosAsignados;
	private ProyectoDTO			proyectoSelected;
	
	UsuarioDTO usuarioDTO;
	
	private String busNomProyecto;
	private String busIntegrador;
	private String busPorEmpresarial;
	
	private String msgWarning;
	
	@PostConstruct
	public void init() {

		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		
		seguimientoService = new SeguimientoServiceImpl();
		listProyectosAsignados = seguimientoService.FindAllProyects(usuarioDTO.getIdUsuario());
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
	
	public void buscarProyectosByParametros() {
		iniciaMsg();
		listProyectosAsignados = seguimientoService.BuscaProyectosByParametros(busNomProyecto, busIntegrador, busPorEmpresarial, usuarioDTO.getIdUsuario());

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

	public ProyectoDTO getProyectoSelected() {
		return proyectoSelected;
	}

	public void setProyectoSelected(ProyectoDTO proyectoSelected) {
		this.proyectoSelected = proyectoSelected;
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
	
}
