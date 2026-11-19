package logico;

public class PlanMovil extends Servicio {

	private int cantidadGigas;
	private int minutosLibres;
	private boolean redesSocialesIlimitadas;

	public PlanMovil(String idServicio, String nombre, String descripcion, float precioBase, int cantidadGigas,
			int minutosLibres, boolean redesSocialesIlimitadas) {
		super(idServicio, nombre, descripcion, precioBase);
		this.cantidadGigas = cantidadGigas;
		this.minutosLibres = minutosLibres;
		this.redesSocialesIlimitadas = redesSocialesIlimitadas;
	}

	public int getCantidadGigas() {
		return cantidadGigas;
	}

	public int getMinutosLibres() {
		return minutosLibres;
	}

	public boolean isRedesSocialesIlimitadas() {
		return redesSocialesIlimitadas;
	}

	boolean setCantidadGigas(int cantidadGigas) {
		if (cantidadGigas < 0) {
			return false;
		}
		this.cantidadGigas = cantidadGigas;
		return true;
	}

	boolean setMinutosLibres(int minutosLibres) {
		if (minutosLibres < 0) {
			return false;
		}
		this.minutosLibres = minutosLibres;
		return true;
	}

	void setRedesSocialesIlimitadas(boolean redesSocialesIlimitadas) {
		this.redesSocialesIlimitadas = redesSocialesIlimitadas;
	}

	@Override
	public String getTipo() {
		return "Plan Movil";
	}

	@Override
	public String getDetalle() {
		String detalle = cantidadGigas + " GB, " + minutosLibres + " minutos libres";
		if (redesSocialesIlimitadas) {
			detalle = detalle + ", redes sociales ilimitadas";
		}
		return detalle;
	}

	@Override
	public boolean esValido() {
		return super.esValido() && cantidadGigas >= 0 && minutosLibres >= 0;
	}
}