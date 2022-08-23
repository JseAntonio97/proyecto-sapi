package com.telcel.sapi.service.impl;



import java.util.List;

import com.telcel.sapi.dao.ProyectoInfrestructuraDAO;
import com.telcel.sapi.dao.impl.ProyectoInfrestructuraDAOImpl;
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ProyectoFolioDTO;
import com.telcel.sapi.service.ProyectoInfrestructuraService;

public class ProyectoInfrestructuraServiceImpl implements ProyectoInfrestructuraService {

	ProyectoInfrestructuraDAO proyectoInfrestructuraDAO;

	@Override
	public int RegistroInfrestructura(ProyectoDTO proyecto, AmbienteDTO ambiente) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.RegistroInfrestructura(proyecto, ambiente);
	}

	@Override
	public int UpdateInfrestructura(AmbienteDTO ambiente) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.UpdateInfrestructura(ambiente);
	}

	@Override
	public List<AmbienteDTO> CargaAmbientes(Long idProyecto) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.CargaAmbientes(idProyecto);
	}

	@Override
	public boolean buscaHostName(String hostname) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.buscaHostName(hostname);
	}

	@Override
	public boolean buscaDireccionIP(String ip) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.buscaDireccionIP(ip);
	}

	@Override
	public boolean buscaHostNameUpdate(AmbienteDTO ambiente) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.buscaHostNameUpdate(ambiente);
	}

	@Override
	public boolean buscaDireccionIPUpdate(AmbienteDTO ambiente) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.buscaDireccionIPUpdate(ambiente);
	}

	@Override
	public int DeleteInfrestructura(AmbienteDTO ambiente) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.DeleteInfrestructura(ambiente);
	}

	@Override
	public List<ProyectoFolioDTO> buscaFoliosByHostName(String hostname) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.buscaFoliosByHostName(hostname);
	}

	@Override
	public List<ProyectoFolioDTO> buscaFoliosByIp(String ip) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.buscaFoliosByIp(ip);
	}

	@Override
	public int cuentaFoliosByHostName(String hostname) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.cuentaFoliosByHostName(hostname);
	}

	@Override
	public int cuentaFoliosByIp(String ip) {
		proyectoInfrestructuraDAO = new ProyectoInfrestructuraDAOImpl(); 
		return proyectoInfrestructuraDAO.cuentaFoliosByIp(ip);
	}
}
