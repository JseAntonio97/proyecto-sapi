package com.telcel.sapi.dto;

public class JefeF60DTO {
	private int 	idJefeF60;
	private String 	nombre;
	private String	estatus;
	
	public JefeF60DTO() {
		super();
	}

	public JefeF60DTO(int idJefeF60, String nombre, String estatus) {
		super();
		this.idJefeF60 = idJefeF60;
		this.nombre = nombre;
		this.estatus = estatus;
	}

	public int getIdJefeF60() {
		return idJefeF60;
	}

	public void setIdJefeF60(int idJefeF60) {
		this.idJefeF60 = idJefeF60;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
