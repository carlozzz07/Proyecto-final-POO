package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class EmpresaAltice {

	private static EmpresaAltice instancia = null;

	private ArrayList<Persona> personas;
	private ArrayList<Servicio> servicios;
	private ArrayList<Plan> planes;
	private ArrayList<Contrato> contratos;
	private ArrayList<Factura> facturas;

	public static int generadorIdCliente = 1;
	public static int generadorIdEmpleado = 1;
	public static int generadorIdServicio = 1;
	public static int generadorIdPlan = 1;
	public static int generadorIdContrato = 1;
	public static int generadorIdFactura = 1;

	private EmpresaAltice() {
		super();
		this.personas = new ArrayList<>();
		this.servicios = new ArrayList<>();
		this.planes = new ArrayList<>();
		this.contratos = new ArrayList<>();
		this.facturas = new ArrayList<>();
	}

	public static EmpresaAltice getInstance() {
		if (instancia == null) {
			instancia = new EmpresaAltice();
		}
		return instancia;
	}

	public ArrayList<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(ArrayList<Persona> personas) {
		this.personas = personas;
	}

	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<Servicio> servicios) {
		this.servicios = servicios;
	}

	public ArrayList<Plan> getPlanes() {
		return planes;
	}

	public void setPlanes(ArrayList<Plan> planes) {
		this.planes = planes;
	}

	public ArrayList<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<Contrato> contratos) {
		this.contratos = contratos;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}

	public void registrarPersona(Persona persona) {
		if (persona != null) {
			if (persona instanceof Cliente && buscarClienteByCedula(persona.getCedula()) == null) {
				this.personas.add(persona);
				generadorIdCliente++;
			} else if (persona instanceof Empleado && buscarEmpleadoByCedula(persona.getCedula()) == null) {
				this.personas.add(persona);
				generadorIdEmpleado++;
			}
		}
	}

	public void registrarServicio(Servicio servicio) {
		if (servicio != null && buscarServicioById(servicio.getIdServicio()) == null) {
			this.servicios.add(servicio);
			generadorIdServicio++;
		}
	}

	public void registrarPlan(Plan plan) {
		if (plan != null && buscarPlanById(plan.getIdPlan()) == null) {
			this.planes.add(plan);
			generadorIdPlan++;
		}
	}

	public void registrarContrato(Contrato contrato) {
		if (contrato != null && buscarContratoById(contrato.getIdContrato()) == null) {
			boolean clienteActivo = contrato.getCliente() != null && contrato.getCliente().isEstado();
			boolean planActivo = contrato.getPlan() != null && contrato.getPlan().isActivo();
			boolean comercialActivo = contrato.getComercial() == null || contrato.getComercial().isEstado();

			if (clienteActivo && planActivo && comercialActivo) {
				this.contratos.add(contrato);
				generadorIdContrato++;

				contrato.getCliente().agregarContrato(contrato);

				if (contrato.getComercial() != null) {
					contrato.getComercial().registrarVenta(contrato.getPlan().calcularPrecio());
				}

				if (contrato.esPrepago()) {
					String idFac = "FAC-" + generadorIdFactura;
					String ncf = String.format("B01%08d", generadorIdFactura);
					Factura facturaInicial = new Factura(idFac, ncf, contrato.getFechaInicio(), contrato);
					registrarFactura(facturaInicial);
				}
			}
		}
	}

	public void registrarFactura(Factura factura) {
		if (factura != null && buscarFacturaById(factura.getIdFactura()) == null) {
			this.facturas.add(factura);
			generadorIdFactura++;

			if (factura.getContrato() != null) {
				factura.getContrato().agregarFactura(factura);
			}
		}
	}

	public int generarFacturasDelMes(Empleado solicitante, int mes, int anio) {
		int generadas = 0;
		if (solicitante instanceof Administrativo && solicitante.isEstado() && mes >= 1 && mes <= 12) {
			LocalDate fechaEmision = LocalDate.of(anio, mes, 1);
			int i = 0;
			while (i < personas.size()) {
				if (personas.get(i) instanceof Cliente) {
					Cliente cliente = (Cliente) personas.get(i);
					ArrayList<Contrato> contratosCliente = cliente.getContratos();
					int j = 0;
					while (j < contratosCliente.size()) {
						Contrato contrato = contratosCliente.get(j);
						if (contrato.isActivo() && !contrato.esPrepago()
								&& !contrato.tieneFacturaEnPeriodo(mes, anio)) {
							String idFac = "FAC-" + generadorIdFactura;
							String ncf = String.format("B01%08d", generadorIdFactura);
							Factura nuevaFactura = new Factura(idFac, ncf, fechaEmision, contrato);
							registrarFactura(nuevaFactura);
							generadas++;
						}
						j++;
					}
				}
				i++;
			}
		}
		return generadas;
	}

	public boolean registrarRecargaPrepago(String idContrato) {
		Contrato contrato = buscarContratoById(idContrato);
		if (contrato != null && contrato.isActivo() && contrato.esPrepago()) {
			String idFac = "FAC-" + generadorIdFactura;
			String ncf = String.format("B01%08d", generadorIdFactura);
			Factura facturaRecarga = new Factura(idFac, ncf, LocalDate.now(), contrato);
			registrarFactura(facturaRecarga);
			return true;
		}
		return false;
	}

	public void verificarSuspensiones() {
		int i = 0;
		while (i < contratos.size()) {
			contratos.get(i).verificarSuspension();
			i++;
		}
	}

	public boolean cambiarEstadoCliente(String cedula, boolean estado) {
		Cliente cliente = buscarClienteByCedula(cedula);
		if (cliente != null) {
			cliente.setEstado(estado);
			return true;
		}
		return false;
	}

	public boolean cambiarEstadoEmpleado(String cedula, boolean estado) {
		Empleado empleado = buscarEmpleadoByCedula(cedula);
		if (empleado != null) {
			empleado.setEstado(estado);
			return true;
		}
		return false;
	}

	public boolean cambiarEstadoServicio(String idServicio, boolean activo) {
		Servicio servicio = buscarServicioById(idServicio);
		if (servicio != null) {
			if (activo) {
				return servicio.activar();
			} else {
				return servicio.darDeBaja();
			}
		}
		return false;
	}

	public boolean cambiarEstadoPlan(String idPlan, boolean activo) {
		Plan plan = buscarPlanById(idPlan);
		if (plan != null) {
			if (activo) {
				return plan.activar();
			} else {
				return plan.desactivar();
			}
		}
		return false;
	}

	public boolean cambiarEstadoContrato(String idContrato, boolean activo) {
		Contrato contrato = buscarContratoById(idContrato);
		if (contrato != null) {
			if (activo) {
				return contrato.reactivar();
			} else {
				return contrato.cancelar();
			}
		}
		return false;
	}

	public Cliente buscarClienteByCedula(String cedula) {
		Cliente cliAux = null;
		int i = 0;
		boolean encontrado = false;
		if (cedula != null) {
			while (!encontrado && i < personas.size()) {
				Persona persona = personas.get(i);
				if (persona instanceof Cliente && cedula.equalsIgnoreCase(persona.getCedula())) {
					cliAux = (Cliente) persona;
					encontrado = true;
				}
				i++;
			}
		}
		return cliAux;
	}

	public Empleado buscarEmpleadoByCedula(String cedula) {
		Empleado empAux = null;
		int i = 0;
		boolean encontrado = false;
		if (cedula != null) {
			while (!encontrado && i < personas.size()) {
				Persona persona = personas.get(i);
				if (persona instanceof Empleado && cedula.equalsIgnoreCase(persona.getCedula())) {
					empAux = (Empleado) persona;
					encontrado = true;
				}
				i++;
			}
		}
		return empAux;
	}

	public Servicio buscarServicioById(String idServicio) {
		Servicio servAux = null;
		int i = 0;
		boolean encontrado = false;
		if (idServicio != null) {
			while (!encontrado && i < servicios.size()) {
				if (idServicio.equalsIgnoreCase(servicios.get(i).getIdServicio())) {
					servAux = servicios.get(i);
					encontrado = true;
				}
				i++;
			}
		}
		return servAux;
	}

	public Plan buscarPlanById(String idPlan) {
		Plan planAux = null;
		int i = 0;
		boolean encontrado = false;
		if (idPlan != null) {
			while (!encontrado && i < planes.size()) {
				if (idPlan.equalsIgnoreCase(planes.get(i).getIdPlan())) {
					planAux = planes.get(i);
					encontrado = true;
				}
				i++;
			}
		}
		return planAux;
	}

	public Contrato buscarContratoById(String id) {
		Contrato contAux = null;
		int i = 0;
		boolean encontrado = false;
		if (id != null) {
			while (!encontrado && i < contratos.size()) {
				if (id.equalsIgnoreCase(contratos.get(i).getIdContrato())) {
					contAux = contratos.get(i);
					encontrado = true;
				}
				i++;
			}
		}
		return contAux;
	}

	public Factura buscarFacturaById(String idFactura) {
		Factura factAux = null;
		int i = 0;
		boolean encontrado = false;
		if (idFactura != null) {
			while (!encontrado && i < facturas.size()) {
				if (idFactura.equalsIgnoreCase(facturas.get(i).getIdFactura())) {
					factAux = facturas.get(i);
					encontrado = true;
				}
				i++;
			}
		}
		return factAux;
	}

	public ArrayList<Cliente> getClientes() {
		ArrayList<Cliente> listaClientes = new ArrayList<>();
		int i = 0;
		while (i < personas.size()) {
			if (personas.get(i) instanceof Cliente) {
				listaClientes.add((Cliente) personas.get(i));
			}
			i++;
		}
		return listaClientes;
	}

	public ArrayList<Empleado> getEmpleados() {
		ArrayList<Empleado> listaEmpleados = new ArrayList<>();
		int i = 0;
		while (i < personas.size()) {
			if (personas.get(i) instanceof Empleado) {
				listaEmpleados.add((Empleado) personas.get(i));
			}
			i++;
		}
		return listaEmpleados;
	}

	public ArrayList<Factura> getFacturasPendientes() {
		ArrayList<Factura> pendientes = new ArrayList<>();
		int i = 0;
		while (i < facturas.size()) {
			if (!facturas.get(i).isPagada()) {
				pendientes.add(facturas.get(i));
			}
			i++;
		}
		return pendientes;
	}

	public String generarReportePeriodoYModalidad(LocalDate inicio, LocalDate fin, boolean esPrepago) {
		float totalFacturado = 0;
		int cantidadContratos = 0;
		String modalidad = "Postpago";
		if (esPrepago) {
			modalidad = "Prepago";
		}
		String periodo = "No especificado";
		if (inicio != null && fin != null) {
			periodo = inicio + " al " + fin;
			int i = 0;
			while (i < facturas.size()) {
				Factura factura = facturas.get(i);
				LocalDate emision = factura.getFechaEmision();
				if (emision != null && !emision.isBefore(inicio) && !emision.isAfter(fin)) {
					if (factura.getContrato() != null && factura.getContrato().esPrepago() == esPrepago) {
						totalFacturado += factura.getTotal();
					}
				}
				i++;
			}
		}

		int j = 0;
		while (j < contratos.size()) {
			Contrato contrato = contratos.get(j);
			if (contrato.isActivo() && contrato.esPrepago() == esPrepago) {
				cantidadContratos++;
			}
			j++;
		}

		return "Modalidad: " + modalidad
				+ "\nPeriodo: " + periodo
				+ "\nContratos activos: " + cantidadContratos
				+ "\nTotal facturado: RD$" + String.format("%.2f", totalFacturado);
	}

}