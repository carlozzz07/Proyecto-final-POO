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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import logico.Administrativo;
import logico.Comercial;
import logico.Empleado;
import logico.EmpresaAltice;
import logico.Persona;

public class RegEmpleado extends JDialog {
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/uuuu")
			.withResolverStyle(ResolverStyle.STRICT);

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtCedula;
	private JTextField txtNombre;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JComboBox<String> cbxTipo;
	private JTextField txtFecha;
	private JTextField txtSueldo;
	private JLabel lblExtra1;
	private JLabel lblExtra2;
	private JTextField txtExtra1;
	private JTextField txtExtra2;
	private JTextField txtUsuario;
	private JPasswordField pwdContrasena;
	private Empleado empleado = null;

	public RegEmpleado(Empleado aux) {
		String titulo = "Registrar empleado";
		String subtitulo = "Complete los datos del nuevo empleado";
		if (aux != null) {
			empleado = aux;
			titulo = "Modificar empleado";
			subtitulo = "Actualice los datos del empleado";
			setTitle("Altice - Modificar Empleado");
		} else {
			setTitle("Altice - Registrar Empleado");
		}
		setBounds(100, 100, 600, 710);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(titulo, subtitulo, 40), BorderLayout.NORTH);
		contentPane.add(crearFormulario(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		actualizarExtras();
		loadEmpleado();
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

		panel.add(crearEtiqueta("TIPO", 40, 240, 160));
		cbxTipo = new JComboBox<String>();
		cbxTipo.addItem("Administrativo");
		cbxTipo.addItem("Comercial");
		estilizarCombo(cbxTipo);
		cbxTipo.setBounds(40, 260, 160, 36);
		cbxTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarExtras();
				txtExtra1.setText("");
				txtExtra2.setText("");
			}
		});
		panel.add(cbxTipo);

		panel.add(crearEtiqueta("FECHA ENTRADA (DD/MM/AAAA)", 220, 240, 180));
		txtFecha = new JTextField();
		estilizarCampo(txtFecha);
		txtFecha.setBounds(220, 260, 160, 36);
		panel.add(txtFecha);

		panel.add(crearEtiqueta("SUELDO BASE (RD$)", 400, 240, 160));
		txtSueldo = new JTextField();
		estilizarCampo(txtSueldo);
		txtSueldo.setBounds(400, 260, 160, 36);
		panel.add(txtSueldo);

		lblExtra1 = crearEtiqueta("", 40, 312, 240);
		panel.add(lblExtra1);
		txtExtra1 = new JTextField();
		estilizarCampo(txtExtra1);
		txtExtra1.setBounds(40, 332, 240, 36);
		panel.add(txtExtra1);

		lblExtra2 = crearEtiqueta("", 320, 312, 240);
		panel.add(lblExtra2);
		txtExtra2 = new JTextField();
		estilizarCampo(txtExtra2);
		txtExtra2.setBounds(320, 332, 240, 36);
		panel.add(txtExtra2);

		agregarSeccion(panel, "ACCESO AL SISTEMA (OBLIGATORIO)", 40, 390, 520);

		panel.add(crearEtiqueta("USUARIO", 40, 430, 240));
		txtUsuario = new JTextField();
		estilizarCampo(txtUsuario);
		txtUsuario.setBounds(40, 450, 240, 36);
		panel.add(txtUsuario);

		panel.add(crearEtiqueta("CONTRASENA", 320, 430, 240));
		pwdContrasena = new JPasswordField();
		estilizarCampo(pwdContrasena);
		pwdContrasena.setBounds(320, 450, 240, 36);
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
		if (empleado != null) {
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

	private void actualizarExtras() {
		if (esAdministrativo()) {
			lblExtra1.setText("DEPARTAMENTO");
			lblExtra2.setText("BONO (RD$)");
		} else {
			lblExtra1.setText("COMISION (%)");
			lblExtra2.setText("META MENSUAL (RD$)");
		}
	}

	private boolean esAdministrativo() {
		return "Administrativo".equals(cbxTipo.getSelectedItem());
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

	private Float leerNumero(String texto) {
		try {
			float valor = Float.parseFloat(texto.trim().replace(',', '.'));
			if (Float.isNaN(valor) || Float.isInfinite(valor)) {
				return null;
			}
			return valor;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private LocalDate leerFecha(String texto) {
		try {
			return LocalDate.parse(texto.trim(), FORMATO);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void guardar() {
		String cedula = txtCedula.getText().trim();
		String nombre = txtNombre.getText().trim();
		String telefono = txtTelefono.getText().trim();
		String direccion = txtDireccion.getText().trim();
		String usuario = txtUsuario.getText().trim();
		String contrasena = new String(pwdContrasena.getPassword());

		if (estaVacio(nombre) || estaVacio(direccion)) {
			mostrarError("Debe llenar el nombre y la direccion");
			return;
		}
		if (cedula.length() != 11 || !soloDigitos(cedula)) {
			mostrarError("La cedula debe tener 11 digitos, sin guiones");
			return;
		}
		if (telefono.length() != 10 || !soloDigitos(telefono)) {
			mostrarError("El telefono debe tener 10 digitos, sin guiones");
			return;
		}
		if (estaVacio(usuario) || estaVacio(contrasena)) {
			mostrarError("Todo empleado debe tener usuario y contrasena");
			return;
		}

		LocalDate fecha = leerFecha(txtFecha.getText());
		if (fecha == null) {
			mostrarError("La fecha de entrada debe tener el formato DD/MM/AAAA (por ejemplo 15/03/2026)");
			return;
		}
		if (fecha.isAfter(LocalDate.now())) {
			mostrarError("La fecha de entrada no puede ser futura");
			return;
		}

		Float sueldo = leerNumero(txtSueldo.getText());
		if (sueldo == null || sueldo <= 0) {
			mostrarError("El sueldo base debe ser un numero mayor que cero");
			return;
		}

		String departamento = "";
		Float bono = null;
		Float comision = null;
		Float meta = null;
		if (esAdministrativo()) {
			departamento = txtExtra1.getText().trim();
			bono = leerNumero(txtExtra2.getText());
			if (estaVacio(departamento)) {
				mostrarError("Debe indicar el departamento");
				return;
			}
			if (bono == null || bono < 0) {
				mostrarError("El bono debe ser un numero mayor o igual a cero");
				return;
			}
		} else {
			comision = leerNumero(txtExtra1.getText());
			meta = leerNumero(txtExtra2.getText());
			if (comision == null || comision < 0 || comision > 100) {
				mostrarError("La comision debe ser un numero entre 0 y 100");
				return;
			}
			if (meta == null || meta < 0) {
				mostrarError("La meta mensual debe ser un numero mayor o igual a cero");
				return;
			}
		}

		// Validar duplicados aqui, porque registrarPersona() no avisa cuando rechaza.
		EmpresaAltice empresa = EmpresaAltice.getInstance();
		Persona existente = empresa.buscarPersonaByUsuario(usuario);
		if (existente != null && existente != empleado) {
			mostrarError("Ese nombre de usuario ya esta en uso");
			return;
		}

		if (empleado == null) {
			if (empresa.buscarEmpleadoByCedula(cedula) != null) {
				mostrarError("Ya existe un empleado con esa cedula");
				return;
			}
			Empleado nuevo;
			if (esAdministrativo()) {
				nuevo = new Administrativo(txtId.getText(), cedula, nombre, telefono, direccion, usuario,
						contrasena, fecha, sueldo, departamento, bono);
			} else {
				nuevo = new Comercial(txtId.getText(), cedula, nombre, telefono, direccion, usuario,
						contrasena, fecha, sueldo, comision, meta);
			}
			empresa.registrarPersona(nuevo);
			if (empresa.buscarEmpleadoByCedula(cedula) == null) {
				mostrarError("No se pudo registrar el empleado");
				return;
			}
			empresa.guardarDatos();
			ListEmpleado.loadEmpleados();
			JOptionPane.showMessageDialog(null, "Registro satisfactorio", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			clean();
		} else {
			empleado.setNombre(nombre);
			empleado.setTelefono(telefono);
			empleado.setDireccion(direccion);
			empleado.setUsuario(usuario);
			empleado.setContrasena(contrasena);
			empleado.setFechaEntrada(fecha);
			empleado.setSueldoBase(sueldo);
			if (empleado instanceof Administrativo) {
				Administrativo adm = (Administrativo) empleado;
				adm.setDepartamento(departamento);
				adm.setBono(bono);
			} else if (empleado instanceof Comercial) {
				Comercial com = (Comercial) empleado;
				com.setPorcentajeComision(comision);
				com.setMetaMensual(meta);
			}
			empresa.guardarDatos();
			ListEmpleado.loadEmpleados();
			JOptionPane.showMessageDialog(null, "Modificacion satisfactoria", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private void loadEmpleado() {
		if (empleado != null) {
			txtId.setText(empleado.getId());
			txtCedula.setText(empleado.getCedula());
			bloquearCampo(txtCedula);
			txtNombre.setText(empleado.getNombre());
			txtTelefono.setText(empleado.getTelefono());
			txtDireccion.setText(empleado.getDireccion());
			cbxTipo.setSelectedItem(empleado.getTipo());
			cbxTipo.setEnabled(false);
			txtFecha.setText(empleado.getFechaEntrada().format(FORMATO));
			txtSueldo.setText(String.valueOf(empleado.getSueldoBase()));
			if (empleado instanceof Administrativo) {
				Administrativo adm = (Administrativo) empleado;
				txtExtra1.setText(adm.getDepartamento());
				txtExtra2.setText(String.valueOf(adm.getBono()));
			} else if (empleado instanceof Comercial) {
				Comercial com = (Comercial) empleado;
				txtExtra1.setText(String.valueOf(com.getPorcentajeComision()));
				txtExtra2.setText(String.valueOf(com.getMetaMensual()));
			}
			txtUsuario.setText(empleado.getUsuario());
			pwdContrasena.setText(empleado.getContrasena());
		} else {
			clean();
		}
	}

	private void clean() {
		txtId.setText("E-" + EmpresaAltice.generadorIdEmpleado);
		txtCedula.setText("");
		txtNombre.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtFecha.setText(LocalDate.now().format(FORMATO));
		txtSueldo.setText("");
		txtExtra1.setText("");
		txtExtra2.setText("");
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

	private void estilizarCombo(JComboBox<?> combo) {
		combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		combo.setForeground(negro);
		combo.setBackground(grisClaro);
		combo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, negro));
		combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
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