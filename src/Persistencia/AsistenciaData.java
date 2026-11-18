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

    // GUARDAR ASISTENCIA Y DESCUENTA UNA ENTRADA 
    public boolean guardarAsistencia(Asistencia asistencia) {

        if (con == null || asistencia == null
                || asistencia.getSocio() == null
                || asistencia.getClase() == null
                || asistencia.getPase() == null
                || asistencia.getFechaAsistencia() == null) {
            return false;
        }

        String sqlValidar = "SELECT CantidadEntradas, Estado, Fecha_Fin "
                + "FROM pase "
                + "WHERE ID_Pase = ? AND ID_Socio = ? FOR UPDATE";

        String sqlInsertar = "INSERT INTO asistencia "
                + "(ID_Socio, ID_Clase, ID_Pase, Fecha_Asistencia) "
                + "VALUES (?, ?, ?, ?)";

        String sqlDescontar = "UPDATE pase "
                + "SET CantidadEntradas = CantidadEntradas - 1 "
                + "WHERE ID_Pase = ? AND CantidadEntradas > 0";

        try {
            con.setAutoCommit(false);

            // 1. VALIDAR EL PASE
            try (PreparedStatement ps = con.prepareStatement(sqlValidar)) {

                ps.setInt(1, asistencia.getPase().getIdPase());
                ps.setInt(2, asistencia.getSocio().getIdSocio());

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    int entradas = rs.getInt("CantidadEntradas");
                    boolean activo = rs.getBoolean("Estado");
                    Date fechaFin = rs.getDate("Fecha_Fin");

                    if (!activo || entradas <= 0
                            || fechaFin == null
                            || fechaFin.toLocalDate().isBefore(
                                    java.time.LocalDate.now())) {

                        con.rollback();
                        return false;
                    }
                }
            }

            // 2. REGISTRAR LA ASISTENCIA
            try (PreparedStatement ps = con.prepareStatement(
                    sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {

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
            }

            // 3. DESCONTAR UNA ENTRADA
            try (PreparedStatement ps = con.prepareStatement(sqlDescontar)) {

                ps.setInt(1, asistencia.getPase().getIdPase());

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            // 4. CONFIRMAR OPERACIÓN
            con.commit();
            return true;

        } catch (SQLException e) {

            try {
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al deshacer operación: " + ex.getMessage());
            }

            System.out.println("Error al guardar asistencia: " + e.getMessage());
            return false;

        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al restaurar conexión: " + e.getMessage());
            }
        }
    }

    // MODIFICAR ASISTENCIA
    public boolean modificarAsistencia(Asistencia asistencia) {

        String sql = "UPDATE asistencia SET "
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

        String sql = "DELETE FROM asistencia WHERE ID_Asistencia = ?";

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

    public List<Asistencia> listarAsistenciaPorSocio(int idSocio) {

        List<Asistencia> lista = new ArrayList<>();

        String sql = "SELECT * FROM asistencia "
                + "WHERE ID_Socio = ? "
                + "ORDER BY Fecha_Asistencia DESC";

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(cargarAsistencia(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar historial: " + e.getMessage());
        }

        return lista;
    }
    
public List<Asistencia> listarAsistencia() {

    List<Asistencia> lista = new ArrayList<>();

    String sql = "SELECT * FROM asistencia "
            + "ORDER BY Fecha_Asistencia DESC, ID_Asistencia DESC";

    if (con == null) {
        return lista;
    }

    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            lista.add(cargarAsistencia(rs));
        }

    } catch (SQLException e) {
        System.out.println("Error al listar asistencias: "
                + e.getMessage());
    }

    return lista;
}

}
