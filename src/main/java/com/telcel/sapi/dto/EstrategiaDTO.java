package com.telcel.sapi.dto;

public class EstrategiaDTO {
	private int 	idEstrategia;
	private String 	estrategia;
	private String	estatus;
	
	public EstrategiaDTO() {
		super();
	}

	public EstrategiaDTO(int idEstrategia, String estrategia, String estatus) {
		super();
		this.idEstrategia = idEstrategia;
		this.estrategia = estrategia;
		this.estatus = estatus;
	}

	public int getIdEstrategia() {
		return idEstrategia;
	}

	public void setIdEstrategia(int idEstrategia) {
		this.idEstrategia = idEstrategia;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	@Override
	public String toString() {
		return "EstrategiaDTO [idEstrategia=" + idEstrategia + ", estrategia=" + estrategia + ", estatus=" + estatus
				+ "]";
	}

}
