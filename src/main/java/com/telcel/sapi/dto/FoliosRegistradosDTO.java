package com.telcel.sapi.dto;

public class FoliosRegistradosDTO {
	private String nombreProyecto;
	private String integrador;
	private String folio;
	private String estatusProyecto;

	public FoliosRegistradosDTO() {
		super();
	}

	public FoliosRegistradosDTO(String nombreProyecto, String integrador, String folio, String estatusProyecto) {
		super();
		this.nombreProyecto = nombreProyecto;
		this.integrador = integrador;
		this.folio = folio;
		this.estatusProyecto = estatusProyecto;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public String getIntegrador() {
		return integrador;
	}

	public void setIntegrador(String integrador) {
		this.integrador = integrador;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getEstatusProyecto() {
		return estatusProyecto;
	}

	public void setEstatusProyecto(String estatusProyecto) {
		this.estatusProyecto = estatusProyecto;
	}
}
