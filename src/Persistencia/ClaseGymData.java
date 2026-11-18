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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClaseGymData {

    private Connection con;

    public ClaseGymData() {
        try {
            con = Conexion.conectar();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // VALIDA SUPERPOSICIN HORARIA DEL ENTRENADOR (idClaseExcluir = 0 si es nueva)
    public boolean entrenadorOcupadoEnHorario(int idEntrenador, String horario, int idClaseExcluir) {
        String sql = "SELECT COUNT(*) FROM clasegym "
                + "WHERE ID_Entrenador = ? AND Horario = ? AND Estado = 1 AND ID_Clase <> ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            ps.setString(2, horario);
            ps.setInt(3, idClaseExcluir);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar superposición de horario: " + e.getMessage());
        }
        return false;
    }

    // GUARDAR CLASE
    public boolean guardarClase(ClaseGym clase) {
        if (entrenadorOcupadoEnHorario(clase.getEntrenador().getIdEntrenador(), clase.getHorario(), 0)) {
            System.out.println("El entrenador ya tiene otra clase asignada en ese horario.");
            return false;
        }

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
                + "ON c.ID_Entrenador = e.ID_Entrenador "
                + "ORDER BY c.ID_Clase";

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
        if (entrenadorOcupadoEnHorario(clase.getEntrenador().getIdEntrenador(), clase.getHorario(), clase.getId())) {
            System.out.println("El entrenador ya tiene otra clase asignada en ese horario.");
            return false;
        }

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

    // BAJA LOGICA
    public boolean bajaLogica(int id) {
        String sql = "UPDATE clasegym SET Estado = 0 WHERE ID_Clase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al dar de baja la clase: " + e.getMessage());
            return false;
        }
    }

    // ALTA LOGICA
    public boolean altaLogica(int id) {
        String sql = "UPDATE clasegym SET Estado = 1 WHERE ID_Clase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al dar de alta la clase: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR CLASE (FISICO)
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

    // CONSULTA INFORME: FILTRAR POR DISCIPLINA
    public List<ClaseGym> listarClasesPorDisciplina(String disciplina) {
        List<ClaseGym> clases = new ArrayList<>();
        String sql = "SELECT c.*, e.DNI, e.Nombre, e.Apellido, e.Especialidad, e.Estado AS EstadoEntrenador "
                + "FROM clasegym c INNER JOIN entrenador e ON c.ID_Entrenador = e.ID_Entrenador "
                + "WHERE c.Disciplina LIKE ? AND c.Estado = 1 ORDER BY c.Horario";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + disciplina + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clases.add(cargarClase(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al filtrar clases por disciplina: " + e.getMessage());
        }
        return clases;
    }

    // CONSULTA INFORME: FILTRAR POR ENTRENADOR
    public List<ClaseGym> listarClasesPorEntrenador(int idEntrenador) {
        List<ClaseGym> clases = new ArrayList<>();
        String sql = "SELECT c.*, e.DNI, e.Nombre, e.Apellido, e.Especialidad, e.Estado AS EstadoEntrenador "
                + "FROM clasegym c INNER JOIN entrenador e ON c.ID_Entrenador = e.ID_Entrenador "
                + "WHERE c.ID_Entrenador = ? AND c.Estado = 1 ORDER BY c.Horario";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clases.add(cargarClase(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al filtrar clases por entrenador: " + e.getMessage());
        }
        return clases;
    }

    // CONSULTA INFORME: CUPOS DISPONIBLES DE UNA CLASE EN UNA FECHA
    public int obtenerCuposDisponibles(int idClase, LocalDate fecha) {
        String sql = "SELECT c.Capacidad - COUNT(a.ID_Asistencia) AS CuposDisponibles "
                + "FROM clasegym c "
                + "LEFT JOIN asistencia a ON c.ID_Clase = a.ID_Clase AND a.Fecha_Asistencia = ? "
                + "WHERE c.ID_Clase = ? "
                + "GROUP BY c.ID_Clase, c.Capacidad";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            ps.setInt(2, idClase);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CuposDisponibles");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular cupos disponibles: " + e.getMessage());
        }
        return 0;
    }

    // CONSULTA INFORME: LISTAR CLASES ACTIVAS CON ENTRENADOR Y CUPOS DISPONIBLES HOY
    public List<Object[]> listarClasesConCupoHoy() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT c.ID_Clase, c.Disciplina, e.Nombre, e.Apellido, c.Horario, c.Capacidad, "
                + "(c.Capacidad - COUNT(a.ID_Asistencia)) AS CuposDisponibles "
                + "FROM clasegym c "
                + "INNER JOIN entrenador e ON c.ID_Entrenador = e.ID_Entrenador "
                + "LEFT JOIN asistencia a ON c.ID_Clase = a.ID_Clase AND a.Fecha_Asistencia = CURDATE() "
                + "WHERE c.Estado = 1 "
                + "GROUP BY c.ID_Clase, c.Disciplina, e.Nombre, e.Apellido, c.Horario, c.Capacidad "
                + "ORDER BY c.Horario";

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("ID_Clase"),
                    rs.getString("Disciplina"),
                    rs.getString("Apellido") + ", " + rs.getString("Nombre"),
                    rs.getString("Horario"),
                    rs.getInt("Capacidad"),
                    rs.getInt("CuposDisponibles")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clases con cupo: " + e.getMessage());
        }
        return lista;
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