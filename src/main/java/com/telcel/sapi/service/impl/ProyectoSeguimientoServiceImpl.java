package com.telcel.sapi.service.impl;

import com.telcel.sapi.dao.ProyectoSeguimientoDAO;
import com.telcel.sapi.dao.impl.ProyectoSeguimientoDAOImpl;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.ProyectoSeguimientoService;

public class ProyectoSeguimientoServiceImpl implements ProyectoSeguimientoService {
	ProyectoSeguimientoDAO proyectoSeguimientoDAO;
	
	@Override
	public ProyectoDTO CargaProyectoSelectAllData(Long idProyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.CargaProyectoSelectAllData(idProyecto);
	}

	@Override
	public int actualizaFase(Long idProyecto, String fase, int tipoFase) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.actualizaFase(idProyecto, fase, tipoFase);
	}

	@Override
	public int GuardaF60(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.GuardaF60(proyecto);
	}

	@Override
	public int UpdateF60(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateF60(proyecto);
	}

	@Override
	public int UpdateTerminaF60(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateTerminaF60(proyecto);
	}

	@Override
	public int GuardaSegInfra(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.GuardaSegInfra(proyecto);
	}

	@Override
	public int UpdateSegInfra(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateSegInfra(proyecto);
	}

	@Override
	public int UpdateTerminaSegInfra(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateTerminaSegInfra(proyecto);
	}

	@Override
	public int GuardaAplicativo(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.GuardaAplicativo(proyecto);
	}

	@Override
	public int UpdateAplicativo(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateAplicativo(proyecto);
	}

	@Override
	public int UpdateTerminaAplicativo(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateTerminaAplicativo(proyecto);
	}

	@Override
	public int GuardaPreAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.GuardaPreAtp(proyecto);
	}

	@Override
	public int UpdatePreAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdatePreAtp(proyecto);
	}

	@Override
	public int UpdateTerminaPreAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateTerminaPreAtp(proyecto);
	}

	@Override
	public int GuardaAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.GuardaAtp(proyecto);
	}

	@Override
	public int UpdateAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateAtp(proyecto);
	}

	@Override
	public int UpdateTerminaAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateTerminaAtp(proyecto);
	}

	@Override
	public int GuardaRto(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.GuardaRto(proyecto);
	}

	@Override
	public int UpdateRto(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateRto(proyecto);
	}

	@Override
	public int UpdateTerminaRto(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.UpdateTerminaRto(proyecto);
	}

	@Override
	public void MarcaProyectoLeido(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		proyectoSeguimientoDAO.MarcaProyectoLeido(proyecto);
	}

	@Override
	public int DeleteF60(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.DeleteF60(proyecto);
	}

	@Override
	public int DeleteSegInfra(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.DeleteSegInfra(proyecto);
	}

	@Override
	public int DeleteAplicativo(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.DeleteAplicativo(proyecto);
	}

	@Override
	public int DeletePreAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.DeletePreAtp(proyecto);
	}

	@Override
	public int DeleteAtp(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.DeleteAtp(proyecto);
	}

	@Override
	public int DeleteRto(ProyectoDTO proyecto) {
		proyectoSeguimientoDAO  = new ProyectoSeguimientoDAOImpl();
		return proyectoSeguimientoDAO.DeleteRto(proyecto);
	}
}
