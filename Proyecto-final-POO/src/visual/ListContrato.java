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
import java.time.format.DateTimeFormatter;
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

import logico.Contrato;
import logico.EmpresaAltice;

public class ListContrato extends JDialog {
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
	private JButton cancelarbtn;
	private JButton reactivarbtn;
	private JButton recargabtn;
	private Contrato contrato = null;

	public ListContrato() {
		setTitle("Altice - Listado de Contratos");
		setBounds(100, 100, 1100, 560);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado("Listado de contratos",
				"Seleccione un contrato para cancelarlo, reactivarlo o registrar una recarga", 30), BorderLayout.NORTH);
		contentPane.add(crearCentro(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadContratos();
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

		JLabel lblBuscar = new JLabel("BUSCAR (ID, CLIENTE O CEDULA)");
		lblBuscar.setForeground(grisOscuro);
		lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 11));
		panelBusqueda.add(lblBuscar, BorderLayout.WEST);

		txtBuscar = new JTextField();
		estilizarCampo(txtBuscar);
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				loadContratos();
				limpiarSeleccion();
			}
		});
		panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

		String headers[] = {"Id", "Cliente", "Plan", "Pago", "Comercial", "Inicio", "Estado", "Deuda (RD$)"};
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
					contrato = EmpresaAltice.getInstance().buscarContratoById(model.getValueAt(index, 0).toString());
					actualizarBotones();
				}
			}
		});
		estilizarTabla(table, 7);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(190);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(90);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);

		centro.add(crearScroll(table), BorderLayout.CENTER);
		return centro;
	}

	private JPanel crearBotones() {
		JPanel barra = crearBarraBotones();

		JButton cerrarButton = new JButton("Cerrar");
		estilizarBotonContorno(cerrarButton);
		cerrarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		barra.add(cerrarButton);

		JButton verificarbtn = new JButton("Verificar suspensiones");
		estilizarBotonContorno(verificarbtn);
		verificarbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verificarSuspensiones();
			}
		});
		barra.add(verificarbtn);

		recargabtn = new JButton("Recarga prepago");
		estilizarBotonContorno(recargabtn);
		recargabtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarRecarga();
			}
		});
		recargabtn.setEnabled(false);
		barra.add(recargabtn);

		reactivarbtn = new JButton("Reactivar");
		estilizarBotonContorno(reactivarbtn);
		reactivarbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reactivar();
			}
		});
		reactivarbtn.setEnabled(false);
		barra.add(reactivarbtn);

		cancelarbtn = new JButton("Cancelar contrato");
		estilizarBotonPrincipal(cancelarbtn);
		cancelarbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar();
			}
		});
		cancelarbtn.setEnabled(false);
		barra.add(cancelarbtn);
		return barra;
	}

	private void actualizarBotones() {
		boolean hay = contrato != null;
		cancelarbtn.setEnabled(hay && contrato.isActivo());
		reactivarbtn.setEnabled(hay && !contrato.isActivo());
		recargabtn.setEnabled(hay && contrato.isActivo() && contrato.esPrepago());
	}

	private void limpiarSeleccion() {
		table.clearSelection();
		contrato = null;
		actualizarBotones();
	}

	private void cancelar() {
		if (contrato != null) {
			int option = JOptionPane.showConfirmDialog(null, "Desea cancelar el contrato " + contrato.getIdContrato()
					+ " de " + contrato.getCliente().getNombre() + "?", "Confirmacion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				EmpresaAltice empresa = EmpresaAltice.getInstance();
				if (contrato.cancelar()) {
					empresa.guardarDatos();
					loadContratos();
					limpiarSeleccion();
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo cancelar el contrato", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void reactivar() {
		if (contrato != null) {
			if (!contrato.getCliente().isEstado() || !contrato.getPlan().isActivo()) {
				JOptionPane.showMessageDialog(null, "No se puede reactivar: el cliente o el plan estan inactivos", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			int option = JOptionPane.showConfirmDialog(null, "Desea reactivar el contrato " + contrato.getIdContrato() + "?",
					"Confirmacion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				EmpresaAltice empresa = EmpresaAltice.getInstance();
				if (contrato.reactivar()) {
					empresa.guardarDatos();
					loadContratos();
					limpiarSeleccion();
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo reactivar el contrato", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void registrarRecarga() {
		if (contrato != null) {
			int option = JOptionPane.showConfirmDialog(null, "Registrar una recarga del plan " + contrato.getPlan().getNombrePlan()
					+ " para " + contrato.getCliente().getNombre() + "?", "Confirmacion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				EmpresaAltice empresa = EmpresaAltice.getInstance();
				if (empresa.registrarRecargaPrepago(contrato.getIdContrato())) {
					empresa.guardarDatos();
					loadContratos();
					limpiarSeleccion();
					JOptionPane.showMessageDialog(null, "Recarga registrada", "Informacion", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo registrar la recarga", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void verificarSuspensiones() {
		int option = JOptionPane.showConfirmDialog(null,
				"Se revisaran todos los contratos: los de pospago con facturas vencidas se suspenden\n"
						+ "y los que llevan 3 meses sin pagar se cancelan. Desea continuar?",
				"Verificar suspensiones", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			EmpresaAltice empresa = EmpresaAltice.getInstance();
			empresa.verificarSuspensiones();
			empresa.guardarDatos();
			loadContratos();
			limpiarSeleccion();
			JOptionPane.showMessageDialog(null, "Verificacion terminada", "Informacion", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static void loadContratos() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		String texto = "";
		if (txtBuscar != null) {
			texto = txtBuscar.getText().toLowerCase();
		}
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		ArrayList<Contrato> lista = EmpresaAltice.getInstance().getContratos();
		int i = 0;
		while (i < lista.size()) {
			Contrato c = lista.get(i);
			if (texto.equals("") || c.getIdContrato().toLowerCase().contains(texto)
					|| c.getCliente().getNombre().toLowerCase().contains(texto)
					|| c.getCliente().getCedula().contains(texto)) {
				row[0] = c.getIdContrato();
				row[1] = c.getCliente().getNombre();
				row[2] = c.getPlan().getNombrePlan();
				row[3] = c.getTipoPago();
				if (c.getComercial() != null) {
					row[4] = c.getComercial().getNombre();
				} else {
					row[4] = "-";
				}
				row[5] = c.getFechaInicio().format(formato);
				if (!c.isActivo()) {
					row[6] = "Cancelado";
				} else if (c.isSuspendido()) {
					row[6] = "Suspendido";
				} else {
					row[6] = "Activo";
				}
				row[7] = String.format("%,.2f", c.calcularDeudaPendiente());
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