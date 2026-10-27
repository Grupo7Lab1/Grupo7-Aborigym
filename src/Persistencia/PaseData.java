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
        String sql = "INSERT INTO pases "
                + "(ID_Socio, ID_Membresia, EntradasIniciales, "
                + "CantidadEntradas, Fecha_Inicio, Fecha_Fin, Costo, Estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

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
        String sql = "UPDATE pases SET "
                + "ID_Socio = ?, ID_Membresia = ?, "
                + "EntradasIniciales = ?, CantidadEntradas = ?, "
                + "Fecha_Inicio = ?, Fecha_Fin = ?, "
                + "Costo = ?, Estado = ? "
                + "WHERE ID_Pase = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

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
        String sql = "DELETE FROM pases WHERE ID_Pase = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

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
                + "FROM pases ORDER BY ID_Pase";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
}
