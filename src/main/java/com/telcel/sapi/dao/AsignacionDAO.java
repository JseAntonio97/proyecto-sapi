package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.AsignacionesDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;

public interface AsignacionDAO {
	List<ProyectoDTO> CargaProyectos();
	List<UsuarioDTO>  CargaColaboradores(UsuarioDTO user);
	int AsignaProyectos(UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia);
	int ReAsignaProyecto (UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia);
	
	List<ProyectoDTO> 	BusquedaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp);
	
	List<AsignacionesDTO> ConteoAsignaciones();
	void MarcaProyectoNoLeido(ProyectoDTO proyecto);
}
