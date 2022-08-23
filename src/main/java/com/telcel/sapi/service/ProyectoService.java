package com.telcel.sapi.service;

import java.util.List;

import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.ProyectoDTO;

public interface ProyectoService {
	/**
	 * 
	 * @param proyecto, El objeto con los datos del proyecto
	 * @param portafolioEmp, En caso de registrar con portafolio empresarial debe tener valor TRUE
	 * @return INT 1: Success 0: Fail
	 */
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
