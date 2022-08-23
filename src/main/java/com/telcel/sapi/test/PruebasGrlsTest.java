package com.telcel.sapi.test;

import java.util.ArrayList;
import java.util.List;

import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.service.ProyectoService;
import com.telcel.sapi.service.impl.ProyectoServiceImpl;

public class PruebasGrlsTest {

	public static void main(String[] args) {
//		CatalogosService service = new CatalogosServiceImpl();
		
//		List<TipoAmbienteDTO> listAmbiente = new ArrayList<TipoAmbienteDTO>();
//		
//		listAmbiente = service.CatListTiposAmbientes();
//		
//		for (TipoAmbienteDTO ambiente : listAmbiente) {
//			System.out.println(ambiente.getIdTipoAmbiente() + " " + ambiente.getTipoAmbiente() + " " + ambiente.getEstatus());
//		}
//		
//		service.UpdateNewTipoAmbiente(new TipoAmbienteDTO(1, "Desarrollo", "ACTIVO"));
//		
//		listAmbiente = service.CatListTiposAmbientes();
//		System.out.println();
//		for (TipoAmbienteDTO ambiente : listAmbiente) {
//			System.out.println(ambiente.getIdTipoAmbiente() + " " + ambiente.getTipoAmbiente() + " " + ambiente.getEstatus());
//		}
		
//		PorcAvanceDTO porcentajes = service.CargaPorcentajesAvance();
//
//		System.out.println("Portafolio: " + porcentajes.getPorportempr());
//
//		System.out.println("F60: " + porcentajes.getPorcF60());
//
//		System.out.println("Infraestructura: " + porcentajes.getPorcInfra());
//
//		System.out.println("Aplicativo: " + porcentajes.getPorcAplicativo());
//
//		System.out.println("Pre ATP: " + porcentajes.getPorcPreATP());
//
//		System.out.println("ATP: " + porcentajes.getPorcATP());
//
//		System.out.println("RTO: " + porcentajes.getPorcRTO());
		
//		List<JefeF60DTO> listJefes = new ArrayList<JefeF60DTO>();
//		listJefes = service.catalogoJefesF60();
//		for(JefeF60DTO jefe : listJefes) {
//			System.out.println(jefe.getIdJefeF60() + " " + jefe.getNombre());
//		}
//		
//		List<TipoF60DTO> listTipos = new ArrayList<TipoF60DTO>();
//		listTipos = service.catalogoTiposF60();
//		for(TipoF60DTO tipo : listTipos) {
//			System.out.println(tipo.getTipoF60());
//		}
		

		ProyectoService 	proyectoService = new ProyectoServiceImpl();
		List<FoliosRegistradosDTO> listFoliosRegistrados = new ArrayList<FoliosRegistradosDTO>();
		listFoliosRegistrados = proyectoService.foliosRegistrados("abc123456");
		
		for(FoliosRegistradosDTO folio : listFoliosRegistrados) {
			System.out.println(folio.getFolio());
		}
		
	}

}
