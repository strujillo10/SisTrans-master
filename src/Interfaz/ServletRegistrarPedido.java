package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;
import mundo.Pedido;

public class ServletRegistrarPedido extends ServletAbstract{

	private String usuario;
	
	private String tipoUsuario;

	
	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Registro de Pedido";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter respuesta = response.getWriter( );
		
		int cantidad = Integer.parseInt(request.getParameter("cantidad"));
		String idProducto = request.getParameter("idProducto");
		String nombre = request.getParameter("nombre");
		String login = request.getParameter("login");
		int precio = Integer.parseInt(request.getParameter("precio"));
		Date pedido = new Date();
		Date entrega = pedido;
		try
		{
			entrega = AplicacionWeb.getInstancia().registrarPedido(login, idProducto, cantidad, pedido);
	        
	        respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
	        respuesta.write( "<tr><h4 align=\"center\">Gracias " + login + " por comprar con ProdAndes. El siguiente pedido se ha realizado de manera exitosa. La fecha de entrega es: " + entrega.toLocaleString() + ".</h4></tr>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<tr><td><input alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\" name=\"producto\"></td>" );
	        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
	        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=\"" + nombre + "\" name=\"producto\" type=\"hidden\"\"><td align=\"right\">" + nombre + "</td></tr>" );
	        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Costo Total: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (precio* cantidad) + "</td></tr>" );
	        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Unidades Pedidas: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + cantidad + "</td></tr>" );
	        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (pedido.toLocaleString()).substring(0, 10) + "</td></tr>" );
	        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + (entrega.toLocaleString()).substring(0, 10) + "</td></tr>" );
	        respuesta.write( "<input type=\"hidden\" name=\"login\" value=" + login + ">" );
	        respuesta.write( "</table></td>" );
	        respuesta.write( "</tr>" );
	        respuesta.write( "<tr></tr>" );
	        respuesta.write( "</table>" );
	        
	        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
	        pedidos = AplicacionWeb.getInstancia().darPedidosCliente(login);
	        
	        if (pedidos.size() != 0)
	        {
	        	respuesta.write( "<hr>" );
	        	respuesta.write( "<h4 align=\"center\">Tienes registrados " + pedidos.size() + " pedidos en total:</h4>" );
	        	respuesta.write( "</table>" );
	        	respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
		        for (Pedido ped : pedidos) {
		        	System.out.println("El tamaño del arreglo de pedidos es" + pedidos.size());
		        	String producto = AplicacionWeb.getInstancia().darNombreProducto(ped.getIdProducto());
		        	respuesta.write( "<form method=\"POST\" action=\"eliminar.htm\">" );
		        	respuesta.write( "<tr>" );
			        respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\"></td>" );
			        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=\"" + producto + "\" name=\"producto\" type=\"hidden\"\"><td align=\"right\">" + producto + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Costo Total: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (precio* cantidad) + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Unidades Pedidas: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + cantidad + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (pedido.toLocaleString()).substring(0, 10) + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + (entrega.toLocaleString()).substring(0, 10) + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"right\"><input value=" + ped.getId() + " name=\"idPedido\" type=\"hidden\"><input value=" + login + " name=\"login\" type=\"hidden\"><input value=\"eliminarPedido\" name=\"criterio\" type=\"hidden\"><input value=\"Eliminar Pedido\" size=\"53\" name=\"eliminar\" type=\"submit\"></td></tr>" );
			        respuesta.write( "</table></td>" );
			        respuesta.write( "</tr>" );
			        respuesta.write( "<tr></tr>" );
			        respuesta.write( "</form>" );
				}
		        respuesta.write( "</table>" );
	        }
		}
		catch (Exception e){
			e.printStackTrace();
	        respuesta.write( "<table bgcolor=\"#ecf0f1\" width=80%>" );
	        respuesta.write( "<tr>" );
	        respuesta.write( "<td><h3>El producto no tiene las unidades disponibles que desea.</h3></td>" );
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
