package controllers;

import play.libs.Json;
import play.mvc.*;

public class Ajax extends Controller {

    public static Result categories() {
        String[] categories = {"Arepera", "Restaurante", "Zapateria"};
        return ok(Json.toJson(categories));
    }

    public static Result states() {
        String[] result = new String[20];
        for(int i = 0; i < result.length; i++) result[i] = "Estado "+Integer.toString(i);
        return ok(Json.toJson(result));
    }

    public static Result cities() {
        String state = request().queryString().get("state")[0];
        String[] result = new String[10];
        for(int i = 0; i < result.length; i++) result[i] = "Ciudad "+Integer.toString(i)+"."+state;
        return ok(Json.toJson(result));
    }

    public static Result neighborhoods() {
        String city = request().queryString().get("city")[0];
        String[] result = new String[10];
        for(int i = 0; i < result.length; i++) result[i] = "Urbanizacion "+Integer.toString(i)+"."+city;
        return ok(Json.toJson(result));
    }

}
