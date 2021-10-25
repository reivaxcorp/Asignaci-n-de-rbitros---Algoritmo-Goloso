package test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import main.AlgoritmoGolosoMain;
import main.ArchivoJSON;
import main.Fixture;
import main.Partido;

public class AlgoritmoGolosoMainTest {

	private FakeMainUITest fakeMainUITest;
	private AlgoritmoGolosoMain algoritmoGolosoMain;
	private Fixture fix;
	private final String NOMBRE_ARCHIVO_JSON = "fechas.json";

	@Before
	public void setup() {
		this.fakeMainUITest = new FakeMainUITest();
		this.algoritmoGolosoMain = new AlgoritmoGolosoMain(fakeMainUITest);
		this.fix = new Fixture();
		new ArchivoJSON(fix.obtenerFechas(), NOMBRE_ARCHIVO_JSON);
		File file = new File("./"+NOMBRE_ARCHIVO_JSON);
		this.algoritmoGolosoMain.setFechas(ArchivoJSON.leerArchivo(file.getName()).getFechas());
		this.algoritmoGolosoMain.colocarDatosDeArchivoUI(algoritmoGolosoMain.getFechas());
	}
	
	@Test(expected = RuntimeException.class)
	public void generarAlgoritmoGoloso_fechasNull_expetion() {
		algoritmoGolosoMain.generarAlgoritmoGoloso(null);
	}
	
	@Test(expected = RuntimeException.class)
	public void generarAlgoritmoGoloso_fechasEmpty_expetion() {
		HashMap<Integer, ArrayList<Partido>> fechas = algoritmoGolosoMain.getFechas();
		fechas.clear();
		algoritmoGolosoMain.generarAlgoritmoGoloso(fechas);
	}
	
	@Test(expected = RuntimeException.class)
	public void colocarDatosDeArchivoUI_fechasNull_expetion() {
		algoritmoGolosoMain.colocarDatosDeAlgoritmoGolosoUI(null);
	}

	@Test(expected = RuntimeException.class)
	public void colocarDatosDeArchivoUI_fechasEmpty_expetion() {
		HashMap<Integer, ArrayList<Partido>> fechas = algoritmoGolosoMain.getFechas();
		fechas.clear();
		algoritmoGolosoMain.colocarDatosDeAlgoritmoGolosoUI(fechas);
	}
	
	@Test(expected = RuntimeException.class)
	public void colocarDatosDeAlgoritmoGolosoUI_fechasNull_expetion() {
		algoritmoGolosoMain.colocarDatosDeAlgoritmoGolosoUI(null);
	}

	@Test(expected = RuntimeException.class)
	public void colocarDatosDeAlgoritmoGolosoUI_fechasEmpty_expetion() {
		HashMap<Integer, ArrayList<Partido>> fechas = algoritmoGolosoMain.getFechas();
		fechas.clear();
		algoritmoGolosoMain.colocarDatosDeAlgoritmoGolosoUI(fechas);
	}
	
	@Test
	public void crearListaArbitros_conFechaIncorrecta() {
		HashMap<String, ArrayList<Integer>> fechas = 
				algoritmoGolosoMain.crearListaArbitrosPorFecha(algoritmoGolosoMain.getFechas());
		fechas.put("22", new ArrayList<Integer>());
		
		assertFalse(fechas.size() == fix.getCantFechas());
		
	}
	
	@Test
	public void crearListaArbitros_conFechaCorrecta() {
		HashMap<String, ArrayList<Integer>> fechas = 
				algoritmoGolosoMain.crearListaArbitrosPorFecha(algoritmoGolosoMain.getFechas());
		
		assertTrue(fechas.size() == fix.getCantFechas());
	}
	
	@Test
	public void cantidadDeArbitros_incorrecto() {
		HashMap<String, ArrayList<Integer>> fechas = 
				algoritmoGolosoMain.crearListaArbitrosPorFecha(algoritmoGolosoMain.getFechas());
		fechas.put(String.valueOf(Integer.MAX_VALUE), new ArrayList<Integer>());
		
		int cantArbitros = 0;
		for (int fecha = 0; fecha < fechas.size(); fecha++) {
			if(fechas.get(String.valueOf(fecha))!=null) {
	 			for (int partido = 0; partido < fechas.get(String.valueOf(fecha)).size(); partido++) {
					cantArbitros ++;
				}
			}
		}
		assertFalse((cantArbitros/fechas.size()) == fix.getCantPartidosPorFecha());
	}
	
	@Test
	public void cantidadDeArbitros_correcto() {
		HashMap<String, ArrayList<Integer>> fechas = 
				algoritmoGolosoMain.crearListaArbitrosPorFecha(algoritmoGolosoMain.getFechas());
		
		int cantArbitros = 0;
		for (int fecha = 0; fecha < fechas.size(); fecha++) {
			if(fechas.get(String.valueOf(fecha))!=null) {
	 			for (int partido = 0; partido < fechas.get(String.valueOf(fecha)).size(); partido++) {
					cantArbitros ++;
				}
			}
		}
		
		assertTrue((cantArbitros/fechas.size()) == fix.getCantPartidosPorFecha());
	}
	
}
