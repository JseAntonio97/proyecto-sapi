package com.telcel.sapi.dto;

public class ResponsableDTO {
	
	private int 	idResponable;
	private String 	nomenclatura;
	private String 	area;
	private String 	responsable;
	private String 	estatus;
	
	public ResponsableDTO() {
		super();
	}

	public ResponsableDTO(int idResponable, String nomenclatura, String area, String responsable, String estatus) {
		super();
		this.idResponable = idResponable;
		this.nomenclatura = nomenclatura;
		this.area = area;
		this.responsable = responsable;
		this.estatus = estatus;
	}

	public int getIdResponable() {
		return idResponable;
	}

	public void setIdResponable(int idResponable) {
		this.idResponable = idResponable;
	}

	public String getNomenclatura() {
		return nomenclatura;
	}

	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	@Override
	public String toString() {
		return "ResponsableDTO [idResponable=" + idResponable + ", nomenclatura=" + nomenclatura + ", area=" + area
				+ ", responsable=" + responsable + ", estatus=" + estatus + "]";
	}
}
