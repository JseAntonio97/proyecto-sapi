package com.telcel.sapi.service.impl;

import java.io.Serializable;

import com.telcel.sapi.dao.CambioPwdDAO;
import com.telcel.sapi.dao.impl.CambioPwdDAOImpl;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CambioPwdService;

public class CambioPwdServiceImpl implements CambioPwdService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6856611188862116388L;

	CambioPwdDAO cambioPwdDao;
	
	@Override
	public boolean CambioPwd(UsuarioDTO usuario, String newPwd) {
		cambioPwdDao = new CambioPwdDAOImpl();
		return cambioPwdDao.CambioPwd(usuario, newPwd);
	}

}
