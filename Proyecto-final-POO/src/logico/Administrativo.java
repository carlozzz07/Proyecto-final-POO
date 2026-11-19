package logico;

import java.time.LocalDate;

public class Administrativo extends Empleado {

	private String departamento;
	private float bono;

	public Administrativo(String id, String cedula, String nombre, String telefono, String direccion,
			LocalDate fechaEntrada, float sueldoBase, String departamento, float bono) {
		super(id, cedula, nombre, telefono, direccion, fechaEntrada, sueldoBase);
		this.departamento = departamento;
		this.bono = bono;
	}

	public String getDepartamento() {
		return departamento;
	}

	public float getBono() {
		return bono;
	}

	boolean setDepartamento(String departamento) {
		if (departamento == null || departamento.equals("")) {
			return false;
		}
		this.departamento = departamento;
		return true;
	}

	boolean setBono(float bono) {
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
