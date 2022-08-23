package com.telcel.sapi.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.telcel.sapi.dao.ProyectoInfrestructuraDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ProyectoFolioDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.service.Conversiones;
import com.telcel.sapi.service.impl.ConversionesImpl;


public class ProyectoInfrestructuraDAOImpl implements ProyectoInfrestructuraDAO,Serializable {

	/**
	 * Desarrollador Por Ing. Christian Brandon Neri Sanchez 14-02-2022
	 */
	private static final long serialVersionUID = -2719910364189296783L;
	static final Logger LOG = Logger.getLogger(ProyectoInfrestructuraDAOImpl.class);
	
	private static final String INSERT_INFRAESTRUCTURA 	= "INSERT INTO TT_AMBIENTE (IDTIPOAMBIENTE, CPU, MEMORIA, DISCODURO, UNIDADMEDIDADD, SISTEMAOPERATIVO, BASEDATOS, HOSTNAME, IP, ENTREGAEQUIPO, IDPROYECTO, COMENTARIO) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_INFRAESTRUCTURA	= "UPDATE TT_AMBIENTE SET IDTIPOAMBIENTE = ?, CPU = ?, MEMORIA = ?, DISCODURO = ?, UNIDADMEDIDADD = ?, SISTEMAOPERATIVO = ?, BASEDATOS = ?, HOSTNAME = ?, IP = ?, ENTREGAEQUIPO = ?, COMENTARIO = ? WHERE IDAMBIENTE = ?";
	private static final String SELECT_AMBIENTES 		= "SELECT AMB.IDAMBIENTE, TAM.IDTIPOAMBIENTE, TAM.TIPOAMBIENTE, AMB.AMBIENTE, AMB.CPU, AMB.MEMORIA, AMB.DISCODURO, AMB.UNIDADMEDIDADD, AMB.SISTEMAOPERATIVO, AMB.BASEDATOS, AMB.HOSTNAME, AMB.IP, AMB.ENTREGAEQUIPO, AMB.COMENTARIO FROM TT_AMBIENTE AMB INNER JOIN CAT_TIPO_AMBIENTE TAM ON AMB.IDTIPOAMBIENTE = TAM.IDTIPOAMBIENTE WHERE IDPROYECTO = ?";
	private static final String BUSCA_HOSTNAME 			= "SELECT HOSTNAME FROM TT_AMBIENTE WHERE HOSTNAME = ?";
	private static final String BUSCA_IP	 			= "SELECT IP FROM TT_AMBIENTE WHERE IP = ?";
	
	private static final String BUSCA_FOLIO_HOSTAME		= "SELECT DISTINCT PYT.NOMBREPROYECTO, PEM.FOLIO FROM TT_AMBIENTE AMB INNER JOIN TT_SEGUIMIENTO_PE PEM ON PEM.IDPROYECTO = AMB.IDPROYECTO INNER JOIN TT_PROYECTO PYT ON PYT.IDPROYECTO = PEM.IDPROYECTO WHERE AMB.HOSTNAME = ? ";
	private static final String BUSCA_FOLIO_IP			= "SELECT DISTINCT PYT.NOMBREPROYECTO, PEM.FOLIO FROM TT_AMBIENTE AMB INNER JOIN TT_SEGUIMIENTO_PE PEM ON PEM.IDPROYECTO = AMB.IDPROYECTO INNER JOIN TT_PROYECTO PYT ON PYT.IDPROYECTO = PEM.IDPROYECTO WHERE AMB.IP = ? ";
	
	private static final String BUSCA_HOSTNAME_ID		= "SELECT IDAMBIENTE, HOSTNAME FROM TT_AMBIENTE WHERE HOSTNAME = ?";
	private static final String BUSCA_IP_ID	 			= "SELECT IDAMBIENTE, IP FROM TT_AMBIENTE WHERE IP = ?";
	
	private static final String DELETE_AMBIENTE_BY_ID	= "DELETE FROM TT_AMBIENTE WHERE IDAMBIENTE = ?";

	private static final String COUNT_HOSTAME			= "SELECT COUNT(IDAMBIENTE) AS NUMAMBIENTES FROM TT_AMBIENTE WHERE HOSTNAME LIKE ? ";
	private static final String COUNT_IP				= "SELECT COUNT(IDAMBIENTE) AS NUMAMBIENTES FROM TT_AMBIENTE WHERE IP LIKE ? ";
	
	Conversiones conversiones;
	@Override
	public int RegistroInfrestructura(ProyectoDTO proyecto, AmbienteDTO ambiente) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoInfrestructuraDAOImpl, L40: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(INSERT_INFRAESTRUCTURA);
			
			stmt.setInt		(1, ambiente.getTipoAmbiente().getIdTipoAmbiente());
			stmt.setInt		(2, ambiente.getCpu());
			stmt.setInt		(3, ambiente.getMemoria());
			stmt.setInt		(4, ambiente.getDiscoDuro());
			stmt.setString	(5, ambiente.getUnidadMedidaDD());
			stmt.setString	(6, ambiente.getSistemaOperativo());
			stmt.setString	(7, ambiente.getBaseDatos());
			stmt.setString	(8, ambiente.getHostName());
			stmt.setString	(9, ambiente.getIp());
			stmt.setString	(10, ambiente.getEntregaEquipo());
			stmt.setLong	(11, proyecto.getIdProyecto());
			stmt.setString	(12, ambiente.getComentario());
			
			stmt.executeUpdate();
			
			conn.commit();
			registro = 1;
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
			registro = 0;
			try {
				conn.rollback();
			} catch (SQLException rollback) {
				LOG.error("Error al realizar el Rollback " + rollback);
			}
		}finally {
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		return registro;
	}

	@Override
	public int UpdateInfrestructura(AmbienteDTO ambiente) {
		int registro = 0;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(UPDATE_INFRAESTRUCTURA);
			
			stmt.setInt		(1, ambiente.getTipoAmbiente().getIdTipoAmbiente());
			stmt.setInt		(2, ambiente.getCpu());
			stmt.setInt		(3, ambiente.getMemoria());
			stmt.setInt		(4, ambiente.getDiscoDuro());
			stmt.setString	(5, ambiente.getUnidadMedidaDD());
			stmt.setString	(6, ambiente.getSistemaOperativo());
			stmt.setString	(7, ambiente.getBaseDatos());
			stmt.setString	(8, ambiente.getHostName());
			stmt.setString	(9, ambiente.getIp());
			stmt.setString	(10, ambiente.getEntregaEquipo());
			stmt.setString	(11, ambiente.getComentario());
			stmt.setInt		(12, ambiente.getIdAmbiente());
			
			stmt.executeUpdate();
			registro = 1;
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
			return registro = 0;
		}finally {
			Conexion.close(stmt);
			Conexion.close(conn);
		}
		return registro;
	}

	@Override
	public List<AmbienteDTO> CargaAmbientes(Long idProyecto) {
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		conversiones = new ConversionesImpl();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();

			stmt = conn.prepareStatement(SELECT_AMBIENTES);
			stmt.setLong(1, idProyecto);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listAmbiente.add(
						new AmbienteDTO(
								rs.getInt	("IDAMBIENTE"), 
								new TipoAmbienteDTO(
										rs.getInt("IDTIPOAMBIENTE"), 
										rs.getString("TIPOAMBIENTE")
										, ""), 
										rs.getString("AMBIENTE"), 
										rs.getInt	("CPU"),
										rs.getInt	("MEMORIA"), 
										rs.getInt	("DISCODURO"), 
										rs.getString("UNIDADMEDIDADD"), 
										rs.getString("SISTEMAOPERATIVO"), 
										rs.getString("BASEDATOS"), 
										rs.getString("HOSTNAME"),
										rs.getString("IP"),
										rs.getString("ENTREGAEQUIPO"),
										(rs.getString("ENTREGAEQUIPO") == null) ? null : conversiones.StringToDate(rs.getString("ENTREGAEQUIPO")),
										rs.getString("COMENTARIO")
								)
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listAmbiente;
	}

	@Override
	public boolean buscaHostName(String hostname) {
		boolean result = false;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(BUSCA_HOSTNAME);
			stmt.setString	(1, hostname);
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				result = true;
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return result;
	}

	@Override
	public boolean buscaDireccionIP(String ip) {
		boolean result = false;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(BUSCA_IP);
			stmt.setString	(1, ip);
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				result = true;
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return result;
	}

	@Override
	public boolean buscaHostNameUpdate(AmbienteDTO ambiente) {
		boolean result = false;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		int 	id 			= 0;
		String 	hostName 	= "";
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(BUSCA_HOSTNAME_ID);
			stmt.setString	(1, ambiente.getHostName());
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				id 			= rs.getInt("IDAMBIENTE");
				hostName	= rs.getString("HOSTNAME");
			}
			
			if(ambiente.getHostName().equals(hostName) && ambiente.getIdAmbiente() == id) {
				result = false;
			}
			
			if(ambiente.getHostName().equals(hostName) && ambiente.getIdAmbiente() != id) {
				result = true;
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}		
		return result;
	}

	@Override
	public boolean buscaDireccionIPUpdate(AmbienteDTO ambiente) {
		boolean result = false;
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		int 	id 	= 0;
		String 	ip 	= "";
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(BUSCA_IP_ID);
			stmt.setString	(1, ambiente.getIp());
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				id 	= rs.getInt("IDAMBIENTE");
				ip	= rs.getString("IP");
			}
			
			if(ambiente.getIp().equals(ip) && ambiente.getIdAmbiente() == id) {
				result = false;
			}
			
			if(ambiente.getIp().equals(ip) && ambiente.getIdAmbiente() != id) {
				result = true;
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}		
		return result;
	}

	@Override
	public int DeleteInfrestructura(AmbienteDTO ambiente) {
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
				LOG.error("Error al retirar el autocommit, VersionDAOImpl, L350: " + autoCommit);
			}
			stmt = conn.prepareStatement(DELETE_AMBIENTE_BY_ID);
			
			stmt.setInt(1, ambiente.getIdAmbiente());		
			stmt.executeUpdate();
			
			result = 1;
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		finally {
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
	public List<ProyectoFolioDTO> buscaFoliosByHostName(String hostname) {
		List<ProyectoFolioDTO> listProyectoFolio = new ArrayList<ProyectoFolioDTO>();

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(BUSCA_FOLIO_HOSTAME);
			stmt.setString	(1, hostname);
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listProyectoFolio.add(
						new ProyectoFolioDTO(
								rs.getString("FOLIO"), 
								rs.getString("NOMBREPROYECTO"))
						);
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
				
		return listProyectoFolio;
	}

	@Override
	public List<ProyectoFolioDTO> buscaFoliosByIp(String ip) {
		List<ProyectoFolioDTO> listFolProy = new ArrayList<ProyectoFolioDTO>();
		conversiones = new ConversionesImpl();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();

			stmt = conn.prepareStatement(BUSCA_FOLIO_IP);
			stmt.setString(1, ip);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listFolProy.add(
						new ProyectoFolioDTO(
								rs.getString("FOLIO"), 
								rs.getString("NOMBREPROYECTO")
								)
						);
			}
			
		} catch (SQLException e) {
			LOG.error(this.getClass() + "Error al conectar a la base de datos " + e);
		}finally {
			try {
				Conexion.close(rs);
				Conexion.close(stmt);
				Conexion.close(conn);
			}catch (Exception e) {
				LOG.error(this.getClass() + " Error al cerrar las conexiones " + e);
			}
		}
		
		return listFolProy;
	}

	@Override
	public int cuentaFoliosByHostName(String hostname) {
		int numRegistros = 0;

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(COUNT_HOSTAME);
			stmt.setString	(1, "%" + hostname + "%");
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				 numRegistros = rs.getInt("NUMAMBIENTES");
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
				
		return numRegistros;
	}

	@Override
	public int cuentaFoliosByIp(String ip) {
		int numRegistros = 0;

		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			stmt = conn.prepareStatement(COUNT_IP);
			stmt.setString	(1, "%" + ip + "%");
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				 numRegistros = rs.getInt("NUMAMBIENTES");
			}
			
		} catch (SQLException e) {
			LOG.error("No se logró conectar a la base de datos - " + e);
		}finally {						
			Conexion.close(rs);
			Conexion.close(stmt);
			Conexion.close(conn);
		}
				
		return numRegistros;
	}
}