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
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class ClienteController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCliente.setItems(getCliente());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cliente> listaCliente;

    @FXML
    private TextField txtNombre, txtDireccion, txtNit;
    @FXML
    public ComboBox cmbCliente;
    @FXML
    public TableView tblClientes;
    @FXML
    private TableColumn colCodigo, colNombre, colDireccion, colNit;
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
    
    public void VentanaTelefonoClientes(){
        escenarioPrincipal.ventanaTelefonoClientes();
    }
    
    public void VentanaEmailClientes(){
        escenarioPrincipal.ventanaEmailClientes();
    }

    public void cargarDatos() {
        tblClientes.setItems(getCliente());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("CodigoCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Direccion"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("Nit"));
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
            cmbCliente.getSelectionModel().select(buscarCliente(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            txtNombre.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getNombre());
            txtDireccion.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
            txtNit.setText(((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getNit());
        } catch (Exception e) {
        }
    }

    // Buscar Clientess
    public Cliente buscarCliente(int codigoCliente) {
        Cliente resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCliente(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Cliente(registro.getInt("CodigoCliente"), registro.getString("Nombre"), registro.getString("Direccion"), registro.getString("Nit"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Clientes a SQL
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

    //Agregar Clientes
    public void agregar() {
        Cliente registro = new Cliente();
        registro.setNombre(txtNombre.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setNit(txtNit.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarCliente(?,?,?)}");
            procedimiento.setString(1, registro.getNombre());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getNit());
            procedimiento.execute();
            listaCliente.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Clientes
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtNombre.setEditable(true);
                    txtDireccion.setEditable(true);
                    txtNit.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Cliente!");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnNuevo.setVisible(true);
                btnEliminar.setVisible(true);
                txtNombre.setEditable(false);
                txtDireccion.setEditable(false);
                txtNit.setEditable(false);
                cargarDatos();
                break;
        }
    }

    //actualizar Clientes
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCliente(?,?,?,?)}");
            Cliente registro = (Cliente) tblClientes.getSelectionModel().getSelectedItem();
            registro.setCodigoCliente(((Cliente) cmbCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setNombre(txtNombre.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setNit(txtNit.getText());
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNombre());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getNit());
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
                    if (tblClientes.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
                                procedimiento.setInt(1, ((Cliente) tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                                procedimiento.execute();
                                listaCliente.remove(tblClientes.getSelectionModel().getSelectedIndex());
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
        txtNombre.setEditable(false);
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
        cmbCliente.setDisable(false);
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
        txtNombre.setEditable(true);
        txtDireccion.setEditable(true);
        txtNit.setEditable(true);
        cmbCliente.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtNombre.clear();
        txtDireccion.clear();
        txtNit.clear();
        cmbCliente.setValue("");
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
        txtNombre.setEditable(false);
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
    }

}
