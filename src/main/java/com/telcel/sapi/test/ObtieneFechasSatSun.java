package com.telcel.sapi.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.telcel.sapi.service.impl.ConversionesImpl;

public class ObtieneFechasSatSun {
	
	static ConversionesImpl conversiones = new ConversionesImpl();
	
	public static void main(String[] args) {
		List<String> listFechasSatSun = new ArrayList<String>();
		boolean fechaIniEqualsFecFin = true;
		
		String fechaInicial = "2000-01-01";
		String fechaFinal	= "2060-12-31";
		
		String fecha = "";
		
		Date fechaInicialDate 	= conversiones.StringToDate(fechaInicial);
		Calendar fechaCalendar 	= Calendar.getInstance();
		fechaCalendar.setTime(fechaInicialDate);		
		
		while(fechaIniEqualsFecFin) {	
			fecha = "";
			fecha = conversiones.DateToStringSum(fechaCalendar.getTime());
			
			if(fecha.equals(fechaFinal)) {
				fechaIniEqualsFecFin = false;
			}
			
			if(fechaCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {			
				listFechasSatSun.add(conversiones.DateToStringSum(fechaCalendar.getTime()));
			}
			fechaCalendar.add(Calendar.DATE, 1);
		}
		

//		for(String fechaInhabil : listFechasSatSun) {
//			System.out.println(fechaInhabil);
//		}
		
//		creaPropertiesFechasInhabiles(listFechasSastSun);
		leeFechasInhabiles();
	}
	
	static void creaPropertiesFechasInhabiles(List<String> listaFechasInhabiles) {
		String ruta = "src\\main\\java\\fechasInhabiles.txt";
		
		File file = new File(ruta);
		// Si el archivo no existe es creado
		
		System.out.println(file.getAbsolutePath());
		
		try {
			if (!file.exists()) {            
				file.createNewFile();			
	        }
			
			FileWriter archivo 	= new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(archivo);
            
			for(String fecha : listaFechasInhabiles) {
				bw.write(fecha);
				bw.write("\n");
			}

            bw.close();
		} catch (IOException e) {
			System.err.println("Error al Escribir en el archivo " + e);
		}
		
		
	}
	
	static List<String> leeFechasInhabiles() {
		List<String> listFechasSatSun = new ArrayList<String>();
		String ruta = "src\\main\\java\\fechasInhabiles.txt";
		
		try {
			FileReader fr = new FileReader(ruta);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			
			String fecha = "";
			
			while((fecha = br.readLine()) != null) {
				System.out.println("date: " + fecha);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listFechasSatSun;
	}

}
