package logico;

public class Servicio {

	private String nombre;
	private String descripcion;
	private float precioMensual;
	private boolean activo;

	public Servicio(String nombre, String descripcion, float precioMensual) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precioMensual = precioMensual;
		this.activo = true;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public float getPrecioMensual() {
		return precioMensual;
	}

	public boolean isActivo() {
		return activo;
	}

	void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	void setPrecioMensual(float precioMensual) {
		this.precioMensual = precioMensual;
	}

	void darDeBaja() {
		activo = false;
	}

	void activar() {
		activo = true;
	}

	public boolean esValido() {
		return !nombre.equals("") && precioMensual > 0;
	}

	@Override
	public String toString() {
		return nombre;
	}
}