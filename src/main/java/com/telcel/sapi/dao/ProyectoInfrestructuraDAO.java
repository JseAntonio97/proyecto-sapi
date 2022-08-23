package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ProyectoFolioDTO;


public interface ProyectoInfrestructuraDAO {
	int RegistroInfrestructura(ProyectoDTO proyecto, AmbienteDTO ambiente);
	int UpdateInfrestructura(AmbienteDTO ambiente);
	List<AmbienteDTO> CargaAmbientes(Long idProyecto);
	boolean buscaHostName(String hostname);
	boolean buscaDireccionIP(String ip);
	boolean buscaHostNameUpdate(AmbienteDTO ambiente);
	boolean buscaDireccionIPUpdate(AmbienteDTO ambiente);

	int DeleteInfrestructura(AmbienteDTO ambiente);
	
	List<ProyectoFolioDTO> buscaFoliosByHostName(String hostname);
	List<ProyectoFolioDTO> buscaFoliosByIp		(String ip);
	
	int cuentaFoliosByHostName	(String hostname);
	int cuentaFoliosByIp		(String ip);
}
