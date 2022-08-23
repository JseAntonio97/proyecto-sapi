package com.telcel.sapi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.dao.UsuariosDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.PermisosDTO;
import com.telcel.sapi.dto.RolDTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class UsuariosDAOImpl implements UsuariosDAO {
	static final Logger LOG = Logger.getLogger(UsuariosDAOImpl.class);

	private static final String SELECT_USUARIOS 		= "SELECT USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, USR.NOMBREUSUARIO, USR.CORREO, USR.ESTATUS, USR2.IDUSUARIO AS IDUSUARIOLIDER, USR2.NOMBRE AS NOMBRELIDER, USR2.PRIMERAPELLIDO AS PRIMERAPELLIDOLIDER, USR2.SEGUNDOAPELLIDO AS SEGUNDOAPELLIDOLIDER, PER.IDPERMISO, PER.REGISTROPROYECTO, PER.ASIGNACION, PER.INFRAESTRUCTURA, PER.SEGUIMIENTO, PER.FUNCIONESOPERATIVAS, PER.REPORTES, PER. SEGUIMIENTOGENERAL FROM TT_USUARIO USR LEFT JOIN TT_USUARIO USR2 ON USR.IDLIDER = USR2.IDUSUARIO INNER JOIN TT_PERMISOS PER ON USR.IDPERMISO = PER.IDPERMISO WHERE USR.IDROL IN (1,2)";
	private static final String INSERT_USUARIO_CON_LIDER= "INSERT INTO TT_USUARIO (NOMBRE, PRIMERAPELLIDO, SEGUNDOAPELLIDO, NOMBREUSUARIO, PASSWORD, CORREO, IDROL, IDLIDER, IDPERMISO, ESTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	private static final String INSERT_USUARIO_SIN_LIDER= "INSERT INTO TT_USUARIO (NOMBRE, PRIMERAPELLIDO, SEGUNDOAPELLIDO, NOMBREUSUARIO, PASSWORD, CORREO, IDROL, IDPERMISO, ESTATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";
	private static final String INSERT_PERMISOS			= "INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES, SEGUIMIENTOGENERAL) VALUES ( ?, ?, ?, ?, ?, ?, ?);";
	private static final String PASSWORD_DEFAULT		= "123456";

	private static final String UPDATE_USUARIO_CON_LIDER= "UPDATE TT_USUARIO SET NOMBRE = ?, PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?, NOMBREUSUARIO = ?, CORREO = ?, IDLIDER = ? WHERE IDUSUARIO = ?";
	private static final String UPDATE_USUARIO_SIN_LIDER= "UPDATE TT_USUARIO SET NOMBRE = ?, PRIMERAPELLIDO = ?, SEGUNDOAPELLIDO = ?, NOMBREUSUARIO = ?, CORREO = ?, IDLIDER = NULL WHERE IDUSUARIO = ?";
	private static final String UPDATE_PERMISOS			= "UPDATE TT_PERMISOS SET REGISTROPROYECTO = ?, ASIGNACION = ?, INFRAESTRUCTURA = ?, SEGUIMIENTO = ?, FUNCIONESOPERATIVAS = ?, REPORTES = ?, SEGUIMIENTOGENERAL = ? WHERE IDPERMISO = ?";
	
	private static final String ON_OFF_USUARIO			= "UPDATE TT_USUARIO SET ESTATUS = ? WHERE IDUSUARIO = ? ";
	
	@Override
	public List<UsuarioDTO> ConsultaUsuarios() {
		List<UsuarioDTO> listUsuarios = new ArrayList<UsuarioDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_USUARIOS);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listUsuarios.add(
						new UsuarioDTO(
								rs.getLong	("IDUSUARIO"), 
								rs.getString("NOMBRE"), 
								rs.getString("PRIMERAPELLIDO"), 
								rs.getString("SEGUNDOAPELLIDO"), 
								rs.getString("NOMBREUSUARIO"), 
								"", 
								"", 
								rs.getString("CORREO"),
								rs.getString("ESTATUS"),
								new UsuarioDTO(
										rs.getLong	("IDUSUARIOLIDER"), 
										rs.getString("NOMBRELIDER"), 
										rs.getString("PRIMERAPELLIDOLIDER"), 
										rs.getString("SEGUNDOAPELLIDOLIDER"), 
										"", "", "", "", "", null, null, null), 
								new RolDTO(), 
								new PermisosDTO(
										rs.getLong("IDPERMISO"),
										(rs.getInt("REGISTROPROYECTO") 		== 1) ? true : false, 
										(rs.getInt("INFRAESTRUCTURA") 		== 1) ? true : false, 
										(rs.getInt("SEGUIMIENTO") 			== 1) ? true : false, 
										(rs.getInt("ASIGNACION") 			== 1) ? true : false, 
										(rs.getInt("FUNCIONESOPERATIVAS") 	== 1) ? true : false, 
										(rs.getInt("REPORTES") 				== 1) ? true : false, 
										(rs.getInt("SEGUIMIENTOGENERAL") 	== 1) ? true : false
										)
								)
						);
			}
			
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + " No se logró conectar a la base de datos u ocurrio un error con la sentencia SQL - " + e);
		}
		
		return listUsuarios;
	}

	@SuppressWarnings("resource")
	@Override
	public int GuardaUsuario(UsuarioDTO usuario) {		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		int				exito		= 0;
		int				idPermisos	= 0;
		
		try {
			conn = Conexion.getConnection();
			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + " Error al retirar el autocommit " + autoCommit);
			}
			
			stmt = conn.prepareStatement(INSERT_PERMISOS, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, (usuario.getPermisos().isRegistros())			? 1 : 0);
			stmt.setInt(2, (usuario.getPermisos().isAsignacion())			? 1 : 0);
			stmt.setInt(3, (usuario.getPermisos().isInfraestructura())		? 1 : 0);
			stmt.setInt(4, (usuario.getPermisos().isSeguimiento())			? 1 : 0);
			stmt.setInt(5, (usuario.getPermisos().isFuncionesOperativas())	? 1 : 0);
			stmt.setInt(6, (usuario.getPermisos().isReportes())				? 1 : 0);
			stmt.setInt(7, (usuario.getPermisos().isSeguimientoGrl())		? 1 : 0);
			
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				idPermisos	= rs.getInt(1);
			}
			
			stmt = null;
			if(usuario.getLider().getIdUsuario().equals(0L)) {
				stmt = conn.prepareStatement(INSERT_USUARIO_SIN_LIDER);

				stmt.setString	(1, usuario.getNombre());
				stmt.setString	(2, usuario.getPrimerApellido());
				stmt.setString	(3, usuario.getSegundoApellido());
				stmt.setString	(4, usuario.getNombreUsuario());
				stmt.setString	(5, PASSWORD_DEFAULT);
				stmt.setString	(6, usuario.getCorreo());
				stmt.setInt		(7, usuario.getRol().getIdRol());
				stmt.setInt		(8, idPermisos);
				stmt.setString	(9, Estatus.ACTIVO.toString());
				stmt.executeUpdate();
			}else {
				stmt = conn.prepareStatement(INSERT_USUARIO_CON_LIDER);

				stmt.setString	(1, usuario.getNombre());
				stmt.setString	(2, usuario.getPrimerApellido());
				stmt.setString	(3, usuario.getSegundoApellido());
				stmt.setString	(4, usuario.getNombreUsuario());
				stmt.setString	(5, PASSWORD_DEFAULT);
				stmt.setString	(6, usuario.getCorreo());
				stmt.setInt		(7, usuario.getRol().getIdRol());
				stmt.setLong	(8, usuario.getLider().getIdUsuario());
				stmt.setInt		(9, idPermisos);
				stmt.setString	(10, Estatus.ACTIVO.toString());
				stmt.executeUpdate();
			}
			
			exito		= 1;
			conn.commit();			
		} catch (SQLException e) {
			LOG.error(this.getClass() + " No se logró conectar a la base de datos u ocurrio un error con la sentencia SQL - " + e);
			exito = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + " Error al realizar el Rollback " + rollback);
			}
		}finally {
			try {				
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return exito;
	}

	@SuppressWarnings("resource")
	@Override
	public int ActualizaUsuario(UsuarioDTO usuario) {
		int result 		= 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L815: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_PERMISOS);
			
			stmt.setInt		(1, (usuario.getPermisos().isRegistros()) 			? 1 : 0);
			stmt.setInt		(2, (usuario.getPermisos().isAsignacion())		 	? 1 : 0);
			stmt.setInt		(3, (usuario.getPermisos().isInfraestructura())		? 1 : 0);
			stmt.setInt		(4, (usuario.getPermisos().isSeguimiento())			? 1 : 0);
			stmt.setInt		(5, (usuario.getPermisos().isFuncionesOperativas()) ? 1 : 0);
			stmt.setInt		(6, (usuario.getPermisos().isReportes()) 			? 1 : 0);
			stmt.setInt		(7, (usuario.getPermisos().isSeguimientoGrl()) 		? 1 : 0);		
			stmt.setLong	(8, usuario.getPermisos().getIdPermiso());
			stmt.executeUpdate();
			
			stmt = null;
			
			if(usuario.getLider().getIdUsuario().equals(0L)) {
				stmt = conn.prepareStatement(UPDATE_USUARIO_SIN_LIDER);
			}else {
				stmt = conn.prepareStatement(UPDATE_USUARIO_CON_LIDER);
			}

			stmt.setString  (1, usuario.getNombre());
			stmt.setString  (2, usuario.getPrimerApellido());
			stmt.setString  (3, usuario.getSegundoApellido());
			stmt.setString  (4, usuario.getNombreUsuario());
			stmt.setString  (5, usuario.getCorreo());
			
			if(usuario.getLider().getIdUsuario().equals(0L)) {
				stmt.setLong	(6, usuario.getIdUsuario());
			}else {
				stmt.setLong	(6, usuario.getLider().getIdUsuario());			
				stmt.setLong	(7, usuario.getIdUsuario());
			}			
			stmt.executeUpdate();
			
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + " No se logró conectar a la base de datos u ocurrio un error con la sentencia SQL - " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + " Error al realizar el Rollback " + rollback);
			}
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
			
			
		return result;
	}

	@Override
	public int ActivaInactivaUsuario(UsuarioDTO usuario, boolean onOff) {
		int result 		= 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + " Error al retirar el autocommit " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ON_OFF_USUARIO);
			stmt.setString	(1, (onOff) ? Estatus.ACTIVO.toString() : Estatus.INACTIVO.toString());
			stmt.setLong	(2, usuario.getIdUsuario());
			
			stmt.executeUpdate();
			
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + " No se logró conectar a la base de datos u ocurrio un error con la sentencia SQL - " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + " Error al realizar el Rollback " + rollback);
			}
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}		
		
		return result;
	}

}
