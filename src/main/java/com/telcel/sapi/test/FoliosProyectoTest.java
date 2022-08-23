package com.telcel.sapi.test;

import java.util.ArrayList;
import java.util.List;

import com.telcel.sapi.dto.ProyectoFolioDTO;
import com.telcel.sapi.service.ProyectoInfrestructuraService;
import com.telcel.sapi.service.impl.ProyectoInfrestructuraServiceImpl;

public class FoliosProyectoTest {

	public static void main(String[] args) {
		ProyectoInfrestructuraService  service = new ProyectoInfrestructuraServiceImpl();
		List<ProyectoFolioDTO> listProyectoFolio = new ArrayList<ProyectoFolioDTO>();
		
		listProyectoFolio = service.buscaFoliosByIp("A10.59.159.106");
		
		for(ProyectoFolioDTO data : listProyectoFolio) {
			System.out.println("Folio: " + data.getFolio() + " Proyecto: " + data.getNombreProyeco());
		}
	}

}
