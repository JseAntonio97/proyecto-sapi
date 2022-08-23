package com.telcel.sapi.dto;

public class PortafolioEmpresarialDTO {
	
	private int		idSeguimiento;
	private String 	aplica;
	private String 	folio;
	private String 	entrada;
	private String 	salida;
	private int count;
	
	public PortafolioEmpresarialDTO() {
		super();
	}

	public PortafolioEmpresarialDTO(int idSeguimiento, String aplica, String folio, String entrada, String salida) {
		super();
		this.idSeguimiento = idSeguimiento;
		this.aplica = aplica;
		this.folio = folio;
		this.entrada = entrada;
		this.salida = salida;
	
	}

	public int getIdSeguimiento() {
		return idSeguimiento;
	}

	public void setIdSeguimiento(int idSeguimiento) {
		this.idSeguimiento = idSeguimiento;
	}

	public String getAplica() {
		return aplica;
	}

	public void setAplica(String aplica) {
		this.aplica = aplica;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getSalida() {
		return salida;
	}

	public void setSalida(String salida) {
		this.salida = salida;
	}
	

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "PortafolioEmpresarialDTO [idSeguimiento=" + idSeguimiento + ", aplica=" + aplica + ", folio=" + folio
				+ ", entrada=" + entrada + ", salida=" + salida + "]";
	}
	
}
