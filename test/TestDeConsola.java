/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import Modelo.entidad;
import Persistencia.entidadData;
import Modelo.Conexion;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Nestor Marchizone
 */
public class TestDeConsola {
     public static void main(String[] args) {

         Scanner teclado = new Scanner(System.in);
         System.out.println("       SISTEMA ABORIGYM");
        // 1. PROBAR CONEXION A MARIADB
        System.out.println("\n1. PROBANDO CONEXION A MARIADB...");
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                System.out.println("No se pudo establecer la conexion.");
                teclado.close();
                return;
            }
            System.out.println("Conexion establecida correctamente.");
        } catch (Exception e) {
            System.out.println("Error de conexion: " + e.getMessage());
            teclado.close();
            return;
        }
        entidadData entidadData = new entidadData();
        int opcion;
        do {
            System.out.println("\n========== MENU ABORIGYM ==========");
            System.out.println("1. Registrar entidad");
            System.out.println("2. Listar entidad");
            System.out.println("3. Buscar entidad por DNI");
            System.out.println("4. Buscar entidad por ID");
            System.out.println("5. Actualizar entidad");
            System.out.println("6. Baja logica");
            System.out.println("7. Alta logica");
            System.out.println("8. Borrar entidad definitivamente");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            try {
                opcion = Integer.parseInt(teclado.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
            switch (opcion) {
                // 2. REGISTRAR SOCIO
                case 1: {
                    System.out.println("\n--- REGISTRAR ENTIDAD ---");

                    System.out.print("DNI: ");
                    String dni = teclado.nextLine();

                  entidad existente = entidadData.buscarPorDni(dni);

                    if (existente != null) {
                        System.out.println("Ya existe una entidad con ese DNI.");
                        System.out.println(existente);
                        break;
                    }

                    System.out.print("Nombre: ");
                    String nombre = teclado.nextLine();

                    System.out.print("Apellido: ");
                    String apellido = teclado.nextLine();
                    int edad;
                    try {
                        System.out.print("Edad: ");
                        edad = Integer.parseInt(teclado.nextLine());

                        if (edad <= 0) {
                            System.out.println("La edad debe ser mayor que cero.");
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("La edad debe ser un numero entero.");
                        break;
                    }
                    System.out.print("Correo electronico: ");
                    String correo = teclado.nextLine();
                    System.out.print("Telefono: ");
                    String telefono = teclado.nextLine();
                    Socio socio = new entidad(
                            dni, nombre, apellido, edad,
                            correo, telefono, true
                    );
                    if (entidadData.guardarSocio(socio)) {
                        System.out.println("entidad registrada correctamente.");
                    } else {
                        System.out.println("No se pudo registrar la entidad.");
                    }
                    break;
                }
                // 3. LISTAR SOCIOS
                case 2: {
                    System.out.println("\n--- LISTADO DE ENTIDAD ---");
                    List<entidad> entidades = entidadData.listarentidades();
                    if (entidades.isEmpty()) {
                        System.out.println("No hay entidades registradas.");
                    } else {
                        for (entidad e : entidades) {
                            System.out.println(e);
                        }
                    }
                    break;
                }
                // 4. BUSCAR POR DNI
                case 3: {
                    System.out.println("\n--- BUSCAR ENTIDADES POR DNI ---");
                    System.out.print("Ingrese DNI: ");
                    String dni = teclado.nextLine();
                    entidad e = entidadData.buscarPorDni(dni);
                    if (s != null) {
                        System.out.println("entidad encontrada:");
                        System.out.println(e);
                    } else {
                        System.out.println("No se encontro la entidad.");
                    }
                    break;
                }
                // 5. BUSCAR POR ID
                case 4: {
                    System.out.println("\n--- BUSCAR ENTIDAD POR ID ---");
                    int id = leerId(teclado);
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                    entidad e = entidadData.buscarPorId(id);
                   if (e != null) {
                        System.out.println(s);
                    } else {
                        System.out.println("No se encontro la entidad.");
                    }
                    break;
                }
                // 6. ACTUALIZAR ENTIDAD
                case 5: {
                    System.out.println("\n--- ACTUALIZAR ENTIDAD ---");
                    int id = leerId(teclado);
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                    entidad e = entidadData.buscarPorId(id);
                    if (e == null) {
                        System.out.println("No se encontro la entidad.");
                        break;
                    }
                    System.out.println("Datos actuales:");
                    System.out.println(e);
                    System.out.println("\nIngrese los nuevos datos:");
                    System.out.print("DNI: ");
                    s.setDni(teclado.nextLine());
                    System.out.print("Nombre: ");
                    s.setNombre(teclado.nextLine());
                    System.out.print("Apellido: ");
                    s.setApellido(teclado.nextLine());
                    try {
                        System.out.print("Edad: ");
                        int edad = Integer.parseInt(teclado.nextLine());
                        if (edad <= 0) {
                            System.out.println("Edad invalida.");
                            break;
                        }
                        e.setEdad(edad);
                    } catch (NumberFormatException e) {
                        System.out.println("La edad debe ser un numero entero.");
                        break;
                    }
                    System.out.print("Correo: ");
                    s.setCorreo(teclado.nextLine());
                    System.out.print("Telefono: ");
                    s.setTelefono(teclado.nextLine());
                    if (entidadData.modificar(e)) {
                        System.out.println("Socio actualizado correctamente.");
                    } else {
                        System.out.println("No se pudo actualizar la entidad.");
                    }
                    break;
                }
                // 7. BAJA LOGICA
                case 6: {
                    System.out.println("\n--- BAJA LOGICA ---");
                    int id = leerId(teclado);
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                    if (entidadData.darDeBaja(id)) {
                        System.out.println("entidaddado de baja. El registro se conserva.");
                    } else {
                        System.out.println("No se pudo dar de baja la entidad.");
                    }
                    break;
                }
                // 8. ALTA lOGICA
                case 7: {
                    System.out.println("\n--- ALTA LOGICA ---");
                    int id = leerId(teclado);
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                    if (entidadData.darDeAlta(id)) {
                        System.out.println("Socio reactivado correctamente.");
                    } else {
                        System.out.println("No se pudo dar de alta a la entidad.");
                    }
                    break;
                }
                // 9. BORRAR DEFINITIVAMENTE
                case 8: {
                    System.out.println("\n--- BORRAR ENTIDAD ---");
                    int id = leerId(teclado);
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                   entidad e = entidadData.buscarPorId(id);
                    if (e == null) {
                        System.out.println("No se encontro la entidad.");
                        break;
                    }
                    System.out.println("entidad seleccionada:");
                    System.out.println(e);
                    System.out.print("Esta operacion es definitiva."+ "Escriba SI para confirmar: "
                    );
                    String confirmacion = teclado.nextLine();
                    if (confirmacion.equalsIgnoreCase("SI")) {
                        if (entidadData.eliminar(id)) {
                            System.out.println("Entidad eliminada definitivamente.");
                        } else {
                            System.out.println("No se pudo borrar. "+ "Puede tener pases o asistencias asociados.");
                        }
                    } else {
                        System.out.println("Operacion cancelada.");
                    }
                    break;
                }
                case 0:
                    System.out.println("Prueba finalizada.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
        teclado.close();
    }
    // METODO AUXILIAR PARA LEER EL ID
    private static int leerId(Scanner teclado) {
        try {
            System.out.print("Ingrese el ID de la entidad: ");
            return Integer.parseInt(teclado.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

