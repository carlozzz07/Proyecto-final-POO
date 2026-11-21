package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logico.Administrativo;
import logico.EmpresaAltice;

public class GenerarFactura extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> cbxMes;
	private JComboBox<Integer> cbxAnio;
	private Administrativo solicitante;

	public GenerarFactura(Administrativo solicitante) {
		this.solicitante = solicitante;
		setTitle("Generar Facturas del Mes");
		setBounds(100, 100, 420, 240);
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

			JLabel lblInfo = new JLabel("Se facturan los contratos activos del período escogido.");
			lblInfo.setBounds(12, 15, 380, 16);
			panel.add(lblInfo);

			JLabel lblMes = new JLabel("Mes:");
			lblMes.setBounds(12, 55, 80, 16);
			panel.add(lblMes);

			cbxMes = new JComboBox<String>();
			cbxMes.setModel(new DefaultComboBoxModel<String>(new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo",
					"Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"}));
			cbxMes.setBounds(12, 75, 180, 24);
			panel.add(cbxMes);

			JLabel lblAnio = new JLabel("Año:");
			lblAnio.setBounds(210, 55, 80, 16);
			panel.add(lblAnio);

			cbxAnio = new JComboBox<Integer>();
			int anioActual = LocalDate.now().getYear();
			cbxAnio.addItem(anioActual - 1);
			cbxAnio.addItem(anioActual);
			cbxAnio.addItem(anioActual + 1);
			cbxAnio.setSelectedItem(anioActual);
			cbxAnio.setBounds(210, 75, 180, 24);
			panel.add(cbxAnio);

			cbxMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Generar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						generar();
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
	}

	private void generar() {
		int mes = cbxMes.getSelectedIndex() + 1;
		int anio = (Integer) cbxAnio.getSelectedItem();

		int cantidad = EmpresaAltice.getInstance().generarFacturasDelMes(solicitante, mes, anio);

		if (cantidad == 0) {
			JOptionPane.showMessageDialog(null, "No se generó ninguna factura. Puede que no haya contratos por facturar\nen ese período o que ya estén facturados.",
					"Información", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Se generaron " + cantidad + " factura(s) del período " + mes + "/" + anio,
					"Información", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}
}