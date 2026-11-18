/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelo.Conexion;
import Modelo.Pase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaseData {

    public boolean guardarPase(Pase pase) {
        String sql = "INSERT INTO pase "
                + "(ID_Socio, ID_Membresia, EntradasIniciales, "
                + "CantidadEntradas, Fecha_Inicio, Fecha_Fin, Costo, Estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pase.getIdSocio());
            ps.setInt(2, pase.getIdMembresia());
            ps.setInt(3, pase.getEntradasIniciales());
            ps.setInt(4, pase.getCantidadEntradas());
            ps.setDate(5, Date.valueOf(pase.getFechaInicio()));
            ps.setDate(6, Date.valueOf(pase.getFechaFin()));
            ps.setDouble(7, pase.getCosto());
            ps.setBoolean(8, pase.isEstado());

            return ps.executeUpdate() == 1;

        } catch (SQLException | RuntimeException e) {
            System.err.println("Error al guardar pase: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarPase(Pase pase) {
        String sql = "UPDATE pase SET "
                + "ID_Socio = ?, ID_Membresia = ?, "
                + "EntradasIniciales = ?, CantidadEntradas = ?, "
                + "Fecha_Inicio = ?, Fecha_Fin = ?, "
                + "Costo = ?, Estado = ? "
                + "WHERE ID_Pase = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pase.getIdSocio());
            ps.setInt(2, pase.getIdMembresia());
            ps.setInt(3, pase.getEntradasIniciales());
            ps.setInt(4, pase.getCantidadEntradas());
            ps.setDate(5, Date.valueOf(pase.getFechaInicio()));
            ps.setDate(6, Date.valueOf(pase.getFechaFin()));
            ps.setDouble(7, pase.getCosto());
            ps.setBoolean(8, pase.isEstado());
            ps.setInt(9, pase.getIdPase());

            return ps.executeUpdate() == 1;

        } catch (SQLException | RuntimeException e) {
            System.err.println("Error al modificar pase: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarPase(int idPase) {
        String sql = "DELETE FROM pase WHERE ID_Pase = ?";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPase);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Error al eliminar pase: " + e.getMessage());
            return false;
        }
    }

    public List<Pase> listarPases() {
        List<Pase> lista = new ArrayList<>();

        String sql = "SELECT ID_Pase, ID_Socio, ID_Membresia, "
                + "EntradasIniciales, CantidadEntradas, "
                + "Fecha_Inicio, Fecha_Fin, Costo, Estado "
                + "FROM pase ORDER BY ID_Pase";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pase p = new Pase();

                p.setIdPase(rs.getInt("ID_Pase"));
                p.setIdSocio(rs.getInt("ID_Socio"));
                p.setIdMembresia(rs.getInt("ID_Membresia"));
                p.setEntradasIniciales(rs.getInt("EntradasIniciales"));
                p.setCantidadEntradas(rs.getInt("CantidadEntradas"));

                Date inicio = rs.getDate("Fecha_Inicio");
                Date fin = rs.getDate("Fecha_Fin");

                p.setFechaInicio(
                        inicio == null ? null : inicio.toLocalDate());
                p.setFechaFin(
                        fin == null ? null : fin.toLocalDate());

                p.setCosto(rs.getDouble("Costo"));
                p.setEstado(rs.getBoolean("Estado"));

                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar pases: " + e.getMessage());
        }

        return lista;
    }

    public boolean tienePaseActivo(int idSocio) {

        String sql = "SELECT COUNT(*) FROM pase "
                + "WHERE ID_Socio = ? AND Estado = 1";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSocio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar pase: " + e.getMessage());
        }

        return false;
    }
    private Pase cargarPase(ResultSet rs) throws SQLException {

    Pase p = new Pase();

    p.setIdPase(rs.getInt("ID_Pase"));
    p.setIdSocio(rs.getInt("ID_Socio"));
    p.setIdMembresia(rs.getInt("ID_Membresia"));
    p.setEntradasIniciales(rs.getInt("EntradasIniciales"));
    p.setCantidadEntradas(rs.getInt("CantidadEntradas"));

    Date inicio = rs.getDate("Fecha_Inicio");
    Date fin = rs.getDate("Fecha_Fin");

    p.setFechaInicio(
        inicio == null ? null : inicio.toLocalDate()
    );

    p.setFechaFin(
        fin == null ? null : fin.toLocalDate()
    );

    p.setCosto(rs.getDouble("Costo"));
    p.setEstado(rs.getBoolean("Estado"));

    return p;
}
    public Pase buscarPasePorSocio(int idSocio) {

    String sql = "SELECT * FROM pase "
            + "WHERE ID_Socio = ? "
            + "ORDER BY Fecha_Inicio DESC, ID_Pase DESC "
            + "LIMIT 1";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idSocio);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return cargarPase(rs);
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar pase: " + e.getMessage());
    }

    return null;
}
    public int buscarEntradasDisponibles(int idSocio) {

    String sql = "SELECT CantidadEntradas FROM pase "
            + "WHERE ID_Socio = ? "
            + "AND Estado = 1 "
            + "AND Fecha_Fin >= CURDATE() "
            + "AND CantidadEntradas > 0 "
            + "ORDER BY Fecha_Inicio DESC, ID_Pase DESC "
            + "LIMIT 1";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idSocio);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("CantidadEntradas");
            }
        }

    } catch (SQLException e) {
        System.err.println("Error al consultar entradas: " + e.getMessage());
    }

    return -1;
}
    public List<Pase> buscarPasesVencidosPorFecha() {

    List<Pase> lista = new ArrayList<>();

    String sql = "SELECT * FROM pase "
            + "WHERE Estado = 1 "
            + "AND Fecha_Fin < CURDATE() "
            + "ORDER BY Fecha_Fin";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            lista.add(cargarPase(rs));
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar vencimientos: " + e.getMessage());
    }

    return lista;
}
public List<Pase> buscarPasesAgotados() {

    List<Pase> lista = new ArrayList<>();

    String sql = "SELECT * FROM pase "
            + "WHERE Estado = 1 "
            + "AND CantidadEntradas <= 0 "
            + "ORDER BY ID_Pase";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            lista.add(cargarPase(rs));
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar pases agotados: " + e.getMessage());
    }

    return lista;
}
public List<Pase> buscarTodosLosPasesVencidos() {

    List<Pase> lista = new ArrayList<>();

    String sql = "SELECT * FROM pase "
            + "WHERE Estado = 1 "
            + "AND (Fecha_Fin < CURDATE() "
            + "OR CantidadEntradas <= 0) "
            + "ORDER BY Fecha_Fin";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            lista.add(cargarPase(rs));
        }

    } catch (SQLException e) {
        System.err.println("Error al buscar pases vencidos: " + e.getMessage());
    }

    return lista;
}
public List<Pase> listarPasesDisponiblesPorSocio(int idSocio) {

    List<Pase> lista = new ArrayList<>();

    String sql = "SELECT * FROM pase "
            + "WHERE ID_Socio = ? "
            + "AND Estado = 1 "
            + "AND Fecha_Inicio <= CURDATE() "
            + "AND Fecha_Fin >= CURDATE() "
            + "AND CantidadEntradas > 0 "
            + "ORDER BY Fecha_Fin ASC";

    try (Connection con = Conexion.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idSocio);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(cargarPase(rs));
            }
        }

    } catch (SQLException e) {
        System.err.println(
                "Error al listar pases disponibles del socio: "
                + e.getMessage()
        );
    }

    return lista;
}
}

