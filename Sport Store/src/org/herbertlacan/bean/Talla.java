package org.herbertlacan.bean;

public class Talla {

    private int CodigoTalla;
    private String Descripcion;

    public Talla() {
    }

    public Talla(int CodigoTalla, String Descripcion) {
        this.CodigoTalla = CodigoTalla;
        this.Descripcion = Descripcion;
    }

    public int getCodigoTalla() {
        return CodigoTalla;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setCodigoTalla(int CodigoTalla) {
        this.CodigoTalla = CodigoTalla;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String toString() {
        return getCodigoTalla() + "  -  " + getDescripcion();
    }
}
