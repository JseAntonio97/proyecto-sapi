package com.telcel.sapi.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.UsuariosService;
import com.telcel.sapi.service.impl.UsuariosServiceImpl;

@ManagedBean (name = "userControlMB")
@ViewScoped
public class UserControlMB implements Serializable{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 7035817674006012723L;
	static final Logger LOG = Logger.getLogger(UserControlMB.class);
	
	UsuariosService usrService;	
	private List<UsuarioDTO> listUsuarios;
	
	@PostConstruct
	public void init() {
		usrService 		= new UsuariosServiceImpl();
		listUsuarios 	= new ArrayList<UsuarioDTO>();
		listUsuarios = usrService.ConsultaUsuarios();
	}

	public List<UsuarioDTO> getListUsuarios() {
		return listUsuarios;
	}

	public void setListUsuarios(List<UsuarioDTO> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}
}
