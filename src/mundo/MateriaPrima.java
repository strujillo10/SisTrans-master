package mundo;

import java.util.ArrayList;

public class MateriaPrima {
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	public static final String NOMBRE = "materiasPrimas";
	
	public static final String[] COLUMNAS = {"id","unidadMedida","cantidadInicial"};
	
	public static final String[] TIPO = {"String","String","int"};
	
	public static final String NOMBRE_REGISTRO_MATERIAS_PRIMAS = "registrosMateriasPrimas";
	
	public static final String[] COLUMNAS_REGISTRO_MATERIAS_PRIMAS = {"id","idMateriaPrima"};
	
	public static final String[] TIPO_REGISTRO_MATERIAS_PRIMAS = {"String","String"};
	
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

	public MateriaPrima(String pId, String pUnidadMedidad, int pCantidadInicial) {
		this.id = pId;
		this.unidadMedida = pUnidadMedidad;
		this.cantidadInicial = pCantidadInicial;
		this.tipo = "Materia Prima";
	}
	
	public MateriaPrima(String pId, String pUnidadMedidad, int pCantidadInicial, int dia, int mes) {
		this.id = pId;
		this.unidadMedida = pUnidadMedidad;
		this.cantidadInicial = pCantidadInicial;
		this.dia = dia;
		this.mes = mes;
		this.pedidos = new ArrayList<String>();
		this.tipo = "Materia Prima";
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

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
		
	}

	public int getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(int cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}
	
	public String getTipo() {
		return tipo;
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
	
	public void addPedido(String idPedido){
		this.pedidos.add(idPedido);
	}

	public void setPedidos(ArrayList<String> pedido) {
		this.pedidos = pedido;
	}

	public String toString(){
		return "id: " + id + ", unidadMedida: " + unidadMedida + ", cantidad; " + cantidadInicial;
	}

}
