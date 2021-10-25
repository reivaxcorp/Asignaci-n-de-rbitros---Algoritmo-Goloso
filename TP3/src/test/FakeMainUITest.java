package test;
import java.util.ArrayList;

import main.Partido;
import main.onActualizaUI;

public class FakeMainUITest implements onActualizaUI{

	@Override
	public void colocarfechasDeArchivoUI(int fecha, ArrayList<Partido> partidos) {
		//System.out.println("Fake UPDATE");
	}

	@Override
	public void colocarfechasAlgoritmoGoloso(int fecha, ArrayList<Partido> partidos) {
		// TODO Auto-generated method stub
		
	}

}
