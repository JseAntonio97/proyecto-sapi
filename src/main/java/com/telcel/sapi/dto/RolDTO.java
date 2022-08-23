package com.telcel.sapi.dto;

public class RolDTO {
	
	private int idRol;
	private String rol;
	private String descripcion;

	public RolDTO() {
		super();
	}

	public RolDTO(int idRol, String rol, String descripcion) {
		super();
		this.idRol = idRol;
		this.rol = rol;
		this.descripcion = descripcion;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "RolDTO [idRol=" + idRol + ", rol=" + rol + ", descripcion=" + descripcion + "]";
	}

}
