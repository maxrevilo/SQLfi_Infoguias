package controllers.forms;

import play.data.validation.Constraints.*;

public class Search {
    @Required
    public String what;
    @Required
    public String what_how;
    public String diff;


    @Required
    public String where;
    public String map;
    public String exactly;

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
