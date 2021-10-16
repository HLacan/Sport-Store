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
import org.herbertlacan.bean.DetalleFactura;
import org.herbertlacan.bean.Producto;
import org.herbertlacan.bean.Factura;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class DetalleFacturaController implements Initializable {

    ProductoController producto = new ProductoController();
    FacturaController factura = new FacturaController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbDetalleFactura.setItems(getDetalleFactura());
        cmbCodigoProducto.setItems(getProducto());
        cmbNumeroFactura.setItems(getFactura());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleFactura> listaDetalleFactura;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Factura> listaFactura;

    @FXML
    private TextField txtCantidad, txtPrecio;
    @FXML
    public ComboBox cmbDetalleFactura, cmbCodigoProducto, cmbNumeroFactura;
    @FXML
    public TableView tblDetalleFacturas;
    @FXML
    private TableColumn colCodigo, colCantidad, colPrecio, colNumeroFactura, colCodigoProducto;
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
        tblDetalleFacturas.setItems(getDetalleFactura());
        colCodigo.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("CodigoDetalleFactura"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("Cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Double>("Precio"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("CodigoProducto"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("NumeroFactura"));
    }

    public ObservableList<DetalleFactura> getDetalleFactura() {
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarDetalleFacturas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new DetalleFactura(resultado.getInt("CodigoDetalleFactura"), resultado.getInt("Cantidad"), resultado.getDouble("Precio"), resultado.getInt("CodigoProducto"), resultado.getInt("NumeroFactura")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaDetalleFactura = FXCollections.observableList(lista);
    }
    
     public ObservableList<Factura> getFactura() {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarFacturas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Factura(resultado.getInt("NumeroFactura"), resultado.getString("Fecha"), resultado.getString("Nit"), resultado.getDouble("Total"), resultado.getString("Estado"), resultado.getInt("CodigoCliente")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaFactura = FXCollections.observableList(lista);
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
            cmbDetalleFactura.getSelectionModel().select(buscarDetalleFactura(((DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura()));
            txtCantidad.setText(String.valueOf(((DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem()).getCantidad()));
            txtPrecio.setText(String.valueOf(((DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem()).getPrecio()));
            cmbCodigoProducto.getSelectionModel().select(producto.buscarProducto(((DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            cmbNumeroFactura.getSelectionModel().select(factura.buscarFactura(((DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem()).getNumeroFactura()));
        } catch (Exception e) {
        }
    }

    // Buscar DetalleFacturass
    public DetalleFactura buscarDetalleFactura(int codigoDetalleFactura) {
        DetalleFactura resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarDetalleFacturas(?)}");
            procedimiento.setInt(1, codigoDetalleFactura);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new DetalleFactura(registro.getInt("CodigoDetalleFactura"), registro.getInt("Cantidad"), registro.getDouble("Precio"), registro.getInt("CodigoProducto"), registro.getInt("NumeroFactura"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones DetalleFacturas a SQL
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                txtPrecio.setText("0.00");
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    //Agregar DetalleFacturas
    public void agregar() {
        DetalleFactura registro = new DetalleFactura();
        registro.setCantidad(Integer.valueOf(txtCantidad.getText()));
        registro.setPrecio(Double.valueOf(txtPrecio.getText()));
        registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        registro.setNumeroFactura(((Factura) cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarDetalleFactura(?,?,?,?)}");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setDouble(2, registro.getPrecio());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.setInt(4, registro.getNumeroFactura());
            procedimiento.execute();
            listaDetalleFactura.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar DetalleFacturas
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblDetalleFacturas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtCantidad.setEditable(true);
                    txtPrecio.setEditable(true);
                    cmbCodigoProducto.setDisable(false);
                    cmbNumeroFactura.setDisable(false);
                    cmbDetalleFactura.setDisable(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un DetalleFactura!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                txtCantidad.setEditable(false);
                txtPrecio.setEditable(false);
                cmbCodigoProducto.setDisable(true);
                cmbNumeroFactura.setDisable(true);
                cmbDetalleFactura.setDisable(true);
                cargarDatos();
                break;
        }
    }

    //actualizar DetalleFacturas
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarDetalleFactura(?,?,?,?)}");
            DetalleFactura registro = (DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem();
            registro.setCodigoDetalleFactura(((DetalleFactura) cmbDetalleFactura.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
            registro.setCantidad(Integer.valueOf(txtCantidad.getText()));
            registro.setPrecio(Double.valueOf(txtPrecio.getText()));
            registro.setCodigoProducto(((DetalleFactura) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setNumeroFactura(((DetalleFactura) cmbNumeroFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            procedimiento.setInt(1, registro.getCodigoDetalleFactura());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setDouble(3, registro.getPrecio());
            procedimiento.setInt(4, registro.getCodigoProducto());
            procedimiento.setInt(5, registro.getNumeroFactura());
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
                    if (tblDetalleFacturas.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarDetalleFactura(?)}");
                                procedimiento.setInt(1, ((DetalleFactura) tblDetalleFacturas.getSelectionModel().getSelectedItem()).getCodigoDetalleFactura());
                                procedimiento.execute();
                                listaDetalleFactura.remove(tblDetalleFacturas.getSelectionModel().getSelectedIndex());
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
        txtPrecio.setEditable(false);
        cmbDetalleFactura.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        cmbNumeroFactura.setDisable(true);
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
        txtPrecio.setEditable(true);
        cmbDetalleFactura.setDisable(false);
        cmbCodigoProducto.setDisable(false);
        cmbNumeroFactura.setDisable(false);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtCantidad.clear();
        txtPrecio.clear();
        cmbDetalleFactura.setValue("");
        cmbCodigoProducto.setValue("");
        cmbNumeroFactura.setValue("");
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
        txtPrecio.setEditable(false);
        cmbDetalleFactura.setDisable(true);
        cmbCodigoProducto.setDisable(true);
        cmbNumeroFactura.setDisable(true);
    }
}
