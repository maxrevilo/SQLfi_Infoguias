package controllers;

import play.*;
import play.mvc.*;

import views.html.*;


import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;

//import Cliente.ClienteSQLfi;
import Cliente.ClienteSQLfiImpl;
import conf.VariablesAmbiente;
import Despachador.*;

public class Application extends Controller {

    public static Result fixtures() throws SQLException, Exception {
        ClienteSQLfiImpl app = ConnectSQLfi();
        String sent =
                "CREATE COMPARATOR parecido ON categorias AS (x, y) IN { (Restaurante, Arepera) / " +
                "0.8, (Arepera, Restaurante) / 0.8, (Arepera, Zapateria) / 0.0, " +
                "(Zapateria, Arepera) / 0.0, (Zapateria, Restaurante) / 0.0, " +
                "(Restaurante, Zapateria) / 0.0};";
        ObjetoSQLfi obj = app.ejecutarSentencia(sent);
        System.out.println(  sent );

        if (obj instanceof ResultadoSQLfi) {
            System.out.println(  "SQLfi result: "+String.valueOf( ((ResultadoSQLfi) obj).getTipo() )  );
        }

        app.desconectarUsuarios();
        return redirect("/");
    }

    public static Result index() throws SQLException, Exception {
        ClienteSQLfiImpl app = ConnectSQLfi();

        String fuzzyQuery =
                "SELECT emp.nombreempresa FROM paginacioncategorias pag, empresas emp, categorias cat " +
                "WHERE pag.empresas_codigoempresa = emp.codigoempresa " +
                "AND pag.codigocategoria = cat.codigocategoria " +
                "AND cat.nombrecategoria comparator_parecido 'Restaurante';";


        ObjetoSQLfi obj = app.ejecutarSentencia(fuzzyQuery);

        String result;
        if (obj instanceof ResultadoSQLfi) {
            result = "SQLfi result: "+String.valueOf( ((ResultadoSQLfi) obj).getTipo() );
        } else {
            result = ResultToString((ConjuntoResultado) obj, fuzzyQuery);
        }


        app.desconectarUsuarios();

        return ok(index.render(result));
    }


    private static ClienteSQLfiImpl ConnectSQLfi() throws Exception {
        ClienteSQLfiImpl app = new ClienteSQLfiImpl();

        // Se carga la configuracion SQLfi del archivo de propiedades.
        app.cargarConfiguracion();

        // Se conectan el usuario SQLfi y el usuario de aplicacion.
        app.conectarUsuarios();
        return app;
    }

    private static String ResultToString(ConjuntoResultado obj, String fuzzyQuery) {
        String out = new String();

        System.out.println("Consulta Difusa");
        System.out.println("---------------");
        System.out.println(fuzzyQuery + "\n");

        /* Se imprime la instruccion ejecutada */
        System.out.println("Consulta Traducida");
        System.out.println("------------------");
        System.out.println(((ConjuntoResultado) obj).obtenerInstruccion() + "\n");

        int numCols = ((ConjuntoResultado) obj).obtenerMetaDatos().obtenerNumeroColumnas();
        boolean p = ((ConjuntoResultado) obj).primero();

        out += "<table><tr>";

        /* Se imprime el encabezado */
        for (int cont = 1; cont <= numCols; cont++) {
            out += "<th>";
            out += ((ConjuntoResultado) obj).obtenerMetaDatos().obtenerEtiquetaColumna(cont);
            out += "</th>";
        }

        out += "</tr>";

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat(VariablesAmbiente.FUZZY_MU_DECIMAL_FORMAT, dfs);

        Object objetoResultado;
        ((ConjuntoResultado) obj).antesDelPrimero();
        /* Se imprimen las filas */
        while (((ConjuntoResultado) obj).proximo()) {
            out += "<tr>";
            for (int cont = 1; cont <= numCols; cont++) {
                out += "<td>";
                objetoResultado = ((ConjuntoResultado) obj).obtenerObjeto(cont);
                if ((objetoResultado != null) && (objetoResultado instanceof Mu)) {
                    out += df.format(((Mu) objetoResultado).obtenerValorMu(),
                            new StringBuffer(""),
                            new FieldPosition(0));
                } else {
                    out += objetoResultado;
                }
                out += "</td>";
            }
            out += "</tr>";
        }

        out += "</table>";

        if (((ConjuntoResultado) obj).obtenerNumeroFilas() == 0) {
            System.out.print("\nfilas no seleccionadas.");
        } else {
            System.out.print("\nseleccionada ");
            System.out.print(((ConjuntoResultado) obj).obtenerNumeroFilas());
            System.out.print(" fila(s).");
        }
        ((ConjuntoResultado) obj).cerrar();
        return out;
    }

}
