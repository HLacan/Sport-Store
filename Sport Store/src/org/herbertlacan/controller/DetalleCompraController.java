package org.herbertlacan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.herbertlacan.bean.Compra;
import org.herbertlacan.bean.DetalleCompra;
import org.herbertlacan.bean.Producto;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class DetalleCompraController implements Initializable {

    CompraController compras = new CompraController();
    ProductoController productos = new ProductoController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoDetalleCompra.setItems(getDetalleCompra());
        cmbNumeroDocumento.setItems(getCompra());
        cmbCodigoProducto.setItems(getProducto());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleCompra> listaDetalleCompra;
    private ObservableList<Compra> listaCompra;
    private ObservableList<Producto> listaProducto;

    @FXML
    private TextField txtCantidad, txtCostoUnitario;
    @FXML
    public ComboBox cmbCodigoDetalleCompra, cmbCodigoProducto, cmbNumeroDocumento;
    @FXML
    public TableView tblDetalleCompras;
    @FXML
    private TableColumn colCodigo, colCantidad, colCostoUnitario, colCodigoProducto, colNumeroDocumento;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void MenuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void cargarDatos() {
        tblDetalleCompras.setItems(getDetalleCompra());
        colCodigo.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("CodigoDetalleCompra"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("Cantidad"));
        colCostoUnitario.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Double>("CostoUnitario"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("CodigoProducto"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("NumeroDocumento"));
    }

    public ObservableList<DetalleCompra> getDetalleCompra() {
        ArrayList<DetalleCompra> lista = new ArrayList<DetalleCompra>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarDetalleCompras}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleCompra(resultado.getInt("CodigoDetalleCompra"), resultado.getInt("Cantidad"), resultado.getDouble("CostoUnitario"), resultado.getInt("CodigoProducto"), resultado.getInt("NumeroDocumento")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaDetalleCompra = FXCollections.observableList(lista);
    }
    
    public ObservableList<Compra> getCompra() {
        ArrayList<Compra> lista = new ArrayList<Compra>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarCompras}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Compra(resultado.getInt("NumeroDocumento"), resultado.getString("Descripcion"), resultado.getDouble("Total"), resultado.getString("Fecha"), resultado.getInt("CodigoProveedor")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaCompra = FXCollections.observableList(lista);
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("CodigoProducto"), resultado.getString("Descripcion"), resultado.getInt("Existencia"), resultado.getString("Imagen"), resultado.getDouble("PrecioUnitario"), resultado.getDouble("PrecioDocena"), resultado.getDouble("PrecioporMayor"), resultado.getInt("CodigoCategoria"), resultado.getInt("CodigoMarca"), resultado.getInt("CodigoTalla")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaProducto = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbCodigoDetalleCompra.getSelectionModel().select(buscarDetalleCompra(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra()));
            txtCantidad.setText(String.valueOf(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCantidad()));
            txtCostoUnitario.setText(String.valueOf(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCostoUnitario()));
            cmbCodigoProducto.getSelectionModel().select(productos.buscarProducto(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            cmbNumeroDocumento.getSelectionModel().select(compras.buscarCompra(((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
        } catch (Exception e) {
        }
    }

    // Buscar DetalleComprass
    public DetalleCompra buscarDetalleCompra(int codigoDetalleCompra) {
        DetalleCompra resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarDetalleCompras(?)}");
            procedimiento.setInt(1, codigoDetalleCompra);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new DetalleCompra(registro.getInt("CodigoDetalleCompra"), registro.getInt("Cantidad"), registro.getDouble("CostoUnitario"), registro.getInt("CodigoProducto"), registro.getInt("NumeroDocumento"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones DetalleCompras a SQL
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            //Guardar
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setVisible(true);
                btnReporte.setVisible(true);
                cmbCodigoDetalleCompra.setDisable(false);
                cmbCodigoProducto.setDisable(false);
                cmbNumeroDocumento.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    //Agregar DetalleCompras
    public void agregar() {
        DetalleCompra registro = new DetalleCompra();
        registro.setCantidad(Integer.valueOf(txtCantidad.getText()));
        registro.setCostoUnitario(Double.valueOf(txtCostoUnitario.getText()));
        registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setNumeroDocumento(((Compra) cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarDetalleDeCompra(?,?,?,?)}");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setDouble(2, registro.getCostoUnitario());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.setInt(4, registro.getNumeroDocumento());
            procedimiento.execute();
            listaDetalleCompra.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar DetalleCompras
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblDetalleCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCantidad.setEditable(true);
                    txtCostoUnitario.setEditable(true);
                    cmbCodigoProducto.setDisable(false);
                    cmbNumeroDocumento.setDisable(false);
                    cmbCodigoDetalleCompra.setDisable(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un DetalleCompra!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtCantidad.setEditable(false);
                txtCostoUnitario.setEditable(false);
                cmbCodigoProducto.setDisable(false);
                cmbNumeroDocumento.setDisable(false);
                cmbCodigoDetalleCompra.setDisable(false);
                cargarDatos();
                break;
        }
    }

    //actualizar DetalleCompras
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarDetalleCompra(?,?,?,?,?)}");
            DetalleCompra registro = (DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleCompra(((DetalleCompra) cmbCodigoDetalleCompra.getSelectionModel().getSelectedItem()).getCodigoDetalleCompra());
            registro.setCantidad(Integer.valueOf(txtCantidad.getText()));
            registro.setCostoUnitario(Double.valueOf(txtCostoUnitario.getText()));
            registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setNumeroDocumento(((Compra) cmbNumeroDocumento.getSelectionModel().getSelectedItem()).getNumeroDocumento());
            procedimiento.setInt(1, registro.getCodigoDetalleCompra());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setDouble(3, registro.getCostoUnitario());
            procedimiento.setInt(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroDocumento());
            procedimiento.execute();
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    public void eliminar() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            cancelar();
        } else {
            tipoDeOperacion = operaciones.ELIMINAR;
            switch (tipoDeOperacion) {
                case ELIMINAR:
                    if (tblDetalleCompras.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleCompra(?)}");
                                procedimiento.setInt(1, ((DetalleCompra) tblDetalleCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                                procedimiento.execute();
                                listaDetalleCompra.remove(tblDetalleCompras.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                                cargarDatos();
                            } catch (SQLException SQLE) {
                                SQLE.printStackTrace();
                            }
                        }
                    }
            }
        }
        tipoDeOperacion = operaciones.NINGUNO;
    }

    public void reporte() {
        if (tipoDeOperacion == operaciones.ACTUALIZAR) {
            cancelar();
        }
        tipoDeOperacion = operaciones.NINGUNO;
    }

    //Desactivar los controles
    public void desactivarControles() {
        txtCantidad.setEditable(false);
        txtCostoUnitario.setEditable(false);
        cmbCodigoDetalleCompra.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        cmbNumeroDocumento.setDisable(true);
    }

    public void Vista() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
    }

    //Activar Botones
    public void activarControles() {
        txtCantidad.setEditable(true);
        txtCostoUnitario.setEditable(true);
        cmbCodigoDetalleCompra.setDisable(false);
        cmbCodigoProducto.setDisable(false);
        cmbNumeroDocumento.setDisable(false);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtCantidad.clear();
        txtCostoUnitario.clear();
        cmbCodigoDetalleCompra.setValue("");
        cmbCodigoProducto.setValue("");
        cmbNumeroDocumento.setValue("");
    }

    public void cancelar() {
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setText("Editar");
        btnReporte.setText("Reporte");
        btnNuevo.setVisible(true);
        btnEliminar.setVisible(true);
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);
        txtCantidad.setEditable(false);
        txtCostoUnitario.setEditable(false);
        cmbCodigoDetalleCompra.setDisable(false);
        cmbCodigoProducto.setDisable(false);
        cmbNumeroDocumento.setDisable(false);
    }
}
