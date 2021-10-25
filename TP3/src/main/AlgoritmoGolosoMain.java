package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class AlgoritmoGolosoMain {
	
	private onActualizaUI onActualizaUI;
	private HashMap<Integer, ArrayList<Partido>> fechas;
	private Random rn;
	
	
	public AlgoritmoGolosoMain(onActualizaUI observadorUI) {
		this.onActualizaUI = observadorUI;
		this.rn = new Random();
		
	}
	
	public void generarAlgoritmoGoloso(HashMap<Integer, ArrayList<Partido>> fechas) {

		if (fechas != null && !fechas.isEmpty()) {

			HashMap<Integer, ArrayList<Partido>> pFechas = fechas;
			HashMap<String, ArrayList<Integer>> arbitros = crearListaArbitrosPorFecha(pFechas);

			for (int fecha = 0; fecha < pFechas.size(); fecha++) {
				for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {

					// la cantidad de partidos por fecha, son el numero de arbitros
					int arbitroRandom = rn.nextInt(arbitros.get(String.valueOf(fecha)).size()); // obtenemos un arbitro
																								// aleatorio de esa
																								// fecha
					int arbitro = arbitros.get(String.valueOf(fecha)).get(arbitroRandom); // el valor o arbitro
					fechas.get(fecha).get(partido).setArbitro(arbitro + 1); // colocamos un arbitro al partido
					arbitros.get(String.valueOf(fecha)).remove(arbitroRandom); // lo eliminamos para que no se repita en
																				// la misma fecha
				}
			}
			colocarDatosDeAlgoritmoGolosoUI(pFechas);
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
	}
	
	// un arbitro por partido
	@SuppressWarnings("unlikely-arg-type")
	public HashMap<String, ArrayList<Integer>> crearListaArbitrosPorFecha(HashMap<Integer, ArrayList<Partido>> fechas) {
		HashMap<String, ArrayList<Integer>> arbitros
							                   = new HashMap<String, ArrayList<Integer>>();

		for (int fecha = 0; fecha < fechas.size(); fecha++) {
			arbitros.put(String.valueOf(fecha), new ArrayList<Integer>());	
				for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {
					arbitros.get(String.valueOf(fecha)).add(partido);
				}
		}
	  return arbitros;		 
	}
	
	public void colocarDatosDeArchivoUI(HashMap<Integer, ArrayList<Partido>> fechas) {
		if(fechas != null && !fechas.isEmpty()) {
			for(int fecha = 0; fecha < fechas.size(); fecha ++) {
			  onActualizaUI.colocarfechasDeArchivoUI(fecha, fechas.get(fecha));
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
	}
	
	public void colocarDatosDeAlgoritmoGolosoUI(HashMap<Integer, ArrayList<Partido>> fechas) {
		if(fechas != null && !fechas.isEmpty()) {
			for(int fecha = 0; fecha < fechas.size(); fecha ++) {
			  onActualizaUI.colocarfechasAlgoritmoGoloso(fecha, fechas.get(fecha));
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<Integer, ArrayList<Partido>> getFechas() {
		return (HashMap<Integer, ArrayList<Partido>>) fechas.clone();
	}

	public void setFechas(HashMap<Integer, ArrayList<Partido>> fechas) {
		this.fechas = fechas;
	}

}
