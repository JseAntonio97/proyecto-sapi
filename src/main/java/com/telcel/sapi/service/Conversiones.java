package com.telcel.sapi.service;

import java.util.Date;

public interface Conversiones {
	String DateToString(Date fecha);
	Date StringToDate(String fecha);
	String DateToStringSum(Date fecha);
	Date StringToDateSum(String fecha);
	
	Date Suma21DiasCompromiso(Date fecha);
	String AdicionDiasCompromiso(Date fechaInicio, int dias);
	String CalculaFechaCompromisoPorDiasHabiles(Date fechaInicio, int dias);
}
