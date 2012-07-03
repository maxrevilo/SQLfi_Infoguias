package controllers;

import play.mvc.*;

import models.SQLfiManager;

public class Ajax extends Controller {

    public static Result categories() throws Exception {
        return SQLfiManager.FastQueryResult(
                "SELECT nombrecategoria " +
                    "FROM  categorias ;"
                , new String[]{"label"});
    }

    public static Result states() throws Exception {
        return SQLfiManager.FastQueryResult(
                "SELECT nombreestado, codigoestado " +
                    "FROM estados ;"
                , new String[]{"label", "value"});
    }

    public static Result cities() throws Exception {
        String state_id = request().queryString().get("state_id")[0];
        return SQLfiManager.FastQueryResult(
                "SELECT nombreciudad, codigociudad " +
                    "FROM ciudades ciu, ciudad_estado ciu_est, estados est " +
                    "WHERE est.codigoestado = ciu_est.codigoestado " +
                    "AND ciu.codigociudad = ciu_est.codigociudad " +
                    "AND est.codigoestado = '"+state_id+"' ;"
                , new String[]{"Mu", "label", "value"});
    }

    public static Result neighborhoods() throws Exception {
        String city_id = request().queryString().get("city_id")[0];
        return SQLfiManager.FastQueryResult(
                "SELECT nombreurb, codigourb " +
                        "FROM ciudades ciu, urbanizaciones urb " +
                        "WHERE ciu.codigociudad = '"+city_id+"' " +
                        "AND urb.ciudades_codigociudad = ciu.codigociudad ;"
                , new String[]{"Mu", "label", "value"});
    }

}
