package org.herbertlacan.bean;

public class Producto {

    private int CodigoProducto;
    private String Descripcion;
    private int Existencia;
    private String Imagen;
    private double PrecioUnitario;
    private double PrecioDocena;
    private double PrecioporMayor;
    private int CodigoCategoria;
    private int CodigoMarca;
    private int CodigoTalla;

    public Producto() {
    }

    public Producto(int CodigoProducto, String Descripcion, int Existencia, String Imagen, double PrecioUnitario, double PrecioDocena, double PrecioporMayor, int CodigoCategoria, int CodigoMarca, int CodigoTalla) {
        this.CodigoProducto = CodigoProducto;
        this.Descripcion = Descripcion;
        this.Existencia = Existencia;
        this.Imagen = Imagen;
        this.PrecioUnitario = PrecioUnitario;
        this.PrecioDocena = PrecioDocena;
        this.PrecioporMayor = PrecioporMayor;
        this.CodigoCategoria = CodigoCategoria;
        this.CodigoMarca = CodigoMarca;
        this.CodigoTalla = CodigoTalla;
    }

    public int getCodigoProducto() {
        return CodigoProducto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public int getExistencia() {
        return Existencia;
    }

    public String getImagen() {
        return Imagen;
    }

    public double getPrecioUnitario() {
        return PrecioUnitario;
    }

    public double getPrecioDocena() {
        return PrecioDocena;
    }

    public double getPrecioporMayor() {
        return PrecioporMayor;
    }

    public int getCodigoCategoria() {
        return CodigoCategoria;
    }

    public int getCodigoMarca() {
        return CodigoMarca;
    }

    public int getCodigoTalla() {
        return CodigoTalla;
    }

    public void setCodigoProducto(int CodigoProducto) {
        this.CodigoProducto = CodigoProducto;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setExistencia(int Existencia) {
        this.Existencia = Existencia;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }

    public void setPrecioUnitario(double PrecioUnitario) {
        this.PrecioUnitario = PrecioUnitario;
    }

    public void setPrecioDocena(double PrecioDocena) {
        this.PrecioDocena = PrecioDocena;
    }

    public void setPrecioporMayor(double PrecioporMayor) {
        this.PrecioporMayor = PrecioporMayor;
    }

    public void setCodigoCategoria(int CodigoCategoria) {
        this.CodigoCategoria = CodigoCategoria;
    }

    public void setCodigoMarca(int CodigoMarca) {
        this.CodigoMarca = CodigoMarca;
    }

    public void setCodigoTalla(int CodigoTalla) {
        this.CodigoTalla = CodigoTalla;
    }

    public String toString() {
        return getCodigoProducto() + "  -  " + getDescripcion();
    }

}
