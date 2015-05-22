package mundo;

import java.util.ArrayList;

public class Usuario {
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	public static final String NOMBRE = "usuarios";
	
	public static final String[] COLUMNAS = {"login", "password", "tipo", "nombre", "direccion", "telefono", "ciudad", "idRepLegal"};
	
	public static final String[] TIPO = {"String", "String", "String", "String", "String", "int", "String", "String"};
	
	private String login;
	
	private String tipo;
	
	private String password;
	
	private String nombre;
	
	private String direccion;
	
	private int telefono;
	
	private String ciudad;
	
	private String idRepLegal;
	
	private ArrayList<Pedido> pedidos;
	
	//--------------------------------------------------
	// CONSTRUCTOR
	//--------------------------------------------------

	public Usuario(String login, String tipo, String password) {
		this.tipo = tipo;
		this.login = login;
		this.password = password;
		this.pedidos = new ArrayList<Pedido>();
	}
	
	public Usuario(String login, String tipo, String password, String nombre,
			String direccion, int telefono, String ciudad, String idRepLegal) {
		this.login = login;
		this.tipo = tipo;
		this.password = password;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.ciudad = ciudad;
		this.idRepLegal = idRepLegal;
		this.pedidos = new ArrayList<Pedido>();
	}

	
	//--------------------------------------------------
	// SETTERS AND GETTERS
	//--------------------------------------------------

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public int getTelefono() {
		return telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getIdRepLegal() {
		return idRepLegal;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setIdRepLegal(String idRepLegal) {
		this.idRepLegal = idRepLegal;
	}	
	
	public ArrayList<Pedido> getPedidos(){
		return pedidos;
	}
	
	public void addPedido (Pedido pedido){
		this.pedidos.add(pedido);
	}
	
	public void addAllPedidos (ArrayList<Pedido> aAgregar){
		this.pedidos.addAll(aAgregar);
	}

}
