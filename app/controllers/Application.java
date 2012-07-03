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
                "CREATE COMPARATOR similar ON nombrecategoria AS (x, y) IN " +
                    "{ (Restaurante, Arepera) / 0.8, (Arepera, Restaurante) / 0.8, " +
                    "(Arepera, Arepera) / 1.0, (Zapateria, Zapateria) / 1.0, " +
                    "(Restaurante, Restaurante) / 1.0, (Restaurant, Restaurante) / 1.0, " +
                    "(Restaurante, Restaurant) / 1.0, (Restaurant, Restaurant) / 1.0, " +
                    "(Carniceria, Restaurante) / 0.2, (Restaurante, Carniceria) / 0.2, " +
                    "(Carniceria, Carniceria) / 1.0, (Carniceria, Restaurant) / 0.2, " +
                    "(Restaurant, Carniceria) / 0.2, (Carniceria, Arepera) / 0.1, " +
                    "(Arepera, Carniceria) / 0.1, (Pizzeria, Restaurante) / 0.9, " +
                    "(Pizzeria, Pizzeria) / 1.0, (Carniceria, Pizzeria) / 0.1, " +
                    "(Pizzeria, Restaurant) / 0.9, (Restaurant, Pizzeria) / 0.9, " +
                    "(Pizzeria, Carniceria) / 0.1, (Restaurante, Pizzeria) / 0.9} ;"
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
                if(search.isDiffuse()) {

                    switch (search.region()) {
                        case COUNTRY:
                            fuzzyQuery =
                                "SELECT emp.nombreempresa " +
                                "FROM paginacioncategorias pag,empresas emp,categorias cat " +
                                "WHERE " +
                                "    pag.empresas_codigoempresa = emp.codigoempresa AND " +
                                "    pag.codigocategoria = cat.codigocategoria AND " +
                                "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
                            break;

                        case STATE:
                            fuzzyQuery =
                                "SELECT emp.nombreempresa " +
                                "FROM categorias cat, paginacioncategorias pag, empresas emp, distancias_estados d_e, estados e, dataempresas data " +
                                "WHERE " +
                                "    ((estado2 = '"+search.where+"' AND d_e.estado1 = e.nombreestado) OR " +
                                "    (d_e.estado2 = e.nombreestado AND estado1 = '"+search.where+"')) AND " +
                                "    d_e.distancia_estados = cerca_estado AND data.codigoempresa = emp.codigoempresa AND data.codigoestado = e.codigoestado AND " +
                                "    cat.nombrecategoria comparator_similar '"+search.what+"' AND " +
                                "    pag.codigoempresa = emp.codigoempresa AND " +
                                "    pag.codigocategoria = cat.codigocategoria ;";
                            break;

                        case CITY:
                            fuzzyQuery =
                                "SELECT emp.nombreempresa " +
                                "FROM distancias_ciudades d, ciudades c, categorias cat, paginacioncategorias pag, empresas emp " +
                                "WHERE " +
                                "    distancia_ciudades = cerca_ciudad AND " +
                                "    ((ciudad1 = '"+search.where+"' AND ciudad2 = c.nombreciudad) " +
                                "    OR (ciudad2 = '"+search.where+"' AND ciudad1 = c.nombreciudad)) AND " +
                                "    c.codigociudad = pag.codigociudad AND " +
                                "    emp.codigoempresa = pag.codigoempresa AND " +
                                "    cat.codigocategoria = pag.codigocategoria AND " +
                                "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
                            break;

                        case NEIGHBORHOODS:
                            fuzzyQuery =
                                    "SELECT emp.nombreempresa " +
                                            "FROM paginacioncategorias pag,empresas emp,categorias cat " +
                                            "WHERE " +
                                            "    pag.empresas_codigoempresa = emp.codigoempresa AND " +
                                            "    pag.codigocategoria = cat.codigocategoria AND " +
                                            "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
                            break;

                        case MAP_AREA:
                            fuzzyQuery =
                                    "SELECT emp.nombreempresa " +
                                            "FROM paginacioncategorias pag,empresas emp,categorias cat " +
                                            "WHERE " +
                                            "    pag.empresas_codigoempresa = emp.codigoempresa AND " +
                                            "    pag.codigocategoria = cat.codigocategoria AND " +
                                            "    cat.nombrecategoria comparator_similar '"+search.what+"' ;";
                            break;

                        default:
                            throw new NotSupportedException();
                    }

                }
                else throw new NotSupportedException();
            }
            else throw new NotSupportedException();

            JsonNode jsonResults =
                    SQLfiManager.ResultToJson( (ConjuntoResultado)
                            SQLfiManager.FastQuery(
                                    fuzzyQuery
                            )
                    );

            return ok(results.render(jsonResults));
        }
    }

}
