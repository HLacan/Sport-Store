package org.herbertlacan.bean;

public class Categoria {

    private int CodigoCategoria;
    private String Descripcion;

    public Categoria() {
    }

    public Categoria(int CodigoCategoria, String Descripcion) {
        this.CodigoCategoria = CodigoCategoria;
        this.Descripcion = Descripcion;
    }

    public int getCodigoCategoria() {
        return CodigoCategoria;
    }

    public void setCodigoCategoria(int CodigoCategoria) {
        this.CodigoCategoria = CodigoCategoria;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    public String toString(){
        return getCodigoCategoria()+" - " + getDescripcion();
    }

}
