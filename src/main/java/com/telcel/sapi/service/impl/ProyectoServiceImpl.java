package com.telcel.sapi.service.impl;

import java.util.List;

import com.telcel.sapi.dao.ProyectoDAO;
import com.telcel.sapi.dao.impl.ProyectoDAOImpl;
import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.ProyectoService;

public class ProyectoServiceImpl implements ProyectoService{
	ProyectoDAO proyectoDAO;

	@Override
	public int RegistroProyecto(ProyectoDTO proyecto, boolean portafolioEmp) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.RegistroProyecto(proyecto, portafolioEmp);
	}

	@Override
	public int AsignaPortafolioEmpresarial(ProyectoDTO proyecto) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.AsignaPortafolioEmpresarial(proyecto);
	}

	@Override
	public int ActualizaPortafolioEmpresarial(ProyectoDTO proyecto) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.ActualizaPortafolioEmpresarial(proyecto);
	}

	@Override
	public int ActualizaInfoProyecto(ProyectoDTO proyecto) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.ActualizaInfoProyecto(proyecto);
	}

	@Override
	public int CancelaProyecto(ProyectoDTO proyecto) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.CancelaProyecto(proyecto);
	}

	@Override
	public int ActivaProyecto(ProyectoDTO proyecto) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.ActivaProyecto(proyecto);
	}

	@Override
	public int EliminarProyecto(ProyectoDTO proyecto) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.EliminarProyecto(proyecto);
	}

	@Override
	public List<String> VerificaFolio(String folio) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.VerificaFolio(folio);
	}

	@Override
	public List<FoliosRegistradosDTO> foliosRegistrados(String folio) {
		proyectoDAO = new ProyectoDAOImpl();
		return proyectoDAO.foliosRegistrados(folio);
	}

}
