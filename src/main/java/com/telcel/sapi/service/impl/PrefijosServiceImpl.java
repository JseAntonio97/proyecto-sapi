package com.telcel.sapi.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.telcel.sapi.service.PrefijosService;

public class PrefijosServiceImpl implements PrefijosService {	

	private static final String PREFIJO		 	= "SIT-";

	public String fecha(String id) {
		LocalDate current_date = LocalDate.now();
		int current_Year = current_date.getYear();
		int current_Month = current_date.getMonthValue();

		String mes = "" + current_Month;
		
		if(mes.length() == 1) {
			mes = "0"+mes;
		}
		return PREFIJO + current_Year + mes + "-" + id;
	}

	@Override
	public String idCompuesto(String id) {
		if(id.length() == 1) {
			id = "00" + id;
		}
		if(id.length() == 2) {
			id = "0" + id;
		}
		
		return fecha(id);
	}

	@Override
	public String NombreReporteXlsx() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		return "SAPI_"+dtf.format(LocalDateTime.now());
	}

}
