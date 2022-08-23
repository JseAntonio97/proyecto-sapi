package com.telcel.sapi.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.model.charts.bar.BarChartModel;

import com.telcel.sapi.constantes.Actividades;
import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.dao.impl.BitacoraDAOImpl;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.AsignacionService;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.impl.AsignacionServiceImpl;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;

@ManagedBean (name = "asignacionMB")
@ViewScoped
public class AsignacionMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4503316694053321178L;

	static final Logger LOG = Logger.getLogger(AsignacionMB.class);
	
	AsignacionService asignacionService;
	CatalogosService 	catalogosService;
	BitacoraDAO 		bitacoraService;
	
	private List<ProyectoDTO> 	listProyectos;
	private List<UsuarioDTO>	listColaboradores;
	private List<EstrategiaDTO>	listEstrategias;
	private UsuarioDTO			usuarioSelected;
	private ProyectoDTO 	  	proyectoSelected;
	private int 				idEstrategia;
	UsuarioDTO	usuarioDTO;
	
	private String msgSuccess;
	private String msgError;
	private String msgWarning;

	private String busNomProyecto;
	private String busIntegrador;
	private String busPorEmpresarial;
	
	private BarChartModel grafica;
	
	@PostConstruct
	public void init() {
		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		
		listProyectos 		= new ArrayList<ProyectoDTO>();
		listColaboradores 	= new ArrayList<UsuarioDTO>();
		listEstrategias		= new ArrayList<EstrategiaDTO>();
		asignacionService 	= new AsignacionServiceImpl();
		catalogosService	= new CatalogosServiceImpl();
		bitacoraService		= new BitacoraDAOImpl();
		usuarioSelected		= new UsuarioDTO(); 
		proyectoSelected	= new ProyectoDTO();
		listEstrategias		= catalogosService.FindEstrategiasActivas();
		listProyectos 		= asignacionService.CargaProyectos();
		listColaboradores 	= asignacionService.CargaColaboradores(usuarioDTO);
		
		grafica = new BarChartModel();
		grafica = asignacionService.graficarAsignaciones();
		
		iniciaMsg();
		iniciaParametrosBusqueda();
	}
	
	public void AsignaProyecto() {
		iniciaMsg();
		
		if(asignacionService.AsignaProyectos(usuarioSelected, proyectoSelected, idEstrategia) == 1) {
			asignacionService.MarcaProyectoNoLeido(proyectoSelected);
			
			msgSuccess = Mensajes.PROYECTO_ASIGNADO_EXITO_1 + proyectoSelected.getNombreProyecto() + " " + 
					Mensajes.PROYECTO_ASIGNADO_EXITO_2 + " " + usuarioSelected.getNombre( )+ " " +usuarioSelected.getPrimerApellido() + " " + usuarioSelected.getSegundoApellido();
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.ASIGNACION_NORMAL, 
					"Asignación de proyecto " + proyectoSelected.getNombreProyecto() + " a " + usuarioSelected.getNombre( )+ " " +usuarioSelected.getPrimerApellido() + " " + usuarioSelected.getSegundoApellido(), 
					"Asignación", proyectoSelected.getIdProyecto());
			grafica = asignacionService.graficarAsignaciones();
		}else {
			msgError   = Mensajes.PROYECTO_ASIGNADO_ERROR;
		}
		idEstrategia = 0;
		listProyectos 		= asignacionService.CargaProyectos();
	}
	
	public void ReasignarProyecto() {
		iniciaMsg();
		
		if(asignacionService.ReAsignaProyecto(usuarioSelected, proyectoSelected, idEstrategia) == 1) {
			asignacionService.MarcaProyectoNoLeido(proyectoSelected);
			
			msgSuccess = Mensajes.PROYECTO_REASIGNADO_EXITO_1 + proyectoSelected.getNombreProyecto() + " " + 
					Mensajes.PROYECTO_REASIGNADO_EXITO_2 + " " + usuarioSelected.getNombre( )+ " " +usuarioSelected.getPrimerApellido() + " " + usuarioSelected.getSegundoApellido();
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.REASIGNACION, 
					"Reasignó el proyecto " + proyectoSelected.getNombreProyecto() + " a " + usuarioSelected.getNombre( )+ " " +usuarioSelected.getPrimerApellido() + " " + usuarioSelected.getSegundoApellido(), 
					"Asignación", proyectoSelected.getIdProyecto());
			grafica = asignacionService.graficarAsignaciones();
		}else {
			msgError   = Mensajes.PROYECTO_REASIGNADO_ERROR;
		}
		idEstrategia = 0;
		listProyectos 		= asignacionService.CargaProyectos();
	}
	
	public void buscarProyectosByParametros() {
		iniciaMsg();
		listProyectos 		= asignacionService.BusquedaProyectosByParametros(busNomProyecto, busIntegrador, busPorEmpresarial);
		
		if(listProyectos.size() == 0) {
			msgWarning = Mensajes.SIN_RESULTADOS_WARNING;
		}
	}
	
	public void cargaEstrategiaActual() {
		if(proyectoSelected.getEstrategia().getIdEstrategia() != 0) {
			idEstrategia = proyectoSelected.getEstrategia().getIdEstrategia();
		}else {
			idEstrategia = 0;
		}
	}

	public void iniciaParametrosBusqueda() {
		busNomProyecto 	= "";
		busIntegrador	= "";
		busPorEmpresarial	= "";
	}
	
	public void iniciaMsg() {
		msgSuccess 	= "";
		msgError	= "";
		msgWarning	= "";
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

	public List<UsuarioDTO> getListColaboradores() {
		return listColaboradores;
	}

	public void setListColaboradores(List<UsuarioDTO> listColaboradores) {
		this.listColaboradores = listColaboradores;
	}

	public UsuarioDTO getUsuarioSelected() {
		return usuarioSelected;
	}

	public void setUsuarioSelected(UsuarioDTO usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
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

	public List<EstrategiaDTO> getListEstrategias() {
		return listEstrategias;
	}

	public void setListEstrategias(List<EstrategiaDTO> listEstrategias) {
		this.listEstrategias = listEstrategias;
	}

	public int getIdEstrategia() {
		return idEstrategia;
	}

	public void setIdEstrategia(int idEstrategia) {
		this.idEstrategia = idEstrategia;
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

	public BarChartModel getGrafica() {
		return grafica;
	}

	public void setGrafica(BarChartModel grafica) {
		this.grafica = grafica;
	}
}
