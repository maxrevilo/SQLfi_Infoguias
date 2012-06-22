Aplicacion de Demostración para Infoguias sobre consultas difusas.
==================================================================

El proyecto está desarrollado sobre [Playframework][1].
El proyecto es _Plataform Independent_.


Instalación del Proyecto:
-------------------------

+ Instalar la [ultima version del JDK][2].
+ Instalar [Play! Framework][1].
+ Instalar MySQL.

### Para montar la base de datos:
1. Crear una base de datos llamada sqlfi_db y un usuario llamado sqlfi con password "sqlfi" y todos los privilegios en sqlfi_db.
2. Correr el archivo de [craecion de SQLfi][3] y de [carga del SQLfi][4] en la base de datos sqlfi_db.
3. Crear una base de datos llamada infoguias_db y un usuario llamado infoguias con password "infoguias" y todos los privilegios en infoguias_db.
4. Correr el archivo de [craecion de base de datos][5] y de [carga][6] en la base de datos infoguias_db.

### Correr el proyecto en DEV:
1. Es necesario tener en la variable $PATH una direccion al archivo ejecutable play.
2. Entrar al directorio donde se encuentra el proyecto y ejecutar el comando play run.
3. La pagina será servida desde la dirección [LocalHost:9000][7].



[1]: http://www.playframework.org "Página de Play! Framework"
[2]: http://www.oracle.com/technetwork/java/javase/downloads/index.html "Página de descargas de Java Oracle"
[3]: https://github.com/maxrevilo/SQLfi_Infoguias/blob/master/SQLfi_scripts/sqlfi_crear.sql "SQLfi_scripts\sqlfi_crear.sql"
[4]: https://github.com/maxrevilo/SQLfi_Infoguias/blob/master/SQLfi_scripts/sqlfi_cargar.sql "SQLfi_scripts\sqlfi_cargar.sql"
[5]: https://github.com/maxrevilo/SQLfi_Infoguias/blob/master/SQLfi_scripts/infoguias_crear.sql "SQLfi_scripts\infoguias_crear.sql"
[6]: https://github.com/maxrevilo/SQLfi_Infoguias/blob/master/SQLfi_scripts/infoguias_cargar.sql "SQLfi_scripts\infoguias_cargar.sql"
[7]: 127.0.0.1:9000 "Local Host por el puerto 9000"