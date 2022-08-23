package com.telcel.sapi.dto;

public class TipoAmbienteDTO {
	private int idTipoAmbiente;
	private String tipoAmbiente;
	private String estatus;
	
	public TipoAmbienteDTO() {
		super();
	}

	public TipoAmbienteDTO(int idTipoAmbiente, String tipoAmbiente, String estatus) {
		super();
		this.idTipoAmbiente = idTipoAmbiente;
		this.tipoAmbiente = tipoAmbiente;
		this.estatus = estatus;
	}

	public int getIdTipoAmbiente() {
		return idTipoAmbiente;
	}

	public void setIdTipoAmbiente(int idTipoAmbiente) {
		this.idTipoAmbiente = idTipoAmbiente;
	}

	public String getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(String tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
}
