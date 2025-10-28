package guiLayer;

import guiLayer.TablaLibros;
import guiLayer.TablaPrestamos;
import guiLayer.TablaUsuarios;
import utilerias.JPanelFondo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Principal extends JFrame {

    private JPanelFondo jpPrincipal;
    private JPanel jpSecundario;
    CardLayout cardLayout = new CardLayout();
    private JLabel lblHora;

    private TablaLibros jpTablaLibros;
    private TablaUsuarios jpTablaUsuarios;

    public Principal(String title) throws HeadlessException{
        super(title);
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        InitComponents();
    }

    private void InitComponents(){
        //Se crea un objeto de tipo ImageIcon para añadirlo al panel principal
        ImageIcon imagenFondo = new ImageIcon("imagenes/FondoLibros.jpg");
        JPanelFondo jpPrincipal = new JPanelFondo(imagenFondo);
        add(jpPrincipal);

        //Se crea el panel secundario en el que se mostraran los datos del sistema de control
        jpSecundario = new JPanel(new BorderLayout());
        jpSecundario.setSize(1000, 560);
        jpSecundario.setBackground(Color.WHITE);
        jpSecundario.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        jpSecundario.setLocation(260, 90);
        jpPrincipal.add(jpSecundario);

        //Crear tarjetas para intercambiar de contenido (Inicio, Libros, Usuarios, Préstamos, Salir)
        JPanel jpTarjetas = new JPanel(cardLayout);
        BarraNavegacion(jpTarjetas);

        //Tarjeta que muestra el título junto con la fecha y hora actual
        JPanel jpInicio = new JPanel();
        jpTarjetas.add(jpInicio, "Inicio");
        ContenidoInicio(jpInicio);

        //Tarjeta que muestra el gestor de libros (añadir, modificar, eliminar)
        jpTablaLibros = new TablaLibros();
        jpTarjetas.add(jpTablaLibros, "Libros");

        //Tarjeta que muestra el gestor de usuarios (registrar, modificar, eliminar)
        jpTablaUsuarios = new TablaUsuarios();
        jpTarjetas.add(jpTablaUsuarios, "Usuarios");

        //Tarjeta que muestra el gestor de préstamos (nuevo préstamo, devolver préstamo)
        JPanel jpPrestamos = new TablaPrestamos(jpTablaLibros, jpTablaUsuarios);
        jpTarjetas.add(jpPrestamos, "Préstamos");

        jpSecundario.add(jpTarjetas, BorderLayout.CENTER);

        //Comenzar el timer para actualizar la hora cuando pasen los 60 segundos
        empezarTimerHora();
    }

    private void empezarTimerHora(){
        //Temporizador que se ejecuta cada 1000 milisegundos --> 1 segundo
        Timer tiempo = new Timer(1000, new ActionListener() {
            //Inicializamos variable que almacene el minuto anterior con un valor de -1
            //No se le da un valor de 0 o default en caso de que ejecutemos el programa en una hora con minuto cero (ej. 10:00)
            private int minutoAnterior = -1;

            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime ahora = LocalDateTime.now();
                int minutoActual = ahora.getMinute();       //Minuto actual

                //Si los minutos no coinciden significará que el minuto actual ha cambiado
                if (minutoActual != minutoAnterior) {
                    ActualizaHora();
                    minutoAnterior = minutoActual;  //Ambas variables tendrán el mismo valor hasta que el minuto actual cambie
                }
            }
        });
        tiempo.start();
    }

    private void ActualizaHora() {
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter formatohora = DateTimeFormatter.ofPattern("hh:mm a");
        String horaActual = hora.format(formatohora);
        lblHora.setText(horaActual);
    }

    private void BarraNavegacion(JPanel jpTarjetas){
        //Se crean botones para cambiar de tarjeta
        JPanel btnsPestana = new JPanel(new GridLayout(1, 0, 0, 0));
        btnsPestana.setBackground(new Color(68, 69, 158));

        //Darle una etiqueta e icono a cada boton
        JButton btnInicio = personalizarBotones("Inicio", 'I');
        btnInicio.setIcon(new ImageIcon("imagenes/inicio.png"));

        JButton btnLibros = personalizarBotones("Libros", 'L');
        btnLibros.setIcon(new ImageIcon("imagenes/libros.png"));

        JButton btnUsuarios = personalizarBotones("Usuarios", 'U');
        btnUsuarios.setIcon(new ImageIcon("imagenes/usuarios.png"));

        JButton btnPrestamo = personalizarBotones("Préstamos", 'P');
        btnPrestamo.setIcon(new ImageIcon("imagenes/prestamos.png"));

        JButton btnSalir = personalizarBotones("Salir", 'S');
        btnSalir.setIcon(new ImageIcon("imagenes/salir.png"));

        //Eventos al clickear los botones
        btnInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(jpTarjetas, "Inicio");
            }
        });

        btnLibros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(jpTarjetas, "Libros");
            }
        });

        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(jpTarjetas, "Usuarios");
            }
        });

        btnPrestamo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(jpTarjetas, "Préstamos");
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Cerrar la aplicación
                System.exit(0);
            }
        });

        btnsPestana.add(btnInicio);
        btnsPestana.add(btnLibros);
        btnsPestana.add(btnUsuarios);
        btnsPestana.add(btnPrestamo);
        btnsPestana.add(btnSalir);

        //Agregar los botones en el panel secundario
        jpSecundario.add(btnsPestana, BorderLayout.NORTH);
    }

    private void ContenidoInicio(JPanel jpInicio) {
        jpInicio.setLayout(new BoxLayout(jpInicio, BoxLayout.Y_AXIS));

        //Etiquetas que se muestran en la pantalla de inicio al ejecutar el programa
        JLabel lblTitulo1 = personalizarEtiquetas("Sistema de Control  ");
        lblTitulo1.setFont(new Font("Garamond", Font.BOLD | Font.ITALIC, 50));

        JLabel lblTitulo2 = personalizarEtiquetas("Bibliotecario  ");
        lblTitulo2.setFont(new Font("Garamond", Font.BOLD | Font.ITALIC, 50));

        //Icono de SCB
        JLabel jlIconoscb = new JLabel(new ImageIcon("imagenes/scb.png"));
        jlIconoscb.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Fecha actual
        LocalDateTime fecha = LocalDateTime.now();
        DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy",
                new Locale("es", "MX"));
        String fechaHoy = fecha.format(formatofecha);
        JLabel lblFecha = personalizarEtiquetas(fechaHoy);
        lblFecha.setFont(new Font("Garamond", Font.PLAIN, 16));

        //Hora actual
        LocalDateTime hora = LocalDateTime.now();
        DateTimeFormatter formatohora = DateTimeFormatter.ofPattern("hh:mm a");
        String horaActual = hora.format(formatohora);
        lblHora = personalizarEtiquetas(horaActual);
        lblHora.setFont(new Font("Garamond", Font.PLAIN, 16));

        jpInicio.add(Box.createVerticalStrut(50));
        jpInicio.add(lblTitulo1);
        jpInicio.add(lblTitulo2);
        jpInicio.add(Box.createVerticalStrut(40));
        jpInicio.add(jlIconoscb);
        jpInicio.add(Box.createVerticalStrut(40));
        jpInicio.add(lblFecha);
        jpInicio.add(Box.createVerticalStrut(10));
        jpInicio.add(lblHora);
    }

    private JButton personalizarBotones (String texto, Character mnemonic){
        JButton boton = new JButton(texto);
        boton.setMnemonic(mnemonic);
        boton.setPreferredSize(new Dimension(120, 50));

        boton.setBackground(new Color(68, 69, 158));
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        Font fuente = new Font("Garamond", Font.BOLD, 16);
        boton.setFont(fuente);
        boton.setFocusPainted(false);

        return boton;
    }

    private JLabel personalizarEtiquetas (String texto){
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiqueta.setForeground(Color.BLACK);

        return etiqueta;
    }
}
