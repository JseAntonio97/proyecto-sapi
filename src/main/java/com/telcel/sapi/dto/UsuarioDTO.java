package com.telcel.sapi.dto;

public class UsuarioDTO {
	
	private Long 	idUsuario;
	private String	nombre;
	private String	primerApellido;
	private	String	segundoApellido;
	private String	nombreUsuario;
	private String	password;
	private String	passwordAnterior;
	private String	correo;
	private String	estatus;
	private UsuarioDTO	lider;
	private RolDTO		rol;
	private PermisosDTO permisos;
	
	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(Long idUsuario, String nombre, String primerApellido, String segundoApellido,
			String nombreUsuario, String password, String passwordAnterior, String correo, String estatus,
			UsuarioDTO lider, RolDTO rol, PermisosDTO permisos) {
		super();
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.passwordAnterior = passwordAnterior;
		this.correo = correo;
		this.estatus = estatus;
		this.lider = lider;
		this.rol = rol;
		this.permisos = permisos;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordAnterior() {
		return passwordAnterior;
	}

	public void setPasswordAnterior(String passwordAnterior) {
		this.passwordAnterior = passwordAnterior;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public UsuarioDTO getLider() {
		return lider;
	}

	public void setLider(UsuarioDTO lider) {
		this.lider = lider;
	}

	public RolDTO getRol() {
		return rol;
	}

	public void setRol(RolDTO rol) {
		this.rol = rol;
	}

	public PermisosDTO getPermisos() {
		return permisos;
	}

	public void setPermisos(PermisosDTO permisos) {
		this.permisos = permisos;
	}

	@Override
	public String toString() {
		return "UsuarioDTO [idUsuario=" + idUsuario + ", nombre=" + nombre + ", primerApellido=" + primerApellido
				+ ", segundoApellido=" + segundoApellido + ", nombreUsuario=" + nombreUsuario + ", password=" + password
				+ ", passwordAnterior=" + passwordAnterior + ", correo=" + correo + ", estatus=" + estatus + ", lider="
				+ lider + ", rol=" + rol + ", permisos=" + permisos + "]";
	}
}
