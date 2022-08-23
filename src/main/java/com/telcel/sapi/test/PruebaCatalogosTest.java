package com.telcel.sapi.test;

import java.util.List;

import com.telcel.sapi.dao.CatalogosDAO;
import com.telcel.sapi.dao.impl.CatalogosDAOImpl;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.GerenciaDTO;

public class PruebaCatalogosTest {

	public static void main(String[] args) {
		CatalogosDAO catalogos = new CatalogosDAOImpl();
		List<DireccionDTO> 	listProyectos 	= catalogos.cargaCatDireccion();
		List<GerenciaDTO>	listGerencia1	= catalogos.cargaCatGerencia(1L);
		List<GerenciaDTO>	listGerencia2	= catalogos.cargaCatGerencia(2L);
		List<GerenciaDTO>	listGerencia3	= catalogos.cargaCatGerencia(3L);
		
		for(DireccionDTO direccion : listProyectos) {
			System.out.println(direccion.getIdDireccion() + " " + direccion.getDireccion());
		}
		System.out.println();
		
		for(GerenciaDTO gerencia : listGerencia1) {
			System.out.println(gerencia.getIdGerencia() + " " + gerencia.getGerencia());
		}
		System.out.println();
		
		for(GerenciaDTO gerencia : listGerencia2) {
			System.out.println(gerencia.getIdGerencia() + " " + gerencia.getGerencia());
		}
		System.out.println();
		
		for(GerenciaDTO gerencia : listGerencia3) {
			System.out.println(gerencia.getIdGerencia() + " " + gerencia.getGerencia());
		}
	}

}
