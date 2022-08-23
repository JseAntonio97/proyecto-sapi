package com.telcel.sapi.dto;

import java.util.List;

public class ProyectoDTO {
	
	private Long	idProyecto;
	private String	identificadorInterno;
	private String	integrador;
	private String	nombreProyecto;
	private String	descripcion;
	private String	solicitante;
	private String	direccion;
	private String	gerencia;
	private GerenciaDTO	gerenciaDto;
	private int		porcAvanceActual;
	private String	porcAvanceAnterior;
	private String	faseActual;
	private String	faseSiguiente;
	private String	comentariosGrls;
	private String	estatus;
	private String	fechaRegistro;
	private String	hraRegistro;
	private Long	marcaLeido;

	private List<IncidenteDTO> 			listIncidentes;
	private EstrategiaDTO				estrategia;
	private PortafolioEmpresarialDTO 	portafolioEmp;
	private List<AmbienteDTO> 			listAmbientes;
	private AsignacionDTO				asignacion;
	private UsuarioDTO					usuarioAsignado;
	private ProyectoF60DTO				proyectoF60;
	private SeguimientoInfraDTO			seguimientoInfra;
	private SeguimientoPreATPDTO		preAtp;
	private SeguimientoRTO				SegRTO;
	private SeguimientoATP				segATP;
	private AplicativoDTO				aplicativo;
	
	public ProyectoDTO() {
		super();
	}

	public ProyectoDTO(Long idProyecto, String identificadorInterno, String integrador, String nombreProyecto,
			String descripcion, String solicitante, String direccion, String gerencia, GerenciaDTO gerenciaDto,
			int porcAvanceActual, String porcAvanceAnterior, String faseActual, String faseSiguiente,
			String comentariosGrls, String estatus, String fechaRegistro, String hraRegistro, Long marcaLeido,
			List<IncidenteDTO> listIncidentes, EstrategiaDTO estrategia, PortafolioEmpresarialDTO portafolioEmp,
			List<AmbienteDTO> listAmbientes, AsignacionDTO asignacion, UsuarioDTO usuarioAsignado,
			ProyectoF60DTO proyectoF60, SeguimientoInfraDTO seguimientoInfra, SeguimientoPreATPDTO preAtp,
			SeguimientoRTO segRTO, SeguimientoATP segATP, AplicativoDTO aplicativo) {
		super();
		this.idProyecto = idProyecto;
		this.identificadorInterno = identificadorInterno;
		this.integrador = integrador;
		this.nombreProyecto = nombreProyecto;
		this.descripcion = descripcion;
		this.solicitante = solicitante;
		this.direccion = direccion;
		this.gerencia = gerencia;
		this.gerenciaDto = gerenciaDto;
		this.porcAvanceActual = porcAvanceActual;
		this.porcAvanceAnterior = porcAvanceAnterior;
		this.faseActual = faseActual;
		this.faseSiguiente = faseSiguiente;
		this.comentariosGrls = comentariosGrls;
		this.estatus = estatus;
		this.fechaRegistro = fechaRegistro;
		this.hraRegistro = hraRegistro;
		this.marcaLeido = marcaLeido;
		this.listIncidentes = listIncidentes;
		this.estrategia = estrategia;
		this.portafolioEmp = portafolioEmp;
		this.listAmbientes = listAmbientes;
		this.asignacion = asignacion;
		this.usuarioAsignado = usuarioAsignado;
		this.proyectoF60 = proyectoF60;
		this.seguimientoInfra = seguimientoInfra;
		this.preAtp = preAtp;
		SegRTO = segRTO;
		this.segATP = segATP;
		this.aplicativo = aplicativo;
	}

	public Long getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Long idProyecto) {
		this.idProyecto = idProyecto;
	}

	public String getIdentificadorInterno() {
		return identificadorInterno;
	}

	public void setIdentificadorInterno(String identificadorInterno) {
		this.identificadorInterno = identificadorInterno;
	}

	public String getIntegrador() {
		return integrador;
	}

	public void setIntegrador(String integrador) {
		this.integrador = integrador;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getGerencia() {
		return gerencia;
	}

	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}

	public GerenciaDTO getGerenciaDto() {
		return gerenciaDto;
	}

	public void setGerenciaDto(GerenciaDTO gerenciaDto) {
		this.gerenciaDto = gerenciaDto;
	}

	public int getPorcAvanceActual() {
		return porcAvanceActual;
	}

	public void setPorcAvanceActual(int porcAvanceActual) {
		this.porcAvanceActual = porcAvanceActual;
	}

	public String getPorcAvanceAnterior() {
		return porcAvanceAnterior;
	}

	public void setPorcAvanceAnterior(String porcAvanceAnterior) {
		this.porcAvanceAnterior = porcAvanceAnterior;
	}

	public String getFaseActual() {
		return faseActual;
	}

	public void setFaseActual(String faseActual) {
		this.faseActual = faseActual;
	}

	public String getFaseSiguiente() {
		return faseSiguiente;
	}

	public void setFaseSiguiente(String faseSiguiente) {
		this.faseSiguiente = faseSiguiente;
	}

	public String getComentariosGrls() {
		return comentariosGrls;
	}

	public void setComentariosGrls(String comentariosGrls) {
		this.comentariosGrls = comentariosGrls;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getHraRegistro() {
		return hraRegistro;
	}

	public void setHraRegistro(String hraRegistro) {
		this.hraRegistro = hraRegistro;
	}

	public Long getMarcaLeido() {
		return marcaLeido;
	}

	public void setMarcaLeido(Long marcaLeido) {
		this.marcaLeido = marcaLeido;
	}

	public List<IncidenteDTO> getListIncidentes() {
		return listIncidentes;
	}

	public void setListIncidentes(List<IncidenteDTO> listIncidentes) {
		this.listIncidentes = listIncidentes;
	}

	public EstrategiaDTO getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(EstrategiaDTO estrategia) {
		this.estrategia = estrategia;
	}

	public PortafolioEmpresarialDTO getPortafolioEmp() {
		return portafolioEmp;
	}

	public void setPortafolioEmp(PortafolioEmpresarialDTO portafolioEmp) {
		this.portafolioEmp = portafolioEmp;
	}

	public List<AmbienteDTO> getListAmbientes() {
		return listAmbientes;
	}

	public void setListAmbientes(List<AmbienteDTO> listAmbientes) {
		this.listAmbientes = listAmbientes;
	}

	public AsignacionDTO getAsignacion() {
		return asignacion;
	}

	public void setAsignacion(AsignacionDTO asignacion) {
		this.asignacion = asignacion;
	}

	public UsuarioDTO getUsuarioAsignado() {
		return usuarioAsignado;
	}

	public void setUsuarioAsignado(UsuarioDTO usuarioAsignado) {
		this.usuarioAsignado = usuarioAsignado;
	}

	public ProyectoF60DTO getProyectoF60() {
		return proyectoF60;
	}

	public void setProyectoF60(ProyectoF60DTO proyectoF60) {
		this.proyectoF60 = proyectoF60;
	}

	public SeguimientoInfraDTO getSeguimientoInfra() {
		return seguimientoInfra;
	}

	public void setSeguimientoInfra(SeguimientoInfraDTO seguimientoInfra) {
		this.seguimientoInfra = seguimientoInfra;
	}

	public SeguimientoPreATPDTO getPreAtp() {
		return preAtp;
	}

	public void setPreAtp(SeguimientoPreATPDTO preAtp) {
		this.preAtp = preAtp;
	}

	public SeguimientoRTO getSegRTO() {
		return SegRTO;
	}

	public void setSegRTO(SeguimientoRTO segRTO) {
		SegRTO = segRTO;
	}

	public SeguimientoATP getSegATP() {
		return segATP;
	}

	public void setSegATP(SeguimientoATP segATP) {
		this.segATP = segATP;
	}

	public AplicativoDTO getAplicativo() {
		return aplicativo;
	}

	public void setAplicativo(AplicativoDTO aplicativo) {
		this.aplicativo = aplicativo;
	}

	@Override
	public String toString() {
		return "ProyectoDTO [idProyecto=" + idProyecto + ", identificadorInterno=" + identificadorInterno
				+ ", integrador=" + integrador + ", nombreProyecto=" + nombreProyecto + ", descripcion=" + descripcion
				+ ", solicitante=" + solicitante + ", direccion=" + direccion + ", gerencia=" + gerencia
				+ ", gerenciaDto=" + gerenciaDto + ", porcAvanceActual=" + porcAvanceActual + ", porcAvanceAnterior="
				+ porcAvanceAnterior + ", faseActual=" + faseActual + ", faseSiguiente=" + faseSiguiente
				+ ", comentariosGrls=" + comentariosGrls + ", estatus=" + estatus + ", fechaRegistro=" + fechaRegistro
				+ ", hraRegistro=" + hraRegistro + ", marcaLeido=" + marcaLeido + ", listIncidentes=" + listIncidentes
				+ ", estrategia=" + estrategia + ", portafolioEmp=" + portafolioEmp + ", listAmbientes=" + listAmbientes
				+ ", asignacion=" + asignacion + ", usuarioAsignado=" + usuarioAsignado + ", proyectoF60=" + proyectoF60
				+ ", seguimientoInfra=" + seguimientoInfra + ", preAtp=" + preAtp + ", SegRTO=" + SegRTO + ", segATP="
				+ segATP + ", aplicativo=" + aplicativo + "]";
	}
}
