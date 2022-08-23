package com.telcel.sapi.service.impl;

import java.io.Serializable;
import java.util.List;

import com.telcel.sapi.dao.UsuariosDAO;
import com.telcel.sapi.dao.impl.UsuariosDAOImpl;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.UsuariosService;

public class UsuariosServiceImpl implements UsuariosService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6527649250972799162L;
	
	UsuariosDAO usuarioDAO;

	@Override
	public List<UsuarioDTO> ConsultaUsuarios() {
		usuarioDAO = new UsuariosDAOImpl();
		return usuarioDAO.ConsultaUsuarios();
	}

	@Override
	public int GuardaUsuario(UsuarioDTO usuario) {
		usuarioDAO = new UsuariosDAOImpl();
		return usuarioDAO.GuardaUsuario(usuario);
	}

	@Override
	public int ActualizaUsuario(UsuarioDTO usuario) {
		usuarioDAO = new UsuariosDAOImpl();
		return usuarioDAO.ActualizaUsuario(usuario);
	}

	@Override
	public int ActivaInactivaUsuario(UsuarioDTO usuario, boolean onOff) {
		usuarioDAO = new UsuariosDAOImpl();
		return usuarioDAO.ActivaInactivaUsuario(usuario, onOff);
	}

}
