package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.ProyectoDTO;

public interface ProyectoDAO {
	int RegistroProyecto(ProyectoDTO proyecto, boolean portafolioEmp);
	int AsignaPortafolioEmpresarial(ProyectoDTO proyecto);
	int ActualizaPortafolioEmpresarial(ProyectoDTO proyecto);
	int ActualizaInfoProyecto(ProyectoDTO proyecto);
	int CancelaProyecto(ProyectoDTO proyecto);
	int ActivaProyecto(ProyectoDTO proyecto);
	int EliminarProyecto(ProyectoDTO proyecto);
	
	List<String> VerificaFolio(String folio);
	List<FoliosRegistradosDTO> foliosRegistrados(String folio);
}
