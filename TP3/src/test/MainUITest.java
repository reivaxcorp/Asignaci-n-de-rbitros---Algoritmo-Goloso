package test;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import main.AlgoritmoGoloso;
import main.FixtureGenerador;
import main.MainUI;
import main.Partido;

public class MainUITest {

	private MainUI mainUI;
	private AlgoritmoGoloso algoritmoGolosoMain;
	private FixtureGenerador fixture;
	

	// ignoramos la generacion del arbicho en el disco
	// lo manejaremos en la memoria. 
	@Before
	public void setup() {
		this.mainUI = new MainUI();
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
	public void colocarDatosDeArchivoUI_fechasNull_expetion() {
		mainUI.colocarDatosDeAlgoritmoGolosoUI(null);
	}

	@Test(expected = RuntimeException.class)
	public void colocarDatosDeArchivoUI_fechasEmpty_expetion() {
		HashMap<Integer, ArrayList<Partido>> fechas = algoritmoGolosoMain.getFechas();
		fechas.clear();
		mainUI.colocarDatosDeAlgoritmoGolosoUI(fechas);
	}
	
	@Test(expected = RuntimeException.class)
	public void colocarDatosDeAlgoritmoGolosoUI_fechasNull_expetion() {
		mainUI.colocarDatosDeAlgoritmoGolosoUI(null);
	}

	@Test(expected = RuntimeException.class)
	public void colocarDatosDeAlgoritmoGolosoUI_fechasEmpty_expetion() {
		HashMap<Integer, ArrayList<Partido>> fechas = algoritmoGolosoMain.getFechas();
		fechas.clear();
		mainUI.colocarDatosDeAlgoritmoGolosoUI(fechas);
	}
	
}
