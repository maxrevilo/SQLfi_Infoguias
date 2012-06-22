--
-- Table structure for table `categorias`
--

CREATE TABLE IF NOT EXISTS `categorias` (
  `codigoseccion` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `codigocategoria` smallint(5) unsigned NOT NULL DEFAULT '0',
  `nombrecategoria` varchar(90) DEFAULT NULL,
  `totalempresas` smallint(5) unsigned NOT NULL DEFAULT '0',
  `secciones_codigoseccion` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`secciones_codigoseccion`,`codigocategoria`),
  KEY `fk_categorias_secciones1` (`secciones_codigoseccion`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `ciudades`
--

CREATE TABLE IF NOT EXISTS `ciudades` (
  `codigociudad` smallint(5) unsigned NOT NULL DEFAULT '0',
  `codigotlf` varchar(4) DEFAULT NULL,
  `nombreciudad` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`codigociudad`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ciudad_estado`
--

CREATE TABLE IF NOT EXISTS `ciudad_estado` (
  `codigociudad` smallint(5) unsigned NOT NULL DEFAULT '0',
  `capital` char(1) NOT NULL DEFAULT 'P',
  `codigoestado` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `estados_codigoestado` tinyint(3) unsigned NOT NULL,
  `ciudades_codigociudad` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ciudades_codigociudad`,`estados_codigoestado`),
  KEY `fk_ciudad_estado_ciudades1` (`ciudades_codigociudad`),
  KEY `fk_ciudad_estado_estados1` (`estados_codigoestado`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataempresas`
--

CREATE TABLE IF NOT EXISTS `dataempresas` (
  `codigoempresa` int(10) unsigned NOT NULL DEFAULT '0',
  `codigosucursal` int(10) unsigned NOT NULL DEFAULT '0',
  `codigoestado` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `codigociudad` smallint(5) unsigned NOT NULL DEFAULT '0',
  `codigourbanizacion` smallint(5) unsigned NOT NULL DEFAULT '0',
  `direccion` varchar(140) DEFAULT NULL,
  `urbanizacion` varchar(60) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `fax` varchar(25) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `empresas_codigoempresa` int(10) unsigned NOT NULL,
  `estados_codigoestado` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`empresas_codigoempresa`,`estados_codigoestado`),
  KEY `fk_dataempresas_estados1` (`estados_codigoestado`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------


--
-- Table structure for table `distancias_ciudades`
--

CREATE TABLE IF NOT EXISTS `distancias_ciudades` (
  `ciudad1` varchar(30) NOT NULL DEFAULT '',
  `ciudad2` varchar(30) NOT NULL DEFAULT '',
  `distancia_ciudades` int(3) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `empresas`
--

CREATE TABLE IF NOT EXISTS `empresas` (
  `codigoempresa` int(10) unsigned NOT NULL DEFAULT '0',
  `rif` varchar(12) DEFAULT NULL,
  `web` varchar(80) DEFAULT NULL,
  `nombreempresa` varchar(80) DEFAULT NULL,
  `descripcionempresa` text,
  `infoadicional` text,
  PRIMARY KEY (`codigoempresa`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `estados`
--

CREATE TABLE IF NOT EXISTS `estados` (
  `codigoestado` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `nombreestado` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`codigoestado`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `paginacioncategorias`
--

CREATE TABLE IF NOT EXISTS `paginacioncategorias` (
  `codigoempresa` int(10) unsigned NOT NULL DEFAULT '0',
  `codigocategoria` int(10) unsigned NOT NULL DEFAULT '0',
  `codigoseccion` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `codigoestado` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `codigociudad` smallint(5) unsigned NOT NULL DEFAULT '0',
  `tipoaviso` varchar(45) NOT NULL DEFAULT 'R',
  `nombreaviso` varchar(12) DEFAULT NULL,
  `eslogan` varchar(50) DEFAULT NULL,
  `numeropagina` int(10) unsigned NOT NULL DEFAULT '0',
  `ordenpagina` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `ordenciudad` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `ordenestado` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `empresas_codigoempresa` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`empresas_codigoempresa`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `secciones`
--

CREATE TABLE IF NOT EXISTS `secciones` (
  `codigoseccion` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `nombreseccion` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`codigoseccion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `urbanizaciones`
--

CREATE TABLE IF NOT EXISTS `urbanizaciones` (
  `codigourb` smallint(5) unsigned NOT NULL DEFAULT '0',
  `codigociudad` smallint(5) unsigned NOT NULL DEFAULT '0',
  `nombreurb` varchar(40) DEFAULT NULL,
  `ciudades_codigociudad` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ciudades_codigociudad`,`codigourb`),
  KEY `fk_urbanizaciones_ciudades1` (`ciudades_codigociudad`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `categorias`
--
ALTER TABLE `categorias`
  ADD CONSTRAINT `fk_categorias_secciones1` FOREIGN KEY (`secciones_codigoseccion`) REFERENCES `secciones` (`codigoseccion`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `ciudad_estado`
--
ALTER TABLE `ciudad_estado`
  ADD CONSTRAINT `fk_ciudad_estado_ciudades1` FOREIGN KEY (`ciudades_codigociudad`) REFERENCES `ciudades` (`codigociudad`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_ciudad_estado_estados1` FOREIGN KEY (`estados_codigoestado`) REFERENCES `estados` (`codigoestado`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `dataempresas`
--
ALTER TABLE `dataempresas`
  ADD CONSTRAINT `fk_dataempresas_empresas` FOREIGN KEY (`empresas_codigoempresa`) REFERENCES `empresas` (`codigoempresa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_dataempresas_estados1` FOREIGN KEY (`estados_codigoestado`) REFERENCES `estados` (`codigoestado`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `paginacioncategorias`
--
ALTER TABLE `paginacioncategorias`
  ADD CONSTRAINT `fk_paginacioncategorias_empresas1` FOREIGN KEY (`empresas_codigoempresa`) REFERENCES `empresas` (`codigoempresa`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `urbanizaciones`
--
ALTER TABLE `urbanizaciones`
  ADD CONSTRAINT `fk_urbanizaciones_ciudades1` FOREIGN KEY (`ciudades_codigociudad`) REFERENCES `ciudades` (`codigociudad`) ON DELETE NO ACTION ON UPDATE NO ACTION;
