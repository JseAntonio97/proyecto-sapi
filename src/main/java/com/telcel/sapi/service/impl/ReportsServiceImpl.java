package com.telcel.sapi.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

import com.telcel.sapi.constantes.Mensajes;
import com.telcel.sapi.dao.ReportsDAO;
import com.telcel.sapi.dao.impl.ReportsDAOImpl;
import com.telcel.sapi.dto.AmbienteDTO;
import com.telcel.sapi.dto.ProyectoDTO;
import com.telcel.sapi.service.ReportsService;

public class ReportsServiceImpl implements ReportsService{
	ReportsDAO reportDAO;
	static final Logger LOG = Logger.getLogger(ReportsServiceImpl.class);

	private static final String CRITERIO_ESTRATEGIA_CAMPO 	= "EST.ESTRATEGIA";
	private static final String CRITERIO_NOMBRE_CAMPO 		= "PYT.NOMBREPROYECTO";
	private static final String CRITERIO_INTEGRADOR_CAMPO 	= "PYT.INTEGRADOR";
	private static final String CRITERIO_FOLIO_CAMPO 		= "PEM.FOLIO";
	private static final String CRITERIO_HOST_CAMPO 		= "AMB.HOSTNAME";
	private static final String CRITERIO_IP_CAMPO 			= "AMB.IP";
	private static final String CRITERIO_ETAPA_CAMPO 		= "PYT.FASEACTUAL";

	@Override
	public List<ProyectoDTO> cargaProyectosReporte() {
		reportDAO = new ReportsDAOImpl();
		return UneListAmbientesAsuProyecto(reportDAO.cargaProyectosReporte());
	}
	/* etapas*/
	@Override
	public List<ProyectoDTO> cargaProyectosReporteEtapas() {
		reportDAO = new ReportsDAOImpl();
		return UneListAmbientesAsuProyecto(reportDAO.cargaProyectosReporteEtapas());
	}
	/*-----------*/
	
	public List<ProyectoDTO> UneListAmbientesAsuProyecto(List<ProyectoDTO> listProyectos){
		/**
		 * Lista que Replica a la lista recibida
		 */
		List<ProyectoDTO> listProjects2 = new ArrayList<ProyectoDTO>();
		/**
		 * Lista Bandera que almacenará los objetos ProyectoDTO unificados con su lista de ambientes
		 */
		List<ProyectoDTO> listProjects3 = new ArrayList<ProyectoDTO>();
		/**
		 * Lista de ambientes creada a partir de los ambientes obtenidos de la lista recibida
		 */
		List<AmbienteDTO> listAmbiente 	= new ArrayList<AmbienteDTO>();
		/**
		 * Nuevo objeto que servira de bandera para unificar los datos del proyecto y la lista de ambientes
		 */
		ProyectoDTO proy = new ProyectoDTO();
		
		/**
		 * Copiar la lista a la  listProjects2
		 */
		listProjects2 = listProyectos;
		
		for(int i = 0 ; i < listProyectos.size() ; i ++) {
			
			if(listProyectos.get(i).getListAmbientes() != null) {
				
				if(i > 0) {
					if(!listProyectos.get(i).getIdProyecto().equals(listProyectos.get(i-1).getIdProyecto())) {		
						
						listAmbiente = new ArrayList<AmbienteDTO>();
						for(ProyectoDTO proyecto2 : listProjects2) {
							
							if(listProyectos.get(i).getIdProyecto().equals(proyecto2.getIdProyecto())) {
								
								listAmbiente.addAll(proyecto2.getListAmbientes());
								
							}
							
						}
						
						proy = new ProyectoDTO();
						proy = listProyectos.get(i);
						proy.getListAmbientes().clear();
						proy.setListAmbientes(listAmbiente);
						listProjects3.add(proy);
					}
				}else {
					listAmbiente = new ArrayList<AmbienteDTO>();
					for(ProyectoDTO proyecto2 : listProjects2) {
						
						if(listProyectos.get(i).getIdProyecto().equals(proyecto2.getIdProyecto())) {
							
							listAmbiente.addAll(proyecto2.getListAmbientes());
							
						}
						
					}
					
					proy = new ProyectoDTO();
					proy = listProyectos.get(i);
					proy.getListAmbientes().clear();
					proy.setListAmbientes(listAmbiente);
					listProjects3.add(proy);
				}
				
			}else {
				listProjects3.add(listProyectos.get(i));
			}
			
		}
		
		/**
		 * Retornamos la lista listProjects3, que ya tiene los objetos unificados
		 */
		return listProjects3;
	}

	@Override
	public List<ProyectoDTO> BuscaProyectosReporte(String criterio, String parametro) {
		String campoCriterio = "";
		
		switch(criterio) {
			case "estrategia":
				campoCriterio = CRITERIO_ESTRATEGIA_CAMPO;
				break;
			case "nombre":
				campoCriterio = CRITERIO_NOMBRE_CAMPO;
				break;
			case "integrador":
				campoCriterio = CRITERIO_INTEGRADOR_CAMPO;
				break;
			case "folio":
				campoCriterio = CRITERIO_FOLIO_CAMPO;
				break;
			case "host":
				campoCriterio = CRITERIO_HOST_CAMPO;
				break;
			case "ip":
				campoCriterio = CRITERIO_IP_CAMPO;
				break;
			case "etapa":
				campoCriterio = CRITERIO_ETAPA_CAMPO;
				break;
			case "responsable":
				campoCriterio = criterio;
				break;
		}
		
		reportDAO = new ReportsDAOImpl();
		return UneListAmbientesAsuProyecto(reportDAO.BuscaProyectosReporte(campoCriterio, parametro));
	}

	@SuppressWarnings("resource")
	@Override
	public void generaReporteExcel(List<ProyectoDTO> 	listProjects, String nombreExcel) {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		response.setContentType("application/xlsx");
		response.addHeader("Content-Disposition", "attachment; filename = " + nombreExcel +".xlsx");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);


		try {
			String [] columas  = {"Estrategia", "Integrador", "Proyecto", "Folio PE", "Etapa", "Semáforo", "Porcentaje de avance", "Responsable"};

			XSSFWorkbook  	workbook 	= new XSSFWorkbook ();
			XSSFSheet  		sheet 		= workbook.createSheet("Proyectos - SAPI");
			Row 	 		row 		= sheet.createRow(0);
			
			for(int i = 0; i < columas.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(columas[i]);
			}
			
			int registro = 1;
			for(ProyectoDTO proyecto : listProjects) {
				
				row = sheet.createRow(registro);
				row.createCell(0).setCellValue(proyecto.getEstrategia().getEstrategia());
				row.createCell(1).setCellValue(proyecto.getIntegrador());
				row.createCell(2).setCellValue(proyecto.getNombreProyecto());
				row.createCell(3).setCellValue(proyecto.getPortafolioEmp().getFolio());
				row.createCell(4).setCellValue(proyecto.getFaseActual());
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue(proyecto.getPorcAvanceActual());
				row.createCell(7).setCellValue(proyecto.getUsuarioAsignado().getNombre()+" "+proyecto.getUsuarioAsignado().getPrimerApellido()+" "+proyecto.getUsuarioAsignado().getSegundoApellido());
				
				registro ++;
			}
			
			OutputStream out = response.getOutputStream();
			
			workbook.write(out);
			
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (IOException e) {
			LOG.error("Error al escribir el archivo Excel " + e);
		}	
	}

	@SuppressWarnings("resource")
	@Override
	public void generaReporteAllDataExcel(List<ProyectoDTO> listProjects, String nombreExcel) {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		response.setContentType	("application/xlsx");
		response.addHeader		("Content-Disposition", "attachment; filename = " + nombreExcel +".xlsx");
		response.setHeader		("Cache-Control", "no-cache");
		response.setHeader		("Pragma", "no-cache");
		response.setDateHeader	("Expires", 0);

		
		try {
			XSSFWorkbook  	workbook 	= new XSSFWorkbook ();
			XSSFSheet  		sheet 		= workbook.createSheet("Proyectos - SAPI");
			Row 	 		row 		= sheet.createRow(0);
			

			sheet.addMergedRegion(CellRangeAddress.valueOf("A1:K1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("L1:O1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("P1:V1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("W1:AA1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("AB1:AE1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("AF1:AI1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("AJ1:AM1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("AN1:AQ1"));
			sheet.addMergedRegion(CellRangeAddress.valueOf("AR1:BA1"));
			
			String [] Encabezados  = {
									"Proyecto", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", 
									"Portafolio Empresarial", " ", " ", " ", 
									"Seguimiento F60", " ", " ", " ", " ", " ", " ", 
									"Seguimiento Infraestructura", " ", " ", " ", " ",
									"Seguimiento Aplicativo", " ", " ", " ",
									"Seguimiento Pre ATP´s", " ", " ", " ",
									"Seguimiento ATP´s", " ", " ", " ",
									"Seguimiento RTO", " ", " ", " ",
									"Ambientes", " ", " ", " ", " ", " ", " ", " ", " ", " "};
			
			for(int j = 0; j < Encabezados.length; j++) {
				Cell cellEncabezado = row.createCell(j);
				cellEncabezado.setCellValue(Encabezados[j]);
			}	
			
			String [] columas  = {"Identificador", "Integrador", "Proyecto", "Estrategia", "Descripción", "Solicitante", "Dirección", "Gerencia", "Porcentaje Avance","Fase Actual", "Comentarios Generales", 
					"Aplica Folio", "Folio", "Entrada", "Salida", 
					"Fecha Inicio F60", "Fecha Compromiso F60", "Fecha Fin F60", "Cuenta con Fechas F60", "Tipo de F60", "Jefe", "Responsable",
					"Kick Off", "Envío", "Fecha Compromiso", "Fecha Fin", "Cuenta con Fechas Infraestructura",
					"Fecha Despliegue", "Fecha Fin de Pruebas", "Fecha de Notificación", "Cuenta con Fechas Aplicativo",
					"Fecha Inicio Pre ATP", "Fecha Compromiso Pre ATP", "Fecha Fin Pre ATP", "Cuenta con Fechas Pre ATP´s",
					"Fecha Inicio ATP", "Fecha Compromiso ATP", "Fecha Fin ATP", "Cuenta con Fechas ATP´s",
					"Fecha Inicio RTO", "Fecha Compromiso RTO", "Fecha Fin RTO", "Cuenta con Fechas RTO",
					"Ambiente", "CPU", "Memoria", "Disco Duro",  "Unidad de Medida HDD", "Base de Datos", "HostName", "IP", "Entrega de Equipo",
					"Responsable"};

			row = sheet.createRow(1);
			for(int i = 0; i < columas.length; i++) {
				Cell cellColumnas = row.createCell(i);
				cellColumnas.setCellValue(columas[i]);
			}
			
			int registro = 2;
			for(ProyectoDTO proyecto : listProjects) {
				
				row = sheet.createRow(registro);
				row.createCell(0).setCellValue	(proyecto.getIdentificadorInterno());
				row.createCell(1).setCellValue	(proyecto.getIntegrador());
				row.createCell(2).setCellValue	(proyecto.getNombreProyecto());
				row.createCell(3).setCellValue	(proyecto.getEstrategia().getEstrategia());
				row.createCell(4).setCellValue	(proyecto.getDescripcion());
				row.createCell(5).setCellValue	(proyecto.getSolicitante());
				row.createCell(6).setCellValue	(proyecto.getGerenciaDto().getDireccion().getDireccion());
				row.createCell(7).setCellValue	(proyecto.getGerenciaDto().getGerencia());
				row.createCell(8).setCellValue	(proyecto.getPorcAvanceActual()+ "%");
				row.createCell(9).setCellValue	(proyecto.getFaseActual());
				row.createCell(10).setCellValue	(proyecto.getComentariosGrls());
				row.createCell(11).setCellValue	(proyecto.getPortafolioEmp().getAplica());
				row.createCell(12).setCellValue	(proyecto.getPortafolioEmp().getFolio());
				row.createCell(13).setCellValue	(proyecto.getPortafolioEmp().getEntrada());
				row.createCell(14).setCellValue	(proyecto.getPortafolioEmp().getSalida());
				
				row.createCell(15).setCellValue	(proyecto.getProyectoF60().getFechaInicio());
				row.createCell(16).setCellValue	(proyecto.getProyectoF60().getFechaCompromiso());
				row.createCell(17).setCellValue	(proyecto.getProyectoF60().getFechaFin());
				row.createCell(18).setCellValue	((proyecto.getProyectoF60().isSinFechas() == true) ? Mensajes.NO : Mensajes.SI);
				row.createCell(19).setCellValue	((proyecto.getProyectoF60().getTipoF60().getTipoF60() == null ) ? "" : proyecto.getProyectoF60().getTipoF60().getTipoF60());
				row.createCell(20).setCellValue	((proyecto.getProyectoF60().getJefeF60().getNombre() == null ) ? "" : proyecto.getProyectoF60().getJefeF60().getNombre());
				row.createCell(21).setCellValue	(proyecto.getProyectoF60().getResponsable());
			
				row.createCell(22).setCellValue	(proyecto.getSeguimientoInfra().getKickoff());
				row.createCell(23).setCellValue	(proyecto.getSeguimientoInfra().getFechaEnvio());
				row.createCell(24).setCellValue	(proyecto.getSeguimientoInfra().getFechaCompromiso());
				row.createCell(25).setCellValue	(proyecto.getSeguimientoInfra().getFechaEntregaUsuario());
				row.createCell(26).setCellValue	((proyecto.getSeguimientoInfra().isSinFechas()) ? Mensajes.NO : Mensajes.SI);
				
				row.createCell(27).setCellValue	(proyecto.getAplicativo().getFechaDespliegue());
				row.createCell(28).setCellValue	(proyecto.getAplicativo().getFechaFinPruebas());
				row.createCell(29).setCellValue	(proyecto.getAplicativo().getFechaNotificacion());
				row.createCell(30).setCellValue	((proyecto.getAplicativo().isSinFechas()) ? Mensajes.NO : Mensajes.SI);
			
				row.createCell(31).setCellValue	(proyecto.getPreAtp().getFechaInicio());
				row.createCell(32).setCellValue	(proyecto.getPreAtp().getFechaCompromiso());
				row.createCell(33).setCellValue	(proyecto.getPreAtp().getFechaFin());
				row.createCell(34).setCellValue	((proyecto.getPreAtp().isSinFechas()) ? Mensajes.NO : Mensajes.SI);
				
				row.createCell(35).setCellValue	(proyecto.getSegATP().getFechaInicio());
				row.createCell(36).setCellValue	(proyecto.getSegATP().getFechaCompromiso());
				row.createCell(37).setCellValue	(proyecto.getSegATP().getFechaFin());
				row.createCell(38).setCellValue	((proyecto.getSegATP().isSinFechas()) ? Mensajes.NO : Mensajes.SI);
			
				row.createCell(39).setCellValue	(proyecto.getSegRTO().getFechaInicio());
				row.createCell(40).setCellValue	(proyecto.getSegRTO().getFechaCompromiso());
				row.createCell(41).setCellValue	(proyecto.getSegRTO().getFechaFin());
				row.createCell(42).setCellValue	((proyecto.getSegRTO().isSinFechas()) ? Mensajes.NO : Mensajes.SI);
			
				row.createCell(43).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getTipoAmbiente().getTipoAmbiente() 		: "");
				row.createCell(44).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getCpu() 			: 0);
				row.createCell(45).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getMemoria() 		: 0);
				row.createCell(46).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getDiscoDuro() 		: 0);
				row.createCell(47).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getUnidadMedidaDD() : "");
				row.createCell(48).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getBaseDatos() 		: "");
				row.createCell(49).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getHostName() 		: "");
				row.createCell(50).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getIp() 			: "");
				row.createCell(51).setCellValue	(( proyecto.getListAmbientes()!= null) ? proyecto.getListAmbientes().get(0).getEntregaEquipo()	: "");
				row.createCell(52).setCellValue	((proyecto.getUsuarioAsignado().getIdUsuario() != 0)? proyecto.getUsuarioAsignado().getNombre()+" "+proyecto.getUsuarioAsignado().getPrimerApellido()+" "+proyecto.getUsuarioAsignado().getSegundoApellido() : "");
				
				registro ++;
			}
			
			OutputStream out = response.getOutputStream();
			
			workbook.write(out);
			
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (IOException e) {
			LOG.error("Error al escribir el archivo Excel " + e);
		}	
	}

	@Override
	public BarChartModel graficaFasesProyectos(List<ProyectoDTO> listProjects) {
		BarChartModel stackedBarModel = new BarChartModel();
		
		ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Fases de los proyectos");
//        barDataSet.setLabel("En Tiempo");
        barDataSet.setBackgroundColor("rgb(13, 137, 3)");        
        List<Number> dataVal = new ArrayList<>();

//        BarChartDataSet barDataSet2 = new BarChartDataSet();
//        barDataSet2.setLabel("Reprogramados");
//        barDataSet2.setBackgroundColor("rgb(249, 230, 0)");
//        List<Number> dataVal2 = new ArrayList<>();
//
//        BarChartDataSet barDataSet3 = new BarChartDataSet();
//        barDataSet3.setLabel("Retrasados");
//        barDataSet3.setBackgroundColor("rgb(249, 23, 0)");
//        List<Number> dataVal3 = new ArrayList<>();

        int pe 	= 0;
        int f60	= 0;
        int inf	= 0;
        int apl = 0;
        int pre	= 0;
        int atp	= 0;
        int rto	= 0;
        int end	= 0;
        
        for(ProyectoDTO proyecto : listProjects) {

        	if(proyecto.getFaseActual().equals("Portafolio Empresarial")) {
        		pe ++;
        	}
        	if(proyecto.getFaseActual().equals("F60")) {
        		f60 ++;
        	}
        	if(proyecto.getFaseActual().equals("INFRAESTRUCTURA")) {
        		inf ++;
        	}
        	if(proyecto.getFaseActual().equals("APLICATIVO")) {
        		apl ++;
        	}
        	if(proyecto.getFaseActual().equals("PREATP")) {
        		pre ++;
        	}
        	if(proyecto.getFaseActual().equals("ATP")) {
        		atp ++;
        	}
        	if(proyecto.getFaseActual().equals("RTO")) {
        		rto ++;
        	}
        	if(proyecto.getFaseActual().equals("TERMINADO")) {
        		end ++;
        	}
        	
        }

        //Proyectos en Tiempo
        dataVal.add(pe);
        dataVal.add(f60);
        dataVal.add(inf);
        dataVal.add(apl);
        dataVal.add(pre);
        dataVal.add(atp);
        dataVal.add(rto);
        dataVal.add(end);
        
        //Proyectos Reprogramados
//        dataVal2.add(6);
//        dataVal2.add(5);
//        dataVal2.add(4);
//        dataVal2.add(3);
//        dataVal2.add(2);
//        dataVal2.add(1);

        //Proyectos Retrasados
//        dataVal3.add(1);
//        dataVal3.add(2);
//        dataVal3.add(3);
//        dataVal3.add(4);
//        dataVal3.add(5);
//        dataVal3.add(6);

        barDataSet.setData	(dataVal);
//        barDataSet2.setData	(dataVal2);
//        barDataSet3.setData	(dataVal3);

        data.addChartDataSet(barDataSet);
//        data.addChartDataSet(barDataSet2);
//        data.addChartDataSet(barDataSet3);

        List<String> labels = new ArrayList<>();
        labels.add("Portafolio");
        labels.add("F60");
        labels.add("Infraestructura");
        labels.add("Aplicativo");
        labels.add("Pre ATP´s");
        labels.add("ATP´s");
        labels.add("RTO");
        labels.add("Terminado");
        data.setLabels(labels);
        stackedBarModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setStacked(true);
        linearAxes.setOffset(true);
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Distribución de proyectos");
        options.setTitle(title);

        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        stackedBarModel.setOptions(options);
		
		return stackedBarModel;
	}
///***ETAPAS***///
	@Override
	public BarChartModel graficaFasesProyectosEtapas(List<ProyectoDTO> listProjects) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int CalculaNumeroAmbientes(List<ProyectoDTO> listProjects) {
		int numAmbientes = 0;

		for(ProyectoDTO proyecto : listProjects) {
			if(proyecto.getListAmbientes() != null) {
				for(AmbienteDTO ambiente : proyecto.getListAmbientes()) {
					
					if(ambiente.getIdAmbiente() != 0) {
						numAmbientes++;
					}
					
				}
			}
			
		}
		
		return numAmbientes;
	}

	@Override
	public int CalculaNumeroFolios(List<ProyectoDTO> listProjects) {
		List<String> listFolios = new ArrayList<String>();
		List<String> listFolios2da = new ArrayList<String>();
		
		for(ProyectoDTO proyecto : listProjects) {
			if(proyecto.getPortafolioEmp().getFolio() != null) {
				listFolios.add(proyecto.getPortafolioEmp().getFolio());
			}
		}
		
		for(int i = 0 ; i < listFolios.size() ; i ++) {
			boolean existe = false ;
			
			for(String folioComparador : listFolios2da) {				
				if(folioComparador != null) {
					if(listFolios.get(i).equals(folioComparador)) {
						existe = true;
						break;
					}
				}				
			}
			
			if(!existe) {
				listFolios2da.add(listFolios.get(i));
			}			
		}
		
		
		return listFolios2da.size();
	}

	@Override
	public List<ProyectoDTO> cargaProyectosAllData() {
		reportDAO = new ReportsDAOImpl();
		return reportDAO.cargaProyectosAllData();
	}

	@Override
	public List<ProyectoDTO> cargaProyectosAllDataByCriterio(String criterio, String parametro) {
		String campoCriterio = "";
		
		switch(criterio) {
			case "estrategia":
				campoCriterio = CRITERIO_ESTRATEGIA_CAMPO;
				break;
			case "nombre":
				campoCriterio = CRITERIO_NOMBRE_CAMPO;
				break;
			case "integrador":
				campoCriterio = CRITERIO_INTEGRADOR_CAMPO;
				break;
			case "folio":
				campoCriterio = CRITERIO_FOLIO_CAMPO;
				break;
			case "host":
				campoCriterio = CRITERIO_HOST_CAMPO;
				break;
			case "ip":
				campoCriterio = CRITERIO_IP_CAMPO;
				break;
			case "etapa":
				campoCriterio = CRITERIO_ETAPA_CAMPO;
				break;
			case "responsable":
				campoCriterio = criterio;
				break;
		}
		
		reportDAO = new ReportsDAOImpl();
		return UneListAmbientesAsuProyecto(reportDAO.cargaProyectosAllData(campoCriterio, parametro));
	}
	



}
