package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Plan implements Serializable {

	private static final long serialVersionUID = 1L;

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

	public void agregarServicio(Servicio servicio) {
		if (servicio != null && servicio.isActivo()) {
			boolean existe = false;
			int i = 0;
			while (!existe && i < servicios.size()) {
				if (servicio.getIdServicio() != null
						&& servicio.getIdServicio().equalsIgnoreCase(servicios.get(i).getIdServicio())) {
					existe = true;
				}
				i++;
			}
			if (!existe) {
				this.servicios.add(servicio);
			}
		}
	}

	public boolean activar() {
		if (this.activo) {
			return false;
		}
		this.activo = true;
		return true;
	}

	public boolean desactivar() {
		if (!this.activo) {
			return false;
		}
		this.activo = false;
		return true;
	}

	public float calcularPrecio() {
		float total = 0;
		int i = 0;
		while (i < servicios.size()) {
			total += servicios.get(i).calcularCosto();
			i++;
		}
		return total;
	}

	public String getTipoPago() {
		if (this.esPrepago) {
			return "Prepago";
		} else {
			return "Postpago";
		}
	}
}