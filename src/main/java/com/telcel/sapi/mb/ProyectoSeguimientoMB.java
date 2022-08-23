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
import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.constantes.Fases;
import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.dao.impl.BitacoraDAOImpl;
import com.telcel.sapi.dto.IncidenteDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.PorcAvanceDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ResponsableDTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.Conversiones;
import com.telcel.sapi.service.IncidentesService;
import com.telcel.sapi.service.LoginService;
import com.telcel.sapi.service.ProyectoSeguimientoService;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;
import com.telcel.sapi.service.impl.ConversionesImpl;
import com.telcel.sapi.service.impl.IncidentesServiceImpl;
import com.telcel.sapi.service.impl.LoginServiceImpl;
import com.telcel.sapi.service.impl.ProyectoSeguimientoServiceImpl;

@ManagedBean (name = "proyectoSegMB")
@ViewScoped
public class ProyectoSeguimientoMB implements Serializable{
	/**
	 * Desarrollado por Ing. Christian Brandon Neri Sanchez 
	 * 31-01-2022
	 */
	
	private static final long serialVersionUID = 5537012969425207311L;
	static final Logger LOG = Logger.getLogger(ProyectoSeguimientoMB.class);

	static final String PAGINA_INFRESTRUCTURA = "add-Infrestructura.html";
	
	static final int 	DIAS_NATURALES_F60 				= 21;
	static final int 	DIAS_NATURALES_INFRESTRUCTURA 	= 21;
	static final int 	DIAS_NATURALES_APLICATIVO 		= 21;
	static final int 	DIAS_NATURALES_PRE_ATP 			= 63;
	static final int 	DIAS_NATURALES_ATP 				= 14;
	static final int 	DIAS_NATURALES_RTO 				= 7;	
	
	static final int 	DIAS_HABILES_F60 				= 15;
	static final int 	DIAS_HABILES_INFRESTRUCTURA 	= 15;
	static final int 	DIAS_HABILES_APLICATIVO 		= 15;
	static final int 	DIAS_HABILES_PRE_ATP 			= 45;//60Días para preatp
	static final int 	DIAS_HABILES_ATP 				= 10;
	static final int 	DIAS_HABILES_RTO 				= 5;

//	static final int 	PORC_AVANCE_F60_TERMINADO				= 10;
//	static final int 	PORC_AVANCE_INFRAESTRUCTURA_TERMINADO	= 15;
//	static final int 	PORC_AVANCE_APLICATIVO_TERMINADO		= 10;
//	static final int 	PORC_AVANCE_PRE_ATP_TERMINADO			= 25;
//	static final int 	PORC_AVANCE_ATP_TERMINADO				= 20;
//	static final int 	PORC_AVANCE_RTO_TERMINADO				= 5;
//
//	static final int 	PORC_AVANCE_ACOMULADO_F60				= 25;
//	static final int 	PORC_AVANCE_ACOMULADO_INFRAESTRUCTURA	= 40;
//	static final int 	PORC_AVANCE_ACOMULADO_APLICATIVO		= 50;
//	static final int 	PORC_AVANCE_ACOMULADO_PRE_ATP			= 75;
//	static final int 	PORC_AVANCE_ACOMULADO_ATP				= 95;
//	static final int 	PORC_AVANCE_ACOMULADO_RTO				= 100;
	
	
	Conversiones 				conversiones;
	CatalogosService			catalogos;
	IncidentesService			incidentesService;
	ProyectoSeguimientoService 	seguimientoService;
	BitacoraDAO 				bitacoraService;
	LoginService 				loginService;
	
	UsuarioDTO 					usuarioDTO;

	private List<IncidenteDTO> 		listIncidentes;
	private List<ResponsableDTO> 	listResponsables;
	private List<JefeF60DTO> 		listJefesF60;
	private List<TipoF60DTO> 		listTiposF60;
		
	private ProyectoDTO 	proyectoSeguimiento;
	private IncidenteDTO	incidenteRegistro;
	private IncidenteDTO	incidente;

	private Date fechaInicioF60;
	private Date fechaFinF60;
	
	private Date fechaKickOffInfra;	
	private Date fechaInicioInfra;
	private Date fechaFinInfra;

	private Date fechaDespliegue;
	private Date fechaFinPruebas;
	private Date fechaNotificacion;

	private Date fechaInicioPreAtp;
	private Date fechaFinPreAtp;

	private Date fechaInicioAtp;
	private Date fechaFinAtp;

	private Date fechaInicioRto;
	private Date fechaFinRto;

	private Date fechaInicioIncidente;
	private Date fechaFinIncidente;
	
	private Date fechaIniIssue;
	private Date fechaFinIssue;
	
	private String msgSuccess;
	private String msgError;

	PorcAvanceDTO porcentajes;
	
	@PostConstruct
	public void init() {
		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");

		loginService		= new LoginServiceImpl();
		loginService.ValidaSesion(usuarioDTO);
		
		proyectoSeguimiento = new ProyectoDTO();
		proyectoSeguimiento = (ProyectoDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("proyectoSeguimiento");
		seguimientoService 	= new ProyectoSeguimientoServiceImpl();
		incidentesService	= new IncidentesServiceImpl();
		bitacoraService		= new BitacoraDAOImpl();
		conversiones		= new ConversionesImpl();
		catalogos			= new CatalogosServiceImpl();
		incidenteRegistro	= new IncidenteDTO();
		incidente			= new IncidenteDTO();
		listIncidentes 		= new ArrayList<IncidenteDTO>();
		listResponsables	= new ArrayList<ResponsableDTO>();
		
		incidenteRegistro.setResponsable(new ResponsableDTO());
		
		listResponsables	= catalogos.CargaResponsablesActivos();
		proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
		
		/**
		 * Marca el proyecto como leído si no ha sido leido
		 */
		if(proyectoSeguimiento.getMarcaLeido() == 0) {
			seguimientoService.MarcaProyectoLeido(proyectoSeguimiento);
		}
		
		porcentajes = new PorcAvanceDTO();
		porcentajes = catalogos.CargaPorcentajesAvance();
				
		inicializaMsg();
		cargaFechasF60();
		cargaFechasInfra();
		cargaFechasAplicativo();
		cargaFechasPreAtp();
		cargaFechasAtp();
		cargaFechasRTO();
		CargaIncidentes();
		cargaCatalogos();
	}
	
	public void cargaCatalogos() {
		listJefesF60 = new ArrayList<JefeF60DTO>();
		listTiposF60 = new ArrayList<TipoF60DTO>();
		
		listJefesF60 = catalogos.catalogoJefesF60();
		listTiposF60 = catalogos.catalogoTiposF60();
	}
	
	public void calculaPorcentajeAvance(String faseConcluida) {
		
		switch(faseConcluida) {
			case "F60":
				proyectoSeguimiento.setPorcAvanceActual(porcentajes.getPorcF60());
				break;
			case "INFRAESTRUCTURA":
				proyectoSeguimiento.setPorcAvanceActual(porcentajes.getPorcInfra());
				break;
			case "APLICATIVO":
				proyectoSeguimiento.setPorcAvanceActual(porcentajes.getPorcAplicativo());
				break;
			case "PREATP":
				proyectoSeguimiento.setPorcAvanceActual(porcentajes.getPorcPreATP());
				break;
			case "ATP":
				proyectoSeguimiento.setPorcAvanceActual(porcentajes.getPorcATP());
				break;
			case "RTO":
				proyectoSeguimiento.setPorcAvanceActual(porcentajes.getPorcRTO());
				break;
		}
		
	}
	
	//Redireccionamiento para Agregar infraestructura
	public void addInfraestructura() {
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proyectoAddInfra", proyectoSeguimiento);
		FacesContext contex = FacesContext.getCurrentInstance();
		try {
			contex.getExternalContext().redirect(PAGINA_INFRESTRUCTURA);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de adición de la Infraestructura del proyecto: " + e);
		}
 	}
	
	//Funciones para F60 [Inicio]
	public void cargaFechasF60() {		
		if(proyectoSeguimiento.getProyectoF60().getFechaInicio() != null) {
			fechaInicioF60 	= conversiones.StringToDate(proyectoSeguimiento.getProyectoF60().getFechaInicio());
		}
		
		if(proyectoSeguimiento.getProyectoF60().getFechaFin() != null) {
			fechaFinF60		= conversiones.StringToDate(proyectoSeguimiento.getProyectoF60().getFechaFin());
		}
	}
	
	public void GuardaF60() {
		inicializaMsg();
		
		if(proyectoSeguimiento.getProyectoF60().getResponsable().isEmpty() || 
				proyectoSeguimiento.getProyectoF60().getJefeF60().getIdJefeF60() == 0 || 
				proyectoSeguimiento.getProyectoF60().getTipoF60().getIdTipoF60() == 0 ) {
			msgError = Mensajes.PROYECTO_F60_DATOS_FALTANTES;
		}else {
			proyectoSeguimiento.getProyectoF60().setFechaFin(conversiones.DateToString(fechaFinF60));
			
			if(proyectoSeguimiento.getProyectoF60().getFechaFin() == "") {
				proyectoSeguimiento.getProyectoF60().setFechaFin(null);
			}
			
			if(proyectoSeguimiento.getProyectoF60().getIdProyectoF60() == 0) {
				
				if(seguimientoService.GuardaF60(proyectoSeguimiento) == 1) {
					msgSuccess = Mensajes.PROYECTO_F60_GUARDADO;
					seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.F60.toString(), 1);
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_F60, "Registro de F60", "Seguimiento", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_F60_ERROR_GUARDADO;
				}
				
			}else if(proyectoSeguimiento.getProyectoF60().getIdProyectoF60() != 0) {
				if(seguimientoService.UpdateF60(proyectoSeguimiento) == 1) {
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					msgSuccess = Mensajes.PROYECTO_F60_GUARDADO;

					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_F60, "Guardado de F60", "Seguimiento", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_F60_ERROR_GUARDADO;
				}
			}
		}
	}
	
	public void calculaFechaCompromisoF60() {		
		if(fechaInicioF60 != null) {
			proyectoSeguimiento.getProyectoF60().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioF60, DIAS_HABILES_F60));
//			proyectoSeguimiento.getProyectoF60().setFechaCompromiso(conversiones.AdicionDiasCompromiso(fechaInicioF60, DIAS_NATURALES_F60));
			proyectoSeguimiento.getProyectoF60().setFechaInicio(conversiones.DateToString(fechaInicioF60));
		}else {
			proyectoSeguimiento.getProyectoF60().setFechaCompromiso(null);
		}
	}
	
	public void TerminaF60() {
		inicializaMsg();
		if((fechaInicioF60 != null && fechaFinF60 != null) || proyectoSeguimiento.getProyectoF60().isSinFechas()) {
			
			GuardaF60();
			calculaPorcentajeAvance(Fases.F60.toString());
			if(seguimientoService.UpdateTerminaF60(proyectoSeguimiento) == 1) {
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), (proyectoSeguimiento.getProyectoF60().isCuentaConInfraestructura()) ? Fases.APLICATIVO.toString(): Fases.INFRAESTRUCTURA.toString(), 2);	
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_F60_TERMINADO;		
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.TERMINAR_F60, "Terminado de F60", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_F60_ERROR_TERMINADO;
			}
			
		}else {
			msgError = Mensajes.PROYECTO_F60_SIN_FECHAS;
		}
	}
	//Funciones para F60 [Fin]
	
	//Funciones para Infraestructura [Inicio]
	
	public void cargaFechasInfra() {		
		if(proyectoSeguimiento.getSeguimientoInfra().getKickoff() != null) {
			fechaKickOffInfra = conversiones.StringToDate(proyectoSeguimiento.getSeguimientoInfra().getKickoff());
		}
		
		if(proyectoSeguimiento.getSeguimientoInfra().getFechaEnvio() != null) {
			fechaInicioInfra = conversiones.StringToDate(proyectoSeguimiento.getSeguimientoInfra().getFechaEnvio());
		}
		
		if(proyectoSeguimiento.getSeguimientoInfra().getFechaEntregaUsuario() != null) {
			fechaFinInfra = conversiones.StringToDate(proyectoSeguimiento.getSeguimientoInfra().getFechaEntregaUsuario());
		}
	}
	
	public void calculaFechaCompromisoInfra() {		
		if(fechaInicioInfra != null) {
			proyectoSeguimiento.getSeguimientoInfra().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioInfra, DIAS_HABILES_INFRESTRUCTURA));
//			proyectoSeguimiento.getSeguimientoInfra().setFechaCompromiso(conversiones.AdicionDiasCompromiso(fechaInicioInfra, DIAS_NATURALES_INFRESTRUCTURA));
		}else {
			proyectoSeguimiento.getSeguimientoInfra().setFechaCompromiso(null);
		}
	}
	
	public void GuardaInfraestructura() {
		inicializaMsg();
		
//		if(fechaKickOffInfra == null) {
//			msgError = Mensajes.INFRA_ERROR_KICKOFF_VACIO;
//		}else {
			
			proyectoSeguimiento.getSeguimientoInfra().setFechaEnvio(conversiones.DateToString(fechaInicioInfra));
			proyectoSeguimiento.getSeguimientoInfra().setKickoff(conversiones.DateToString(fechaKickOffInfra));
			proyectoSeguimiento.getSeguimientoInfra().setFechaEntregaUsuario(conversiones.DateToString(fechaFinInfra));

			if(proyectoSeguimiento.getSeguimientoInfra().getKickoff() == "") {
				proyectoSeguimiento.getSeguimientoInfra().setKickoff(null);
			}
			if(proyectoSeguimiento.getSeguimientoInfra().getFechaEntregaUsuario() == "") {
				proyectoSeguimiento.getSeguimientoInfra().setFechaEntregaUsuario(null);
			}
			if(proyectoSeguimiento.getSeguimientoInfra().getFechaEnvio() == "") {
				proyectoSeguimiento.getSeguimientoInfra().setFechaEnvio(null);
			}
			if(proyectoSeguimiento.getSeguimientoInfra().getFechaCompromiso() == "") {
				proyectoSeguimiento.getSeguimientoInfra().setFechaCompromiso(null);
			}
			
			if(proyectoSeguimiento.getSeguimientoInfra().getIdSeguimientoInfra() == 0) {
				
				if(seguimientoService.GuardaSegInfra(proyectoSeguimiento) == 1) {
					msgSuccess = Mensajes.PROYECTO_INFRA_GUARDADO;
					seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.INFRAESTRUCTURA.toString(), 1);
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_INFRAESTRUCTURA , "Registro de infraestructura", "Seguimiento", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_INFRA_ERROR_GUARDADO;
				}
				
			}else if(proyectoSeguimiento.getSeguimientoInfra().getIdSeguimientoInfra() != 0) {
				
				if(seguimientoService.UpdateSegInfra(proyectoSeguimiento) == 1) {
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					msgSuccess = Mensajes.PROYECTO_INFRA_MODIFICADO_EXITO;
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.MODIFICAR_INFRAESTRUCTURA, "Modificó los datos de infraestructura", "Seguimiento", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_INFRA_ERROR_GUARDADO;
				}
				
			}
//		}
	}
	
	public void TerminaInfraestructura() {
		inicializaMsg();
		
		if((fechaInicioInfra != null && fechaFinInfra != null) || proyectoSeguimiento.getSeguimientoInfra().isSinFechas()) {
			
			GuardaInfraestructura();
			calculaPorcentajeAvance(Fases.INFRAESTRUCTURA.toString());
			
			if(seguimientoService.UpdateTerminaSegInfra(proyectoSeguimiento) == 1) {
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.APLICATIVO.toString(), 2);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_INFRA_TERMINADO;
				bitacoraService.insertBitacora(
						usuarioDTO.getIdUsuario(), 
						Actividades.TERMINAR_INFRAESTRUCTURA, 
						"Término de infraestructura", 
						"Seguimiento", 
						proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_INFRA_ERROR_TERMINADO;
			}
			
		}else {
			msgError = Mensajes.INFRA_SIN_FECHAS;
		}
	}

	//Funciones para Infraestructura [Fin]

	//Funciones para Aplicativo [Inicio]
	
	public void cargaFechasAplicativo() {
		if(proyectoSeguimiento.getAplicativo().getFechaDespliegue() != null) {
			fechaDespliegue = conversiones.StringToDate(proyectoSeguimiento.getAplicativo().getFechaDespliegue());
		}
		
		if(proyectoSeguimiento.getAplicativo().getFechaFinPruebas() != null) {
			fechaFinPruebas = conversiones.StringToDate(proyectoSeguimiento.getAplicativo().getFechaFinPruebas());
		}
		
		if(proyectoSeguimiento.getAplicativo().getFechaNotificacion() != null) {
			fechaNotificacion = conversiones.StringToDate(proyectoSeguimiento.getAplicativo().getFechaNotificacion());
		}
	}
	
	public void calculaFechaCompromisoAplicativo() {
		if(fechaDespliegue != null) {
			proyectoSeguimiento.getAplicativo().setFechaFinPruebas(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaDespliegue, DIAS_HABILES_APLICATIVO));
//			proyectoSeguimiento.getAplicativo().setFechaFinPruebas(conversiones.AdicionDiasCompromiso(fechaDespliegue, DIAS_NATURALES_APLICATIVO));
		}else {
			proyectoSeguimiento.getAplicativo().setFechaFinPruebas(null);
		}
	}
	
	public void GuardaAplicativo() {
		inicializaMsg();

		proyectoSeguimiento.getAplicativo().setFechaDespliegue(conversiones.DateToString(fechaDespliegue));
		proyectoSeguimiento.getAplicativo().setFechaFinPruebas(conversiones.DateToString(fechaFinPruebas));
		proyectoSeguimiento.getAplicativo().setFechaNotificacion(conversiones.DateToString(fechaNotificacion));
		
		if(proyectoSeguimiento.getAplicativo().getFechaDespliegue() == "") {
			proyectoSeguimiento.getAplicativo().setFechaDespliegue(null);
		}
		if(proyectoSeguimiento.getAplicativo().getFechaFinPruebas() == "") {
			proyectoSeguimiento.getAplicativo().setFechaFinPruebas(null);
		}
		if(proyectoSeguimiento.getAplicativo().getFechaNotificacion() == "") {
			proyectoSeguimiento.getAplicativo().setFechaNotificacion(null);
		}
		
		if(proyectoSeguimiento.getAplicativo().getIdAplicativo() == 0) {
			
			if(seguimientoService.GuardaAplicativo(proyectoSeguimiento) == 1) {
				msgSuccess = Mensajes.PROYECTO_APLICATIVO_GUARDADO;
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.APLICATIVO.toString(), 1);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_APLICATIVO , "Registro de aplicativo", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_APLICATIVO_ERROR_GUARDADO;
			}
			
		}else if(proyectoSeguimiento.getAplicativo().getIdAplicativo() != 0) {
			
			if(seguimientoService.UpdateAplicativo(proyectoSeguimiento) == 1) {
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_APLICATIVO_GUARDADO;
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_APLICATIVO , "Guardado de aplicativo", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_APLICATIVO_ERROR_GUARDADO;
			}
			
		}
	}
	
	public void TerminaAplicativo() {
		inicializaMsg();
		
		if((fechaDespliegue != null && fechaFinPruebas != null && fechaNotificacion != null) || proyectoSeguimiento.getAplicativo().isSinFechas()) {
			
			GuardaAplicativo();
			calculaPorcentajeAvance(Fases.APLICATIVO.toString());
			if(seguimientoService.UpdateTerminaAplicativo(proyectoSeguimiento) == 1) {
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.PREATP.toString(), 2);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_APLICATIVO_TERMINADO;
				bitacoraService.insertBitacora(
						usuarioDTO.getIdUsuario(), 
						Actividades.TERMINAR_APLICATIVO , 
						"Término de aplicativo", "Seguimiento", 
						proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_APLICATIVO_ERROR_TERMINADO;
			}
			
		}else {
			msgError = Mensajes.APLICATIVO_SIN_FECHAS;
		}
		
	}
		
	//Funciones para Aplicativo [Fin]
	
	
	//Funciones Para Pre ATP [Inicio]
	
	public void cargaFechasPreAtp() {
		if(proyectoSeguimiento.getPreAtp().getFechaInicio() != null) {
			fechaInicioPreAtp = conversiones.StringToDate(proyectoSeguimiento.getPreAtp().getFechaInicio());
		}
		
		if(proyectoSeguimiento.getPreAtp().getFechaFin() != null) {
			fechaFinPreAtp = conversiones.StringToDate(proyectoSeguimiento.getPreAtp().getFechaFin());
		}
	}
	
	public void calculaFechaCompromisoPreAtp() {
		if(fechaInicioPreAtp != null) {
			proyectoSeguimiento.getPreAtp().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioPreAtp, DIAS_HABILES_PRE_ATP));
//			proyectoSeguimiento.getPreAtp().setFechaCompromiso(conversiones.AdicionDiasCompromiso(fechaInicioPreAtp, DIAS_NATURALES_PRE_ATP));
		}else {
			proyectoSeguimiento.getPreAtp().setFechaCompromiso(null);
		}
	}
	
	public void GuardaPreAtp() {
		inicializaMsg();
		
		proyectoSeguimiento.getPreAtp().setFechaInicio(conversiones.DateToString(fechaInicioPreAtp));
		proyectoSeguimiento.getPreAtp().setFechaFin(conversiones.DateToString(fechaFinPreAtp));
		
		if(proyectoSeguimiento.getPreAtp().getFechaInicio() == "") {
			proyectoSeguimiento.getPreAtp().setFechaInicio(null);
		}
		if(proyectoSeguimiento.getPreAtp().getFechaFin() == "") {
			proyectoSeguimiento.getPreAtp().setFechaFin(null);
		}
		
		if(proyectoSeguimiento.getPreAtp().getIdPreAtp() == 0) {
			
			if(seguimientoService.GuardaPreAtp(proyectoSeguimiento) == 1) {
				msgSuccess = Mensajes.PROYECTO_PRE_ATP_GUARDADO;
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.PREATP.toString(), 1);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_PRE_ATP , "Registro de Pre ATP", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_PRE_ATP_ERROR_GUARDADO;
			}
			
		}else if(proyectoSeguimiento.getPreAtp().getIdPreAtp() != 0) {
			
			if(seguimientoService.UpdatePreAtp(proyectoSeguimiento) == 1) {
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_PRE_ATP_GUARDADO;
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_PRE_ATP , "Guardado de Pre ATP", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_PRE_ATP_ERROR_GUARDADO;
			}
			
		}
	}
	
	public void TerminaPreAtp() {
		inicializaMsg();
		
		if((fechaInicioPreAtp != null && fechaFinPreAtp != null) || proyectoSeguimiento.getPreAtp().isSinFechas()) {
			
			GuardaPreAtp();
			calculaPorcentajeAvance(Fases.PREATP.toString());
			if(seguimientoService.UpdateTerminaPreAtp(proyectoSeguimiento) == 1) {
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.ATP.toString(), 2);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_PRE_ATP_TERMINADO;
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.TERMINAR_PRE_ATP , "Término de Pre ATP", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_PRE_ATP_ERROR_TERMINADO;
			}
			
		}else {
			msgError = Mensajes.PROYECTO_PRE_ATP_SIN_FECHAS;
		}
	}
	
	//Funciones para Pre ATP [Fin]
	
	
	//Funciones Para ATP [Inicio]
	
	public void cargaFechasAtp() {
		if(proyectoSeguimiento.getSegATP().getFechaInicio() != null) {
			fechaInicioAtp = conversiones.StringToDate(proyectoSeguimiento.getSegATP().getFechaInicio());
		}
		
		if(proyectoSeguimiento.getSegATP().getFechaFin() != null) {
			fechaFinAtp = conversiones.StringToDate(proyectoSeguimiento.getSegATP().getFechaFin());
		}
	}
	
	public void calculaFechaCompromisoAtp() {
		if(fechaInicioAtp != null) {
			proyectoSeguimiento.getSegATP().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioAtp, DIAS_HABILES_ATP));
//			proyectoSeguimiento.getSegATP().setFechaCompromiso(conversiones.AdicionDiasCompromiso(fechaInicioAtp, DIAS_NATURALES_ATP));
		}else {
			proyectoSeguimiento.getSegATP().setFechaCompromiso(null);
		}
	}
	
	public void GuardarAtp() {
		inicializaMsg();
		
		proyectoSeguimiento.getSegATP().setFechaInicio(conversiones.DateToString(fechaInicioAtp));
		proyectoSeguimiento.getSegATP().setFechaFin(conversiones.DateToString(fechaFinAtp));
		
		if(proyectoSeguimiento.getSegATP().getFechaInicio() == "") {
			proyectoSeguimiento.getSegATP().setFechaInicio(null);
		}
		if(proyectoSeguimiento.getSegATP().getFechaFin() == "") {
			proyectoSeguimiento.getSegATP().setFechaFin(null);
		}
		
		if(proyectoSeguimiento.getSegATP().getIdSeguimientoATP() == 0) {
			
			if(seguimientoService.GuardaAtp(proyectoSeguimiento) == 1) {
				msgSuccess = Mensajes.PROYECTO_ATP_GUARDADO;
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.ATP.toString(), 1);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_ATP , "Registro de ATP", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_ATP_ERROR_GUARDADO;
			}
			
		}else if(proyectoSeguimiento.getSegATP().getIdSeguimientoATP() != 0) {
			
			if(seguimientoService.UpdateAtp(proyectoSeguimiento) == 1) {
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_ATP_GUARDADO;
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_ATP , "Guardado de ATP", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_ATP_ERROR_GUARDADO;
			}
			
		}
	}
	
	public void TerminarAtp() {
		inicializaMsg();

		if((fechaInicioAtp != null && fechaFinAtp != null) || proyectoSeguimiento.getSegATP().isSinFechas()) {
			GuardarAtp();
			calculaPorcentajeAvance(Fases.ATP.toString());
			if(seguimientoService.UpdateTerminaAtp(proyectoSeguimiento) == 1) {
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.RTO.toString(), 2);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_ATP_TERMINADO;
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.TERMINAR_ATP , "Término de ATP", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_ATP_ERROR_TERMINADO;
			}
		}else {
			msgError = Mensajes.PROYECTO_ATP_SIN_FECHAS;
		}
	}
	
	//Funciones para ATP [Fin]
	
	
	//Funciones Para RTO [Inicio]
	
	public void cargaFechasRTO() {
		if(proyectoSeguimiento.getSegRTO().getFechaInicio() != null) {
			fechaInicioRto = conversiones.StringToDate(proyectoSeguimiento.getSegRTO().getFechaInicio());
		}
		
		if(proyectoSeguimiento.getSegRTO().getFechaFin() != null) {
			fechaFinRto = conversiones.StringToDate(proyectoSeguimiento.getSegRTO().getFechaFin());
		}
	}
	
	public void calculaFechaCompromisoRTO() {
		if(fechaInicioRto != null) {
			proyectoSeguimiento.getSegRTO().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioRto, DIAS_HABILES_RTO));
//			proyectoSeguimiento.getSegRTO().setFechaCompromiso(conversiones.AdicionDiasCompromiso(fechaInicioRto, DIAS_NATURALES_RTO));
		}else {
			proyectoSeguimiento.getSegRTO().setFechaCompromiso(null);
		}
	}
	
	public void GuardarRTO() {
		inicializaMsg();
		
		proyectoSeguimiento.getSegRTO().setFechaInicio(conversiones.DateToString(fechaInicioRto));
		proyectoSeguimiento.getSegRTO().setFechaFin(conversiones.DateToString(fechaFinRto));
		
		if(proyectoSeguimiento.getSegRTO().getFechaInicio() == "") {
			proyectoSeguimiento.getSegRTO().setFechaInicio(null);
		}
		if(proyectoSeguimiento.getSegRTO().getFechaFin() == "") {
			proyectoSeguimiento.getSegRTO().setFechaFin(null);
		}
		
		if(proyectoSeguimiento.getSegRTO().getIdSeguimientoRTO() == 0) {
			
			if(seguimientoService.GuardaRto(proyectoSeguimiento) == 1) {
				msgSuccess = Mensajes.PROYECTO_RTO_GUARDADO;
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.RTO.toString(), 1);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_RTO , "Registro de RTO", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_RTO_ERROR_GUARDADO;
			}
			
		}else if(proyectoSeguimiento.getSegRTO().getIdSeguimientoRTO() != 0) {
			
			if(seguimientoService.UpdateRto(proyectoSeguimiento) == 1) {
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_RTO_GUARDADO;				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_RTO , "Guardado de RTO", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_RTO_ERROR_GUARDADO;
			}
			
		}
	}
	
	public void TerminarRTO() {
		inicializaMsg();
		
		if((fechaInicioRto != null && fechaFinRto != null) || proyectoSeguimiento.getSegRTO().isSinFechas()) {			
			GuardarRTO();
			calculaPorcentajeAvance(Fases.RTO.toString());
			if(seguimientoService.UpdateTerminaRto(proyectoSeguimiento) == 1) {
				seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.TERMINADO.toString(), 2);
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				msgSuccess = Mensajes.PROYECTO_RTO_TERMINADO;
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.TERMINAR_RTO , "Término de RTO", "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {
				msgError = Mensajes.PROYECTO_RTO_ERROR_TERMINADO;
			}
		}else{
			msgError = Mensajes.PROYECTO_RTO_SIN_FECHAS;
		}
	}
	
	//Funciones para RTO [Fin]
	
	public void RegistrarIncidente() {
		inicializaMsg();
		
		if(!incidenteRegistro.getIssueIncidente().isEmpty() || incidenteRegistro.getResponsable().getIdResponable() != 0) {
						
			if(fechaIniIssue != null) {
				incidenteRegistro.setFechaInicio(conversiones.DateToString(fechaIniIssue));
			}
			
			if(incidentesService.RegistroIncidente(incidenteRegistro, proyectoSeguimiento) == 1) {			
				msgSuccess 	= Mensajes.INCIDENTE_REGISTRADO;
				incidenteRegistro	= new IncidenteDTO();
				incidenteRegistro.setResponsable(new ResponsableDTO());
				fechaIniIssue = null;
				CargaIncidentes();
				
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGA_INCIDENTE , "Se registra incidente para " + proyectoSeguimiento.getNombreProyecto(), "Seguimiento", proyectoSeguimiento.getIdProyecto());
			}else {			
				msgError = Mensajes.INCIDENTE_ERROR_REGISTRO;			
			}			
		}else {
			msgError = Mensajes.INCIDENTE_ERROR_CAMPOS_VACIO;
		}
		
		
	}
	
	public void CargaIncidentes() {
		listIncidentes = incidentesService.CargaIncidentes(proyectoSeguimiento.getIdProyecto());
		
		if(listIncidentes.size() != 0) {

			for(IncidenteDTO incidenteSelect : listIncidentes) {
				
				if(incidenteSelect.getEstatus().equals(Estatus.ACTIVO.toString())) {
					incidente = incidenteSelect;
					if(incidente.getFechaInicio() != null) {
						fechaInicioIncidente = conversiones.StringToDate(incidente.getFechaInicio());
					}
					if(incidente.getFechaResolucion() != null) {
						fechaFinIncidente  = conversiones.StringToDate(incidente.getFechaResolucion());
					}
				}
				
			}
			reiniciaIncidente();
			
		}reiniciaIncidente();
		
	}
	
	public void reiniciaIncidente() {
		if(incidente.getIdIncidente() == 0) {
			incidente				= new IncidenteDTO();
			incidente.setResponsable(new ResponsableDTO());
			fechaInicioIncidente	= null;
			fechaFinIncidente 		= null;
		}
	}
	
	public void UpdateIncidente() {
		inicializaMsg();

		if(fechaInicioIncidente != null) {
			incidente.setFechaInicio(conversiones.DateToString(fechaInicioIncidente));
		}
		if(fechaFinIncidente != null) {
			incidente.setFechaResolucion(conversiones.DateToString(fechaFinIncidente));
		}
		
		if(incidentesService.ActualizaIncidente(incidente) == 1) {
			msgSuccess 	= Mensajes.INCIDENTE_ACTUALIZADO;			
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_INCIDENTE , "Se actualizó el incidente " + incidente.getIdIncidente(), "Seguimiento", proyectoSeguimiento.getIdProyecto());
			incidente	= new IncidenteDTO();
			incidente.setResponsable(new ResponsableDTO());
			CargaIncidentes();
		}else {
			msgError = Mensajes.INCIDENTE_ERROR_ACTUALIZADO;
		}
		
	}
	
	public void CerrarIncidente() {
		inicializaMsg();
		
		if(incidentesService.CulminaIncidente(incidente) == 1) {
			msgSuccess 	= Mensajes.INCIDENTE_CERRADO;
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_INCIDENTE , "Se cerró el incidente " + incidente.getIdIncidente(), "Seguimiento", proyectoSeguimiento.getIdProyecto());
			incidente	= new IncidenteDTO();
			fechaInicioIncidente = null;
			fechaFinIncidente = null;
			CargaIncidentes();
		}else {
			msgError = Mensajes.INCIDENTE_ERROR_CERRADO;
		}
	}
	
	public void inicializaMsg() {
		msgSuccess 	= "";
		msgError 	= "";
	}

	public String getMsgSuccess() {
		return msgSuccess;
	}

	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
	}

	public ProyectoDTO getProyectoSeguimiento() {
		return proyectoSeguimiento;
	}

	public void setProyectoSeguimiento(ProyectoDTO proyectoSeguimiento) {
		this.proyectoSeguimiento = proyectoSeguimiento;
	}

	public Date getFechaInicioF60() {
		return fechaInicioF60;
	}

	public void setFechaInicioF60(Date fechaInicioF60) {
		this.fechaInicioF60 = fechaInicioF60;
	}

	public Date getFechaFinF60() {
		return fechaFinF60;
	}

	public void setFechaFinF60(Date fechaFinF60) {
		this.fechaFinF60 = fechaFinF60;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public Date getFechaKickOffInfra() {
		return fechaKickOffInfra;
	}

	public void setFechaKickOffInfra(Date fechaKickOffInfra) {
		this.fechaKickOffInfra = fechaKickOffInfra;
	}

	public Date getFechaInicioInfra() {
		return fechaInicioInfra;
	}

	public void setFechaInicioInfra(Date fechaInicioInfra) {
		this.fechaInicioInfra = fechaInicioInfra;
	}

	public Date getFechaFinInfra() {
		return fechaFinInfra;
	}

	public void setFechaFinInfra(Date fechaFinInfra) {
		this.fechaFinInfra = fechaFinInfra;
	}

	public Date getFechaDespliegue() {
		return fechaDespliegue;
	}

	public void setFechaDespliegue(Date fechaDespliegue) {
		this.fechaDespliegue = fechaDespliegue;
	}

	public Date getFechaNotificacion() {
		return fechaNotificacion;
	}

	public void setFechaNotificacion(Date fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}

	public Date getFechaInicioPreAtp() {
		return fechaInicioPreAtp;
	}

	public void setFechaInicioPreAtp(Date fechaInicioPreAtp) {
		this.fechaInicioPreAtp = fechaInicioPreAtp;
	}

	public Date getFechaFinPreAtp() {
		return fechaFinPreAtp;
	}

	public void setFechaFinPreAtp(Date fechaFinPreAtp) {
		this.fechaFinPreAtp = fechaFinPreAtp;
	}

	public Date getFechaInicioAtp() {
		return fechaInicioAtp;
	}

	public void setFechaInicioAtp(Date fechaInicioAtp) {
		this.fechaInicioAtp = fechaInicioAtp;
	}

	public Date getFechaFinAtp() {
		return fechaFinAtp;
	}

	public void setFechaFinAtp(Date fechaFinAtp) {
		this.fechaFinAtp = fechaFinAtp;
	}

	public Date getFechaInicioRto() {
		return fechaInicioRto;
	}

	public void setFechaInicioRto(Date fechaInicioRto) {
		this.fechaInicioRto = fechaInicioRto;
	}

	public Date getFechaFinRto() {
		return fechaFinRto;
	}

	public void setFechaFinRto(Date fechaFinRto) {
		this.fechaFinRto = fechaFinRto;
	}

	public List<IncidenteDTO> getListIncidentes() {
		return listIncidentes;
	}

	public void setListIncidentes(List<IncidenteDTO> listIncidentes) {
		this.listIncidentes = listIncidentes;
	}

	public IncidenteDTO getIncidente() {
		return incidente;
	}

	public void setIncidente(IncidenteDTO incidente) {
		this.incidente = incidente;
	}

	public Date getFechaInicioIncidente() {
		return fechaInicioIncidente;
	}

	public void setFechaInicioIncidente(Date fechaInicioIncidente) {
		this.fechaInicioIncidente = fechaInicioIncidente;
	}

	public Date getFechaFinIncidente() {
		return fechaFinIncidente;
	}

	public void setFechaFinIncidente(Date fechaFinIncidente) {
		this.fechaFinIncidente = fechaFinIncidente;
	}

	public IncidenteDTO getIncidenteRegistro() {
		return incidenteRegistro;
	}

	public void setIncidenteRegistro(IncidenteDTO incidenteRegistro) {
		this.incidenteRegistro = incidenteRegistro;
	}

	public Date getFechaIniIssue() {
		return fechaIniIssue;
	}

	public void setFechaIniIssue(Date fechaIniIssue) {
		this.fechaIniIssue = fechaIniIssue;
	}

	public Date getFechaFinIssue() {
		return fechaFinIssue;
	}

	public void setFechaFinIssue(Date fechaFinIssue) {
		this.fechaFinIssue = fechaFinIssue;
	}

	public Date getFechaFinPruebas() {
		return fechaFinPruebas;
	}

	public void setFechaFinPruebas(Date fechaFinPruebas) {
		this.fechaFinPruebas = fechaFinPruebas;
	}

	public List<ResponsableDTO> getListResponsables() {
		return listResponsables;
	}

	public void setListResponsables(List<ResponsableDTO> listResponsables) {
		this.listResponsables = listResponsables;
	}

	public List<JefeF60DTO> getListJefesF60() {
		return listJefesF60;
	}

	public void setListJefesF60(List<JefeF60DTO> listJefesF60) {
		this.listJefesF60 = listJefesF60;
	}

	public List<TipoF60DTO> getListTiposF60() {
		return listTiposF60;
	}

	public void setListTiposF60(List<TipoF60DTO> listTiposF60) {
		this.listTiposF60 = listTiposF60;
	}
}
