package mundo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;


public class ConexionDAO
{	

	private ConnectionFactory cf;
	private Connection conex;
	private Session sesion;
	private Destination destino;
	private MessageProducer mp;
	private Sender sender;
	private Receiver receiver;
	
	/**
	 * Conexi�n
	 */
	private Connection conexion;
	
	/**
	 * Usuario
	 */
	private String usuario;
	
	/**
	 * Constrase�a
	 */
	private String contrasenia;
	
	/**
	 * URL
	 */
	private String url;
	
	/**
	 * Class name
	 */
	public final static String CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
	
	/**
	 * Constructor de la clase
	 */
	public ConexionDAO ()
	{
		
//		usuario = "ISIS2304461510";
//		contrasenia = "cjdsault";
		usuario = "ISIS2304311510";
		contrasenia = "tquz5";
		url = "jdbc:oracle:thin:@prod.oracle.virtual.uniandes.edu.co:1531:prod";
		
		//TRUJILLO
		sender = new Sender(); 
		receiver = new Receiver(); 
		try 
		{
			// Inicia el contexto seg�n la interfaz dada por JBOSS.
			InitialContext init = new InitialContext();
			this.cf = (ConnectionFactory) init.lookup("RemoteConnectionFactory");
			this.destino = (Destination) init.lookup("queue/testCola");
			this.conex = (Connection) this.cf.createConnection("sistrans", "test");
			((javax.jms.Connection) this.conex).start();
			this.sesion = ((javax.jms.Connection) this.conex).createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.mp = this.sesion.createProducer(this.destino);

			System.out.println("OK");
		} 
		catch (Exception e) 
		{
			// Ocurrio un error y se imprimira por consola
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor de la clase
	 */
	public ConexionDAO (String url, String usuario, String contrasenia){
		
		this.usuario = usuario;
		this.contrasenia = contrasenia;
		this.url = url;
	}
	
	/**
	 * Retorna la conexion
	 * @return la conexion
	 */
	public Connection darConexion(){
		return conexion;
	}
	
	/**
	 * Inicia la conexi�n con la base de datos
	 */
	public void iniciarConexion (){
		try
		{
			Class.forName(CLASS_NAME);
			conexion = DriverManager.getConnection(url, usuario, contrasenia);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//TRUJILLO
	public void establecerConexion()
	{
		try 
		{
			System.out.println("Abriendo conexiones..");
			conex = (Connection) cf.createConnection();
		} 
		catch (Exception e) 
		{
			// Error al crear las conexiones
			e.printStackTrace();
		}
	}
	public void closeConnection(Connection connection) throws Exception 
	{        
		try 
		{
			connection.close();
			connection = null;
		} 
		catch (SQLException exception) 
		{
			throw new Exception("ERROR: ConsultaDAO: closeConnection() = cerrando una conexion.");
		}
	} 
	
	/**
	 * Cierra la conexi�n con la base de datos
	 */
    public void cerrarConexion() throws Exception{        
		try {
			conexion.close();
			conexion = null;
		} catch (SQLException exception) {
			throw new Exception("Error al intentar cerrar la conexi�n.");
		}
    }
    
    /**
     * Da el nivel de aislamiento implementado
     */
    public int darNivelAislamiento(){
    	try{
        	return conexion.getTransactionIsolation();
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
		return 0;
    }
    
    /**
     * Cambia la propiedad de autocommit de la conexión a falso
     */
    public void setAutoCommitFalso(){
    	try{
        	conexion.setAutoCommit(false);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * Cambia la propiedad de autocommit de la conexión a verdadero
     */
    public void setAutoCommitVerdadero(){
    	try{
        	conexion.setAutoCommit(true);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }

	/**
	 * Crea las tablas
	 */
	public void crearTablas (){
		try 
		{
			// TABLA 1: PRODUCTOS ------------------------------------------------------------
		
			Statement s = conexion.createStatement( );
			
			boolean crearTabla = false;
			
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM productos" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}
			
			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE productos (id varchar(32), nombre varchar(32), precio int, PRIMARY KEY (id))" );
				System.out.println("Se cre� la tabla productos");
			}
			else
			System.out.println("La tabla productos ya existe");
			s.close();
			
			
			// TABLA 2: PROVEEDORES ------------------------------------------------------------

			crearTabla = false;

			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM proveedores" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE proveedores (id varchar(32), direccion varchar(32), telefono varchar(32), ciudad varchar(32), idRepLegal varchar(32), PRIMARY KEY (id))" );
				System.out.println("Se cre� la tabla proveedores");
			}
			else
			System.out.println("La tabla proveedores ya existe");
			s.close();
			
			
			// TABLA 3: USUARIOS ------------------------------------------------------------

			crearTabla = false;

			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM usuarios" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE usuarios (login varchar(32), password varchar(32), tipo varchar(32), nombre varchar(32), direccion varchar(32), telefono int, ciudad varchar(32), idRepLegal varchar(32), PRIMARY KEY (login))" );
				System.out.println("Se cre� la tabla usuarios");
			}
			else
			System.out.println("La tabla usuarios ya existe");
			s.close();
			
			
			// TABLA 4: MATERIAS PRIMAS ------------------------------------------------------------

			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM materiasPrimas" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE materiasPrimas (id varchar(32), unidadMedida varchar(32), cantidadInicial int, PRIMARY KEY (id))" );
				System.out.println("Se cre� la tabla materiasPrima");
			}
			else
			System.out.println("La tabla materiasPrimas ya existe");
			s.close();
			
			
			// TABLA 5: COMPONENTES  ------------------------------------------------------------
			
			crearTabla = false;

			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM componentes" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE componentes (id varchar(32), unidadMedida varchar(32), cantidadInicial int, PRIMARY KEY (id))" );
				System.out.println("Se cre� la tabla componentes");
			}
			else
			System.out.println("La tabla componentes ya existe");
			s.close();
			
			
			// TABLA 6: PROVEEDORES MATERIAS PRIMAS ------------------------------------------------------------
			
			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM ProveedoresMateriasPrimas" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE ProveedoresMateriasPrimas (id_proveedor varchar(32), id_materiaPrima varchar(32), PRIMARY KEY (id_proveedor,id_materiaPrima), CONSTRAINT fk_idProveedorMateriaPrima FOREIGN KEY (id_proveedor) REFERENCES proveedores(id), CONSTRAINT fk_idMateriasPrimasProveedor FOREIGN KEY (id_materiaPrima) REFERENCES materiasPrimas(id))" );
				System.out.println("Se cre� la tabla ProveedoresMateriasPrimas");
			}
			else
			System.out.println("La tabla componentes ProveedoresMateriasPrimas");
			s.close();
			
			
			// TABLA 7: PROVEEDORES COMPONENTES ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM ProveedoresComponentes" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE ProveedoresComponentes (id_proveedor varchar(32), id_componente varchar(32), PRIMARY KEY (id_proveedor,id_componente), CONSTRAINT fk_idProveCompo FOREIGN KEY (id_proveedor) REFERENCES proveedores(id), CONSTRAINT fk_idComponentesProveedor FOREIGN KEY (id_componente) REFERENCES componentes(id))" );
				System.out.println("Se cre� la tabla ProveedoresComponentes");
			}
			else
			System.out.println("La tabla ProveedoresComponentes ya existe");
			s.close();
			
			
			// TABLA 8: GENERADORID  ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM generadorId" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE generadorId (id varchar(32), PRIMARY KEY (id))" );
				System.out.println("Se cre� la tabla generadorId");
			}
			else
				System.out.println("La tabla generadorId ya existe");
			s.close();
			
			
			// TABLA 9: ESTACIONES ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM estaciones" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE estaciones (id varchar(32), nombre varchar(32), tipo varchar(32), activada int, PRIMARY KEY (id))" );
				System.out.println("Se cre� la tabla estaciones");
			}
			else
			System.out.println("La tabla estaciones ya existe");
			s.close();
			
			
			// TABLA 10: REGISTROS ESTACIONES ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM registrosEstaciones" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE registrosEstaciones (id varchar(32), idEstacion varchar(32), dia int, mes int, PRIMARY KEY (id), CONSTRAINT fk_idEstacionRegistro FOREIGN KEY (idEstacion) REFERENCES estaciones(id), CONSTRAINT unq_EtapaRegistroEstacion UNIQUE (idEstacion,dia,mes))" );
				System.out.println("Se cre� la tabla registrosEstaciones");
			}
			else
			System.out.println("La tabla registrosEstaciones ya existe");
			s.close();
			
			
			// TABLA 11: REGISTROS MATERIAS PRIMAS ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM registrosMateriasPrimas" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE registrosMateriasPrimas (id varchar(32), idMateriaPrima varchar(32), PRIMARY KEY (id), CONSTRAINT fk_idMateriaPrimaRegistro FOREIGN KEY (idMateriaPrima) REFERENCES materiasPrimas(id))" );
				System.out.println("Se cre� la tabla registrosMateriasPrimas");
			}
			else
			System.out.println("La tabla registrosMateriasPrimas ya existe");
			s.close();
			
			
			// TABLA 12: REGISTROS COMPONENTES ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM registrosComponentes" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE registrosComponentes (id varchar(32), idComponente varchar(32), PRIMARY KEY (id), CONSTRAINT fk_idComponenteRegistro FOREIGN KEY (idComponente) REFERENCES componentes(id))" );
				System.out.println("Se cre� la tabla registrosComponentes");
			}
			else
			System.out.println("La tabla registrosComponentes ya existe");
			s.close();
			
			
			// TABLA 13: ETAPAS ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM etapas" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
	
			if( crearTabla )
			{
				s.execute( "CREATE TABLE etapas (id varchar(32),nombre varchar(32), idProducto varchar(32), idEstacion varchar(32), idMateriaPrima varchar(32), idComponente varchar(32), duracion int, numeroSecuencia int, idAnterior varchar(32), PRIMARY KEY (id), CONSTRAINT fk_idProductoEtapa FOREIGN KEY (idProducto) REFERENCES productos(id), CONSTRAINT fk_idEstacionEtapa FOREIGN KEY (idEstacion) REFERENCES estaciones(id), CONSTRAINT fk_idMateriaPrimaEtapa FOREIGN KEY (idMateriaPrima) REFERENCES materiasPrimas(id), CONSTRAINT fk_idComponenteEtapa FOREIGN KEY (idComponente) REFERENCES componentes(id), CONSTRAINT unq_ofertaUnicaEtapa UNIQUE (idProducto,numeroSecuencia))" );
				System.out.println("Se cre� la tabla etapas");
			}
			else
			System.out.println("La tabla etapas ya existe");
			s.close();
			
			
			// TABLA 14: PEDIDOS ------------------------------------------------------------
			
			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM pedidos" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE pedidos (id varchar(32), idProducto varchar(32), idUsuario varchar(32), diaPedido int, mesPedido int, diaEntrega int, mesEntrega int, cantidad int, PRIMARY KEY (id),CONSTRAINT fk_idCliPed FOREIGN KEY (idUsuario) REFERENCES usuarios(login))" );
				System.out.println("Se cre� la tabla pedidos");
			}
			else
			System.out.println("La tabla pedidos ya existe");
			s.close();	
			
			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM inventarioProductos" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE inventarioProductos (id varchar(32), idProducto varchar(32), idPedido varchar(32), PRIMARY KEY (id), CONSTRAINT fk_idProductoInventario FOREIGN KEY (idProducto) REFERENCES productos(id),CONSTRAINT fk_idPedidoInven FOREIGN KEY (idPedido) REFERENCES pedidos(id))" );
				System.out.println("Se cre� la tabla inventarioProductos");
			}
			else
			System.out.println("La tabla inventarioProductos ya existe");
			s.close();
		
			
			// TABLA 15: INVENTARIO PRODUCTOS ------------------------------------------------------------

			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM inventarioProductos" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE inventarioProductos (id varchar(32), idProducto varchar(32), idPedido varchar(32), PRIMARY KEY (id), CONSTRAINT fk_idProductoInventario FOREIGN KEY (idProducto) REFERENCES productos(id),CONSTRAINT fk_idPedidoInven FOREIGN KEY (idPedido) REFERENCES pedidos(id))" );
				System.out.println("Se cre� la tabla inventarioProductos");
			}
			else
			System.out.println("La tabla inventarioProductos ya existe");
			s.close();	
			

			// TABLA 16: REGISTROS PRODUCTOS ------------------------------------------------------------
			
			crearTabla = false;
			
			s = conexion.createStatement( );
			try
			{
				// Verificar si ya existe la tabla
				s.executeQuery( "SELECT * FROM registrosProductos" );
			}
			catch( SQLException se )
			{
				// La excepci�n se lanza si la tabla no existe
				crearTabla = true;
			}

			// Se crea una nueva tabla vac�a
			if( crearTabla )
			{
				s.execute( "CREATE TABLE registrosProductos (id varchar(32), idEtapa varchar(32), idInventario varchar(32), idRegistroEstacion varchar(32), idRegistroMateriaPrima varchar(32), idRegistroComponente varchar(32), PRIMARY KEY (id), CONSTRAINT fk_idEtapaReg FOREIGN KEY (idEtapa) REFERENCES etapas(id), CONSTRAINT fk_idInventarioReg FOREIGN KEY (idInventario) REFERENCES inventarioProductos(id), CONSTRAINT fk_idRegistroEstacion FOREIGN KEY (idRegistroEstacion) REFERENCES registrosEstaciones(id), CONSTRAINT fk_idRegistrosMateriaPrima FOREIGN KEY (idRegistroMateriaPrima) REFERENCES registrosMateriasPrimas(id), CONSTRAINT fk_idRegistroComponente FOREIGN KEY (idRegistroComponente) REFERENCES registrosComponentes(id),CONSTRAINT unq_registroEstacion UNIQUE (idRegistroEstacion),CONSTRAINT unq_registroMateriaPrima UNIQUE (idRegistroMateriaPrima),CONSTRAINT unq_registroComponente UNIQUE (idRegistroComponente))" );
				System.out.println("Se cre� la tabla registrosProductos");
			}
			else
			System.out.println("La tabla registrosProductos ya existe");
			s.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
	}
}