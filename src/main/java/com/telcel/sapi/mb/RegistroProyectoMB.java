package com.telcel.sapi.mb;

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
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.PortafolioEmpresarialDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;
import com.telcel.sapi.service.Conversiones;
import com.telcel.sapi.service.ProyectoService;
import com.telcel.sapi.service.impl.CatalogosServiceImpl;
import com.telcel.sapi.service.impl.ConversionesImpl;
import com.telcel.sapi.service.impl.ProyectoServiceImpl;

@ManagedBean (name = "registroMB")
@ViewScoped
public class RegistroProyectoMB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4062914975109908542L;
	static final Logger LOG = Logger.getLogger(RegistroProyectoMB.class);
	
	private static final String STYLE_ERROR_VALUE 			= "boder-error";
	
	CatalogosService		catalogosService;
	Conversiones			conversiones;
	ProyectoService 		proyectoService;
	BitacoraDAO 			bitacoraService;
	
	UsuarioDTO 				usuarioDTO;
	
	private List<DireccionDTO> 	listDireccion;
	private List<GerenciaDTO>	listGerencia;
	
	private ProyectoDTO 	proyecto;
	
	private boolean 		checkActivatePE;
	private Date 			FecEntrada;
	private Date 			FecSalida;
	
	private String 	msgError;
	private String 	msgSuccess;
	private String 	msgWarnning;
	private String 	msgWarnningFolio;
	
	private String styleErrornombreProyecto;	
	private String styleErrorsolicitante;	
	private String styleErrordireccion;
	private String styleErrorgerencia;
	
	private Long 	idDireccion;
	private Long	idGerencia;

	private List<FoliosRegistradosDTO> listFoliosRegistrados;
	private int numFoliosExistente;
	
	
	@PostConstruct
	public void init() {
		//Valida el rol de acceso
		usuarioDTO = new UsuarioDTO();
		usuarioDTO = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		// ---- ----
		
		conversiones 		= new ConversionesImpl();
		bitacoraService 	= new BitacoraDAOImpl();
		catalogosService 	= new CatalogosServiceImpl();
		
		listDireccion 	= catalogosService.cargaCatDireccion();
		listGerencia	= new ArrayList<GerenciaDTO>();
		
		
		inicializa();
		iniciaMsg();
		iniciaStyles();
	}
	
	public void imprimeGerencia() {
		LOG.info(idGerencia);
	}
	
	public void cargaGerencias() {
		if(idDireccion == 0) {
			listGerencia.clear(); 
			idGerencia = 0L;
		}else {
			listGerencia	= catalogosService.cargaCatGerencia(idDireccion);
		}
	}
	
	public void guardar() {
		iniciaMsg();
		
		if(FecEntrada != null) {
			proyecto.getPortafolioEmp().setEntrada(conversiones.DateToString(FecEntrada));
		}else {
			proyecto.getPortafolioEmp().setEntrada(null);
		}
		
		if(FecSalida != null) {
			proyecto.getPortafolioEmp().setSalida(conversiones.DateToString(FecSalida));
		}else {
			proyecto.getPortafolioEmp().setSalida(null);
		}
		
//		RegistraProyecto();
		
		if(checkActivatePE) {
			if(proyecto.getPortafolioEmp().getAplica().equals("Si")) {
				
				if(proyecto.getPortafolioEmp().getFolio().equals("") || proyecto.getPortafolioEmp().getFolio().isEmpty()) {
					msgError = Mensajes.ERROR_FOLIO_VACIO;
				}else {
					
					if(FecEntrada == null || FecSalida == null) {
						msgError = Mensajes.ERROR_FECHAS_VACIAS;
					}else {
						RegistraProyecto();	
					}
					
				}
			}else {
				RegistraProyecto();
			}
		}else {
			RegistraProyecto();
		}
	}
	
	public void RegistraProyecto() {
		Long idProyecto = 0L;
		
		if(ValidaCamposRegistro() <= 0) {	
			proyecto.setGerenciaDto(new GerenciaDTO(idGerencia, "", null, ""));
			idProyecto = (long) proyectoService.RegistroProyecto(proyecto, checkActivatePE);
			if(idProyecto > 1) {
				msgSuccess = Mensajes.REGISTRO_PROYECTO_EXITOSO;
				bitacoraService.insertBitacora(usuarioDTO.getIdUsuario(), Actividades.REGISTRO_PROYECTO, "Alta de proyectos nuevos", "Registro de proyectos", idProyecto);
				inicializa();
			}else {
				LOG.error("Falló el registro");
				msgError = Mensajes.ERROR_OCURRIDO;
			}
			
		}else {			
			msgError = Mensajes.CAMPOS_VACIOS_ERROR;			
		}
	}
	
	public int ValidaCamposRegistro() {
		iniciaStyles();
        int camposVacios = 0;
		
		
		if(proyecto.getNombreProyecto().isEmpty()) {
			camposVacios ++;
			styleErrornombreProyecto = STYLE_ERROR_VALUE;
		}
		if(proyecto.getSolicitante().isEmpty()) {
			camposVacios ++;
			styleErrorsolicitante = STYLE_ERROR_VALUE;
		}
		if(idDireccion == 0) {
			camposVacios ++;
			styleErrordireccion = STYLE_ERROR_VALUE;
		}
		if(idGerencia == 0L) {
			camposVacios ++;
			styleErrorgerencia = STYLE_ERROR_VALUE;
		}
		return camposVacios;
	}
	
	public void iniciaStyles() {
		styleErrornombreProyecto	= "";
		styleErrorsolicitante	    = "";
	    styleErrordireccion         = "";
	    styleErrorgerencia          = "";		
	}
	
	public void iniciaMsg() {
		msgError 		= "";
		msgSuccess 		= "";
		msgWarnning		= "";
	}
	
	public void inicializa() {
		proyecto = new ProyectoDTO();
		proyecto.setPortafolioEmp(new PortafolioEmpresarialDTO());
		proyectoService = new ProyectoServiceImpl();
		checkActivatePE = false;
		FecEntrada 		= null;
		FecSalida 		= null;

		idGerencia = 0L;		
		idDireccion = 0L;
	}
	
	public void verificaFolioExistente() {
		iniciaMsg();
		listFoliosRegistrados = proyectoService.foliosRegistrados(proyecto.getPortafolioEmp().getFolio());
		numFoliosExistente = listFoliosRegistrados.size();
		
		if(numFoliosExistente != 0) {
			msgWarnningFolio = Mensajes.ERROR_FOLIO_REPETIDO + numFoliosExistente + Mensajes.PORYECTOS;
			 if(numFoliosExistente > 1) {
				 msgWarnningFolio = msgWarnningFolio + "s";
			 }
			 msgWarnning = Mensajes.PROYECTOS_FOLIO_REGISTRADOS;			
		}else {
			msgWarnningFolio = "";
		}
	}
	
	public void check() {
		LOG.info(checkActivatePE);
	}

	public ProyectoDTO getProyecto() {
		return proyecto;
	}
	public void setProyecto(ProyectoDTO proyecto) {
		this.proyecto = proyecto;
	}
	public boolean isCheckActivatePE() {
		return checkActivatePE;
	}
	public void setCheckActivatePE(boolean checkActivatePE) {
		this.checkActivatePE = checkActivatePE;
	}
	public String getMsgError() {
		return msgError;
	}
	public String getMsgSuccess() {
		return msgSuccess;
	}
	public Date getFecEntrada() {
		return FecEntrada;
	}
	public void setFecEntrada(Date fecEntrada) {
		FecEntrada = fecEntrada;
	}

	public Date getFecSalida() {
		return FecSalida;
	}

	public void setFecSalida(Date fecSalida) {
		FecSalida = fecSalida;
	}


	public String getStyleErrornombreProyecto() {
		return styleErrornombreProyecto;
	}

	public void setStyleErrornombreProyecto(String styleErrornombreProyecto) {
		this.styleErrornombreProyecto = styleErrornombreProyecto;
	}

	public String getStyleErrorsolicitante() {
		return styleErrorsolicitante;
	}

	public void setStyleErrorsolicitante(String styleErrorsolicitante) {
		this.styleErrorsolicitante = styleErrorsolicitante;
	}

	public String getStyleErrordireccion() {
		return styleErrordireccion;
	}

	public void setStyleErrordireccion(String styleErrordireccion) {
		this.styleErrordireccion = styleErrordireccion;
	}

	public String getStyleErrorgerencia() {
		return styleErrorgerencia;
	}

	public void setStyleErrorgerencia(String styleErrorgerencia) {
		this.styleErrorgerencia = styleErrorgerencia;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public void setMsgSuccess(String msgSuccess) {
		this.msgSuccess = msgSuccess;
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

	public Long getIdGerencia() {
		return idGerencia;
	}

	public void setIdGerencia(Long idGerencia) {
		this.idGerencia = idGerencia;
	}

	public Long getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
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

	public String getMsgWarnningFolio() {
		return msgWarnningFolio;
	}

	public void setMsgWarnningFolio(String msgWarnningFolio) {
		this.msgWarnningFolio = msgWarnningFolio;
	}
}
