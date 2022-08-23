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
import com.telcel.sapi.constantes.Fases;
import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.dao.impl.BitacoraDAOImpl;
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.IncidenteDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ResponsableDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.Conversiones;
import com.telcel.sapi.service.IncidentesService;
import com.telcel.sapi.service.LoginService;
import com.telcel.sapi.service.ProyectoInfrestructuraService;
import com.telcel.sapi.service.ProyectoSeguimientoService;
import com.telcel.sapi.service.ProyectoService;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;
import com.telcel.sapi.service.impl.ConversionesImpl;
import com.telcel.sapi.service.impl.IncidentesServiceImpl;
import com.telcel.sapi.service.impl.LoginServiceImpl;
import com.telcel.sapi.service.impl.ProyectoInfrestructuraServiceImpl;
import com.telcel.sapi.service.impl.ProyectoSeguimientoServiceImpl;
import com.telcel.sapi.service.impl.ProyectoServiceImpl;

@ManagedBean (name = "superUsrFollowMB")
@ViewScoped
public class SuperUserFollowMB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2200879769792376252L;

	static final Logger LOG = Logger.getLogger(SuperUserFollowMB.class);

	static final String PAGINA_SEGUIMIENTO 				= "super-seguimiento.html";
	private static final String STYLE_ERROR_VALUE 		= "boder-error";
	static final String SI				 				= "Si";
	static final String SALTO_LINEA				 		= "\n";
	
	
	static final int 	DIAS_HABILES_F60 				= 15;
	static final int 	DIAS_HABILES_INFRESTRUCTURA 	= 15;
	static final int 	DIAS_HABILES_APLICATIVO 		= 15;
	static final int 	DIAS_HABILES_PRE_ATP 			= 45;
	static final int 	DIAS_HABILES_ATP 				= 10;
	static final int 	DIAS_HABILES_RTO 				= 5;
	
	ProyectoInfrestructuraService 	ambientesService;
	Conversiones 					conversiones;	
	ProyectoService 				proyectoService;
	ProyectoSeguimientoService 		seguimientoService;
	BitacoraDAO 					bitacoraService;
	IncidentesService				incidentesService;
	CatalogosService				catalogos;

	LoginService 					loginService;

	UsuarioDTO usuarioDTO;
	private ProyectoDTO 			proyectoSeguimiento;
	
	private List<IncidenteDTO> 		listIncidentes;	
	private List<AmbienteDTO> 		listAmbiente;
	private List<ResponsableDTO> 	listResponsables;
	private List<TipoAmbienteDTO> 	listTipoAmbiente;
	
	private List<JefeF60DTO> 		listJefesF60;
	private List<TipoF60DTO> 		listTiposF60;
	
	private AmbienteDTO ambienteUpdate;
	private AmbienteDTO ambienteDelete;

	private IncidenteDTO incidenteUpdate;
	private IncidenteDTO incidenteClose;

	private int	numIncidentes;
	private int	numeroAmbientes;

	private String msgSuccess;
	private String msgError;
	private String 	msgWarnning;
	
	private String msgHost;
	private String msgIP;
	
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
	
	private Date datePEEntrada;
	private Date datePESalida;

	private String styleErrorHost;
	private String styleErrorIp;

	private int numHostRepetidos;
	private int numIpRepetidos;

	private Date fechaInicioIncidente;
	private Date fechaFinIncidente;
	
	private Long idGerencia;
	private Long idDireccion;
	
	private List<DireccionDTO> 	listDireccion;
	private List<GerenciaDTO>	listGerencia;

	private List<FoliosRegistradosDTO> listFoliosRegistrados;
	private int numFoliosExistente;
	
	private int numCaracteresRestantes;
	private String comentarioCancelacion;
	private String comentarioActivacion;
	
	@PostConstruct
	public void init() {
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mensajeEliminacion", null);
		
		usuarioDTO 			= new UsuarioDTO();
		usuarioDTO 			= (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");

		loginService		= new LoginServiceImpl();
		loginService.ValidaSesion(usuarioDTO);
		
		proyectoSeguimiento = new ProyectoDTO();
		proyectoSeguimiento = (ProyectoDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("proyectoSeguimiento");
			
		ambientesService	= new ProyectoInfrestructuraServiceImpl();
		seguimientoService 	= new ProyectoSeguimientoServiceImpl();
		proyectoService		= new ProyectoServiceImpl();
		bitacoraService		= new BitacoraDAOImpl();
		conversiones		= new ConversionesImpl();
		incidentesService	= new IncidentesServiceImpl();
		catalogos			= new CatalogosServiceImpl();
		
		listIncidentes 		= new ArrayList<IncidenteDTO>();
		listAmbiente		= new ArrayList<AmbienteDTO>();
		listResponsables	= new ArrayList<ResponsableDTO>();
		listTipoAmbiente	= new ArrayList<TipoAmbienteDTO>();
		
		listDireccion 		= new ArrayList<DireccionDTO>();
		listGerencia		= new ArrayList<GerenciaDTO>();

		iniciaAmbienteUpdate();
		iniciaAmbienteDelete();
		
		incidenteUpdate		= new IncidenteDTO();
		incidenteClose		= new IncidenteDTO();
		incidenteUpdate.setResponsable(new ResponsableDTO());
		
		listResponsables	= catalogos.CargaResponsablesActivos();
		listDireccion		= catalogos.cargaCatDireccion();
		listTipoAmbiente	= catalogos.CatListTiposAmbientesActivos();
		
		proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
		inicializaMsg();
		numIncidentes = 0;
		
		idDireccion = proyectoSeguimiento.getGerenciaDto().getDireccion().getIdDireccion();
		idGerencia	= proyectoSeguimiento.getGerenciaDto().getIdGerencia();
		
		cargaGerencias();
		
		cargaAmbientes(proyectoSeguimiento.getIdProyecto());
		CargaFechasPortafolioEmp();
		CargaIncidentes();
		cargaFechasF60();
		cargaFechasInfra();
		cargaFechasAplicativo();
		cargaFechasPreAtp();
		cargaFechasAtp();
		cargaFechasRTO();
		cargaCatalogos();
		cargaComentarios();
	}
	
	public void cargaComentarios() {
		comentarioActivacion 	= proyectoSeguimiento.getComentariosGrls();
		comentarioCancelacion 	= proyectoSeguimiento.getComentariosGrls();
	}
	
	public void cargaCatalogos() {
		listJefesF60 = new ArrayList<JefeF60DTO>();
		listTiposF60 = new ArrayList<TipoF60DTO>();
		
		listJefesF60 = catalogos.catalogoJefesF60();
		listTiposF60 = catalogos.catalogoTiposF60();
	}
	
	public void iniciaAmbienteUpdate() {
		ambienteUpdate = new AmbienteDTO();
		ambienteUpdate.setTipoAmbiente(new TipoAmbienteDTO(0, "", ""));
	}
	
	public void iniciaAmbienteDelete() {
		ambienteDelete	= new AmbienteDTO();
		ambienteDelete.setTipoAmbiente(new TipoAmbienteDTO(0, "", ""));
	}
	
	public void cargaGerencias() {
		if(idDireccion == 0) {
			listGerencia.clear(); 
			idGerencia = 0L;
		}else {
			listGerencia	= catalogos.cargaCatGerencia(idDireccion);
		}
	}
	
	public void cargaAmbientes(Long idProyecto) {
		listAmbiente = ambientesService.CargaAmbientes(idProyecto);
		numeroAmbientes = listAmbiente.size();
	}
	
	public void GuardaDatosProyecto() {
		inicializaMsg();
		proyectoSeguimiento.setGerenciaDto(new GerenciaDTO(idGerencia, "", null, ""));
		if(proyectoService.ActualizaInfoProyecto(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.ACTUALIZA_PROYECTO_EXITOSO;
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.ACTUALIZA_PROYECTO, "Cambios en el proyecto " + proyectoSeguimiento.getNombreProyecto(), "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
		}else {
			LOG.error("Falló el registro");
			msgError = Mensajes.ERROR_OCURRIDO;
		}
	}
	
	public void CargaFechasPortafolioEmp() {
		
		if(proyectoSeguimiento.getPortafolioEmp().getIdSeguimiento() != 0) {
			if(proyectoSeguimiento.getPortafolioEmp().getEntrada() != null) {
				datePEEntrada = conversiones.StringToDate(proyectoSeguimiento.getPortafolioEmp().getEntrada());
			}
			if(proyectoSeguimiento.getPortafolioEmp().getSalida() != null) {
				datePESalida = conversiones.StringToDate(proyectoSeguimiento.getPortafolioEmp().getSalida());	
			}
		}
		
	}
	
	public void GuardaDatosPortafolioEmp() {
		inicializaMsg();
		if(datePEEntrada != null) {
			proyectoSeguimiento.getPortafolioEmp().setEntrada(conversiones.DateToString(datePEEntrada));
		}else {
			proyectoSeguimiento.getPortafolioEmp().setEntrada(null);
		}
		
		if(datePESalida != null) {
			proyectoSeguimiento.getPortafolioEmp().setSalida(conversiones.DateToString(datePESalida));
		}else {
			proyectoSeguimiento.getPortafolioEmp().setSalida(null);
		}
		
		if(folioNoVacios()) {
			if(fechasNoVacias()) {
				
				if(proyectoService.ActualizaPortafolioEmpresarial(proyectoSeguimiento) == 1) {
					msgSuccess = Mensajes.ACTUALIZA_PORTAFOLIO_EXITO;
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), 
							Actividades.ACTUALIZA_PORTAFOLIO, 
							"Actualización de portafolio", 
							"Super Seguimiento Usuario",
							proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.ERROR_ACTUALIZA_PORTAFOLIO;
				}
				
			}else {
				msgError = Mensajes.ERROR_FECHAS_VACIAS;
			}
		}else {
			msgError = Mensajes.ERROR_FOLIO_VACIO;
		}
		
	}
	
	public boolean folioNoVacios() {
		boolean noVacios = true;
		
		if(proyectoSeguimiento.getPortafolioEmp().getFolio().equals("") || proyectoSeguimiento.getPortafolioEmp().getFolio().isEmpty()) {
			noVacios = false;
		}
		
		return noVacios;
	}
	
	public boolean fechasNoVacias() {
		boolean noVacias = true;
		
		if(proyectoSeguimiento.getPortafolioEmp().getAplica().equals(SI)) {
			if(datePEEntrada == null || datePESalida == null) {
				noVacias = false;
			}
		}
		
		return noVacias;
	}
	
	//Funciones para F60 [Inicio]
	public void cargaFechasF60() {		
		if(proyectoSeguimiento.getProyectoF60().getFechaInicio() != null) {
			fechaInicioF60 	= conversiones.StringToDate(proyectoSeguimiento.getProyectoF60().getFechaInicio());
		}else {
			fechaInicioF60 	= null;
		}
		
		if(proyectoSeguimiento.getProyectoF60().getFechaFin() != null) {
			fechaFinF60		= conversiones.StringToDate(proyectoSeguimiento.getProyectoF60().getFechaFin());
		}else {
			fechaFinF60		= null;
		}
	}
	
	public void calculaFechaCompromisoF60() {		
		if(fechaInicioF60 != null) {
			proyectoSeguimiento.getProyectoF60().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioF60, DIAS_HABILES_F60));
			proyectoSeguimiento.getProyectoF60().setFechaInicio(conversiones.DateToString(fechaInicioF60));
		}else {
			proyectoSeguimiento.getProyectoF60().setFechaCompromiso(null);
		}
	}
	
	public void GuardaF60() {
		inicializaMsg();
		
		if(proyectoSeguimiento.getProyectoF60().getResponsable().isEmpty() || 
				proyectoSeguimiento.getProyectoF60().getJefeF60().getIdJefeF60() == 0 || 
				proyectoSeguimiento.getProyectoF60().getTipoF60().getIdTipoF60() == 0 ) {
			msgError = Mensajes.PROYECTO_F60_DATOS_FALTANTES;
		}else {
			if((fechaInicioF60 != null || fechaFinF60 != null) || proyectoSeguimiento.getProyectoF60().isSinFechas()) {
				
				proyectoSeguimiento.getProyectoF60().setFechaInicio(conversiones.DateToString(fechaInicioF60));
				
				if(proyectoSeguimiento.getProyectoF60().getFechaInicio() == "") {
					proyectoSeguimiento.getProyectoF60().setFechaInicio(null);
				}
				
				proyectoSeguimiento.getProyectoF60().setFechaFin(conversiones.DateToString(fechaFinF60));
				
				if(proyectoSeguimiento.getProyectoF60().getFechaFin() == "") {
					proyectoSeguimiento.getProyectoF60().setFechaFin(null);
				}
				
				if(proyectoSeguimiento.getProyectoF60().getIdProyectoF60() == 0) {
					
					if(seguimientoService.GuardaF60(proyectoSeguimiento) == 1) {
						msgSuccess = Mensajes.PROYECTO_F60_GUARDADO;
						seguimientoService.actualizaFase(proyectoSeguimiento.getIdProyecto(), Fases.F60.toString(), 1);
						proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
						
						bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_F60, "Registro de F60", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
					}else {
						msgError = Mensajes.PROYECTO_F60_ERROR_GUARDADO;
					}
					
				}else if(proyectoSeguimiento.getProyectoF60().getIdProyectoF60() != 0) {
					if(seguimientoService.UpdateF60(proyectoSeguimiento) == 1) {
						proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
						msgSuccess = Mensajes.PROYECTO_F60_MODIFICADO_EXITO;

						bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_F60, "Guardado de F60", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
					}else {
						msgError = Mensajes.PROYECTO_F60_ERROR_GUARDADO;
					}
				}
			}else {
				msgError = Mensajes.PROYECTO_F60_SIN_FECHAS_SEG_GEN;
			}
		}
		
	}
	
	/**
	 * Infraestructura
	 */
	public void cargaFechasInfra() {		
		if(proyectoSeguimiento.getSeguimientoInfra().getKickoff() != null) {
			fechaKickOffInfra = conversiones.StringToDate(proyectoSeguimiento.getSeguimientoInfra().getKickoff());
		}else {
			fechaKickOffInfra = null;
		}
		
		if(proyectoSeguimiento.getSeguimientoInfra().getFechaEnvio() != null) {
			fechaInicioInfra = conversiones.StringToDate(proyectoSeguimiento.getSeguimientoInfra().getFechaEnvio());
		}else {
			fechaInicioInfra = null;
		}
		
		if(proyectoSeguimiento.getSeguimientoInfra().getFechaEntregaUsuario() != null) {
			fechaFinInfra = conversiones.StringToDate(proyectoSeguimiento.getSeguimientoInfra().getFechaEntregaUsuario());
		}else {
			fechaFinInfra = null;
		}
	}
	
	public void calculaFechaCompromisoInfra() {		
		if(fechaInicioInfra != null) {
			proyectoSeguimiento.getSeguimientoInfra().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioInfra, DIAS_HABILES_INFRESTRUCTURA));
		}else {
			proyectoSeguimiento.getSeguimientoInfra().setFechaCompromiso(null);
		}
	}
	
	public void GuardaInfraestructura() {
		inicializaMsg();
		
		if((fechaInicioInfra != null || fechaKickOffInfra != null || fechaFinInfra != null) || proyectoSeguimiento.getSeguimientoInfra().isSinFechas()) {

//			if(fechaKickOffInfra == null) {
//				msgError = Mensajes.INFRA_ERROR_KICKOFF_VACIO;
//			}else {
				
				proyectoSeguimiento.getSeguimientoInfra().setFechaEnvio			(conversiones.DateToString(fechaInicioInfra));
				proyectoSeguimiento.getSeguimientoInfra().setKickoff			(conversiones.DateToString(fechaKickOffInfra));
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
						
						bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_INFRAESTRUCTURA , "Registro de infraestructura", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
					}else {
						msgError = Mensajes.PROYECTO_INFRA_ERROR_GUARDADO;
					}
					
				}else if(proyectoSeguimiento.getSeguimientoInfra().getIdSeguimientoInfra() != 0) {
					
					if(seguimientoService.UpdateSegInfra(proyectoSeguimiento) == 1) {
						proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
						msgSuccess = Mensajes.PROYECTO_INFRA_MODIFICADO_EXITO;
						bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.MODIFICAR_INFRAESTRUCTURA, "Guardado de infraestructura", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
					}else {
						msgError = Mensajes.PROYECTO_INFRA_ERROR_GUARDADO;
					}
					
				}
//			}
			
		}else {
			msgError = Mensajes.PROYECTO_INFRA_SIN_FECHAS_SEG_GEN;
		}
		
	}
	
	/**
	 * Aplicativo
	 */
	public void cargaFechasAplicativo() {
		if(proyectoSeguimiento.getAplicativo().getFechaDespliegue() != null) {
			fechaDespliegue = conversiones.StringToDate(proyectoSeguimiento.getAplicativo().getFechaDespliegue());
		}else {
			fechaDespliegue = null;
		}
		
		if(proyectoSeguimiento.getAplicativo().getFechaFinPruebas() != null) {
			fechaFinPruebas = conversiones.StringToDate(proyectoSeguimiento.getAplicativo().getFechaFinPruebas());
		}else {
			fechaFinPruebas = null;
		}
		
		if(proyectoSeguimiento.getAplicativo().getFechaNotificacion() != null) {
			fechaNotificacion = conversiones.StringToDate(proyectoSeguimiento.getAplicativo().getFechaNotificacion());
		}else {
			fechaNotificacion = null;
		}
	}
	
	public void calculaFechaCompromisoAplicativo() {
		if(fechaDespliegue != null) {
			proyectoSeguimiento.getAplicativo().setFechaFinPruebas(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaDespliegue, DIAS_HABILES_APLICATIVO));
		}else {
			proyectoSeguimiento.getAplicativo().setFechaFinPruebas(null);
		}
	}
	
	public void GuardaAplicativo() {
		inicializaMsg();
		
		if((fechaDespliegue != null || fechaFinPruebas != null || fechaNotificacion != null) || proyectoSeguimiento.getAplicativo().isSinFechas()) {

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
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_APLICATIVO , "Registro de aplicativo", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_APLICATIVO_ERROR_GUARDADO;
				}
				
			}else if(proyectoSeguimiento.getAplicativo().getIdAplicativo() != 0) {
				
				if(seguimientoService.UpdateAplicativo(proyectoSeguimiento) == 1) {
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					msgSuccess = Mensajes.PROYECTO_APLICATIVO_MODIFICADO_EXITO;
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_APLICATIVO , "Guardado de aplicativo", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_APLICATIVO_MODIFICADO_ERROR;
				}
				
			}
		}else {
			msgError = Mensajes.PROYECTO_APLICATIVO_SIN_FECHAS_SEG_GEN;
		}
	}
	
	/**
	 * Pre Atp´s
	 */
	public void cargaFechasPreAtp() {
		if(proyectoSeguimiento.getPreAtp().getFechaInicio() != null) {
			fechaInicioPreAtp = conversiones.StringToDate(proyectoSeguimiento.getPreAtp().getFechaInicio());
		}else {
			fechaInicioPreAtp = null;
		}
		
		if(proyectoSeguimiento.getPreAtp().getFechaFin() != null) {
			fechaFinPreAtp = conversiones.StringToDate(proyectoSeguimiento.getPreAtp().getFechaFin());
		}else {
			fechaFinPreAtp = null;
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
		
		if((fechaInicioPreAtp != null || fechaFinPreAtp != null) || proyectoSeguimiento.getPreAtp().isSinFechas()) {
		
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
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_PRE_ATP , "Registro de Pre ATP", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_PRE_ATP_ERROR_GUARDADO;
				}
				
			}else if(proyectoSeguimiento.getPreAtp().getIdPreAtp() != 0) {
				
				if(seguimientoService.UpdatePreAtp(proyectoSeguimiento) == 1) {
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					msgSuccess = Mensajes.PROYECTO_PRE_ATP_MODIFICADO_EXITO;
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.GUARDAR_PRE_ATP , "Guardado de Pre ATP", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_PRE_ATP_MODIFICADO_ERROR;
				}
				
			}
		}else {
			msgError = Mensajes.PROYECTO_PRE_ATP_SIN_FECHAS_SEG_GEN;
		}
	}
	
	/**
	 * ATP´s
	 */
	public void cargaFechasAtp() {
		if(proyectoSeguimiento.getSegATP().getFechaInicio() != null) {
			fechaInicioAtp = conversiones.StringToDate(proyectoSeguimiento.getSegATP().getFechaInicio());
		}else {
			fechaInicioAtp = null;
		}
		
		if(proyectoSeguimiento.getSegATP().getFechaFin() != null) {
			fechaFinAtp = conversiones.StringToDate(proyectoSeguimiento.getSegATP().getFechaFin());
		}else {
			fechaFinAtp = null;
		}
	}
	
	public void calculaFechaCompromisoAtp() {
		if(fechaInicioAtp != null) {
			proyectoSeguimiento.getSegATP().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioAtp, DIAS_HABILES_ATP));
		}else {
			proyectoSeguimiento.getSegATP().setFechaCompromiso(null);
		}
	}
	
	public void GuardarAtp() {
		inicializaMsg();
		
		if((fechaInicioAtp != null || fechaFinAtp != null) || proyectoSeguimiento.getSegATP().isSinFechas()) {
		
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
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_ATP , "Registro de ATP", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_ATP_ERROR_GUARDADO;
				}
				
			}else if(proyectoSeguimiento.getSegATP().getIdSeguimientoATP() != 0) {
				
				if(seguimientoService.UpdateAtp(proyectoSeguimiento) == 1) {
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					msgSuccess = Mensajes.PROYECTO_ATP_MODIFICADO_EXITO;
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_ATP , "Actualizó fechas de ATP", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_ATP_MODIFICADO_ERROR;
				}
				
			}
		}else{
			msgError = Mensajes.PROYECTO_ATP_SIN_FECHAS_SEG_GEN;
		}
	}
	
	/**
	 * RTO
	 */
	public void cargaFechasRTO() {
		if(proyectoSeguimiento.getSegRTO().getFechaInicio() != null) {
			fechaInicioRto = conversiones.StringToDate(proyectoSeguimiento.getSegRTO().getFechaInicio());
		}else {
			fechaInicioRto = null;
		}
		
		if(proyectoSeguimiento.getSegRTO().getFechaFin() != null) {
			fechaFinRto = conversiones.StringToDate(proyectoSeguimiento.getSegRTO().getFechaFin());
		}else {
			fechaFinRto = null;
		}
	}
	
	public void calculaFechaCompromisoRTO() {
		if(fechaInicioRto != null) {
			proyectoSeguimiento.getSegRTO().setFechaCompromiso(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaInicioRto, DIAS_HABILES_RTO));
		}else {
			proyectoSeguimiento.getSegRTO().setFechaCompromiso(null);
		}
	}
	
	public void GuardarRTO() {
		inicializaMsg();
		
		if((fechaInicioRto != null || fechaFinRto != null) || proyectoSeguimiento.getSegRTO().isSinFechas()) {
		
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
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_RTO , "Registro de RTO", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_RTO_ERROR_GUARDADO;
				}
				
			}else if(proyectoSeguimiento.getSegRTO().getIdSeguimientoRTO() != 0) {
				
				if(seguimientoService.UpdateRto(proyectoSeguimiento) == 1) {
					proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
					msgSuccess = Mensajes.PROYECTO_RTO_MODIFICADO_EXITO;
					
					bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_RTO , "Modificación de RTO", "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
				}else {
					msgError = Mensajes.PROYECTO_RTO_MODIFICADO_ERROR;
				}
				
			}
		}else {
			msgError = Mensajes.PROYECTO_RTO_SIN_FECHAS_SEG_GEN;
		}
	}
	
	/**
	 * Ambientes
	 */
	public void ActualizaAmbiente() {
		inicializaMsg();
		if(ambientesService.UpdateInfrestructura(ambienteUpdate) == 1) {
			msgSuccess = Mensajes.AMBIENTE_ACTUALIZACION_EXITO;	
			cargaAmbientes(proyectoSeguimiento.getIdProyecto());
			iniciaAmbienteUpdate();
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_AMBIENTE, "Registro de ambiente para " + proyectoSeguimiento.getNombreProyecto(), "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
		}else {
			msgError = Mensajes.AMBIENTE_ACTUALIZACION_ERROR;
		}
	}

	
	public void eliminarAmbiente(){
		inicializaMsg();
		
		if(ambientesService.DeleteInfrestructura(ambienteDelete) == 1) {
			msgSuccess = Mensajes.AMBIENTE_ELIMINACION_EXITO;
			cargaAmbientes(proyectoSeguimiento.getIdProyecto());
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.ELIMINA_AMBIENTE, "Eliminó el ambiente " + ambienteDelete.getHostName()+ " para el proyecto " + proyectoSeguimiento.getNombreProyecto(), "Super Seguimiento Usuario", proyectoSeguimiento.getIdProyecto());
			
		}else {
			msgError = Mensajes.AMBIENTE_ELIMINACION_ERROR + ambienteDelete.getHostName();
			cargaAmbientes(proyectoSeguimiento.getIdProyecto());
		}
	}
	
	public void VerificaHost() {
		inicializaMsg();
		styleErrorHost 	= "";
		msgHost 		= "";
		
		numHostRepetidos = ambientesService.cuentaFoliosByHostName(ambienteUpdate.getHostName());
		
		if(numHostRepetidos > 0) {
			styleErrorHost = STYLE_ERROR_VALUE;
			msgHost = Mensajes.AMBIENTE_HOST_ERROR + numHostRepetidos + " proyecto(s)";
		}else {
			styleErrorHost = "";
		}
		
	}
	
	public void VerificaIp() {
		styleErrorIp	= "";
		msgIP			= "";
		
		numIpRepetidos = ambientesService.cuentaFoliosByIp(ambienteUpdate.getIp());
		
		if(numIpRepetidos > 0) {
			styleErrorIp = STYLE_ERROR_VALUE;
			msgIP = Mensajes.AMBIENTE_IP_ERROR + numIpRepetidos + " proyecto(s)";
		}else {
			styleErrorIp = "";
		}
		
	}
	
	public void AsignaFechaEntrega() {
		if(ambienteUpdate.getEntregaEquipoDate() != null) {
			ambienteUpdate.setEntregaEquipo(conversiones.DateToString(ambienteUpdate.getEntregaEquipoDate()));
		}else if(ambienteUpdate.getEntregaEquipoDate() == null) {
			ambienteUpdate.setEntregaEquipo(null);
		}
	}
	
	public void iniciaMensajeHostIP() {
		msgHost 		= "";
		msgIP			= "";
		styleErrorIp	= "";
		styleErrorHost 	= "";
	}
	
	public void iniciaObject() {
		ambienteUpdate 		= new AmbienteDTO();
	}
	
	
	/**
	 * Métodos para Incidentes
	 */
	public void CargaIncidentes() {
		listIncidentes = incidentesService.CargaIncidentes(proyectoSeguimiento.getIdProyecto());
		numIncidentes = listIncidentes.size();
	}
	
	public void UpdateIncidente() {
		inicializaMsg();

		if(fechaInicioIncidente != null) {
			incidenteUpdate.setFechaInicio(conversiones.DateToString(fechaInicioIncidente));
		}
		if(fechaFinIncidente != null) {
			incidenteUpdate.setFechaResolucion(conversiones.DateToString(fechaFinIncidente));
		}
		
		if(incidentesService.ActualizaIncidente(incidenteUpdate) == 1) {
			msgSuccess 	= Mensajes.INCIDENTE_ACTUALIZADO;			
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_INCIDENTE , "Se actualizó el incidente " + incidenteUpdate.getIdIncidente(), "Seguimiento General", proyectoSeguimiento.getIdProyecto());
			incidenteUpdate	= new IncidenteDTO();
			incidenteUpdate.setResponsable(new ResponsableDTO());
			CargaIncidentes();
		}else {
			msgError = Mensajes.INCIDENTE_ERROR_ACTUALIZADO;
		}
		
	}
	
	public void cargaFechasIncidente() {
		fechaInicioIncidente 	= (incidenteUpdate.getFechaInicio() 	== null) 	?   null : conversiones.StringToDate(incidenteUpdate.getFechaInicio());
		fechaFinIncidente  		= (incidenteUpdate.getFechaResolucion() == null) 	? 	null : conversiones.StringToDate(incidenteUpdate.getFechaResolucion());
	}
	
	public void CerrarIncidente() {
		inicializaMsg();
		
		if(incidentesService.CulminaIncidente(incidenteClose) == 1) {
			msgSuccess 	= Mensajes.INCIDENTE_CERRADO;
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.UPDATE_INCIDENTE , "Se cerró el incidente " + incidenteClose.getIdIncidente(), "Seguimiento General", proyectoSeguimiento.getIdProyecto());
			incidenteClose	= new IncidenteDTO();
			fechaInicioIncidente = null;
			fechaFinIncidente = null;
			CargaIncidentes();
		}else {
			msgError = Mensajes.INCIDENTE_ERROR_CERRADO;
		}
	}
	
	public void cancelarProyecto() {
		inicializaMsg();
		
		if(comentarioCancelacion.isEmpty() || comentarioCancelacion == "") {
			msgError	= Mensajes.COMENTARIO_VACIO;
		}else {			
			proyectoSeguimiento.setComentariosGrls(comentarioCancelacion);
			if(proyectoService.CancelaProyecto(proyectoSeguimiento) == 1) {
				msgSuccess = Mensajes.CANCELACION_PROYECTO_EXITO;
				
				bitacoraService.insertBitacora(
						usuarioDTO.getIdUsuario(), 
						Actividades.CANCELA_PROYECTO, 
						"Canceló el proyecto " + proyectoSeguimiento.getNombreProyecto(), 
						"Super Seguimiento Usuario", 
						proyectoSeguimiento.getIdProyecto());
				
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				cargaComentarios();
			}else {
				msgError	= Mensajes.CANCELACION_PROYECTO_ERROR;
			}
		}
		
	}
	
	public void activarProyecto() {
		inicializaMsg();
		
		if(comentarioActivacion.isEmpty() || comentarioActivacion == "") {
			msgError	= Mensajes.COMENTARIO_VACIO;
		}else {
			proyectoSeguimiento.setComentariosGrls(comentarioActivacion);
			if(proyectoService.ActivaProyecto(proyectoSeguimiento) == 1) {
				msgSuccess = Mensajes.ACTIVACION_PROYECTO_EXITO;
				
				bitacoraService.insertBitacora(
						usuarioDTO.getIdUsuario(), 
						Actividades.ACTIVA_PROYECTO, 
						"Activó el proyecto " + proyectoSeguimiento.getNombreProyecto(), 
						"Super Seguimiento Usuario", 
						proyectoSeguimiento.getIdProyecto());
				
				proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());
				cargaComentarios();
			}else {
				msgError	= Mensajes.ACTIVACION_PROYECTO_ERROR;
			}
		}
	}
	
	/**
	 * Eliminación del proyecto
	 */
	public void eliminaProyecto() {
		inicializaMsg();
		
		if(proyectoService.EliminarProyecto(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.ELIMINACION_PROYECTO_EXITO;
			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_PROYECTO, 
					"Eliminó el proyecto " + proyectoSeguimiento.getNombreProyecto() + " con el ID: " + proyectoSeguimiento.getIdProyecto() + " e Identificador Interno: " + proyectoSeguimiento.getIdentificadorInterno() + " Folio PE: " + proyectoSeguimiento.getPortafolioEmp().getFolio(), 
					"Super Seguimiento Usuario", 
					0L);
			
			redireccionaSuperSegumiento(msgSuccess);
		}else {
			msgError	= Mensajes.ELIMINACION_PROYECTO_ERROR;
		}
	}
	
	public void redireccionaSuperSegumiento(String msgSuccess) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mensajeEliminacion", msgSuccess);
		FacesContext contex = FacesContext.getCurrentInstance();
		try {
			contex.getExternalContext().redirect(PAGINA_SEGUIMIENTO);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de actualización del proyecto: " + e);
		}
	}
	
	/**
	 * Eliminación de Fases
	 */
	public void eliminaFaseF60() {
		inicializaMsg();
		
		if(seguimientoService.DeleteF60(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.PROYECTO_F60_ELIMINADO_EXITO;

			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_F60, 
					"Eliminó la fase de F60 del proyecto " + proyectoSeguimiento.getNombreProyecto(), 
					"Super Seguimiento Usuario", 
					proyectoSeguimiento.getIdProyecto());
			
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());			
			cargaFechasF60();
		}else {
			msgError =  Mensajes.PROYECTO_F60_ELIMINADO_ERROR;
		}
	}
	
	public void eliminaFaseInfraestructura() {
		inicializaMsg();
		
		if(seguimientoService.DeleteSegInfra(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.PROYECTO_INFRA_ELIMINADO_EXITO;

			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_INFRAESTRUCTURA, 
					"Eliminó la fase de infraestructura del proyecto " + proyectoSeguimiento.getNombreProyecto(), 
					"Super Seguimiento Usuario", 
					proyectoSeguimiento.getIdProyecto());
			
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());			
			cargaFechasInfra();
		}else {
			msgError =  Mensajes.PROYECTO_INFRA_ELIMINADO_ERROR;
		}
	}
	
	public void eliminaFaseAplicativo() {
		inicializaMsg();
		
		if(seguimientoService.DeleteAplicativo(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.PROYECTO_APLICATIVO_ELIMINADO_EXITO;

			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_APLICATIVO, 
					"Eliminó la fase Aplicativo del proyecto " + proyectoSeguimiento.getNombreProyecto(), 
					"Super Seguimiento Usuario", 
					proyectoSeguimiento.getIdProyecto());
			
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());			
			cargaFechasAplicativo();
		}else {
			msgError =  Mensajes.PROYECTO_APLICATIVO_ELIMINADO_ERROR;
		}
	}
	
	public void eliminaFasePreATP() {
		inicializaMsg();
		
		if(seguimientoService.DeletePreAtp(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.PROYECTO_PRE_ATP_ELIMINADO_EXITO;

			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_PRE_ATP, 
					"Eliminó la fase Pre ATP del proyecto " + proyectoSeguimiento.getNombreProyecto(), 
					"Super Seguimiento Usuario", 
					proyectoSeguimiento.getIdProyecto());
			
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());			
			cargaFechasPreAtp();
		}else {
			msgError =  Mensajes.PROYECTO_PRE_ATP_ELIMINADO_ERROR;
		}
	}
	
	public void eliminaFaseATP() {
		inicializaMsg();
		
		if(seguimientoService.DeleteAtp(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.PROYECTO_ATP_ELIMINADO_EXITO;

			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_ATP, 
					"Eliminó la fase ATP del proyecto " + proyectoSeguimiento.getNombreProyecto(), 
					"Super Seguimiento Usuario", 
					proyectoSeguimiento.getIdProyecto());
			
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());			
			cargaFechasAtp();
		}else {
			msgError =  Mensajes.PROYECTO_ATP_ELIMINADO_ERROR;
		}
	}
		
	public void eliminaFaseRTO() {
		inicializaMsg();
		
		if(seguimientoService.DeleteRto(proyectoSeguimiento) == 1) {
			msgSuccess = Mensajes.PROYECTO_RTO_ELIMINADO_EXITO;

			bitacoraService.insertBitacora(
					usuarioDTO.getIdUsuario(), 
					Actividades.ELIMINA_RTO, 
					"Eliminó la fase RTO del proyecto " + proyectoSeguimiento.getNombreProyecto(), 
					"Super Seguimiento Usuario", 
					proyectoSeguimiento.getIdProyecto());
			
			proyectoSeguimiento = seguimientoService.CargaProyectoSelectAllData(proyectoSeguimiento.getIdProyecto());

			cargaFechasRTO();
		}else {
			msgError = Mensajes.PROYECTO_RTO_ELIMINADO_ERROR;
		}
	}
	
	/**
	 * Verificación de Folios repetidos
	 */
	
	public void verificaFolioExistente() {
		inicializaMsg();
		listFoliosRegistrados = proyectoService.foliosRegistrados(proyectoSeguimiento.getPortafolioEmp().getFolio());
		numFoliosExistente = listFoliosRegistrados.size();
		
		if(numFoliosExistente != 0) {
			msgWarnning = Mensajes.ERROR_FOLIO_REPETIDO + numFoliosExistente + Mensajes.PORYECTOS;
			 if(numFoliosExistente > 1) {
				 msgWarnning = msgWarnning + "s";
			 }
			 //msgWarnning = Mensajes.PROYECTOS_FOLIO_REGISTRADOS;			
		}else {
			msgWarnning = "";
		}
	}
	
	/**
	 * Método de inicio
	 */
	public void inicializaMsg() {
		msgSuccess 	= "";
		msgError 	= "";
		msgWarnning		= "";
	}

	public ProyectoDTO getProyectoSeguimiento() {
		return proyectoSeguimiento;
	}

	public void setProyectoSeguimiento(ProyectoDTO proyectoSeguimiento) {
		this.proyectoSeguimiento = proyectoSeguimiento;
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

	public Date getDatePEEntrada() {
		return datePEEntrada;
	}

	public void setDatePEEntrada(Date datePEEntrada) {
		this.datePEEntrada = datePEEntrada;
	}

	public Date getDatePESalida() {
		return datePESalida;
	}

	public void setDatePESalida(Date datePESalida) {
		this.datePESalida = datePESalida;
	}

	public List<IncidenteDTO> getListIncidentes() {
		return listIncidentes;
	}

	public void setListIncidentes(List<IncidenteDTO> listIncidentes) {
		this.listIncidentes = listIncidentes;
	}

	public int getNumIncidentes() {
		return numIncidentes;
	}

	public void setNumIncidentes(int numIncidentes) {
		this.numIncidentes = numIncidentes;
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

	public Date getFechaFinPruebas() {
		return fechaFinPruebas;
	}

	public void setFechaFinPruebas(Date fechaFinPruebas) {
		this.fechaFinPruebas = fechaFinPruebas;
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

	public List<AmbienteDTO> getListAmbiente() {
		return listAmbiente;
	}

	public void setListAmbiente(List<AmbienteDTO> listAmbiente) {
		this.listAmbiente = listAmbiente;
	}

	public int getNumeroAmbientes() {
		return numeroAmbientes;
	}

	public void setNumeroAmbientes(int numeroAmbientes) {
		this.numeroAmbientes = numeroAmbientes;
	}

	public AmbienteDTO getAmbienteUpdate() {
		return ambienteUpdate;
	}

	public void setAmbienteUpdate(AmbienteDTO ambienteUpdate) {
		this.ambienteUpdate = ambienteUpdate;
	}

	public AmbienteDTO getAmbienteDelete() {
		return ambienteDelete;
	}

	public void setAmbienteDelete(AmbienteDTO ambienteDelete) {
		this.ambienteDelete = ambienteDelete;
	}

	public String getStyleErrorHost() {
		return styleErrorHost;
	}

	public void setStyleErrorHost(String styleErrorHost) {
		this.styleErrorHost = styleErrorHost;
	}

	public String getStyleErrorIp() {
		return styleErrorIp;
	}

	public void setStyleErrorIp(String styleErrorIp) {
		this.styleErrorIp = styleErrorIp;
	}

	public String getMsgHost() {
		return msgHost;
	}

	public void setMsgHost(String msgHost) {
		this.msgHost = msgHost;
	}

	public String getMsgIP() {
		return msgIP;
	}

	public void setMsgIP(String msgIP) {
		this.msgIP = msgIP;
	}

	public int getNumHostRepetidos() {
		return numHostRepetidos;
	}

	public void setNumHostRepetidos(int numHostRepetidos) {
		this.numHostRepetidos = numHostRepetidos;
	}

	public int getNumIpRepetidos() {
		return numIpRepetidos;
	}

	public void setNumIpRepetidos(int numIpRepetidos) {
		this.numIpRepetidos = numIpRepetidos;
	}

	public IncidenteDTO getIncidenteUpdate() {
		return incidenteUpdate;
	}

	public void setIncidenteUpdate(IncidenteDTO incidenteUpdate) {
		this.incidenteUpdate = incidenteUpdate;
	}

	public List<ResponsableDTO> getListResponsables() {
		return listResponsables;
	}

	public void setListResponsables(List<ResponsableDTO> listResponsables) {
		this.listResponsables = listResponsables;
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

	public IncidenteDTO getIncidenteClose() {
		return incidenteClose;
	}

	public void setIncidenteClose(IncidenteDTO incidenteClose) {
		this.incidenteClose = incidenteClose;
	}

	public Long getIdGerencia() {
		return idGerencia;
	}

	public void setIdGerencia(Long idGerencia) {
		this.idGerencia = idGerencia;
	}

	public List<DireccionDTO> getListDireccion() {
		return listDireccion;
	}

	public void setListDireccion(List<DireccionDTO> listDireccion) {
		this.listDireccion = listDireccion;
	}

	public List<GerenciaDTO> getListGerencia() {
		return listGerencia;
	}

	public void setListGerencia(List<GerenciaDTO> listGerencia) {
		this.listGerencia = listGerencia;
	}

	public Long getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}

	public List<TipoAmbienteDTO> getListTipoAmbiente() {
		return listTipoAmbiente;
	}

	public void setListTipoAmbiente(List<TipoAmbienteDTO> listTipoAmbiente) {
		this.listTipoAmbiente = listTipoAmbiente;
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

	public String getMsgWarnning() {
		return msgWarnning;
	}

	public void setMsgWarnning(String msgWarnning) {
		this.msgWarnning = msgWarnning;
	}

	public List<FoliosRegistradosDTO> getListFoliosRegistrados() {
		return listFoliosRegistrados;
	}

	public void setListFoliosRegistrados(List<FoliosRegistradosDTO> listFoliosRegistrados) {
		this.listFoliosRegistrados = listFoliosRegistrados;
	}

	public int getNumFoliosExistente() {
		return numFoliosExistente;
	}

	public void setNumFoliosExistente(int numFoliosExistente) {
		this.numFoliosExistente = numFoliosExistente;
	}

	public int getNumCaracteresRestantes() {
		return numCaracteresRestantes;
	}

	public void setNumCaracteresRestantes(int numCaracteresRestantes) {
		this.numCaracteresRestantes = numCaracteresRestantes;
	}

	public String getComentarioCancelacion() {
		return comentarioCancelacion;
	}

	public void setComentarioCancelacion(String comentarioCancelacion) {
		this.comentarioCancelacion = comentarioCancelacion;
	}

	public String getComentarioActivacion() {
		return comentarioActivacion;
	}

	public void setComentarioActivacion(String comentarioActivacion) {
		this.comentarioActivacion = comentarioActivacion;
	}
}
