package com.telcel.sapi.mb;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.telcel.sapi.dto.UsuarioDTO;

@ManagedBean (name = "bandejaMB")
@ViewScoped
public class BandejaSIMB implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7306903988620976844L;
	static final Logger LOG = Logger.getLogger(BandejaSIMB.class);

	private UsuarioDTO usuario;
	
	private String instrucciones = "";

	@PostConstruct
	private void init() {
		//Valida el rol de acceso
		usuario = new UsuarioDTO();
		usuario = (UsuarioDTO) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioSesion");
		// ---- ----
		
		
		instrucciones = "";
	}

	public String getInstrucciones() {
		return instrucciones;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	
}
