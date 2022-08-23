package com.telcel.sapi.dto;

public class AsignacionesDTO {
	private String	usuario;
	private int		numeroAsignaciones;
	
	public AsignacionesDTO() {
		super();
	}

	public AsignacionesDTO(String usuario, int numeroAsignaciones) {
		super();
		this.usuario = usuario;
		this.numeroAsignaciones = numeroAsignaciones;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getNumeroAsignaciones() {
		return numeroAsignaciones;
	}

	public void setNumeroAsignaciones(int numeroAsignaciones) {
		this.numeroAsignaciones = numeroAsignaciones;
	}
}
