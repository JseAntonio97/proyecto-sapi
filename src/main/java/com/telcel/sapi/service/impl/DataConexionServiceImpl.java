package com.telcel.sapi.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.telcel.sapi.dto.ConexionDTO;
import com.telcel.sapi.service.DataConexionService;

public class DataConexionServiceImpl implements DataConexionService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8411632448249841027L;
	static final Logger LOG = Logger.getLogger(DataConexionServiceImpl.class);
	/**
	 * 
	 */

	@Override
	public ConexionDTO cargaDatosConexion(String properties) {
		ConexionDTO data = new ConexionDTO();
		  
	  try {
			
		  /**Creamos un Objeto de tipo Properties*/
		  Properties propiedades = new Properties();
	   
		  /**Cargamos el archivo desde la ruta especificada*/
		  File file = new File(this.getClass().getResource(properties).getPath());
//		  LOG.info(file.getAbsolutePath());

	      String newPath = file.getAbsolutePath().replace("%20", " ");
//		  LOG.info(newPath);
		  
		  propiedades.load(new FileInputStream(newPath));
	
		  /**Obtenemos los parametros definidos en el archivo*/
		  data.setUrl	(propiedades.getProperty("jdbc_url"));
		  data.setUser	(propiedades.getProperty("jdbc_user"));
		  data.setPwd	(propiedades.getProperty("jdbc_pass"));
	
		  /**Imprimimos los valores*/
		  //LOG.info(dato + ": "+ datoObtenido);
	  } catch (FileNotFoundException e) {
		  LOG.info("Error, El archivo no exite");
	  } catch (IOException e) {
		  LOG.info("Error, No se puede leer el archivo");
	  }
	  
	  return data;
	}

}
