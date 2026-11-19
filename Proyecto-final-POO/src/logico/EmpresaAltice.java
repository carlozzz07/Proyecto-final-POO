package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class EmpresaAltice {

	private static EmpresaAltice instancia;

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

	public void registrarPersona(Persona persona) {
		if (persona == null) {
			return;
		}
		if (persona instanceof Cliente && buscarClienteByCedula(persona.getCedula()) == null) {
			this.personas.add(persona);
			generadorIdCliente++;
		} else if (persona instanceof Empleado && buscarEmpleadoByCedula(persona.getCedula()) == null) {
			this.personas.add(persona);
			generadorIdEmpleado++;
		}
	}

	public void registrarServicio(Servicio servicio) {
		if (servicio != null) {
			this.servicios.add(servicio);
			generadorIdServicio++;
		}
	}

	public void registrarPlan(Plan plan) {
		if (plan != null) {
			this.planes.add(plan);
			generadorIdPlan++;
		}
	}

	public void registrarContrato(Contrato contrato) {
		if (contrato != null && buscarContratoById(contrato.getIdContrato()) == null) {
			this.contratos.add(contrato);
			generadorIdContrato++;

			if (contrato.getCliente() != null) {
				contrato.getCliente().agregarContrato(contrato);
			}

			if (contrato.getComercial() != null && contrato.getPlan() != null) {
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

	public void registrarFactura(Factura factura) {
		if (factura != null) {
			this.facturas.add(factura);
			generadorIdFactura++;

			if (factura.getContrato() != null) {
				factura.getContrato().agregarFactura(factura);
			}
		}
	}

	public int generarFacturasDelMes(int mes, int anio) {
		if (mes < 1 || mes > 12) {
			return 0;
		}
		int generadas = 0;
		LocalDate fechaEmision = LocalDate.of(anio, mes, 1);

		for (int i = 0; i < contratos.size(); i++) {
			Contrato contrato = contratos.get(i);
			if (contrato.isActivo() && !contrato.esPrepago() && !contrato.tieneFacturaEnPeriodo(mes, anio)) {
				String idFac = "FAC-" + generadorIdFactura;
				String ncf = String.format("B01%08d", generadorIdFactura);
				Factura nuevaFactura = new Factura(idFac, ncf, fechaEmision, contrato);
				registrarFactura(nuevaFactura);
				generadas++;
			}
		}
		return generadas;
	}

	public void verificarSuspensiones() {
		for (int i = 0; i < contratos.size(); i++) {
			contratos.get(i).verificarSuspension();
		}
	}

	public Cliente buscarClienteByCedula(String cedula) {
		if (cedula == null) {
			return null;
		}
		for (int i = 0; i < personas.size(); i++) {
			Persona persona = personas.get(i);
			if (persona instanceof Cliente && cedula.equalsIgnoreCase(persona.getCedula())) {
				return (Cliente) persona;
			}
		}
		return null;
	}

	public Empleado buscarEmpleadoByCedula(String cedula) {
		if (cedula == null) {
			return null;
		}
		for (int i = 0; i < personas.size(); i++) {
			Persona persona = personas.get(i);
			if (persona instanceof Empleado && cedula.equalsIgnoreCase(persona.getCedula())) {
				return (Empleado) persona;
			}
		}
		return null;
	}

	public Contrato buscarContratoById(String id) {
		if (id == null) {
			return null;
		}
		for (int i = 0; i < contratos.size(); i++) {
			if (id.equalsIgnoreCase(contratos.get(i).getIdContrato())) {
				return contratos.get(i);
			}
		}
		return null;
	}

	public ArrayList<Cliente> getClientes() {
		ArrayList<Cliente> listaClientes = new ArrayList<>();
		for (int i = 0; i < personas.size(); i++) {
			if (personas.get(i) instanceof Cliente) {
				listaClientes.add((Cliente) personas.get(i));
			}
		}
		return listaClientes;
	}

	public ArrayList<Empleado> getEmpleados() {
		ArrayList<Empleado> listaEmpleados = new ArrayList<>();
		for (int i = 0; i < personas.size(); i++) {
			if (personas.get(i) instanceof Empleado) {
				listaEmpleados.add((Empleado) personas.get(i));
			}
		}
		return listaEmpleados;
	}

	public float totalFacturadoEnPeriodo(LocalDate inicio, LocalDate fin) {
		float total = 0.0f;
		if (inicio == null || fin == null) {
			return total;
		}
		for (int i = 0; i < facturas.size(); i++) {
			LocalDate emision = facturas.get(i).getFechaEmision();
			if (emision != null && !emision.isBefore(inicio) && !emision.isAfter(fin)) {
				total += facturas.get(i).getTotal();
			}
		}
		return total;
	}

	public int cantidadContratosPorModalidad(boolean esPrepago) {
		int cantidad = 0;
		for (int i = 0; i < contratos.size(); i++) {
			if (contratos.get(i).isActivo() && contratos.get(i).esPrepago() == esPrepago) {
				cantidad++;
			}
		}
		return cantidad;
	}
}