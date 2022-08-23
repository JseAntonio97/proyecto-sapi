package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.UsuarioDTO;

public interface UsuariosDAO {
	List<UsuarioDTO> ConsultaUsuarios();
	int GuardaUsuario(UsuarioDTO usuario);
	int ActualizaUsuario(UsuarioDTO usuario);
	int ActivaInactivaUsuario(UsuarioDTO usuario, boolean onOff);
}
