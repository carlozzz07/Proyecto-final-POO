package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.Cliente;
import logico.EmpresaAltice;

public class ListCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	public static DefaultTableModel model;
	public static Object row[];
	private static JTextField txtBuscar;
	private JButton updatebtn;
	private JButton estadobtn;
	private Cliente cliente = null;

	public ListCliente() {
		setTitle("Listado de Clientes");
		setBounds(100, 100, 850, 449);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panelBusqueda = new JPanel();
				panelBusqueda.setLayout(new FlowLayout(FlowLayout.LEFT));
				panel.add(panelBusqueda, BorderLayout.NORTH);

				JLabel lblBuscar = new JLabel("Buscar (nombre o cédula):");
				panelBusqueda.add(lblBuscar);

				txtBuscar = new JTextField();
				txtBuscar.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						loadClientes();
						cliente = null;
						updatebtn.setEnabled(false);
						estadobtn.setEnabled(false);
					}
				});
				panelBusqueda.add(txtBuscar);
				txtBuscar.setColumns(20);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				panel.add(scrollPane, BorderLayout.CENTER);
				{
					String headers[] = {"Id", "Cédula", "Nombre", "Teléfono", "Dirección", "Deuda", "Estado"};
					model = new DefaultTableModel() {
						public boolean isCellEditable(int fila, int columna) {
							return false;
						}
					};
					model.setColumnIdentifiers(headers);
					table = new JTable();
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
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					table.setModel(model);
					scrollPane.setViewportView(table);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				updatebtn = new JButton("Actualizar");
				updatebtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						RegCliente act = new RegCliente(cliente);
						act.setModal(true);
						act.setVisible(true);
					}
				});
				updatebtn.setEnabled(false);
				buttonPane.add(updatebtn);
			}
			{
				estadobtn = new JButton("Activar / Desactivar");
				estadobtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cambiarEstado();
					}
				});
				estadobtn.setEnabled(false);
				buttonPane.add(estadobtn);
			}
			{
				JButton cancelButton = new JButton("Cerrar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		loadClientes();
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
				if (EmpresaAltice.getInstance().cambiarEstadoCliente(cliente.getId(), !cliente.isEstado())) {
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