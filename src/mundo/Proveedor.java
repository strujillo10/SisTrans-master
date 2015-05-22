package mundo;

import java.util.ArrayList;
import java.util.List;

public class Proveedor{
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	public static final String NOMBRE = "Proveedores";
	
	public static final String[] COLUMNAS = {"id","direccion","telefono","ciudad","idRepLegal"};
	
	public static final String[] TIPO = {"String","String","String","String","String"};
	
	public static final String NOMBRE_RELACION_MATERIA_PRIMA = "ProveedoresMateriasPrimas";
	
	public static final String[] COLUMNAS_RELACION_MATERIA_PRIMA = {"id_proveedor","id_materiaPrima"};
	
	public static final String[] TIPO_RELACION_MATERIA_PRIMA = {"String","String"};
	
	public static final String NOMBRE_RELACION_COMPONENTE = "ProveedoresComponentes";
	
	public static final String[] COLUMNAS_RELACION_COMPONENTE = {"id_proveedor","id_componente"};
	
	public static final String[] TIPO_RELACION_COMPONENTE = {"String","String"};
	
	private String id;
	
	private String direccion;
	
	private int telefono;
	
	private String ciudad;
	
	private String idRepLegal;
	
	private ArrayList<MateriaPrima> materiasPrimas;
	
	private ArrayList<Componente> componentes;
	
	
	
	//--------------------------------------------------
	// CONSTRUCTOR
	//--------------------------------------------------

	public Proveedor(String pId, String pDireccion, int pTelefono, String pCiudad, String pIdRepLegal) {
		this.id = pId;
		this.direccion = pDireccion;
		this.telefono = pTelefono;
		this.ciudad = pCiudad;
		this.idRepLegal = pIdRepLegal;
		this.materiasPrimas = new ArrayList<MateriaPrima>();
		this.componentes = new ArrayList<Componente>();	
	}
	
	//--------------------------------------------------
	// GETTERS AND SETTERS
	//--------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getIdRepLegal() {
		return idRepLegal;
	}

	public void setIdRepLegal(String idRepLegal) {
		this.idRepLegal = idRepLegal;
	}

	public ArrayList<MateriaPrima> getMateriasPrimas() {
		return materiasPrimas;
	}

	public void setMateriasPrimas(ArrayList<MateriaPrima> materiasPrimas) {
		this.materiasPrimas = materiasPrimas;
	}

	public ArrayList<Componente> getComponentes() {
		return componentes;
	}

	public void setComponentes(ArrayList<Componente> componentes) {
		this.componentes = componentes;
	}
	
	public void addMateriaPrima (MateriaPrima materiaPrima){
		this.materiasPrimas.add(materiaPrima);
	}
	
	public void addComponente (Componente componente){
		this.componentes.add(componente);
	}
	
	public String toString (){
		return "id: " + id + " ciudad: " + ciudad;
	}
	
}
