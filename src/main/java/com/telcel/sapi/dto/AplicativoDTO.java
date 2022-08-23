package com.telcel.sapi.dto;

public class AplicativoDTO {
	private int 	idAplicativo;
	private String	fechaDespliegue;
	private String	fechaFinPruebas;
	private String	fechaNotificacion;
	private String	estatus;
	private boolean	sinFechas;
		
	public AplicativoDTO() {
		super();
	}

	public AplicativoDTO(int idAplicativo, String fechaDespliegue, String fechaFinPruebas, String fechaNotificacion,
			String estatus, boolean sinFechas) {
		super();
		this.idAplicativo = idAplicativo;
		this.fechaDespliegue = fechaDespliegue;
		this.fechaFinPruebas = fechaFinPruebas;
		this.fechaNotificacion = fechaNotificacion;
		this.estatus = estatus;
		this.sinFechas = sinFechas;
	}

	public int getIdAplicativo() {
		return idAplicativo;
	}

	public void setIdAplicativo(int idAplicativo) {
		this.idAplicativo = idAplicativo;
	}

	public String getFechaDespliegue() {
		return fechaDespliegue;
	}

	public void setFechaDespliegue(String fechaDespliegue) {
		this.fechaDespliegue = fechaDespliegue;
	}

	public String getFechaFinPruebas() {
		return fechaFinPruebas;
	}

	public void setFechaFinPruebas(String fechaFinPruebas) {
		this.fechaFinPruebas = fechaFinPruebas;
	}

	public String getFechaNotificacion() {
		return fechaNotificacion;
	}

	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public boolean isSinFechas() {
		return sinFechas;
	}

	public void setSinFechas(boolean sinFechas) {
		this.sinFechas = sinFechas;
	}

	@Override
	public String toString() {
		return "AplicativoDTO [idAplicativo=" + idAplicativo + ", fechaDespliegue=" + fechaDespliegue
				+ ", fechaFinPruebas=" + fechaFinPruebas + ", fechaNotificacion=" + fechaNotificacion + ", estatus="
				+ estatus + ", sinFechas=" + sinFechas + "]";
	}
}
