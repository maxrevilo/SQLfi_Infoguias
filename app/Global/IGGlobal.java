package Global;

import Cliente.ClienteSQLfiImpl;
import play.*;

public class IGGlobal extends GlobalSettings {
    private static ClienteSQLfiImpl sqlfiApp;
    static int i;

    @Override
    public void onStart(Application app) {
        sqlfiApp = new ClienteSQLfiImpl();

        // Se carga la configuracion SQLfi del archivo de propiedades.
        try {
            sqlfiApp.cargarConfiguracion();
            sqlfiApp.conectarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.info("SQLfi Client connected");

        i = 0;
    }


    @Override
    public void onStop(Application app) {
        if(sqlfiApp!=null) {
            try {
                sqlfiApp.desconectarUsuarios();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.info("SQLfi Client disconnected...");
    }

    public static Cliente.ClienteSQLfi SQLfiApp() { return sqlfiApp; }

    public static int i() { return i++;}
}