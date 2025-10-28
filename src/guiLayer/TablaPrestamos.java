
package guiLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utilerias.ManejadorArchivos;

public class TablaPrestamos extends JPanel {
    private TablaLibros tablaLibros;
    private TablaUsuarios tablaUsuarios;
    DefaultTableModel modeloTabla;
    JTable tablaPrestamos;
    private ManejadorArchivos manejadorArchivos;
    JComboBox<String> comboBoxUsuarios = new JComboBox();
    JComboBox<String> comboBoxLibros = new JComboBox();

    public TablaPrestamos(TablaLibros tablaLibros, TablaUsuarios tablaUsuarios) {
        this.tablaLibros = tablaLibros;
        this.tablaUsuarios = tablaUsuarios;
        this.manejadorArchivos = new ManejadorArchivos();
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Prestamos");
        lblTitulo.setFont(new Font("Garamond", 3, 30));
        lblTitulo.setHorizontalAlignment(0);
        this.add(lblTitulo, "North");
        JPanel panelTabla = new JPanel();
        String[] columnas = new String[]{"Id Préstamo", "Usuario", "Libro", "Fecha Préstamo", "Fecha devolución", "Estado"};
        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tablaPrestamos = new JTable(this.modeloTabla);
        JScrollPane scrollPane = new JScrollPane(this.tablaPrestamos);
        scrollPane.setPreferredSize(new Dimension(600, 270));
        panelTabla.add(scrollPane);
        this.cargarPrestamosDesdeArchivo();
        this.add(panelTabla, "Center");
        JPanel botonesPrestamos = new JPanel();
        JButton nuevoPrestamo = new JButton("Nuevo Préstamo");
        JButton devolverPrestamo = new JButton("Devolver Préstamo");
        nuevoPrestamo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaPrestamos.this.dialogoNuevoPrestamo();
            }
        });
        devolverPrestamo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaPrestamos.this.dialogoDevolverPrestamo();
            }
        });
        botonesPrestamos.add(nuevoPrestamo);
        botonesPrestamos.add(devolverPrestamo);
        panelTabla.add(botonesPrestamos, "South");
    }

    private void dialogoNuevoPrestamo() {
        JDialog dialogo = new JDialog((Frame)null, "Nuevo Préstamo", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Nuevo Préstamo");
        lblTitulo.setFont(new Font("Garamond", 3, 20));
        lblTitulo.setHorizontalAlignment(0);
        dialogo.add(lblTitulo, "North");
        this.Campos(dialogo);
        dialogo.setVisible(true);
    }

    private void Campos(final JDialog dialogo) {
        this.comboBoxUsuarios = new JComboBox();
        this.mostrarUsuarios(this.comboBoxUsuarios);
        this.comboBoxLibros = new JComboBox();
        this.mostrarLibros(this.comboBoxLibros);
        JPanel jpCentro = new JPanel();
        jpCentro.setLayout(new GridLayout(5, 2, 10, 10));
        jpCentro.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jpCentro.setFont(new Font("Garamond", 0, 16));
        jpCentro.add(new JLabel("Usuario"));
        jpCentro.add(this.comboBoxUsuarios);
        jpCentro.add(new JLabel("Libro"));
        jpCentro.add(this.comboBoxLibros);
        JButton btnCrear = new JButton("Crear Préstamo");
        JButton btnCancelar = new JButton("Cancelar");
        btnCrear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (TablaPrestamos.this.validacionesNuevoPrestamo(TablaPrestamos.this.comboBoxUsuarios, TablaPrestamos.this.comboBoxLibros)) {
                    TablaPrestamos.this.crearNuevoPrestamo(TablaPrestamos.this.comboBoxUsuarios, TablaPrestamos.this.comboBoxLibros, dialogo);
                }

            }
        });
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose();
            }
        });
        JPanel botonesNuevoPrestamo = new JPanel(new FlowLayout());
        botonesNuevoPrestamo.add(btnCrear);
        botonesNuevoPrestamo.add(btnCancelar);
        jpCentro.add(botonesNuevoPrestamo);
        dialogo.add(jpCentro, "Center");
    }

    private void mostrarUsuarios(JComboBox<String> comboBoxUsuarios) {
        DefaultTableModel modeloUsuarios = this.tablaUsuarios.getModeloTabla();

        for(int i = 0; i < modeloUsuarios.getRowCount(); ++i) {
            String nombre = (String)modeloUsuarios.getValueAt(i, 1);
            String apellido = (String)modeloUsuarios.getValueAt(i, 2);
            comboBoxUsuarios.addItem(nombre + " " + apellido);
        }

    }

    private void mostrarLibros(JComboBox<String> comboBoxLibros) {
        DefaultTableModel modeloLibros = this.tablaLibros.getModeloTabla();

        for(int i = 0; i < modeloLibros.getRowCount(); ++i) {
            Boolean disponible = (Boolean)modeloLibros.getValueAt(i, 4);
            if (disponible) {
                String titulo = (String)modeloLibros.getValueAt(i, 1);
                comboBoxLibros.addItem(titulo);
            }
        }

    }

    private boolean validacionesNuevoPrestamo(JComboBox<String> comboBoxUsuarios, JComboBox<String> comboBoxLibros) {
        if (comboBoxUsuarios.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario para poder crear un préstamo.");
            return false;
        } else if (comboBoxLibros.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro o no contamos con libros disponibles por el momento.");
            return false;
        } else {
            return true;
        }
    }

    private void crearNuevoPrestamo(JComboBox<String> comboBoxUsuarios, JComboBox<String> comboBoxLibros, JDialog dialogo) {
        String usuarioSeleccionado = (String)comboBoxUsuarios.getSelectedItem();
        String libroSeleccionado = (String)comboBoxLibros.getSelectedItem();
        int dias = 15;
        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays((long)dias);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int idPrestamo = this.modeloTabla.getRowCount() + 1;
        this.modeloTabla.addRow(new Object[]{idPrestamo, usuarioSeleccionado, libroSeleccionado, fechaPrestamo.format(formato), fechaDevolucion.format(formato), "En Préstamo"});
        this.guardarPrestamosEnArchivo();
        this.libroNoDisponible(libroSeleccionado);
        JOptionPane.showMessageDialog(this, "Préstamo: " + idPrestamo + "\nUsuario: " + usuarioSeleccionado + "\nLibro: " + libroSeleccionado + "\nFecha Devolución: " + fechaDevolucion.format(formato) + "\nRegistrado exitosamente.");
        dialogo.dispose();
    }

    private void libroNoDisponible(String libroSeleccionado) {
        DefaultTableModel modeloLibros = this.tablaLibros.getModeloTabla();

        for(int i = 0; i < modeloLibros.getRowCount(); ++i) {
            String tituloLibro = (String)modeloLibros.getValueAt(i, 1);
            if (tituloLibro.equals(libroSeleccionado)) {
                this.tablaLibros.cambiarDisponibilidad(i, false);
                break;
            }
        }

    }

    private void dialogoDevolverPrestamo() {
        int filaSeleccionada = this.tablaPrestamos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un préstamo para devolver.");
        } else {
            String estado = (String)this.modeloTabla.getValueAt(filaSeleccionada, 5);
            if ("Devuelto".equals(estado)) {
                JOptionPane.showMessageDialog(this, "El registro del libro seleccionado ya ha sido devuelto");
            } else {
                String nombreUsuario = (String)this.modeloTabla.getValueAt(filaSeleccionada, 1);
                String nombreLibro = (String)this.modeloTabla.getValueAt(filaSeleccionada, 2);
                String fechaDevoluciontxt = (String)this.modeloTabla.getValueAt(filaSeleccionada, 4);
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaDevolucionSeleccionada = LocalDate.parse(fechaDevoluciontxt, formato);
                LocalDate fechaActual = LocalDate.now();
                long diasDiferencia = ChronoUnit.DAYS.between(fechaDevolucionSeleccionada, fechaActual);
                int multa = 0;
                if (diasDiferencia > 0L) {
                    multa = (int)(diasDiferencia * 5L);
                }

                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Confirma la devolución del libro " + nombreLibro + " de parte del usuario " + nombreUsuario + " con un monto total de multa de $" + multa + ".00?", "Confirmar devolución", 0);
                if (confirmacion == 0) {
                    this.modeloTabla.setValueAt("Devuelto", filaSeleccionada, 5);
                    this.guardarPrestamosEnArchivo();
                    JOptionPane.showMessageDialog(this, "El libro fue devuelto exitosamente y se encuentra disponible para nuevos préstamos");
                    this.libroDisponible(nombreLibro);
                } else {
                    JOptionPane.showMessageDialog(this, "Devolución cancelada");
                }

            }
        }
    }

    private void libroDisponible(String libroSeleccionado) {
        DefaultTableModel modeloLibros = this.tablaLibros.getModeloTabla();

        for(int i = 0; i < modeloLibros.getRowCount(); ++i) {
            String tituloLibro = (String)modeloLibros.getValueAt(i, 1);
            if (tituloLibro.equals(libroSeleccionado)) {
                this.tablaLibros.cambiarDisponibilidad(i, true);
                break;
            }
        }

    }

    private void cargarPrestamosDesdeArchivo() {
        for(String[] prestamo : this.manejadorArchivos.cargarDatos("prestamos")) {
            if (prestamo.length >= 6) {
                try {
                    int id = Integer.parseInt(prestamo[0]);
                    String usuario = prestamo[1];
                    String libro = prestamo[2];
                    String fechaPrestamo = prestamo[3];
                    String fechaDevolucion = prestamo[4];
                    String estado = prestamo[5];
                    this.modeloTabla.addRow(new Object[]{id, usuario, libro, fechaPrestamo, fechaDevolucion, estado});
                } catch (NumberFormatException e) {
                    System.err.println("Error al cargar prestamo: " + e.getMessage());
                }
            }
        }

    }

    private void guardarPrestamosEnArchivo() {
        List<String[]> prestamos = new ArrayList();

        for(int i = 0; i < this.modeloTabla.getRowCount(); ++i) {
            String[] prestamo = new String[]{this.modeloTabla.getValueAt(i, 0).toString(), this.modeloTabla.getValueAt(i, 1).toString(), this.modeloTabla.getValueAt(i, 2).toString(), this.modeloTabla.getValueAt(i, 3).toString(), this.modeloTabla.getValueAt(i, 4).toString(), this.modeloTabla.getValueAt(i, 5).toString()};
            prestamos.add(prestamo);
        }

        this.manejadorArchivos.guardarDatos("prestamos", prestamos);
    }
}
