package com.telcel.sapi.dto;

public class IncidenteDTO {
	
	private int 	idIncidente;
	private String	issueIncidente;
	private ResponsableDTO	responsable;
	private String	fechaInicio;
	private String	fechaResolucion;
	private String	detalles;
	private	String	etapa;
	private	String	estatus;
	
	public IncidenteDTO() {
		super();
	}

	public IncidenteDTO(int idIncidente, String issueIncidente, ResponsableDTO responsable, String fechaInicio,
			String fechaResolucion, String detalles, String etapa, String estatus) {
		super();
		this.idIncidente = idIncidente;
		this.issueIncidente = issueIncidente;
		this.responsable = responsable;
		this.fechaInicio = fechaInicio;
		this.fechaResolucion = fechaResolucion;
		this.detalles = detalles;
		this.etapa = etapa;
		this.estatus = estatus;
	}

	public int getIdIncidente() {
		return idIncidente;
	}

	public void setIdIncidente(int idIncidente) {
		this.idIncidente = idIncidente;
	}

	public String getIssueIncidente() {
		return issueIncidente;
	}

	public void setIssueIncidente(String issueIncidente) {
		this.issueIncidente = issueIncidente;
	}

	public ResponsableDTO getResponsable() {
		return responsable;
	}

	public void setResponsable(ResponsableDTO responsable) {
		this.responsable = responsable;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaResolucion() {
		return fechaResolucion;
	}

	public void setFechaResolucion(String fechaResolucion) {
		this.fechaResolucion = fechaResolucion;
	}

	public String getDetalles() {
		return detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	@Override
	public String toString() {
		return "IncidenteDTO [idIncidente=" + idIncidente + ", issueIncidente=" + issueIncidente + ", responsable="
				+ responsable + ", fechaInicio=" + fechaInicio + ", fechaResolucion=" + fechaResolucion + ", detalles="
				+ detalles + ", etapa=" + etapa + ", estatus=" + estatus + "]";
	}
}
