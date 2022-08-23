package com.telcel.sapi.service.impl;

import java.io.IOException;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.dao.LoginDAO;
import com.telcel.sapi.dao.impl.LoginDAOImpl;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.LoginService;

public class LoginServiceImpl implements LoginService {
	static final Logger LOG = Logger.getLogger(LoginServiceImpl.class);
	
	LoginDAO loginDao;

	@Override
	public UsuarioDTO cargaUsuario(String usr) {
		loginDao = new LoginDAOImpl();
		return loginDao.cargaUsuario(usr);
	}

	@Override
	public void ValidaSesion(UsuarioDTO usuarioSesion) {
		try {
			if(usuarioSesion == null){		
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				try {					
					FacesContext.getCurrentInstance().getExternalContext().redirect("/SAPI");
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			
		} catch (Exception e) {
			LOG.error("Error al obtener informació de la sesión - " + e);
		}
	}
	
}
