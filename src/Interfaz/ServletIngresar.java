package Interfaz;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.ExceptionList;

import mundo.AplicacionWeb;
import mundo.Producto;

public class ServletIngresar extends ServletAbstract{

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Seccion Usuarios";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter respuesta = response.getWriter( );
		
		String usuario = request.getParameter("usuario");
		String constrasenia = request.getParameter("contrasenia");
		String primerLogin = request.getParameter("primerLogin");
		ArrayList<Producto> productos = new ArrayList<Producto>();
		String reingreso = request.getParameter("reingreso");
		String tipo = "";
		
		try{						
			if (reingreso == null){
				tipo = AplicacionWeb.getInstancia().buscarUsuario(usuario, constrasenia);
				if (primerLogin != null){
					String nombre = request.getParameter("nombre");
					String direccion = request.getParameter("direccion");
					int telefono = Integer.parseInt(request.getParameter("telefono"));
					String ciudad = request.getParameter("ciudad");
					String idRepLegal = request.getParameter("idRepLegal");
					AplicacionWeb.getInstancia().registrarCliente(usuario, nombre, direccion, telefono, ciudad, idRepLegal);
				}
			}
			aceptarIngreso(respuesta, usuario, tipo, reingreso,productos);
		}	
		catch (Exception e){
			denegarIngreso(respuesta);
		}
		
	}

	public void aceptarIngreso(PrintWriter respuesta, String login, String tipo, String reingreso, ArrayList<Producto> productos){
		
		if (tipo.equals("admin"))
			ingresarAdmin(respuesta, login);
		else if (tipo.equals("natural") || tipo.equals("juridica"))
			ingresarCliente(respuesta, login, productos);
		else if (reingreso != null)
			reingresar(respuesta, reingreso, productos);
		else
			denegarIngreso(respuesta);
	}
	
	public void ingresarAdmin(PrintWriter respuesta, String login){
		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=\"80%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3>BIENVENIDO ADMINISTRADOR " + login + "</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
	
		respuesta.write( "<table align=\"center\" style=\"border: 2px solid #a1a1a1;\" width=\"90%\">" );
		respuesta.write( "<tr>" );
		
		respuesta.write( "<td><table align=\"left\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Proveedores</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"registrarProveedor.htm\"><h4 align=\"left\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><input type=\"submit\" value=\"Registrar Proveedores\" name=\"regProve\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><h4 align=\"left\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><input value=\"darProveedores\" name=\"criterio\" type=\"hidden\"><input type=\"submit\" value=\"Consultar Proveedores\" name=\"regProve\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );

//		Siguiente columna
		
		respuesta.write( "<td><table align=\"center\" width=\"30%\">" );
		respuesta.write( "<form method=\"POST\" action=\"registroLlegadaMaterial.htm\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td>" );
		respuesta.write( "<table align=\"center\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Materiales</h3></td>" );
		respuesta.write( "<td><select style=\"font-size: 16px;\" name=\"tipo\" size=\"1\"  class=\"normal\">" );
		respuesta.write( "<option value=\"materiaPrima\">Materia Prima</option>" );
		respuesta.write( "<option value=\"componente\">Componente</option>" );
		respuesta.write( "</select></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h4 align=\"left\"><input value=\"materiaPrima\" name=\"tipo\" type=\"hidden\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><input type=\"submit\" value=\"Registrar Materiales\" name=\"regMate\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><h4 align=\"left\"><input value=\"darMaterial\" name=\"criterio\" type=\"hidden\"><h4><input type=\"submit\" value=\"Consultar Materiales\" name=\"consMate\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );


//		Siguiente columna
					
		respuesta.write( "<td><table align=\"right\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Productos</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"registrarProducto.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><h4 align=\"left\"><input type=\"submit\" value=\"Registrar Productos\" name=\"regProd\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><h4 align=\"left\"><input value=\"darProductos\" name=\"criterio\" type=\"hidden\"><input type=\"submit\" value=\"Consultar Productos\" name=\"consProd\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );
		respuesta.write( "</tr>" );
		
//		Siguiente Fila
		
		respuesta.write( "<tr><td>" );
		respuesta.write( "<table align=\"left\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Estaciones</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"registrarEstacion.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><h4 align=\"left\"><input type=\"submit\" value=\"Registrar Estaciones\" name=\"regEst\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><h4 align=\"left\"><input type=\"hidden\" value=\"darEstaciones\" name=\"criterio\"><input type=\"submit\" value=\"Consultar Estaciones\" name=\"consEst\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );
		
//		Siguiente columna
					
		respuesta.write( "<td>" );
		respuesta.write( "<table align=\"center\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Clientes</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><input value=\"darClientes\" name=\"criterio\" type=\"hidden\"><h4 align=\"left\"><input type=\"submit\" value=\"Consultar Clientes\" name=\"regProve\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );
		
//		Siguiente columna
		
		respuesta.write( "<td><table align=\"right\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Pedidos</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><input type=\"hidden\" value=\"" + login + "\" name=\"login\"><input value=\"consultarPedidos\" name=\"criterio\" type=\"hidden\"><h4 align=\"left\"><input type=\"submit\" value=\"Consultar Pedidos\" name=\"regProve\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );
		respuesta.write( "</tr>" );
		
//		Siguiente Fila
		
		respuesta.write( "<tr><td>" );
		respuesta.write( "<table align=\"left\" width=\"30%\">" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Etapas Produccion</h3></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"registrarEstacion.htm\"><h4 align=\"left\"><input type=\"submit\" value=\"Registrar Etapa\" name=\"regEst\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr>" );
		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><h4 align=\"left\"><input type=\"hidden\" value=\"coincidirEtapas\" name=\"criterio\"><input type=\"submit\" value=\"Consultar Etapas\" name=\"consEtap\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
		respuesta.write( "</tr>" );
		respuesta.write( "<tr></tr>" );
		respuesta.write( "</table>" );
		respuesta.write( "</td>" );
		
////		Siguiente columna
//		
//		respuesta.write( "<td><table align=\"right\" width=\"30%\">" );
//		respuesta.write( "<tr>" );
//		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Etapas Produccion</h3></td>" );
//		respuesta.write( "</tr>" );
//		respuesta.write( "<tr>" );
//		respuesta.write( "<td><form method=\"POST\" action=\"registrarProducto.htm\"><h4 align=\"left\"><input type=\"submit\" value=\"Registrar Etapas\" name=\"regProd\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
//		respuesta.write( "</tr>" );
//		respuesta.write( "<tr>" );
//		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><h4 align=\"left\"><input value=\"darEtapas\" name=\"criterio\" type=\"hidden\"><input type=\"submit\" value=\"Consultar Etapas\" name=\"consEtap\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
//		respuesta.write( "</tr>" );
//		respuesta.write( "<tr></tr>" );
//		respuesta.write( "</table>" );
//		respuesta.write( "</td>" );
//		
////		Siguiente columna
//		
//		respuesta.write( "<td><table align=\"right\" width=\"30%\">" );
//		respuesta.write( "<tr>" );
//		respuesta.write( "<td><h3 style=\"padding:0.5em;\"> Etapas Produccion</h3></td>" );
//		respuesta.write( "</tr>" );
//		respuesta.write( "<tr>" );
//		respuesta.write( "<td><form method=\"POST\" action=\"registrarProducto.htm\"><h4 align=\"left\"><input type=\"submit\" value=\"Registrar Etapas\" name=\"regProd\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
//		respuesta.write( "</tr>" );
//		respuesta.write( "<tr>" );
//		respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><h4 align=\"left\"><input value=\"darEtapas\" name=\"criterio\" type=\"hidden\"><input type=\"submit\" value=\"Consultar Etapas\" name=\"consEtap\" class=\"normal\"style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
//		respuesta.write( "</tr>" );
//		respuesta.write( "<tr></tr>" );
//		respuesta.write( "</table>" );
		respuesta.write( "</tr>" );
		
		respuesta.write("</tr>");
		respuesta.write( "</table>" );
	}
	
	public void ingresarCliente(PrintWriter respuesta, String login, ArrayList<Producto> productos){
		try
		{
			productos = AplicacionWeb.getInstancia().darCantidadProductos(100);
			
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td><h3> BIENVENIDO " + login + "</h3></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
			respuesta.write( "<div></div>" );
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td><form method=\"POST\" action=\"resultadoBusqueda.htm\"><input type=\"hidden\" value=" + login + " name=\"login\" class=\"normal\"><input type=\"hidden\" value=\"darPedidos\" name=\"criterio\" class=\"normal\"><h4 align=\"left\"><input type=\"submit\" value=\"Consulta los detalles de tus pedidos\" name=\"regProve\" class=\"normal\" style=\"background: #FFF; border: none; padding-left: 5em\"></h4></form></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
	        respuesta.write( "<hr>" );
	        respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
	        respuesta.write( "<td><input style=\"color=#BFBFBF\" type=\"text\" name=\"nombre\" class=\"normal\" value=\"Ingresa el nombre de un producto que estes buscando\" size=\"110\"></td>" );
	        respuesta.write( "<input type=\"hidden\" value=" + login + " name=\"login\" class=\"normal\">" );
	        respuesta.write( "<input type=\"hidden\" value=\"buscarProductoCliente\" name=\"criterio\" class=\"normal\">" );
	        respuesta.write( "<td><input type=\"submit\" value=\"Buscar Productos\" name=\"BusProd\" class=\"normal\"></td></form>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
	        respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td><h3> Productos que podr�an interesarte </h3></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
	        respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        for (int i = 0; i < productos.size(); i++) {
	        	respuesta.write( "<tr>" );
	        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        respuesta.write( "<input type=\"hidden\" name=\"criterio\" value=\"buscarProductoCliente\" >" );
		        respuesta.write( "<input type=\"hidden\" name=\"idProducto\" value=" + productos.get(i).getId() + " >" );
		        respuesta.write( "<input type=\"hidden\" name=\"login\" value=" + login + ">" );
	        	respuesta.write( "<td><input alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\" name=\"producto\" value=" + productos.get(i).getId() + "></td>" );
		        respuesta.write( "<td><input value=" + productos.get(i).getNombre() + " name=\"nombre\" style=\"background: #FFFFFF; border: none;\" type=\"submit\"\"></td>" );
		        respuesta.write( "</form>" );
		        try
		        {
		        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<input type=\"hidden\" name=\"criterio\" value=\"buscarProductoCliente\" >" );
		        	respuesta.write( "<input type=\"hidden\" name=\"idProducto\" value=" + productos.get(i+1).getId() + " >" );
			        respuesta.write( "<input type=\"hidden\" name=\"login\" value=" + login + ">" );
		        	respuesta.write( "<td><input alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\" name=\"producto\" value=" + productos.get(i + 1).getId() + "></td>" );
			        respuesta.write( "<td><input value=" + productos.get(i+1).getNombre() + " name=\"idProducto\" style=\"background: #FFFFFF; border: none;\" type=\"submit\"\"></td>" );
			        respuesta.write( "</form>" );
		        }
		        catch(Exception e2){	
		        }
		        try
		        {
		        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<input type=\"hidden\" name=\"criterio\" value=\"buscarProductoCliente\" >" );
		        	respuesta.write( "<input type=\"hidden\" name=\"idProducto\" value=" + productos.get(i+2).getId() + " >" );
			        respuesta.write( "<input type=\"hidden\" name=\"login\" value=" + login + ">" );
		        	respuesta.write( "<td><input alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\" name=\"producto\" value=" + productos.get(i + 2).getId() + "></td>" );
			        respuesta.write( "<td><input value=" + productos.get(i+2).getNombre() + " name=\"idProducto\" style=\"background: #FFFFFF; border: none;\" type=\"submit\"\"></td>" );
			        respuesta.write( "</form>" );
		        }
		        catch(Exception e3){	
		        }
		        try
		        {
		        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<input type=\"hidden\" name=\"criterio\" value=\"buscarProductoCliente\" >" );
		        	respuesta.write( "<input type=\"hidden\" name=\"idProducto\" value=" + productos.get(i+3).getId() + " >" );
			        respuesta.write( "<input type=\"hidden\" name=\"login\" value=" + login + ">" );
		        	respuesta.write( "<td><input alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\" name=\"producto\" value=" + productos.get(i + 3).getId() + "></td>" );
		        	respuesta.write( "<td><input value=" + productos.get(i+3).getNombre() + " name=\"idProducto\" style=\"background: #FFFFFF; border: none;\" type=\"submit\"\"></td>" );
			        respuesta.write( "</form>" );
		        }
		        catch(Exception e4){	
		        }
		        respuesta.write( "</tr>" );
		        i+=3;
	        }
	        respuesta.write( "</table>" );
		}
		catch (Exception e1){
		}
	}
	
	public void reingresar(PrintWriter respuesta, String reingreso, ArrayList<Producto> productos){
		if (reingreso.equals("admin"))
			ingresarAdmin(respuesta, AplicacionWeb.getInstancia().darUsuarioActual());
		else if (reingreso.equals("cliente"))
			ingresarCliente(respuesta, AplicacionWeb.getInstancia().darUsuarioActual(), productos);
		else
			denegarIngreso(respuesta);
	}
	
	public void denegarIngreso(PrintWriter respuesta){
		
        respuesta.write( "<table bgcolor=\"#ecf0f1\" width=80%>" );
        respuesta.write( "<tr>" );
        respuesta.write( "<td><h3>Error: Usuario o contrasenia incorrectos</FONT></td>" );
        respuesta.write( "</tr>" );
        respuesta.write( "</table>" );
		
	}
	

	@Override
	public String darUsuario() {
		return usuario;
	}

	@Override
	public String darTipoUsuario() {
		return tipoUsuario;
	}

	@Override
	public void setUsuario( HttpServletRequest request, HttpServletResponse response )  throws IOException{	
		String login = request.getParameter("usuario");
		super.usuario = login;
		System.out.println("Servlet Ingreso: " + super.tipoUsuario);
	}

	@Override
	public void setTipoUsuario( HttpServletRequest request, HttpServletResponse response )  throws IOException{
		String login = request.getParameter("usuario");
		String tipo = "";
		try {
			tipo = AplicacionWeb.getInstancia().buscarUsuario(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tipoUsuario = tipo;
		System.out.println("Servlet Ingreso: " + super.tipoUsuario);
	}
}
