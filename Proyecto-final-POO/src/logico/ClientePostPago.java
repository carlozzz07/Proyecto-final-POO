package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class ClientePostPago extends Cliente {
	private double deudaPendiente;
	private double limiteCredito;
	private ArrayList<Contrato> contratos;
	private ArrayList<Factura> misFacturas;

	public ClientePostPago(String idCliente, String cedula, String nombreCliente, String direccion, String telefonoPrincipal, String numeroContrato, double limiteCredito) {
		super(idCliente, cedula, nombreCliente, direccion, telefonoPrincipal, numeroContrato);
		this.deudaPendiente = 0.0;
		this.limiteCredito = limiteCredito;
		this.contratos = new ArrayList<>();
		this.misFacturas = new ArrayList<>();
	}

	@Override
	public String getTipo() {
		return "Postpago";
	}

	@Override
	public boolean comprarPaquete(Paquete paquete) {
		if (this.estadoActivo && paquete != null && (this.deudaPendiente + paquete.getPrecio()) <= this.limiteCredito) {
			this.deudaPendiente = this.deudaPendiente + paquete.getPrecio();
			PaqueteActivo nuevoPaquete = new PaqueteActivo(paquete, LocalDate.now());
			this.paquetes.add(nuevoPaquete);
			verificarSuspension();
			return true;
		}
		return false;
	}

	public void agregarContrato(Contrato contrato) {
		if (contrato != null) {
			this.contratos.add(contrato);
		}
	}

	public double calcularRentaMensual() {
		double total = 0.0;
		int i = 0;
		while (i < contratos.size()) {
			if (contratos.get(i).isActivo()) {
				total = total + contratos.get(i).calcularCosto();
			}
			i++;
		}
		return total;
	}

	public void agregarFactura(Factura factura) {
		if (factura != null) {
			this.misFacturas.add(factura);
			this.deudaPendiente = this.deudaPendiente + calcularRentaMensual();
			int i = 0;
			while (i < contratos.size()) {
				if (contratos.get(i).isActivo()) {
					contratos.get(i).consumirMesPromocion();
				}
				i++;
			}
			verificarSuspension();
		}
	}

	public boolean verificarSuspension() {
		if (this.deudaPendiente > this.limiteCredito) {
			this.estadoActivo = false;
		}
		return !this.estadoActivo;
	}

	public void abonarDeuda(double monto) {
		if (monto > 0 && monto <= this.deudaPendiente) {
			this.deudaPendiente = this.deudaPendiente - monto;
			this.fidepuntos = this.fidepuntos + (int) (monto / 100);
			if (this.deudaPendiente <= this.limiteCredito) {
				this.estadoActivo = true;
			}
		}
	}

	public double getDeudaPendiente() {
		return deudaPendiente;
	}

	public void setDeudaPendiente(double deudaPendiente) {
		this.deudaPendiente = deudaPendiente;
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public ArrayList<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<Contrato> contratos) {
		this.contratos = contratos;
	}

	public ArrayList<Factura> getMisFacturas() {
		return misFacturas;
	}

	public void setMisFacturas(ArrayList<Factura> misFacturas) {
		this.misFacturas = misFacturas;
	}
}