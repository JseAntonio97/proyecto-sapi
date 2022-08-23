package com.telcel.sapi.test;

import java.util.ArrayList;
import java.util.List;

import com.telcel.sapi.dao.impl.SeguimientoDAOImpl;
import com.telcel.sapi.dto.ProyectoDTO;

public class CargaProyectosAsignacionTest {

	public static void main(String[] args) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		SeguimientoDAOImpl dao = new SeguimientoDAOImpl();
		
		listProyectos = dao.BuscaProyectosByParametros("", "", "", 4L);
		
		for(ProyectoDTO proyecto : listProyectos) {
			System.out.println(proyecto.getNombreProyecto() + " " + proyecto.getPortafolioEmp().getFolio());
		}

	}

}
