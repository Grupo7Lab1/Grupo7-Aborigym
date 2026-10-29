/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Mrjoshep
 */
public class Membresia {
    private int id;
    private String tipo;
    private double precio;
    private int duracionDias;
 
    public Membresia() {
    }
 
    public Membresia(int id, String tipo, double precio, int duracionDias) {
        this.id = id;
        this.tipo = tipo;
        this.precio = precio;
        this.duracionDias = duracionDias;
    }
 
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
 
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
 
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
 
    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }
}
