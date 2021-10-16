package org.herbertlacan.bean;

public class Proveedor {
    private int CodigoProveedor;
    private String Direccion;
    private String Nit;
    private String RazonSocial;
    private String PaginaWeb;

    public Proveedor() {
    }

    public Proveedor(int CodigoProveedor, String Direccion, String Nit, String RazonSocial, String PaginaWeb) {
        this.CodigoProveedor = CodigoProveedor;
        this.Direccion = Direccion;
        this.Nit = Nit;
        this.RazonSocial = RazonSocial;
        this.PaginaWeb = PaginaWeb;
    }

    public int getCodigoProveedor() {
        return CodigoProveedor;
    }

    public void setCodigoProveedor(int CodigoProveedor) {
        this.CodigoProveedor = CodigoProveedor;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getNit() {
        return Nit;
    }

    public void setNit(String Nit) {
        this.Nit = Nit;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String RazonSocial) {
        this.RazonSocial = RazonSocial;
    }

    public String getPaginaWeb() {
        return PaginaWeb;
    }

    public void setPaginaWeb(String PaginaWeb) {
        this.PaginaWeb = PaginaWeb;
    }
    
    public String toString(){
        return getCodigoProveedor()+"  -  " + getRazonSocial();
    }
}
