package com.telcel.sapi.mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Actividades;
import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.dao.impl.BitacoraDAOImpl;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.PortEmpService;
import com.telcel.sapi.service.impl.PortEmpServiceImpl;

@ManagedBean (name = "portaEmpMB")
@ViewScoped
public class PortaEmpMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6218158191539845265L;
	static final Logger LOG = Logger.getLogger(PortaEmpMB.class);
	static final String PAGINA_ADD_PORTAFOLIO = "add-portafolio.html";
	static final int 	PORC_AVANCE_PORTAFOLIO_TERMINADO		= 15;
	
	PortEmpService 		portaEmprService;
	BitacoraDAO 		bitacoraService;
	
	private List<ProyectoDTO> listProyectos;
	private List<String> 	  listNomProyectos;
	private ProyectoDTO 	  proyectoSelected;
	private String			  nombreProyecto;
	private String			  msgSuccess;
	private String			  msgError;
	
	UsuarioDTO 				  usuarioDTO;

	@PostConstruct
	private void init() {
		/**
		 * Valida el rol de acceso
		 */
		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		
		bitacoraService = new BitacoraDAOImpl();
		
		portaEmprService 	= new PortEmpServiceImpl();
		listProyectos 		= portaEmprService.CargaProyectos();
		proyectoSelected	= new ProyectoDTO();
		nombreProyecto		= "";
		listNomProyectos	= portaEmprService.CargaNameProyectos();
		msgSuccess	= "";
		msgError	= "";
	}
	
	public void proyectoAddPortafolio() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proyectoAdd", proyectoSelected);
		FacesContext contex = FacesContext.getCurrentInstance();
		try {
			contex.getExternalContext().redirect(PAGINA_ADD_PORTAFOLIO);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de actualización del proyecto: " + e);
		}
 	}
	
	public  List<String> completeText(String query){
		String queryLowerCase = query.toLowerCase();
		return listNomProyectos.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
	}
	
	public void obtieneProyectoByName() {
		listProyectos.clear();
		
		if(nombreProyecto.isEmpty() || nombreProyecto.equals("")) {
			listProyectos 	= portaEmprService.CargaProyectos();
		}else {
			listProyectos	= portaEmprService.CargaProyectos(nombreProyecto);
		}
	}
	
	public void liberarProyecto() {
		msgSuccess	= "";
		msgError	= "";
		
		proyectoSelected.setPorcAvanceActual(PORC_AVANCE_PORTAFOLIO_TERMINADO);
		if(portaEmprService.LiberaProyecto(proyectoSelected) == 1) {
			msgSuccess	= Mensajes.PROYECTO_LIBERADO_PE;
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.LIBERA_PROYECTO , "Liberación de proyecto " + proyectoSelected.getNombreProyecto(), "Portafolio Empresarial", proyectoSelected.getIdProyecto());
		}else {
			msgError	= Mensajes.ERROR_LIBERAR_PROYECTO_PE;
		}
		listProyectos 	= portaEmprService.CargaProyectos();
		listNomProyectos	= portaEmprService.CargaNameProyectos();
	}
	
	public void printProyect() {
		LOG.info(proyectoSelected.toString());
	}

	public List<ProyectoDTO> getListProyectos() {
		return listProyectos;
	}

	public void setListProyectos(List<ProyectoDTO> listProyectos) {
		this.listProyectos = listProyectos;
	}

	public ProyectoDTO getProyectoSelected() {
		return proyectoSelected;
	}

	public void setProyectoSelected(ProyectoDTO proyectoSelected) {
		this.proyectoSelected = proyectoSelected;
	}

	public List<String> getListNomProyectos() {
		return listNomProyectos;
	}

	public void setListNomProyectos(List<String> listNomProyectos) {
		this.listNomProyectos = listNomProyectos;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public String getMsgSuccess() {
		return msgSuccess;
	}

	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
}
