package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.telcel.sapi.dao.BitacoraDAO;
import com.telcel.sapi.database.Conexion;

public class BitacoraDAOImpl implements BitacoraDAO, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8420041391441932440L;
	static final Logger LOG = Logger.getLogger(BitacoraDAOImpl.class);

	private static final String INSERT_BITACORA_CON_PROYECTO 	= "INSERT INTO TT_BITACORA (IDUSUARIO, ACTIVIDAD, DESCRIPCION, MODULO, FECHAHRA, IDPROYECTO) VALUES ( ?, ?, ?, ?, NOW(), ?)";
	private static final String INSERT_BITACORA_SIN_PROYECTO 	= "INSERT INTO TT_BITACORA (IDUSUARIO, ACTIVIDAD, DESCRIPCION, MODULO, FECHAHRA) VALUES ( ?, ?, ?, ?, NOW())";

	@Override
	public int insertBitacora(Long idUsuario, String actividad, String descripcion, String modulo, Long idProyecto) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int				registro	= 0;
		
		try {
			conn = Conexion.getConnection();
			if(idProyecto.equals(0L)) {
				stmt = conn.prepareStatement(INSERT_BITACORA_SIN_PROYECTO);				
				stmt.setLong	(1, idUsuario);
				stmt.setString	(2, actividad);
				stmt.setString	(3, descripcion);
				stmt.setString	(4, modulo);
			}else {
				stmt = conn.prepareStatement(INSERT_BITACORA_CON_PROYECTO);
				
				stmt.setLong	(1, idUsuario);
				stmt.setString	(2, actividad);
				stmt.setString	(3, descripcion);
				stmt.setString	(4, modulo);
				stmt.setLong	(5, idProyecto);
			}
			
			registro = stmt.executeUpdate();
						
		} catch (SQLException e) {
			LOG.error("Error al conectar o ejecutar sentecia en la BD" + e);
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error("Error al cerrar conexiones" + e);
			}
		}
		return registro;
	}
	
}