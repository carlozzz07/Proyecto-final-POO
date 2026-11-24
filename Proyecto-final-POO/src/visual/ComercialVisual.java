package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logico.Comercial;
import logico.EmpresaAltice;

public class ComercialVisual extends JFrame {

	private JPanel contentPane;
	private Comercial comercial;
	private JLabel lblVentas;
	private JLabel lblMeta;
	private JLabel lblEstadoMeta;
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	public ComercialVisual(Comercial comercial) {
		this.comercial = comercial;
		setTitle("Altice - Comercial");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		setBounds(100, 100, 1000, 640);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(), BorderLayout.NORTH);
		contentPane.add(crearCentro(), BorderLayout.CENTER);
		contentPane.add(crearBarraEstado(), BorderLayout.SOUTH);
		actualizarResumen();
	}

	private JPanel crearEncabezado() {
		JPanel encabezado = new JPanel();
		encabezado.setBackground(negro);
		encabezado.setLayout(new BorderLayout(0, 0));

		JPanel panelIzquierdo = new JPanel();
		panelIzquierdo.setOpaque(false);
		panelIzquierdo.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 16));
		encabezado.add(panelIzquierdo, BorderLayout.WEST);

		JLabel lblLogo = new JLabel("altice");
		lblLogo.setForeground(blanco);
		lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 24));
		panelIzquierdo.add(lblLogo);

		JButton btnRegCliente = new JButton("Registrar cliente");
		estilizarBotonMenu(btnRegCliente);
		btnRegCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegCliente reg = new RegCliente(null);
				reg.setModal(true);
				reg.setVisible(true);
				actualizarResumen();
			}
		});
		panelIzquierdo.add(btnRegCliente);

		JButton btnListCliente = new JButton("Listar clientes");
		estilizarBotonMenu(btnListCliente);
		btnListCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListCliente list = new ListCliente();
				list.setModal(true);
				list.setVisible(true);
				actualizarResumen();
			}
		});
		panelIzquierdo.add(btnListCliente);

		JPanel panelDerecho = new JPanel();
		panelDerecho.setOpaque(false);
		panelDerecho.setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 16));
		encabezado.add(panelDerecho, BorderLayout.EAST);

		JButton btnCerrarSesion = new JButton("Cerrar sesion");
		estilizarBotonContorno(btnCerrarSesion);
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmpresaAltice.getInstance().guardarDatos();
				Login login = new Login();
				login.setVisible(true);
				dispose();
			}
		});
		panelDerecho.add(btnCerrarSesion);

		JButton btnSalir = new JButton("Salir");
		estilizarBotonContorno(btnSalir);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		panelDerecho.add(btnSalir);
		return encabezado;
	}

	private JPanel crearCentro() {
		JPanel centro = new JPanel();
		centro.setBackground(blanco);
		centro.setBorder(BorderFactory.createEmptyBorder(44, 60, 40, 60));
		centro.setLayout(new BorderLayout(0, 0));

		JPanel panelTitulo = new JPanel();
		panelTitulo.setOpaque(false);
		panelTitulo.setLayout(new GridLayout(2, 1, 0, 4));
		centro.add(panelTitulo, BorderLayout.NORTH);

		JLabel lblBienvenida = new JLabel("Bienvenido, " + comercial.getNombre());
		lblBienvenida.setForeground(negro);
		lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 30));
		panelTitulo.add(lblBienvenida);

		JLabel lblSubtitulo = new JLabel("Panel comercial");
		lblSubtitulo.setForeground(grisMedio);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		panelTitulo.add(lblSubtitulo);

		JPanel panelTarjetas = new JPanel();
		panelTarjetas.setOpaque(false);
		panelTarjetas.setLayout(new GridLayout(1, 3, 24, 0));

		lblVentas = new JLabel();
		panelTarjetas.add(crearTarjeta("Ventas del mes", lblVentas));
		lblMeta = new JLabel();
		panelTarjetas.add(crearTarjeta("Meta mensual", lblMeta));
		lblEstadoMeta = new JLabel();
		panelTarjetas.add(crearTarjeta("Estado de la meta", lblEstadoMeta));

		JPanel contenedorTarjetas = new JPanel();
		contenedorTarjetas.setOpaque(false);
		contenedorTarjetas.setBorder(BorderFactory.createEmptyBorder(36, 0, 0, 0));
		contenedorTarjetas.setLayout(new BorderLayout(0, 0));
		contenedorTarjetas.add(panelTarjetas, BorderLayout.NORTH);
		centro.add(contenedorTarjetas, BorderLayout.CENTER);
		return centro;
	}

	private JPanel crearBarraEstado() {
		JPanel barra = new JPanel();
		barra.setBackground(grisClaro);
		barra.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, grisBorde));
		barra.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 6));

		String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		JLabel lblEstado = new JLabel("Comercial: " + comercial.getNombre()
				+ "   |   Fecha: " + fecha);
		lblEstado.setForeground(grisOscuro);
		lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		barra.add(lblEstado);
		return barra;
	}

	private JPanel crearTarjeta(String titulo, JLabel lblValor) {
		JPanel tarjeta = new JPanel();
		tarjeta.setBackground(grisClaro);
		tarjeta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(4, 0, 0, 0, negro),
				BorderFactory.createEmptyBorder(18, 22, 18, 22)));
		tarjeta.setLayout(new GridLayout(2, 1, 0, 6));

		lblValor.setForeground(negro);
		lblValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
		tarjeta.add(lblValor);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(grisMedio);
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tarjeta.add(lblTitulo);
		return tarjeta;
	}

	private void estilizarBotonMenu(JButton boton) {
		boton.setBackground(negro);
		boton.setForeground(blanco);
		boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setOpaque(true);
		boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setBackground(grisOscuro);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(negro);
			}
		});
	}

	private void estilizarBotonContorno(JButton boton) {
		estilizarBotonMenu(boton);
		boton.setBorderPainted(true);
		boton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(grisMedio, 1),
				BorderFactory.createEmptyBorder(7, 14, 7, 14)));
	}

	private void actualizarResumen() {
		lblVentas.setText("RD$ " + String.format("%,.2f", comercial.getVentasDelMes()));
		lblMeta.setText("RD$ " + String.format("%,.2f", comercial.getMetaMensual()));
		if (comercial.cumplioMeta()) {
			lblEstadoMeta.setText("Cumplida");
		} else {
			lblEstadoMeta.setText("Pendiente");
		}
	}

	private void salir() {
		int option = JOptionPane.showConfirmDialog(null, "Desea salir del sistema?", "Salir", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			EmpresaAltice.getInstance().guardarDatos();
			System.exit(0);
		}
	}
}
