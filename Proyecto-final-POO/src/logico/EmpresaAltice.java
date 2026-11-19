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
		if (persona != null && !this.personas.contains(persona)) {
			this.personas.add(persona);
			if (persona instanceof Cliente) {
				generadorIdCliente++;
			} else if (persona instanceof Empleado) {
				generadorIdEmpleado++;
			}
		}
	}

	public void registrarServicio(Servicio servicio) {
		if (servicio != null && !this.servicios.contains(servicio)) {
			this.servicios.add(servicio);
			generadorIdServicio++;
		}
	}

	public void registrarPlan(Plan plan) {
		if (plan != null && !this.planes.contains(plan)) {
			this.planes.add(plan);
			generadorIdPlan++;
		}
	}

	public void registrarContrato(Contrato contrato) {
		if (contrato != null && !this.contratos.contains(contrato)) {
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
		if (factura != null && !this.facturas.contains(factura)) {
			this.facturas.add(factura);
			generadorIdFactura++;

			if (factura.getContrato() != null) {
				factura.getContrato().agregarFactura(factura);
			}
		}
	}

	public int generarFacturasDelMes(int mes, int anio) {
		int generadas = 0;
		LocalDate fechaEmision = LocalDate.of(anio, mes, 1);

		for (Contrato contrato : contratos) {
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
		for (Contrato contrato : contratos) {
			contrato.verificarSuspension();
		}
	}

	public Cliente buscarClienteByCedula(String cedula) {
		if (cedula == null) {
			return null;
		}
		for (Persona persona : personas) {
			if (persona instanceof Cliente && persona.getCedula().equalsIgnoreCase(cedula)) {
				return (Cliente) persona;
			}
		}
		return null;
	}

	public Empleado buscarEmpleadoByCedula(String cedula) {
		if (cedula == null) {
			return null;
		}
		for (Persona persona : personas) {
			if (persona instanceof Empleado && persona.getCedula().equalsIgnoreCase(cedula)) {
				return (Empleado) persona;
			}
		}
		return null;
	}

	public Contrato buscarContratoById(String id) {
		if (id == null) {
			return null;
		}
		for (Contrato contrato : contratos) {
			if (contrato.getIdContrato().equalsIgnoreCase(id)) {
				return contrato;
			}
		}
		return null;
	}

	public ArrayList<Cliente> getClientes() {
		ArrayList<Cliente> listaClientes = new ArrayList<>();
		for (Persona persona : personas) {
			if (persona instanceof Cliente) {
				listaClientes.add((Cliente) persona);
			}
		}
		return listaClientes;
	}

	public ArrayList<Empleado> getEmpleados() {
		ArrayList<Empleado> listaEmpleados = new ArrayList<>();
		for (Persona persona : personas) {
			if (persona instanceof Empleado) {
				listaEmpleados.add((Empleado) persona);
			}
		}
		return listaEmpleados;
	}

	public float totalFacturadoEnPeriodo(LocalDate inicio, LocalDate fin) {
		float total = 0.0f;
		if (inicio == null || fin == null) {
			return total;
		}
		for (Factura factura : facturas) {
			LocalDate emision = factura.getFechaEmision();
			if (emision != null && !emision.isBefore(inicio) && !emision.isAfter(fin)) {
				total += factura.getTotal();
			}
		}
		return total;
	}

	public int cantidadContratosPorModalidad(boolean esPrepago) {
		int cantidad = 0;
		for (Contrato contrato : contratos) {
			if (contrato.isActivo() && contrato.esPrepago() == esPrepago) {
				cantidad++;
			}
		}
		return cantidad;
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
}