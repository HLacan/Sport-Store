package org.herbertlacan.bean;

public class Factura {
    private int NumeroFactura;
    private String Fecha;
    private String Nit;
    private double Total;
    private String estado;
    private int CodigoCliente;

    public Factura() {
    }

    public Factura(int NumeroFactura, String Fecha, String Nit, double Total, String estado, int CodigoCliente) {
        this.NumeroFactura = NumeroFactura;
        this.Fecha = Fecha;
        this.Nit = Nit;
        this.Total = Total;
        this.estado = estado;
        this.CodigoCliente = CodigoCliente;
    }

    public int getNumeroFactura() {
        return NumeroFactura;
    }

    public void setNumeroFactura(int NumeroFactura) {
        this.NumeroFactura = NumeroFactura;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public String getNit() {
        return Nit;
    }

    public void setNit(String Nit) {
        this.Nit = Nit;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoCliente(int CodigoCliente) {
        this.CodigoCliente = CodigoCliente;
    }
    
    public String toString(){
        return getNumeroFactura()+"";
    }
}
