package com.telcel.sapi.dto;

public class SeguimientoATP {
	private int 	idSeguimientoATP;
	private String	fechaInicio;
	private String	fechaCompromiso;
	private	String	fechaFin;
	private	String	estatus;
	private boolean	sinFechas;
	
	public SeguimientoATP() {
		super();
	}

	public SeguimientoATP(int idSeguimientoATP, String fechaInicio, String fechaCompromiso, String fechaFin,
			String estatus, boolean sinFechas) {
		super();
		this.idSeguimientoATP = idSeguimientoATP;
		this.fechaInicio = fechaInicio;
		this.fechaCompromiso = fechaCompromiso;
		this.fechaFin = fechaFin;
		this.estatus = estatus;
		this.sinFechas = sinFechas;
	}

	public int getIdSeguimientoATP() {
		return idSeguimientoATP;
	}

	public void setIdSeguimientoATP(int idSeguimientoATP) {
		this.idSeguimientoATP = idSeguimientoATP;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaCompromiso() {
		return fechaCompromiso;
	}

	public void setFechaCompromiso(String fechaCompromiso) {
		this.fechaCompromiso = fechaCompromiso;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
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
		return "SeguimientoATP [idSeguimientoATP=" + idSeguimientoATP + ", fechaInicio=" + fechaInicio
				+ ", fechaCompromiso=" + fechaCompromiso + ", fechaFin=" + fechaFin + ", estatus=" + estatus
				+ ", sinFechas=" + sinFechas + "]";
	}
}
