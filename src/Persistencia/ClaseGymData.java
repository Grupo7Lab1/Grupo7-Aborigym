/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.ClaseGym;
import Modelo.Entrenador;
import Modelo.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rotta
 */
public class ClaseGymData {

    private Connection con;

    public ClaseGymData() {
        try {
            con = Conexion.conectar();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: "
                    + e.getMessage());
        }
    }

    // GUARDAR CLASE
    public boolean guardarClase(ClaseGym clase) {

        String sql = "INSERT INTO clasegym "
                + "(Disciplina, ID_Entrenador, Horario, Capacidad, Estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, clase.getDisciplina());
            ps.setInt(2, clase.getEntrenador().getIdEntrenador());
            ps.setString(3, clase.getHorario());
            ps.setInt(4, clase.getCapacidad());
            ps.setBoolean(5, clase.isEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    clase.setId(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar clase: " + e.getMessage());
            return false;
        }
    }

// LISTAR CLASES PARA LOS COMBOBOX
    public List<ClaseGym> listarClasesParaCombo() {

        List<ClaseGym> clases = new ArrayList<>();

        String sql = "SELECT DISTINCT c.ID_Clase, c.Disciplina, "
                + "c.ID_Entrenador, e.DNI, e.Nombre, e.Apellido, "
                + "e.Especialidad, e.Estado AS EstadoEntrenador "
                + "FROM clasegym c "
                + "INNER JOIN entrenador e "
                + "ON c.ID_Entrenador = e.ID_Entrenador "
                + "ORDER BY c.ID_Clase";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Entrenador entrenador = new Entrenador();

                entrenador.setIdEntrenador(rs.getInt("ID_Entrenador"));
                entrenador.setDni(rs.getString("DNI"));
                entrenador.setNombre(rs.getString("Nombre"));
                entrenador.setApellido(rs.getString("Apellido"));
                entrenador.setEspecialidad(rs.getString("Especialidad"));
                entrenador.setEstado(rs.getBoolean("EstadoEntrenador"));

                ClaseGym clase = new ClaseGym();

                clase.setId(rs.getInt("ID_Clase"));
                clase.setDisciplina(rs.getString("Disciplina"));
                clase.setEntrenador(entrenador);

                clases.add(clase);
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar clases: " + e.getMessage());
        }

        return clases;
    }

    // BUSCAR CLASE POR ID
    public ClaseGym buscarClase(int id) {

        String sql = "SELECT c.*, "
                + "e.DNI, e.Nombre, e.Apellido, "
                + "e.Especialidad, e.Estado AS EstadoEntrenador "
                + "FROM clasegym c "
                + "INNER JOIN entrenador e "
                + "ON c.ID_Entrenador = e.ID_Entrenador "
                + "WHERE c.ID_Clase = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return cargarClase(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar clase: " + e.getMessage());
        }

        return null;
    }

    // LISTAR TODAS LAS CLASES
    public List<ClaseGym> listarClases() {

        List<ClaseGym> clases = new ArrayList<>();

        String sql = "SELECT c.*, "
                + "e.DNI, e.Nombre, e.Apellido, "
                + "e.Especialidad, e.Estado AS EstadoEntrenador "
                + "FROM clasegym c "
                + "INNER JOIN entrenador e "
                + "ON c.ID_Entrenador = e.ID_Entrenador";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clases.add(cargarClase(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clases: " + e.getMessage());
        }

        return clases;
    }

    // MODIFICAR CLASE
    public boolean modificarClase(ClaseGym clase) {

        String sql = "UPDATE clasegym SET "
                + "Disciplina = ?, "
                + "ID_Entrenador = ?, "
                + "Horario = ?, "
                + "Capacidad = ?, "
                + "Estado = ? "
                + "WHERE ID_Clase = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, clase.getDisciplina());
            ps.setInt(2, clase.getEntrenador().getIdEntrenador());
            ps.setString(3, clase.getHorario());
            ps.setInt(4, clase.getCapacidad());
            ps.setBoolean(5, clase.isEstado());
            ps.setInt(6, clase.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar clase: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR CLASE
    public boolean eliminarClase(int id) {

        String sql = "DELETE FROM clasegym WHERE ID_Clase = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar clase: " + e.getMessage());
            return false;
        }
    }

    // CARGAR CLASE Y SU ENTRENADOR
    private ClaseGym cargarClase(ResultSet rs) throws SQLException {

        Entrenador entrenador = new Entrenador();

        entrenador.setIdEntrenador(rs.getInt("ID_Entrenador"));
        entrenador.setDni(rs.getString("DNI"));
        entrenador.setNombre(rs.getString("Nombre"));
        entrenador.setApellido(rs.getString("Apellido"));
        entrenador.setEspecialidad(rs.getString("Especialidad"));
        entrenador.setEstado(rs.getBoolean("EstadoEntrenador"));

        ClaseGym clase = new ClaseGym();

        clase.setId(rs.getInt("ID_Clase"));
        clase.setDisciplina(rs.getString("Disciplina"));
        clase.setEntrenador(entrenador);
        clase.setHorario(rs.getString("Horario"));
        clase.setCapacidad(rs.getInt("Capacidad"));
        clase.setEstado(rs.getBoolean("Estado"));

        return clase;
    }
}
