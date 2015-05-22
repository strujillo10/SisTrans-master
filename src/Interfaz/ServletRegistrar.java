package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.security.util.Password;
import mundo.AplicacionWeb;
import mundo.Producto;

public class ServletRegistrar extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Seccion Usuarios";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter respuesta = response.getWriter( );
		
		String usuario = request.getParameter("usuario");
		String contrasenia = request.getParameter("contrasenia");
		String tipo = request.getParameter("tipo");

		try{
			AplicacionWeb.getInstancia().registrarUsuario(usuario, contrasenia, tipo);
			aceptarIngreso(respuesta, usuario, contrasenia, tipo);
		}
		catch(Exception e){
			denegarRegistro(respuesta);
		}
	}

	public void aceptarIngreso(PrintWriter respuesta, String login, String contrasenia, String tipo){
		if (tipo.equals("admin"))
			registroAdmin(respuesta, login);
		else if (tipo.equals("natural") || tipo.equals("juridica"))
			registroCliente(respuesta, login, contrasenia);
		else
			denegarRegistro(respuesta);
	}
	
	public void registroAdmin(PrintWriter respuesta, String login){
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
	
	public void registroCliente(PrintWriter respuesta, String login, String contrasenia){
		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
        respuesta.write( "<tr>" );
        respuesta.write( "<td><h3> BIENVENIDO " + login + "</h3></td>" );
        respuesta.write( "</tr>" );
        respuesta.write( "</table>" );
		respuesta.write( "<div></div>" );
		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
        respuesta.write( "<tr>" );
        respuesta.write( "<td><h4 align=\"center\">Por favor ingresa los siguientes datos para completar tu registro.</h4></td>" );
        respuesta.write( "</tr>" );
        respuesta.write( "</table>" );
        respuesta.write( "<hr>" );
        
        respuesta.write( " <form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"primerLogin\" name=\"primerLogin\"><input type=\"hidden\" value=" + login + " name=\"usuario\" class=\"normal\"><input type=\"hidden\" value=" + contrasenia + " name=\"contrasenia\" class=\"normal\">" );
		respuesta.write( " <table align=\"center\" bgcolor=\"#ecf0f1\" width=\"50%\">" );
		respuesta.write( " <tr>" );
		respuesta.write( " <td><h3>Nombre</h3></td>" );
		respuesta.write( " <td><input type=\"text\" name=\"nombre\" size=\"23\" class=\"normal\"></td>" );
		respuesta.write( " </tr>" );
		respuesta.write( " <tr>" );
		respuesta.write( " <td><h3>Direccion</h3></td>" );
		respuesta.write( " <td><input type=\"text\" name=\"direccion\" size=\"23\" class=\"normal\"></td>" );				
		respuesta.write( " </tr>" );
		respuesta.write( " <tr>" );
		respuesta.write( " <td><h3>Telefono</h3></td>" );
		respuesta.write( " <td><input type=\"text\" name=\"telefono\" size=\"23\" class=\"normal\"></td>" );
		respuesta.write( " </tr>" );
		respuesta.write( " <tr>" );
		respuesta.write( " <td><h3>Ciudad</h3></td>" );
		respuesta.write( " <td><input type=\"text\" name=\"ciudad\" size=\"23\" class=\"normal\"></td>" );
		respuesta.write( " </tr>" );
		respuesta.write( " <tr>" );
		respuesta.write( " <td><h3>Id Representante</h3></td>" );
		respuesta.write( " <td><input type=\"text\" name=\"idRepLegal\" size=\"23\" class=\"normal\"></td>" );
		respuesta.write( " </tr>" );
		respuesta.write( " </table>" );
		respuesta.write( " <p align=center>" );
		respuesta.write( " <input type=\"submit\" value=\"Continuar\" name=\"B1\" class=\"normal\">" );
		respuesta.write( " <input type=\"reset\" value=\"Borrar\" name=\"B2\" class=\"normal\"></p>" );
		respuesta.write( " </form>" );
	}
	
	public void denegarRegistro(PrintWriter respuesta){
		
        respuesta.write( "<table bgcolor=\"#ecf0f1\" width=80%>" );
        respuesta.write( "<tr>" );
        respuesta.write( "<td><h3>Error: Ya existe un usuario con ese identificador.</h3></td>" );
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
		PrintWriter respuesta = response.getWriter( );
		
		String login = request.getParameter("usuario");
		usuario = login;
	}

	@Override
	public void setTipoUsuario( HttpServletRequest request, HttpServletResponse response )  throws IOException{
		PrintWriter respuesta = response.getWriter( );
		
		String login = request.getParameter("usuario");
		String tipo = "";
		try {
			tipo = AplicacionWeb.getInstancia().buscarUsuario(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tipoUsuario = tipo;
	}
	
}
