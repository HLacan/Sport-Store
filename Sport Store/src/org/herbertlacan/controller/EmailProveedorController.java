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
import org.herbertlacan.bean.EmailProveedor;
import org.herbertlacan.bean.Proveedor;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class EmailProveedorController implements Initializable {

    ProveedorController proveedor = new ProveedorController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbEmailProveedor.setItems(getEmailProveedor());
        cmbCodigoProveedor.setItems(getProveedor());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<EmailProveedor> listaEmailProveedor;
    private ObservableList<Proveedor> listaProveedor;

    @FXML
    private TextField txtDescripcion, txtEmail;
    @FXML
    public ComboBox cmbEmailProveedor, cmbCodigoProveedor;
    @FXML
    public TableView tblEmailProveedores;
    @FXML
    private TableColumn colCodigo, colDescripcion, colEmail, colCodigoProveedor;
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
        tblEmailProveedores.setItems(getEmailProveedor());
        colCodigo.setCellValueFactory(new PropertyValueFactory<EmailProveedor, Integer>("CodigoEmailProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("Descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("Email"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<EmailProveedor, Integer>("CodigoProveedor"));
    }

    public ObservableList<EmailProveedor> getEmailProveedor() {
        ArrayList<EmailProveedor> lista = new ArrayList<EmailProveedor>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarEmailProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new EmailProveedor(resultado.getInt("CodigoEmailProveedor"), resultado.getString("Descripcion"), resultado.getString("Email"), resultado.getInt("CodigoProveedor")));

            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaEmailProveedor = FXCollections.observableList(lista);
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
            cmbEmailProveedor.getSelectionModel().select(buscarEmailProveedor(((EmailProveedor) tblEmailProveedores.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor()));
            txtDescripcion.setText(((EmailProveedor) tblEmailProveedores.getSelectionModel().getSelectedItem()).getDescripcion());
            txtEmail.setText(((EmailProveedor) tblEmailProveedores.getSelectionModel().getSelectedItem()).getEmail());
            cmbCodigoProveedor.getSelectionModel().select(proveedor.buscarProveedor(((EmailProveedor) tblEmailProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        } catch (Exception e) {
        }
    }

    // Buscar EmailProveedoress
    public EmailProveedor buscarEmailProveedor(int codigoEmailProveedor) {
        EmailProveedor resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEmailProveedor(?)}");
            procedimiento.setInt(1, codigoEmailProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new EmailProveedor(registro.getInt("CodigoEmailProveedor"), registro.getString("Descripcion"), registro.getString("Email"), registro.getInt("CodigoProveedor"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones EmailProveedores a SQL
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

    //Agregar EmailProveedores
    public void agregar() {
        EmailProveedor registro = new EmailProveedor();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setCodigoProveedor(((Proveedor) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarEmailProveedor(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setInt(3, registro.getCodigoProveedor());
            procedimiento.execute();
            listaEmailProveedor.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar EmailProveedores
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmailProveedores.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                    txtEmail.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un EmailProveedor!");
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
                txtEmail.setEditable(false);
                cargarDatos();
                break;
        }
    }

    //actualizar EmailProveedores
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailProveedor(?,?,?,?)}");
            EmailProveedor registro = (EmailProveedor) tblEmailProveedores.getSelectionModel().getSelectedItem();
            registro.setCodigoEmailProveedor(((EmailProveedor) cmbEmailProveedor.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setEmail(txtEmail.getText());
            registro.setCodigoProveedor(((Proveedor) cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoEmailProveedor());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getEmail());
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
                    if (tblEmailProveedores.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailProveedor(?)}");
                                procedimiento.setInt(1, ((EmailProveedor) tblEmailProveedores.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
                                procedimiento.execute();
                                listaEmailProveedor.remove(tblEmailProveedores.getSelectionModel().getSelectedIndex());
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
        txtEmail.setEditable(false);
        cmbEmailProveedor.setDisable(false);
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
        txtEmail.setEditable(true);
        cmbEmailProveedor.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        txtEmail.clear();
        cmbEmailProveedor.setValue("");
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
        txtEmail.setEditable(false);
    }

}
