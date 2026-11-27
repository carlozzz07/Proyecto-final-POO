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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logico.EmpresaAltice;
import logico.InternetFijo;
import logico.PlanMovil;
import logico.Servicio;
import logico.Television;

public class RegServicio extends JDialog {
	private Color negro = new Color(17, 17, 17);
	private Color grisOscuro = new Color(51, 51, 51);
	private Color grisMedio = new Color(110, 110, 110);
	private Color grisSuave = new Color(170, 170, 170);
	private Color grisBorde = new Color(220, 220, 220);
	private Color grisClaro = new Color(242, 242, 242);
	private Color blanco = Color.WHITE;

	private JPanel contentPane;
	private JTextField txtId;
	private JComboBox<String> cbxTipo;
	private JTextField txtNombre;
	private JTextField txtPrecio;
	private JTextField txtDescripcion;
	private JLabel lblExtra1;
	private JLabel lblExtra2;
	private JLabel lblExtra3;
	private JLabel lblExtra4;
	private JTextField txtExtra1;
	private JTextField txtExtra2;
	private JTextField txtExtra3;
	private JTextField txtExtra4;
	private JCheckBox chkOpcion;
	private Servicio servicio = null;

	public RegServicio(Servicio aux) {
		String titulo = "Registrar servicio";
		String subtitulo = "Complete los datos del nuevo servicio";
		if (aux != null) {
			servicio = aux;
			titulo = "Modificar servicio";
			subtitulo = "Actualice los datos del servicio";
			setTitle("Altice - Modificar Servicio");
		} else {
			setTitle("Altice - Registrar Servicio");
		}
		setBounds(100, 100, 600, 670);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(blanco);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		contentPane.add(crearEncabezado(titulo, subtitulo, 40), BorderLayout.NORTH);
		contentPane.add(crearFormulario(), BorderLayout.CENTER);
		contentPane.add(crearBotones(), BorderLayout.SOUTH);
		actualizarCampos();
		loadServicio();
	}

	private JPanel crearFormulario() {
		JPanel panel = new JPanel();
		panel.setBackground(blanco);
		panel.setLayout(null);

		panel.add(crearEtiqueta("ID", 40, 24, 240));
		txtId = new JTextField();
		estilizarCampo(txtId);
		bloquearCampo(txtId);
		txtId.setBounds(40, 44, 240, 36);
		panel.add(txtId);

		panel.add(crearEtiqueta("TIPO DE SERVICIO", 320, 24, 240));
		cbxTipo = new JComboBox<String>();
		cbxTipo.addItem("Internet Fijo");
		cbxTipo.addItem("Plan Movil");
		cbxTipo.addItem("Television");
		estilizarCombo(cbxTipo);
		cbxTipo.setBounds(320, 44, 240, 36);
		cbxTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarCampos();
				txtExtra1.setText("");
				txtExtra2.setText("");
				txtExtra3.setText("");
				txtExtra4.setText("");
				chkOpcion.setSelected(false);
			}
		});
		panel.add(cbxTipo);

		panel.add(crearEtiqueta("NOMBRE", 40, 96, 520));
		txtNombre = new JTextField();
		estilizarCampo(txtNombre);
		txtNombre.setBounds(40, 116, 520, 36);
		panel.add(txtNombre);

		panel.add(crearEtiqueta("PRECIO BASE (RD$)", 40, 168, 240));
		txtPrecio = new JTextField();
		estilizarCampo(txtPrecio);
		txtPrecio.setBounds(40, 188, 240, 36);
		panel.add(txtPrecio);

		panel.add(crearEtiqueta("DESCRIPCION (OPCIONAL)", 320, 168, 240));
		txtDescripcion = new JTextField();
		estilizarCampo(txtDescripcion);
		txtDescripcion.setBounds(320, 188, 240, 36);
		panel.add(txtDescripcion);

		agregarSeccion(panel, "CARACTERISTICAS", 40, 246, 520);

		lblExtra1 = crearEtiqueta("", 40, 286, 240);
		panel.add(lblExtra1);
		txtExtra1 = new JTextField();
		estilizarCampo(txtExtra1);
		txtExtra1.setBounds(40, 306, 240, 36);
		panel.add(txtExtra1);

		lblExtra2 = crearEtiqueta("", 320, 286, 240);
		panel.add(lblExtra2);
		txtExtra2 = new JTextField();
		estilizarCampo(txtExtra2);
		txtExtra2.setBounds(320, 306, 240, 36);
		panel.add(txtExtra2);

		lblExtra3 = crearEtiqueta("", 40, 358, 240);
		panel.add(lblExtra3);
		txtExtra3 = new JTextField();
		estilizarCampo(txtExtra3);
		txtExtra3.setBounds(40, 378, 240, 36);
		panel.add(txtExtra3);

		lblExtra4 = crearEtiqueta("", 320, 358, 240);
		panel.add(lblExtra4);
		txtExtra4 = new JTextField();
		estilizarCampo(txtExtra4);
		txtExtra4.setBounds(320, 378, 240, 36);
		panel.add(txtExtra4);

		chkOpcion = new JCheckBox();
		estilizarCheck(chkOpcion);
		chkOpcion.setBounds(40, 436, 300, 26);
		panel.add(chkOpcion);
		return panel;
	}

	private JPanel crearBotones() {
		JPanel barra = crearBarraBotones();

		JButton cancelButton = new JButton("Cancelar");
		estilizarBotonContorno(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		barra.add(cancelButton);

		JButton okButton = new JButton("Registrar");
		if (servicio != null) {
			okButton.setText("Modificar");
		}
		estilizarBotonPrincipal(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		barra.add(okButton);
		getRootPane().setDefaultButton(okButton);
		return barra;
	}

	private void actualizarCampos() {
		String tipo = (String) cbxTipo.getSelectedItem();
		boolean cuatroCampos = true;
		if ("Internet Fijo".equals(tipo)) {
			lblExtra1.setText("BAJADA (MBPS)");
			lblExtra2.setText("SUBIDA (MBPS)");
			lblExtra3.setText("LIMITE DE DATOS (GB)");
			lblExtra4.setText("BAJADA REDUCIDA (MBPS)");
			chkOpcion.setText("Incluye router");
		} else if ("Plan Movil".equals(tipo)) {
			lblExtra1.setText("GIGAS INCLUIDOS");
			lblExtra2.setText("MINUTOS LIBRES");
			lblExtra3.setText("VELOCIDAD NORMAL (MBPS)");
			lblExtra4.setText("VELOCIDAD REDUCIDA (MBPS)");
			chkOpcion.setText("Redes sociales ilimitadas");
		} else {
			lblExtra1.setText("CANTIDAD DE CANALES");
			lblExtra2.setText("CAJILLAS EXTRA");
			lblExtra3.setText("");
			lblExtra4.setText("");
			chkOpcion.setText("Incluye HD");
			cuatroCampos = false;
		}
		lblExtra3.setVisible(cuatroCampos);
		txtExtra3.setVisible(cuatroCampos);
		lblExtra4.setVisible(cuatroCampos);
		txtExtra4.setVisible(cuatroCampos);
	}

	private boolean estaVacio(String texto) {
		if (texto == null) {
			return true;
		}
		int i = 0;
		while (i < texto.length()) {
			if (texto.charAt(i) != ' ') {
				return false;
			}
			i++;
		}
		return true;
	}

	private Float leerNumero(String texto) {
		try {
			float valor = Float.parseFloat(texto.trim().replace(',', '.'));
			if (Float.isNaN(valor) || Float.isInfinite(valor)) {
				return null;
			}
			return valor;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Integer leerEntero(String texto) {
		try {
			return Integer.valueOf(texto.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void guardar() {
		String nombre = txtNombre.getText().trim();
		String descripcion = txtDescripcion.getText().trim();
		Float precio = leerNumero(txtPrecio.getText());
		String tipo = (String) cbxTipo.getSelectedItem();
		boolean opcion = chkOpcion.isSelected();

		if (estaVacio(nombre)) {
			mostrarError("Debe indicar el nombre del servicio");
			return;
		}
		if (precio == null || precio <= 0) {
			mostrarError("El precio base debe ser un numero mayor que cero");
			return;
		}

		Servicio nuevo = null;
		String id = txtId.getText();

		if ("Internet Fijo".equals(tipo)) {
			Integer bajada = leerEntero(txtExtra1.getText());
			Integer subida = leerEntero(txtExtra2.getText());
			Float limite = leerNumero(txtExtra3.getText());
			Integer reducida = leerEntero(txtExtra4.getText());
			if (bajada == null || bajada <= 0) {
				mostrarError("La bajada debe ser un numero entero mayor que cero");
				return;
			}
			if (subida == null || subida <= 0) {
				mostrarError("La subida debe ser un numero entero mayor que cero");
				return;
			}
			if (limite == null || limite <= 0) {
				mostrarError("El limite de datos debe ser un numero mayor que cero");
				return;
			}
			if (reducida == null || reducida <= 0 || reducida > bajada) {
				mostrarError("La bajada reducida debe ser mayor que cero y no superar la bajada normal");
				return;
			}
			if (servicio == null) {
				nuevo = new InternetFijo(id, nombre, descripcion, precio, bajada, subida, opcion, limite, reducida);
			} else {
				InternetFijo inet = (InternetFijo) servicio;
				inet.setMegasBajada(bajada);
				inet.setMegasSubida(subida);
				inet.setLimiteDatosGB(limite);
				inet.setMegasBajadaReducida(reducida);
				inet.setIncluyeRouter(opcion);
			}
		} else if ("Plan Movil".equals(tipo)) {
			Integer gigas = leerEntero(txtExtra1.getText());
			Integer minutos = leerEntero(txtExtra2.getText());
			Integer normal = leerEntero(txtExtra3.getText());
			Integer reducida = leerEntero(txtExtra4.getText());
			if (gigas == null || gigas < 0) {
				mostrarError("Los gigas deben ser un numero entero mayor o igual a cero");
				return;
			}
			if (minutos == null || minutos < 0) {
				mostrarError("Los minutos libres deben ser un numero entero mayor o igual a cero");
				return;
			}
			if (normal == null || normal <= 0) {
				mostrarError("La velocidad normal debe ser un numero entero mayor que cero");
				return;
			}
			if (reducida == null || reducida <= 0 || reducida > normal) {
				mostrarError("La velocidad reducida debe ser mayor que cero y no superar la velocidad normal");
				return;
			}
			if (servicio == null) {
				nuevo = new PlanMovil(id, nombre, descripcion, precio, gigas, minutos, opcion, normal, reducida);
			} else {
				PlanMovil movil = (PlanMovil) servicio;
				movil.setCantidadGigas(gigas);
				movil.setMinutosLibres(minutos);
				movil.setVelocidadNormalMbps(normal);
				movil.setVelocidadReducidaMbps(reducida);
				movil.setRedesSocialesIlimitadas(opcion);
			}
		} else {
			Integer canales = leerEntero(txtExtra1.getText());
			Integer cajillas = leerEntero(txtExtra2.getText());
			if (canales == null || canales <= 0) {
				mostrarError("La cantidad de canales debe ser un numero entero mayor que cero");
				return;
			}
			if (cajillas == null || cajillas < 0) {
				mostrarError("Las cajillas extra deben ser un numero entero mayor o igual a cero");
				return;
			}
			if (servicio == null) {
				nuevo = new Television(id, nombre, descripcion, precio, canales, opcion, cajillas);
			} else {
				Television tv = (Television) servicio;
				tv.setCantidadCanales(canales);
				tv.setCajillasExtra(cajillas);
				tv.setIncluyeHD(opcion);
			}
		}

		EmpresaAltice empresa = EmpresaAltice.getInstance();
		if (servicio == null) {
			empresa.registrarServicio(nuevo);
			if (empresa.buscarServicioById(id) == null) {
				mostrarError("No se pudo registrar el servicio");
				return;
			}
			empresa.guardarDatos();
			ListServicio.loadServicios();
			JOptionPane.showMessageDialog(null, "Registro satisfactorio", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			clean();
		} else {
			servicio.setNombre(nombre);
			servicio.setDescripcion(descripcion);
			servicio.setPrecioBase(precio);
			empresa.guardarDatos();
			ListServicio.loadServicios();
			JOptionPane.showMessageDialog(null, "Modificacion satisfactoria", "Informacion", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private void loadServicio() {
		if (servicio != null) {
			txtId.setText(servicio.getIdServicio());
			cbxTipo.setSelectedItem(servicio.getTipo());
			cbxTipo.setEnabled(false);
			txtNombre.setText(servicio.getNombre());
			txtPrecio.setText(String.valueOf(servicio.getPrecioBase()));
			txtDescripcion.setText(servicio.getDescripcion());
			if (servicio instanceof InternetFijo) {
				InternetFijo inet = (InternetFijo) servicio;
				txtExtra1.setText(String.valueOf(inet.getMegasBajada()));
				txtExtra2.setText(String.valueOf(inet.getMegasSubida()));
				txtExtra3.setText(String.valueOf(inet.getLimiteDatosGB()));
				txtExtra4.setText(String.valueOf(inet.getMegasBajadaReducida()));
				chkOpcion.setSelected(inet.isIncluyeRouter());
			} else if (servicio instanceof PlanMovil) {
				PlanMovil movil = (PlanMovil) servicio;
				txtExtra1.setText(String.valueOf(movil.getCantidadGigas()));
				txtExtra2.setText(String.valueOf(movil.getMinutosLibres()));
				txtExtra3.setText(String.valueOf(movil.getVelocidadNormalMbps()));
				txtExtra4.setText(String.valueOf(movil.getVelocidadReducidaMbps()));
				chkOpcion.setSelected(movil.isRedesSocialesIlimitadas());
			} else if (servicio instanceof Television) {
				Television tv = (Television) servicio;
				txtExtra1.setText(String.valueOf(tv.getCantidadCanales()));
				txtExtra2.setText(String.valueOf(tv.getCajillasExtra()));
				chkOpcion.setSelected(tv.isIncluyeHD());
			}
		} else {
			clean();
		}
	}

	private void clean() {
		txtId.setText("S-" + EmpresaAltice.generadorIdServicio);
		txtNombre.setText("");
		txtPrecio.setText("");
		txtDescripcion.setText("");
		txtExtra1.setText("");
		txtExtra2.setText("");
		txtExtra3.setText("");
		txtExtra4.setText("");
		chkOpcion.setSelected(false);
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

	private JLabel crearEtiqueta(String texto, int x, int y, int ancho) {
		JLabel etiqueta = new JLabel(texto);
		etiqueta.setForeground(grisOscuro);
		etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 11));
		etiqueta.setBounds(x, y, ancho, 16);
		return etiqueta;
	}

	private void agregarSeccion(JPanel panel, String texto, int x, int y, int ancho) {
		JPanel linea = new JPanel();
		linea.setBackground(grisBorde);
		linea.setBounds(x, y, ancho, 1);
		panel.add(linea);

		JLabel lblSeccion = new JLabel(texto);
		lblSeccion.setForeground(negro);
		lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblSeccion.setBounds(x, y + 10, ancho, 18);
		panel.add(lblSeccion);
	}

	private void estilizarCampo(JTextField campo) {
		campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		campo.setForeground(negro);
		campo.setBackground(grisClaro);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, negro),
				BorderFactory.createEmptyBorder(4, 10, 4, 10)));
	}

	private void bloquearCampo(JTextField campo) {
		campo.setEditable(false);
		campo.setBackground(grisBorde);
		campo.setForeground(grisMedio);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, grisSuave),
				BorderFactory.createEmptyBorder(4, 10, 4, 10)));
	}

	private void estilizarCombo(JComboBox<?> combo) {
		combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		combo.setForeground(negro);
		combo.setBackground(grisClaro);
		combo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, negro));
		combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	private void estilizarCheck(JCheckBox check) {
		check.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		check.setForeground(negro);
		check.setBackground(blanco);
		check.setFocusPainted(false);
		check.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
}