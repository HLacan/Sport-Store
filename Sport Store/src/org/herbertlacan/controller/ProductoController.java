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
import org.herbertlacan.bean.Producto;
import org.herbertlacan.bean.Categoria;
import org.herbertlacan.bean.Marca;
import org.herbertlacan.bean.Talla;
import org.herbertlacan.db.Conexion;
import org.herbertlacan.sistema.Principal;

public class ProductoController implements Initializable{
    
    CategoriaController categoria = new CategoriaController();
    MarcaController marca = new MarcaController();
    TallaController talla = new TallaController();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        cmbCodigoProducto.setItems(getProducto());
        cmbCodigoCategoria.setItems(getCategoria());
        cmbCodigoMarca.setItems(getMarca());
        cmbCodigoTalla.setItems(getTalla());
    }
    
    private enum operaciones{
        NNUEVO, GUARDAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO, ELIMINAR
    }
    
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Producto> listaProducto;
    private ObservableList <Categoria> listaCategoria;
    private ObservableList <Marca> listaMarca;
    private ObservableList <Talla> listaTalla;
    
    @FXML private TextField txtDescripcion, txtExistencia, txtImagen, txtPrecioUnidad, txtPrecioDocena, txtPrecioMayor;
    @FXML private ComboBox cmbCodigoProducto, cmbCodigoCategoria, cmbCodigoMarca, cmbCodigoTalla;
    @FXML private TableView tblProductos;
    @FXML private TableColumn colCodigoProducto, colDescripcion, colExistencia, colImagen, colPrecioUnidad, colPrecioDocena, colPrecioMayor, colCodigoCategoria, colCodigoMarca, colCodigoTalla;
    @FXML private Button btnNuevo, btnEditar, btnEliminar, btnReporte;
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void MenuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    // --------------------------------------------------- Cargar Datos a la Tabla -----------------------------------------------------------
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("CodigoProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("Descripcion"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("Existencia"));
        colImagen.setCellValueFactory(new PropertyValueFactory<Producto, String>("Imagen"));
        colPrecioUnidad.setCellValueFactory(new PropertyValueFactory<Producto, Double>("PrecioUnitario"));
        colPrecioDocena.setCellValueFactory(new PropertyValueFactory<Producto, Double>("PrecioDocena"));
        colPrecioMayor.setCellValueFactory(new PropertyValueFactory<Producto, Double>("PrecioporMayor"));
        colCodigoCategoria.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("CodigoCategoria"));
        colCodigoMarca.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("CodigoMarca"));
        colCodigoTalla.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("CodigoTalla"));
    }
    
    //------------------------------------------------- Obtener Datos de SQL ----------------------------------------------------------------
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Producto(resultado.getInt("CodigoProducto"), resultado.getString("Descripcion"), resultado.getInt("Existencia"), resultado.getString("Imagen"), resultado.getDouble("PrecioUnitario"), resultado.getDouble("PrecioDocena"), resultado.getDouble("PrecioporMayor"), resultado.getInt("CodigoCategoria"), resultado.getInt("CodigoMarca"), resultado.getInt("CodigoTalla")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaProducto = FXCollections.observableList(lista);
    }
    
    //---------------------------------------------------------------------------------
    
    public ObservableList<Categoria> getCategoria(){
        ArrayList<Categoria> lista = new ArrayList<Categoria>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarCategorias}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Categoria(resultado.getInt("CodigoCategoria"), resultado.getString("Descripcion")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaCategoria = FXCollections.observableList(lista);
    }
    
    //---------------------------------------------------------------------------------------------
    
    public ObservableList<Marca> getMarca(){
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Marca(resultado.getInt("CodigoMarca"), resultado.getString("Descripcion")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaMarca = FXCollections.observableList(lista);
    }
    
    //-----------------------------------------------------------------------------------------------
    
    public ObservableList<Talla> getTalla(){
        ArrayList<Talla> lista = new ArrayList<Talla>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_MostrarTallas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Talla(resultado.getInt("CodigoTalla"), resultado.getString("Descripcion")));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        } 
        return listaTalla = FXCollections.observableList(lista);
    }
    
    //------------------------------------------------- Seleccion del Elemento -----------------------------------------------------------
    
    public void seleccionarElemento(){
        try{
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtDescripcion.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getDescripcion());
        txtExistencia.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getExistencia()));
        txtImagen.setText(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getImagen());
        txtPrecioUnidad.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioUnitario()));
        txtPrecioDocena.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioDocena()));
        txtPrecioMayor.setText(String.valueOf(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getPrecioporMayor()));
        cmbCodigoCategoria.getSelectionModel().select(categoria.buscarCategoria(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoCategoria()));
        cmbCodigoMarca.getSelectionModel().select(marca.buscarMarca(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoMarca()));
        cmbCodigoTalla.getSelectionModel().select(talla.buscarTalla(((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoTalla()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
      
    //------------------------------------------------------ Buscar Datos de Producto -------------------------------------------------------
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("CodigoProducto"), registro.getString("Descripcion"), registro.getInt("Existencia"), registro.getString("Imagen"), registro.getDouble("PrecioUnitario"), registro.getDouble("PrecioDocena"), registro.getDouble("PrecioporMayor"), registro.getInt("CodigoCategoria"), registro.getInt("CodigoMarca"), registro.getInt("CodigoTalla"));
            }
        }catch(SQLException SQLE){
            SQLE.printStackTrace();
        }
        return resultado;
    }
    
    //------------------------------------------------- Agregar Producto ------------------------------------------------
    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                activarControles();
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
                Vista();
                JOptionPane.showMessageDialog(null, "Producto Agregado Exitosamente!", "Agregar Producto", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
                break;

        }
    }

    //------------------------------------------------------------------------------------------
    public void agregar() {
        Producto registro = new Producto();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setExistencia(Integer.valueOf(txtExistencia.getText()));
        registro.setImagen(txtImagen.getText());
        registro.setPrecioUnitario(Double.valueOf(txtPrecioUnidad.getText()));
        registro.setPrecioDocena(Double.valueOf(txtPrecioDocena.getText()));
        registro.setPrecioporMayor(Double.valueOf(txtPrecioMayor.getText()));
        registro.setCodigoCategoria(((Categoria) cmbCodigoCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
        registro.setCodigoMarca(((Marca) cmbCodigoMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
        registro.setCodigoTalla(((Talla) cmbCodigoTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_IngresarProducto(?,?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.setInt(2, registro.getExistencia());
            procedimiento.setString(3, registro.getImagen());
            procedimiento.setDouble(4, registro.getPrecioUnitario());
            procedimiento.setDouble(5, registro.getPrecioDocena());
            procedimiento.setDouble(6, registro.getPrecioporMayor());
            procedimiento.setInt(7, registro.getCodigoCategoria());
            procedimiento.setInt(8, registro.getCodigoMarca());
            procedimiento.setInt(9, registro.getCodigoTalla());   
            procedimiento.execute();
            listaProducto.add(registro);
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //------------------------------------------------ Actualizar Producto -----------------------------------------------------------------
    
    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    btnNuevo.setVisible(false);
                    btnEliminar.setVisible(false);
                    txtDescripcion.setEditable(true);
                    txtImagen.setEditable(true);
                    cmbCodigoCategoria.setMouseTransparent(false);
                    cmbCodigoTalla.setMouseTransparent(false);
                    cmbCodigoMarca.setMouseTransparent(false);
                } else {
                    Vista();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una categoria!", "Editar", JOptionPane.INFORMATION_MESSAGE);
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
                desactivarControles();
                Vista();
                JOptionPane.showMessageDialog(null, "Producto Modificado Exitosamente!", "Actualizar Producto", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
                break;
        }
    }

    //-----------------------------------------------------------------------------------------------
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?,?,?,?,?,?,?,?)}");
            Producto registro = (Producto) tblProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(((Producto) cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            registro.setCodigoCategoria(((Categoria) cmbCodigoCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
            registro.setCodigoMarca(((Marca) cmbCodigoMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
            registro.setCodigoTalla(((Talla) cmbCodigoTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setExistencia(Integer.parseInt(txtExistencia.getText()));
            registro.setImagen(txtImagen.getText());
            registro.setPrecioUnitario(Double.parseDouble(txtPrecioUnidad.getText()));
            registro.setPrecioDocena(Double.parseDouble(txtPrecioDocena.getText()));
            registro.setPrecioporMayor(Double.parseDouble(txtPrecioMayor.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setInt(3, registro.getExistencia());
            procedimiento.setString(4, registro.getImagen());
            procedimiento.setDouble(5, registro.getPrecioUnitario());
            procedimiento.setDouble(6, registro.getPrecioDocena());
            procedimiento.setDouble(7, registro.getPrecioporMayor());
            procedimiento.setInt(8, registro.getCodigoCategoria());
            procedimiento.setInt(9, registro.getCodigoMarca());
            procedimiento.setInt(10, registro.getCodigoTalla());
            procedimiento.execute();
        } catch (SQLException SQLE) {
            SQLE.printStackTrace();
        }
    }

    //------------------------------------------------------ Eliminar un Producto ---------------------------------------------------------
    public void eliminar() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            cancelar();
        } else {
            tipoDeOperacion = operaciones.ELIMINAR;
            switch (tipoDeOperacion) {
                case ELIMINAR:
                    if (tblProductos.getSelectionModel().getSelectedItem() != null) {
                        Vista();
                        int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de Eliminar el registro?", "Eliminar Producto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (respuesta == JOptionPane.YES_OPTION) {
                            try {
                                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                                procedimiento.setInt(1, ((Producto) tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                                procedimiento.execute();
                                listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
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

    //----------------------------------------------------- Crear el Reporte de Producto ---------------------------------------------
    
    public void reporte() {
        if (tipoDeOperacion == operaciones.ACTUALIZAR) {
            cancelar();
        }
        tipoDeOperacion = operaciones.NINGUNO;
    }

    //----------------------------- Accion Cancelar ------------------------------
    
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
        desactivarControles();
    }

    // -------------------------------------------- Desactivar los controles ------------------------------
    public void desactivarControles() {
        cmbCodigoProducto.setMouseTransparent(false);
        cmbCodigoCategoria.setMouseTransparent(true);
        cmbCodigoMarca.setMouseTransparent(true);
        cmbCodigoTalla.setMouseTransparent(true);
        txtDescripcion.setEditable(false);
        txtImagen.setEditable(false);
        txtExistencia.setDisable(false);
        txtPrecioUnidad.setDisable(false);
        txtPrecioDocena.setDisable(false);
        txtPrecioMayor.setDisable(false);
    }

    //-------------------------------- Activar Botones -----------------------------
    public void activarControles() {
        cmbCodigoProducto.setValue("Codigo del Producto");
        cmbCodigoCategoria.setValue("Codigo de la Categoria");
        cmbCodigoMarca.setValue("Codigo de la Marca");
        cmbCodigoTalla.setValue("Codigo de la Talla");
        txtDescripcion.clear();
        txtImagen.clear();
        cmbCodigoProducto.setMouseTransparent(true);
        cmbCodigoCategoria.setMouseTransparent(false);
        cmbCodigoMarca.setMouseTransparent(false);
        cmbCodigoTalla.setMouseTransparent(false);
        txtDescripcion.setEditable(true);
        txtImagen.setEditable(true);
        txtExistencia.setText("0");
        txtExistencia.setDisable(true);
        txtPrecioUnidad.setText("0.0");
        txtPrecioUnidad.setDisable(true);
        txtPrecioDocena.setText("0.0");
        txtPrecioDocena.setDisable(true);
        txtPrecioMayor.setText("0.0");
        txtPrecioMayor.setDisable(true);
    }


    // ---------------------------------- Estilo para el JOptionPane --------------------------------
    public void Vista() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
        }
    }    
}

