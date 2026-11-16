package logico;

public class Trabajador extends Personal {

	private String puesto;
	private String turno;
	private int horasExtra;
	private float tafiraHorasExtra;
	
	public Trabajador(String codigo, String cedula, String nombre, String apellidos, String telefono, String usario,
			String contrasena, boolean estado, float sueldoBase, String puesto, String turno, int horasExtra,
			float tafiraHorasExtra) {
		
		super(codigo, cedula, nombre, apellidos, telefono, usario, contrasena, estado, sueldoBase);
		this.puesto = puesto;
		this.turno = turno;
		this.horasExtra = horasExtra;
		this.tafiraHorasExtra = tafiraHorasExtra;
	}

	public String getPuesto() {
		return puesto;
	}


	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}


	public String getTurno() {
		return turno;
	}


	public void setTurno(String turno) {
		this.turno = turno;
	}


	public int getHorasExtra() {
		return horasExtra;
	}


	public void setHorasExtra(int horasExtra) {
		this.horasExtra = horasExtra;
	}


	public float getTafiraHorasExtra() {
		return tafiraHorasExtra;
	}


	public void setTafiraHorasExtra(float tafiraHorasExtra) {
		this.tafiraHorasExtra = tafiraHorasExtra;
	}

	@Override
	public String getTipo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//float calcularSueldo();
	//void registrarHorasExtra(int horas);
}
