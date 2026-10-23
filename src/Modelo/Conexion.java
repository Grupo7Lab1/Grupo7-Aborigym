/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Nestor Marchizone
 */
public class Conexion {
     private static final String URL
            = "jdbc:mariadb://localhost:3306/g7_gimnasio_ aborigym";

    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
