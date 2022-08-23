package com.telcel.sapi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.telcel.sapi.constantes.Estatus;
import com.telcel.sapi.constantes.Fases;
import com.telcel.sapi.dao.ProyectoSeguimientoDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.AplicativoDTO;
import com.telcel.sapi.dto.AsignacionDTO;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.PortafolioEmpresarialDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.ProyectoF60DTO;
import com.telcel.sapi.dto.SeguimientoATP;
import com.telcel.sapi.dto.SeguimientoInfraDTO;
import com.telcel.sapi.dto.SeguimientoPreATPDTO;
import com.telcel.sapi.dto.SeguimientoRTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class ProyectoSeguimientoDAOImpl implements ProyectoSeguimientoDAO {
	
	static final Logger LOG = Logger.getLogger(ProyectoSeguimientoDAOImpl.class);
	
	private static final String SELECT_PROYECTOS 		= "SELECT ASG.IDASIGNACION, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, EST.IDESTRATEGIA, EST.ESTRATEGIA, USR.IDUSUARIO, USR.NOMBRE AS ASIGNADO, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, APL.IDAPLICATIVO, APL.FECHADESPLIEGUE, APL.FECHAFINPRUEBAS, APL.FECHANOTIFICACION, APL.ESTATUS AS ESTATUSAPL, APL.SINFECHAS AS SINFECHAS_APL, F60.IDPROYECTOF60, F60.FECHAINICIO AS FECHAINICIOF60, F60.FECHACOMPROMISO AS FECHACOMPROMISOF60, F60.FECHAFIN AS FECHAFINF60, F60.CUENTACONINFRAESTRUCTURA AS CONINFRAF60, F60.ESTATUS AS ESTATUSF60, F60.SINFECHAS AS SINFECHAS_F60, F60.RESPONSABLE, BOSS.IDJEFEF60, BOSS.NOMBRE AS NOMBRE_BOSS, BOSS.ESTATUS AS ESTATUS_BOSS, TF60.IDTIPOF60, TF60.TIPOF60, TF60.ESTATUS AS ESTATUS_TIPOF60, SIF.IDINFRAESTRUCTURA, SIF.KICKOFF, SIF.ENVIO, SIF.FECHACOMPROMISOINFRA, SIF.FECHAENTREGAUSR, SIF.ESTATUS AS ESTATUSINFRA, SIF.SINFECHAS AS SINFECHAS_INFRA, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA, PATP.IDPREATP, PATP.FECHAINICIO AS FECHAINICIOPREATP, PATP.FECHACOMPROMISO AS FECHACOMPROMISOPREATP, PATP.FECHAFIN AS FECHAFINPREATP, PATP.ESTATUS AS ESTATUSPREATP, PATP.SINFECHAS AS SINFECHAS_PATP, RTO.IDSEGUIMIENTORTO, RTO.FECHAINICIO AS FECHAINICIORTO, RTO.FECHACOMPROMISO AS FECHACOMPROMISORTO, RTO.FECHAFIN AS FECHAFINRTO, RTO.ESTATUS AS ESTATUSRTO, RTO.SINFECHAS AS SINFECHAS_RTO, ATP.IDSEGUIMIENTOATP, ATP.FECHAINICIO AS FECHAINICIOATP, ATP.FECHACOMPROMISO AS FECHACOMPROMISOATP, ATP.FECHAFIN AS FECHAFINATP, ATP.ESTATUS AS ESTATUSATP, ATP.SINFECHAS AS SINFECHAS_ATP, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_ASIGNACION ASG ON ASG.IDPROYECTO = PYT.IDPROYECTO LEFT JOIN CAT_ESTRATEGIA EST ON PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_USUARIO USR ON ASG.IDUSUARIO = USR.IDUSUARIO LEFT JOIN TT_PROYECTO_F60 F60 ON PYT.IDPROYECTO = F60.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_INFRA SIF ON PYT.IDPROYECTO = SIF.IDPROYECTO LEFT JOIN TT_APLICATIVO APL ON PYT.IDPROYECTO = APL.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO LEFT JOIN TT_PRE_ATP PATP ON PYT.IDPROYECTO = PATP.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_RTO RTO ON PYT.IDPROYECTO = RTO.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_ATP ATP ON PYT.IDPROYECTO = ATP.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION LEFT JOIN CAT_JEFE_F60 BOSS ON F60.IDJEFEF60 = BOSS.IDJEFEF60 LEFT JOIN CAT_TIPO_F60 TF60 ON F60.IDTIPOF60 = TF60.IDTIPOF60 WHERE PYT.IDPROYECTO = ?";
	private static final String UPDATE_PROYECTO_AVANCE	= "UPDATE TT_PROYECTO SET PORCAVANCEACTUAL = ? WHERE IDPROYECTO = ?";
	
	private static final String INSERT_PROYECTO_F60 	= "INSERT INTO TT_PROYECTO_F60 (FECHAINICIO, FECHACOMPROMISO, FECHAFIN, IDPROYECTO, CUENTACONINFRAESTRUCTURA, ESTATUS, RESPONSABLE, IDJEFEF60, IDTIPOF60) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PROYECTO_F60 	= "UPDATE TT_PROYECTO_F60 SET FECHAINICIO = ?,  FECHACOMPROMISO = ?,  FECHAFIN = ?,  CUENTACONINFRAESTRUCTURA = ?, SINFECHAS = ?, RESPONSABLE = ?, IDJEFEF60 = ?, IDTIPOF60 = ? WHERE IDPROYECTOF60 = ?";
	private static final String UPDATE_TERMINA_F60		= "UPDATE TT_PROYECTO_F60 SET ESTATUS = ?, SINFECHAS = ? WHERE IDPROYECTOF60 = ?";
	private static final String DELETE_F60				= "DELETE FROM TT_PROYECTO_F60 WHERE IDPROYECTOF60 = ? ";
	
	private static final String INSERT_SEG_INFRAESTRUCTURA 			= "INSERT INTO TT_SEGUIMIENTO_INFRA (KICKOFF, ENVIO, FECHACOMPROMISOINFRA, FECHAENTREGAUSR, SINFECHAS, IDPROYECTO, ESTATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_SEG_INFRAESTRUCTURA 			= "UPDATE TT_SEGUIMIENTO_INFRA SET KICKOFF = ?, ENVIO = ?, FECHACOMPROMISOINFRA = ?, FECHAENTREGAUSR = ?, SINFECHAS = ? WHERE IDINFRAESTRUCTURA = ?";
	private static final String UPDATE_TERMINA_SEG_INFRAESTRUCTURA	= "UPDATE TT_SEGUIMIENTO_INFRA SET ESTATUS = ? WHERE IDINFRAESTRUCTURA = ?";
	private static final String DELETE_SEG_INFRAESTRUCTURA			= "DELETE FROM TT_SEGUIMIENTO_INFRA WHERE IDINFRAESTRUCTURA = ? ";
	
	private static final String INSERT_APLICATIVO 			= "INSERT INTO TT_APLICATIVO (FECHADESPLIEGUE, FECHAFINPRUEBAS, FECHANOTIFICACION, SINFECHAS, IDPROYECTO, ESTATUS) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_APLICATIVO			= "UPDATE TT_APLICATIVO SET FECHADESPLIEGUE = ?, FECHAFINPRUEBAS = ?, FECHANOTIFICACION = ?, SINFECHAS = ? WHERE IDAPLICATIVO = ?";
	private static final String UPDATE_TERMINA_APLICATIVO  	= "UPDATE TT_APLICATIVO SET ESTATUS = ? WHERE IDAPLICATIVO = ?";
	private static final String DELETE_APLICATIVO			= "DELETE FROM TT_APLICATIVO WHERE IDAPLICATIVO = ? ";
	
	private static final String INSERT_PRE_ATP 			= "INSERT INTO TT_PRE_ATP (FECHAINICIO, FECHACOMPROMISO, FECHAFIN, SINFECHAS, IDPROYECTO, ESTATUS) VALUES ( ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_PRE_ATP			= "UPDATE TT_PRE_ATP SET FECHAINICIO = ?, FECHACOMPROMISO = ?, FECHAFIN = ?,  ESTATUS = ?, SINFECHAS = ? WHERE IDPREATP = ?";
	private static final String UPDATE_TERMINA_PRE_ATP  = "UPDATE TT_PRE_ATP SET ESTATUS = ? WHERE IDPREATP = ?";
	private static final String DELETE_PRE_ATP			= "DELETE FROM TT_PRE_ATP WHERE IDPREATP = ? ";

	private static final String INSERT_ATP 				= "INSERT INTO TT_SEGUIMIENTO_ATP (FECHAINICIO, FECHACOMPROMISO, FECHAFIN, SINFECHAS, IDPROYECTO, ESTATUS) VALUES ( ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_ATP				= "UPDATE TT_SEGUIMIENTO_ATP SET FECHAINICIO = ?, FECHACOMPROMISO = ?, FECHAFIN = ?,  ESTATUS = ?, SINFECHAS = ? WHERE IDSEGUIMIENTOATP = ?";
	private static final String UPDATE_TERMINA_ATP  	= "UPDATE TT_SEGUIMIENTO_ATP SET ESTATUS = ? WHERE IDSEGUIMIENTOATP = ?";
	private static final String DELETE_ATP				= "DELETE FROM TT_SEGUIMIENTO_ATP WHERE IDSEGUIMIENTOATP = ? ";

	private static final String INSERT_RTO 				= "INSERT INTO TT_SEGUIMIENTO_RTO (FECHAINICIO, FECHACOMPROMISO, FECHAFIN, SINFECHAS, IDPROYECTO, ESTATUS) VALUES ( ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_RTO				= "UPDATE TT_SEGUIMIENTO_RTO SET FECHAINICIO = ?, FECHACOMPROMISO = ?, FECHAFIN = ?,  ESTATUS = ?, SINFECHAS = ? WHERE IDSEGUIMIENTORTO = ?";
	private static final String UPDATE_TERMINA_RTO  	= "UPDATE TT_SEGUIMIENTO_RTO SET ESTATUS = ? WHERE IDSEGUIMIENTORTO = ?";
	private static final String DELETE_RTO				= "DELETE FROM TT_SEGUIMIENTO_RTO WHERE IDSEGUIMIENTORTO = ? ";

	private static final String UPDATE_FASE_ACTUAL 		= "UPDATE TT_PROYECTO SET FASEACTUAL = ? WHERE IDPROYECTO = ? ";
	private static final String UPDATE_FASE_SIGUIENTE	= "UPDATE TT_PROYECTO SET FASESIGUIENTE = ? WHERE IDPROYECTO = ? ";
	private static final String UPDATE_ESTATUS_PROYECTO	= "UPDATE TT_PROYECTO SET FASEACTUAL = ?, ESTATUS = ? WHERE IDPROYECTO = ? ";
	

	private static final String UPDATE_ESTATUS_FASES_PROYECTO	= "UPDATE TT_PROYECTO SET FASEACTUAL = ? , FASESIGUIENTE = ?, ESTATUS = ? WHERE IDPROYECTO = ? ";
	private static final String MARCA_PROYECTO_LEIDO 	= "UPDATE TT_PROYECTO SET LEIDO = ? WHERE IDPROYECTO = ?";
	
	@Override
	public int actualizaFase(Long idProyecto, String fase, int tipoFase) {
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
			
			switch(tipoFase) {
			case 1:
				stmt = conn.prepareStatement(UPDATE_FASE_ACTUAL);
				break;
			case 2:
				stmt = conn.prepareStatement(UPDATE_FASE_SIGUIENTE);
				break;
			}
			
			stmt.setString  (1, fase);
			stmt.setLong	(2, idProyecto);
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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
	public ProyectoDTO CargaProyectoSelectAllData(Long idProyecto) {
		ProyectoDTO proyecto = new ProyectoDTO();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			stmt = conn.prepareStatement(SELECT_PROYECTOS);
			stmt.setLong(1, idProyecto);
			
			rs	 = stmt.executeQuery();
			
			if(rs.next()) {
				proyecto = new ProyectoDTO(
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
								rs.getInt("IDESTRATEGIA"), 
								rs.getString("ESTRATEGIA"),
								""
								), 
						new PortafolioEmpresarialDTO(
								rs.getInt("IDSEGUIMIENTOPE"),
								rs.getString("APLICA"), 
								rs.getString("FOLIO"),
								rs.getString("ENTRADA"),
								rs.getString("SALIDA")
								),
						null, 
						new AsignacionDTO(
								rs.getInt("IDASIGNACION"), 
								rs.getInt("IDUSUARIO"),
								rs.getString("FECASIGNACION"), 
								rs.getString("TIPOASIGNACION")
								), 
						new UsuarioDTO(
								rs.getLong("IDUSUARIO"), 
								rs.getString("ASIGNADO"), 
								rs.getString("PRIMERAPELLIDO"), 
								rs.getString("SEGUNDOAPELLIDO"), 
								"", "", "", "", "", null, null, null
								),
						new ProyectoF60DTO(
								rs.getInt("IDPROYECTOF60"), 
								rs.getString("FECHAINICIOF60"),
								rs.getString("FECHACOMPROMISOF60"),
								rs.getString("FECHAFINF60"),
								(rs.getInt("CONINFRAF60") == 0) 	 ? false : true,
								rs.getString("ESTATUSF60"),
								(rs.getInt("SINFECHAS_F60") == 0) 	 ? false : true,
										(rs.getString("RESPONSABLE") == null) ? "" : rs.getString("RESPONSABLE"),
										new JefeF60DTO(
													rs.getInt("IDJEFEF60"), 
													rs.getString("NOMBRE_BOSS"), 
													rs.getString("ESTATUS_BOSS")
												),
										new TipoF60DTO(
													rs.getInt("IDTIPOF60"), 
													rs.getString("TIPOF60"), 
													rs.getString("ESTATUS_TIPOF60")
												)
										),
						new SeguimientoInfraDTO(
								rs.getInt("IDINFRAESTRUCTURA"),
								rs.getString("KICKOFF"),
								rs.getString("ENVIO"),
								rs.getString("FECHACOMPROMISOINFRA"),
								rs.getString("FECHAENTREGAUSR"),
								rs.getString("ESTATUSINFRA"),
								(rs.getInt("SINFECHAS_INFRA") == 0) ? false : true ),
						new SeguimientoPreATPDTO(
								rs.getInt("IDPREATP"),
								rs.getString("FECHAINICIOPREATP"),
								rs.getString("FECHACOMPROMISOPREATP"),
								rs.getString("FECHAFINPREATP"),
								rs.getString("ESTATUSPREATP"),
								(rs.getInt("SINFECHAS_PATP") == 0) ? false : true 
								),
						new SeguimientoRTO(
								rs.getInt("IDSEGUIMIENTORTO"), 
								rs.getString("FECHAINICIORTO"),
								rs.getString("FECHACOMPROMISORTO"),
								rs.getString("FECHAFINRTO"),
								rs.getString("ESTATUSRTO"),
								(rs.getInt("SINFECHAS_RTO") == 0) ? false : true 
								),
						new SeguimientoATP(
								rs.getInt("IDSEGUIMIENTOATP"), 
								rs.getString("FECHAINICIOATP"),
								rs.getString("FECHACOMPROMISOATP"),
								rs.getString("FECHAFINATP"),
								rs.getString("ESTATUSATP"),
								(rs.getInt("SINFECHAS_ATP") == 0) ? false : true
								),
						new AplicativoDTO(
								rs.getInt("IDAPLICATIVO"),  
								rs.getString("FECHADESPLIEGUE"),
								rs.getString("FECHAFINPRUEBAS"),
								rs.getString("FECHANOTIFICACION"),
								rs.getString("ESTATUSAPL"),
								(rs.getInt("SINFECHAS_APL") == 0) ? false : true
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
		
		return proyecto;
	}
	
	@Override
	public int GuardaF60(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoSeguimientoDAOImpl, L159: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(INSERT_PROYECTO_F60);
			stmt.setString	(1, proyecto.getProyectoF60().getFechaInicio());
			stmt.setString	(2, proyecto.getProyectoF60().getFechaCompromiso());
			stmt.setString	(3, proyecto.getProyectoF60().getFechaFin());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.setInt		(5, (proyecto.getProyectoF60().isCuentaConInfraestructura()) ?  1 : 0 );
			stmt.setString	(6, Estatus.GUARDADO.toString());
			stmt.setString	(7, proyecto.getProyectoF60().getResponsable());
			stmt.setInt		(8, proyecto.getProyectoF60().getJefeF60().getIdJefeF60());
			stmt.setInt		(9, proyecto.getProyectoF60().getTipoF60().getIdTipoF60());
			
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
	public int UpdateF60(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_PROYECTO_F60);
			stmt.setString	(1, proyecto.getProyectoF60().getFechaInicio());
			stmt.setString	(2, proyecto.getProyectoF60().getFechaCompromiso());
			stmt.setString	(3, proyecto.getProyectoF60().getFechaFin());
			stmt.setInt		(4, (proyecto.getProyectoF60().isCuentaConInfraestructura()) ?  1 : 0 );
			stmt.setInt		(5, (proyecto.getProyectoF60().isSinFechas()) ?  1 : 0 );
			stmt.setString	(6, proyecto.getProyectoF60().getResponsable());
			stmt.setInt		(7, proyecto.getProyectoF60().getJefeF60().getIdJefeF60());
			stmt.setInt		(8, proyecto.getProyectoF60().getTipoF60().getIdTipoF60());
			stmt.setInt		(9, proyecto.getProyectoF60().getIdProyectoF60());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int UpdateTerminaF60(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_TERMINA_F60);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setInt		(2, (proyecto.getProyectoF60().isSinFechas()) ?  1 : 0 );
			stmt.setInt		(3, proyecto.getProyectoF60().getIdProyectoF60());
			stmt.executeUpdate();			
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_AVANCE);
			stmt.setInt	(1, proyecto.getPorcAvanceActual());
			stmt.setLong(2, proyecto.getIdProyecto());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
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
	public int GuardaSegInfra(ProyectoDTO proyecto) {
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
			stmt = conn.prepareStatement(INSERT_SEG_INFRAESTRUCTURA );
			stmt.setString	(1, proyecto.getSeguimientoInfra().getKickoff());
			stmt.setString	(2, proyecto.getSeguimientoInfra().getFechaEnvio());
			stmt.setString	(3, proyecto.getSeguimientoInfra().getFechaCompromiso());
			stmt.setString	(4, proyecto.getSeguimientoInfra().getFechaEntregaUsuario());
			stmt.setInt		(5, (proyecto.getSeguimientoInfra().isSinFechas()) ?  1 : 0 );
			stmt.setLong	(6, proyecto.getIdProyecto());
			stmt.setString	(7, Estatus.GUARDADO.toString());
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
	public int UpdateSegInfra(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_SEG_INFRAESTRUCTURA);
			stmt.setString	(1, proyecto.getSeguimientoInfra().getKickoff());
			stmt.setString	(2, proyecto.getSeguimientoInfra().getFechaEnvio());
			stmt.setString	(3, proyecto.getSeguimientoInfra().getFechaCompromiso());
			stmt.setString	(4, proyecto.getSeguimientoInfra().getFechaEntregaUsuario());
			stmt.setInt		(5, (proyecto.getSeguimientoInfra().isSinFechas()) ?  1 : 0 );
			stmt.setInt		(6, proyecto.getSeguimientoInfra().getIdSeguimientoInfra());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int UpdateTerminaSegInfra(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_TERMINA_SEG_INFRAESTRUCTURA);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setInt		(2, proyecto.getSeguimientoInfra().getIdSeguimientoInfra());
			stmt.executeUpdate();				
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_AVANCE);
			stmt.setInt	(1, proyecto.getPorcAvanceActual());
			stmt.setLong(2, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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
	public int GuardaAplicativo(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoSeguimientoDAOImpl, L470: " + autoCommit);
			}
			
			stmt 	= null;			
			stmt = conn.prepareStatement(INSERT_APLICATIVO);
			stmt.setString	(1, proyecto.getAplicativo().getFechaDespliegue());
			stmt.setString	(2, proyecto.getAplicativo().getFechaFinPruebas());
			stmt.setString	(3, proyecto.getAplicativo().getFechaNotificacion());
			stmt.setInt		(4, (proyecto.getAplicativo().isSinFechas()) ?  1 : 0 );
			stmt.setLong	(5, proyecto.getIdProyecto());
			stmt.setString	(6, Estatus.GUARDADO.toString());
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
	public int UpdateAplicativo(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L516: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_APLICATIVO);
			stmt.setString	(1, proyecto.getAplicativo().getFechaDespliegue());
			stmt.setString	(2, proyecto.getAplicativo().getFechaFinPruebas());
			stmt.setString	(3, proyecto.getAplicativo().getFechaNotificacion());
			stmt.setInt		(4, (proyecto.getAplicativo().isSinFechas()) ?  1 : 0 );
			stmt.setInt		(5, proyecto.getAplicativo().getIdAplicativo());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int UpdateTerminaAplicativo(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_TERMINA_APLICATIVO);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setInt		(2, proyecto.getAplicativo().getIdAplicativo());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_AVANCE);
			stmt.setInt	(1, proyecto.getPorcAvanceActual());
			stmt.setLong(2, proyecto.getIdProyecto());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();			
		}catch (SQLException e) {
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
	public int GuardaPreAtp(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoSeguimientoDAOImpl, L159: " + autoCommit);
			}
			
			stmt 	= null;			
			stmt = conn.prepareStatement(INSERT_PRE_ATP);
			stmt.setString	(1, proyecto.getPreAtp().getFechaInicio());
			stmt.setString	(2, proyecto.getPreAtp().getFechaCompromiso());
			stmt.setString	(3, proyecto.getPreAtp().getFechaFin());
			stmt.setInt		(4, (proyecto.getPreAtp().isSinFechas()) ?  1 : 0 );
			stmt.setLong	(5, proyecto.getIdProyecto());
			stmt.setString	(6, Estatus.GUARDADO.toString());
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
	public int UpdatePreAtp(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_PRE_ATP);
			stmt.setString	(1, proyecto.getPreAtp().getFechaInicio());
			stmt.setString	(2, proyecto.getPreAtp().getFechaCompromiso());
			stmt.setString	(3, proyecto.getPreAtp().getFechaFin());
			stmt.setString	(4, proyecto.getPreAtp().getEstatus());
			stmt.setInt		(5, (proyecto.getPreAtp().isSinFechas()) ?  1 : 0 );
			stmt.setInt		(6, proyecto.getPreAtp().getIdPreAtp());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int UpdateTerminaPreAtp(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_TERMINA_PRE_ATP);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setInt		(2, proyecto.getPreAtp().getIdPreAtp());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_AVANCE);
			stmt.setInt	(1, proyecto.getPorcAvanceActual());
			stmt.setLong(2, proyecto.getIdProyecto());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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
	public int GuardaAtp(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoSeguimientoDAOImpl, L769: " + autoCommit);
			}
			
			stmt 	= null;			
			stmt = conn.prepareStatement(INSERT_ATP);
			stmt.setString	(1, proyecto.getSegATP().getFechaInicio());
			stmt.setString	(2, proyecto.getSegATP().getFechaCompromiso());
			stmt.setString	(3, proyecto.getSegATP().getFechaFin());
			stmt.setInt		(4, (proyecto.getSegATP().isSinFechas()) ?  1 : 0 );
			stmt.setLong	(5, proyecto.getIdProyecto());
			stmt.setString	(6, Estatus.GUARDADO.toString());
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
	public int UpdateAtp(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_ATP);
			stmt.setString	(1, proyecto.getSegATP().getFechaInicio());
			stmt.setString	(2, proyecto.getSegATP().getFechaCompromiso());
			stmt.setString	(3, proyecto.getSegATP().getFechaFin());
			stmt.setString	(4, proyecto.getSegATP().getEstatus());
			stmt.setInt		(5, (proyecto.getSegATP().isSinFechas()) ?  1 : 0 );
			stmt.setInt		(6, proyecto.getSegATP().getIdSeguimientoATP());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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
	

	@SuppressWarnings("resource")
	@Override
	public int UpdateTerminaAtp(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L865: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_TERMINA_ATP);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setInt		(2, proyecto.getSegATP().getIdSeguimientoATP());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_AVANCE);
			stmt.setInt	(1, proyecto.getPorcAvanceActual());
			stmt.setLong(2, proyecto.getIdProyecto());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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
	public int GuardaRto(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoSeguimientoDAOImpl, L769: " + autoCommit);
			}
			
			stmt 	= null;			
			stmt = conn.prepareStatement(INSERT_RTO);
			stmt.setString	(1, proyecto.getSegRTO().getFechaInicio());
			stmt.setString	(2, proyecto.getSegRTO().getFechaCompromiso());
			stmt.setString	(3, proyecto.getSegRTO().getFechaFin());
			stmt.setInt		(4, (proyecto.getSegRTO().isSinFechas()) ?  1 : 0 );
			stmt.setLong	(5, proyecto.getIdProyecto());
			stmt.setString	(6, Estatus.GUARDADO.toString());
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
	public int UpdateRto(ProyectoDTO proyecto) {
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
			
			stmt = conn.prepareStatement(UPDATE_RTO);
			stmt.setString	(1, proyecto.getSegRTO().getFechaInicio());
			stmt.setString	(2, proyecto.getSegRTO().getFechaCompromiso());
			stmt.setString	(3, proyecto.getSegRTO().getFechaFin());
			stmt.setString	(4, proyecto.getSegRTO().getEstatus());
			stmt.setInt		(5, (proyecto.getSegRTO().isSinFechas()) ?  1 : 0 );
			stmt.setInt		(6, proyecto.getSegRTO().getIdSeguimientoRTO());
			stmt.executeUpdate();			
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	
	@SuppressWarnings("resource")
	@Override
	public int UpdateTerminaRto(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L865: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(UPDATE_TERMINA_RTO);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setInt		(2, proyecto.getSegRTO().getIdSeguimientoRTO());
			stmt.executeUpdate();	
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_PROYECTO);
			stmt.setString	(1, Estatus.TERMINADO.toString());
			stmt.setString	(2, Estatus.TERMINADO.toString());
			stmt.setLong	(3, proyecto.getIdProyecto());
			stmt.executeUpdate();				
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_PROYECTO_AVANCE);
			stmt.setInt	(1, proyecto.getPorcAvanceActual());
			stmt.setLong(2, proyecto.getIdProyecto());
			stmt.executeUpdate();		
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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
	public void MarcaProyectoLeido(ProyectoDTO proyecto) {
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		
		try {
			conn = Conexion.getConnection();			
			try {
				if(conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException autoCommit) {
				LOG.error(this.getClass() + "Error al retirar el autocommit, ProyectoDAOImpl, L1128: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(MARCA_PROYECTO_LEIDO);
			stmt.setInt		(1, 1);
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

	@SuppressWarnings("resource")
	@Override
	public int DeleteF60(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit o al ejecutar el Query: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(DELETE_F60);
			stmt.setInt	(1, proyecto.getProyectoF60().getIdProyectoF60());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_FASES_PROYECTO);
			stmt.setString	(1, "Portafolio Empresarial");
			stmt.setString	(2, Fases.F60.toString());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int DeleteSegInfra(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit o al ejecutar el Query: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(DELETE_SEG_INFRAESTRUCTURA);
			stmt.setInt	(1, proyecto.getSeguimientoInfra().getIdSeguimientoInfra());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_FASES_PROYECTO);
			stmt.setString	(1, Fases.F60.toString());
			if(proyecto.getProyectoF60().isCuentaConInfraestructura()) {
				stmt.setString	(2, Fases.APLICATIVO.toString());
			}else {
				stmt.setString	(2, Fases.INFRAESTRUCTURA.toString());
			}
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int DeleteAplicativo(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit o al ejecutar el Query: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(DELETE_APLICATIVO);
			stmt.setInt	(1, proyecto.getAplicativo().getIdAplicativo());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_FASES_PROYECTO);
			if(proyecto.getProyectoF60().isCuentaConInfraestructura()) {
				stmt.setString	(1, Fases.F60.toString());
			}else {
				stmt.setString	(1, Fases.INFRAESTRUCTURA.toString());
			}
			stmt.setString	(2, Fases.APLICATIVO.toString());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int DeletePreAtp(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit o al ejecutar el Query: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(DELETE_PRE_ATP);
			stmt.setInt	(1, proyecto.getPreAtp().getIdPreAtp());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_FASES_PROYECTO);
			stmt.setString	(1, Fases.APLICATIVO.toString());
			stmt.setString	(2, Fases.PREATP.toString());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int DeleteAtp(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit o al ejecutar el Query: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(DELETE_ATP);
			stmt.setInt	(1, proyecto.getSegATP().getIdSeguimientoATP());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_FASES_PROYECTO);
			stmt.setString	(1, Fases.PREATP.toString());
			stmt.setString	(2, Fases.ATP.toString());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

	@SuppressWarnings("resource")
	@Override
	public int DeleteRto(ProyectoDTO proyecto) {
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
				LOG.error(this.getClass() + "Error al retirar el autocommit o al ejecutar el Query: " + autoCommit);
			}
			
			stmt = conn.prepareStatement(DELETE_RTO);
			stmt.setInt	(1, proyecto.getSegRTO().getIdSeguimientoRTO());
			stmt.executeUpdate();
			
			stmt = null;
			stmt = conn.prepareStatement(UPDATE_ESTATUS_FASES_PROYECTO);
			stmt.setString	(1, Fases.ATP.toString());
			stmt.setString	(2, Fases.RTO.toString());
			stmt.setString	(3, Estatus.ASIGNADO.toString());
			stmt.setLong	(4, proyecto.getIdProyecto());
			stmt.executeUpdate();	
			
			result = 1;
			conn.commit();
			
		}catch (SQLException e) {
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

}
