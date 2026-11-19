package logico;

import java.time.LocalDate;

public abstract class Empleado extends Persona {

	private LocalDate fechaEntrada;
	private float sueldoBase;

	protected Empleado(String id, String cedula, String nombre, String telefono, String direccion,
			LocalDate fechaEntrada, float sueldoBase) {
		super(id, cedula, nombre, telefono, direccion);
		this.fechaEntrada = fechaEntrada;
		this.sueldoBase = sueldoBase;
	}

	public LocalDate getFechaEntrada() {
		return fechaEntrada;
	}

	public float getSueldoBase() {
		return sueldoBase;
	}

	boolean setSueldoBase(float sueldoBase) {
		if (sueldoBase <= 0) {
			return false;
		}
		this.sueldoBase = sueldoBase;
		return true;
	}

	public int calcularAniosEnEmpresa() {
		return fechaEntrada.until(LocalDate.now()).getYears();
	}

	public abstract float calcularSueldo();

	public boolean esValido() {
		return getId() != null && !getId().equals("")
				&& getCedula() != null && !getCedula().equals("")
				&& getNombre() != null && !getNombre().equals("")
				&& fechaEntrada != null && !fechaEntrada.isAfter(LocalDate.now())
				&& sueldoBase > 0;
	}

	@Override
	public String toString() {
		return getId() + " - " + getNombre() + " (" + getTipo() + ")";
	}
}