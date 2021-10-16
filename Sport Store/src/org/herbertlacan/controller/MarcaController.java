
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
import javax.swing.JOptionPane;
import org.herbertlacan.bean.Marca;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class MarcaController implements Initializable {
    
    private enum operaciones {
        NUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    };
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Marca> listaMarca;

    @FXML
    private TextField txtDescripcion;
    @FXML
    private ComboBox cmbMarca;
    @FXML
    private TableView tblMarcas;
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
     cmbMarca.setItems(getMarcas());
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
        tblMarcas.setItems(getMarcas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Marca, Integer>("CodigoMarca"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Marca, String>("Descripcion"));
    }

    public ObservableList<Marca> getMarcas() {
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Marca(resultado.getInt("CodigoMarca"), resultado.getString("Descripcion")));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return listaMarca = FXCollections.observableList(lista);
    }

    // Seleccionar elemento desde SQL
  public void seleccionarElemento() {

            cmbMarca.getSelectionModel().select(buscarMarca(((Marca) tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca()));
            txtDescripcion.setText(((Marca) tblMarcas.getSelectionModel().getSelectedItem()).getDescripcion());


    }

    // Buscar Marcas
    public Marca buscarMarca(int codigoMarca) {
        Marca resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMarca(?)}");
            procedimiento.setInt(1, codigoMarca);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Marca(registro.getInt("CodigoMarca"), registro.getString("Descripcion"));
            }
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
        return resultado;
    }

    // Cambiar Funcion de Botones Marcas a SQL
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

    //Agregar Marcas
    public void agregar() {
        Marca registro = new Marca();
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarMarca(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaMarca.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Editar Marcas
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblMarcas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria!");
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
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarDescripcionMarca(?,?)}");
            Marca registro = (Marca) tblMarcas.getSelectionModel().getSelectedItem();
            registro.setCodigoMarca(((Marca) cmbMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getCodigoMarca());
            procedimiento.execute();
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //Eliminar Marcas
    public void eliminar() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            cancelar();
        } else {
            tipoDeOperacion = operaciones.ELIMINAR;
            switch (tipoDeOperacion) {
                case ELIMINAR:
                    if (tblMarcas.getSelectionModel().getSelectedItem() != null) {
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Marca", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMarca(?)}");
                                procedimiento.setInt(1, ((Marca) tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca());
                                procedimiento.execute();
                                listaMarca.remove(tblMarcas.getSelectionModel().getSelectedIndex());
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


    public void cancelar() {
        btnNuevo.setText("Nuevo");
        btnEliminar.setText("Eliminar");
        btnEditar.setVisible(true);
        btnReporte.setVisible(true);
        txtDescripcion.setEditable(false);
    }

    //Desactivar los controles
    public void desactivarControles() {
        txtDescripcion.setEditable(false);
        cmbMarca.setDisable(false);
    }

    //Activar Botones
    public void activarControles() {
        txtDescripcion.setEditable(true);
        cmbMarca.setDisable(true);
    }

    //Limpiar Textos
    public void limpiarControles() {
        txtDescripcion.clear();
        cmbMarca.setValue("");
    }
}
