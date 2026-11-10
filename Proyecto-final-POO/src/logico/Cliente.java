package logico;

public class Cliente {
	private String nombreCliente;
	private String idCliente;
	private int numeroTelefono;
	private int numeroContrato;
	public Cliente(String nombreCliente, String idCliente, int numeroContrato) {
		super();
		this.nombreCliente = nombreCliente;
		this.idCliente = idCliente;
		this.numeroContrato = numeroContrato;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	public int getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(int numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public int getNumeroContrato() {
		return numeroContrato;
	}
	public void setNumeroContrato(int numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

}
