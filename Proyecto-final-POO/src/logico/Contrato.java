package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Contrato {

	public static final int DIAS_INACTIVIDAD_PREPAGO = 30;

	private String idContrato;
	private Cliente cliente;
	private Plan plan;
	private Comercial comercial;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private LocalDate fechaUltimaActividad;
	private boolean activo;
	private boolean suspendido;
	private String motivoSuspension;
	private String motivoCancelacion;
	private ArrayList<Factura> facturas;

	public Contrato(String idContrato, Cliente cliente, Plan plan, Comercial comercial, LocalDate fechaInicio) {
		super();
		this.idContrato = idContrato;
		this.cliente = cliente;
		this.plan = plan;
		this.comercial = comercial;
		if (fechaInicio != null) {
			this.fechaInicio = fechaInicio;
		} else {
			this.fechaInicio = LocalDate.now();
		}
		this.fechaFin = null;
		this.fechaUltimaActividad = this.fechaInicio;
		this.activo = true;
		this.suspendido = false;
		this.motivoSuspension = "";
		this.motivoCancelacion = "";
		this.facturas = new ArrayList<>();
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

	public LocalDate getFechaUltimaActividad() {
		return fechaUltimaActividad;
	}

	public void setFechaUltimaActividad(LocalDate fechaUltimaActividad) {
		this.fechaUltimaActividad = fechaUltimaActividad;
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

	public String getMotivoSuspension() {
		return motivoSuspension;
	}

	public void setMotivoSuspension(String motivoSuspension) {
		this.motivoSuspension = motivoSuspension;
	}

	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
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
		if (factura != null) {
			boolean existe = false;
			int i = 0;
			while (!existe && i < facturas.size()) {
				if (factura.getIdFactura() != null
						&& factura.getIdFactura().equalsIgnoreCase(facturas.get(i).getIdFactura())) {
					existe = true;
				}
				i++;
			}
			if (!existe) {
				this.facturas.add(factura);
				if (esPrepago() && factura.getFechaEmision() != null) {
					registrarActividadPrepago(factura.getFechaEmision());
				}
			}
		}
	}

	public void registrarActividadPrepago(LocalDate fecha) {
		if (this.activo && esPrepago() && fecha != null) {
			if (this.fechaUltimaActividad == null || fecha.isAfter(this.fechaUltimaActividad)) {
				this.fechaUltimaActividad = fecha;
			}
			if (!LocalDate.now().isAfter(this.fechaUltimaActividad.plusDays(DIAS_INACTIVIDAD_PREPAGO))) {
				this.suspendido = false;
				this.motivoSuspension = "";
			}
		}
	}

	public boolean tieneFacturaEnPeriodo(int mes, int anio) {
		boolean encontrada = false;
		int i = 0;
		while (!encontrada && i < facturas.size()) {
			LocalDate emision = facturas.get(i).getFechaEmision();
			if (emision != null && emision.getMonthValue() == mes && emision.getYear() == anio) {
				encontrada = true;
			}
			i++;
		}
		return encontrada;
	}

	public float calcularDeudaPendiente() {
		float deuda = 0;
		int i = 0;
		while (i < facturas.size()) {
			if (!facturas.get(i).isPagada()) {
				deuda += facturas.get(i).getTotal();
			}
			i++;
		}
		return deuda;
	}

	public boolean verificarSuspension() {
		if (!this.activo || this.fechaFin != null) {
			return false;
		}

		if (esPrepago()) {
			if (this.fechaUltimaActividad != null
					&& LocalDate.now().isAfter(this.fechaUltimaActividad.plusDays(DIAS_INACTIVIDAD_PREPAGO))) {
				this.suspendido = true;
				this.motivoSuspension = "Inactividad";
				return true;
			} else {
				this.suspendido = false;
				this.motivoSuspension = "";
				return false;
			}
		}

		int facturasVencidas = 0;
		boolean superaTresMeses = false;
		int i = 0;
		while (i < facturas.size()) {
			Factura factura = facturas.get(i);
			if (factura.estaVencida()) {
				facturasVencidas++;
			}
			if (!factura.isPagada() && factura.getFechaVencimiento() != null
					&& !LocalDate.now().isBefore(factura.getFechaVencimiento().plusMonths(3))) {
				superaTresMeses = true;
			}
			i++;
		}

		if (facturasVencidas >= 3 || superaTresMeses) {
			cancelar("Falta de pago (3 meses)");
			return false;
		}

		if (facturasVencidas > 0) {
			this.suspendido = true;
			this.motivoSuspension = "Falta de pago";
			return true;
		}

		this.suspendido = false;
		this.motivoSuspension = "";
		return false;
	}

	public boolean cancelar() {
		return cancelar("Cancelacion solicitada");
	}

	public boolean cancelar(String motivo) {
		if (!this.activo) {
			return false;
		}
		this.activo = false;
		this.suspendido = false;
		this.motivoSuspension = "";
		if (motivo != null) {
			this.motivoCancelacion = motivo;
		}
		this.fechaFin = LocalDate.now();
		return true;
	}

	public boolean reactivar() {
		if (this.activo) {
			return false;
		}
		this.activo = true;
		this.fechaFin = null;
		this.motivoCancelacion = "";
		this.suspendido = false;
		this.motivoSuspension = "";
		if (esPrepago()) {
			this.fechaUltimaActividad = LocalDate.now();
		}
		return true;
	}
}