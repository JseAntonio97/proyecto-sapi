package com.telcel.sapi.dao;

public interface BitacoraDAO {
	int insertBitacora(Long idUsuario, String actividad, String descripcion, String modulo, Long idProyecto);
}
