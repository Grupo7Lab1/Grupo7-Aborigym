/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.entidad;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class entidadData {

    // GUARDAR ENTIDAD
    public boolean guardarEntidad(entidad s) {

        String sql = "INSERT INTO Socio "
                + "(DNI, Nombre, Apellido, Edad, Correo, Telefono, Estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getDni());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getApellido());
            ps.setInt(4, s.getEdad());
            ps.setString(5, s.getCorreo());
            ps.setString(6, s.getTelefono());
            ps.setBoolean(7, s.isEstado());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al guardar entidad: " + e.getMessage());
            return false;
        }
    }

    // LISTAR ENTIDADES
    public List<entidad> listarEntidad() {

        List<entidad> entidades = new ArrayList<>();

        String sql = "SELECT ID_Socio, DNI, Nombre, Apellido, "
                + "Edad, Correo, Telefono, Estado "
                + "FROM Socio ORDER BY ID_Socio";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                entidad s = new entidad(
                        rs.getInt("ID_Socio"),
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getInt("Edad"),
                        rs.getString("Correo"),
                        rs.getString("Telefono"),
                        rs.getBoolean("Estado")
                );

                entidades.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entidad: " + e.getMessage());
        }

        return entidades;
    }

    // BUSCAR ENTIDAD POR DNI
    public entidad buscarPorDni(String dni) {

        String sql = "SELECT ID_Socio, DNI, Nombre, Apellido, "
                + "Edad, Correo, Telefono, Estado "
                + "FROM Socio WHERE DNI = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new entidad(
                            rs.getInt("ID_Socio"),
                            rs.getString("DNI"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido"),
                            rs.getInt("Edad"),
                            rs.getString("Correo"),
                            rs.getString("Telefono"),
                            rs.getBoolean("Estado")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar entidad por DNI: "
                    + e.getMessage());
        }

        return null;
    }

    // BUSCAR ENTIDAD POR ID
    public entidad buscarPorId(int idSocio) {

        String sql = "SELECT ID_Socio, DNI, Nombre, Apellido, "
                + "Edad, Correo, Telefono, Estado "
                + "FROM Socio WHERE ID_Socio = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new entidad(
                            rs.getInt("ID_Socio"),
                            rs.getString("DNI"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido"),
                            rs.getInt("Edad"),
                            rs.getString("Correo"),
                            rs.getString("Telefono"),
                            rs.getBoolean("Estado")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar entidad por ID: "
                    + e.getMessage());
        }

        return null;
    }

    // MODIFICAR ENTIDAD
    public boolean modificar(entidad s) {

        String sql = "UPDATE Socio SET "
                + "DNI = ?, "
                + "Nombre = ?, "
                + "Apellido = ?, "
                + "Edad = ?, "
                + "Correo = ?, "
                + "Telefono = ?, "
                + "Estado = ? "
                + "WHERE ID_Socio = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getDni());
            ps.setString(2, s.getNombre());
            ps.setString(3, s.getApellido());
            ps.setInt(4, s.getEdad());
            ps.setString(5, s.getCorreo());
            ps.setString(6, s.getTelefono());
            ps.setBoolean(7, s.isEstado());
            ps.setInt(8, s.getIdSocio());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al modificar entidad: "
                    + e.getMessage());
            return false;
        }
    }

    // DAR DE ALTA (ALTA LOGICA)
    public boolean darDeAlta(int idSocio) {

        String sql = "UPDATE Socio SET Estado = TRUE "
                + "WHERE ID_Socio = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al dar de alta entidad: "
                    + e.getMessage());
            return false;
        }
    }

    // DAR DE BAJA (BAJA LOGICA)
    public boolean darDeBaja(int idSocio) {

        String sql = "UPDATE Socio SET Estado = FALSE "
                + "WHERE ID_Socio = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al dar de baja entidad: "
                    + e.getMessage());
            return false;
        }
    }

    // ELIMINAR ENTIDAD (BAJA FISICA)
    public boolean eliminar(int idSocio) {

        String sql = "DELETE FROM Socio WHERE ID_Socio = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entidad: "
                    + e.getMessage());
            return false;
        }
    }
}
