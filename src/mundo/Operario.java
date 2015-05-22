package mundo;

public class Operario {
	
	//--------------------------------------------------
	// ATRIBUTOS
	//--------------------------------------------------
	
	private String id;
	
	private String nombre;
	
	private boolean estaOcupado;
	
	private Estacion estacionProduccion;
	
	//--------------------------------------------------
	// CONSTRUCTOR
	//--------------------------------------------------

	public Operario(String pId, String pNombre, boolean pEstaOcupado, Estacion pEstacionProduccion) {
		this.id = pId;
		this.nombre = pNombre;
		this.estaOcupado = pEstaOcupado;
		this.estacionProduccion = pEstacionProduccion;
	}
	
	//--------------------------------------------------
	// MÉTODOS
	//--------------------------------------------------

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isEstaOcupado() {
		return estaOcupado;
	}

	public void setEstaOcupado(boolean estaOcupado) {
		this.estaOcupado = estaOcupado;
	}

	public Estacion getEstacionProduccion() {
		return estacionProduccion;
	}

	public void setEstacionProduccion(Estacion estacionProduccion) {
		this.estacionProduccion = estacionProduccion;
	}	

}
