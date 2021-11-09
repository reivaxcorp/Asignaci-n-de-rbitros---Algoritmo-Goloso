package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import main.AlgoritmoGolosoMain;
import main.FixtureGenerador;
import main.MainUI;
import main.Partido;

public class AlgoritmoGolosoMainTest {

	private AlgoritmoGolosoMain algoritmoGolosoMain;
	private FixtureGenerador fixture;
 	
	// ignoramos la generacion del arbicho en el disco
	// lo manejaremos en la memoria. 
	@Before
	public void setup() {
		this.algoritmoGolosoMain = new AlgoritmoGolosoMain();
		
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

	
	private HashMap<Integer, ArrayList<Partido>> getFechas() {
		return algoritmoGolosoMain.getFechas();
	}
}
