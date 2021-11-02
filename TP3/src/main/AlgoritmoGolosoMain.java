package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.swing.SwingWorker;

import org.junit.runners.Parameterized.Parameter;


public class AlgoritmoGolosoMain {
	
	private onActualizaUI onActualizaUI;
	private HashMap<Integer, ArrayList<Partido>> fechas; 
	private int mi = 4; // mi a la maaxima cantidad de partidos de i con un mismo ´arbitro
	private Random rn;
	private int temp = 0;
	
	public AlgoritmoGolosoMain(onActualizaUI observadorUI) {
		this.onActualizaUI = observadorUI;
		this.rn = new Random();
		
	}
	
	public HashMap<Integer, ArrayList<Partido>> generarAlgoritmoGoloso(HashMap<Integer, ArrayList<Partido>> fechas, int cantidadArbitros) {

		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		HashMap<Integer, ArrayList<Partido>> pFechas = fechas;

		if (fechas != null && !fechas.isEmpty()) {
				
			
		
			for (int fecha = 0; fecha < pFechas.size(); fecha++) {
				temp = 0;
				for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {
						
					

					
					asignarArbitro(pFechas, pFechas.get(fecha).get(partido), cantidadArbitros);
					
					/*
					// la cantidad de partidos por fecha, son el numero de arbitros
					int arbitroRandom = rn.nextInt(arbitros.get(String.valueOf(fecha)).size()); // obtenemos un arbitro
																								// aleatorio de esa
																								// fecha
					int arbitro = arbitros.get(String.valueOf(fecha)).get(arbitroRandom); // el valor o arbitro
					fechas.get(fecha).get(partido).setArbitro(arbitro + 1); // colocamos un arbitro al partido
					arbitros.get(String.valueOf(fecha)).remove(arbitroRandom); // lo eliminamos para que no se repita en
					*/														// la misma fecha
				}
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
		return pFechas;
	}
	
	// buscar todos los partidos en todas las fechas de los dos equipos del partido. OK
	public void asignarArbitro(HashMap<Integer, ArrayList<Partido>> pFechas, Partido mPartido, int cantArbitros) {
	
		ArrayList<Partido> equipoLocal = new ArrayList<Partido>(); // cant. veces equipo local
		ArrayList<Partido> equipoVisitante = new ArrayList<Partido>(); // cant. veces equipo visitante

		ArrayList<Partido> encuentros = new ArrayList<Partido>(); // cantidad de veces que juegan juntos en cada fecha

		
		for (int fecha = 0; fecha < pFechas.size(); fecha++) {
			for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {
				
				String visitante = pFechas.get(fecha).get(partido).getVisitante();
				String local = pFechas.get(fecha).get(partido).getLocal();

				
				if(mPartido.getVisitante().equals(visitante)) {
					equipoVisitante.add(pFechas.get(fecha).get(partido));
				}
				if(mPartido.getLocal().equals(local)) {
					equipoLocal.add(pFechas.get(fecha).get(partido));
				}
				
				if((mPartido.getLocal().equals(local) && mPartido.getVisitante().equals(visitante)) || 
				   (mPartido.getVisitante().equals(visitante) && mPartido.getLocal().equals(local)) ) {
					encuentros.add(pFechas.get(fecha).get(partido));
					//System.out.println(pFechas.get(fecha).get(partido));
				}
			}
		}
		
		// buscar la cantidad de veces que los arbitros aparecen en cada equipo . OK
		
		// key arbitro, value cantidad de veces. 
		HashMap<Integer, Integer> cantVecesArbitroEquipoLocal = 
				new HashMap<Integer, Integer>(); 
		
		// key arbitro, value cantidad de veces. 
		HashMap<Integer, Integer> cantVecesArbitroEquipoVisitante = 
				new HashMap<Integer, Integer>(); 
		
	
		for(int arbitro = 0; arbitro < cantArbitros; arbitro ++) {
				
			int cantVeces = 0;
			for (int local = 0; local < equipoLocal.size(); local++) {
					if(equipoLocal.get(local).getArbitro() == arbitro) {
						cantVeces ++;
						cantVecesArbitroEquipoLocal.put(arbitro, cantVeces);
					} else {
						//cantVecesArbitroEquipoLocal.put(arbitro, cantVeces);
					}
				}
		}
		
		 
		for(int arbitro = 0; arbitro < cantArbitros; arbitro ++) {
			
			int cantVeces = 0;
			for (int local = 0; local < equipoVisitante.size(); local++) {
					if(equipoVisitante.get(local).getArbitro() == arbitro) {
						cantVeces ++;
						cantVecesArbitroEquipoVisitante.put(arbitro, cantVeces);
					} else {
					//	cantVecesArbitroEquipoVisitante.put(arbitro, cantVeces);
					}
				}
		}
		//  fecha 1
		if(cantVecesArbitroEquipoVisitante.isEmpty() && cantVecesArbitroEquipoLocal.isEmpty()) {
			
			int arbitroRandom = rn.nextInt(cantArbitros);
			mPartido.setArbitro(arbitroRandom+1);
			
		} else {
		
		// buscar todos los arbitros de los dos equipos < mi partido. OK
		
	    HashMap<Integer, Integer> menoresLocalMi = 
	    				new HashMap<Integer, Integer>(); 
	    HashMap<Integer, Integer> menoresVisitanteMi = 
						new HashMap<Integer, Integer>(); 
	    
	    for(int arbitro: cantVecesArbitroEquipoLocal.keySet()) {
	    	if(cantVecesArbitroEquipoLocal.get(arbitro) < mi && 
	    			cantVecesArbitroEquipoLocal.get(arbitro) > 0) {
	    		menoresLocalMi.put(arbitro, cantVecesArbitroEquipoLocal.get(arbitro));
	    	}
	    }
	 
	    for(int arbitro: cantVecesArbitroEquipoVisitante.keySet()) {
	    	if(cantVecesArbitroEquipoVisitante.get(arbitro) < mi && 
	    			cantVecesArbitroEquipoVisitante.get(arbitro) > 0) {
	    		menoresVisitanteMi.put(arbitro, cantVecesArbitroEquipoVisitante.get(arbitro));
	    	}
	    }
		
		// buscar la cantidad de veces que se selecciono el mismo arbitro para el encuentro. OK
		
		 HashMap<Integer, Integer> cantArbitrosPorEncuentroLocal = 
 				new HashMap<Integer, Integer>();  // arbitros disponibles 
		 HashMap<Integer, Integer> cantArbitrosPorEncuentroVisitante = 
	 				new HashMap<Integer, Integer>();  // arbitros disponibles 
		 
		 HashMap<Integer, Integer> arbitrosDisponibles = 
				 new HashMap<Integer, Integer>();
		 
		if(!menoresLocalMi.isEmpty() && !menoresVisitanteMi.isEmpty()) {
		
				for(int arbitros: menoresLocalMi.keySet()) {
					int cantVecesLocal  = 0;
					for (int encuentro = 0; encuentro < encuentros.size(); encuentro++) {
						if(encuentros.get(encuentro).getArbitro() == menoresLocalMi.get(arbitros)) {
							cantVecesLocal++;
						}
					}		
					cantArbitrosPorEncuentroLocal.put(arbitros, cantVecesLocal);
				}
			
				for(int arbitros: menoresVisitanteMi.keySet()) {
					int cantVecesVisitante  = 0;
					for (int encuentro = 0; encuentro < encuentros.size(); encuentro++) {
						if(encuentros.get(encuentro).getArbitro() == menoresVisitanteMi.get(arbitros)) {
							cantVecesVisitante++;
						}
					}		
					cantArbitrosPorEncuentroVisitante.put(arbitros, cantVecesVisitante);
				}
				
				// seleccionar los arbitros de los encuentros menores a mi. 

				for(int arbitroLocal : cantArbitrosPorEncuentroLocal.keySet()) {
					
					for(int arbitroVisitante : cantArbitrosPorEncuentroVisitante.keySet()) {
					
						// los mismos arbitros de cada equipo
						if(arbitroLocal == arbitroVisitante) {
							
							int suma = cantArbitrosPorEncuentroLocal.get(arbitroLocal) + 
									   cantArbitrosPorEncuentroVisitante.get(arbitroVisitante);
							
							if(suma < mi) {
								arbitrosDisponibles.put(arbitroLocal, suma);
							}
						}
					}
				}
				
			 
				if(!arbitrosDisponibles.isEmpty()) {
				
					ArrayList<Integer> arbitros = new ArrayList<Integer>();
					for(int arbitro: arbitrosDisponibles.keySet()) {
						arbitros.add(arbitro);
					}
					
					int arbitroRandom = rn.nextInt(arbitros.size());
					mPartido.setArbitro(arbitros.get(arbitroRandom)+1);
				
				} else {
					
					int arbitroRandom = rn.nextInt(cantArbitros);
					mPartido.setArbitro(arbitroRandom+1);
			
				}
				
		} else if(menoresLocalMi.isEmpty() && menoresVisitanteMi.isEmpty()) {
			
			int arbitroRandom = rn.nextInt(cantArbitros);
			mPartido.setArbitro(arbitroRandom+1);
			
		
		 } else if(menoresLocalMi.isEmpty() && !menoresVisitanteMi.isEmpty()) {

			 ArrayList<Integer> arbitrosList = new ArrayList<Integer>();
			 for(int arbitros : menoresVisitanteMi.keySet()) {
				 arbitrosList.add(arbitros);
			 }
				int arbitroRandom = rn.nextInt(arbitrosList.size());
				mPartido.setArbitro(arbitrosList.get(arbitroRandom)+1);
			 
		 } else {
			 
			 ArrayList<Integer> arbitrosList = new ArrayList<Integer>();
			 for(int arbitros : menoresLocalMi.keySet()) {
				 arbitrosList.add(arbitros);
			 }
				int arbitroRandom = rn.nextInt(arbitrosList.size());
				mPartido.setArbitro(arbitrosList.get(arbitroRandom)+1);
		 }
		
		}
		
		

		
	}

	
	// un arbitro por partido
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
