package DAL;

import Entidades.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManejadoraCampeonato {

    /**
     * Insertara en la tabla equipo un objeto de tipo clsEquipo
     * @param equipo quipo a insertar
     * @return true o false, dependiendo de si se pudo insertar o no
     */
    public static boolean insertarEquipo(clsEquipo equipo){
        //Intentamos insertar en la tabla equipo los valores del objeto equipo
        return ManejadoraAuxiliar.insertarDatos("Equipo",new String[]{"idEquipo","ganados","empatados","perdidos","golesMarcados","golesRecibidos"},
                new String[]{equipo.getIdEquipo(),String.valueOf(equipo.getGanados()),String.valueOf(equipo.getEmpatados()),String.valueOf(equipo.getPerdidos()),
                String.valueOf(equipo.getGolesMarcados()),String.valueOf(equipo.getGolesRecibidos())});
    }

    /**
     * Insertara los resultados de un partido de octavos en la base de datos
     * @param partido sera el partido a insertar
     * @return true o false, dependiendo de si se pudo insertar o no
     */
    public static boolean insertarPartido(clsPartido partido, String tabla){
        //Intentamos insertar en la tabla octavos el partido
        return ManejadoraAuxiliar.insertarDatos(tabla,new String[]{"equipoA","equipoB","golesA","golesB"},new String[]{partido.getEquipoA(), partido.getEquipoB(),
                String.valueOf(partido.getGolesA()),String.valueOf(partido.getGolesB())});
    }

    /**
     * Devolvera una lista con todos los partidos que esten en la tabla que se indique, la tabla debera ser octavos, cuartos, semifinal, o final
     * @return lista de partidos
     */
    public static List<clsPartido> obtenerPartidos(String tabla){
        List<clsPartido>listaPartidos=new ArrayList<>();
        ResultSet partidos;
        //Listamos los contactos del usuario
        partidos=ManejadoraAuxiliar.listar(tabla,new String[]{"equipoA","equipoB","golesA","golesB"},"-");

        try {
            if (partidos.next()) {//Comprobamos que el ResultSet no este vacio
                do{
                    //Vamos guardando cadau no de los regtistros en la lista
                    clsPartido partido=new clsPartido();
                    partido.setEquipoA(partidos.getString("equipoA"));
                    partido.setEquipoB(partidos.getString("equipoB"));
                    partido.setGolesA(partidos.getInt("golesA"));
                    partido.setGolesB(partidos.getInt("golesB"));
                    listaPartidos.add(partido);
                }while (partidos.next());
                partidos.close();
            }
        } catch (SQLException e) {
            System.out.println("Error listando los datos");
            e.printStackTrace();
        }

        return listaPartidos;
    }

    /**
     * Buscara un equipo por el id en la base de datos
     * @param idEquipo el id por el que se quiere buscar a un equipo
     * @return equipo buscado
     */
    public clsEquipo obtenerEquipoPorId(String idEquipo){
        ResultSet resultado;
        //Recogemos un result set con el usuario
        resultado=ManejadoraAuxiliar.listar("Equipo",new String[]{"idEquipo","ganados","empatados","perdidos","golesMarcados","golesRecibidos"},"idEquipo='"+idEquipo+"'");
        //Guardamos lo recogido en un objeto clsUsuario
        clsEquipo equipo=new clsEquipo();
        try{
            while (resultado.next()){
                equipo.setIdEquipo(resultado.getString("idEquipo"));
                equipo.setGanados(resultado.getInt("ganados"));
                equipo.setEmpatados(resultado.getInt("empatados"));
                equipo.setPerdidos(resultado.getInt("perdidos"));
                equipo.setGolesMarcados(resultado.getInt("golesMarcados"));
                equipo.setGolesRecibidos(resultado.getInt("golesRecibidos"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return equipo;
    }

}
