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
import org.herbertlacan.bean.EmailCliente;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class EmailClienteController implements Initializable {

       ClienteController cliente = new ClienteController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbEmailCliente.setItems(getEmailCliente());
        cmbCodigoCliente.setItems(getCliente());
    }

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<EmailCliente> listaEmailCliente;
    private ObservableList<Cliente> listaCliente;

    @FXML
    private TextField txtDescripcion, txtEmail;
    @FXML
    public ComboBox cmbEmailCliente, cmbCodigoCliente;
    @FXML
    public TableView tblEmailClientes;
    @FXML
    private TableColumn colCodigo, colDescripcion, colEmail, colCodigoCliente;
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
        tblEmailClientes.setItems(getEmailCliente());
        colCodigo.setCellValueFactory(new PropertyValueFactory<EmailCliente, Integer>("CodigoEmailCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailCliente, String>("Descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailCliente, String>("Email"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<EmailCliente, Integer>("CodigoCliente"));
    }

    public ObservableList<EmailCliente> getEmailCliente() {
        ArrayList<EmailCliente> lista = new ArrayList<EmailCliente>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarEmailClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new EmailCliente(resultado.getInt("CodigoEmailCliente"), resultado.getString("Descripcion"), resultado.getString("Email"), resultado.getInt("CodigoCliente")));

            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaEmailCliente = FXCollections.observableList(lista);
    }
    
     public ObservableList<Cliente> getCliente(){
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cliente(resultado.getInt("CodigoCliente"), resultado.getString("Nombre"), resultado.getString("Direccion"), resultado.getString("Nit")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaCliente = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbEmailCliente.getSelectionModel().select(buscarEmailCliente(((EmailCliente) tblEmailClientes.getSelectionModel().getSelectedItem()).getCodigoEmailCliente()));
            txtDescripcion.setText(((EmailCliente) tblEmailClientes.getSelectionModel().getSelectedItem()).getDescripcion());
            txtEmail.setText(((EmailCliente) tblEmailClientes.getSelectionModel().getSelectedItem()).getEmail());
            cmbCodigoCliente.getSelectionModel().select(cliente.buscarCliente(((EmailCliente) tblEmailClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        } catch (Exception e) {
        }
    }

    // Buscar EmailClientess
    public EmailCliente buscarEmailCliente(int codigoEmailCliente) {
        EmailCliente resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEmailCliente(?)}");
            procedimiento.setInt(1, codigoEmailCliente);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new EmailCliente(registro.getInt("CodigoEmailCliente"), registro.getString("Descripcion"), registro.getString("Email"), registro.getInt("CodigoCliente"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones EmailClientes a SQL
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

    //Agregar EmailClientes
    public void agregar() {
        EmailCliente registro = new EmailCliente();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setEmail(txtEmail.getText());
        registro.setCodigoCliente(((Cliente) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarEmailCliente(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setInt(3, registro.getCodigoCliente());
            procedimiento.execute();
            listaEmailCliente.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar EmailClientes
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmailClientes.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                    txtEmail.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un EmailCliente!");
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

    //actualizar EmailClientes
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailCliente(?,?,?,?)}");
            EmailCliente registro = (EmailCliente) tblEmailClientes.getSelectionModel().getSelectedItem();
            registro.setCodigoEmailCliente(((EmailCliente) cmbEmailCliente.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setEmail(txtEmail.getText());
            registro.setCodigoCliente(((Cliente) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            procedimiento.setInt(1, registro.getCodigoEmailCliente());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getEmail());
            procedimiento.setInt(4, registro.getCodigoCliente());
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
                    if (tblEmailClientes.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailCliente(?)}");
                                procedimiento.setInt(1, ((EmailCliente) tblEmailClientes.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
                                procedimiento.execute();
                                listaEmailCliente.remove(tblEmailClientes.getSelectionModel().getSelectedIndex());
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
        cmbEmailCliente.setDisable(false);
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
        cmbEmailCliente.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        txtEmail.clear();
        cmbEmailCliente.setValue("");
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
        txtDescripcion.setEditable(false);
        txtEmail.setEditable(false);
    }

}