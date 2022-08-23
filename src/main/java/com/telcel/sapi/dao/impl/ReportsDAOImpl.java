package com.telcel.sapi.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


import com.telcel.sapi.dao.ReportsDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.AplicativoDTO;
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
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;

public class ReportsDAOImpl implements ReportsDAO{
	
	static final Logger LOG = Logger.getLogger(ReportsDAOImpl.class);

	private static final String SELECT_PROYECTOS_AMBIENTE		= "SELECT PYT.IDPROYECTO, EST.IDESTRATEGIA, EST.ESTRATEGIA, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.FASEACTUAL, PYT.PORCAVANCEACTUAL, PYT.ESTATUS, PYT.FECHAREGISTRO, PEM.FOLIO, USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, AMB.IDAMBIENTE, TAM.IDTIPOAMBIENTE, TAM.TIPOAMBIENTE, AMB.CPU, AMB.MEMORIA, AMB.DISCODURO, AMB.UNIDADMEDIDADD, AMB.SISTEMAOPERATIVO, AMB.BASEDATOS, AMB.HOSTNAME, AMB.IP, AMB.ENTREGAEQUIPO, AMB.COMENTARIO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN CAT_ESTRATEGIA EST ON	PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN TT_SEGUIMIENTO_PE PEM ON PYT.IDPROYECTO = PEM.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN CAT_TIPO_AMBIENTE TAM ON AMB.IDTIPOAMBIENTE = TAM.IDTIPOAMBIENTE LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.ESTATUS IN ('PORTAFOLIO', 'LIBERADO', 'ASIGNADO', 'TERMINADO') ORDER BY PYT.IDPROYECTO";
	private static final String SELECT_PROYECTOS_BY_CRITERIO 	= "SELECT PYT.IDPROYECTO, EST.IDESTRATEGIA, EST.ESTRATEGIA, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.FASEACTUAL, PYT.PORCAVANCEACTUAL, PYT.ESTATUS, PYT.FECHAREGISTRO, PEM.FOLIO, USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, AMB.IDAMBIENTE, TAM.IDTIPOAMBIENTE, TAM.TIPOAMBIENTE, AMB.CPU, AMB.MEMORIA, AMB.DISCODURO, AMB.UNIDADMEDIDADD, AMB.SISTEMAOPERATIVO, AMB.BASEDATOS, AMB.HOSTNAME, AMB.IP, AMB.ENTREGAEQUIPO, AMB.COMENTARIO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN CAT_ESTRATEGIA EST ON	PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN TT_SEGUIMIENTO_PE PEM ON PYT.IDPROYECTO = PEM.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN CAT_TIPO_AMBIENTE TAM ON AMB.IDTIPOAMBIENTE = TAM.IDTIPOAMBIENTE LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE ";
	private static final String LIKE						 	= " LIKE ? AND PYT.ESTATUS IN ('PORTAFOLIO', 'LIBERADO', 'ASIGNADO', 'TERMINADO') ORDER BY PYT.IDPROYECTO";
	private static final String SAME						 	= " = ? AND PYT.ESTATUS IN ('PORTAFOLIO', 'LIBERADO', 'ASIGNADO', 'TERMINADO') ORDER BY PYT.IDPROYECTO";
	private static final String RESPONSABLE_ASIGNADO		 	= " ASG.IDUSUARIO = ? ORDER BY PYT.IDPROYECTO";
	
	private static final String SELECT_PROYECTOS_ALL_DATA 				= "SELECT PYT.IDPROYECTO, EST.IDESTRATEGIA, EST.ESTRATEGIA, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.FASEACTUAL, PYT.PORCAVANCEACTUAL, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PEM.IDSEGUIMIENTOPE, PEM.APLICA, PEM.FOLIO, PEM.ENTRADA, PEM.SALIDA, F60.IDPROYECTOF60, F60.FECHAINICIO AS FECHAINICIOF60, F60.FECHACOMPROMISO AS FECHACOMPROMISOF60, F60.FECHAFIN AS FECHAFINF60, F60.SINFECHAS AS SINFECHAS_F60, F60.RESPONSABLE AS RESPONSABLE_F60, BOSS.IDJEFEF60, BOSS.NOMBRE AS NOMBRE_BOSS, BOSS.ESTATUS AS ESTATUS_BOSS, TF60.IDTIPOF60, TF60.TIPOF60, TF60.ESTATUS AS ESTATUS_TIPOF60, INFRA.IDINFRAESTRUCTURA, INFRA.KICKOFF, INFRA.ENVIO, INFRA.FECHACOMPROMISOINFRA, INFRA.FECHAENTREGAUSR, INFRA.SINFECHAS AS SINFECHAS_INFRA, APL.IDAPLICATIVO, APL.FECHADESPLIEGUE, APL.FECHAFINPRUEBAS, APL.FECHANOTIFICACION, APL.SINFECHAS AS SINFECHAS_APL, PAT.IDPREATP, PAT.FECHAINICIO AS FECHAINICIOPREATP, PAT.FECHACOMPROMISO AS FECHACOMPROMISOPREATP, PAT.FECHAFIN AS FECHAFINPREATP, PAT.SINFECHAS AS SINFECHAS_PAT, ATP.IDSEGUIMIENTOATP, ATP.FECHAINICIO AS FECHAINICIOATP, ATP.FECHACOMPROMISO AS FECHACOMPROMISOATP, ATP.FECHAFIN AS FECHAFINATP, ATP.SINFECHAS AS SINFECHAS_ATP, RTO.IDSEGUIMIENTORTO, RTO.FECHAINICIO AS FECHAINICIORTO, RTO.FECHACOMPROMISO AS FECHACOMPROMISORTO, RTO.FECHAFIN AS FECHAFINRTO, RTO.SINFECHAS AS SINFECHAS_RTO, USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, AMB.IDAMBIENTE, TAM.IDTIPOAMBIENTE, TAM.TIPOAMBIENTE, AMB.CPU, AMB.MEMORIA, AMB.DISCODURO, AMB.UNIDADMEDIDADD, AMB.SISTEMAOPERATIVO, AMB.BASEDATOS, AMB.HOSTNAME, AMB.IP, AMB.ENTREGAEQUIPO, AMB.COMENTARIO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_SEGUIMIENTO_INFRA INFRA ON PYT.IDPROYECTO = INFRA.IDPROYECTO LEFT JOIN TT_PROYECTO_F60 F60 ON PYT.IDPROYECTO = F60.IDPROYECTO LEFT JOIN TT_APLICATIVO APL ON PYT.IDPROYECTO = APL.IDPROYECTO LEFT JOIN CAT_ESTRATEGIA EST ON	PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN TT_SEGUIMIENTO_PE PEM ON PYT.IDPROYECTO = PEM.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN TT_PRE_ATP PAT ON PYT.IDPROYECTO = PAT.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_ATP ATP ON PYT.IDPROYECTO = ATP.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_RTO RTO ON PYT.IDPROYECTO = RTO.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION LEFT JOIN CAT_TIPO_AMBIENTE TAM ON AMB.IDTIPOAMBIENTE = TAM.IDTIPOAMBIENTE LEFT JOIN CAT_JEFE_F60 BOSS ON F60.IDJEFEF60 = BOSS.IDJEFEF60 LEFT JOIN CAT_TIPO_F60 TF60 ON F60.IDTIPOF60 = TF60.IDTIPOF60 WHERE PYT.ESTATUS IN ('PORTAFOLIO', 'LIBERADO', 'ASIGNADO', 'TERMINADO') ORDER BY PYT.IDPROYECTO";
	private static final String SELECT_PROYECTOS_ALL_DATA_BY_CRITERIO 	= "SELECT PYT.IDPROYECTO, EST.IDESTRATEGIA, EST.ESTRATEGIA, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.FASEACTUAL, PYT.PORCAVANCEACTUAL, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PEM.IDSEGUIMIENTOPE, PEM.APLICA, PEM.FOLIO, PEM.ENTRADA, PEM.SALIDA, F60.IDPROYECTOF60, F60.FECHAINICIO AS FECHAINICIOF60, F60.FECHACOMPROMISO AS FECHACOMPROMISOF60, F60.FECHAFIN AS FECHAFINF60, F60.SINFECHAS AS SINFECHAS_F60, F60.RESPONSABLE AS RESPONSABLE_F60, BOSS.IDJEFEF60, BOSS.NOMBRE AS NOMBRE_BOSS, BOSS.ESTATUS AS ESTATUS_BOSS, TF60.IDTIPOF60, TF60.TIPOF60, TF60.ESTATUS AS ESTATUS_TIPOF60, INFRA.IDINFRAESTRUCTURA, INFRA.KICKOFF, INFRA.ENVIO, INFRA.FECHACOMPROMISOINFRA, INFRA.FECHAENTREGAUSR, INFRA.SINFECHAS AS SINFECHAS_INFRA, APL.IDAPLICATIVO, APL.FECHADESPLIEGUE, APL.FECHAFINPRUEBAS, APL.FECHANOTIFICACION, APL.SINFECHAS AS SINFECHAS_APL, PAT.IDPREATP, PAT.FECHAINICIO AS FECHAINICIOPREATP, PAT.FECHACOMPROMISO AS FECHACOMPROMISOPREATP, PAT.FECHAFIN AS FECHAFINPREATP, PAT.SINFECHAS AS SINFECHAS_PAT, ATP.IDSEGUIMIENTOATP, ATP.FECHAINICIO AS FECHAINICIOATP, ATP.FECHACOMPROMISO AS FECHACOMPROMISOATP, ATP.FECHAFIN AS FECHAFINATP, ATP.SINFECHAS AS SINFECHAS_ATP, RTO.IDSEGUIMIENTORTO, RTO.FECHAINICIO AS FECHAINICIORTO, RTO.FECHACOMPROMISO AS FECHACOMPROMISORTO, RTO.FECHAFIN AS FECHAFINRTO, RTO.SINFECHAS AS SINFECHAS_RTO, USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, AMB.IDAMBIENTE, TAM.IDTIPOAMBIENTE, TAM.TIPOAMBIENTE, AMB.CPU, AMB.MEMORIA, AMB.DISCODURO, AMB.UNIDADMEDIDADD, AMB.SISTEMAOPERATIVO, AMB.BASEDATOS, AMB.HOSTNAME, AMB.IP, AMB.ENTREGAEQUIPO, AMB.COMENTARIO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN TT_SEGUIMIENTO_INFRA INFRA ON PYT.IDPROYECTO = INFRA.IDPROYECTO LEFT JOIN TT_PROYECTO_F60 F60 ON PYT.IDPROYECTO = F60.IDPROYECTO LEFT JOIN TT_APLICATIVO APL ON PYT.IDPROYECTO = APL.IDPROYECTO LEFT JOIN CAT_ESTRATEGIA EST ON	PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN TT_SEGUIMIENTO_PE PEM ON PYT.IDPROYECTO = PEM.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN TT_PRE_ATP PAT ON PYT.IDPROYECTO = PAT.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_ATP ATP ON PYT.IDPROYECTO = ATP.IDPROYECTO LEFT JOIN TT_SEGUIMIENTO_RTO RTO ON PYT.IDPROYECTO = RTO.IDPROYECTO LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION LEFT JOIN CAT_TIPO_AMBIENTE TAM ON AMB.IDTIPOAMBIENTE = TAM.IDTIPOAMBIENTE LEFT JOIN CAT_JEFE_F60 BOSS ON F60.IDJEFEF60 = BOSS.IDJEFEF60 LEFT JOIN CAT_TIPO_F60 TF60 ON F60.IDTIPOF60 = TF60.IDTIPOF60 WHERE ";

	private static final String RESPONSABLE 		= "responsable";
	private static final String ETAPA 				= "PYT.FASEACTUAL";
	
	///**ETAPAS**////
	private static final String SELECT_PROYECTOS_ETAPAS		= "SELECT PYT.IDPROYECTO, EST.IDESTRATEGIA, EST.ESTRATEGIA, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.FASEACTUAL, PYT.PORCAVANCEACTUAL, PYT.ESTATUS, PYT.FECHAREGISTRO, PEM.FOLIO, USR.IDUSUARIO, USR.NOMBRE, USR.PRIMERAPELLIDO, USR.SEGUNDOAPELLIDO, AMB.IDAMBIENTE, TAM.IDTIPOAMBIENTE, TAM.TIPOAMBIENTE, AMB.CPU, AMB.MEMORIA, AMB.DISCODURO, AMB.UNIDADMEDIDADD, AMB.SISTEMAOPERATIVO, AMB.BASEDATOS, AMB.HOSTNAME, AMB.IP, AMB.ENTREGAEQUIPO, AMB.COMENTARIO, GER.IDGERENCIA, GER.GERENCIA AS UPDATE_GERENCIA, DIR.IDDIRECCION, DIR.DIRECCION AS UPDATE_DIRECCION FROM TT_PROYECTO PYT LEFT JOIN CAT_ESTRATEGIA EST ON	PYT.IDESTRATEGIA = EST.IDESTRATEGIA LEFT JOIN TT_ASIGNACION ASG ON PYT.IDPROYECTO = ASG.IDPROYECTO LEFT JOIN TT_USUARIO USR ON USR.IDUSUARIO = ASG.IDUSUARIO LEFT JOIN TT_SEGUIMIENTO_PE PEM ON PYT.IDPROYECTO = PEM.IDPROYECTO LEFT JOIN TT_AMBIENTE AMB ON PYT.IDPROYECTO = AMB.IDPROYECTO LEFT JOIN CAT_TIPO_AMBIENTE TAM ON AMB.IDTIPOAMBIENTE = TAM.IDTIPOAMBIENTE LEFT JOIN CAT_GERENCIA GER ON PYT.IDGERENCIA = GER.IDGERENCIA LEFT JOIN CAT_DIRECCION DIR ON GER.IDDIRECCION = DIR.IDDIRECCION WHERE PYT.ESTATUS IN ('PORTAFOLIO', 'LIBERADO', 'ASIGNADO', 'TERMINADO') ORDER BY PYT.IDPROYECTO";
	
	
	@Override
	public List<ProyectoDTO> cargaProyectosReporte() {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();		
			stmt = conn.prepareStatement(SELECT_PROYECTOS_AMBIENTE);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				
				listAmbiente = new ArrayList<AmbienteDTO>();
				if(rs.getInt	("IDAMBIENTE") != 0) {
					listAmbiente.add(
						new AmbienteDTO(
								rs.getInt	("IDAMBIENTE"),
								new TipoAmbienteDTO(
										rs.getInt("IDTIPOAMBIENTE"), 
										rs.getString("TIPOAMBIENTE")
										, ""),
								"",
								rs.getInt	("CPU"), 
								rs.getInt	("MEMORIA"), 
								rs.getInt	("DISCODURO"), 
								rs.getString("UNIDADMEDIDADD"), 
								rs.getString("SISTEMAOPERATIVO"), 
								rs.getString("BASEDATOS"), 
								rs.getString("HOSTNAME"), 
								rs.getString("IP"), 
								rs.getString("ENTREGAEQUIPO"), 
								null,
								rs.getString("COMENTARIO") 
								)
						);
				}
				
				listProjects.add(
						new ProyectoDTO(
								rs.getLong("IDPROYECTO"),
								"", 
								rs.getString("INTEGRADOR"),
								rs.getString("NOMBREPROYECTO"), 
								"", 
								"", 
								"", 
								"", 
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
								"", 
								rs.getString("FASEACTUAL"), 
								"", 
								"", 
								rs.getString("ESTATUS"),
								rs.getString("FECHAREGISTRO"), 
								"", 
								0L,
								
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
								(rs.getInt	("IDAMBIENTE") != 0) ? listAmbiente : null,
								null, 
								new UsuarioDTO(
										rs.getLong	("IDUSUARIO"), 
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
		
		return listProjects;
	}
	
	//////////*** codigo reportes etapas **/////////
	@Override
	public List<ProyectoDTO> cargaProyectosReporteEtapas() {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		List<PortafolioEmpresarialDTO> portafolioEmp = new ArrayList<PortafolioEmpresarialDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();		
			stmt = conn.prepareStatement(SELECT_PROYECTOS_ETAPAS);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				
				listAmbiente = new ArrayList<AmbienteDTO>();
				if(rs.getInt	("IDAMBIENTE") != 0) {
					listAmbiente.add(
						new AmbienteDTO(
								rs.getInt	("IDAMBIENTE"),
								new TipoAmbienteDTO(
										rs.getInt("IDTIPOAMBIENTE"), 
										rs.getString("TIPOAMBIENTE")
										, ""),
								"",
								rs.getInt	("CPU"), 
								rs.getInt	("MEMORIA"), 
								rs.getInt	("DISCODURO"), 
								rs.getString("UNIDADMEDIDADD"), 
								rs.getString("SISTEMAOPERATIVO"), 
								rs.getString("BASEDATOS"), 
								rs.getString("HOSTNAME"), 
								rs.getString("IP"), 
								rs.getString("ENTREGAEQUIPO"), 
								null,
								rs.getString("COMENTARIO") 
								)
						);
				}
				
				listProjects.add(
						new ProyectoDTO(
								rs.getLong("IDPROYECTO"),
								"", 
								rs.getString("INTEGRADOR"),
								rs.getString("NOMBREPROYECTO"), 
								"", 
								"", 
								"", 
								"", 
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
								"", 
								rs.getString("FASEACTUAL"), 
								"", 
								"", 
								rs.getString("ESTATUS"),
								rs.getString("FECHAREGISTRO"), 
								"", 
								0L,
								
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
								(rs.getInt	("IDAMBIENTE") != 0) ? listAmbiente : null,
								null, 
								new UsuarioDTO(
										rs.getLong	("IDUSUARIO"), 
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
		
		return listProjects;
	}

	
	/////////////////************************//////////////////

	@Override
	public List<ProyectoDTO> BuscaProyectosReporte(String criterio, String parametro) {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			if(criterio.equals(RESPONSABLE)) {
				stmt = conn.prepareStatement(SELECT_PROYECTOS_BY_CRITERIO + RESPONSABLE_ASIGNADO);
				stmt.setLong(1, Long.parseLong(parametro));
			}else if(criterio.equals(ETAPA)) {
				stmt = conn.prepareStatement(SELECT_PROYECTOS_BY_CRITERIO + criterio + SAME );				
				stmt.setString (1, parametro);
			}else {				
				stmt = conn.prepareStatement(SELECT_PROYECTOS_BY_CRITERIO + criterio + LIKE );				
				stmt.setString (1, "%"+ parametro +"%");	
			}
			
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
				listAmbiente = new ArrayList<AmbienteDTO>();
				if(rs.getInt	("IDAMBIENTE") != 0) {
					listAmbiente.add(
						new AmbienteDTO(
								rs.getInt	("IDAMBIENTE"),
								new TipoAmbienteDTO(
										rs.getInt("IDTIPOAMBIENTE"), 
										rs.getString("TIPOAMBIENTE")
										, ""),
								"", 
								rs.getInt	("CPU"), 
								rs.getInt	("MEMORIA"), 
								rs.getInt	("DISCODURO"), 
								rs.getString("UNIDADMEDIDADD"), 
								rs.getString("SISTEMAOPERATIVO"), 
								rs.getString("BASEDATOS"), 
								rs.getString("HOSTNAME"), 
								rs.getString("IP"), 
								rs.getString("ENTREGAEQUIPO"), 
								null,
								rs.getString("COMENTARIO")
								)
						);
				}
				
				listProjects.add(
						new ProyectoDTO(
								rs.getLong("IDPROYECTO"),
								"", 
								rs.getString("INTEGRADOR"),
								rs.getString("NOMBREPROYECTO"), 
								"", 
								"", 
								"", 
								"", 
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
								"", 
								rs.getString("FASEACTUAL"), 
								"", 
								"", 
								rs.getString("ESTATUS"),
								rs.getString("FECHAREGISTRO"), 
								"", 
								0L,
								
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
								(rs.getInt	("IDAMBIENTE") != 0) ? listAmbiente : null,
								null, 
								new UsuarioDTO(
										rs.getLong	("IDUSUARIO"), 
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
		
		return listProjects;
	}

	@Override
	public List<ProyectoDTO> cargaProyectosAllData() {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();		
			stmt = conn.prepareStatement(SELECT_PROYECTOS_ALL_DATA);
			rs	 = stmt.executeQuery();
			
			
			while(rs.next()) {
				listAmbiente = new ArrayList<AmbienteDTO>();
				listAmbiente.add(new AmbienteDTO(
						rs.getInt	("IDAMBIENTE"),
						new TipoAmbienteDTO(
								rs.getInt("IDTIPOAMBIENTE"), 
								rs.getString("TIPOAMBIENTE")
								, ""),
						"", 
						rs.getInt	("CPU"), 
						rs.getInt	("MEMORIA"), 
						rs.getInt	("DISCODURO"), 
						rs.getString("UNIDADMEDIDADD"), 
						rs.getString("SISTEMAOPERATIVO"), 
						rs.getString("BASEDATOS"), 
						rs.getString("HOSTNAME"), 
						rs.getString("IP"), 
						rs.getString("ENTREGAEQUIPO"), 
						null,
						rs.getString("COMENTARIO")
						)
					);
				
				listProjects.add(
						new ProyectoDTO(
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
								"", 
								rs.getString("FASEACTUAL"), 
								"", 
								rs.getString("COMENTARIOSGRLS"), 
								rs.getString("ESTATUS"),
								rs.getString("FECHAREGISTRO"), 
								"", 
								0L,
								
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
								(rs.getInt	("IDAMBIENTE") != 0) ? listAmbiente : null,
								null, 
								new UsuarioDTO(
										rs.getLong	("IDUSUARIO"), 
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
								new ProyectoF60DTO(
										rs.getInt	("IDPROYECTOF60"),
										rs.getString("FECHAINICIOF60"), 
										rs.getString("FECHACOMPROMISOF60"), 
										rs.getString("FECHAFINF60"), 
										false, 
										"",
										(rs.getInt("SINFECHAS_F60") == 0) 	 ? false : true,
										(rs.getString("RESPONSABLE_F60") == null) ? "" : rs.getString("RESPONSABLE_F60"),
										new JefeF60DTO(
													rs.getInt("IDJEFEF60"), 
													rs.getString("NOMBRE_BOSS"), 
													rs.getString("ESTATUS_BOSS")
												),
										new TipoF60DTO(
													rs.getInt("IDTIPOF60"), 
													rs.getString("TIPOF60"), 
													rs.getString("ESTATUS_TIPOF60")
												)), 
								new SeguimientoInfraDTO(
										rs.getInt	("IDINFRAESTRUCTURA"),
										rs.getString("KICKOFF"), 
										rs.getString("ENVIO"), 
										rs.getString("FECHACOMPROMISOINFRA"), 
										rs.getString("FECHAENTREGAUSR"), 
										"",
										(rs.getInt("SINFECHAS_INFRA") == 0) ? false : true ), 
								new SeguimientoPreATPDTO(
										rs.getInt	("IDPREATP"), 
										rs.getString("FECHAINICIOPREATP"), 
										rs.getString("FECHACOMPROMISOPREATP"), 
										rs.getString("FECHAFINPREATP"), 
										"",
										(rs.getInt("SINFECHAS_PAT") == 0) ? false : true), 
								new SeguimientoRTO(
										rs.getInt("IDSEGUIMIENTORTO"), 
										rs.getString("FECHAINICIORTO"), 
										rs.getString("FECHACOMPROMISORTO"), 
										rs.getString("FECHAFINRTO"), "",
										(rs.getInt("SINFECHAS_RTO") == 0) ? false : true), 
								new SeguimientoATP(
										rs.getInt	("IDSEGUIMIENTOATP"), 
										rs.getString("FECHAINICIOATP"), 
										rs.getString("FECHACOMPROMISOATP"), 
										rs.getString("FECHAFINATP"), "",
										(rs.getInt("SINFECHAS_ATP") == 0) ? false : true), 
								new AplicativoDTO(
										rs.getInt("IDAPLICATIVO"), 
										rs.getString("FECHADESPLIEGUE"), 
										rs.getString("FECHAFINPRUEBAS"), 
										rs.getString("FECHANOTIFICACION"), "",
										(rs.getInt("SINFECHAS_APL") == 0) ? false : true)
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
		
		return listProjects;
	}

	@Override
	public List<ProyectoDTO> cargaProyectosAllData(String criterio, String parametro) {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();
			
			if(criterio.equals(RESPONSABLE)) {
				stmt = conn.prepareStatement(SELECT_PROYECTOS_ALL_DATA_BY_CRITERIO + RESPONSABLE_ASIGNADO);
				stmt.setLong(1, Long.parseLong(parametro));
			}else {
				
				stmt = conn.prepareStatement(SELECT_PROYECTOS_ALL_DATA_BY_CRITERIO + criterio + LIKE );			
				stmt.setString (1, "%"+ parametro +"%");
			}			
			
			rs	 = stmt.executeQuery();
			
			
			while(rs.next()) {
				listAmbiente = new ArrayList<AmbienteDTO>();
				listAmbiente.add(new AmbienteDTO(
						rs.getInt	("IDAMBIENTE"),
						new TipoAmbienteDTO(
								rs.getInt("IDTIPOAMBIENTE"), 
								rs.getString("TIPOAMBIENTE")
								, ""),
						"",
						rs.getInt	("CPU"), 
						rs.getInt	("MEMORIA"), 
						rs.getInt	("DISCODURO"), 
						rs.getString("UNIDADMEDIDADD"), 
						rs.getString("SISTEMAOPERATIVO"), 
						rs.getString("BASEDATOS"), 
						rs.getString("HOSTNAME"), 
						rs.getString("IP"), 
						rs.getString("ENTREGAEQUIPO"), 
						null,
						rs.getString("COMENTARIO")
					)
				);
				
				listProjects.add(
						new ProyectoDTO(
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
								"", 
								rs.getString("FASEACTUAL"), 
								"", 
								rs.getString("COMENTARIOSGRLS"), 
								rs.getString("ESTATUS"),
								rs.getString("FECHAREGISTRO"), 
								"", 
								0L,
								
								null, 
								new EstrategiaDTO(
										rs.getInt	("IDESTRATEGIA"), 
										rs.getString("ESTRATEGIA"),
										""), 
								new PortafolioEmpresarialDTO(
										rs.getInt	("IDSEGUIMIENTOPE"),
										rs.getString("FOLIO"),
										rs.getString("APLICA"),
										rs.getString("ENTRADA"),
										rs.getString("SALIDA")), 
								(rs.getInt	("IDAMBIENTE") != 0) ? listAmbiente : null,
								null, 
								new UsuarioDTO(
										rs.getLong	("IDUSUARIO"), 
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
								new ProyectoF60DTO(
										rs.getInt	("IDPROYECTOF60"),
										rs.getString("FECHAINICIOF60"), 
										rs.getString("FECHACOMPROMISOF60"), 
										rs.getString("FECHAFINF60"), 
										false, 
										"",
										(rs.getInt("SINFECHAS_F60") == 0) 	 ? false : true,
										(rs.getString("RESPONSABLE_F60") == null) ? "" : rs.getString("RESPONSABLE_F60"),
										new JefeF60DTO(
													rs.getInt("IDJEFEF60"), 
													rs.getString("NOMBRE_BOSS"), 
													rs.getString("ESTATUS_BOSS")
												),
										new TipoF60DTO(
													rs.getInt("IDTIPOF60"), 
													rs.getString("TIPOF60"), 
													rs.getString("ESTATUS_TIPOF60")
												)), 
								new SeguimientoInfraDTO(
										rs.getInt	("IDINFRAESTRUCTURA"),
										rs.getString("KICKOFF"), 
										rs.getString("ENVIO"), 
										rs.getString("FECHACOMPROMISOINFRA"), 
										rs.getString("FECHAENTREGAUSR"), 
										"",
										(rs.getInt("SINFECHAS_INFRA") == 0) ? false : true), 
								new SeguimientoPreATPDTO(
										rs.getInt	("IDPREATP"), 
										rs.getString("FECHAINICIOPREATP"), 
										rs.getString("FECHACOMPROMISOPREATP"), 
										rs.getString("FECHAFINPREATP"), 
										"",
										(rs.getInt("SINFECHAS_PAT") == 0) ? false : true), 
								new SeguimientoRTO(
										rs.getInt("IDSEGUIMIENTORTO"), 
										rs.getString("FECHAINICIORTO"), 
										rs.getString("FECHACOMPROMISORTO"), 
										rs.getString("FECHAFINRTO"), "",
										(rs.getInt("SINFECHAS_RTO") == 0) ? false : true), 
								new SeguimientoATP(
										rs.getInt	("IDSEGUIMIENTOATP"), 
										rs.getString("FECHAINICIOATP"), 
										rs.getString("FECHACOMPROMISOATP"), 
										rs.getString("FECHAFINATP"), "",
										(rs.getInt("SINFECHAS_ATP") == 0) ? false : true), 
								new AplicativoDTO(
										rs.getInt("IDAPLICATIVO"), 
										rs.getString("FECHADESPLIEGUE"), 
										rs.getString("FECHAFINPRUEBAS"), 
										rs.getString("FECHANOTIFICACION"), "",
										(rs.getInt("SINFECHAS_APL") == 0) ? false : true)
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
		
		return listProjects;
	}
	/*
	 * private static final String SELECT_MENSUAL_ETAPA = "";
	 * 
	 * @Override public List<ProyectoDTO> BuscarMensualesProyectosEtapa(String
	 * fechaIni, String fechaFin) { List<ProyectoDTO> listProjects = new
	 * ArrayList<ProyectoDTO>(); PortafolioEmpresarialDTO portafolioempresarialdto =
	 * null;
	 * 
	 * Connection conn = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * 
	 * try { conn = Conexion.getConnection(); stmt =
	 * conn.prepareStatement(SELECT_MENSUAL_ETAPA);
	 * 
	 * stmt.setString(1, fechaIni); stmt.setString(2, fechaFin);
	 * 
	 * rs = stmt.executeQuery();
	 * 
	 * while (rs.next()) { portafolioempresarialdto = new
	 * PortafolioEmpresarialDTO();
	 * 
	 * portafolioempresarialdto.setSalida(rs.getString("SALIDA"));
	 * portafolioempresarialdto.setCount(rs.getInt("COUNT"));
	 * 
	 * listProjects.add(portafolioempresarialdto);
	 * 
	 * } } catch (SQLException e) {
	 * LOG.error("No se logró conectar a la base de datos - " + e); } finally {
	 * Conexion.close(rs); Conexion.close(stmt); Conexion.close(conn); } return
	 * listProjects; }
	 */

	@Override
	public List<ProyectoDTO> BuscarMensualesProyectosEtapa(String fechaIni, String fechaFin) {
		List<ProyectoDTO> listProjects = new ArrayList<ProyectoDTO>();
		List<AmbienteDTO> listAmbiente = new ArrayList<AmbienteDTO>();
		List<PortafolioEmpresarialDTO> portafolioEmp = new ArrayList<PortafolioEmpresarialDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;
		
		try {
			conn = Conexion.getConnection();		
			stmt = conn.prepareStatement(SELECT_PROYECTOS_ETAPAS);
			rs	 = stmt.executeQuery();
			
			while(rs.next()) {
			
				
				listProjects.add(
						new ProyectoDTO(
								rs.getLong("IDPROYECTO"),
								"", 
								rs.getString("INTEGRADOR"),
								rs.getString("NOMBREPROYECTO"), 
								"", 
								"", 
								"", 
								"", 
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
								"", 
								rs.getString("FASEACTUAL"), 
								"", 
								"", 
								rs.getString("ESTATUS"),
								rs.getString("FECHAREGISTRO"), 
								"", 
								0L,
								
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
								(rs.getInt	("IDAMBIENTE") != 0) ? listAmbiente : null,
								null, 
								new UsuarioDTO(
										rs.getLong	("IDUSUARIO"), 
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
		
		return listProjects;
	}

	
	
}
