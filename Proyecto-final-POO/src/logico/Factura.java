package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Factura implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idFactura;
	private String ncf;
	private LocalDate fechaEmision;
	private LocalDate fechaVencimiento;
	private LocalDate fechaPago;
	private Contrato contrato;
	private float subtotal;
	private float itbis;
	private float total;
	private boolean pagada;

	public Factura(String idFactura, String ncf, LocalDate fechaEmision, Contrato contrato) {
		super();
		this.idFactura = idFactura;
		this.ncf = ncf;
		if (fechaEmision != null) {
			this.fechaEmision = fechaEmision;
		} else {
			this.fechaEmision = LocalDate.now();
		}
		this.contrato = contrato;
		this.subtotal = calcularSubtotal();
		this.itbis = calcularItbis();
		this.total = calcularTotal();

		if (this.contrato != null && this.contrato.esPrepago()) {
			this.fechaVencimiento = this.fechaEmision;
			this.fechaPago = this.fechaEmision;
			this.pagada = true;
		} else {
			this.fechaVencimiento = this.fechaEmision.plusDays(15);
			this.fechaPago = null;
			this.pagada = false;
		}
	}

	public String getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}

	public String getNcf() {
		return ncf;
	}

	public void setNcf(String ncf) {
		this.ncf = ncf;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getItbis() {
		return itbis;
	}

	public void setItbis(float itbis) {
		this.itbis = itbis;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public boolean isPagada() {
		return pagada;
	}

	public void setPagada(boolean pagada) {
		this.pagada = pagada;
	}

	public float calcularSubtotal() {
		if (this.contrato != null && this.contrato.getPlan() != null) {
			return this.contrato.getPlan().calcularPrecio();
		}
		return 0;
	}

	public float calcularItbis() {
		return this.subtotal * 0.18f;
	}

	public float calcularTotal() {
		return this.subtotal + this.itbis;
	}

	public boolean estaVencida() {
		if (this.pagada || this.fechaVencimiento == null) {
			return false;
		}
		return LocalDate.now().isAfter(this.fechaVencimiento);
	}

	public void marcarComoPagada() {
		if (!this.pagada) {
			this.pagada = true;
			this.fechaPago = LocalDate.now();
			if (this.contrato != null) {
				this.contrato.verificarSuspension();
			}
		}
	}
}