package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Factura {

	private String codigo;
	private Cliente cliente;
	private ArrayList<Servicio> servicios;
	private int mes;
	private int anio;
	private LocalDate fechaEmision;
	private float monto;
	private boolean pagada;
	private LocalDate fechaPago;

	Factura(String codigo, Cliente cliente, ArrayList<Servicio> servicios, int mes, int anio) {
		super();
		this.codigo = codigo;
		this.cliente = cliente;
		this.servicios = new ArrayList<Servicio>(servicios);
		this.mes = mes;
		this.anio = anio;
		this.fechaEmision = LocalDate.now();
		this.monto = calcularMonto();
		this.pagada = false;
		this.fechaPago = null;
	}

	public String getCodigo() {
		return codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public ArrayList<Servicio> getServicios() {
		return new ArrayList<Servicio>(servicios);
	}

	public int cantidadServicios() {
		return servicios.size();
	}

	public int getMes() {
		return mes;
	}

	public int getAnio() {
		return anio;
	}

	public String getPeriodo() {
		return mes + "/" + anio;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public float getMonto() {
		return monto;
	}

	public boolean isPagada() {
		return pagada;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public boolean perteneceAlPeriodo(int mes, int anio) {
		return this.mes == mes && this.anio == anio;
	}
	
	private float calcularMonto() {
		
		float total = 0;
		int i = 0;
		
		while (i < servicios.size()) {
			total = total + servicios.get(i).getPrecioMensual();
			i++;
		}
		return total;
	}

	boolean registrarPago() {
		
		if (pagada) {
			return false;
		}
		pagada = true;
		fechaPago = LocalDate.now();
		return true;
	}

	public boolean esValido() {
		return !codigo.equals("")&& cliente != null&& servicios.size() > 0
				&& mes >= 1 && mes <= 12 && anio > 0;
	}

	@Override
	public String toString() {
		return codigo + " - " + cliente.getNombreCliente() + " (" + getPeriodo() + ")";
	}
}