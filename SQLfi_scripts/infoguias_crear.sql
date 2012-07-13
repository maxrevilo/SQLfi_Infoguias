-- --------------------------------------------------------

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=63 ;


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
  PRIMARY KEY (`codigociudad`,`codigoestado`),
  KEY `fk_ciudad_estado_ciudades1` (`codigociudad`),
  KEY `fk_ciudad_estado_estados1` (`codigoestado`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `dataempresas`
--

CREATE TABLE IF NOT EXISTS `dataempresas` (
  `codigoempresa` int(10) unsigned NOT NULL,
  `codigosucursal` int(10) unsigned NOT NULL,
  `codigoestado` tinyint(3) unsigned NOT NULL,
  `codigociudad` smallint(5) unsigned NOT NULL,
  `codigourbanizacion` smallint(5) unsigned NOT NULL DEFAULT '0',
  `direccion` varchar(140) DEFAULT NULL,
  `urbanizacion` varchar(60) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `fax` varchar(25) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `latitud` decimal(8,2) DEFAULT NULL,
  `longitud` decimal(8,2) DEFAULT NULL
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
-- Table structure for table `distancias_estados`
--

CREATE TABLE IF NOT EXISTS `distancias_estados` (
  `estado1` varchar(25) NOT NULL,
  `estado2` varchar(25) NOT NULL,
  `distancia_estados` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `distancias_urbanizaciones`
--

CREATE TABLE IF NOT EXISTS `distancias_urbanizaciones` (
  `urb1` varchar(30) NOT NULL DEFAULT '',
  `urb2` varchar(30) NOT NULL DEFAULT '',
  `distancia_urbanizaciones` int(3) DEFAULT '0'
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
  `codigoempresa` int(10) unsigned NOT NULL,
  `codigocategoria` smallint(5) unsigned NOT NULL,
  `codigoseccion` tinyint(3) unsigned NOT NULL,
  `codigoestado` tinyint(3) unsigned NOT NULL,
  `codigociudad` smallint(5) unsigned NOT NULL,
  `tipoaviso` varchar(45) NOT NULL,
  `nombreaviso` varchar(12) DEFAULT NULL,
  `eslogan` varchar(50) DEFAULT NULL,
  `numeropagina` int(10) unsigned NOT NULL,
  `ordenpagina` tinyint(3) unsigned NOT NULL,
  `ordenciudad` tinyint(3) unsigned NOT NULL,
  `ordenestado` tinyint(3) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


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
  PRIMARY KEY (`codigociudad`,`codigourb`),
  KEY `fk_urbanizaciones_ciudades1` (`codigociudad`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;