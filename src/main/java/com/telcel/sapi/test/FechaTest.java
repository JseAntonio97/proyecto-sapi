package com.telcel.sapi.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaTest {

	public static void main(String[] args) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        System.out.println("SAPI_"+dtf.format(LocalDateTime.now()));
	}

}
