package com.telcel.sapi.dto;

public class AsignacionDTO {
	
	private int idAsignacion;
	private int idUsuario;
	private String fecAsignacion;
	private String tipoAsignacion;
	
	public AsignacionDTO() {
		super();
	}

	public AsignacionDTO(int idAsignacion, int idUsuario, String fecAsignacion, String tipoAsignacion) {
		super();
		this.idAsignacion = idAsignacion;
		this.idUsuario = idUsuario;
		this.fecAsignacion = fecAsignacion;
		this.tipoAsignacion = tipoAsignacion;
	}

	public int getIdAsignacion() {
		return idAsignacion;
	}

	public void setIdAsignacion(int idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getFecAsignacion() {
		return fecAsignacion;
	}

	public void setFecAsignacion(String fecAsignacion) {
		this.fecAsignacion = fecAsignacion;
	}

	public String getTipoAsignacion() {
		return tipoAsignacion;
	}

	public void setTipoAsignacion(String tipoAsignacion) {
		this.tipoAsignacion = tipoAsignacion;
	}
}
