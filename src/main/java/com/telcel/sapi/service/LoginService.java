package com.telcel.sapi.service;

import com.telcel.sapi.dto.UsuarioDTO;

public interface LoginService {
	UsuarioDTO cargaUsuario(String usr);
	void ValidaSesion (UsuarioDTO usuarioSesion);
}
