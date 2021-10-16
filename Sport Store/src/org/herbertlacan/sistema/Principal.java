package org.herbertlacan.sistema;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import org.herbertlacan.controller.CategoriaController;
import org.herbertlacan.controller.MenuPrincipalController;
import org.herbertlacan.controller.ClienteController;
import org.herbertlacan.controller.CompraController;
import org.herbertlacan.controller.DetalleCompraController;
import org.herbertlacan.controller.DetalleFacturaController;
import org.herbertlacan.controller.EmailClienteController;
import org.herbertlacan.controller.EmailProveedorController;
import org.herbertlacan.controller.FacturaController;
import org.herbertlacan.controller.MarcaController;
import org.herbertlacan.controller.ProductoController;
import org.herbertlacan.controller.ProveedorController;
import org.herbertlacan.controller.TallaController;
import org.herbertlacan.controller.TelefonoClienteController;
import org.herbertlacan.controller.TelefonoProveedorController;

/**
 *
 * @author informatica
 */
public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/herbertlacan/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Sport Store");
        escenarioPrincipal.resizableProperty().setValue(Boolean.FALSE);
        escenarioPrincipal.getIcons().add(new Image("org/herbertlacan/images/pelota.png"));
        menuPrincipal();
        escenarioPrincipal.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    
    public void About(){
    JOptionPane.showMessageDialog(null,"Creado por: \nHerbert Rafael Lacan Hernandez \n2016136 \nIN5AV");
    }
    
    public void ventanaMarca() {
        try {
            MarcaController marcaController = (MarcaController) cambiarEscena("MarcasView.fxml", 600, 420);
            marcaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 600, 420);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaCategorias() {
        try {
            CategoriaController categoriaController = (CategoriaController) cambiarEscena("CategoriasView.fxml", 600, 420);
            categoriaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaCliente() {
        try {
            ClienteController clienteController = (ClienteController) cambiarEscena("ClientesView.fxml", 600, 550);
            clienteController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTallas() {
        try {
            TallaController tallaController = (TallaController) cambiarEscena("TallasView.fxml", 600, 420);
            tallaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProductos() {
        try {
            ProductoController productoController = (ProductoController) cambiarEscena("ProductosView.fxml", 800, 620);
            productoController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaCompras() {
        try {
            CompraController compraController = (CompraController) cambiarEscena("ComprasView.fxml", 600, 525);
            compraController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaDetalleCompras() {
        try {
            DetalleCompraController detalleCompraController = (DetalleCompraController) cambiarEscena("DetalleDeComprasView.fxml", 600, 500);
            detalleCompraController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTelefonoClientes() {
        try {
            TelefonoClienteController telefonoClientesController = (TelefonoClienteController) cambiarEscena("TelefonoClientesView.fxml", 600, 420);
            telefonoClientesController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaDetalleFacturas() {
        try {
            DetalleFacturaController detalleFacturaController = (DetalleFacturaController) cambiarEscena("DetalleDeFacturasView.fxml", 600, 520);
            detalleFacturaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmailClientes() {
        try {
            EmailClienteController emailClienteController = (EmailClienteController) cambiarEscena("EmailClientesView.fxml", 600, 420);
            emailClienteController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmailProveedores() {
        try {
            EmailProveedorController emailProveedorController = (EmailProveedorController) cambiarEscena("EmailProveedoresView.fxml", 600, 420);
            emailProveedorController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaFacturas() {
        try {
            FacturaController facturaController = (FacturaController) cambiarEscena("FacturasView.fxml", 600, 500);
            facturaController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProveedores() {
        try {
            ProveedorController provedorController = (ProveedorController) cambiarEscena("ProveedoresView.fxml", 600, 500);
            provedorController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTelefonoProveedores() {
        try {
            TelefonoProveedorController telefonoProveedorController = (TelefonoProveedorController) cambiarEscena("TelefonoProveedoresView.fxml", 600, 420);
            telefonoProveedorController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
}
