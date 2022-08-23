package com.telcel.sapi.mb;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.PermisosDTO;
import com.telcel.sapi.dto.PorcAvanceDTO;
import com.telcel.sapi.dto.ResponsableDTO;
import com.telcel.sapi.dto.RolDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.UsuariosService;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;
import com.telcel.sapi.service.impl.UsuariosServiceImpl;

@ManagedBean(name = "funOperativeMB")
@ViewScoped
public class FuncionesOperativasMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5857014910967343355L;
	static final Logger LOG = Logger.getLogger(FuncionesOperativasMB.class);

	private static final String STYLE_ERROR_VALUE 			= "boder-error";
	
	CatalogosService 	catalogosService;
	BitacoraDAO 		bitacoraService;
	UsuariosService 	usrService;
	
	UsuarioDTO usuarioSesion;
	
	private List<UsuarioDTO> 	listUsuarios;
	private UsuarioDTO			usuarioFO;
	private UsuarioDTO			usuarioUpdate;
	private UsuarioDTO			usuarioOnOff;

	private List<EstrategiaDTO>  	listEstrategias;
	private List<ResponsableDTO> 	listResponsables;
	private List<TipoAmbienteDTO> 	listTipoAmbiente;
	private List<DireccionDTO>	 	listDirecciones;
	private List<GerenciaDTO>	 	listGerencias;
	
	private String				tipoAmbienteNew;
	private TipoAmbienteDTO		tipoAmbienteOnOff;
	private TipoAmbienteDTO 	tipoAmbienteUpdate;
	private TipoAmbienteDTO 	tipoAmbienteDelete;

	private String		  		estrategiaNew;
	private EstrategiaDTO 		estrategiaUpdate;
	private EstrategiaDTO 		estrategiaOnOff;
	private EstrategiaDTO 		estrategiaDelete;

	private ResponsableDTO		newResponsable;
	private ResponsableDTO		responsableUpdate;
	private ResponsableDTO		responsableOnOff;
	private ResponsableDTO		responsableDelete;
	
	private String				newDireccion;
	private DireccionDTO		direccionOnOff;
	private DireccionDTO		direccionUpdate;
	
	private String				newGerencia;
	private GerenciaDTO			gerenciaOnOff;
	private GerenciaDTO			gerenciaUpdate;
	
	private PorcAvanceDTO		porcentajes;
	
	private Long 				idDireccion;
	
	private int					pestaniaActiva;
	
	private String 				msgSuccess;
	private String 				msgError;	

	private String 				styleErrorPorcPE;
	private String 				styleErrorPorcF60;
	private String 				styleErrorPorcInfra;
	private String 				styleErrorPorcApl;
	private String 				styleErrorPorcPreATP;
	private String 				styleErrorPorcATP;
	private String 				styleErrorPorcRTO;
	
	@PostConstruct
	public void init() {
		
		usuarioSesion 			= new UsuarioDTO();
		usuarioSesion 			= (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");

		bitacoraService		= new BitacoraDAOImpl();		
		catalogosService	= new CatalogosServiceImpl();
		listEstrategias 	= new ArrayList<EstrategiaDTO>();
		listResponsables 	= new ArrayList<ResponsableDTO>();
		listTipoAmbiente	= new ArrayList<TipoAmbienteDTO>();
		listDirecciones		= new ArrayList<DireccionDTO>();
		listGerencias		= new ArrayList<GerenciaDTO>();

		listEstrategias 	= catalogosService.FindEstrategiasInAc();
		listResponsables	= catalogosService.CargaAllResponsables();
		listTipoAmbiente	= catalogosService.CatListTiposAmbientes();
		listDirecciones		= catalogosService.CatalogoDirecciones();
		listGerencias		= catalogosService.CatalogoGerencia();

		estrategiaNew		= "";
		estrategiaUpdate 	= new EstrategiaDTO();
		estrategiaOnOff 	= new EstrategiaDTO();
		estrategiaDelete 	= new EstrategiaDTO();

		newResponsable		= new ResponsableDTO();
		responsableUpdate 	= new ResponsableDTO();
		responsableOnOff 	= new ResponsableDTO();
		responsableDelete 	= new ResponsableDTO();
		
		tipoAmbienteNew 	= "";
		tipoAmbienteOnOff 	= new TipoAmbienteDTO();
		tipoAmbienteUpdate 	= new TipoAmbienteDTO();
		tipoAmbienteDelete 	= new TipoAmbienteDTO();
		
		newDireccion		= "";
		direccionOnOff		= new DireccionDTO();
		direccionUpdate		= new DireccionDTO();
		
		newGerencia			= "";
		gerenciaOnOff		= new GerenciaDTO();
		gerenciaUpdate		= new GerenciaDTO();
		gerenciaUpdate.setDireccion(new DireccionDTO(0L, "", ""));
		
		porcentajes		= new PorcAvanceDTO();
		porcentajes		= catalogosService.CargaPorcentajesAvance();
		
		idDireccion 	= 0L;
		pestaniaActiva 	= 0;
		
		startMsg();
		estrategiaNew = "";
		
		/**
		 * Funciones para Usuarios
		 */
		usrService 		= new UsuariosServiceImpl();
		listUsuarios 	= new ArrayList<UsuarioDTO>();
		inicializaObjetoUsuarioFO();
		inicializaObjetoUsuarioUpdate();
		inicializaObjetoUsuarioOnOff();
		InicializaEstilosPorcentajes();
		
		listUsuarios = usrService.ConsultaUsuarios();
	}
	/**
	 * Métodos para Administrar Estrategias
	 */
	
	public void NuevaEstrategia() {
		startMsg();
		if(catalogosService.GuardaNewEstrategia(estrategiaNew) == 1) {			
			msgSuccess 		= Mensajes.REGISTRO_ESTRATEGIA_EXITOSO;
			listEstrategias = catalogosService.FindEstrategiasInAc();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.NUEVA_ESTRATEGIA, 
					"Registró una nueva estrategia: " + estrategiaNew, 
					"Funciones Operativas", 0L);

			estrategiaNew = "";
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_OCURRIDO_ESTRATEGIA;
		}
	}
	
	public void UpdateEstrategia() {
		startMsg();
		if(catalogosService.UpdateEstrategia(estrategiaUpdate) == 1) {
			msgSuccess = Mensajes.ACTUALIZACION_ESTRATEGIAS_EXITOSO;
			listEstrategias = catalogosService.FindEstrategiasInAc();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTUALIZA_ESTRATEGIA, 
					"Actualizó la estrategia ID: " + estrategiaUpdate.getIdEstrategia(), 
					"Funciones Operativas", 0L);

			estrategiaUpdate 	= new EstrategiaDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_ACTUALIZAR_ESTRATEGIA;
		}
	}
	
	public void ActivaEstrategia() {
		startMsg();
		if(catalogosService.ActivaInactivaEstrategia(estrategiaOnOff, true) == 1) {
			msgSuccess = Mensajes.ACTIVAR_ESTRATEGIA_EXITOSO;
			listEstrategias = catalogosService.FindEstrategiasInAc();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTIVA_ESTRATEGIA, 
					"Activó la estrategia: " + estrategiaOnOff.getEstrategia(), 
					"Funciones Operativas", 0L);

			estrategiaOnOff 	= new EstrategiaDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_ACTIVAR_ESTRATEGIA;
		}
	}
	
	public void DesactivaEstrategia() {
		startMsg();
		if(catalogosService.ActivaInactivaEstrategia(estrategiaOnOff, false) == 1) {
			msgSuccess = Mensajes.DESACTIVAR_ESTRATEGIA_EXITOSO;
			listEstrategias = catalogosService.FindEstrategiasInAc();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.DESACTIVA_ESTRATEGIA, 
					"Desactivó la estrategia: " + estrategiaOnOff.getEstrategia(), 
					"Funciones Operativas", 0L);

			estrategiaOnOff 	= new EstrategiaDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_DESACTIVAR_ESTRATEGIA;
		}
	}
	
	public void EliminaEstrategia() {
		startMsg();
		if(catalogosService.DeleteEstrategia(estrategiaDelete) == 1) {
			msgSuccess = Mensajes.ELIMINACION_ESTRATEGIAS_EXITO;
			listEstrategias = catalogosService.FindEstrategiasInAc();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ELIMINA_ESTRATEGIA, 
					"Eliminó la estrategia: " + estrategiaOnOff.getEstrategia(), 
					"Funciones Operativas", 0L);

			estrategiaDelete 	= new EstrategiaDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ELIMINACION_ERROR_ESTRATEGIAS;
		}
	}
	
	
	/**
	 * Métodos para Administrar Responsables
	 */
	public void GuardarResponsable() {
		startMsg();
		if(catalogosService.GuardaResponsable(newResponsable) == 1) {			
			msgSuccess 		= Mensajes.REGISTRO_RESPONSABLE_EXITOSO;
			listResponsables	= catalogosService.CargaAllResponsables();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.NUEVO_RESPONSABLE, 
					"Registró un nuevo responsable: " + newResponsable.getResponsable(), 
					"Funciones Operativas", 0L);

			newResponsable = new ResponsableDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_REGISTRO_RESPONSABLE;
		}
	}
	
	public void UpdateResponsable() {
		startMsg();
		if(catalogosService.UpdateResponsable(responsableUpdate) == 1) {			
			msgSuccess 		= Mensajes.UPDATE_RESPONSABLES_EXITOSO;
			listResponsables	= catalogosService.CargaAllResponsables();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTUALIZA_RESPONSABLE, 
					"Actualizó responsable: " + responsableUpdate.getResponsable(), 
					"Funciones Operativas", 0L);

			responsableUpdate = new ResponsableDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.UPDATE_RESPONSABLES_ERROR;
		}
	}
	
	public void ActivaResponsable() {
		startMsg();
		if(catalogosService.OnOffResponsable(responsableOnOff, true) == 1) {			
			msgSuccess 		= Mensajes.ACTIVAR_RESPONSABLE_EXITOSO;
			listResponsables	= catalogosService.CargaAllResponsables();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTIVA_RESPONSABLE, 
					"Activa responsable: " + responsableOnOff.getResponsable(), 
					"Funciones Operativas", 0L);

			responsableDelete = new ResponsableDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_ACTIVAR_RESPONSABLE;
		}
	}
	
	public void DesActivaResponsable() {
		startMsg();
		if(catalogosService.OnOffResponsable(responsableOnOff, false) == 1) {			
			msgSuccess 		= Mensajes.DESACTIVAR_RESPONSABLE_EXITOSO;
			listResponsables	= catalogosService.CargaAllResponsables();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.DESACTIVA_RESPONSABLE, 
					"Desactiva responsable: " + responsableOnOff.getResponsable(), 
					"Funciones Operativas", 0L);

			responsableDelete = new ResponsableDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_DESACTIVAR_RESPONSABLE;
		}
	}
	
	public void DeleteResponsable() {
		startMsg();
		if(catalogosService.DeleteResponsable(responsableDelete) == 1) {			
			msgSuccess 		= Mensajes.ELIMINACION_RESPONSABLE_EXITO;
			listResponsables	= catalogosService.CargaAllResponsables();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ELIMINA_RESPONSABLE, 
					"Elimina responsable: " + responsableDelete.getResponsable(), 
					"Funciones Operativas", 0L);

			responsableDelete = new ResponsableDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ELIMINACION_ERROR_RESPONSABLE;
		}
	}
	
	/**
	 * Métodos para administrar Tipos de Ambiente 
	 */
	public void RegistraNewTipoAmbiente() {
		startMsg();
		if(catalogosService.GuardaNewTipoAmbiente(tipoAmbienteNew) == 1) {
			listTipoAmbiente = catalogosService.CatListTiposAmbientes();
			msgSuccess = Mensajes.REGISTRO_NUEVO_TIPO_AMBIENTE;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.NUEVO_TIPO_AMBIENTE, 
					"Registro de tipo de ambiente: " + tipoAmbienteNew, 
					"Funciones Operativas", 0L);
			
			tipoAmbienteNew = "";
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_REGISTRO_NUEVO_TIPO_AMBIENTE;
		}
	}
	
	public void UpdateTipoAmbiente() {
		startMsg();
		if(catalogosService.UpdateNewTipoAmbiente(tipoAmbienteUpdate) == 1) {
			listTipoAmbiente = catalogosService.CatListTiposAmbientes();
			msgSuccess = Mensajes.UPDATE_NUEVO_TIPO_AMBIENTE;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTUALIZA_TIPO_AMBIENTE, 
					"Actualiza tipo de ambiente: " + tipoAmbienteUpdate.getTipoAmbiente(), 
					"Funciones Operativas", 0L);
			
			tipoAmbienteUpdate = new TipoAmbienteDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_UPDATE_NUEVO_TIPO_AMBIENTE;
		}
	}
	
	public void ActivaTipoAmbiente() {
		startMsg();
		if(catalogosService.ActivaInactivaTipoAmbiente(tipoAmbienteOnOff, true) == 1) {
			msgSuccess = Mensajes.ACTIVACION_TIPO_AMBIENTE;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTIVA_TIPO_AMBIENTE, 
					"Activación de tipo de ambiente: " + tipoAmbienteOnOff.getTipoAmbiente(), 
					"Funciones Operativas", 0L);
			
			listTipoAmbiente = catalogosService.CatListTiposAmbientes();
			tipoAmbienteOnOff = new TipoAmbienteDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_ACTIVACION_TIPO_AMBIENTE;
		}
	}
	
	public void DesActivaTipoAmbiente() {
		startMsg();
		if(catalogosService.ActivaInactivaTipoAmbiente(tipoAmbienteOnOff, false) == 1) {
			msgSuccess = Mensajes.DESACTIVACION_TIPO_AMBIENTE;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.DESACTIVA_TIPO_AMBIENTE, 
					"Desactivación de tipo de ambiente: " + tipoAmbienteOnOff.getTipoAmbiente(), 
					"Funciones Operativas", 0L);
			
			listTipoAmbiente = catalogosService.CatListTiposAmbientes();
			tipoAmbienteOnOff = new TipoAmbienteDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ERROR_DESACTIVAR_TIPO_AMBIENTE;
		}
	}
	
//	public void DeleteTipoAmbiente() {
//		startMsg();
//		if(catalogosService.DeleteTipoAmbiente(tipoAmbienteDelete) == 1) {
//			msgSuccess = Mensajes.DELETE_NUEVO_TIPO_AMBIENTE;
//			listTipoAmbiente = catalogosService.CatListTiposAmbientes();
//			tipoAmbienteDelete = new TipoAmbienteDTO();
//		}else {
//			msgError = Mensajes.ERROR_DELETE_NUEVO_TIPO_AMBIENTE;
//		}
//	}
	
	/**
	 * Métodos para administrar Catalogo de Direcciones
	 */
	public void registraNuevaDireccion() {
		startMsg();
		if(catalogosService.GuardaNuevaDireccion(newDireccion) == 1) {
			listDirecciones = catalogosService.CatalogoDirecciones();
			msgSuccess = Mensajes.REGISTRO_NUEVA_DIRECCION_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.NUEVA_DIRECCION, 
					"Registro de nueva dirección: " + newDireccion, 
					"Funciones Operativas", 0L);
			
			newDireccion = "";
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.REGISTRO_NUEVA_DIRECCION_ERROR;
		}
	}
	
	public void updateDireccionBySelected() {
		startMsg();
		if( catalogosService.UpdateDireccionBySelected(direccionUpdate) == 1 ) {
			listDirecciones = catalogosService.CatalogoDirecciones();
			msgSuccess 		= Mensajes.ACTUALIZACION_DIRECCION_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTUALIZA_DIRECCION, 
					"Actualiza dirección: " + direccionUpdate.getDireccion(), 
					"Funciones Operativas", 0L);
			
			direccionUpdate = new DireccionDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ACTUALIZACION_DIRECCION_ERROR;
		}
	}
	
	public void activaDireccion() {
		startMsg();
		if(catalogosService.ActivaInactivaDireccion(direccionOnOff, true) == 1) {
			listDirecciones = catalogosService.CatalogoDirecciones();
			msgSuccess 		= Mensajes.ACTIVA_DIRECCION_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTIVA_DIRECCION, 
					"Activa dirección: " + direccionOnOff.getDireccion(), 
					"Funciones Operativas", 0L);
			
			direccionOnOff 	= new DireccionDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ACTIVA_DIRECCION_ERROR;
		}
	}
	
	public void desactivaDireccion() {
		startMsg();
		if(catalogosService.ActivaInactivaDireccion(direccionOnOff, false) == 1) {
			listDirecciones = catalogosService.CatalogoDirecciones();
			msgSuccess 		= Mensajes.DESACTIVA_DIRECCION_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.DESACTIVA_DIRECCION, 
					"Desactiva dirección: " + direccionOnOff.getDireccion(), 
					"Funciones Operativas", 0L);
			
			direccionOnOff 	= new DireccionDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.DESACTIVA_DIRECCION_ERROR;
		}
	}
	
	/**
	 * Metodos para Administrar las Gerencias
	 */
	public void guardaNvaGerencia() {
		startMsg();
		if(catalogosService.GuardaNuevaGerencia(newGerencia, idDireccion)== 1) {
			listGerencias = catalogosService.CatalogoGerencia();
			msgSuccess = Mensajes.REGISTRO_NUEVA_GERENCIA_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.NUEVA_GERENCIA, 
					"Registro de nueva gerencia: " + newGerencia, 
					"Funciones Operativas", 0L);
			
			newGerencia = "";
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.REGISTRO_NUEVA_GERENCIA_ERROR;
		}
	}
	
	public void updateGerenciaBySelected() {
		startMsg();
		if(catalogosService.UpdateGerenciaBySelected(gerenciaUpdate) == 1) {
			listGerencias = catalogosService.CatalogoGerencia();
			msgSuccess = Mensajes.ACTUALIZACION_GERENCIA_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTUALIZA_GERENCIA, 
					"Actualiza gerencia: " + gerenciaUpdate.getGerencia(), 
					"Funciones Operativas", 0L);
			
			gerenciaUpdate = new GerenciaDTO();
			gerenciaUpdate.setDireccion(new DireccionDTO(0L, "", ""));
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ACTUALIZACION_GERENCIA_ERROR;
		}
	}
	
	public void activaGerencia() {
		startMsg();
		if(catalogosService.ActivaInactivaGerencia(gerenciaOnOff, true) == 1) {
			listGerencias = catalogosService.CatalogoGerencia();
			msgSuccess 		= Mensajes.ACTIVA_GERENCIA_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTIVA_GERENCIA, 
					"Activa gerencia: " + gerenciaOnOff.getGerencia(), 
					"Funciones Operativas", 0L);
			
			direccionOnOff 	= new DireccionDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.ACTIVA_GERENCIA_ERROR;
		}
	}
	
	public void desactivaGerencia() {
		startMsg();
		if(catalogosService.ActivaInactivaGerencia(gerenciaOnOff, false) == 1) {
			listGerencias = catalogosService.CatalogoGerencia();
			msgSuccess 		= Mensajes.DESACTIVA_GERENCIA_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.DESACTIVA_GERENCIA, 
					"Desactiva gerencia: " + gerenciaOnOff.getGerencia(), 
					"Funciones Operativas", 0L);
			
			direccionOnOff 	= new DireccionDTO();
			pestaniaActiva 	= 1;
		}else {
			msgError = Mensajes.DESACTIVA_GERENCIA_ERROR;
		}
	}
	
	
	/**
	 * Inicialización de Mensajes
	 */
	public void startMsg() {
		msgSuccess 	= "";
		msgError	= "";
	}
	
	/**
	 * Métodos para Usuarios
	 */
	public void guardaUsuarioNuevo() {
		startMsg();
		
		if(usuarioFO.getLider().getIdUsuario() == 0) {
			usuarioFO.getLider().setIdUsuario(0L);
		}
		
		if(usrService.GuardaUsuario(usuarioFO) == 1) {
			msgSuccess 	= Mensajes.REGISTRO_USUARIO_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.NUEVO_USUARIO, 
					"Registro de nuevo usuario: " + usuarioFO.getNombreUsuario(), 
					"Funciones Operativas", 0L);
			
			listUsuarios = usrService.ConsultaUsuarios();
			inicializaObjetoUsuarioFO();
			pestaniaActiva 	= 0;
		}else {
			msgError	= Mensajes.REGISTRO_USUARIO_ERROR;
		}
	}
	
	public void actualizaUsuarioNuevo() {
		startMsg();
		
		if(usuarioUpdate.getLider().getIdUsuario() == 0) {
			usuarioFO.getLider().setIdUsuario(0L);
		}
		
		if(usrService.ActualizaUsuario(usuarioUpdate) == 1) {
			msgSuccess 	= Mensajes.ACTUALIZACION_USUARIO_EXITO;
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTUALIZA_USUARIO, 
					"Actualiza usuario: " + usuarioUpdate.getNombreUsuario(), 
					"Funciones Operativas", 0L);
			
			listUsuarios = usrService.ConsultaUsuarios();
			inicializaObjetoUsuarioFO();
			pestaniaActiva 	= 0;
		}else {
			msgError	= Mensajes.ACTUALIZACION_USUARIO_ERROR + usuarioUpdate.getNombre() + " " + usuarioUpdate.getPrimerApellido() + " " + usuarioUpdate.getSegundoApellido();
		}
	}
	
	public void activaUsuario() {
		startMsg();
		pestaniaActiva 	= 0;
		if(usrService.ActivaInactivaUsuario(usuarioOnOff, true) == 1) {
			listUsuarios = usrService.ConsultaUsuarios();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.ACTIVA_USUARIO, 
					"Activa usuario: " + usuarioOnOff.getNombreUsuario(), 
					"Funciones Operativas", 0L);
			
			inicializaObjetoUsuarioOnOff();
			msgSuccess 	= Mensajes.ACTIVACION_USUARIO_EXITO;
		}else {
			msgError	= Mensajes.ACTIVACION_USUARIO_ERROR;
		}
	}
	
	public void desactivaUsuario() {
		startMsg();
		pestaniaActiva 	= 0;
		if(usrService.ActivaInactivaUsuario(usuarioOnOff, false) == 1) {
			listUsuarios = usrService.ConsultaUsuarios();
			
			bitacoraService.insertBitacora(
					usuarioSesion.getIdUsuario(), 
					Actividades.DESACTIVA_USUARIO, 
					"Desactiva usuario: " + usuarioOnOff.getNombreUsuario(), 
					"Funciones Operativas", 0L);
			
			inicializaObjetoUsuarioOnOff();
			msgSuccess 	= Mensajes.DESACTIVACION_USUARIO_EXITO;
		}else {
			msgError	= Mensajes.DESACTIVACION_USUARIO_ERROR;
		}
	}
	
	/**
	 * Inicializa Objeto UsuarioFO
	 */
	public void inicializaObjetoUsuarioFO() {
		usuarioFO		= new UsuarioDTO(
				0L, "", "", "", "", "", "", "", "", 
				new UsuarioDTO(
						0L, "", "", "", "", "", "", "", "", 
						new UsuarioDTO(), 
						new RolDTO(2, "", ""), 
						new PermisosDTO(0L, false, false, false, false, false, false, false)), 
				new RolDTO(2, "", ""), 
				new PermisosDTO(0L, false, false, false, false, false, false, false));
	}
	
	public void inicializaObjetoUsuarioUpdate() {
		usuarioUpdate	= new UsuarioDTO(
				0L, "", "", "", "", "", "", "", "", 
				new UsuarioDTO(
						0L, "", "", "", "", "", "", "", "", 
						new UsuarioDTO(), 
						new RolDTO(2, "", ""), 
						new PermisosDTO(0L, false, false, false, false, false, false, false)), 
				new RolDTO(2, "", ""), 
				new PermisosDTO(0L, false, false, false, false, false, false, false));
	}
	
	public void inicializaObjetoUsuarioOnOff() {
		usuarioOnOff	= new UsuarioDTO(
				0L, "", "", "", "", "", "", "", "", 
				new UsuarioDTO(
						0L, "", "", "", "", "", "", "", "", 
						new UsuarioDTO(), 
						new RolDTO(2, "", ""), 
						new PermisosDTO(0L, false, false, false, false, false, false, false)), 
				new RolDTO(2, "", ""), 
				new PermisosDTO(0L, false, false, false, false, false, false, false));
	}
	

	public void InicializaEstilosPorcentajes() {
		styleErrorPorcPE 		= "";
		styleErrorPorcF60 		= "";
		styleErrorPorcInfra 	= "";
		styleErrorPorcApl 		= "";
		styleErrorPorcPreATP 	= "";
		styleErrorPorcATP 		= "";
		styleErrorPorcRTO 		= "";
	}
	
	public int velificaCampos() {
		int numVacios = 0;
		
		if(porcentajes.getPorportempr() == 0) { 
			numVacios++;
			styleErrorPorcPE 	= STYLE_ERROR_VALUE;
		}
		if(porcentajes.getPorcF60() == 0) {
			numVacios++;
			styleErrorPorcF60 	= STYLE_ERROR_VALUE;
		}
		if(porcentajes.getPorcInfra() == 0) {
			numVacios++;
			styleErrorPorcInfra = STYLE_ERROR_VALUE;
		}
		if(porcentajes.getPorcAplicativo() == 0) {
			numVacios++;
			styleErrorPorcApl 	= STYLE_ERROR_VALUE;
		}
		if(porcentajes.getPorcPreATP() == 0) {
			numVacios++;
			styleErrorPorcPreATP = STYLE_ERROR_VALUE;
		}
		if(porcentajes.getPorcATP() == 0) {
			numVacios++;
			styleErrorPorcATP 	= STYLE_ERROR_VALUE;
		}
		if(porcentajes.getPorcRTO() == 0) {
			numVacios++;
			styleErrorPorcRTO 	= STYLE_ERROR_VALUE;
		}
		return numVacios;
	}
	
	public int validaValoresPorcentajes() {
		
		int validacion = 0;
		
		if(porcentajes.getPorcF60() <= porcentajes.getPorportempr()) {
			validacion++;
			styleErrorPorcPE 	= STYLE_ERROR_VALUE;
			msgError 			= Mensajes.F60_MENOR_QUE_PE;
		}
		
		if(porcentajes.getPorcInfra() <= porcentajes.getPorcF60()) {
			validacion++;
			styleErrorPorcInfra = STYLE_ERROR_VALUE;
			msgError 			= Mensajes.INFRA_MENOR_QUE_F60;
		}
		
		if(porcentajes.getPorcAplicativo() <= porcentajes.getPorcInfra()) {
			validacion++;
			styleErrorPorcApl 	= STYLE_ERROR_VALUE;
			msgError 			= Mensajes.APLICATIVO_MENOR_QUE_INFRA;
		}
		
		if(porcentajes.getPorcPreATP() <= porcentajes.getPorcAplicativo()) {
			validacion++;
			styleErrorPorcPreATP = STYLE_ERROR_VALUE;
			msgError 			= Mensajes.PRE_ATP_MENOR_QUE_APLICATIVO;
		}
		
		if(porcentajes.getPorcATP() <= porcentajes.getPorcPreATP()) {
			validacion++;
			styleErrorPorcATP 	= STYLE_ERROR_VALUE;
			msgError 			= Mensajes.ATP_MENOR_QUE_PRE_ATP;
		}
		
		if(porcentajes.getPorcRTO() <= porcentajes.getPorcATP()) {
			validacion++;
			styleErrorPorcRTO 	= STYLE_ERROR_VALUE;
			msgError 			= Mensajes.RTO_MENOR_QUE_ATP;
		}
		
		return validacion;
	}
	
	public void ActualizaPorcentajes() {
		startMsg();
		InicializaEstilosPorcentajes();
		pestaniaActiva 	= 2;
		if(velificaCampos() > 0) {
			msgError = Mensajes.PORCENTAJES_VACIOS;
		}else {			
			if(validaValoresPorcentajes() == 0) {
				if(catalogosService.ActualizaPorcentajes(porcentajes) == 1) {
					porcentajes = catalogosService.CargaPorcentajesAvance();
					msgSuccess = Mensajes.PORCENTAJES_ACTUALIZADOS_EXITO;
				}else {
					msgError = Mensajes.PORCENTAJES_ACTUALIZADOS_ERROR;
				}
			}
		}
		
	}
	
	/**
	 * Métodos Accesores
	 * @return
	 */
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

	public EstrategiaDTO getEstrategiaUpdate() {
		return estrategiaUpdate;
	}

	public void setEstrategiaUpdate(EstrategiaDTO estrategiaUpdate) {
		this.estrategiaUpdate = estrategiaUpdate;
	}

	public EstrategiaDTO getEstrategiaDelete() {
		return estrategiaDelete;
	}

	public void setEstrategiaDelete(EstrategiaDTO estrategiaDelete) {
		this.estrategiaDelete = estrategiaDelete;
	}

	public String getEstrategiaNew() {
		return estrategiaNew;
	}

	public void setEstrategiaNew(String estrategiaNew) {
		this.estrategiaNew = estrategiaNew;
	}

	public List<ResponsableDTO> getListResponsables() {
		return listResponsables;
	}

	public void setListResponsables(List<ResponsableDTO> listResponsables) {
		this.listResponsables = listResponsables;
	}

	public ResponsableDTO getResponsableUpdate() {
		return responsableUpdate;
	}

	public void setResponsableUpdate(ResponsableDTO responsableUpdate) {
		this.responsableUpdate = responsableUpdate;
	}

	public ResponsableDTO getResponsableDelete() {
		return responsableDelete;
	}

	public void setResponsableDelete(ResponsableDTO responsableDelete) {
		this.responsableDelete = responsableDelete;
	}

	public ResponsableDTO getNewResponsable() {
		return newResponsable;
	}

	public void setNewResponsable(ResponsableDTO newResponsable) {
		this.newResponsable = newResponsable;
	}

	public ResponsableDTO getResponsableOnOff() {
		return responsableOnOff;
	}

	public void setResponsableOnOff(ResponsableDTO responsableOnOff) {
		this.responsableOnOff = responsableOnOff;
	}

	public EstrategiaDTO getEstrategiaOnOff() {
		return estrategiaOnOff;
	}

	public void setEstrategiaOnOff(EstrategiaDTO estrategiaOnOff) {
		this.estrategiaOnOff = estrategiaOnOff;
	}

	public List<TipoAmbienteDTO> getListTipoAmbiente() {
		return listTipoAmbiente;
	}

	public void setListTipoAmbiente(List<TipoAmbienteDTO> listTipoAmbiente) {
		this.listTipoAmbiente = listTipoAmbiente;
	}

	public String getTipoAmbienteNew() {
		return tipoAmbienteNew;
	}

	public void setTipoAmbienteNew(String tipoAmbienteNew) {
		this.tipoAmbienteNew = tipoAmbienteNew;
	}

	public TipoAmbienteDTO getTipoAmbienteOnOff() {
		return tipoAmbienteOnOff;
	}

	public void setTipoAmbienteOnOff(TipoAmbienteDTO tipoAmbienteOnOff) {
		this.tipoAmbienteOnOff = tipoAmbienteOnOff;
	}

	public TipoAmbienteDTO getTipoAmbienteUpdate() {
		return tipoAmbienteUpdate;
	}

	public void setTipoAmbienteUpdate(TipoAmbienteDTO tipoAmbienteUpdate) {
		this.tipoAmbienteUpdate = tipoAmbienteUpdate;
	}

	public TipoAmbienteDTO getTipoAmbienteDelete() {
		return tipoAmbienteDelete;
	}

	public void setTipoAmbienteDelete(TipoAmbienteDTO tipoAmbienteDelete) {
		this.tipoAmbienteDelete = tipoAmbienteDelete;
	}

	public List<DireccionDTO> getListDirecciones() {
		return listDirecciones;
	}

	public void setListDirecciones(List<DireccionDTO> listDirecciones) {
		this.listDirecciones = listDirecciones;
	}

	public List<GerenciaDTO> getListGerencias() {
		return listGerencias;
	}

	public void setListGerencias(List<GerenciaDTO> listGerencias) {
		this.listGerencias = listGerencias;
	}

	public String getNewDireccion() {
		return newDireccion;
	}

	public void setNewDireccion(String newDireccion) {
		this.newDireccion = newDireccion;
	}

	public DireccionDTO getDireccionOnOff() {
		return direccionOnOff;
	}

	public void setDireccionOnOff(DireccionDTO direccionOnOff) {
		this.direccionOnOff = direccionOnOff;
	}

	public DireccionDTO getDireccionUpdate() {
		return direccionUpdate;
	}

	public void setDireccionUpdate(DireccionDTO direccionUpdate) {
		this.direccionUpdate = direccionUpdate;
	}

	public String getNewGerencia() {
		return newGerencia;
	}

	public void setNewGerencia(String newGerencia) {
		this.newGerencia = newGerencia;
	}

	public GerenciaDTO getGerenciaOnOff() {
		return gerenciaOnOff;
	}

	public void setGerenciaOnOff(GerenciaDTO gerenciaOnOff) {
		this.gerenciaOnOff = gerenciaOnOff;
	}

	public GerenciaDTO getGerenciaUpdate() {
		return gerenciaUpdate;
	}

	public void setGerenciaUpdate(GerenciaDTO gerenciaUpdate) {
		this.gerenciaUpdate = gerenciaUpdate;
	}

	public Long getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}

	public List<UsuarioDTO> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioDTO> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

	public UsuarioDTO getUsuarioFO() {
		return usuarioFO;
	}

	public void setUsuarioFO(UsuarioDTO usuarioFO) {
		this.usuarioFO = usuarioFO;
	}

	public int getPestaniaActiva() {
		return pestaniaActiva;
	}

	public UsuarioDTO getUsuarioUpdate() {
		return usuarioUpdate;
	}

	public void setUsuarioUpdate(UsuarioDTO usuarioUpdate) {
		this.usuarioUpdate = usuarioUpdate;
	}

	public UsuarioDTO getUsuarioOnOff() {
		return usuarioOnOff;
	}

	public void setUsuarioOnOff(UsuarioDTO usuarioOnOff) {
		this.usuarioOnOff = usuarioOnOff;
	}

	public PorcAvanceDTO getPorcentajes() {
		return porcentajes;
	}

	public void setPorcentajes(PorcAvanceDTO porcentajes) {
		this.porcentajes = porcentajes;
	}

	public String getStyleErrorPorcPE() {
		return styleErrorPorcPE;
	}

	public void setStyleErrorPorcPE(String styleErrorPorcPE) {
		this.styleErrorPorcPE = styleErrorPorcPE;
	}

	public String getStyleErrorPorcF60() {
		return styleErrorPorcF60;
	}

	public void setStyleErrorPorcF60(String styleErrorPorcF60) {
		this.styleErrorPorcF60 = styleErrorPorcF60;
	}

	public String getStyleErrorPorcInfra() {
		return styleErrorPorcInfra;
	}

	public void setStyleErrorPorcInfra(String styleErrorPorcInfra) {
		this.styleErrorPorcInfra = styleErrorPorcInfra;
	}

	public String getStyleErrorPorcApl() {
		return styleErrorPorcApl;
	}

	public void setStyleErrorPorcApl(String styleErrorPorcApl) {
		this.styleErrorPorcApl = styleErrorPorcApl;
	}

	public String getStyleErrorPorcPreATP() {
		return styleErrorPorcPreATP;
	}

	public void setStyleErrorPorcPreATP(String styleErrorPorcPreATP) {
		this.styleErrorPorcPreATP = styleErrorPorcPreATP;
	}

	public String getStyleErrorPorcATP() {
		return styleErrorPorcATP;
	}

	public void setStyleErrorPorcATP(String styleErrorPorcATP) {
		this.styleErrorPorcATP = styleErrorPorcATP;
	}

	public String getStyleErrorPorcRTO() {
		return styleErrorPorcRTO;
	}

	public void setStyleErrorPorcRTO(String styleErrorPorcRTO) {
		this.styleErrorPorcRTO = styleErrorPorcRTO;
	}
}
