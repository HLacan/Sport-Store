package org.herbertlacan.bean;

public class EmailCliente {
    private int CodigoEmailCliente;
    private String Descripcion;
    private String Email;
    private int CodigoCliente;

    public EmailCliente() {
    }

    public EmailCliente(int CodigoEmailCliente, String Descripcion, String Email, int CodigoCliente) {
        this.CodigoEmailCliente = CodigoEmailCliente;
        this.Descripcion = Descripcion;
        this.Email = Email;
        this.CodigoCliente = CodigoCliente;
    }

    public int getCodigoEmailCliente() {
        return CodigoEmailCliente;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getEmail() {
        return Email;
    }

    public int getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoEmailCliente(int CodigoEmailCliente) {
        this.CodigoEmailCliente = CodigoEmailCliente;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setCodigoCliente(int CodigoCliente) {
        this.CodigoCliente = CodigoCliente;
    }
    
    @Override
    public String toString() {
        return getCodigoEmailCliente() + "";
    }
}
