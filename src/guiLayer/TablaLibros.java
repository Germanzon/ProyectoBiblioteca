

package guiLayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
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
import utilerias.ManejadorArchivos;

public class TablaLibros extends JPanel {
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();
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
        String[] columnas = new String[]{"ID", "Título", "Autor", "Año", "Disponibilidad"};
        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tablaLibros = new JTable(this.modeloTabla);
        JScrollPane scrollPane = new JScrollPane(this.tablaLibros);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panelTabla.add(scrollPane);
        this.cargarLibrosDesdeArchivo();
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
                int filaSeleccionada = TablaLibros.this.tablaLibros.getSelectedRow();
                if (filaSeleccionada != -1) {
                    int confirmacion = JOptionPane.showConfirmDialog(TablaLibros.this, "¿Estas seguro de que deseas eliminar este libro?", "Confirmar Eliminación", 0);
                    if (confirmacion == 0) {
                        TablaLibros.this.modeloTabla.removeRow(filaSeleccionada);
                        TablaLibros.this.guardarLibrosEnArchivo();
                    }
                } else {
                    JOptionPane.showMessageDialog(TablaLibros.this, "Por favor, selecciona un libro para eliminar.", "No hay selección", 2);
                }

            }
        });
    }

    public DefaultTableModel getModeloTabla() {
        return this.modeloTabla;
    }

    public void cambiarDisponibilidad(int fila, boolean disponible) {
        this.modeloTabla.setValueAt(disponible, fila, 4);
        this.guardarLibrosEnArchivo();
    }

    private void mostrarDialogoActualizarLibro() {
        int filaSeleccionada = this.tablaLibros.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un libro para actualizar.", "No hay selección", 2);
        } else {
            JDialog dialogo = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Actualizar Libro", true);
            dialogo.setLayout(new GridLayout(5, 2, 10, 10));
            ((JPanel)dialogo.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Obtener los datos actuales del libro seleccionado
            String tituloActual = this.modeloTabla.getValueAt(filaSeleccionada, 1).toString();
            String autorActual = this.modeloTabla.getValueAt(filaSeleccionada, 2).toString();
            String anioActual = this.modeloTabla.getValueAt(filaSeleccionada, 3).toString();

            // Crear los campos con los datos actuales
            JTextField txtTitulo = new JTextField(tituloActual);
            JTextField txtAutor = new JTextField(autorActual);
            JTextField txtAnio = new JTextField(anioActual);

            dialogo.add(new JLabel("Título:"));
            dialogo.add(txtTitulo);
            dialogo.add(new JLabel("Autor:"));
            dialogo.add(txtAutor);
            dialogo.add(new JLabel("Año:"));
            dialogo.add(txtAnio);
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
            JButton btnGuardar = new JButton("Guardar Cambios");
            JButton btnCancelar = new JButton("Cancelar");
            btnGuardar.addActionListener((e) -> {
                String nuevoTitulo = txtTitulo.getText().trim();
                String nuevoAutor = txtAutor.getText().trim();
                String nuevoAnioStr = txtAnio.getText().trim();
                String idSeleccionado = this.modeloTabla.getValueAt(filaSeleccionada, 0).toString();
                if (this.validaciones(idSeleccionado, nuevoTitulo, nuevoAutor, nuevoAnioStr)) {
                    this.modeloTabla.setValueAt(nuevoTitulo, filaSeleccionada, 1);
                    this.modeloTabla.setValueAt(nuevoAutor, filaSeleccionada, 2);
                    this.modeloTabla.setValueAt(nuevoAnioStr, filaSeleccionada, 3);
                    this.guardarLibrosEnArchivo();
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
        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtAnio = new JTextField();
        dialogo.add(new JLabel("ID:"));
        dialogo.add(txtId);
        dialogo.add(new JLabel("Título:"));
        dialogo.add(txtTitulo);
        dialogo.add(new JLabel("Autor:"));
        dialogo.add(txtAutor);
        dialogo.add(new JLabel("Año:"));
        dialogo.add(txtAnio);
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
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCerrar = new JButton("Cerrar");
        btnGuardar.addActionListener((e) -> {
            String idString = txtId.getText();
            String titulo = txtTitulo.getText();
            String autor = txtAutor.getText();
            String anioString = txtAnio.getText();
            if (this.validaciones(idString, titulo, autor, anioString)) {
                this.modeloTabla.addRow(new Object[]{idString, titulo, autor, anioString, true});
                this.guardarLibrosEnArchivo();
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

    private boolean validaciones(String id, String titulo, String autor, String anio) {
        if (!id.isEmpty() && !titulo.isEmpty() && !autor.isEmpty() && !anio.isEmpty()) {
            try {
                int idNum = Integer.parseInt(id);
                if (idNum <= 0) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número positivo.", "Error de Validación", 0);
                    return false;
                }
            } catch (NumberFormatException var7) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error de Formato", 0);
                return false;
            }

            try {
                int anioNum = Integer.parseInt(anio);
                if (anioNum <= 0) {
                    JOptionPane.showMessageDialog(this, "El año debe ser un número positivo.", "Error de Validación", 0);
                    return false;
                } else {
                    return true;
                }
            } catch (NumberFormatException var6) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número válido.", "Error de Formato", 0);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de Validación", 0);
            return false;
        }
    }

    private void cargarLibrosDesdeArchivo() {
        List<String[]> libros = this.manejadorArchivos.cargarDatos("libros");
        if (libros.isEmpty()) {
            this.modeloTabla.addRow(new Object[]{"1", "Cien años de soledad", "Gabriel García Márquez", "1967", true});
            this.modeloTabla.addRow(new Object[]{"2", "El principito", "Antoine de Saint-Exupéry", "1943", true});
            this.modeloTabla.addRow(new Object[]{"3", "Don Quijote de la Mancha", "Miguel de Cervantes", "1605", true});
            this.guardarLibrosEnArchivo();
        } else {
            for(String[] libro : libros) {
                if (libro.length >= 5) {
                    try {
                        String id = libro[0];
                        String titulo = libro[1];
                        String autor = libro[2];
                        String anio = libro[3];
                        boolean disponible = Boolean.parseBoolean(libro[4]);
                        this.modeloTabla.addRow(new Object[]{id, titulo, autor, anio, disponible});
                    } catch (Exception e) {
                        System.err.println("Error al cargar libro: " + e.getMessage());
                    }
                }
            }
        }

    }

    private void guardarLibrosEnArchivo() {
        List<String[]> libros = new ArrayList();

        for(int i = 0; i < this.modeloTabla.getRowCount(); ++i) {
            String[] libro = new String[]{this.modeloTabla.getValueAt(i, 0).toString(), this.modeloTabla.getValueAt(i, 1).toString(), this.modeloTabla.getValueAt(i, 2).toString(), this.modeloTabla.getValueAt(i, 3).toString(), this.modeloTabla.getValueAt(i, 4).toString()};
            libros.add(libro);
        }

        this.manejadorArchivos.guardarDatos("libros", libros);
    }
}
