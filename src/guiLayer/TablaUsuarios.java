
package guiLayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import javax.swing.table.DefaultTableModel;
import utilerias.ManejadorArchivos;

public class TablaUsuarios extends JPanel {
    DefaultTableModel modeloTabla;
    JTable tablaUsuarios;
    private ManejadorArchivos manejadorArchivos = new ManejadorArchivos();

    public TablaUsuarios() {
        this.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Usuarios");
        lblTitulo.setFont(new Font("Garamond", 3, 30));
        lblTitulo.setHorizontalAlignment(0);
        this.add(lblTitulo, "North");
        JPanel panelTabla = new JPanel();
        String[] columnas = new String[]{"Id", "Nombre", "Apellido", "Email", "Teléfono"};
        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tablaUsuarios = new JTable(this.modeloTabla);
        JScrollPane scrollPane = new JScrollPane(this.tablaUsuarios);
        scrollPane.setPreferredSize(new Dimension(600, 270));
        panelTabla.add(scrollPane);
        this.cargarUsuariosDesdeArchivo();
        JPanel Botones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");
        JButton btnEditar = new JButton("Editar Usuario");
        Botones.add(btnAgregar);
        Botones.add(btnEliminar);
        Botones.add(btnEditar);
        panelTabla.add(Botones, "South");
        this.add(panelTabla, "Center");
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaUsuarios.this.VentanaAgregarUsuario();
            }
        });
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaUsuarios.this.Eliminar();
            }
        });
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TablaUsuarios.this.VentanaEditarUsuario();
            }
        });
    }

    public DefaultTableModel getModeloTabla() {
        return this.modeloTabla;
    }

    public void VentanaAgregarUsuario() {
        JDialog dialog = new JDialog((Frame)null, "Agregar Usuario", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Registro de usuarios");
        lblTitulo.setFont(new Font("Garamond", 3, 20));
        lblTitulo.setHorizontalAlignment(0);
        dialog.add(lblTitulo, "North");
        this.Campos(dialog);
        dialog.setVisible(true);
    }

    public void VentanaEditarUsuario() {
        int filaSeleccionada = this.tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario para editar.");
        } else {
            JDialog dialog = new JDialog((Frame)null, "Editar Usuario", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            JLabel lblTitulo = new JLabel("Editar Usuario");
            lblTitulo.setFont(new Font("Garamond", 3, 20));
            lblTitulo.setHorizontalAlignment(0);
            dialog.add(lblTitulo, "North");
            this.Editar(dialog);
            dialog.setVisible(true);
        }
    }

    public void Eliminar() {
        int filaSeleccionada = this.tablaUsuarios.getSelectedRow();
        String nombreUsuario = (String)this.modeloTabla.getValueAt(filaSeleccionada, 1);
        String apellidoUsuario = (String)this.modeloTabla.getValueAt(filaSeleccionada, 2);
        if (filaSeleccionada != -1) {
            int confirmacion = JOptionPane.showConfirmDialog(this, "Esta seguro de que desea eliminar este usuario: " + nombreUsuario + " " + apellidoUsuario + "?", "Confirmar eliminacion", 0, 3);
            if (confirmacion == 0) {
                this.modeloTabla.removeRow(filaSeleccionada);
                this.guardarUsuariosEnArchivo();
                JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para eliminar.");
        }

    }

    public void Editar(final JDialog dialog) {
        final int filaSeleccionada = this.tablaUsuarios.getSelectedRow();
        if (filaSeleccionada != -1) {
            final int id = (Integer)this.modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreActual = (String)this.modeloTabla.getValueAt(filaSeleccionada, 1);
            String apellidoActual = (String)this.modeloTabla.getValueAt(filaSeleccionada, 2);
            String emailActual = (String)this.modeloTabla.getValueAt(filaSeleccionada, 3);
            String telefonoActual = (String)this.modeloTabla.getValueAt(filaSeleccionada, 4);
            final JTextField txtNombre = new JTextField(nombreActual, 20);
            final JTextField txtApellido = new JTextField(apellidoActual, 20);
            final JTextField txtEmail = new JTextField(emailActual, 20);
            final JTextField txtTelefono = new JTextField(telefonoActual, 20);
            JButton btnGuardar = new JButton("Actualizar");
            JPanel jpCentro = new JPanel();
            jpCentro.setLayout(new GridLayout(5, 2, 10, 10));
            jpCentro.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
            jpCentro.setFont(new Font("Garamond", 0, 16));
            jpCentro.add(new Label("Nombre: "));
            jpCentro.add(txtNombre);
            jpCentro.add(new Label("Apellido: "));
            jpCentro.add(txtApellido);
            jpCentro.add(new Label("Email: "));
            jpCentro.add(txtEmail);
            jpCentro.add(new Label("Teléfono: "));
            jpCentro.add(txtTelefono);
            jpCentro.add(btnGuardar);
            dialog.add(jpCentro, "Center");
            txtNombre.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char caracter = e.getKeyChar();
                    if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b') {
                        e.consume();
                    }

                }
            });
            txtApellido.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char caracter = e.getKeyChar();
                    if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b') {
                        e.consume();
                    }

                }
            });
            txtTelefono.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char caracter = e.getKeyChar();
                    if (!Character.isDigit(caracter) && caracter != '\b') {
                        e.consume();
                    }

                    if (txtTelefono.getText().length() >= 10) {
                        e.consume();
                    }

                }
            });
            txtEmail.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    String email = txtEmail.getText();
                    if (email.contains("@") && email.contains(".")) {
                        txtEmail.setBackground(Color.WHITE);
                    } else {
                        txtEmail.setBackground(Color.RED);
                        JOptionPane.showMessageDialog((Component)null, "Email invalido, debe conterner @ y .");
                    }

                }
            });
            btnGuardar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String nombre = txtNombre.getText();
                    String apellido = txtApellido.getText();
                    String email = txtEmail.getText();
                    String telefono = txtTelefono.getText();
                    if (TablaUsuarios.this.Validaciones(dialog, txtNombre, txtApellido, txtEmail, txtTelefono, nombre, apellido, email, telefono, id)) {
                        TablaUsuarios.this.EditarDatos(filaSeleccionada, nombre, apellido, email, telefono, id);
                        dialog.dispose();
                    }

                }
            });
        }

    }

    public void Campos(final JDialog dialog) {
        final int id = this.modeloTabla.getRowCount() + 1;
        final JTextField txtNombre = new JTextField(20);
        final JTextField txtApellido = new JTextField(20);
        final JTextField txtEmail = new JTextField(20);
        final JTextField txtTelefono = new JTextField(20);
        JButton btnGuardar = new JButton("Registrar");
        JPanel jpCentro = new JPanel();
        jpCentro.setLayout(new GridLayout(5, 2, 10, 10));
        jpCentro.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        jpCentro.setFont(new Font("Garamond", 0, 16));
        jpCentro.add(new Label("Nombre: "));
        jpCentro.add(txtNombre);
        jpCentro.add(new Label("Apellido: "));
        jpCentro.add(txtApellido);
        jpCentro.add(new Label("Email: "));
        jpCentro.add(txtEmail);
        jpCentro.add(new Label("Teléfono: "));
        jpCentro.add(txtTelefono);
        jpCentro.add(btnGuardar);
        dialog.add(jpCentro, "Center");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                String email = txtEmail.getText();
                String telefono = txtTelefono.getText();
                if (TablaUsuarios.this.Validaciones(dialog, txtNombre, txtApellido, txtEmail, txtTelefono, nombre, apellido, email, telefono, id)) {
                    TablaUsuarios.this.InsercionDatos(TablaUsuarios.this.modeloTabla, nombre, apellido, email, telefono, id);
                    dialog.dispose();
                }

            }
        });
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b') {
                    e.consume();
                }

            }
        });
        txtApellido.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (!Character.isLetter(caracter) && !Character.isSpaceChar(caracter) && caracter != '\b') {
                    e.consume();
                }

            }
        });
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                if (!Character.isDigit(caracter) && caracter != '\b') {
                    e.consume();
                }

                if (txtTelefono.getText().length() >= 10) {
                    e.consume();
                }

            }
        });
        txtEmail.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String email = txtEmail.getText();
                if (email.contains("@") && email.contains(".")) {
                    txtEmail.setBackground(Color.WHITE);
                } else {
                    txtEmail.setBackground(Color.RED);
                    JOptionPane.showMessageDialog((Component)null, "Email invalido, debe conterner @ y .");
                }

            }
        });
    }

    public boolean Validaciones(JDialog dialog, JTextField txtNombre, JTextField txtApellido, JTextField txtEmail, JTextField txtTelefono, String nombre, String apellido, String email, String telefono, int id) {
        if (!txtNombre.getText().isEmpty() && !txtApellido.getText().isEmpty() && !txtEmail.getText().isEmpty() && !txtTelefono.getText().isEmpty()) {
            if (txtTelefono.getText().length() == 10 && txtTelefono.getText().matches("[0-9]+")) {
                JOptionPane.showMessageDialog(dialog, "Id: " + id + "\nUsuario: " + nombre + " " + apellido + "\nEmail: " + email + "\nTelefono: " + telefono + "\nregistrado exitosamente.");
                txtNombre.setText("");
                txtApellido.setText("");
                txtEmail.setText("");
                txtTelefono.setText("");
                return true;
            } else {
                JOptionPane.showMessageDialog(dialog, "El teléfono debe tener exactamente 10 dígitos");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Favor de no dejar campos vacíos");
            return false;
        }
    }

    public void InsercionDatos(DefaultTableModel modeloTabla, String nombre, String apellido, String email, String telefono, int id) {
        modeloTabla.addRow(new Object[]{id, nombre, apellido, email, telefono});
        this.guardarUsuariosEnArchivo();
    }

    public void EditarDatos(int filaSeleccionada, String nombre, String apellido, String email, String telefono, int id) {
        this.modeloTabla.setValueAt(id, filaSeleccionada, 0);
        this.modeloTabla.setValueAt(nombre, filaSeleccionada, 1);
        this.modeloTabla.setValueAt(apellido, filaSeleccionada, 2);
        this.modeloTabla.setValueAt(email, filaSeleccionada, 3);
        this.modeloTabla.setValueAt(telefono, filaSeleccionada, 4);
        this.guardarUsuariosEnArchivo();
    }

    private void cargarUsuariosDesdeArchivo() {
        List<String[]> usuarios = this.manejadorArchivos.cargarDatos("usuarios");
        if (usuarios.isEmpty()) {
            this.modeloTabla.addRow(new Object[]{1, "Juan", "Pérez", "juan.perez@example.com", "6675902346"});
            this.modeloTabla.addRow(new Object[]{2, "Jesus", "Molina", "jmolina@gmail.com", "6672348902"});
            this.modeloTabla.addRow(new Object[]{3, "Emiliano", "Arellanes", "emln@hotmail.com", "6673458101"});
            this.guardarUsuariosEnArchivo();
        } else {
            for(String[] usuario : usuarios) {
                if (usuario.length >= 5) {
                    try {
                        int id = Integer.parseInt(usuario[0]);
                        this.modeloTabla.addRow(new Object[]{id, usuario[1], usuario[2], usuario[3], usuario[4]});
                    } catch (NumberFormatException var5) {
                        System.err.println("Error al parsear ID del usuario: " + usuario[0]);
                    }
                }
            }
        }

    }

    private void guardarUsuariosEnArchivo() {
        List<String[]> usuarios = new ArrayList();

        for(int i = 0; i < this.modeloTabla.getRowCount(); ++i) {
            String[] usuario = new String[]{this.modeloTabla.getValueAt(i, 0).toString(), this.modeloTabla.getValueAt(i, 1).toString(), this.modeloTabla.getValueAt(i, 2).toString(), this.modeloTabla.getValueAt(i, 3).toString(), this.modeloTabla.getValueAt(i, 4).toString()};
            usuarios.add(usuario);
        }

        this.manejadorArchivos.guardarDatos("usuarios", usuarios);
    }
}
