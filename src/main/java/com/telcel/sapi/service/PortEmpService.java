package com.telcel.sapi.service;

import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;

public interface PortEmpService {
	List<ProyectoDTO> CargaProyectos();
	List<ProyectoDTO> CargaProyectos(String proyectoName);
	List<String> CargaNameProyectos();
	int LiberaProyecto(ProyectoDTO proyecto);
}
