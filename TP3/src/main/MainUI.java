package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class MainUI extends JFrame implements KeyListener {

	/**
	 * 
	 */
	public static int cantidadArbitrosPorDefecto = 12;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JScrollPane scrollPaneArchivoCargado;
	private JPanel panelArchivoCargado;
	private JPanel panelAlgoritmoGoloso;
	private JPanel panelEstadisticas;
	private JScrollPane scrollPaneAlgoritmoGoloso;
	private JScrollPane scrollPanelEstadisticas;
	private JButton btnGenerarAlgoritmo;
	private  Pattern patternCantArbitros;
	private final String EXTENCION = "json";
	private final String NOMBRE_ARCHIVO_JSON = "fechas.json";
	private int datosCargadosY = 10;
	private int datosGeneradosGolosoY = 10;

	private AlgoritmoGoloso golosoMain;
	private JTextField cantidadArbitrostField;
	private HashMap<Integer, ArrayList<Partido>> fechasUI;
	
	private ArrayList<Integer> partidoLabelIndex;
	private int indexCountPartidoLabel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI frame = new MainUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainUI() {
		
		this.patternCantArbitros = Pattern.compile("^[0]|\\D"); // true si es cero el primer caracter  y si no es un digito
		 
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\reiva\\Desktop\\Programacion III\\TP3\\icon.png"));
		setTitle("Generador de arbitros por equipos");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
	  
		
	    btnGenerarAlgoritmo = new JButton("Generar");
		btnGenerarAlgoritmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				 resetUIEstadisticas();
				 resetUIAlgoritmoGoloso();  
				 golosoMain.resetArbitros();
				 calcularAlgoritmoGoloso();
				 switchGenerarBtn();
			}
		});
		btnGenerarAlgoritmo.setBounds(539, 26, 89, 23);
		contentPane.add(btnGenerarAlgoritmo);
		
		JButton btnNewButton_1 = new JButton("Abrir Archivo");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirArchivo();
			}

		});
		btnNewButton_1.setBounds(92, 26, 190, 23);
		contentPane.add(btnNewButton_1);
		
		scrollPaneArchivoCargado = new JScrollPane();
		scrollPaneArchivoCargado.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneArchivoCargado.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneArchivoCargado.setBounds(24, 105, 263, 508);
		scrollPaneArchivoCargado.setWheelScrollingEnabled(true);
		contentPane.add(scrollPaneArchivoCargado);
		
	    scrollPaneAlgoritmoGoloso = new JScrollPane();
		scrollPaneAlgoritmoGoloso.setWheelScrollingEnabled(true);
		scrollPaneAlgoritmoGoloso.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneAlgoritmoGoloso.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPaneAlgoritmoGoloso.setBounds(311, 105, 448, 508);
		contentPane.add(scrollPaneAlgoritmoGoloso);
		
		scrollPanelEstadisticas = new JScrollPane();
		scrollPanelEstadisticas.setWheelScrollingEnabled(true);
		scrollPanelEstadisticas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanelEstadisticas.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPanelEstadisticas.setBounds(24, 624, 735, 126);
		contentPane.add(scrollPanelEstadisticas);
		
		JLabel lblNewLabel = new JLabel("Equipos sin arbitro");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(66, 71, 221, 23);
		contentPane.add(lblNewLabel);
		
		JLabel lblEquiposConArbitros = new JLabel("Equipos con arbitros");
		lblEquiposConArbitros.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblEquiposConArbitros.setBounds(465, 71, 221, 23);
		contentPane.add(lblEquiposConArbitros);
		
		cantidadArbitrostField = new JTextField();
		cantidadArbitrostField.setColumns(1);
		cantidadArbitrostField.addKeyListener(this);
	
		cantidadArbitrostField.setToolTipText("Cantidad de arbitros");
		cantidadArbitrostField.setBounds(422, 27, 86, 20);
		contentPane.add(cantidadArbitrostField);
		cantidadArbitrostField.setText(Integer.toString(cantidadArbitrosPorDefecto));
		
		JLabel lblNewLabelCantidadArbitros = new JLabel("Cantidad Arbitros");
		lblNewLabelCantidadArbitros.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabelCantidadArbitros.setForeground(Color.BLACK);
		lblNewLabelCantidadArbitros.setBounds(311, 30, 104, 14);
		contentPane.add(lblNewLabelCantidadArbitros);
		
		JButton resetButton = new JButton("Resetear");
		resetButton.setToolTipText("Resetea datos");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 resetUIFechasCargadas();
				 resetUIAlgoritmoGoloso();
				 resetUIEstadisticas();
				 inicializarLogicaTP();
				 calcularAlgoritmoGoloso();
				 switchGenerarBtn();
			}
		});
		resetButton.setBounds(670, 26, 89, 23);
		contentPane.add(resetButton);
		
	
		
		panelArchivoCargado = new JPanel();
		panelArchivoCargado.setLayout(null);
		
		panelAlgoritmoGoloso = new JPanel();
		panelAlgoritmoGoloso.setLayout(null);
		
		panelEstadisticas = new JPanel();
		panelEstadisticas.setLayout(null);
		
		partidoLabelIndex = new ArrayList<Integer>();
		
		inicializarLogicaTP();
		
	}

	/**
	 * Crea el archivo. json por defecto, luego crea la clase principal de nuestro algoritmo goloso. Luego procede a cargar los datos de nuestro archivo y la muestra en la interfaz. 
	 */
	 private void inicializarLogicaTP() {
		 FixtureGenerador fixtureGenerador = new FixtureGenerador();
		// Creamos archivo y fechas por defecto
		 this.golosoMain = new AlgoritmoGoloso();
		 cargarDatos(fixtureGenerador);
	}

	 /**
	  * Carga el archivo .json, y coloca los datos o las fechas, en nuestro algoritmo goloso para ser procesado.
	  */
	private void cargarDatos(FixtureGenerador fixtureGenerador) {
		  File file = crearArchivo(fixtureGenerador);
		  this.golosoMain.setFechas(ArchivoJSON.leerArchivo(file.getName()).getFechas());
		  colocarDatosDeArchivoUI(golosoMain.getFechas());
	}

	private File crearArchivo(FixtureGenerador fixtureGenerador) {
		new ArchivoJSON(fixtureGenerador.obtenerFechas(), NOMBRE_ARCHIVO_JSON);
		  File file = new File("./"+NOMBRE_ARCHIVO_JSON);
		return file;
	}
	
	/**
	 * Coloca los datos del archivo cargado en la interfaz, en el cuadro izquierdo. Recibe la fecha y la lista de partidos para esa fecha. Coloca la fecha y los partidos en cada llamada
	 */
	public void colocarDatosDeArchivoUI(HashMap<Integer, ArrayList<Partido>> fechas) {
		if(fechas != null && !fechas.isEmpty()) {
			for(int fecha = 0; fecha < fechas.size(); fecha ++) {
			  colocarfechasDeArchivoUI(fecha, fechas.get(fecha));
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
	}
	
	/**
	 * Muestra los resultados de nuestro algoritmo goloso en la interfaz, con la asignaci�n de �rbitros aleatoria.
	 */
	private void calcularAlgoritmoGoloso() {
		
		new Thread() {
			    public void run() {	
					fechasUI = golosoMain.generarAlgoritmoGoloso(
							   golosoMain.getFechas(),
							   cantidadArbitrosPorDefecto);
							
					colocarDatosDeAlgoritmoGolosoUI(fechasUI);
			    };
		 }.start();
		 
	}

	
	/**
	Coloca los datos del archivo cargado en la interfaz, en el cuadro izquierdo. Recibe la fecha y la lista de partidos para esa fecha. Coloca la fecha y los partidos en cada llamada.
 	*/
	public void colocarfechasDeArchivoUI(int fecha, ArrayList<Partido> partidos) {
		// fecha
		JLabel jlabel = new JLabel(
				"<html>" 
				+"<font color=\"red\">fecha "+ Integer.toString(fecha +1)+"</font>"	
				+"</html>" 
				);
		jlabel.setBounds(10, datosCargadosY, 100, 14);
		panelArchivoCargado.add(jlabel);

		// equipo
		int espacioLabelPartidos = datosCargadosY + 17;
		for(int partido = 0; partido < partidos.size(); partido ++) {
		
			JLabel partidoLabel = new JLabel(
					"<html>" 
					+partidos.get(partido).getLocal() 
					+"<font color=\"green\"> VS </font>"	
					+partidos.get(partido).getVisitante()
					+"</html>"
					);
			partidoLabel.setBounds(20, espacioLabelPartidos, 300, 14);
			espacioLabelPartidos += 17;
			panelArchivoCargado.add(partidoLabel);
			
		}
		datosCargadosY = espacioLabelPartidos;
		
		panelArchivoCargado.setPreferredSize(new Dimension(418, datosCargadosY));
		scrollPaneArchivoCargado.setViewportView(panelArchivoCargado);
		contentPane.updateUI();
		
	}
	
	/**
	Coloca los datos procesados en la interfaz, en el cuadro derecho. Recibe la fecha y la lista de partidos para esa fecha, pero con los �rbitros ya asignados. Coloca la fecha y los partidos en cada llamada, con sus respectivos �rbitros. 
	 */
	public void colocarfechasAlgoritmoGoloso(int fecha, ArrayList<Partido> partidos) {
		
				
				// fechas
				JLabel jlabel = new JLabel(
						"<html>" 
						+"<font color=\"red\">fecha "+ Integer.toString(fecha +1)+"</font>"	
						+"</html>" 
						);
				jlabel.setBounds(10, datosGeneradosGolosoY, 100, 14);
				panelAlgoritmoGoloso.add(jlabel);

				// equipos
				int espacioLabelPartidos = datosGeneradosGolosoY + 17;
				for(int partido = 0; partido < partidos.size(); partido ++) {
					
					String local = partidos.get(partido).getLocal();
					String visitante = partidos.get(partido).getVisitante();
					
					JButton partidoLocalButton = new JButton(local);
					JButton partidoVisitanteButton = new JButton(visitante);
					partidoLocalButton.setBounds(20, espacioLabelPartidos, 120, 14);
					partidoVisitanteButton.setBounds(partidoLocalButton.getX() + partidoLocalButton.getWidth(), espacioLabelPartidos, partidoLocalButton.getWidth(), 14);
					
					partidoLocalButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							resaltarEquipo(local);
							resetUIEstadisticas();
							mostrarEstadisticas(local);
						}
					});
					partidoVisitanteButton.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							resaltarEquipo(visitante);
							resetUIEstadisticas();
							mostrarEstadisticas(visitante);
						}
					});
					
					
					JLabel arbitro = new JLabel("<html><font color=\"blue\"> Arbitro: </font>"
							+partidos.get(partido).getArbitro()
							+"</html>");
					
					arbitro.setBounds(partidoVisitanteButton.getBounds().x+partidoVisitanteButton.getBounds().width, partidoVisitanteButton.getBounds().y, 80, partidoVisitanteButton.getBounds().height);
					panelAlgoritmoGoloso.add(partidoLocalButton);
					panelAlgoritmoGoloso.add(partidoVisitanteButton);
					panelAlgoritmoGoloso.add(arbitro);
					espacioLabelPartidos += 17;	
					
					partidoLabelIndex.add(indexCountPartidoLabel);
					indexCountPartidoLabel ++;
				}
				datosGeneradosGolosoY = espacioLabelPartidos;
				
				// datosGeneradosGoloso Y es la posicion, del ultimo item.
				panelAlgoritmoGoloso.setPreferredSize(new Dimension(scrollPaneAlgoritmoGoloso.getWidth(), datosGeneradosGolosoY));
				scrollPaneAlgoritmoGoloso.setViewportView(panelAlgoritmoGoloso);
				contentPane.updateUI();
		
	}
	
	/**
	 * Coloca las estad�sticas individuales de cada equipo al seleccionar un equipo del cuadro derecho con sus �rbitros procesados, en el cuadro inferior veremos los �rbitros y la cantidad de veces que fueron asignados esos �rbitros para ese equipo. 
	 */
	public void mostrarEstadisticas(String equipo) {
		
		HashMap<Integer, Integer> arbitros = new HashMap<Integer, Integer>();
		ArrayList<JLabel> labelsArbitros = new ArrayList<JLabel>();
		
		JLabel pEquipo = new JLabel("Equipo: " +equipo);
		
		pEquipo.setBounds(panelEstadisticas.getX(), panelEstadisticas.getY(), 500, 12);
		JLabel arbitrosTitulo = new JLabel("Cantidad de arbitros:");
		arbitrosTitulo.setBounds(pEquipo.getX(), pEquipo.getY() + 22, 500, 12);
		
		panelEstadisticas.add(pEquipo);
		panelEstadisticas.add(arbitrosTitulo);
		// inicializar
		int espaciolabelarbitro = pEquipo.getX();
		for(int arbitro = 1; arbitro <= cantidadArbitrosPorDefecto; arbitro++) {
			arbitros.put(arbitro, 0);
			JLabel arbitroLabel = new JLabel(String.valueOf(arbitro));
			arbitroLabel.setBounds(espaciolabelarbitro, arbitrosTitulo.getY() + 20, 50, 12);
			panelEstadisticas.add(arbitroLabel);
			espaciolabelarbitro += arbitroLabel.getWidth();
			labelsArbitros.add(arbitroLabel);
		}
		
		for(Integer fecha: fechasUI.keySet()) {
			for(Partido e: fechasUI.get(fecha)) {
				if(e.getLocal().equals(equipo) || e.getVisitante().equals(equipo)) {
					if(e.getArbitro() != 0)
						arbitros.put(e.getArbitro(), arbitros.get(e.getArbitro())+1);
				}
			}
		}
	
		//System.out.println(equipo);
		 for(int arbitro: arbitros.keySet()) {
			
			 for(JLabel label: labelsArbitros) {
				 
				 if(label.getText().equals(Integer.toString(arbitro))) {
					 JLabel cantVeces = new JLabel(String.valueOf(arbitros.get(arbitro)));
					 cantVeces.setForeground(Color.BLUE);
					 cantVeces.setFont(new Font("Tahoma", Font.BOLD, 14));
					 cantVeces.setBounds(label.getX(), label.getY()+16, label.getWidth(), label.getHeight());
					 panelEstadisticas.add(cantVeces);
	 
				 }
				
				 
			 }
		} 
		
		
		panelEstadisticas.setPreferredSize(new Dimension(espaciolabelarbitro, scrollPanelEstadisticas.getHeight()));
		scrollPanelEstadisticas.setViewportView(panelEstadisticas);
		contentPane.updateUI();
	}


	
   /**
	Coloca los datos procesados en la interfaz, en el cuadro derecho. Recibe la fecha y la lista de partidos para esa fecha, pero con los �rbitros ya asignados. Coloca la fecha y los partidos en cada llamada, con sus respectivos �rbitros
  */
	public void colocarDatosDeAlgoritmoGolosoUI(HashMap<Integer, ArrayList<Partido>> fechas) {
		if(fechas != null && !fechas.isEmpty()) {
			for(int fecha = 0; fecha < fechas.size(); fecha ++) {
			  colocarfechasAlgoritmoGoloso(fecha, fechas.get(fecha));
			}
		} else {
			throw new RuntimeException("Fechas incorrectas");
		}
		switchGenerarBtn();
	}
	
	/**
	 * Selecciona de la lista de �rbitros procesados, la selecci�n de nuestro equipo, y resaltara al equipo seleccionado en cada fecha. 
	 */
	private void resaltarEquipo(String equipo) {
		
		// reset seleccion
		for(Component c: panelAlgoritmoGoloso.getComponents()) {				
			if(c instanceof JButton) {
				JButton partido = (JButton) c; 
				partido.setForeground(null);
				partido.setBackground(null);
			}
		}
		
		ArrayList<JButton> jButtons = new ArrayList<JButton>();
		
		for(Component c: panelAlgoritmoGoloso.getComponents()) {				
				if(c instanceof JButton) {
					JButton partido = (JButton) c; 
					if(partido.getText().contains(equipo)) {
						jButtons.add(partido);
					}
				}		
		}
		
		for(JButton button : jButtons) {
			button.setForeground(Color.WHITE);
			button.setBackground(Color.BLACK);
		 }
	
		contentPane.updateUI();
	}
	
	/**
	 * Habilita y deshabilita nuestro bot�n de generar algoritmo goloso, cuando lo est� procesando lo deshabilita y cuando termino de procesar lo deshabilita. 
	 */
	private void switchGenerarBtn() {
		 btnGenerarAlgoritmo.setEnabled(!btnGenerarAlgoritmo.isEnabled());
	}
	
	/**
	 * Limpia los resultados, si generamos nuevo n�mero de �rbitros
	 */
	private void resetUIAlgoritmoGoloso() {
		panelAlgoritmoGoloso.removeAll();
		scrollPaneAlgoritmoGoloso.setViewportView(null);
		this.datosGeneradosGolosoY = 10;
	}
	
	/**
	 * Limpia las fechas cargadas del cuadro derecho, al abrir un nuevo archivo
	 */
	private void resetUIFechasCargadas() {
		panelArchivoCargado.removeAll();
		scrollPaneArchivoCargado.setViewportView(null);
		partidoLabelIndex.clear();
		indexCountPartidoLabel = 0;
		datosCargadosY = 10;
	}
	
	/**
	 * Borras las estadisticas del cuadro inferior, cuando seleccionamos otro nuevo equipo
	 */
	private void resetUIEstadisticas() {
		panelEstadisticas.removeAll();
		scrollPanelEstadisticas.setViewportView(null);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	   // TODO no implemented
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO no implemented
	}

	/**
	 * Lee los datos colocados en la caja de texto para ingresar el n�mero de �rbitros, verificara que la cantidad sea mayor a cero, con una expresi�n regular
	 */
	@SuppressWarnings("static-access")
	@Override
	public void keyReleased(KeyEvent e) {
		 Matcher disableGenerador = patternCantArbitros.matcher(cantidadArbitrostField.getText());
		 boolean off = disableGenerador.find();
		 if(off || cantidadArbitrostField.getText().isEmpty()) {
			 btnGenerarAlgoritmo.setEnabled(false);
		 } else {
			 btnGenerarAlgoritmo.setEnabled(true);
			 this.cantidadArbitrosPorDefecto = Integer.valueOf(cantidadArbitrostField.getText());
		 }
		
	}
	
	/**
	 * Abre un archivo .json, que corresponda al formato establecido por defecto.
	 */
	private void abrirArchivo() {
		
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./"));
		fc.setAcceptAllFileFilterUsed(false);
		
		fc.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "Solo Archivos *.json";
			}
			
			@Override
			public boolean accept(File f) {
				 if (f.isDirectory()) {
				        return true;
				    }

				    String extension = obtenerExtensionArchivo(f);
				    if (extension != null) {
				        if (extension.equals(EXTENCION)) {
				             return true;
				        } else {
				            return false;
				        }
				    }

				    return false;
			}
		});
		int returnVal = fc.showOpenDialog(contentPane);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			resetUIFechasCargadas();
			resetUIAlgoritmoGoloso();
            File file = fc.getSelectedFile();
            golosoMain.setFechas(ArchivoJSON.leerArchivo(file.getName()).getFechas());
            colocarDatosDeArchivoUI(golosoMain.getFechas());
        } 
	}
	
	/**
	 Obtenemos la extension de un archivo a abrir
	 */
	 private  String obtenerExtensionArchivo(File f) {
	        String ext = null;
	        String s = f.getName();
	        int i = s.lastIndexOf('.');

	        if (i > 0 &&  i < s.length() - 1) {
	            ext = s.substring(i+1).toLowerCase();
	        }
	        return ext;
	    }
}
