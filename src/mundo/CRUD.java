package mundo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CRUD {

	private Connection conexion;
	
	public CRUD (ConexionDAO dao){
		conexion = dao.darConexion();
	}
	
	public Connection darConexion(){	
		return conexion;
	}
	
	/**
	 * @param tabla
	 * @param columnas
	 * @param tipos
	 * @param datos
	 * @throws Exception
	 */
	public void insertarTupla (String tabla, String[] columnas, String[] tipos,String[] datos) throws Exception{
		if (columnas.length == datos.length)
		{   
		    String atributos ="(" + columnas[0];
			String values ="(?";
			if(columnas.length == 1)
			{
				values+=")";
				atributos+=")";
			}
		    for(int i=1; i< columnas.length;i++){
		    	atributos += (","+columnas[i]);
		    	values+=",?";
		    	if(i+1==columnas.length){
		    		values+=")";
		    		atributos+=")";
		    	}
		    }
			String sql = "INSERT INTO " + tabla + " " + atributos + " VALUES " + values;
			PreparedStatement statement = conexion.prepareStatement(sql); 
			for(int i =0; i<columnas.length; i++){
				if(tipos[i].equals("String")){
					statement.setString(i+1, datos[i]);
					System.out.println( "'" +  datos[i] + "'");
				}
				else if(tipos[i].equals("int")){
					statement.setInt(i+1, Integer.parseInt(datos[i]));
					System.out.println(Integer.parseInt(datos[i]));
				}
				else if(tipos[i].equals("boolean")){
					boolean dato = false;
					dato = datos[i].equals("true");
					statement.setBoolean(i+1, dato);
					System.out.println(datos[i]);
				}
			}
			System.out.println(sql);
			
			int filasInsertadas = statement.executeUpdate();
			statement.close();
			if (filasInsertadas > 0) {
			    System.out.println("Nuevo registro fue ingresado con exito a la tabla " + tabla);
			}
		}
		
	}
	
	public void eliminarTupla(String tabla,String condicion) throws Exception{
		Statement s = conexion.createStatement();
		s.executeQuery ("DELETE FROM " + tabla + " WHERE " + condicion);
		s.close(); 
	}
	
	public void eliminarTuplaPorId(String tabla,String id) throws Exception{
		Statement s = conexion.createStatement();
		s.executeQuery ("DELETE FROM " + tabla + " WHERE " + "id="+id);
		s.close(); 
	}
	
	public void actualizarTupla (String tabla, String[] columnas,String[] datos, String condicion) throws Exception{
		if (columnas.length == datos.length)
		{
			String consulta = columnas[0] + " = " + datos[0];
			for (int i = 1; i < datos.length; i++) {
				consulta += ", " + columnas[i] + " = " + datos[i];
			}
			try 
			{
				Statement s = conexion.createStatement();
				s.executeQuery ("UPDATE " + tabla + " SET " + consulta + " WHERE " + condicion);
				System.out.println("Se actualiz� la tabla " + tabla);
				s.close();
			} 
			catch (SQLException e) 
			{
				throw new Exception("La tupla con ese id no existe.");
			}
		}
		else 
			throw new Exception("La cantidad de columnas en la tabla " + tabla + " debe ser igual a la cantidad de datos a actualizar.");
	}
	
	public ArrayList<String> darTuplas (String tabla){
		ArrayList<String> resultado = new ArrayList<String>();
		try
		{
			Statement s = conexion.createStatement();
			ResultSet rS = s.executeQuery ("SELECT id FROM " + tabla);
			while (rS.next()){
				resultado.add(rS.getString(1));
			}
			s.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	public ArrayList<String> darSubTabla (String tabla, String listaColumnas, String condicion) throws Exception{
		int contador = 0;
		ArrayList<String> resultado = new ArrayList<String>();
		Statement s = conexion.createStatement();
		String sql = "SELECT " + listaColumnas + " FROM " + tabla + " WHERE " + condicion ;
		System.out.println(sql);
		ResultSet rS = s.executeQuery (sql);
		while (rS.next()){
			contador++;
			System.out.println(rS.getString(1));
			resultado.add(rS.getString(1));	
		}
		s.close();
		System.out.println("Tuplas encontradas: " + contador);
		return resultado;
	}
	
	
	public void poblarTablas(){
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//productos.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Producto.NOMBRE, Producto.COLUMNAS, Producto.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//proveedores.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Proveedor.NOMBRE, Proveedor.COLUMNAS, Proveedor.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//usuarios.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Usuario.NOMBRE, Usuario.COLUMNAS, Usuario.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//materiasPrimas.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(MateriaPrima.NOMBRE, MateriaPrima.COLUMNAS, MateriaPrima.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//componentes.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Componente.NOMBRE, Componente.COLUMNAS, Componente.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//proveedoresComponentes.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Proveedor.NOMBRE_RELACION_COMPONENTE, Proveedor.COLUMNAS_RELACION_COMPONENTE, Proveedor.TIPO_RELACION_COMPONENTE,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//proveedoresMateriasPrimas.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Proveedor.NOMBRE_RELACION_MATERIA_PRIMA, Proveedor.COLUMNAS_RELACION_MATERIA_PRIMA, Proveedor.TIPO_RELACION_MATERIA_PRIMA,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//generadorID.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(AplicacionWeb.ID, AplicacionWeb.COLUMNAS, AplicacionWeb.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//estaciones.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Estacion.NOMBRE, Estacion.COLUMNAS, Estacion.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//registrosEstaciones.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Estacion.NOMBRE_REGISTRO_ESTACIONES, Estacion.COLUMNAS_REGISTRO_ESTACIONES, Estacion.TIPO_REGISTRO_ESTACIONES,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//registrosMateriasPrimas.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(MateriaPrima.NOMBRE_REGISTRO_MATERIAS_PRIMAS, MateriaPrima.COLUMNAS_REGISTRO_MATERIAS_PRIMAS, MateriaPrima.TIPO_REGISTRO_MATERIAS_PRIMAS,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//registrosComponentes.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Componente.NOMBRE_REGISTRO_COMPONENTES, Componente.COLUMNAS_REGISTRO_COMPONENTES, Componente.TIPO_REGISTRO_COMPONENTES,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//etapas.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Etapa.NOMBRE, Etapa.COLUMNAS, Etapa.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//pedidos.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Pedido.NOMBRE, Pedido.COLUMNAS, Pedido.TIPO,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//inventarioProductos.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Producto.NOMBRE_INVENTARIO_PRODUCTOS, Producto.COLUMNAS_INVENTARIO_PRODUCTOS, Producto.TIPO_INVENTARIO_PRODUCTOS,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			BufferedReader reader = new BufferedReader(new FileReader("data//datosTablas//registrosProductos.csv"));
			String linea = null;
			while((linea = reader.readLine())!=null){
				String[] lineaInsertar = linea.split(",");
				insertarTupla(Producto.NOMBRE_REGISTRO_PRODUCTOS, Producto.COLUMNAS_REGISTRO_PRODUCTOS, Producto.TIPO_REGISTRO_PRODUCTOS,lineaInsertar);
			}
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
