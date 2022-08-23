package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;

public interface SeguimientoDAO {
	List<ProyectoDTO> FindAllProyects(Long idUsuario);	
	List<ProyectoDTO> BuscaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp, Long idUsuario);
	List<ProyectoDTO> BuscaProyectosByParametrosInfra(String nomProyecto, String integrador, String folioPortEmp, String ip, String hostName, Long idUsuario);
	
	List<ProyectoDTO> FindAllProyectsAsigned();
	
	List<ProyectoDTO> BuscaAllAsignedProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp, String ip, String hostName);
}
