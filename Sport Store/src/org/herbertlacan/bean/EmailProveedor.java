package org.herbertlacan.bean;

public class EmailProveedor {

    private int CodigoEmailProveedor;
    private String Descripcion;
    private String Email;
    private int CodigoProveedor;

    public EmailProveedor() {
    }

    public EmailProveedor(int CodigoEmailProveedor, String Descripcion, String Email, int CodigoProveedor) {
        this.CodigoEmailProveedor = CodigoEmailProveedor;
        this.Descripcion = Descripcion;
        this.Email = Email;
        this.CodigoProveedor = CodigoProveedor;
    }

    public int getCodigoEmailProveedor() {
        return CodigoEmailProveedor;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getEmail() {
        return Email;
    }

    public int getCodigoProveedor() {
        return CodigoProveedor;
    }

    public void setCodigoEmailProveedor(int CodigoEmailProveedor) {
        this.CodigoEmailProveedor = CodigoEmailProveedor;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setCodigoProveedor(int CodigoProveedor) {
        this.CodigoProveedor = CodigoProveedor;
    }

    @Override
    public String toString() {
        return getCodigoEmailProveedor() + "";
    }
}
