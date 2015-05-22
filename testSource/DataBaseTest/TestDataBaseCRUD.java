package DataBaseTest;

import mundo.AplicacionWeb;
import mundo.CRUD;
import mundo.ConexionDAO;
import mundo.Usuario;
import junit.framework.TestCase;

public class TestDataBaseCRUD extends TestCase{

	private CRUD crud;
	private ConexionDAO dao;
	
	/**
	 * El setup del escenario 1
	 */
	public void setupEscenario1(){
		dao = new ConexionDAO();
		crud = new CRUD(dao);
	}
	
	/**
	 * El setup del escenario 2
	 */
	public void setupEscenario2(){
		setupEscenario1();
		try
		{
			String[] datos = { "1", "1", "admin"};
			crud.insertarTupla(Usuario.NOMBRE, Usuario.COLUMNAS, Usuario.TIPO, datos);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Prueba del metodo agregar
	 */
	public void testNoAceptaIdDoble(){
		setupEscenario2();
		try
		{
			String[] datos = { "1", "1", "admin"};
			crud.insertarTupla(Usuario.NOMBRE, Usuario.COLUMNAS, Usuario.TIPO, datos);
			fail("No debió haber agregado la tupla");
		}
		catch (Exception e){
			
		}
	}
	}
