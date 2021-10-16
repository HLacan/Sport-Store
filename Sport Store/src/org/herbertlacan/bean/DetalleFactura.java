package org.herbertlacan.bean;

public class DetalleFactura {
    private int CodigoDetalleFactura;
    private int Cantidad;
    private double Precio;
    private int NumeroFactura;
    private int CodigoProducto;

    public DetalleFactura() {
    }

    public DetalleFactura(int CodigoDetalleFactura, int Cantidad, double Precio, int NumeroFactura, int CodigoProducto) {
        this.CodigoDetalleFactura = CodigoDetalleFactura;
        this.Cantidad = Cantidad;
        this.Precio = Precio;
        this.NumeroFactura = NumeroFactura;
        this.CodigoProducto = CodigoProducto;
    }

    public int getCodigoDetalleFactura() {
        return CodigoDetalleFactura;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public double getPrecio() {
        return Precio;
    }

    public int getNumeroFactura() {
        return NumeroFactura;
    }

    public int getCodigoProducto() {
        return CodigoProducto;
    }

    public void setCodigoDetalleFactura(int CodigoDetalleFactura) {
        this.CodigoDetalleFactura = CodigoDetalleFactura;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    public void setNumeroFactura(int NumeroFactura) {
        this.NumeroFactura = NumeroFactura;
    }

    public void setCodigoProducto(int CodigoProducto) {
        this.CodigoProducto = CodigoProducto;
    }
    
    public String toString(){
        return getCodigoDetalleFactura() + "";
    }
}
