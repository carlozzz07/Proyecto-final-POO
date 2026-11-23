package logico;

import java.time.LocalDate;

public class Comercial extends Empleado {
	
	private static final long serialVersionUID = 1L;

	private float porcentajeComision;
	private float metaMensual;
	private float ventasDelMes;

	public Comercial(String id, String cedula, String nombre, String telefono, String direccion,
			String usuario, String contrasena, LocalDate fechaEntrada, float sueldoBase,
			float porcentajeComision, float metaMensual) {
		super(id, cedula, nombre, telefono, direccion, usuario, contrasena, fechaEntrada, sueldoBase);
		this.porcentajeComision = porcentajeComision;
		this.metaMensual = metaMensual;
		this.ventasDelMes = 0.0f;
	}

	public float getPorcentajeComision() {
		return porcentajeComision;
	}

	public boolean setPorcentajeComision(float porcentajeComision) {
		if (porcentajeComision < 0 || porcentajeComision > 100) {
			return false;
		}
		this.porcentajeComision = porcentajeComision;
		return true;
	}

	public float getMetaMensual() {
		return metaMensual;
	}

	public boolean setMetaMensual(float metaMensual) {
		if (metaMensual < 0) {
			return false;
		}
		this.metaMensual = metaMensual;
		return true;
	}

	public float getVentasDelMes() {
		return ventasDelMes;
	}

	public boolean registrarVenta(float monto) {
		if (monto <= 0) {
			return false;
		}
		ventasDelMes = ventasDelMes + monto;
		return true;
	}

	public void reiniciarVentasDelMes() {
		ventasDelMes = 0.0f;
	}

	public boolean cumplioMeta() {
		return ventasDelMes >= metaMensual;
	}

	@Override
	public String getTipo() {
		return "Comercial";
	}

	@Override
	public float calcularSueldo() {
		return getSueldoBase() + (ventasDelMes * porcentajeComision / 100);
	}

	@Override
	public boolean esValido() {
		return super.esValido() && porcentajeComision >= 0 && porcentajeComision <= 100 && metaMensual >= 0;
	}
}