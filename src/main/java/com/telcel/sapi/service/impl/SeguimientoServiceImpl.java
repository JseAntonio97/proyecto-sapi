package com.telcel.sapi.service.impl;

import java.util.List;

import com.telcel.sapi.dao.SeguimientoDAO;
import com.telcel.sapi.dao.impl.SeguimientoDAOImpl;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.SeguimientoService;

public class SeguimientoServiceImpl implements SeguimientoService {
	SeguimientoDAO daoService;

	@Override
	public List<ProyectoDTO> FindAllProyects(Long idUsuario) {
		daoService = new SeguimientoDAOImpl();
		return daoService.FindAllProyects(idUsuario);
	}

	@Override
	public List<ProyectoDTO> BuscaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp,
			Long idUsuario) {
		daoService = new SeguimientoDAOImpl();
		return daoService.BuscaProyectosByParametros(nomProyecto, integrador, folioPortEmp, idUsuario);
	}

	@Override
	public List<ProyectoDTO> BuscaProyectosByParametrosInfra(String nomProyecto, String integrador, String folioPortEmp,
			String ip, String hostName, Long idUsuario) {
		daoService = new SeguimientoDAOImpl();
		return daoService.BuscaProyectosByParametrosInfra(nomProyecto, integrador, folioPortEmp, ip, hostName, idUsuario);
	}

	@Override
	public int MarcaLeidoProyecto(ProyectoDTO proyecto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ProyectoDTO> FindAllProyectsAsigned() {
		daoService = new SeguimientoDAOImpl();
		return daoService.FindAllProyectsAsigned();
	}

	@Override
	public List<ProyectoDTO> BuscaAllAsignedProyectosByParametros(String nomProyecto, String integrador,
			String folioPortEmp, String ip, String hostName) {
		daoService = new SeguimientoDAOImpl();
		return daoService.BuscaAllAsignedProyectosByParametros(nomProyecto, integrador, folioPortEmp, ip, hostName);
	}

}
