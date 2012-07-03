package controllers.forms;

import play.data.validation.Constraints.*;

public class Search {
    public enum Region {
        COUNTRY, STATE, CITY, NEIGHBORHOODS, MAP_AREA
    }

    @Required
    public String what;
    @Required
    public String what_how;
    public String diff;


    @Required
    public String where;
    @Required
    public String where_region;
    @Required
    public String where_how;

    public boolean isByCategory() {return what_how.equals("cat");}
    public boolean isByName() {return what_how.equals("nam");}
    public boolean isDiffuse() {return diff != null && diff.equals("on");}
    public Region region() throws Exception {
        //TODO: realizar este proceso en el contructor una unica vez.
        if(where_region.equals("state")) {
            if(where.equals("-1")) return Region.COUNTRY;
            else return Region.STATE;
        } else if(where_region.equals("city")) {
            return Region.CITY;
        } else if(where_region.equals("neighbor")) {
            return Region.NEIGHBORHOODS;
        } else if(where_region.equals("map")) {
            return Region.MAP_AREA;
        } else {
            throw new Exception("where_region value not suported");
        }
    }
    public boolean isExact() {return where_how.equals("exact");}
    public boolean isNear() {return where_how.equals("near");}

    /*
    public String validate() {
        if(what == null || what.isEmpty()) return "El campo \"¿Que quieres?\" debe tener un valor";

        if(what_how.equals("nam")) {
            if(diff.equals("on")) return "Consultas difusas no soportadas en busqueda por nombre";
        } else
        if(what_how.equals("cat")) {
        } else {
            return "Valor para \"what_how\" no soportado: "+what_how;
        }

        if(where == null || where.isEmpty()) return "El campo \"¿Donde?\" debe tener un valor";

        return null;
    }*/
}
