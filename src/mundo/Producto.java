package mundo;

import java.util.ArrayList;

public class Producto {
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------

	public static final String NOMBRE = "productos";
	
	public final static String[] COLUMNAS = {"id", "nombre", "precio"};
	
	public final static String[] TIPO = {"String", "String", "int"};
	
	public static final String NOMBRE_REGISTRO_PRODUCTOS = "registrosProductos";
	
	public static final String[] COLUMNAS_REGISTRO_PRODUCTOS = {"id","idEtapa","idInventario","idRegistroEstacion","idRegistroMateriaPrima","idRegistroComponente"};
	
	public static final String[] TIPO_REGISTRO_PRODUCTOS = {"String","String","String","String","String","String"};
				
	public static final String NOMBRE_INVENTARIO_PRODUCTOS = "inventarioProductos";
	
	public static final String[] COLUMNAS_INVENTARIO_PRODUCTOS = {"id","idProducto","idPedido"};
	
	public static final String[] TIPO_INVENTARIO_PRODUCTOS = {"String","String", "String"};

	private String id;

	private String nombre;

	private int precio;
	
	private ArrayList<Etapa> etapasProduccion;
	
	//---------------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------------

	public Producto(String id, String nombre, int precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.etapasProduccion = new ArrayList<Etapa>();
	}

	//---------------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public ArrayList<Etapa> getEtapasProduccion() {
		return etapasProduccion;
	}

	public void setEtapasProduccion(ArrayList<Etapa> etapasProduccion) {
		this.etapasProduccion = etapasProduccion;
	}
	
	
	
}
