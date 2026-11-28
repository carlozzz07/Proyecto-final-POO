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
import java.text.Normalizer;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import logico.Empleado;
import logico.EmpresaAltice;

public class ListEmpleado extends JDialog {
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
	private Empleado empleado = null;
	private Empleado sesion;

	/**
	 * @param sesion empleado que inicio sesion (puede ser null). Se usa para
	 *               que nadie pueda desactivar su propia cuenta.
	 */
	public ListEmpleado(Empleado sesion) {
		this.sesion = sesion;
		setTitle("Altice - Listado de Empleados");
		setBounds(100, 100, 960, 560);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado("Listado de empleados",
				"Seleccione un empleado para actualizarlo o cambiar su estado", 30), BorderLayout.NORTH);
		contentPane.add(crearCentro(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		loadEmpleados();
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

		JLabel lblBuscar = new JLabel("BUSCAR (NOMBRE O CEDULA)");
		lblBuscar.setForeground(grisOscuro);
		lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 11));
		panelBusqueda.add(lblBuscar, BorderLayout.WEST);

		txtBuscar = new JTextField();
		estilizarCampo(txtBuscar);
		txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				filtrar();
			}

			public void removeUpdate(DocumentEvent e) {
				filtrar();
			}

			public void changedUpdate(DocumentEvent e) {
				filtrar();
			}
		});
		panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

		String headers[] = {"Id", "Cedula", "Nombre", "Tipo", "Telefono", "Sueldo (RD$)", "Estado"};
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
					updatebtn.setEnabled(true);
					estadobtn.setEnabled(true);
					empleado = EmpresaAltice.getInstance().buscarEmpleadoByCedula(model.getValueAt(index, 1).toString());
				}
			}
		});
		estilizarTabla(table, 5);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(220);
		table.getColumnModel().getColumn(3).setPreferredWidth(110);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(80);

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
				if (empleado != null) {
					RegEmpleado act = new RegEmpleado(empleado);
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

	private void filtrar() {
		loadEmpleados();
		limpiarSeleccion();
	}

	private void limpiarSeleccion() {
		table.clearSelection();
		empleado = null;
		updatebtn.setEnabled(false);
		estadobtn.setEnabled(false);
	}

	private void cambiarEstado() {
		if (empleado != null) {
			if (sesion != null && sesion.getCedula().equals(empleado.getCedula()) && empleado.isEstado()) {
				JOptionPane.showMessageDialog(null, "No puede desactivar su propia cuenta", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String accion = "desactivar";
			if (!empleado.isEstado()) {
				accion = "activar";
			}
			int option = JOptionPane.showConfirmDialog(null, "Desea " + accion + " al empleado " + empleado.getNombre() + "?",
					"Confirmacion", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				EmpresaAltice empresa = EmpresaAltice.getInstance();
				if (empresa.cambiarEstadoEmpleado(empleado.getCedula(), !empleado.isEstado())) {
					empresa.guardarDatos();
					loadEmpleados();
					limpiarSeleccion();
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo cambiar el estado del empleado", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public static void loadEmpleados() {
		if (model == null) {
			return;
		}
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];

		String texto = "";
		if (txtBuscar != null) {
			texto = txtBuscar.getText().toLowerCase();
		}

		ArrayList<Empleado> lista = EmpresaAltice.getInstance().getEmpleados();
		int i = 0;
		while (i < lista.size()) {
			Empleado emp = lista.get(i);
			if (coincide(texto, emp.getNombre(), emp.getCedula(), emp.getId(), emp.getTipo())) {
				row[0] = emp.getId();
				row[1] = emp.getCedula();
				row[2] = emp.getNombre();
				row[3] = emp.getTipo();
				row[4] = emp.getTelefono();
				row[5] = String.format("%,.2f", emp.calcularSueldo());
				if (emp.isEstado()) {
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
					if ("Inactivo".equals(valor)) {
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

	private static String normalizar(String texto) {
		String limpio = Normalizer.normalize(texto, Normalizer.Form.NFD);
		return limpio.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	}

	/**
	 * Devuelve true si todas las palabras escritas en la busqueda aparecen en
	 * alguno de los campos (sin importar mayusculas, tildes ni el orden).
	 */
	private static boolean coincide(String busqueda, String... campos) {
		String[] palabras = normalizar(busqueda).trim().split("\\s+");
		String todo = "";
		int i = 0;
		while (i < campos.length) {
			todo = todo + " " + normalizar(campos[i]);
			i++;
		}
		int j = 0;
		while (j < palabras.length) {
			if (!todo.contains(palabras[j])) {
				return false;
			}
			j++;
		}
		return true;
	}
}