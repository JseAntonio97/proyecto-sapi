package com.telcel.sapi.dto;

public class ProyectoF60DTO {
	private int			idProyectoF60;
	private String 		fechaInicio;
	private String 		fechaCompromiso;
	private String 		fechaFin;
	private boolean		cuentaConInfraestructura;
	private String		estatus;
	private boolean		sinFechas;
	private String		responsable;
	private JefeF60DTO 	jefeF60;
	private TipoF60DTO 	tipoF60;
	
	public ProyectoF60DTO() {
		super();
	}

	public ProyectoF60DTO(int idProyectoF60, String fechaInicio, String fechaCompromiso, String fechaFin,
			boolean cuentaConInfraestructura, String estatus, boolean sinFechas, String responsable, JefeF60DTO jefeF60,
			TipoF60DTO tipoF60) {
		super();
		this.idProyectoF60 = idProyectoF60;
		this.fechaInicio = fechaInicio;
		this.fechaCompromiso = fechaCompromiso;
		this.fechaFin = fechaFin;
		this.cuentaConInfraestructura = cuentaConInfraestructura;
		this.estatus = estatus;
		this.sinFechas = sinFechas;
		this.responsable = responsable;
		this.jefeF60 = jefeF60;
		this.tipoF60 = tipoF60;
	}

	public int getIdProyectoF60() {
		return idProyectoF60;
	}

	public void setIdProyectoF60(int idProyectoF60) {
		this.idProyectoF60 = idProyectoF60;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaCompromiso() {
		return fechaCompromiso;
	}

	public void setFechaCompromiso(String fechaCompromiso) {
		this.fechaCompromiso = fechaCompromiso;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isCuentaConInfraestructura() {
		return cuentaConInfraestructura;
	}

	public void setCuentaConInfraestructura(boolean cuentaConInfraestructura) {
		this.cuentaConInfraestructura = cuentaConInfraestructura;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public boolean isSinFechas() {
		return sinFechas;
	}

	public void setSinFechas(boolean sinFechas) {
		this.sinFechas = sinFechas;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public JefeF60DTO getJefeF60() {
		return jefeF60;
	}

	public void setJefeF60(JefeF60DTO jefeF60) {
		this.jefeF60 = jefeF60;
	}

	public TipoF60DTO getTipoF60() {
		return tipoF60;
	}

	public void setTipoF60(TipoF60DTO tipoF60) {
		this.tipoF60 = tipoF60;
	}
}
