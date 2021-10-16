package org.herbertlacan.bean;

public class Compra {
    private int NumeroDocumento;
    private String Descripcion;
    private double Total;
    private String Fecha;
    private int CodigoProveedor;

    public Compra() {
    }

    public Compra(int NumeroDocumento, String Descripcion, double Total, String Fecha, int CodigoProveedor) {
        this.NumeroDocumento = NumeroDocumento;
        this.Descripcion = Descripcion;
        this.Total = Total;
        this.Fecha = Fecha;
        this.CodigoProveedor = CodigoProveedor;
    }

    public int getNumeroDocumento() {
        return NumeroDocumento;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public double getTotal() {
        return Total;
    }

    public String getFecha() {
        return Fecha;
    }

    public int getCodigoProveedor() {
        return CodigoProveedor;
    }

    public void setNumeroDocumento(int NumeroDocumento) {
        this.NumeroDocumento = NumeroDocumento;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public void setCodigoProveedor(int CodigoProveedor) {
        this.CodigoProveedor = CodigoProveedor;
    }
    
    public String toString(){
        return getNumeroDocumento()+"";
    }
}
