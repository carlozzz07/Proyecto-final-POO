package logico;

public class InternetFijo extends Servicio {

	private int megasBajada;
	private int megasSubida;
	private boolean incluyeRouter;

	public InternetFijo(String idServicio, String nombre, String descripcion, float precioBase, int megasBajada,
			int megasSubida, boolean incluyeRouter) {
		super(idServicio, nombre, descripcion, precioBase);
		this.megasBajada = megasBajada;
		this.megasSubida = megasSubida;
		this.incluyeRouter = incluyeRouter;
	}

	public int getMegasBajada() {
		return megasBajada;
	}

	public int getMegasSubida() {
		return megasSubida;
	}

	public boolean isIncluyeRouter() {
		return incluyeRouter;
	}

	boolean setMegasBajada(int megasBajada) {
		if (megasBajada <= 0) {
			return false;
		}
		this.megasBajada = megasBajada;
		return true;
	}

	boolean setMegasSubida(int megasSubida) {
		if (megasSubida <= 0) {
			return false;
		}
		this.megasSubida = megasSubida;
		return true;
	}

	void setIncluyeRouter(boolean incluyeRouter) {
		this.incluyeRouter = incluyeRouter;
	}

	@Override
	public String getTipo() {
		return "Internet Fijo";
	}

	@Override
	public String getDetalle() {
		String detalle = "Bajada " + megasBajada + " Mbps / Subida " + megasSubida + " Mbps";
		if (incluyeRouter) {
			detalle = detalle + ", router incluido";
		}
		return detalle;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && megasBajada > 0 && megasSubida > 0;
	}
}