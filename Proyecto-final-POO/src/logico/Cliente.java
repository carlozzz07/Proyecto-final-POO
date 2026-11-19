package logico;

import java.util.ArrayList;

public class Cliente extends Persona {

	private ArrayList<Contrato> contratos;

	public Cliente(String id, String cedula, String nombre, String telefono, String direccion) {
		super(id, cedula, nombre, telefono, direccion);
		this.contratos = new ArrayList<>();
	}

	@Override
	public String getTipo() {
		return "Cliente";
	}

	public void agregarContrato(Contrato contrato) {
		if (contrato != null && !this.contratos.contains(contrato)) {
			this.contratos.add(contrato);
		}
	}

	public boolean cancelarContrato(String idContrato) {
		if (idContrato == null) {
			return false;
		}
		for (Contrato contrato : contratos) {
			if (contrato.getIdContrato().equalsIgnoreCase(idContrato)) {
				return contrato.cancelar();
			}
		}
		return false;
	}

	public float calcularDeudaTotal() {
		float totalDeuda = 0.0f;
		for (Contrato contrato : contratos) {
			totalDeuda += contrato.calcularDeudaPendiente();
		}
		return totalDeuda;
	}

	public ArrayList<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<Contrato> contratos) {
		this.contratos = contratos;
	}
}