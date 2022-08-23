package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;

public interface PortEmpDAO {
	List<ProyectoDTO> CargaProyectos();
	List<ProyectoDTO> CargaProyectos(String proyectoName);
	List<String> CargaNameProyectos();
	int LiberaProyecto(ProyectoDTO proyecto);
}
