package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.dao.SeguimientoDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.AsignacionDTO;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.PortafolioEmpresarialDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class SeguimientoDAOImpl implements SeguimientoDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2843502501790138172L;
	static final Logger LOG = Logger.getLogger(SeguimientoDAOImpl.class);
	
	private static final String SELECT_PROYECTOS_ASIGNADOS_BY_USER 	= "SELECT ASG.IDASIGNACION, ASG.IDUSUARIO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA, EST.IDESTRATEGIA, EST.ESTRATEGIA, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_ASIGNACION ASG INNER JOIN TT_PROYECTO PYT ON ASG.IDPROYECTO = PYT.IDPROYECTO INNER JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO INNER JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA INNER JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.ESTATUS IN ( 'ASIGNADO' ,'TERMINADO') AND ASG.IDUSUARIO = ? ORDER BY PYT.LEIDO, PYT.ESTATUS ASC";
	private static final String BUSCA_PROYECTOS_BY_PARAMETROS		= "SELECT ASG.IDASIGNACION, ASG.IDUSUARIO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA, EST.IDESTRATEGIA, EST.ESTRATEGIA, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_ASIGNACION ASG INNER JOIN TT_PROYECTO PYT ON ASG.IDPROYECTO = PYT.IDPROYECTO INNER JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO INNER JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA INNER JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE ( ? IS NULL OR PYT.NOMBREPROYECTO LIKE ?) AND ( ? IS NULL OR PYT.INTEGRADOR LIKE ?) AND ( ? IS NULL OR PE.FOLIO LIKE ?) AND PYT.ESTATUS IN ( 'ASIGNADO' ,'TERMINADO') AND ASG.IDUSUARIO = ? ORDER BY PYT.LEIDO, PYT.ESTATUS ASC";
	private static final String BUSCA_PROYECTOS_BY_PARAMETROS_INFRA	= "SELECT DISTINCT ASG.IDASIGNACION, ASG.IDUSUARIO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA, EST.IDESTRATEGIA, EST.ESTRATEGIA, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA , GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_ASIGNACION ASG INNER JOIN TT_PROYECTO PYT ON ASG.IDPROYECTO = PYT.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO INNER JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO INNER JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA INNER JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE ( ? IS NULL OR PYT.NOMBREPROYECTO LIKE ? ) AND ( ? IS NULL OR PYT.INTEGRADOR LIKE ? ) AND ( ? IS NULL OR PE.FOLIO LIKE ? ) AND ( ? IS NULL OR AMB.IP LIKE ? ) AND ( ? IS NULL OR AMB.HOSTNAME LIKE ? ) AND PYT.ESTATUS IN ( 'ASIGNADO' ,'TERMINADO') AND ASG.IDUSUARIO = ? ORDER BY PYT.LEIDO, PYT.ESTATUS ASC";
	
	private static final String SELECT_ALL_PROYECTOS_ASIGNADOS 		= "SELECT DISTINCT ASG.IDASIGNACION, ASG.IDUSUARIO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA, EST.IDESTRATEGIA, EST.ESTRATEGIA, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION ORDER BY PYT.LEIDO, PYT.ESTATUS ASC";
	private static final String BUSCA_ALL_PROYECTOS_BY_PARAMETROS	= "SELECT DISTINCT ASG.IDASIGNACION, ASG.IDUSUARIO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA, EST.IDESTRATEGIA, EST.ESTRATEGIA, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO LEFT JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION  WHERE ( ? IS NULL OR PYT.NOMBREPROYECTO LIKE ? ) AND ( ? IS NULL OR PYT.INTEGRADOR LIKE ?) AND ( ? IS NULL OR PE.FOLIO LIKE ?) AND ( ? IS NULL OR AMB.IP LIKE ?) AND ( ? IS NULL OR AMB.HOSTNAME LIKE ?) ORDER BY PYT.LEIDO, PYT.ESTATUS ASC";

	@Override
	public List<ProyectoDTO> FindAllProyects(Long idUsuario) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_PROYECTOS_ASIGNADOS_BY_USER);
			stmt.setLong	(1, idUsuario);
			
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
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						new AsignacionDTO(
								rs.getInt	("IDASIGNACION"),
								rs.getInt	("IDUSUARIO"),
								rs.getString("FECASIGNACION"),
								rs.getString("TIPOASIGNACION")),
						null,
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
	public List<ProyectoDTO> BuscaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp, Long idUsuario) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(BUSCA_PROYECTOS_BY_PARAMETROS);
			
			stmt.setString	(1, (nomProyecto.isEmpty()) ? null : "%"+nomProyecto+"%");
			stmt.setString	(2, (nomProyecto.isEmpty()) ? null : "%"+nomProyecto+"%");
			
			stmt.setString	(3, (integrador.isEmpty()) ? null : "%"+integrador+"%");
			stmt.setString	(4, (integrador.isEmpty()) ? null : "%"+integrador+"%");
			
			stmt.setString	(5, (folioPortEmp.isEmpty()) ? null : "%"+folioPortEmp+"%");
			stmt.setString	(6, (folioPortEmp.isEmpty()) ? null : "%"+folioPortEmp+"%");
			stmt.setLong	(7, idUsuario);
			
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
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						new AsignacionDTO(
								rs.getInt	("IDASIGNACION"),
								rs.getInt	("IDUSUARIO"),
								rs.getString("FECASIGNACION"),
								rs.getString("TIPOASIGNACION")),
						null,
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
	public List<ProyectoDTO> BuscaProyectosByParametrosInfra(String nomProyecto, String integrador, String folioPortEmp,
			String ip, String hostName, Long idUsuario) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(BUSCA_PROYECTOS_BY_PARAMETROS_INFRA);
			
			stmt.setString	(1, (nomProyecto.isEmpty()) 	? null : "%"+ nomProyecto +"%");
			stmt.setString	(2, (nomProyecto.isEmpty()) 	? null : "%"+ nomProyecto +"%");
			
			stmt.setString	(3, (integrador.isEmpty()) 		? null : "%"+ integrador +"%");
			stmt.setString	(4, (integrador.isEmpty()) 		? null : "%"+ integrador +"%");
			
			stmt.setString	(5, (folioPortEmp.isEmpty()) 	? null : "%"+ folioPortEmp +"%");
			stmt.setString	(6, (folioPortEmp.isEmpty()) 	? null : "%"+ folioPortEmp +"%");
			
			stmt.setString	(7, (ip.isEmpty()) 				? null : "%"+ ip +"%");
			stmt.setString	(8, (ip.isEmpty()) 				? null : "%"+ ip +"%");
			
			stmt.setString	(9, (hostName.isEmpty()) 		? null : "%"+ hostName +"%");
			stmt.setString	(10, (hostName.isEmpty()) 		? null : "%"+ hostName +"%");
			
			stmt.setLong	(11, idUsuario);
			
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
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						new AsignacionDTO(
								rs.getInt	("IDASIGNACION"),
								rs.getInt	("IDUSUARIO"),
								rs.getString("FECASIGNACION"),
								rs.getString("TIPOASIGNACION")),
						null,
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
	public List<ProyectoDTO> BuscaAllAsignedProyectosByParametros(String nomProyecto, String integrador,
			String folioPortEmp, String ip, String hostName) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(BUSCA_ALL_PROYECTOS_BY_PARAMETROS);
			
			stmt.setString	(1, (nomProyecto.isEmpty()) 	? null : "%"+ nomProyecto +"%");
			stmt.setString	(2, (nomProyecto.isEmpty()) 	? null : "%"+ nomProyecto +"%");
			
			stmt.setString	(3, (integrador.isEmpty()) 		? null : "%"+ integrador +"%");
			stmt.setString	(4, (integrador.isEmpty()) 		? null : "%"+ integrador +"%");
			
			stmt.setString	(5, (folioPortEmp.isEmpty()) 	? null : "%"+ folioPortEmp +"%");
			stmt.setString	(6, (folioPortEmp.isEmpty()) 	? null : "%"+ folioPortEmp +"%");
			
			stmt.setString	(7, (ip.isEmpty()) 				? null : "%"+ ip +"%");
			stmt.setString	(8, (ip.isEmpty()) 				? null : "%"+ ip +"%");
			
			stmt.setString	(9, (hostName.isEmpty()) 		? null : "%"+ hostName +"%");
			stmt.setString	(10, (hostName.isEmpty()) 		? null : "%"+ hostName +"%");
			
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
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						new AsignacionDTO(
								rs.getInt	("IDASIGNACION"),
								rs.getInt	("IDUSUARIO"),
								rs.getString("FECASIGNACION"),
								rs.getString("TIPOASIGNACION")),
						new UsuarioDTO(
								rs.getLong("IDUSUARIO"), 
								rs.getString("NOMBRE"), 
								rs.getString("PRIMERAPELLIDO"), 
								rs.getString("SEGUNDOAPELLIDO"), 
								"", "", "", "", "", null, null, null),
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
	public List<ProyectoDTO> FindAllProyectsAsigned() {
		 List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_ALL_PROYECTOS_ASIGNADOS);
			
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
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						new AsignacionDTO(
								rs.getInt	("IDASIGNACION"),
								rs.getInt	("IDUSUARIO"),
								rs.getString("FECASIGNACION"),
								rs.getString("TIPOASIGNACION")),
						new UsuarioDTO(
								rs.getLong("IDUSUARIO"), 
								rs.getString("NOMBRE"), 
								rs.getString("PRIMERAPELLIDO"), 
								rs.getString("SEGUNDOAPELLIDO"), 
								"", "", "", "", "", null, null, null),
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
}
