package com.telcel.sapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.telcel.sapi.dto.ConexionDTO;
import com.telcel.sapi.service.DataConexionService;
import com.telcel.sapi.service.impl.DataConexionServiceImpl;

public class Conexion {

	//private static final String DATA_URL = "/conexion.properties";
	private static final String DATA_URL = "/conexionProd.properties";
	
	static DataConexionService  dataConexService;
	static ConexionDTO datos;
	
    
    public static Connection getConnection() throws SQLException {
		try {
			Class.forName ( "com.mysql.cj.jdbc.Driver" );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		dataConexService = new DataConexionServiceImpl();
		datos = dataConexService.cargaDatosConexion(DATA_URL);
				
        return DriverManager.getConnection(datos.getUrl(), datos.getUser(), datos.getPwd());
    }
    
    public static void close(ResultSet rs){
        try {
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    	
	public static void close(Statement smtm) {
		try {
			smtm.close();
		} catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
    
    public static void close(PreparedStatement stmt){
        try {
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }
}
