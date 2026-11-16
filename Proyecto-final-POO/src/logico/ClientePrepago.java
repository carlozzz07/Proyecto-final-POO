package logico;

import java.time.LocalDate;

public class ClientePrepago extends Cliente {
	private double saldo;

	public ClientePrepago(String idCliente, String cedula, String nombreCliente, String direccion, String telefonoPrincipal, String numeroContrato, double saldo) {
		super(idCliente, cedula, nombreCliente, direccion, telefonoPrincipal, numeroContrato);
		this.saldo = saldo;
	}

	@Override
	public String getTipo() {
		return "Prepago";
	}

	@Override
	public boolean comprarPaquete(Paquete paquete) {
		if (this.estadoActivo && paquete != null && this.saldo >= paquete.getPrecio()) {
			this.saldo = this.saldo - paquete.getPrecio();
			PaqueteActivo nuevoPaquete = new PaqueteActivo(paquete, LocalDate.now());
			this.paquetes.add(nuevoPaquete);
			this.fidepuntos = this.fidepuntos + (int) (paquete.getPrecio() / 100);
			return true;
		}
		return false;
	}

	public void recargar(double monto) {
		if (monto > 0) {
			this.saldo = this.saldo + monto;
			this.estadoActivo = true;
		}
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}