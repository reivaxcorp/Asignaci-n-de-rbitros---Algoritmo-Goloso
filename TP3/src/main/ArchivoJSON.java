package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ArchivoJSON {

	private HashMap<Integer, ArrayList<Partido>> datoJson;
	
	public ArchivoJSON(ArrayList<ArrayList<Partido>> fechas, String nombreArchivo) {
		
		this.datoJson = new HashMap<Integer, ArrayList<Partido>>();
		
		for (int fecha = 0; fecha < fechas.size(); fecha++) {
			datoJson.put(fecha, fechas.get(fecha));
		}
		
		generarJSON(nombreArchivo);
	}
	
	public void generarJSON(String archivo) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);
		
		try {
			FileWriter writer = new FileWriter(archivo);
			writer.write(json);
			writer.close();
		} catch (Exception e) {
			System.out.println("Error "  + e);
		}
	}
	public static ArchivoJSON leerArchivo(String archivo) {
		Gson gson = new Gson();
		ArchivoJSON ret = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(archivo));
			ret = gson.fromJson(br, ArchivoJSON.class);
		} catch (Exception e) {
			System.out.println("Error "  + e);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<Integer, ArrayList<Partido>> getFechas() 
	{
		return (HashMap<Integer, ArrayList<Partido>>) datoJson.clone();
	}
	
	
}
