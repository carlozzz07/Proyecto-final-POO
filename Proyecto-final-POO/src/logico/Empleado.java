package logico;

import java.time.LocalDate;

public abstract class Empleado extends Persona {
	
	private static final long serialVersionUID = 1L;

	private LocalDate fechaEntrada;
	private float sueldoBase;

	protected Empleado(String id, String cedula, String nombre, String telefono, String direccion,
			String usuario, String contrasena, LocalDate fechaEntrada, float sueldoBase) {
		super(id, cedula, nombre, telefono, direccion, usuario, contrasena);
		this.fechaEntrada = fechaEntrada;
		this.sueldoBase = sueldoBase;
	}

	public LocalDate getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(LocalDate fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public float getSueldoBase() {
		return sueldoBase;
	}

	public boolean setSueldoBase(float sueldoBase) {
		if (sueldoBase <= 0) {
			return false;
		}
		this.sueldoBase = sueldoBase;
		return true;
	}

	public int calcularAniosEnEmpresa() {
		if (fechaEntrada == null) {
			return 0;
		}
		return fechaEntrada.until(LocalDate.now()).getYears();
	}

	public boolean esValido() {
		return getId() != null && !getId().equals("")
				&& getCedula() != null && !getCedula().equals("")
				&& getNombre() != null && !getNombre().equals("")
				&& getUsuario() != null && !getUsuario().equals("")
				&& getContrasena() != null && !getContrasena().equals("")
				&& fechaEntrada != null && !fechaEntrada.isAfter(LocalDate.now())
				&& sueldoBase > 0;
	}

	@Override
	public String toString() {
		return getId() + " - " + getNombre() + " (" + getTipo() + ")";
	}

	public abstract float calcularSueldo();
}