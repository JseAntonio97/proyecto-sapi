package com.telcel.sapi.dto;

public class PermisosDTO {
	private Long	idPermiso;
	private boolean registros;
	private boolean infraestructura;
	private boolean seguimiento;
	private boolean asignacion;
	private boolean funcionesOperativas;
	private boolean reportes;
	private boolean seguimientoGrl;
	
	public PermisosDTO() {
		super();
	}

	public PermisosDTO(Long idPermiso, boolean registros, boolean infraestructura, boolean seguimiento,
			boolean asignacion, boolean funcionesOperativas, boolean reportes, boolean seguimientoGrl) {
		super();
		this.idPermiso = idPermiso;
		this.registros = registros;
		this.infraestructura = infraestructura;
		this.seguimiento = seguimiento;
		this.asignacion = asignacion;
		this.funcionesOperativas = funcionesOperativas;
		this.reportes = reportes;
		this.seguimientoGrl = seguimientoGrl;
	}

	public Long getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Long idPermiso) {
		this.idPermiso = idPermiso;
	}

	public boolean isRegistros() {
		return registros;
	}

	public void setRegistros(boolean registros) {
		this.registros = registros;
	}

	public boolean isInfraestructura() {
		return infraestructura;
	}

	public void setInfraestructura(boolean infraestructura) {
		this.infraestructura = infraestructura;
	}

	public boolean isSeguimiento() {
		return seguimiento;
	}

	public void setSeguimiento(boolean seguimiento) {
		this.seguimiento = seguimiento;
	}

	public boolean isAsignacion() {
		return asignacion;
	}

	public void setAsignacion(boolean asignacion) {
		this.asignacion = asignacion;
	}

	public boolean isFuncionesOperativas() {
		return funcionesOperativas;
	}

	public void setFuncionesOperativas(boolean funcionesOperativas) {
		this.funcionesOperativas = funcionesOperativas;
	}

	public boolean isReportes() {
		return reportes;
	}

	public void setReportes(boolean reportes) {
		this.reportes = reportes;
	}

	public boolean isSeguimientoGrl() {
		return seguimientoGrl;
	}

	public void setSeguimientoGrl(boolean seguimientoGrl) {
		this.seguimientoGrl = seguimientoGrl;
	}

	@Override
	public String toString() {
		return "PermisosDTO [idPermiso=" + idPermiso + ", registros=" + registros + ", infraestructura="
				+ infraestructura + ", seguimiento=" + seguimiento + ", asignacion=" + asignacion
				+ ", funcionesOperativas=" + funcionesOperativas + ", reportes=" + reportes + ", seguimientoGrl="
				+ seguimientoGrl + "]";
	}
}
