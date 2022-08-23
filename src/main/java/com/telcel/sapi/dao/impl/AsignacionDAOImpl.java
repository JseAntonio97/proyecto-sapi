package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Actividades;
import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.constantes.Fases;
import com.telcel.sapi.dao.AsignacionDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.AsignacionDTO;
import com.telcel.sapi.dto.AsignacionesDTO;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.PermisosDTO;
import com.telcel.sapi.dto.PortafolioEmpresarialDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class AsignacionDAOImpl implements AsignacionDAO, Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2044993367064221220L;
	static final Logger LOG = Logger.getLogger(AsignacionDAOImpl.class);
	
	private static final String SELECT_PROYECTOS 				= "SELECT PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, PE.FOLIO, ASG.IDASIGNACION, ASG.IDUSUARIO AS USUARIOASIGNADO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, EST.IDESTRATEGIA, EST.ESTRATEGIA, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO INNER JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN TT_USUARIO USR ON ASG.IDUSUARIO = USR.IDUSUARIO LEFT JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.ESTATUS IN ('LIBERADO','ASIGNADO','TERMINADO') ORDER BY FIELD ( PYT.ESTATUS, 'LIBERADO','ASIGNADO','TERMINADO')";

//	private static final String SELECT_COLABORADORES_BY_LIDER	= "SELECT IDUSUARIO, NOMBRE, PRIMERAPELLIDO, SEGUNDOAPELLIDO, CORREO, IDLIDER FROM TT_USUARIO WHERE IDLIDER = ? OR IDUSUARIO = ?";
	private static final String SELECT_COLABORADORES_BY_LIDER	= "SELECT USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, USR.CORREO, USR2.IDUSUARIO AS IDUSUARIOLIDER, USR2.NOMBRE AS NOMBRELIDER, USR2.PRIMERAPELLIDO AS PRIMERAPELLIDOLIDER, USR2.SEGUNDOAPELLIDO AS SEGUNDOAPELLIDOLIDER FROM TT_USUARIO USR LEFT JOIN TT_USUARIO USR2 ON USR.IDLIDER = USR2.IDUSUARIO WHERE USR.ESTATUS = 'ACTIVO' AND USR.IDLIDER = ? OR USR.IDUSUARIO = ?";
//	private static final String SELECT_COLABORADORES_TO_BOSS	= "SELECT IDUSUARIO, NOMBRE, PRIMERAPELLIDO, SEGUNDOAPELLIDO, CORREO, IDLIDER FROM TT_USUARIO WHERE IDLIDER IN ( SELECT IDLIDER FROM TT_USUARIO WHERE NOMBREUSUARIO != ? ) ORDER BY IDLIDER";
	private static final String SELECT_COLABORADORES_TO_BOSS	= "SELECT USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, USR.CORREO, USR2.IDUSUARIO AS IDUSUARIOLIDER, USR2.NOMBRE AS NOMBRELIDER, USR2.PRIMERAPELLIDO AS PRIMERAPELLIDOLIDER, USR2.SEGUNDOAPELLIDO AS SEGUNDOAPELLIDOLIDER FROM TT_USUARIO USR LEFT JOIN TT_USUARIO USR2 ON USR.IDLIDER = USR2.IDUSUARIO WHERE USR.ESTATUS = 'ACTIVO' AND USR.IDLIDER IN ( SELECT IDLIDER FROM TT_USUARIO WHERE NOMBREUSUARIO != ? ) ORDER BY USR.IDLIDER";
	private static final String INSERT_ASIGNACION				= "INSERT INTO TT_ASIGNACION (IDUSUARIO, IDPROYECTO, FECASIGNACION, TIPOASIGNACION) VALUES (?, ?, CURDATE(), ?)";
	private static final String UPDATE_PROYECTO_ASIGNACION		= "UPDATE TT_PROYECTO SET IDESTRATEGIA = ?, FASESIGUIENTE = ?, ESTATUS = ? WHERE IDPROYECTO = ?";
	
	private static final String UPDATE_RE_ASIGNACION			= "UPDATE TT_ASIGNACION SET IDUSUARIO = ?, FECASIGNACION = CURDATE() WHERE IDASIGNACION = ? AND IDPROYECTO = ? AND IDUSUARIO = ?";
	
	private static final String SELECT_PROYECTOS_BY_PARAMETERS	= "SELECT PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, ASG.IDASIGNACION, ASG.IDUSUARIO AS USUARIOASIGNADO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, EST.IDESTRATEGIA, EST.ESTRATEGIA , GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT INNER JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_USUARIO USR ON ASG.IDUSUARIO = USR.IDUSUARIO LEFT JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.ESTATUS IN ('LIBERADO','ASIGNADO','TERMINADO') AND ( ? IS NULL OR PYT.NOMBREPROYECTO LIKE ?) AND ( ? IS NULL OR PYT.INTEGRADOR LIKE ? ) AND ( ? IS NULL OR PE.FOLIO LIKE  ?) ORDER BY FIELD ( PYT.ESTATUS, 'LIBERADO','ASIGNADO','TERMINADO')";
	
	private static final String HUGO		= "Hugo";
	private static final String CABALLERO	= "Caballero";
	private static final String RAMIREZ		= "Ramirez";

	private static final String HECTOR		= "Hector";
	private static final String MORALES		= "Morales";
	private static final String BOBADILLA	= "Bobadilla";

	private static final String BUSCA_USUARIOS_CON_ASIGNACION	= "SELECT DISTINCT IDUSUARIO FROM TT_ASIGNACION";
	private static final String BUSCA_CANTIDAD_ASIGNACIONES		= "SELECT USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, COUNT(*) AS ASIGNACIONES FROM TT_ASIGNACION ASG INNER JOIN TT_USUARIO USR ON ASG.IDUSUARIO = USR.IDUSUARIO INNER JOIN TT_PROYECTO PYT ON PYT.IDPROYECTO = ASG.IDPROYECTO WHERE ASG.IDUSUARIO = ? AND PYT.ESTATUS = 'ASIGNADO'";
	private static final String MARCA_PROYECTO_NO_LEIDO 		= "UPDATE TT_PROYECTO SET LEIDO = ? WHERE IDPROYECTO = ?";
	
	public List<ProyectoDTO> CargaProyectos(){
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_PROYECTOS);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listProyectos.add(new ProyectoDTO(
						rs.getLong	("IDPROYECTO"),
						rs.getString("IDENTIFICADORINTERNO"),
						rs.getString("INTEGRADOR"),
						rs.getString("NOMBREPROYECTO"),
						rs.getString("DESCRIPCION"),
						rs.getString("SOLICITANTE"),
						rs.getString("DIRECCION"),
						rs.getString("GERENCIA"),
						new GerenciaDTO(
								rs.getLong("IDGERENCIA"), 
								rs.getString("UPDATE_GERENCIA"), 
								new DireccionDTO(
										rs.getLong("IDDIRECCION"), 
										rs.getString("UPDATE_DIRECCION"),
										""),
								""
								),
						rs.getInt	("PORCAVANCEACTUAL"),
						rs.getString("PORCAVANCEANTERIOR"),
						rs.getString("FASEACTUAL"),
						rs.getString("FASESIGUIENTE"),
						rs.getString("COMENTARIOSGRLS"),
						rs.getString("ESTATUS"),
						rs.getString("FECHAREGISTRO"),
						rs.getString("HORAREGISTRO"),
						rs.getLong	("LEIDO"),
						null,
						new EstrategiaDTO(
								rs.getInt	("IDESTRATEGIA"), 
								rs.getString("ESTRATEGIA"),
								""),
						new PortafolioEmpresarialDTO(
								0,
								"",
								rs.getString("FOLIO"),
								"",
								""),
						null,
						new AsignacionDTO(
								rs.getInt	("IDASIGNACION"),
								rs.getInt	("USUARIOASIGNADO"),
								rs.getString("FECASIGNACION"),
								rs.getString("TIPOASIGNACION")
								),
						new UsuarioDTO(
								rs.getLong("USUARIOASIGNADO"), 
								rs.getString("NOMBRE"), 
								rs.getString("PRIMERAPELLIDO"), 
								rs.getString("SEGUNDOAPELLIDO"), 
								"", 
								"", 
								"",
								"",
								"",
								null, 
								null, 
								null),
						null,
						null,
						null,
						null,
						null,
						null
						));
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listProyectos;
	}
	

	@Override
	public List<UsuarioDTO> CargaColaboradores(UsuarioDTO user) {
		List<UsuarioDTO> listColaboradores = new ArrayList<UsuarioDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			if(user.getNombre().equals(HUGO) && user.getPrimerApellido().equals(CABALLERO) && user.getSegundoApellido().equals(RAMIREZ)  || 
					user.getNombre().equals(HECTOR) && user.getPrimerApellido().equals(MORALES) && user.getSegundoApellido().equals(BOBADILLA) ) {

				stmt = conn.prepareStatement(SELECT_COLABORADORES_TO_BOSS);
				stmt.setString(1, user.getNombreUsuario());
			}else {
				stmt = conn.prepareStatement(SELECT_COLABORADORES_BY_LIDER);
				stmt.setLong(1, user.getIdUsuario());
				stmt.setLong(2, user.getIdUsuario());
			}
			
			
			rs	 = stmt.executeQuery();
			

			while(rs.next()) {
				listColaboradores.add(
						new UsuarioDTO(
								rs.getLong	("IDUSUARIO"),
								rs.getString("NOMBRE"), 
								(rs.getString("PRIMERAPELLIDO") == null) ? "" : rs.getString("PRIMERAPELLIDO"), 
								(rs.getString("SEGUNDOAPELLIDO") == null) ? "" : rs.getString("SEGUNDOAPELLIDO"), 
								"", 
								"", 
								"", 
								(rs.getString("CORREO") == null) ? "" : rs.getString("CORREO"),
								"",
								null,
								null, 
								new PermisosDTO(0L, false, false, false, false, false, false, false)
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
		
		return listColaboradores;
	}

	@SuppressWarnings("resource")
	@Override
	public int AsignaProyectos(UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia) {
		int registro = 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, AsignacionDAOImpl, L161: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(INSERT_ASIGNACION);

			stmt.setLong	(1, usrAsignado.getIdUsuario());
			stmt.setLong	(2, proyecto.getIdProyecto());
			stmt.setString	(3, Actividades.ASIGNACION_NORMAL);
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_ASIGNACION);
			stmt.setInt		(1, idEstrategia);
			stmt.setString	(2, Fases.F60.toString());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();

			registro = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
			registro = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
			return registro;
		}finally {
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return registro;
	}
	
	@SuppressWarnings("resource")
	@Override
	public int ReAsignaProyecto(UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia) {
		int registro = 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, AsignacionDAOImpl, L215: " + autoCommit);
			}
			
			stmt 	= null;			
			stmt = conn.prepareStatement(UPDATE_RE_ASIGNACION);
			stmt.setLong	(1, usrAsignado.getIdUsuario());
			stmt.setInt		(2, proyecto.getAsignacion().getIdAsignacion());
			stmt.setLong	(3, proyecto.getIdProyecto());
			stmt.setInt		(4, proyecto.getAsignacion().getIdUsuario());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_ASIGNACION);
			stmt.setInt		(1, idEstrategia);
			stmt.setString	(2, proyecto.getFaseSiguiente());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();

			registro = 1;
			conn.commit();
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
			registro = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
			return registro;
		}finally {
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return registro;
	}

	@Override
	public List<ProyectoDTO> BusquedaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_PROYECTOS_BY_PARAMETERS);
			stmt.setString(1, (nomProyecto == null) ? null : "%"+nomProyecto+"%");
			stmt.setString(2, (nomProyecto == null) ? null : "%"+nomProyecto+"%");
			
			stmt.setString(3, (integrador == null) ? null : "%"+integrador+"%");
			stmt.setString(4, (integrador == null) ? null : "%"+integrador+"%");
			
			stmt.setString(5, (folioPortEmp == null) ? null : "%"+folioPortEmp+"%");
			stmt.setString(6, (folioPortEmp == null) ? null : "%"+folioPortEmp+"%");
			
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listProyectos.add(new ProyectoDTO(
						rs.getLong("IDPROYECTO"), 
						rs.getString("IDENTIFICADORINTERNO"), 
						rs.getString("INTEGRADOR"), 
						rs.getString("NOMBREPROYECTO"), 
						rs.getString("DESCRIPCION"), 
						rs.getString("SOLICITANTE"), 
						rs.getString("DIRECCION"), 
						rs.getString("GERENCIA"), 
						new GerenciaDTO(
								rs.getLong("IDGERENCIA"), 
								rs.getString("UPDATE_GERENCIA"), 
								new DireccionDTO(
										rs.getLong("IDDIRECCION"), 
										rs.getString("UPDATE_DIRECCION"),
										""),
								""
								),
						rs.getInt	("PORCAVANCEACTUAL"), 
						rs.getString("PORCAVANCEANTERIOR"), 
						rs.getString("FASEACTUAL"), 
						rs.getString("FASESIGUIENTE"), 
						rs.getString("COMENTARIOSGRLS"), 
						rs.getString("ESTATUS"), 
						rs.getString("FECHAREGISTRO"), 
						rs.getString("HORAREGISTRO"), 
						rs.getLong	("LEIDO"),
						null, 
						new EstrategiaDTO(
								rs.getInt	("IDESTRATEGIA"), 
								rs.getString("ESTRATEGIA"),
								""), 
						new PortafolioEmpresarialDTO(
								rs.getInt("IDSEGUIMIENTOPE"), 
								rs.getString("APLICA"), 
								rs.getString("FOLIO"), 
								rs.getString("ENTRADA"), 
								rs.getString("SALIDA")), 
						null, 
						new AsignacionDTO(
								rs.getInt("IDASIGNACION"), 
								rs.getInt("USUARIOASIGNADO"), 
								rs.getString("FECASIGNACION"), 
								rs.getString("TIPOASIGNACION")), 
						new UsuarioDTO(
								rs.getLong("USUARIOASIGNADO"), 
								rs.getString("NOMBRE"), 
								rs.getString("PRIMERAPELLIDO"), 
								rs.getString("SEGUNDOAPELLIDO"), 
								"", "", "", "", "",
								null, 
								null, 
								null), 
						null, 
						null, 
						null, 
						null, 
						null, 
						null)
						);
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		return listProyectos;
	}

	@SuppressWarnings("resource")
	@Override
	public List<AsignacionesDTO> ConteoAsignaciones() {
		List<AsignacionesDTO> 	listConteo 		= new ArrayList<AsignacionesDTO>();
		List<Integer> 			listIdAsignados = new ArrayList<Integer>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(BUSCA_USUARIOS_CON_ASIGNACION);			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listIdAsignados.add(
							rs.getInt("IDUSUARIO")
						);
			}
			
			if(listIdAsignados.size() != 0) {
				stmt 	= null;
				rs		= null;
				stmt = conn.prepareStatement(BUSCA_CANTIDAD_ASIGNACIONES);
				
				for(Integer idUsuario : listIdAsignados) {
					stmt.setInt(1, idUsuario);
					rs	 = stmt.executeQuery();
					
					if (rs.next()) {
						listConteo.add(
								new AsignacionesDTO(rs.getString("NOMBRE") + " " + rs.getString("PRIMERAPELLIDO") + " " + rs.getString("SEGUNDOAPELLIDO"), rs.getInt("ASIGNACIONES")));		
					}
				}
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos u ocurrio un error: " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listConteo;
	}


	@Override
	public void MarcaProyectoNoLeido(ProyectoDTO proyecto) {	
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L442: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(MARCA_PROYECTO_NO_LEIDO);
			stmt.setInt		(1, 0);
			stmt.setLong	(2, proyecto.getIdProyecto());
			stmt.executeUpdate();			
			
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos" + e);
			
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
	}
			
}
