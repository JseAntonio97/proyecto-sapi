package com.telcel.sapi.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import com.telcel.sapi.dao.AsignacionDAO;
import com.telcel.sapi.dao.impl.AsignacionDAOImpl;
import com.telcel.sapi.dto.AsignacionesDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.dto.UsuarioDTO;
import com.telcel.sapi.service.AsignacionService;

public class AsignacionServiceImpl implements AsignacionService, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3875587015960406778L;

	static final Logger LOG = Logger.getLogger(AsignacionServiceImpl.class);
	
	AsignacionDAO asignacionDAO;
	
	@Override
	public List<ProyectoDTO> CargaProyectos() {
		asignacionDAO = new AsignacionDAOImpl();
		return asignacionDAO.CargaProyectos();
	}

	@Override
	public List<UsuarioDTO> CargaColaboradores(UsuarioDTO user) {
		asignacionDAO = new AsignacionDAOImpl();
		return asignacionDAO.CargaColaboradores(user);
	}

	@Override
	public int AsignaProyectos(UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia) {
		asignacionDAO = new AsignacionDAOImpl();
		return asignacionDAO.AsignaProyectos(usrAsignado, proyecto, idEstrategia);
	}

	@Override
	public int ReAsignaProyecto(UsuarioDTO usrAsignado, ProyectoDTO proyecto, int idEstrategia) {
		asignacionDAO = new AsignacionDAOImpl();
		return asignacionDAO.ReAsignaProyecto(usrAsignado, proyecto, idEstrategia);
	}

	@Override
	public List<ProyectoDTO> BusquedaProyectosByParametros(String nomProyecto, String integrador, String folioPortEmp) {
		asignacionDAO = new AsignacionDAOImpl();
		return asignacionDAO.BusquedaProyectosByParametros(nomProyecto, integrador, folioPortEmp);
	}

	@Override
	public BarChartModel graficarAsignaciones() {
		BarChartModel mixedModel = new BarChartModel();
		
		ChartData data = new ChartData();

        BarChartDataSet dataSet = new BarChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        List<AsignacionesDTO> listAsignacionesConteo = asignacionDAO.ConteoAsignaciones();
        
        for(AsignacionesDTO asignaciones : listAsignacionesConteo) {
        	values.add(asignaciones.getNumeroAsignaciones());
        	labels.add(asignaciones.getUsuario());
        }
        
        dataSet.setData(values);
        dataSet.setLabel("Asignaciones");
        dataSet.setBorderColor("rgb(11, 77, 162)");
        dataSet.setBackgroundColor("rgba(11, 77, 162, 1.0)");
        
        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        mixedModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        
        Title title = new Title();
        title.setDisplay(true);
        //title.setText("Reporte Procesos " );
        options.setTitle(title);
        
        mixedModel.setOptions(options);
		
		return mixedModel;


	}

	@Override
	public void MarcaProyectoNoLeido(ProyectoDTO proyecto) {
		asignacionDAO = new AsignacionDAOImpl();
		asignacionDAO.MarcaProyectoNoLeido(proyecto);
	}

}
