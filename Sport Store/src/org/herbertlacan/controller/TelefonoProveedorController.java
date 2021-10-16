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
import org.herbertlacan.bean.TelefonoProveedor;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class TelefonoProveedorController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbTelefonoProveedor.setItems(getTelefonoProveedor());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoProveedor> listaTelefonoProveedor;

    @FXML
    private TextField txtDescripcion, txtTelefono;
    @FXML
    public ComboBox cmbTelefonoProveedor, cmbCodigoProveedor;
    @FXML
    public TableView tblTelefonoProveedores;
    @FXML
    private TableColumn colCodigo, colDescripcion, colTelefono, colCodigoProveedor;
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
        tblTelefonoProveedores.setItems(getTelefonoProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("CodigoTelefonoProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("Descripcion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("Telefono"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("CodigoProveedor"));
    }

    public ObservableList<TelefonoProveedor> getTelefonoProveedor() {
        ArrayList<TelefonoProveedor> lista = new ArrayList<TelefonoProveedor>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarTelefonoProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TelefonoProveedor(resultado.getInt("CodigoTelefonoProveedor"), resultado.getString("Descripcion"), resultado.getInt("Telefono"), resultado.getInt("CodigoProveedor")));

            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaTelefonoProveedor = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbTelefonoProveedor.getSelectionModel().select(buscarTelefonoProveedor(((TelefonoProveedor) tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
            txtDescripcion.setText(((TelefonoProveedor) tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getDescripcion());
            txtTelefono.setText(String.valueOf(((TelefonoProveedor) tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getTelefono()));
            cmbCodigoProveedor.getSelectionModel().select(buscarTelefonoProveedor(((TelefonoProveedor) tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        } catch (Exception e) {
        }
    }

    // Buscar TelefonoProveedoress
    public TelefonoProveedor buscarTelefonoProveedor(int codigoTelefonoProveedor) {
        TelefonoProveedor resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoProveedor(?)}");
            procedimiento.setInt(1, codigoTelefonoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TelefonoProveedor(registro.getInt("CodigoTelefonoProveedor"), registro.getString("Descripcion"), registro.getInt("Telefono"), registro.getInt("CodigoProveedor"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones TelefonoProveedores a SQL
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

    //Agregar TelefonoProveedores
    public void agregar() {
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTelefono(Integer.valueOf(txtTelefono.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarTelefonoProveedor(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getTelefono());
            procedimiento.setInt(3, registro.getCodigoProveedor());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar TelefonoProveedores
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTelefonoProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                    txtTelefono.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un TelefonoProveedor!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtDescripcion.setEditable(false);
                txtTelefono.setEditable(false);
                cargarDatos();
                break;
        }
    }

    //actualizar TelefonoProveedores
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoProveedor(?,?,?,?)}");
            TelefonoProveedor registro = (TelefonoProveedor) tblTelefonoProveedores.getSelectionModel().getSelectedItem();
            registro.setCodigoTelefonoProveedor(((TelefonoProveedor) cmbTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTelefono(Integer.valueOf(txtTelefono.getText()));
            registro.setCodigoProveedor(((TelefonoProveedor) cmbTelefonoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setInt(3, registro.getTelefono());
            procedimiento.setInt(4, registro.getCodigoProveedor());
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
                    if (tblTelefonoProveedores.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoProveedor(?)}");
                                procedimiento.setInt(1, ((TelefonoProveedor) tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
                                procedimiento.execute();
                                listaTelefonoProveedor.remove(tblTelefonoProveedores.getSelectionModel().getSelectedIndex());
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
        txtDescripcion.setEditable(false);
        txtTelefono.setEditable(false);
        cmbTelefonoProveedor.setDisable(false);
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
        txtDescripcion.setEditable(true);
        txtTelefono.setEditable(true);
        cmbTelefonoProveedor.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        txtTelefono.clear();
        cmbTelefonoProveedor.setValue("");
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
        txtDescripcion.setEditable(false);
        txtTelefono.setEditable(false);
    }

}

