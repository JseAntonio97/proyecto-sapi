-- -----------------------------------------------------
-- Schema SAPIDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SAPIDB` DEFAULT CHARACTER SET utf8 ;
USE `SAPIDB` ;

-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_ESTRATEGIA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_ESTRATEGIA` (
  `idEstrategia` INT NOT NULL AUTO_INCREMENT,
  `estrategia` VARCHAR(60) NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idEstrategia`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_DIRECCION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_DIRECCION` (
  `idDireccion` INT NOT NULL AUTO_INCREMENT,
  `direccion` VARCHAR(65) NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idDireccion`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_GERENCIA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_GERENCIA` (
  `idGerencia` INT NOT NULL AUTO_INCREMENT,
  `gerencia` VARCHAR(70) NULL,
  `idDireccion` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idGerencia`),
  INDEX `fk_CAT_GERENCIA_CAT_DIRECCION1_idx` (`idDireccion` ASC) VISIBLE,
  CONSTRAINT `fk_CAT_GERENCIA_CAT_DIRECCION1`
    FOREIGN KEY (`idDireccion`)
    REFERENCES `SAPIDB`.`CAT_DIRECCION` (`idDireccion`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_PROYECTO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_PROYECTO` (
  `idproyecto` INT NOT NULL AUTO_INCREMENT,
  `identificadorInterno` VARCHAR(30) NULL,
  `integrador` VARCHAR(150) NULL,
  `nombreProyecto` VARCHAR(150) NULL,
  `descripcion` VARCHAR(500) NULL,
  `solicitante` VARCHAR(50) NULL,
  `direccion` VARCHAR(45) NULL,
  `gerencia` VARCHAR(80) NULL,
  `idGerencia` INT NULL,
  `porcAvanceActual` INT NULL,
  `porcAvanceAnterior` INT NULL,
  `faseActual` VARCHAR(50) NULL,
  `faseAnterior` VARCHAR(50) NULL,
  `comentariosGrls` VARCHAR(500) NULL,
  `estatus` VARCHAR(30) NULL,
  `fechaRegistro` DATE NULL,
  `horaRegistro` TIME NULL,
  `leido` INT NULL,
  `idEstrategia` INT NULL,
  PRIMARY KEY (`idproyecto`),
  INDEX `fk_TT_PROYECTO_CAT_ESTRATEGIA1_idx` (`idEstrategia` ASC) VISIBLE,
  INDEX `fk_TT_PROYECTO_CAT_GERENCIA1_idx` (`idGerencia` ASC) VISIBLE,
  CONSTRAINT `fk_TT_PROYECTO_CAT_ESTRATEGIA1`
    FOREIGN KEY (`idEstrategia`)
    REFERENCES `SAPIDB`.`CAT_ESTRATEGIA` (`idEstrategia`),
  CONSTRAINT `fk_TT_PROYECTO_CAT_GERENCIA1`
    FOREIGN KEY (`idGerencia`)
    REFERENCES `SAPIDB`.`CAT_GERENCIA` (`idGerencia`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_TIPO_AMBIENTE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_TIPO_AMBIENTE` (
  `idTipoAmbiente` INT NOT NULL AUTO_INCREMENT,
  `tipoAmbiente` VARCHAR(50) NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idTipoAmbiente`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_AMBIENTE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_AMBIENTE` (
  `idAmbiente` INT NOT NULL AUTO_INCREMENT,
  `idTipoAmbiente` INT NULL,
  `ambiente` VARCHAR(45) NULL,
  `cpu` INT NULL,
  `memoria` INT NULL,
  `discoDuro` INT NULL,
  `unidadMedidaDD` VARCHAR(20) NULL COMMENT 'Unidad de medida para el almacenamiento del disco duro',
  `sistemaOperativo` VARCHAR(45) NULL,
  `baseDatos` VARCHAR(45) NULL,
  `hostname` VARCHAR(50) NULL,
  `ip` VARCHAR(30) NULL,
  `entregaEquipo` VARCHAR(45) NULL,
  `idproyecto` INT NOT NULL,
  PRIMARY KEY (`idAmbiente`),
  INDEX `fk_TT_AMBIENTE_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  INDEX `fk_TT_AMBIENTE_CAT_TIPO_AMBIENTE1_idx` (`idTipoAmbiente` ASC) VISIBLE,
  CONSTRAINT `fk_TT_AMBIENTE_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`),
  CONSTRAINT `fk_TT_AMBIENTE_CAT_TIPO_AMBIENTE1`
    FOREIGN KEY (`idTipoAmbiente`)
    REFERENCES `SAPIDB`.`CAT_TIPO_AMBIENTE` (`idTipoAmbiente`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_RESPONSABLE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_RESPONSABLE` (
  `idResponsable` INT NOT NULL AUTO_INCREMENT,
  `nomenclatura` VARCHAR(15) NULL,
  `area` VARCHAR(120) NULL,
  `responsable` VARCHAR(55) NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idResponsable`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_INCIDENTE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_INCIDENTE` (
  `idIncidente` INT NOT NULL AUTO_INCREMENT,
  `issueIncidente` VARCHAR(50) NULL,
  `idResponsable` INT NOT NULL,
  `fechaInicio` DATE NULL,
  `fechaSolucion` DATE NULL,
  `detalles` VARCHAR(200) NULL,
  `etapa` VARCHAR(25) NULL,
  `idproyecto` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idIncidente`),
  INDEX `fk_TT_INCIDENTE_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  INDEX `fk_TT_INCIDENTE_CAT_RESPONSABLE1_idx` (`idResponsable` ASC) VISIBLE,
  CONSTRAINT `fk_TT_INCIDENTE_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`),
  CONSTRAINT `fk_TT_INCIDENTE_CAT_RESPONSABLE1`
    FOREIGN KEY (`idResponsable`)
    REFERENCES `SAPIDB`.`CAT_RESPONSABLE` (`idResponsable`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_ROL`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_ROL` (
  `idRol` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `descripcion` VARCHAR(80) NULL,
  PRIMARY KEY (`idRol`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_PERMISOS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_PERMISOS` (
  `idPermiso` INT NOT NULL AUTO_INCREMENT,
  `registroProyecto` INT NULL,
  `asignacion` INT NULL,
  `infraestructura` INT NULL,
  `seguimiento` INT NULL,
  `funcionesOperativas` INT NULL,
  `reportes` INT NULL,
  PRIMARY KEY (`idPermiso`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_USUARIO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_USUARIO` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(30) NULL,
  `primerApellido` VARCHAR(20) NULL,
  `segundoApellido` VARCHAR(20) NULL,
  `nombreUsuario` VARCHAR(30) NULL,
  `password` VARCHAR(25) NULL,
  `passwordantes` VARCHAR(25) NULL,
  `correo` VARCHAR(60) NULL,
  `idRol` INT NOT NULL,
  `idLider` INT NULL,
  `idPermiso` INT NOT NULL,
  PRIMARY KEY (`idUsuario`),
  INDEX `fk_TT_USUARIO_CAT_ROL1_idx` (`idRol` ASC) VISIBLE,
  INDEX `fk_TT_USUARIO_TT_USUARIO1_idx` (`idLider` ASC) VISIBLE,
  INDEX `fk_TT_USUARIO_TT_PERMISOS1_idx` (`idPermiso` ASC) VISIBLE,
  CONSTRAINT `fk_TT_USUARIO_CAT_ROL1`
    FOREIGN KEY (`idRol`)
    REFERENCES `SAPIDB`.`CAT_ROL` (`idRol`),
  CONSTRAINT `fk_TT_USUARIO_TT_USUARIO1`
    FOREIGN KEY (`idLider`)
    REFERENCES `SAPIDB`.`TT_USUARIO` (`idUsuario`),
  CONSTRAINT `fk_TT_USUARIO_TT_PERMISOS1`
    FOREIGN KEY (`idPermiso`)
    REFERENCES `SAPIDB`.`TT_PERMISOS` (`idPermiso`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_BITACORA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_BITACORA` (
  `idBitacora` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `actividad` VARCHAR(60) NULL,
  `fechaHra` DATETIME NULL,
  `descripcion` VARCHAR(500) NULL,
  `modulo` VARCHAR(55) NULL,
  PRIMARY KEY (`idBitacora`),
  INDEX `fk_TT_BITACORA_TT_USUARIO1_idx` (`idUsuario` ASC) VISIBLE,
  CONSTRAINT `fk_TT_BITACORA_TT_USUARIO1`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `SAPIDB`.`TT_USUARIO` (`idUsuario`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_SEGUIMIENTO_PE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_SEGUIMIENTO_PE` (
  `idSeguimientoPE` INT NOT NULL AUTO_INCREMENT,
  `aplica` VARCHAR(45) NULL,
  `folio` VARCHAR(45) NULL,
  `entrada` DATE NULL,
  `salida` DATE NULL,
  `idproyecto` INT NOT NULL,
  PRIMARY KEY (`idSeguimientoPE`),
  INDEX `fk_TT_SEGUIMIENTO_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_SEGUIMIENTO_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_SEGUIMIENTO_INFRA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_SEGUIMIENTO_INFRA` (
  `idInfraestructura` INT NOT NULL AUTO_INCREMENT,
  `kickOff` DATE NULL,
  `envio` DATE NULL,
  `fechaCompromisoInfra` DATE NULL,
  `fechaEntregaUsr` DATE NULL,
  `idproyecto` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idInfraestructura`),
  INDEX `fk_TT_PROYECTO_INFRA_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_PROYECTO_INFRA_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_PROYECTO_F60`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_PROYECTO_F60` (
  `idProyectoF60` INT NOT NULL AUTO_INCREMENT,
  `fechaInicio` DATE NULL,
  `fechaCompromiso` DATE NULL,
  `fechaFin` DATE NULL,
  `idproyecto` INT NOT NULL,
  `cuentaConInfraestructura` INT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idProyectoF60`),
  INDEX `fk_TT_PROYECTO_F60_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_PROYECTO_F60_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_PRE_ATP`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_PRE_ATP` (
  `idPreAtp` INT NOT NULL AUTO_INCREMENT,
  `fechaInicio` DATE NULL,
  `fechaCompromiso` DATE NULL,
  `fechaFin` DATE NULL,
  `idproyecto` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idPreAtp`),
  INDEX `fk_TT_PRE_ATP_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_PRE_ATP_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_SEGUIMIENTO_ATP`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_SEGUIMIENTO_ATP` (
  `idSeguimientoATP` INT NOT NULL AUTO_INCREMENT,
  `fechaInicio` DATE NULL,
  `fechaCompromiso` DATE NULL,
  `fechaFin` DATE NULL,
  `idproyecto` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idSeguimientoATP`),
  INDEX `fk_TT_SEGUIMIENTO_ATP_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_SEGUIMIENTO_ATP_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_SEGUIMIENTO_RTO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_SEGUIMIENTO_RTO` (
  `idSeguimientoRto` INT NOT NULL AUTO_INCREMENT,
  `fechaInicio` DATE NULL,
  `fechaCompromiso` DATE NULL,
  `fechaFin` DATE NULL,
  `idproyecto` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idSeguimientoRto`),
  INDEX `fk_TT_SEGUMIENTO_RTO_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_SEGUMIENTO_RTO_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_ASIGNACION`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_ASIGNACION` (
  `idAsignacion` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `idproyecto` INT NOT NULL,
  `fecAsignacion` DATE NULL,
  `TipoAsignacion` VARCHAR(45) NULL,
  INDEX `fk_TT_ASIGNACION_TT_USUARIO1_idx` (`idUsuario` ASC) VISIBLE,
  INDEX `fk_TT_ASIGNACION_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  PRIMARY KEY (`idAsignacion`),
  CONSTRAINT `fk_TT_ASIGNACION_TT_USUARIO1`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `SAPIDB`.`TT_USUARIO` (`idUsuario`),
  CONSTRAINT `fk_TT_ASIGNACION_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`TT_APLICATIVO`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`TT_APLICATIVO` (
  `idAplicativo` INT NOT NULL AUTO_INCREMENT,
  `fechaDespliegue` DATE NULL,
  `fechaFinPruebas` DATE NULL,
  `fechaNotificacion` DATE NULL,
  `idproyecto` INT NOT NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idAplicativo`),
  INDEX `fk_TT_APLICATIVO_TT_PROYECTO1_idx` (`idproyecto` ASC) VISIBLE,
  CONSTRAINT `fk_TT_APLICATIVO_TT_PROYECTO1`
    FOREIGN KEY (`idproyecto`)
    REFERENCES `SAPIDB`.`TT_PROYECTO` (`idproyecto`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_SLA`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_SLA` (
  `idSLA` INT NOT NULL,
  `slaF60` INT NULL,
  `slaInfra` INT NULL,
  `slaAplicativo` INT NULL,
  `slaPreAtp` INT NULL,
  `slaAtp` INT NULL,
  `slaRto` INT NULL,
  PRIMARY KEY (`idSLA`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_PORCENTAJE_AVANCE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_PORCENTAJE_AVANCE` (
  `idPorAvance` INT NOT NULL AUTO_INCREMENT,
  `porF60` INT NULL,
  `porInfra` INT NULL,
  `porAplicativo` INT NULL,
  `porPreAtp` INT NULL,
  `porAtp` INT NULL,
  `porRto` INT NULL,
  PRIMARY KEY (`idPorAvance`));

-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_JEFE_F60`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_JEFE_F60` (
  `idJefeF60` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(75) NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idJefeF60`));


-- -----------------------------------------------------
-- Table `SAPIDB`.`CAT_TIPO_F60`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAPIDB`.`CAT_TIPO_F60` (
  `idTipoF60` INT NOT NULL AUTO_INCREMENT,
  `tipoF60` VARCHAR(45) NULL,
  `estatus` VARCHAR(15) NULL,
  PRIMARY KEY (`idTipoF60`));

INSERT INTO CAT_ROL (nombre,descripcion) VALUES ('admin', 'Puede asignar y dar seguimiento a los proyectos');
INSERT INTO CAT_ROL (nombre,descripcion) VALUES ('colaborador', 'Da seguimiento a los proyectos');
INSERT INTO CAT_ROL (NOMBRE, DESCRIPCION) VALUE ('DESARROLLADOR',  'Desarrollo y pruebas del sistema');
	
/*PREMISO PARA SUPER-ADMIN*/
INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES) VALUES (1, 1, 1, 1, 1, 1);
/*PREMISO PARA HUGO*//*PREMISO PARA HECTOR*/
INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES) VALUES (0, 1, 0, 0, 1, 1);
/*PREMISO PARA ANA*//*PREMISO PARA ODISEO*/
INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES) VALUES (1, 1, 1, 1, 0, 1);
/*PREMISO PARA MIGUEL*//*PREMISO PARA SALVADOR*/
INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES) VALUES (0, 1, 1, 1, 0, 1);
/*PREMISO PARA Luis, Abraham, Ricardo*/
INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES) VALUES (0, 0, 1, 1, 0, 1);
/*PREMISO PARA Javier*/
INSERT INTO TT_PERMISOS (REGISTROPROYECTO, ASIGNACION, INFRAESTRUCTURA, SEGUIMIENTO, FUNCIONESOPERATIVAS, REPORTES) VALUES (0, 1, 0, 0, 1, 1);

INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idPermiso) 
				VALUES ('Hugo', 'Caballero', 'Ramirez', 'VI300AA', '123456', 'hcaball@telcel.com', 1, 2);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider, idPermiso) 
				VALUES ('Hector', 'Morales', 'Bobadilla', 'VI312AB', '123456', 'hmorale@telcel.com', 1, 1, 2);                
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Ana Lizbeth', 'Gutierrez ', 'Perez', 'VI36765', '123456', 'ana.gutierrez@mail.telcel.com', 1, 2, 3);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Miguel Angel', 'De la Rosa', 'Moron', 'VI35A6E', '123456', 'miguel.delarosa@telcel.com', 1, 2, 4);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Salvador', 'Garcia', 'Chavez', 'VI3BD48', '123456', 'salvador.garciachavez@mail.telcel.com', 1, 2, 4);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Odiseo', 'Aguilera', 'Hernández', 'VI3EF47', '123456', null, 1, 3, 3);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Luis Angel', 'Santos', 'Luciano', 'VI3G00R', '123456', null, 1, 4, 5);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Abraham', 'Avilés', 'Martinez', 'VI310A51', '123456', null, 1, 4, 5);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Ricardo', 'Gonzalez', 'Ortiz', 'VI334BE', '123456', null, 1, 4, 5);
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idLider,idPermiso) 
				VALUES ('Gabriel', 'Aguirre', 'Castro', 'VDIG003', '123456', null, 2, 5, 5);
                
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idPermiso) 
				VALUES ('Christian Brandon', 'Neri', 'Sanchez', 'VI3G00G', '123456', 'christian.neri@telcel.com', 1, 1);                
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idPermiso) 
				VALUES ('Alan Hermir', 'Contreras', 'Mondragon', 'VI3G00Q', '123456', 'alan.contrerasm@telcel.com', 1, 1);            
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idPermiso) 
				VALUES ('Jose Antonio', 'Jimenez', 'Enriquez', 'VI3G00H', '123456', 'jose.enriquez@mail.telcel.com', 1, 1);         
INSERT INTO TT_USUARIO (nombre, primerApellido, segundoApellido, nombreUsuario, password, correo, idRol, idPermiso) 
				VALUES ('Javier', '', '', 'VDI0A6B', '123456', '', 1, 1);

	
INSERT INTO CAT_TIPO_AMBIENTE (TIPOAMBIENTE, ESTATUS) VALUES 
('Desarrollo', 'ACTIVO'),
('QA', 'ACTIVO'),
('Producción', 'ACTIVO');
				
INSERT INTO CAT_ESTRATEGIA (ESTRATEGIA, ESTATUS) VALUES  
('Criticos Ola 1', 'ACTIVO'), 
('Criticos Ola 2', 'ACTIVO'), 
('Criticos Ola 3', 'ACTIVO'), 
('prioritarios ola 2', 'ACTIVO'), 
('prioritarios ola 3', 'ACTIVO'), 
('General', 'ACTIVO'), 
('ORACLE-IBM', 'ACTIVO');

INSERT INTO CAT_RESPONSABLE (NOMENCLATURA, AREA, RESPONSABLE, ESTATUS) VALUES 
('USR', 'Usuario', '', 'ACTIVO'), 
('PE', 'Portafolio Empresarial', 'Ricardo Bogar', 'ACTIVO'), 
('IPS', 'Integración de Plataformas de Ingeniería / Integración de Plataformas SVA/RI (Integración de Plataformas y Servicios)', 'Sandra Luz Gonzalez Carmona / Iván Noe Alvarado', 'ACTIVO'), 
('NII', 'Normatividad e Integración de Infraestructura', 'Alejandro Luviano Mendoza/ Hugo Maldonado', 'ACTIVO'), 
('RO&M', 'O&M Recepción Sistemas Virtualizados Plataformas Abiertas', 'Hugo Trejo', 'ACTIVO'), 
('SIT', 'Soporte e Integración Tecnológica', 'Hector Morales / Miguel de la Rosa', 'ACTIVO'), 
('O&M', 'Operación y Mantenimiento', 'Guardias O&M', 'ACTIVO'), 
('ID', 'Infraestructura Digital', 'Rodrigo Lopez Martínez', 'ACTIVO'), 
('SSI', 'Seguridad de Sistemas e Infraestructura', '', 'ACTIVO');

INSERT INTO CAT_DIRECCION (DIRECCION, ESTATUS) VALUES 
	('Informática', 'ACTIVO'), 
    ('Operaciones Región 9', 'ACTIVO'), 
    ('Dir Regional Centro', 'ACTIVO'), 
    ('Dir Regional Baja California', 'ACTIVO');
    
INSERT INTO CAT_GERENCIA (GERENCIA, IDDIRECCION, ESTATUS) VALUES 
	('Gcia Aplicaciones Móviles', 1, 'ACTIVO'),
	('Gcia Arquitectura de Procesos y Proyectos TI', 1, 'ACTIVO'),
	('Gcia Arquitectura e Ing de Infraestructura Servicios', 1, 'ACTIVO'),
	('Gcia Desarrollo de Sistemas', 1, 'ACTIVO'),
	('Gcia Desarrollo e Ing de Servicios Online/Offline', 1, 'ACTIVO'),
	('Gcia Diseño Ing y Desarrollo de Mediación de Servicios', 1, 'ACTIVO'),
	('Gcia Identidad Digital y Operaciones BES', 1, 'ACTIVO'),
	('Gcia Integración e Innovación TI', 1, 'ACTIVO'),
	('Gcia Lógica de Negocios y Diseño de Servicios', 1, 'ACTIVO'),
	('Gcia Producción y Operación de Sistemas', 1, 'ACTIVO'),
	('Gcia SERTEC', 1, 'ACTIVO'),
	('Gcia Sistema de Facturación Interconexión', 1, 'ACTIVO'),
	('Gcia Sistemas Administrativos', 1, 'ACTIVO'),
	('Gcia Sistemas AM', 1, 'ACTIVO'),
	('Gcia Sistemas CRM', 1, 'ACTIVO'),
	('Gcia Sistemas de Activaciones', 1, 'ACTIVO'),
	('Gcia Sistemas de Comercio Electrónico', 1, 'ACTIVO'),
	('Gcia Sistemas de Recursos Humanos', 1, 'ACTIVO'),
	('Gcia Sistemas Posventa', 1, 'ACTIVO'),
	('Gcia Soporte Técnico a Sistemas de Facturación', 1, 'ACTIVO'),
	('Gcia Soporte y Admon MOBILE 2000', 1, 'ACTIVO'),
	('Gcia Sistemas R9', 2, 'ACTIVO'),
	('Dir Regional Centro', 3, 'ACTIVO'),
	('Dir Regional Baja California', 4, 'ACTIVO');
