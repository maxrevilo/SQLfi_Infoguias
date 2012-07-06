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
    public String similar;


    public String where;
    @Required
    public String where_region;
    public String near;
    public String llat, llng, glat, glng;

    public boolean isByCategory() { return what_how.equals("cat"); }
    public boolean isByName()     { return what_how.equals("nam"); }
    public boolean Similars()     { return similar != null && similar.equals("on"); }
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
    public boolean ExactPlace() { return !NearPlaces(); }
    public boolean NearPlaces() { return near != null && near.equals("on"); }
    public Double LLatitude()    { return Double.valueOf(llat); }
    public Double LLongitude()   { return Double.valueOf(llng); }
    public Double GLatitude()    { return Double.valueOf(glat); }
    public Double GLongitude()   { return Double.valueOf(glng); }

}
