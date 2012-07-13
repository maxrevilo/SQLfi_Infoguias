package controllers;

//Java
import Global.IGGlobal;
import org.codehaus.jackson.JsonNode;
import javax.transaction.NotSupportedException;
import java.sql.SQLException;

//Play!
import play.Logger;
import play.data.Form;
import play.mvc.*;
import views.html.*;

//SQLfi
import Despachador.ConjuntoResultado;

//Utils
import models.SQLfiManager;
import controllers.forms.Search;


public class Application extends Controller {

    public static Result fixtures() throws SQLException, Exception {
        SQLfiManager.MultipleQueries(
                //COMPARATOR: similar
                "DROP COMPARATOR similar ;"
                ,
                "CREATE COMPARATOR similar ON codigocategoria AS (x, y) IN \n" +
                "{ (2136, 2136) / 1.0, (2144, 2144) / 1.0, \n" +
                "  (2382, 2382) / 1.0, \n" +
                "  (2397, 2397) / 1.0, (1039,1039) / 1.0, (2544,2544) / 1.0,\n" +
                "  (2136,2144) / 1.0, (2144,2136) / 1.0,\n" +
                "  (2397,1039) / 1.0, (1039, 2397) / 1.0, (2397,2544) / 1.0,\n" +
                "  (2544,2397) / 1.0, (2544,1039) / 1.0, (1039,2544) / 1.0,\n" +
                "  (2136,2382) / 0.8, (2382,2136) / 0.8, (2144,2382) / 0.8,\n" +
                "  (2382,2144) / 0.8,\n" +
                "  (2382,2397) / 0.5, (2397,2382) / 0.5, (2382,1039) / 0.4,\n" +
                "  (1039,2382) / 0.4, (2382,2544) / 0.4, (2544,2382) / 0.4,\n" +
                "  (2136,2397) / 0.3, (2397,2136) / 0.3, (2136,1039) / 0.3,\n" +
                "  (1039,2136) / 0.3, (2136,2544) / 0.3, (2544,2136) / 0.3,\n" +
                "  (2144,2397) / 0.2, (2397,2144) / 0.2, (2144,1039) / 0.4,\n" +
                "  (1039,2144) / 0.4, (2144,2544) / 0.2, (2544,2144) / 0.2, \n" +

                "  (139,139) / 1.0, (139, 2136) / 0.5, (2136, 784) / 1.0, (784, 2136) / 1.0, (784, 784) / 1.0, \n"+

                "  (2346, 2346) / 1.0,  (2136, 2346) / 0.8, (2346, 2136) / 0.8, "+

                "  (2136,139) / 0.5 \n" +
                        "} ;"
                ,
                //PREDICADO DIFUSO: cerca_ciudad
                "DROP PREDICATE cerca_ciudad ;"
                ,
                "CREATE FUZZY PREDICATE cerca_ciudad ON 0..1000 AS (0 , 0 , 50 , 120)  ;"
                ,
                //PREDICADO DIFUSO: cerca_estad
                "DROP PREDICATE cerca_estado ;"
                ,
                "CREATE FUZZY PREDICATE cerca_estado ON 1..5 AS (0 , 1 , 1 , 3) ;"
                ,
                //PREDICADO DIFUSO: cerca_urb
                "DROP PREDICATE cerca_urb ;"
                ,
                "CREATE FUZZY PREDICATE cerca_urb ON 0..1000 AS (infinit , infinit , 5 , 15) ;"
                //
        );

        return redirect("/");
    }

    public static Result index() {
        return ok(index.render());
    }

    public static Result query_map() throws Exception {
        return ok(query_map.render());
    }

    public static Result results() throws Exception {
        Form<Search> searchForm = form(Search.class).bindFromRequest();

        if(searchForm.hasErrors()) {
            return badRequest(searchForm.errorsAsJson()); //return ok(index.render( errors = searchForm.errors() ));
        } else {
            Search search = searchForm.get();
            String what_query;
            String fuzzyQuery;
            int queryNum = 0;

            if(search.isByName()) what_query = "    emp.nombreempresa = '"+search.what+"' ;";
            else  what_query =
                    "    pag.codigocategoria = cat.codigocategoria AND\n" +
                    "    cat.codigocategoria comparator_similar "+search.what+" ;";


            if(search.Similars()) {

                switch (search.region()) {
                    case COUNTRY:
                        fuzzyQuery =
                            "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                            "FROM paginacioncategorias pag,empresas emp,categorias cat\n" +
                            "WHERE\n" +
                            "    pag.codigoempresa = emp.codigoempresa AND\n" +
                            what_query;
                        break;

                    case STATE:
                        fuzzyQuery =
                            "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                            "FROM categorias cat, paginacioncategorias pag, empresas emp, distancias_estados d_e, estados e, dataempresas data\n" +
                            "WHERE\n" +
                            "    ((estado2 = '"+search.where+"' AND d_e.estado1 = e.codigoestado) OR\n" +
                            "    (d_e.estado2 = e.codigoestado AND estado1 = '"+search.where+"')) AND\n" +
                            "    d_e.distancia_estados = cerca_estado AND data.codigoempresa = emp.codigoempresa AND data.codigoestado = e.codigoestado AND\n" +
                            "    pag.codigoempresa = emp.codigoempresa AND\n" +
                                    what_query;
                        break;

                    case CITY:
                        fuzzyQuery =
                            "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                            "FROM distancias_ciudades d, ciudades c, categorias cat, paginacioncategorias pag, empresas emp\n" +
                            "WHERE\n" +
                            "    distancia_ciudades = cerca_ciudad AND\n" +
                            "    ((ciudad1 = '"+search.where+"' AND ciudad2 = c.codigociudad)\n" +
                            "    OR (ciudad2 = '"+search.where+"' AND ciudad1 = c.codigociudad)) AND\n" +
                            "    c.codigociudad = pag.codigociudad AND\n" +
                            "    emp.codigoempresa = pag.codigoempresa AND\n" +
                                    what_query;
                        break;

                    case NEIGHBORHOODS:
                        fuzzyQuery =
                                "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                                "FROM paginacioncategorias pag,empresas emp,categorias cat\n" +
                                "WHERE\n" +
                                "    pag.codigoempresa = emp.codigoempresa AND\n" +
                                        what_query;
                        break;

                    case MAP_AREA:
                        Double  dlat  = 10000.0 * (search.GLatitude() - search.LLatitude()) / 2.0,
                                dlng  = 10000.0 * (search.GLongitude() - search.LLongitude()) / 2.0,
                                llat  = 10000.0 * search.LLatitude(),
                                glat  = 10000.0 * search.GLatitude(),
                                llng  = 10000.0 * search.LLongitude(),
                                glng  = 10000.0 * search.GLongitude(),
                                lllat = (llat - dlat),
                                gglat = (glat + dlat),
                                lllng = (llng - dlng),
                                gglng = (glng + dlng);

                        queryNum = IGGlobal.i();

                        SQLfiManager.MultipleQueries(
                            "CREATE FUZZY PREDICATE cerca_lat"+queryNum+" ON -9999999.0..9999999.0 AS ("+lllat+", "+llat+", "+glat+", "+gglat+");",
                            "CREATE FUZZY PREDICATE cerca_lng"+queryNum+" ON -9999999.0..9999999.0 AS ("+lllng+", "+llng+", "+glng+", "+gglng+");"
                        );

                        fuzzyQuery =
                            "SELECT emp.nombreempresa, emp.descripcionempresa \n" +
                            "FROM paginacioncategorias pag,empresas emp,categorias cat, dataempresas dat \n" +
                            "WHERE \n" +
                            "    pag.codigoempresa = emp.codigoempresa AND \n" +
                            "    dat.latitud = cerca_lat"+queryNum+" AND \n" +
                            "    dat.longitud = cerca_lng"+queryNum+" AND \n" +
                            "    dat.codigoempresa = emp.codigoempresa AND \n" +
                                    what_query;

                        break;

                    default:
                        throw new NotSupportedException();
                }

            }
            else throw new NotSupportedException();

            JsonNode jsonResults =
                    SQLfiManager.ResultToJson( (ConjuntoResultado)
                            SQLfiManager.FastQuery(
                                    fuzzyQuery
                            )
                    );

            if(search.region() == Search.Region.MAP_AREA) {
                SQLfiManager.MultipleQueries(
                        "DROP PREDICATE cerca_lat"+queryNum+" ;",
                        "DROP PREDICATE cerca_lng"+queryNum+" ;"
                );
            }
            return ok(results.render(jsonResults, search.isByCategory(), search.what));
        }
    }

}
