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
import org.herbertlacan.bean.Cliente;
import org.herbertlacan.bean.Factura;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class FacturaController implements Initializable {

    ClienteController cliente = new ClienteController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbFactura.setItems(getFactura());
        cmbCodigoCliente.setItems(getCliente());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Factura> listaFactura;
    private ObservableList<Cliente> listaCliente;

    @FXML
    private TextField txtFecha, txtNit, txtTotal, txtEstado;
    @FXML
    public ComboBox cmbFactura, cmbCodigoCliente;
    @FXML
    public TableView tblFacturas;
    @FXML
    private TableColumn colNumeroFactura, colFecha, colNit, colTotal, colEstado, colCodigoCliente;
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
        tblFacturas.setItems(getFactura());
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("NumeroFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Factura, String>("Fecha"));
        colNit.setCellValueFactory(new PropertyValueFactory<Factura, String>("Nit"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("Total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Factura, String>("Estado"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("CodigoCliente"));
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
    
    public ObservableList<Cliente> getCliente() {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Cliente(resultado.getInt("CodigoCliente"), resultado.getString("Nombre"), resultado.getString("Direccion"), resultado.getString("Nit")));

            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaCliente = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbFactura.getSelectionModel().select(buscarFactura(((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            txtFecha.setText(((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getFecha());
            txtNit.setText(((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getNit());
            txtTotal.setText(String.valueOf(((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getTotal()));
            txtEstado.setText(((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getEstado());
            cmbCodigoCliente.getSelectionModel().select(cliente.buscarCliente(((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        } catch (Exception e) {
        }
    }

    // Buscar Facturass
    public Factura buscarFactura(int codigoFactura) {
        Factura resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarFactura(?)}");
            procedimiento.setInt(1, codigoFactura);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Factura(registro.getInt("NumeroFactura"), registro.getString("Fecha"), registro.getString("Nit"), registro.getDouble("Total"), registro.getString("Estado"), registro.getInt("CodigoCliente"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Facturas a SQL
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

    //Agregar Facturas
    public void agregar() {
        Factura registro = new Factura();
        registro.setFecha(txtFecha.getText());
        registro.setNit(txtNit.getText());
        registro.setTotal(Double.valueOf(txtTotal.getText()));
        registro.setEstado(txtEstado.getText());
        registro.setCodigoCliente(((Cliente) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarFactura(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getFecha());
            procedimiento.setString(2, registro.getNit());
            procedimiento.setDouble(3, registro.getTotal());
            procedimiento.setString(4, registro.getEstado());
            procedimiento.setInt(5, registro.getCodigoCliente());
            procedimiento.execute();
            listaFactura.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Facturas
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblFacturas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtNit.setEditable(true);
                    txtEstado.setEditable(true);
                    txtTotal.setEditable(true);
                    txtFecha.setEditable(true);
                    cmbFactura.setDisable(false);
                    cmbCodigoCliente.setDisable(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Factura!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                    txtNit.setEditable(false);
                    txtEstado.setEditable(false);
                    txtTotal.setEditable(false);
                    txtFecha.setEditable(false);
                    cmbFactura.setDisable(true);
                    cmbCodigoCliente.setDisable(true);
                cargarDatos();
                break;
        }
    }

    //actualizar Facturas
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarFactura(?,?,?,?,?,?)}");
            Factura registro = (Factura) tblFacturas.getSelectionModel().getSelectedItem();
            registro.setNumeroFactura(((Factura) cmbFactura.getSelectionModel().getSelectedItem()).getNumeroFactura());
            registro.setFecha(txtFecha.getText());
            registro.setNit(txtNit.getText());
            registro.setTotal(Double.valueOf(txtTotal.getText()));
            registro.setEstado(txtEstado.getText());
            registro.setCodigoCliente(((Factura) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            procedimiento.setInt(1, registro.getNumeroFactura());
            procedimiento.setString(2, registro.getFecha());
            procedimiento.setString(3, registro.getNit());
            procedimiento.setDouble(4, registro.getTotal());
            procedimiento.setString(5, registro.getEstado());
            procedimiento.setInt(6, registro.getCodigoCliente());
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
                    if (tblFacturas.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarFactura(?)}");
                                procedimiento.setInt(1, ((Factura) tblFacturas.getSelectionModel().getSelectedItem()).getNumeroFactura());
                                procedimiento.execute();
                                listaFactura.remove(tblFacturas.getSelectionModel().getSelectedIndex());
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
                txtFecha.setEditable(false);
                txtNit.setEditable(false);
                txtTotal.setEditable(false);
                txtEstado.setEditable(false);
                cmbFactura.setDisable(true);
                cmbCodigoCliente.setDisable(true);
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
                txtFecha.setEditable(true);
                txtNit.setEditable(true);
                txtTotal.setEditable(true);
                txtEstado.setEditable(true);
                cmbFactura.setDisable(false);
                cmbCodigoCliente.setDisable(false);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtFecha.clear();
        txtNit.clear();
        txtTotal.clear();
        txtEstado.clear();
        cmbFactura.setValue("");
        cmbCodigoCliente.setValue("");
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
        txtFecha.setEditable(false);
        txtNit.setEditable(false);
        txtTotal.setEditable(false);
        txtEstado.setEditable(false);
        cmbFactura.setDisable(true);
        cmbCodigoCliente.setDisable(true);
    }
}
