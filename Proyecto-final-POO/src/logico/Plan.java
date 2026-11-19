package logico;

import java.util.ArrayList;

public class Plan {

	private String idPlan;
	private String nombrePlan;
	private String descripcion;
	private boolean esPrepago;
	private ArrayList<Servicio> servicios;
	private boolean activo;

	public Plan(String idPlan, String nombrePlan, String descripcion, boolean esPrepago) {
		super();
		this.idPlan = idPlan;
		this.nombrePlan = nombrePlan;
		this.descripcion = descripcion;
		this.esPrepago = esPrepago;
		this.servicios = new ArrayList<>();
		this.activo = true;
	}

	public void agregarServicio(Servicio servicio) {
		if (servicio != null && !this.servicios.contains(servicio)) {
			this.servicios.add(servicio);
		}
	}

	public boolean eliminarServicio(String idServicio) {
		if (idServicio == null) {
			return false;
		}
		for (int i = 0; i < servicios.size(); i++) {
			if (servicios.get(i).getIdServicio().equalsIgnoreCase(idServicio)) {
				servicios.remove(i);
				return true;
			}
		}
		return false;
	}

	public float calcularPrecio() {
		float total = 0.0f;
		for (Servicio servicio : servicios) {
			total += servicio.calcularCosto();
		}
		return total;
	}

	public String getTipoPago() {
		if (this.esPrepago) {
			return "Prepago";
		}
		return "Postpago";
	}

	public String getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(String idPlan) {
		this.idPlan = idPlan;
	}

	public String getNombrePlan() {
		return nombrePlan;
	}

	public void setNombrePlan(String nombrePlan) {
		this.nombrePlan = nombrePlan;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEsPrepago() {
		return esPrepago;
	}

	public void setEsPrepago(boolean esPrepago) {
		this.esPrepago = esPrepago;
	}

	public ArrayList<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<Servicio> servicios) {
		this.servicios = servicios;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}