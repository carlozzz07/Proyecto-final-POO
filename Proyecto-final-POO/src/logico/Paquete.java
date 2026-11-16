package logico;

public class Paquete {
	private String idPaquete;
	private String nombre;
	private double precio;
	private int datosMB;
	private int minutos;
	private int sms;
	private int vigenciaDias;

	public Paquete(String idPaquete, String nombre, double precio, int datosMB, int minutos, int sms, int vigenciaDias) {
		super();
		this.idPaquete = idPaquete;
		this.nombre = nombre;
		this.precio = precio;
		this.datosMB = datosMB;
		this.minutos = minutos;
		this.sms = sms;
		this.vigenciaDias = vigenciaDias;
	}

	public String getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getDatosMB() {
		return datosMB;
	}

	public void setDatosMB(int datosMB) {
		this.datosMB = datosMB;
	}

	public int getMinutos() {
		return minutos;
	}

	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	public int getSms() {
		return sms;
	}

	public void setSms(int sms) {
		this.sms = sms;
	}

	public int getVigenciaDias() {
		return vigenciaDias;
	}

	public void setVigenciaDias(int vigenciaDias) {
		this.vigenciaDias = vigenciaDias;
	}
}