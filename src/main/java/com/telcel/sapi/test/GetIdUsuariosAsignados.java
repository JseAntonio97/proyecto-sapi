package com.telcel.sapi.test;

import java.util.ArrayList;
import java.util.List;

import com.telcel.sapi.dao.impl.AsignacionDAOImpl;
import com.telcel.sapi.dto.AsignacionesDTO;

public class GetIdUsuariosAsignados {
	
	public static void main(String[] args) {
		AsignacionDAOImpl dasok = new AsignacionDAOImpl();
		
		List<AsignacionesDTO> 	listConteo 		= new ArrayList<AsignacionesDTO>();
		
		listConteo = dasok.ConteoAsignaciones();
		
		for(AsignacionesDTO asignacionesDTO : listConteo) {
			System.out.println("Usuario: " + asignacionesDTO.getUsuario() +" - Asignaciones: " + asignacionesDTO.getNumeroAsignaciones());
		}
	}
}
