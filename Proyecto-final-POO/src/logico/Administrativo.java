package logico;

public class Administrativo extends Personal {

	private String departamento;
	private float bono;
	
	public Administrativo(String codigo, String cedula, String nombre, String apellidos, String telefono, String usario,
			String contrasena, boolean estado, float sueldoBase, String departamento, float bono) {
		super(codigo, cedula, nombre, apellidos, telefono, usario, contrasena, estado, sueldoBase);
		this.departamento = departamento;
		this.bono = bono;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public float getBono() {
		return bono;
	}

	public void setBono(float bono) {
		this.bono = bono;
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return null;
	}

}
