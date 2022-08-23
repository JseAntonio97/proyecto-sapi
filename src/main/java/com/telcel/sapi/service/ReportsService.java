package com.telcel.sapi.service;

import java.util.List;

import org.primefaces.model.charts.bar.BarChartModel;

import com.telcel.sapi.dto.ProyectoDTO;

public interface ReportsService {
	
	/**list reporte etapas*/
	List<ProyectoDTO> cargaProyectosReporteEtapas();
	List<ProyectoDTO> cargaProyectosReporte();
	List<ProyectoDTO> BuscaProyectosReporte(String criterio, String parametro);

	void generaReporteExcel(List<ProyectoDTO> listProjects, String nombreExcel);
	void generaReporteAllDataExcel(List<ProyectoDTO> listProjects, String nombreExcel);
	
	BarChartModel graficaFasesProyectos(List<ProyectoDTO> listProjects);
	/****/
	BarChartModel graficaFasesProyectosEtapas(List<ProyectoDTO> listProjects);
	
	int CalculaNumeroAmbientes(List<ProyectoDTO> listProjects);
	int CalculaNumeroFolios(List<ProyectoDTO> listProjects);
	
	List<ProyectoDTO> cargaProyectosAllData();
	List<ProyectoDTO> cargaProyectosAllDataByCriterio(String criterio, String parametro);
}
