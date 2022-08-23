package com.telcel.sapi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.dao.IncidentesDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.IncidenteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ResponsableDTO;

public class IncidentesDAOImpl implements IncidentesDAO {
	
	static final Logger LOG = Logger.getLogger(IncidentesDAOImpl.class);
	
//	private static final String CARGA_RESPONSABLES			= "SELECT IDRESPONSABLE, NOMENCLATURA, AREA, RESPONSABLE FROM CAT_RESPONSABLE";
	private static final String INSERT_INCIDENTE 			= "INSERT INTO TT_INCIDENTE (ISSUEINCIDENTE, IDRESPONSABLE, FECHAINICIO, DETALLES, ETAPA, IDPROYECTO, ESTATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_INCIDENTES			= "SELECT INC.IDINCIDENTE, INC.ISSUEINCIDENTE, RESP.IDRESPONSABLE, INC.FECHAINICIO, INC.FECHASOLUCION, INC.DETALLES, INC.ETAPA, INC.ESTATUS, RESP.NOMENCLATURA, RESP.AREA, RESP.RESPONSABLE FROM TT_INCIDENTE INC INNER JOIN CAT_RESPONSABLE RESP ON INC.IDRESPONSABLE = RESP.IDRESPONSABLE WHERE INC.IDPROYECTO = ? ORDER BY INC.IDINCIDENTE DESC";
	private static final String UPDATE_INCIDENTE			= "UPDATE TT_INCIDENTE SET FECHAINICIO = ? ,ISSUEINCIDENTE = ?, IDRESPONSABLE = ?, FECHASOLUCION = ?, DETALLES = ?  WHERE IDINCIDENTE = ?";
	private static final String UPDATE_CULMINA_INCIDENTE	= "UPDATE TT_INCIDENTE SET ESTATUS = ?  WHERE IDINCIDENTE = ?";
	
	
	@Override
	public List<IncidenteDTO> CargaIncidentes(Long idProyecto) {
		List<IncidenteDTO> listIncidentes = new ArrayList<IncidenteDTO>();

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(SELECT_INCIDENTES);
			stmt.setLong	(1, idProyecto);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listIncidentes.add(
						new IncidenteDTO(
								rs.getInt("IDINCIDENTE"), 
								rs.getString("ISSUEINCIDENTE"),
								new ResponsableDTO(
										rs.getInt	("IDRESPONSABLE"), 
										rs.getString("NOMENCLATURA"), 
										rs.getString("AREA"), 
										rs.getString("RESPONSABLE"),
										""
										),
								rs.getString("FECHAINICIO"),
								rs.getString("FECHASOLUCION"),
								rs.getString("DETALLES"),
								rs.getString("ETAPA"),
								rs.getString("ESTATUS")
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
				
		return listIncidentes;
	}

	@Override
	public int RegistroIncidente(IncidenteDTO incidente, ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, IncidentesDAOImpl, L44: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(INSERT_INCIDENTE);
			
			stmt.setString	(1, incidente.getIssueIncidente());
			stmt.setInt		(2, incidente.getResponsable().getIdResponable());
			stmt.setString	(3, incidente.getFechaInicio());
			stmt.setString	(4, incidente.getDetalles());	
			stmt.setString	(5,	proyecto.getFaseActual());	
			stmt.setLong	(6, proyecto.getIdProyecto());
			stmt.setString	(7,	Estatus.ACTIVO.toString());	

			stmt.executeUpdate();
			
			registro = 1;
			conn.commit();			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			registro = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
			return registro;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				registro = 2;
				return registro;
			}
		}
		
		return registro;
	}

	@Override
	public int CulminaIncidente(IncidenteDTO incidente) {
		int result = 0;		

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(UPDATE_CULMINA_INCIDENTE);

			stmt.setString	(1, Estatus.CERRADO.toString());
			stmt.setInt		(2, incidente.getIdIncidente());
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

	@Override
	public int ActualizaIncidente(IncidenteDTO incidente) {
		int result = 0;		

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(UPDATE_INCIDENTE);

			stmt.setString	(1, incidente.getFechaInicio());
			stmt.setString	(2, incidente.getIssueIncidente());
			stmt.setInt		(3, incidente.getResponsable().getIdResponable());
			stmt.setString	(4, incidente.getFechaResolucion());
			stmt.setString	(5, incidente.getDetalles());
			stmt.setInt		(6, incidente.getIdIncidente());

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
