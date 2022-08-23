package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;

public interface ReportsDAO {
	
	/*etapas*/
	List<ProyectoDTO> cargaProyectosReporteEtapas();
	List<ProyectoDTO> cargaProyectosReporte();
	List<ProyectoDTO> BuscaProyectosReporte(String criterio, String parametro);

	List<ProyectoDTO> cargaProyectosAllData();
	List<ProyectoDTO> cargaProyectosAllData(String criterio, String parametro);
	
	////* reportes mensuales etapa
	List<ProyectoDTO> BuscarMensualesProyectosEtapa(String fechaIni, String fechaFin);
	
}
