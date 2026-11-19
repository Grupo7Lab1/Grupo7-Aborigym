/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Modelo.ClaseGym;
import Modelo.Entrenador;
import Persistencia.ClaseGymData;
import Persistencia.EntrenadorData;
import java.text.Collator;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class VistaClaseGym extends javax.swing.JInternalFrame {

    private List<Entrenador> entrenadoresCargados = new ArrayList<>();
    private List<ClaseGym> clasesCargadas = new ArrayList<>();
    private ClaseGym claseSeleccionada = null;
    private DefaultTableModel modeloTabla;

    public VistaClaseGym() {
        initComponents();
        Iconos.aplicar(this);
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        configurarTabla();
        cargarDisciplinas();
        cargarHorarios();
        cargarCapacidades();
        cargarEstados();
        initEventos();
        cargarTabla();
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Disciplina", "Entrenador", "Horario", "Capacidad", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(modeloTabla);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initEventos() {
        jBGuardar.addActionListener(this::guardarClase);
        jBModificar.addActionListener(this::modificarClase);
        jBBorrar.addActionListener(this::borrarClase);
        jBLimpiar.addActionListener(e -> limpiarFormulario());

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            int fila = jTable1.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0 && fila < clasesCargadas.size()) {
                cargarClaseEnFormulario(clasesCargadas.get(fila));
            }
        });
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        ClaseGymData data = new ClaseGymData();
        clasesCargadas = data.listarClases();

        for (ClaseGym c : clasesCargadas) {
            modeloTabla.addRow(new Object[]{
                c.getId(),
                c.getDisciplina(),
                c.getEntrenador() != null ? c.getEntrenador().getApellido() + ", " + c.getEntrenador().getNombre() : "Sin asignar",
                c.getHorario(),
                c.getCapacidad(),
                c.isEstado() ? "Activa" : "Inactiva"
            });
        }
    }

    private void cargarClaseEnFormulario(ClaseGym c) {
        claseSeleccionada = c;
        String disciplina = c.getDisciplina();
        if (((javax.swing.DefaultComboBoxModel<String>) jCBDisciplina.getModel()).getIndexOf(disciplina) < 0) {
            jCBDisciplina.addItem(disciplina);
        }
        jCBDisciplina.setSelectedItem(disciplina);
        jCBEntrenador.setSelectedIndex(-1);

        if (c.getEntrenador() != null) {
            for (int i = 0; i < entrenadoresCargados.size(); i++) {
                if (entrenadoresCargados.get(i).getIdEntrenador() == c.getEntrenador().getIdEntrenador()) {
                    jCBEntrenador.setSelectedIndex(i);
                    break;
                }
            }
        }

        String horario = java.time.LocalTime.parse(c.getHorario().split(" - ", 2)[0].trim()).toString();
        for (int i = 0; i < jCBHorario.getItemCount(); i++) {
            if (jCBHorario.getItemAt(i).split(" - ", 2)[0].equals(horario)) {
                jCBHorario.setSelectedIndex(i);
                horario = null;
                break;
            }
        }
        if (horario != null) {
            jCBHorario.addItem(horario);
            jCBHorario.setSelectedItem(horario);
        }
        jCBCapacidad.setSelectedItem(String.valueOf(c.getCapacidad()));
        jCBEstado.setSelectedItem(c.isEstado() ? "Activa" : "Inactiva");
    }

    private void limpiarFormulario() {
        claseSeleccionada = null;
        jTable1.clearSelection();
        if (jCBDisciplina.getItemCount() > 0) {
            jCBDisciplina.setSelectedIndex(0);
        }
        if (jCBHorario.getItemCount() > 0) {
            jCBHorario.setSelectedIndex(0);
        }
        if (jCBCapacidad.getItemCount() > 0) {
            jCBCapacidad.setSelectedIndex(0);
        }
        if (jCBEstado.getItemCount() > 0) {
            jCBEstado.setSelectedIndex(0);
        }
    }

    private void cargarDisciplinas() {
        jCBDisciplina.removeAllItems();
        jCBDisciplina.addItem("Musculación");
        jCBDisciplina.addItem("Spinning");
        jCBDisciplina.addItem("Funcional");
        jCBDisciplina.addItem("Pilates");
        jCBDisciplina.addItem("CrossFit");
    }

    private void cargarHorarios() {
        jCBHorario.removeAllItems();
        jCBHorario.addItem("08:00 - 09:00");
        jCBHorario.addItem("09:00 - 10:00");
        jCBHorario.addItem("10:00 - 11:00");
        jCBHorario.addItem("17:00 - 18:00");
        jCBHorario.addItem("18:00 - 19:00");
    }

    private void cargarCapacidades() {
        jCBCapacidad.removeAllItems();
        jCBCapacidad.addItem("10");
        jCBCapacidad.addItem("15");
        jCBCapacidad.addItem("20");
        jCBCapacidad.addItem("25");
    }

    private void cargarEstados() {
        jCBEstado.removeAllItems();
        jCBEstado.addItem("Activa");
        jCBEstado.addItem("Inactiva");
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCapas = new javax.swing.JLayeredPane();
        panelControles = new javax.swing.JPanel();
        lblFondo = new javax.swing.JLabel();
        jLDatosDeLaClase = new javax.swing.JLabel();
        jLDisciplina = new javax.swing.JLabel();
        jCBDisciplina = new javax.swing.JComboBox<>();
        jLEntrenador = new javax.swing.JLabel();
        jCBEntrenador = new javax.swing.JComboBox<>();
        jLHorario = new javax.swing.JLabel();
        jCBHorario = new javax.swing.JComboBox<>();
        jLCapacidad = new javax.swing.JLabel();
        jCBCapacidad = new javax.swing.JComboBox<>();
        jLEstado = new javax.swing.JLabel();
        jCBEstado = new javax.swing.JComboBox<>();
        jBGuardar = new javax.swing.JButton();
        jBModificar = new javax.swing.JButton();
        jBBorrar = new javax.swing.JButton();
        jBLimpiar = new javax.swing.JButton();
        jSPClaseGym = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setTitle("Gestión de Clases");

        panelControles.setOpaque(false);

        jLDatosDeLaClase.setBackground(new java.awt.Color(51, 51, 51));
        jLDatosDeLaClase.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLDatosDeLaClase.setForeground(new java.awt.Color(245, 249, 250));
        jLDatosDeLaClase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLDatosDeLaClase.setText("DATOS DE LA CLASE");
        jLDatosDeLaClase.setOpaque(true);

        jLDisciplina.setBackground(new java.awt.Color(51, 51, 51));
        jLDisciplina.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLDisciplina.setForeground(new java.awt.Color(245, 249, 250));
        jLDisciplina.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLDisciplina.setText("DISCIPLINA");
        jLDisciplina.setOpaque(true);

        jCBDisciplina.addActionListener(this::jCBDisciplinaActionPerformed);

        jLEntrenador.setBackground(new java.awt.Color(51, 51, 51));
        jLEntrenador.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLEntrenador.setForeground(new java.awt.Color(245, 249, 250));
        jLEntrenador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLEntrenador.setText("ENTRENADOR");
        jLEntrenador.setOpaque(true);

        jCBEntrenador.addActionListener(this::jCBEntrenadorActionPerformed);

        jLHorario.setBackground(new java.awt.Color(51, 51, 51));
        jLHorario.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLHorario.setForeground(new java.awt.Color(245, 249, 250));
        jLHorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLHorario.setText("HORARIO");
        jLHorario.setOpaque(true);

        jCBHorario.addActionListener(this::jCBHorarioActionPerformed);

        jLCapacidad.setBackground(new java.awt.Color(51, 51, 51));
        jLCapacidad.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLCapacidad.setForeground(new java.awt.Color(245, 249, 250));
        jLCapacidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLCapacidad.setText("CAPACIDAD");
        jLCapacidad.setOpaque(true);

        jLEstado.setBackground(new java.awt.Color(51, 51, 51));
        jLEstado.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLEstado.setForeground(new java.awt.Color(245, 249, 250));
        jLEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLEstado.setText("ESTADO");
        jLEstado.setOpaque(true);

        jBGuardar.setBackground(new java.awt.Color(42, 105, 95));
        jBGuardar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBGuardar.setForeground(new java.awt.Color(245, 249, 250));
        jBGuardar.setText("GUARDAR");
        jBGuardar.setContentAreaFilled(false);
        jBGuardar.setOpaque(true);

        jBModificar.setBackground(new java.awt.Color(255, 189, 87));
        jBModificar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBModificar.setForeground(new java.awt.Color(245, 249, 250));
        jBModificar.setText("MODIFICAR");
        jBModificar.setContentAreaFilled(false);
        jBModificar.setOpaque(true);

        jBBorrar.setBackground(new java.awt.Color(133, 49, 65));
        jBBorrar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBBorrar.setForeground(new java.awt.Color(245, 249, 250));
        jBBorrar.setText("BORRAR");
        jBBorrar.setContentAreaFilled(false);
        jBBorrar.setOpaque(true);

        jBLimpiar.setBackground(new java.awt.Color(55, 169, 183));
        jBLimpiar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBLimpiar.setForeground(new java.awt.Color(245, 249, 250));
        jBLimpiar.setText("LIMPIAR");
        jBLimpiar.setContentAreaFilled(false);
        jBLimpiar.setOpaque(true);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Disciplina", "Entrenador", "Horario", "Capacidad", "Estado"
            }
        ));
        jSPClaseGym.setViewportView(jTable1);

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelControlesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCBEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelControlesLayout.createSequentialGroup()
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(12)
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLDatosDeLaClase, 0, 465, Short.MAX_VALUE)
                        .addComponent(jSPClaseGym, 0, 465, Short.MAX_VALUE))
                    .addContainerGap())
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelControlesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelControlesLayout.createSequentialGroup()
                            .addComponent(jLDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBEntrenador, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBHorario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jLEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jCBEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(12)
                            .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jBLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jBBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelControlesLayout.createSequentialGroup()
                            .addComponent(jLDatosDeLaClase, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6)
                            .addComponent(jSPClaseGym, 0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        lblFondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/fondos/clasegym.jpg"))); // NOI18N
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

    private void jCBDisciplinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBDisciplinaActionPerformed
        jCBEntrenador.removeAllItems();
        entrenadoresCargados.clear();

        String disciplina = (String) jCBDisciplina.getSelectedItem();
        if (disciplina == null) {
            return;
        }

        EntrenadorData entrenadorData = new EntrenadorData();
        List<Entrenador> lista = entrenadorData.listarEntrenadores();
        Collator comparador = Collator.getInstance(Locale.forLanguageTag("es-AR"));
        comparador.setStrength(Collator.PRIMARY);

        for (Entrenador entrenador : lista) {
            if (entrenador.isEstado()
                    && entrenador.getEspecialidad() != null
                    && comparador.compare(entrenador.getEspecialidad().trim(), disciplina) == 0) {

                entrenadoresCargados.add(entrenador);
                jCBEntrenador.addItem(
                        entrenador.getNombre() + " " + entrenador.getApellido()
                );
            }
        }
    }//GEN-LAST:event_jCBDisciplinaActionPerformed

    private void jCBEntrenadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEntrenadorActionPerformed


    }//GEN-LAST:event_jCBEntrenadorActionPerformed

    private void jCBHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBHorarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBHorarioActionPerformed
    private void guardarClase(java.awt.event.ActionEvent evt) {
        if (claseSeleccionada != null) {
            JOptionPane.showMessageDialog(this,
                    "Esa clase ya existe. Usa MODIFICAR para guardar cambios o LIMPIAR para crear una nueva.");
            return;
        }

        int indiceEntrenador = jCBEntrenador.getSelectedIndex();
        if (indiceEntrenador < 0 || indiceEntrenador >= entrenadoresCargados.size()) {
            JOptionPane.showMessageDialog(this,
                    "No hay un entrenador activo disponible para esa disciplina.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String disciplina = (String) jCBDisciplina.getSelectedItem();
        Entrenador entrenador = entrenadoresCargados.get(indiceEntrenador);
        String horario = (String) jCBHorario.getSelectedItem();
        int capacidad = Integer.parseInt((String) jCBCapacidad.getSelectedItem());
        boolean estado = "Activa".equals(jCBEstado.getSelectedItem());

        ClaseGymData data = new ClaseGymData();

        if (estado && data.entrenadorOcupadoEnHorario(entrenador.getIdEntrenador(), horario, 0)) {
            JOptionPane.showMessageDialog(this,
                    "El entrenador " + entrenador.getNombre() + " " + entrenador.getApellido()
                    + " ya tiene otra clase asignada en el horario " + horario + ".",
                    "Horario superpuesto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClaseGym nueva = new ClaseGym(0, disciplina, entrenador, horario, capacidad, estado);

        if (data.guardarClase(nueva)) {
            JOptionPane.showMessageDialog(this, "Clase guardada correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la clase.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarClase(java.awt.event.ActionEvent evt) {
        if (claseSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná una clase de la tabla para modificar.");
            return;
        }

        int indiceEntrenador = jCBEntrenador.getSelectedIndex();
        if (indiceEntrenador < 0 || indiceEntrenador >= entrenadoresCargados.size()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná un entrenador válido para esa disciplina.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String disciplina = (String) jCBDisciplina.getSelectedItem();
        Entrenador entrenador = entrenadoresCargados.get(indiceEntrenador);
        String horario = (String) jCBHorario.getSelectedItem();
        int capacidad = Integer.parseInt((String) jCBCapacidad.getSelectedItem());
        boolean estado = "Activa".equals(jCBEstado.getSelectedItem());

        ClaseGymData data = new ClaseGymData();

        if (estado && data.entrenadorOcupadoEnHorario(entrenador.getIdEntrenador(), horario, claseSeleccionada.getId())) {
            JOptionPane.showMessageDialog(this,
                    "El entrenador " + entrenador.getNombre() + " " + entrenador.getApellido()
                    + " ya tiene otra clase asignada en el horario " + horario + ".",
                    "Horario superpuesto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        claseSeleccionada.setDisciplina(disciplina);
        claseSeleccionada.setEntrenador(entrenador);
        claseSeleccionada.setHorario(horario);
        claseSeleccionada.setCapacidad(capacidad);
        claseSeleccionada.setEstado(estado);

        if (data.modificarClase(claseSeleccionada)) {
            JOptionPane.showMessageDialog(this, "Clase modificada correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo modificar la clase.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarClase(java.awt.event.ActionEvent evt) {
        if (claseSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná una clase de la tabla.");
            return;
        }

        ClaseGymData data = new ClaseGymData();

        if (claseSeleccionada.isEstado()) {
            int r = JOptionPane.showConfirmDialog(this,
                    "¿Deseás dar de baja (inactivar) la clase de " + claseSeleccionada.getDisciplina() + "?",
                    "Confirmar baja lógica", JOptionPane.YES_NO_OPTION);

            if (r == JOptionPane.YES_OPTION) {
                if (data.bajaLogica(claseSeleccionada.getId())) {
                    JOptionPane.showMessageDialog(this, "Clase dada de baja correctamente.");
                    cargarTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo dar de baja la clase.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            int r = JOptionPane.showConfirmDialog(this,
                    "La clase ya está inactiva. ¿Deseás eliminarla definitivamente de la base de datos?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (r == JOptionPane.YES_OPTION) {
                if (data.eliminarClase(claseSeleccionada.getId())) {
                    JOptionPane.showMessageDialog(this, "Clase eliminada correctamente.");
                    cargarTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la clase (puede tener asistencias asociadas).",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLayeredPane panelCapas;
    private javax.swing.JPanel panelControles;
    private javax.swing.JButton jBBorrar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBLimpiar;
    private javax.swing.JButton jBModificar;
    private javax.swing.JComboBox<String> jCBCapacidad;
    private javax.swing.JComboBox<String> jCBDisciplina;
    private javax.swing.JComboBox<String> jCBEntrenador;
    private javax.swing.JComboBox<String> jCBEstado;
    private javax.swing.JComboBox<String> jCBHorario;
    private javax.swing.JLabel jLCapacidad;
    private javax.swing.JLabel jLDatosDeLaClase;
    private javax.swing.JLabel jLDisciplina;
    private javax.swing.JLabel jLEntrenador;
    private javax.swing.JLabel jLEstado;
    private javax.swing.JLabel jLHorario;
    private javax.swing.JScrollPane jSPClaseGym;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
