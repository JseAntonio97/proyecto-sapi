package com.telcel.sapi.service;

import java.util.List;

import org.primefaces.model.charts.bar.BarChartModel;

import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;

public interface AsignacionService {
	List<ProyectoDTO> 	CargaProyectos();
	List<UsuarioDTO>	CargaColaboradores(UsuarioDTO user);
	int AsignaProyectos (UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia);
	int ReAsignaProyecto (UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia);
	
	List<ProyectoDTO> 	BusquedaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp);
	
	BarChartModel graficarAsignaciones();
	void MarcaProyectoNoLeido(ProyectoDTO proyecto);
}
