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
import com.telcel.sapi.dao.CatalogosDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.PorcAvanceDTO;
import com.telcel.sapi.dto.ResponsableDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class CatalogosDAOImpl implements CatalogosDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2689392433982110241L;
	static final Logger LOG = Logger.getLogger(CatalogosDAOImpl.class);
	
	private static final String CATALOGO_DIRECCION_ACTIVOS	= "SELECT IDDIRECCION, DIRECCION, ESTATUS FROM CAT_DIRECCION WHERE ESTATUS = 'ACTIVO'";
	private static final String CATALOGO_GERENCIA 			= "SELECT IDGERENCIA, GERENCIA, IDDIRECCION, ESTATUS FROM CAT_GERENCIA WHERE IDDIRECCION = ? AND ESTATUS = 'ACTIVO'";
	private static final String CATALOGO_TIPO_AMBIENTE_ACTIVO= "SELECT IDTIPOAMBIENTE, TIPOAMBIENTE, ESTATUS FROM CAT_TIPO_AMBIENTE WHERE ESTATUS = 'ACTIVO'";
	
	private static final String SELECT_ESTRATEGIAS_ACTIVAS  = "SELECT IDESTRATEGIA, ESTRATEGIA FROM CAT_ESTRATEGIA WHERE ESTATUS = 'ACTIVO' ORDER BY ESTRATEGIA";
	private static final String SELECT_ALL_ESTRATEGIAS  	= "SELECT IDESTRATEGIA, ESTRATEGIA, ESTATUS FROM CAT_ESTRATEGIA ORDER BY ESTRATEGIA";
	private static final String CARGA_RESPONSABLES_ACTIVOS	= "SELECT IDRESPONSABLE, NOMENCLATURA, AREA, RESPONSABLE FROM CAT_RESPONSABLE WHERE ESTATUS = 'ACTIVO'";
	private static final String CARGA_ALL_RESPONSABLES		= "SELECT IDRESPONSABLE, NOMENCLATURA, AREA, RESPONSABLE, ESTATUS FROM CAT_RESPONSABLE";
	
	private static final String INSERT_ESTRATEGIA			= "INSERT INTO CAT_ESTRATEGIA (ESTRATEGIA, ESTATUS) VALUES (?, ?)";
	private static final String UPDATE_ESTRATEGIA_BY_ID		= "UPDATE CAT_ESTRATEGIA SET ESTRATEGIA = ? WHERE IDESTRATEGIA = ?";
	private static final String ON_OFF_ESTRATEGIA_BY_ID 	= "UPDATE CAT_ESTRATEGIA SET ESTATUS = ? WHERE IDESTRATEGIA = ?";
	private static final String DELETE_ESTRATEGIA_BY_ID		= "DELETE FROM CAT_ESTRATEGIA WHERE IDESTRATEGIA = ?";
	
	private static final String INSERT_RESPONSABLE			= "INSERT INTO CAT_RESPONSABLE (NOMENCLATURA, AREA, RESPONSABLE) VALUES ( ?, ?, ?)";
	private static final String UPDATE_RESPONSABLE_BY_ID	= "UPDATE CAT_RESPONSABLE SET NOMENCLATURA = ? , AREA = ? , RESPONSABLE = ? WHERE IDRESPONSABLE = ?";
	private static final String ON_OFF_RESPONSABLE_BY_ID 	= "UPDATE CAT_RESPONSABLE SET ESTATUS = ? WHERE IDRESPONSABLE = ?";
	private static final String DELETE_RESPONSABLE_BY_ID	= "DELETE FROM CAT_RESPONSABLE WHERE IDRESPONSABLE = ?";
	
	private static final String USUARIOS_CON_ASIGNACION		= "SELECT USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO FROM TT_USUARIO USR INNER JOIN TT_ASIGNACION ASG ON USR.IDUSUARIO = ASG.IDUSUARIO GROUP BY USR.IDUSUARIO";

	private static final String CATALOGO_TIPO_AMBIENTE		= "SELECT IDTIPOAMBIENTE, TIPOAMBIENTE, ESTATUS FROM CAT_TIPO_AMBIENTE";
	private static final String ON_OFF_TIPO_AMBIENTE		= "UPDATE CAT_TIPO_AMBIENTE SET ESTATUS = ? WHERE IDTIPOAMBIENTE = ? ";
	private static final String NUEVO_TIPO_AMBIENTE			= "INSERT INTO CAT_TIPO_AMBIENTE (TIPOAMBIENTE, ESTATUS) VALUES ( ? , ?)";
	private static final String UPDATE_TIPO_AMBIENTE		= "UPDATE CAT_TIPO_AMBIENTE SET TIPOAMBIENTE = ? WHERE IDTIPOAMBIENTE = ?";
	private static final String DELETE_TIPO_AMBIENTE_BY_ID	= "DELETE FROM CAT_TIPO_AMBIENTE WHERE IDTIPOAMBIENTE = ?";
	
	private static final String CATALOGO_DIRECCION			= "SELECT IDDIRECCION, DIRECCION, ESTATUS FROM CAT_DIRECCION";
	private static final String INSERT_DIRECCION			= "INSERT INTO CAT_DIRECCION (DIRECCION, ESTATUS) VALUES (?, ?)";
	private static final String UPDATE_DIRECCION			= "UPDATE CAT_DIRECCION SET DIRECCION = ? WHERE IDDIRECCION = ?";
	private static final String ON_OFF_DIRECCION			= "UPDATE CAT_DIRECCION SET ESTATUS = ? WHERE IDDIRECCION = ? ";
	
	private static final String SELECT_CATALOGO_GERENCIA_ACT= "SELECT GER.IDGERENCIA, GER.GERENCIA, DIR.IDDIRECCION, GER.ESTATUS, DIR.DIRECCION FROM CAT_GERENCIA GER INNER JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE GER.ESTATUS = 'ACTIVO'";
	private static final String SELECT_CATALOGO_GERENCIA	= "SELECT GER.IDGERENCIA, GER.GERENCIA, DIR.IDDIRECCION, GER.ESTATUS, DIR.DIRECCION FROM CAT_GERENCIA GER INNER JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION";
	private static final String INSERT_GERENCIA				= "INSERT INTO CAT_GERENCIA (GERENCIA, IDDIRECCION, ESTATUS) VALUES ( ?, ?, ?)";
	private static final String UPDATE_GERENCIA				= "UPDATE CAT_GERENCIA SET GERENCIA = ?, IDDIRECCION = ? WHERE IDGERENCIA = ?";
	private static final String ON_OFF_GERENCIA				= "UPDATE CAT_GERENCIA SET ESTATUS = ? WHERE IDGERENCIA = ? ";
	
	private static final String SELECT_PORCENTAJES			= "SELECT PORPORTEMPR, PORF60, PORINFRA, PORAPLICATIVO, PORPREATP, PORATP, PORRTO FROM CAT_PORCENTAJE_AVANCE";
	private static final String UPDATE_PORCENTAJES			= "UPDATE CAT_PORCENTAJE_AVANCE SET PORPORTEMPR = ?, PORF60 = ?, PORINFRA = ?, PORAPLICATIVO = ?, PORPREATP = ?, PORATP = ?, PORRTO = ?";	
	
	private static final String SELECT_JEFES_F60			= "SELECT IDJEFEF60, NOMBRE, ESTATUS FROM CAT_JEFE_F60";
	private static final String SELECT_TIPOS_F60			= "SELECT IDTIPOF60, TIPOF60, ESTATUS FROM CAT_TIPO_F60";
	
	@Override
	public List<DireccionDTO> cargaCatDireccion() {
		List<DireccionDTO> listDireccion = new ArrayList<DireccionDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(CATALOGO_DIRECCION_ACTIVOS);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listDireccion.add(new DireccionDTO(
						rs.getLong("IDDIRECCION"), 
						rs.getString("DIRECCION"),
						rs.getString("ESTATUS"))
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listDireccion;
	}

	@Override
	public List<GerenciaDTO> cargaCatGerencia(Long idDireccion) {
		List<GerenciaDTO> listGerencia = new ArrayList<GerenciaDTO>(); 
		 
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(CATALOGO_GERENCIA);
			stmt.setLong(1, idDireccion);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listGerencia.add(new GerenciaDTO(
						rs.getLong("IDGERENCIA"), 
						rs.getString("GERENCIA"), 
						new DireccionDTO(
								rs.getLong("IDDIRECCION"), 
								"",
								""),
						rs.getString("ESTATUS")
						)						
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listGerencia;
	}

	@Override
	public List<GerenciaDTO> cargaDataGerencia() {
		List<GerenciaDTO> listGerencia = new ArrayList<GerenciaDTO>(); 
		 
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_CATALOGO_GERENCIA_ACT);			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listGerencia.add(new GerenciaDTO(
						rs.getLong("IDGERENCIA"), 
						rs.getString("GERENCIA"), 
						new DireccionDTO(
								rs.getLong("IDDIRECCION"), 
								rs.getString("DIRECCION"),
								""),
						rs.getString("ESTATUS")
						)						
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listGerencia;
	}
	
	@Override
	public List<EstrategiaDTO> FindEstrategiasActivas() {
		List<EstrategiaDTO> listEstrategias = new ArrayList<EstrategiaDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(SELECT_ESTRATEGIAS_ACTIVAS);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listEstrategias.add(
						new EstrategiaDTO(
							rs.getInt("IDESTRATEGIA"), 
							rs.getString("ESTRATEGIA"),
							"")
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listEstrategias;
	}

	@Override
	public List<ResponsableDTO> CargaAllResponsables() {
		List<ResponsableDTO> listResponsables = new ArrayList<ResponsableDTO>();

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(CARGA_ALL_RESPONSABLES);			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listResponsables.add(
						new ResponsableDTO(
								rs.getInt	("IDRESPONSABLE"), 
								rs.getString("NOMENCLATURA"),
								rs.getString("AREA"),
								rs.getString("RESPONSABLE"), 
								rs.getString("ESTATUS")
							)
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
				
		return listResponsables;
	}

	@Override
	public List<ResponsableDTO> CargaResponsablesActivos() {
		List<ResponsableDTO> listResponsables = new ArrayList<ResponsableDTO>();

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(CARGA_RESPONSABLES_ACTIVOS);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listResponsables.add(
						new ResponsableDTO(
								rs.getInt	("IDRESPONSABLE"), 
								rs.getString("NOMENCLATURA"),
								rs.getString("AREA"),
								rs.getString("RESPONSABLE"),
								""
							)
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
				
		return listResponsables;
	}

	@Override
	public int GuardaNewEstrategia(String estrategia) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int				registro	= 0;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(INSERT_ESTRATEGIA);

			stmt.setString(1, estrategia.trim());
			stmt.setString(2, Estatus.ACTIVO.toString());
			
			registro = stmt.executeUpdate();
			registro	= 1;
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar o ejecutar sentecia en la BD " + e);
			return 0;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + "Error al cerrar conexiones" + e);
				registro	= 2;
			}
		}
		return registro;
	}

	@Override
	public int ActivaInactivaEstrategia(EstrategiaDTO estrategiaUpdate, boolean accion) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L228: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ON_OFF_ESTRATEGIA_BY_ID);
			
			stmt.setString	(1, (accion) ? Estatus.ACTIVO.toString() : Estatus.INACTIVO.toString());
			stmt.setInt		(2, estrategiaUpdate.getIdEstrategia());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + "Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int UpdateEstrategia(EstrategiaDTO estrategiaUpdate) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L214: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_ESTRATEGIA_BY_ID);
			
			stmt.setString	(1, estrategiaUpdate.getEstrategia());
			stmt.setInt		(2, estrategiaUpdate.getIdEstrategia());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + "Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int DeleteEstrategia(EstrategiaDTO estrategiaUpdate) {
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
			stmt = conn.prepareStatement(DELETE_ESTRATEGIA_BY_ID);
			
			stmt.setInt(1, estrategiaUpdate.getIdEstrategia());		
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
	public int GuardaResponsable(ResponsableDTO responsable) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int				registro	= 0;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(INSERT_RESPONSABLE);

			stmt.setString(1, responsable.getNomenclatura().trim());
			stmt.setString(2, responsable.getArea().trim());
			stmt.setString(3, responsable.getResponsable().trim());
			
			registro = stmt.executeUpdate();
			registro	= 1;
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar o ejecutar sentecia en la BD " + e);
			return 0;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + "Error al cerrar conexiones" + e);
				registro	= 2;
			}
		}
		return registro;
	}

	@Override
	public int UpdateResponsable(ResponsableDTO responsableUpdate) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L214: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_RESPONSABLE_BY_ID);

			stmt.setString	(1, responsableUpdate.getNomenclatura().trim());
			stmt.setString	(2, responsableUpdate.getArea().trim());
			stmt.setString	(3, responsableUpdate.getResponsable().trim());
			stmt.setInt		(4, responsableUpdate.getIdResponable());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
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
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int DeleteResponsable(ResponsableDTO responsableDelete) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L388: " + autoCommit);
			}
			stmt = conn.prepareStatement(DELETE_RESPONSABLE_BY_ID);
			
			stmt.setInt(1, responsableDelete.getIdResponable());		
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
	public int OnOffResponsable(ResponsableDTO responsableOnOff, boolean accion) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L486: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ON_OFF_RESPONSABLE_BY_ID);
			
			stmt.setString	(1, (accion) ? Estatus.ACTIVO.toString() : Estatus.INACTIVO.toString());
			stmt.setInt		(2, responsableOnOff.getIdResponable());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + "Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public List<EstrategiaDTO> FindEstrategiasInAc() {
		List<EstrategiaDTO> listEstrategias = new ArrayList<EstrategiaDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(SELECT_ALL_ESTRATEGIAS);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listEstrategias.add(
						new EstrategiaDTO(
							rs.getInt("IDESTRATEGIA"), 
							rs.getString("ESTRATEGIA"),
							rs.getString("ESTATUS")
							)
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listEstrategias;
	}

	@Override
	public List<UsuarioDTO> ConsultaUsuariosResponsables() {
		List<UsuarioDTO>	listUsuariosAsignados = new ArrayList<UsuarioDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();	
			stmt = conn.prepareStatement(USUARIOS_CON_ASIGNACION);		
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listUsuariosAsignados.add(
						new UsuarioDTO(
								rs.getLong("IDUSUARIO"), 
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
								null)
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos o falló la ejecusión de la sentencia - " + e);
		}	
		
		return listUsuariosAsignados;
	}

	@Override
	public List<TipoAmbienteDTO> CatListTiposAmbientes() {
		List<TipoAmbienteDTO> listDireccion = new ArrayList<TipoAmbienteDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(CATALOGO_TIPO_AMBIENTE);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listDireccion.add(new TipoAmbienteDTO(
						rs.getInt("IDTIPOAMBIENTE"), 
						rs.getString("TIPOAMBIENTE"), 
						rs.getString("ESTATUS"))
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listDireccion;
	}
	
	@Override
	public List<TipoAmbienteDTO> CatListTiposAmbientesActivos() {
		List<TipoAmbienteDTO> listDireccion = new ArrayList<TipoAmbienteDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(CATALOGO_TIPO_AMBIENTE_ACTIVO);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listDireccion.add(new TipoAmbienteDTO(
						rs.getInt("IDTIPOAMBIENTE"), 
						rs.getString("TIPOAMBIENTE"), 
						rs.getString("ESTATUS"))
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listDireccion;
	}

	@Override
	public int ActivaInactivaTipoAmbiente(TipoAmbienteDTO tipoAmbiente, boolean onOff){
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L689: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ON_OFF_TIPO_AMBIENTE);
			
			stmt.setString	(1, (onOff) ? Estatus.ACTIVO.toString() : Estatus.INACTIVO.toString());
			stmt.setInt		(2, tipoAmbiente.getIdTipoAmbiente());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + "Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int GuardaNewTipoAmbiente(String nuevoTipoAmbiente) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int				registro	= 0;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(NUEVO_TIPO_AMBIENTE);

			stmt.setString(1, nuevoTipoAmbiente);
			stmt.setString(2, Estatus.ACTIVO.toString());
			
			registro = stmt.executeUpdate();
			registro	= 1;
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar o ejecutar sentecia en la BD " + e);
			return 0;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + "Error al cerrar conexiones" + e);
				registro	= 2;
			}
		}
		return registro;
	}

	@Override
	public int UpdateNewTipoAmbiente(TipoAmbienteDTO tipoAmbiente) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L770: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_TIPO_AMBIENTE);

			stmt.setString	(1, tipoAmbiente.getTipoAmbiente().trim());
			stmt.setInt		(2, tipoAmbiente.getIdTipoAmbiente());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
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
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int DeleteTipoAmbiente(TipoAmbienteDTO tipoAmbiente) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L820: " + autoCommit);
			}
			stmt = conn.prepareStatement(DELETE_TIPO_AMBIENTE_BY_ID);
			
			stmt.setInt(1, tipoAmbiente.getIdTipoAmbiente());		
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
	public List<DireccionDTO> CatalogoDirecciones() {
		List<DireccionDTO> listDireccion = new ArrayList<DireccionDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(CATALOGO_DIRECCION);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listDireccion.add(new DireccionDTO(
						rs.getLong("IDDIRECCION"), 
						rs.getString("DIRECCION"),
						rs.getString("ESTATUS"))
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listDireccion;
	}

	@Override
	public List<GerenciaDTO> CatalogoGerencia() {
		List<GerenciaDTO> listGerencia = new ArrayList<GerenciaDTO>(); 
		 
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_CATALOGO_GERENCIA);			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listGerencia.add(new GerenciaDTO(
						rs.getLong("IDGERENCIA"), 
						rs.getString("GERENCIA"), 
						new DireccionDTO(
								rs.getLong("IDDIRECCION"), 
								rs.getString("DIRECCION"),
								""),
						rs.getString("ESTATUS")
						)						
					);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listGerencia;
	}

	@Override
	public int GuardaNuevaDireccion(String nuevaDireccion) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int					registro= 0;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(INSERT_DIRECCION);

			stmt.setString(1, nuevaDireccion.trim());
			stmt.setString(2, Estatus.ACTIVO.toString());
			
			registro = stmt.executeUpdate();
			registro	= 1;
		} catch (SQLException e) {
			LOG.error(this.getClass() + " Error al conectar o ejecutar sentecia en la BD " + e);
			return 0;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar conexiones" + e);
				registro	= 2;
			}
		}
		return registro;
	}

	@Override
	public int UpdateDireccionBySelected(DireccionDTO direccionSelected) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L214: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_DIRECCION);

			stmt.setString	(1, direccionSelected.getDireccion().trim());
			stmt.setLong	(2, direccionSelected.getIdDireccion());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
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
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int ActivaInactivaDireccion(DireccionDTO direccionSelected, boolean onOff) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L228: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ON_OFF_DIRECCION);
			
			stmt.setString	(1, (onOff) ? Estatus.ACTIVO.toString() : Estatus.INACTIVO.toString());
			stmt.setLong	(2, direccionSelected.getIdDireccion());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + "Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int GuardaNuevaGerencia(String nvaGerencia, Long idDireccion) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		int					registro= 0;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(INSERT_GERENCIA);

			stmt.setString	(1, nvaGerencia.trim());
			stmt.setLong	(2, idDireccion);
			stmt.setString	(3, Estatus.ACTIVO.toString());
			
			registro = stmt.executeUpdate();
			registro	= 1;
		} catch (SQLException e) {
			LOG.error(this.getClass() + " Error al conectar o ejecutar sentecia en la BD " + e);
			return 0;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar conexiones" + e);
				registro	= 2;
			}
		}
		return registro;
	}

	@Override
	public int UpdateGerenciaBySelected(GerenciaDTO gerencia) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L214: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_GERENCIA);

			stmt.setString	(1, gerencia.getGerencia().trim());
			stmt.setLong	(2, gerencia.getDireccion().getIdDireccion());
			stmt.setLong	(3, gerencia.getIdGerencia());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
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
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public int ActivaInactivaGerencia(GerenciaDTO gerencia, boolean onOff) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L228: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(ON_OFF_GERENCIA);
			
			stmt.setString	(1, (onOff) ? Estatus.ACTIVO.toString() : Estatus.INACTIVO.toString());
			stmt.setLong	(2, gerencia.getIdGerencia());
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
			result = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error(this.getClass() + "Error al realizar el Rollback " + rollback);
			}
			return result;
		}finally {
			try {
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public PorcAvanceDTO CargaPorcentajesAvance() {
		PorcAvanceDTO porcentajes = new PorcAvanceDTO();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(SELECT_PORCENTAJES);
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				porcentajes = new PorcAvanceDTO(
						0, 
						rs.getInt("PORPORTEMPR"), 
						rs.getInt("PORF60"), 
						rs.getInt("PORINFRA"), 
						rs.getInt("PORAPLICATIVO"), 
						rs.getInt("PORPREATP"), 
						rs.getInt("PORATP"), 
						rs.getInt("PORRTO")
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos, u ocurrio un error al ejecutar el SQL - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return porcentajes;
	}

	@Override
	public int ActualizaPorcentajes(PorcAvanceDTO porcentajes) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, CatalogosDAOImpl, L1312: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_PORCENTAJES);

			stmt.setInt	(1, porcentajes.getPorportempr());
			stmt.setInt	(2, porcentajes.getPorcF60());
			stmt.setInt	(3, porcentajes.getPorcInfra());
			stmt.setInt	(4, porcentajes.getPorcAplicativo());
			stmt.setInt	(5, porcentajes.getPorcPreATP());
			stmt.setInt	(6, porcentajes.getPorcATP());
			stmt.setInt	(7, porcentajes.getPorcRTO());			
			
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
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
				return result = 2;
			}
		}
		
		return result;
	}

	@Override
	public List<JefeF60DTO> catalogoJefesF60() {
		List<JefeF60DTO> listJefes = new ArrayList<JefeF60DTO>(); 
		 
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_JEFES_F60);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listJefes.add(
						new JefeF60DTO(
							rs.getInt("IDJEFEF60"), 
							rs.getString("NOMBRE"),						
							rs.getString("ESTATUS")
						)						
				);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listJefes;
	}

	@Override
	public List<TipoF60DTO> catalogoTiposF60() {
		List<TipoF60DTO> listTiposF60 = new ArrayList<TipoF60DTO>(); 
		 
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_TIPOS_F60);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listTiposF60.add(
						new TipoF60DTO(
							rs.getInt("IDTIPOF60"), 
							rs.getString("TIPOF60"),						
							rs.getString("ESTATUS")
						)						
				);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		
		return listTiposF60;
	}
}
