package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.dao.ProyectoDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.FoliosRegistradosDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.PrefijosService;
import com.telcel.sapi.service.impl.PrefijosServiceImpl;

public class ProyectoDAOImpl implements ProyectoDAO, Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -406984163148484971L;

	static final Logger LOG = Logger.getLogger(ProyectoDAOImpl.class);
	
	private static final String INSERT_REGISTRA_PROYECTO 		= "INSERT INTO TT_PROYECTO (IDENTIFICADORINTERNO, INTEGRADOR, NOMBREPROYECTO, SOLICITANTE, IDGERENCIA, DESCRIPCION, COMENTARIOSGRLS, ESTATUS, FECHAREGISTRO, HORAREGISTRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, curdate(), now())";
	private static final String INSERT_PORTAFOLIO_EMPRESARIAL 	= "INSERT INTO TT_SEGUIMIENTO_PE (APLICA, FOLIO, ENTRADA, SALIDA, IDPROYECTO) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_ESTATUS_PROYECTO		 	= "UPDATE TT_PROYECTO SET FASEACTUAL = ?, ESTATUS = ? WHERE IDPROYECTO = ? ";
	private static final String UPDATE_IDENTIFICADOR_INTERNO_PROYECTO	= "UPDATE TT_PROYECTO SET IDENTIFICADORINTERNO = ? WHERE IDPROYECTO = ? ";
	
	private static final String UPDATE_PROTAFOLIO_EMPRESARIAL	= "UPDATE TT_SEGUIMIENTO_PE SET APLICA = ?, FOLIO = ?, ENTRADA = ?, SALIDA = ? WHERE IDSEGUIMIENTOPE = ? ";
	private static final String UPDATE_PROYECTO					= "UPDATE TT_PROYECTO SET INTEGRADOR = ?, NOMBREPROYECTO = ?, SOLICITANTE = ?, DIRECCION = ?, GERENCIA = ?, IDGERENCIA = ?, DESCRIPCION = ?, COMENTARIOSGRLS = ? WHERE IDPROYECTO = ? ";
	
	private static final String SELECT_FOLIOS					= "SELECT FOLIO FROM TT_SEGUIMIENTO_PE WHERE FOLIO = ?";
	private static final String SELECT_PROYETOS_BY_FOLIO		= "SELECT PYT.NOMBREPROYECTO, PYT.INTEGRADOR, PYT.ESTATUS, SPE.FOLIO FROM TT_PROYECTO PYT INNER JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO WHERE SPE.FOLIO = ?";

	private static final String ACTIVAR_CANCELAR_PROYECTO		= "UPDATE TT_PROYECTO SET COMENTARIOSGRLS = ?, ESTATUS = ? WHERE IDPROYECTO = ? ";
	private static final String ELIMINAR_PROYECTO				= "DELETE FROM TT_PROYECTO WHERE IDPROYECTO =  ? ";
	
	PrefijosService prefijos;
	
	@SuppressWarnings("resource")
	@Override
	public int RegistroProyecto(ProyectoDTO proyecto, boolean portafolioEmp) {
		prefijos  = new PrefijosServiceImpl();
		int idProyecto = 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L44: " + autoCommit);
			}
			stmt = conn.prepareStatement(INSERT_REGISTRA_PROYECTO, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString	(1, proyecto.getIdentificadorInterno());
			stmt.setString	(2, proyecto.getIntegrador());
			stmt.setString	(3, proyecto.getNombreProyecto());	
			stmt.setString	(4, proyecto.getSolicitante());	
			stmt.setLong	(5, proyecto.getGerenciaDto().getIdGerencia());	
			stmt.setString	(6, proyecto.getDescripcion());	
			stmt.setString	(7, proyecto.getComentariosGrls());
			stmt.setString	(8, Estatus.REGISTRADO.toString());
			
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				idProyecto	= rs.getInt(1);
			}
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_IDENTIFICADOR_INTERNO_PROYECTO);
			stmt.setString	(1, prefijos.idCompuesto("" + idProyecto));
			stmt.setInt		(2, idProyecto);
			stmt.executeUpdate();
			
			if(portafolioEmp) {
				stmt = null;
				stmt = conn.prepareStatement(INSERT_PORTAFOLIO_EMPRESARIAL);
				
				stmt.setString	(1, proyecto.getPortafolioEmp().getAplica());
				stmt.setString	(2, proyecto.getPortafolioEmp().getFolio());
				stmt.setString	(3, proyecto.getPortafolioEmp().getEntrada());
				stmt.setString	(4, proyecto.getPortafolioEmp().getSalida());
				stmt.setInt		(5, idProyecto);
				stmt.executeUpdate();
				
				stmt = null;
				stmt = conn.prepareStatement(UPDATE_ESTATUS_PROYECTO);
				stmt.setString	(1, "Portafolio Empresarial");
				stmt.setString	(2, Estatus.PORTAFOLIO.toString());
				stmt.setInt		(3, idProyecto);
				stmt.executeUpdate();
			}
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos" + e);
			idProyecto = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
			return idProyecto;
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return 0;
			}
		}		
		return idProyecto;
	}
	
	@SuppressWarnings("resource")
	public int AsignaPortafolioEmpresarial(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L126: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(INSERT_PORTAFOLIO_EMPRESARIAL);
			
			stmt.setString	(1, proyecto.getPortafolioEmp().getAplica());
			stmt.setString	(2, proyecto.getPortafolioEmp().getFolio());
			stmt.setString	(3, proyecto.getPortafolioEmp().getEntrada());	
			stmt.setString	(4, proyecto.getPortafolioEmp().getSalida());	
			stmt.setLong	(5, proyecto.getIdProyecto());			
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_PROYECTO);
			stmt.setString	(1, "Portafolio Empresarial");
			stmt.setString	(2, Estatus.PORTAFOLIO.toString());
			stmt.setLong	(3, proyecto.getIdProyecto());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos" + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				result = 2;
				return result;
			}
		}
		
		return result;
	}

	@Override
	public int ActualizaPortafolioEmpresarial(ProyectoDTO proyecto) {
		int result 		= 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(UPDATE_PROTAFOLIO_EMPRESARIAL);
			stmt.setString	(1, proyecto.getPortafolioEmp().getAplica());
			stmt.setString	(2, proyecto.getPortafolioEmp().getFolio());
			stmt.setString	(3, proyecto.getPortafolioEmp().getEntrada());
			stmt.setString	(4, proyecto.getPortafolioEmp().getSalida());
			stmt.setInt		(5, proyecto.getPortafolioEmp().getIdSeguimiento());
			
			stmt.executeUpdate();			
			
			result = 1;
			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos" + e);
			result = 0;
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				result = 2;
				return result;
			}
		}
		
		return result;
	}

	@Override
	public int ActualizaInfoProyecto(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + " Error al retirar el autocommit: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_PROYECTO);

			stmt.setString	(1, proyecto.getIntegrador());
			stmt.setString	(2, proyecto.getNombreProyecto());
			stmt.setString	(3, proyecto.getSolicitante());
			stmt.setString	(4, proyecto.getDireccion());
			stmt.setString	(5, proyecto.getGerencia());
			stmt.setLong	(6, proyecto.getGerenciaDto().getIdGerencia());
			stmt.setString	(7, proyecto.getDescripcion());
			stmt.setString	(8, proyecto.getComentariosGrls());
			stmt.setLong	(9, proyecto.getIdProyecto());

			stmt.executeUpdate();
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos" + e);
			result = 0;
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				result = 2;
				return result;
			}
		}
		
		return result;
	}

	@Override
	public List<String> VerificaFolio(String folio) {
		List<String> listFoliosRegistrados = new ArrayList<String>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {

			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_FOLIOS);
			stmt.setString	(1, folio);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listFoliosRegistrados.add(rs.getString("FOLIO"));
			}
			
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos, o al ejecutar el Query" + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listFoliosRegistrados;
	}

	@Override
	public int CancelaProyecto(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + " Error al retirar el autocommit: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ACTIVAR_CANCELAR_PROYECTO);

			stmt.setString	(1, proyecto.getComentariosGrls());
			stmt.setString	(2, Estatus.CANCELADO.toString());
			stmt.setLong	(3, proyecto.getIdProyecto());

			stmt.executeUpdate();
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + " Error al conectar a la base de datos" + e);
			result = 0;
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				result = 2;
				return result;
			}
		}
		
		return result;
	}

	@Override
	public int ActivaProyecto(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + " Error al retirar el autocommit: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ACTIVAR_CANCELAR_PROYECTO);

			stmt.setString	(1, proyecto.getComentariosGrls());
			
			if(!proyecto.getFaseSiguiente().equals(Estatus.TERMINADO.toString())) {
				stmt.setString	(2, Estatus.ASIGNADO.toString());
			}else {
				stmt.setString	(2, Estatus.TERMINADO.toString());
			}
			stmt.setLong	(3, proyecto.getIdProyecto());

			stmt.executeUpdate();
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + " Error al conectar a la base de datos" + e);
			result = 0;
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				result = 2;
				return result;
			}
		}
		
		return result;
	}

	@Override
	public int EliminarProyecto(ProyectoDTO proyecto) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;

		int					result	= 0;
		
		try {
			conn = Conexion.getConnection();			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L263: " + autoCommit);
			}
			stmt = conn.prepareStatement(ELIMINAR_PROYECTO);
			
			stmt.setLong(1, proyecto.getIdProyecto());		
			stmt.executeUpdate();
			
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			return 0;
		}		
		finally {
			try {			
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return 2;
			}
		}
		return result;
	}

	@Override
	public List<FoliosRegistradosDTO> foliosRegistrados(String folio) {
		List<FoliosRegistradosDTO> listFoliosRegistrados = new ArrayList<FoliosRegistradosDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {

			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_PROYETOS_BY_FOLIO);
			stmt.setString	(1, folio);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listFoliosRegistrados.add(
							new FoliosRegistradosDTO(rs.getString("NOMBREPROYECTO"), rs.getString("INTEGRADOR"), rs.getString("FOLIO"), rs.getString("ESTATUS"))							
						);
			}
			
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos, o al ejecutar el Query" + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listFoliosRegistrados;
	}
	
}
