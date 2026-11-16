package logico;

import java.time.LocalDate;

public class Contrato {
	private String idContrato;
	private Servicio servicio;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private boolean activo;
	private boolean tienePromocion;
	private int mesesPromocionRestantes;
	private float porcentajeDescuento;

	public Contrato(String idContrato, Servicio servicio, LocalDate fechaInicio) {
		super();
		this.idContrato = idContrato;
		this.servicio = servicio;
		this.fechaInicio = fechaInicio;
		this.fechaFin = null;
		this.activo = true;
		this.tienePromocion = false;
		this.mesesPromocionRestantes = 0;
		this.porcentajeDescuento = 0.0f;
	}

	public float calcularCosto() {
		if (!this.activo || this.servicio == null) {
			return 0.0f;
		}
		float costo = this.servicio.getPrecioBase();
		if (this.tienePromocion && this.mesesPromocionRestantes > 0) {
			float descuento = costo * (this.porcentajeDescuento / 100.0f);
			costo = costo - descuento;
		}
		return costo;
	}

	public boolean activarPromocion(int meses, float porcentaje) {
		if (this.activo && meses > 0 && porcentaje > 0 && porcentaje <= 100) {
			this.tienePromocion = true;
			this.mesesPromocionRestantes = meses;
			this.porcentajeDescuento = porcentaje;
			return true;
		}
		return false;
	}

	public void consumirMesPromocion() {
		if (this.tienePromocion && this.mesesPromocionRestantes > 0) {
			this.mesesPromocionRestantes--;
			if (this.mesesPromocionRestantes == 0) {
				quitarPromocion();
			}
		}
	}

	public void quitarPromocion() {
		this.tienePromocion = false;
		this.mesesPromocionRestantes = 0;
		this.porcentajeDescuento = 0.0f;
	}

	public boolean cancelar() {
		if (this.activo) {
			this.activo = false;
			this.fechaFin = LocalDate.now();
			quitarPromocion();
			return true;
		}
		return false;
	}

	public String getIdContrato() {
		return idContrato;
	}

	public void setIdContrato(String idContrato) {
		this.idContrato = idContrato;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
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

	public boolean isTienePromocion() {
		return tienePromocion;
	}

	public void setTienePromocion(boolean tienePromocion) {
		this.tienePromocion = tienePromocion;
	}

	public int getMesesPromocionRestantes() {
		return mesesPromocionRestantes;
	}

	public void setMesesPromocionRestantes(int mesesPromocionRestantes) {
		this.mesesPromocionRestantes = mesesPromocionRestantes;
	}

	public float getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public void setPorcentajeDescuento(float porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
}