package models;

import Cliente.ClienteSQLfiImpl;
import Despachador.ConjuntoResultado;
import Despachador.Mu;
import conf.VariablesAmbiente;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;

public class SQLfiManager {
    public static ClienteSQLfiImpl ConnectSQLfi() throws Exception {
        ClienteSQLfiImpl app = new ClienteSQLfiImpl();

        // Se carga la configuracion SQLfi del archivo de propiedades.
        app.cargarConfiguracion();

        // Se conectan el usuario SQLfi y el usuario de aplicacion.
        app.conectarUsuarios();
        return app;
    }


    public static String ResultToString(ConjuntoResultado obj, String fuzzyQuery) {
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
