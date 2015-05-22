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

public class ServletRegistrarEtapasProduccion extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	public static final String VERDADERO = "'1'='1'"; 
	
	public static final String FALSO = "'1'='2'"; 
	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Registrar Etapa de Produccion";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter respuesta = response.getWriter( );
		
		String idProducto = request.getParameter("idProducto");
		
		String idAnterior = request.getParameter("idAnterior");
		
		String nombre = request.getParameter("nombre");
		
		int numeroSecuencia = Integer.parseInt(request.getParameter("numeroSecuencia"));
		
		int duracion = Integer.parseInt(request.getParameter("duracion"));
		
		String idEstacion = request.getParameter("estacion");
		
		String idMateriaPrima = request.getParameter("materiaPrima");
		
		String idComponente = request.getParameter("componente");
		
		String criterio = request.getParameter("criterio");
		
		String idActual = Integer.toString(AplicacionWeb.getInstancia().darContadorId());
		
		ArrayList<Estacion> estaciones = new ArrayList<Estacion>();
		
		ArrayList<MateriaPrima> materiasPrimas = new ArrayList<MateriaPrima>();
		
		ArrayList<Componente> componentes = new ArrayList<Componente>();
		
		try
		{
			AplicacionWeb.getInstancia().registrarEtapaProduccion(idActual, nombre, idProducto, idEstacion, idMateriaPrima, idComponente, duracion, numeroSecuencia, idAnterior);
			if (criterio.contains("Siguiente"))
			{				
				estaciones = AplicacionWeb.getInstancia().darEstaciones();
				materiasPrimas = AplicacionWeb.getInstancia().darMateriasPrimas(VERDADERO);
				componentes = AplicacionWeb.getInstancia().darComponentes(VERDADERO);
				respuesta.write( "<form method=\"POST\" action=\"registroEtapasProduccion.htm\"><input type=\"hidden\" value=" + idProducto + " name=\"idProducto\"><input type=\"hidden\" value=" + idActual + " name=\"idAnterior\">" );
				respuesta.write( "<table align= center bgcolor=\"#ecf0f1\" width=\"45%\">" );
				respuesta.write( "<tr>" );
				respuesta.write( "<td><h4>Nombre Etapa (descripcion): </h4></td>" );
				respuesta.write( "<td><input type=\"text\" name=\"nombre\" size=\"23\" class=\"normal\"></td>" );
				respuesta.write( "</tr>" );
				respuesta.write( "<tr>" );
				respuesta.write( "<td><h4>Numero de Secuencia:</h4></td>" );
				respuesta.write( "<td><input type=\"text\" name=\"numeroSecuencia\" size=\"23\" class=\"normal\" style=\"background: #FFFFFF; border: none;\" value=" + (numeroSecuencia + 1) + " readonly=\"readonly\" ></td>" );
				respuesta.write( "</tr>" );
				respuesta.write( "<tr></tr>" );
				respuesta.write( "<tr>" );
				respuesta.write( "<td><h4>Duracion (en dias):</h4></td>" );
				respuesta.write( "<td><input type=\"text\" name=\"duracion\" size=\"23\" class=\"normal\"></td>" );
				respuesta.write( "</tr>" );
				respuesta.write( "<tr></tr>" );
				respuesta.write( "<tr>" );
				respuesta.write( "<td><h4 align=\"left\">Estacion:</h4></td>" );
				respuesta.write( "<td width=\"100\"><select size=\"1\" name=\"estacion\" style=\"width: 207px\" class=\"normal\">" );
				for (Estacion estacion : estaciones) {
					respuesta.write( "<option value=" + estacion.getId() + ">" + estacion.getId() + "</option>" );
				}
				respuesta.write( "</select>" );
				respuesta.write( "</td>" );
				respuesta.write( "</tr>" );
				respuesta.write( "<tr></tr>" );
				respuesta.write( "<tr>" );
				respuesta.write( "<td><h4 align=\"left\">Materia Prima:</h4></td>" );
				respuesta.write( "<td width=\"100\"><select size=\"1\" name=\"materiaPrima\" style=\"width: 207px\" class=\"normal\">" );
				for (MateriaPrima materiaPrima : materiasPrimas) {
					respuesta.write( "<option value=" + materiaPrima.getId() + ">" + materiaPrima.getId() + "</option>" );
				}
				respuesta.write( "</select>" );
				respuesta.write( "</td>" );
				respuesta.write( "</tr>" );
				respuesta.write( "<tr></tr>" );
				respuesta.write( "<tr>" );
				respuesta.write( "<td><h4 align=\"left\">Componente:</h4></td>" );
				respuesta.write( "<td width=\"100\"><select size=\"1\" name=\"componente\" style=\"width: 207px\" class=\"normal\">" );
				for (Componente componente : componentes) {
					respuesta.write( "<option value=" + componente.getId() + ">" + componente.getId() + "</option>" );
				}
				respuesta.write( "</select>" );
				respuesta.write( "</td>" );
				respuesta.write( "</tr>" );
				respuesta.write( "<tr></tr>" );
				respuesta.write( "</table>" );
				respuesta.write( "<h4 align=\"center\"><input type=\"submit\" value=\"Registrar Siguiente Etapa de Produccion\" size=\"33\" name=\"criterio\" class=\"normal\" style=\"background: #FFFFFF; border: none;\"></h4>" );
				respuesta.write( "<h4 align=\"center\"><input type=\"submit\" value=\"Finalizar Registro Etapas de Produccion\" size=\"33\" name=\"criterio\" class=\"normal\" style=\"background: #FFFFFF; border: none;\"></form></h4>" );
			}
			else
			{
				respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
		        respuesta.write( "<tr>" );
		        respuesta.write( "<td align=\"center\"><h3>Se registraron correctamente el producto (" + idProducto + ") y sus respectivas estapas de produccion.</h3></td>" );
		        respuesta.write( "</tr>" );
		        respuesta.write( "<tr>" );
		        respuesta.write( "<td align=\"center\"><form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"admin\" name=\"reingreso\"><input type=\"submit\" value=\"Volver\" size=\"33\" name=\"reingreso\" class=\"normal\"></form></td>" );
		        respuesta.write( "</tr>" );
		        respuesta.write( "</table>" );
			}
		}
		catch (Exception e){
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td><h3>Debe ingresar las especificaciones solicitadas para continuar registrando etapas de produccion.</h3></td>" );
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
