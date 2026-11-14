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
    private int proximoId = 1;
    private Membresia seleccionada = null;
    private DefaultTableModel modelo;

    public VistaMembresia() {
        initComponents();
        Iconos.aplicar(this);
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

        setTitle("Membresia");

        jLTipoDeMembresia.setText("Tipo de Membresia");

        jLPrecio.setText("Precio");

        jLDuracion.setText("Duracion  (Dias)");

        jBGuardar.setBackground(new java.awt.Color(204, 204, 255));
        jBGuardar.setForeground(new java.awt.Color(0, 0, 0));
        jBGuardar.setText("Guardar");
        jBGuardar.addActionListener(this::jBGuardarActionPerformed);

        jBNueva.setBackground(new java.awt.Color(204, 204, 255));
        jBNueva.setForeground(new java.awt.Color(0, 0, 0));
        jBNueva.setText("Nueva");
        jBNueva.addActionListener(this::jBNuevaActionPerformed);

        jBEliminar.setBackground(new java.awt.Color(204, 204, 255));
        jBEliminar.setForeground(new java.awt.Color(0, 0, 0));
        jBEliminar.setText("Eliminar");
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

        jBModificar.setBackground(new java.awt.Color(204, 204, 255));
        jBModificar.setForeground(new java.awt.Color(0, 0, 0));
        jBModificar.setText("Modificar");
        jBModificar.addActionListener(this::jBModificarActionPerformed);

        jLabel1.setText("Cantidad de Clases");

        jTCantidadDeClases.addActionListener(this::jTCantidadDeClasesActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLTipoDeMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jBModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBEliminar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBNueva, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTDuracion, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLDuracion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(jTPrecio, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLPrecio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTTipoDeMembresia, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTCantidadDeClases))
                        .addGap(18, 18, 18)
                        .addComponent(jSPMembresia, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTipoDeMembresia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTTipoDeMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLPrecio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTCantidadDeClases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLDuracion)
                        .addGap(18, 18, 18)
                        .addComponent(jTDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBNueva)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBEliminar))
                    .addComponent(jSPMembresia, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBModificar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGuardarActionPerformed
        String tipo = jTTipoDeMembresia.getText().trim();
        double precio;
        int duracion;

        if (tipo.isEmpty()) {
            avisar("Escribí el tipo de membresía.");
            return;
        }

        try {
            precio = Double.parseDouble(
                    jTPrecio.getText().trim().replace(",", ".")
            );

            if (precio < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            avisar("El precio debe ser un número válido.");
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
            membresias.add(new Membresia(
                    proximoId++,
                    tipo,
                    0,
                    duracion,
                    precio
            ));
        } else {
            seleccionada.setNombre(tipo);
            seleccionada.setPrecio(precio);
            seleccionada.setDuracionDias(duracion);
        }

        refrescarTabla();
        modoNuevo();

        JOptionPane.showMessageDialog(this,
                "Membresía guardada correctamente.");
    }//GEN-LAST:event_jBGuardarActionPerformed

    private void jBNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNuevaActionPerformed
        modoNuevo();
    }//GEN-LAST:event_jBNuevaActionPerformed

    private void jBEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEliminarActionPerformed
        if (seleccionada == null) {
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar esta membresía?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            membresias.remove(seleccionada);
            refrescarTabla();
            modoNuevo();
        }
    }//GEN-LAST:event_jBEliminarActionPerformed

    private void jBModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBModificarActionPerformed

        if (seleccionada == null) {
            avisar("Primero seleccioná una membresía de la tabla.");
            return;
        }

        String tipo = jTTipoDeMembresia.getText().trim();

        if (tipo.isEmpty()) {
            avisar("Escribí el tipo de membresía.");
            return;
        }

        double precio;
        int duracion;

        try {
            precio = Double.parseDouble(
                    jTPrecio.getText().trim().replace(",", ".")
            );

            if (precio < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            avisar("El precio debe ser un número válido.");
            return;
        }

        try {
            duracion = Integer.parseInt(jTDuracion.getText().trim());

            if (duracion <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            avisar("La duración debe ser mayor a 0.");
            return;
        }

        seleccionada.setNombre(tipo);
        seleccionada.setPrecio(precio);
        seleccionada.setDuracionDias(duracion);

        refrescarTabla();
        modoNuevo();

        JOptionPane.showMessageDialog(this,
                "Membresía modificada correctamente.");
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
    // End of variables declaration//GEN-END:variables
}
