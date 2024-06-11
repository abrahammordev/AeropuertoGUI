package Test;

import javax.swing.SwingUtilities;
import GUI.*;

import Conection.DataBaseConection;

public class Test {

	public static void main(String[] args) {
		// Inicializar la base de datos
		try {
			DataBaseConection.createDataBase();
			DataBaseConection.tablesCreation();
		} catch (Exception e) {
			System.err.println("Error al inicializar la base de datos: " + e.getMessage());
			e.printStackTrace();
		}

		// Inicializar la interfaz gráfica
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
		});
	}
	
	
}
