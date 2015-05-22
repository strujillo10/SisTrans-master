package mundo;

import java.util.ArrayList;

public class Etapa {
	
	public static final String NOMBRE = "etapas";
	
	public static final String[] COLUMNAS = {"id", "nombre", "idProducto", "idEstacion", "idMateriaPrima", "idComponente", "duracion", "numeroSecuencia", "idAnterior"};
	
	public static final String[] TIPO = {"String", "String", "String", "String", "String", "String", "int", "int", "String"};
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	private String id;
	
	private String nombre;

	private String idProducto;
	
	private ArrayList<String> pedidos;
	
	private int dia;
	
	private int mes;
	
	private String idEstacion;

	private String idMateriaPrima;
	
	private String idComponente;
	
	private int duracion;
	
	private int numeroSecuencia;
	
	private String idAnterior;

	
	//--------------------------------------------------
	// CONSTRUCTOR
	//--------------------------------------------------

	public Etapa(String id, String nombre, String idProducto, String idEstacion,
			String idMateriaPrima, String idComponente, int duracion,
			int numeroSecuencia, String idAnterior) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.idProducto = idProducto;
		this.idEstacion = idEstacion;
		this.idMateriaPrima = idMateriaPrima;
		this.idComponente = idComponente;
		this.duracion = duracion;
		this.numeroSecuencia = numeroSecuencia;
		this.idAnterior = idAnterior;
	}
	
	public Etapa(String id, String nombre, String idProducto, int dia, int mes,
			String materiaPrima, String componente, int duracion,
			int numeroSecuencia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.idProducto = idProducto;
		this.pedidos = new ArrayList<String>();
		this.dia = dia;
		this.mes = mes;
		this.idMateriaPrima = materiaPrima;
		this.idComponente = componente;
		this.duracion = duracion;
		this.numeroSecuencia = numeroSecuencia;
	}
	
	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public String getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(String idEstacion) {
		this.idEstacion = idEstacion;
	}

	public String getIdMateriaPrima() {
		return idMateriaPrima;
	}

	public void setIdMateriaPrima(String idMateriaPrima) {
		this.idMateriaPrima = idMateriaPrima;
	}

	public String getIdComponente() {
		return idComponente;
	}

	public void setIdComponente(String idComponente) {
		this.idComponente = idComponente;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getIdAnterior() {
		return idAnterior;
	}

	public void setIdAnterior(String idAnterior) {
		this.idAnterior = idAnterior;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumeroSecuencia() {
		return numeroSecuencia;
	}

	public void setNumeroSecuencia(int numeroSecuencia) {
		this.numeroSecuencia = numeroSecuencia;
	}

	public String getProducto() {
		return idProducto;
	}

	public void setProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public ArrayList<String> getIdPedido() {
		return pedidos;
	}

	public int getDia() {
		return dia;
	}

	public int getMes() {
		return mes;
	}
	
	
	//--------------------------------------------------
	// MÉTODOS
	//--------------------------------------------------

	public void addIdPedido (String idPedido){
		this.pedidos.add(idPedido);
	}
	
	public void setIdPedido(ArrayList<String> idPedido) {
		this.pedidos = idPedido;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public String toString(){
		return COLUMNAS[0] + " : " + id + " , " + COLUMNAS[1] + " : " + nombre + " , " + COLUMNAS[2] + " : " + idProducto + " , " + COLUMNAS[3] + " : " + idEstacion + " , " + COLUMNAS[4] + " : " + idMateriaPrima + " , " + COLUMNAS[5] + " : " + idComponente + " , " + COLUMNAS[6] + " : " + duracion + " , " + COLUMNAS[7] + " : " + numeroSecuencia + " , " + COLUMNAS[8] + " : " + idAnterior;
	}
	
	
}
