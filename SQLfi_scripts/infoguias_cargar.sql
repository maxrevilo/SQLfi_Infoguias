--
-- Dumping data for table `secciones`
--

INSERT INTO `secciones` (`codigoseccion`, `nombreseccion`) VALUES
(1, 'seccion1'),
(2, 'seccion2');

--
-- Dumping data for table `categorias`
--

INSERT INTO `categorias` (`codigoseccion`, `codigocategoria`, `nombrecategoria`, `totalempresas`, `secciones_codigoseccion`) VALUES
(1, 1, 'Restaurante', 1, 1),
(1, 2, 'Arepera', 1, 1),
(3, 3, 'Zapateria', 1, 1);



--
-- Dumping data for table `estados`
--

INSERT INTO `estados` (`codigoestado`, `nombreestado`) VALUES
(1, 'DistritoCapital'),
(2, 'Aragua'),
(3, 'Zulia'),
(4, 'Lara');



--
-- Dumping data for table `ciudades`
--

INSERT INTO `ciudades` (`codigociudad`, `codigotlf`, `nombreciudad`) VALUES
(1, '0212', 'Caracas'),
(2, '0243', 'Maracay'),
(3, '0251', 'Barquisimeto'),
(4, NULL, 'Maracaibo');


--
-- Dumping data for table `ciudad_estado`
--

INSERT INTO `ciudad_estado` (`codigociudad`, `capital`, `codigoestado`, `estados_codigoestado`, `ciudades_codigociudad`) VALUES
(1, 'T', 1, 1, 1),
(2, 'T', 2, 2, 2);


--
-- Dumping data for table `distancias_ciudades`
--

INSERT INTO `distancias_ciudades` (`ciudad1`, `ciudad2`, `distancia_ciudades`) VALUES
('Caracas', 'Barquisimeto', 363),
('Caracas', 'Maracaibo', 706),
('Caracas', 'Maracay', 109),
('Maracay', 'Barquisimeto', 254),
('Maracay', 'Maracaibo', 597),
('Barquisimeto', 'Maracaibo', 322),
('Caracas', 'Barquisimeto', 363),
('Caracas', 'Maracaibo', 706),
('Caracas', 'Maracay', 109),
('Maracay', 'Barquisimeto', 254),
('Maracay', 'Maracaibo', 597),
('Barquisimeto', 'Maracaibo', 322);


--
-- Dumping data for table `empresas`
--

INSERT INTO `empresas` (`codigoempresa`, `rif`, `web`, `nombreempresa`, `descripcionempresa`, `infoadicional`) VALUES
(1, 'adasdas', NULL, 'Restaurante Rusio Moro', NULL, NULL),
(2, 'dssad', NULL, 'Arepera Socialista Negro Primero', NULL, NULL),
(3, 'adasdcc', NULL, 'Zapateria Hermanos Rojas', NULL, NULL);


--
-- Dumping data for table `paginacioncategorias`
--

INSERT INTO `paginacioncategorias` (`codigoempresa`, `codigocategoria`, `codigoseccion`, `codigoestado`, `codigociudad`, `tipoaviso`, `nombreaviso`, `eslogan`, `numeropagina`, `ordenpagina`, `ordenciudad`, `ordenestado`, `empresas_codigoempresa`) VALUES
(1, 1, 1, 1, 1, 'R', NULL, NULL, 0, 0, 0, 0, 1),
(2, 2, 2, 1, 1, 'R', NULL, NULL, 0, 0, 0, 0, 2),
(3, 3, 2, 3, 3, 'R', NULL, NULL, 0, 0, 0, 0, 3);