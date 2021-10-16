package org.herbertlacan.bean;

public class DetalleCompra {
   private int CodigoDetalleCompra;
   private int Cantidad;
   private double CostoUnitario;
   private int CodigoProducto;
   private int NumeroDocumento;

    public DetalleCompra() {
    }

    public DetalleCompra(int CodigoDetalleCompra, int Cantidad, double CostoUnitario, int CodigoProducto, int NumeroDocumento) {
        this.CodigoDetalleCompra = CodigoDetalleCompra;
        this.Cantidad = Cantidad;
        this.CostoUnitario = CostoUnitario;
        this.CodigoProducto = CodigoProducto;
        this.NumeroDocumento = NumeroDocumento;
    }

    public int getCodigoDetalleCompra() {
        return CodigoDetalleCompra;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public double getCostoUnitario() {
        return CostoUnitario;
    }

    public int getCodigoProducto() {
        return CodigoProducto;
    }

    public int getNumeroDocumento() {
        return NumeroDocumento;
    }

    public void setCodigoDetalleCompra(int CodigoDetalleCompra) {
        this.CodigoDetalleCompra = CodigoDetalleCompra;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public void setCostoUnitario(double CostoUnitario) {
        this.CostoUnitario = CostoUnitario;
    }

    public void setCodigoProducto(int CodigoProducto) {
        this.CodigoProducto = CodigoProducto;
    }

    public void setNumeroDocumento(int NumeroDocumento) {
        this.NumeroDocumento = NumeroDocumento;
    }
   
   public String toString(){
       return getCodigoDetalleCompra()+"";
   }
}
