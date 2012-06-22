-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -		GRUPO		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE TABLE SQLfi_Grupo (
	Nombre VARCHAR(30) NOT NULL,
	PRIMARY KEY(Nombre)
);


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -		PERMISO		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE TABLE SQLfi_Permiso (
	Id INTEGER NOT NULL AUTO_INCREMENT,
	Accion VARCHAR(30) NOT NULL,
	Objeto VARCHAR(30) NOT NULL,
	UNIQUE (Accion,Objeto),
	PRIMARY KEY (Id) 	
);

CREATE TABLE SQLfi_Permiso_Grupo (
	Id_Permiso INTEGER NOT NULL,
	Grupo VARCHAR(30) NOT NULL,
	PRIMARY KEY(Id_Permiso,Grupo),
	FOREIGN KEY (Id_Permiso) 	
		  REFERENCES SQLfi_Permiso (Id) 
		  ON DELETE CASCADE,
	FOREIGN KEY (Grupo) 	
		  REFERENCES SQLfi_Grupo (Nombre) 
		  ON DELETE CASCADE
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -		Base de Datos   -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

CREATE TABLE SQLfi_Bd (
	Dns VARCHAR(100) NOT NULL,
	Ip VARCHAR(15) NOT NULL,
	NombreBd VARCHAR(30) NOT NULL,
	Puerto INTEGER(4) NOT NULL,
	PRIMARY KEY (Dns)
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -		USUARIO		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE TABLE SQLfi_Usuario (
	Usuario INTEGER NOT NULL AUTO_INCREMENT,
	Login VARCHAR(30) NOT NULL,
	Bd VARCHAR(100) NOT NULL,
	Grupo VARCHAR(30) NOT NULL,
	Clave VARCHAR(10) NOT NULL,
	PRIMARY KEY(Usuario),
	UNIQUE(Login,bd),
	FOREIGN KEY (BD) REFERENCES SQLfi_Bd (Dns) ON DELETE CASCADE,
	FOREIGN KEY (Grupo) 	
		  REFERENCES SQLfi_Grupo (Nombre) 
		  ON DELETE CASCADE
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	TERMINO DIFUSO		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- Tipo:
-- 1=Predicado Conjunto 2=Predicado Condicion
-- 3=Cuantificador 4=Modificador Potencia 5= Modificador Translacion
-- 6=Modificador Norma 7=Conector 8=Comparador Conjunto
-- 9=Comparador Relacion 10=Tabla 11=Tabla Difusa 12=Vista 13=vista difusa 14=Asercion

CREATE TABLE SQLfi_Termino_Difuso (
	Codigo INTEGER NOT NULL AUTO_INCREMENT, 
	Identificador VARCHAR(40) NOT NULL,
	Propietario	INTEGER NOT NULL,
	Tipo INTEGER(3) NOT NULL CHECK (Tipo IN (1,2,3,4,5,6,7,8,9,10,11,12,13,14)),
	PRIMARY KEY(Codigo),
	UNIQUE (Identificador, Propietario),
	FOREIGN KEY (Propietario) 	
		  REFERENCES SQLfi_Usuario (Usuario) 
		  ON DELETE CASCADE  
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	PREDICADO  DIFUSO	-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

CREATE TABLE SQLfi_Predicado_Condicion (
	Codigo_Termino INTEGER NOT NULL,
	Condicion VARCHAR(256) NOT NULL,
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	COMPARADORES DIFUSO	-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE TABLE SQLfi_Comparador_Difuso (
	Codigo_Termino INTEGER NOT NULL,
	Tipo INTEGER(1) NOT NULL CHECK (Tipo IN (0,1,2)),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	MODIFICADORES DIFUSO	-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
--  MODIFICADOR POR POTENCIA
CREATE TABLE SQLfi_Modificador_Potencia (
	Codigo_Termino INTEGER NOT NULL, 
	Potencia FLOAT NOT NULL ,CHECK (Potencia >= 0),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);

--  MODIFICADOR POR TRANSLACION
CREATE TABLE SQLfi_Modificador_Trans (
	Codigo_Termino INTEGER NOT NULL, 
	Translacion FLOAT NOT NULL,
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);

--  MODIFICADOR POR NORMA
CREATE TABLE SQLfi_Modificador_Norma (
	Codigo_Termino INTEGER NOT NULL,
	Expresion VARCHAR(256) NOT NULL,
	Repeticiones INTEGER NOT NULL CHECK(Repeticiones >= 0),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);


-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	CUANTIFICADOR DIFUSO	-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE TABLE SQLfi_Cuantificador_Difuso (
	Codigo_Termino INTEGER NOT NULL,
	Tipo INTEGER(1) NOT NULL CHECK (Tipo IN (0,1)),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	CONECTOR  DIFUSO	-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE TABLE SQLfi_Conector_Difuso (
	Codigo_Termino INTEGER NOT NULL,
	Expresion VARCHAR(256) NOT NULL,
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	CONJUNTO DIFUSO		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- Tipo
-- 1=trapecio 2=expresion 3=extension escalar 4=extension num

CREATE TABLE SQLfi_Conjunto_Difuso (
	Codigo_Termino INTEGER NOT NULL,
	Tipo INTEGER(1) NOT NULL CHECK (Tipo IN (1,2,3,4)),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);

CREATE TABLE SQLfi_Conjunto_Trapecio (
	Codigo_Conjunto INTEGER 	NOT NULL,
	Coord_A FLOAT NOT NULL,
	Coord_B FLOAT NOT NULL,
	Coord_C FLOAT NOT NULL,
	Coord_D FLOAT NOT NULL,
	PRIMARY KEY (Codigo_Conjunto),
	FOREIGN KEY (Codigo_Conjunto) 	
		  REFERENCES SQLfi_Conjunto_Difuso (Codigo_Termino) 
		  ON DELETE CASCADE
);

CREATE TABLE SQLfi_Conjunto_Expresion (
	Codigo_Conjunto INTEGER 	NOT NULL,
	Expresion VARCHAR(256) NOT NULL,
	PRIMARY KEY (Codigo_Conjunto),
	FOREIGN KEY (Codigo_Conjunto) 	
		  REFERENCES SQLfi_Conjunto_Difuso (Codigo_Termino) 
		  ON DELETE CASCADE
);

CREATE TABLE SQLfi_Elemento_Escalar (
	Codigo_Conjunto INTEGER 	NOT NULL,
	Elemento VARCHAR(20) NOT NULL,
	Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),
	PRIMARY KEY (Codigo_Conjunto, Elemento),
	FOREIGN KEY (Codigo_Conjunto) 	
		  REFERENCES SQLfi_Conjunto_Difuso (Codigo_Termino) 
		  ON DELETE CASCADE
);

CREATE TABLE SQLfi_Elemento_Numerico (
	Codigo_Conjunto INTEGER 	NOT NULL,
	Elemento FLOAT NOT NULL,
	Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),
	PRIMARY KEY (Codigo_Conjunto, Elemento),
	FOREIGN KEY (Codigo_Conjunto) 	
		  REFERENCES SQLfi_Conjunto_Difuso (Codigo_Termino) 
		  ON DELETE CASCADE
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- 		DOMINIO		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- Tipo
-- 1=escalar 2=numerico 3=fecha 4=hora 5=tuple

CREATE TABLE SQLfi_Dominio (
	Codigo_Termino INTEGER NOT NULL,
	Tipo INTEGER(1) NOT NULL CHECK (Tipo IN (1,2,3,4,5)),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);
CREATE TABLE SQLfi_Dominio_Numerico (
	Codigo_Dominio INTEGER NOT NULL,
	Dom_inf FLOAT NOT NULL,
	Dom_sup FLOAT NOT NULL,
	PRIMARY KEY (Codigo_Dominio),
	FOREIGN KEY (Codigo_Dominio) 	
		  REFERENCES SQLfi_Dominio (Codigo_Termino) 
		  ON DELETE CASCADE
);
CREATE TABLE SQLfi_Dominio_Escalar (
	Codigo_Dominio INTEGER NOT NULL,
	Dominio VARCHAR(20) NOT NULL,
	PRIMARY KEY (Codigo_Dominio),
	FOREIGN KEY (Codigo_Dominio) 	
		  REFERENCES SQLfi_Dominio (Codigo_Termino) 
		  ON DELETE CASCADE
);
CREATE TABLE SQLfi_Dominio_Fecha (
	Codigo_Dominio INTEGER NOT NULL,
	Dominio VARCHAR(10) NOT NULL CHECK(Dominio IN ('DATE', 'DATE_DAY', 'DATE_MONTH', 'DATE_YEAR')),
	PRIMARY KEY (Codigo_Dominio),
	FOREIGN KEY (Codigo_Dominio) 	
		  REFERENCES SQLfi_Dominio (Codigo_Termino) 
		  ON DELETE CASCADE
);
CREATE TABLE SQLfi_Dominio_Hora (
	Codigo_Dominio INTEGER NOT NULL,
	Dominio VARCHAR(10) NOT NULL CHECK(Dominio IN ('TIMESTAMP')),
	PRIMARY KEY (Codigo_Dominio),
	FOREIGN KEY (Codigo_Dominio) 	
		  REFERENCES SQLfi_Dominio (Codigo_Termino) 
		  ON DELETE CASCADE
);
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -	RELACION DIFUSA		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

-- Tipo 
-- 0=escalar 1=numerico

CREATE TABLE SQLfi_Relacion_Difusa (
	Codigo_Termino INTEGER NOT NULL,
	Tipo INTEGER(1) NOT NULL CHECK (Tipo IN (0,1)), 
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);
CREATE TABLE SQLfi_Relacion_Escalar (
	Codigo_Relacion INTEGER NOT NULL,
	Elemento1 VARCHAR(20) NOT NULL,
	Elemento2 VARCHAR(20) NOT NULL,
	Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),
	PRIMARY KEY (Codigo_Relacion,Elemento1,Elemento2),
	FOREIGN KEY (Codigo_Relacion) 	
		  REFERENCES SQLfi_Relacion_Difusa (Codigo_Termino) 
		  ON DELETE CASCADE
);
CREATE TABLE SQLfi_Relacion_Num (	
	Codigo_Relacion INTEGER NOT NULL,
	Elemento1 FLOAT NOT NULL,
	Elemento2 FLOAT NOT NULL,
	Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),
	PRIMARY KEY (Codigo_Relacion,Elemento1,Elemento2),
	FOREIGN KEY (Codigo_Relacion) 	
		  REFERENCES SQLfi_Relacion_Difusa (Codigo_Termino) 
		  ON DELETE CASCADE
);
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -		TABLA DIFUSA		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -

--  Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),

CREATE TABLE SQLfi_Restriccion (
	Codigo INTEGER NOT NULL AUTO_INCREMENT,
	Codigo_Termino INTEGER NOT NULL,
	SentenciaCheck VARCHAR(256) NOT NULL,
	Membresia FLOAT NOT NULL,--  CHECK (Membresia >= 0.0 AND Membresia <= 1.0),
	PRIMARY KEY (Codigo,Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);

-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- 		VISTA DIFUSA		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 

--  Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),

CREATE TABLE SQLfi_Vista_Difusa (
	Codigo_Termino INTEGER NOT NULL,
	SubConsulta VARCHAR(512) NOT NULL,
	Membresia FLOAT NOT NULL, 
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -		ASERCION DIFUSA		-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -	
CREATE TABLE SQLfi_Asercion_Difusa (
	Codigo_Termino INTEGER NOT NULL,
	Condicion VARCHAR(256) NOT NULL,
	Membresia FLOAT NOT NULL CHECK (Membresia >= 0.0 AND Membresia <= 1.0),
	PRIMARY KEY (Codigo_Termino),
	FOREIGN KEY (Codigo_Termino) 	
		  REFERENCES SQLfi_Termino_Difuso (Codigo) 
		  ON DELETE CASCADE
);
