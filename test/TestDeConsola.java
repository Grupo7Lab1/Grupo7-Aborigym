/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import Modelo.Asistencia;
import Modelo.ClaseGym;
import Modelo.Conexion;
import Modelo.Entrenador;
import Modelo.Pase;
import Modelo.Socio;
import Persistencia.AsistenciaData;
import Persistencia.ClaseGymData;
import Persistencia.EntrenadorData;
import Persistencia.PaseData;
import Persistencia.SocioData;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Rotta Alejandro
 */
public class TestDeConsola {
     public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
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
            SocioData socioData = new SocioData();
            int opcion;
            do {
                System.out.println("\n========== MENU ABORIGYM ==========");
                System.out.println("1. Registrar socio");
                System.out.println("2. Listar socios");
                System.out.println("3. Buscar socio por DNI");
                System.out.println("4. Buscar socio por ID");
                System.out.println("5. Actualizar socio");
                System.out.println("6. Baja logica");
                System.out.println("7. Alta logica");
                System.out.println("8. Borrar socio definitivamente");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opcion: ");
                try {
                    opcion = Integer.parseInt(teclado.nextLine());
                } catch (NumberFormatException e) {
                    opcion = -1;
                }
                switch (opcion) {
                    case 1 ->  {
                        System.out.println("\n--- REGISTRAR SOCIO ---");
                        
                        System.out.print("DNI: ");
                        String dni = teclado.nextLine();
                        
                        Socio existente = socioData.buscarPorDni(dni);
                        
                        if (existente != null) {
                            System.out.println("Ya existe un socio con ese DNI.");
                            System.out.println(existente);
                        }
                        
                        System.out.print("Nombre: ");
                        String nombre = teclado.nextLine();
                        
                        System.out.print("Apellido: ");
                        String apellido = teclado.nextLine();
                        int edad = 0;
                        try {
                            System.out.print("Edad: ");
                            edad = Integer.parseInt(teclado.nextLine());
                            
                            if (edad <= 0) {
                                System.out.println("La edad debe ser mayor que cero.");
                            }
                            
                        } catch (NumberFormatException e) {
                            System.out.println("La edad debe ser un numero entero.");
                        }
                        System.out.print("Correo electronico: ");
                        String correo = teclado.nextLine();
                        System.out.print("Telefono: ");
                        String telefono = teclado.nextLine();
                        Socio socio = new Socio(
                                dni, nombre, apellido, edad,
                                correo, telefono, true
                        );
                        if (socioData.guardarSocio(socio)) {
                            System.out.println("Socio registrado correctamente.");
                        } else {
                            System.out.println("No se pudo registrar el socio.");
                        }
                    }
                    case 2 ->  {
                        System.out.println("\n--- LISTADO DE SOCIOS ---");
                        List<Socio> socios = socioData.listarSocio();
                        if (socios.isEmpty()) {
                            System.out.println("No hay socios registrados.");
                        } else {
                            for (Socio s : socios) {
                                System.out.println(s);
                            }
                        }
                    }
                    case 3 ->  {
                        System.out.println("\n--- BUSCAR SOCIO POR DNI ---");
                        System.out.print("Ingrese DNI: ");
                        String dni = teclado.nextLine();
                        Socio s = socioData.buscarPorDni(dni);
                        if (s != null) {
                            System.out.println("Socio encontrado:");
                            System.out.println(s);
                        } else {
                            System.out.println("No se encontro el socio.");
                        }
                    }
                    case 4 ->  {
                        System.out.println("\n--- BUSCAR SOCIO POR ID ---");
                        int id = leerId(teclado);
                        if (id <= 0) {
                            System.out.println("ID invalido.");
                        }
                        Socio s = socioData.buscarPorId(id);
                        if (s != null) {
                            System.out.println(s);
                        } else {
                            System.out.println("No se encontro el socio.");
                        }
                    }
                    case 5 ->  {
                        System.out.println("\n--- ACTUALIZAR SOCIO ---");
                        int id = leerId(teclado);
                        if (id <= 0) {
                            System.out.println("ID invalido.");
                        }
                        Socio s = socioData.buscarPorId(id);
                        if (s == null) {
                            System.out.println("No se encontro el socio.");
                        }
                        System.out.println("Datos actuales:");
                        System.out.println(s);
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
                            }
                            s.setEdad(edad);
                        } catch (NumberFormatException e) {
                            System.out.println("La edad debe ser un numero entero.");
                        }
                        System.out.print("Correo: ");
                        s.setCorreo(teclado.nextLine());
                        System.out.print("Telefono: ");
                        s.setTelefono(teclado.nextLine());
                        if (socioData.modificar(s)) {
                            System.out.println("Socio actualizado correctamente.");
                        } else {
                            System.out.println("No se pudo actualizar el socio.");
                        }
                    }
                    case 6 ->  {
                        System.out.println("\n--- BAJA LOGICA ---");
                        int id = leerId(teclado);
                        if (id <= 0) {
                            System.out.println("ID invalido.");
                        }
                        if (socioData.darDeBaja(id)) {
                            System.out.println(
                                    "Socio dado de baja. El registro se conserva."
                            );
                        } else {
                            System.out.println("No se pudo dar de baja al socio.");
                        }
                    }
                    case 7 ->  {
                        System.out.println("\n--- ALTA LOGICA ---");
                        int id = leerId(teclado);
                        if (id <= 0) {
                            System.out.println("ID invalido.");
                        }
                        if (socioData.darDeAlta(id)) {
                            System.out.println("Socio reactivado correctamente.");
                        } else {
                            System.out.println("No se pudo dar de alta al socio.");
                        }
                    }
                    case 8 ->  {
                        System.out.println("\n--- BORRAR SOCIO ---");
                        int id = leerId(teclado);
                        if (id <= 0) {
                            System.out.println("ID invalido.");
                        }
                        Socio s = socioData.buscarPorId(id);
                        if (s == null) {
                            System.out.println("No se encontro el socio.");
                        }
                        System.out.println("Socio seleccionado:");
                        System.out.println(s);
                        System.out.print(
                                "Esta operacion es definitiva. "
                                        + "Escriba SI para confirmar: "
                        );
                        String confirmacion = teclado.nextLine();
                        if (confirmacion.equalsIgnoreCase("SI")) {
                            if (socioData.eliminar(id)) {
                                System.out.println("Socio eliminado definitivamente.");
                            } else {
                                System.out.println(
                                        "No se pudo borrar. "
                                                + "Puede tener pases o asistencias asociados."
                                );
                            }
                        } else {
                            System.out.println("Operacion cancelada.");
                        }
                    }
                    case 0 -> System.out.println("Prueba finalizada.");
                    default -> System.out.println("Opcion invalida.");
                }
                // 2. REGISTRAR SOCIO
                // 3. LISTAR SOCIOS
                // 4. BUSCAR POR DNI
                // 5. BUSCAR POR ID
                // 6. ACTUALIZAR SOCIO
                // 7. BAJA LOGICA
                // 8. ALTA lOGICA
                // 9. BORRAR DEFINITIVAMENTE
                            } while (opcion != 0);
        }    }
    // MENU ENTRENADORES
    private static void menuEntrenadores(Scanner teclado, EntrenadorData data) {
        int opcion;
        do {
            System.out.println("\n---------- ENTRENADORES ----------");
            System.out.println("1. Registrar entrenador");
            System.out.println("2. Listar entrenadores");
            System.out.println("3. Buscar entrenador por ID");
            System.out.println("4. Actualizar entrenador");
            System.out.println("5. Baja logica");
            System.out.println("6. Alta logica");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 ->  {
                    System.out.println("\n--- REGISTRAR ENTRENADOR ---");
                    System.out.print("DNI: ");
                    String dni = teclado.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = teclado.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = teclado.nextLine();
                    System.out.print("Especialidad (ej. Spinning): ");
                    String especialidad = teclado.nextLine();

                    Entrenador e = new Entrenador(0, dni, nombre, apellido,
                            especialidad, true);
                    if (data.insertar(e)) {
                        System.out.println("Entrenador registrado correctamente.");
                    } else {
                        System.out.println("No se pudo registrar el entrenador.");
                    }
                }
                case 2 ->  {
                    System.out.println("\n--- LISTADO DE ENTRENADORES ---");
                    List<Entrenador> lista = data.listarEntrenadores();
                    if (lista.isEmpty()) {
                        System.out.println("No hay entrenadores registrados.");
                    } else {
                        for (Entrenador e : lista) {
                            mostrarEntrenador(e);
                        }
                    }
                }
                case 3 ->  {
                    System.out.println("\n--- BUSCAR ENTRENADOR POR ID ---");
                    int id = leerEntero(teclado, "Ingrese el ID del entrenador: ");
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                    }
                    Entrenador e = data.buscarPorId(id);
                    if (e != null) {
                        mostrarEntrenador(e);
                    } else {
                        System.out.println("No se encontro el entrenador.");
                    }
                }
                case 4 ->  {
                    System.out.println("\n--- ACTUALIZAR ENTRENADOR ---");
                    int id = leerEntero(teclado, "Ingrese el ID del entrenador: ");
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                    }
                    Entrenador e = data.buscarPorId(id);
                    if (e == null) {
                        System.out.println("No se encontro el entrenador.");
                    }
                    System.out.println("Datos actuales:");
                    mostrarEntrenador(e);
                    System.out.println("\nIngrese los nuevos datos:");
                    System.out.print("DNI: ");
                    e.setDni(teclado.nextLine());
                    System.out.print("Nombre: ");
                    e.setNombre(teclado.nextLine());
                    System.out.print("Apellido: ");
                    e.setApellido(teclado.nextLine());
                    System.out.print("Especialidad: ");
                    e.setEspecialidad(teclado.nextLine());

                    if (data.modificar(e)) {
                        System.out.println("Entrenador actualizado correctamente.");
                    } else {
                        System.out.println("No se pudo actualizar el entrenador.");
                    }
                }
                case 5 ->  {
                    int id = leerEntero(teclado, "ID del entrenador a dar de baja: ");
                    if (id > 0 && data.bajaLogica(id)) {
                        System.out.println("Entrenador dado de baja.");
                    } else {
                        System.out.println("No se pudo dar de baja al entrenador.");
                    }
                }
                case 6 ->  {
                    int id = leerEntero(teclado, "ID del entrenador a dar de alta: ");
                    if (id > 0 && data.altaLogica(id)) {
                        System.out.println("Entrenador reactivado.");
                    } else {
                        System.out.println("No se pudo dar de alta al entrenador.");
                    }
                }
                case 0 -> System.out.println("Volviendo al menu principal.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

     // CLASES
     private static void menuClases(Scanner teclado, ClaseGymData claseData,
            EntrenadorData entrenadorData) {
        int opcion;
        do {
            System.out.println("\n---------- CLASES ----------");
            System.out.println("1. Registrar clase");
            System.out.println("2. Listar clases");
            System.out.println("3. Buscar clase por ID");
            System.out.println("4. Modificar clase");
            System.out.println("5. Eliminar clase");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 ->  {
                    System.out.println("\n--- REGISTRAR CLASE ---");
                    int idEntrenador = leerEntero(teclado, "ID del entrenador: ");
                    Entrenador e = idEntrenador > 0
                            ? entrenadorData.buscarPorId(idEntrenador) : null;
                    if (e == null) {
                        System.out.println("No se encontro el entrenador.");
                    }
                    if (!e.isEstado()) {
                        System.out.println("El entrenador esta dado de baja.");
                    }
                    System.out.print("Disciplina (ej. Spinning): ");
                    String disciplina = teclado.nextLine();
                    System.out.print("Horario (ej. 18:00 - 19:00): ");
                    String horario = teclado.nextLine();
                    int capacidad = leerEntero(teclado, "Capacidad: ");
                    if (capacidad <= 0) {
                        System.out.println("La capacidad debe ser mayor que cero.");
                    }

                    ClaseGym c = new ClaseGym(0, disciplina, e, horario,
                            capacidad, true);
                    if (claseData.guardarClase(c)) {
                        System.out.println("Clase registrada. ID asignado: " + c.getId());
                    } else {
                        System.out.println("No se pudo registrar la clase.");
                    }
                }
                case 2 ->  {
                    System.out.println("\n--- LISTADO DE CLASES ---");
                    List<ClaseGym> lista = claseData.listarClases();
                    if (lista.isEmpty()) {
                        System.out.println("No hay clases registradas.");
                    } else {
                        for (ClaseGym c : lista) {
                            mostrarClase(c);
                        }
                    }
                }
                case 3 ->  {
                    int id = leerEntero(teclado, "Ingrese el ID de la clase: ");
                    ClaseGym c = id > 0 ? claseData.buscarClase(id) : null;
                    if (c != null) {
                        mostrarClase(c);
                    } else {
                        System.out.println("No se encontro la clase.");
                    }
                }
                case 4 ->  {
                    System.out.println("\n--- MODIFICAR CLASE ---");
                    int id = leerEntero(teclado, "Ingrese el ID de la clase: ");
                    ClaseGym c = id > 0 ? claseData.buscarClase(id) : null;
                    if (c == null) {
                        System.out.println("No se encontro la clase.");
                    }
                    System.out.println("Datos actuales:");
                    mostrarClase(c);
                    System.out.print("Nuevo horario (ENTER para dejar igual): ");
                    String horario = teclado.nextLine();
                    if (!horario.trim().isEmpty()) {
                        c.setHorario(horario);
                    }
                    System.out.print("Nueva capacidad (ENTER para dejar igual): ");
                    String cap = teclado.nextLine().trim();
                    if (!cap.isEmpty()) {
                        try {
                            int nueva = Integer.parseInt(cap);
                            if (nueva <= 0) {
                                System.out.println("Capacidad invalida.");
                            }
                            c.setCapacidad(nueva);
                        } catch (NumberFormatException ex) {
                            System.out.println("La capacidad debe ser un numero entero.");
                        }
                    }
                    if (claseData.modificarClase(c)) {
                        System.out.println("Clase modificada correctamente.");
                    } else {
                        System.out.println("No se pudo modificar la clase.");
                    }
                }
                case 5 ->  {
                    int id = leerEntero(teclado, "ID de la clase a eliminar: ");
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                    }
                    System.out.print("Escriba SI para confirmar: ");
                    if (teclado.nextLine().equalsIgnoreCase("SI")) {
                        if (claseData.eliminarClase(id)) {
                            System.out.println("Clase eliminada.");
                        } else {
                            System.out.println("No se pudo eliminar. "
                                    + "Puede tener asistencias asociadas.");
                        }
                    } else {
                        System.out.println("Operacion cancelada.");
                    }
                }
                case 0 -> System.out.println("Volviendo al menu principal.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

     // PASES
     private static void menuPases(Scanner teclado, PaseData paseData,
            SocioData socioData) {
        int opcion;
        do {
            System.out.println("\n---------- PASES ----------");
            System.out.println("1. Registrar pase");
            System.out.println("2. Listar pases");
            System.out.println("3. Modificar entradas de un pase");
            System.out.println("4. Eliminar pase");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 ->  {
                    System.out.println("\n--- REGISTRAR PASE ---");
                    int idSocio = leerEntero(teclado, "ID del socio: ");
                    Socio s = idSocio > 0 ? socioData.buscarPorId(idSocio) : null;
                    if (s == null) {
                        System.out.println("No se encontro el socio.");
                    }
                    System.out.println("Socio: " + s.getApellido() + ", " + s.getNombre());

                    // No existe MembresiaData: el ID debe existir ya en la tabla de membresias.
                    int idMembresia = leerEntero(teclado,
                            "ID de membresia (debe existir en la BD): ");
                    if (idMembresia <= 0) {
                        System.out.println("ID de membresia invalido.");
                    }
                    int entradas = leerEntero(teclado, "Entradas del pase (8, 12 o 20): ");
                    if (entradas <= 0) {
                        System.out.println("La cantidad de entradas debe ser mayor que cero.");
                    }
                    LocalDate inicio = leerFecha(teclado,
                            "Fecha de inicio (AAAA-MM-DD, ENTER = hoy): ");
                    if (inicio == null) {
                        System.out.println("Fecha invalida.");
                    }
                    double costo = leerDecimal(teclado, "Costo: ");
                    if (costo < 0) {
                        System.out.println("El costo debe ser un numero mayor o igual a cero.");
                    }

                    Pase p = new Pase();
                    p.setIdSocio(idSocio);
                    p.setIdMembresia(idMembresia);
                    p.setEntradasIniciales(entradas);
                    p.setCantidadEntradas(entradas);
                    p.setFechaInicio(inicio);
                    p.setFechaFin(inicio.plusDays(30));
                    p.setCosto(costo);
                    p.setEstado(true);

                    if (paseData.guardarPase(p)) {
                        System.out.println("Pase registrado. Vence el " + p.getFechaFin());
                    } else {
                        System.out.println("No se pudo registrar el pase.");
                    }
                }
                case 2 ->  {
                    System.out.println("\n--- LISTADO DE PASES ---");
                    List<Pase> lista = paseData.listarPases();
                    if (lista.isEmpty()) {
                        System.out.println("No hay pases registrados.");
                    } else {
                        for (Pase p : lista) {
                            mostrarPase(p);
                        }
                    }
                }
                case 3 ->  {
                    System.out.println("\n--- MODIFICAR PASE ---");
                    int id = leerEntero(teclado, "ID del pase: ");
                    Pase p = buscarPase(paseData, id);
                    if (p == null) {
                        System.out.println("No se encontro el pase.");
                    }
                    mostrarPase(p);
                    int nuevas = leerEntero(teclado, "Nueva cantidad de entradas: ");
                    if (nuevas < 0) {
                        System.out.println("Cantidad invalida.");
                    }
                    p.setCantidadEntradas(nuevas);
                    if (paseData.modificarPase(p)) {
                        System.out.println("Pase modificado correctamente.");
                    } else {
                        System.out.println("No se pudo modificar el pase.");
                    }
                }
                case 4 ->  {
                    int id = leerEntero(teclado, "ID del pase a eliminar: ");
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                    }
                    System.out.print("Escriba SI para confirmar: ");
                    if (teclado.nextLine().equalsIgnoreCase("SI")) {
                        if (paseData.eliminarPase(id)) {
                            System.out.println("Pase eliminado.");
                        } else {
                            System.out.println("No se pudo eliminar. "
                                    + "Puede tener asistencias asociadas.");
                        }
                    } else {
                        System.out.println("Operacion cancelada.");
                    }
                }
                case 0 -> System.out.println("Volviendo al menu principal.");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

     // ASISTENCIAS
     private static void menuAsistencias(Scanner teclado, AsistenciaData asistenciaData,
            SocioData socioData, ClaseGymData claseData, PaseData paseData) {
        int opcion;
        do {
            System.out.println("\n---------- ASISTENCIAS ----------");
            System.out.println("1. Registrar asistencia");
            System.out.println("2. Listar asistencias");
            System.out.println("3. Buscar asistencia por ID");
            System.out.println("4. Modificar fecha de una asistencia");
            System.out.println("5. Eliminar asistencia");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1: {
                    System.out.println("\n--- REGISTRAR ASISTENCIA ---");
                    int idSocio = leerEntero(teclado, "ID del socio: ");
                    Socio s = idSocio > 0 ? socioData.buscarPorId(idSocio) : null;
                    if (s == null) {
                        System.out.println("No se encontro el socio.");
                        break;
                    }
                    int idClase = leerEntero(teclado, "ID de la clase: ");
                    ClaseGym c = idClase > 0 ? claseData.buscarClase(idClase) : null;
                    if (c == null) {
                        System.out.println("No se encontro la clase.");
                        break;
                    }
                    int idPase = leerEntero(teclado, "ID del pase: ");
                    Pase p = buscarPase(paseData, idPase);
                    if (p == null) {
                        System.out.println("No se encontro el pase.");
                        break;
                    }
                    // Misma regla que VistaAsistencia: el pase debe ser del socio.
                    if (p.getIdSocio() != s.getIdSocio()) {
                        System.out.println("El pase seleccionado no pertenece al socio.");
                        break;
                    }
                    LocalDate fecha = leerFecha(teclado,
                            "Fecha (AAAA-MM-DD, ENTER = hoy): ");
                    if (fecha == null) {
                        System.out.println("Fecha invalida.");
                        break;
                    }

                    Asistencia a = new Asistencia(0, s, c, p, fecha);
                    if (asistenciaData.guardarAsistencia(a)) {
                        System.out.println("Asistencia registrada. ID asignado: " + a.getId());
                    } else {
                        System.out.println("No se pudo registrar la asistencia.");
                    }
                    break;
                }
                case 2: {
                    System.out.println("\n--- LISTADO DE ASISTENCIAS ---");
                    List<Asistencia> lista = asistenciaData.listarAsistencias();
                    if (lista.isEmpty()) {
                        System.out.println("No hay asistencias registradas.");
                    } else {
                        for (Asistencia a : lista) {
                            System.out.println(a);
                        }
                    }
                    break;
                }
                case 3: {
                    int id = leerEntero(teclado, "ID de la asistencia: ");
                    Asistencia a = id > 0 ? asistenciaData.buscarAsistencia(id) : null;
                    System.out.println(a != null ? a : "No se encontro la asistencia.");
                    break;
                }
                case 4: {
                    int id = leerEntero(teclado, "ID de la asistencia: ");
                    Asistencia a = id > 0 ? asistenciaData.buscarAsistencia(id) : null;
                    if (a == null) {
                        System.out.println("No se encontro la asistencia.");
                        break;
                    }
                    System.out.println("Actual: " + a);
                    LocalDate fecha = leerFecha(teclado,
                            "Nueva fecha (AAAA-MM-DD, ENTER = hoy): ");
                    if (fecha == null) {
                        System.out.println("Fecha invalida.");
                        break;
                    }
                    a.setFechaAsistencia(fecha);
                    if (asistenciaData.modificarAsistencia(a)) {
                        System.out.println("Asistencia modificada correctamente.");
                    } else {
                        System.out.println("No se pudo modificar la asistencia.");
                    }
                    break;
                }
                case 5: {
                    int id = leerEntero(teclado, "ID de la asistencia a eliminar: ");
                    if (id > 0 && asistenciaData.eliminarAsistencia(id)) {
                        System.out.println("Asistencia eliminada.");
                    } else {
                        System.out.println("No se pudo eliminar la asistencia.");
                    }
                    break;
                }
                case 0:
                    System.out.println("Volviendo al menu principal.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // METODOS AUXILIARES
     private static int leerEntero(Scanner teclado, String mensaje) {
        try {
            System.out.print(mensaje);
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double leerDecimal(Scanner teclado, String mensaje) {
        try {
            System.out.print(mensaje);
            return Double.parseDouble(teclado.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ENTER vacio = hoy. Devuelve null si el formato es invalido.
    private static LocalDate leerFecha(Scanner teclado, String mensaje) {
        System.out.print(mensaje);
        String texto = teclado.nextLine().trim();
        if (texto.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(texto);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    // PaseData no tiene buscarPase: se busca dentro de listarPases().
    private static Pase buscarPase(PaseData paseData, int id) {
        if (id <= 0) {
            return null;
        }
        for (Pase p : paseData.listarPases()) {
            if (p.getIdPase() == id) {
                return p;
            }
        }
        return null;
    }

    private static void mostrarEntrenador(Entrenador e) {
        System.out.println(e.getIdEntrenador() + " | " + e.getDni() + " | "
                + e.getApellido() + ", " + e.getNombre()
                + " | " + e.getEspecialidad()
                + " | " + (e.isEstado() ? "Activo" : "Inactivo"));
    }

    private static void mostrarClase(ClaseGym c) {
        String entrenador = c.getEntrenador() != null
                ? c.getEntrenador().getApellido() + ", " + c.getEntrenador().getNombre()
                : "Sin entrenador";
        System.out.println(c.getId() + " | " + c.getDisciplina()
                + " | " + c.getHorario()
                + " | Capacidad: " + c.getCapacidad()
                + " | " + entrenador
                + " | " + (c.isEstado() ? "Activa" : "Inactiva"));
    }

    private static void mostrarPase(Pase p) {
        System.out.println("Pase " + p.getIdPase()
                + " | Socio: " + p.getIdSocio()
                + " | Membresia: " + p.getIdMembresia()
                + " | Entradas: " + p.getCantidadEntradas() + "/" + p.getEntradasIniciales()
                + " | " + p.getFechaInicio() + " a " + p.getFechaFin()
                + " | $" + p.getCosto()
                + " | " + (p.isEstado() ? "Activo" : "Inactivo"));
    }

    private static int leerId(Scanner teclado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
     