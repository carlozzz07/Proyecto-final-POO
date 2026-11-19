package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Contrato {

	private String idContrato;
	private Cliente cliente;
	private Plan plan;
	private Comercial comercial;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private boolean activo;
	private boolean suspendido;
	private ArrayList<Factura> facturas;

	public Contrato(String idContrato, Cliente cliente, Plan plan, Comercial comercial, LocalDate fechaInicio) {
		super();
		this.idContrato = idContrato;
		this.cliente = cliente;
		this.plan = plan;
		this.comercial = comercial;
		this.fechaInicio = (fechaInicio != null) ? fechaInicio : LocalDate.now();
		this.fechaFin = null;
		this.activo = true;
		this.suspendido = false;
		this.facturas = new ArrayList<>();
	}

	public boolean esPrepago() {
		return this.plan != null && this.plan.isEsPrepago();
	}

	public String getTipoPago() {
		if (this.plan == null) {
			return "";
		}
		return this.plan.getTipoPago();
	}

	public void agregarFactura(Factura factura) {
		if (factura != null && !this.facturas.contains(factura)) {
			this.facturas.add(factura);
		}
	}

	public boolean tieneFacturaEnPeriodo(int mes, int anio) {
		for (Factura factura : facturas) {
			LocalDate emision = factura.getFechaEmision();
			if (emision != null && emision.getMonthValue() == mes && emision.getYear() == anio) {
				return true;
			}
		}
		return false;
	}

	public float calcularDeudaPendiente() {
		float deuda = 0.0f;
		for (Factura factura : facturas) {
			if (!factura.isPagada()) {
				deuda += factura.getTotal();
			}
		}
		return deuda;
	}

	public boolean verificarSuspension() {
		if (!this.activo || this.fechaFin != null) {
			return false;
		}
		if (esPrepago()) {
			this.suspendido = false;
			return false;
		}
		for (Factura factura : facturas) {
			if (factura.estaVencida()) {
				this.suspendido = true;
				return true;
			}
		}
		this.suspendido = false;
		return false;
	}

	public boolean cancelar() {
		if (!this.activo) {
			return false;
		}
		this.activo = false;
		this.suspendido = false;
		this.fechaFin = LocalDate.now();
		return true;
	}

	public String getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(String idContrato) {
		this.idContrato = idContrato;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Comercial getComercial() {
		return comercial;
	}

	public void setComercial(Comercial comercial) {
		this.comercial = comercial;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isSuspendido() {
		return suspendido;
	}

	public void setSuspendido(boolean suspendido) {
		this.suspendido = suspendido;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}
}