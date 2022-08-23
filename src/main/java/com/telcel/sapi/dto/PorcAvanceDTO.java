package com.telcel.sapi.dto;

public class PorcAvanceDTO {
	
	private int idPorcentaje;
	private int porportempr;
	private int porcF60;
	private int porcInfra;
	private int porcAplicativo;
	private int porcPreATP;
	private int porcATP;
	private int porcRTO;

	public PorcAvanceDTO() {
		super();
	}

	public PorcAvanceDTO(int idPorcentaje, int porportempr, int porcF60, int porcInfra, int porcAplicativo,
			int porcPreATP, int porcATP, int porcRTO) {
		super();
		this.idPorcentaje = idPorcentaje;
		this.porportempr = porportempr;
		this.porcF60 = porcF60;
		this.porcInfra = porcInfra;
		this.porcAplicativo = porcAplicativo;
		this.porcPreATP = porcPreATP;
		this.porcATP = porcATP;
		this.porcRTO = porcRTO;
	}

	public int getIdPorcentaje() {
		return idPorcentaje;
	}

	public void setIdPorcentaje(int idPorcentaje) {
		this.idPorcentaje = idPorcentaje;
	}

	public int getPorportempr() {
		return porportempr;
	}

	public void setPorportempr(int porportempr) {
		this.porportempr = porportempr;
	}

	public int getPorcF60() {
		return porcF60;
	}

	public void setPorcF60(int porcF60) {
		this.porcF60 = porcF60;
	}

	public int getPorcInfra() {
		return porcInfra;
	}

	public void setPorcInfra(int porcInfra) {
		this.porcInfra = porcInfra;
	}

	public int getPorcAplicativo() {
		return porcAplicativo;
	}

	public void setPorcAplicativo(int porcAplicativo) {
		this.porcAplicativo = porcAplicativo;
	}

	public int getPorcPreATP() {
		return porcPreATP;
	}

	public void setPorcPreATP(int porcPreATP) {
		this.porcPreATP = porcPreATP;
	}

	public int getPorcATP() {
		return porcATP;
	}

	public void setPorcATP(int porcATP) {
		this.porcATP = porcATP;
	}

	public int getPorcRTO() {
		return porcRTO;
	}

	public void setPorcRTO(int porcRTO) {
		this.porcRTO = porcRTO;
	}
}
