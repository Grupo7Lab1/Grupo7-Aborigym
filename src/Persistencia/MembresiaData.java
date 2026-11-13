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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mrjoshep
 */
public class MembresiaData {
     // GUARDAR MEMBRESIA
    public boolean guardarMembresia(Membresia membresia) {

        String sql = "INSERT INTO membresia "
                + "(Tipo, Precio, DuracionDias) "
                + "VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, membresia.getTipo());
            ps.setDouble(2, membresia.getPrecio());
            ps.setInt(3, membresia.getDuracionDias());

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
            System.out.println("Error al guardar membresia: " + e.getMessage());
            return false;
        }
    }

    // LISTAR MEMBRESIAS
    public List<Membresia> listarMembresias() {

        List<Membresia> lista = new ArrayList<>();

        String sql = "SELECT ID_Membresia, Tipo, Precio, DuracionDias "
                + "FROM membresia ORDER BY ID_Membresia";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(cargarMembresia(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar membresias: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR MEMBRESIA POR ID
    public Membresia buscarMembresia(int id) {

        String sql = "SELECT ID_Membresia, Tipo, Precio, DuracionDias "
                + "FROM membresia WHERE ID_Membresia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return cargarMembresia(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar membresia: " + e.getMessage());
        }

        return null;
    }

    // BUSCAR MEMBRESIA POR TIPO (para evitar tipos repetidos)
    public Membresia buscarPorTipo(String tipo) {

        String sql = "SELECT ID_Membresia, Tipo, Precio, DuracionDias "
                + "FROM membresia WHERE Tipo = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return cargarMembresia(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar membresia por tipo: "
                    + e.getMessage());
        }

        return null;
    }

    // MODIFICAR MEMBRESIA
    public boolean modificarMembresia(Membresia membresia) {

        String sql = "UPDATE membresia SET "
                + "Tipo = ?, Precio = ?, DuracionDias = ? "
                + "WHERE ID_Membresia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, membresia.getTipo());
            ps.setDouble(2, membresia.getPrecio());
            ps.setInt(3, membresia.getDuracionDias());
            ps.setInt(4, membresia.getId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al modificar membresia: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR MEMBRESIA (baja fisica)
    public boolean eliminarMembresia(int id) {

        String sql = "DELETE FROM membresia WHERE ID_Membresia = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al eliminar membresia: " + e.getMessage());
            return false;
        }
    }

    // CARGAR MEMBRESIA DESDE RESULTSET
    private Membresia cargarMembresia(ResultSet rs) throws SQLException {
        return new Membresia(
                rs.getInt("ID_Membresia"),
                rs.getString("Tipo"),
                rs.getDouble("Precio"),
                rs.getInt("DuracionDias")
        );
    }
}

