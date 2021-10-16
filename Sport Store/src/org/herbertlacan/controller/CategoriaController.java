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
import org.herbertlacan.bean.Categoria;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class CategoriaController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Categoria> listaCategoria;

    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox cmbCategoria;
    @FXML
    private TableView tblCategorias;
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
        cmbCategoria.setItems(getCategorias());
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
        tblCategorias.setItems(getCategorias());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Categoria, Integer>("CodigoCategoria"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Categoria, String>("Descripcion"));
    }

    public ObservableList<Categoria> getCategorias() {
        ArrayList<Categoria> lista = new ArrayList<Categoria>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarCategorias}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Categoria(resultado.getInt("CodigoCategoria"), resultado.getString("Descripcion")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaCategoria = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
    public void seleccionarElemento() {
        try {
            cmbCategoria.getSelectionModel().select(buscarCategoria(((Categoria) tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria()));
            txtDescripcion.setText(((Categoria) tblCategorias.getSelectionModel().getSelectedItem()).getDescripcion());
        } catch (Exception e) {
        }

    }

    // Buscar Categorias
    public Categoria buscarCategoria(int codigoCategoria) {
        Categoria resultado = null;
        try {
            //Llamar Procedimiento Almacenado en SQL
            //? = Cuantos parametros lleva el Procedimiento Almacenado
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCategoria(?)}");
            //Definir los Parametros al Procedimiento almacenado
            procedimiento.setInt(1, codigoCategoria);
            //Ejecutar el Query
            ResultSet registro = procedimiento.executeQuery();
            //Recorrer los Registros en las Tablas
            while (registro.next()) {
                resultado = new Categoria(registro.getInt("CodigoCategoria"), registro.getString("Descripcion"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Categorias a SQL
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

    //Agregar Categorias
    public void agregar() {
        Categoria registro = new Categoria();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            //llamar al procedimiento agregarCategorias
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarCategoria(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaCategoria.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Categorias
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblCategorias.getSelectionModel().getSelectedItem() != null) {
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

    //actualizar Categorias
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCategoria(?,?)}");
            Categoria registro = (Categoria) tblCategorias.getSelectionModel().getSelectedItem();
            registro.setCodigoCategoria(((Categoria) cmbCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getCodigoCategoria());
            procedimiento.execute();
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Eliminar Categorias
    public void eliminar() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            cancelar();
        } else {
            tipoDeOperacion = operaciones.ELIMINAR;
            switch (tipoDeOperacion) {
                case ELIMINAR:
                    if (tblCategorias.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Categoria", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCategoria(?)}");
                                procedimiento.setInt(1, ((Categoria) tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria());
                                procedimiento.execute();
                                listaCategoria.remove(tblCategorias.getSelectionModel().getSelectedIndex());
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
        cmbCategoria.setDisable(false);
    }

    //Activar Botones
    public void activarControles() {
        txtDescripcion.setEditable(true);
        cmbCategoria.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        cmbCategoria.setValue("");
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
