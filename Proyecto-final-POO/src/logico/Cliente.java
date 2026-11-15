package logico;

import java.util.ArrayList;

public class Cliente {
	private String idCliente;
	private String cedula;
	private String nombreCliente;
	private String direccion;
	private String tipoCliente;
	private String numeroContrato;
	private boolean estadoActivo;
	private float deudaPendiente;
	private float limiteCredito;
	private int fidepuntos;
	private ArrayList<String> numerosTelefono;
	private ArrayList<Servicio> serviciosContratados;
	private ArrayList<Factura> misFacturas;

	public Cliente(String idCliente, String cedula, String nombreCliente, String direccion, String tipoCliente, String telefonoPrincipal, String numeroContrato, float limiteCredito) {
		super();
		this.idCliente = idCliente;
		this.cedula = cedula;
		this.nombreCliente = nombreCliente;
		this.direccion = direccion;
		this.tipoCliente = tipoCliente;
		this.numeroContrato = numeroContrato;
		this.estadoActivo = true;
		this.deudaPendiente = 0.0f;
		this.limiteCredito = limiteCredito;
		this.fidepuntos = 0;
		this.numerosTelefono = new ArrayList<>();
		if (telefonoPrincipal != null && !telefonoPrincipal.trim().isEmpty()) {
			this.numerosTelefono.add(telefonoPrincipal.trim());
		}
		this.serviciosContratados = new ArrayList<>();
		this.misFacturas = new ArrayList<>();
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

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
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

	public float getDeudaPendiente() {
		return deudaPendiente;
	}

	public void setDeudaPendiente(float deudaPendiente) {
		this.deudaPendiente = deudaPendiente;
	}

	public float getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(float limiteCredito) {
		this.limiteCredito = limiteCredito;
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

	public ArrayList<Servicio> getServiciosContratados() {
		return serviciosContratados;
	}

	public void setServiciosContratados(ArrayList<Servicio> serviciosContratados) {
		this.serviciosContratados = serviciosContratados;
	}

	public ArrayList<Factura> getMisFacturas() {
		return misFacturas;
	}

	public void setMisFacturas(ArrayList<Factura> misFacturas) {
		this.misFacturas = misFacturas;
	}

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
		if (!tieneTelefono(nuevoTelefono)) {
			this.numerosTelefono.add(nuevoTelefono);
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

	public void agregarServicio(Servicio servicio) {
		this.serviciosContratados.add(servicio);
	}

	public boolean verificarSuspension() {
		if (this.deudaPendiente > this.limiteCredito) {
			this.estadoActivo = false;
		}
		return !this.estadoActivo;
	}

	public void abonarDeuda(float monto) {
		if (monto > 0 && monto <= this.deudaPendiente) {
			this.deudaPendiente = this.deudaPendiente - monto;
			this.fidepuntos = this.fidepuntos + (int) (monto / 100);
			if (this.deudaPendiente <= this.limiteCredito) {
				this.estadoActivo = true;
			}
		}
	}

	public boolean canjearFidepuntos(int puntosRequeridos) {
		if (puntosRequeridos > 0 && this.fidepuntos >= puntosRequeridos) {
			this.fidepuntos = this.fidepuntos - puntosRequeridos;
			return true;
		}
		return false;
	}

	/*
	public Servicio buscarServicioById(String idServicio) {
		Servicio servAux = null;
		int i = 0;
		boolean encontrado = false;
		while (!encontrado && i < serviciosContratados.size()) {
			if (serviciosContratados.get(i).getIdServicio().equalsIgnoreCase(idServicio)) {
				servAux = serviciosContratados.get(i);
				encontrado = true;
			}
			i++;
		}
		return servAux;
	}

	public void eliminarServicio(String idServicio) {
		Servicio aux = buscarServicioById(idServicio);
		if (aux != null) {
			serviciosContratados.remove(aux);
		}
	}

	*/
	
	/*EN espera de clase servicio para completar los calculos de costos 
	public void agregarFactura(Factura factura) {
		this.misFacturas.add(factura);
		this.deudaPendiente = this.deudaPendiente + calcularRentaMensual();
		int i = 0;
		while (i < serviciosContratados.size()) {
			serviciosContratados.get(i).consumirMesPromocion();
			i++;
		}
		verificarSuspension();
	}

	public float calcularRentaMensual() {
		float total = 0;
		int i = 0;
		while (i < serviciosContratados.size()) {
			total = total + serviciosContratados.get(i).calcularCosto();
			i++;
		}
		return total;
	}
	*/
}