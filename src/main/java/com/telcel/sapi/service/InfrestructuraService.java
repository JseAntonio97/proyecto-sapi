package com.telcel.sapi.service;

import java.util.List;

import com.telcel.sapi.dto.ProyectoDTO;

public interface InfrestructuraService {
	List<ProyectoDTO> FindAllProyects(int idUsuario);
}
