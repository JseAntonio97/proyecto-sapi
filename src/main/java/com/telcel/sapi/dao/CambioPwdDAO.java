package com.telcel.sapi.dao;

import com.telcel.sapi.dto.UsuarioDTO;

public interface CambioPwdDAO {
	boolean CambioPwd(UsuarioDTO usuario, String newPwd);
}
