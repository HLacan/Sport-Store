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
import org.herbertlacan.bean.Talla;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class TallaController implements Initializable {
    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Talla> listaTalla;

    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox cmbTalla;
    @FXML
    private TableView tblTallas;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbTalla.setItems(getTallas());
    }

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
        tblTallas.setItems(getTallas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Talla, Integer>("CodigoTalla"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Talla, String>("Descripcion"));
    }

    public ObservableList<Talla> getTallas() {
        ArrayList<Talla> lista = new ArrayList<Talla>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarTallas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Talla(resultado.getInt("CodigoTalla"), resultado.getString("Descripcion")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaTalla = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbTalla.getSelectionModel().select(buscarTalla(((Talla) tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla()));
            txtDescripcion.setText(((Talla) tblTallas.getSelectionModel().getSelectedItem()).getDescripcion());
        } catch (Exception e) {
        }

    }

    // Buscar Tallas
    public Talla buscarTalla(int codigoTalla) {
        Talla resultado = null;
        try {
            //Llamar Procedimiento Almacenado en SQL
            //? = Cuantos parametros lleva el Procedimiento Almacenado
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTalla(?)}");
            //Definir los Parametros al Procedimiento almacenado
            procedimiento.setInt(1, codigoTalla);
            //Ejecutar el Query
            ResultSet registro = procedimiento.executeQuery();
            //Recorrer los Registros en las Tablas
            while (registro.next()) {
                resultado = new Talla(registro.getInt("CodigoTalla"), registro.getString("Descripcion"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Tallas a SQL
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

    //Agregar Tallas
    public void agregar() {
        Talla registro = new Talla();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            //llamar al procedimiento agregarTallas
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarTalla(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTalla.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Tallas
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblTallas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                } else {
                    Vista();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria!", "Editar", JOptionPane.INFORMATION_MESSAGE );
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
                cargarDatos();
                break;
        }
    }

    //actualizar Tallas
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarDescripcionTalla(?,?)}");
            Talla registro = (Talla) tblTallas.getSelectionModel().getSelectedItem();
            registro.setCodigoTalla(((Talla) cmbTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getCodigoTalla());
            procedimiento.execute();
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Eliminar Tallas
    public void eliminar() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            cancelar();
        } else {
            tipoDeOperacion = operaciones.ELIMINAR;
            switch (tipoDeOperacion) {
                case ELIMINAR:
                    if (tblTallas.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Talla", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTalla(?)}");
                                procedimiento.setInt(1, ((Talla) tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla());
                                procedimiento.execute();
                                listaTalla.remove(tblTallas.getSelectionModel().getSelectedIndex());
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
    }

    //Desactivar los controles
    public void desactivarControles() {
        txtDescripcion.setEditable(false);
        cmbTalla.setDisable(false);
    }

    //Activar Botones
    public void activarControles() {
        txtDescripcion.setEditable(true);
        cmbTalla.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        cmbTalla.setValue("");
    }

    public void Vista() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
    }
}
