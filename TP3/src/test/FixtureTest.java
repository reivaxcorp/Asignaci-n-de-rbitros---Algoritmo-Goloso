package test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import main.Fixture;

public class FixtureTest {

	private Fixture fixture;
	
	@Before
	public void setup() {
		fixture = new Fixture();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void generarFechas_equiposImpares_exception() {
		fixture.agregarEquipo("El Torino");
		fixture.generarFechas(fixture.obtenerEquiposDisponibles());
	}
	@Test
	public void generarFechas_equiposPar() {
		fixture.agregarEquipo("San Miguel");
		fixture.agregarEquipo("Tigre");
		
		boolean res = fixture.generarFechas(fixture.obtenerEquiposDisponibles());
		assertEquals(true, res);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void agregarEquipo_existente() {
		fixture.agregarEquipo("San Miguel");
		fixture.agregarEquipo("San Miguel");
	}
	
	@Test
	public void equiposDisponibles_par() {
		int par = fixture.obtenerEquiposDisponibles().size();
		
		assertEquals(0, par % 2);
	}
	
}
