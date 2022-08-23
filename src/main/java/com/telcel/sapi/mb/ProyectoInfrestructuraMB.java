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
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ProyectoFolioDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.Conversiones;
import com.telcel.sapi.service.ProyectoInfrestructuraService;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;
import com.telcel.sapi.service.impl.ConversionesImpl;
import com.telcel.sapi.service.impl.ProyectoInfrestructuraServiceImpl;


@ManagedBean (name = "infraestructuraRegMB")
@ViewScoped
public class ProyectoInfrestructuraMB implements Serializable {

	/**
	 * Desarrollador Por Ing. Christian Brandon Neri Sanchez 14-02-2022
	 */
	private static final long serialVersionUID = -8042412816930218065L;
	static final Logger LOG = Logger.getLogger(ProyectoInfrestructuraMB.class);
	
	private static final String STYLE_ERROR_VALUE 			= "boder-error";
	private static final String PAGINA_PROYECTO_SEGUIMIENTO = "proyecto-seguimiento.html";
	
	ProyectoInfrestructuraService proyectoinfrestructuraService;
	 
	BitacoraDAO 	bitacoraService;
	Conversiones	conversiones;
	UsuarioDTO 		usuarioDTO;
	CatalogosService catalogoServ;
	
	private List<AmbienteDTO> listAmbiente;
	private List<TipoAmbienteDTO> listTipoAmbiente;
	
	private ProyectoDTO proyectoAddInfra;
	
	private AmbienteDTO ambienteAdd;
	private AmbienteDTO ambienteUpdate;
	private AmbienteDTO ambienteDelete;
	
	private int numeroAmbientes;
	
	private List<ProyectoFolioDTO> listfolioProyHost;
	private List<ProyectoFolioDTO> listfolioProyIp;
	private int numHostRepetidos;
	private int numIpRepetidos;

	private String msgError;
	private String msgSuccess;
	private String msgWarningHost;
	private String msgWarningIp;
	
	private String msgHost;
	private String msgIP;
	private String msgHostUp;
	private String msgIPUp;

	private String styleErrorCpu;	
	private String styleErrorMemory;	
	private String styleErrorHdd;	
	private String styleErrorHost;
	private String styleErrorIp;
	private String styleErrorFec;	

	private String styleWarnHost;
	private String styleWarnIp;
	
	private String styleErrorHostUp;
	private String styleErrorIpUp;
	
	private Date fechaEntregaEquipo;

	@PostConstruct
	public void init() {
		proyectoinfrestructuraService = new ProyectoInfrestructuraServiceImpl();
		usuarioDTO		 	= new UsuarioDTO();
		usuarioDTO 			= (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");		
		proyectoAddInfra 	= (ProyectoDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("proyectoAddInfra");
		bitacoraService 	= new BitacoraDAOImpl();
		catalogoServ		= new CatalogosServiceImpl();
		
		listAmbiente		= new ArrayList<AmbienteDTO>();
		listTipoAmbiente	= new ArrayList<TipoAmbienteDTO>();
		conversiones		= new ConversionesImpl();
		iniciaAmbienteAdd();
		iniciaAmbienteUpdate();
		iniciaAmbienteDelete();
		
		numeroAmbientes = 0;
		
		listTipoAmbiente = catalogoServ.CatListTiposAmbientesActivos();
		
		cargaAmbientes(proyectoAddInfra.getIdProyecto());
		
		iniciaMsg();
		iniciaStyles();
		iniciaContadores();
		
		listfolioProyHost = new ArrayList<ProyectoFolioDTO>();
		listfolioProyIp	  = new ArrayList<ProyectoFolioDTO>();
	}
	 
	public void RegistraAmbiente() {
		iniciaMsg();
		
		if(ValidaCampos() <= 0) {
			
			if(proyectoinfrestructuraService.RegistroInfrestructura(proyectoAddInfra, ambienteAdd) == 1) {
				msgSuccess = Mensajes.AMBIENTE_REGISTRO_EXITO;	
				cargaAmbientes(proyectoAddInfra.getIdProyecto());
				iniciaAmbienteAdd();
				fechaEntregaEquipo = null;
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_AMBIENTE, "Registro de ambiente para " + proyectoAddInfra.getNombreProyecto(), "Infraestructura", proyectoAddInfra.getIdProyecto());
				iniciaContadores();
			}else {
				msgError = Mensajes.AMBIENTE_ERROR_GUARDAR;
			}
			
		}else {			
			msgError = Mensajes.CAMPOS_VACIOS_ERROR;			
		}
		
		
	}
	
	public int ValidaCampos() {
		iniciaStyles();
		int camposVacios = 0;
		
		if(ambienteAdd.getCpu() == 0) {
			camposVacios ++;
			styleErrorCpu = STYLE_ERROR_VALUE;
		}
		
		if(ambienteAdd.getMemoria() == 0) {
			camposVacios++;
			styleErrorMemory = STYLE_ERROR_VALUE;
		}
		
		if(ambienteAdd.getDiscoDuro() == 0) {
			camposVacios++;
			styleErrorHdd = STYLE_ERROR_VALUE;
		}
		
		if(ambienteAdd.getHostName().isEmpty()) {
			camposVacios++;
			styleErrorHost = STYLE_ERROR_VALUE;
		}
		
		if(ambienteAdd.getIp().isEmpty()) {
			camposVacios++;
			styleErrorIp = STYLE_ERROR_VALUE;
		}
		
		return camposVacios;
	}
	
	public void AsignaFechaEntrega() {
		if(fechaEntregaEquipo != null) {
			ambienteAdd.setEntregaEquipo(conversiones.DateToString(fechaEntregaEquipo));
		}else if(fechaEntregaEquipo == null) {
			ambienteAdd.setEntregaEquipo(null);
		}
	}
	
	public void cargaAmbientes(Long idProyecto) {
		listAmbiente = proyectoinfrestructuraService.CargaAmbientes(idProyecto);
		numeroAmbientes = listAmbiente.size();
	}
	
	public void VerificaHost() {
		iniciaContadores();
		styleErrorHost 	= "";
		msgHost 		= "";
		
		listfolioProyHost = proyectoinfrestructuraService.buscaFoliosByHostName(ambienteAdd.getHostName());
		numHostRepetidos = listfolioProyHost.size();
		
		if(numHostRepetidos != 0) {
			styleErrorHost = STYLE_ERROR_VALUE;
			msgHost = Mensajes.AMBIENTE_HOST_ERROR + numHostRepetidos + " proyecto(s)";
			msgWarningHost = Mensajes.AMBIENTE_HOST_ERROR;
		}else {
			styleErrorHost = "";
			msgWarningHost = "";
		}
		
	}
	
	public void VerificaIp() {
		iniciaContadores();
		styleErrorIp	= "";
		msgIP			= "";
		
		listfolioProyIp = proyectoinfrestructuraService.buscaFoliosByIp(ambienteAdd.getIp());
		numIpRepetidos = listfolioProyIp.size();
		
		if(numIpRepetidos != 0) {
			styleErrorIp = STYLE_ERROR_VALUE;
			msgIP = Mensajes.AMBIENTE_IP_ERROR + numIpRepetidos + " proyecto(s)";
			msgWarningIp = Mensajes.AMBIENTE_IP_ERROR;
		}else {
			styleErrorIp = "";
			msgWarningIp = "";
		}
		
	}
	
	//Verificación del modal	
	
	public void AsignaFechaEntregaUp() {
		if(ambienteUpdate.getEntregaEquipoDate() != null) {
			ambienteUpdate.setEntregaEquipo(conversiones.DateToString(ambienteUpdate.getEntregaEquipoDate()));
		}else if(ambienteUpdate.getEntregaEquipoDate() == null) {
			ambienteUpdate.setEntregaEquipo(null);
		}
	}
	
	public void VerificaHostUp() {
		styleErrorHostUp 	= "";
		msgHostUp 		= "";
		if(proyectoinfrestructuraService.buscaHostNameUpdate(ambienteUpdate)) {
			styleErrorHostUp = STYLE_ERROR_VALUE;
			msgHostUp = Mensajes.AMBIENTE_HOST_ERROR;
		}else {
			styleErrorHostUp = "";
		}
	}
	
	public void VerificaIpUp() {
		styleErrorIp	= "";
		msgIPUp			= "";
		if(proyectoinfrestructuraService.buscaDireccionIPUpdate(ambienteUpdate)) {
			styleErrorIpUp = STYLE_ERROR_VALUE;
			msgIPUp = Mensajes.AMBIENTE_IP_ERROR;
		}else {
			styleErrorIpUp = "";
		}
	}
	
	public void ActualizaAmbiente() {
		iniciaMsg();
		if(proyectoinfrestructuraService.UpdateInfrestructura(ambienteUpdate) == 1) {
			msgSuccess = Mensajes.AMBIENTE_ACTUALIZACION_EXITO;	
			cargaAmbientes(proyectoAddInfra.getIdProyecto());
			iniciaAmbienteUpdate();
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.AGREGAR_AMBIENTE, "Registro de ambiente para " + proyectoAddInfra.getNombreProyecto(), "Infraestructura", proyectoAddInfra.getIdProyecto());
		}else {
			msgError = Mensajes.AMBIENTE_ACTUALIZACION_ERROR;
		}
	}
	
	public void eliminarAmbiente(){
		iniciaMsg();
		
		if(proyectoinfrestructuraService.DeleteInfrestructura(ambienteDelete) == 1) {
			msgSuccess = Mensajes.AMBIENTE_ELIMINACION_EXITO;
			cargaAmbientes(proyectoAddInfra.getIdProyecto());
			bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.ELIMINA_AMBIENTE, "Eliminó el ambiente " + ambienteDelete.getHostName()+ " para el proyecto " + proyectoAddInfra.getNombreProyecto(), "Infraestructura", proyectoAddInfra.getIdProyecto());
			
		}else {
			msgError = Mensajes.AMBIENTE_ELIMINACION_ERROR + ambienteDelete.getHostName();
			cargaAmbientes(proyectoAddInfra.getIdProyecto());
		}
	}
		
	public void iniciaMsg() {
		msgWarningHost	= "";
		msgWarningIp	= "";
		msgError 	= "";
		msgSuccess 	= "";
		msgHost 	= "";
		msgIP		= "";
		styleErrorHost 	= "";
		styleErrorIp	= "";
		styleErrorHostUp= "";
		styleErrorIpUp	= "";
	}
	
	public void iniciaStyles() {
		styleErrorCpu 		= "";	
		styleErrorMemory 	= "";
		styleErrorHdd 		= "";
		styleErrorHost 		= "";
		styleErrorIp 		= "";
		styleErrorFec 		= "";
	}
	
	public void iniciaContadores() {
		numHostRepetidos 	= 0;
		numIpRepetidos		= 0;
	}
	
	public void iniciaAmbienteAdd() {
		ambienteAdd = new AmbienteDTO();
		ambienteAdd.setTipoAmbiente(new TipoAmbienteDTO(0, "", ""));
	}
	
	public void iniciaAmbienteUpdate() {
		ambienteUpdate = new AmbienteDTO();
		ambienteUpdate.setTipoAmbiente(new TipoAmbienteDTO(0, "", ""));
	}
	
	public void iniciaAmbienteDelete() {
		ambienteDelete	= new AmbienteDTO();
		ambienteDelete.setTipoAmbiente(new TipoAmbienteDTO(0, "", ""));
	}
	
	//Redireccionamiento para Agregar infraestructura
	public void seguimientoProyectoRedirect() {
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("proyectoSeguimiento", proyectoAddInfra);
		FacesContext contex = FacesContext.getCurrentInstance();
		try {
			contex.getExternalContext().redirect(PAGINA_PROYECTO_SEGUIMIENTO);
		} catch (IOException e) {
			LOG.error("Error al redireccionara a la página de adición de la Infraestructura del proyecto: " + e);
		}
 	}

	public ProyectoDTO getProyectoAddInfra() {
		return proyectoAddInfra;
	}

	public void setProyectoAddInfra(ProyectoDTO proyectoAddInfra) {
		this.proyectoAddInfra = proyectoAddInfra;
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

	public AmbienteDTO getAmbienteAdd() {
		return ambienteAdd;
	}

	public void setAmbienteAdd(AmbienteDTO ambienteAdd) {
		this.ambienteAdd = ambienteAdd;
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

	public Date getFechaEntregaEquipo() {
		return fechaEntregaEquipo;
	}

	public void setFechaEntregaEquipo(Date fechaEntregaEquipo) {
		this.fechaEntregaEquipo = fechaEntregaEquipo;
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

	public AmbienteDTO getAmbienteUpdate() {
		return ambienteUpdate;
	}

	public void setAmbienteUpdate(AmbienteDTO ambienteUpdate) {
		this.ambienteUpdate = ambienteUpdate;
	}

	public String getMsgHostUp() {
		return msgHostUp;
	}

	public void setMsgHostUp(String msgHostUp) {
		this.msgHostUp = msgHostUp;
	}

	public String getMsgIPUp() {
		return msgIPUp;
	}

	public void setMsgIPUp(String msgIPUp) {
		this.msgIPUp = msgIPUp;
	}

	public String getStyleErrorHostUp() {
		return styleErrorHostUp;
	}

	public void setStyleErrorHostUp(String styleErrorHostUp) {
		this.styleErrorHostUp = styleErrorHostUp;
	}

	public String getStyleErrorIpUp() {
		return styleErrorIpUp;
	}

	public void setStyleErrorIpUp(String styleErrorIpUp) {
		this.styleErrorIpUp = styleErrorIpUp;
	}

	public String getStyleErrorCpu() {
		return styleErrorCpu;
	}

	public String getStyleErrorMemory() {
		return styleErrorMemory;
	}

	public String getStyleErrorHdd() {
		return styleErrorHdd;
	}

	public String getStyleErrorFec() {
		return styleErrorFec;
	}

	public String getStyleErrorHost() {
		return styleErrorHost;
	}

	public String getStyleErrorIp() {
		return styleErrorIp;
	}

	public AmbienteDTO getAmbienteDelete() {
		return ambienteDelete;
	}

	public void setAmbienteDelete(AmbienteDTO ambienteDelete) {
		this.ambienteDelete = ambienteDelete;
	}

	public String getStyleWarnHost() {
		return styleWarnHost;
	}

	public String getStyleWarnIp() {
		return styleWarnIp;
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

	public List<ProyectoFolioDTO> getListfolioProyHost() {
		return listfolioProyHost;
	}

	public void setListfolioProyHost(List<ProyectoFolioDTO> listfolioProyHost) {
		this.listfolioProyHost = listfolioProyHost;
	}

	public List<ProyectoFolioDTO> getListfolioProyIp() {
		return listfolioProyIp;
	}

	public void setListfolioProyIp(List<ProyectoFolioDTO> listfolioProyIp) {
		this.listfolioProyIp = listfolioProyIp;
	}

	public String getMsgWarningHost() {
		return msgWarningHost;
	}

	public String getMsgWarningIp() {
		return msgWarningIp;
	}

	public List<TipoAmbienteDTO> getListTipoAmbiente() {
		return listTipoAmbiente;
	}

	public void setListTipoAmbiente(List<TipoAmbienteDTO> listTipoAmbiente) {
		this.listTipoAmbiente = listTipoAmbiente;
	}
}
