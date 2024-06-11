package Service;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

import Conection.DataBaseConection;

public class AeropuertoImplService {
	// Menu para introducir , alterar , borrar o cambiar datos de aeropuertos
	// Creacion de un objeto aeropuerto e inserción del mismo a través de un prepare
	// statement en la bd

	// Menú de primera aparición para seleccionar la acción deseada

	// Método para añadir aeropuerto
	public static void añadirAeropuerto(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			cnx.setAutoCommit(false);
			System.out.println("Introduce los datos del aeropuerto que deseas añadir");

			// Vericación de datos?
			// Prepare statement para la introducción de datos
			String sentenciaSql = "INSERT INTO AEROPUERTOS (codigo,nombre, ciudad) VALUES (?,?,?)";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);

			System.out.println("Introduce el código del aeropuerto");
			prepareStatement.setString(1, scanner.nextLine());

			System.out.println("Introduce el nombre del aeropuerto");
			prepareStatement.setString(2, scanner.nextLine());

			System.out.println("Introduce la ciudad del aeropuerto");
			prepareStatement.setString(3, scanner.nextLine());

			int afectedRows = prepareStatement.executeUpdate();

			if (afectedRows == 1) {
				System.out.println("Se ha introducido un aeropuerto adecuadamente");
				cnx.commit();
			} else {
				System.out.println("Ha habido un problema al introducir el aeropuerto");
			}

		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de introducir su aeropuerto" + e.getMessage());

		}
	}

	// Método para eliminar aeropuerto

	public static void eliminarAeropuerto(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			// Implementar borrado recursivo para eliminar vuelos cuando el aeropuerto es
			// eliminado
			// eliminar también los valores en la tabla unión

			System.out.println(
					"¿Quiere eliminar el aeropuerto a partir de su nombre o a partir de su código?\nSi desea eliminarlo por nombre -> 0\nSi desea eliminarlo por código -> 1");
			int select = scanner.nextInt();
			scanner.nextLine();
			int afectedRows = 0;
			String sentenciaSql = "";
			// Prepare statement para la introducción de datos

			switch (select) {
			case 0:
				System.out.println("Introduce la ciudad del vuelo que deseas eliminar");
				sentenciaSql = "DELETE FROM AEROPUERTOS WHERE  ciudad = ?";
				PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);

				prepareStatement.setString(1, scanner.nextLine());

				afectedRows = prepareStatement.executeUpdate();
				break;
			case 1:
				System.out.println("Introduce el codigo del aeropuerto que deseas eliminar");
				String codAeropuerto = scanner.nextLine();

				// Borrado recursivo
				// Primero debemos borrar los vuelos de la tabla intermedia con persona , luego
				// los vuelos de la tabla intermedia Vuelo_Aeropuerto luego los de la tabla
				// vuelos y por último los de la
				// tabla Aeropuerto

				// Borrado de intermedia de vuelo-persona

				borradoTablaVueloPersona(scanner, codAeropuerto);

				sentenciaSql = "DELETE FROM AEROPUERTOS WHERE  codigo = ?";
				PreparedStatement prepareStatement2 = cnx.prepareStatement(sentenciaSql);

				prepareStatement2.setString(1, codAeropuerto);

				afectedRows = prepareStatement2.executeUpdate();
				break;
			default:
				System.out.println("No ha seleccionado una opción válida, retornando al origen del menú");
			}

			if (afectedRows >= 1) {
				System.out.println("Se han eliminado " + afectedRows + " aeropuertos adecuadamente");
			} else {
				System.out.println("No se ha encontrado el aeropuerto con el código proporcionado");
			}

		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de eliminar dichos aeropuertos" + e.getMessage());

		}
	}

	public static void borradorAeropuertoIntermedia(Scanner scanner, String codAeropuerto) throws SQLException {
		Connection cnx = DataBaseConection.getConnection();

		String obtenerCodigoVuelo = "DELETE FROM AEROPUERTO_VUELO WHERE nombreAeropuertoSalida = ? or nombreAeropuertoLlegada = ?";
		PreparedStatement stmObtenerCodVuelo = cnx.prepareStatement(obtenerCodigoVuelo);
		stmObtenerCodVuelo.setString(1, codAeropuerto);
		stmObtenerCodVuelo.setString(2, codAeropuerto);
		stmObtenerCodVuelo.executeUpdate();

	}

	public static void borradoTablaVueloPersona(Scanner scanner, String codAeropuerto) throws SQLException {
		Connection cnx = DataBaseConection.getConnection();

		String obtenerCodigoVuelo = "SELECT codigoVuelo FROM AEROPUERTO_VUELO WHERE nombreAeropuertoSalida = ? or nombreAeropuertoLlegada = ?";
		PreparedStatement stmObtenerCodVuelo = cnx.prepareStatement(obtenerCodigoVuelo);
		stmObtenerCodVuelo.setString(1, codAeropuerto);
		stmObtenerCodVuelo.setString(2, codAeropuerto);
		ResultSet rs = stmObtenerCodVuelo.executeQuery();

		while (rs.next()) {
			String codVuelo = rs.getString("codigoVuelo");

			String sentenciaSql = "DELETE FROM VUELOS WHERE  codigo = ? ";
			PreparedStatement prepareStatement2 = cnx.prepareStatement(sentenciaSql);
			prepareStatement2.setString(1, codVuelo);

			PreparedStatement stmBorradoVueloPersona = cnx
					.prepareStatement("DELETE  FROM VUELO_PERSONA WHERE codigoVuelo = ?");
			stmBorradoVueloPersona.setString(1, codVuelo);

			// funciona
			stmBorradoVueloPersona.executeUpdate();

			// falla porque debe borrarse primerode V-A
			borradorAeropuertoIntermedia(scanner, codAeropuerto);

			// borrado V
			prepareStatement2.executeUpdate();
		}

	}

	public static void modificarAeropuerto(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			PreparedStatement stm = null;
			System.out.println("Introduce el código del vuelo del que quieres hacer alguna modificación");
			String codVuelo = scanner.nextLine();

			System.out.println("Introduce el valor que desea modificar.\nPuede modificar\n\t ->nombre\t -> ciudad");
			boolean confirm = true;
			String columActualizar = "";

			while (confirm) {
				columActualizar = scanner.nextLine();
				if (columActualizar.equalsIgnoreCase("nombre") || columActualizar.equalsIgnoreCase("ciudad")) {
					System.out.println(
							"¿Qué dato quieres introducir? (Ten en cuenta el tipo de dato que introduces debe coincidir con la base de datos)");
					confirm = false;
				} else {
					System.out.println("Has introducido un dato incorrecto con respecto a la columna a notificar");
				}

			}

			String valorIntroducido = scanner.nextLine();

			String sentenciaSql = "UPDATE AEROPUERTOS SET " + columActualizar + " = ? WHERE codigo = ?";

			stm = cnx.prepareStatement(sentenciaSql);
			stm.setString(1, valorIntroducido);
			stm.setString(2, codVuelo);

			int affectedRows = stm.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("La modificación ha alterado " + affectedRows + " filas");
			}

		} catch (SQLException e) {
			System.out.println("Ha habido un fallo a la hora de modificar los datos del aeropuertos");
		}
	}

	public static void mostrarAeropuerto(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			System.out.println("Introduce el código del aeropuerto para mostrarlo");
			int afectedRows = 0;
			// Prepare statement para la introducción de datos

			String sentenciaSql = "SELECT *  FROM AEROPUERTOS WHERE  codigo = ?";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);

			prepareStatement.setString(1, scanner.nextLine());

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				System.out.println("Código: " + rs.getString("codigo") + ", Nombre: " + rs.getString("nombre")
						+ ", Ciudad: " + rs.getString("ciudad"));
				afectedRows++;

			}
			if (afectedRows >= 1) {
				System.out.println("Se han devuelto " + afectedRows + " aeropuertos adecuadamente");
			} else {
				System.out.println("Ha habido un problema al mostrar el aeropuerto");
			}
			cnx.close();

		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de mostrar dichos aeropuerto" + e.getMessage());

		}

	}

}
