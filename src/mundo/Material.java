package mundo;

import java.util.ArrayList;

public interface Material {
	
	public String getId();

	public String getUnidadMedida();

	public int getCantidadInicial();
	 
	public String getTipo();

	public int getDia();

	public int getMes();

	public void setId(String id);

	public void setUnidadMedida(String unidadMedida);

	public void setCantidadInicial(int cantidadInicial);

	public void setTipo(String tipo);

	public void setDia(int dia);

	public void setMes(int mes);
	
	public ArrayList<String> getPedidos();
	
	public void setPedidos(ArrayList<String> pedido);
	
}
