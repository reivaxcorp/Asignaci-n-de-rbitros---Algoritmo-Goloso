
public class  Partido {

	private String local;
	private String visitante;
	private int arbitro;

	public Partido(String local, String visitante, int arbitro) {
		this.local = local;
		this.visitante = visitante;
		this.arbitro = arbitro;
	}

	
	public String getLocal() {
		return local;
	}

	public String getVisitante() {
		return visitante;
	}


	public int getArbitro() {
		return arbitro;
	}


	public void setArbitro(int arbitro) {
		this.arbitro = arbitro;
	}

}
