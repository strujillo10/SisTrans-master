package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;
import mundo.Componente;
import mundo.MateriaPrima;

public class ServletRegistrarMaterial extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Registro Material";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter respuesta = response.getWriter( );
		
		String nombre = request.getParameter("nombre");
		String unidadMedida = request.getParameter("unidadMedida");
		int cantInicial = Integer.parseInt(request.getParameter("cantInicial"));
		String idProveedor = request.getParameter("idProveedor");
		String fecha = "" + request.getParameter("fecha");
		String tipo;
		
		try
		{			
			if (unidadMedida != null)
			{
				tipo = "Materia Prima";
				AplicacionWeb.getInstancia().registrarMateriaPrima(nombre, unidadMedida, cantInicial, idProveedor);
			}
			else{
				tipo = "Componente";
				AplicacionWeb.getInstancia().registrarComponente(nombre, cantInicial, idProveedor);
			}
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><h3>Se agrego correctamente el material " + nombre + " al sistema.</h3></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"admin\" name=\"reingreso\"><input type=\"submit\" value=\"Volver\" size=\"33\" name=\"reingreso\" class=\"normal\"></form></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
		}
		catch (Exception e){
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><h3>Hubo un error al agregar el material " + nombre + " al sistema, intentelo nuevamente.</h3></td>" );
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
