package guiLayer;

import dataLayer.DAOUsuarios;
import dataLayer.DBConexion;
import dataLayer.Usuarios;

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
import java.sql.*;
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

public class TablaUsuarios extends JPanel {
    DefaultTableModel modeloTabla;
    JTable tablaUsuarios;

    public TablaUsuarios() {
        this.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("Usuarios");
        lblTitulo.setFont(new Font("Garamond", 3, 30));
        lblTitulo.setHorizontalAlignment(0);
        this.add(lblTitulo, "North");
        JPanel panelTabla = new JPanel();
        String[] columnas = new String[]{"Id", "Nombre", "Apellido", "Correo", "Teléfono"};

        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.tablaUsuarios = new JTable(this.modeloTabla);
        JScrollPane scrollPane = new JScrollPane(this.tablaUsuarios);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        panelTabla.add(scrollPane);
        this.cargarUsuariosDesdeDb();
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
        JDialog dialog = new JDialog((Frame) null, "Agregar Usuario", true);
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
            JDialog dialog = new JDialog((Frame) null, "Editar Usuario", true);
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

        if (filaSeleccionada != -1) {
            int id = (Integer) this.modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombreUsuario = (String) this.modeloTabla.getValueAt(filaSeleccionada, 1);
            String apellidoUsuario = (String) this.modeloTabla.getValueAt(filaSeleccionada, 2);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de que desea eliminar al usuario: " + nombreUsuario + " " + apellidoUsuario + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    // Eliminar el usuario de la base de datos
                    DAOUsuarios.Eliminar(id);

                    // Recargar la tabla
                    this.cargarUsuariosDesdeDb();

                    JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar usuario: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila para eliminar.");
            }
        }
    }

        public void Editar ( final JDialog dialog){
            final int filaSeleccionada = this.tablaUsuarios.getSelectedRow();
            if (filaSeleccionada != -1) {
                final int id = (Integer) this.modeloTabla.getValueAt(filaSeleccionada, 0);
                String nombreActual = (String) this.modeloTabla.getValueAt(filaSeleccionada, 1);
                String apellidoActual = (String) this.modeloTabla.getValueAt(filaSeleccionada, 2);
                String correoActual = (String) this.modeloTabla.getValueAt(filaSeleccionada, 3);
                String telefonoActual = (String) this.modeloTabla.getValueAt(filaSeleccionada, 4);

                final JTextField txtNombre = new JTextField(nombreActual, 20);
                final JTextField txtApellido = new JTextField(apellidoActual, 20);
                final JTextField txtCorreo = new JTextField(correoActual, 20);
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
                jpCentro.add(new Label("Correo: "));
                jpCentro.add(txtCorreo);
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
                txtCorreo.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        String correo = txtCorreo.getText();
                        if (correo.contains("@") && correo.contains(".")) {
                            txtCorreo.setBackground(Color.WHITE);
                        } else {
                            txtCorreo.setBackground(Color.RED);
                            JOptionPane.showMessageDialog((Component) null, "Correo invalido, debe conterner @ y .");
                        }

                    }
                });
                btnGuardar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String nombre = txtNombre.getText();
                        String apellido = txtApellido.getText();
                        String correo = txtCorreo.getText();
                        String telefono = txtTelefono.getText();
                        if (TablaUsuarios.this.Validaciones(dialog, txtNombre, txtApellido, txtCorreo, txtTelefono, nombre, apellido, correo, telefono, id)) {
                            TablaUsuarios.this.EditarDatos(nombre, apellido, correo, telefono, id);
                            dialog.dispose();
                        }

                    }
                });
            }

        }

        public void Campos ( final JDialog dialog){
            final int id = this.modeloTabla.getRowCount() + 1;
            final JTextField txtNombre = new JTextField(20);
            final JTextField txtApellido = new JTextField(20);
            final JTextField txtCorreo = new JTextField(20);
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
            jpCentro.add(new Label("Correo: "));
            jpCentro.add(txtCorreo);
            jpCentro.add(new Label("Teléfono: "));
            jpCentro.add(txtTelefono);
            jpCentro.add(btnGuardar);
            dialog.add(jpCentro, "Center");

            btnGuardar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String nombre = txtNombre.getText();
                    String apellido = txtApellido.getText();
                    String correo = txtCorreo.getText();
                    String telefono = txtTelefono.getText();

                    if (TablaUsuarios.this.Validaciones(dialog, txtNombre, txtApellido, txtCorreo, txtTelefono, nombre, apellido, correo, telefono, 0)) {
                        TablaUsuarios.this.InsercionDatos(nombre, apellido, correo, telefono);
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
            txtCorreo.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    String correo = txtCorreo.getText();
                    if (correo.contains("@") && correo.contains(".")) {
                        txtCorreo.setBackground(Color.WHITE);
                    } else {
                        txtCorreo.setBackground(Color.RED);
                        JOptionPane.showMessageDialog((Component) null, "Correo invalido, debe conterner @ y .");
                    }

                }
            });
        }

        public boolean Validaciones (JDialog dialog, JTextField txtNombre, JTextField txtApellido, JTextField
        txtCorreo, JTextField txtTelefono, String nombre, String apellido, String correo, String telefono,int id){
            if (!txtNombre.getText().isEmpty() && !txtApellido.getText().isEmpty() && !txtCorreo.getText().isEmpty() && !txtTelefono.getText().isEmpty()) {
                if (txtTelefono.getText().length() == 10 && txtTelefono.getText().matches("[0-9]+")) {
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

        public void InsercionDatos (String nombre, String apellido, String correo, String telefono){
            try {
                //Crear objeto Usuario
                Usuarios usuario = new Usuarios();
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setCorreo(correo);
                usuario.setTelefono(telefono);

                // Insertar los datos en la base de datos
                DAOUsuarios.Insertar(usuario);

                this.cargarUsuariosDesdeDb();

                JOptionPane.showMessageDialog(this,"\nUsuario: " + nombre + " " + apellido + "\nCorreo: " + correo + "\nTelefono: " + telefono + "\nregistrado exitosamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar usuario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        public void EditarDatos (String nombre, String apellido, String correo, String telefono,
        int id){
            try {
                // Crear objeto Usuario con los datos actualizados
                Usuarios usuario = new Usuarios();
                usuario.setId_Usuario(id);
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setCorreo(correo);
                usuario.setTelefono(telefono);

                // Actualiza en la base de datos
                DAOUsuarios.Actualizar(usuario);

                this.cargarUsuariosDesdeDb();

                JOptionPane.showMessageDialog(this,
                        "Usuario actualizado exitosamente.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al actualizar usuario: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }


        private void cargarUsuariosDesdeDb () {
            this.modeloTabla.setRowCount(0);

            try {
                Connection conn = DBConexion.GetConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT ID_Usuario, nombre, apellido, correo, telefono FROM Usuarios");

                // Cargar los datos en la tabla
                while (rs.next()) {
                    int id = rs.getInt("ID_Usuario");
                    String nombre = rs.getString("Nombre");
                    String apellido = rs.getString("Apellido");
                    String correo = rs.getString("correo");
                    String telefono = rs.getString("Telefono");

                    this.modeloTabla.addRow(new Object[]{id, nombre, apellido, correo, telefono});
                }

                // Cerrar los recursos
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar usuarios desde la base de datos: " + ex.getMessage(),
                        "Error de Conexion",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
