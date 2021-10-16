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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.herbertlacan.bean.TelefonoCliente;
import org.herbertlacan.bean.Cliente;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class TelefonoClienteController implements Initializable{
    
    ClienteController clienteController = new ClienteController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        cmbCodigoTelefonoCliente.setItems(getTelefonoCliente());
        cmbCodigoCliente.setItems(getCliente());
    }
    
    private enum operaciones{
        NNUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    }
    
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <TelefonoCliente> listaTelefonoCliente;
    private ObservableList <Cliente> listaCliente;
    
    @FXML private TextField txtDescripcion, txtTelefono;
    @FXML private ComboBox cmbCodigoTelefonoCliente, cmbCodigoCliente;
    @FXML private TableView tblTelefonoClientes;
    @FXML private TableColumn colCodigoTelefonoCliente, colDescripcion, colTelefono, colCodigoCliente;
    @FXML private Button btnNuevo, btnEliminar, btnEditar, btnReporte;
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void MenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void cargarDatos(){
        tblTelefonoClientes.setItems(getTelefonoCliente());
        colCodigoTelefonoCliente.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, Integer>("CodigoTelefonoCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, String>("Descripcion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, Integer>("Telefono"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, Integer>("CodigoCliente"));
    }
    
    public ObservableList<TelefonoCliente> getTelefonoCliente(){
        ArrayList<TelefonoCliente> lista = new ArrayList<TelefonoCliente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarTelefonoClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TelefonoCliente(resultado.getInt("CodigoTelefonoCliente"), resultado.getString("Descripcion"), resultado.getInt("Telefono"), resultado.getInt("CodigoCliente")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaTelefonoCliente = FXCollections.observableList(lista);
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
    
    public void seleccionarElemento(){
        try{
        cmbCodigoTelefonoCliente.getSelectionModel().select(buscarTelefonoCliente(((TelefonoCliente) tblTelefonoClientes.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente()));
        txtDescripcion.setText(((TelefonoCliente) tblTelefonoClientes.getSelectionModel().getSelectedItem()).getDescripcion());
        txtTelefono.setText(String.valueOf(((TelefonoCliente) tblTelefonoClientes.getSelectionModel().getSelectedItem()).getTelefono()));
        cmbCodigoCliente.getSelectionModel().select(buscarCliente(((TelefonoCliente) tblTelefonoClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public TelefonoCliente buscarTelefonoCliente(int codigoTelefonoCliente){
        TelefonoCliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoCliente (?)}");
            procedimiento.setInt(1, codigoTelefonoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TelefonoCliente(registro.getInt("CodigoTelefonoCliente"), registro.getString("Descripcion"), registro.getInt("Telefono"), registro.getInt("CodigoCliente"));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        }
        return resultado;
    }
    
        public Cliente buscarCliente(int codigoCliente){
        Cliente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCliente (?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Cliente(
                registro.getInt("CodigoCliente"), 
                registro.getString("Nombre"), 
                registro.getString("Direccion"), 
                registro.getString("Nit"));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        }
        return resultado;
    }
    
    //Cambiar Funcion de Botones TelefonoClientees a SQL
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

    //Agregar TelefonoClientees
    public void agregar() {
        TelefonoCliente registro = new TelefonoCliente();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setTelefono(Integer.valueOf(txtTelefono.getText()));
        registro.setCodigoCliente(((Cliente) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarTelefonoCliente(?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getTelefono());
            procedimiento.setInt(3, registro.getCodigoCliente());
            procedimiento.execute();
            listaTelefonoCliente.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar TelefonoClientees
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTelefonoClientes.getSelectionModel().getSelectedItem() != null) {
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                    txtTelefono.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un TelefonoCliente!");
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

    //actualizar TelefonoClientees
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoCliente(?,?,?,?)}");
            TelefonoCliente registro = (TelefonoCliente) tblTelefonoClientes.getSelectionModel().getSelectedItem();
            registro.setCodigoTelefonoCliente(((TelefonoCliente) cmbCodigoTelefonoCliente.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setTelefono(Integer.valueOf(txtTelefono.getText()));
            registro.setCodigoCliente(((Cliente) cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            procedimiento.setInt(1, registro.getCodigoTelefonoCliente());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setInt(3, registro.getTelefono());
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
                    if (tblTelefonoClientes.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoCliente(?)}");
                                procedimiento.setInt(1, ((TelefonoCliente) tblTelefonoClientes.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
                                procedimiento.execute();
                                listaTelefonoCliente.remove(tblTelefonoClientes.getSelectionModel().getSelectedIndex());
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
        cmbCodigoTelefonoCliente.setMouseTransparent(false);
        cmbCodigoCliente.setMouseTransparent(false);
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
        cmbCodigoTelefonoCliente.setMouseTransparent(false);
        cmbCodigoCliente.setMouseTransparent(false);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        txtTelefono.clear();
        cmbCodigoTelefonoCliente.setValue("");
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
        txtTelefono.setEditable(false);
    }
}

