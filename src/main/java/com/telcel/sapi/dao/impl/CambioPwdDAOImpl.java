package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.telcel.sapi.dao.CambioPwdDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.UsuarioDTO;

public class CambioPwdDAOImpl implements CambioPwdDAO,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3402359716122927798L;
	static final Logger LOG = Logger.getLogger(CambioPwdDAOImpl.class);
	
	private static final String UPDATE_PASSWORD_TO_USER	= "UPDATE TT_USUARIO SET PASSWORD = ?, PASSWORDANTES = ? WHERE IDUSUARIO = ?";

	@Override
	public boolean CambioPwd(UsuarioDTO usuario, String newPwd) {
		boolean result = false;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(UPDATE_PASSWORD_TO_USER);
			stmt.setString	(1, newPwd);
			stmt.setString	(2, usuario.getPassword());
			stmt.setLong	(3, usuario.getIdUsuario());
			
			stmt.executeUpdate();
			
			result = true;
			
		} catch (SQLException e) {
			result = false;
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				result = false;
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return result;
	}

}
