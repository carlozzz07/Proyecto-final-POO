package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logico.Cliente;
import logico.EmpresaAltice;
import logico.Persona;

public class RegCliente extends JDialog {
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JTextField txtUsuario;
	private JPasswordField pwdContrasena;
	private Cliente cliente = null;

	public RegCliente(Cliente aux) {
		String titulo = "Registrar cliente";
		String subtitulo = "Complete los datos del nuevo cliente";
		if (aux != null) {
			cliente = aux;
			titulo = "Modificar cliente";
			subtitulo = "Actualice los datos del cliente";
			setTitle("Altice - Modificar Cliente");
		} else {
			setTitle("Altice - Registrar Cliente");
		}
		setBounds(100, 100, 600, 590);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(titulo, subtitulo, 40), BorderLayout.NORTH);
		contentPane.add(crearFormulario(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadCliente();
	}

	private JPanel crearFormulario() {
		JPanel panel = new JPanel();
		panel.setBackground(blanco);
		panel.setLayout(null);

		panel.add(crearEtiqueta("ID", 40, 24, 240));
		txtId = new JTextField();
		estilizarCampo(txtId);
		bloquearCampo(txtId);
		txtId.setBounds(40, 44, 240, 36);
		panel.add(txtId);

		panel.add(crearEtiqueta("CEDULA (11 DIGITOS)", 320, 24, 240));
		txtCedula = new JTextField();
		estilizarCampo(txtCedula);
		txtCedula.setBounds(320, 44, 240, 36);
		panel.add(txtCedula);

		panel.add(crearEtiqueta("NOMBRE COMPLETO", 40, 96, 520));
		txtNombre = new JTextField();
		estilizarCampo(txtNombre);
		txtNombre.setBounds(40, 116, 520, 36);
		panel.add(txtNombre);

		panel.add(crearEtiqueta("TELEFONO (10 DIGITOS)", 40, 168, 240));
		txtTelefono = new JTextField();
		estilizarCampo(txtTelefono);
		txtTelefono.setBounds(40, 188, 240, 36);
		panel.add(txtTelefono);

		panel.add(crearEtiqueta("DIRECCION", 320, 168, 240));
		txtDireccion = new JTextField();
		estilizarCampo(txtDireccion);
		txtDireccion.setBounds(320, 188, 240, 36);
		panel.add(txtDireccion);

		agregarSeccion(panel, "ACCESO AL SISTEMA (OPCIONAL)", 40, 246, 520);

		panel.add(crearEtiqueta("USUARIO", 40, 286, 240));
		txtUsuario = new JTextField();
		estilizarCampo(txtUsuario);
		txtUsuario.setBounds(40, 306, 240, 36);
		panel.add(txtUsuario);

		panel.add(crearEtiqueta("CONTRASENA", 320, 286, 240));
		pwdContrasena = new JPasswordField();
		estilizarCampo(pwdContrasena);
		pwdContrasena.setBounds(320, 306, 240, 36);
		panel.add(pwdContrasena);
		return panel;
	}

	private JPanel crearBotones() {
		JPanel barra = crearBarraBotones();

		JButton cancelButton = new JButton("Cancelar");
		estilizarBotonContorno(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		barra.add(cancelButton);

		JButton okButton = new JButton("Registrar");
		if (cliente != null) {
			okButton.setText("Modificar");
		}
		estilizarBotonPrincipal(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		barra.add(okButton);
		getRootPane().setDefaultButton(okButton);
		return barra;
	}

	private boolean estaVacio(String texto) {
		if (texto == null) {
			return true;
		}
		int i = 0;
		while (i < texto.length()) {
			if (texto.charAt(i) != ' ') {
				return false;
			}
			i++;
		}
		return true;
	}

	private boolean soloDigitos(String texto) {
		int i = 0;
		while (i < texto.length()) {
			if (!Character.isDigit(texto.charAt(i))) {
				return false;
			}
			i++;
		}
		return true;
	}

	private void guardar() {
		String cedula = txtCedula.getText();
		String nombre = txtNombre.getText();
		String telefono = txtTelefono.getText();
		String direccion = txtDireccion.getText();
		String usuario = txtUsuario.getText();
		String contrasena = new String(pwdContrasena.getPassword());

		if (estaVacio(nombre) || estaVacio(direccion)) {
			JOptionPane.showMessageDialog(null, "Debe llenar el nombre y la direccion", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (cedula.length() != 11 || !soloDigitos(cedula)) {
			JOptionPane.showMessageDialog(null, "La cedula debe tener 11 digitos, sin guiones", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (telefono.length() != 10 || !soloDigitos(telefono)) {
			JOptionPane.showMessageDialog(null, "El telefono debe tener 10 digitos, sin guiones", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (estaVacio(usuario) != estaVacio(contrasena)) {
			JOptionPane.showMessageDialog(null, "Si asigna acceso al cliente, debe llenar usuario y contrasena (o dejar ambos en blanco)",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (estaVacio(usuario)) {
			usuario = "";
			contrasena = "";
		}

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		if (!usuario.equals("")) {
			Persona existente = empresa.buscarPersonaByUsuario(usuario);
			if (existente != null && existente != cliente) {
				JOptionPane.showMessageDialog(null, "Ese nombre de usuario ya esta en uso", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if (cliente == null) {
			if (empresa.buscarClienteByCedula(cedula) != null) {
				JOptionPane.showMessageDialog(null, "Ya existe un cliente con esa cedula", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Cliente nuevo = new Cliente(txtId.getText(), cedula, nombre, telefono, direccion, usuario, contrasena);
			empresa.registrarPersona(nuevo);
			empresa.guardarDatos();
			ListCliente.loadClientes();
			JOptionPane.showMessageDialog(null, "Registro satisfactorio", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			clean();
		} else {
			cliente.setNombre(nombre);
			cliente.setTelefono(telefono);
			cliente.setDireccion(direccion);
			cliente.setUsuario(usuario);
			cliente.setContrasena(contrasena);
			empresa.guardarDatos();
			ListCliente.loadClientes();
			JOptionPane.showMessageDialog(null, "Modificacion satisfactoria", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private void loadCliente() {
		if (cliente != null) {
			txtId.setText(cliente.getId());
			txtCedula.setText(cliente.getCedula());
			bloquearCampo(txtCedula);
			txtNombre.setText(cliente.getNombre());
			txtTelefono.setText(cliente.getTelefono());
			txtDireccion.setText(cliente.getDireccion());
			txtUsuario.setText(cliente.getUsuario());
			pwdContrasena.setText(cliente.getContrasena());
		} else {
			clean();
		}
	}

	private void clean() {
		txtId.setText("C-" + EmpresaAltice.generadorIdCliente);
		txtCedula.setText("");
		txtNombre.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtUsuario.setText("");
		pwdContrasena.setText("");
	}

	private JPanel crearEncabezado(String titulo, String subtitulo, int margen) {
		JPanel encabezado = new JPanel();
		encabezado.setBackground(negro);
		encabezado.setLayout(new BorderLayout(0, 0));
		encabezado.setBorder(BorderFactory.createEmptyBorder(18, margen, 18, margen));

		JPanel textos = new JPanel();
		textos.setOpaque(false);
		textos.setLayout(new GridLayout(2, 1, 0, 2));
		encabezado.add(textos, BorderLayout.WEST);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(blanco);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		textos.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel(subtitulo);
		lblSubtitulo.setForeground(grisSuave);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textos.add(lblSubtitulo);

		JLabel lblLogo = crearLogo(130, 34, 26);
		encabezado.add(lblLogo, BorderLayout.EAST);
		return encabezado;
	}

	private JPanel crearBarraBotones() {
		JPanel barra = new JPanel();
		barra.setBackground(grisClaro);
		barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, grisBorde));
		barra.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 14));
		return barra;
	}

	private JLabel crearEtiqueta(String texto, int x, int y, int ancho) {
		JLabel etiqueta = new JLabel(texto);
		etiqueta.setForeground(grisOscuro);
		etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 11));
		etiqueta.setBounds(x, y, ancho, 16);
		return etiqueta;
	}

	private void agregarSeccion(JPanel panel, String texto, int x, int y, int ancho) {
		JPanel linea = new JPanel();
		linea.setBackground(grisBorde);
		linea.setBounds(x, y, ancho, 1);
		panel.add(linea);

		JLabel lblSeccion = new JLabel(texto);
		lblSeccion.setForeground(negro);
		lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblSeccion.setBounds(x, y + 10, ancho, 18);
		panel.add(lblSeccion);
	}

	private void estilizarCampo(JTextField campo) {
		campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		campo.setForeground(negro);
		campo.setBackground(grisClaro);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, negro),
				BorderFactory.createEmptyBorder(4, 10, 4, 10)));
	}

	private void bloquearCampo(JTextField campo) {
		campo.setEditable(false);
		campo.setBackground(grisBorde);
		campo.setForeground(grisMedio);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, grisSuave),
				BorderFactory.createEmptyBorder(4, 10, 4, 10)));
	}

	private void estilizarBotonPrincipal(JButton boton) {
		boton.setBackground(negro);
		boton.setForeground(blanco);
		boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setOpaque(true);
		boton.setBorder(BorderFactory.createEmptyBorder(9, 26, 9, 26));
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (boton.isEnabled()) {
					boton.setBackground(grisOscuro);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(negro);
			}
		});
	}

	private void estilizarBotonContorno(JButton boton) {
		boton.setBackground(blanco);
		boton.setForeground(grisOscuro);
		boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		boton.setFocusPainted(false);
		boton.setOpaque(true);
		boton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(grisMedio, 1),
				BorderFactory.createEmptyBorder(8, 24, 8, 24)));
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (boton.isEnabled()) {
					boton.setBackground(grisBorde);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(blanco);
			}
		});
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