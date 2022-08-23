package com.telcel.sapi.dao;

import java.util.List;


import com.telcel.sapi.dto.ProyectoDTO;

public interface InfrestructuraDAO {
	List<ProyectoDTO> FindAllProyects(int idUsuario);
 
}
