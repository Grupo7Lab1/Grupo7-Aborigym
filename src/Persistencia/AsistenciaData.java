
package Persistencia;

import Modelo.Asistencia;
import Modelo.Socio;
import Modelo.ClaseGym;
import Modelo.Conexion;
import Modelo.Pase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class AsistenciaData {

    private Connection con;

    public AsistenciaData() {
        try {
            con = Conexion.conectar();
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

    // GUARDAR ASISTENCIA
    public boolean guardarAsistencia(Asistencia asistencia) {

        String sql = "INSERT INTO asistencias "
                + "(ID_Socio, ID_Clase, ID_Pase, Fecha_Asistencia) "
                + "VALUES (?, ?, ?, ?)";

        if (con == null || asistencia == null
                || asistencia.getSocio() == null
                || asistencia.getClase() == null
                || asistencia.getPase() == null
                || asistencia.getFechaAsistencia() == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, asistencia.getSocio().getIdSocio());
            ps.setInt(2, asistencia.getClase().getId());
            ps.setInt(3, asistencia.getPase().getIdPase());
            ps.setDate(4, Date.valueOf(asistencia.getFechaAsistencia()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    asistencia.setId(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar asistencia: " + e.getMessage());
            return false;
        }
    }

    // BUSCAR ASISTENCIA POR ID
    public Asistencia buscarAsistencia(int id) {

        String sql = "SELECT * FROM asistencias WHERE ID_Asistencia = ?";

        if (con == null) {
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return cargarAsistencia(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar asistencia: " + e.getMessage());
        }

        return null;
    }

    // LISTAR ASISTENCIAS
    public List<Asistencia> listarAsistencias() {

        List<Asistencia> asistencias = new ArrayList<>();

        String sql = "SELECT * FROM asistencias";

        if (con == null) {
            return asistencias;
        }

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                asistencias.add(cargarAsistencia(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar asistencias: " + e.getMessage());
        }

        return asistencias;
    }

    // MODIFICAR ASISTENCIA
    public boolean modificarAsistencia(Asistencia asistencia) {

        String sql = "UPDATE asistencias SET "
                + "ID_Socio = ?, ID_Clase = ?, "
                + "ID_Pase = ?, Fecha_Asistencia = ? "
                + "WHERE ID_Asistencia = ?";

        if (con == null || asistencia == null
                || asistencia.getSocio() == null
                || asistencia.getClase() == null
                || asistencia.getPase() == null
                || asistencia.getFechaAsistencia() == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, asistencia.getSocio().getIdSocio());
            ps.setInt(2, asistencia.getClase().getId());
            ps.setInt(3, asistencia.getPase().getIdPase());
            ps.setDate(4, Date.valueOf(asistencia.getFechaAsistencia()));
            ps.setInt(5, asistencia.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar asistencia: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR ASISTENCIA
    public boolean eliminarAsistencia(int id) {

        String sql = "DELETE FROM asistencias WHERE ID_Asistencia = ?";

        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar asistencia: " + e.getMessage());
            return false;
        }
    }

    // CARGAR ASISTENCIA DESDE RESULTSET
    private Asistencia cargarAsistencia(ResultSet rs) throws SQLException {

        Socio socio = new Socio();
        socio.setIdSocio(rs.getInt("ID_Socio"));

        ClaseGym clase = new ClaseGym();
        clase.setId(rs.getInt("ID_Clase"));

        Pase pase = new Pase();
        pase.setIdPase(rs.getInt("ID_Pase"));

        Asistencia asistencia = new Asistencia();

        asistencia.setId(rs.getInt("ID_Asistencia"));
        asistencia.setSocio(socio);
        asistencia.setClase(clase);
        asistencia.setPase(pase);
        asistencia.setFechaAsistencia(
                rs.getDate("Fecha_Asistencia").toLocalDate()
        );

        return asistencia;
    }
}
