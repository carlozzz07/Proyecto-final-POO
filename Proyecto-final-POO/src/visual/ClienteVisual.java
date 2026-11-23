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

import logico.Cliente;

public class ClienteVisual extends JFrame {

	private JPanel contentPane;
	private Cliente cliente;

	public ClienteVisual(Cliente cliente) {
		this.cliente = cliente;
		setTitle("Altice - Cliente");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnMiCuenta = new JMenu("Mi Cuenta");
		menuBar.add(mnMiCuenta);

		JMenuItem mntmMisContratos = new JMenuItem("Mis contratos");
		mntmMisContratos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnMiCuenta.add(mntmMisContratos);

		JMenuItem mntmMisFacturas = new JMenuItem("Mis facturas");
		mntmMisFacturas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnMiCuenta.add(mntmMisFacturas);

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
		return "Cliente: " + cliente.getNombre() + "   |   Fecha: " + fecha;
	}
}