package org.herbertlacan.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.herbertlacan.sistema.Principal;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaCategorias() {
        escenarioPrincipal.ventanaCategorias();
    }

    public void ventanaMarca() {
        escenarioPrincipal.ventanaMarca();
    }

    public void ventanaCliente() {
        escenarioPrincipal.ventanaCliente();
    }

    public void ventanaTallas() {
        escenarioPrincipal.ventanaTallas();
    }

    public void ventanaProductos() {
        escenarioPrincipal.ventanaProductos();
    }

    public void ventanaTelefonoClientes() {
        escenarioPrincipal.ventanaTelefonoClientes();
    }

    public void ventanaCompras() {
        escenarioPrincipal.ventanaCompras();
    }

    public void ventanaDetalleCompras() {
        escenarioPrincipal.ventanaDetalleCompras();
    }

    public void ventanaDetalleFacturas() {
        escenarioPrincipal.ventanaDetalleFacturas();
    }

    public void ventanaEmailClientes() {
        escenarioPrincipal.ventanaEmailClientes();
    }
    
    public void ventanaEmailProveedores() {
        escenarioPrincipal.ventanaEmailProveedores();
    }
    
    public void ventanaFacturas(){
        escenarioPrincipal.ventanaFacturas();
    }
    
    public void ventanaProveedores(){
        escenarioPrincipal.ventanaProveedores();
    }
    public void ventanaTelefonoProveedores(){
        escenarioPrincipal.ventanaTelefonoProveedores();
    }
    
    public void About(){
        escenarioPrincipal.About();
    }
}
