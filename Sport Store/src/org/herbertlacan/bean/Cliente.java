package org.herbertlacan.bean;

public class Cliente {

    private int CodigoCliente;
    private String Nombre;
    private String Direccion;
    private String Nit;

    public Cliente() {
    }

   public Cliente(int CodigoCliente, String Nombre, String Direccion, String Nit) {
        this.CodigoCliente = CodigoCliente;
        this.Nombre = Nombre;
        this.Direccion = Direccion;
        this.Nit = Nit;
    }

    public int getCodigoCliente() {
        return CodigoCliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public String getNit() {
        return Nit;
    }

    public void setCodigoCliente(int CodigoCliente) {
        this.CodigoCliente = CodigoCliente;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public void setNit(String Nit) {
        this.Nit = Nit;
    }

    
    @Override
    public String toString(){
        return getCodigoCliente()+"";
    }

}

