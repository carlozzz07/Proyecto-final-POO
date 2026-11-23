package logico;

public abstract class Persona {

	protected String id;
	protected String cedula;
	protected String nombre;
	protected String telefono;
	protected String direccion;
	protected String usuario;
	protected String contrasena;
	protected boolean estado;

	public Persona(String id, String cedula, String nombre, String telefono, String direccion,
			String usuario, String contrasena) {
		super();
		this.id = id;
		this.cedula = cedula;
		this.nombre = nombre;
		this.telefono = telefono;
		this.direccion = direccion;
		if (usuario != null) {
			this.usuario = usuario;
		} else {
			this.usuario = "";
		}
		if (contrasena != null) {
			this.contrasena = contrasena;
		} else {
			this.contrasena = "";
		}
		this.estado = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		if (usuario != null) {
			this.usuario = usuario;
		} else {
			this.usuario = "";
		}
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		if (contrasena != null) {
			this.contrasena = contrasena;
		} else {
			this.contrasena = "";
		}
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public abstract String getTipo();
}