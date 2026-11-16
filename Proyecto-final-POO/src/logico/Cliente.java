package logico;

import java.util.ArrayList;

public abstract class Cliente {
	protected String idCliente;
	protected String cedula;
	protected String nombreCliente;
	protected String direccion;
	protected String numeroContrato;
	protected boolean estadoActivo;
	protected int fidepuntos;
	protected ArrayList<String> numerosTelefono;
	protected ArrayList<PaqueteActivo> paquetes;

	public Cliente(String idCliente, String cedula, String nombreCliente, String direccion, String telefonoPrincipal, String numeroContrato) {
		super();
		this.idCliente = idCliente;
		this.cedula = cedula;
		this.nombreCliente = nombreCliente;
		this.direccion = direccion;
		this.numeroContrato = numeroContrato;
		this.estadoActivo = true;
		this.fidepuntos = 0;
		this.numerosTelefono = new ArrayList<>();
		if (telefonoPrincipal != null && !telefonoPrincipal.trim().isEmpty()) {
			this.numerosTelefono.add(telefonoPrincipal.trim());
		}
		this.paquetes = new ArrayList<>();
	}

	public abstract String getTipo();

	public abstract boolean comprarPaquete(Paquete paquete);

	public boolean tieneTelefono(String telefono) {
		int i = 0;
		boolean encontrado = false;
		while (!encontrado && i < numerosTelefono.size()) {
			if (numerosTelefono.get(i).equalsIgnoreCase(telefono)) {
				encontrado = true;
			}
			i++;
		}
		return encontrado;
	}

	public void agregarTelefono(String nuevoTelefono) {
		if (nuevoTelefono != null && !nuevoTelefono.trim().isEmpty() && !tieneTelefono(nuevoTelefono.trim())) {
			this.numerosTelefono.add(nuevoTelefono.trim());
		}
	}

	public void eliminarTelefono(String telefono) {
		int i = 0;
		boolean encontrado = false;
		while (!encontrado && i < numerosTelefono.size()) {
			if (numerosTelefono.get(i).equalsIgnoreCase(telefono)) {
				numerosTelefono.remove(i);
				encontrado = true;
			}
			i++;
		}
	}

	public boolean canjearFidepuntos(int puntosRequeridos) {
		if (puntosRequeridos > 0 && this.fidepuntos >= puntosRequeridos) {
			this.fidepuntos = this.fidepuntos - puntosRequeridos;
			return true;
		}
		return false;
	}

	public String getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public boolean isEstadoActivo() {
		return estadoActivo;
	}

	public void setEstadoActivo(boolean estadoActivo) {
		this.estadoActivo = estadoActivo;
	}

	public int getFidepuntos() {
		return fidepuntos;
	}

	public void setFidepuntos(int fidepuntos) {
		this.fidepuntos = fidepuntos;
	}

	public ArrayList<String> getNumerosTelefono() {
		return numerosTelefono;
	}

	public void setNumerosTelefono(ArrayList<String> numerosTelefono) {
		this.numerosTelefono = numerosTelefono;
	}

	public ArrayList<PaqueteActivo> getPaquetes() {
		return paquetes;
	}

	public void setPaquetes(ArrayList<PaqueteActivo> paquetes) {
		this.paquetes = paquetes;
	}
}