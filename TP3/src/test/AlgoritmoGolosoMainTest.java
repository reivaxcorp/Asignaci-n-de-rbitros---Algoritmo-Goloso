package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import main.AlgoritmoGoloso;
import main.FixtureGenerador;
import main.MainUI;
import main.Partido;

public class AlgoritmoGolosoMainTest {

	private AlgoritmoGoloso algoritmoGolosoMain;
	private FixtureGenerador fixture;
 	
	// ignoramos la generacion del arbicho en el disco
	// lo manejaremos en la memoria. 
	@Before
	public void setup() {
		this.algoritmoGolosoMain = new AlgoritmoGoloso();
		
		this.fixture = new FixtureGenerador();
		HashMap<Integer, ArrayList<Partido>> fechas = 
				new HashMap<Integer, ArrayList<Partido>>();
	
		for (int fecha = 0; fecha < fixture.obtenerFechas().size(); fecha++) {
			fechas.put(fecha, fixture.obtenerFechas().get(fecha));
		}
		this.algoritmoGolosoMain.setFechas(fechas);
 	}
	
	
	
	@Test(expected = RuntimeException.class)
	public void generarAlgoritmoGoloso_fechasNull_expetion() {
		algoritmoGolosoMain.generarAlgoritmoGoloso(null, -1);
	}
	
	@Test(expected = RuntimeException.class)
	public void generarAlgoritmoGoloso_fechasEmpty_expetion() {
		HashMap<Integer, ArrayList<Partido>> fechas = algoritmoGolosoMain.getFechas();
		fechas.clear();
		algoritmoGolosoMain.generarAlgoritmoGoloso(fechas, -1);
	}
	
	@Test
	public void generarAlgoritmoGoloso_fechasNoEmpty() {
		assertTrue(getFechas().size() > 0);
	}
	
	@Test(expected = RuntimeException.class)
	public void generarAlgoritmoGoloso_arbitrosCero_expetion() {
		algoritmoGolosoMain.generarAlgoritmoGoloso(getFechas(), 0);
	}
	
	@Test(expected = RuntimeException.class)
	public void asignarArbitro_False() {
		
 		
		for (int fecha = 0; fecha < getFechas().size(); fecha++) {
			for (int partido = 0; partido < getFechas().get(fecha).size(); partido++) {	
				Partido p =  getFechas().get(fecha).get(partido);
				p = null;
			    algoritmoGolosoMain.asignarArbitro(getFechas(),  p, MainUI.cantidadArbitrosPorDefecto, fecha);
			}
		}	
	}
	
	@Test
	public void asignarArbitro_True() {
		
 		
		for (int fecha = 0; fecha < getFechas().size(); fecha++) {
			for (int partido = 0; partido < getFechas().get(fecha).size(); partido++) {	
				Partido p =  getFechas().get(fecha).get(partido);
				int arbitro = algoritmoGolosoMain.asignarArbitro(getFechas(), p, MainUI.cantidadArbitrosPorDefecto, fecha);
				
				assertTrue(
						arbitro >  0 &&
						arbitro <= MainUI.cantidadArbitrosPorDefecto
						);
				
			}
		}	
	}
	
	@Test
	public void obtenerPartidosEquipo_equipoIxistente_True() {
		 ArrayList<Partido> partidos = 
				 algoritmoGolosoMain.obtenerPartidosEquipo("Sacachispas", getFechas());
		 
		 assertTrue(partidos.isEmpty());
	}
	
	@Test
	public void obtenerPartidosEquipo_equipoExistente_True() {
		Random rn = new Random(); 
		int fechaRandom = rn.nextInt(getFechas().size());
		int partidoRandom = rn.nextInt(getFechas().get(fechaRandom).size());
		Partido partido = getFechas().get(fechaRandom).get(partidoRandom);
		
		ArrayList<Partido> partidos = 
				 algoritmoGolosoMain.obtenerPartidosEquipo(
						 partido.getLocal(), getFechas()
						 );
		
		 
		 assertTrue(!partidos.isEmpty());
	}
	
	@Test 
	public void obtenerArbitroConsecutivo_False() {
		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		ArrayList<Integer> arbitros = new ArrayList<Integer>();
		
		for (int fecha = 0; fecha < fechas.size(); fecha++) {
			for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {	
				Partido p =  fechas.get(fecha).get(partido);
				int arbitro = algoritmoGolosoMain.obtenerArbitroConsecutivo(fechas, MainUI.cantidadArbitrosPorDefecto);
				p.setArbitro(arbitro);
 				
				arbitros.add(arbitro);
				arbitros.add(arbitro);
			}
		}
		boolean consecutivo = true;
		for(int arbitro = 1; arbitro < MainUI.cantidadArbitrosPorDefecto; arbitro++) {
			consecutivo = consecutivo && arbitros.get(arbitro-1)  == arbitro;
		}
		assertFalse(consecutivo);
	}
	
	@Test 
	public void obtenerArbitroConsecutivo_True() {
		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		ArrayList<Integer> arbitros = new ArrayList<Integer>();
		
		for (int fecha = 0; fecha < fechas.size(); fecha++) {
			for (int partido = 0; partido < fechas.get(fecha).size(); partido++) {	
				Partido p =  fechas.get(fecha).get(partido);
				int arbitro = algoritmoGolosoMain.obtenerArbitroConsecutivo(fechas, MainUI.cantidadArbitrosPorDefecto);
				p.setArbitro(arbitro);
				arbitros.add(arbitro);
			}
		}
		
		boolean consecutivo = true;
		for(int arbitro = 1; arbitro < MainUI.cantidadArbitrosPorDefecto; arbitro++) {
			consecutivo = consecutivo && arbitros.get(arbitro-1)  == arbitro;
		}
		assertTrue(consecutivo);
		
	}
	
	
	@Test
	public void calcularSiguienteArbitro_numeroDos_False() {
		
		int fechaActual = 0;
		int cantArbitros = 4;
		HashMap<Integer, Integer> partidosDeLocal = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> partidosDeVisitante = new HashMap<Integer, Integer>();
		partidosDeLocal.put(1, 1); // ejemplo, arbitro 1, 1 vez. 
		partidosDeLocal.put(2, 2); 
		partidosDeLocal.put(3, 2); // ejemplo, arbitro 3, 3 veces. 
		//partidosDeLocal.put(4, 5);
		
		partidosDeVisitante.put(1, 5);
		partidosDeVisitante.put(2, 2); 
		partidosDeVisitante.put(3, 2);
		//partidosDeVisitante.put(4, 10);

		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		
		
		int siguiente = algoritmoGolosoMain.calcularSiguienteArbitro(
				     		partidosDeLocal,
				    		partidosDeVisitante,
				    		fechas,
				    		cantArbitros,
				    		fechaActual);

		assertFalse(siguiente == 2);	
	
	}
	
	// arbitro que no dirigio aún el partido
	@Test
	public void calcularSiguienteArbitro_numeroDos_True() {
		
		int fechaActual = 0;
		int cantArbitros = 4;
		HashMap<Integer, Integer> partidosDeLocal = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> partidosDeVisitante = new HashMap<Integer, Integer>();
		partidosDeLocal.put(1, 1); // ejemplo, arbitro 1, 1 vez. 
		//partidosDeLocal.put(2, 2); 
		partidosDeLocal.put(3, 2); // ejemplo, arbitro 3, 2 veces. 
		partidosDeLocal.put(4, 5);
		
		partidosDeVisitante.put(1, 5);
		//partidosDeVisitante.put(2, 2); 
		partidosDeVisitante.put(3, 2);
		partidosDeVisitante.put(4, 10);

		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		
		
		int siguiente = algoritmoGolosoMain.calcularSiguienteArbitro(
				     		partidosDeLocal,
				    		partidosDeVisitante,
				    		fechas,
				    		cantArbitros,
				    		fechaActual);

		assertTrue(siguiente == 2);	
	
	}

	// arbitro que menos dirigio los equipos
	@Test
	public void getArbitroMenosAsignado_tres_true() {
		
		// setup
		HashMap<Integer, Integer> partidosDeLocal = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> partidosDeVisitante = new HashMap<Integer, Integer>();
		partidosDeLocal.put(1, 100); 
		partidosDeLocal.put(2, 2); 
		partidosDeLocal.put(3, 2); 
		partidosDeLocal.put(4, 5);
		
		partidosDeVisitante.put(1, 5);
		partidosDeVisitante.put(2, 2); 
		partidosDeVisitante.put(3, 1);
		partidosDeVisitante.put(4, 10);

		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		
		Random rn = new Random(); 
		int fechaRandom = rn.nextInt(getFechas().size());
		int partidoRandom = rn.nextInt(getFechas().get(fechaRandom).size());
		Partido partido = getFechas().get(fechaRandom).get(partidoRandom);
		
		
		
		for(int fecha: fechas.keySet()) {
			for(Partido p: fechas.get(fecha)) {
				if( (p.getLocal().equals(partido.getLocal()) && p.getVisitante().equals(partido.getVisitante())) ||
					(p.getLocal().equals(partido.getVisitante()) && p.getVisitante().equals(partido.getLocal())) ) {
					 p.setArbitro(4);
				}
			}
		}
		
		// excersice
		int siguiente = algoritmoGolosoMain.getArbitroMenosAsignado(
						partidosDeLocal,
						partidosDeVisitante 
						);
		
		// verify
		assertTrue(siguiente == 3);	
	}

	@Test 
	public void getCantidadVecesArbitroEnEquipo_cantidad_False() {
		
		int cantArbitros = 4;
		Partido mPartido = new Partido("Boca Juniors", "River Plate", -1);
		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		// buscar todos los partidos en todas las fechas de los dos equipos del partido.
		ArrayList<Partido> equipoLocal = algoritmoGolosoMain.obtenerPartidosEquipo(mPartido.getLocal(), fechas); 
		ArrayList<Partido> equipoVisitante = algoritmoGolosoMain.obtenerPartidosEquipo(mPartido.getVisitante(), fechas);
		
		for(int partido = 0; partido < equipoLocal.size(); partido ++) {
			if(equipoLocal.get(partido).getArbitro() == -1) {
				if(partido  == 0) continue;
				if(partido  == 1) continue;
				equipoLocal.get(partido).setArbitro(1);
			}
		}
		for(int partido = 0; partido < equipoVisitante.size(); partido ++) {
			if(equipoVisitante.get(partido).getArbitro() == -1) {
				if(partido  == 0) continue;
				if(partido  == 1) continue;
				if(partido  == 2) continue;
				if(partido  == 4) continue;
				equipoVisitante.get(partido).setArbitro(1);
			}
		}
		
		
		HashMap<Integer, Integer> partidosDeLocal = 
				algoritmoGolosoMain.getCantidadVecesArbitroEnEquipo(cantArbitros, equipoLocal);

		HashMap<Integer, Integer> partidosDeVisitante = 
				algoritmoGolosoMain.getCantidadVecesArbitroEnEquipo(cantArbitros, equipoVisitante);
	
		
		// la cantidad de veces que se asigno corresponde a la cantidad de fechas. 
		assertTrue(partidosDeLocal.get(1) == 11);
		assertTrue(partidosDeVisitante.get(1) == 9);
		
	}
	
	@Test 
	public void getCantidadVecesArbitroEnEquipo_cantidad_True() {
		
		int cantArbitros = 4;
		Partido mPartido = new Partido("Boca Juniors", "River Plate", -1);
		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		// buscar todos los partidos en todas las fechas de los dos equipos del partido.
		ArrayList<Partido> equipoLocal = algoritmoGolosoMain.obtenerPartidosEquipo(mPartido.getLocal(), fechas); 
		ArrayList<Partido> equipoVisitante = algoritmoGolosoMain.obtenerPartidosEquipo(mPartido.getVisitante(), fechas);
		
		for(int partido = 0; partido < equipoLocal.size(); partido ++) {
			if(equipoLocal.get(partido).getArbitro() == -1) {
				if(partido  == 0) continue;
				if(partido  == 1) continue;
				equipoLocal.get(partido).setArbitro(3);
			}
		}
		for(int partido = 0; partido < equipoVisitante.size(); partido ++) {
			if(equipoVisitante.get(partido).getArbitro() == -1) {
				if(partido  == 12) continue;
				equipoVisitante.get(partido).setArbitro(4);
			}
		}
		
		
		HashMap<Integer, Integer> partidosDeLocal = 
				algoritmoGolosoMain.getCantidadVecesArbitroEnEquipo(cantArbitros, equipoLocal);

		HashMap<Integer, Integer> partidosDeVisitante = 
				algoritmoGolosoMain.getCantidadVecesArbitroEnEquipo(cantArbitros, equipoVisitante);
	
		
		// la cantidad de veces que se asigno corresponde a la cantidad de fechas. 
		assertFalse(partidosDeLocal.get(3) == 12);
		assertFalse(partidosDeVisitante.get(4) == 13);
		
	}
	
	@Test 
	public void obtenerArbitroNoAsignado_ArbitroCuatro_False() {
		// setup
		int cantArbitros = 4;
		int fechaActual = 0;
		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		HashMap<Integer, Integer> partidosDeLocal = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> partidosDeVisitante = new HashMap<Integer, Integer>();
		
		partidosDeLocal.put(1, 100); 
		partidosDeLocal.put(2, 2); 
		partidosDeLocal.put(3, 2); 
		partidosDeLocal.put(4, 5);
		
		partidosDeVisitante.put(1, 5);
		partidosDeVisitante.put(2, 2); 
		partidosDeVisitante.put(3, 1);
		partidosDeVisitante.put(4, 10);

    	ArrayList<Integer> arbitrosYaAsignadoEnFecha = algoritmoGolosoMain.obtenerArbitrosDeFecha(fechas.get(fechaActual));
    	
		
    	
    	// buscar un arbitro que no haya dirigido ambos equipos, de lo contrario retorna cero.
    	int arbitroLibre = algoritmoGolosoMain.obtenerArbitroNoAsignado(arbitrosYaAsignadoEnFecha, cantArbitros, partidosDeLocal, partidosDeVisitante); 
    	
    	assertFalse(arbitroLibre == 4);
	}
	
	@Test 
	public void obtenerArbitroNoAsignado_ArbitroTres_True() {
		// setup
		int cantArbitros = 4;
		int fechaActual = 0;
		HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
		HashMap<Integer, Integer> partidosDeLocal = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> partidosDeVisitante = new HashMap<Integer, Integer>();
		
		partidosDeLocal.put(1, 100); 
		partidosDeLocal.put(2, 2); 
		//partidosDeLocal.put(3, 2); 
		partidosDeLocal.put(4, 5);
		
		partidosDeVisitante.put(1, 5);
		partidosDeVisitante.put(2, 2); 
		//partidosDeVisitante.put(3, 1);
		partidosDeVisitante.put(4, 10);

    	ArrayList<Integer> arbitrosYaAsignadoEnFecha = algoritmoGolosoMain.obtenerArbitrosDeFecha(fechas.get(fechaActual));
    	
		
    	
    	// buscar un arbitro que no haya dirigido ambos equipos, de lo contrario retorna cero.
    	int arbitroLibre = algoritmoGolosoMain.obtenerArbitroNoAsignado(arbitrosYaAsignadoEnFecha, cantArbitros, partidosDeLocal, partidosDeVisitante); 
    	
    	assertTrue(arbitroLibre == 3);
	}
	
	@Test
	public void menorCantidadDeArbitroRepetido_arbitroTrece_False() {
		
	 HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
	 int cantArbitros = 13;
	 
	 int [] arbitros = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; 
	 int index = 0;
		for(int fecha: fechas.keySet()) {
			for(Partido p: fechas.get(fecha)) {
					if(index < arbitros.length) {
						p.setArbitro(arbitros[index]);
						index ++;
					}
				}
			}
		int arbitro = algoritmoGolosoMain.menorCantidadDeArbitroRepetido(fechas, cantArbitros);
	
		assertFalse(arbitro == 13);
		
	}
	
	@Test
	public void menorCantidadDeArbitroRepetido_arbitroDos_True() {
		
	 HashMap<Integer, ArrayList<Partido>> fechas = getFechas();
	 int cantArbitros = 13;
	 
	 int [] arbitros = {1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13}; // falta el 2. 
	 int index = 0;
		for(int fecha: fechas.keySet()) {
			for(Partido p: fechas.get(fecha)) {
					if(index < arbitros.length) {
						p.setArbitro(arbitros[index]);
						index ++;
					}
				}
			}
		int arbitro = algoritmoGolosoMain.menorCantidadDeArbitroRepetido(fechas, cantArbitros);
	
		assertTrue(arbitro == 2);
		
	}
	
	private HashMap<Integer, ArrayList<Partido>> getFechas() {
		return algoritmoGolosoMain.getFechas();
	}
}
