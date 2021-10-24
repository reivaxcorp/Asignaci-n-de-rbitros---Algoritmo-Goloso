
public class  Partido {

	private String local;
	private String visitante;

	public Partido(String local, String visitante) {
		this.local = local;
		this.visitante = visitante;
	}

	
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}



	public String getVisitante() {
		return visitante;
	}

	public void setVisitante(String visitante) {
		this.visitante = visitante;
	}

}
