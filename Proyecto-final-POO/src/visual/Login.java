package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logico.Administrativo;
import logico.Cliente;
import logico.Comercial;
import logico.EmpresaAltice;
import logico.Persona;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField pwdContrasena;
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	public static void main(String[] args) {
		EmpresaAltice.getInstance().cargarDatos();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		setTitle("Altice - Iniciar sesion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 480);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(crearPanelMarca(), BorderLayout.WEST);
		contentPane.add(crearPanelFormulario(), BorderLayout.CENTER);
	}

	private JPanel crearPanelMarca() {
		JPanel panel = new JPanel();
		panel.setBackground(negro);
		panel.setPreferredSize(new Dimension(320, 450));
		panel.setLayout(null);

		JLabel lblLogo = crearLogo(240, 90, 56);
		lblLogo.setBounds(44, 122, 240, 90);
		panel.add(lblLogo);

		JPanel barra = new JPanel();
		barra.setBackground(blanco);
		barra.setBounds(44, 220, 60, 4);
		panel.add(barra);

		JLabel lblLema = new JLabel("Sistema de gestion");
		lblLema.setForeground(grisClaro);
		lblLema.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblLema.setBounds(44, 236, 260, 26);
		panel.add(lblLema);

		JLabel lblServicios = new JLabel("Internet  |  Movil  |  Television");
		lblServicios.setForeground(grisSuave);
		lblServicios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblServicios.setBounds(44, 266, 260, 20);
		panel.add(lblServicios);
		return panel;
	}

	private JPanel crearPanelFormulario() {
		JPanel panel = new JPanel();
		panel.setBackground(blanco);
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel("Iniciar sesion");
		lblTitulo.setForeground(negro);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTitulo.setBounds(50, 60, 340, 38);
		panel.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel("Ingresa con tu usuario y contrasena");
		lblSubtitulo.setForeground(grisMedio);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblSubtitulo.setBounds(50, 100, 340, 20);
		panel.add(lblSubtitulo);

		JLabel lblUsuario = new JLabel("USUARIO");
		lblUsuario.setForeground(grisOscuro);
		lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblUsuario.setBounds(50, 150, 340, 16);
		panel.add(lblUsuario);

		txtUsuario = new JTextField();
		estilizarCampo(txtUsuario);
		txtUsuario.setBounds(50, 170, 340, 38);
		panel.add(txtUsuario);

		JLabel lblContrasena = new JLabel("CONTRASENA");
		lblContrasena.setForeground(grisOscuro);
		lblContrasena.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblContrasena.setBounds(50, 226, 340, 16);
		panel.add(lblContrasena);

		pwdContrasena = new JPasswordField();
		estilizarCampo(pwdContrasena);
		pwdContrasena.setBounds(50, 246, 340, 38);
		panel.add(pwdContrasena);

		JButton btnEntrar = crearBotonPrincipal("Entrar");
		btnEntrar.setBounds(50, 316, 340, 46);
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iniciarSesion();
			}
		});
		panel.add(btnEntrar);
		getRootPane().setDefaultButton(btnEntrar);

		JButton btnSalir = crearBotonSecundario("Salir");
		btnSalir.setBounds(50, 372, 340, 30);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(btnSalir);
		return panel;
	}

	private void estilizarCampo(JTextField campo) {
		campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		campo.setForeground(negro);
		campo.setBackground(grisClaro);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, negro),
				BorderFactory.createEmptyBorder(4, 10, 4, 10)));
	}

	private JButton crearBotonPrincipal(String texto) {
		JButton boton = new JButton(texto);
		boton.setBackground(negro);
		boton.setForeground(blanco);
		boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setOpaque(true);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setBackground(grisOscuro);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(negro);
			}
		});
		return boton;
	}

	private JButton crearBotonSecundario(String texto) {
		JButton boton = new JButton(texto);
		boton.setForeground(grisMedio);
		boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setForeground(negro);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setForeground(grisMedio);
			}
		});
		return boton;
	}

	private void iniciarSesion() {
		String usuario = txtUsuario.getText();
		String contrasena = new String(pwdContrasena.getPassword());

		if (estaVacio(usuario) || estaVacio(contrasena)) {
			JOptionPane.showMessageDialog(null, "Debe ingresar usuario y contrasena", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		Persona persona = empresa.iniciarSesion(usuario, contrasena);

		if (persona == null) {
			JOptionPane.showMessageDialog(null, "Usuario o contrasena incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
			pwdContrasena.setText("");
			return;
		}

		if (persona instanceof Administrativo) {
			AdministrativoVisual ventana = new AdministrativoVisual((Administrativo) persona);
			ventana.setVisible(true);
			dispose();
		} else if (persona instanceof Comercial) {
			ComercialVisual ventana = new ComercialVisual((Comercial) persona);
			ventana.setVisible(true);
			dispose();
		} else if (persona instanceof Cliente) {
			ClienteVisual ventana = new ClienteVisual((Cliente) persona);
			ventana.setVisible(true);
			dispose();
		}
	}

	private boolean estaVacio(String texto) {
		int i = 0;
		while (i < texto.length()) {
			if (texto.charAt(i) != ' ') {
				return false;
			}
			i++;
		}
		return true;
	}

	private JLabel crearLogo(int maxAncho, int maxAlto, int tamanoTexto) {
		JLabel logo = new JLabel();
		logo.setHorizontalAlignment(JLabel.LEFT);
		java.net.URL url = getClass().getResource("/imagenes/logo_altice.png");
		if (url != null) {
			ImageIcon icono = new ImageIcon(url);
			if (icono.getIconWidth() > 0 && icono.getIconHeight() > 0) {
				double escala = Math.min((double) maxAncho / icono.getIconWidth(),
						(double) maxAlto / icono.getIconHeight());
				int ancho = (int) Math.round(icono.getIconWidth() * escala);
				int alto = (int) Math.round(icono.getIconHeight() * escala);
				Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
				logo.setIcon(new ImageIcon(imagen));
				return logo;
			}
		}
		logo.setText("Altice");
		logo.setForeground(blanco);
		logo.setFont(new Font("Segoe UI", Font.BOLD, tamanoTexto));
		return logo;
	}
}