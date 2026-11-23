package visual;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import logico.Comercial;

public class ComercialVisual extends JFrame {

	private JPanel contentPane;
	private Comercial comercial;

	public ComercialVisual(Comercial comercial) {
		this.comercial = comercial;
		setTitle("Altice - Comercial");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnCliente = new JMenu("Cliente");
		menuBar.add(mnCliente);

		JMenuItem mntmRegCliente = new JMenuItem("Registrar");
		mntmRegCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegCliente reg = new RegCliente(null);
				reg.setModal(true);
				reg.setVisible(true);
			}
		});
		mnCliente.add(mntmRegCliente);

		JMenuItem mntmListCliente = new JMenuItem("Listar");
		mntmListCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListCliente list = new ListCliente();
				list.setModal(true);
				list.setVisible(true);
			}
		});
		mnCliente.add(mntmListCliente);

		JMenu mnSesion = new JMenu("Sesión");
		menuBar.add(mnSesion);

		JMenuItem mntmCerrarSesion = new JMenuItem("Cerrar sesión");
		mntmCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
				login.setVisible(true);
				dispose();
			}
		});
		mnSesion.add(mntmCerrarSesion);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "¿Desea salir del sistema?", "Salir", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		mnSesion.add(mntmSalir);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panelCentro = new JPanel();
		panelCentro.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCentro.setLayout(new BorderLayout(0, 0));
		contentPane.add(panelCentro, BorderLayout.CENTER);

		JLabel lblBienvenida = new JLabel("Bienvenido");
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		panelCentro.add(lblBienvenida, BorderLayout.CENTER);

		JPanel panelSur = new JPanel();
		panelSur.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(panelSur, BorderLayout.SOUTH);

		JLabel lblEstado = new JLabel(textoEstado());
		panelSur.add(lblEstado);
	}

	private String textoEstado() {
		String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String meta = "meta pendiente";
		if (comercial.cumplioMeta()) {
			meta = "meta cumplida";
		}
		return "Comercial: " + comercial.getNombre()
				+ "   |   Ventas del mes: " + String.format("%,.2f", comercial.getVentasDelMes())
				+ " de " + String.format("%,.2f", comercial.getMetaMensual()) + " (" + meta + ")"
				+ "   |   Fecha: " + fecha;
	}
}