package com.telcel.sapi.dto;

public class TipoF60DTO {
	private int 	idTipoF60;
	private String 	tipoF60;
	private String 	estatus;
	
	public TipoF60DTO() {
		super();
	}

	public TipoF60DTO(int idTipoF60, String tipoF60, String estatus) {
		super();
		this.idTipoF60 = idTipoF60;
		this.tipoF60 = tipoF60;
		this.estatus = estatus;
	}

	public int getIdTipoF60() {
		return idTipoF60;
	}

	public void setIdTipoF60(int idTipoF60) {
		this.idTipoF60 = idTipoF60;
	}

	public String getTipoF60() {
		return tipoF60;
	}

	public void setTipoF60(String tipoF60) {
		this.tipoF60 = tipoF60;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
