package com.telcel.sapi.dto;

public class DireccionDTO {
	private Long 	idDireccion;
	private String 	direccion;
	private String	estatus;
	
	public DireccionDTO() {
		super();
	}

	public DireccionDTO(Long idDireccion, String direccion, String estatus) {
		super();
		this.idDireccion = idDireccion;
		this.direccion = direccion;
		this.estatus = estatus;
	}

	public Long getIdDireccion() {
		return idDireccion;
	}

	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
