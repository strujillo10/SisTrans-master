package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;

public class ServletRegistrarProveedor extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Registro de Proveedores al Sistema";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,HttpServletResponse response) throws IOException {

		PrintWriter respuesta = response.getWriter( );
		String idProveedor = request.getParameter("id");
		String ciudad = request.getParameter("ciudad");
		String direccion = request.getParameter("direccion");
		int telefono = Integer.parseInt(request.getParameter("telefono"));
		String idRepLegal = request.getParameter("idRepLegal");
		
		try
		{
			AplicacionWeb.getInstancia().registrarProveedor(idProveedor, direccion, telefono, ciudad, idRepLegal);
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><h3>Se agrego el proveedor " + idProveedor + " correctamente.</h3></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"admin\" name=\"reingreso\"><input type=\"submit\" value=\"Volver\" size=\"33\" name=\"reingreso\" class=\"normal\"></form></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
		}
		catch (Exception e)
		{
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><h3>Hubo un error al agregar el proveedor, intentelo nuevamente.</h3></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"admin\" name=\"reingreso\"><input type=\"submit\" value=\"Volver\" size=\"33\" name=\"reingreso\" class=\"normal\"></form></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
		}
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
		
		String login = AplicacionWeb.getInstancia().darUsuarioActual();
		usuario = login;
	}

	@Override
	public void setTipoUsuario( HttpServletRequest request, HttpServletResponse response )  throws IOException{
		PrintWriter respuesta = response.getWriter( );
		
		String login = AplicacionWeb.getInstancia().darUsuarioActual();
		String tipo = "";
		try {
			tipo = AplicacionWeb.getInstancia().buscarUsuario(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tipoUsuario = tipo;
	}

}
