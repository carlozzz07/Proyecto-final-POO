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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

import logico.EmpresaAltice;
import logico.InternetFijo;
import logico.PlanMovil;
import logico.Servicio;

public class ListServicio extends JDialog {
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	private JPanel contentPane;
	private JTable table;
	public static DefaultTableModel model;
	public static Object row[];
	private static JTextField txtBuscar;
	private JButton updatebtn;
	private JButton estadobtn;
	private JButton promobtn;
	private JButton consumobtn;
	private Servicio servicio = null;

	public ListServicio() {
		setTitle("Altice - Listado de Servicios");
		setBounds(100, 100, 1100, 560);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado("Listado de servicios",
				"Internet fijo, plan movil y television, con sus promociones y consumo", 30), BorderLayout.NORTH);
		contentPane.add(crearCentro(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadServicios();
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

		JLabel lblBuscar = new JLabel("BUSCAR (NOMBRE O TIPO)");
		lblBuscar.setForeground(grisOscuro);
		lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 11));
		panelBusqueda.add(lblBuscar, BorderLayout.WEST);

		txtBuscar = new JTextField();
		estilizarCampo(txtBuscar);
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				loadServicios();
				limpiarSeleccion();
			}
		});
		panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

		String headers[] = {"Id", "Tipo", "Nombre", "Detalle", "Precio (RD$)", "Promocion", "Estado"};
		model = new DefaultTableModel() {
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		model.setColumnIdentifiers(headers);

		table = new JTable();
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index > -1) {
					servicio = EmpresaAltice.getInstance().buscarServicioById(model.getValueAt(index, 0).toString());
					actualizarBotones();
				}
			}
		});
		estilizarTabla(table, 4);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(160);
		table.getColumnModel().getColumn(3).setPreferredWidth(330);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(130);
		table.getColumnModel().getColumn(6).setPreferredWidth(70);

		centro.add(crearScroll(table), BorderLayout.CENTER);
		return centro;
	}

	private JPanel crearBotones() {
		JPanel barra = crearBarraBotones();

		JButton cancelButton = new JButton("Cerrar");
		estilizarBotonContorno(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		barra.add(cancelButton);

		consumobtn = new JButton("Consumo");
		estilizarBotonContorno(consumobtn);
		consumobtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarConsumo();
			}
		});
		consumobtn.setEnabled(false);
		barra.add(consumobtn);

		promobtn = new JButton("Promocion");
		estilizarBotonContorno(promobtn);
		promobtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gestionarPromocion();
			}
		});
		promobtn.setEnabled(false);
		barra.add(promobtn);

		estadobtn = new JButton("Activar / Desactivar");
		estilizarBotonContorno(estadobtn);
		estadobtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cambiarEstado();
			}
		});
		estadobtn.setEnabled(false);
		barra.add(estadobtn);

		updatebtn = new JButton("Actualizar");
		estilizarBotonPrincipal(updatebtn);
		updatebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (servicio != null) {
					RegServicio act = new RegServicio(servicio);
					act.setModal(true);
					act.setVisible(true);
					limpiarSeleccion();
				}
			}
		});
		updatebtn.setEnabled(false);
		barra.add(updatebtn);
		return barra;
	}

	private void actualizarBotones() {
		boolean hay = servicio != null;
		updatebtn.setEnabled(hay);
		estadobtn.setEnabled(hay);
		promobtn.setEnabled(hay);
		consumobtn.setEnabled(hay && (servicio instanceof InternetFijo || servicio instanceof PlanMovil));
	}

	private void limpiarSeleccion() {
		table.clearSelection();
		servicio = null;
		actualizarBotones();
	}

	private void cambiarEstado() {
		if (servicio != null) {
			String accion = "desactivar";
			if (!servicio.isActivo()) {
				accion = "activar";
			}
			int option = JOptionPane.showConfirmDialog(null, "Desea " + accion + " el servicio " + servicio.getNombre() + "?",
					"Confirmacion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				EmpresaAltice empresa = EmpresaAltice.getInstance();
				if (empresa.cambiarEstadoServicio(servicio.getIdServicio(), !servicio.isActivo())) {
					empresa.guardarDatos();
					loadServicios();
					limpiarSeleccion();
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo cambiar el estado del servicio", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private JTextField crearCampoDialogo() {
		JTextField campo = new JTextField();
		estilizarCampo(campo);
		return campo;
	}

	private void gestionarPromocion() {
		if (servicio == null) {
			return;
		}
		EmpresaAltice empresa = EmpresaAltice.getInstance();
		if (servicio.isTienePromocion()) {
			int option = JOptionPane.showConfirmDialog(this,
					"Este servicio tiene una promocion de " + String.format("%.0f", servicio.getPorcentajeDescuento())
							+ "% (" + servicio.getMesesPromocionRestantes() + " meses restantes).\nDesea quitarla?",
					"Promocion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				servicio.quitarPromocion();
				empresa.guardarDatos();
				loadServicios();
				limpiarSeleccion();
			}
			return;
		}

		JTextField txtMeses = crearCampoDialogo();
		JTextField txtDescuento = crearCampoDialogo();
		JPanel panel = new JPanel();
		panel.setBackground(blanco);
		panel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));
		panel.add(new JLabel("Servicio: " + servicio.getNombre()));
		panel.add(new JLabel("DURACION (MESES)"));
		panel.add(txtMeses);
		panel.add(new JLabel("DESCUENTO (%)"));
		panel.add(txtDescuento);

		int option = JOptionPane.showConfirmDialog(this, panel, "Activar promocion", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			int meses = 0;
			float descuento = 0;
			try {
				meses = Integer.parseInt(txtMeses.getText().trim());
				descuento = Float.parseFloat(txtDescuento.getText().trim().replace(',', '.'));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Escriba numeros validos para los meses y el descuento", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (servicio.activarPromocion(meses, descuento)) {
				empresa.guardarDatos();
				loadServicios();
				limpiarSeleccion();
				JOptionPane.showMessageDialog(null, "Promocion activada", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,
						"No se pudo activar la promocion.\nEl servicio debe estar activo, los meses deben ser mayores que cero\ny el descuento debe estar entre 0 y 100.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void registrarConsumo() {
		if (servicio == null) {
			return;
		}
		String info = "";
		float limite = 0;
		float consumido = 0;
		if (servicio instanceof InternetFijo) {
			InternetFijo inet = (InternetFijo) servicio;
			limite = inet.getLimiteDatosGB();
			consumido = inet.getDatosConsumidosGB();
			info = "Consumido: " + String.format("%.2f", consumido) + " de " + String.format("%.2f", limite)
					+ " GB. Bajada actual: " + inet.obtenerVelocidadBajadaActual() + " Mbps";
			if (inet.superoLimiteDatos()) {
				info = info + " (reducida)";
			}
		} else if (servicio instanceof PlanMovil) {
			PlanMovil movil = (PlanMovil) servicio;
			limite = movil.getCantidadGigas();
			consumido = movil.getGigasConsumidos();
			info = "Consumido: " + String.format("%.2f", consumido) + " de " + String.format("%.2f", limite)
					+ " GB. Velocidad actual: " + movil.obtenerVelocidadActual() + " Mbps";
			if (movil.superoLimiteDatos()) {
				info = info + " (reducida)";
			}
		} else {
			return;
		}

		JTextField txtGB = crearCampoDialogo();
		JPanel panel = new JPanel();
		panel.setBackground(blanco);
		panel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));
		panel.add(new JLabel("Servicio: " + servicio.getNombre()));
		panel.add(new JLabel(info));
		panel.add(new JLabel("GB A REGISTRAR"));
		panel.add(txtGB);

		Object[] opciones = {"Registrar", "Reiniciar consumo", "Cerrar"};
		int respuesta = JOptionPane.showOptionDialog(this, panel, "Consumo", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		if (respuesta == 0) {
			float gb = 0;
			try {
				gb = Float.parseFloat(txtGB.getText().trim().replace(',', '.'));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Escriba un numero valido de GB", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			boolean ok = false;
			String resultado = "";
			if (servicio instanceof InternetFijo) {
				InternetFijo inet = (InternetFijo) servicio;
				ok = inet.registrarConsumo(gb);
				resultado = "Bajada actual: " + inet.obtenerVelocidadBajadaActual() + " Mbps";
				if (inet.superoLimiteDatos()) {
					resultado = resultado + " (se supero el limite, velocidad reducida)";
				}
			} else {
				PlanMovil movil = (PlanMovil) servicio;
				ok = movil.registrarConsumoGigas(gb);
				resultado = "Velocidad actual: " + movil.obtenerVelocidadActual() + " Mbps";
				if (movil.superoLimiteDatos()) {
					resultado = resultado + " (se supero el limite, velocidad reducida)";
				}
			}
			if (ok) {
				empresa.guardarDatos();
				loadServicios();
				limpiarSeleccion();
				JOptionPane.showMessageDialog(null, "Consumo registrado.\n" + resultado, "Informacion", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Los GB a registrar deben ser mayores que cero", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (respuesta == 1) {
			if (servicio instanceof InternetFijo) {
				((InternetFijo) servicio).reiniciarConsumo();
			} else {
				((PlanMovil) servicio).reiniciarConsumoGigas();
			}
			empresa.guardarDatos();
			loadServicios();
			limpiarSeleccion();
			JOptionPane.showMessageDialog(null, "Consumo reiniciado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void loadServicios() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		String texto = "";
		if (txtBuscar != null) {
			texto = txtBuscar.getText().toLowerCase();
		}

		ArrayList<Servicio> lista = EmpresaAltice.getInstance().getServicios();
		int i = 0;
		while (i < lista.size()) {
			Servicio s = lista.get(i);
			if (texto.equals("") || s.getNombre().toLowerCase().contains(texto)
					|| s.getTipo().toLowerCase().contains(texto)) {
				row[0] = s.getIdServicio();
				row[1] = s.getTipo();
				row[2] = s.getNombre();
				row[3] = s.getDetalle();
				row[4] = String.format("%,.2f", s.getPrecioBase());
				if (s.isTienePromocion()) {
					row[5] = String.format("%.0f", s.getPorcentajeDescuento()) + "% (" + s.getMesesPromocionRestantes() + " meses)";
				} else {
					row[5] = "-";
				}
				if (s.isActivo()) {
					row[6] = "Activo";
				} else {
					row[6] = "Inactivo";
				}
				model.addRow(row);
			}
			i++;
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

	private void estilizarCampo(JTextField campo) {
		campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		campo.setForeground(negro);
		campo.setBackground(grisClaro);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, negro),
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
}