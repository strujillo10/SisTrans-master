package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;
import mundo.Componente;
import mundo.Estacion;
import mundo.MateriaPrima;

public class ServletRegistrarEstacion extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	public static final String VERDADERO = "'1'='1'"; 
	
	public static final String FALSO = "'1'='2'"; 
	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Registro Estacion";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter respuesta = response.getWriter( );
		
		String idEstacion = Integer.toString(AplicacionWeb.getInstancia().darContadorId());
		
		String nombre = request.getParameter("nombre");
		
		String tipo = request.getParameter("tipo");
				
		try
		{
			AplicacionWeb.getInstancia().registrarEstacion(idEstacion,nombre, tipo);
			
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><h3>La estacion " + nombre + " de tipo " + tipo + " se registro correctamente.</h3></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"admin\" name=\"reingreso\"><input type=\"submit\" value=\"Volver\" size=\"33\" name=\"reingreso\" class=\"normal\"></form></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "</table>" );
		}
		catch (Exception e){
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td align=\"center\"><h3>Hubo un error en el registro de estaciones, intentelo nuevamente.</h3></td>" );
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
