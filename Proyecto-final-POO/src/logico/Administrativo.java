package logico;

import java.time.LocalDate;

public class Administrativo extends Empleado {
	
	private static final long serialVersionUID = 1L;

	private String departamento;
	private float bono;

	public Administrativo(String id, String cedula, String nombre, String telefono, String direccion,
			String usuario, String contrasena, LocalDate fechaEntrada, float sueldoBase,
			String departamento, float bono) {
		super(id, cedula, nombre, telefono, direccion, usuario, contrasena, fechaEntrada, sueldoBase);
		this.departamento = departamento;
		this.bono = bono;
	}

	public String getDepartamento() {
		return departamento;
	}

	public boolean setDepartamento(String departamento) {
		if (departamento == null || departamento.equals("")) {
			return false;
		}
		this.departamento = departamento;
		return true;
	}

	public float getBono() {
		return bono;
	}

	public boolean setBono(float bono) {
		if (bono < 0) {
			return false;
		}
		this.bono = bono;
		return true;
	}

	@Override
	public String getTipo() {
		return "Administrativo";
	}

	@Override
	public float calcularSueldo() {
		return getSueldoBase() + bono;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && departamento != null && !departamento.equals("") && bono >= 0;
	}
}