package main;
import java.util.ArrayList;
import java.util.Random;


public class FixtureGenerador {

	private ArrayList<ArrayList<Partido>> fechas;
	private static int cantFechas = 13;
	private int cantPartidosPorFecha = 0;

	private Random seleccionAleatoria;
	private ArrayList<String> equiposDisponibles;
	
	public FixtureGenerador() {
		
		this.fechas = new ArrayList<ArrayList<Partido>>(cantFechas);
		for (int fecha = 0; fecha < cantFechas; fecha++) this.fechas.add(new ArrayList<Partido>());
		this.equiposDisponibles = new ArrayList<String>();
		this.seleccionAleatoria = new Random();
		crearListaPorDefectoEquipos();
		generarFechas(equiposDisponibles); 
	}

	/**
	 * Crea la lista de equipos de defecto por medio del enum de nombre Equipos. 
	 */
	private void crearListaPorDefectoEquipos() {

		for(Equipos equipo: Equipos.values()) {
			agregarEquipo(equipo.getNombreEquipo());
		}
		actualizarCantidadPartidos();
	}

	/**
	Genera las fechas, siempre que los equipos agregados sean pares. 	
	 */
	@SuppressWarnings("unchecked")
	public boolean generarFechas(ArrayList<String> equiposDisponibles) {
		
		if(equiposDisponibles.size() % 2 != 0)
			throw new IllegalArgumentException("El numero de equipos debe ser par.");
		if(equiposDisponibles.size() < 2)
			throw new IllegalArgumentException("La cantidad de equipos debe ser mayor que uno.");
		
		for (int fecha = 0; fecha < cantFechas; fecha++) {
			ArrayList<String> equipos =  (ArrayList<String>) equiposDisponibles.clone();
			for (int partido = 0; partido < cantPartidosPorFecha; partido++) {
				fechas.get(fecha).add(armarEquipo(equipos));			
			}
		}
		return true;
	}
	
	/**
	Agrega un equipo, siempre que no exista en los equipos agregados y sea un nombre con longitud mayor a tres caracteres.
	 */
	public void agregarEquipo(String equipo) {
		
		if(equipo.equals("") && equipo.length() < 3)
			throw new IllegalArgumentException("El nombre del equipo es invalido");
		if(existeEquipo(equipo))
			throw new IllegalArgumentException("El equipo ya existe");
		
		equiposDisponibles.add(equipo);
		
		actualizarCantidadPartidos();
	}

	/**
	Arma los equipos en forma aleatoria, siempre removiendo el que se seleccion� y eligiendo nuevamente su rival
	 */
	private Partido armarEquipo(ArrayList<String> equipos) {

		int posicionAleatoriaEquipoUno = seleccionAleatoria.nextInt(equipos.size());
		String equipoUno = equipos.get(posicionAleatoriaEquipoUno);
		equipos.remove(posicionAleatoriaEquipoUno);

		int posicionAleatoriaEquipoDos = seleccionAleatoria.nextInt(equipos.size());
		String equipoDos = equipos.get(posicionAleatoriaEquipoDos);
		equipos.remove(posicionAleatoriaEquipoDos);
		
		if(equipoUno.equals(equipoDos))
			throw new RuntimeException("Los equipos son iguales");
		
		return new Partido(equipoUno, equipoDos, -1);
		
	}
	
	private void actualizarCantidadPartidos() {
		cantPartidosPorFecha = equiposDisponibles.size() / 2;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Partido>> obtenerFechas() {
		return (ArrayList<ArrayList<Partido>>) fechas.clone();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> obtenerEquiposDisponibles() {
		return (ArrayList<String>) equiposDisponibles.clone();
	}

	private boolean existeEquipo(String equipo) {
		return equiposDisponibles.contains(equipo);
	}
	
	public static int getCantFechas() {
		return cantFechas;
	}

	public int getCantPartidosPorFecha() {
		return cantPartidosPorFecha;
	}

	// equipos por defecto
	// la cantidad de partidos dependera de la cantidad de quipos divido dos.
	private enum Equipos {

		BOCA_JUNIORS("Boca Juniors"), RIVER_PLATE("River Plate"), TALLERES("Talleres"), HURACAN("Hurac�n"),
		COLON("Col�n"), INDEPENDIENTE("Independiente"), RACING("Racing"), GODOY_CRUZ("Godoy Cruz"),
		GIMNASIA("Gimnasia"), UNION("Uni�n"), NEWELLS("Newell's"), SAN_LORENZO("San Lorenzo"), PATRONATO("Patronato"),
		BANFIELD("Banfield"), ARSENAL("Arsenal"), ARGENTINOS_JRS("Argentinos Juniors"), VELEZ("V�lez"),
		ESTUDIANTES("Estudiantes"), ROSARIO_CENTRAL("Rosario Central"), PLATENCE("Platence"), ALDOSIVI("Aldosivi"),
		CENTRAL_CORDOBA("Central C�rdoba");

		private String nombreEquipo;

		private Equipos(String nombreEquipo) {
			this.nombreEquipo = nombreEquipo;
		}

		public String getNombreEquipo() {
			return nombreEquipo;
		}
	}

 
}