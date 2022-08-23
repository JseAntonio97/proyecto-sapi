package com.telcel.sapi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.telcel.sapi.dao.LoginDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.PermisosDTO;
import com.telcel.sapi.dto.RolDTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class LoginDAOImpl implements LoginDAO {
	static final Logger LOG = Logger.getLogger(LoginDAOImpl.class);
	
	private static final String SELECT_USUARIO_LOGIN = "SELECT USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, USR.NOMBREUSUARIO, USR.PASSWORD, USR.PASSWORDANTES, USR.CORREO, USR.IDLIDER, USR.ESTATUS, ROL.IDROL, PER.REGISTROPROYECTO, PER.ASIGNACION, PER.INFRAESTRUCTURA, PER.SEGUIMIENTO, PER.FUNCIONESOPERATIVAS, PER.REPORTES, PER.SEGUIMIENTOGENERAL FROM TT_USUARIO USR INNER JOIN CAT_ROL ROL ON USR.IDROL = ROL.IDROL INNER JOIN TT_PERMISOS PER ON USR.IDPERMISO = PER.IDPERMISO WHERE USR.NOMBREUSUARIO =  ? ";

	@Override
	public UsuarioDTO cargaUsuario(String usr) {
		UsuarioDTO usuarioLogin		= null;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(SELECT_USUARIO_LOGIN);
			stmt.setString(1, usr);
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				
				usuarioLogin = new UsuarioDTO(
						rs.getLong	("IDUSUARIO"), 
						rs.getString("NOMBRE"), 
						rs.getString("PRIMERAPELLIDO"), 
						rs.getString("SEGUNDOAPELLIDO"), 
						rs.getString("NOMBREUSUARIO"), 
						rs.getString("PASSWORD"), 
						rs.getString("PASSWORDANTES"), 
						rs.getString("CORREO"),
						rs.getString("ESTATUS"), 
						null, 
						new RolDTO(), 
						new PermisosDTO(
								0L,
								(rs.getInt("REGISTROPROYECTO") 		== 1) ? true : false, 
								(rs.getInt("INFRAESTRUCTURA") 		== 1) ? true : false, 
								(rs.getInt("SEGUIMIENTO") 			== 1) ? true : false, 
								(rs.getInt("ASIGNACION") 			== 1) ? true : false, 
								(rs.getInt("FUNCIONESOPERATIVAS") 	== 1) ? true : false, 
								(rs.getInt("REPORTES") 				== 1) ? true : false, 
								(rs.getInt("SEGUIMIENTOGENERAL") 	== 1) ? true : false
								)
						);
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return usuarioLogin;
	}

}
