package com.telcel.sapi.dto;

public class SeguimientoInfraDTO {
	private int 	idSeguimientoInfra;
	private String  kickoff;
	private String 	fechaEnvio;
	private String	fechaCompromiso;
	private String	fechaEntregaUsuario;
	private String	estatus;
	public boolean	sinFechas;
	
	public SeguimientoInfraDTO() {
		super();
	}

	public SeguimientoInfraDTO(int idSeguimientoInfra, String kickoff, String fechaEnvio, String fechaCompromiso,
			String fechaEntregaUsuario, String estatus, boolean sinFechas) {
		super();
		this.idSeguimientoInfra = idSeguimientoInfra;
		this.kickoff = kickoff;
		this.fechaEnvio = fechaEnvio;
		this.fechaCompromiso = fechaCompromiso;
		this.fechaEntregaUsuario = fechaEntregaUsuario;
		this.estatus = estatus;
		this.sinFechas = sinFechas;
	}

	public int getIdSeguimientoInfra() {
		return idSeguimientoInfra;
	}

	public void setIdSeguimientoInfra(int idSeguimientoInfra) {
		this.idSeguimientoInfra = idSeguimientoInfra;
	}

	public String getKickoff() {
		return kickoff;
	}

	public void setKickoff(String kickoff) {
		this.kickoff = kickoff;
	}

	public String getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public String getFechaCompromiso() {
		return fechaCompromiso;
	}

	public void setFechaCompromiso(String fechaCompromiso) {
		this.fechaCompromiso = fechaCompromiso;
	}

	public String getFechaEntregaUsuario() {
		return fechaEntregaUsuario;
	}

	public void setFechaEntregaUsuario(String fechaEntregaUsuario) {
		this.fechaEntregaUsuario = fechaEntregaUsuario;
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
		return "SeguimientoInfraDTO [idSeguimientoInfra=" + idSeguimientoInfra + ", kickoff=" + kickoff
				+ ", fechaEnvio=" + fechaEnvio + ", fechaCompromiso=" + fechaCompromiso + ", fechaEntregaUsuario="
				+ fechaEntregaUsuario + ", estatus=" + estatus + ", sinFechas=" + sinFechas + "]";
	}
}
