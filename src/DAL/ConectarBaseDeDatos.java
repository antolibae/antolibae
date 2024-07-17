package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarBaseDeDatos {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * Creara una coneccion a la base de datos ad2223_dcarvajal
     * @return devolvera una variable de tipo Connection con la coneccion
     */
    public static Connection crearConeccion() {
        Connection connection = null;
        try {
            String servidor = "jdbc:mysql://dns11036.phdns11.es/ad2223_dcarvajal";
            //Drivers
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Nos conectamos al servidor con nuestro usuario
            connection = DriverManager.getConnection(servidor, "ad2223_dcarvajal", "1234");

        } catch (ClassNotFoundException e) {
            System.out.println(ANSI_RED+"No se encontraron los drivers de la base de datos"+ANSI_RESET);
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Hubo algun error en la coneccion"+ANSI_RESET);
        }
        return connection;
    }


}
