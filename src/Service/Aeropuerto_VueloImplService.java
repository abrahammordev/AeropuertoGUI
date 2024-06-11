package Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import Conection.DataBaseConection;

public class Aeropuerto_VueloImplService {

	// Inserción en la tabla intermedia cuando se inserta un vuelo y tanto el código
	// del aeropuerto de salida como el de llegada coinciden con aeropuertos
	// existentes en la DB

	public static void añadirTablaIntermediaVA(Scanner scanner) throws SQLException {
		Connection cnx = DataBaseConection.getConnection();
		cnx.setAutoCommit(false);
		
		
	}
}
