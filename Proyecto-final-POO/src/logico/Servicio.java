package logico;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Servicio implements Serializable {

	private static final long serialVersionUID = 1L;

	private String idServicio;
	private String nombre;
	private String descripcion;
	private float precioBase;
	private boolean activo;
	private LocalDate fechaCreacion;
	private LocalDate fechaBaja;
	private boolean tienePromocion;
	private int mesesPromocionRestantes;
	private float porcentajeDescuento;

	protected Servicio(String idServicio, String nombre, String descripcion, float precioBase) {
		super();
		this.idServicio = idServicio;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precioBase = precioBase;
		this.activo = true;
		this.fechaCreacion = LocalDate.now();
		this.fechaBaja = null;
		this.tienePromocion = false;
		this.mesesPromocionRestantes = 0;
		this.porcentajeDescuento = 0.0f;
	}

	public String getIdServicio() {
		return idServicio;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean setNombre(String nombre) {
		if (nombre == null || nombre.equals("")) {
			return false;
		}
		this.nombre = nombre;
		return true;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public float getPrecioBase() {
		return precioBase;
	}

	public boolean setPrecioBase(float precioBase) {
		if (precioBase <= 0) {
			return false;
		}
		this.precioBase = precioBase;
		return true;
	}

	public boolean isActivo() {
		return activo;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public LocalDate getFechaBaja() {
		return fechaBaja;
	}

	public boolean isTienePromocion() {
		return tienePromocion;
	}

	public int getMesesPromocionRestantes() {
		return mesesPromocionRestantes;
	}

	public float getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	public boolean darDeBaja() {
		if (!activo) {
			return false;
		}
		activo = false;
		fechaBaja = LocalDate.now();
		quitarPromocion();
		return true;
	}

	public boolean activar() {
		if (activo) {
			return false;
		}
		activo = true;
		fechaBaja = null;
		return true;
	}

	public boolean activarPromocion(int mesesDuracion, float porcentajeDescuento) {
		if (!activo || tienePromocion) {
			return false;
		}
		if (mesesDuracion > 0 && porcentajeDescuento > 0 && porcentajeDescuento <= 100) {
			this.tienePromocion = true;
			this.mesesPromocionRestantes = mesesDuracion;
			this.porcentajeDescuento = porcentajeDescuento;
			return true;
		}
		return false;
	}

	public void consumirMesPromocion() {
		if (this.tienePromocion && this.mesesPromocionRestantes > 0) {
			this.mesesPromocionRestantes--;
			if (this.mesesPromocionRestantes == 0) {
				this.tienePromocion = false;
				this.porcentajeDescuento = 0.0f;
			}
		}
	}

	public void quitarPromocion() {
		this.tienePromocion = false;
		this.mesesPromocionRestantes = 0;
		this.porcentajeDescuento = 0.0f;
	}

	public float calcularCosto() {
		float costo = precioBase;
		if (tienePromocion) {
			costo = costo - (costo * porcentajeDescuento / 100);
		}
		return costo;
	}

	public boolean esValido() {
		return idServicio != null && !idServicio.equals("")
				&& nombre != null && !nombre.equals("")
				&& precioBase > 0;
	}

	public boolean estuvoActivoEn(LocalDate fecha) {
		if (fecha == null || fecha.isBefore(fechaCreacion)) {
			return false;
		}
		return fechaBaja == null || fecha.isBefore(fechaBaja);
	}

	public boolean estuvoActivoEnPeriodo(int mes, int anio) {
		if (mes < 1 || mes > 12 || anio <= 0) {
			return false;
		}
		LocalDate inicio = LocalDate.of(anio, mes, 1);
		LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
		boolean creadoAntesDelFin = !fechaCreacion.isAfter(fin);
		boolean noDadoDeBajaAntes = fechaBaja == null || fechaBaja.isAfter(inicio);
		return creadoAntesDelFin && noDadoDeBajaAntes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Servicio)) {
			return false;
		}
		Servicio otro = (Servicio) obj;
		return idServicio != null && idServicio.equalsIgnoreCase(otro.idServicio);
	}

	@Override
	public int hashCode() {
		if (idServicio == null) {
			return 0;
		}
		return idServicio.toLowerCase().hashCode();
	}

	@Override
	public String toString() {
		String texto = idServicio + " - " + nombre + " ($" + String.format("%.2f", precioBase) + ")";
		if (tienePromocion) {
			texto = texto + " - promo " + porcentajeDescuento + "% (" + mesesPromocionRestantes + " meses)";
		}
		return texto;
	}

	public abstract String getTipo();

	public abstract String getDetalle();
}