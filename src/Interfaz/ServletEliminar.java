package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;
import mundo.Componente;
import mundo.Estacion;
import mundo.MateriaPrima;
import mundo.Pedido;
import mundo.Producto;
import mundo.Proveedor;
import mundo.Usuario;

public class ServletEliminar extends ServletAbstract{
	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Eliminar";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter respuesta = response.getWriter( );
	
		String criterio = request.getParameter("criterio");
		System.out.println("El criterio es: " + criterio);
				
		if (criterio.equals("eliminarPedido"))
			eliminarPedido(request, respuesta);
		
		else if (criterio.equals("desactivarEstacion"))
			desactivarEstacion(request, respuesta);
		

	}
	
	public void eliminarPedido (HttpServletRequest request, PrintWriter respuesta){
		String login = request.getParameter("login");
		String idPedido = request.getParameter("idPedido");
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
		try
		{
			AplicacionWeb.getInstancia().eliminarPedidoCliente(login, idPedido);
		}
		catch (Exception e)
		{
			error(respuesta, "Lo sentimos, hubo un error, por favor intentalo nuevamente.", "cliente");
		}
		try 
        {
        	pedidos = AplicacionWeb.getInstancia().darPedidosCliente(login);
        	if (pedidos.size() != 0)
	        {
        		respuesta.write( "<h4 align=\"center\">Tienes registrados " + pedidos.size() + " pedidos en total:</h4>" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
		        for (Pedido ped : pedidos) {
		        	String producto = AplicacionWeb.getInstancia().darNombreProducto(ped.getIdProducto());
		        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<tr>" );
			        respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\"></td>" );
			        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + producto + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Unidades Pedidas: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + ped.getCantidad() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (ped.getFechaPedido().toLocaleString()).substring(0, 10) + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + (ped.getFechaEntrega().toLocaleString()).substring(0, 10) + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"right\"><input value=" + ped.getId() + " name=\"idPedido\" type=\"hidden\"><input value=" + login + " name=\"login\" type=\"hidden\"><input value=\"eliminarPedido\" name=\"criterio\" type=\"hidden\"><input value=\"Eliminar Pedido\" size=\"53\" name=\"eliminar\" type=\"submit\"></td></tr>" );
			        respuesta.write( "</table></td>" );
			        respuesta.write( "</tr>" );
			        respuesta.write( "<tr></tr>" );
			        respuesta.write( "</form>" );
				}
		        respuesta.write( "</table>" );
	        }
        	else
        		noHayPedidos(login, respuesta);
        }
		catch (Exception e)
        {
        	noHayPedidos(login, respuesta);
        }
	}

	public void desactivarEstacion(HttpServletRequest request, PrintWriter respuesta){
		String idEstacion = request.getParameter("idEstacion");
		System.out.println("La estacion a desactivar es: " + idEstacion);
		try{
			AplicacionWeb.getInstancia().desactivarEstacionProduccion(idEstacion);
			ArrayList<Estacion> estaciones = new ArrayList<Estacion>();
			try
			{
				estaciones = AplicacionWeb.getInstancia().darEstaciones();
				if (!estaciones.isEmpty()){
					respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + estaciones.size() + " estaciones en total:</h4>" );
	        		respuesta.write( "<form method=\"POST\" action=\"eliminar.htm\"><input name=\"criterio\" value=\"consultarPedidos\" type=\"hidden\">" );
	        		respuesta.write( "<hr>" );
					respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=20%>" );
			        for (Estacion estacion: estaciones) {
			        	String activada = "Desactivada";
			        	if( estacion.isActivada() )
			        		activada = "Activada";
			        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
			        	respuesta.write( "<tr><td><h3>Estacion: " + estacion.getId() + " -  Tipo: " + estacion.getTipo() + "</h3></td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Estacion: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + estacion.getId() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + estacion.getNombre() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Tipo: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + estacion.getTipo() + "</td></tr>" );
				        if (activada.equals("Activada"))
				        	respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Estado: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + activada + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"right\"><input value=\"" + estacion.getId() + "\" name=\"idEstacion\" type=\"hidden\"><td align=\"right\"><input value=\"desactivarEstacion\" name=\"criterio\" type=\"hidden\"><input value=\"Desactivar Estacion\" name=\"eliEsta\" type=\"submit\"></td></tr>");
				        respuesta.write( "<tr></tr>" );
				        respuesta.write( "</form>" );
			        }
			        respuesta.write( "</table>" );
				}
				else
					error(respuesta, "No hay estaciones registradas en ProdAndes.", "admin");	
			}
			catch (Exception e1){
				error(respuesta, "No hay estaciones registradas en ProdAndes.", "admin");
				e1.printStackTrace();
			}
		}
		catch(Exception e){
			error(respuesta, "No se puede desactivar la estaci�n, no hay estaciones suficientes para satisfacer los pedidos actuales.", "admin");
			e.printStackTrace();
		}
	}

	public void noHayPedidos(String login, PrintWriter respuesta){
		respuesta.write( "<h4 align=\"center\">No has registrado ning�n pedido con nosotros, creemos que estos productos que podr�an interesarte.</h4>" );
    	ArrayList<Producto> productos = new ArrayList<Producto>();
    	try
		{
			productos = AplicacionWeb.getInstancia().darCantidadProductos(100);
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
		
	public void error(PrintWriter respuesta, String mensaje, String tipo){
		
		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
        respuesta.write( "<tr>" );
        respuesta.write( "<td align=\"center\"><h3>" + mensaje + "</h3></td>" );
        respuesta.write( "</tr>" );
        respuesta.write( "<tr>" );
        respuesta.write( "<td align=\"center\"><form method=\"POST\" action=\"ingreso.htm\"><input type=\"hidden\" value=\"" + tipo + "\" name=\"reingreso\"><input type=\"submit\" value=\"Volver\" size=\"33\" name=\"reingreso\" class=\"normal\"></form></td>" );
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
		String login = AplicacionWeb.getInstancia().darUsuarioActual();
		super.usuario = login;
	}

	@Override
	public void setTipoUsuario( HttpServletRequest request, HttpServletResponse response )  throws IOException{
		String login = AplicacionWeb.getInstancia().darUsuarioActual();
		String tipo = "";
		try {
			tipo = AplicacionWeb.getInstancia().buscarUsuario(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.tipoUsuario = tipo;
	}
}
