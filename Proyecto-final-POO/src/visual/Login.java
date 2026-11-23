package visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logico.Administrativo;
import logico.Cliente;
import logico.Comercial;
import logico.EmpresaAltice;
import logico.Persona;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField pwdContrasena;

	public static void main(String[] args) {
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
		setTitle("Altice - Iniciar sesión");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 290);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPane.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);

			JLabel lblTitulo = new JLabel("Sistema de gestión Altice");
			lblTitulo.setBounds(12, 15, 380, 20);
			panel.add(lblTitulo);

			JLabel lblUsuario = new JLabel("Usuario:");
			lblUsuario.setBounds(40, 60, 100, 16);
			panel.add(lblUsuario);

			txtUsuario = new JTextField();
			txtUsuario.setBounds(40, 80, 320, 24);
			panel.add(txtUsuario);
			txtUsuario.setColumns(10);

			JLabel lblContrasena = new JLabel("Contraseña:");
			lblContrasena.setBounds(40, 115, 100, 16);
			panel.add(lblContrasena);

			pwdContrasena = new JPasswordField();
			pwdContrasena.setBounds(40, 135, 320, 24);
			panel.add(pwdContrasena);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			contentPane.add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnEntrar = new JButton("Entrar");
				btnEntrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						iniciarSesion();
					}
				});
				buttonPane.add(btnEntrar);
				getRootPane().setDefaultButton(btnEntrar);
			}
			{
				JButton btnSalir = new JButton("Salir");
				btnSalir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				buttonPane.add(btnSalir);
			}
		}
	}

	private void iniciarSesion() {
		String usuario = txtUsuario.getText();
		String contrasena = new String(pwdContrasena.getPassword());

		if (estaVacio(usuario) || estaVacio(contrasena)) {
			JOptionPane.showMessageDialog(null, "Debe ingresar usuario y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		Persona persona = empresa.iniciarSesion(usuario, contrasena);

		if (persona == null) {
			JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
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
}