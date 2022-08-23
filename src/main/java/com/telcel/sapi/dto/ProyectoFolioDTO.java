package com.telcel.sapi.dto;

public class ProyectoFolioDTO {
	private String folio;
	private String nombreProyeco;
	
	public ProyectoFolioDTO() {
		super();
	}

	public ProyectoFolioDTO(String folio, String nombreProyeco) {
		super();
		this.folio = folio;
		this.nombreProyeco = nombreProyeco;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getNombreProyeco() {
		return nombreProyeco;
	}

	public void setNombreProyeco(String nombreProyeco) {
		this.nombreProyeco = nombreProyeco;
	}
}
