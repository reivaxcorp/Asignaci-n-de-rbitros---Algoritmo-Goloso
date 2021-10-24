import java.util.ArrayList;
import java.util.Random;


public class Fixture {

	private ArrayList<ArrayList<Partido>> fechas;
	private int cantFechas = 13;
	private int cantPartidosPorFecha = 0;
	private Random seleccionAleatoria;
	private ArrayList<String> equiposDisponibles;
	private ArrayList<Partido> equiposCreados;
	
	public Fixture() {
		
		
		
		this.fechas = new ArrayList<ArrayList<Partido>>(cantFechas);
		for (int fecha = 0; fecha < cantFechas; fecha++) this.fechas.add(new ArrayList<Partido>());
		this.equiposCreados = new ArrayList<Partido>();
		this.equiposDisponibles = new ArrayList<String>();
		this.seleccionAleatoria = new Random();
		crearListaEquipos();
		generarFechas(); 
		informacionUI();
	}


	private void informacionUI() {
		for(int fecha = 0; fecha <  fechas.size(); fecha ++) {
			for(int partidos = 0; partidos <  cantPartidosPorFecha; partidos ++) {
				System.out.println("fecha " +(fecha +1)+ "  "+ fechas.get(fecha).get(partidos).getLocal() + "  " +
						fechas.get(fecha).get(partidos).getVisitante());
				
			}
		}
	}

	
	private void crearListaEquipos() {
		
		if(Equipo.values().length % 2 != 0)
			throw new IllegalArgumentException("El numero de equipos debe ser par.");
		if(Equipo.values().length < 1)
			throw new IllegalArgumentException("La cantidad de equipos debe ser mayor que uno.");
		
		for(Equipo equipo: Equipo.values()) {
			equiposDisponibles.add(equipo.getNombreEquipo());
		}
		cantPartidosPorFecha = equiposDisponibles.size() / 2;
	}

	private void generarFechas() {
		
		for (int fecha = 0; fecha < cantFechas; fecha++) {
			for (int partido = 0; partido < cantPartidosPorFecha; partido++) {
				fechas.get(fecha).add(obtenerEquipo());			
			}
			crearListaEquipos(); // generamos de nuevo la lista para la siguiente fecha
		}
	}

	private Partido obtenerEquipo() {

		
		int posicionAleatoriaEquipoUno = seleccionAleatoria.nextInt(equiposDisponibles.size());
		String equipoUno = equiposDisponibles.get(posicionAleatoriaEquipoUno);
		equiposDisponibles.remove(posicionAleatoriaEquipoUno);

		int posicionAleatoriaEquipoDos = seleccionAleatoria.nextInt(equiposDisponibles.size());
		String equipoDos = equiposDisponibles.get(posicionAleatoriaEquipoDos);
		equiposDisponibles.remove(posicionAleatoriaEquipoDos);
		return new Partido(equipoUno, equipoDos);
		
	}

	enum Equipo {

		BOCA_JUNIORS("Boca Juniors"), RIVER_PLATE("River Plate"), TALLERES("Talleres"), HURACAN("Huracán"),
		COLON("Colón"), INDEPENDIENTE("Independiente"), RACING("Racing"), GODOY_CRUZ("Godoy Cruz"),
		GIMNASIA("Gimnasia"), UNION("Unión"), NEWELLS("Newell's"), SAN_LORENZO("San Lorenzo"), PATRONATO("Patronato"),
		BANFIELD("Banfield"), ARSENAL("Arsenal"), ARGENTINOS_JRS("Argentinos Juniors"), VELEZ("Vélez"),
		ESTUDIANTES("Estudiantes"), ROSARIO_CENTRAL("Rosario Central"), PLATENCE("Platence"), ALDOSIVI("Aldosivi"),
		CENTRAL_CORDOBA("Central Córdoba");

		private String nombreEquipo;

		private Equipo(String nombreEquipo) {
			this.nombreEquipo = nombreEquipo;
		}

		public String getNombreEquipo() {
			return nombreEquipo;
		}
	}

 
}