package com.telcel.sapi.service.impl;

import java.io.Serializable;
import java.util.List;

import com.telcel.sapi.dao.PortEmpDAO;
import com.telcel.sapi.dao.impl.PortEmpDAOImpl;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.PortEmpService;

public class PortEmpServiceImpl implements PortEmpService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3991893047575481440L;
	PortEmpDAO portafolioDAO;
	
	@Override
	public List<ProyectoDTO> CargaProyectos() {
		portafolioDAO = new PortEmpDAOImpl();
		return portafolioDAO.CargaProyectos();
	}

	@Override
	public List<ProyectoDTO> CargaProyectos(String proyectoName) {
		portafolioDAO = new PortEmpDAOImpl();
		return portafolioDAO.CargaProyectos(proyectoName);
	}

	@Override
	public List<String> CargaNameProyectos() {
		portafolioDAO = new PortEmpDAOImpl();
		return portafolioDAO.CargaNameProyectos();
	}

	@Override
	public int LiberaProyecto(ProyectoDTO proyecto) {
		portafolioDAO = new PortEmpDAOImpl();
		return portafolioDAO.LiberaProyecto(proyecto);
	}
	
}
