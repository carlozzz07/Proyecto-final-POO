package logico;

public abstract class Personal {

	protected String codigo;
	protected String cedula;
	protected String nombre;
	protected String apellidos;
	protected String telefono;
	protected String usuario;
	protected String contrasena;
	protected boolean estado;
	protected float sueldoBase;
	
	public Personal(String codigo, String cedula, String nombre, String apellidos, String telefono, String usario,
			String contrasena, boolean estado, float sueldoBase) {
		super();
		this.codigo = codigo;
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.usuario = usario;
		this.contrasena = contrasena;
		this.sueldoBase= sueldoBase;
		
		this.estado = true;
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
		return usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public boolean isEstado() {
		return estado;
	}

	public float getSueldoBase() {
		return sueldoBase;
	}

	public void setSueldoBase(float sueldoBase) {
		this.sueldoBase = sueldoBase;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public abstract String getTipo();
	
}
