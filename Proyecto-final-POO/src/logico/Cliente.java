package logico;

import java.util.ArrayList;

public class Cliente extends Persona {
	
	private static final long serialVersionUID = 1L;

	private ArrayList<Contrato> contratos;

	public Cliente(String id, String cedula, String nombre, String telefono, String direccion,
			String usuario, String contrasena) {
		super(id, cedula, nombre, telefono, direccion, usuario, contrasena);
		this.contratos = new ArrayList<>();
	}

	public Cliente(String id, String cedula, String nombre, String telefono, String direccion) {
		super(id, cedula, nombre, telefono, direccion, "", "");
		this.contratos = new ArrayList<>();
	}

	public ArrayList<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<Contrato> contratos) {
		this.contratos = contratos;
	}

	@Override
	public String getTipo() {
		return "Cliente";
	}

	public void agregarContrato(Contrato contrato) {
		if (contrato != null) {
			boolean existe = false;
			int i = 0;
			while (!existe && i < contratos.size()) {
				if (contrato.getIdContrato() != null
						&& contrato.getIdContrato().equalsIgnoreCase(contratos.get(i).getIdContrato())) {
					existe = true;
				}
				i++;
			}
			if (!existe) {
				this.contratos.add(contrato);
			}
		}
	}

	public boolean cancelarContrato(String idContrato) {
		boolean cancelado = false;
		boolean encontrado = false;
		int i = 0;
		if (idContrato != null) {
			while (!encontrado && i < contratos.size()) {
				if (idContrato.equalsIgnoreCase(contratos.get(i).getIdContrato())) {
					cancelado = contratos.get(i).cancelar();
					encontrado = true;
				}
				i++;
			}
		}
		return cancelado;
	}

	public float calcularDeudaTotal() {
		float totalDeuda = 0;
		int i = 0;
		while (i < contratos.size()) {
			totalDeuda += contratos.get(i).calcularDeudaPendiente();
			i++;
		}
		return totalDeuda;
	}
}