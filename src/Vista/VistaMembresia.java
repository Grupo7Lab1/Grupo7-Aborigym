/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Modelo.Membresia;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import Persistencia.MembresiaData;

public class VistaMembresia extends javax.swing.JInternalFrame {

    private final List<Membresia> membresias = new ArrayList<>();
    private Membresia seleccionada = null;
    private DefaultTableModel modelo;

    public VistaMembresia() {
        initComponents();
        Iconos.aplicar(this);
        pack();
        configurarTabla();
        initEventos();
        cargarMembresias();
        modoNuevo();
        setClosable(true);
    }

    private void configurarTabla() {
        modelo = new DefaultTableModel(
                new Object[]{
                    "ID", "Tipo", "Cantidad de clases",
                    "Precio", "Duración (días)"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        jTMembresia.setModel(modelo);
        jTMembresia.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void cargarMembresias() {
        MembresiaData data = new MembresiaData();

        membresias.clear();
        membresias.addAll(data.listarMembresias());

        refrescarTabla();
    }

    private void initEventos() {
        jTMembresia.getSelectionModel().addListSelectionListener(e -> {

            int fila = jTMembresia.getSelectedRow();

            if (!e.getValueIsAdjusting()
                    && fila >= 0
                    && fila < membresias.size()) {

                cargar(membresias.get(fila));
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCapas = new javax.swing.JLayeredPane();
        panelControles = new javax.swing.JPanel();
        jLTipoDeMembresia = new javax.swing.JLabel();
        jTTipoDeMembresia = new javax.swing.JTextField();
        jLPrecio = new javax.swing.JLabel();
        jTPrecio = new javax.swing.JTextField();
        jLDuracion = new javax.swing.JLabel();
        jTDuracion = new javax.swing.JTextField();
        jBGuardar = new javax.swing.JButton();
        jBNueva = new javax.swing.JButton();
        jBEliminar = new javax.swing.JButton();
        jSPMembresia = new javax.swing.JScrollPane();
        jTMembresia = new javax.swing.JTable();
        jBModificar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTCantidadDeClases = new javax.swing.JTextField();
        lblFondo = new javax.swing.JLabel();

        setTitle("Membresía");

        panelControles.setOpaque(false);

        jLTipoDeMembresia.setBackground(new java.awt.Color(51, 51, 51));
        jLTipoDeMembresia.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLTipoDeMembresia.setForeground(new java.awt.Color(245, 249, 250));
        jLTipoDeMembresia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLTipoDeMembresia.setText("TIPO DE MEMBRESÍA");
        jLTipoDeMembresia.setOpaque(true);

        jLPrecio.setBackground(new java.awt.Color(51, 51, 51));
        jLPrecio.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLPrecio.setForeground(new java.awt.Color(245, 249, 250));
        jLPrecio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLPrecio.setText("PRECIO");
        jLPrecio.setOpaque(true);

        jLDuracion.setBackground(new java.awt.Color(51, 51, 51));
        jLDuracion.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLDuracion.setForeground(new java.awt.Color(245, 249, 250));
        jLDuracion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLDuracion.setText("DURACIÓN (DÍAS)");
        jLDuracion.setOpaque(true);

        jBGuardar.setBackground(new java.awt.Color(42, 105, 95));
        jBGuardar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBGuardar.setForeground(new java.awt.Color(245, 249, 250));
        jBGuardar.setText("GUARDAR");
        jBGuardar.setContentAreaFilled(false);
        jBGuardar.setOpaque(true);
        jBGuardar.addActionListener(this::jBGuardarActionPerformed);

        jBNueva.setBackground(new java.awt.Color(55, 169, 183));
        jBNueva.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBNueva.setForeground(new java.awt.Color(245, 249, 250));
        jBNueva.setText("NUEVA");
        jBNueva.setContentAreaFilled(false);
        jBNueva.setOpaque(true);
        jBNueva.addActionListener(this::jBNuevaActionPerformed);

        jBEliminar.setBackground(new java.awt.Color(133, 49, 65));
        jBEliminar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBEliminar.setForeground(new java.awt.Color(245, 249, 250));
        jBEliminar.setText("ELIMINAR");
        jBEliminar.setContentAreaFilled(false);
        jBEliminar.setOpaque(true);
        jBEliminar.addActionListener(this::jBEliminarActionPerformed);

        jTMembresia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tipo De Membresia", "Precio", "Cant. de Clases", "Duracion(Dias)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jSPMembresia.setViewportView(jTMembresia);

        jBModificar.setBackground(new java.awt.Color(255, 189, 87));
        jBModificar.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jBModificar.setForeground(new java.awt.Color(245, 249, 250));
        jBModificar.setText("MODIFICAR");
        jBModificar.setContentAreaFilled(false);
        jBModificar.setOpaque(true);
        jBModificar.addActionListener(this::jBModificarActionPerformed);

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Dubai", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(245, 249, 250));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CANTIDAD DE CLASES");
        jLabel1.setOpaque(true);

        jTCantidadDeClases.addActionListener(this::jTCantidadDeClasesActionPerformed);

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTTipoDeMembresia, 0, 194, 194)
                    .addComponent(jLPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTPrecio, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTCantidadDeClases, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLDuracion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTDuracion, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBNueva, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLTipoDeMembresia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSPMembresia, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSPMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(jLTipoDeMembresia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTTipoDeMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLPrecio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTCantidadDeClases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLDuracion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBGuardar)
                            .addComponent(jBModificar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBNueva)
                            .addComponent(jBEliminar))))
                .addGap(16, 16, 16))
        );

        lblFondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Recursos/fondos/membresia.jpg"))); // NOI18N

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCapas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelCapas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardarActionPerformed
        String tipo = jTTipoDeMembresia.getText().trim();
        double precio;
        int duracion;

        if (tipo.isEmpty()) {
            avisar("Ingresá el tipo de membresía.");
            return;
        }

        if (tipo.length() < 3) {
            avisar("El tipo de membresía debe tener al menos 3 caracteres.");
            return;
        }

        String textoPrecio = jTPrecio.getText().trim().replace(",", ".");

        if (textoPrecio.replace(".", "").length() > 10) {
            avisar("El precio no puede superar los 10 dígitos.");
            return;
        }
        try {
            precio = Double.parseDouble(textoPrecio);

            if (precio <= 0 || Double.isInfinite(precio) || Double.isNaN(precio)) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            avisar("El precio debe ser un número válido y mayor que 0.");
            return;
        }

        int cantidadClases;

        try {
            cantidadClases = Integer.parseInt(
                    jTCantidadDeClases.getText().trim()
            );

            if (cantidadClases <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            avisar("La cantidad de clases debe ser un número entero mayor que 0.");
            return;
        }

        try {
            duracion = Integer.parseInt(jTDuracion.getText().trim());

            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            avisar("La duración debe ser mayor a 0.");
            return;
        }

        for (Membresia m : membresias) {
            if (m.getNombre().equalsIgnoreCase(tipo)
                    && m != seleccionada) {
                avisar("Ya existe una membresía con ese tipo.");
                return;
            }
        }

        if (seleccionada == null) {

            Membresia nueva = new Membresia(
                    0,
                    tipo,
                    cantidadClases,
                    duracion,
                    precio
            );

            MembresiaData data = new MembresiaData();

            if (data.guardarMembresia(nueva)) {
                JOptionPane.showMessageDialog(this,
                        "Membresía guardada correctamente.");

                cargarMembresias();
                modoNuevo();

            } else {
                avisar("No se pudo guardar la membresía.");
            }

        } else {
            avisar("Esa membresía ya existe. Usá MODIFICAR para guardar cambios o NUEVA para crear otra.");
        }
       
    }//GEN-LAST:event_jBGuardarActionPerformed

    private void jBNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNuevaActionPerformed
        modoNuevo();
    }//GEN-LAST:event_jBNuevaActionPerformed

    private void jBEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEliminarActionPerformed
        if (seleccionada == null) {
            avisar("Primero seleccioná una membresía de la tabla.");
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar esta membresía?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            MembresiaData data = new MembresiaData();
            if (data.eliminarMembresia(seleccionada.getId())) {
                JOptionPane.showMessageDialog(this, "Membresía eliminada correctamente.");
                cargarMembresias();
                modoNuevo();
            } else {
                avisar("No se pudo eliminar la membresía (verifica que no este asociada a un pase).");
            }
        }
    }//GEN-LAST:event_jBEliminarActionPerformed

    private void jBModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBModificarActionPerformed
        if (seleccionada == null) {
            avisar("Primero seleccioná una membresía de la tabla.");
            return;
        }

        String tipo = jTTipoDeMembresia.getText().trim();

        if (tipo.isEmpty() || tipo.length() < 3) {
            avisar("El tipo de membresía debe tener al menos 3 caracteres.");
            return;
        }

        double precio;
        int duracion;
        int cantidadClases;

        String textoPrecio = jTPrecio.getText().trim().replace(",", ".");

        if (textoPrecio.replace(".", "").length() > 10) {
            avisar("El precio no puede superar los 10 dígitos.");
            return;
        }

        try {
            precio = Double.parseDouble(textoPrecio);
            if (precio <= 0 || Double.isInfinite(precio) || Double.isNaN(precio)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            avisar("El precio debe ser un número válido y mayor que 0");
            return;
        }

        try {
            cantidadClases = Integer.parseInt(jTCantidadDeClases.getText().trim());
            if (cantidadClases <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            avisar("La cantidad de clases debe ser un número entero mayor que 0");
            return;
        }

        try {
            duracion = Integer.parseInt(jTDuracion.getText().trim());
            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            avisar("La duración debe ser mayor a 0");
            return;
        }

        for (Membresia m : membresias) {
            if (m.getNombre().equalsIgnoreCase(tipo) && m.getId() != seleccionada.getId()) {
                avisar("Ya existe otra membresía con ese tipo.");
                return;
            }
        }

        seleccionada.setNombre(tipo);
        seleccionada.setCantidadClases(cantidadClases);
        seleccionada.setPrecio(precio);
        seleccionada.setDuracionDias(duracion);

        MembresiaData data = new MembresiaData();

        if (data.modificarMembresia(seleccionada)) {
            JOptionPane.showMessageDialog(this, "Membresía modificada correctamente.");
            cargarMembresias();
            modoNuevo();
        } else {
            avisar("No se pudo modificar la membresía.");
        }

    }//GEN-LAST:event_jBModificarActionPerformed

    private void jTCantidadDeClasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTCantidadDeClasesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTCantidadDeClasesActionPerformed
    private void avisar(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Revisá los datos", JOptionPane.WARNING_MESSAGE);
    }

    private String formatoPrecio(double precio) {
        return (precio % 1 == 0) ? String.valueOf((long) precio) : String.valueOf(precio);
    }

    private void refrescarTabla() {

        modelo.setRowCount(0);

        for (Membresia m : membresias) {

            modelo.addRow(new Object[]{
                m.getId(),
                m.getNombre(),
                m.getCantidadClases(),
                formatoPrecio(m.getPrecio()),
                m.getDuracionDias()
            });
        }
    }

    private void cargar(Membresia m) {
        seleccionada = m;

        jTTipoDeMembresia.setText(m.getNombre());

        jTCantidadDeClases.setText(
                String.valueOf(m.getCantidadClases())
        );

        jTPrecio.setText(formatoPrecio(m.getPrecio()));

        jTDuracion.setText(
                String.valueOf(m.getDuracionDias())
        );

        jBEliminar.setEnabled(true);
    }

    private void modoNuevo() {
        seleccionada = null;
        jTTipoDeMembresia.setText("");
        jTPrecio.setText("");
        jTDuracion.setText("");
        jTMembresia.clearSelection();
        jBEliminar.setEnabled(false);
        jTTipoDeMembresia.requestFocusInWindow();
        jTCantidadDeClases.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBEliminar;
    private javax.swing.JButton jBGuardar;
    private javax.swing.JButton jBModificar;
    private javax.swing.JButton jBNueva;
    private javax.swing.JLabel jLDuracion;
    private javax.swing.JLabel jLPrecio;
    private javax.swing.JLabel jLTipoDeMembresia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jSPMembresia;
    private javax.swing.JTextField jTCantidadDeClases;
    private javax.swing.JTextField jTDuracion;
    private javax.swing.JTable jTMembresia;
    private javax.swing.JTextField jTPrecio;
    private javax.swing.JTextField jTTipoDeMembresia;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLayeredPane panelCapas;
    private javax.swing.JPanel panelControles;
    // End of variables declaration//GEN-END:variables
}
