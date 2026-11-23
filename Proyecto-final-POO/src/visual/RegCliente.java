package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logico.Cliente;
import logico.EmpresaAltice;
import logico.Persona;

public class RegCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JTextField txtUsuario;
	private JPasswordField pwdContrasena;
	private Cliente cliente = null;

	public RegCliente(Cliente aux) {
		if (aux != null) {
			cliente = aux;
			setTitle("Modificar Cliente");
		} else {
			setTitle("Registrar Cliente");
		}
		setBounds(100, 100, 570, 380);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);

			JLabel lblId = new JLabel("Id:");
			lblId.setBounds(12, 13, 88, 16);
			panel.add(lblId);

			txtId = new JTextField();
			txtId.setEditable(false);
			txtId.setBounds(12, 32, 239, 22);
			panel.add(txtId);
			txtId.setColumns(10);

			JLabel lblCedula = new JLabel("Cédula (11 dígitos):");
			lblCedula.setBounds(279, 13, 200, 16);
			panel.add(lblCedula);

			txtCedula = new JTextField();
			txtCedula.setBounds(279, 32, 251, 22);
			panel.add(txtCedula);
			txtCedula.setColumns(10);

			JLabel lblNombre = new JLabel("Nombre completo:");
			lblNombre.setBounds(12, 67, 150, 16);
			panel.add(lblNombre);

			txtNombre = new JTextField();
			txtNombre.setBounds(12, 89, 518, 22);
			panel.add(txtNombre);
			txtNombre.setColumns(10);

			JLabel lblTelefono = new JLabel("Teléfono (10 dígitos):");
			lblTelefono.setBounds(12, 122, 200, 16);
			panel.add(lblTelefono);

			txtTelefono = new JTextField();
			txtTelefono.setBounds(12, 146, 239, 22);
			panel.add(txtTelefono);
			txtTelefono.setColumns(10);

			JLabel lblDireccion = new JLabel("Dirección:");
			lblDireccion.setBounds(279, 122, 100, 16);
			panel.add(lblDireccion);

			txtDireccion = new JTextField();
			txtDireccion.setBounds(279, 146, 251, 22);
			panel.add(txtDireccion);
			txtDireccion.setColumns(10);

			JLabel lblUsuario = new JLabel("Usuario (opcional):");
			lblUsuario.setBounds(12, 181, 200, 16);
			panel.add(lblUsuario);

			txtUsuario = new JTextField();
			txtUsuario.setBounds(12, 204, 239, 22);
			panel.add(txtUsuario);
			txtUsuario.setColumns(10);

			JLabel lblContrasena = new JLabel("Contraseña (opcional):");
			lblContrasena.setBounds(279, 181, 200, 16);
			panel.add(lblContrasena);

			pwdContrasena = new JPasswordField();
			pwdContrasena.setBounds(279, 204, 251, 22);
			panel.add(pwdContrasena);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Registrar");
				if (cliente != null) {
					okButton.setText("Modificar");
				}
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						guardar();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		loadCliente();
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
			txtCedula.setEditable(false);
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