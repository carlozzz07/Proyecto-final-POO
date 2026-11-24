package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
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

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JTextField txtUsuario;
	private JPasswordField pwdContrasena;
	private Cliente cliente = null;
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

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

		contentPane.add(crearEncabezado(titulo, subtitulo), BorderLayout.NORTH);
		contentPane.add(crearFormulario(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadCliente();
	}

	private JPanel crearEncabezado(String titulo, String subtitulo) {
		JPanel encabezado = new JPanel();
		encabezado.setBackground(negro);
		encabezado.setLayout(new BorderLayout(0, 0));
		encabezado.setBorder(BorderFactory.createEmptyBorder(18, 40, 18, 40));

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

		JLabel lblLogo = new JLabel("Altice");
		lblLogo.setForeground(blanco);
		lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
		encabezado.add(lblLogo, BorderLayout.EAST);
		return encabezado;
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

		panel.add(crearEtiqueta("CÉDULA (11 DÍGITOS)", 320, 24, 240));
		txtCedula = new JTextField();
		estilizarCampo(txtCedula);
		txtCedula.setBounds(320, 44, 240, 36);
		panel.add(txtCedula);

		panel.add(crearEtiqueta("NOMBRE COMPLETO", 40, 96, 520));
		txtNombre = new JTextField();
		estilizarCampo(txtNombre);
		txtNombre.setBounds(40, 116, 520, 36);
		panel.add(txtNombre);

		panel.add(crearEtiqueta("TELÉFONO (10 DÍGITOS)", 40, 168, 240));
		txtTelefono = new JTextField();
		estilizarCampo(txtTelefono);
		txtTelefono.setBounds(40, 188, 240, 36);
		panel.add(txtTelefono);

		panel.add(crearEtiqueta("DIRECCIÓN", 320, 168, 240));
		txtDireccion = new JTextField();
		estilizarCampo(txtDireccion);
		txtDireccion.setBounds(320, 188, 240, 36);
		panel.add(txtDireccion);

		JPanel linea = new JPanel();
		linea.setBackground(grisBorde);
		linea.setBounds(40, 246, 520, 1);
		panel.add(linea);

		JLabel lblAcceso = new JLabel("ACCESO AL SISTEMA (OPCIONAL)");
		lblAcceso.setForeground(negro);
		lblAcceso.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblAcceso.setBounds(40, 256, 520, 18);
		panel.add(lblAcceso);

		panel.add(crearEtiqueta("USUARIO", 40, 286, 240));
		txtUsuario = new JTextField();
		estilizarCampo(txtUsuario);
		txtUsuario.setBounds(40, 306, 240, 36);
		panel.add(txtUsuario);

		panel.add(crearEtiqueta("CONTRASEÑA", 320, 286, 240));
		pwdContrasena = new JPasswordField();
		estilizarCampo(pwdContrasena);
		pwdContrasena.setBounds(320, 306, 240, 36);
		panel.add(pwdContrasena);
		return panel;
	}

	private JPanel crearBotones() {
		JPanel barra = new JPanel();
		barra.setBackground(grisClaro);
		barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, grisBorde));
		barra.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 14));

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

	private JLabel crearEtiqueta(String texto, int x, int y, int ancho) {
		JLabel etiqueta = new JLabel(texto);
		etiqueta.setForeground(grisOscuro);
		etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 11));
		etiqueta.setBounds(x, y, ancho, 16);
		return etiqueta;
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
				boton.setBackground(grisOscuro);
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
				boton.setBackground(grisBorde);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(blanco);
			}
		});
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
			JOptionPane.showMessageDialog(null, "Debe llenar el nombre y la dirección", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (cedula.length() != 11 || !soloDigitos(cedula)) {
			JOptionPane.showMessageDialog(null, "La cédula debe tener 11 dígitos, sin guiones", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (telefono.length() != 10 || !soloDigitos(telefono)) {
			JOptionPane.showMessageDialog(null, "El teléfono debe tener 10 dígitos, sin guiones", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (estaVacio(usuario) != estaVacio(contrasena)) {
			JOptionPane.showMessageDialog(null, "Si asigna acceso al cliente, debe llenar usuario y contraseña (o dejar ambos en blanco)",
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
				JOptionPane.showMessageDialog(null, "Ese nombre de usuario ya está en uso", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if (cliente == null) {
			if (empresa.buscarClienteByCedula(cedula) != null) {
				JOptionPane.showMessageDialog(null, "Ya existe un cliente con esa cédula", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Cliente nuevo = new Cliente(txtId.getText(), cedula, nombre, telefono, direccion, usuario, contrasena);
			empresa.registrarPersona(nuevo);
			ListCliente.loadClientes();
			JOptionPane.showMessageDialog(null, "Registro satisfactorio", "Información", JOptionPane.INFORMATION_MESSAGE);
			clean();
		} else {
			cliente.setNombre(nombre);
			cliente.setTelefono(telefono);
			cliente.setDireccion(direccion);
			cliente.setUsuario(usuario);
			cliente.setContrasena(contrasena);
			ListCliente.loadClientes();
			JOptionPane.showMessageDialog(null, "Modificación satisfactoria", "Información", JOptionPane.INFORMATION_MESSAGE);
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
}
