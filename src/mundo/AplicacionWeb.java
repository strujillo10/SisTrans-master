package mundo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;


public class AplicacionWeb {

	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	public static final String VERDADERO = "'1'='1'"; 
	
	public static final String FALSO = "'1'='2'"; 

	public static final String ID = "generadorId";

	public static final String[] COLUMNAS = {"id"};

	public static final String[] TIPO = {"String"};

	private static CRUD crud;

	private static ConexionDAO conexion;

	private static AplicacionWeb instancia ;

	private String usuarioActual;

	private int contadorId;


	//--------------------------------------------------
	// CONSTRUCTOR E INSTANCIACION
	//--------------------------------------------------

	public static AplicacionWeb getInstancia() {		
		if(instancia == null){
			instancia = new AplicacionWeb(); 
		}
		return instancia;
	}

	public AplicacionWeb() {
		conexion = new ConexionDAO();
		conexion.iniciarConexion();
		conexion.crearTablas();
		crud = new CRUD(conexion);
//		poblarTablas();
		try
		{
			Statement s = crud.darConexion().createStatement();
			String sql = "SELECT MAX(id) FROM generadorId";
			System.out.println(sql);
			ResultSet rs = s.executeQuery(sql);
			while(rs.next())
			{
				contadorId = Integer.parseInt(rs.getString(1));
				System.out.println("El id actual es: " + contadorId);
			}
		}
		catch (Exception e){
			contadorId = 1004;
		}
	}

	//--------------------------------------------------
	// GETTERS AND SETTERS
	//--------------------------------------------------

	public int darContadorId(){
		try
		{
			String[] id = {Integer.toString(++contadorId)};
			crud.insertarTupla(ID, COLUMNAS, TIPO, id);
			return contadorId;
		}
		catch (Exception e)
		{
			return ++contadorId;
		}
	}

	public static CRUD getCrud() {
		return crud;
	}

	public static void setCrud(CRUD crud) {
		AplicacionWeb.crud = crud;
	}

	public static ConexionDAO getConexion() {
		return conexion;
	}

	public static void setConexion(ConexionDAO conexion) {
		AplicacionWeb.conexion = conexion;
	}

	public static void setInstancia(AplicacionWeb instancia) {
		AplicacionWeb.instancia = instancia;
	}
	
	//--------------------------------------------------
	// METODOS AUXILIARES
	//--------------------------------------------------
	
	/**
	 * 
	 */
	public static void poblarTablas(){
		crud.poblarTablas();	
	}

	//--------------------------------------------------
	// METODOS CREAR
	//--------------------------------------------------
	
	/**
	 * @param login
	 * @param password
	 * @param tipo
	 * @throws Exception
	 */
	public void registrarUsuario (String login, String password, String tipo) throws Exception{
		//		columnas de Usuario: login, password, tipo, nombre, direccion, telefono, ciudad, idRepLegal
		
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try{
			String[] datos = {login, password, tipo, "", "", Integer.toString(0), "", ""}; 
			crud.insertarTupla(Usuario.NOMBRE, Usuario.COLUMNAS, Usuario.TIPO, datos);
			usuarioActual = login;
			System.out.println("El usuario actual es:" + usuarioActual);
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}
	
	/**
	 * 
	 * @param login
	 * @param nombre
	 * @param direccion
	 * @param telefono
	 * @param ciudad
	 * @param idRepLegal
	 * @throws Exception
	 */
	public void registrarCliente (String login, String nombre, String direccion, int telefono, String ciudad, String idRepLegal) throws Exception{
		System.out.println("El usuario " + login + "ingresa por primera vez. " + nombre + direccion + telefono + ciudad + idRepLegal);
		String sql = "UPDATE " + Usuario.NOMBRE + " SET nombre = '" + nombre + "', direccion = '" + direccion + "', telefono = " + telefono + ", ciudad = '" + ciudad + "', idRepLegal = '" + idRepLegal + "' WHERE login = '" + login + "'";
		System.out.println(sql);
		
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try{
			crud.darConexion().createStatement().executeQuery(sql);
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}
	
	/**
	 * @param idProveedor
	 * @param direccion
	 * @param telefono
	 * @param ciudad
	 * @param idRepLegal
	 * @throws Exception
	 */
	public void registrarProveedor (String idProveedor, String direccion, int telefono, String ciudad, String idRepLegal) throws Exception{
		String[] id = {idProveedor};
		String[] datosSimples = {id[0],direccion, Integer.toString(telefono) ,ciudad,idRepLegal};
			
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try{
			crud.insertarTupla(Proveedor.NOMBRE, Proveedor.COLUMNAS, Proveedor.TIPO, datosSimples);
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
		
		}

	/**
	 * @param id
	 * @param unidadMedida
	 * @param cantidadInicial
	 * @throws Exception
	 */
	public void registrarMateriaPrima (String id, String unidadMedida, int cantidadInicial, String idProveedor) throws Exception{
		String[] datosSimples = {id.toLowerCase(), unidadMedida, Integer.toString(cantidadInicial)};
		int cantidadActual = cantidadInicial;
		
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try
		{
			try{
				cantidadActual+= Integer.parseInt((crud.darSubTabla(MateriaPrima.NOMBRE, "cantidadInicial", "UPPER(id)=UPPER('"+id+"')").get(0)));
				String[] columnas = new String[1];
				columnas[0] = "cantidadInicial";
				String[] cantidad = new String[1];
				cantidad[0] = (Integer.toString(cantidadInicial + cantidadActual));
				crud.actualizarTupla(MateriaPrima.NOMBRE,columnas,cantidad, "UPPER(id)= UPPER('"+id+"')");
				
				ArrayList<String> relacion = crud.darSubTabla(Proveedor.NOMBRE_RELACION_MATERIA_PRIMA, "id_Proveedor", "id_Proveedor = '" + idProveedor + "' AND UPPER(id_MateriaPrima) = UPPER('" + id + "')");
				if (relacion.isEmpty())
				{
					String[] datosRelacion = {idProveedor,id.toLowerCase()}; 
					crud.insertarTupla(Proveedor.NOMBRE_RELACION_MATERIA_PRIMA, Proveedor.COLUMNAS_RELACION_MATERIA_PRIMA, Proveedor.TIPO_RELACION_MATERIA_PRIMA, datosRelacion);
				}
			}
			catch(Exception e){
				crud.insertarTupla(MateriaPrima.NOMBRE, MateriaPrima.COLUMNAS, MateriaPrima.TIPO, datosSimples);
				String[] datosRelacion = {idProveedor,id.toLowerCase()}; 
				crud.insertarTupla(Proveedor.NOMBRE_RELACION_MATERIA_PRIMA, Proveedor.COLUMNAS_RELACION_MATERIA_PRIMA, Proveedor.TIPO_RELACION_MATERIA_PRIMA, datosRelacion);
			}
			for (int i = 0; i < cantidadInicial; i++) {
				String[] datosRegistro = {Integer.toString(darContadorId()), id.toLowerCase()};
				crud.insertarTupla(MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS, MateriaPrima.COLUMNAS_REGISTRO_MATERIAS_PRIMAS, MateriaPrima.TIPO_REGISTRO_MATERIAS_PRIMAS, datosRegistro);
			}
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}

	/**
	 * @param id
	 * @param cantidadInicial
	 * @throws Exception
	 */
	public void registrarComponente (String id, int cantidadInicial, String idProveedor) throws Exception {
		String[] datosSimples = {id.toLowerCase(),"unidades", Integer.toString(cantidadInicial)};
		int cantidadActual = cantidadInicial;
		
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try
		{
			try{
				cantidadActual+= Integer.parseInt((crud.darSubTabla(Componente.NOMBRE, "cantidadInicial", "UPPER(id)=UPPER('"+id+"')").get(0)));
				String[] columnas = new String[1];
				columnas[0] = "cantidadInicial";
				String[] cantidad = new String[1];
				cantidad[0] = (Integer.toString(cantidadInicial + cantidadActual));
				crud.actualizarTupla(Componente.NOMBRE,columnas,cantidad, "UPPER(id)=UPPER('"+id+"')");
				
				ArrayList<String> relacion = crud.darSubTabla(Proveedor.NOMBRE_RELACION_COMPONENTE, "id_Proveedor", "id_Proveedor = '" + idProveedor + "' AND UPPER(id_Componente) = UPPER('" + id + "')");
				if (relacion.isEmpty())
				{
					String[] datosRelacion = {idProveedor,id.toLowerCase()}; 
					crud.insertarTupla(Proveedor.NOMBRE_RELACION_COMPONENTE, Proveedor.COLUMNAS_RELACION_COMPONENTE, Proveedor.TIPO_RELACION_COMPONENTE, datosRelacion);
				}
			}
			catch(Exception e){
				crud.insertarTupla(Componente.NOMBRE, Componente.COLUMNAS, Componente.TIPO, datosSimples);
				String[] datosRelacion = {idProveedor,id.toLowerCase()}; 
				crud.insertarTupla(Proveedor.NOMBRE_RELACION_COMPONENTE, Proveedor.COLUMNAS_RELACION_COMPONENTE, Proveedor.TIPO_RELACION_COMPONENTE, datosRelacion);
			}
			for (int i = 0; i < cantidadInicial; i++) {
				String[] datosRegistro = {Integer.toString(darContadorId()), id.toLowerCase()};
				crud.insertarTupla(Componente.NOMBRE_REGISTRO_COMPONENTES, Componente.COLUMNAS_REGISTRO_COMPONENTES, Componente.TIPO_REGISTRO_COMPONENTES, datosRegistro);
			}
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}

	/**
	 * @param id
	 * @param nombre
	 * @param precio
	 * @throws Exception
	 */
	public void registrarProducto (String id, String nombre, int precio) throws Exception{
		String[] datos = {id, nombre, Integer.toString(precio)};
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try{
			crud.insertarTupla(Producto.NOMBRE, Producto.COLUMNAS, Producto.TIPO, datos);
			System.out.println("Se registro " + datos);
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}
	
	public void registrarEstacion (String id, String nombre, String tipo) throws Exception {
		String[] datos = {id, nombre, tipo, Integer.toString(1)};
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try{
			crud.insertarTupla(Estacion.NOMBRE, Estacion.COLUMNAS, Estacion.TIPO, datos);
			System.out.println("Se registro " + datos);
			Date hoy = new Date();
			for (int i = 0; i < 30; i++){
				String[] datosRegistros = {Integer.toString(darContadorId()), id, Integer.toString(hoy.getDate()), Integer.toString(hoy.getMonth())};
				crud.insertarTupla(Estacion.NOMBRE_REGISTRO_ESTACIONES, Estacion.COLUMNAS_REGISTRO_ESTACIONES, Estacion.TIPO_REGISTRO_ESTACIONES, datosRegistros);
				Calendar calendario = Calendar.getInstance();
				calendario.setTime(hoy);
				calendario.add(Calendar.DATE, 1);
				hoy = calendario.getTime();
			}
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			rollBack.printStackTrace();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}

	/**
	 * @param id
	 * @param idProducto
	 * @param idEstacion
	 * @param idMateriaPrima
	 * @param idComponente
	 * @param duracion
	 * @param numeroSecuencia
	 * @param idAnterior
	 * @throws Exception
	 */
	public void registrarEtapaProduccion  (String id, String nombre, String idProducto, String idEstacion, String idMateriaPrima, String idComponente, int duracion, int numeroSecuencia, String idAnterior) throws Exception{
		String[] datos = {id, nombre, idProducto, idEstacion, idMateriaPrima, idComponente, Integer.toString(duracion), Integer.toString(numeroSecuencia), idAnterior};
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		try{
			crud.insertarTupla(Etapa.NOMBRE, Etapa.COLUMNAS, Etapa.TIPO, datos);
		}
		catch (Exception rollBack){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
		
	}
	
	/**
	 * @param login
	 * @param producto
	 * @param cantidad
	 * @param pedido
	 * @param entrega
	 * @throws Exception
	 */
	public Date registrarPedido (String login, String idProducto, int cantidad, Date fechaPedido) throws Exception{		
		System.out.println(login + " - " + idProducto + " - " + cantidad + " - " + fechaPedido.toLocaleString());
		ArrayList<Etapa> etapas = new ArrayList<Etapa>();
		String idPedido = Integer.toString(darContadorId());
		
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		
		etapas = obtenerEtapas(idProducto);
		Date fechaEntrega = null;
		String[] datosPedido = {idPedido,idProducto,login,Integer.toString(fechaPedido.getDay()),Integer.toString(fechaPedido.getMonth()),Integer.toString(fechaPedido.getDay()),Integer.toString(fechaPedido.getDay()),Integer.toString(cantidad)};
		crud.insertarTupla(Pedido.NOMBRE, Pedido.COLUMNAS, Pedido.TIPO, datosPedido);
		
		ArrayList<String> idInventarios = new ArrayList<String>();
		for(int i = 0; i < cantidad; i++){
			try{
			idInventarios.add(Integer.toString(darContadorId()));
			String[] datosInventario = {idInventarios.get(i),idProducto,idPedido};
			crud.insertarTupla(Producto.NOMBRE_INVENTARIO_PRODUCTOS, Producto.COLUMNAS_INVENTARIO_PRODUCTOS, Producto.TIPO_INVENTARIO_PRODUCTOS, datosInventario);
			}
			catch(Exception e){
				conexion.darConexion().rollback(save);
				e.printStackTrace();
			}
		}
		for(Etapa etapa : etapas){
			try{
				fechaEntrega = verificarExistencias(idProducto,etapa,cantidad,etapas.size(),idPedido,idInventarios);
			}
			catch(Exception e){
				conexion.darConexion().rollback(save);
				e.printStackTrace();
				throw new Exception();
			}
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
		return fechaEntrega;
	}	
	
	/**
	 * @param idProducto
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Etapa> obtenerEtapas (String idProducto) throws Exception{
		ArrayList<Etapa> etapas = new ArrayList<Etapa>();
		ResultSet rs_etapas = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Etapa.NOMBRE + " WHERE idProducto = '" + idProducto + "'");
		while(rs_etapas.next()){
			String idEtapa = rs_etapas.getString(1);
			String nombre = rs_etapas.getString(2);
			String idEstacion = rs_etapas.getString(4);
			String idMateriaPrima = rs_etapas.getString(5);
			String idComponente = rs_etapas.getString(6);
			int duracion = rs_etapas.getInt(7);
			int numeroSecuencia = rs_etapas.getInt(8);
			String idAnterior = rs_etapas.getString(9);
			Etapa etapa = new Etapa(idEtapa, nombre, idProducto, idEstacion, idMateriaPrima, idComponente, duracion, numeroSecuencia, idAnterior);
			etapas.add(etapa);
			System.out.println(etapa.toString());
		}
		rs_etapas.close();
		return etapas;
	}
	
	
	/**
	 * @param idProducto
	 * @param etapa
	 * @param cantidad
	 * @param ultimaEtapa
	 * @param login
	 * @param fechaPedido
	 * @param idPedido
	 * @return
	 * @throws Exception
	 */
	public Date verificarExistencias (String idProducto, Etapa etapa, int cantidad, int ultimaEtapa, String idPedido, ArrayList<String> idInventarios) throws Exception{
		
		String verificarEstacionesText = "SELECT a.id AS idRegistroEstacion FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " a INNER JOIN " + Estacion.NOMBRE + " b ON a." + Estacion.COLUMNAS_REGISTRO_ESTACIONES[1] + "=b." + Estacion.COLUMNAS[0] + " WHERE b.tipo = '" + etapa.getIdEstacion() + "' AND NOT EXISTS (SELECT c.id FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " c WHERE idRegistroEstacion = a.id)";
		System.out.println(verificarEstacionesText);
		ResultSet rs_verificarEstaciones = crud.darConexion().createStatement().executeQuery(verificarEstacionesText);	
		
		String verificarMateriasPrimasText = "SELECT a.id FROM " + MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS + " a WHERE a.idMateriaPrima = '" + etapa.getIdMateriaPrima() + "' AND NOT EXISTS (SELECT b.id FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " b WHERE idRegistroMateriaPrima = a.id)";
		System.out.println(verificarMateriasPrimasText);
		ResultSet rs_verificarMateriasPrimas =  crud.darConexion().createStatement().executeQuery(verificarMateriasPrimasText);

		String verificarComponentesText = "SELECT a.id FROM " + Componente.NOMBRE_REGISTRO_COMPONENTES + " a WHERE a.idComponente = '" + etapa.getIdComponente() + "' AND NOT EXISTS (SELECT b.id FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " b WHERE idRegistroComponente = a.id)";
		System.out.println(verificarComponentesText);
		ResultSet rs_verificarComponentes =  crud.darConexion().createStatement().executeQuery(verificarComponentesText);
		
		Date fechaEntrega = null;

		for(int i = 0; i < cantidad; i++){
			rs_verificarEstaciones.next();
			rs_verificarMateriasPrimas.next();
			rs_verificarComponentes.next();
			
			String idRegProd = Integer.toString(darContadorId());
			String[] datosRegProd = {idRegProd,etapa.getId(),idInventarios.get(i),rs_verificarEstaciones.getString(1),rs_verificarMateriasPrimas.getString(1),rs_verificarComponentes.getString(1)};
			crud.insertarTupla(Producto.NOMBRE_REGISTRO_PRODUCTOS, Producto.COLUMNAS_REGISTRO_PRODUCTOS, Producto.TIPO_REGISTRO_PRODUCTOS, datosRegProd);
//			String estacionActivar = rs_verificarEstaciones.getString(2);
//			String sql_activar = "UPDATE " + Estacion.NOMBRE + " SET activada = 1 WHERE id = '" + estacionActivar + "'";
//			System.out.println(sql_activar);
//			crud.darConexion().createStatement().executeQuery(sql_activar);
			
			if(etapa.getNumeroSecuencia() == ultimaEtapa){
				if(i==cantidad-1){
					String hallarFechaEntregaText = "SELECT a.dia, a.mes FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " a WHERE a.id = '" + rs_verificarEstaciones.getString(1) + "'";
					ResultSet rs_hallarFechaEntrega =  crud.darConexion().createStatement().executeQuery(hallarFechaEntregaText);
					rs_hallarFechaEntrega.next();				
					int diaEntrega = Integer.parseInt(rs_hallarFechaEntrega.getString(1));
					int mesEntrega = Integer.parseInt(rs_hallarFechaEntrega.getString(2));
					Calendar calendario = Calendar.getInstance();
					calendario.setTime(new Date(2015, mesEntrega, diaEntrega));
					calendario.add(Calendar.DATE, 1);
					fechaEntrega = calendario.getTime();
					String actualizarFechaEntrega = "UPDATE pedidos SET diaEntrega = " + Integer.toString(fechaEntrega.getDay()) + ", mesEntrega = " + Integer.toString(fechaEntrega.getMonth()) + " WHERE id = '" + idPedido + "'";
					crud.darConexion().createStatement().executeQuery(actualizarFechaEntrega);
				}
			}
		}
		rs_verificarComponentes.close();
		rs_verificarEstaciones.close();
		rs_verificarMateriasPrimas.close();
		return fechaEntrega;
	}
	
	
	
	//--------------------------------------------------
	// METODOS DAR Y BUSCAR
	//--------------------------------------------------

	/**
	 * @param login
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String buscarUsuario (String login, String password) throws Exception{
		ArrayList<String> usuario = crud.darSubTabla(Usuario.NOMBRE, "tipo", "login = '" + login + "' AND password = '" + password + "'");
		usuarioActual = login;
		System.out.println("El usuario actual es:" + usuarioActual);
		if ( usuario.get(0) != null )
			return usuario.get(0);
		return "";
	}
	
	/**
	 * @param login
	 * @return
	 * @throws Exception
	 */
	public String buscarUsuario (String login) throws Exception{
		ArrayList<String> usuario = crud.darSubTabla(Usuario.NOMBRE, "tipo", "login = '" + login + "'");
		usuarioActual = login;
		System.out.println("El usuario actual es:" + usuarioActual);
		if ( usuario.get(0) != null )
			return usuario.get(0);
		return "";
	}
	
	/**
	 * @param id
	 * @param idDeseado
	 * @param rango
	 * @param mayorA
	 * @param menorA
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> buscarMateriaPrima (boolean id, String idDeseado, boolean rango, int mayorA, int menorA) throws Exception{
		if(id && rango){
			return crud.darSubTabla (MateriaPrima.NOMBRE, "cantidadInicial", "id = " + idDeseado + " AND cantidadInicial BETWEEN " + mayorA + " AND " + menorA);				
		}
		else if(id){
			return crud.darSubTabla (MateriaPrima.NOMBRE, "cantidadInicial", "id = " + idDeseado);				
		}
		else if(rango){
			return crud.darSubTabla (MateriaPrima.NOMBRE, "cantidadInicial", "cantidadInicial BETWEEN " + mayorA + " AND " + menorA);				
		}
		return null;
	}

	/**
	 * @param nombre
	 * @param nombreDeseado
	 * @param rango
	 * @param mayorA
	 * @param menorA
	 * @param etapa
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> buscarProducto (boolean nombre, String nombreDeseado, boolean rango, int mayorA, int menorA, boolean etapa) throws Exception{
		if(nombre && rango && etapa){
			return crud.darSubTabla (Producto.NOMBRE, "cantidad", "nombre = " + nombre + " AND cantidad BETWEEN " + mayorA + " AND " + menorA);				
		}
		else if(nombre && rango){
			return crud.darSubTabla (Producto.NOMBRE, "cantidad", "nombre = " + nombre + " AND cantidad BETWEEN " + mayorA + " AND " + menorA);				
		}
		else if(nombre){
			return crud.darSubTabla (Producto.NOMBRE, "cantidad", "nombre = " + nombre);				
		}
		else if(rango){
			return crud.darSubTabla (Producto.NOMBRE, "cantidad", "cantidad BETWEEN " + mayorA + " AND " + menorA);				
		}
		return null;
	}

	/**
	 * @param id
	 * @param idDeseado
	 * @param rango
	 * @param mayorA
	 * @param menorA
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> buscarComponente (boolean id, String idDeseado, boolean rango, int mayorA, int menorA) throws Exception{
		if(id && rango){
			return crud.darSubTabla (Componente.NOMBRE, "cantidadInicial", "id = " + idDeseado + " AND cantidadInicial BETWEEN " + mayorA + " AND " + menorA);				
		}
		else if(id){
			return crud.darSubTabla (Componente.NOMBRE, "cantidadInicial", "id = " + idDeseado);				
		}
		else if(rango){
			return crud.darSubTabla (Componente.NOMBRE, "cantidadInicial", "cantidadInicial BETWEEN " + mayorA + " AND " + menorA);				
		}
		return null;
	}

	/**
	 * @param nombre
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Producto> buscarProducto (String nombre) throws Exception{
		ArrayList<Producto> rta = new ArrayList<Producto>();
		ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT DISTINCT * FROM " + Producto.NOMBRE + " WHERE UPPER(nombre) LIKE UPPER('%" + nombre + "%')");
		while(rs.next())
		{
			String id = rs.getString(1);
			String pNombre = rs.getString(2);
			int precio = rs.getInt(3);
			Producto producto = new Producto(id, pNombre, precio);
			rta.add(producto);
		}
		return rta;
	}

	public ArrayList<MateriaPrima> darMateriasPrimas (String condicion) throws Exception{
		ArrayList<MateriaPrima> materiales = new ArrayList<MateriaPrima>();
		TreeMap<String, MateriaPrima> rta = new TreeMap<String, MateriaPrima>();
		String sql = "SELECT E.dia, E.mes, consultaEsta.* FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " E INNER JOIN (SELECT I.idPedido, consultaPedidos.* FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " I INNER JOIN (SELECT RP.IDINVENTARIO, RP.IDREGISTROESTACION, registros.id AS idMateriaPrima, registros.UNIDADMEDIDA, registros.CANTIDADINICIAL, registros.idRegistroMP FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " RP INNER JOIN (SELECT MP.*, RMP.id AS idRegistroMP FROM " + MateriaPrima.NOMBRE + " MP INNER JOIN " + MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS + " RMP ON MP.id = RMP.IDMATERIAPRIMA WHERE UPPER(MP.id) LIKE UPPER('%" + condicion + "%')) registros ON RP.IDREGISTROMATERIAPRIMA = registros.idRegistroMP) consultaPedidos ON I.id = consultaPedidos.IDINVENTARIO) consultaEsta ON E.id = consultaEsta.idRegistroEstacion";
		System.out.println(sql);
		Statement s = crud.darConexion().createStatement();
		ResultSet rs = s.executeQuery(sql);
		while(rs.next())
		{
			int dia = Integer.parseInt(rs.getString(1));
			int mes = Integer.parseInt(rs.getString(2));
			String idPedido = rs.getString(3);
			String id = rs.getString(6);
			String unidadMedida = rs.getString(7);
			int cantidad = Integer.parseInt(rs.getString(8));
			MateriaPrima material = new MateriaPrima(id, unidadMedida, cantidad, dia, mes);
			if(rta.containsKey(id)){
				MateriaPrima actual = rta.get(id);
				MateriaPrima nueva = new MateriaPrima(actual.getId(), actual.getUnidadMedida(), actual.getCantidadInicial(), actual.getDia(), actual.getMes());
				nueva.setPedidos(actual.getPedidos());
				rta.remove(id);
				nueva.addPedido(idPedido);
				rta.put(nueva.getId(), nueva);
			}
			else{
				MateriaPrima nueva = new MateriaPrima(id, unidadMedida, cantidad, dia, mes);
				nueva.addPedido(idPedido);
				rta.put(id, nueva);
			}
		}
		Collection<MateriaPrima> e = rta.values();
		Iterator<MateriaPrima> i = e.iterator();
		while(i.hasNext())
			materiales.add(i.next());
		rs.close();
		s.close();
		return materiales;
	}
	
	/**
	 * @param pedido
	 * @param pedido1
	 * @param entrega
	 * @param entrega1
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Pedido> darPedidos (String condicionPedido, String condicionNombreCliente, String condicionLoginCliente, String condicionProducto, String condicionMaterial, String condicionCantidad ) throws Exception{
		ArrayList<Pedido> rta = new ArrayList<Pedido>();
		TreeMap<String, Pedido> arbol = new TreeMap<String, Pedido>();
		String sql = "SELECT etapPed.login, etapPed.nombreCliente, etapPed.idProducto, etapPed.idPedido, etapPed.nombreProducto, etapPed.cantidad, etapPed.diaPedido, etapPed.mesPedido, etapPed.diaEntrega, etapPed.mesEntrega, etapas.idMateriaPrima, etapas.idComponente FROM etapas INNER JOIN (SELECT user1.login AS login, user1.nombre AS nombreCliente, proPed.idProducto, proPed.id AS idPedido, proPed.nombre AS nombreProducto, proPed.cantidad AS cantidad, proPed.diaPedido AS diaPedido, proPed.mesPedido AS mesPedido, proPed.diaEntrega AS diaEntrega, proPed.mesEntrega AS mesEntrega FROM usuarios user1 INNER JOIN (SELECT /*+ index(\"ISIS2304461510\".\"PEDIDOS\" P_idProd) */ ped.id, prod.id AS idProducto, prod.nombre, ped.idUsuario, ped.cantidad, ped.diaPedido, ped.mesPedido, ped.diaEntrega, ped.mesEntrega FROM pedidos ped INNER JOIN productos prod ON ped.idProducto = prod.id) proPed ON user1.login = proPed.idUsuario WHERE user1.tipo = 'natural' OR user1.tipo = 'juridica') etapPed ON etapas.idProducto = etapPed.idProducto  WHERE " + condicionNombreCliente + " AND "+ condicionLoginCliente + " AND " + condicionPedido + " AND " + condicionProducto + " AND " + condicionMaterial + " AND " + condicionCantidad + "";
		System.out.println(sql);
		ResultSet rs = crud.darConexion().createStatement().executeQuery(sql);
		while(rs.next())
		{
			String login = rs.getString(1);		
			String nombre = rs.getString(2);
			String idProducto = rs.getString(3);
			String idPedido = rs.getString(4);
			String nombreProducto = rs.getString(5);
			int cantidad = Integer.parseInt(rs.getString(6));
			int diaPedido = Integer.parseInt(rs.getString(7));
			int mesPedido = Integer.parseInt(rs.getString(8));
			int diaEntrega = Integer.parseInt(rs.getString(9));
			int mesEntrega = Integer.parseInt(rs.getString(10));
			
			Date fechaPedido = new Date(2015, mesPedido, diaPedido);
			Date fechaEntrega = new Date(2015, mesEntrega, diaEntrega);
			Pedido pedido = new Pedido(idPedido, nombreProducto, login, nombre, cantidad, fechaPedido, fechaEntrega);
			arbol.put(pedido.getId(),pedido);
		}
		Collection<Pedido> e = arbol.values();
		Iterator<Pedido> i = e.iterator();
		while(i.hasNext())
			rta.add(i.next());
		return rta;
	}
	
	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String darNombreProducto (String id) throws Exception{
		System.out.println("El id del producto es: " + id);
		return (crud.darSubTabla(Producto.NOMBRE, "nombre", "id = '" + id + "'")).get(0);
	}
	
	/**
	 * @param cantidad
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Producto> darCantidadProductos (int cantidad) throws Exception{
		ArrayList<Producto> rta = new ArrayList<Producto>();
		ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Producto.NOMBRE + "");
		while(rs.next())
		{
			String id = rs.getString(1);
			String nombre = rs.getString(2);
			int precio = rs.getInt(3);
			Producto producto = new Producto(id, nombre, precio);
			rta.add(producto);
		}
		return rta;
	}
	
	/**
	 * 
	 * @return
	 */
	public String darUsuarioActual(){
		return usuarioActual;
	}
	
	/**
	 * 
	 * @param condicion
	 * @return
	 */
	public ArrayList<Etapa> darEtapas (String condicion) throws Exception{
		int contador = 0;
		conexion.cerrarConexion();
		conexion = new ConexionDAO();
		conexion.iniciarConexion();
		crud = new CRUD(conexion);
		ArrayList<Etapa> etapas = new ArrayList<Etapa>();
		TreeMap<String, Etapa> rta = new TreeMap<String, Etapa>();
		String sql = "SELECT ConsultaC.idPedido, ConsultaC.dia, ConsultaC.mes, ConsultaC.idEtapa, ConsultaC.idMateriaPrima, RC.idComponente FROM " + Componente.NOMBRE_REGISTRO_COMPONENTES + " RC INNER JOIN (SELECT RMP.idMateriaPrima, ConsultaMP.idPedido, ConsultaMP.dia, ConsultaMP.mes, ConsultaMP.idEtapa, ConsultaMP.idRegistroComponente FROM " + MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS + " RMP INNER JOIN (SELECT I.idPedido, ConsultaInventario.dia, ConsultaInventario.mes, ConsultaInventario.idEtapa, ConsultaInventario.idRegistroMateriaPrima, ConsultaInventario.idRegistroComponente FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " I INNER JOIN (SELECT /*+ index(\"ISIS2304461510\".\"REGISTROSPRODUCTOS\" RP_idInventario) */ A.*, B.idEtapa, B.idInventario, B.idRegistroMateriaPrima, B.idRegistroComponente FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " A INNER JOIN " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " B ON A.id = B.idRegistroEstacion) ConsultaInventario ON I.id = ConsultaInventario.idInventario) ConsultaMP ON RMP.id = ConsultaMP.idRegistroMateriaPrima) ConsultaC ON RC.id = ConsultaC.idRegistroComponente WHERE " + condicion + "";
		System.out.println(sql);
		Statement s = crud.darConexion().createStatement();
		ResultSet rs = s.executeQuery(sql);
		while (rs.next()){
			String idPedido = rs.getString(1);
			int dia = Integer.parseInt(rs.getString(2));
			int mes = Integer.parseInt(rs.getString(3));
			String idEtapa = rs.getString(4);
			String idMateriaPrima = rs.getString(5);
			String idComponente = rs.getString(6);
			String sqlEtapa = "SELECT E.nombre, E.idProducto, E.duracion, E.numeroSecuencia FROM " + Etapa.NOMBRE + " E WHERE E.id = '" + idEtapa + "'";
			System.out.println(sqlEtapa);
			System.out.println(contador++);
			ResultSet rs_etapas = crud.darConexion().createStatement().executeQuery(sqlEtapa);
			while (rs_etapas.next()){
				String nombre = rs_etapas.getString(1);
				String idProducto = rs_etapas.getString(2);
				int duracion = Integer.parseInt(rs_etapas.getString(3));
				int numeroSecuencia = Integer.parseInt(rs_etapas.getString(4));
				if(rta.containsKey(idEtapa)){
					Etapa actual = rta.get(idEtapa);
					Etapa nueva = new Etapa(actual.getId(), actual.getNombre(), actual.getIdProducto(), actual.getDia(), actual.getMes(), actual.getIdMateriaPrima(), actual.getIdComponente(), actual.getDuracion(), actual.getNumeroSecuencia());
					nueva.setIdPedido(actual.getIdPedido());
					rta.remove(idEtapa);
					nueva.addIdPedido(idPedido);
					rta.put(nueva.getId(), nueva);
				}
				else{
					Etapa etapa = new Etapa(idEtapa, nombre, idProducto, dia, mes, idMateriaPrima, idComponente, duracion, numeroSecuencia);
					etapa.addIdPedido(idPedido);
					rta.put(idEtapa, etapa);
				}
			}
			rs_etapas.close();
		}
		rs.close();
		s.close();
		Collection<Etapa> e = rta.values();
		Iterator<Etapa> i = e.iterator();
		while(i.hasNext())
			etapas.add(i.next());
		conexion.cerrarConexion();
		conexion = new ConexionDAO();
		conexion.iniciarConexion();
		crud = new CRUD(conexion);
		return etapas;
	}
	
	/**
	 * 
	 * @param condicion
	 * @return
	 */
	public ArrayList<Etapa> darEtapasNo (String condicion, String condicionFecha) throws Exception{
		ArrayList<Etapa> etapas = new ArrayList<Etapa>();
		TreeMap<String, Etapa> rta = new TreeMap<String, Etapa>();
		String sql = "SELECT ConsultaC.idPedido, ConsultaC.dia, ConsultaC.mes, ConsultaC.idEtapa, ConsultaC.idMateriaPrima, RC.idComponente FROM " + Componente.NOMBRE_REGISTRO_COMPONENTES + " RC INNER JOIN (SELECT RMP.idMateriaPrima, ConsultaMP.idPedido, ConsultaMP.dia, ConsultaMP.mes, ConsultaMP.idEtapa, ConsultaMP.idRegistroComponente FROM " + MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS + " RMP INNER JOIN (SELECT I.idPedido, ConsultaInventario.dia, ConsultaInventario.mes, ConsultaInventario.idEtapa, ConsultaInventario.idRegistroMateriaPrima, ConsultaInventario.idRegistroComponente FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " I INNER JOIN (SELECT A.*, B.idEtapa, B.idInventario, B.idRegistroMateriaPrima, B.idRegistroComponente FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " A INNER JOIN " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " B ON A.id = B.idRegistroEstacion) ConsultaInventario ON I.id = ConsultaInventario.idInventario) ConsultaMP ON RMP.id = ConsultaMP.idRegistroMateriaPrima) ConsultaC ON RC.id = ConsultaC.idRegistroComponente WHERE " + condicionFecha + " AND NOT (" + condicion + ")";
		System.out.println(sql);
		Statement s = crud.darConexion().createStatement();
		ResultSet rs = s.executeQuery(sql);
		while (rs.next()){
			String idPedido = rs.getString(1);
			int dia = Integer.parseInt(rs.getString(2));
			int mes = Integer.parseInt(rs.getString(3));
			String idEtapa = rs.getString(4);
			String idMateriaPrima = rs.getString(5);
			String idComponente = rs.getString(6);
			String sqlEtapa = "SELECT E.nombre, E.idProducto, E.duracion, E.numeroSecuencia FROM " + Etapa.NOMBRE + " E WHERE E.id = '" + idEtapa + "'";
			Statement s1 = crud.darConexion().createStatement();
			ResultSet rs_etapas = s1.executeQuery(sqlEtapa);
			while (rs_etapas.next()){
				String nombre = rs_etapas.getString(1);
				String idProducto = rs_etapas.getString(2);
				int duracion = Integer.parseInt(rs_etapas.getString(3));
				int numeroSecuencia = Integer.parseInt(rs_etapas.getString(4));
				if(rta.containsKey(idEtapa)){
					Etapa actual = rta.get(idEtapa);
					Etapa nueva = new Etapa(actual.getId(), actual.getNombre(), actual.getIdProducto(), actual.getDia(), actual.getMes(), actual.getIdMateriaPrima(), actual.getIdComponente(), actual.getDuracion(), actual.getNumeroSecuencia());
					nueva.setIdPedido(actual.getIdPedido());
					rta.remove(idEtapa);
					nueva.addIdPedido(idPedido);
					rta.put(nueva.getId(), nueva);
				}
				else{
					Etapa etapa = new Etapa(idEtapa, nombre, idProducto, dia, mes, idMateriaPrima, idComponente, duracion, numeroSecuencia);
					etapa.addIdPedido(idPedido);
					rta.put(idEtapa, etapa);
				}
			}
			s1.close();
			rs_etapas.close();
		}
		s.close();
		rs.close();
		Collection<Etapa> e = rta.values();
		Iterator<Etapa> i = e.iterator();
		while(i.hasNext())
			etapas.add(i.next());
		return etapas;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Estacion> darEstaciones() throws Exception{
		ArrayList<Estacion> rta = new ArrayList<Estacion>();
		ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Estacion.NOMBRE + "");
		while(rs.next())
		{
			String id = rs.getString(1);
			String nombre = rs.getString(2);
			String tipo = rs.getString(3);
			int enteroAct = Integer.parseInt(rs.getString(4));
			boolean activada = enteroAct == 1;
			Estacion estacion = new Estacion(id, nombre, tipo, activada);
			rta.add(estacion);
		}
		return rta;
	}

	/**
	 * @param login
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Pedido> darPedidosCliente(String login) throws Exception{
		ArrayList<Pedido> rta = new ArrayList<Pedido>();
		Statement s = crud.darConexion().createStatement();
		String sql = "SELECT * FROM " + Pedido.NOMBRE + " WHERE idUsuario = '" + login + "'";
		System.out.println(sql);
		ResultSet rs = s.executeQuery(sql);
		while(rs.next()){
			String id = rs.getString(1);
			String idProducto = rs.getString(2);
			String idUsuario = rs.getString(3);
			int diaPedido = Integer.parseInt(rs.getString(4));
			int mesPedido = Integer.parseInt(rs.getString(5));
			int diaEntrega = Integer.parseInt(rs.getString(6));
			int mesEntrega = Integer.parseInt(rs.getString(7));
			int cantidad = Integer.parseInt(rs.getString(8));
			Date fechaPedido = new Date(2015, mesPedido, diaPedido);
			Date fechaEntrega = new Date(2015, mesEntrega, diaEntrega);
			Pedido pedido = new Pedido(id, idProducto, login, cantidad, fechaPedido, fechaEntrega);
			System.out.println(pedido.toString());
			rta.add(pedido);
		}
		return rta;
	}

	/**
	 * @param producto
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> darIdPedido (String producto) throws Exception{
		return crud.darSubTabla(Pedido.NOMBRE, "id", "idCliente = " + usuarioActual + "idProducto = " + producto);
	}
	
	public ArrayList<Producto> darProducto(String id) throws Exception{
		ArrayList<Producto> rta = new ArrayList<Producto>();
		String sql = "SELECT * FROM " + Producto.NOMBRE + " WHERE id = '" + id + "'";
		System.out.println(sql);
		ResultSet rs = crud.darConexion().createStatement().executeQuery(sql);
		while(rs.next())
		{
			String nombre = rs.getString(2);
			int precio = rs.getInt(3);
			Producto producto = new Producto(id, nombre, precio);
			rta.add(producto);
			System.out.println(producto.getNombre());
		}
		return rta;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Componente> darComponentes( String condicion ) throws Exception {
		ArrayList<Componente> compenentes = new ArrayList<Componente>();
		TreeMap<String, Componente> rta = new TreeMap<>();
		String sql = "SELECT E.dia, E.mes, consultaEsta.* FROM registrosEstaciones E INNER JOIN (SELECT I.idPedido, consultaPedidos.* FROM inventarioProductos I INNER JOIN (SELECT RP.IDINVENTARIO, RP.IDREGISTROESTACION, registros.* FROM registrosProductos RP INNER JOIN (SELECT MP.*, RMP.id AS idRegistroMP FROM componentes MP INNER JOIN registroscomponentes RMP ON MP.id = RMP.IDcomponente WHERE UPPER(MP.id) LIKE UPPER('%" + condicion + "%'))  registros ON RP.IDREGISTROcomponente = registros.idRegistroMP) consultaPedidos ON I.id = consultaPedidos.IDINVENTARIO) consultaEsta ON E.id = consultaEsta.idRegistroEstacion";
		System.out.println(sql);
		Statement s = crud.darConexion().createStatement();
		ResultSet rs = s.executeQuery(sql);
		while(rs.next())
		{
			int dia = Integer.parseInt(rs.getString(1));
			int mes = Integer.parseInt(rs.getString(2));
			String idPedido = rs.getString(3);
			String id = rs.getString(6);
			String unidadMedida = rs.getString(7);
			int cantidad = Integer.parseInt(rs.getString(8));
			Componente componente = new Componente(id, unidadMedida, cantidad, dia, mes);
			if(rta.containsKey(id)){
				Componente actual = rta.get(id);
				Componente nueva = new Componente(actual.getId(), actual.getUnidadMedida(), actual.getCantidadInicial(), actual.getDia(), actual.getMes());
				nueva.setPedidos(actual.getPedidos());
				rta.remove(id);
				nueva.addPedido(idPedido);
				rta.put(nueva.getId(), nueva);
			}
			else{
				Componente nueva = new Componente(id, unidadMedida, cantidad, dia, mes);
				nueva.addPedido(idPedido);
				rta.put(id, nueva);
			}
		}
		Collection<Componente> e = rta.values();
		Iterator<Componente> i = e.iterator();
		while(i.hasNext())
			compenentes.add(i.next());
		rs.close();
		s.close();
		return compenentes;
	}

	public ArrayList<MateriaPrima> darMateriasPrimasProveedor(String idProveedor) throws Exception{
		ArrayList<String> idsMateriasPrimas = crud.darSubTabla(Proveedor.NOMBRE_RELACION_MATERIA_PRIMA, "id_materiaPrima", "id_proveedor = '" + idProveedor + "'");
		ArrayList<MateriaPrima> rta = new ArrayList<MateriaPrima>();
		for (int i = 0; i < idsMateriasPrimas.size(); i++) {
			ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + MateriaPrima.NOMBRE + " WHERE id = '" + idsMateriasPrimas.get(i) + "'");
			while(rs.next())
			{
				String id = rs.getString(1);
				String unidadMedida = rs.getString(2);
				int cantidad = rs.getInt(3);
				MateriaPrima estacion = new MateriaPrima(id, unidadMedida, cantidad);
				rta.add(estacion);
			}
		}
		return rta;
	}
	
	public ArrayList<Producto> darProductosProveedor(String idProveedor) throws Exception{
		
		Set<Producto> setProductosProveedorMateriasPrimas = new HashSet<Producto>();
		
		ResultSet rsIdMateriasPrimas = crud.darConexion().createStatement().executeQuery("SELECT id_materiaPrima FROM " + Proveedor.NOMBRE_RELACION_MATERIA_PRIMA + " WHERE id_Proveedor = '" + idProveedor + "'");
		while(rsIdMateriasPrimas.next()){
			String idMateriaPrima = rsIdMateriasPrimas.getString(1);
			ResultSet rsIdRegistroMP = crud.darConexion().createStatement().executeQuery("SELECT id FROM " + MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS + " WHERE idMateriaPrima = '" + idMateriaPrima + "'");
			while(rsIdRegistroMP.next()){
				String idRegMateriaPrima = rsIdRegistroMP.getString(1);
				ResultSet rsIdInventarios = crud.darConexion().createStatement().executeQuery("SELECT idInventario FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " WHERE idRegistroMateriaPrima = '" + idRegMateriaPrima + "'");
				while(rsIdInventarios.next()){
					String idInventario = rsIdInventarios.getString(1);
					ResultSet rsIdProductos = crud.darConexion().createStatement().executeQuery("SELECT idProducto FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " WHERE id = '" + idInventario + "'");
					while(rsIdProductos.next()){
						String idProducto = rsIdProductos.getString(1);
						ResultSet rsProductos = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Producto.NOMBRE + " WHERE id = '" + idProducto + "'");
						while(rsProductos.next())
						{
							String id = rsProductos.getString(1);
							String nombre = rsProductos.getString(2);
							int precio = rsProductos.getInt(3);
							Producto producto = new Producto(id, nombre, precio);
							setProductosProveedorMateriasPrimas.add(producto);
						}
					}
				}
			}	
		}
		
		Set<Producto> setProductosProveedorComponentes = new HashSet<Producto>();
		
		ResultSet rsIdComponentes = crud.darConexion().createStatement().executeQuery("SELECT id_Componente FROM " + Proveedor.NOMBRE_RELACION_COMPONENTE + " WHERE id_Proveedor = '" + idProveedor + "'");
		while(rsIdComponentes.next()){
			String idComponente = rsIdComponentes.getString(1);
			ResultSet rsIDRegistroC = crud.darConexion().createStatement().executeQuery("SELECT id FROM " + Componente.NOMBRE_REGISTRO_COMPONENTES + " WHERE idComponente = '" + idComponente + "'");
			while (rsIDRegistroC.next()){
				String idRegComponente = rsIDRegistroC.getString(1);
				ResultSet rsIdInventarios = crud.darConexion().createStatement().executeQuery("SELECT idInventario FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " WHERE idRegistroComponente = '" + idRegComponente + "'");
				while(rsIdInventarios.next()){
					String idInventario = rsIdInventarios.getString(1);
					ResultSet rsIdProductos = crud.darConexion().createStatement().executeQuery("SELECT idProducto FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " WHERE id = '" + idInventario + "'");
					while(rsIdProductos.next()){
						String idProducto = rsIdProductos.getString(1);
						ResultSet rsProductos = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Producto.NOMBRE + " WHERE id = '" + idProducto + "'");
						while(rsProductos.next())
						{
							String id = rsProductos.getString(1);
							String nombre = rsProductos.getString(2);
							int precio = rsProductos.getInt(3);
							Producto producto = new Producto(id, nombre, precio);
							setProductosProveedorComponentes.add(producto);
						}
					}
				}

			}
		}
		ArrayList<Producto> productosProveedor = new ArrayList<Producto>();
		productosProveedor.addAll(setProductosProveedorMateriasPrimas);
		productosProveedor.addAll(setProductosProveedorComponentes);
		return productosProveedor;
	}
	
	public ArrayList<Pedido> darPedidosProveedor(String idProveedor) throws Exception{

		ArrayList<Producto> productosProveedor = darProductosProveedor(idProveedor);
		
		Set<Pedido> setPedidosProveedor = new HashSet<Pedido>();
		for(int i = 0; i < productosProveedor.size(); i++){
			Producto productoActual = productosProveedor.get(i);
			String idProductoActual = productoActual.getId();
			ResultSet rsPedidos = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Pedido.NOMBRE + " WHERE idProducto = '" + idProductoActual + "'");
			while(rsPedidos.next()){
				String id = rsPedidos.getString(1);
				String idProducto = rsPedidos.getString(2);
				String idUsuario = rsPedidos.getString(3);
				int diaPedido = rsPedidos.getInt(4);
				int mesPedido = rsPedidos.getInt(5);
				int diaEntrega = rsPedidos.getInt(6);
				int mesEntrega = rsPedidos.getInt(7);
				int cantidad = rsPedidos.getInt(8);
				Date fechaPedido = new Date(2015, mesPedido, diaPedido);
				Date fechaEntrega = new Date(2015, mesEntrega, diaEntrega);
				Pedido pedido = new Pedido(id, idProducto, idUsuario, cantidad, fechaPedido, fechaEntrega);	
				setPedidosProveedor.add(pedido);
			}
		}
		ArrayList<Pedido> pedidosProveedor = new ArrayList<Pedido>();
		pedidosProveedor.addAll(setPedidosProveedor);
		pedidosProveedor.addAll(setPedidosProveedor);
		return pedidosProveedor;	
	}
	

	/**
	 * @param idProveedor
	 * @return
	 * @throws Exception
	 */
	private ArrayList<Componente> darComponentesProveedor(String idProveedor) throws Exception {
		ArrayList<String> idsComponentes = crud.darSubTabla(Proveedor.NOMBRE_RELACION_COMPONENTE, "id_componente", "id_proveedor = '" + idProveedor + "'");
		ArrayList<Componente> rta = new ArrayList<Componente>();
		for (int i = 0; i < idsComponentes.size(); i++) {
			ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Componente.NOMBRE + " WHERE id = '" + idsComponentes.get(i) + "'");
			while(rs.next())
			{
				String id = rs.getString(1);
				String unidadMedida = rs.getString(2);
				int cantidad = rs.getInt(3);
				Componente estacion = new Componente(id, unidadMedida, cantidad);
				rta.add(estacion);
			}
		}
		return rta;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Usuario> darClientes(String condicionNombre, String condicionPedido, String condicionProducto) throws Exception{
		ArrayList<Usuario> rta = new ArrayList<Usuario>();
		String sql = "SELECT * FROM (SELECT user1.*, proPed.id AS idPedido, proPed.nombre AS nombreProducto, proPed.cantidad AS cantidad, proPed.diaPedido AS diaPedido, proPed.mesPedido AS mesPedido, proPed.diaEntrega AS diaEntrega, proPed.mesEntrega AS mesEntrega FROM usuarios user1 LEFT JOIN (SELECT ped.id, prod.nombre, ped.idUsuario, ped.cantidad, ped.diaPedido, ped.mesPedido, ped.diaEntrega, ped.mesEntrega FROM pedidos ped INNER JOIN productos prod ON ped.idProducto = prod.id) proPed ON user1.login = proPed.idUsuario WHERE user1.tipo = 'natural' OR user1.tipo = 'juridica')  WHERE " + condicionNombre + " AND " + condicionPedido + " AND " + condicionProducto;
		System.out.println(sql);
		ResultSet rs = crud.darConexion().createStatement().executeQuery(sql);
		while(rs.next())
		{
			String login = rs.getString(1);
			
			System.out.println(login);
			
			String tipo = rs.getString(3);
			String nombre = rs.getString(4);
			String direccion = rs.getString(5);
			int telefono = 0;
			if (rs.getString(6) != null)
				telefono = Integer.parseInt(rs.getString(6));
			String ciudad = rs.getString(7);
			String idRepLegal = rs.getString(8);
			
			Usuario user = new Usuario(login, tipo, "", nombre, direccion, telefono, ciudad, idRepLegal);
						
			String idPedido = rs.getString(9);
			String nombreProducto = rs.getString(10);
			int cantidad = 0;
			if (rs.getString(11) != null)
				cantidad = Integer.parseInt(rs.getString(11));
			int diaPedido = 0;
			if (rs.getString(12) != null)
				diaPedido = Integer.parseInt(rs.getString(12));
			int mesPedido = 0;
			if (rs.getString(13) != null)
				mesPedido = Integer.parseInt(rs.getString(13));
			int diaEntrega = 0;
			if (rs.getString(14) != null)
				diaEntrega = Integer.parseInt(rs.getString(14));
			int mesEntrega = 0;
			if (rs.getString(15) != null)
				mesEntrega = Integer.parseInt(rs.getString(15));
			
			Date fechaPedido = new Date(2015, mesPedido, diaPedido);
			Date fechaEntrega = new Date(2015, mesEntrega, diaEntrega);
			Pedido pedido = null;
			boolean encontro = false;
			if (idPedido != null)
			{
				pedido = new Pedido(idPedido, nombreProducto, login, cantidad, fechaPedido, fechaEntrega);
				
				user.addPedido(pedido);
				for (int i = 0; i < rta.size() && !encontro; i++) {
					if (rta.get(i).getLogin().equals(login))
					{
						user.addAllPedidos(rta.get(i).getPedidos());
						rta.remove(rta.get(i));
						rta.add(user);
						encontro = true;
					}	
				}
			}
		
			if(!encontro)
				rta.add(user);
		}
		return rta;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Proveedor> darProveedores(String condicionProveedores, String condicionMateriasPrimas, String condicionComponentes) throws Exception {
		ArrayList<Proveedor> rta = new ArrayList<Proveedor>();
		ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT * FROM " + Proveedor.NOMBRE + " WHERE " + condicionProveedores );
		while(rs.next())
		{
			String pId = rs.getString(1);
			String pDireccion = rs.getString(2);
			int pTelefono = Integer.parseInt(rs.getString(3));
			String pCiudad = rs.getString(4);
			String pIdRepLegal = rs.getString(5);
			
			Proveedor prov = new Proveedor(pId, pDireccion, pTelefono, pCiudad, pIdRepLegal);
			
			ArrayList<String> idMateriaPrima = new ArrayList<String>();
			ArrayList<String> idComponente = new ArrayList<String>();
			
			String sql_materias = "SELECT * FROM " + Proveedor.NOMBRE_RELACION_MATERIA_PRIMA + " WHERE id_proveedor = '" + prov.getId() + "' AND " + condicionMateriasPrimas;
			System.out.println(sql_materias);
			ResultSet rs_materias = crud.darConexion().createStatement().executeQuery(sql_materias);
			while(rs_materias.next())
			{
				idMateriaPrima.add(rs_materias.getString(2));
			}
			String sql_componentes = "SELECT * FROM " + Proveedor.NOMBRE_RELACION_COMPONENTE + " WHERE id_proveedor = '" + prov.getId() + "' AND " + condicionComponentes;
			System.out.println(sql_componentes);
			ResultSet rs_componentes = crud.darConexion().createStatement().executeQuery(sql_componentes);
			while(rs_componentes.next())
			{
				idComponente.add(rs_componentes.getString(2));
			}
			for (String materiaPrima : idMateriaPrima) {
				String sql_materiasPrimas = "SELECT * FROM " + MateriaPrima.NOMBRE + " WHERE id = '" + materiaPrima + "'";
				ResultSet rs_1 = crud.darConexion().createStatement().executeQuery(sql_materiasPrimas);
				while(rs_1.next())
				{
					String id = rs_1.getString(1);
					String unidadMedida = rs_1.getString(2);
					int cantidadInicial = rs_1.getInt(3);
					MateriaPrima m = new MateriaPrima(id, unidadMedida, cantidadInicial);
					prov.addMateriaPrima(m);
				}
			}
			for (String componente : idComponente) {
				String sql_componentesProv = "SELECT * FROM " + Componente.NOMBRE + " WHERE id = '" + componente + "'";
				ResultSet rs_1 = crud.darConexion().createStatement().executeQuery(sql_componentesProv);
				while(rs_1.next())
				{
					String id = rs_1.getString(1);
					String unidadMedida = rs_1.getString(2);
					int cantidadInicial = rs_1.getInt(3);
					Componente c = new Componente(id, unidadMedida, cantidadInicial);
					prov.addComponente(c);
				}
			}
			if (prov.getComponentes().size() != 0 || prov.getMateriasPrimas().size() != 0 || (condicionComponentes.equals(VERDADERO) && condicionMateriasPrimas.equals(VERDADERO) && condicionProveedores.equals(VERDADERO)))
			{
				System.out.println(prov.getId());
				rta.add(prov);
			}
		}
		return rta;
	}
	
	//--------------------------------------------------
	// METODOS ELIMINAR
	//--------------------------------------------------
	
	/**
	 * @param login
	 * @param idPedido
	 * @throws Exception
	 */
	public void eliminarPedidoCliente(String login, String idPedido) throws Exception{
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		try
		{
			ResultSet rs = crud.darConexion().createStatement().executeQuery("SELECT id FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " WHERE " + "idPedido = '" + idPedido + "'");
			while(rs.next()){
				crud.eliminarTupla(Producto.NOMBRE_REGISTRO_PRODUCTOS, "idInventario = '" + rs.getString(1) + "'");
			}
			crud.eliminarTupla(Producto.NOMBRE_INVENTARIO_PRODUCTOS, "idPedido = '" + idPedido + "'");
			crud.eliminarTuplaPorId(Pedido.NOMBRE, idPedido);
		}
		catch(Exception e){
			conexion.darConexion().rollback(save);
			throw new Exception();
		}
		conexion.darConexion().commit();
		conexion.setAutoCommitVerdadero();
	}
	
	public void desactivarEstacionProduccion(String idEstacion) throws Exception{
		conexion.setAutoCommitFalso();
		Savepoint save = conexion.darConexion().setSavepoint();
		try {
			Set<String> idPedidosAfectados = new HashSet<String>();
			String sql_regProd = "SELECT a.id, a.idInventario FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " a INNER JOIN " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " b ON a.idRegistroEstacion = b.id WHERE b.idEstacion = '" + idEstacion + "' ORDER BY  cast(a.id as int)";
			System.out.println(sql_regProd);
			ResultSet rsRegistrosProductos = crud.darConexion().createStatement().executeQuery(sql_regProd);
			String sql_regEsta = "SELECT a.id FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " a WHERE a.idEstacion != '" + idEstacion + "' AND ((SELECT tipo FROM " + Estacion.NOMBRE + " b WHERE b.id = a.idEstacion) = (SELECT tipo FROM " + Estacion.NOMBRE + " WHERE id = '" + idEstacion + "')) AND NOT EXISTS (SELECT c.id FROM " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " c WHERE idRegistroEstacion = a.id) ORDER BY cast(a.id as int)";
			System.out.println(sql_regEsta);
			ResultSet rsRegistrosEstaciones = crud.darConexion().createStatement().executeQuery(sql_regEsta);
			while(rsRegistrosProductos.next()){
				String sql_pedido = "SELECT a.idPedido FROM " + Producto.NOMBRE_INVENTARIO_PRODUCTOS + " a WHERE a.id = '" + rsRegistrosProductos.getString(2) + "'";
				System.out.println(sql_pedido);
				ResultSet rsPedidos = crud.darConexion().createStatement().executeQuery(sql_pedido);	
				rsPedidos.next();
				idPedidosAfectados.add(rsPedidos.getString(1));
				rsRegistrosEstaciones.next();
				//String estacionActivar = rsRegistrosEstaciones.getString(2);
				String sql = "UPDATE " + Producto.NOMBRE_REGISTRO_PRODUCTOS + " SET idRegistroEstacion = '" + rsRegistrosEstaciones.getString(1) + "' WHERE id = '" + rsRegistrosProductos.getString(1) + "'";
				System.out.println(sql);
				crud.darConexion().createStatement().executeUpdate(sql);
				//String sql_activar = "UPDATE " + Estacion.NOMBRE + " SET activada = 1 WHERE id = '" + estacionActivar + "'";
				//System.out.println(sql_activar);
				//crud.darConexion().createStatement().executeQuery(sql_activar);
			}
			crud.darConexion().createStatement().executeUpdate("DELETE FROM " + Estacion.NOMBRE_REGISTRO_ESTACIONES + " WHERE idEstacion = '" + idEstacion + "'");
			verificarFechasEntregaPedidos(idPedidosAfectados);
			String sql_desactivar = "UPDATE " + Estacion.NOMBRE + " SET activada = 0 WHERE id = '" + idEstacion + "'";
			System.out.println(sql_desactivar);
			crud.darConexion().createStatement().executeQuery(sql_desactivar);
		} 
		catch (Exception e) {
			conexion.darConexion().rollback(save);
			throw new Exception(e.getMessage());
		}
		conexion.darConexion().commit();
	}
	
	public void verificarFechasEntregaPedidos(Set<String> idPedidosAfectados) throws Exception{
		System.out.println(idPedidosAfectados.size());
		for(String idPedido : idPedidosAfectados){
			String sql = "SELECT a.id, a.diaEntrega, a.mesEntrega, d.dia, d.mes FROM ((Pedidos a INNER JOIN InventarioProductos b ON a.id = b.idPedido) INNER JOIN RegistrosProductos c ON b.id = c.idInventario) INNER JOIN RegistrosEstaciones d ON d.id = c.idRegistroEstacion WHERE a.id = '" + idPedido + "'";
			ResultSet mesesYDiasEstacionesAsignadas = crud.darConexion().createStatement().executeQuery(sql);
			ArrayList<Integer> dias = new ArrayList<Integer>();
			ArrayList<Integer> meses = new ArrayList<Integer>();
			while(mesesYDiasEstacionesAsignadas.next()){
				dias.add(Integer.parseInt(mesesYDiasEstacionesAsignadas.getString(4)));
				System.out.println("Dia estacion " + mesesYDiasEstacionesAsignadas.getString(4));
				meses.add(Integer.parseInt(mesesYDiasEstacionesAsignadas.getString(5)));
				System.out.println("Mes Estacion" + mesesYDiasEstacionesAsignadas.getString(5));
			}
			Integer mayorMes = meses.get(0);
			for(int m = 0; m < meses.size(); m++){
				if(meses.get(m)>mayorMes){
					mayorMes = meses.get(m);
				}
			}
			System.out.println("tamano meses: " + meses.size());
			System.out.println("tamano dias: " + dias.size());
			System.out.println(mayorMes);
			for(int i = 0; i < meses.size(); i++){
				System.out.println("El dia en " + i + " es " + dias.get(i));
				System.out.println("El mes en " + i + " es " + meses.get(i));
				if(meses.get(i)!=mayorMes){
					dias.set(i, 0);
					System.out.println(dias.get(i));
				}
			}
			Collections.sort(dias);
			Integer mayorDiaMayorMes = dias.get(dias.size()-1);
			System.out.println(mayorDiaMayorMes);
			String sqlUpdate = "UPDATE " + Pedido.NOMBRE + " SET diaEntrega = '" + Integer.toString(mayorDiaMayorMes) + "', mesEntrega = '" + Integer.toString(mayorMes) + "' WHERE id = '" + idPedido + "'";
			crud.darConexion().createStatement().executeQuery(sqlUpdate);
		}
	}
	
	//--------------------------------------------------
	// MAIN
	//--------------------------------------------------

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AplicacionWeb aplicacionWeb = getInstancia();
		ArrayList<String> usuarios = new ArrayList<String>();
		ArrayList<String> idProductos = new ArrayList<String>();
		ArrayList<String> productos = new ArrayList<String>();
		try {
//			BufferedReader lector = new BufferedReader(new FileReader("C:\\Users\\Meili\\Dropbox\\Sistemas Transaccionales\\Datos\\usuarios.csv"));
//			String linea = lector.readLine();
//			while(linea != null){
//				String usuario = linea.substring(0, linea.indexOf(","));
//				System.out.println(usuario);
//				if (!usuario.equals("MeiMei"))
//					usuarios.add(usuario);
//				linea = lector.readLine();
//			}
//			lector.close();
//			BufferedReader lector2 = new BufferedReader(new FileReader("C:\\Users\\Meili\\Dropbox\\Sistemas Transaccionales\\Datos\\productos.csv"));
//			linea = lector2.readLine();
//			while(linea != null){
//				String[] datos = linea.split(",");
//				idProductos.add(datos[0]);
//				productos.add(datos[1]);
//				System.out.println(datos[1]);
//				linea = lector2.readLine();
//			}
//			lector2.close();
//			for (int i = 0; i < 100000; i++) {
//				Random randUsuario = new Random();
//			    int usuario = randUsuario.nextInt(9 - 0);
//				Random randProducto = new Random();
//				int producto = randProducto.nextInt(9-0);
//				System.out.println("El usuario: " + usuarios.get(usuario) + " pidió el producto: " + productos.get(producto));
//				Date dia = new Date();
//				aplicacionWeb.registrarPedido(usuarios.get(usuario), idProductos.get(producto), 1, dia);
//			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
