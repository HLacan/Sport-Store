package org.herbertlacan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.herbertlacan.bean.Proveedor;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class CompraController implements Initializable {

    ProveedorController proveedor = new ProveedorController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNumeroDocumento.setItems(getCompra());
        cmbCodigoProveedor.setItems(getProveedor());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Compra> listaCompra;
    private ObservableList<Proveedor> listaProveedor;

    @FXML
    private TextField txtNumeroDocumento, txtDescripcion, txtTotal, txtFecha;
    @FXML
    public ComboBox cmbCodigoProveedor, cmbNumeroDocumento;
    @FXML
    public TableView tblCompras;
    @FXML
    private TableColumn colNumeroDocumento, colDescripcion, colTotal, colFecha, colCodigoProveedor;
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
        tblCompras.setItems(getCompra());
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("NumeroDocumento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compra, String>("Descripcion"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Compra, Double>("Total"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Compra, String>("Fecha"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("CodigoProveedor"));
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
    
    public ObservableList<Proveedor> getProveedor() {
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Proveedor(
                resultado.getInt("CodigoProveedor"), 
                resultado.getString("Direccion"), 
                resultado.getString("Nit"), 
                resultado.getString("RazonSocial"), 
                resultado.getString("PaginaWeb")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaProveedor = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbNumeroDocumento.getSelectionModel().select(buscarCompra(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
            txtNumeroDocumento.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento()));
            txtDescripcion.setText(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getDescripcion());
            txtTotal.setText(String.valueOf(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getTotal()));
            txtFecha.setText(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getFecha());
            cmbCodigoProveedor.getSelectionModel().select(proveedor.buscarProveedor(((Compra) tblCompras.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        } catch (Exception e) {
        }
    }

    // Buscar Comprass
    public Compra buscarCompra(int codigoCompra) {
        Compra resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCompra(?)}");
            procedimiento.setInt(1, codigoCompra);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Compra(registro.getInt("NumeroDocumento"), registro.getString("Descripcion"), registro.getDouble("Total"), registro.getString("Fecha"), registro.getInt("CodigoProveedor"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Compras a SQL
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setVisible(false);
                btnReporte.setVisible(false);
                txtTotal.setDisable(true);
                txtTotal.setText("0.00");
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
                txtTotal.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    //Agregar Compras
    public void agregar() {
        Compra registro = new Compra();
        registro.setNumeroDocumento(Integer.valueOf(txtNumeroDocumento.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTotal(Double.valueOf(txtTotal.getText()));
        registro.setFecha(txtFecha.getText());
        registro.setCodigoProveedor(((Proveedor) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarCompra(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setDouble(3, registro.getTotal());
            procedimiento.setString(4, registro.getFecha());
            procedimiento.setInt(5, registro.getCodigoProveedor());
            procedimiento.execute();
            listaCompra.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Compras
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtNumeroDocumento.setEditable(true);
                    txtDescripcion.setEditable(true);
                    txtTotal.setDisable(true);
                    txtFecha.setEditable(true);
                    cmbCodigoProveedor.setDisable(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Compra!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtNumeroDocumento.setEditable(false);
                txtDescripcion.setEditable(false);
                txtTotal.setDisable(false);
                txtTotal.setEditable(false);
                txtFecha.setEditable(false);
                cmbCodigoProveedor.setDisable(false);
                cargarDatos();
                break;
        }
    }

    //actualizar Compras
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCompra(?,?,?,?,?)}");
            Compra registro = (Compra) tblCompras.getSelectionModel().getSelectedItem();
            registro.setCodigoProveedor(((Proveedor) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setNumeroDocumento(Integer.valueOf(txtNumeroDocumento.getText()));
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTotal(Double.valueOf(txtTotal.getText()));
            procedimiento.setInt(1, registro.getNumeroDocumento());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setDouble(3, registro.getTotal());
            procedimiento.setString(4, registro.getFecha());
            procedimiento.setInt(5, registro.getCodigoProveedor());
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
                    if (tblCompras.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCompras(?)}");
                                procedimiento.setInt(1, ((Compra) tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento());
                                procedimiento.execute();
                                listaCompra.remove(tblCompras.getSelectionModel().getSelectedIndex());
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

    public void generarReporte() {
        if (tipoDeOperacion == operaciones.ACTUALIZAR) {
            cancelar();
        }else{
        
        Map parametros = new HashMap();
        //Mandar Numero Documento
        int numeroDocumento = ((Compra)tblCompras.getSelectionModel().getSelectedItem()).getNumeroDocumento();
        parametros.put("_NumeroDocumento", numeroDocumento);
        //GenerarReporte.mostrarReporte("GenerarReporte.jasper","Reporte de Compras",parametros);
        }
        tipoDeOperacion = operaciones.NINGUNO;
    }

    //Desactivar los controles
    public void desactivarControles() {
        txtNumeroDocumento.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotal.setEditable(false);
        txtFecha.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
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
        txtNumeroDocumento.setEditable(true);
        txtDescripcion.setEditable(true);
        txtTotal.setEditable(true);
        txtFecha.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtNumeroDocumento.clear();
        txtDescripcion.clear();
        txtTotal.clear();
        txtFecha.clear();
        cmbCodigoProveedor.setValue("");
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
        txtNumeroDocumento.setEditable(false);
        txtDescripcion.setEditable(false);
        txtTotal.setEditable(false);
        txtFecha.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }
}
