package controllers;

//Java
import org.codehaus.jackson.JsonNode;
import javax.transaction.NotSupportedException;
import java.sql.SQLException;

//Play!
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
                "CREATE COMPARATOR similar ON nombrecategoria AS (x, y) IN \n" +
                "    { (Restaurante, Arepera) / 0.8, (Arepera, Restaurante) / 0.8, \n" +
                "    (Arepera, Arepera) / 1.0, (Zapateria, Zapateria) / 1.0, \n" +
                "    (Restaurante, Restaurante) / 1.0, (Restaurant, Restaurante) / 1.0, \n" +
                "    (Restaurante, Restaurant) / 1.0, (Restaurant, Restaurant) / 1.0, \n" +
                "    (Carniceria, Restaurante) / 0.2, (Restaurante, Carniceria) / 0.2, \n" +
                "    (Carniceria, Carniceria) / 1.0, (Carniceria, Restaurant) / 0.2, \n" +
                "    (Restaurant, Carniceria) / 0.2, (Carniceria, Arepera) / 0.1, \n" +
                "    (Arepera, Carniceria) / 0.1, (Pizzeria, Restaurante) / 0.9, \n" +
                "    (Pizzeria, Pizzeria) / 1.0, (Carniceria, Pizzeria) / 0.1, \n" +
                "    (Pizzeria, Restaurant) / 0.9, (Restaurant, Pizzeria) / 0.9, \n" +
                "    (Pizzeria, Carniceria) / 0.1, (Restaurante, Pizzeria) / 0.9} ;"
                ,
                //PREDICADO DIFUSO: cerca_estado
                "DROP PREDICATE cerca_ciudad ;"
                ,
                "CREATE FUZZY PREDICATE cerca_ciudad ON 0..1000 AS (0 , 0 , 50 , 120) ;"
                ,
                //PREDICADO DIFUSO: cerca_ciudad
                "DROP PREDICATE cerca_estado ;"
                ,
                "CREATE FUZZY PREDICATE cerca_estado ON 0..1000 AS (0 , 0 , 50 , 120) ;"
                ,
                //PREDICADO DIFUSO: cerca_urb
                "DROP PREDICATE cerca_urb ;"
                ,
                "CREATE FUZZY PREDICATE cerca_urb ON 0..1000 AS (infinit , infinit , 5 , 10) ;"
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
            String fuzzyQuery;

            if(search.isByCategory()) {
                if(search.Similars()) {

                    switch (search.region()) {
                        case COUNTRY:
                            fuzzyQuery =
                                "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                                "FROM paginacioncategorias pag,empresas emp,categorias cat\n" +
                                "WHERE\n" +
                                "    pag.empresas_codigoempresa = emp.codigoempresa AND\n" +
                                "    pag.codigocategoria = cat.codigocategoria AND\n" +
                                "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
                            break;

                        case STATE:
                            fuzzyQuery =
                                "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                                "FROM categorias cat, paginacioncategorias pag, empresas emp, distancias_estados d_e, estados e, dataempresas data\n" +
                                "WHERE\n" +
                                "    ((estado2 = '"+search.where+"' AND d_e.estado1 = e.codigoestado) OR\n" +
                                "    (d_e.estado2 = e.codigoestado AND estado1 = '"+search.where+"')) AND\n" +
                                "    d_e.distancia_estados = cerca_estado AND data.codigoempresa = emp.codigoempresa AND data.codigoestado = e.codigoestado AND\n" +
                                "    cat.nombrecategoria comparator_similar '"+search.what+"' AND\n" +
                                "    pag.codigoempresa = emp.codigoempresa AND\n" +
                                "    pag.codigocategoria = cat.codigocategoria ;";
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
                                "    cat.codigocategoria = pag.codigocategoria AND\n" +
                                "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
                            break;

                        case NEIGHBORHOODS:
                            fuzzyQuery =
                                    "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                                    "FROM paginacioncategorias pag,empresas emp,categorias cat\n" +
                                    "WHERE\n" +
                                    "    pag.empresas_codigoempresa = emp.codigoempresa AND\n" +
                                    "    pag.codigocategoria = cat.codigocategoria AND\n" +
                                    "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
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

                            SQLfiManager.MultipleQueries(
                                "CREATE FUZZY PREDICATE cerca_lat ON 500000.0..910000.0 AS ("+lllat+", "+llat+", "+glat+", "+gglat+");",
                                "CREATE FUZZY PREDICATE cerca_lng ON -990000.0..-910000.0 AS ("+lllng+", "+llng+", "+glng+", "+gglng+");"
                            );

                            fuzzyQuery =
                                    "SELECT emp.nombreempresa, emp.descripcionempresa\n" +
                                    "FROM paginacioncategorias pag,empresas emp,categorias cat, dataempresas data \n" +
                                    "WHERE\n" +
                                    "    data.codigoempresa = emp.codigoempresa AND\n" +
                                    "    pag.empresas_codigoempresa = emp.codigoempresa AND\n" +
                                    "    pag.codigocategoria = cat.codigocategoria AND\n" +
                                    "    cat.nombrecategoria comparator_similar '"+search.what+"' AND\n" +
                                    "    data.latitud = cerca_lat AND data.longitud = cerca_lng ;";

                            break;

                        default:
                            throw new NotSupportedException();
                    }

                }
                else throw new NotSupportedException();
            }
            else throw new NotSupportedException();

            System.out.println("QUERY:\n"+fuzzyQuery+"\n\n");
            JsonNode jsonResults =
                    SQLfiManager.ResultToJson( (ConjuntoResultado)
                            SQLfiManager.FastQuery(
                                    fuzzyQuery
                            )
                    );

            if(search.region() == Search.Region.MAP_AREA) {
                SQLfiManager.MultipleQueries(
                        "DROP PREDICATE cerca_lat ;",
                        "DROP PREDICATE cerca_lng ;");
            }
            return ok(results.render(jsonResults));
        }
    }

}
