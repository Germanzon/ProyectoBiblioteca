
package guiLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

import dataLayer.*;

public class TablaPrestamos extends JPanel {
    private TablaLibros tablaLibros;
    private TablaUsuarios tablaUsuarios;

    DefaultTableModel modeloTabla;
    JTable tablaPrestamos;

    JComboBox<String> comboBoxUsuarios = new JComboBox();
    JComboBox<String> comboBoxLibros = new JComboBox();

    public TablaPrestamos(TablaLibros tablaLibros, TablaUsuarios tablaUsuarios) {
        this.tablaLibros = tablaLibros;
        this.tablaUsuarios = tablaUsuarios;
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
        scrollPane.setPreferredSize(new Dimension(900, 400));
        panelTabla.add(scrollPane);

        //Se cargan los datos de la tabla
        this.cargarPrestamosDesdeDb();
        this.add(panelTabla);

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
        panelTabla.add(botonesPrestamos);
    }

    private void dialogoNuevoPrestamo() {
        JDialog dialogo = new JDialog((Frame)null, "Nuevo Préstamo", true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Nuevo Préstamo");
        lblTitulo.setFont(new Font("Garamond", 3, 20));
        lblTitulo.setHorizontalAlignment(0);
        dialogo.add(lblTitulo);
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
            int disponibilidad = (Integer)modeloLibros.getValueAt(i, 4);
            if (disponibilidad > 0) {
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

        this.InsercionDatos(usuarioSeleccionado, libroSeleccionado );
        this.actualizarDisponibilidad(libroSeleccionado, -1);

        JOptionPane.showMessageDialog(this, "Préstamo registrado exitosamente." + "\nUsuario: " + usuarioSeleccionado + "\nLibro: " + libroSeleccionado);
        dialogo.dispose();
    }

    //Este metodo es llamado al crear o devolver un préstamo, y el valor de modificación será 1 o -1 dependiendo la acción
    private void actualizarDisponibilidad(String libroSeleccionado, int modificacion){
        DefaultTableModel modeloLibros = this.tablaLibros.getModeloTabla();

        for(int i = 0; i < modeloLibros.getRowCount(); ++i) {
            String tituloLibro = (String)modeloLibros.getValueAt(i, 1);
            if (tituloLibro.equals(libroSeleccionado)) {
                int disponibilidadActual = (Integer)modeloLibros.getValueAt(i, 4);
                int nuevaDisponibilidad = disponibilidadActual + modificacion;
                if (nuevaDisponibilidad < 0) nuevaDisponibilidad = 0;
                this.tablaLibros.cambiarDisponibilidad(i, nuevaDisponibilidad);
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

                DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fechaDevolucionSeleccionada = LocalDate.parse(fechaDevoluciontxt, formato);
                LocalDate fechaActual = LocalDate.now();
                long diasDiferencia = ChronoUnit.DAYS.between(fechaDevolucionSeleccionada, fechaActual);
                int multa = 0;
                if (diasDiferencia > 0L) {
                    multa = (int)(diasDiferencia * 5L);
                }

                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Confirma la devolución del libro " + nombreLibro + " de parte del usuario " + nombreUsuario + " con un monto total de multa de $" + multa + ".00?", "Confirmar devolución", 0);
                if (confirmacion == 0) {
                    this.ActualizarDatos();
                    this.actualizarDisponibilidad(nombreLibro, 1);
                    JOptionPane.showMessageDialog(this, "El libro fue devuelto exitosamente y se encuentra disponible para nuevos préstamos");
                } else {
                    JOptionPane.showMessageDialog(this, "Devolución cancelada");
                }

            }
        }
    }

    private void cargarPrestamosDesdeDb() {
        this.modeloTabla.setRowCount(0);

        try {
            Connection conn = DBConexion.GetConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select p.ID_Prestamo, u.Nombre as Usuario, l.titulo as Titulo, p.Fecha_Prestamo, p.Fecha_Devolucion,p.Estado FROM Prestamos p\n" +
                    "join Usuarios u on p.ID_Usuario = u.ID_Usuario " +
                    "join Libros l on p.ID_Libro = l.ID_Libros");

            // Cargar los datos en la tabla
            while (rs.next()) {
                int id_Prestamo = rs.getInt("ID_Prestamo");
                String nombre = rs.getString("Usuario");
                String titulo = rs.getString("Titulo");
                String fechaPrestamo = rs.getString("Fecha_Prestamo");
                String fechaDevolucion = rs.getString("Fecha_Devolucion");
                String estado = rs.getString("Estado");

                this.modeloTabla.addRow(new Object[]{id_Prestamo, nombre, titulo, fechaPrestamo, fechaDevolucion, estado});
            }

            // Cerrar los recursos
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar prestamos desde la base de datos: " + ex.getMessage(),
                    "Error de Conexion",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void InsercionDatos (String nombreCompleto, String titulo){
        try {
            //Separar nombre y apellido
            String[] partes = nombreCompleto.split(" ", 2);
            String nombre = partes[0];
            String apellido = partes[1];

            // Usar métodos DAO de Usuarios y Libros para obtener los objetos
            Usuarios usuario = DAOUsuarios.BuscarUsuarioPorNombre(nombre, apellido);
            Libros libro = DAOLibros.BuscarLibroPorTitulo(titulo);

            // Insertar los datos en la base de datos
            DAOPrestamos.Insertar(usuario, libro);
            this.cargarPrestamosDesdeDb();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar prestamo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void ActualizarDatos (){
        try {
            int filaSeleccionada = this.tablaPrestamos.getSelectedRow();
            if (filaSeleccionada != -1) {
                // Obtener el ID del préstamo de la fila seleccionada
                int idPrestamo = (int) this.modeloTabla.getValueAt(filaSeleccionada, 0);

                //Crear objeto Prestamos
                Prestamos prestamo = new Prestamos();
                prestamo.setId_Prestamo(idPrestamo);
                prestamo.setEstado("Devuelto");

                //Actualizar la base de datos
                DAOPrestamos.Devolver(prestamo);
                this.cargarPrestamosDesdeDb();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar prestamo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


}
