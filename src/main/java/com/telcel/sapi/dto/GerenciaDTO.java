package com.telcel.sapi.dto;

public class GerenciaDTO {
	private Long 		idGerencia;
	private String 		gerencia;
	private DireccionDTO direccion;
	private String 		estatus;

	public GerenciaDTO() {
		super();
	}

	public GerenciaDTO(Long idGerencia, String gerencia, DireccionDTO direccion, String estatus) {
		super();
		this.idGerencia = idGerencia;
		this.gerencia = gerencia;
		this.direccion = direccion;
		this.estatus = estatus;
	}

	public String getGerencia() {
		return gerencia;
	}

	public void setGerencia(String gerencia) {
		this.gerencia = gerencia;
	}

	public DireccionDTO getDireccion() {
		return direccion;
	}

	public void setDireccion(DireccionDTO direccion) {
		this.direccion = direccion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public Long getIdGerencia() {
		return idGerencia;
	}

	public void setIdGerencia(Long idGerencia) {
		this.idGerencia = idGerencia;
	}
}
