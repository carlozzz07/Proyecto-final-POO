package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
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

import logico.Cliente;
import logico.Contrato;
import logico.EmpresaAltice;
import logico.Factura;

public class ListFactura extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private Object row[];
	private JTextField txtBuscar;
	private JComboBox<String> cbxFiltro;
	private JLabel lblResumen;
	private JButton btnDetalle;
	private JButton btnPagar;
	private Factura factura = null;
	private Cliente clienteFijo;
	private boolean soloLectura;
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	public ListFactura() {
		this(null);
	}

	public ListFactura(Cliente clienteFijo) {
		this.clienteFijo = clienteFijo;
		this.soloLectura = clienteFijo != null;

		if (soloLectura) {
			setTitle("Altice - Mis Facturas");
		} else {
			setTitle("Altice - Listado de Facturas");
		}
		setBounds(100, 100, 1000, 580);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(), BorderLayout.NORTH);
		contentPane.add(crearCentro(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadFacturas();
	}

	private JPanel crearEncabezado() {
		String titulo = "Listado de facturas";
		String subtitulo = "Seleccione una factura para marcarla como pagada o ver su detalle";
		if (soloLectura) {
			titulo = "Mis facturas";
			subtitulo = "Consulte sus facturas pendientes y pagadas";
		}

		JPanel encabezado = new JPanel();
		encabezado.setBackground(negro);
		encabezado.setLayout(new BorderLayout(0, 0));
		encabezado.setBorder(BorderFactory.createEmptyBorder(18, 30, 18, 30));

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

	private JPanel crearCentro() {
		JPanel centro = new JPanel();
		centro.setBackground(blanco);
		centro.setBorder(BorderFactory.createEmptyBorder(20, 30, 16, 30));
		centro.setLayout(new BorderLayout(0, 14));

		JPanel panelBusqueda = new JPanel();
		panelBusqueda.setOpaque(false);
		panelBusqueda.setLayout(new BorderLayout(14, 0));
		centro.add(panelBusqueda, BorderLayout.NORTH);

		JLabel lblBuscar = new JLabel("BUSCAR (ID, NCF, CLIENTE O CÉDULA)");
		lblBuscar.setForeground(grisOscuro);
		lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 11));
		panelBusqueda.add(lblBuscar, BorderLayout.WEST);

		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtBuscar.setForeground(negro);
		txtBuscar.setBackground(grisClaro);
		txtBuscar.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, negro),
				BorderFactory.createEmptyBorder(6, 10, 6, 10)));
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				loadFacturas();
				limpiarSeleccion();
			}
		});
		panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

		cbxFiltro = new JComboBox<String>();
		cbxFiltro.setModel(new DefaultComboBoxModel<String>(new String[] {"Todas", "Pendientes", "Vencidas", "Pagadas"}));
		cbxFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cbxFiltro.setForeground(negro);
		cbxFiltro.setBackground(grisClaro);
		cbxFiltro.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, negro));
		cbxFiltro.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cbxFiltro.setPreferredSize(new Dimension(150, 36));
		cbxFiltro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadFacturas();
				limpiarSeleccion();
			}
		});
		panelBusqueda.add(cbxFiltro, BorderLayout.EAST);

		String headers[] = {"Id", "NCF", "Cliente", "Contrato", "Emisión", "Vencimiento", "Total", "Estado"};
		model = new DefaultTableModel() {
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		model.setColumnIdentifiers(headers);

		table = new JTable();
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(32);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setFillsViewportHeight(true);
		table.setBackground(blanco);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				seleccionar();
			}
		});
		estilizarTabla();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createLineBorder(grisBorde, 1));
		scrollPane.getViewport().setBackground(blanco);
		scrollPane.setViewportView(table);
		centro.add(scrollPane, BorderLayout.CENTER);

		lblResumen = new JLabel();
		lblResumen.setForeground(grisOscuro);
		lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 13));
		centro.add(lblResumen, BorderLayout.SOUTH);
		return centro;
	}

	private void estilizarTabla() {
		DefaultTableCellRenderer encabezadoRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
					boolean foco, int fila, int columna) {
				super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
				setBackground(negro);
				setForeground(blanco);
				setFont(new Font("Segoe UI", Font.BOLD, 12));
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
				return this;
			}
		};
		table.getTableHeader().setDefaultRenderer(encabezadoRenderer);
		table.getTableHeader().setPreferredSize(new Dimension(0, 36));
		table.getTableHeader().setReorderingAllowed(false);

		DefaultTableCellRenderer celdaRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionado,
					boolean foco, int fila, int columna) {
				super.getTableCellRendererComponent(tabla, valor, seleccionado, false, fila, columna);
				setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
				setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
					if (columna == 7 && "Vencida".equals(valor)) {
						setFont(new Font("Segoe UI", Font.BOLD, 13));
					}
					if (columna == 7 && "Pagada".equals(valor)) {
						setForeground(grisMedio);
					}
				}
				if (columna == 6) {
					setHorizontalAlignment(RIGHT);
				} else {
					setHorizontalAlignment(LEFT);
				}
				return this;
			}
		};
		int i = 0;
		while (i < table.getColumnCount()) {
			table.getColumnModel().getColumn(i).setCellRenderer(celdaRenderer);
			i++;
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(160);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(90);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(80);
	}

	private JPanel crearBotones() {
		JPanel barra = new JPanel();
		barra.setBackground(grisClaro);
		barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, grisBorde));
		barra.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 14));

		JButton cancelButton = new JButton("Cerrar");
		estilizarBotonContorno(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		barra.add(cancelButton);

		if (!soloLectura) {
			JButton btnVerificar = new JButton("Verificar suspensiones");
			estilizarBotonContorno(btnVerificar);
			btnVerificar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ejecutarVerificacion();
				}
			});
			barra.add(btnVerificar);
		}

		btnDetalle = new JButton("Ver detalle");
		if (soloLectura) {
			estilizarBotonPrincipal(btnDetalle);
		} else {
			estilizarBotonContorno(btnDetalle);
		}
		btnDetalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verDetalle();
			}
		});
		btnDetalle.setEnabled(false);
		barra.add(btnDetalle);

		if (!soloLectura) {
			btnPagar = new JButton("Marcar como pagada");
			estilizarBotonPrincipal(btnPagar);
			btnPagar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					marcarPagada();
				}
			});
			btnPagar.setEnabled(false);
			barra.add(btnPagar);
		}
		return barra;
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

	private ArrayList<Factura> obtenerFacturas() {
		if (clienteFijo == null) {
			return EmpresaAltice.getInstance().getFacturas();
		}
		ArrayList<Factura> lista = new ArrayList<Factura>();
		ArrayList<Contrato> contratos = clienteFijo.getContratos();
		int i = 0;
		while (i < contratos.size()) {
			ArrayList<Factura> facturas = contratos.get(i).getFacturas();
			int j = 0;
			while (j < facturas.size()) {
				lista.add(facturas.get(j));
				j++;
			}
			i++;
		}
		return lista;
	}

	private boolean cumpleFiltro(Factura f) {
		int filtro = cbxFiltro.getSelectedIndex();
		if (filtro == 1) {
			return !f.isPagada();
		}
		if (filtro == 2) {
			return f.estaVencida();
		}
		if (filtro == 3) {
			return f.isPagada();
		}
		return true;
	}

	private boolean coincide(Factura f, String texto) {
		if (texto.equals("")) {
			return true;
		}
		return f.getIdFactura().toLowerCase().contains(texto)
				|| f.getNcf().toLowerCase().contains(texto)
				|| f.getContrato().getCliente().getNombre().toLowerCase().contains(texto)
				|| f.getContrato().getCliente().getCedula().contains(texto);
	}

	private String textoEstado(Factura f) {
		if (f.isPagada()) {
			return "Pagada";
		}
		if (f.estaVencida()) {
			return "Vencida";
		}
		return "Pendiente";
	}

	private String fecha(LocalDate fecha) {
		if (fecha == null) {
			return "-";
		}
		return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	private void loadFacturas() {
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		String texto = txtBuscar.getText().toLowerCase();
		int mostradas = 0;
		float porCobrar = 0;

		ArrayList<Factura> lista = obtenerFacturas();
		int i = 0;
		while (i < lista.size()) {
			Factura f = lista.get(i);
			if (cumpleFiltro(f) && coincide(f, texto)) {
				row[0] = f.getIdFactura();
				row[1] = f.getNcf();
				row[2] = f.getContrato().getCliente().getNombre();
				row[3] = f.getContrato().getIdContrato();
				row[4] = fecha(f.getFechaEmision());
				row[5] = fecha(f.getFechaVencimiento());
				row[6] = String.format("%,.2f", f.getTotal());
				row[7] = textoEstado(f);
				model.addRow(row);
				mostradas++;
				if (!f.isPagada()) {
					porCobrar = porCobrar + f.getTotal();
				}
			}
			i++;
		}
		lblResumen.setText("Facturas mostradas: " + mostradas + "   |   Pendiente por cobrar: RD$ "
				+ String.format("%,.2f", porCobrar));
	}

	private void seleccionar() {
		int index = table.getSelectedRow();
		if (index > -1) {
			factura = EmpresaAltice.getInstance().buscarFacturaById(model.getValueAt(index, 0).toString());
			if (factura != null) {
				btnDetalle.setEnabled(true);
				if (!soloLectura) {
					btnPagar.setEnabled(!factura.isPagada());
				}
			}
		}
	}

	private void limpiarSeleccion() {
		factura = null;
		btnDetalle.setEnabled(false);
		if (!soloLectura) {
			btnPagar.setEnabled(false);
		}
	}

	private void verDetalle() {
		if (factura == null) {
			return;
		}
		String texto = "Factura: " + factura.getIdFactura()
				+ "\nNCF: " + factura.getNcf()
				+ "\nCliente: " + factura.getContrato().getCliente().getNombre()
				+ "\nContrato: " + factura.getContrato().getIdContrato()
				+ "\nPlan: " + factura.getContrato().getPlan().getNombrePlan()
				+ "\nEmisión: " + fecha(factura.getFechaEmision())
				+ "\nVencimiento: " + fecha(factura.getFechaVencimiento())
				+ "\nEstado: " + textoEstado(factura);
		if (factura.isPagada()) {
			texto = texto + "\nFecha de pago: " + fecha(factura.getFechaPago());
		}
		texto = texto + "\n\nSubtotal: RD$ " + String.format("%,.2f", factura.getSubtotal())
				+ "\nITBIS: RD$ " + String.format("%,.2f", factura.getItbis())
				+ "\nTotal: RD$ " + String.format("%,.2f", factura.getTotal());
		JOptionPane.showMessageDialog(null, texto, "Detalle de la factura", JOptionPane.INFORMATION_MESSAGE);
	}

	private void marcarPagada() {
		if (factura == null) {
			return;
		}
		if (factura.isPagada()) {
			JOptionPane.showMessageDialog(null, "La factura ya está pagada", "Información", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		int option = JOptionPane.showConfirmDialog(null,
				"¿Marcar como pagada la factura " + factura.getIdFactura() + " por RD$ "
						+ String.format("%,.2f", factura.getTotal()) + "?",
				"Confirmación", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			String id = factura.getIdFactura();
			Contrato contrato = factura.getContrato();
			boolean estabaSuspendido = contrato != null && contrato.isSuspendido();

			factura.marcarComoPagada();
			EmpresaAltice.getInstance().guardarDatos();
			loadFacturas();
			limpiarSeleccion();

			String mensaje = "Factura " + id + " marcada como pagada.";
			if (contrato != null) {
				if (!contrato.isActivo()) {
					mensaje = mensaje + "\nEl contrato " + contrato.getIdContrato()
							+ " está cancelado. Puede reactivarse desde el listado de contratos.";
				} else if (estabaSuspendido && !contrato.isSuspendido()) {
					mensaje = mensaje + "\nEl contrato " + contrato.getIdContrato() + " ya no está suspendido.";
				} else if (contrato.isSuspendido()) {
					mensaje = mensaje + "\nEl contrato " + contrato.getIdContrato()
							+ " sigue suspendido (" + contrato.getMotivoSuspension() + ").";
				}
			}
			JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private int contarSuspendidos() {
		ArrayList<Contrato> lista = EmpresaAltice.getInstance().getContratos();
		int cantidad = 0;
		int i = 0;
		while (i < lista.size()) {
			if (lista.get(i).isActivo() && lista.get(i).isSuspendido()) {
				cantidad++;
			}
			i++;
		}
		return cantidad;
	}

	private int contarCancelados() {
		ArrayList<Contrato> lista = EmpresaAltice.getInstance().getContratos();
		int cantidad = 0;
		int i = 0;
		while (i < lista.size()) {
			if (!lista.get(i).isActivo()) {
				cantidad++;
			}
			i++;
		}
		return cantidad;
	}

	private void ejecutarVerificacion() {
		int option = JOptionPane.showConfirmDialog(null,
				"Se revisarán todos los contratos:\n"
						+ "- Postpago: se suspenden con facturas vencidas y se cancelan con 3 facturas vencidas o 3 meses de atraso.\n"
						+ "- Prepago: se suspenden tras " + Contrato.DIAS_INACTIVIDAD_PREPAGO + " días sin actividad.\n\n"
						+ "¿Desea continuar?",
				"Verificar suspensiones", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			EmpresaAltice empresa = EmpresaAltice.getInstance();
			int suspendidosAntes = contarSuspendidos();
			int canceladosAntes = contarCancelados();

			empresa.verificarSuspensiones();
			empresa.guardarDatos();
			loadFacturas();
			limpiarSeleccion();

			JOptionPane.showMessageDialog(null, "Verificación terminada.\n\nContratos suspendidos: " + suspendidosAntes
					+ " -> " + contarSuspendidos() + "\nContratos cancelados: " + canceladosAntes + " -> " + contarCancelados(),
					"Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}