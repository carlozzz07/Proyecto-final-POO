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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logico.Cliente;
import logico.Comercial;
import logico.Contrato;
import logico.Empleado;
import logico.EmpresaAltice;
import logico.Plan;
import logico.Servicio;

public class RegContrato extends JDialog {
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
	private JTextField txtFecha;
	private JComboBox<String> cbxCliente;
	private JComboBox<String> cbxPlan;
	private JComboBox<String> cbxComercial;
	private JLabel lblResumen;
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private ArrayList<Plan> planes = new ArrayList<Plan>();
	private ArrayList<Comercial> comerciales = new ArrayList<Comercial>();
	private Comercial vendedor;

	/**
	 * @param vendedor comercial que registra el contrato. Si es null se elige
	 *                 de la lista (o se deja sin comercial).
	 */
	public RegContrato(Comercial vendedor) {
		this.vendedor = vendedor;
		setTitle("Altice - Registrar Contrato");
		setBounds(100, 100, 600, 660);
		setLocationRelativeTo(null);
		setResizable(false);

		cargarListas();

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado("Registrar contrato",
				"Asigne un plan a un cliente", 40), BorderLayout.NORTH);
		contentPane.add(crearFormulario(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		clean();
		actualizarResumen();
	}

	private void cargarListas() {
		EmpresaAltice empresa = EmpresaAltice.getInstance();

		ArrayList<Cliente> todosClientes = empresa.getClientes();
		int i = 0;
		while (i < todosClientes.size()) {
			if (todosClientes.get(i).isEstado()) {
				clientes.add(todosClientes.get(i));
			}
			i++;
		}

		ArrayList<Plan> todosPlanes = empresa.getPlanes();
		i = 0;
		while (i < todosPlanes.size()) {
			if (todosPlanes.get(i).isActivo()) {
				planes.add(todosPlanes.get(i));
			}
			i++;
		}

		ArrayList<Empleado> empleados = empresa.getEmpleados();
		i = 0;
		while (i < empleados.size()) {
			if (empleados.get(i) instanceof Comercial && empleados.get(i).isEstado()) {
				comerciales.add((Comercial) empleados.get(i));
			}
			i++;
		}
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

		panel.add(crearEtiqueta("FECHA DE INICIO (DD/MM/AAAA)", 320, 24, 240));
		txtFecha = new JTextField();
		estilizarCampo(txtFecha);
		txtFecha.setBounds(320, 44, 240, 36);
		panel.add(txtFecha);

		panel.add(crearEtiqueta("CLIENTE", 40, 96, 520));
		cbxCliente = new JComboBox<String>();
		int i = 0;
		while (i < clientes.size()) {
			Cliente c = clientes.get(i);
			cbxCliente.addItem(c.getId() + " - " + c.getNombre() + " (" + c.getCedula() + ")");
			i++;
		}
		estilizarCombo(cbxCliente);
		cbxCliente.setBounds(40, 116, 520, 36);
		panel.add(cbxCliente);

		panel.add(crearEtiqueta("PLAN", 40, 168, 520));
		cbxPlan = new JComboBox<String>();
		i = 0;
		while (i < planes.size()) {
			Plan p = planes.get(i);
			cbxPlan.addItem(p.getIdPlan() + " - " + p.getNombrePlan() + " (" + p.getTipoPago() + ")");
			i++;
		}
		estilizarCombo(cbxPlan);
		cbxPlan.setBounds(40, 188, 520, 36);
		cbxPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarResumen();
			}
		});
		panel.add(cbxPlan);

		panel.add(crearEtiqueta("COMERCIAL", 40, 240, 520));
		cbxComercial = new JComboBox<String>();
		if (vendedor != null) {
			cbxComercial.addItem(vendedor.getId() + " - " + vendedor.getNombre());
			cbxComercial.setEnabled(false);
		} else {
			cbxComercial.addItem("(Sin comercial)");
			i = 0;
			while (i < comerciales.size()) {
				Comercial c = comerciales.get(i);
				cbxComercial.addItem(c.getId() + " - " + c.getNombre());
				i++;
			}
		}
		estilizarCombo(cbxComercial);
		cbxComercial.setBounds(40, 260, 520, 36);
		panel.add(cbxComercial);

		agregarSeccion(panel, "RESUMEN DEL PLAN", 40, 318, 520);

		lblResumen = new JLabel();
		lblResumen.setForeground(grisOscuro);
		lblResumen.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
		lblResumen.setVerticalAlignment(JLabel.TOP);
		lblResumen.setBounds(40, 354, 520, 100);
		panel.add(lblResumen);
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

	private void actualizarResumen() {
		int index = cbxPlan.getSelectedIndex();
		if (index < 0 || index >= planes.size()) {
			lblResumen.setText("<html>No hay planes activos. Registre un plan antes de crear contratos.</html>");
			return;
		}
		Plan p = planes.get(index);
		String servicios = "";
		ArrayList<Servicio> lista = p.getServicios();
		int i = 0;
		while (i < lista.size()) {
			if (i > 0) {
				servicios = servicios + ", ";
			}
			servicios = servicios + lista.get(i).getNombre();
			i++;
		}
		float precio = p.calcularPrecio();
		float total = precio + precio * 0.18f;
		lblResumen.setText("<html>Modalidad: " + p.getTipoPago()
				+ "<br>Servicios: " + servicios
				+ "<br>Precio mensual: RD$ " + String.format("%,.2f", precio)
				+ " + ITBIS (18%) = RD$ " + String.format("%,.2f", total) + "</html>");
	}

	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private boolean tieneContratoActivo(Cliente cliente, Plan plan) {
		ArrayList<Contrato> contratos = cliente.getContratos();
		int i = 0;
		while (i < contratos.size()) {
			Contrato c = contratos.get(i);
			if (c.isActivo() && c.getPlan() != null
					&& c.getPlan().getIdPlan().equalsIgnoreCase(plan.getIdPlan())) {
				return true;
			}
			i++;
		}
		return false;
	}

	private void guardar() {
		if (clientes.isEmpty()) {
			mostrarError("No hay clientes activos. Registre un cliente primero.");
			return;
		}
		if (planes.isEmpty()) {
			mostrarError("No hay planes activos. Registre un plan primero.");
			return;
		}

		LocalDate fecha = null;
		try {
			fecha = LocalDate.parse(txtFecha.getText().trim(), FORMATO);
		} catch (DateTimeParseException e) {
			mostrarError("La fecha de inicio debe tener el formato DD/MM/AAAA (por ejemplo 15/03/2026)");
			return;
		}
		if (fecha.isAfter(LocalDate.now())) {
			mostrarError("La fecha de inicio no puede ser futura");
			return;
		}

		Cliente cliente = clientes.get(cbxCliente.getSelectedIndex());
		Plan plan = planes.get(cbxPlan.getSelectedIndex());
		Comercial comercial = null;
		if (vendedor != null) {
			comercial = vendedor;
		} else if (cbxComercial.getSelectedIndex() > 0) {
			comercial = comerciales.get(cbxComercial.getSelectedIndex() - 1);
		}
		
		if (plan.getServicios().size() == 0) {
			mostrarError("El plan seleccionado no tiene servicios");
			return;
		}
		if (tieneContratoActivo(cliente, plan)) {
			mostrarError("El cliente ya tiene un contrato activo con ese plan");
			return;
		}

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		String id = txtId.getText();
		Contrato nuevo = new Contrato(id, cliente, plan, comercial, fecha);
		empresa.registrarContrato(nuevo);
		if (empresa.buscarContratoById(id) == null) {
			mostrarError("No se pudo registrar el contrato.\nVerifique que el cliente, el plan y el comercial esten activos.");
			return;
		}
		empresa.guardarDatos();
		ListContrato.loadContratos();

		String mensaje = "Registro satisfactorio";
		if (plan.isEsPrepago()) {
			mensaje = mensaje + "\n(Plan prepago: se registra con su primera factura)";
		}
		JOptionPane.showMessageDialog(null, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
		clean();
	}

	private void clean() {
		txtId.setText("CT-" + EmpresaAltice.generadorIdContrato);
		txtFecha.setText(LocalDate.now().format(FORMATO));
		if (cbxCliente.getItemCount() > 0) {
			cbxCliente.setSelectedIndex(0);
		}
		if (cbxPlan.getItemCount() > 0) {
			cbxPlan.setSelectedIndex(0);
		}
		if (vendedor == null && cbxComercial.getItemCount() > 0) {
			cbxComercial.setSelectedIndex(0);
		}
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

		JLabel lblLogo = new JLabel("Altice");
		lblLogo.setForeground(blanco);
		lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
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
}