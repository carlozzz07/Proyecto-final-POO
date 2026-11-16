package logico;

public abstract class Personal {

	protected String codigo;
	protected String cedula;
	protected String nombre;
	protected String apellidos;
	protected String telefono;
	protected String usario;
	protected String contrasena;
	protected boolean estado;
	
	public Personal(String codigo, String cedula, String nombre, String apellidos, String telefono, String usario,
			String contrasena, boolean estado) {
		super();
		this.codigo = codigo;
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.usario = usario;
		this.contrasena = contrasena;
		this.estado = estado;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getCedula() {
		return cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getUsario() {
		return usario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public boolean isEstado() {
		return estado;
	}
	
	
}
