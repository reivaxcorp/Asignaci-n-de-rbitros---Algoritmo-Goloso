package main;
import java.util.ArrayList;
import java.util.HashMap;

public class AlgoritmoGoloso {
	
	private HashMap<Integer, ArrayList<Partido>> fechas; 

	/**
	 * M�todo que devolver� las fechas con los �rbitros asignados, recibe las fechas sin �rbitros, y la cantidad de �rbitros para procesar en cada fecha
	 */
	public HashMap<Integer, ArrayList<Partido>> generarAlgoritmoGoloso(
			HashMap<Integer,ArrayList<Partido>> fechas,
			int cantidadArbitros
			) {
		
		HashMap<Integer, ArrayList<Partido>> pFechas = fechas;

		if (pFechas != null && !pFechas.isEmpty() && cantidadArbitros > 0) {
		
			for (int fecha = 0; fecha < pFechas.size(); fecha++) {
				for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {	
					Partido p =  pFechas.get(fecha).get(partido);
					int arbitro = asignarArbitro(pFechas, p, cantidadArbitros, fecha);
					p.setArbitro(arbitro);
					
				}
			}
		} else {
			if(cantidadArbitros <=0)
				throw new RuntimeException("Arbitros debe ser mayor a cero");
			else
				throw new RuntimeException("Fechas incorrectas");
		}
		
		return pFechas;
	}
	
	
	
	/**
	 * Asigna un �rbitro a un partido, recibe la fecha actual, el partido a asignar un �rbitro, y la cantidad de �rbitros, y las fechas para comparar �rbitros anteriores
	 * @param pFechas las fechas con la lista de partidos 
	 * @param mPartido partido actual o encuentro a procesar y asignar un arbitro
	 * @param cantArbitros la cantidad de arbitros que tenemos disponibles
	 * @param fechaActual la fecha actual en la cual nos encontramos
	 * @return nos devuelve un arbitro equilibrado 
	 */
	public int asignarArbitro(
			HashMap<Integer, ArrayList<Partido>> pFechas,
			Partido mPartido,
			int cantArbitros, 
			int fechaActual) {
		
		if(pFechas == null ||  mPartido == null || pFechas.isEmpty() ||
		   (cantArbitros >= MainUI.cantidadArbitrosPorDefecto &&
		   cantArbitros <= MainUI.cantidadArbitrosPorDefecto) == false) {
			throw new RuntimeException("Datos invalidos");
		}
	
		// en un principio seran los arbitros de 1..n 
		// fechas 1 si la cantidad de arbitros es menor a la cantidad de partidos
		// si los arbitros ya fueron seleccionado retorna cero
		int arbitro = obtenerArbitroConsecutivo(pFechas, cantArbitros);
		if(arbitro != 0) {
			return arbitro;
		} else {
			
			// buscar todos los partidos en todas las fechas de los dos equipos del partido.
			ArrayList<Partido> equipoLocal = obtenerPartidosEquipo(mPartido.getLocal(), pFechas); 
			ArrayList<Partido> equipoVisitante = obtenerPartidosEquipo(mPartido.getVisitante(), pFechas); 
			
			// buscar la cantidad de veces que los arbitros aparecen en cada equipo.
			HashMap<Integer, Integer> partidosDeLocal = getCantidadVecesArbitroEnEquipo(cantArbitros, equipoLocal);
			HashMap<Integer, Integer> partidosDeVisitante = getCantidadVecesArbitroEnEquipo(cantArbitros, equipoVisitante); 
			
			int valor = calcularSiguienteArbitro(partidosDeLocal, partidosDeVisitante, pFechas, cantArbitros, fechaActual);
			return valor;

		}

	}
	/**
	 * Obtiene todos los partidos del equipo en las fechas, ya sea de local o visitante	 
	 */
	public ArrayList<Partido> obtenerPartidosEquipo(String nombreEquipo, HashMap<Integer, ArrayList<Partido>> pFechas) {
		
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
	
    /**
     * Obtiene la cantidad de veces en que un �rbitro fue asignado en cada partido, devuelve un HashMap con el arbitro correspondiente en su key, y como valor la cantidad de �rbitros	
     */
    public HashMap<Integer, Integer> getCantidadVecesArbitroEnEquipo(int cantArbitros, ArrayList<Partido> partidos) {
    	
    	HashMap<Integer, Integer> arbitros = new HashMap<Integer, Integer>(); 
    	
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
    
    /**
     *Devuelve un �rbitro que no se ha sido seleccionado en algunos de los equipos y que no se ha asignado hasta la fecha actual.  Recibe un ArrayList con los �rbitros que ya han sido asignado en la fecha, tambi�n recibe la cantidad de �rbitros. Y por otro lado tambi�n recibe dos HashMap, uno para cada equipo que corresponde a los �rbitros que ya han dirigido hasta el momento, con la informaci�n de la cantidad de �rbitros y el �rbitro que dirigi� al equipo en las fechas. 
     */
    public int obtenerArbitroNoAsignado(
     		ArrayList<Integer> arbitrosYaAsignadoEnFecha, 
    		int cantArbitros,
    		HashMap<Integer, Integer> partidosDeLocal, 
    		HashMap<Integer, Integer> partidosDeVisitante
    		) {
    	
    	int arbitroLibre = 0;
    	
		// los arbitros que ya se han seleccionado en algunos de los equipos
		ArrayList<Integer> obtenerArbitrosYaSeleccionado = new ArrayList<Integer>();
		
		// equipo local
		for(int arbitro : partidosDeLocal.keySet()) {
			if( obtenerArbitrosYaSeleccionado.contains(arbitro) == false)
				obtenerArbitrosYaSeleccionado.add(arbitro);
		}
		// equipo visitante
		for(int arbitro : partidosDeVisitante.keySet()) {
			if( obtenerArbitrosYaSeleccionado.contains(arbitro) == false )
				obtenerArbitrosYaSeleccionado.add(arbitro);
		}
	
		
		// buscar los arbitros no seleccionado algunos de esos equipos
		// ademas que no se hayan repetido para la fecha en arbitrosNoDisponible
		for(int arbitro = 1; arbitro <= cantArbitros; arbitro++) {
			if(obtenerArbitrosYaSeleccionado.contains(arbitro) == false && 
					arbitrosYaAsignadoEnFecha.contains(arbitro) == false) {
				arbitroLibre = arbitro;
			}
		}
		
		return arbitroLibre;
    }
    
    /**
	Hace el c�lculo del �rbitro que menos se ha seleccionado en ambos equipos, y devuelve ese �rbitro, en caso de que la cantidad sea igual devolver� cero, y ser� el m�todo menorCantidadDeArbitroRepetido, que tratara este caso. 
     */
    public int getArbitroMenosAsignado(
    		HashMap<Integer,Integer> partidosDeLocal,
    		HashMap<Integer,Integer> partidosDeVisitante 
    		) {
    	
    	
    	int cantMenorLocal = Integer.MAX_VALUE;
    	int cantMenorVisitante = Integer.MAX_VALUE;
    	int arbitroLocalMenorVeces = 0;
    	int arbitroVisitanteMenorVeces = 0;

    	for(int arbitro: partidosDeLocal.keySet()) {
    		if(partidosDeLocal.get(arbitro) < cantMenorLocal) {
    			cantMenorLocal = partidosDeLocal.get(arbitro);
    			arbitroLocalMenorVeces = arbitro;
    		}
    	}
    	
    	for(int arbitro: partidosDeVisitante.keySet()) {
    		if(partidosDeVisitante.get(arbitro) < cantMenorVisitante) {
    			cantMenorVisitante = partidosDeVisitante.get(arbitro);
    			arbitroVisitanteMenorVeces = arbitro;
    		}
    	}

		
		if(cantMenorVisitante < cantMenorLocal) {
			return arbitroVisitanteMenorVeces;
		} else if(cantMenorLocal < cantMenorVisitante) {
			return arbitroLocalMenorVeces;
		} else  {
			return 0;
		}
    }
    
    
   
    /**
	Buscamos el �rbitro que no se haya colocado en ambos equipos, en caso contrario buscamos el menos repetido. Recibe Dos HashMap con la informaci�n de cada equipo de sus �rbitros. 
     */
    public int calcularSiguienteArbitro(
     		HashMap<Integer, Integer> partidosDeLocal,
    		HashMap<Integer, Integer> partidosDeVisitante,
    		HashMap<Integer, ArrayList<Partido>> pFechas,
    		int cantArbitros,
    		int fechaActual) {
    	
		// evitar estos arbitros ya se utilizaron en la fecha actual, hasta el momento.
    	ArrayList<Integer> arbitrosYaAsignadoEnFecha = obtenerArbitrosDeFecha(pFechas.get(fechaActual));
    	
    	// buscar un arbitro que no haya dirigido ambos equipos, de lo contrario retorna cero.
    	int arbitroLibre = obtenerArbitroNoAsignado(arbitrosYaAsignadoEnFecha, cantArbitros, partidosDeLocal, partidosDeVisitante); 
     
		// se han terminado la disponibilidad de arbitros que han dirigido 
		// ambos equipos, es el momento de repetir.
		if(arbitroLibre == 0) {

			// verificamos los encuentros de los dos equiopos y eligiremos un arbitro que no hayan
			// sido asignados en el partido.
			arbitroLibre = getArbitroMenosAsignado(partidosDeLocal, partidosDeVisitante);
			
			//  No hay arbitros que no se hayan colocado en el encuentro
			//  buscar el menor arbitro que se haya repetido entre las fechas
			if(arbitroLibre == 0) {
				return menorCantidadDeArbitroRepetido(pFechas, cantArbitros);
			}
		
			return arbitroLibre;
		}
			
		 return arbitroLibre; // existe un arbitro que no ha sido asignado a�n.
    }


    /**
 	 Devuelve los �rbitros que ya han sido seleccionado para una fecha. 
     */
	public ArrayList<Integer> obtenerArbitrosDeFecha(ArrayList<Partido> partidos) {
    	ArrayList<Integer> arbitrosYaAsignadoEnFecha = new ArrayList<Integer>();
    	for(Partido p: partidos) {
    		if(arbitrosYaAsignadoEnFecha.contains(p.getArbitro()) == false) {
    			arbitrosYaAsignadoEnFecha.add(p.getArbitro());
    		}
    	}
		return arbitrosYaAsignadoEnFecha;
	}
    
 
	/**
	Buscamos el arbitro que menos se haya repetido en todas las fechas, recibe las fechas. Recibe como par�metro las fechas, y la cantidad de arbitros.
	 */
    public int menorCantidadDeArbitroRepetido(HashMap<Integer, ArrayList<Partido>> pFechas, int cantArbitros) {
    	
    	HashMap<Integer, Integer> cantidadArbitros = new HashMap<Integer, Integer>();
    
    	// Inicializar
    	for(int arbitro = 1; arbitro <= cantArbitros; arbitro++)
    		cantidadArbitros.put(arbitro, 0);
    	
    	for(int fecha: pFechas.keySet()) {
    		for(Partido p: pFechas.get(fecha)) {
    			if(p.getArbitro() != -1)
    				cantidadArbitros.put(p.getArbitro(), cantidadArbitros.get(p.getArbitro()) +1);
    		}
    	}
    	
    	int cantVecesMenosRepetido = Integer.MAX_VALUE;
    	int arbitroMenosRepetido = 1;
    	
    	for(int arbitro: cantidadArbitros.keySet()) {
    		if(cantidadArbitros.get(arbitro) < cantVecesMenosRepetido) {
    			cantVecesMenosRepetido = cantidadArbitros.get(arbitro);
    			arbitroMenosRepetido = arbitro;
    		}
    	}
    	return arbitroMenosRepetido;
    }
    
    
    /**
	Devuelve los �rbitros en forma consecutiva, en los primeros partidos, cuando se terminen de asignar todos los �rbitros devolver� cero	
     */
     public int obtenerArbitroConsecutivo(HashMap<Integer, ArrayList<Partido>> pFechas, int cantArbitros) {
		
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
    
     /**
      * Resetea los �rbitros para la fecha cuando volvemos a generar nuevos �rbitros. 
      */
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
