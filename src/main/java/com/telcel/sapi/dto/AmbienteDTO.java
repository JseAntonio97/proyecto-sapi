package com.telcel.sapi.dto;

import java.util.Date;

public class AmbienteDTO {
	private int		idAmbiente;
	private TipoAmbienteDTO	tipoAmbiente;
	private String	ambiente;
	private	int		cpu;
	private int		memoria;
	private int		discoDuro;
	private String	unidadMedidaDD;
	private String	sistemaOperativo;
	private String	baseDatos;
	private String	hostName;
	private String	ip;
	private String	entregaEquipo;
	private Date	entregaEquipoDate;
	private String	comentario;
	
	public AmbienteDTO() {
		super();
	}

	public AmbienteDTO(int idAmbiente, TipoAmbienteDTO tipoAmbiente, String ambiente, int cpu, int memoria,
			int discoDuro, String unidadMedidaDD, String sistemaOperativo, String baseDatos, String hostName, String ip,
			String entregaEquipo, Date entregaEquipoDate, String comentario) {
		super();
		this.idAmbiente = idAmbiente;
		this.tipoAmbiente = tipoAmbiente;
		this.ambiente = ambiente;
		this.cpu = cpu;
		this.memoria = memoria;
		this.discoDuro = discoDuro;
		this.unidadMedidaDD = unidadMedidaDD;
		this.sistemaOperativo = sistemaOperativo;
		this.baseDatos = baseDatos;
		this.hostName = hostName;
		this.ip = ip;
		this.entregaEquipo = entregaEquipo;
		this.entregaEquipoDate = entregaEquipoDate;
		this.comentario = comentario;
	}

	public int getIdAmbiente() {
		return idAmbiente;
	}

	public void setIdAmbiente(int idAmbiente) {
		this.idAmbiente = idAmbiente;
	}

	public TipoAmbienteDTO getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(TipoAmbienteDTO tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public int getMemoria() {
		return memoria;
	}

	public void setMemoria(int memoria) {
		this.memoria = memoria;
	}

	public int getDiscoDuro() {
		return discoDuro;
	}

	public void setDiscoDuro(int discoDuro) {
		this.discoDuro = discoDuro;
	}

	public String getUnidadMedidaDD() {
		return unidadMedidaDD;
	}

	public void setUnidadMedidaDD(String unidadMedidaDD) {
		this.unidadMedidaDD = unidadMedidaDD;
	}

	public String getSistemaOperativo() {
		return sistemaOperativo;
	}

	public void setSistemaOperativo(String sistemaOperativo) {
		this.sistemaOperativo = sistemaOperativo;
	}

	public String getBaseDatos() {
		return baseDatos;
	}

	public void setBaseDatos(String baseDatos) {
		this.baseDatos = baseDatos;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getEntregaEquipo() {
		return entregaEquipo;
	}

	public void setEntregaEquipo(String entregaEquipo) {
		this.entregaEquipo = entregaEquipo;
	}

	public Date getEntregaEquipoDate() {
		return entregaEquipoDate;
	}

	public void setEntregaEquipoDate(Date entregaEquipoDate) {
		this.entregaEquipoDate = entregaEquipoDate;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
