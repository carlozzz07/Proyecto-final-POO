package logico;

public class InternetFijo extends Servicio {
	
	private static final long serialVersionUID = 1L;

	private int megasBajada;
	private int megasSubida;
	private boolean incluyeRouter;
	private float limiteDatosGB;
	private float datosConsumidosGB;
	private int megasBajadaReducida;

	public InternetFijo(String idServicio, String nombre, String descripcion, float precioBase, int megasBajada,
			int megasSubida, boolean incluyeRouter, float limiteDatosGB, int megasBajadaReducida) {
		super(idServicio, nombre, descripcion, precioBase);
		this.megasBajada = megasBajada;
		this.megasSubida = megasSubida;
		this.incluyeRouter = incluyeRouter;
		this.limiteDatosGB = limiteDatosGB;
		this.datosConsumidosGB = 0;
		this.megasBajadaReducida = megasBajadaReducida;
	}

	public int getMegasBajada() {
		return megasBajada;
	}

	public boolean setMegasBajada(int megasBajada) {
		if (megasBajada <= 0) {
			return false;
		}
		this.megasBajada = megasBajada;
		return true;
	}

	public int getMegasSubida() {
		return megasSubida;
	}

	public boolean setMegasSubida(int megasSubida) {
		if (megasSubida <= 0) {
			return false;
		}
		this.megasSubida = megasSubida;
		return true;
	}

	public boolean isIncluyeRouter() {
		return incluyeRouter;
	}

	public void setIncluyeRouter(boolean incluyeRouter) {
		this.incluyeRouter = incluyeRouter;
	}

	public float getLimiteDatosGB() {
		return limiteDatosGB;
	}

	public boolean setLimiteDatosGB(float limiteDatosGB) {
		if (limiteDatosGB <= 0) {
			return false;
		}
		this.limiteDatosGB = limiteDatosGB;
		return true;
	}

	public float getDatosConsumidosGB() {
		return datosConsumidosGB;
	}

	public int getMegasBajadaReducida() {
		return megasBajadaReducida;
	}

	public boolean setMegasBajadaReducida(int megasBajadaReducida) {
		if (megasBajadaReducida <= 0 || megasBajadaReducida > megasBajada) {
			return false;
		}
		this.megasBajadaReducida = megasBajadaReducida;
		return true;
	}

	public boolean registrarConsumo(float gb) {
		if (gb <= 0) {
			return false;
		}
		this.datosConsumidosGB += gb;
		return true;
	}

	public void reiniciarConsumo() {
		this.datosConsumidosGB = 0;
	}

	public boolean superoLimiteDatos() {
		return this.limiteDatosGB > 0 && this.datosConsumidosGB > this.limiteDatosGB;
	}

	public int obtenerVelocidadBajadaActual() {
		if (superoLimiteDatos()) {
			return this.megasBajadaReducida;
		} else {
			return this.megasBajada;
		}
	}

	public int obtenerVelocidadSubidaActual() {
		if (superoLimiteDatos()) {
			int subidaReducida = this.megasSubida / 4;
			if (subidaReducida < 1) {
				subidaReducida = 1;
			}
			return subidaReducida;
		} else {
			return this.megasSubida;
		}
	}

	@Override
	public String getTipo() {
		return "Internet Fijo";
	}

	@Override
	public String getDetalle() {
		String detalle = "Bajada " + obtenerVelocidadBajadaActual() + " Mbps / Subida "
				+ obtenerVelocidadSubidaActual() + " Mbps";
		if (superoLimiteDatos()) {
			detalle = detalle + " (velocidad reducida)";
		}
		if (incluyeRouter) {
			detalle = detalle + ", router incluido";
		}
		return detalle;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && megasBajada > 0 && megasSubida > 0
				&& limiteDatosGB > 0 && megasBajadaReducida > 0 && megasBajadaReducida <= megasBajada;
	}
	
	
}