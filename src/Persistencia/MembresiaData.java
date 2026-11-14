/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.Conexion;
import Modelo.Membresia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mrjoshep
 */
public class MembresiaData {
    
    // GUARDAR
    public boolean guardarMembresia(Membresia membresia) {

        String sql = "INSERT INTO membresia "
                + "(Nombre, CantidadClases, DuracionDias, Precio) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, membresia.getNombre());
            ps.setInt(2, membresia.getCantidadClases());
            ps.setInt(3, membresia.getDuracionDias());
            ps.setDouble(4, membresia.getPrecio());

            if (ps.executeUpdate() != 1) {
                return false;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    membresia.setId(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error al guardar membresía: "
                    + e.getMessage());
            return false;
        }
    }

    // LISTAR
    public List<Membresia> listarMembresias() {

        List<Membresia> lista = new ArrayList<>();

        String sql = "SELECT * FROM membresia "
                + "ORDER BY ID_Membresia";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(cargarMembresia(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar membresías: "
                    + e.getMessage());
        }

        return lista;
    }

    // BUSCAR POR ID
    public Membresia buscarMembresia(int id) {

        String sql = "SELECT * FROM membresia "
                + "WHERE ID_Membresia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return cargarMembresia(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar membresía: "
                    + e.getMessage());
        }

        return null;
    }

    // BUSCAR POR NOMBRE
    public Membresia buscarPorNombre(String nombre) {

        String sql = "SELECT * FROM membresia WHERE Nombre = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return cargarMembresia(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por nombre: "
                    + e.getMessage());
        }

        return null;
    }

    // MODIFICAR
    public boolean modificarMembresia(Membresia membresia) {

        String sql = "UPDATE membresia SET "
                + "Nombre = ?, CantidadClases = ?, "
                + "DuracionDias = ?, Precio = ? "
                + "WHERE ID_Membresia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, membresia.getNombre());
            ps.setInt(2, membresia.getCantidadClases());
            ps.setInt(3, membresia.getDuracionDias());
            ps.setDouble(4, membresia.getPrecio());
            ps.setInt(5, membresia.getId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error al modificar membresía: "
                    + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminarMembresia(int id) {

        String sql = "DELETE FROM membresia "
                + "WHERE ID_Membresia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error al eliminar membresía: "
                    + e.getMessage());
            return false;
        }
    }

    // CARGAR OBJETO
    private Membresia cargarMembresia(ResultSet rs)
            throws SQLException {

        return new Membresia(
                rs.getInt("ID_Membresia"),
                rs.getString("Nombre"),
                rs.getInt("CantidadClases"),
                rs.getInt("DuracionDias"),
                rs.getDouble("Precio")
        );
    }
}

