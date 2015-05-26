package mundo;

import java.util.ArrayList;

public class Componente implements Material{
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	public static final String NOMBRE = "COMPONENTES";
	
	public static final String[] COLUMNAS = {"id","unidadMedida","cantidadInicial"};
	
	public static final String[] TIPO = {"String","String","int"};
	
	public static final String NOMBRE_REGISTRO_COMPONENTES = "registrosComponentes";
	
	public static final String[] COLUMNAS_REGISTRO_COMPONENTES = {"id","idComponente"};
	
	public static final String[] TIPO_REGISTRO_COMPONENTES = {"String","String"};
	
	private String id;
	
	private String unidadMedida;
	
	private int cantidadInicial;
	
	private String tipo;
	
	private int dia;
	
	private int mes;
	
	private ArrayList<String> pedidos;
	
	//--------------------------------------------------
	// CONSTRUCTOR
	//--------------------------------------------------

	public Componente(String pId, String unidadMedida, int pCantidadInicial) {
		this.id = pId;
		this.unidadMedida = unidadMedida;
		this.cantidadInicial = pCantidadInicial;
		this.tipo = "Componente";
	}
	
	public Componente(String id, String unidadMedida, int cantidad, int dia, int mes) {
		this.id = id;
		this.unidadMedida = unidadMedida;
		this.cantidadInicial = cantidad;
		this.dia = dia;
		this.mes = mes;
		this.pedidos = new ArrayList<String>();
		this.tipo = "Componente";
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

	public int getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(int cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}
	
	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void addPedido(String idPedido){
		this.pedidos.add(idPedido);
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public int getDia() {
		return dia;
	}

	public int getMes() {
		return mes;
	}

	public ArrayList<String> getPedidos() {
		return pedidos;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public void setPedidos(ArrayList<String> pedidos) {
		this.pedidos = pedidos;
	}

	public String toString(){
		return "id: " + id + ", cantidad; " + cantidadInicial;
	}
}
