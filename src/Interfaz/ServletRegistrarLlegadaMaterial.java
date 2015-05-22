package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;
import mundo.Proveedor;

public class ServletRegistrarLlegadaMaterial extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Registro Llegada Material";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try
		
		{
			PrintWriter respuesta = response.getWriter( );
				
			String llegadaMaterial = request.getParameter("tipo");
			
			ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
			
			proveedores = AplicacionWeb.getInstancia().darProveedores(ServletResultadoBusqueda.VERDADERO, ServletResultadoBusqueda.VERDADERO, ServletResultadoBusqueda.VERDADERO);
			
			respuesta.write( " <form method=\"POST\" action=\"registroMaterial.htm\">" );
			respuesta.write( " <table align=\"center\" bgcolor=\"#ecf0f1\" width=\"80%\">" );
			respuesta.write( " <tr>" );
			
			if (llegadaMaterial.equals("materiaPrima"))
			{
				respuesta.write( " <td><h3>Fecha</h3></td>" );
				respuesta.write( " <td><h3>Nombre</h3></td>" );
				respuesta.write( " <td><h3>Unidad de Medida</h3></td>" );
				respuesta.write( " <td><h3>Cantidad</h3></td>" );
				respuesta.write( " <td><h3>Proveedor</h3></td>" );
				respuesta.write( " </tr>" );
				respuesta.write( " <tr>" );
				respuesta.write( " <td><h4><input size=\"23\" type=\"hidden\" id=\"date\"/ name=\"fecha\"> " );
				respuesta.write( " <script name=\"fecha\" language=\"javascript\">" );
				respuesta.write( " var today = new Date();" );
				respuesta.write( " document.write(today.toDateString());" );
				respuesta.write( " </script></h4></td>" );
				respuesta.write( " <form method=\"POST\" action=\"registroMaterial.htm\">" );
				respuesta.write( " <td><input type=\"text\" name=\"nombre\" size=\"23\" class=\"normal\"></td>" );
				respuesta.write( " <td><input type=\"text\" name=\"unidadMedida\" size=\"23\" class=\"normal\"></td>" );				
				respuesta.write( " <td><input type=\"text\" name=\"cantInicial\" size=\"23\" class=\"normal\"></td>" );
				respuesta.write( "<td>" );
		        respuesta.write( "<select size=\"1\" name=\"idProveedor\" class=\"normal\" style=\"border: none;\">" );
		        for (Proveedor proveedor : proveedores) {
		        	respuesta.write( "<option value=\"" + proveedor.getId() + "\">" + proveedor.getId() + "</option>" );
				}
		        respuesta.write( "</select>" );
		        respuesta.write( "</td>");

			}
			else
			{
				respuesta.write( " <td><h3>Fecha a Registrar</h3></td>" );
				respuesta.write( " <td><h3>Nombre</h3></td>" );
				respuesta.write( " <td><h3>Cantidad</h3></td>" );
				respuesta.write( " <td><h3>Proveedor</h3></td>" );
				respuesta.write( " </tr>" );
				respuesta.write( " <tr>" );
				respuesta.write( " <td><h4><input size=\"23\" type=\"hidden\" id=\"date\"/ name=\"fecha\"> " );
				respuesta.write( " <script name=\"fecha\" language=\"javascript\">" );
				respuesta.write( " var today = new Date();" );
				respuesta.write( " document.write(today.toDateString());" );
				respuesta.write( " </script></h4></td>" );
				respuesta.write( " <form method=\"POST\" action=\"registroMaterial.htm\">" );
				respuesta.write( " <td><input type=\"text\" name=\"nombre\" size=\"23\" class=\"normal\"></td>" );		
				respuesta.write( " <td><input type=\"text\" name=\"cantInicial\" size=\"23\" class=\"normal\"></td>" );
				respuesta.write( "<td>" );
		        respuesta.write( "<select size=\"1\" name=\"idProveedor\" class=\"normal\" style=\"border: none;\">" );
		        for (Proveedor proveedor : proveedores) {
		        	respuesta.write( "<option value=\"" + proveedor.getId() + "\">" + proveedor.getId() + "</option>" );
				}
		        respuesta.write( "</select>" );
		        respuesta.write( "</td>");
			}
			
			respuesta.write( " </form>" );
			respuesta.write( " </tr>" );
			respuesta.write( " </table>" );
			respuesta.write( " <p align=center>" );
			respuesta.write( " <input type=\"submit\" value=\"Registrar\" name=\"B1\" class=\"normal\">" );
			respuesta.write( " <input type=\"reset\" value=\"Borrar\" name=\"B2\" class=\"normal\"></p>" );
			respuesta.write( " </form>" );
		
		}	
		catch (Exception e)
		{
			e.printStackTrace();
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
