package mundo;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

public class Estacion {
	
	public static final String NOMBRE = "estaciones";
	
	public static final String[] COLUMNAS = {"id", "nombre", "tipo", "activada"};
	
	public static final String[] TIPO = {"String", "String", "String", "int"};
	
	public static final String NOMBRE_REGISTRO_ESTACIONES = "registrosEstaciones";
	
	public static final String[] COLUMNAS_REGISTRO_ESTACIONES = {"id", "idEstacion","dia","mes"};
	
	public static final String[] TIPO_REGISTRO_ESTACIONES = {"String", "String","int","int"};
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	private String id;
	
	private String nombre;
	
	private String tipo;
	
	private boolean activada;
		
	//--------------------------------------------------
	// CONSTRUCTOR
	//--------------------------------------------------

	public Estacion(String pId, String pNombre, String pTipo, boolean activada) {
		this.id = pId;
		this.nombre = pNombre;
		this.tipo = pTipo;
		this.activada = activada;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String idNombre) {
		this.nombre = idNombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public boolean isActivada() {
		return activada;
	}

	public void setActivada(boolean activada) {
		this.activada = activada;
	}
	
}
