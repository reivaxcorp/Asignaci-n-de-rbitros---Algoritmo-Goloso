package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.SwingWorker;


public class AlgoritmoGolosoMain {
	
	private HashMap<Integer, ArrayList<Partido>> fechas; 
	private int mi = 4; // mi a la maaxima cantidad de partidos de i con un mismo �arbitro

	
	public HashMap<Integer, ArrayList<Partido>> generarAlgoritmoGoloso(HashMap<Integer, ArrayList<Partido>> fechas, int cantidadArbitros) {
 
		
		HashMap<Integer, ArrayList<Partido>> pFechas = fechas;

		if (pFechas != null && !pFechas.isEmpty()) {
		
			for (int fecha = 0; fecha < pFechas.size(); fecha++) {
				for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {	
					
					Partido p =  pFechas.get(fecha).get(partido);
					p.setArbitro(asignarArbitro(pFechas, p, cantidadArbitros, fecha));
					
				}
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
		
		return pFechas;
	}
	
	private int obtenerArbitroSiguiente(HashMap<Integer, ArrayList<Partido>> pFechas, int cantArbitros) {
		
		ArrayList<Integer> arbitrosYaSeleccionadoPorFecha = new ArrayList<Integer>();
	
		for(int fecha: pFechas.keySet()) {
			for(Partido p: pFechas.get(fecha)) {
				if(p.getArbitro() != -1) {
					  arbitrosYaSeleccionadoPorFecha.add(p.getArbitro());
					}
			}
		}
		
		for( int arbitrosDisponible = 0; arbitrosDisponible < cantArbitros; arbitrosDisponible ++) {
			
			if(!(arbitrosYaSeleccionadoPorFecha.contains(arbitrosDisponible +1)))
				return arbitrosDisponible +1;
		}
		
		return 0;
	}
	
	public int asignarArbitro(HashMap<Integer, ArrayList<Partido>> pFechas, Partido mPartido, int cantArbitros, int fechaActual) {
		
		// buscar todos los partidos en todas las fechas de los dos equipos del partido. OK
		ArrayList<Partido> equipoLocal = obtenerPartidosEquipo(mPartido.getLocal(), pFechas); // cant. veces equipo local
		ArrayList<Partido> equipoVisitante = obtenerPartidosEquipo(mPartido.getVisitante(), pFechas);; // cant. veces equipo visitante
		// busca todos los partidos entre ellos, cantidad de veces que juegan juntos en cada fecha
		ArrayList<Partido> encuentros = obtenerEncuentros(mPartido, pFechas); 

		// buscar la cantidad de veces que los arbitros aparecen en cada equipo . OK

		// key arbitro, value cantidad de veces. 
		HashMap<Integer, Integer> cantVecesArbitroEquipoLocal = obtenerArbitrosEquipo(cantArbitros, equipoLocal); 
		// key arbitro, value cantidad de veces. 
		HashMap<Integer, Integer> cantVecesArbitroEquipoVisitante = obtenerArbitrosEquipo(cantArbitros, equipoVisitante); 

		int arbitro = obtenerArbitroSiguiente(pFechas, cantArbitros);
		if(arbitro != 0) {
			return arbitro;
		} else {
		
			// buscar todos los arbitros de los dos equipos <= Mi partido. OK
			HashMap<Integer, Integer> menoresLocalMi = obtenerArbitrosHastaMi(cantVecesArbitroEquipoLocal);
			HashMap<Integer, Integer> menoresVisitanteMi = obtenerArbitrosHastaMi(cantVecesArbitroEquipoVisitante);
	
			int valor = obtenerArbitroCoincidentePartidos(menoresLocalMi, menoresVisitanteMi, pFechas, cantArbitros, fechaActual, encuentros);
			return valor;

		}

	}
	
	
	private ArrayList<Partido> obtenerPartidosEquipo(String nombreEquipo, HashMap<Integer, ArrayList<Partido>> pFechas) {
		
		ArrayList<Partido> partidos = new ArrayList<Partido>();
		
		for (int fecha = 0; fecha < pFechas.size(); fecha++) {
			for (int partido = 0; partido < getFechas().get(fecha).size(); partido++) {
				
				String equipoVisitante = pFechas.get(fecha).get(partido).getVisitante();
				String equipoLocal = pFechas.get(fecha).get(partido).getLocal();
				
				if(equipoVisitante.equals(nombreEquipo) || equipoLocal.equals(nombreEquipo))
					partidos.add(pFechas.get(fecha).get(partido));
			}
		}
		return partidos;
	}
	
    private ArrayList<Partido> obtenerEncuentros(Partido mPartido, HashMap<Integer, ArrayList<Partido>> pFechas) {
    	
    	ArrayList<Partido> encuentros = new ArrayList<Partido>();
    	
    	for (int fecha = 0; fecha < pFechas.size(); fecha++) {
			for (int partido = 0; partido < getFechas().get(fecha).size(); partido++) {
				
				String equipoVisitante = pFechas.get(fecha).get(partido).getVisitante();
				String equipoLocal = pFechas.get(fecha).get(partido).getLocal();
				
				if((mPartido.getLocal().equals(equipoLocal) && mPartido.getVisitante().equals(equipoVisitante)) || 
						(mPartido.getVisitante().equals(equipoVisitante) && mPartido.getLocal().equals(equipoLocal)) ) {
						
						encuentros.add(pFechas.get(fecha).get(partido));
					}
			 }
		}
    	return encuentros;
    }
	
    private HashMap<Integer, Integer> obtenerArbitrosEquipo(int cantArbitros, ArrayList<Partido> partidos) {
    	
    	HashMap<Integer, Integer> arbitros = 
				new HashMap<Integer, Integer>(); 
    	
    	for(int arbitro = 0; arbitro < cantArbitros; arbitro ++) {

			int cantVeces = 0;
			for (int partido = 0; partido < partidos.size(); partido++) {
				if(partidos.get(partido).getArbitro() == arbitro+1) {
					cantVeces ++;
					arbitros.put(arbitro+1, cantVeces);
				}
			}
		}
    	return arbitros;
    }
    
    private HashMap<Integer, Integer> obtenerArbitrosHastaMi(HashMap<Integer, Integer> cantArbitrosPorEquipo) {
    	HashMap<Integer, Integer> menoresMi = 
				new HashMap<Integer, Integer>(); 
    	
    	for(int arbitro: cantArbitrosPorEquipo.keySet()) {
			if(cantArbitrosPorEquipo.get(arbitro) <= mi && 
					cantArbitrosPorEquipo.get(arbitro) > 0) {
				menoresMi.put(arbitro, cantArbitrosPorEquipo.get(arbitro));
			}
		}
    	return menoresMi;
    }
    
    // cantidad de veces que los dos arbitros dirigieron al mismo equipo
    // debemos buscar el arbitro que menos haya dirigido el partido en el encuentro actual
    private int obtenerArbitroCoincidentePartidos(
     		HashMap<Integer, Integer> menoresLocalMi,
    		HashMap<Integer, Integer> menoresVisitanteMi,
    		HashMap<Integer, ArrayList<Partido>> pFechas,
    		int cantArbitros,
    		int fechaActual, ArrayList<Partido> encuentros) {
    	
    	int arbitroLibre = 0; // nuestro arbitro disponible
    	
		// evitar estos arbitros ya se utilizaron hasta el momento
		ArrayList<Integer> arbitrosNoDisponible = new ArrayList<Integer>();
		for(Partido p: pFechas.get(fechaActual)) {
			if(arbitrosNoDisponible.contains(p.getArbitro()) == false) {
				arbitrosNoDisponible.add(p.getArbitro());
			}
		}
		
		// los arbitros que ya se han seleccionado en algunos de los equipos
		ArrayList<Integer> obtenerArbitrosYaSeleccionado = new ArrayList<Integer>();
		
		// equipo local
		for(int arbitro : menoresLocalMi.keySet()) {
			if( obtenerArbitrosYaSeleccionado.contains(arbitro) == false)
				obtenerArbitrosYaSeleccionado.add(arbitro);
		}
		// equipo visitante
		for(int arbitro : menoresVisitanteMi.keySet()) {
			if( obtenerArbitrosYaSeleccionado.contains(arbitro) == false )
				obtenerArbitrosYaSeleccionado.add(arbitro);
		}
	
		
		// buscar los arbitros no seleccionado algunos de esos equipos
		// ademas que no se hayan repetido para la fecha en arbitrosNoDisponible
		for(int arbitro = 1; arbitro <= cantArbitros; arbitro++) {
			if(obtenerArbitrosYaSeleccionado.contains(arbitro) == false && 
					arbitrosNoDisponible.contains(arbitro) == false) {
				arbitroLibre = arbitro;
			}
		}
		
			 
		// se han terminado la disponibilidad de arbitros que han dirigido 
		// ambos partidos, es el momento de repetir
		if(arbitroLibre == 0) {
			// evitar los arbitros que ya se utilizaron en los encuentros
			ArrayList<Integer> arbitrosEncuentros = new ArrayList<Integer>();
			
			for(Partido p: encuentros) {
				if(arbitrosEncuentros.contains(p.getArbitro()) == false) {
					arbitrosEncuentros.add(p.getArbitro());
				}
			}
			for(int arbitro = 1; arbitro <= cantArbitros; arbitro++) {
				
				// debe ser un arbitro que que se haya repetido 
				// pero que a�n no se haya colocado hasta en la fecha
				if(arbitrosEncuentros.contains(arbitro) == false && 
						arbitrosNoDisponible.contains(arbitro) == false) {
					arbitroLibre = arbitro;
				}
			}
		
			return arbitroLibre;
		}
		
				
		 return arbitroLibre;
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
	

	public void resetArbitros() {
		for(int f: fechas.keySet()) {
			for(Partido p: fechas.get(f))
				p.setArbitro(-1);
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
