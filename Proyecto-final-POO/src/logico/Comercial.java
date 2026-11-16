package logico;

public class Comercial extends Personal {

	private float porcentajeComision;
	private float metaMensual;
	private float VentaDelMes;
	
	public Comercial(String codigo, String cedula, String nombre, String apellidos, String telefono, String usario,
			String contrasena, boolean estado, float sueldoBase, float porcentajeComision, float metaMensual,
			float ventaDelMes) {
		
		super(codigo, cedula, nombre, apellidos, telefono, usario, contrasena, estado, sueldoBase);
		this.porcentajeComision = porcentajeComision;
		this.metaMensual = metaMensual;
		VentaDelMes = ventaDelMes;
	}

	public float getPorcentajeComision() {
		return porcentajeComision;
	}

	public float getMetaMensual() {
		return metaMensual;
	}

	public float getVentaDelMes() {
		return VentaDelMes;
	}
	
	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return null;
	}


}
