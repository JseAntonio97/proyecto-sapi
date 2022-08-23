package com.telcel.sapi.service.impl;

import java.util.List;

import com.telcel.sapi.dao.InfrestructuraDAO;
import com.telcel.sapi.dao.impl.InfrestructuraDAOImpl;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.InfrestructuraService;

public class InfrestructuraServiceImpl implements InfrestructuraService {
  InfrestructuraDAO daoService;
	
	@Override
	public List<ProyectoDTO> FindAllProyects(int idUsuario) {
		daoService = new InfrestructuraDAOImpl();
		return daoService.FindAllProyects(idUsuario);
	}

}
