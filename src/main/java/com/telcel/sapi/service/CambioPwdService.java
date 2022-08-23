package com.telcel.sapi.service;

import com.telcel.sapi.dto.UsuarioDTO;

public interface CambioPwdService {
	boolean CambioPwd(UsuarioDTO usuario, String newPwd);
}
