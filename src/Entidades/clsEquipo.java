package Entidades;

public class clsEquipo {
    //region Atributos
    String idEquipo;
    int ganados, empatados, perdidos, golesMarcados, golesRecibidos;
    //endregion
    //region Constructores
    public clsEquipo(String idEquipo, int ganados, int empatados, int perdidos, int golesMarcados, int golesRecibidos) {
        this.idEquipo = idEquipo;
        this.ganados = ganados;
        this.empatados = empatados;
        this.perdidos = perdidos;
        this.golesMarcados = golesMarcados;
        this.golesRecibidos = golesRecibidos;
    }
    public clsEquipo(){
        this.idEquipo="";
        this.ganados=0;
        this.empatados=0;
        this.perdidos=0;
        this.golesMarcados=0;
        this.golesRecibidos=0;
    }

    //endregion
    //region Getters y setters

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getGanados() {
        return ganados;
    }

    public void setGanados(int ganados) {
        this.ganados = ganados;
    }

    public int getEmpatados() {
        return empatados;
    }

    public void setEmpatados(int empatados) {
        this.empatados = empatados;
    }

    public int getPerdidos() {
        return perdidos;
    }

    public void setPerdidos(int perdidos) {
        this.perdidos = perdidos;
    }

    public int getGolesMarcados() {
        return golesMarcados;
    }

    public void setGolesMarcados(int golesMarcados) {
        this.golesMarcados = golesMarcados;
    }

    public int getGolesRecibidos() {
        return golesRecibidos;
    }

    public void setGolesRecibidos(int golesRecibidos) {
        this.golesRecibidos = golesRecibidos;
    }

    //endregion
}
