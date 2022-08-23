package com.telcel.sapi.service.impl;

import java.util.List;

import com.telcel.sapi.dao.IncidentesDAO;
import com.telcel.sapi.dao.impl.IncidentesDAOImpl;
import com.telcel.sapi.dto.IncidenteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.IncidentesService;

public class IncidentesServiceImpl implements IncidentesService {
	IncidentesDAO incidenteDao;
	
	@Override
	public List<IncidenteDTO> CargaIncidentes(Long idProyecto) {
		incidenteDao = new IncidentesDAOImpl();
		return incidenteDao.CargaIncidentes(idProyecto);
	}

	@Override
	public int RegistroIncidente(IncidenteDTO incidente, ProyectoDTO proyecto) {
		incidenteDao = new IncidentesDAOImpl();
		return incidenteDao.RegistroIncidente(incidente, proyecto);
	}

	@Override
	public int CulminaIncidente(IncidenteDTO incidente) {
		incidenteDao = new IncidentesDAOImpl();
		return incidenteDao.CulminaIncidente(incidente);
	}

	@Override
	public int ActualizaIncidente(IncidenteDTO incidente) {
		incidenteDao = new IncidentesDAOImpl();
		return incidenteDao.ActualizaIncidente(incidente);
	}

}
