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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logico.Administrativo;
import logico.Contrato;
import logico.EmpresaAltice;

public class AdministrativoVisual extends JFrame {

	private JPanel contentPane;
	private Administrativo administrativo;
	private JLabel lblClientes;
	private JLabel lblContratos;
	private JLabel lblPendientes;
	private JPanel menuLateral;
	private ArrayList<JPanel> modulos = new ArrayList<JPanel>();
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	public AdministrativoVisual(Administrativo administrativo) {
		this.administrativo = administrativo;
		setTitle("Altice - Administrativo");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		setBounds(100, 100, 1120, 700);
		setMinimumSize(new Dimension(1000, 660));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearMenuLateral(), BorderLayout.WEST);

		JPanel derecha = new JPanel();
		derecha.setBackground(blanco);
		derecha.setLayout(new BorderLayout(0, 0));
		derecha.add(crearCentro(), BorderLayout.CENTER);
		derecha.add(crearBarraEstado(), BorderLayout.SOUTH);
		contentPane.add(derecha, BorderLayout.CENTER);

		actualizarResumen();
	}

	// ------------------------------------------------------------------
	// Menu lateral
	// ------------------------------------------------------------------

	private JPanel crearMenuLateral() {
		menuLateral = new JPanel();
		menuLateral.setBackground(negro);
		menuLateral.setPreferredSize(new Dimension(250, 0));
		menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));

		JPanel marca = new JPanel();
		marca.setOpaque(false);
		marca.setLayout(new GridLayout(2, 1, 0, 2));
		marca.setBorder(BorderFactory.createEmptyBorder(22, 22, 18, 22));
		marca.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
		marca.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblLogo = crearLogo(190, 40, 32);
		marca.add(lblLogo);

		JLabel lblRol = new JLabel("Panel administrativo");
		lblRol.setForeground(grisSuave);
		lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		marca.add(lblRol);
		menuLateral.add(marca);

		JLabel lblModulos = new JLabel("MODULOS");
		lblModulos.setForeground(grisMedio);
		lblModulos.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblModulos.setBorder(BorderFactory.createEmptyBorder(6, 22, 8, 0));
		lblModulos.setAlignmentX(Component.LEFT_ALIGNMENT);
		menuLateral.add(lblModulos);

		// Clientes
		JPanel clientes = crearModulo("Clientes");
		agregarOpcion(clientes, "Registrar cliente", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegCliente reg = new RegCliente(null);
				reg.setModal(true);
				reg.setVisible(true);
				actualizarResumen();
			}
		});
		agregarOpcion(clientes, "Listar clientes", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListCliente list = new ListCliente();
				list.setModal(true);
				list.setVisible(true);
				actualizarResumen();
			}
		});

		// Empleados
		JPanel empleados = crearModulo("Empleados");
		agregarOpcion(empleados, "Registrar empleado", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegEmpleado reg = new RegEmpleado(null);
				reg.setModal(true);
				reg.setVisible(true);
			}
		});
		agregarOpcion(empleados, "Listar empleados", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListEmpleado list = new ListEmpleado(AdministrativoVisual.this.administrativo);
				list.setModal(true);
				list.setVisible(true);
			}
		});

		// Servicios
		JPanel servicios = crearModulo("Servicios");
		agregarOpcion(servicios, "Registrar servicio", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegServicio reg = new RegServicio(null);
				reg.setModal(true);
				reg.setVisible(true);
			}
		});
		agregarOpcion(servicios, "Listar servicios", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListServicio list = new ListServicio();
				list.setModal(true);
				list.setVisible(true);
			}
		});

		// Planes
		JPanel planes = crearModulo("Planes");
		agregarOpcion(planes, "Registrar plan", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegPlan reg = new RegPlan(null);
				reg.setModal(true);
				reg.setVisible(true);
			}
		});
		agregarOpcion(planes, "Listar planes", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListPlan list = new ListPlan();
				list.setModal(true);
				list.setVisible(true);
			}
		});

		// Contratos
		JPanel contratos = crearModulo("Contratos");
		agregarOpcion(contratos, "Registrar contrato", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegContrato reg = new RegContrato(null);
				reg.setModal(true);
				reg.setVisible(true);
				actualizarResumen();
			}
		});
		agregarOpcion(contratos, "Listar contratos", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListContrato list = new ListContrato();
				list.setModal(true);
				list.setVisible(true);
				actualizarResumen();
			}
		});

		// Facturas
		JPanel facturas = crearModulo("Facturas");
		agregarOpcion(facturas, "Generar facturas", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GenerarFactura generar = new GenerarFactura(AdministrativoVisual.this.administrativo);
				generar.setModal(true);
				generar.setVisible(true);
				actualizarResumen();
			}
		});

		agregarOpcion(facturas, "Listar facturas", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListFactura list = new ListFactura();
				list.setModal(true);
				list.setVisible(true);
				actualizarResumen();
			}
		});

		// Reportes (pendiente: vista de reportes)
		crearModuloSimple("Reportes", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enDesarrollo("Reportes");
			}
		});

		menuLateral.add(Box.createVerticalGlue());
		menuLateral.add(crearPanelSesion());
		return menuLateral;
	}

	private JPanel crearModulo(String titulo) {
		final JPanel modulo = new JPanel();
		modulo.setOpaque(false);
		modulo.setLayout(new BoxLayout(modulo, BoxLayout.Y_AXIS));
		modulo.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton cabecera = new JButton();
		estilizarBotonModulo(cabecera);
		cabecera.putClientProperty("titulo", titulo);
		cabecera.putClientProperty("conSubmenu", Boolean.TRUE);
		marcarModulo(cabecera, false);
		cabecera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alternarModulo(modulo);
			}
		});

		JPanel submenu = new JPanel();
		submenu.setOpaque(false);
		submenu.setLayout(new BoxLayout(submenu, BoxLayout.Y_AXIS));
		submenu.setAlignmentX(Component.LEFT_ALIGNMENT);
		submenu.setVisible(false);

		modulo.add(cabecera);
		modulo.add(submenu);
		modulos.add(modulo);
		menuLateral.add(modulo);
		return modulo;
	}

	private void crearModuloSimple(String titulo, final ActionListener accion) {
		JPanel modulo = new JPanel();
		modulo.setOpaque(false);
		modulo.setLayout(new BoxLayout(modulo, BoxLayout.Y_AXIS));
		modulo.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton cabecera = new JButton();
		estilizarBotonModulo(cabecera);
		cabecera.putClientProperty("titulo", titulo);
		cabecera.putClientProperty("conSubmenu", Boolean.FALSE);
		marcarModulo(cabecera, false);
		cabecera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cerrarModulos();
				marcarModulo((JButton) e.getSource(), true);
				accion.actionPerformed(e);
			}
		});

		JPanel submenu = new JPanel();
		submenu.setOpaque(false);
		submenu.setVisible(false);

		modulo.add(cabecera);
		modulo.add(submenu);
		modulos.add(modulo);
		menuLateral.add(modulo);
	}

	private void agregarOpcion(JPanel modulo, String texto, ActionListener accion) {
		JButton opcion = new JButton(texto);
		opcion.setBackground(negro);
		opcion.setForeground(grisSuave);
		opcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		opcion.setHorizontalAlignment(SwingConstants.LEFT);
		opcion.setFocusPainted(false);
		opcion.setBorderPainted(false);
		opcion.setOpaque(true);
		opcion.setBorder(BorderFactory.createEmptyBorder(0, 42, 0, 10));
		opcion.setPreferredSize(new Dimension(250, 36));
		opcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		opcion.setAlignmentX(Component.LEFT_ALIGNMENT);
		opcion.setCursor(new Cursor(Cursor.HAND_CURSOR));
		opcion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				opcion.setBackground(grisOscuro);
				opcion.setForeground(blanco);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				opcion.setBackground(negro);
				opcion.setForeground(grisSuave);
			}
		});
		opcion.addActionListener(accion);
		JPanel submenu = (JPanel) modulo.getComponent(1);
		submenu.add(opcion);
	}

	private void estilizarBotonModulo(JButton boton) {
		boton.setBackground(negro);
		boton.setForeground(blanco);
		boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		boton.setHorizontalAlignment(SwingConstants.LEFT);
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setOpaque(true);
		boton.setPreferredSize(new Dimension(250, 44));
		boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		boton.setAlignmentX(Component.LEFT_ALIGNMENT);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setBackground(grisOscuro);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (Boolean.TRUE.equals(boton.getClientProperty("abierto"))) {
					boton.setBackground(grisOscuro);
				} else {
					boton.setBackground(negro);
				}
			}
		});
	}

	private void marcarModulo(JButton cabecera, boolean abierto) {
		String titulo = (String) cabecera.getClientProperty("titulo");
		boolean conSubmenu = Boolean.TRUE.equals(cabecera.getClientProperty("conSubmenu"));
		cabecera.putClientProperty("abierto", Boolean.valueOf(abierto));
		if (conSubmenu) {
			if (abierto) {
				cabecera.setText(titulo + "    -");
			} else {
				cabecera.setText(titulo + "    +");
			}
		} else {
			cabecera.setText(titulo);
		}
		if (abierto) {
			cabecera.setBackground(grisOscuro);
			cabecera.setFont(new Font("Segoe UI", Font.BOLD, 14));
			// Borde compuesto: barra blanca a la izquierda + margen interno mayor,
			// con lo que el texto del modulo seleccionado se corre hacia adelante.
			cabecera.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createMatteBorder(0, 5, 0, 0, blanco),
					BorderFactory.createEmptyBorder(0, 33, 0, 10)));
		} else {
			cabecera.setBackground(negro);
			cabecera.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			cabecera.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 10));
		}
	}

	private void alternarModulo(JPanel elegido) {
		int i = 0;
		while (i < modulos.size()) {
			JPanel modulo = modulos.get(i);
			JButton cabecera = (JButton) modulo.getComponent(0);
			JPanel submenu = (JPanel) modulo.getComponent(1);
			boolean abrir = (modulo == elegido) && !submenu.isVisible();
			submenu.setVisible(abrir);
			marcarModulo(cabecera, abrir);
			i++;
		}
		menuLateral.revalidate();
		menuLateral.repaint();
	}

	private void cerrarModulos() {
		int i = 0;
		while (i < modulos.size()) {
			JPanel modulo = modulos.get(i);
			((JPanel) modulo.getComponent(1)).setVisible(false);
			marcarModulo((JButton) modulo.getComponent(0), false);
			i++;
		}
		menuLateral.revalidate();
		menuLateral.repaint();
	}

	private JPanel crearPanelSesion() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 22, 22, 22));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

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
		panel.add(btnCerrarSesion);
		panel.add(Box.createVerticalStrut(8));

		JButton btnSalir = new JButton("Salir");
		estilizarBotonContorno(btnSalir);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salir();
			}
		});
		panel.add(btnSalir);
		return panel;
	}

	private void estilizarBotonContorno(JButton boton) {
		boton.setBackground(negro);
		boton.setForeground(blanco);
		boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		boton.setFocusPainted(false);
		boton.setOpaque(true);
		boton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(grisMedio, 1),
				BorderFactory.createEmptyBorder(7, 14, 7, 14)));
		boton.setPreferredSize(new Dimension(206, 38));
		boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
		boton.setAlignmentX(Component.LEFT_ALIGNMENT);
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

	// ------------------------------------------------------------------
	// Panel principal
	// ------------------------------------------------------------------

	private JPanel crearCentro() {
		JPanel centro = new JPanel();
		centro.setBackground(blanco);
		centro.setBorder(BorderFactory.createEmptyBorder(44, 50, 40, 50));
		centro.setLayout(new BorderLayout(0, 0));

		JPanel panelTitulo = new JPanel();
		panelTitulo.setOpaque(false);
		panelTitulo.setLayout(new GridLayout(2, 1, 0, 4));
		centro.add(panelTitulo, BorderLayout.NORTH);

		JLabel lblBienvenida = new JLabel("Bienvenido, " + administrativo.getNombre());
		lblBienvenida.setForeground(negro);
		lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 30));
		panelTitulo.add(lblBienvenida);

		JLabel lblSubtitulo = new JLabel("Panel de administracion");
		lblSubtitulo.setForeground(grisMedio);
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		panelTitulo.add(lblSubtitulo);

		JPanel panelTarjetas = new JPanel();
		panelTarjetas.setOpaque(false);
		panelTarjetas.setLayout(new GridLayout(1, 3, 24, 0));

		lblClientes = new JLabel();
		panelTarjetas.add(crearTarjeta("Clientes registrados", lblClientes));
		lblContratos = new JLabel();
		panelTarjetas.add(crearTarjeta("Contratos activos", lblContratos));
		lblPendientes = new JLabel();
		panelTarjetas.add(crearTarjeta("Facturas pendientes", lblPendientes));

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
		JLabel lblEstado = new JLabel("Administrativo: " + administrativo.getNombre()
				+ "   |   Departamento: " + administrativo.getDepartamento()
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
		lblValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
		tarjeta.add(lblValor);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(grisMedio);
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tarjeta.add(lblTitulo);
		return tarjeta;
	}

	private int contarContratosActivos() {
		ArrayList<Contrato> lista = EmpresaAltice.getInstance().getContratos();
		int cantidad = 0;
		int i = 0;
		while (i < lista.size()) {
			if (lista.get(i).isActivo()) {
				cantidad++;
			}
			i++;
		}
		return cantidad;
	}

	private void actualizarResumen() {
		EmpresaAltice empresa = EmpresaAltice.getInstance();
		lblClientes.setText(String.valueOf(empresa.getClientes().size()));
		lblContratos.setText(String.valueOf(contarContratosActivos()));
		lblPendientes.setText(String.valueOf(empresa.getFacturasPendientes().size()));
	}

	private void enDesarrollo(String modulo) {
		JOptionPane.showMessageDialog(this, modulo + " todavia no esta disponible (en desarrollo)",
				"Altice", JOptionPane.INFORMATION_MESSAGE);
	}

	private void salir() {
		int option = JOptionPane.showConfirmDialog(null, "Desea salir del sistema?", "Salir", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			EmpresaAltice.getInstance().guardarDatos();
			System.exit(0);
		}
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
}