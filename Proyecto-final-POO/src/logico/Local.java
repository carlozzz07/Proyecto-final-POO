package logico;

import java.util.ArrayList;

public class Local {
	
	private String idLocal;
	private String nombreSucursal;
	private String direccion;
	private ArrayList<Factura>facturasEmitidas;
	private ArrayList<Personal>empleadosAsignados;
	
	public Local(String idLocal, String nombreSucursal, String direccion, ArrayList<Factura> facturasEmitidas,
			ArrayList<Personal> empleadosAsignados) {
		super();
		this.idLocal = idLocal;
		this.nombreSucursal = nombreSucursal;
		this.direccion = direccion;
		this.facturasEmitidas = new ArrayList<Factura>(facturasEmitidas);
		this.empleadosAsignados = new ArrayList<Personal>(empleadosAsignados);
	}

	public String getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(String idLocal) {
		this.idLocal = idLocal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public ArrayList<Factura> getFacturasEmitidas() {
		return facturasEmitidas;
	}

	public void setFacturasEmitidas(ArrayList<Factura> facturasEmitidas) {
		this.facturasEmitidas = facturasEmitidas;
	}

	public ArrayList<Personal> getEmpleadosAsignados() {
		return empleadosAsignados;
	}

	public void setEmpleadosAsignados(ArrayList<Personal> empleadosAsignados) {
		this.empleadosAsignados = empleadosAsignados;
	}
	
	//void agregarFactura(Factura factura);
	//void agregarEmpleado(Personal empleado);
	//Personal buscarEmpleadoDelMes(int mes);
	
	
}
