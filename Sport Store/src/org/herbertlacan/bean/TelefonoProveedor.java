package org.herbertlacan.bean;

public class TelefonoProveedor {
    private int CodigoTelefonoProveedor;
    private String Descripcion;
    private int Telefono;
    private int CodigoProveedor;

    public TelefonoProveedor() {
    }

    public TelefonoProveedor(int CodigoTelefonoProveedor, String Descripcion, int Telefono, int CodigoProveedor) {
        this.CodigoTelefonoProveedor = CodigoTelefonoProveedor;
        this.Descripcion = Descripcion;
        this.Telefono = Telefono;
        this.CodigoProveedor = CodigoProveedor;
    }

    public int getCodigoTelefonoProveedor() {
        return CodigoTelefonoProveedor;
    }

    public void setCodigoTelefonoProveedor(int CodigoTelefonoProveedor) {
        this.CodigoTelefonoProveedor = CodigoTelefonoProveedor;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getTelefono() {
        return Telefono;
    }

    public void setTelefono(int Telefono) {
        this.Telefono = Telefono;
    }

    public int getCodigoProveedor() {
        return CodigoProveedor;
    }

    public void setCodigoProveedor(int CodigoProveedor) {
        this.CodigoProveedor = CodigoProveedor;
    }
    
    public String toString(){
        return getCodigoTelefonoProveedor()+"";
    }
}
