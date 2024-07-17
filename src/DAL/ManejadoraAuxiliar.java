package DAL;

import java.sql.*;
import java.util.Objects;

public class ManejadoraAuxiliar {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    /**
     * Insertara los valores en la tabla indicada
     * @param tabla sera el nombre de la tabla en la que insertaran los datos
     * @param values seran lon valores que se insertaran en la tabla
     * @return deolvera true si se pudieron insertar los datos y false si no se puedieron insertar
     */
    public static boolean insertarDatos(String tabla,String[]campos, String[]values){
        boolean insertado=false;
        //Creamos la coneccion a la base de datos
        PreparedStatement st = null;
        Connection conn = ConectarBaseDeDatos.crearConeccion();

        //Preparamos la sentencia sql
        String sql = "INSERT INTO " + tabla +"("+campos[0];
        //Introducimos los campos
        for (int i = 1; i < values.length; i++) {
            sql += ","+campos[i];
        }
        //Introducimos los valores
         sql+=") VALUES (?";
        for (int i = 1; i < values.length; i++) {
            sql += ",?";
        }
        sql += ")";

        try {
            //Insertamos los datos
            assert conn != null;
            st = conn.prepareStatement(sql);
            st.setString(1, values[0]);
            for (int i = 1; i < values.length; i++) {
                st.setString(i + 1, values[i]);
            }
            //Ejecutamos la sentencia
            //System.out.println(values[5]);
            int filasAfectadas=st.executeUpdate();
            if(filasAfectadas>0){
                insertado=true;
            }
            System.out.println(filasAfectadas + " filas afectadas");
            st.close();
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error insertando los datos"+ANSI_RESET);

        }finally {
            try {
                st.close();
                conn.close();
            }catch (SQLException e){
                System.out.println(ANSI_RED+"Error cerrando la coneccion"+ANSI_RESET);
            }
        }
        return insertado;
    }

    /**
     * Este metodo comprobara si existe en una tabla un registro con cierto valor en cierto campo
     * @param tabla sera la tabla en la que se ara la comprobacion
     * @param campo sera el campo por el que se buscara
     * @param valor sera el valor del campo por el que buscara
     * @return devolvera true si existe y false si no o no se pudo realizar la comprobracion correctamente
     */
    public static boolean comprobarSiExiste(String tabla, String campo, String valor){
        boolean existe=false;
        //Creamos la coneccion a la base de datos
        PreparedStatement st = null;
        Connection conn = ConectarBaseDeDatos.crearConeccion();

        //Preparamos la sentencia sql
        String sql="SELECT * FROM "+tabla+" WHERE "+campo+" = '"+valor+"'";
        try {
            assert conn != null;
            st = conn.prepareStatement(sql);
            if(st.executeQuery(sql).next()){
                existe=true;
            }

        } catch (Exception e) {
            System.out.println(ANSI_RED+"Error en la comprobacion de los datos"+ANSI_RESET);
        }finally {
            try {
                st.close();
                conn.close();
            }catch (SQLException e){
                System.out.println(ANSI_RED+"Error cerrando la coneccion"+ANSI_RESET);
            }
        }

        return existe;
    }

    /**
     * Se devolvera un resultset con los datos que se recogan la tabla indicada por parametros, habra que indicar campos(vacion si se quieren elejir todos)
     * y restricciones en el caso de no querer pasar un string vacio
     *
     * @param tabla          el nombre de la tabla en la que se quiere buscar
     * @param campos         un array con los campos que se quiere obtener, si es un - se listaran todos los campos
     * @param restriccionnes un string con las restricciones, sera un - en caso de no querer restricciones
     * @return devolvera un ResultSet con los datos listados
     */
    public static ResultSet listar(String tabla, String[] campos, String restriccionnes) {
        //Creamos la coneccion a la base de datos
        Statement st = null;
        Connection conn=null;
        try {
            conn=ConectarBaseDeDatos.crearConeccion();
            st = conn.createStatement();
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error preparando el statment"+ANSI_RESET);
        }
        //Añadimos los campos en los que se quiere buscar
        String campossql;
        if (Objects.equals(campos[0], "-")) {
            campossql = "*";
        } else {
            campossql = campos[0];
            for (int i = 1; i < campos.length; i++) {
                campossql += "," + campos[i];
            }
        }
        if (Objects.equals(restriccionnes, "-")) {
            restriccionnes="";
        }else{
            restriccionnes=" WHERE "+restriccionnes;
        }
        //Preparamos la sentencia sql
        String sql = "SELECT " + campossql + " FROM " + tabla + " " + restriccionnes;
        //Ejecutamos la sentencia
        ResultSet resultado = null;
        try {
            assert st != null;
            resultado = st.executeQuery(sql);
            //No cerramos el statment, ya que sino nos dara un error al intentar leer el ResulSet
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error listando los datos"+ANSI_RESET);
        }
        //No podemos cerrar la coneccion ya que si la cerramos no se podra leer el resultset que devuelve, el recolector de basura de java la cerrar automaticamente
        return resultado;
    }

    /**
     * Se hara un update a la tabla que se indique, con los cambios que se indiquen y las condiciones que se indicquen
     *
     * @param tabla     sera el nombre de la tabla
     * @param seteos    seran los cambiios que se quieren realizar
     * @param condicion sera la condicion que se debe de cumplir en los campos que se quieren cambiar
     * @return devolvera el numero de filas afectadas
     */
    public static int update(String tabla, String[] seteos, String condicion) {
        int filasAfectadas=0;
        //Creamos la coneccion a la base de datos
        Statement st = null;
        Connection conn=null;
        try {
            conn=ConectarBaseDeDatos.crearConeccion();
            st = conn.createStatement();
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error preparando el statment"+ANSI_RESET);
        }
        //Preparamos la sentencia sql
        String sql = "UPDATE " + tabla + " SET " + seteos[0];
        for (int i = 1; i < seteos.length; i++) {
            sql += "," + seteos[i];
        }
        if(!Objects.equals(condicion, "-")){
            sql+=" WHERE "+condicion;
        }
        //Ejecutamos la sentencia sql

        try {
            assert st != null;
            filasAfectadas=st.executeUpdate(sql);
            System.out.println(filasAfectadas + " filas afectadas");
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error actualizando los datos"+ANSI_RESET);
        }finally {
            try {
                st.close();
                conn.close();
            }catch (SQLException e){
                System.out.println(ANSI_RED+"Error cerrando la coneccion"+ANSI_RESET);
            }
        }
        return filasAfectadas;
    }

    /**
     * Vacia todos los datos de una tabla
     * @param tabla tabla de la que se borraran los datos
     * @return devolvera true si se borraron los datos corrctamente y false si no
     */
    public static boolean vaciarTabla(String tabla) {
        boolean salida=false;
        //Creamos la coneccion a la base de datos
        Statement st = null;
        Connection conn = null;
        try {
            conn = ConectarBaseDeDatos.crearConeccion();
            st= conn.createStatement();
        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error preparando el statment"+ANSI_RESET);
        }
        //Preparamos la sentencia sql
        String sql = "DELETE FROM "+tabla;
        try {
            assert conn != null;
            int filasAfectadas = st.executeUpdate(sql);
            if(filasAfectadas>=0){
                salida=true;
            }
            System.out.println(filasAfectadas + " filas borradas");

        } catch (SQLException e) {
            System.out.println(ANSI_RED+"Error borrando los datos"+ANSI_RESET);

        }finally {
            try {
                st.close();
                conn.close();
            }catch (SQLException e){
                System.out.println(ANSI_RED+"Error cerrando la coneccion"+ANSI_RESET);
            }
        }
        return salida;
    }

}
