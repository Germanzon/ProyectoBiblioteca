
package guiLayer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import utilerias.JPanelFondo;

public class Principal extends JFrame {
    private JPanelFondo jpPrincipal;
    private JPanel jpSecundario;
    CardLayout cardLayout = new CardLayout();
    private JLabel lblHora;
    private Libros jpLibros;
    private Usuarios jpUsuarios;

    public Principal(String title) throws HeadlessException {
        super(title);
        this.setSize(1000, 600);
        this.setLocationRelativeTo((Component)null);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.InitComponents();
    }

    private void InitComponents() {
        ImageIcon imagenFondo = new ImageIcon("imagenes/FondoLibros.jpg");
        JPanelFondo jpPrincipal = new JPanelFondo(imagenFondo);
        jpPrincipal.setLayout((LayoutManager)null);
        this.add(jpPrincipal);
        this.jpSecundario = new JPanel(new BorderLayout());
        this.jpSecundario.setSize(640, 430);
        this.jpSecundario.setBackground(Color.WHITE);
        this.jpSecundario.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        this.jpSecundario.setLocation(170, 60);
        jpPrincipal.add(this.jpSecundario);
        JPanel jpTarjetas = new JPanel(this.cardLayout);
        this.BarraNavegacion(jpTarjetas);
        JPanel jpInicio = new JPanel();
        jpTarjetas.add(jpInicio, "Inicio");
        this.ContenidoInicio(jpInicio);
        this.jpLibros = new Libros();
        jpTarjetas.add(this.jpLibros, "Libros");
        this.jpUsuarios = new Usuarios();
        jpTarjetas.add(this.jpUsuarios, "Usuarios");
        JPanel jpPrestamos = new Prestamos(this.jpLibros, this.jpUsuarios);
        jpTarjetas.add(jpPrestamos, "Préstamos");
        this.jpSecundario.add(jpTarjetas, "Center");
        this.empezarTimerHora();
    }

    private void empezarTimerHora() {
        Timer tiempo = new Timer(1000, new ActionListener() {
            private int minutoAnterior = -1;

            public void actionPerformed(ActionEvent e) {
                LocalDateTime ahora = LocalDateTime.now();
                int minutoActual = ahora.getMinute();
                if (minutoActual != this.minutoAnterior) {
                    Principal.this.ActualizaHora();
                    this.minutoAnterior = minutoActual;
                }

            }
        });
        tiempo.start();
    }

    private void ActualizaHora() {
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter formatohora = DateTimeFormatter.ofPattern("hh:mm a");
        String horaActual = hora.format(formatohora);
        this.lblHora.setText(horaActual);
    }

    private void BarraNavegacion(final JPanel jpTarjetas) {
        JPanel btnsPestana = new JPanel(new FlowLayout());
        btnsPestana.setBackground(new Color(68, 69, 158));
        JButton btnInicio = this.personalizarBotones("Inicio");
        btnInicio.setIcon(new ImageIcon("imagenes/inicio.png"));
        JButton btnLibros = this.personalizarBotones("Libros");
        btnLibros.setIcon(new ImageIcon("imagenes/libros.png"));
        JButton btnUsuarios = this.personalizarBotones("Usuarios");
        btnUsuarios.setIcon(new ImageIcon("imagenes/usuarios.png"));
        JButton btnPrestamo = this.personalizarBotones("Préstamos");
        btnPrestamo.setIcon(new ImageIcon("imagenes/prestamos.png"));
        JButton btnSalir = this.personalizarBotones("Salir");
        btnSalir.setIcon(new ImageIcon("imagenes/salir.png"));
        btnInicio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Principal.this.cardLayout.show(jpTarjetas, "Inicio");
            }
        });
        btnLibros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Principal.this.cardLayout.show(jpTarjetas, "Libros");
            }
        });
        btnUsuarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Principal.this.cardLayout.show(jpTarjetas, "Usuarios");
            }
        });
        btnPrestamo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Principal.this.cardLayout.show(jpTarjetas, "Préstamos");
            }
        });
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnsPestana.add(btnInicio);
        btnsPestana.add(btnLibros);
        btnsPestana.add(btnUsuarios);
        btnsPestana.add(btnPrestamo);
        btnsPestana.add(btnSalir);
        this.jpSecundario.add(btnsPestana, "North");
    }

    private void ContenidoInicio(JPanel jpInicio) {
        jpInicio.setLayout(new BoxLayout(jpInicio, 1));
        JLabel lblTitulo1 = this.personalizarEtiquetas("Sistema de Control  ");
        lblTitulo1.setFont(new Font("Garamond", 3, 50));
        JLabel lblTitulo2 = this.personalizarEtiquetas("Bibliotecario  ");
        lblTitulo2.setFont(new Font("Garamond", 3, 50));
        JLabel jlIconoscb = new JLabel(new ImageIcon("imagenes/scb.png"));
        jlIconoscb.setAlignmentX(0.5F);
        LocalDateTime fecha = LocalDateTime.now();
        DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "MX"));
        String fechaHoy = fecha.format(formatofecha);
        JLabel lblFecha = this.personalizarEtiquetas(fechaHoy);
        lblFecha.setFont(new Font("Garamond", 0, 16));
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter formatohora = DateTimeFormatter.ofPattern("hh:mm a");
        String horaActual = hora.format(formatohora);
        this.lblHora = this.personalizarEtiquetas(horaActual);
        this.lblHora.setFont(new Font("Garamond", 0, 16));
        jpInicio.add(Box.createVerticalStrut(50));
        jpInicio.add(lblTitulo1);
        jpInicio.add(lblTitulo2);
        jpInicio.add(Box.createVerticalStrut(40));
        jpInicio.add(jlIconoscb);
        jpInicio.add(Box.createVerticalStrut(40));
        jpInicio.add(lblFecha);
        jpInicio.add(Box.createVerticalStrut(10));
        jpInicio.add(this.lblHora);
    }

    private JButton personalizarBotones(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(120, 35));
        boton.setBackground(new Color(68, 69, 158));
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        Font fuente = new Font("Garamond", 1, 16);
        boton.setFont(fuente);
        boton.setFocusPainted(false);
        return boton;
    }

    private JLabel personalizarEtiquetas(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setAlignmentX(0.5F);
        etiqueta.setForeground(Color.BLACK);
        return etiqueta;
    }
}
