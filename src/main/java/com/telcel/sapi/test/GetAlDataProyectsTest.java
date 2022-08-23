package com.telcel.sapi.test;

import java.util.ArrayList;
import java.util.List;

import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.ReportsService;
import com.telcel.sapi.service.impl.ReportsServiceImpl;

public class GetAlDataProyectsTest {

	public static void main(String[] args) {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		ReportsService report = new ReportsServiceImpl();
		
		listProjects = report.cargaProyectosAllDataByCriterio("nombre", "SAPI");
		
		for(ProyectoDTO proyecto : listProjects) {
			
//			System.out.println(
//				proyecto.getIdProyecto() 			+ " " +
//				proyecto.getIdentificadorInterno() 	+ " " +
//				proyecto.getIntegrador()		 	+ " " +
//				proyecto.getNombreProyecto() 		+ " " +
//				proyecto.getDescripcion() 			+ " " +
//				proyecto.getSolicitante() 			+ " " +
//				proyecto.getDireccion()
//				
//			);
			
			
			if(proyecto.getListAmbientes() != null) {
				System.out.print(" " + proyecto.getListAmbientes().get(0).getBaseDatos() + " ");
				
				for(AmbienteDTO ambiente : proyecto.getListAmbientes()) {
					System.out.println(ambiente.toString());
				}
			}
		}

	}

}
