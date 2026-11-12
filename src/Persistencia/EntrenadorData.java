/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.Entrenador;
import Modelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntrenadorData {

    public boolean insertar(Entrenador entrenador) {

        String sql = "INSERT INTO entrenador "
                + "(dni, nombre, apellido, especialidad, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entrenador.getDni());
            ps.setString(2, entrenador.getNombre());
            ps.setString(3, entrenador.getApellido());
            ps.setString(4, entrenador.getEspecialidad());
            ps.setBoolean(5, entrenador.isEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar entrenador: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Entrenador entrenador) {

        String sql = "UPDATE entrenador SET "
                + "dni = ?, nombre = ?, apellido = ?, "
                + "especialidad = ?, estado = ? "
                + "WHERE ID_Entrenador = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entrenador.getDni());
            ps.setString(2, entrenador.getNombre());
            ps.setString(3, entrenador.getApellido());
            ps.setString(4, entrenador.getEspecialidad());
            ps.setBoolean(5, entrenador.isEstado());
            ps.setInt(6, entrenador.getIdEntrenador());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar entrenador: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean bajaLogica(int idEntrenador) {

        String sql = "UPDATE entrenador "
                + "SET estado = 0 "
                + "WHERE ID_Entrenador = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEntrenador);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al dar de baja: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean altaLogica(int idEntrenador) {

        String sql = "UPDATE entrenador "
                + "SET estado = 1 "
                + "WHERE ID_Entrenador = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEntrenador);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al dar de alta: "
                    + e.getMessage());
            return false;
        }
    }

    public Entrenador buscarPorId(int idEntrenador) {

        String sql = "SELECT * FROM entrenador "
                + "WHERE ID_Entrenador = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEntrenador);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Entrenador entrenador = new Entrenador();
                    entrenador.setIdEntrenador(rs.getInt("ID_Entrenador"));
                    entrenador.setDni(rs.getString("dni"));
                    entrenador.setNombre(rs.getString("nombre"));
                    entrenador.setApellido(rs.getString("apellido"));
                    entrenador.setEspecialidad(rs.getString("especialidad"));
                    entrenador.setEstado(rs.getBoolean("estado"));
                    return entrenador;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar entrenador: "
                    + e.getMessage());
        }

        return null;

    }

    public List<Entrenador> listarEntrenadores() {

        String sql = "SELECT * FROM entrenador";

        ArrayList<Entrenador> lista = new ArrayList<>();

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                //System.out.println("Entrenador encontrado: " + rs.getString("nombre"));

                Entrenador entrenador = new Entrenador();

                entrenador.setIdEntrenador(
                        rs.getInt("ID_Entrenador"));

                entrenador.setDni(rs.getString("dni"));
                entrenador.setNombre(rs.getString("nombre"));
                entrenador.setApellido(rs.getString("apellido"));

                entrenador.setEspecialidad(
                        rs.getString("especialidad"));

                entrenador.setEstado(rs.getBoolean("estado"));

                lista.add(entrenador);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entrenador: "
                    + e.getMessage());
        }

        return lista;
    }

}
