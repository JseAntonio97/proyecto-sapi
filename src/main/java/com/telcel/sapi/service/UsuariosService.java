package com.telcel.sapi.service;

import java.util.List;

import com.telcel.sapi.dto.UsuarioDTO;

public interface UsuariosService {
	List<UsuarioDTO> ConsultaUsuarios();
	int GuardaUsuario(UsuarioDTO usuario);
	int ActualizaUsuario(UsuarioDTO usuario);
	int ActivaInactivaUsuario(UsuarioDTO usuario, boolean onOff);
}
