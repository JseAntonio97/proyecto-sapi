package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.dao.PortEmpDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.PortafolioEmpresarialDTO;
import com.telcel.sapi.dto.ProyectoDTO;

public class PortEmpDAOImpl implements PortEmpDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8585819478228315216L;
	static final Logger LOG = Logger.getLogger(PortEmpDAOImpl.class);
	
	private static final String SELECT_PROYECTOS 		= "SELECT PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.ESTATUS IN ('PORTAFOLIO','REGISTRADO') ORDER BY PYT.ESTATUS DESC";
	private static final String SELECT_NAME_PROYECTOS 	= "SELECT NOMBREPROYECTO FROM TT_PROYECTO WHERE ESTATUS IN ('PORTAFOLIO','REGISTRADO')";
	private static final String SELECT_PROYECTO_BY_NAME	= "SELECT PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, PE.IDSEGUIMIENTOPE, PE.APLICA, PE.FOLIO, PE.ENTRADA, PE.SALIDA, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_SEGUIMIENTO_PE PE ON PYT.IDPROYECTO = PE.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.NOMBREPROYECTO = ? AND PYT.ESTATUS IN ('PORTAFOLIO','REGISTRADO') ORDER BY PYT.ESTATUS DESC";
	private static final String UPDATE_PROYECTO_BY_ID	= "UPDATE TT_PROYECTO SET ESTATUS = ?, PORCAVANCEACTUAL = ? WHERE IDPROYECTO = ?";
	
	@Override
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
								""),
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
						null,
						new PortafolioEmpresarialDTO(
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						null,
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
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listProyectos;
	}

	@Override
	public List<ProyectoDTO> CargaProyectos(String proyectoName){
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_PROYECTO_BY_NAME);
			stmt.setString(1, proyectoName);
			
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
								""),
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
						null,
						new PortafolioEmpresarialDTO(
								rs.getInt	("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"),
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")),
						null,
						null,
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
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listProyectos;		
	}
	
	@Override
	public List<String> CargaNameProyectos(){
		List<String> listNomProyectos = new ArrayList<String>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_NAME_PROYECTOS);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listNomProyectos.add(rs.getString("NOMBREPROYECTO"));
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listNomProyectos;
	}

	@Override
	public int LiberaProyecto(ProyectoDTO proyecto) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int result = 0;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(UPDATE_PROYECTO_BY_ID);
			stmt.setString	(1, Estatus.LIBERADO.toString());
			stmt.setInt		(2, proyecto.getPorcAvanceActual());
			stmt.setLong	(3, proyecto.getIdProyecto());
			
			stmt.executeUpdate();
			
			result = 1;
			
		} catch (SQLException e) {
			result = 0;
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				result = 0;
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return result;
	}
	
}
