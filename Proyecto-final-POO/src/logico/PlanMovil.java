package logico;

public class PlanMovil extends Servicio {

	private int cantidadGigas;
	private int minutosLibres;
	private boolean redesSocialesIlimitadas;
	private float gigasConsumidos;
	private int velocidadNormalMbps;
	private int velocidadReducidaMbps;

	public PlanMovil(String idServicio, String nombre, String descripcion, float precioBase, int cantidadGigas,
			int minutosLibres, boolean redesSocialesIlimitadas, int velocidadNormalMbps, int velocidadReducidaMbps) {
		super(idServicio, nombre, descripcion, precioBase);
		this.cantidadGigas = cantidadGigas;
		this.minutosLibres = minutosLibres;
		this.redesSocialesIlimitadas = redesSocialesIlimitadas;
		this.gigasConsumidos = 0;
		this.velocidadNormalMbps = velocidadNormalMbps;
		this.velocidadReducidaMbps = velocidadReducidaMbps;
	}

	public int getCantidadGigas() {
		return cantidadGigas;
	}

	public boolean setCantidadGigas(int cantidadGigas) {
		if (cantidadGigas < 0) {
			return false;
		}
		this.cantidadGigas = cantidadGigas;
		return true;
	}

	public int getMinutosLibres() {
		return minutosLibres;
	}

	public boolean setMinutosLibres(int minutosLibres) {
		if (minutosLibres < 0) {
			return false;
		}
		this.minutosLibres = minutosLibres;
		return true;
	}

	public boolean isRedesSocialesIlimitadas() {
		return redesSocialesIlimitadas;
	}

	public void setRedesSocialesIlimitadas(boolean redesSocialesIlimitadas) {
		this.redesSocialesIlimitadas = redesSocialesIlimitadas;
	}

	public float getGigasConsumidos() {
		return gigasConsumidos;
	}

	public int getVelocidadNormalMbps() {
		return velocidadNormalMbps;
	}

	public boolean setVelocidadNormalMbps(int velocidadNormalMbps) {
		if (velocidadNormalMbps <= 0) {
			return false;
		}
		this.velocidadNormalMbps = velocidadNormalMbps;
		return true;
	}

	public int getVelocidadReducidaMbps() {
		return velocidadReducidaMbps;
	}

	public boolean setVelocidadReducidaMbps(int velocidadReducidaMbps) {
		if (velocidadReducidaMbps <= 0 || velocidadReducidaMbps > velocidadNormalMbps) {
			return false;
		}
		this.velocidadReducidaMbps = velocidadReducidaMbps;
		return true;
	}

	public boolean registrarConsumoGigas(float gb) {
		if (gb <= 0) {
			return false;
		}
		this.gigasConsumidos += gb;
		return true;
	}

	public void reiniciarConsumoGigas() {
		this.gigasConsumidos = 0;
	}

	public boolean superoLimiteDatos() {
		return this.cantidadGigas > 0 && this.gigasConsumidos > this.cantidadGigas;
	}

	public int obtenerVelocidadActual() {
		if (superoLimiteDatos()) {
			return this.velocidadReducidaMbps;
		} else {
			return this.velocidadNormalMbps;
		}
	}

	@Override
	public String getTipo() {
		return "Plan Movil";
	}

	@Override
	public String getDetalle() {
		String detalle = cantidadGigas + " GB (" + obtenerVelocidadActual() + " Mbps), " + minutosLibres
				+ " minutos libres";
		if (superoLimiteDatos()) {
			detalle = detalle + " (velocidad reducida)";
		}
		if (redesSocialesIlimitadas) {
			detalle = detalle + ", redes sociales ilimitadas";
		}
		return detalle;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && cantidadGigas >= 0 && minutosLibres >= 0
				&& velocidadNormalMbps > 0 && velocidadReducidaMbps > 0
				&& velocidadReducidaMbps <= velocidadNormalMbps;
	}
}