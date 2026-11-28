package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logico.Contrato;
import logico.EmpresaAltice;
import logico.Plan;
import logico.Servicio;

public class RegPlan extends JDialog {
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtDescripcion;
	private JComboBox<String> cbxTipoPago;
	private JLabel lblTipoPago;
	private JTable tablaDisponibles;
	private JTable tablaPlan;
	private DefaultTableModel modeloDisponibles;
	private DefaultTableModel modeloPlan;
	private JLabel lblPrecio;
	private Plan plan = null;
	private ArrayList<Servicio> enPlan = new ArrayList<Servicio>();

	public RegPlan(Plan aux) {
		String titulo = "Registrar plan";
		String subtitulo = "Defina el plan y escoja los servicios que incluye";
		if (aux != null) {
			plan = aux;
			enPlan.addAll(aux.getServicios());
			titulo = "Modificar plan";
			subtitulo = "Actualice los datos y los servicios del plan";
			setTitle("Altice - Modificar Plan");
		} else {
			setTitle("Altice - Registrar Plan");
		}
		setBounds(100, 100, 860, 690);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(titulo, subtitulo, 40), BorderLayout.NORTH);
		contentPane.add(crearFormulario(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadPlan();
		refrescarTablas();
	}

	private JPanel crearFormulario() {
		JPanel panel = new JPanel();
		panel.setBackground(blanco);
		panel.setLayout(null);

		panel.add(crearEtiqueta("ID", 40, 24, 120));
		txtId = new JTextField();
		estilizarCampo(txtId);
		bloquearCampo(txtId);
		txtId.setBounds(40, 44, 120, 36);
		panel.add(txtId);

		panel.add(crearEtiqueta("NOMBRE DEL PLAN", 180, 24, 380));
		txtNombre = new JTextField();
		estilizarCampo(txtNombre);
		txtNombre.setBounds(180, 44, 380, 36);
		panel.add(txtNombre);

		lblTipoPago = crearEtiqueta("TIPO DE PAGO", 580, 24, 240);
		panel.add(lblTipoPago);
		cbxTipoPago = new JComboBox<String>();
		cbxTipoPago.addItem("Postpago");
		cbxTipoPago.addItem("Prepago");
		estilizarCombo(cbxTipoPago);
		cbxTipoPago.setBounds(580, 44, 240, 36);
		panel.add(cbxTipoPago);

		panel.add(crearEtiqueta("DESCRIPCION (OPCIONAL)", 40, 96, 780));
		txtDescripcion = new JTextField();
		estilizarCampo(txtDescripcion);
		txtDescripcion.setBounds(40, 116, 780, 36);
		panel.add(txtDescripcion);

		agregarSeccion(panel, "SERVICIOS DEL PLAN (SOLO SERVICIOS ACTIVOS)", 40, 174, 780);

		panel.add(crearEtiqueta("DISPONIBLES", 40, 214, 330));
		modeloDisponibles = crearModelo();
		tablaDisponibles = new JTable();
		tablaDisponibles.setModel(modeloDisponibles);
		tablaDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		estilizarTabla(tablaDisponibles, 3);
		ajustarColumnas(tablaDisponibles);
		javax.swing.JScrollPane scrollDisp = crearScroll(tablaDisponibles);
		scrollDisp.setBounds(40, 234, 330, 190);
		panel.add(scrollDisp);

		JButton btnAgregar = new JButton("Agregar >");
		estilizarBotonPrincipal(btnAgregar);
		btnAgregar.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
		btnAgregar.setBounds(384, 290, 92, 36);
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarServicio();
			}
		});
		panel.add(btnAgregar);

		JButton btnQuitar = new JButton("< Quitar");
		estilizarBotonContorno(btnQuitar);
		btnQuitar.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(grisMedio, 1),
				BorderFactory.createEmptyBorder(5, 8, 5, 8)));
		btnQuitar.setBounds(384, 334, 92, 36);
		btnQuitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitarServicio();
			}
		});
		panel.add(btnQuitar);

		panel.add(crearEtiqueta("EN EL PLAN", 490, 214, 330));
		modeloPlan = crearModelo();
		tablaPlan = new JTable();
		tablaPlan.setModel(modeloPlan);
		tablaPlan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		estilizarTabla(tablaPlan, 3);
		ajustarColumnas(tablaPlan);
		javax.swing.JScrollPane scrollPlan = crearScroll(tablaPlan);
		scrollPlan.setBounds(490, 234, 330, 190);
		panel.add(scrollPlan);

		lblPrecio = new JLabel();
		lblPrecio.setForeground(negro);
		lblPrecio.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
		lblPrecio.setBounds(40, 440, 780, 24);
		panel.add(lblPrecio);
		return panel;
	}

	private DefaultTableModel crearModelo() {
		DefaultTableModel modelo = new DefaultTableModel() {
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		modelo.setColumnIdentifiers(new String[] {"Id", "Tipo", "Nombre", "Costo (RD$)"});
		return modelo;
	}

	private void ajustarColumnas(JTable tabla) {
		tabla.getColumnModel().getColumn(0).setPreferredWidth(45);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(80);
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
		if (plan != null) {
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

	private boolean estaEnPlan(Servicio s) {
		int i = 0;
		while (i < enPlan.size()) {
			if (enPlan.get(i).getIdServicio().equalsIgnoreCase(s.getIdServicio())) {
				return true;
			}
			i++;
		}
		return false;
	}

	private Object[] fila(Servicio s) {
		return new Object[] {s.getIdServicio(), s.getTipo(), s.getNombre(), String.format("%,.2f", s.calcularCosto())};
	}

	private void refrescarTablas() {
		modeloDisponibles.setRowCount(0);
		modeloPlan.setRowCount(0);

		ArrayList<Servicio> todos = EmpresaAltice.getInstance().getServicios();
		int i = 0;
		while (i < todos.size()) {
			Servicio s = todos.get(i);
			if (s.isActivo() && !estaEnPlan(s)) {
				modeloDisponibles.addRow(fila(s));
			}
			i++;
		}

		float total = 0;
		i = 0;
		while (i < enPlan.size()) {
			modeloPlan.addRow(fila(enPlan.get(i)));
			total += enPlan.get(i).calcularCosto();
			i++;
		}
		lblPrecio.setText("Precio mensual del plan (sin ITBIS): RD$ " + String.format("%,.2f", total));
	}

	private void agregarServicio() {
		int index = tablaDisponibles.getSelectedRow();
		if (index < 0) {
			JOptionPane.showMessageDialog(null, "Seleccione un servicio de la lista de disponibles", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Servicio s = EmpresaAltice.getInstance().buscarServicioById(modeloDisponibles.getValueAt(index, 0).toString());
		if (s != null && !estaEnPlan(s)) {
			enPlan.add(s);
		}
		refrescarTablas();
	}

	private void quitarServicio() {
		int index = tablaPlan.getSelectedRow();
		if (index < 0) {
			JOptionPane.showMessageDialog(null, "Seleccione un servicio de la lista del plan", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String id = modeloPlan.getValueAt(index, 0).toString();
		int i = 0;
		boolean quitado = false;
		while (!quitado && i < enPlan.size()) {
			if (enPlan.get(i).getIdServicio().equalsIgnoreCase(id)) {
				enPlan.remove(i);
				quitado = true;
			}
			i++;
		}
		refrescarTablas();
	}

	private boolean tieneContratos(Plan p) {
		ArrayList<Contrato> contratos = EmpresaAltice.getInstance().getContratos();
		int i = 0;
		while (i < contratos.size()) {
			if (contratos.get(i).getPlan() == p) {
				return true;
			}
			i++;
		}
		return false;
	}

	private void guardar() {
		String nombre = txtNombre.getText().trim();
		String descripcion = txtDescripcion.getText().trim();
		boolean prepago = "Prepago".equals(cbxTipoPago.getSelectedItem());

		if (estaVacio(nombre)) {
			JOptionPane.showMessageDialog(null, "Debe indicar el nombre del plan", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (enPlan.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El plan debe incluir al menos un servicio", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		ArrayList<Plan> planes = empresa.getPlanes();
		int i = 0;
		while (i < planes.size()) {
			Plan otro = planes.get(i);
			if (otro != plan && otro.getNombrePlan().equalsIgnoreCase(nombre)) {
				JOptionPane.showMessageDialog(null, "Ya existe un plan con ese nombre", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			i++;
		}

		if (plan == null) {
			String id = txtId.getText();
			Plan nuevo = new Plan(id, nombre, descripcion, prepago);
			i = 0;
			while (i < enPlan.size()) {
				nuevo.agregarServicio(enPlan.get(i));
				i++;
			}
			empresa.registrarPlan(nuevo);
			if (empresa.buscarPlanById(id) == null) {
				JOptionPane.showMessageDialog(null, "No se pudo registrar el plan", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			empresa.guardarDatos();
			ListPlan.loadPlanes();
			JOptionPane.showMessageDialog(null, "Registro satisfactorio", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			clean();
		} else {
			plan.setNombrePlan(nombre);
			plan.setDescripcion(descripcion);
			if (cbxTipoPago.isEnabled()) {
				plan.setEsPrepago(prepago);
			}
			plan.setServicios(new ArrayList<Servicio>(enPlan));
			empresa.guardarDatos();
			ListPlan.loadPlanes();
			JOptionPane.showMessageDialog(null, "Modificacion satisfactoria", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private void loadPlan() {
		if (plan != null) {
			txtId.setText(plan.getIdPlan());
			txtNombre.setText(plan.getNombrePlan());
			txtDescripcion.setText(plan.getDescripcion());
			cbxTipoPago.setSelectedItem(plan.getTipoPago());
			if (tieneContratos(plan)) {
				cbxTipoPago.setEnabled(false);
				lblTipoPago.setText("TIPO DE PAGO (BLOQUEADO: TIENE CONTRATOS)");
			}
		} else {
			clean();
		}
	}

	private void clean() {
		txtId.setText("P-" + EmpresaAltice.generadorIdPlan);
		txtNombre.setText("");
		txtDescripcion.setText("");
		cbxTipoPago.setSelectedIndex(0);
		enPlan.clear();
		if (modeloPlan != null) {
			refrescarTablas();
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

	private void estilizarTabla(JTable tabla, int... columnasDerecha) {
		tabla.setRowHeight(32);
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tabla.setShowGrid(false);
		tabla.setIntercellSpacing(new Dimension(0, 0));
		tabla.setFillsViewportHeight(true);
		tabla.setBackground(blanco);

		DefaultTableCellRenderer encabezadoRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionado,
					boolean foco, int fila, int columna) {
				super.getTableCellRendererComponent(t, valor, false, false, fila, columna);
				setBackground(negro);
				setForeground(blanco);
				setFont(new Font("Segoe UI", Font.BOLD, 12));
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
				return this;
			}
		};
		tabla.getTableHeader().setDefaultRenderer(encabezadoRenderer);
		tabla.getTableHeader().setPreferredSize(new Dimension(0, 36));
		tabla.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer celdaRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionado,
					boolean foco, int fila, int columna) {
				super.getTableCellRendererComponent(t, valor, seleccionado, false, fila, columna);
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
				if (seleccionado) {
					setBackground(negro);
					setForeground(blanco);
				} else {
					if (fila % 2 == 0) {
						setBackground(blanco);
					} else {
						setBackground(grisClaro);
					}
					setForeground(negro);
					if ("Inactivo".equals(valor) || "Cancelado".equals(valor)) {
						setForeground(grisSuave);
					}
				}
				boolean derecha = false;
				int i = 0;
				while (!derecha && i < columnasDerecha.length) {
					if (columnasDerecha[i] == columna) {
						derecha = true;
					}
					i++;
				}
				if (derecha) {
					setHorizontalAlignment(RIGHT);
				} else {
					setHorizontalAlignment(LEFT);
				}
				return this;
			}
		};
		tabla.setDefaultRenderer(Object.class, celdaRenderer);
	}

	private JScrollPane crearScroll(JTable tabla) {
		JScrollPane scroll = new JScrollPane();
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createLineBorder(grisBorde, 1));
		scroll.getViewport().setBackground(blanco);
		scroll.setViewportView(tabla);
		return scroll;
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