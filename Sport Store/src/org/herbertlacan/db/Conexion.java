package org.herbertlacan.db;

import java.sql.DriverManager; //ODBC
import java.sql.ResultSet; //  ejecutar los querys
import java.sql.Connection; // conexion hacia la base de datos
import java.sql.SQLException; // provee informacion si la conexion tiene error
import java.sql.Statement; // sirve para poder enviar sentencias de sql a la bd
import com.microsoft.sqlserver.jdbc.SQLServerDriver; //Paquete de conexion con el driver hacia el ODBC

public class Conexion {

    private Connection conexion;
    private Statement sentencia;
    private static Conexion instancia;

    public Conexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            //conexion = DriverManager.getConnection("jdbc:sqlserver://LABC23-13:0;instanceName=SQLKINALQUINTO;dataBaseName=DBSportStore2016136;user=2016136;password=Guatemala;");
            conexion = DriverManager.getConnection("jdbc:sqlserver://Lacan:0;instanceName=SQLKINALQUINTO;dataBaseName=DBSportStore2016136;user=2016136;password=Guatemala;");
            //conexion = DriverManager.getConnection("jdbc:sqlserver://Hellen:0;instanceName=SQLKINALQUINTO;dataBaseName=DBSportStore2016136;user=2016136;password=Guatemala;");
        } catch (ClassNotFoundException CNFE) {
            CNFE.printStackTrace();
        } catch (InstantiationException IE){
            IE.printStackTrace();
        } catch (IllegalAccessException IAE){
            IAE.printStackTrace();
        } catch (SQLException SQLE){
            SQLE.printStackTrace();
        }
    }
    
    public static Conexion getInstancia(){
        if(instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }
    
    public Connection getConexion(){
        return conexion;
    }
    
    public void setConexion(Connection conexion){
        this.conexion = conexion;
    }
}
