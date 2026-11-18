
import Modelo.Asistencia;
import Modelo.ClaseGym;
import Modelo.Conexion;
import Modelo.Entrenador;
import Modelo.Membresia;
import Modelo.Pase;
import Modelo.Socio;
import Persistencia.AsistenciaData;
import Persistencia.ClaseGymData;
import Persistencia.EntrenadorData;
import Persistencia.MembresiaData;
import Persistencia.PaseData;
import Persistencia.SocioData;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TestDeConsola {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("=== SISTEMA ABORIGYM ===");

        System.out.println("\n1. Probando conexion a MariaDB...");
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
        EntrenadorData entrenadorData = new EntrenadorData();
        MembresiaData membresiaData = new MembresiaData();
        ClaseGymData claseData = new ClaseGymData();
        PaseData paseData = new PaseData();
        AsistenciaData asistenciaData = new AsistenciaData();

        int opcion;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Gestion de Socios");
            System.out.println("2. Gestion de Entrenadores");
            System.out.println("3. Gestion de Membresias");
            System.out.println("4. Gestion de Clases");
            System.out.println("5. Gestion de Pases");
            System.out.println("6. Gestion de Asistencias");
            System.out.println("0. Salir");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 ->
                    menuSocios(teclado, socioData, paseData);
                case 2 ->
                    menuEntrenadores(teclado, entrenadorData);
                case 3 ->
                    menuMembresias(teclado, membresiaData);
                case 4 ->
                    menuClases(teclado, claseData, entrenadorData);
                case 5 ->
                    menuPases(teclado, paseData, socioData, membresiaData);
                case 6 ->
                    menuAsistencias(teclado, asistenciaData, socioData, claseData, paseData);
                case 0 ->
                    System.out.println("Prueba de consola finalizada.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);

        teclado.close();
    }

    // 1. MENU SOCIOS
    private static void menuSocios(Scanner teclado, SocioData socioData, PaseData paseData) {
        int opcion;
        do {
            System.out.println("\n--- SOCIOS ---");
            System.out.println("1. Registrar socio");
            System.out.println("2. Listar todos los socios");
            System.out.println("3. Buscar socio por DNI");
            System.out.println("4. Buscar socio por ID");
            System.out.println("5. Actualizar socio");
            System.out.println("6. Baja logica");
            System.out.println("7. Alta logica");
            System.out.println("8. Borrar socio definitivamente");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> {
                    System.out.print("DNI: ");
                    String dni = teclado.nextLine().trim();

                    Socio existente = socioData.buscarPorDni(dni);
                    if (existente != null) {
                        System.out.println("Ya existe un socio con ese DNI: " + existente);
                        break;
                    }

                    System.out.print("Nombre: ");
                    String nombre = teclado.nextLine().trim();
                    System.out.print("Apellido: ");
                    String apellido = teclado.nextLine().trim();
                    int edad = leerEntero(teclado, "Edad: ");
                    if (edad <= 0) {
                        System.out.println("La edad debe ser mayor que cero.");
                        break;
                    }
                    System.out.print("Correo electronico: ");
                    String correo = teclado.nextLine().trim();
                    System.out.print("Telefono: ");
                    String telefono = teclado.nextLine().trim();

                    Socio socio = new Socio(dni, nombre, apellido, edad, correo, telefono, true);
                    if (socioData.guardarSocio(socio)) {
                        System.out.println("Socio registrado correctamente.");
                    } else {
                        System.out.println("No se pudo registrar el socio.");
                    }
                }
                case 2 -> {
                    List<Socio> socios = socioData.listarSocio();
                    List<Pase> pases = paseData.listarPases();
                    if (socios.isEmpty()) {
                        System.out.println("No hay socios registrados.");
                    } else {
                        for (Socio s : socios) {
                            int entradas = 0;
                            for (Pase p : pases) {
                                if (p.getIdSocio() == s.getIdSocio() && p.isEstado()) {
                                    entradas += p.getCantidadEntradas();
                                }
                            }
                            System.out.println(s + " | Entradas disp.: " + entradas + " | Estado: " + (s.isEstado() ? "Activo" : "Inactivo"));
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Ingrese DNI: ");
                    String dni = teclado.nextLine().trim();
                    Socio s = socioData.buscarPorDni(dni);
                    System.out.println(s != null ? s : "No se encontro el socio.");
                }
                case 4 -> {
                    int id = leerEntero(teclado, "Ingrese ID del socio: ");
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                    Socio s = socioData.buscarPorId(id);
                    System.out.println(s != null ? s : "No se encontro el socio.");
                }
                case 5 -> {
                    int id = leerEntero(teclado, "Ingrese ID del socio a actualizar: ");
                    if (id <= 0) {
                        System.out.println("ID invalido.");
                        break;
                    }
                    Socio s = socioData.buscarPorId(id);
                    if (s == null) {
                        System.out.println("No se encontro el socio.");
                        break;
                    }
                    System.out.println("Datos actuales: " + s);
                    System.out.print("Nuevo DNI: ");
                    String nuevoDni = teclado.nextLine().trim();
                    Socio otro = socioData.buscarPorDni(nuevoDni);
                    if (otro != null && otro.getIdSocio() != id) {
                        System.out.println("Ese DNI ya pertenece a otro socio.");
                        break;
                    }
                    s.setDni(nuevoDni);
                    System.out.print("Nuevo Nombre: ");
                    s.setNombre(teclado.nextLine().trim());
                    System.out.print("Nuevo Apellido: ");
                    s.setApellido(teclado.nextLine().trim());
                    int edad = leerEntero(teclado, "Nueva Edad: ");
                    if (edad > 0) {
                        s.setEdad(edad);
                    }
                    System.out.print("Nuevo Correo: ");
                    s.setCorreo(teclado.nextLine().trim());
                    System.out.print("Nuevo Telefono: ");
                    s.setTelefono(teclado.nextLine().trim());

                    if (socioData.modificar(s)) {
                        System.out.println("Socio actualizado correctamente.");
                    } else {
                        System.out.println("No se pudo actualizar el socio.");
                    }
                }
                case 6 -> {
                    int id = leerEntero(teclado, "ID del socio para baja logica: ");
                    if (id > 0 && socioData.darDeBaja(id)) {
                        System.out.println("Socio dado de baja correctamente.");
                    } else {
                        System.out.println("No se pudo dar de baja al socio.");
                    }
                }
                case 7 -> {
                    int id = leerEntero(teclado, "ID del socio para alta logica: ");
                    if (id > 0 && socioData.darDeAlta(id)) {
                        System.out.println("Socio reactivado correctamente.");
                    } else {
                        System.out.println("No se pudo dar de alta al socio.");
                    }
                }
                case 8 -> {
                    int id = leerEntero(teclado, "ID del socio a borrar definitivamente: ");
                    Socio s = id > 0 ? socioData.buscarPorId(id) : null;
                    if (s == null) {
                        System.out.println("No se encontro el socio.");
                        break;
                    }
                    System.out.println("Socio seleccionado: " + s);
                    System.out.print("Esta operacion es definitiva. Escriba SI para confirmar: ");
                    if (teclado.nextLine().equalsIgnoreCase("SI")) {
                        if (socioData.eliminar(id)) {
                            System.out.println("Socio eliminado definitivamente.");
                        } else {
                            System.out.println("No se pudo borrar (tiene pases o asistencias asociadas).");
                        }
                    } else {
                        System.out.println("Operacion cancelada.");
                    }
                }
                case 0 ->
                    System.out.println("Volviendo al menu principal.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // 2. MENU ENTRENADORES
    private static void menuEntrenadores(Scanner teclado, EntrenadorData data) {
        int opcion;
        do {
            System.out.println("\n--- ENTRENADORES ---");
            System.out.println("1. Registrar entrenador");
            System.out.println("2. Listar entrenadores");
            System.out.println("3. Buscar entrenador por ID");
            System.out.println("4. Actualizar entrenador");
            System.out.println("5. Baja logica");
            System.out.println("6. Alta logica");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> {
                    System.out.print("DNI: ");
                    String dni = teclado.nextLine().trim();
                    if (data.existeDni(dni)) {
                        System.out.println("Ya existe un entrenador con ese DNI.");
                        break;
                    }
                    System.out.print("Nombre: ");
                    String nombre = teclado.nextLine().trim();
                    System.out.print("Apellido: ");
                    String apellido = teclado.nextLine().trim();
                    System.out.print("Especialidad (ej. Spinning): ");
                    String especialidad = teclado.nextLine().trim();

                    Entrenador e = new Entrenador(0, dni, nombre, apellido, especialidad, true);
                    if (data.insertar(e)) {
                        System.out.println("Entrenador registrado correctamente.");
                    } else {
                        System.out.println("No se pudo registrar el entrenador.");
                    }
                }
                case 2 -> {
                    List<Entrenador> lista = data.listarEntrenadores();
                    if (lista.isEmpty()) {
                        System.out.println("No hay entrenadores registrados.");
                    } else {
                        for (Entrenador e : lista) {
                            mostrarEntrenador(e);
                        }
                    }
                }
                case 3 -> {
                    int id = leerEntero(teclado, "Ingrese el ID del entrenador: ");
                    Entrenador e = id > 0 ? data.buscarPorId(id) : null;
                    if (e != null) {
                        mostrarEntrenador(e);
                    } else {
                        System.out.println("No se encontro el entrenador.");
                    }
                }
                case 4 -> {
                    int id = leerEntero(teclado, "Ingrese el ID del entrenador: ");
                    Entrenador e = id > 0 ? data.buscarPorId(id) : null;
                    if (e == null) {
                        System.out.println("No se encontro el entrenador.");
                        break;
                    }
                    mostrarEntrenador(e);
                    System.out.print("Nuevo DNI: ");
                    String nuevoDni = teclado.nextLine().trim();
                    if (data.existeDniEnOtroEntrenador(nuevoDni, id)) {
                        System.out.println("Ese DNI ya pertenece a otro entrenador.");
                        break;
                    }
                    e.setDni(nuevoDni);
                    System.out.print("Nuevo Nombre: ");
                    e.setNombre(teclado.nextLine().trim());
                    System.out.print("Nuevo Apellido: ");
                    e.setApellido(teclado.nextLine().trim());
                    System.out.print("Nueva Especialidad: ");
                    e.setEspecialidad(teclado.nextLine().trim());

                    if (data.modificar(e)) {
                        System.out.println("Entrenador actualizado correctamente.");
                    } else {
                        System.out.println("No se pudo actualizar el entrenador.");
                    }
                }
                case 5 -> {
                    int id = leerEntero(teclado, "ID del entrenador a dar de baja: ");
                    if (id > 0 && data.bajaLogica(id)) {
                        System.out.println("Entrenador dado de baja.");
                    } else {
                        System.out.println("No se pudo dar de baja al entrenador.");
                    }
                }
                case 6 -> {
                    int id = leerEntero(teclado, "ID del entrenador a dar de alta: ");
                    if (id > 0 && data.altaLogica(id)) {
                        System.out.println("Entrenador reactivado.");
                    } else {
                        System.out.println("No se pudo dar de alta al entrenador.");
                    }
                }
                case 0 ->
                    System.out.println("Volviendo al menu principal.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // 3. MENU MEMBRESIAS
    private static void menuMembresias(Scanner teclado, MembresiaData data) {
        int opcion;
        do {
            System.out.println("\n--- MEMBRESIAS ---");
            System.out.println("1. Registrar membresia");
            System.out.println("2. Listar membresias");
            System.out.println("3. Modificar membresia");
            System.out.println("4. Eliminar membresia");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nombre de la membresia (ej. Plan Mensual 12): ");
                    String nombre = teclado.nextLine().trim();
                    int clases = leerEntero(teclado, "Cantidad de clases: ");
                    int dias = leerEntero(teclado, "Duracion en dias (ej. 30): ");
                    double precio = leerDecimal(teclado, "Precio: ");

                    if (nombre.isEmpty() || clases <= 0 || dias <= 0 || precio < 0) {
                        System.out.println("Datos invalidos.");
                        break;
                    }

                    Membresia m = new Membresia(0, nombre, clases, dias, precio);
                    if (data.guardarMembresia(m)) {
                        System.out.println("Membresia registrada correctamente.");
                    } else {
                        System.out.println("No se pudo registrar la membresia.");
                    }
                }
                case 2 -> {
                    List<Membresia> lista = data.listarMembresias();
                    if (lista.isEmpty()) {
                        System.out.println("No hay membresias registradas.");
                    } else {
                        for (Membresia m : lista) {
                            System.out.println(m.getId() + " | " + m.getNombre()
                                    + " | Clases: " + m.getCantidadClases()
                                    + " | Duracion: " + m.getDuracionDias() + " dias"
                                    + " | $" + m.getPrecio());
                        }
                    }
                }
                case 3 -> {
                    int id = leerEntero(teclado, "ID de la membresia a modificar: ");
                    Membresia m = id > 0 ? data.buscarMembresia(id) : null;
                    if (m == null) {
                        System.out.println("No se encontro la membresia.");
                        break;
                    }
                    System.out.print("Nuevo nombre (" + m.getNombre() + "): ");
                    String nombre = teclado.nextLine().trim();
                    if (!nombre.isEmpty()) {
                        m.setNombre(nombre);
                    }

                    int clases = leerEntero(teclado, "Nueva cantidad de clases: ");
                    if (clases > 0) {
                        m.setCantidadClases(clases);
                    }

                    int dias = leerEntero(teclado, "Nueva duracion en dias: ");
                    if (dias > 0) {
                        m.setDuracionDias(dias);
                    }

                    double precio = leerDecimal(teclado, "Nuevo precio: ");
                    if (precio >= 0) {
                        m.setPrecio(precio);
                    }

                    if (data.modificarMembresia(m)) {
                        System.out.println("Membresia modificada correctamente.");
                    } else {
                        System.out.println("No se pudo modificar la membresia.");
                    }
                }
                case 4 -> {
                    int id = leerEntero(teclado, "ID de la membresia a eliminar: ");
                    if (id > 0 && data.eliminarMembresia(id)) {
                        System.out.println("Membresia eliminada.");
                    } else {
                        System.out.println("No se pudo eliminar (puede estar asociada a un pase).");
                    }
                }
                case 0 ->
                    System.out.println("Volviendo al menu principal.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // 4. MENU CLASES
    private static void menuClases(Scanner teclado, ClaseGymData claseData, EntrenadorData entrenadorData) {
        int opcion;
        do {
            System.out.println("\n--- CLASES ---");
            System.out.println("1. Registrar clase");
            System.out.println("2. Listar clases");
            System.out.println("3. Buscar clase por ID");
            System.out.println("4. Modificar clase");
            System.out.println("5. Baja logica de clase");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> {
                    int idEntrenador = leerEntero(teclado, "ID del entrenador: ");
                    Entrenador e = idEntrenador > 0 ? entrenadorData.buscarPorId(idEntrenador) : null;
                    if (e == null || !e.isEstado()) {
                        System.out.println("Entrenador no encontrado o inactivo.");
                        break;
                    }
                    System.out.print("Disciplina (ej. Spinning): ");
                    String disciplina = teclado.nextLine().trim();
                    System.out.print("Horario (ej. 18:00 - 19:00): ");
                    String horario = teclado.nextLine().trim();

                    if (claseData.entrenadorOcupadoEnHorario(idEntrenador, horario, 0)) {
                        System.out.println("El entrenador ya tiene una clase en ese horario.");
                        break;
                    }

                    int capacidad = leerEntero(teclado, "Capacidad: ");
                    if (capacidad <= 0) {
                        System.out.println("La capacidad debe ser mayor que cero.");
                        break;
                    }

                    ClaseGym c = new ClaseGym(0, disciplina, e, horario, capacidad, true);
                    if (claseData.guardarClase(c)) {
                        System.out.println("Clase registrada. ID asignado: " + c.getId());
                    } else {
                        System.out.println("No se pudo registrar la clase.");
                    }
                }
                case 2 -> {
                    List<ClaseGym> lista = claseData.listarClases();
                    if (lista.isEmpty()) {
                        System.out.println("No hay clases registradas.");
                    } else {
                        for (ClaseGym c : lista) {
                            mostrarClase(c);
                        }
                    }
                }
                case 3 -> {
                    int id = leerEntero(teclado, "Ingrese el ID de la clase: ");
                    ClaseGym c = id > 0 ? claseData.buscarClase(id) : null;
                    if (c != null) {
                        mostrarClase(c);
                    } else {
                        System.out.println("No se encontro la clase.");
                    }
                }
                case 4 -> {
                    int id = leerEntero(teclado, "Ingrese el ID de la clase: ");
                    ClaseGym c = id > 0 ? claseData.buscarClase(id) : null;
                    if (c == null) {
                        System.out.println("No se encontro la clase.");
                        break;
                    }
                    mostrarClase(c);
                    System.out.print("Nuevo horario (ENTER para dejar igual): ");
                    String horario = teclado.nextLine().trim();
                    if (!horario.isEmpty()) {
                        c.setHorario(horario);
                    }
                    System.out.print("Nueva capacidad (ENTER para dejar igual): ");
                    String cap = teclado.nextLine().trim();
                    if (!cap.isEmpty()) {
                        try {
                            int nueva = Integer.parseInt(cap);
                            if (nueva > 0) {
                                c.setCapacidad(nueva);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Capacidad invalida.");
                        }
                    }
                    if (claseData.modificarClase(c)) {
                        System.out.println("Clase modificada correctamente.");
                    } else {
                        System.out.println("No se pudo modificar la clase.");
                    }
                }
                case 5 -> {
                    int id = leerEntero(teclado, "ID de la clase a dar de baja: ");
                    if (id > 0 && claseData.bajaLogica(id)) {
                        System.out.println("Clase dada de baja.");
                    } else {
                        System.out.println("No se pudo dar de baja la clase.");
                    }
                }
                case 0 ->
                    System.out.println("Volviendo al menu principal.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // 5. MENU PASES
    private static void menuPases(Scanner teclado, PaseData paseData, SocioData socioData, MembresiaData membresiaData) {
        int opcion;
        do {
            System.out.println("\n--- PASES ---");
            System.out.println("1. Registrar pase");
            System.out.println("2. Listar pases");
            System.out.println("3. Modificar entradas de un pase");
            System.out.println("4. Baja logica de pase");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> {
                    int idSocio = leerEntero(teclado, "ID del socio: ");
                    Socio s = idSocio > 0 ? socioData.buscarPorId(idSocio) : null;
                    if (s == null || !s.isEstado()) {
                        System.out.println("Socio no encontrado o inactivo.");
                        break;
                    }
                    if (paseData.tienePaseActivo(idSocio)) {
                        System.out.println("El socio ya tiene un pase activo.");
                        break;
                    }

                    int idMembresia = leerEntero(teclado, "ID de membresia: ");
                    Membresia m = idMembresia > 0 ? membresiaData.buscarMembresia(idMembresia) : null;
                    if (m == null) {
                        System.out.println("Membresia no encontrada.");
                        break;
                    }

                    LocalDate inicio = leerFecha(teclado, "Fecha de inicio (AAAA-MM-DD, ENTER = hoy): ");
                    if (inicio == null) {
                        System.out.println("Fecha invalida.");
                        break;
                    }

                    Pase p = new Pase();
                    p.setIdSocio(idSocio);
                    p.setIdMembresia(idMembresia);
                    p.setEntradasIniciales(m.getCantidadClases());
                    p.setCantidadEntradas(m.getCantidadClases());
                    p.setFechaInicio(inicio);
                    p.setFechaFin(inicio.plusDays(m.getDuracionDias()));
                    p.setCosto(m.getPrecio());
                    p.setEstado(true);

                    if (paseData.guardarPase(p)) {
                        System.out.println("Pase registrado (" + m.getCantidadClases() + " entradas). Vence el " + p.getFechaFin());
                    } else {
                        System.out.println("No se pudo registrar el pase.");
                    }
                }
                case 2 -> {
                    List<Pase> lista = paseData.listarPases();
                    if (lista.isEmpty()) {
                        System.out.println("No hay pases registrados.");
                    } else {
                        for (Pase p : lista) {
                            mostrarPase(p);
                        }
                    }
                }
                case 3 -> {
                    int id = leerEntero(teclado, "ID del pase: ");
                    Pase p = buscarPase(paseData, id);
                    if (p == null) {
                        System.out.println("No se encontro el pase.");
                        break;
                    }
                    mostrarPase(p);
                    int nuevas = leerEntero(teclado, "Nueva cantidad de entradas: ");
                    if (nuevas < 0) {
                        System.out.println("Cantidad invalida.");
                        break;
                    }
                    p.setCantidadEntradas(nuevas);
                    if (paseData.modificarPase(p)) {
                        System.out.println("Pase modificado correctamente.");
                    } else {
                        System.out.println("No se pudo modificar el pase.");
                    }
                }
                case 4 -> {
                    int id = leerEntero(teclado, "ID del pase a dar de baja: ");
                    if (id > 0 && paseData.bajaLogica(id)) {
                        System.out.println("Pase dado de baja.");
                    } else {
                        System.out.println("No se pudo dar de baja el pase.");
                    }
                }
                case 0 ->
                    System.out.println("Volviendo al menu principal.");
                default ->
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // 6. MENU ASISTENCIAS
    private static void menuAsistencias(Scanner teclado, AsistenciaData asistenciaData,
            SocioData socioData, ClaseGymData claseData, PaseData paseData) {
        int opcion;
        do {
            System.out.println("\n--- ASISTENCIAS ---");
            System.out.println("1. Registrar asistencia");
            System.out.println("2. Listar asistencias");
            System.out.println("3. Buscar asistencia por ID");
            System.out.println("4. Eliminar asistencia (reintegra entrada)");
            System.out.println("0. Volver");
            opcion = leerEntero(teclado, "Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> {
                    int idSocio = leerEntero(teclado, "ID del socio: ");
                    Socio s = idSocio > 0 ? socioData.buscarPorId(idSocio) : null;
                    if (s == null || !s.isEstado()) {
                        System.out.println("Socio no encontrado o inactivo.");
                        break;
                    }
                    int idClase = leerEntero(teclado, "ID de la clase: ");
                    ClaseGym c = idClase > 0 ? claseData.buscarClase(idClase) : null;
                    if (c == null || !c.isEstado()) {
                        System.out.println("Clase no encontrada o inactiva.");
                        break;
                    }
                    int idPase = leerEntero(teclado, "ID del pase: ");
                    Pase p = buscarPase(paseData, idPase);
                    if (p == null || p.getIdSocio() != s.getIdSocio()) {
                        System.out.println("Pase no encontrado o no pertenece al socio.");
                        break;
                    }
                    LocalDate fecha = leerFecha(teclado, "Fecha (AAAA-MM-DD, ENTER = hoy): ");
                    if (fecha == null) {
                        System.out.println("Fecha invalida.");
                        break;
                    }
                    if (!asistenciaData.hayCupoDisponible(idClase, fecha)) {
                        System.out.println("No hay cupo disponible en esa clase para esa fecha.");
                        break;
                    }
                    if (asistenciaData.existeAsistencia(idSocio, idClase, fecha)) {
                        System.out.println("El socio ya tiene asistencia registrada en esa clase y fecha.");
                        break;
                    }

                    Asistencia a = new Asistencia(0, s, c, p, fecha);
                    if (asistenciaData.guardarAsistencia(a)) {
                        System.out.println("Asistencia registrada correctamente (se desconto 1 entrada del pase).");
                    } else {
                        System.out.println("No se pudo registrar la asistencia.");
                    }
                }
                case 2 -> {
                    List<Asistencia> lista = asistenciaData.listarAsistencia();
                    if (lista.isEmpty()) {
                        System.out.println("No hay asistencias registradas.");
                    } else {
                        for (Asistencia a : lista) {
                            System.out.println("ID: " + a.getId()
                                    + " | Socio: " + a.getSocio().getApellido() + ", " + a.getSocio().getNombre()
                                    + " | Clase: " + a.getClase().getDisciplina() + " (" + a.getClase().getHorario() + ")"
                                    + " | Pase #" + a.getPase().getIdPase()
                                    + " | Fecha: " + a.getFechaAsistencia());
                        }
                    }
                }
                case 3 -> {
                    int id = leerEntero(teclado, "ID de la asistencia: ");
                    Asistencia a = id > 0 ? asistenciaData.buscarAsistencia(id) : null;
                    System.out.println(a != null ? a : "No se encontro la asistencia.");
                }
                case 4 -> {
                    int id = leerEntero(teclado, "ID de la asistencia a eliminar: ");
                    if (id > 0 && asistenciaData.eliminarAsistencia(id)) {
                        System.out.println("Asistencia eliminada y entrada reintegrada al pase.");
                    } else {
                        System.out.println("No se pudo eliminar la asistencia.");
                    }
                }
                case 0 ->
                    System.out.println("Volviendo al menu principal.");
                default ->
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
}
