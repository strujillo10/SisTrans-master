package Interfaz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mundo.AplicacionWeb;
import mundo.Componente;
import mundo.Estacion;
import mundo.Etapa;
import mundo.MateriaPrima;
import mundo.Pedido;
import mundo.Producto;
import mundo.Proveedor;
import mundo.Usuario;

public class ServletResultadoBusqueda extends ServletAbstract{
	
	private String usuario;
	
	private String tipoUsuario;

	
	public static final String VERDADERO = "'1'='1'"; 
	
	public static final String FALSO = "'1'='2'"; 

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Resultado de Busqueda";
	}

	@Override
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter respuesta = response.getWriter( );
	
		String criterio = request.getParameter("criterio");
				
		if (criterio.equals("buscarProductoCliente"))
			buscarProductoCliente(request, respuesta);
		
		else if (criterio.equals("darPedidos"))
			darPedidos(request, respuesta);
		
		else if (criterio.equals("darClientes"))
			darClientes(request, respuesta);
		
		else if (criterio.equals("darProveedores"))
			darProveedores(request, respuesta);
		
		else if (criterio.equals("darProductosProveedor"))
			darProductosProveedor(request, respuesta);
			
		else if (criterio.equals("darPedidosProveedor"))
			darPedidosProveedor(request, respuesta);
		
		else if (criterio.equals("consultarPedidos"))
			consultarPedidos(request, respuesta);
		
		else if (criterio.equals("darEstaciones"))
			darEstaciones(request, respuesta);
		
		else if (criterio.equals("darProductos"))
			darProductos(request, respuesta);
		
		else if (criterio.equals("darMaterial"))
			darMateriales(request, respuesta);
		
		else if (criterio.equals("noCoincidirEtapas"))
			darEtapasNo(request, respuesta);
		
		else if (criterio.equals("coincidirEtapas"))
			darEtapas(request, respuesta);
	
	}

	public void buscarProductoCliente (HttpServletRequest request, PrintWriter respuesta){
		String idProducto = request.getParameter("idProducto");
		String nombreProducto = request.getParameter("nombre");
		ArrayList<Producto> rta = new ArrayList<Producto>();
		if (idProducto == null)
			idProducto = request.getParameter("producto");
		if(idProducto == null){
			try{
				rta = AplicacionWeb.getInstancia().buscarProducto(nombreProducto);
			}
			catch (Exception e){
				error(respuesta, "Hubo un error, por favor, vuelve a intentarlo.", "cliente");
				e.printStackTrace();
			}
		}
		if (rta.isEmpty()){
			try{
				rta = AplicacionWeb.getInstancia().darProducto(idProducto);
			}
			catch (Exception e){
				error(respuesta, "No se encontr� el producto que estas buscando.", "cliente");
			}
		}
		
		String login = request.getParameter("login");
		
		if (!rta.isEmpty()){
			respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
			for (int i = 0; i < rta.size(); i++) {
				respuesta.write( "<form method=\"POST\" action=\"registroPedido.htm\">" );
	        	respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\"></td>" );
	        	respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=10%>" );
		        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=" + rta.get(i).getId() + " name=\"idProducto\" type=\"hidden\"><td align=\"right\">" + rta.get(i).getNombre() + "</td></tr>" );
		        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Precio: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=" + rta.get(i).getNombre() + " name=\"nombre\" type=\"hidden\"><td align=\"right\">" + rta.get(i).getPrecio() + "</td></tr>" );
		        respuesta.write( "<tr><td><table bgcolor=\"#ecf0f1\" width=10%>" );
		        respuesta.write( "<tr><td align=\"left\"><h4>Unidades: </h4></td>" );
		        respuesta.write( "<td align=\"right\">" );
		        respuesta.write( "<select size=\"1\" name=\"cantidad\" class=\"normal\" style=\"border: none;\">" );
		        respuesta.write( "<option value=\"1\">1</option>" );
		        respuesta.write( "<option value=\"2\">2</option>" );
		        respuesta.write( "<option value=\"3\">3</option>" );
		        respuesta.write( "</select>" );
		        respuesta.write( "</td></tr>" );
		        respuesta.write( "</table></td></tr>" );
		        respuesta.write( "<tr><td align=\"right\"><input value=\"Pedir\" size=\"33\" name=\"pedir\" type=\"submit\"\"></td></tr>" );
		        respuesta.write( "<input type=\"hidden\" name=\"login\" value=" + login + ">" );
		        respuesta.write( "<input type=\"hidden\" name=\"precio\" value=" + rta.get(i).getPrecio() + ">" );
		        respuesta.write( "</table></td>" );
		        respuesta.write( "</tr>" );
		        respuesta.write( "</form>" );
	        }
			respuesta.write( "</table>" );
		}
		else
			error(respuesta, "No se encontro el producto que estas buscando.", "cliente");
	}
	
	public void darPedidos (HttpServletRequest request, PrintWriter respuesta){
		String login = request.getParameter("login");
		String admin = request.getParameter("admin");
		
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        try 
        {
        	pedidos = AplicacionWeb.getInstancia().darPedidosCliente(login);
        	if (pedidos.size() != 0)
	        {
        		if (admin != null)
        			respuesta.write( "<h4 align=\"center\">El cliente " + login + " tiene registrados " + pedidos.size() + " pedidos en total:</h4>" );
        		else
        			respuesta.write( "<h4 align=\"center\">Tienes registrados " + pedidos.size() + " pedidos en total:</h4>" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
		        for (Pedido ped : pedidos) {
		        	String producto = AplicacionWeb.getInstancia().darNombreProducto(ped.getIdProducto());
		        	respuesta.write( "<form method=\"POST\" action=\"eliminar.htm\">" );
		        	respuesta.write( "<tr>" );
			        respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\"></td>" );
			        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + producto + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Unidades Pedidas: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + ped.getCantidad() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (ped.getFechaPedido().toLocaleString()).substring(0, 5) + "2015</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + (ped.getFechaEntrega().toLocaleString()).substring(0, 5) + "2015</td></tr>" );
			        if (admin == null)
			        	respuesta.write( "<tr><td align=\"right\"><input value=" + ped.getId() + " name=\"idPedido\" type=\"hidden\"><input value=" + login + " name=\"login\" type=\"hidden\"><input value=\"eliminarPedido\" name=\"criterio\" type=\"hidden\"><input value=\"Eliminar Pedido\" size=\"53\" name=\"eliminar\" type=\"submit\"></td></tr>" );
			        respuesta.write( "</table></td>" );
			        respuesta.write( "</tr>" );
			        respuesta.write( "<tr></tr>" );
			        respuesta.write( "</form>" );
				}
		        respuesta.write( "</table>" );
	        }
        	else{
        		noHayPedidos(login, respuesta);
        		error(respuesta, "", "cliente");
        	}
        }
        catch (Exception e){
        	noHayPedidos(login, respuesta);
        	error(respuesta, "", "cliente");
        	e.printStackTrace();
        }
	}
	
	public void darClientes (HttpServletRequest request, PrintWriter respuesta){
		String filtro = request.getParameter("filtro");
		String atributo = request.getParameter("atributo");
		String filtroNombre = VERDADERO;
		String filtroPedido = VERDADERO;
		String filtroProducto = VERDADERO;
		ArrayList<Usuario> clientes = new ArrayList<Usuario>();
		try
		{
			if (filtro != null)
			{
				if (atributo.equals("nombre"))
				{
					filtroNombre = "UPPER(nombre) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("pedido"))
				{
					filtroPedido = "UPPER(idPedido) LIKE UPPER('%" + filtro + "%')";
				}
				if (atributo.equals("producto"))
				{
					filtroProducto = "UPPER(nombreProducto) LIKE UPPER('%" + filtro + "%')";
				}
			}

			clientes = AplicacionWeb.getInstancia().darClientes(filtroNombre, filtroPedido, filtroProducto);
			if (clientes.size() != 0)
	        {
        		respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + clientes.size() + " clientes en total:</h4>" );
        		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\"><input name=\"criterio\" value=\"darClientes\" type=\"hidden\">" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
        		respuesta.write( "<tr>" );
        		respuesta.write( "<td><h4 align=\"center\">Filtrar por: </h4></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"atributo\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"nombre\">Nombre</option>" );
        		respuesta.write( "<option value=\"pedido\">Pedido</option>" );
    			respuesta.write( "<option value=\"producto\">Producto</option>" );
    			respuesta.write( "</select></td>" );
    			respuesta.write( "<td align=\"center\"><input name=\"filtro\" value=\"Ingrese un nombre\" type=\"text\"></td>" );
    			respuesta.write( "<td align=\"center\"><input name=\"buscar\" value=\"Buscar\" type=\"submit\"></td>" );
        		respuesta.write( "</tr>" );
        		respuesta.write( "</table>" );
        		respuesta.write( "</form>" );
        		respuesta.write( "<hr>" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=70%>" );
		        for (Usuario cliente : clientes) {
        			respuesta.write( "<tr><td align=\"left\"><h3><input value=\"Cliente: " + cliente.getNombre() + " \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h3></td></tr>" );
			        respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<tr>" );
			        respuesta.write( "<td><img alt=\"Cliente\" src=\"imagenes/usuario.jpg\" name=\"usuario\"></td>" );
			        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=70%>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + cliente.getNombre() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Usuario: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + cliente.getLogin() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Direccion: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + cliente.getDireccion() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Telefono: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + cliente.getTelefono() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Ciudad: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + cliente.getCiudad() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Represetante: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + cliente.getIdRepLegal() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );   
			        if (cliente.getPedidos().size() != 0)
			        {
			        	respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Pedidos: " + cliente.getPedidos().size() + "\" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td></tr>" );
			        	for (Pedido pedido : cliente.getPedidos()){
					        respuesta.write( "<tr><td align=\"left\"><h4 style=\"padding:1em;\"><input value=\"Id Pedido: " + pedido.getId() + " \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Producto: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h3></td><td><input value=\"" + pedido.getIdProducto() + "\" readonly=\"readonly\" style=\"width:100%; font-size: 12.8px; border: none; text-align:left;\"></td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Cantidad: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h3></td><td><input value=\"" + pedido.getCantidad() + "\" readonly=\"readonly\" style=\"width:100%; font-size: 12.8px; border: none; text-align:left;\"></td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h3></td><td><input value=\"" + pedido.getFechaPedido().toLocaleString().substring(0, 10) + "\" readonly=\"readonly\" style=\"width:100%; font-size: 12.8px; border: none; text-align:left;\"></td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h3></td><td><input value=\"" + pedido.getFechaEntrega().toLocaleString().substring(0, 10) + "\" readonly=\"readonly\" style=\"width:100%; border: none; font-size: 12.8px; text-align:left;\"></td></tr>" );
			        	}
			        }
			        respuesta.write( "</table></td>" );
			        respuesta.write( "</tr>" );
			        respuesta.write( "<tr></tr>" );
			        respuesta.write( "</form>" );
				}
		        respuesta.write( "</table>" );
	        }
			else
			{
				error(respuesta, "No hay clientes registrados en ProdAndes.", "admin");
			}
		}
		catch (Exception e)
		{
			error(respuesta, "No hay clientes registrados en ProdAndes.", "admin");
			e.printStackTrace();
		}
	}
	
	public void darProveedores (HttpServletRequest request, PrintWriter respuesta){
		String filtro = request.getParameter("filtro");
		String atributo = request.getParameter("atributo");
		String filtroProveedor = VERDADERO;
		String filtroMateria = VERDADERO;
		String filtroComponente = VERDADERO;
		ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
		try
		{
			if (filtro != null)
			{
				if (atributo.equals("proveedor"))
				{
					filtroProveedor = "UPPER(id) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("componente"))
				{
					filtroComponente = "UPPER(id_componente) LIKE UPPER('%" + filtro + "%')";
					filtroMateria = FALSO;
				}
				if (atributo.equals("materiaPrima"))
				{
					filtroMateria = "UPPER(id_materiaPrima) LIKE UPPER('%" + filtro + "%')";
					filtroComponente = FALSO;
				}
			}
			proveedores = AplicacionWeb.getInstancia().darProveedores(filtroProveedor,filtroMateria,filtroComponente);
			System.out.println(proveedores.size());
			if (proveedores.size() != 0)
	        {
				
        		respuesta.write( "<h3 align=\"center\">ProdAndes tiene registrados " + proveedores.size() + " proveedores en total:</h3>" );
        		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\"><input name=\"criterio\" value=\"darProveedores\" type=\"hidden\">" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
        		respuesta.write( "<tr>" );
        		respuesta.write( "<td><h4 align=\"center\">Filtrar por: </h4></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"atributo\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"proveedor\">Proveedor</option>" );
        		respuesta.write( "<option value=\"materiaPrima\">Materia Prima</option>" );
    			respuesta.write( "<option value=\"componente\">Componente</option>" );
    			respuesta.write( "</select></td>" );
    			respuesta.write( "<td align=\"center\"><input name=\"filtro\" value=\"Ingrese un nombre\" type=\"text\"></td>" );
    			respuesta.write( "<td align=\"center\"><input name=\"buscar\" value=\"Buscar\" type=\"submit\"></td>" );
        		respuesta.write( "</tr>" );
        		respuesta.write( "</table>" );
        		respuesta.write( "</form>" );
        		respuesta.write( "<hr>" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=70%>" );
		        for (Proveedor proveedor : proveedores) {

			        respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<tr><td align=\"left\"><h3><input value=\"Proveedor: " + proveedor.getId() + "\" style=\"border: none;\" type=\"text\"\"></h3></td></tr>" );
		        	respuesta.write( "<tr>" );
		        	respuesta.write( "<td><img alt=\"Proveedor\" src=\"imagenes/proveedor.jpg\" name=\"proveedor\"></td>" );
			        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=70%>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Nombre: \" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + proveedor.getId() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Direccion: \" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + proveedor.getDireccion() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Telefono: \" style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + proveedor.getTelefono() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Ciudad: \"  style=\"border: none;\" type=\"text\"\"></h4></td><td ><input value=\"" + proveedor.getCiudad() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Represetante: \"  style=\"border: none;\" type=\"text\"\"></h4></td><td><input value=\"" + proveedor.getIdRepLegal() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
			        if (proveedor.getMateriasPrimas().size() != 0)
			        {
			        	respuesta.write( "<tr><td align=\"left\"><h4 style=\"padding:1em;\"><input value=\"(" + proveedor.getMateriasPrimas().size() + ") Materias Primas\"  style=\"border: none;\" type=\"text\"\"></h4></td></tr>" );
				        for (int i = 0; i < proveedor.getMateriasPrimas().size(); i++) {
				        	respuesta.write( "<tr><td align=\"left\"><h4><input value=\"" + (i+1) + ". " + proveedor.getMateriasPrimas().get(i).getId() + "\" style=\"border: none;\" type=\"text\"\"></h4></td><td> <input value=\" : " + proveedor.getMateriasPrimas().get(i).getCantidadInicial() + " " + proveedor.getMateriasPrimas().get(i).getUnidadMedida() + "\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left;\"></td></tr>" );
						}
			        }
			        if (proveedor.getComponentes().size() != 0)
			        {
			        	respuesta.write( "<tr><td align=\"left\"><h4 style=\"padding:1em;\"><input value=\"(" + proveedor.getComponentes().size() + ") Componentes\"  style=\"border: none;\" type=\"text\"\"></h4></td></tr>" );
				        for (int i = 0; i < proveedor.getComponentes().size(); i++) {
				        	respuesta.write( "<tr><td align=\"left\"><h4><input value=\"" + (i+1) + ". " + proveedor.getComponentes().get(i).getId() + "\" style=\"border: none;\" type=\"text\"\"></h4></td><td > <input value=\" : " + proveedor.getComponentes().get(i).getCantidadInicial() + " unidades\" readonly=\"readonly\" style=\"width:100%; border: none; text-align:left; \"></td></tr>" );
						}
			        }
			        respuesta.write( "<tr><td align=\"right\"><input value=" + proveedor.getId() + " name=\"idProveedor\" type=\"hidden\"><h4><input value=\"darProductosProveedor\" name=\"criterio\" type=\"hidden\"><input value=\"Ver Productos Dependientes\" size=\"53\" name=\"verProductos\" type=\"submit\" style=\"border: none; background: #FFFFFF\"></h4></td></tr></form>" );
			        respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\"><tr><td align=\"right\"><h4><input value=" + proveedor.getId() + " name=\"idProveedor\" type=\"hidden\"><input value=\"darPedidosProveedor\" name=\"criterio\" type=\"hidden\"><input value=\"Ver Pedidos Dependientes\" size=\"53\" name=\"verPedidos\" style=\"border: none; background: #FFFFFF\" type=\"submit\"></h4></td></tr></form>" );
			        respuesta.write( "</table></td>" );
			        respuesta.write( "</tr>" );
			        respuesta.write( "<tr></tr>" );
				}
		        respuesta.write( "</table>" );
	        }
			else
			{
				error(respuesta, "No hay proveedores registrados en ProdAndes.", "admin");
			}
		}
		catch (Exception e){
			error(respuesta, "No hay proveedores registrados en ProdAndes.", "admin");
			e.printStackTrace();
		}
	}
	
	public void darProductosProveedor (HttpServletRequest request, PrintWriter respuesta){
		String idProveedor = request.getParameter("idProveedor");
		ArrayList<Producto> productosProveedor = new ArrayList<Producto>();
		try
		{
			productosProveedor = AplicacionWeb.getInstancia().darProductosProveedor(idProveedor);
			if (!productosProveedor.isEmpty())
			{
				respuesta.write( "<h3 align=\"center\">Hay " + productosProveedor.size() + " productos relacionados a un pedido que dependen del proveedor " + idProveedor.toUpperCase() + "</h3>" );
				respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
				for (int i = 0; i < productosProveedor.size(); i++) {
					respuesta.write( "<tr><td align=\"left\"><h3><input value=\"Id Producto: " + productosProveedor.get(i).getId() + "\" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h3></td></tr>");
		        	respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" type=\"image\"></td>" );
		        	respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=10%>" );
		        	respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Producto: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=" + productosProveedor.get(i).getId() + " name=\"idProducto\" type=\"hidden\"><td align=\"right\">" + productosProveedor.get(i).getId() + "</td></tr>" );
		        	respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=" + productosProveedor.get(i).getId() + " name=\"idProducto\" type=\"hidden\"><td align=\"right\">" + productosProveedor.get(i).getNombre() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Precio: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><input value=" + productosProveedor.get(i).getNombre() + " name=\"nombre\" type=\"hidden\"><td align=\"right\">" + productosProveedor.get(i).getPrecio() + "</td></tr>" );
			        respuesta.write( "<tr><td><table bgcolor=\"#ecf0f1\" width=10%>" );
			        respuesta.write( "</table></td></tr>" );
			        respuesta.write( "</table></td>" );
			        respuesta.write( "</tr>" );
			        respuesta.write( "</form>" );
		        }
				respuesta.write( "</table>" );
			}
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	public void darPedidosProveedor (HttpServletRequest request, PrintWriter respuesta){
		String idProveedor = request.getParameter("idProveedor");
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
		try
    	{
			pedidos = AplicacionWeb.getInstancia().darPedidosProveedor(idProveedor);
			if (!pedidos.isEmpty())
			{
				respuesta.write( "<h4 align=\"center\">Existen " + pedidos.size() + " pedidos que dependen del proveedor " + idProveedor + "</h4>" );
	    		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
		        for (Pedido ped : pedidos) {
		        	
	        		String producto = AplicacionWeb.getInstancia().darNombreProducto(ped.getIdProducto());
	        		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
		        	respuesta.write( "<tr>" );
			        respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\"></td>" );
			        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + ped.getId() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + producto + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Unidades Pedidas: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + ped.getCantidad() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (ped.getFechaPedido().toLocaleString()).substring(0, 5) + "2015</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + (ped.getFechaEntrega().toLocaleString()).substring(0, 5) + "2015</td></tr>" );
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
    	}
	}
	
	public void consultarPedidos(HttpServletRequest request, PrintWriter respuesta){
		String filtro = request.getParameter("filtro");
		String atributo = request.getParameter("atributo");
		String filtroLogin = VERDADERO;
		String filtroNombreCliente = VERDADERO;
		String filtroProducto = VERDADERO;
		String filtroPedido = VERDADERO;
		String filtroMaterial = VERDADERO;
		String filtroCantidad = VERDADERO;
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
		try
		{
			if (filtro != null)
			{
				if (atributo.equals("login"))
				{
					filtroLogin = "UPPER(login) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("nombreCliente"))
				{
					filtroNombreCliente = "UPPER(nombreCliente) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("pedido"))
				{
					filtroPedido = "idPedido = '" + filtro + "'";
				}
				else if (atributo.equals("material"))
				{
					filtroMaterial = "UPPER(idMateriaPrima) LIKE UPPER('%" + filtro + "%') OR UPPER(idComponente) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("cantidad"))
				{
					filtroCantidad = "cantidad > " + filtro + "";
				}
				if (atributo.equals("producto"))
				{
					filtroProducto = "UPPER(nombreProducto) LIKE UPPER('%" + filtro + "%')";
				}
			}
    		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\"><input name=\"criterio\" value=\"consultarPedidos\" type=\"hidden\">" );
    		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
    		respuesta.write( "<tr>" );
    		respuesta.write( "<td><h4 align=\"center\">Filtrar por: </h4></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"atributo\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"nombreCliente\">Nombre Cliente</option>" );
    		respuesta.write( "<option value=\"login\">Usuario Cliente</option>" );
    		respuesta.write( "<option value=\"pedido\">Pedido</option>" );
			respuesta.write( "<option value=\"producto\">Producto</option>" );
			respuesta.write( "<option value=\"material\">Material</option>" );
			respuesta.write( "<option value=\"cantidad\">Cantidad mayor a: </option>" );
			respuesta.write( "</select></td>" );
			respuesta.write( "<td align=\"center\"><input name=\"filtro\" value=\"Ingrese un nombre\" type=\"text\"></td>" );
			respuesta.write( "<td align=\"center\"><input name=\"buscar\" value=\"Buscar\" type=\"submit\"></td>" );
    		respuesta.write( "</tr>" );
    		respuesta.write( "</table>" );
    		respuesta.write( "</form>" );
    		respuesta.write( "<hr>" );
			if(filtro != null){
				pedidos = AplicacionWeb.getInstancia().darPedidos(filtroPedido, filtroNombreCliente, filtroLogin, filtroProducto, filtroMaterial, filtroCantidad);
				respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + pedidos.size() + " pedidos en total:</h4>" );
				if (!pedidos.isEmpty())
				{
					respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
			        for (Pedido ped : pedidos) {
			        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
			        	respuesta.write( "<tr><td><h3>Id Pedido: " + ped.getId() + " -  Cliente: " + ped.getNombreCliente() + " (" + ped.getIdCliente() + ")</h3></td></tr>" );
				        respuesta.write( "<tr><td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\"></td>" );
				        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Producto Pedido: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + ped.getIdProducto() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Unidades Pedidas: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + ped.getCantidad() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + (ped.getFechaPedido().toLocaleString()).substring(0, 5) + "2015</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Fecha Entrega: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + (ped.getFechaEntrega().toLocaleString()).substring(0, 5) + "2015</td></tr>" );
				        respuesta.write( "</table></td>" );
				        respuesta.write( "</tr>" );
				        respuesta.write( "<tr></tr>" );
				        respuesta.write( "</form>" );
			        }
			        respuesta.write( "</table>" );
				}
				else{
					error(respuesta, "No hay pedidos registrados en ProdAndes.", "admin");
				}
			}
		}
		catch (Exception e){
			error(respuesta, "No hay pedidos registrados en ProdAndes.", "admin");
			e.printStackTrace();
		}
	}
	
	public void darEstaciones (HttpServletRequest request, PrintWriter respuesta){
		ArrayList<Estacion> estaciones = new ArrayList<Estacion>();
		try
		{
			estaciones = AplicacionWeb.getInstancia().darEstaciones();
			if (!estaciones.isEmpty()){
				respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + estaciones.size() + " estaciones en total:</h4>" );
        		respuesta.write( "<hr>" );
				respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=20%>" );
		        for (Estacion estacion: estaciones) {
		        	String activada = "Desactivada";
		        	if( estacion.isActivada() )
		        		activada = "Activada";
		        	respuesta.write( "<form method=\"POST\" action=\"eliminar.htm\">" );
		        	respuesta.write( "<tr><td><h3>Estacion: " + estacion.getId() + " -  Tipo: " + estacion.getTipo() + "</h3></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Estacion: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + estacion.getId() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + estacion.getNombre() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Tipo: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + estacion.getTipo() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Estado: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + activada + "</td></tr>" );
			        if (activada.equals("Activada"))
			        	respuesta.write( "<tr><input value=\"" + estacion.getId() + "\" name=\"idEstacion\" type=\"hidden\"><td align=\"right\"><input value=\"desactivarEstacion\" name=\"criterio\" type=\"hidden\"><input value=\"Desactivar Estacion\" name=\"eliEsta\" type=\"submit\"></td></tr>");
			        respuesta.write( "<tr></tr>" );
			        respuesta.write( "</form>" );
		        }
		        respuesta.write( "</table>" );
			}
			else
				error(respuesta, "No hay estaciones registradas en ProdAndes.", "admin");	
		}
		catch (Exception e){
			error(respuesta, "No hay estaciones registradas en ProdAndes.", "admin");
			e.printStackTrace();
		}
	}
	
	public void darMateriales(HttpServletRequest request, PrintWriter respuesta){
		String tipo = request.getParameter("tipo");
		String filtro = request.getParameter("filtro");
		ArrayList <MateriaPrima> materiasPrimas = new ArrayList<MateriaPrima>();
		ArrayList<Componente> componentes = new ArrayList<Componente>();
		
		try{
    		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\"><input name=\"criterio\" value=\"darMaterial\" type=\"hidden\">" );
    		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
    		respuesta.write( "<tr>" );
    		respuesta.write( "<td><h4 align=\"center\">Filtrar por: </h4></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"tipo\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"materiaPrima\">Materia Prima</option>" );
    		respuesta.write( "<option value=\"componente\">Componente</option>" );
			respuesta.write( "</select></td>" );
			respuesta.write( "<td align=\"center\"><input name=\"filtro\" value=\"Ingrese un nombre\" type=\"text\"></td>" );
			respuesta.write( "<td align=\"center\"><input name=\"buscar\" value=\"Buscar\" type=\"submit\"></td>" );
    		respuesta.write( "</tr>" );
    		respuesta.write( "</table>" );
    		respuesta.write( "</form>" );
    		respuesta.write( "<hr>" );
			
			if (filtro != null){
				
				if (tipo != null && tipo.equals("materiaPrima")){
					materiasPrimas = AplicacionWeb.getInstancia().darMateriasPrimas(filtro);
				}
				else if (tipo != null && tipo.equals("componente")){
					componentes = AplicacionWeb.getInstancia().darComponentes(filtro);
				}
				else if (tipo == null){
					materiasPrimas = AplicacionWeb.getInstancia().darMateriasPrimas(filtro);
					componentes = AplicacionWeb.getInstancia().darComponentes(filtro);
					}
				if ((componentes.size() + materiasPrimas.size()) != 0){
					respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + (materiasPrimas.size() + componentes.size()) + " materiales en total.</h4>" );
					respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
					if (!materiasPrimas.isEmpty()){
						respuesta.write( "<tr><td><h3 align=\"center\">Materias Primas (" + materiasPrimas.size() + ") </h3></td></tr>" );
						for (MateriaPrima materia : materiasPrimas) {
				        	respuesta.write( "<tr><td><h4>Materia Prima: " + materia.getId() + "</h4></td></tr>" );
				        	respuesta.write( "<tr><td align=\"left\"><img alt=\"material\" src=\"imagenes/material.jpg\" name=\"material\"></td>" );
				        	respuesta.write( "<td align=\"right\"><table align=\"center\" width=25%><tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label1\" style=\"border: none;\" type=\"text\"></h4></td><td align=\"right\">" + materia.getId() + "</td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Cantidad: \" name=\"label2\" style=\"border: none;\" type=\"text\"></h4></td><td align=\"right\">" + materia.getCantidadInicial() + " " + materia.getUnidadMedida().toLowerCase() + "</td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Dia Ultima Aparicion: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + materia.getDia() + "/" + materia.getMes() + "/2015</td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Cantidad Pedidos Asociados: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + materia.getPedidos().size() + "</td></tr>" );
					        respuesta.write( "<tr><td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
					        for (String idPedido: materia.getPedidos()) {
					        	respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Id Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h5></td><td align=\"right\" size=\"\">" + idPedido + "</td></tr>" );
							}
					        respuesta.write( "</table></td></tr></table></td><td></td></tr>" );
					        respuesta.write( "<tr></tr>" );
						}
					}
					if (!componentes.isEmpty()){
						respuesta.write( "<tr><td><h3 align=\"center\">Componentes (" + componentes.size() + ") </h3></td></tr>" );
						for (Componente componente: componentes) {
				        	respuesta.write( "<tr><td><h4>Componente: " + componente.getId() + "</h4></td></tr>" );
				        	respuesta.write( "<tr><td align=\"left\"><img alt=\"material\" src=\"imagenes/material.jpg\" name=\"material\"></td>" );
				        	respuesta.write( "<td align=\"right\"><table align=\"center\" width=25%><tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label1\" style=\"border: none;\" type=\"text\"></h4></td><td align=\"right\">" + componente.getId() + "</td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Cantidad: \" name=\"label2\" style=\"border: none;\" type=\"text\"></h4></td><td align=\"right\">" + componente.getCantidadInicial() + " " + componente.getUnidadMedida().toLowerCase() + "</td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Dia Ultima Aparicion: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + componente.getDia() + "/" + componente.getMes() + "/2015</td></tr>" );
					        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Cantidad Pedidos Asociados: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + componente.getPedidos().size() + "</td></tr>" );
					        respuesta.write( "<tr><td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
					        for (String idPedido: componente.getPedidos()) {
					        	respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Id Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h5></td><td align=\"right\" size=\"\">" + idPedido + "</td></tr>" );
							}
					        respuesta.write( "</table></td></tr></table></td><td></td></tr>" );
					        respuesta.write( "<tr></tr>" );
						}
					}
					respuesta.write( "</table>" );
						
				}
				else{
					error(respuesta, "No hay material con ese nombre", "admin");
				}
			}
		}
		catch (Exception e){
			error(respuesta, "Hubo un error, por favor intentelo mas tarde.", "admin");
			e.printStackTrace();
		}
	}
	
	public void darProductos (HttpServletRequest request, PrintWriter respuesta){
		ArrayList<Producto> productos = new ArrayList<Producto>();
		try{
			productos = AplicacionWeb.getInstancia().darCantidadProductos(100);
			
			if (!productos.isEmpty()){
				respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + productos.size() + " productos en total.</h4>" );
				respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=80%>" );
				for (int i = 0; i < productos.size(); i++) {
					respuesta.write( "<tr>" );
					respuesta.write( "<td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\" value=" + productos.get(i).getNombre() + "></td>" );
					respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=20%>" );
		        	respuesta.write( "<tr><td><h3>Producto: " + productos.get(i).getId() + "</h3></td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Producto: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + productos.get(i).getId() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + productos.get(i).getNombre() + "</td></tr>" );
			        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Precio: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + productos.get(i).getPrecio() + "</td></tr>" );
			        respuesta.write( "</table>" );
			        respuesta.write( "</td>" );
					
					try
					{
						respuesta.write( "<td><img alt=\"Producto\" src=\"imagenes/producto.jpg\" name=\"producto\" value=" + productos.get(i+1).getNombre() + "></td>" );
						respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=20%>" );
			        	respuesta.write( "<tr><td><h3>Producto: " + productos.get(i+1).getId() + "</h3></td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Id Producto: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + productos.get(i+1).getId() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Nombre: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + productos.get(i+1).getNombre() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Precio: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + productos.get(i+1).getPrecio() + "</td></tr>" );
				        respuesta.write( "</table>" );
				        respuesta.write( "</td>" );
					}
					catch(Exception e2){	
					}
					respuesta.write( "</tr>" );
					i+=1;
				}
				respuesta.write( "</table>" );
			}
			else
				error(respuesta, "No hay productos registrados en ProdAndes.", "admin");
		}
		catch(Exception e){
			error(respuesta, "Hubo un error, por favor int�ntalo de nuevo.", "admin");
		}
	}
	
	public void darEtapas (HttpServletRequest request, PrintWriter respuesta){
		System.out.println("Dar Etapas, coincidir");
		String filtro = request.getParameter("filtro");
		String atributo = request.getParameter("atributo");
		String diaMenor = request.getParameter("diaMenor");
		String mesMenor = request.getParameter("mesMenor");
		String anioMenor = request.getParameter("anioMenor");
		String diaMayor = request.getParameter("diaMayor");
		String mesMayor = request.getParameter("mesMayor");
		String anioMayor = request.getParameter("anioMayor");
		Date fechaInicial = null;
		Date fechaFinal = null;
		String condicion = VERDADERO;
		Collection<Etapa> etapas = new ArrayList<Etapa>();
		try
		{
			if (filtro != null)
			{
				if (atributo.equals("materiaPrima"))
				{
					condicion = "UPPER(idMateriaPrima) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("componente"))
				{
					condicion = "UPPER(idComponente) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("pedido"))
				{
					condicion = "idPedido = '" + filtro + "'";
				}
			}
			if(diaMenor != null && diaMayor != null && mesMenor != null && mesMayor != null && anioMenor != null && anioMayor != null){
				fechaInicial = new Date(Integer.parseInt(anioMenor), Integer.parseInt(mesMenor), Integer.parseInt(diaMenor));
				fechaFinal = new Date(Integer.parseInt(anioMayor), Integer.parseInt(mesMayor), Integer.parseInt(diaMayor));
				if	(!condicion.equals(VERDADERO) && fechaInicial != null && fechaFinal != null)
					condicion += " AND dia >= " + diaMenor + " AND mes >= " + mesMenor + " AND dia <= " + diaMayor + " AND mes <= " + mesMayor + "";
				else if (condicion.equals(VERDADERO) && fechaInicial != null && fechaFinal != null)
					condicion = "dia >= " + diaMenor + " AND mes >= " + mesMenor + " AND dia <= " + diaMayor + " AND mes <= " + mesMayor + "";
			}
			
        		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
        		respuesta.write( "<tr>" );
        		respuesta.write( "<td><h4 align=\"center\">Filtrar por: </h4></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"atributo\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"materiaPrima\">Materia Prima</option>" );
        		respuesta.write( "<option value=\"componente\">Componente</option>" );
        		respuesta.write( "<option value=\"pedido\">Pedido</option>" );
    			respuesta.write( "</select></td>" );
    			respuesta.write( "<td align=\"center\"><input name=\"filtro\" value=\"Ingrese un nombre\" type=\"text\"></td>" );
        		respuesta.write( "</tr>" );
        		respuesta.write( "</table>" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
        		respuesta.write( "<tr>" );
        		respuesta.write( "<td><h4 align=\"center\">Desde: </h4></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"diaMenor\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"1\">1</option>" );
        		respuesta.write( "<option value=\"2\">2</option>" );
        		respuesta.write( "<option value=\"3\">3</option>" );
        		respuesta.write( "<option value=\"4\">4</option>" );
        		respuesta.write( "<option value=\"5\">5</option>" );
        		respuesta.write( "<option value=\"6\">6</option>" );
        		respuesta.write( "<option value=\"7\">7</option>" );
        		respuesta.write( "<option value=\"8\">8</option>" );
        		respuesta.write( "<option value=\"9\">9</option>" );
        		respuesta.write( "<option value=\"10\">10</option>" );
        		respuesta.write( "<option value=\"11\">11</option>" );
        		respuesta.write( "<option value=\"12\">12</option>" );
        		respuesta.write( "<option value=\"13\">13</option>" );
        		respuesta.write( "<option value=\"14\">14</option>" );
        		respuesta.write( "<option value=\"15\">15</option>" );
        		respuesta.write( "<option value=\"16\">16</option>" );
        		respuesta.write( "<option value=\"17\">17</option>" );
        		respuesta.write( "<option value=\"18\">18</option>" );
        		respuesta.write( "<option value=\"19\">19</option>" );
        		respuesta.write( "<option value=\"20\">20</option>" );
        		respuesta.write( "<option value=\"21\">21</option>" );
        		respuesta.write( "<option value=\"22\">22</option>" );
        		respuesta.write( "<option value=\"23\">23</option>" );
        		respuesta.write( "<option value=\"24\">24</option>" );
        		respuesta.write( "<option value=\"25\">25</option>" );
        		respuesta.write( "<option value=\"26\">26</option>" );
        		respuesta.write( "<option value=\"27\">27</option>" );
        		respuesta.write( "<option value=\"28\">28</option>" );
        		respuesta.write( "<option value=\"29\">29</option>" );
        		respuesta.write( "<option value=\"30\">30</option>" );
        		respuesta.write( "<option value=\"31\">31</option>" );
    			respuesta.write( "</select></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"mesMenor\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"1\">enero</option>" );
        		respuesta.write( "<option value=\"2\">febrero</option>" );
        		respuesta.write( "<option value=\"3\">marzo</option>" );
        		respuesta.write( "<option value=\"4\">abril</option>" );
        		respuesta.write( "<option value=\"5\">mayo</option>" );
        		respuesta.write( "<option value=\"6\">junio</option>" );
        		respuesta.write( "<option value=\"7\">julio</option>" );
        		respuesta.write( "<option value=\"8\">agosto</option>" );
        		respuesta.write( "<option value=\"9\">septiembre</option>" );
        		respuesta.write( "<option value=\"10\">octubre</option>" );
        		respuesta.write( "<option value=\"11\">noviembre</option>" );
        		respuesta.write( "<option value=\"12\">diciembre</option>" );
    			respuesta.write( "</select></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"anioMenor\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"1\">2004</option>" );
        		respuesta.write( "<option value=\"2\">2005</option>" );
        		respuesta.write( "<option value=\"3\">2006</option>" );
        		respuesta.write( "<option value=\"4\">2007</option>" );
        		respuesta.write( "<option value=\"5\">2008</option>" );
        		respuesta.write( "<option value=\"6\">2009</option>" );
        		respuesta.write( "<option value=\"7\">2010</option>" );
        		respuesta.write( "<option value=\"8\">2011</option>" );
        		respuesta.write( "<option value=\"9\">2012</option>" );
        		respuesta.write( "<option value=\"10\">2013</option>" );
        		respuesta.write( "<option value=\"11\">2014</option>" );
        		respuesta.write( "<option value=\"12\">2015</option>" );
    			respuesta.write( "</select></td>" );
        		respuesta.write( "</tr>" );
        		respuesta.write( "<tr>" );
        		respuesta.write( "<td><h4 align=\"center\">Hasta: </h4></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"diaMayor\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"1\">1</option>" );
        		respuesta.write( "<option value=\"2\">2</option>" );
        		respuesta.write( "<option value=\"3\">3</option>" );
        		respuesta.write( "<option value=\"4\">4</option>" );
        		respuesta.write( "<option value=\"5\">5</option>" );
        		respuesta.write( "<option value=\"6\">6</option>" );
        		respuesta.write( "<option value=\"7\">7</option>" );
        		respuesta.write( "<option value=\"8\">8</option>" );
        		respuesta.write( "<option value=\"9\">9</option>" );
        		respuesta.write( "<option value=\"10\">10</option>" );
        		respuesta.write( "<option value=\"11\">11</option>" );
        		respuesta.write( "<option value=\"12\">12</option>" );
        		respuesta.write( "<option value=\"13\">13</option>" );
        		respuesta.write( "<option value=\"14\">14</option>" );
        		respuesta.write( "<option value=\"15\">15</option>" );
        		respuesta.write( "<option value=\"16\">16</option>" );
        		respuesta.write( "<option value=\"17\">17</option>" );
        		respuesta.write( "<option value=\"18\">18</option>" );
        		respuesta.write( "<option value=\"19\">19</option>" );
        		respuesta.write( "<option value=\"20\">20</option>" );
        		respuesta.write( "<option value=\"21\">21</option>" );
        		respuesta.write( "<option value=\"22\">22</option>" );
        		respuesta.write( "<option value=\"23\">23</option>" );
        		respuesta.write( "<option value=\"24\">24</option>" );
        		respuesta.write( "<option value=\"25\">25</option>" );
        		respuesta.write( "<option value=\"26\">26</option>" );
        		respuesta.write( "<option value=\"27\">27</option>" );
        		respuesta.write( "<option value=\"28\">28</option>" );
        		respuesta.write( "<option value=\"29\">29</option>" );
        		respuesta.write( "<option value=\"30\">30</option>" );
        		respuesta.write( "<option value=\"31\">31</option>" );
    			respuesta.write( "</select></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"mesMayor\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"1\">enero</option>" );
        		respuesta.write( "<option value=\"2\">febrero</option>" );
        		respuesta.write( "<option value=\"3\">marzo</option>" );
        		respuesta.write( "<option value=\"4\">abril</option>" );
        		respuesta.write( "<option value=\"5\">mayo</option>" );
        		respuesta.write( "<option value=\"6\">junio</option>" );
        		respuesta.write( "<option value=\"7\">julio</option>" );
        		respuesta.write( "<option value=\"8\">agosto</option>" );
        		respuesta.write( "<option value=\"9\">septiembre</option>" );
        		respuesta.write( "<option value=\"10\">octubre</option>" );
        		respuesta.write( "<option value=\"11\">noviembre</option>" );
        		respuesta.write( "<option value=\"12\">diciembre</option>" );
    			respuesta.write( "</select></td>" );
        		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"anioMayor\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"1\">2004</option>" );
        		respuesta.write( "<option value=\"2\">2005</option>" );
        		respuesta.write( "<option value=\"3\">2006</option>" );
        		respuesta.write( "<option value=\"4\">2007</option>" );
        		respuesta.write( "<option value=\"5\">2008</option>" );
        		respuesta.write( "<option value=\"6\">2009</option>" );
        		respuesta.write( "<option value=\"7\">2010</option>" );
        		respuesta.write( "<option value=\"8\">2011</option>" );
        		respuesta.write( "<option value=\"9\">2012</option>" );
        		respuesta.write( "<option value=\"10\">2013</option>" );
        		respuesta.write( "<option value=\"11\">2014</option>" );
        		respuesta.write( "<option value=\"12\">2015</option>" );
    			respuesta.write( "</select></td>" );
        		respuesta.write( "</table>" );
        		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
        		respuesta.write( "<tr><td align=\"center\"><select style=\"font-size: 15px;\" name=\"criterio\" size=\"1\"  class=\"normal\" \">" );
        		respuesta.write( "<option value=\"coincidirEtapas\">Coincidir</option>" );
        		respuesta.write( "<option value=\"noCoincidirEtapas\">No Coincidir</option>" );
    			respuesta.write( "</select></td>" );
    			respuesta.write( "<td align=\"center\"><input name=\"buscar\" value=\"Buscar\" type=\"submit\"></td></tr>" );
    			respuesta.write( "</table>");
        		respuesta.write( "</form>" );
        		respuesta.write( "<hr>" );
				if (!condicion.equals(VERDADERO)){
				etapas = AplicacionWeb.getInstancia().darEtapas(condicion);
				respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + etapas.size() + " etapas en total:</h4>" );
				if (!etapas.isEmpty())
				{
					respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
			        for (Etapa etapa : etapas) {
			        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
			        	respuesta.write( "<tr><td><h3>Etapa: " + etapa.getNombre() + " -  Id: " + etapa.getId() + "</h3></td></tr>" );
				        respuesta.write( "<tr>" );
				        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Numero Secuencia: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + etapa.getNumeroSecuencia() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Cantidad Pedidos Asociados: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + etapa.getIdPedido().size() + "</td></tr>" );
				        respuesta.write( "<tr><td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
				        for (String idPedido: etapa.getIdPedido()) {
				        	respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Id Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h5></td><td align=\"right\" size=\"\">" + idPedido + "</td></tr>" );
						}
				        respuesta.write( "</table></td><td></td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Dia ejecutada: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + etapa.getDia() + "/" + etapa.getMes() + "/2015</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Materia Prima usada: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + etapa.getIdMateriaPrima() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Componente usado: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + etapa.getIdComponente() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Duracion (en dias): \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + etapa.getDuracion() + "</td></tr>" );
				        respuesta.write( "</table></td>" );
				        respuesta.write( "</tr>" );
				        respuesta.write( "<tr></tr>" );
				        respuesta.write( "</form>" );
			        }
			        respuesta.write( "</table>" );
				}
				else{
					error(respuesta, "No hay etapas de produccion registrados en ProdAndes.", "admin");
				}
			}
		}	
		catch (Exception e){
			error(respuesta, "No hay etapas de produccion registrados en ProdAndes.", "admin");
			e.printStackTrace();
		}
	}
	
	public void darEtapasNo (HttpServletRequest request, PrintWriter respuesta){
		System.out.println("Dar Etapas, no coincidir");
		String filtro = request.getParameter("filtro");
		String atributo = request.getParameter("atributo");
		String diaMenor = request.getParameter("diaMenor");
		String mesMenor = request.getParameter("mesMenor");
		String anioMenor = request.getParameter("anioMenor");
		String diaMayor = request.getParameter("diaMayor");
		String mesMayor = request.getParameter("mesMayor");
		String anioMayor = request.getParameter("anioMayor");
		String condicionFecha = FALSO;
		Date fechaInicial = null;
		Date fechaFinal = null;
		if (diaMenor != null && diaMayor != null && mesMenor != null && mesMayor != null && anioMenor != null && anioMayor != null){
			fechaInicial = new Date(Integer.parseInt(anioMenor), Integer.parseInt(mesMenor), Integer.parseInt(diaMenor));
			fechaFinal = new Date(Integer.parseInt(anioMayor), Integer.parseInt(mesMayor), Integer.parseInt(diaMayor));
		}
		String condicion = VERDADERO;
		Collection<Etapa> etapas = new ArrayList<Etapa>();
		try
		{
			if (filtro != null)
			{
				if (atributo.equals("materiaPrima"))
				{
					condicion = "UPPER(idMateriaPrima) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("componente"))
				{
					condicion = "UPPER(idComponente) LIKE UPPER('%" + filtro + "%')";
				}
				else if (atributo.equals("pedido"))
				{
					condicion = "idPedido = '" + filtro + "'";
				}
			}
			
			if (fechaInicial != null && fechaFinal != null){
				condicionFecha = "dia >= " + diaMenor + " AND mes >= " + mesMenor + " AND dia <= " + diaMayor + " AND mes <= " + mesMayor + "";
			}
		
    		respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
    		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
    		respuesta.write( "<tr>" );
    		respuesta.write( "<td><h4 align=\"center\">Filtrar por: </h4></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"atributo\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"materiaPrima\">Materia Prima</option>" );
    		respuesta.write( "<option value=\"componente\">Componente</option>" );
    		respuesta.write( "<option value=\"pedido\">Pedido</option>" );
			respuesta.write( "</select></td>" );
			respuesta.write( "<td align=\"center\"><input name=\"filtro\" value=\"Ingrese un nombre\" type=\"text\"></td>" );
    		respuesta.write( "</tr>" );
    		respuesta.write( "</table>" );
    		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
    		respuesta.write( "<tr>" );
    		respuesta.write( "<td><h4 align=\"center\">Desde: </h4></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"diaMenor\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"1\">1</option>" );
    		respuesta.write( "<option value=\"2\">2</option>" );
    		respuesta.write( "<option value=\"3\">3</option>" );
    		respuesta.write( "<option value=\"4\">4</option>" );
    		respuesta.write( "<option value=\"5\">5</option>" );
    		respuesta.write( "<option value=\"6\">6</option>" );
    		respuesta.write( "<option value=\"7\">7</option>" );
    		respuesta.write( "<option value=\"8\">8</option>" );
    		respuesta.write( "<option value=\"9\">9</option>" );
    		respuesta.write( "<option value=\"10\">10</option>" );
    		respuesta.write( "<option value=\"11\">11</option>" );
    		respuesta.write( "<option value=\"12\">12</option>" );
    		respuesta.write( "<option value=\"13\">13</option>" );
    		respuesta.write( "<option value=\"14\">14</option>" );
    		respuesta.write( "<option value=\"15\">15</option>" );
    		respuesta.write( "<option value=\"16\">16</option>" );
    		respuesta.write( "<option value=\"17\">17</option>" );
    		respuesta.write( "<option value=\"18\">18</option>" );
    		respuesta.write( "<option value=\"19\">19</option>" );
    		respuesta.write( "<option value=\"20\">20</option>" );
    		respuesta.write( "<option value=\"21\">21</option>" );
    		respuesta.write( "<option value=\"22\">22</option>" );
    		respuesta.write( "<option value=\"23\">23</option>" );
    		respuesta.write( "<option value=\"24\">24</option>" );
    		respuesta.write( "<option value=\"25\">25</option>" );
    		respuesta.write( "<option value=\"26\">26</option>" );
    		respuesta.write( "<option value=\"27\">27</option>" );
    		respuesta.write( "<option value=\"28\">28</option>" );
    		respuesta.write( "<option value=\"29\">29</option>" );
    		respuesta.write( "<option value=\"30\">30</option>" );
    		respuesta.write( "<option value=\"31\">31</option>" );
			respuesta.write( "</select></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"mesMenor\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"1\">enero</option>" );
    		respuesta.write( "<option value=\"2\">febrero</option>" );
    		respuesta.write( "<option value=\"3\">marzo</option>" );
    		respuesta.write( "<option value=\"4\">abril</option>" );
    		respuesta.write( "<option value=\"5\">mayo</option>" );
    		respuesta.write( "<option value=\"6\">junio</option>" );
    		respuesta.write( "<option value=\"7\">julio</option>" );
    		respuesta.write( "<option value=\"8\">agosto</option>" );
    		respuesta.write( "<option value=\"9\">septiembre</option>" );
    		respuesta.write( "<option value=\"10\">octubre</option>" );
    		respuesta.write( "<option value=\"11\">noviembre</option>" );
    		respuesta.write( "<option value=\"12\">diciembre</option>" );
			respuesta.write( "</select></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"anioMenor\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"1\">2004</option>" );
    		respuesta.write( "<option value=\"2\">2005</option>" );
    		respuesta.write( "<option value=\"3\">2006</option>" );
    		respuesta.write( "<option value=\"4\">2007</option>" );
    		respuesta.write( "<option value=\"5\">2008</option>" );
    		respuesta.write( "<option value=\"6\">2009</option>" );
    		respuesta.write( "<option value=\"7\">2010</option>" );
    		respuesta.write( "<option value=\"8\">2011</option>" );
    		respuesta.write( "<option value=\"9\">2012</option>" );
    		respuesta.write( "<option value=\"10\">2013</option>" );
    		respuesta.write( "<option value=\"11\">2014</option>" );
    		respuesta.write( "<option value=\"12\">2015</option>" );
			respuesta.write( "</select></td>" );
    		respuesta.write( "</tr>" );
    		respuesta.write( "<tr>" );
    		respuesta.write( "<td><h4 align=\"center\">Hasta: </h4></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"diaMayor\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"1\">1</option>" );
    		respuesta.write( "<option value=\"2\">2</option>" );
    		respuesta.write( "<option value=\"3\">3</option>" );
    		respuesta.write( "<option value=\"4\">4</option>" );
    		respuesta.write( "<option value=\"5\">5</option>" );
    		respuesta.write( "<option value=\"6\">6</option>" );
    		respuesta.write( "<option value=\"7\">7</option>" );
    		respuesta.write( "<option value=\"8\">8</option>" );
    		respuesta.write( "<option value=\"9\">9</option>" );
    		respuesta.write( "<option value=\"10\">10</option>" );
    		respuesta.write( "<option value=\"11\">11</option>" );
    		respuesta.write( "<option value=\"12\">12</option>" );
    		respuesta.write( "<option value=\"13\">13</option>" );
    		respuesta.write( "<option value=\"14\">14</option>" );
    		respuesta.write( "<option value=\"15\">15</option>" );
    		respuesta.write( "<option value=\"16\">16</option>" );
    		respuesta.write( "<option value=\"17\">17</option>" );
    		respuesta.write( "<option value=\"18\">18</option>" );
    		respuesta.write( "<option value=\"19\">19</option>" );
    		respuesta.write( "<option value=\"20\">20</option>" );
    		respuesta.write( "<option value=\"21\">21</option>" );
    		respuesta.write( "<option value=\"22\">22</option>" );
    		respuesta.write( "<option value=\"23\">23</option>" );
    		respuesta.write( "<option value=\"24\">24</option>" );
    		respuesta.write( "<option value=\"25\">25</option>" );
    		respuesta.write( "<option value=\"26\">26</option>" );
    		respuesta.write( "<option value=\"27\">27</option>" );
    		respuesta.write( "<option value=\"28\">28</option>" );
    		respuesta.write( "<option value=\"29\">29</option>" );
    		respuesta.write( "<option value=\"30\">30</option>" );
    		respuesta.write( "<option value=\"31\">31</option>" );
			respuesta.write( "</select></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"mesMayor\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"1\">enero</option>" );
    		respuesta.write( "<option value=\"2\">febrero</option>" );
    		respuesta.write( "<option value=\"3\">marzo</option>" );
    		respuesta.write( "<option value=\"4\">abril</option>" );
    		respuesta.write( "<option value=\"5\">mayo</option>" );
    		respuesta.write( "<option value=\"6\">junio</option>" );
    		respuesta.write( "<option value=\"7\">julio</option>" );
    		respuesta.write( "<option value=\"8\">agosto</option>" );
    		respuesta.write( "<option value=\"9\">septiembre</option>" );
    		respuesta.write( "<option value=\"10\">octubre</option>" );
    		respuesta.write( "<option value=\"11\">noviembre</option>" );
    		respuesta.write( "<option value=\"12\">diciembre</option>" );
			respuesta.write( "</select></td>" );
    		respuesta.write( "<td align=\"center\"><select style=\"font-size: 15px;\" name=\"anioMayor\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"1\">2004</option>" );
    		respuesta.write( "<option value=\"2\">2005</option>" );
    		respuesta.write( "<option value=\"3\">2006</option>" );
    		respuesta.write( "<option value=\"4\">2007</option>" );
    		respuesta.write( "<option value=\"5\">2008</option>" );
    		respuesta.write( "<option value=\"6\">2009</option>" );
    		respuesta.write( "<option value=\"7\">2010</option>" );
    		respuesta.write( "<option value=\"8\">2011</option>" );
    		respuesta.write( "<option value=\"9\">2012</option>" );
    		respuesta.write( "<option value=\"10\">2013</option>" );
    		respuesta.write( "<option value=\"11\">2014</option>" );
    		respuesta.write( "<option value=\"12\">2015</option>" );
			respuesta.write( "</select></td>" );
    		respuesta.write( "</table>" );
    		respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
    		respuesta.write( "<tr><td align=\"center\"><select style=\"font-size: 15px;\" name=\"criterio\" size=\"1\"  class=\"normal\" \">" );
    		respuesta.write( "<option value=\"coincidirEtapas\">Coincidir</option>" );
    		respuesta.write( "<option value=\"noCoincidirEtapas\">No Coincidir</option>" );
			respuesta.write( "</select></td>" );
			respuesta.write( "<td align=\"center\"><input name=\"buscar\" value=\"Buscar\" type=\"submit\"></td></tr>" );
			respuesta.write( "</table>");
    		respuesta.write( "</form>" );
    		respuesta.write( "<hr>" );
			if (!condicion.equals(VERDADERO)){
				etapas = AplicacionWeb.getInstancia().darEtapasNo(condicion, condicionFecha);
				respuesta.write( "<h4 align=\"center\">ProdAndes tiene registrados " + etapas.size() + " etapas en total:</h4>" );
				if (!etapas.isEmpty())
				{
					respuesta.write( "<table align=\"center\" bgcolor=\"#ecf0f1\" width=50%>" );
			        for (Etapa etapa : etapas) {
			        	respuesta.write( "<form method=\"POST\" action=\"resultadoBusqueda.htm\">" );
			        	respuesta.write( "<tr><td><h3>Etapa: " + etapa.getNombre() + " -  Id: " + etapa.getId() + "</h3></td></tr>" );
				        respuesta.write( "<tr>" );
				        respuesta.write( "<td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Numero Secuencia: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + etapa.getNumeroSecuencia() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Cantidad Pedidos Asociados: \" name=\"label1\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + etapa.getIdPedido().size() + "</td></tr>" );
				        respuesta.write( "<tr><td><table align=\"center\" bgcolor=\"#ecf0f1\" width=30%>" );
				        for (String idPedido: etapa.getIdPedido()) {
				        	respuesta.write( "<tr><td align=\"left\"><h5><input value=\"Id Pedido: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h5></td><td align=\"right\" size=\"\">" + idPedido + "</td></tr>" );
						}
				        respuesta.write( "</table></td><td></td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Dia ejecutada: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + etapa.getDia() + "/" + etapa.getMes() + "/2015</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Materia Prima usada: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\">" + etapa.getIdMateriaPrima() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Componente usado: \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + etapa.getIdComponente() + "</td></tr>" );
				        respuesta.write( "<tr><td align=\"left\"><h4><input value=\"Duracion (en dias): \" name=\"label2\" style=\"border: none;\" type=\"text\"\"></h4></td><td align=\"right\" size=\"\">" + etapa.getDuracion() + "</td></tr>" );
				        respuesta.write( "</table></td>" );
				        respuesta.write( "</tr>" );
				        respuesta.write( "<tr></tr>" );
				        respuesta.write( "</form>" );
			        }
			        respuesta.write( "</table>" );
				}
				else{
					error(respuesta, "No hay etapas de produccion registrados en ProdAndes.", "admin");
				}
			}
		}
		catch (Exception e){
			error(respuesta, "No hay etapas de produccion registrados en ProdAndes.", "admin");
			e.printStackTrace();
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
			e1.printStackTrace();
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
