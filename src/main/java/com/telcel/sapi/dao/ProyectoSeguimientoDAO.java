package com.telcel.sapi.dao;

import com.telcel.sapi.dto.ProyectoDTO;

public interface ProyectoSeguimientoDAO {
	ProyectoDTO CargaProyectoSelectAllData(Long idProyecto);

	int actualizaFase(Long idProyecto, String fase, int tipoFase);
	
	int GuardaF60(ProyectoDTO proyecto);
	int UpdateF60(ProyectoDTO proyecto);
	int UpdateTerminaF60(ProyectoDTO proyecto);
	int DeleteF60(ProyectoDTO proyecto);
	
	int GuardaSegInfra(ProyectoDTO proyecto);
	int UpdateSegInfra(ProyectoDTO proyecto);
	int UpdateTerminaSegInfra(ProyectoDTO proyecto);
	int DeleteSegInfra(ProyectoDTO proyecto);

	int GuardaAplicativo(ProyectoDTO proyecto);
	int UpdateAplicativo(ProyectoDTO proyecto);
	int UpdateTerminaAplicativo(ProyectoDTO proyecto);
	int DeleteAplicativo(ProyectoDTO proyecto);
	
	int GuardaPreAtp(ProyectoDTO proyecto);
	int UpdatePreAtp(ProyectoDTO proyecto);
	int UpdateTerminaPreAtp(ProyectoDTO proyecto);
	int DeletePreAtp(ProyectoDTO proyecto);
	
	int GuardaAtp(ProyectoDTO proyecto);
	int UpdateAtp(ProyectoDTO proyecto);
	int UpdateTerminaAtp(ProyectoDTO proyecto);
	int DeleteAtp(ProyectoDTO proyecto);
	
	int GuardaRto(ProyectoDTO proyecto);
	int UpdateRto(ProyectoDTO proyecto);
	int UpdateTerminaRto(ProyectoDTO proyecto);
	int DeleteRto(ProyectoDTO proyecto);
	
	void MarcaProyectoLeido(ProyectoDTO proyecto);
}
