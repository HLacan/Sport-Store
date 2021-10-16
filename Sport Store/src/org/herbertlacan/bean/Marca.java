
package org.herbertlacan.bean;

public class Marca {
    private int CodigoMarca;
    private String Descripcion;

    public Marca() {
    }

    public Marca(int CodigoMarca, String Descripcion) {
        this.CodigoMarca = CodigoMarca;
        this.Descripcion = Descripcion;
    }

    public int getCodigoMarca() {
        return CodigoMarca;
    }

    public void setCodigoMarca(int CodigoMarca) {
        this.CodigoMarca = CodigoMarca;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
    
    public String toString(){
        return getCodigoMarca()+ "  -  " + getDescripcion();
    }
}
