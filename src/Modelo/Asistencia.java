/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.time.LocalDate;


public class Asistencia {
    private int id;
    private Socio socio;
    private ClaseGym clase;
    private Pase pase;
    private LocalDate fechaAsistencia;

    public Asistencia() {
    }

    public Asistencia(int id, Socio socio, ClaseGym clase,
                      Pase pase, LocalDate fechaAsistencia) {
        this.id = id;
        this.socio = socio;
        this.clase = clase;
        this.pase = pase;
        this.fechaAsistencia = fechaAsistencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public ClaseGym getClase() {
        return clase;
    }

    public void setClase(ClaseGym clase) {
        this.clase = clase;
    }

    public Pase getPase() {
        return pase;
    }

    public void setPase(Pase pase) {
        this.pase = pase;
    }

    public LocalDate getFechaAsistencia() {
        return fechaAsistencia;
    }

    public void setFechaAsistencia(LocalDate fechaAsistencia) {
        this.fechaAsistencia = fechaAsistencia;
    }

    @Override
    public String toString() {
        return "Asistencia N° " + id
                + " - Socio ID: " + (socio != null ? socio.getIdSocio() : "Sin socio")
                + " - Clase ID: " + (clase != null ? clase.getId() : "Sin clase")
                + " - Fecha: " + fechaAsistencia;
    }
}