package com.telcel.sapi.test;

import java.util.ArrayList;
import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.ReportsService;
import com.telcel.sapi.service.impl.ReportsServiceImpl;

public class ProyectosAmbienteTest {

	public static void main(String[] args) {
		
		ReportsService serviceProyect = new ReportsServiceImpl();
		
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		
		listProjects = serviceProyect.cargaProyectosReporte();
		List<String> listFolios = new ArrayList<String>();
		List<String> listFolios2da = new ArrayList<String>();
		
		System.out.println("Tamaño de la lista Folios inicial: " + listFolios.size());
				
		/**
		 * Calcula los Folios Empresariales no repetidos
		 */

		for(ProyectoDTO proyecto : listProjects) {
			if(proyecto.getPortafolioEmp().getFolio() != null) {
				listFolios.add(proyecto.getPortafolioEmp().getFolio());
			}
		}
		System.out.println("Tamaño de la lista Folios final: " + listFolios.size());
		
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		for(int i = 0 ; i < listFolios.size() ; i ++) {
			boolean existe = false ;
			
			for(String folioComparador : listFolios2da) {				
				if(folioComparador != null) {
					if(listFolios.get(i).equals(folioComparador)) {
						existe = true;
						break;
					}
				}				
			}
			
			if(!existe) {
				listFolios2da.add(listFolios.get(i));
			}else {
				System.out.println(listFolios.get(i));
			}
			
		}

		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		
		
		System.out.println(listFolios2da.size());
		
	}

}
