package guiLayer;

import dataLayer.DBConexion;
import dataLayer.DAOLibros;
import dataLayer.Libros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class TablaLibros extends JPanel {
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar;
    private JButton btnActualizar;
    private JButton btnEliminar;

    public TablaLibros() {
        this.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Libros");
        lblTitulo.setFont(new Font("Garamond", 3, 30));
        lblTitulo.setHorizontalAlignment(0);
        this.add(lblTitulo, "North");

        JPanel panelBotonesPestaniaLibro = new JPanel();
        this.btnAgregar = new JButton("Agregar Libro");
        this.btnActualizar = new JButton("Actualizar Libro");
        this.btnEliminar = new JButton("Eliminar Libro");

        panelBotonesPestaniaLibro.add(this.btnAgregar);
        panelBotonesPestaniaLibro.add(this.btnActualizar);
        panelBotonesPestaniaLibro.add(this.btnEliminar);

        JPanel panelTabla = new JPanel();
        String[] columnas = new String[]{"ID", "Título", "Autor", "Año", "Copias Disponibles"};
        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.tablaLibros = new JTable(this.modeloTabla);
        JScrollPane scrollPane = new JScrollPane(this.tablaLibros);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panelTabla.add(scrollPane);

        // Se cargan los libros desde la base de datos
        this.cargarLibrosDesdeDb();
        this.add(panelTabla, "Center");
        panelTabla.add(panelBotonesPestaniaLibro, "South");

        this.btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaLibros.this.mostrarDialogoAgregarLibro();
            }
        });

        this.btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaLibros.this.mostrarDialogoActualizarLibro();
            }
        });

        this.btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaLibros.this.eliminarLibro();
            }
        });
    }

    public DefaultTableModel getModeloTabla() {
        return this.modeloTabla;
    }

    public void cambiarDisponibilidad(int fila, int cantidad) {
        this.modeloTabla.setValueAt(cantidad, fila, 4);
        // Actualizar en la base de datos
        try {
            int idLibro = Integer.parseInt(this.modeloTabla.getValueAt(fila, 0).toString());
            String titulo = this.modeloTabla.getValueAt(fila, 1).toString();
            String autor = this.modeloTabla.getValueAt(fila, 2).toString();
            int anio = Integer.parseInt(this.modeloTabla.getValueAt(fila, 3).toString());

            Libros libro = new Libros();
            libro.setId_Libro(idLibro);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setAnio_pubicacion(anio);
            libro.setDisponibilidad(cantidad);

            DAOLibros.Actualizar(libro);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar disponibilidad: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void mostrarDialogoActualizarLibro() {
        int filaSeleccionada = this.tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro para actualizar.", "No hay selección", 2);
        } else {
            JDialog dialogo = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Actualizar Libro", true);
            dialogo.setLayout(new GridLayout(6, 2, 10, 10));
            ((JPanel)dialogo.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Obtener los datos actuales del libro seleccionado
            String tituloActual = this.modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            String autorActual = this.modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            String anioActual = this.modeloTabla.getValueAt(filaSeleccionada, 3).toString();
            String disponibilidadActual = this.modeloTabla.getValueAt(filaSeleccionada, 4).toString();

            // Crear los campos con los datos actuales
            JTextField txtTitulo = new JTextField(tituloActual);
            JTextField txtAutor = new JTextField(autorActual);
            JTextField txtAnio = new JTextField(anioActual);
            JTextField txtDisponibilidad = new JTextField(disponibilidadActual);

            dialogo.add(new JLabel("Título:"));
            dialogo.add(txtTitulo);
            dialogo.add(new JLabel("Autor:"));
            dialogo.add(txtAutor);
            dialogo.add(new JLabel("Año:"));
            dialogo.add(txtAnio);
            dialogo.add(new JLabel("Copias Disponibles:"));
            dialogo.add(txtDisponibilidad);

            txtAutor.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char caracter = e.getKeyChar();
                    if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b') {
                        e.consume();
                    }
                }
            });

            txtAnio.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char caracter = e.getKeyChar();
                    JTextField campo = (JTextField)e.getSource();
                    if (!Character.isDigit(caracter) && caracter != '\b') {
                        e.consume();
                    }
                    if (campo.getText().length() >= 4 && caracter != '\b') {
                        e.consume();
                    }
                }
            });

            txtDisponibilidad.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char caracter = e.getKeyChar();
                    if (!Character.isDigit(caracter) && caracter != '\b') {
                        e.consume();
                    }
                }
            });

            JButton btnGuardar = new JButton("Guardar Cambios");
            JButton btnCancelar = new JButton("Cancelar");

            btnGuardar.addActionListener((e) -> {
                String nuevoTitulo = txtTitulo.getText().trim();
                String nuevoAutor = txtAutor.getText().trim();
                String nuevoAnioStr = txtAnio.getText().trim();
                String nuevaDisponibilidadStr = txtDisponibilidad.getText().trim();
                int idSeleccionado = Integer.parseInt(this.modeloTabla.getValueAt(filaSeleccionada, 0).toString());

                if (this.validaciones(nuevoTitulo, nuevoAutor, nuevoAnioStr, nuevaDisponibilidadStr)) {
                    this.actualizarLibro(idSeleccionado, nuevoTitulo, nuevoAutor, nuevoAnioStr, nuevaDisponibilidadStr);
                    dialogo.dispose();
                }
            });

            btnCancelar.addActionListener((e) -> dialogo.dispose());

            dialogo.add(btnGuardar);
            dialogo.add(btnCancelar);
            dialogo.pack();
            dialogo.setLocationRelativeTo(this);
            dialogo.setVisible(true);
        }
    }

    private void mostrarDialogoAgregarLibro() {
        JDialog dialogo = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Agregar Nuevo Libro", true);
        dialogo.setLayout(new GridLayout(5, 2, 10, 10));
        ((JPanel)dialogo.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtAnio = new JTextField();
        JTextField txtDisponibilidad = new JTextField();

        dialogo.add(new JLabel("Título:"));
        dialogo.add(txtTitulo);
        dialogo.add(new JLabel("Autor:"));
        dialogo.add(txtAutor);
        dialogo.add(new JLabel("Año:"));
        dialogo.add(txtAnio);
        dialogo.add(new JLabel("Copias Disponibles:"));
        dialogo.add(txtDisponibilidad);

        txtAutor.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b') {
                    e.consume();
                }
            }
        });

        txtAnio.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                JTextField campo = (JTextField)e.getSource();
                if (!Character.isDigit(caracter) && caracter != '\b') {
                    e.consume();
                }
                if (campo.getText().length() >= 4 && caracter != '\b') {
                    e.consume();
                }
            }
        });

        txtDisponibilidad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (!Character.isDigit(caracter) && caracter != '\b') {
                    e.consume();
                }
            }
        });

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCerrar = new JButton("Cerrar");

        btnGuardar.addActionListener((e) -> {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String anioString = txtAnio.getText().trim();
            String disponibilidadString = txtDisponibilidad.getText().trim();

            if (this.validaciones(titulo, autor, anioString, disponibilidadString)) {
                this.insertarLibro(titulo, autor, anioString, disponibilidadString);
                dialogo.dispose();
            }
        });

        btnCerrar.addActionListener((e) -> dialogo.dispose());

        dialogo.add(btnGuardar);
        dialogo.add(btnCerrar);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private boolean validaciones(String titulo, String autor, String anio, String disponibilidad) {
        if (titulo.isEmpty() || autor.isEmpty() || anio.isEmpty() || disponibilidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", 0);
            return false;
        }

        try {
            int anioNum = Integer.parseInt(anio);
            if (anioNum <= 0) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número positivo.", "Error de Validación", 0);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número válido.", "Error de Formato", 0);
            return false;
        }

        try {
            int disponibilidadNum = Integer.parseInt(disponibilidad);
            if (disponibilidadNum < 0) {
                JOptionPane.showMessageDialog(this, "Las copias disponibles deben ser un número positivo o cero.", "Error de Validación", 0);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Las copias disponibles deben ser un número válido.", "Error de Formato", 0);
            return false;
        }

        return true;
    }

    private void cargarLibrosDesdeDb() {
        this.modeloTabla.setRowCount(0);

        try {
            Connection conn = DBConexion.GetConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID_Libros, titulo, autor, anio_publicacion, disponibilidad FROM Libros");

            while (rs.next()) {
                int id = rs.getInt("ID_Libros");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int anio = rs.getInt("anio_publicacion");
                int disponibilidad = rs.getInt("disponibilidad");

                this.modeloTabla.addRow(new Object[]{id, titulo, autor, anio, disponibilidad});
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar libros desde la base de datos: " + ex.getMessage(),
                    "Error de Conexion",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void insertarLibro(String titulo, String autor, String anio, String disponibilidad) {
        try {
            Libros libro = new Libros();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setAnio_pubicacion(Integer.parseInt(anio));
            libro.setDisponibilidad(Integer.parseInt(disponibilidad));

            DAOLibros.Insertar(libro);
            this.cargarLibrosDesdeDb();
            JOptionPane.showMessageDialog(this, "Libro agregado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar libro: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarLibro(int id, String titulo, String autor, String anio, String disponibilidad) {
        try {
            Libros libro = new Libros();
            libro.setId_Libro(id);
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setAnio_pubicacion(Integer.parseInt(anio));
            libro.setDisponibilidad(Integer.parseInt(disponibilidad));

            DAOLibros.Actualizar(libro);
            this.cargarLibrosDesdeDb();
            JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar libro: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = this.tablaLibros.getSelectedRow();
        if (filaSeleccionada != -1) {
            int id = Integer.parseInt(this.modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            String tituloLibro = this.modeloTabla.getValueAt(filaSeleccionada, 1).toString();

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que deseas eliminar el libro: " + tituloLibro + "?",
                    "Confirmar Eliminación", 0);

            if (confirmacion == 0) {
                try {
                    DAOLibros.Eliminar(id);
                    this.cargarLibrosDesdeDb();
                    JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar libro: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un libro para eliminar.", "No hay selección", 2);
        }
    }
}