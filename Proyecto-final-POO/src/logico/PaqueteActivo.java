package logico;

import java.time.LocalDate;

public class PaqueteActivo {
	private Paquete paquete;
	private LocalDate fechaActivacion;
	private LocalDate fechaVencimiento;
	private boolean activo;

	public PaqueteActivo(Paquete paquete, LocalDate fechaActivacion) {
		super();
		this.paquete = paquete;
		this.fechaActivacion = fechaActivacion;
		this.fechaVencimiento = fechaActivacion.plusDays(paquete.getVigenciaDias());
		this.activo = true;
	}

	public boolean verificarVigencia(LocalDate fechaActual) {
		if (fechaActual.isAfter(this.fechaVencimiento)) {
			this.activo = false;
		}
		return this.activo;
	}

	public Paquete getPaquete() {
		return paquete;
	}

	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}

	public LocalDate getFechaActivacion() {
		return fechaActivacion;
	}

	public void setFechaActivacion(LocalDate fechaActivacion) {
		this.fechaActivacion = fechaActivacion;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}