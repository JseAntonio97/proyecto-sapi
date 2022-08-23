package com.telcel.sapi.service.impl;

import java.util.List;

import com.telcel.sapi.dao.CatalogosDAO;
import com.telcel.sapi.dao.impl.CatalogosDAOImpl;
import com.telcel.sapi.dto.DireccionDTO;
import com.telcel.sapi.dto.EstrategiaDTO;
import com.telcel.sapi.dto.GerenciaDTO;
import com.telcel.sapi.dto.JefeF60DTO;
import com.telcel.sapi.dto.PorcAvanceDTO;
import com.telcel.sapi.dto.ResponsableDTO;
import com.telcel.sapi.dto.TipoAmbienteDTO;
import com.telcel.sapi.dto.TipoF60DTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.CatalogosService;

public class CatalogosServiceImpl implements CatalogosService {

	CatalogosDAO catalogosDAO;

	@Override
	public List<DireccionDTO> cargaCatDireccion() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.cargaCatDireccion();
	}

	@Override
	public List<GerenciaDTO> cargaDataGerencia() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.cargaDataGerencia();
	}

	@Override
	public List<GerenciaDTO> cargaCatGerencia(Long idDireccion) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.cargaCatGerencia(idDireccion);
	}
	
	@Override
	public List<EstrategiaDTO> FindEstrategiasActivas() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.FindEstrategiasActivas();
	}

	@Override
	public List<ResponsableDTO> CargaAllResponsables() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CargaAllResponsables();
	}

	@Override
	public List<ResponsableDTO> CargaResponsablesActivos() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CargaResponsablesActivos();
	}

	/**
	 * Estrategias
	 */

	@Override
	public List<EstrategiaDTO> FindEstrategiasInAc() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.FindEstrategiasInAc();
	}
	
	@Override
	public int GuardaNewEstrategia(String estrategia) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.GuardaNewEstrategia(estrategia);
	}

	@Override
	public int UpdateEstrategia(EstrategiaDTO estrategiaUpdate) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.UpdateEstrategia(estrategiaUpdate);
	}

	@Override
	public int ActivaInactivaEstrategia(EstrategiaDTO estrategiaUpdate, boolean accion) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.ActivaInactivaEstrategia(estrategiaUpdate, accion);
	}

	@Override
	public int DeleteEstrategia(EstrategiaDTO estrategiaUpdate) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.DeleteEstrategia(estrategiaUpdate);
	}

	/**
	 * Responsables
	 */
	
	@Override
	public int GuardaResponsable(ResponsableDTO responsable) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.GuardaResponsable(responsable);
	}

	@Override
	public int UpdateResponsable(ResponsableDTO responsableUpdate) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.UpdateResponsable(responsableUpdate);
	}

	@Override
	public int OnOffResponsable(ResponsableDTO responsableOnOff, boolean accion) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.OnOffResponsable(responsableOnOff, accion);
	}

	@Override
	public int DeleteResponsable(ResponsableDTO responsableDelete) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.DeleteResponsable(responsableDelete);
	}

	@Override
	public List<UsuarioDTO> ConsultaUsuariosResponsables() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.ConsultaUsuariosResponsables();
	}

	@Override
	public List<TipoAmbienteDTO> CatListTiposAmbientes() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CatListTiposAmbientes();
	}

	@Override
	public List<TipoAmbienteDTO> CatListTiposAmbientesActivos() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CatListTiposAmbientesActivos();
	}

	@Override
	public int ActivaInactivaTipoAmbiente(TipoAmbienteDTO tipoAmbiente, boolean onOff) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.ActivaInactivaTipoAmbiente(tipoAmbiente, onOff);
	}

	@Override
	public int GuardaNewTipoAmbiente(String nuevoTipoAmbiente) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.GuardaNewTipoAmbiente(nuevoTipoAmbiente);
	}

	@Override
	public int UpdateNewTipoAmbiente(TipoAmbienteDTO tipoAmbiente) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.UpdateNewTipoAmbiente(tipoAmbiente);
	}

	@Override
	public List<DireccionDTO> CatalogoDirecciones() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CatalogoDirecciones();
	}

	@Override
	public List<GerenciaDTO> CatalogoGerencia() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CatalogoGerencia();
	}

	@Override
	public int GuardaNuevaDireccion(String nuevaDireccion) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.GuardaNuevaDireccion(nuevaDireccion);
	}

	@Override
	public int UpdateDireccionBySelected(DireccionDTO direccionSelected) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.UpdateDireccionBySelected(direccionSelected);
	}

	@Override
	public int ActivaInactivaDireccion(DireccionDTO direccionSelected, boolean onOff) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.ActivaInactivaDireccion(direccionSelected, onOff);
	}

	@Override
	public int GuardaNuevaGerencia(String nvaGerencia, Long idDireccion) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.GuardaNuevaGerencia(nvaGerencia, idDireccion);
	}

	@Override
	public int UpdateGerenciaBySelected(GerenciaDTO gerencia) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.UpdateGerenciaBySelected(gerencia);
	}

	@Override
	public int ActivaInactivaGerencia(GerenciaDTO gerencia, boolean onOff) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.ActivaInactivaGerencia(gerencia, onOff);
	}

	@Override
	public PorcAvanceDTO CargaPorcentajesAvance() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.CargaPorcentajesAvance();
	}

	@Override
	public int ActualizaPorcentajes(PorcAvanceDTO porcentajes) {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.ActualizaPorcentajes(porcentajes);
	}

	@Override
	public List<JefeF60DTO> catalogoJefesF60() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.catalogoJefesF60();
	}

	@Override
	public List<TipoF60DTO> catalogoTiposF60() {
		catalogosDAO = new CatalogosDAOImpl();
		return catalogosDAO.catalogoTiposF60();
	}
}
