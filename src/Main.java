import java.io.*;
import java.util.*;

import DAL.ManejadoraAuxiliar;
import DAL.ManejadoraCampeonato;
import Entidades.*;
public class Main {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static void main(String[] args) {
        //Sera la eleccion que elija el usuario
        int eleccion;
        eleccion=menu();
        //Mientras el usuario no quiera salir
        while(eleccion!=6){
            switch (eleccion){
                case 1:
                    if(ManejadoraCampeonato.obtenerPartidos("Octavos").size()==0) {
                        iniciarOctavos();
                    }else {
                        mostrarResultadosFase("Octavos");
                    }
                    break;
                case 2:
                    if(ManejadoraCampeonato.obtenerPartidos("Cuartos").size()==0) {
                        iniciarCuartos();
                    }else {
                        mostrarResultadosFase("Cuartos");
                    }
                    break;
                case 3:
                    if(ManejadoraCampeonato.obtenerPartidos("Semifinal").size()==0) {
                        iniciarSemifinales();
                    }else {
                        mostrarResultadosFase("Semifinal");
                    }
                    break;
                case 4:
                    if(ManejadoraCampeonato.obtenerPartidos("Final").size()==0) {
                        iniciarFinal();
                    }else {
                        mostrarResultadosFase("Final");
                    }
                    break;
                case 5:
                    reiniciar();
                    break;
            }
            eleccion=menu();
        }
    }

    /**
     * Mostrara un menu y recogera la respuesta del usuario, validando que sea una respuesta correcta
     * @return opcion elejida
     */
    private static int menu(){
        int eleccion;
        do {
            //Mostramos el menu
            System.out.println("Seleccione una opcion");
            System.out.println("---------------------");
            System.out.println("""
                    1-Iniciar Octavos
                    2-Iniciar Cuartos
                    3-Iniciar Semifinales
                    4-Iniciar final
                    5-Reiniciar
                    6-Salir""");
            System.out.println("---------------------");
            eleccion=leerEntero();
            //En caso de introducir los datos mas, se mostrara un mensaje de error
            if(eleccion<=0||eleccion>6){
                System.out.println(ANSI_RED+"Error, elija una de las opciones"+ANSI_RESET);
            }
        }while (eleccion<=0||eleccion>6);//Lo repetiremos hasta que se introduzca un valor valido
        return eleccion;
    }

    /**
     * Leera un entero por teclado, validando que el formato el correcto, es decir que se introduce un entero y no otro caracter
     * @return entero leido
     */
    private static int leerEntero(){
        Scanner sc=new Scanner(System.in);
        int entero=0;
        boolean salida=false;//Sera la variable que controlara si se ha introducido un valor valido o no

        while(!salida){//Pediremos un valor hasta que se introduzca un entero valido
            try{
                entero= sc.nextInt();
                salida=true;
            }catch (Exception e){
                salida=false;
                //Terminamos de leer la linea
                sc.nextLine();
                System.out.println(ANSI_RED+"Error, debe introducir un entero"+ANSI_RESET);
            }
        }
        return entero;
    }

    /**
     * Cogera 16 equipos del fichero equipos, los ordenara aleatoriamente, los insertara en la base de datos y realizara los partidos guardando
     * los resultados en la base de datos
     * Precondiciones: Abra 16 equipos insertados en la base de datos
     */
    private static void iniciarOctavos(){
        String linea;
        List<clsEquipo>equipos=new ArrayList<>();
        clsPartido partido;
        //Leemos los equipos del fichero
        try(BufferedReader reader=new BufferedReader(new FileReader("equipos"))){
            linea= reader.readLine();
            while (linea!=null){
                clsEquipo equipo=new clsEquipo(linea,0,0,0,0,0);
                equipos.add(equipo);
                //Insertamos el equipo en la base de datos
                ManejadoraCampeonato.insertarEquipo(equipo);
                linea=reader.readLine();
            }
            //Los ordenamos aleatoriamente
            Random rndm = new Random();
            Collections.shuffle(equipos, rndm);
            //Vamos a ir jugando los partidos con cada 2 equipos
            for (int i = 0; i < equipos.size(); i+=2) {
                partido=jugarPartido(equipos.get(i).getIdEquipo(),equipos.get(i+1).getIdEquipo());
                //Una vez jugado el partido, insertaremos los resultados en la base de datos
                ManejadoraCampeonato.insertarPartido(partido, "Octavos");
            }
        }catch (FileNotFoundException e){
            System.out.println(ANSI_RED+"No se encontro el archivo"+ANSI_RESET);
        }catch (IOException e){
            System.out.println(ANSI_RED+"Error leyendo los datos"+ANSI_RESET);
        }
    }

    /**
     * Cogera de la base de datos los equipos que ganaron en octavos, y simulara sus partidos guardando los resultados en la tabla cuartos
     * Precondiciones: Los octavos estaran insertados en la base de datos
     */
    private static void iniciarCuartos(){
        List<clsEquipo>equipos=new ArrayList<>();
        //Recorreremos los octavos de la base de datos, guardando a el equipo ganadro de cada partido en un array
        for (clsPartido partido:ManejadoraCampeonato.obtenerPartidos("Octavos")) {
            clsEquipo equipo=new clsEquipo();
            if(partido.getGolesA()>partido.getGolesB()){
                equipo.setIdEquipo(partido.getEquipoA());

            }else{
                equipo.setIdEquipo(partido.getEquipoB());
            }
            equipos.add(equipo);
        }

        clsPartido partido;
        //Vamos enlace a los equipos 2 a 2 u jugando ls partidos
        for (int i = 0; i < equipos.size(); i+=2) {
            partido=jugarPartido(equipos.get(i).getIdEquipo(),equipos.get(i+1).getIdEquipo());
            //Una vez jugado el partido, insertaremos los resultados en la base de datos
            ManejadoraCampeonato.insertarPartido(partido, "Cuartos");
        }

    }
    /**
     * Cogera de la base de datos los equipos que ganaron en cuartos, y simulara sus partidos guardando los resultados en la tabla semifinalesç
     * Precondiciones:Los cuartos deben estar insertados en la base de datos
     */
    private static void iniciarSemifinales(){
        List<clsEquipo>equipos=new ArrayList<>();
        //Recorreremos los cuartos de la base de datos, guardando a el equipo ganadro de cada partido en un array
        for (clsPartido partido:ManejadoraCampeonato.obtenerPartidos("Cuartos")) {
            clsEquipo equipo=new clsEquipo();
            if(partido.getGolesA()>partido.getGolesB()){
                equipo.setIdEquipo(partido.getEquipoA());

            }else{
                equipo.setIdEquipo(partido.getEquipoB());
            }
            equipos.add(equipo);
        }

        clsPartido partido;
        //Vamos enlace a los equipos 2 a 2 y jugando ls partidos
        for (int i = 0; i < equipos.size(); i+=2) {
            partido=jugarPartido(equipos.get(i).getIdEquipo(),equipos.get(i+1).getIdEquipo());
            //Una vez jugado el partido, insertaremos los resultados en la base de datos
            ManejadoraCampeonato.insertarPartido(partido, "Semifinal");
        }

    }
    /**
     * Cogera de la base de datos los equipos que ganaron en semifinales, y simulara sus partidos guardando los resultados en la tabla semifinales
     */
    private static void iniciarFinal(){
        List<clsEquipo>equipos=new ArrayList<>();
        //Recorreremos la semifinal de la base de datos, guardando a el equipo ganadro de cada partido en un array
        for (clsPartido partido:ManejadoraCampeonato.obtenerPartidos("Semifinal")) {
            clsEquipo equipo=new clsEquipo();
            if(partido.getGolesA()>partido.getGolesB()){
                equipo.setIdEquipo(partido.getEquipoA());
            }else{
                equipo.setIdEquipo(partido.getEquipoB());
            }
            equipos.add(equipo);
        }

        clsPartido partido;
        //Vamos enlace a los equipos 2 a 2 y jugando ls partidos
        for (int i = 0; i < equipos.size(); i+=2) {
            partido=jugarPartido(equipos.get(i).getIdEquipo(),equipos.get(i+1).getIdEquipo());
            //Una vez jugado el partido, insertaremos los resultados en la base de datos
            ManejadoraCampeonato.insertarPartido(partido, "Final");
        }

    }
    /**
     * Mostrara los resultados de los partidos de una fase
     * @param fase sera la fase de la que se mostraran los resultados
     */
    private static void mostrarResultadosFase(String fase){
        //Recorremos los partidos jugados de una fase mostrando los resultados
        List<clsPartido>partidos=ManejadoraCampeonato.obtenerPartidos(fase);
        for (clsPartido partido: partidos) {
            System.out.println(partido.getEquipoA()+" "+partido.getGolesA()+"-"+partido.getEquipoB()+" "+partido.getGolesB());
        }
    }

    /**
     * Borrara todos los datos de las tablas de la base de datos
     */
    private static void reiniciar(){
        //Vaciamos todas las tablas de la base de datos
            ManejadoraAuxiliar.vaciarTabla("Octavos");
            ManejadoraAuxiliar.vaciarTabla("Cuartos");
            ManejadoraAuxiliar.vaciarTabla("Semifinal");
            ManejadoraAuxiliar.vaciarTabla("Final");
            ManejadoraAuxiliar.vaciarTabla("Equipo");

    }

    /**
     * Simulara que se juega un partido calculando la posesion, la cual variara en cada minuto y la llegar al 95% marcara gol ese equipo, se jugara 90min y en caso de empate hasta que se desempate
     * @param equipoA uno de los equipos que va a jugar
     * @param equipoB uno de los equipos que va a jugar
     * @return resultados del partido jugado
     */
    private static clsPartido jugarPartido(String equipoA, String equipoB){
        clsPartido partido=new clsPartido(equipoA,equipoB,0,0);
        int minuto=0;//Esta variable controlara cuando acaba el partido
        int posesionA=50, posesionB=50, rndmA, rndmB, dif;
        System.out.println("--Inicio del partido "+equipoA+" vs "+equipoB);
        //Lo repetiremos hasta el munuto 90
        while (minuto<=90||partido.getGolesA()==partido.getGolesB()){
            //Comprobamos cuanto va acambiar la posesion este minuto
            rndmA=(int)(Math.random()*15)+1;
            rndmB=(int)(Math.random()*15)+1;
            dif=rndmA-rndmB;
            posesionA+=dif;
            posesionB-=dif;
            System.out.println(equipoA+" "+posesionA+"% - "+equipoB+" "+posesionB+"% minuto "+minuto);
            //Comprobamos si alguno tiene que mete gol
            if(posesionA>=95){
                partido.setGolesA(partido.getGolesA()+1);
                //Actualizamos lso contadores de la base de las datos
                ManejadoraAuxiliar.update("Equipo",new String[]{"golesMarcados=golesMarcados+1"},"idEquipo='"+equipoA+"'");
                ManejadoraAuxiliar.update("Equipo",new String[]{"golesRecibidos=golesRecibidos+1"},"idEquipo='"+equipoB+"'");
                posesionA=50;
                posesionB=50;
                System.out.println("GOOOOOOOOL de "+equipoA);
            } else if (posesionB>=95) {
                partido.setGolesB(partido.getGolesB()+1);
                //Actualizamos lso contadores de la base de las datos
                ManejadoraAuxiliar.update("Equipo",new String[]{"golesMarcados=golesMarcados+1"},"idEquipo='"+equipoB+"'");
                ManejadoraAuxiliar.update("Equipo",new String[]{"golesRecibidos=golesRecibidos+1"},"idEquipo='"+equipoA+"'");
                posesionA=50;
                posesionB=50;
                System.out.println("GOOOOOOOOL de "+equipoB);
            }
            ++minuto;
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println(ANSI_RED+"Error en el tiempo de espera"+ANSI_RESET);
            }
        }

        //Actualizaremos los partidos ganados y perdiso de la base de datos
        if(partido.getGolesA()>partido.getGolesB()){
            ManejadoraAuxiliar.update("Equipo", new String[]{"ganados=ganados+1"},"idEquipo='"+equipoA+"'");
            ManejadoraAuxiliar.update("Equipo", new String[]{"perdidos=perdidos+1"},"idEquipo='"+equipoB+"'");
        }else{
            ManejadoraAuxiliar.update("Equipo", new String[]{"ganados=ganados+1"},"idEquipo='"+equipoB+"'");
            ManejadoraAuxiliar.update("Equipo", new String[]{"perdidos=perdidos+1"},"idEquipo='"+equipoA+"'");
        }
        return partido;
    }
}