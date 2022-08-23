package com.telcel.sapi.test;

import java.util.Calendar;
import java.util.Date;

import com.telcel.sapi.service.impl.ConversionesImpl;

public class CalculaFechasTest {

	static ConversionesImpl conversiones = new ConversionesImpl();

	public static void main(String[] args) {
		
		int dias = 15;
		String fechaRecibida = "2022-03-09";
		Date fechaDate 		= conversiones.StringToDateSum(fechaRecibida);
		
//		System.out.println(calculaDiasHabiles(fechaRecibida, dias));
		
		System.out.println(conversiones.CalculaFechaCompromisoPorDiasHabiles(fechaDate, dias));
	}
	
	public static String calculaDiasHabiles(String fechaRecibida, int dias) {
		Date fechaDate 			= conversiones.StringToDateSum(fechaRecibida);
		Calendar fechaCalendar 	= Calendar.getInstance();
		fechaCalendar.setTime(fechaDate);
			
		int diasNatContados = 0;
		
		while(diasNatContados < dias) {
			fechaCalendar.add(Calendar.DATE, 1);		
			System.out.println(nombreDia(fechaCalendar));
			if(fechaCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				fechaCalendar.add(Calendar.DATE, 1);
			}else {
				diasNatContados++;
			}
		}
		
		return conversiones.DateToStringSum(fechaCalendar.getTime());
	}
	
	public static String nombreDia(Calendar fechaCalendar) {
		
		String nombreFecha = "";
		
		switch(fechaCalendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				nombreFecha = "Domingo - " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
			case Calendar.MONDAY:
				nombreFecha = "Lunes 		- " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
			case Calendar.TUESDAY:
				nombreFecha = "Martes 		- " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
			case Calendar.WEDNESDAY:
				nombreFecha = "Miercoles 	- " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
			case Calendar.THURSDAY:
				nombreFecha = "Jueves 		- " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
			case Calendar.FRIDAY:
				nombreFecha = "Viernes		- " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
			case Calendar.SATURDAY:
				nombreFecha = "Sábado - " + conversiones.DateToStringSum(fechaCalendar.getTime());
				break;
		}
		
		
		return nombreFecha;
	}

}
