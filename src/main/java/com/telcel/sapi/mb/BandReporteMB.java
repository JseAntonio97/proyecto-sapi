package com.telcel.sapi.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.model.charts.bar.BarChartModel;

import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.IncidenteDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ProyectoF60DTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.IncidentesService;
import com.telcel.sapi.service.PrefijosService;
import com.telcel.sapi.service.ProyectoSeguimientoService;
import com.telcel.sapi.service.ReportsService;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;
import com.telcel.sapi.service.impl.IncidentesServiceImpl;
import com.telcel.sapi.service.impl.PrefijosServiceImpl;
import com.telcel.sapi.service.impl.ProyectoSeguimientoServiceImpl;
import com.telcel.sapi.service.impl.ReportsServiceImpl;

@ManagedBean (name = "bandReporteMB")
@ViewScoped
public class BandReporteMB implements Serializable{

	/**
	 * Desarrollado por Ing. Christian Brandon Neri Sanchez 
	 * 28-02-2022
	 */
	private static final long serialVersionUID = 476114727946665735L;
	static final Logger LOG = Logger.getLogger(BandReporteMB.class);
	
	PrefijosService				prefijosService;
	ReportsService 				reportsService;
	CatalogosService 			catalogosService;
	IncidentesService			incidentesService;
	ProyectoSeguimientoService 	seguimientoService;
	CatalogosService			catalogos;
	
	private BarChartModel stackedBarModel;

	private List<ProyectoDTO> 	listProjects;
	private List<EstrategiaDTO> listEstrategias;
	private List<IncidenteDTO>	listIncidentes;
	private List<AmbienteDTO>	listAmbientes;
	private List<UsuarioDTO>	listUsuariosAsignados;
	
	private String criterioBusqueda;
	private String busquedaParametro;
	
	private ProyectoDTO	proyectoSelected;
	
	private String msgError = "";

	private int countProyect;
	private int numAmbientes;
	private int numFolios;
	
	@PostConstruct
	public void init() {
		proyectoSelected	= new ProyectoDTO();
		ProyectoF60DTO proyecF60 = new ProyectoF60DTO(0, "", "", "", false, "", false, "", new JefeF60DTO(0, "", ""), new TipoF60DTO(0, "", ""));
		proyectoSelected.setProyectoF60(proyecF60);
		
		prefijosService		= new PrefijosServiceImpl();
		reportsService 		= new ReportsServiceImpl();
		catalogosService 	= new CatalogosServiceImpl();
		incidentesService	= new IncidentesServiceImpl();
		seguimientoService	= new ProyectoSeguimientoServiceImpl();
		catalogos			= new CatalogosServiceImpl();
		
		listProjects 		= new ArrayList<ProyectoDTO>();
		listProjects 		= reportsService.cargaProyectosReporte();
		
		listEstrategias 	= new ArrayList<EstrategiaDTO>();
		listEstrategias 	= catalogosService.FindEstrategiasInAc();
		
		listUsuariosAsignados = new ArrayList<UsuarioDTO>();
		listUsuariosAsignados = catalogosService.ConsultaUsuariosResponsables();

		listIncidentes 		= new ArrayList<IncidenteDTO>();
		listAmbientes		= new ArrayList<AmbienteDTO>();
		
		stackedBarModel 	= new BarChartModel();
		stackedBarModel 	= reportsService.graficaFasesProyectos(listProjects);
		
		criterioBusqueda 	= "";
		busquedaParametro	= "";

		ConteaCatidadesResultados();
	} 
	
	public void imprimeIdResponsable() {
		LOG.info(busquedaParametro);
	}
	
	public void buscaProyectos() {
		reinicisaMsg();
		
		if(criterioBusqueda.equals("all")) {
			busquedaParametro	= "";
			listProjects = reportsService.cargaProyectosReporte();
			stackedBarModel = reportsService.graficaFasesProyectos(listProjects);
		}else if(criterioBusqueda.isEmpty()) {
			
			msgError = Mensajes.CRITERIO_BUSQUEDA_VACIO;
			
		}else {
			
			if(criterioBusqueda.equals("estrategia")) {
				if(busquedaParametro.equals("0")) {
					msgError = Mensajes.ESTRATEGIA_BUSQUEDA_VACIO;
				}else {
					cargaDatosDeBusqueda();
				}
			}else if(criterioBusqueda.equals("responsable")) {
				
				if(busquedaParametro.equals("0")) {
					msgError = Mensajes.RESPONSABLE_BUSQUEDA_VACIO;
				}else {
					cargaDatosDeBusqueda();
				}
				
			}else if(criterioBusqueda.equals("etapa")) {
				if(busquedaParametro.equals("0")) {
					msgError = Mensajes.ETAPA_BUSQUEDA_VACIO;
				}else {
					cargaDatosDeBusqueda();
				}
			}else {
				cargaDatosDeBusqueda();
			}
		}
		ConteaCatidadesResultados();
	}
	
	public void cargaDatosDeBusqueda() {
		listProjects.clear();
		listProjects 	= reportsService.BuscaProyectosReporte(criterioBusqueda, busquedaParametro);
		stackedBarModel = reportsService.graficaFasesProyectos(listProjects);
	}
	
	public void cargaIncidentes() {
		listIncidentes.clear();
		listIncidentes = incidentesService.CargaIncidentes(proyectoSelected.getIdProyecto());
	}
	
	public void cargaSeguimiento() {
		listAmbientes		= proyectoSelected.getListAmbientes();
		proyectoSelected 	= seguimientoService.CargaProyectoSelectAllData(proyectoSelected.getIdProyecto());
		if(listAmbientes != null) {
			proyectoSelected.setListAmbientes(listAmbientes);
		}
	}
	
	public void generaReporteExcel() {
		reportsService.generaReporteExcel(listProjects, prefijosService.NombreReporteXlsx());
	}
	
	public void generaReporteExcelAllData() {
		if(criterioBusqueda.equals("all") || criterioBusqueda.equals("")) {
			reportsService.generaReporteAllDataExcel(
					reportsService.cargaProyectosAllData(), prefijosService.NombreReporteXlsx());
		}else{
			reportsService.generaReporteAllDataExcel(
					reportsService.cargaProyectosAllDataByCriterio(criterioBusqueda, busquedaParametro), 
					prefijosService.NombreReporteXlsx()
					);
		}
	}
	
	public void reinicisaMsg() {
		msgError 	= "";
	}
	
	public void ConteaCatidadesResultados() {
		countProyect = 0;
		numAmbientes = 0;
		numFolios	 = 0;
		
		countProyect 	= listProjects.size();
		numAmbientes 	= reportsService.CalculaNumeroAmbientes(listProjects);
		numFolios		= reportsService.CalculaNumeroFolios(listProjects);
	}
	
	public void limpiaParametroBusqueda() {
		if(criterioBusqueda.equals("estrategia") || criterioBusqueda.equals("responsable")) {
			busquedaParametro	= "0";
		}else {
			busquedaParametro	= "";
		}
	}
	
	
	/**
	 * Graficas de proyectos generales por etapas
	 * Desarrollador Jose Antonio Jimenez
	 * @return
	 * 
	 */
	
	
	
	
	//------
	public List<ProyectoDTO> getListProjects() {
		return listProjects;
	}

	public void setListProjects(List<ProyectoDTO> listProjects) {
		this.listProjects = listProjects;
	}

	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public String getBusquedaParametro() {
		return busquedaParametro;
	}

	public void setBusquedaParametro(String busquedaParametro) {
		this.busquedaParametro = busquedaParametro;
	}

	public List<EstrategiaDTO> getListEstrategias() {
		return listEstrategias;
	}

	public void setListEstrategias(List<EstrategiaDTO> listEstrategias) {
		this.listEstrategias = listEstrategias;
	}

	public ProyectoDTO getProyectoSelected() {
		return proyectoSelected;
	}

	public void setProyectoSelected(ProyectoDTO proyectoSelected) {
		this.proyectoSelected = proyectoSelected;
	}

	public List<IncidenteDTO> getListIncidentes() {
		return listIncidentes;
	}

	public void setListIncidentes(List<IncidenteDTO> listIncidentes) {
		this.listIncidentes = listIncidentes;
	}

	public BarChartModel getStackedBarModel() {
		return stackedBarModel;
	}

	public void setStackedBarModel(BarChartModel stackedBarModel) {
		this.stackedBarModel = stackedBarModel;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public int getCountProyect() {
		return countProyect;
	}

	public void setCountProyect(int countProyect) {
		this.countProyect = countProyect;
	}

	public int getNumAmbientes() {
		return numAmbientes;
	}

	public void setNumAmbientes(int numAmbientes) {
		this.numAmbientes = numAmbientes;
	}

	public int getNumFolios() {
		return numFolios;
	}

	public void setNumFolios(int numFolios) {
		this.numFolios = numFolios;
	}

	public List<UsuarioDTO> getListUsuariosAsignados() {
		return listUsuariosAsignados;
	}

	public void setListUsuariosAsignados(List<UsuarioDTO> listUsuariosAsignados) {
		this.listUsuariosAsignados = listUsuariosAsignados;
	}
}
