package com.telcel.sapi.service;

import java.util.List;

import com.telcel.sapi.dto.IncidenteDTO;
import com.telcel.sapi.dto.ProyectoDTO;

public interface IncidentesService {

	List<IncidenteDTO> CargaIncidentes(Long idProyecto);
	int RegistroIncidente(IncidenteDTO incidente, ProyectoDTO proyecto);
	int CulminaIncidente(IncidenteDTO incidente);
	int ActualizaIncidente(IncidenteDTO incidente);
}
