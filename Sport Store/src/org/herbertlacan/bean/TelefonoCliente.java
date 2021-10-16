package org.herbertlacan.bean;

public class TelefonoCliente {
    private int CodigoTelefonoCliente;
    private String Descripcion;
    private int Telefono;
    private int CodigoCliente;

    public TelefonoCliente() {
    }

    public TelefonoCliente(int CodigoTelefonoCliente, String Descripcion, int Telefono, int CodigoCliente) {
        this.CodigoTelefonoCliente = CodigoTelefonoCliente;
        this.Descripcion = Descripcion;
        this.Telefono = Telefono;
        this.CodigoCliente = CodigoCliente;
    }

    public int getCodigoTelefonoCliente() {
        return CodigoTelefonoCliente;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public int getTelefono() {
        return Telefono;
    }

    public int getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoTelefonoCliente(int CodigoTelefonoCliente) {
        this.CodigoTelefonoCliente = CodigoTelefonoCliente;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setTelefono(int Telefono) {
        this.Telefono = Telefono;
    }

    public void setCodigoCliente(int CodigoCliente) {
        this.CodigoCliente = CodigoCliente;
    }
    
    @Override
    public String toString() {
        return getCodigoTelefonoCliente()+"";
    }

}
