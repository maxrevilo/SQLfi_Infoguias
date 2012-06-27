package controllers;

//Play!
import play.data.Form;
import play.mvc.*;
import views.html.*;

//SQLfi
import Cliente.ClienteSQLfiImpl;
import Despachador.*;

//Utils
import models.SQLfiManager;
import controllers.forms.Search;
import java.sql.SQLException;



public class Application extends Controller {

    public static Result fixtures() throws SQLException, Exception {
        ClienteSQLfiImpl app = SQLfiManager.ConnectSQLfi();
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

    public static Result index() {
        return ok(index.render());
    }

    public static Result results() throws SQLException, Exception {
        Form<Search> searchForm = form(Search.class).bindFromRequest();

        if(searchForm.hasErrors()) {
            return badRequest(searchForm.errorsAsJson()); //return ok(index.render( errors = searchForm.errors() ));
        } else {
            Search search = searchForm.get();
            return ok(results.render(search.what));
        }


        /*
        String fuzzyQuery =
                "SELECT emp.nombreempresa FROM paginacioncategorias pag, empresas emp, categorias cat " +
                        "WHERE pag.empresas_codigoempresa = emp.codigoempresa " +
                        "AND pag.codigocategoria = cat.codigocategoria " +
                        "AND cat.nombrecategoria comparator_parecido '"+get+"';";

        ClienteSQLfiImpl app = ConnectSQLfi();
        ObjetoSQLfi obj = app.ejecutarSentencia(fuzzyQuery);
        app.desconectarUsuarios();

        String result;
        if (obj instanceof ResultadoSQLfi) {
            result = "SQLfi result: "+String.valueOf( ((ResultadoSQLfi) obj).getTipo() );
        } else {
            result = ResultToString((ConjuntoResultado) obj, fuzzyQuery);
        }
        */
    }

}
