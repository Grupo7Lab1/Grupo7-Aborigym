/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Modelo.Socio;
import Modelo.ClaseGym;
import Persistencia.AsistenciaData;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import Persistencia.SocioData;
import java.util.List;
import java.util.ArrayList;
import Persistencia.ClaseGymData;
import Modelo.Pase;
import Persistencia.PaseData;
import javax.swing.table.DefaultTableModel;
import Modelo.Asistencia;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Nestor Marchizone
 */
public class VistaAsistencia extends javax.swing.JInternalFrame {

    private List<Socio> listaSocios = new ArrayList<>();
    private List<ClaseGym> listaClases = new ArrayList<>();
    private List<Pase> listaPases = new ArrayList<>();
    private List<Asistencia> listaAsistencias = new ArrayList<>();

    public VistaAsistencia() {
        initComponents();
        Iconos.aplicar(this);
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        configurarTabla();
        cargarSocios();
        cargarClases();
        initEventos();
        cargarTabla();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Socio", "Clase", "Pase N°", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTAsistencia.setModel(modelo);
        jTAsistencia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTIdSocio.setEditable(false);
        jTEntradasDisponibles.setEditable(false);
    }

    private void initEventos() {
        jTAsistencia.getSelectionModel().addListSelectionListener(e -> {
            int fila = jTAsistencia.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0 && fila < listaAsistencias.size()) {
                cargarAsistenciaEnFormulario(listaAsistencias.get(fila));
            }
        });
    }

    private void cargarAsistenciaEnFormulario(Asistencia a) {
        if (a.getSocio() != null) {
            for (int i = 0; i < listaSocios.size(); i++) {
                if (listaSocios.get(i).getIdSocio() == a.getSocio().getIdSocio()) {
                    jCBSocios.setSelectedIndex(i);
                    break;
                }
            }
        }

        actualizarDatosSocioSeleccionado();
        if (a.getPase() != null && a.getPase().getIdPase() > 0) {
            int idPase = a.getPase().getIdPase();
            int indicePase = -1;
            for (int i = 0; i < listaPases.size(); i++) {
                if (listaPases.get(i).getIdPase() == idPase) {
                    indicePase = i;
                    break;
                }
            }
            if (indicePase < 0) {
                Pase paseOriginal = new PaseData().buscarPasePorId(idPase);
                if (paseOriginal != null) {
                    indicePase = listaPases.size();
                    listaPases.add(paseOriginal);
                    jCBPases.addItem("Pase N° " + idPase + " (asignado a esta asistencia)");
                }
            }
            jCBPases.setSelectedIndex(indicePase);
        } else {
            jCBPases.setSelectedIndex(-1);
        }

        jCBClase.setSelectedIndex(-1);
        if (a.getClase() != null) {
            for (int i = 0; i < listaClases.size(); i++) {
                if (listaClases.get(i).getId() == a.getClase().getId()) {
                    jCBClase.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (a.getFechaAsistencia() != null) {
            jDCFechaDeAsistencia.setDate(Date.from(a.getFechaAsistencia().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
    }

    private void cargarSocios() {

        jCBSocios.removeAllItems();
        SocioData socioData = new SocioData();
        listaSocios = socioData.listarSocio();

        for (Socio socio : listaSocios) {
            jCBSocios.addItem(socio.getApellido() + ", " + socio.getNombre() + " (DNI: " + socio.getDni() + ")");
        }

        if (!listaSocios.isEmpty()) {
            jCBSocios.setSelectedIndex(0);
            actualizarDatosSocioSeleccionado();
        }
    }

    private void cargarClases() {

        jCBClase.removeAllItems();
        ClaseGymData claseData = new ClaseGymData();
        listaClases = claseData.listarClasesActivas();

        for (ClaseGym clase : listaClases) {
            jCBClase.addItem(clase.toString());
        }
    }

    private void cargarPasesDelSocio(int idSocio) {

        jCBPases.removeAllItems();
        listaPases.clear();
        jTEntradasDisponibles.setText("");

        PaseData paseData = new PaseData();
        listaPases = paseData.listarPasesDisponiblesPorSocio(idSocio);

        for (Pase pase : listaPases) {
            jCBPases.addItem("Pase N° " + pase.getIdPase()
                    + " (Vence: " + pase.getFechaFin() + ")");
        }

        if (!listaPases.isEmpty()) {
            jCBPases.setSelectedIndex(0);
            jTEntradasDisponibles.setText(String.valueOf(listaPases.get(0).getCantidadEntradas()));
        } else {
            jTEntradasDisponibles.setText("0 (Sin pase activo)");
        }
    }

    private void actualizarDatosSocioSeleccionado() {
        int indice = jCBSocios.getSelectedIndex();
        if (indice >= 0 && indice < listaSocios.size()) {
            Socio socio = listaSocios.get(indice);
            jTIdSocio.setText(String.valueOf(socio.getIdSocio()));
            cargarPasesDelSocio(socio.getIdSocio());
        } else {
            jTIdSocio.setText("");
            jCBPases.removeAllItems();
            listaPases.clear();
            jTEntradasDisponibles.setText("");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCapas = new javax.swing.JLayeredPane();
        panelControles = new javax.swing.JPanel();
        lblFondo = new javax.swing.JLabel();
        jLRegistrodeAsistencia = new javax.swing.JLabel();
        jLSocios = new javax.swing.JLabel();
        jLClase = new javax.swing.JLabel();
        jCBClase = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jDCFechaDeAsistencia = new com.toedter.calendar.JDateChooser();
        jCBSocios = new javax.swing.JComboBox<>();
        jBRegistrar = new javax.swing.JButton();
        jBModificar = new javax.swing.JButton();
        jBBorrar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jSPAsistencia = new javax.swing.JScrollPane();
        jTAsistencia = new javax.swing.JTable();
        jLPase = new javax.swing.JLabel();
        jCBPases = new javax.swing.JComboBox<>();
        jLId = new javax.swing.JLabel();
        jTIdSocio = new javax.swing.JTextField();
        JLEntradasDisponibles = new javax.swing.JLabel();
        jTEntradasDisponibles = new javax.swing.JTextField();

        setTitle("Gestión de Asistencia");

        panelControles.setOpaque(false);

        jLRegistrodeAsistencia.setBackground(new java.awt.Color(51, 51, 51));
        jLRegistrodeAsistencia.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLRegistrodeAsistencia.setForeground(new java.awt.Color(245, 249, 250));
        jLRegistrodeAsistencia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLRegistrodeAsistencia.setText("REGISTRO DE ASISTENCIA");
        jLRegistrodeAsistencia.setOpaque(true);

        jLSocios.setBackground(new java.awt.Color(51, 51, 51));
        jLSocios.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLSocios.setForeground(new java.awt.Color(245, 249, 250));
        jLSocios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLSocios.setText("SOCIO");
        jLSocios.setOpaque(true);

        jLClase.setBackground(new java.awt.Color(51, 51, 51));
        jLClase.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLClase.setForeground(new java.awt.Color(245, 249, 250));
        jLClase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLClase.setText("CLASE");
        jLClase.setOpaque(true);

        jCBClase.addActionListener(this::jCBClaseActionPerformed);

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(245, 249, 250));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("FECHA DE ASISTENCIA");
        jLabel1.setOpaque(true);

        jCBSocios.addActionListener(this::jCBSociosActionPerformed);

        jBRegistrar.setBackground(new java.awt.Color(42, 105, 95));
        jBRegistrar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBRegistrar.setForeground(new java.awt.Color(245, 249, 250));
        jBRegistrar.setText("REGISTRAR");
        jBRegistrar.setContentAreaFilled(false);
        jBRegistrar.setOpaque(true);
        jBRegistrar.addActionListener(this::jBRegistrarActionPerformed);

        jBModificar.setBackground(new java.awt.Color(255, 189, 87));
        jBModificar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBModificar.setForeground(new java.awt.Color(245, 249, 250));
        jBModificar.setText("MODIFICAR");
        jBModificar.setContentAreaFilled(false);
        jBModificar.setOpaque(true);
        jBModificar.addActionListener(this::jBModificarActionPerformed);

        jBBorrar.setBackground(new java.awt.Color(133, 49, 65));
        jBBorrar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBBorrar.setForeground(new java.awt.Color(245, 249, 250));
        jBBorrar.setText("BORRAR");
        jBBorrar.setContentAreaFilled(false);
        jBBorrar.setOpaque(true);
        jBBorrar.addActionListener(this::jBBorrarActionPerformed);

        jBLimpiar.setBackground(new java.awt.Color(55, 169, 183));
        jBLimpiar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBLimpiar.setForeground(new java.awt.Color(245, 249, 250));
        jBLimpiar.setText("LIMPIAR");
        jBLimpiar.setContentAreaFilled(false);
        jBLimpiar.setOpaque(true);
        jBLimpiar.addActionListener(this::jBLimpiarActionPerformed);

        jTAsistencia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID_Asistencia", "ID_Socio", "ID_Clase", "Fecha"
            }
        ));
        jSPAsistencia.setViewportView(jTAsistencia);

        jLPase.setBackground(new java.awt.Color(51, 51, 51));
        jLPase.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLPase.setForeground(new java.awt.Color(245, 249, 250));
        jLPase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLPase.setText("PASE");
        jLPase.setOpaque(true);

        jCBPases.addActionListener(this::jCBPasesActionPerformed);

        jLId.setBackground(new java.awt.Color(51, 51, 51));
        jLId.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLId.setForeground(new java.awt.Color(245, 249, 250));
        jLId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLId.setText("ID DEL SOCIO");
        jLId.setOpaque(true);

        JLEntradasDisponibles.setBackground(new java.awt.Color(51, 51, 51));
        JLEntradasDisponibles.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        JLEntradasDisponibles.setForeground(new java.awt.Color(245, 249, 250));
        JLEntradasDisponibles.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JLEntradasDisponibles.setText("ENTRADAS DISPONIBLES");
        JLEntradasDisponibles.setOpaque(true);

        jTIdSocio.addActionListener(this::jTIdSocioActionPerformed);

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelControlesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLSocios, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBSocios, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLId, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTIdSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLPase, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBPases, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JLEntradasDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTEntradasDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLClase, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBClase, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jDCFechaDeAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelControlesLayout.createSequentialGroup()
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(12)
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLRegistrodeAsistencia, 0, 465, Short.MAX_VALUE)
                        .addComponent(jSPAsistencia, 0, 465, Short.MAX_VALUE))
                    .addContainerGap())
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelControlesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelControlesLayout.createSequentialGroup()
                            .addComponent(jLSocios, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBSocios, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLId, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jTIdSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLPase, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBPases, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(JLEntradasDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jTEntradasDisponibles, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLClase, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBClase, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jDCFechaDeAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelControlesLayout.createSequentialGroup()
                            .addComponent(jLRegistrodeAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jSPAsistencia, 0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        lblFondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/fondos/asistencia.jpg"))); // NOI18N
        panelCapas.setLayer(panelControles, javax.swing.JLayeredPane.PALETTE_LAYER);
        panelCapas.setLayer(lblFondo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        javax.swing.GroupLayout panelCapasLayout = new javax.swing.GroupLayout(panelCapas);
        panelCapas.setLayout(panelCapasLayout);
        panelCapasLayout.setHorizontalGroup(
            panelCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelControles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblFondo, 0, 0, Short.MAX_VALUE)
        );
        panelCapasLayout.setVerticalGroup(
            panelCapasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelControles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblFondo, 0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout ventanaLayout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(ventanaLayout);
        ventanaLayout.setHorizontalGroup(
            ventanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCapas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ventanaLayout.setVerticalGroup(
            ventanaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCapas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCBSociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBSociosActionPerformed
        actualizarDatosSocioSeleccionado();

    }//GEN-LAST:event_jCBSociosActionPerformed

    private void jCBClaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBClaseActionPerformed

        int indice = jCBClase.getSelectedIndex();

        if (indice >= 0 && indice < listaClases.size()) {

            ClaseGym clase = listaClases.get(indice);

            System.out.println("ID Clase: " + clase.getId());
            System.out.println("Disciplina: " + clase.getDisciplina());

            if (clase.getEntrenador() != null) {
                System.out.println("Entrenador: "
                        + clase.getEntrenador().getNombre() + " "
                        + clase.getEntrenador().getApellido());

                System.out.println("ID Entrenador: "
                        + clase.getEntrenador().getIdEntrenador());
            }
        }
    }//GEN-LAST:event_jCBClaseActionPerformed

    private void jBRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBRegistrarActionPerformed

        int indice = jCBSocios.getSelectedIndex();
        if (indice < 0 || indice >= listaSocios.size()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un socio.");
            return;
        }
        Socio socio = listaSocios.get(indice);

        if (!socio.isEstado()) {
            JOptionPane.showMessageDialog(this, "El socio seleccionado está inactivo.");
            return;
        }

        int indicePase = jCBPases.getSelectedIndex();
        if (indicePase < 0 || indicePase >= listaPases.size()) {
            JOptionPane.showMessageDialog(this, "El socio no tiene un pase activo con entradas disponibles.");
            return;
        }
        Pase pase = listaPases.get(indicePase);

        int indiceClase = jCBClase.getSelectedIndex();
        if (indiceClase < 0 || indiceClase >= listaClases.size()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una clase.");
            return;
        }
        ClaseGym clase = listaClases.get(indiceClase);

        java.util.Date fecha = jDCFechaDeAsistencia.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de asistencia.");
            return;
        }

        LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        AsistenciaData asistenciaData = new AsistenciaData();

        if (!asistenciaData.hayCupoDisponible(clase.getId(), fechaLocal)) {
            JOptionPane.showMessageDialog(this,
                    "La clase de " + clase.getDisciplina() + " ya alcanzó su cupo máximo (" + clase.getCapacidad() + ") para esa fecha.",
                    "Sin cupo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (asistenciaData.existeAsistencia(socio.getIdSocio(), clase.getId(), fechaLocal)) {
            JOptionPane.showMessageDialog(this,
                    "El socio ya tiene registrada una asistencia a esa clase en la fecha seleccionada.",
                    "Asistencia duplicada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Asistencia asistencia = new Asistencia(0, socio, clase, pase, fechaLocal);

        if (asistenciaData.guardarAsistencia(asistencia)) {
            JOptionPane.showMessageDialog(this, "Asistencia registrada correctamente.");
            cargarTabla();
            actualizarDatosSocioSeleccionado();
            jDCFechaDeAsistencia.setDate(null);
            jTAsistencia.clearSelection();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar la asistencia. Verificá que el pase esté vigente para la fecha elegida.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jBRegistrarActionPerformed

    private void jBModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBModificarActionPerformed
        int fila = jTAsistencia.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una asistencia de la tabla.");
            return;
        }

        int id = Integer.parseInt(jTAsistencia.getValueAt(fila, 0).toString());

        int indiceSocio = jCBSocios.getSelectedIndex();
        int indiceClase = jCBClase.getSelectedIndex();
        int indicePase = jCBPases.getSelectedIndex();

        if (indiceSocio < 0 || indiceClase < 0 || indicePase < 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un socio, un pase activo y una clase.");
            return;
        }

        Socio socio = listaSocios.get(indiceSocio);
        ClaseGym clase = listaClases.get(indiceClase);
        Pase pase = listaPases.get(indicePase);

        if (pase.getIdSocio() != socio.getIdSocio()) {
            JOptionPane.showMessageDialog(this, "El pase seleccionado no pertenece al socio.");
            return;
        }

        java.util.Date fecha = jDCFechaDeAsistencia.getDate();
        if (fecha == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha.");
            return;
        }

        LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Asistencia asistencia = new Asistencia(id, socio, clase, pase, fechaLocal);
        AsistenciaData asistenciaData = new AsistenciaData();

        if (asistenciaData.modificarAsistencia(asistencia)) {
            JOptionPane.showMessageDialog(this, "Asistencia modificada correctamente.");
            cargarTabla();
            actualizarDatosSocioSeleccionado();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar la asistencia. Revisá la vigencia del pase, el cupo y las asistencias duplicadas.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jBModificarActionPerformed

    private void jBBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBorrarActionPerformed

        int fila = jTAsistencia.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una asistencia de la tabla.");
            return;
        }

        int id = Integer.parseInt(jTAsistencia.getValueAt(fila, 0).toString());

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar esta asistencia? (Se reintegrará 1 entrada al pase)",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        AsistenciaData asistenciaData = new AsistenciaData();

        if (asistenciaData.eliminarAsistencia(id)) {
            JOptionPane.showMessageDialog(this, "Asistencia eliminada y entrada reintegrada correctamente.");
            cargarTabla();
            actualizarDatosSocioSeleccionado();
            jTAsistencia.clearSelection();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la asistencia.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jBBorrarActionPerformed

    private void jBLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimpiarActionPerformed
        if (jCBSocios.getItemCount() > 0) {
            jCBSocios.setSelectedIndex(0);
        }
        if (jCBClase.getItemCount() > 0) {
            jCBClase.setSelectedIndex(0);
        }
        jDCFechaDeAsistencia.setDate(null);
        jTAsistencia.clearSelection();

    }//GEN-LAST:event_jBLimpiarActionPerformed

    private void jCBPasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBPasesActionPerformed

        int indice = jCBPases.getSelectedIndex();
        if (indice >= 0 && indice < listaPases.size()) {
            Pase pase = listaPases.get(indice);
            jTEntradasDisponibles.setText(String.valueOf(pase.getCantidadEntradas()));
        } else {
            jTEntradasDisponibles.setText("0");
        }
    }//GEN-LAST:event_jCBPasesActionPerformed

    private void jTIdSocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTIdSocioActionPerformed

    }//GEN-LAST:event_jTIdSocioActionPerformed

        private void cargarTabla() {
            DefaultTableModel modelo = (DefaultTableModel) jTAsistencia.getModel();
            modelo.setRowCount(0);

            AsistenciaData asistenciaData = new AsistenciaData();
            listaAsistencias = asistenciaData.listarAsistencia();

            for (Asistencia asistencia : listaAsistencias) {
                String nombreSocio = asistencia.getSocio() != null
                        ? asistencia.getSocio().getApellido() + ", " + asistencia.getSocio().getNombre()
                        : "N/A";
                String nombreClase = asistencia.getClase() != null
                        ? asistencia.getClase().getDisciplina() + " (" + asistencia.getClase().getHorario() + ")"
                        : "N/A";
                int idPase = asistencia.getPase() != null ? asistencia.getPase().getIdPase() : 0;

                modelo.addRow(new Object[]{
                    asistencia.getId(),
                    nombreSocio,
                    nombreClase,
                    "Pase #" + idPase,
                    asistencia.getFechaAsistencia()
                });
            }
        }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLEntradasDisponibles;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLayeredPane panelCapas;
    private javax.swing.JPanel panelControles;
    private javax.swing.JButton jBBorrar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JButton jBModificar;
    private javax.swing.JButton jBRegistrar;
    private javax.swing.JComboBox<String> jCBClase;
    private javax.swing.JComboBox<String> jCBPases;
    private javax.swing.JComboBox<String> jCBSocios;
    private javax.swing.JLabel jLClase;
    private javax.swing.JLabel jLId;
    private javax.swing.JLabel jLPase;
    private javax.swing.JLabel jLRegistrodeAsistencia;
    private javax.swing.JLabel jLSocios;
    private com.toedter.calendar.JDateChooser jDCFechaDeAsistencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jSPAsistencia;
    private javax.swing.JTable jTAsistencia;
    private javax.swing.JTextField jTEntradasDisponibles;
    private javax.swing.JTextField jTIdSocio;
    // End of variables declaration//GEN-END:variables
}
