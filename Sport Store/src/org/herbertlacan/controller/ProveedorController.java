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
import org.herbertlacan.bean.Proveedor;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class ProveedorController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbProveedor.setItems(getProveedor());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Proveedor> listaProveedor;

    @FXML
    private TextField txtDireccion, txtNit, txtRazonSocial, txtPaginaWeb;
    @FXML
    public ComboBox cmbProveedor;
    @FXML
    public TableView tblProveedores;
    @FXML
    private TableColumn colCodigo, colDireccion, colNit, colRazon, colPagina;
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
        tblProveedores.setItems(getProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("CodigoProveedor"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("Direccion"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("Nit"));
        colRazon.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("RazonSocial"));
        colPagina.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("paginaWeb"));
    }

    public ObservableList<Proveedor> getProveedor() {
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Proveedor(resultado.getInt("CodigoProveedor"), resultado.getString("Direccion"), resultado.getString("Nit"), resultado.getString("RazonSocial"), resultado.getString("PaginaWeb")));

            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaProveedor = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbProveedor.getSelectionModel().select(buscarProveedor(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            txtDireccion.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
            txtNit.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getNit());
            txtRazonSocial.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
            txtPaginaWeb.setText(((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
        } catch (Exception e) {
        }
    }

    // Buscar Proveedoress
    public Proveedor buscarProveedor(int codigoProveedor) {
        Proveedor resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarProveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Proveedor(registro.getInt("CodigoProveedor"), registro.getString("Direccion"), registro.getString("Nit"), registro.getString("RazonSocial"), registro.getString("PaginaWeb"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Proveedores a SQL
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

    //Agregar Proveedores
    public void agregar() {
        Proveedor registro = new Proveedor();
        registro.setDireccion(txtDireccion.getText());
        registro.setNit(txtNit.getText());
        registro.setRazonSocial(txtRazonSocial.getText());
        registro.setPaginaWeb(txtPaginaWeb.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarProveedor(?,?,?,?)}");
            procedimiento.setString(1, registro.getDireccion());
            procedimiento.setString(2, registro.getNit());
            procedimiento.setString(3, registro.getRazonSocial());
            procedimiento.setString(4, registro.getPaginaWeb());
            procedimiento.execute();
            listaProveedor.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Proveedores
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtRazonSocial.setEditable(true);
                    txtDireccion.setEditable(true);
                    txtNit.setEditable(true);
                    txtPaginaWeb.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Proveedor!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtRazonSocial.setEditable(false);
                txtDireccion.setEditable(false);
                txtNit.setEditable(false);
                txtPaginaWeb.setEditable(false);
                cargarDatos();
                break;
        }
    }

    //actualizar Proveedores
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProveedor(?,?,?,?,?)}");
            Proveedor registro = (Proveedor) tblProveedores.getSelectionModel().getSelectedItem();
            registro.setCodigoProveedor(((Proveedor) cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            registro.setDireccion(txtDireccion.getText());
            registro.setNit(txtNit.getText());
            registro.setRazonSocial(txtRazonSocial.getText());
            registro.setPaginaWeb(txtPaginaWeb.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getNit());
            procedimiento.setString(4, registro.getRazonSocial());
            procedimiento.setString(5, registro.getPaginaWeb());
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
                    if (tblProveedores.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedor(?)}");
                                procedimiento.setInt(1, ((Proveedor) tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
                                procedimiento.execute();
                                listaProveedor.remove(tblProveedores.getSelectionModel().getSelectedIndex());
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
        txtRazonSocial.setEditable(false);
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
        txtPaginaWeb.setEditable(false);
        cmbProveedor.setDisable(false);
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
        txtRazonSocial.setEditable(true);
        txtDireccion.setEditable(true);
        txtNit.setEditable(true);
        txtPaginaWeb.setEditable(true);
        cmbProveedor.setDisable(false);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtRazonSocial.clear();
        txtDireccion.clear();
        txtNit.clear();
        txtPaginaWeb.clear();
        cmbProveedor.setValue("");
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
        txtRazonSocial.setEditable(false);
        txtPaginaWeb.setEditable(false);
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
    }

}


