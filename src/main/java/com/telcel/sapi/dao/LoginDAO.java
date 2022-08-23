package com.telcel.sapi.dao;

import com.telcel.sapi.dto.UsuarioDTO;

public interface LoginDAO {
	UsuarioDTO cargaUsuario(String usr);
}
