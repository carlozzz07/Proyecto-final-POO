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

import logico.Cliente;
import logico.EmpresaAltice;

public class ListCliente extends JDialog {

	private JPanel contentPane;
	private JTable table;
	public static DefaultTableModel model;
	public static Object row[];
	private static JTextField txtBuscar;
	private JButton updatebtn;
	private JButton estadobtn;
	private Cliente cliente = null;
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	public ListCliente() {
		setTitle("Altice - Listado de Clientes");
		setBounds(100, 100, 960, 560);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(), BorderLayout.NORTH);
		contentPane.add(crearCentro(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadClientes();
	}

	private JPanel crearEncabezado() {
		JPanel encabezado = new JPanel();
		encabezado.setBackground(negro);
		encabezado.setLayout(new BorderLayout(0, 0));
		encabezado.setBorder(BorderFactory.createEmptyBorder(18, 30, 18, 30));

		JPanel textos = new JPanel();
		textos.setOpaque(false);
		textos.setLayout(new GridLayout(2, 1, 0, 2));
		encabezado.add(textos, BorderLayout.WEST);

		JLabel lblTitulo = new JLabel("Listado de clientes");
		lblTitulo.setForeground(blanco);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		textos.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel("Seleccione un cliente para actualizarlo o cambiar su estado");
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

		JLabel lblBuscar = new JLabel("BUSCAR (NOMBRE O CÉDULA)");
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
				loadClientes();
				cliente = null;
				updatebtn.setEnabled(false);
				estadobtn.setEnabled(false);
			}
		});
		panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

		String headers[] = {"Id", "Cédula", "Nombre", "Teléfono", "Dirección", "Deuda", "Estado"};
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
				int index = table.getSelectedRow();
				if (index > -1) {
					updatebtn.setEnabled(true);
					estadobtn.setEnabled(true);
					cliente = EmpresaAltice.getInstance().buscarClienteByCedula(model.getValueAt(index, 1).toString());
				}
			}
		});
		estilizarTabla();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createLineBorder(grisBorde, 1));
		scrollPane.getViewport().setBackground(blanco);
		scrollPane.setViewportView(table);
		centro.add(scrollPane, BorderLayout.CENTER);
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
					if (columna == 6 && "Inactivo".equals(valor)) {
						setForeground(grisSuave);
					}
				}
				if (columna == 5) {
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
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.getColumnModel().getColumn(5).setPreferredWidth(90);
		table.getColumnModel().getColumn(6).setPreferredWidth(80);
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
				RegCliente act = new RegCliente(cliente);
				act.setModal(true);
				act.setVisible(true);
			}
		});
		updatebtn.setEnabled(false);
		barra.add(updatebtn);
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

	private void cambiarEstado() {
		if (cliente != null) {
			String accion = "desactivar";
			if (!cliente.isEstado()) {
				accion = "activar";
			}
			int option = JOptionPane.showConfirmDialog(null, "¿Desea " + accion + " al cliente " + cliente.getNombre() + "?",
					"Confirmación", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				if (EmpresaAltice.getInstance().cambiarEstadoCliente(cliente.getCedula(), !cliente.isEstado())) {
					loadClientes();
					cliente = null;
					updatebtn.setEnabled(false);
					estadobtn.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo cambiar el estado del cliente", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public static void loadClientes() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		String texto = "";
		if (txtBuscar != null) {
			texto = txtBuscar.getText().toLowerCase();
		}

		ArrayList<Cliente> lista = EmpresaAltice.getInstance().getClientes();
		int i = 0;
		while (i < lista.size()) {
			Cliente c = lista.get(i);
			if (texto.equals("") || c.getNombre().toLowerCase().contains(texto) || c.getCedula().contains(texto)) {
				row[0] = c.getId();
				row[1] = c.getCedula();
				row[2] = c.getNombre();
				row[3] = c.getTelefono();
				row[4] = c.getDireccion();
				row[5] = String.format("%,.2f", c.calcularDeudaTotal());
				if (c.isEstado()) {
					row[6] = "Activo";
				} else {
					row[6] = "Inactivo";
				}
				model.addRow(row);
			}
			i++;
		}
	}
}