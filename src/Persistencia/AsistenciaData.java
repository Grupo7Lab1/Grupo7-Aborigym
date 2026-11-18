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
import java.time.LocalDate;
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

    // VERIFICAR SI EL SOCIO YA TIENE ASISTENCIA EN ESA CLASE Y FECHA
    public boolean existeAsistencia(int idSocio, int idClase, LocalDate fecha) {
        String sql = "SELECT COUNT(*) FROM asistencia "
                + "WHERE ID_Socio = ? AND ID_Clase = ? AND Fecha_Asistencia = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            ps.setInt(2, idClase);
            ps.setDate(3, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar duplicado de asistencia: " + e.getMessage());
        }
        return false;
    }

    // VERIFICAR CUPO MAXIMO DISPONIBLE EN LA CLASE
    public boolean hayCupoDisponible(int idClase, LocalDate fecha) {
        String sql = "SELECT c.Capacidad, COUNT(a.ID_Asistencia) AS Inscriptos "
                + "FROM clasegym c "
                + "LEFT JOIN asistencia a ON c.ID_Clase = a.ID_Clase AND a.Fecha_Asistencia = ? "
                + "WHERE c.ID_Clase = ? AND c.Estado = 1 "
                + "GROUP BY c.ID_Clase, c.Capacidad";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setInt(2, idClase);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int capacidad = rs.getInt("Capacidad");
                    int inscriptos = rs.getInt("Inscriptos");
                    return inscriptos < capacidad;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar cupo de clase: " + e.getMessage());
        }
        return false;
    }

    // GUARDAR ASISTENCIA, VALIDAR CUPO/PASE Y DESCONTAR UNA ENTRADA
    public boolean guardarAsistencia(Asistencia asistencia) {

        if (con == null || asistencia == null
                || asistencia.getSocio() == null
                || asistencia.getClase() == null
                || asistencia.getPase() == null
                || asistencia.getFechaAsistencia() == null) {
            return false;
        }

        if (existeAsistencia(asistencia.getSocio().getIdSocio(),
                asistencia.getClase().getId(),
                asistencia.getFechaAsistencia())) {
            System.out.println("El socio ya está inscripto en esa clase para esa fecha.");
            return false;
        }

        if (!hayCupoDisponible(asistencia.getClase().getId(), asistencia.getFechaAsistencia())) {
            System.out.println("No hay cupo disponible en la clase seleccionada.");
            return false;
        }

        String sqlValidar = "SELECT CantidadEntradas, Estado, Fecha_Inicio, Fecha_Fin "
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
                    Date fechaInicio = rs.getDate("Fecha_Inicio");
                    Date fechaFin = rs.getDate("Fecha_Fin");
                    LocalDate fechaAsist = asistencia.getFechaAsistencia();

                    if (!activo || entradas <= 0
                            || fechaFin == null
                            || fechaFin.toLocalDate().isBefore(fechaAsist)
                            || (fechaInicio != null && fechaInicio.toLocalDate().isAfter(fechaAsist))) {
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

            // 4. CONFIRMAR OPERACION
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

    // ELIMINAR ASISTENCIA Y REINTEGRAR LA ENTRADA AL PASE
    public boolean eliminarAsistencia(int id) {
        Asistencia asist = buscarAsistencia(id);
        if (con == null || asist == null) {
            return false;
        }

        String sqlDelete = "DELETE FROM asistencia WHERE ID_Asistencia = ?";
        String sqlReintegrar = "UPDATE pase SET CantidadEntradas = CantidadEntradas + 1 WHERE ID_Pase = ?";

        try {
            con.setAutoCommit(false);

            try (PreparedStatement psDel = con.prepareStatement(sqlDelete)) {
                psDel.setInt(1, id);
                if (psDel.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            try (PreparedStatement psReint = con.prepareStatement(sqlReintegrar)) {
                psReint.setInt(1, asist.getPase().getIdPase());
                psReint.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println("Error al eliminar asistencia: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al restaurar autoCommit: " + e.getMessage());
            }
        }
    }

    // CARGAR ASISTENCIA CON DATOS COMPLETOS (JOIN)
    private Asistencia cargarAsistenciaCompleta(ResultSet rs) throws SQLException {
        Socio socio = new Socio();
        socio.setIdSocio(rs.getInt("ID_Socio"));
        socio.setDni(rs.getString("DNI"));
        socio.setNombre(rs.getString("Nombre"));
        socio.setApellido(rs.getString("Apellido"));

        ClaseGym clase = new ClaseGym();
        clase.setId(rs.getInt("ID_Clase"));
        clase.setDisciplina(rs.getString("Disciplina"));
        clase.setHorario(rs.getString("Horario"));

        Pase pase = new Pase();
        pase.setIdPase(rs.getInt("ID_Pase"));
        pase.setEntradasIniciales(rs.getInt("EntradasIniciales"));
        pase.setCantidadEntradas(rs.getInt("CantidadEntradas"));

        Asistencia asistencia = new Asistencia();
        asistencia.setId(rs.getInt("ID_Asistencia"));
        asistencia.setSocio(socio);
        asistencia.setClase(clase);
        asistencia.setPase(pase);
        asistencia.setFechaAsistencia(rs.getDate("Fecha_Asistencia").toLocalDate());

        return asistencia;
    }

    // CONSULTA INFORME: HISTORIAL DE ASISTENCIAS DE UN SOCIO
    public List<Asistencia> listarAsistenciaPorSocio(int idSocio) {
        List<Asistencia> lista = new ArrayList<>();

        String sql = "SELECT a.*, s.DNI, s.Nombre, s.Apellido, "
                + "c.Disciplina, c.Horario, p.EntradasIniciales, p.CantidadEntradas "
                + "FROM asistencia a "
                + "INNER JOIN socio s ON a.ID_Socio = s.ID_Socio "
                + "INNER JOIN clasegym c ON a.ID_Clase = c.ID_Clase "
                + "INNER JOIN pase p ON a.ID_Pase = p.ID_Pase "
                + "WHERE a.ID_Socio = ? "
                + "ORDER BY a.Fecha_Asistencia DESC, a.ID_Asistencia DESC";

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(cargarAsistenciaCompleta(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar historial por socio: " + e.getMessage());
        }

        return lista;
    }

    // CONSULTA INFORME: CONCURRENCIA A UNA CLASE EN UNA FECHA DETERMINADA
    public List<Asistencia> listarAsistenciaPorClaseYFecha(int idClase, LocalDate fecha) {
        List<Asistencia> lista = new ArrayList<>();

        String sql = "SELECT a.*, s.DNI, s.Nombre, s.Apellido, "
                + "c.Disciplina, c.Horario, p.EntradasIniciales, p.CantidadEntradas "
                + "FROM asistencia a "
                + "INNER JOIN socio s ON a.ID_Socio = s.ID_Socio "
                + "INNER JOIN clasegym c ON a.ID_Clase = c.ID_Clase "
                + "INNER JOIN pase p ON a.ID_Pase = p.ID_Pase "
                + "WHERE a.ID_Clase = ? AND a.Fecha_Asistencia = ? "
                + "ORDER BY s.Apellido, s.Nombre";

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            ps.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(cargarAsistenciaCompleta(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar concurrencia de clase: " + e.getMessage());
        }

        return lista;
    }

    // LISTAR TODAS LAS ASISTENCIAS
    public List<Asistencia> listarAsistencia() {
        List<Asistencia> lista = new ArrayList<>();

        String sql = "SELECT a.*, s.DNI, s.Nombre, s.Apellido, "
                + "c.Disciplina, c.Horario, p.EntradasIniciales, p.CantidadEntradas "
                + "FROM asistencia a "
                + "INNER JOIN socio s ON a.ID_Socio = s.ID_Socio "
                + "INNER JOIN clasegym c ON a.ID_Clase = c.ID_Clase "
                + "INNER JOIN pase p ON a.ID_Pase = p.ID_Pase "
                + "ORDER BY a.Fecha_Asistencia DESC, a.ID_Asistencia DESC";

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(cargarAsistenciaCompleta(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar asistencias: " + e.getMessage());
        }

        return lista;
    }

    // BUSCAR ASISTENCIA POR ID
    public Asistencia buscarAsistencia(int id) {
        String sql = "SELECT a.*, s.DNI, s.Nombre, s.Apellido, "
                + "c.Disciplina, c.Horario, p.EntradasIniciales, p.CantidadEntradas "
                + "FROM asistencia a "
                + "INNER JOIN socio s ON a.ID_Socio = s.ID_Socio "
                + "INNER JOIN clasegym c ON a.ID_Clase = c.ID_Clase "
                + "INNER JOIN pase p ON a.ID_Pase = p.ID_Pase "
                + "WHERE a.ID_Asistencia = ?";

        if (con == null) {
            return null;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return cargarAsistenciaCompleta(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar asistencia: " + e.getMessage());
        }

        return null;
    }
}
