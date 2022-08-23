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
import com.telcel.sapi.dao.InfrestructuraDAO;
import com.telcel.sapi.database.Conexion;
import com.telcel.sapi.dto.AsignacionDTO;
import com.telcel.sapi.dto.PortafolioEmpresarialDTO;
import com.telcel.sapi.dto.ProyectoDTO;

public class InfrestructuraDAOImpl implements InfrestructuraDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7779080258552744189L;

	static final Logger LOG = Logger.getLogger(InfrestructuraDAOImpl.class);
	
	private static final String SELECT_PROYECTOS_ASIGNADOS_BY_USER = "SELECT ASG.IDASIGNACION, ASG.IDUSUARIO, ASG.FECASIGNACION, ASG.TIPOASIGNACION, PYT.IDPROYECTO, PYT.IDENTIFICADORINTERNO, PYT.INTEGRADOR, PYT.NOMBREPROYECTO, PYT.DESCRIPCION, PYT.SOLICITANTE, PYT.DIRECCION, PYT.GERENCIA, PYT.PORCAVANCEACTUAL, PYT.PORCAVANCEANTERIOR, PYT.FASEACTUAL, PYT.FASESIGUIENTE, PYT.COMENTARIOSGRLS, PYT.ESTATUS, PYT.FECHAREGISTRO, PYT.HORAREGISTRO, PYT.LEIDO, SPE.IDSEGUIMIENTOPE, SPE.APLICA, SPE.FOLIO, SPE.ENTRADA, SPE.SALIDA FROM TT_ASIGNACION ASG INNER JOIN TT_PROYECTO PYT ON ASG.IDPROYECTO = PYT.IDPROYECTO INNER JOIN TT_SEGUIMIENTO_PE SPE ON PYT.IDPROYECTO = SPE.IDPROYECTO WHERE PYT.ESTATUS = ? AND ASG.IDUSUARIO = ?";

	@Override
	public List<ProyectoDTO> FindAllProyects(int idUsuario) {
		List<ProyectoDTO> listProyectos = new ArrayList<ProyectoDTO>();
		
		Connection 			conn 	= null;
		PreparedStatement 	stmt 	= null;
		ResultSet 			rs		= null;

		try {
			conn = Conexion.getConnection();			
			stmt = conn.prepareStatement(SELECT_PROYECTOS_ASIGNADOS_BY_USER);
			stmt.setString	(1, Estatus.ASIGNADO.toString());
			stmt.setInt		(2, idUsuario);
			
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
						null,
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

}
	
	


