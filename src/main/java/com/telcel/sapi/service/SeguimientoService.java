package com.telcel.sapi.service;

import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;

public interface SeguimientoService {
	List<ProyectoDTO> FindAllProyects(Long idUsuario);
	
	List<ProyectoDTO> BuscaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp, Long idUsuario);
	
	List<ProyectoDTO> BuscaProyectosByParametrosInfra(String nomProyecto, String integrador, String folioPortEmp, String ip, String hostName, Long idUsuario);
	
	int MarcaLeidoProyecto(ProyectoDTO proyecto);
	
	List<ProyectoDTO> FindAllProyectsAsigned();
	
	List<ProyectoDTO> BuscaAllAsignedProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp, String ip, String hostName);
}
