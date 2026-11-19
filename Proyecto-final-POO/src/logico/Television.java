package logico;

public class Television extends Servicio {

	private int cantidadCanales;
	private boolean incluyeHD;
	private int cajillasExtra;

	public Television(String idServicio, String nombre, String descripcion, float precioBase, int cantidadCanales,
			boolean incluyeHD, int cajillasExtra) {
		super(idServicio, nombre, descripcion, precioBase);
		this.cantidadCanales = cantidadCanales;
		this.incluyeHD = incluyeHD;
		this.cajillasExtra = cajillasExtra;
	}

	public int getCantidadCanales() {
		return cantidadCanales;
	}

	public boolean isIncluyeHD() {
		return incluyeHD;
	}

	public int getCajillasExtra() {
		return cajillasExtra;
	}

	boolean setCantidadCanales(int cantidadCanales) {
		if (cantidadCanales <= 0) {
			return false;
		}
		this.cantidadCanales = cantidadCanales;
		return true;
	}

	void setIncluyeHD(boolean incluyeHD) {
		this.incluyeHD = incluyeHD;
	}

	boolean setCajillasExtra(int cajillasExtra) {
		if (cajillasExtra < 0) {
			return false;
		}
		this.cajillasExtra = cajillasExtra;
		return true;
	}

	@Override
	public String getTipo() {
		return "Television";
	}

	@Override
	public String getDetalle() {
		String detalle = cantidadCanales + " canales";
		if (incluyeHD) {
			detalle = detalle + ", HD incluido";
		}
		if (cajillasExtra > 0) {
			detalle = detalle + ", " + cajillasExtra + " cajilla(s) extra";
		}
		return detalle;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && cantidadCanales > 0 && cajillasExtra >= 0;
	}
}