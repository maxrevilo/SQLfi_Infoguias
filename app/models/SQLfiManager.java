package models;


import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import javax.transaction.NotSupportedException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;

import Despachador.*;
import Cliente.ClienteSQLfiImpl;
import conf.VariablesAmbiente;
import play.mvc.Controller;
import play.mvc.Results;




public class SQLfiManager {

    public static void MultipleQueries(String... strings) throws Exception {
        ClienteSQLfiImpl app = SQLfiManager.ConnectSQLfi();

        for(String fuzzyQuery : strings) {
            ObjetoSQLfi obj = app.ejecutarSentencia(fuzzyQuery);

            System.out.println(  fuzzyQuery );
            System.out.println("Result\n\tClass: "+obj.getClass().toString()+".");

            if (obj instanceof ResultadoSQLfi) {
                System.out.println(  "\tTipe: "+String.valueOf( ((ResultadoSQLfi) obj).getTipo() )  );
                System.out.println(  "\tString form: "+String.valueOf( ((ResultadoSQLfi) obj).toString() )  );
            } else {
                System.out.println( "\tString form: "+obj.toString() );
            }
        }

        app.desconectarUsuarios();
    }

    public static Results.Status FastQueryResult(String fuzzyQuery) throws Exception {
        return FastQueryResult(fuzzyQuery, null);
    }

    public static Results.Status FastQueryResult(String fuzzyQuery, String[] format) throws Exception {
        ObjetoSQLfi obj = SQLfiManager.FastQuery(fuzzyQuery);
        if (obj instanceof ResultadoSQLfi) {
            return Controller.ok( "SQLfi result: " + String.valueOf(((ResultadoSQLfi) obj).getTipo()) );
        } else {
            return Controller.ok( SQLfiManager.ResultToJson((ConjuntoResultado) obj, format) );
        }
    }

    public static ObjetoSQLfi FastQuery(String fuzzyQuery) throws Exception {
        ClienteSQLfiImpl app = SQLfiManager.ConnectSQLfi();
        ObjetoSQLfi obj = app.ejecutarSentencia(fuzzyQuery);
        app.desconectarUsuarios();
        return obj;
    }

    public static ClienteSQLfiImpl ConnectSQLfi() throws Exception {
        ClienteSQLfiImpl app = new ClienteSQLfiImpl();

        // Se carga la configuracion SQLfi del archivo de propiedades.
        app.cargarConfiguracion();
        app.conectarUsuarios();

        return app;
    }

    public static JsonNode ResultToJson(ConjuntoResultado obj)
    {return ResultToJson(obj, null);}

    public static JsonNode ResultToJson(ConjuntoResultado obj, String[] format) {
        int numCols = ((ConjuntoResultado) obj).obtenerMetaDatos().obtenerNumeroColumnas();
        boolean p = ((ConjuntoResultado) obj).primero();

        MetaDatosConjuntoResultado metaData = obj.obtenerMetaDatos();
        if(format == null) {
            format = new String[numCols];
            for (int cont = 1; cont <= numCols; cont++) {
                format[cont-1] = metaData.obtenerEtiquetaColumna(cont);
            }
        } else if(format.length != numCols)
            throw new InvalidParameterException(
                    "format and the query result must have the same number of objects, format has "+
                    format.length +" elements and the query returned "+numCols);

        ObjectNode base = Json.newObject();
        ArrayNode results = base.putArray("result");

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat(VariablesAmbiente.FUZZY_MU_DECIMAL_FORMAT, dfs);


        ObjectNode nodeResult;
        Object objetoResultado;
        ((ConjuntoResultado) obj).antesDelPrimero();
        while (((ConjuntoResultado) obj).proximo()) {
            nodeResult = results.addObject();

            for (int cont = 1; cont <= numCols; cont++) {

                objetoResultado = ((ConjuntoResultado) obj).obtenerObjeto(cont);


                if ((objetoResultado != null) && (objetoResultado instanceof Mu)) {
                    nodeResult.put(
                            format[cont-1],
                            df.format(((Mu) objetoResultado).obtenerValorMu(),
                                    new StringBuffer(""),
                                    new FieldPosition(0)).toString()
                            );
                } else if(objetoResultado != null) {
                    nodeResult.put(
                            format[cont-1],
                            objetoResultado.toString()
                    );
                } else {
                    /** EMPTY FIELD **/
                }

            }

        }
        ((ConjuntoResultado) obj).cerrar();

        return results;
    }

}
