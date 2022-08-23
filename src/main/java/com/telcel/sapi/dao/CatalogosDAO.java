package com.telcel.sapi.dao;

import java.util.List;

import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.PorcAvanceDTO;
import com.telcel.sapi.dto.ResponsableDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;

public interface CatalogosDAO {
	List<DireccionDTO> 		cargaCatDireccion();
	List<GerenciaDTO>		cargaDataGerencia();
	List<GerenciaDTO>		cargaCatGerencia(Long idDireccion);
	List<EstrategiaDTO> 	FindEstrategiasActivas();
	
	List<ResponsableDTO> 	CargaAllResponsables();	
	List<ResponsableDTO>	CargaResponsablesActivos();	

	List<EstrategiaDTO> 	FindEstrategiasInAc();
	
	int GuardaNewEstrategia(String estrategia);
	int UpdateEstrategia (EstrategiaDTO estrategiaUpdate);
	int DeleteEstrategia (EstrategiaDTO estrategiaUpdate);
	int ActivaInactivaEstrategia(EstrategiaDTO estrategiaUpdate, boolean accion);

	int GuardaResponsable(ResponsableDTO responsable);
	int UpdateResponsable (ResponsableDTO responsableUpdate);
	int OnOffResponsable (ResponsableDTO responsableOnOff, boolean accion);
	int DeleteResponsable (ResponsableDTO responsableDelete);
	
	List<UsuarioDTO> ConsultaUsuariosResponsables();
	
	/**
	 * Ambientes
	 */
	List<TipoAmbienteDTO> CatListTiposAmbientes();
	List<TipoAmbienteDTO> CatListTiposAmbientesActivos();
	int GuardaNewTipoAmbiente(String nuevoTipoAmbiente);
	int ActivaInactivaTipoAmbiente(TipoAmbienteDTO tipoAmbiente, boolean onOff);
	int UpdateNewTipoAmbiente(TipoAmbienteDTO tipoAmbiente);
	int DeleteTipoAmbiente(TipoAmbienteDTO tipoAmbiente);
	
	/**
	 * Direcciones
	 */
	List<DireccionDTO> 		CatalogoDirecciones();
	int GuardaNuevaDireccion(String nuevaDireccion);
	int UpdateDireccionBySelected(DireccionDTO direccionSelected);
	int ActivaInactivaDireccion(DireccionDTO direccionSelected, boolean onOff);
	
	/**
	 * Geencias
	 */
	List<GerenciaDTO>		CatalogoGerencia();
	int GuardaNuevaGerencia(String nvaGerencia, Long idDireccion);
	int UpdateGerenciaBySelected(GerenciaDTO gerencia);
	int ActivaInactivaGerencia(GerenciaDTO gerencia, boolean onOff);
	
	/**
	 * Porcentajes de Avance
	 */
	PorcAvanceDTO CargaPorcentajesAvance();
	int ActualizaPorcentajes(PorcAvanceDTO porcentajes);
	
	/**
	 * Jefes F60
	 */
	List<JefeF60DTO> catalogoJefesF60();
	
	/**
	 * TiposF60
	 */
	List<TipoF60DTO> catalogoTiposF60();
}
