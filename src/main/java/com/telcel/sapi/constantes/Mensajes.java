package com.telcel.sapi.constantes;

public class Mensajes {
	/**Mensajes de Error
	 * 
	 */
	/**Mensajes de Login
	 * 
	 */
	public final static String ERROR_USUARIO_INEXISTENTE	= "El usuario no se encuentra registrado";
	public final static String ERROR_CONSTRASENIA_INVALIDA 	= "La contraseña es incorrecta";
	public final static String ERROR_USUARIO_INHABILITADO 	= "El usuario ingresado se encuentra inhabilitado, favor de contactar a asu lider para habilitar su usuario";	
	
	/**
	 * Mensajes de Registro de Proyectos
	 */

	public final static String FECHAS_PORTAFOLIO_REQUERIDOS	= "Las fechas del portafolio empresarial son requeridas";
	public final static String ERROR_OCURRIDO				= "Ocurrió un error inesperado, favor de intentar mas tarde";
	public final static String ERROR_FECHAS_VACIAS			= "Las fechas para registrar el portafolio empresarial son requeridas";
	public final static String ERROR_FOLIO_VACIO			= "Al seleccionar 'Si' aplica, es requerido el folio para el portafolio empresarial";
	
	
	/**
	 * Mensajes de Cambio de contraseña
	 */
	public final static String ERROR_PWD_OLD_VACIA 			= "La contraseña anterior es requerida";
	public final static String ERROR_PWD_OLD_ERRONEA		= "La contraseña es incorrecta";
	public final static String ERROR_PWD_NEW_VACIA 			= "La nueva contraseña es requerida";
	public final static String ERROR_PWD_NEW_REPEAT_VACIA 	= "La nueva contraseña es requerida";
	public final static String ERROR_PWDS_NO_COINCIDEN	 	= "No coincide con la nueva contraseña";
	
	
	/**
	 * Mensajes Advertencia
	 */
	public final static String USUARIO_PWD_VACIOS 			= "Favor de introducir sus credenciales";
	public final static String USUARIO_VACIO 				= "Favor de introducir su número de empleado";
	public final static String PASSWORD_VACIO 				= "Favor de introducir su contraseña";
	
	/**
	 * Mensajes Exitosos
	 */
	public static final String REGISTRO_PROYECTO_EXITOSO	= "El registro se ha realizado exitosamente";
	public static final String PASSWORD_ACTUALIZADA_EXITO	= "La contraseña se ha actualizado correctamente";
	public static final String ACTUALIZA_PROYECTO_EXITOSO	= "El proyecto se ha actualizado exitosamente";
	
	/**
	 * Mensajes Portafolio Empresarial
	 */
	public final static String ACTUALIZA_PORTAFOLIO_EXITO	= "Se actualizó el portafolio empresarial exitosamente";
	public static final String ERROR_ACTUALIZA_PORTAFOLIO	= "Ocurrió un error al actualizar el portafolio empresarial";
	public static final String ERROR_FOLIO_REPETIDO			= "El folio ingresado ya se encuentra registrado para ";
	public static final String PORYECTOS					= " proyecto";
	public static final String PROYECTOS_FOLIO_REGISTRADOS	= "El Folio ingresado se encuentra registrado con los siguientes proyectos";
	
	
	/**
	 * Mensajes Cambio de Estado
	 */
	public static final String PROYECTO_LIBERADO_PE			= "El proyecto se ha liberado exitosamente";
	public static final String ERROR_LIBERAR_PROYECTO_PE	= "Ocurrió un error al liberar el proyecto seleccionado";

	/**
	 * Mensajes Asignación
	 */
	public static final String PROYECTO_ASIGNADO_EXITO_1	= "El proyecto ";
	public static final String PROYECTO_ASIGNADO_EXITO_2	= "ha sido asignado exitosamente con";
	public static final String PROYECTO_ASIGNADO_ERROR		= "Ocurrió un error al asignar el proyecto seleccionado";
	public static final String SIN_RESULTADOS_WARNING		= "No se lograron obtener resultados de la búsqueda solicitada";
	
	/**
	 * Mensajes ReAsignación
	 */
	public static final String PROYECTO_REASIGNADO_EXITO_1	= "El proyecto ";
	public static final String PROYECTO_REASIGNADO_EXITO_2	= "ha sido reasignado exitosamente con";
	public static final String PROYECTO_REASIGNADO_ERROR	= "Ocurrió un error al reasignar el proyecto seleccionado";
	
	public static final String PROYECTO_SIN_ESTRATEGIA		= "Favor de seleccionar una estrategía";
	
	/**
	 * Mensajes Seguimiento
	 */
	public static final String PROYECTO_F60_GUARDADO				= "El F60 ha sido almacenado exitosamente";
	public static final String PROYECTO_F60_MODIFICADO_EXITO		= "El F60 ha sido modificado exitosamente";
	public static final String PROYECTO_F60_ERROR_GUARDADO			= "El F60 no ha sido almacenado debido a un error interno";
	public static final String PROYECTO_F60_DATOS_FALTANTES			= "Los Campos Tipo F60, Jefes y Responsable son requeridos";
	public static final String PROYECTO_F60_TERMINADO				= "El F60 ha sido terminado exitosamente";
	public static final String PROYECTO_F60_ERROR_TERMINADO			= "El F60 no ha sido terminado debido a un error interno";
	public static final String PROYECTO_F60_SIN_FECHAS				= "Para terminar F60 es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_F60_SIN_FECHAS_SEG_GEN		= "Para modificar el F60 es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_F60_ELIMINADO_EXITO			= "Se ha eliminado el seguimiento F60 satisfactoriamente";
	public static final String PROYECTO_F60_ELIMINADO_ERROR			= "Ha ocurrido un error al eliminar el seguimiento F60";
	
	public static final String PROYECTO_INFRA_GUARDADO				= "El Seguimiento de infraestructura ha sido guardado exitosamente";
	public static final String PROYECTO_INFRA_MODIFICADO_EXITO		= "El Seguimiento de infraestructura ha sido modificado exitosamente";
	public static final String PROYECTO_INFRA_ERROR_GUARDADO		= "El Seguimiento de infraestructura no ha sido almacenado debido a un error interno";
	public static final String PROYECTO_INFRA_MODIFICADO_ERROR		= "El Seguimiento de infraestructura no ha sido modificado debido a un error interno";
	public static final String PROYECTO_INFRA_TERMINADO				= "El Seguimiento de infraestructura ha sido terminado exitosamente";
	public static final String PROYECTO_INFRA_ERROR_TERMINADO		= "El Seguimiento de infraestructura no ha sido terminado debido a un error interno";
	public static final String INFRA_ERROR_KICKOFF_VACIO			= "No se puede guardar la fecha inicio sí el KickOff esta vacío";
	public static final String INFRA_SIN_FECHAS						= "Para terminar la Infraestructura es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_INFRA_SIN_FECHAS_SEG_GEN	= "Para modificar Infraestructura es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_INFRA_ELIMINADO_EXITO		= "Se ha eliminado el seguimiento Infraestructura satisfactoriamente";
	public static final String PROYECTO_INFRA_ELIMINADO_ERROR		= "Ha ocurrido un error al eliminar el seguimiento Infraestructura";

	public static final String PROYECTO_APLICATIVO_GUARDADO			= "Las fechas del aplicativo han sido almacenadas exitosamente";
	public static final String PROYECTO_APLICATIVO_MODIFICADO_EXITO	= "Las fechas del aplicativo han sido modificadas exitosamente";
	public static final String PROYECTO_APLICATIVO_ERROR_GUARDADO	= "Las fechas del aplicativo no han sido almacenadas debido a un error interno";
	public static final String PROYECTO_APLICATIVO_MODIFICADO_ERROR	= "Las fechas del aplicativo no han sido modificadas debido a un error interno";
	public static final String PROYECTO_APLICATIVO_TERMINADO		= "Aplicativo ha sido terminado exitosamente";
	public static final String PROYECTO_APLICATIVO_ERROR_TERMINADO	= "Aplicativo no ha sido terminado debido a un error interno";
	public static final String APLICATIVO_SIN_FECHAS				= "Para terminar el Aplicativo es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_APLICATIVO_SIN_FECHAS_SEG_GEN= "Para modificar el Aplicativo es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_APLICATIVO_ELIMINADO_EXITO	= "Se ha eliminado el seguimiento Aplicativo satisfactoriamente";
	public static final String PROYECTO_APLICATIVO_ELIMINADO_ERROR	= "Ha ocurrido un error al eliminar el seguimiento Aplicativo";

	public static final String PROYECTO_PRE_ATP_GUARDADO			= "Las fechas de Pre ATPs han sido almacenadas exitosamente";
	public static final String PROYECTO_PRE_ATP_MODIFICADO_EXITO	= "Las fechas de Pre ATPs han sido modificadas exitosamente";
	public static final String PROYECTO_PRE_ATP_ERROR_GUARDADO		= "Las fechas de Pre ATPs no han sido almacenadas debido a un error interno";
	public static final String PROYECTO_PRE_ATP_MODIFICADO_ERROR	= "Las fechas de Pre ATPs no han sido modificadas debido a un error interno";
	public static final String PROYECTO_PRE_ATP_TERMINADO			= "Los Pre ATPs han sido terminado exitosamente";
	public static final String PROYECTO_PRE_ATP_ERROR_TERMINADO		= "Los Pre ATPs no han sido terminado debido a un error interno";
	public static final String PROYECTO_PRE_ATP_SIN_FECHAS			= "Para terminar el PRE ATP es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_PRE_ATP_SIN_FECHAS_SEG_GEN	= "Para modificar el PRE ATP es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_PRE_ATP_ELIMINADO_EXITO		= "Se ha eliminado el seguimiento Pre ATP satisfactoriamente";
	public static final String PROYECTO_PRE_ATP_ELIMINADO_ERROR		= "Ha ocurrido un error al eliminar el seguimiento Pre ATP";

	public static final String PROYECTO_ATP_GUARDADO				= "Las fechas ATPs han sido almacenadas exitosamente";
	public static final String PROYECTO_ATP_ERROR_GUARDADO			= "Las fechas ATPs no han sido almacenadas debido a un error interno";
	public static final String PROYECTO_ATP_MODIFICADO_EXITO		= "Las fechas ATPs han sido modificadas exitosamente";
	public static final String PROYECTO_ATP_MODIFICADO_ERROR		= "Las fechas ATPs no han sido modificadas debido a un error interno";
	public static final String PROYECTO_ATP_TERMINADO				= "Los ATPs han sido terminado exitosamente";
	public static final String PROYECTO_ATP_ERROR_TERMINADO			= "Los ATPs no han sido terminado debido a un error interno";
	public static final String PROYECTO_ATP_SIN_FECHAS				= "Para terminar ATP es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_ATP_SIN_FECHAS_SEG_GEN		= "Para modificar el ATP es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_ATP_ELIMINADO_EXITO			= "Se ha eliminado el seguimiento ATP satisfactoriamente";
	public static final String PROYECTO_ATP_ELIMINADO_ERROR			= "Ha ocurrido un error al eliminar el seguimiento ATP";

	public static final String PROYECTO_RTO_GUARDADO				= "Las fechas RTO han sido almacenadas exitosamente";
	public static final String PROYECTO_RTO_ERROR_GUARDADO			= "Las fechas RTO no han sido almacenadas debido a un error interno";
	public static final String PROYECTO_RTO_MODIFICADO_EXITO		= "Las fechas RTO han sido modificadas exitosamente";
	public static final String PROYECTO_RTO_MODIFICADO_ERROR		= "Las fechas RTO no han sido modificadas debido a un error interno";
	public static final String PROYECTO_RTO_TERMINADO				= "Los RTO han sido terminado exitosamente";
	public static final String PROYECTO_RTO_ERROR_TERMINADO			= "Los RTO no han sido terminado debido a un error interno";
	public static final String PROYECTO_RTO_SIN_FECHAS				= "Para terminar RTO es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_RTO_SIN_FECHAS_SEG_GEN		= "Para modificar el RTO es necesario introducir las fechas requeridas, en caso de no contar con ellas, marque la casilla 'No se cuenta con fechas'";
	public static final String PROYECTO_RTO_ELIMINADO_EXITO			= "Se ha eliminado el seguimiento RTO satisfactoriamente";
	public static final String PROYECTO_RTO_ELIMINADO_ERROR			= "Ha ocurrido un error al eliminar el seguimiento RTO";

	public static final String INCIDENTE_REGISTRADO					= "Se registró el incidente";
	public static final String INCIDENTE_ERROR_REGISTRO				= "Ocurrió un error al registrar el incidente";
	public static final String INCIDENTE_ACTUALIZADO				= "Se actualizó el incidente correctamente";
	public static final String INCIDENTE_ERROR_ACTUALIZADO			= "Ocurrió un error al actualizar el incidente";
	public static final String INCIDENTE_CERRADO					= "Se cerró el incidente";
	public static final String INCIDENTE_ERROR_CERRADO				= "Ocurrió un error al cerrar el incidente";
	public static final String INCIDENTE_ERROR_CAMPOS_VACIO			= "Los Campos 'Incidente' y 'responsable' no pueden ser vacios";
	

	/**
	 * Mensajes Infraestructura
	 */
	public static final String CAMPOS_VACIOS_ERROR					= "Favor de Completar los campos requeridos *";
	
	public static final String AMBIENTE_REGISTRO_EXITO				= "El ambiente ha sido registrado exitosamente";
	public static final String AMBIENTE_REGISTRO_ERROR				= "Ocurrió un error al registrar el ambiente";
	public static final String AMBIENTE_ERROR_GUARDAR				= "Error al Guardar el registro";
	public static final String AMBIENTE_ACTUALIZACION_EXITO			= "El ambiente ha sido actualizado exitosamente";
	public static final String AMBIENTE_ACTUALIZACION_ERROR			= "Ocurrió un error al actualizar el ambiente";
	public static final String AMBIENTE_HOST_ERROR					= "El hostname que intenta relacionar ya existe en ";
	public static final String AMBIENTE_IP_ERROR					= "La dirección IP que intenta agregar ya existe en ";
	public static final String AMBIENTE_ELIMINACION_EXITO			= "El ambiente ha sido eliminado exitosamente";
	public static final String AMBIENTE_ELIMINACION_ERROR			= "Ocurrió un error al eliminar el ambiente ";


	/**
	 * Mensajes Reportes
	 */
	public static final String CRITERIO_BUSQUEDA_VACIO				= "Debe seleccionar un criterio de búsqueda"; 
	public static final String ESTRATEGIA_BUSQUEDA_VACIO			= "Debe seleccionar una estrategia";
	public static final String RESPONSABLE_BUSQUEDA_VACIO			= "Debe seleccionar un responsable";
	public static final String ETAPA_BUSQUEDA_VACIO					= "Debe seleccionar una etapa";
	
	/**
	 * Reporte Excel
	 */
	public static final String FASE_SIN_FECHAS						= "No cuenta con fechas";
	public static final String FASE_CON_FECHAS						= "Cuenta con fechas";
	public static final String SI									= "SI";
	public static final String NO									= "NO";
	
	
	/**
	 *  Mensajes de catalogos de funciones operativas responsables y estrategias 
	 */
	
	public static final String REGISTRO_RESPONSABLE_EXITOSO  	= "El responsable ha sido registrado exitosamente";
	public static final String ERROR_REGISTRO_RESPONSABLE		= "Ha ocurrido un error al registrar el responsable";
	
	public static final String UPDATE_RESPONSABLES_EXITOSO      = "El responsable ha sido modificado exitosamente";
	public static final String UPDATE_RESPONSABLES_ERROR        = "Ocurrió un error al modificar el responsable";
	
	public static final String ACTIVAR_RESPONSABLE_EXITOSO 		= "El responsable fue activado correctamente";
	public static final String ERROR_ACTIVAR_RESPONSABLE   		= "Ocurrió un error al activar al responsable";
	
	public static final String DESACTIVAR_RESPONSABLE_EXITOSO 	= "El responsable fue desactivado correctamente";
	public static final String ERROR_DESACTIVAR_RESPONSABLE   	= "Ocurrió un error al desactivar al responsable";

	public static final String ELIMINACION_RESPONSABLE_EXITO	= "El responsable seleccionado ha sido eliminado exitosamente";
	public static final String ELIMINACION_ERROR_RESPONSABLE 	= "Ocurrió un error al eliminar el responsable seleccionado";
	
	public static final String REGISTRO_ESTRATEGIA_EXITOSO 		= "La Estrategia fue Registrada correctamente";
	public static final String ERROR_OCURRIDO_ESTRATEGIA	   	= "Ocurrió un error al registrar la estrategia ingresada";
	
	public static final String ACTIVAR_ESTRATEGIA_EXITOSO 		= "La Estrategia fue activada correctamente";
	public static final String ERROR_ACTIVAR_ESTRATEGIA	   		= "Ocurrió un error al activar la estrategia ingresada";
	
	public static final String DESACTIVAR_ESTRATEGIA_EXITOSO 	= "La Estrategia fue desactivada correctamente";
	public static final String ERROR_DESACTIVAR_ESTRATEGIA	   	= "Ocurrió un error al desactivar la estrategia ingresada";
	
	public static final String ACTUALIZACION_ESTRATEGIAS_EXITOSO= "La Estrategia ha sido Actualizada correctamente";
	public static final String ERROR_ACTUALIZAR_ESTRATEGIA	   	= "Ocurrió un error al actualizar la estrategia seleccionada";

	public static final String ELIMINACION_ESTRATEGIAS_EXITO 	= "La Estrategia Seleccionada ha sido eliminada exitosamente";
	public static final String ELIMINACION_ERROR_ESTRATEGIAS 	= "Ocurrió un error al eliminar la estrategia seleccionada";
	
	/**
	 * Mensajes de Catalogos Tipos de Ambientes
	 */
	public static final String ACTIVACION_TIPO_AMBIENTE	  		= "El ambiente ha sido activado exitosamente";
	public static final String ERROR_ACTIVACION_TIPO_AMBIENTE	= "Ocurrió un error al activar el ambiente";
	
	public static final String DESACTIVACION_TIPO_AMBIENTE	  	= "El ambiente ha sido desactivado exitosamente";
	public static final String ERROR_DESACTIVAR_TIPO_AMBIENTE	= "Ocurrió un error al desactivar el ambiente";
	
	public static final String REGISTRO_NUEVO_TIPO_AMBIENTE	  		= "El nuevo ambiente ha sido registrado exitosamente";
	public static final String ERROR_REGISTRO_NUEVO_TIPO_AMBIENTE	= "Ocurrió un error al registrar el ambiente";
	
	public static final String UPDATE_NUEVO_TIPO_AMBIENTE	  	= "El ambiente ha sido actualizado exitosamente";
	public static final String ERROR_UPDATE_NUEVO_TIPO_AMBIENTE	= "Ocurrió un error al actualizar el ambiente";
	
	public static final String DELETE_NUEVO_TIPO_AMBIENTE	  	= "El ambiente ha sido eliminado exitosamente";
	public static final String ERROR_DELETE_NUEVO_TIPO_AMBIENTE	= "Ocurrió un error al eliminar el ambiente";
	
	/**
	 * Mensajes de catalogos de Direcciones 
	 */
	public static final String REGISTRO_NUEVA_DIRECCION_EXITO	= "La nueva dirección ha sido registrada exitosamente";
	public static final String REGISTRO_NUEVA_DIRECCION_ERROR	= "Ocurrió un error al intentar registrar la nueva dirección";
	
	public static final String ACTUALIZACION_DIRECCION_EXITO	= "Se ha actualzado exitosamente la dirección seleccionada";
	public static final String ACTUALIZACION_DIRECCION_ERROR	= "Ocurrió un error al actualizar la dirección seleccionada";

	public static final String ACTIVA_DIRECCION_EXITO			= "La dirección ha sido activada satisfactoriamente";
	public static final String ACTIVA_DIRECCION_ERROR			= "Ocurrió un error al activar la dirección seleccionada";

	public static final String DESACTIVA_DIRECCION_EXITO		= "La dirección ha sido desactivada satisfactoriamente";
	public static final String DESACTIVA_DIRECCION_ERROR		= "Ocurrió un error al desactivar la dirección seleccionada";
	
	/**
	 * Mensajes de catalogos de Gerencias
	 */
	public static final String REGISTRO_NUEVA_GERENCIA_EXITO	= "La nueva gerencia ha sido registrada exitosamente";
	public static final String REGISTRO_NUEVA_GERENCIA_ERROR	= "Ocurrió un error al intentar registrar la nueva gerencia";
	
	public static final String ACTUALIZACION_GERENCIA_EXITO		= "Se ha actualzado exitosamente la gerencia seleccionada";
	public static final String ACTUALIZACION_GERENCIA_ERROR		= "Ocurrió un error al actualizar la gerencia seleccionada";

	public static final String ACTIVA_GERENCIA_EXITO			= "La gerencia ha sido activada satisfactoriamente";
	public static final String ACTIVA_GERENCIA_ERROR			= "Ocurrió un error al activar la gerencia seleccionada";

	public static final String DESACTIVA_GERENCIA_EXITO			= "La gerencia ha sido desactivada satisfactoriamente";
	public static final String DESACTIVA_GERENCIA_ERROR			= "Ocurrió un error al desactivar la gerencia seleccionada";
	
	/**
	 * Mensajes de Usuarios
	 */
	public static final String REGISTRO_USUARIO_EXITO			= "El usuario ingresado ha sido registrado exitosamente";
	public static final String REGISTRO_USUARIO_ERROR			= "Ocurrió un error al intentar registrar al nuevo usuario";
	
	public static final String ACTUALIZACION_USUARIO_EXITO		= "El usuario ha sido actualizado exitosamente";
	public static final String ACTUALIZACION_USUARIO_ERROR		= "Ocurrió un error al intentar actualizar al usuario ";
	
	public static final String ACTIVACION_USUARIO_EXITO			= "El usuario ha sido activado exitosamente";
	public static final String ACTIVACION_USUARIO_ERROR			= "Ocurrió un error al intentar activar al usuario ";
	
	public static final String DESACTIVACION_USUARIO_EXITO		= "El usuario ha sido desactivado exitosamente";
	public static final String DESACTIVACION_USUARIO_ERROR		= "Ocurrió un error al intentar desactivar al usuario ";
	
	/**
	 * Porcentajes de avance
	 */
	public static final String PORCENTAJES_VACIOS				= "No se puede dejar en 0 los porcentajes de avance señalados";
	public static final String F60_MENOR_QUE_PE					= "El porcentaje de avance del F60 no puede ser menor que el portafolio empresarial";
	public static final String INFRA_MENOR_QUE_F60				= "El porcentaje de avance de Infraestructura no puede ser menor que el F60";
	public static final String APLICATIVO_MENOR_QUE_INFRA		= "El porcentaje de avance del Aplicativo no puede ser menor que Infraestructura";
	public static final String PRE_ATP_MENOR_QUE_APLICATIVO		= "El porcentaje de avance del Pre ATP no puede ser menor que el aplicativo";
	public static final String ATP_MENOR_QUE_PRE_ATP			= "El porcentaje de avance del ATP no puede ser menor que el Pre ATP";
	public static final String RTO_MENOR_QUE_ATP				= "El porcentaje de avance del RTO no puede ser menor que el ATP";

	public static final String PORCENTAJES_ACTUALIZADOS_EXITO	= "Los Porcentajes se han guardado exitosamente";
	public static final String PORCENTAJES_ACTUALIZADOS_ERROR	= "Ha ocurrido un error interno al intentar guardar los porcentajes";
	

	/**
	 * Cancelación - Activación - Eliminación
	 */

	public static final String CANCELACION_PROYECTO_EXITO		= "El proyecto ha sido cancelado exitosamente";
	public static final String CANCELACION_PROYECTO_ERROR		= "Ocurrió un error al cancelar el proyecto";
	public static final String COMENTARIO_VACIO					= "Para hacer registro de la cancelación del proyecto es necesario que agregue un motivo de cancelación";
	public static final String ACTIVACION_PROYECTO_EXITO		= "El proyecto ha sido activado exitosamente";
	public static final String ACTIVACION_PROYECTO_ERROR		= "Ocurrió un error al activar el proyecto";
	public static final String ELIMINACION_PROYECTO_EXITO		= "El proyecto ha sido eliminado exitosamente";
	public static final String ELIMINACION_PROYECTO_ERROR		= "Ocurrió un error al eliminar el proyecto";
}
