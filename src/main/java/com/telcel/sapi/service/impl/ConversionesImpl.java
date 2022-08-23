package com.telcel.sapi.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.telcel.sapi.service.Conversiones;

public class ConversionesImpl implements Conversiones {
	
	@Override
	public String DateToString(Date fecha) {
		String fechaFormat = "";
		
		if(fecha != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			fechaFormat = format.format(fecha);
		}
		
		return fechaFormat;
	}

	@Override
	public Date StringToDate(String fecha) {
		Date fechaDate = new Date();
		
		if(fecha != null) {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-mm-dd"); 
			try {
				fechaDate = formato.parse(fecha);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return fechaDate;
	}

	@Override
	public String DateToStringSum(Date fecha) {
		String fechaFormat = "";
		
		if(fecha != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			fechaFormat = format.format(fecha);
		}
		
		return fechaFormat;
	}

	@Override
	public Date StringToDateSum(String fecha) {
		Date fechaDate = new Date();
		
		if(fecha != null) {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd"); 
			try {
				fechaDate = formato.parse(fecha);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return fechaDate;
	}

	@Override
	public Date Suma21DiasCompromiso(Date fecha) {
		int dias = 21;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		for(int i=0; i < dias;i++) {
			calendar.add(Calendar.DATE, 1);
	    }
		
		return calendar.getTime();
	}

	@Override
	public String AdicionDiasCompromiso(Date fechaInicio, int dias) {
		String fechaInicioString = "";
		
		fechaInicioString = DateToString(fechaInicio);
		Date date = StringToDateSum(fechaInicioString);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		for(int i=0; i < dias;i++) {
			calendar.add(Calendar.DATE, 1);
	    }
		
		return DateToStringSum(calendar.getTime());
	}

	@Override
	public String CalculaFechaCompromisoPorDiasHabiles(Date fechaInicio, int dias) {
		String fechaObtenida = "";
		
		fechaObtenida = DateToString(fechaInicio);
		Date date = StringToDateSum(fechaObtenida);
		
		Calendar fechaCalendar 	= Calendar.getInstance();
		fechaCalendar.setTime(date);
			
		int diasNatContados = 0;
		
		while(diasNatContados < dias) {
			fechaCalendar.add(Calendar.DATE, 1);		
			
			if(fechaCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || fechaCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				fechaCalendar.add(Calendar.DATE, 1);
			}else {
				diasNatContados++;
			}
		}
		
		return DateToStringSum(fechaCalendar.getTime());
	}

}
