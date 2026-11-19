package logico;

import java.util.ArrayList;

public class EmpresaAltice {

	private static EmpresaAltice instancia;

	private ArrayList<Cliente> clientes;
	private ArrayList<Factura> facturas;
	private ArrayList<Servicio> servicios;
	private ArrayList<Empleado> personal;
	public static int generadorIdCliente = 1;
	public static int generadorIdFactura = 1;
	public static int generadorIdServicio = 1;
	public static int generadorIdPersonal = 1;

	private EmpresaAltice() {
		super();
		this.clientes = new ArrayList<>();
		this.facturas = new ArrayList<>();
		this.servicios = new ArrayList<>();
		this.personal = new ArrayList<>();
	}

	public static EmpresaAltice getInstance() {
		if (instancia == null) {
			instancia = new EmpresaAltice();
		}
		return instancia;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(ArrayList<Cliente> clientes) {
		this.clientes = clientes;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}

	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<Servicio> servicios) {
		this.servicios = servicios;
	}

	public ArrayList<Empleado> getPersonal() {
		return personal;
	}

	public void setPersonal(ArrayList<Empleado> personal) {
		this.personal = personal;
	}
}