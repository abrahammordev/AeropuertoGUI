package Conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConection {

	static String url = "jdbc:mysql://localhost/";
	static String user = "root";
	static String password = "";

	public static Connection createDataBase() throws SQLException {
		Connection cnx = DriverManager.getConnection(url, user, password);
		Statement stm = cnx.createStatement();
		stm.executeUpdate("CREATE DATABASE IF NOT EXISTS aeropuerto2");
		stm.close();
		cnx.close();
		return DriverManager.getConnection(url + "aeropuerto2", user, password);

	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection((url + "aeropuerto2"), user, password);
	}

	public static void tablesCreation() {

		try (Connection cnx = getConnection();) {

			// Asumimos que la conexión se abre?

			cnx.setAutoCommit(false);

			Statement stm = cnx.createStatement();

			stm.addBatch("USE AEROPUERTO2");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS VUELOS (codigo VARCHAR(10) PRIMARY KEY NOT NULL, origen VARCHAR(100), destino VARCHAR(100), fechaSalida DATETIME, fechaLlegada DATETIME, duracion VARCHAR(100), numPlazas INT, completo BOOLEAN, numPasajeros INT)");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS AEROPUERTOS (codigo VARCHAR(3) PRIMARY KEY NOT NULL, nombre VARCHAR(100), ciudad VARCHAR(100))");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS AEROPUERTO_VUELO (codigoVuelo VARCHAR(10), nombreAeropuertoSalida VARCHAR(10), nombreAeropuertoLlegada VARCHAR(10),FOREIGN KEY (nombreAeropuertoSalida) REFERENCES AEROPUERTOS(codigo), FOREIGN KEY (codigoVuelo) REFERENCES VUELOS(codigo), FOREIGN KEY (nombreAeropuertoLlegada) REFERENCES AEROPUERTOS(codigo))");
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS PERSONAS (dni VARCHAR(100) PRIMARY KEY NOT NULL, nombre VARCHAR(100), apellido VARCHAR(100), fechaNacimiento DATE, telefono VARCHAR(100))");
			// editar para que haya 3 entradas en la tabla vuelo aeropuerto
			stm.addBatch(
					"CREATE TABLE IF NOT EXISTS VUELO_PERSONA (codigoVuelo VARCHAR(10) NOT NULL, dni VARCHAR(100) NOT NULL, FOREIGN KEY (codigoVuelo) REFERENCES VUELOS(codigo), FOREIGN KEY (dni) REFERENCES PERSONAS(dni))");

			int[] resultados = stm.executeBatch();

			for (int i = 0; i < resultados.length; i++) {
				if (resultados[i] == Statement.EXECUTE_FAILED) {
					System.out.println(
							"Ha fallado la creación de una tabla, porfavor vuelve a la creación de la Base de Datos");
					cnx.rollback();
				}

			}
			cnx.commit();

		} catch (SQLException e) {
			System.out.println("Un error se ha producido durante la conexión a la base de datos" + e.getMessage());

		}
	}

}
