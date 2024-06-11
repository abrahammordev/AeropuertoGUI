package Service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Conection.DataBaseConection;

public class PersonaImplService {

	// Creacion de un objeto persona y utilizacion de prepare statement para
	// introducir los valores de la misma en la BD

	// Métodos para añadir personas a la bd
	// Debe pedir el código del vuelo en el que va para vincular los vuelos a las
	// personas
	// Asumimos que una persona puede ir a varios vuelos

	public static void añadirPersona(String dni, String nombre, String apellido, String fechaNacimiento,
			String telefono) {
		try (Connection cnx = DataBaseConection.getConnection()) {
			// Prepare statement para la introducción de datos
			String sentenciaSql = "INSERT INTO PERSONAS (dni, nombre ,apellido , fechaNacimiento, telefono) VALUES (?,?,?,?,?)";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// Establecer los valores para los parámetros
			prepareStatement.setString(1, dni);
			prepareStatement.setString(2, nombre);
			prepareStatement.setString(3, apellido);
			// Convertir la fecha de nacimiento a formato adecuado
			LocalDate fechaN = LocalDate.parse(fechaNacimiento, formatter);
			prepareStatement.setDate(4, Date.valueOf(fechaN));
			prepareStatement.setString(5, telefono);

			int afectedRows = prepareStatement.executeUpdate();

			if (afectedRows == 1) {
				System.out.println("Se ha introducido una persona adecuadamente");
			} else {
				System.out.println("No se ha introducido la persona");
			}
		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de introducir la persona" + e.getMessage());
		}
	}

	public static void añadirPasajeroVuelo(Scanner scanner) {

		try (Connection cnx = DataBaseConection.getConnection()) {

			System.out.println("Introduce el DNI de la persona que quieras añadir a un vuelo");
			String dni = scanner.nextLine();
			String sql = "INSERT INTO VUELO_PERSONA (codigoVuelo, dni) VALUES (?,?)";
			PreparedStatement stmPersonaVuelo = cnx.prepareStatement(sql);
			System.out.println("Introduce un código de vuelo al que lo quiere añadir");
			String codigoVuelo = scanner.nextLine();
			stmPersonaVuelo.setString(1, codigoVuelo);
			stmPersonaVuelo.setString(2, dni);
			int affectedRows = stmPersonaVuelo.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Se ha introducido adecuadamente la entrada");
				String sentenciaAumentarPasajeros = "UPDATE VUELOS SET numPasajeros = numPasajeros + 1 WHERE codigo = ?";
				stmPersonaVuelo.setString(1, codigoVuelo);
				PreparedStatement stmAumentarPasajeros = cnx.prepareStatement(sentenciaAumentarPasajeros);
				stmAumentarPasajeros.execute();
			} else {
				System.out.println("No se ha introducido ningún valor");
			}
		} catch (SQLException e) {
			System.out.println("Error al añadir una persona al vuelo" + e.getMessage());
		}
	}

	// Método para eliminar personas

	public static void eliminarPersona(Scanner scanner) {

		// Controlar que si noexi en la intermedia no tira error
		// controlar dni fehcas etc
		try (Connection cnx = DataBaseConection.getConnection()) {

			System.out.println("Introduce el DNI de la persona para eliminarla");

			int afectedRows = 0;

			String sentenciaSqlIntermedia = "DELETE FROM VUELO_PERSONA WHERE  dni = ?";
			PreparedStatement prepareStatementIntermedia = cnx.prepareStatement(sentenciaSqlIntermedia);

			String dni = scanner.nextLine();
			prepareStatementIntermedia.setString(1, dni);
			prepareStatementIntermedia.executeUpdate();
			// Prepare statement para la introducción de datos

			String sentenciaSql = "DELETE FROM PERSONAS WHERE  dni = ?";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);

			prepareStatement.setString(1, dni);
			afectedRows += prepareStatement.executeUpdate();

			if (afectedRows >= 1) {
				System.out.println("Se ha eliminado " + afectedRows + " persona adecuadamente");
			} else {
				System.out.println("Ha habido un problema al eliminar a la persona");
			}
			cnx.close();

		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de eliminar a la persona" + e.getMessage());

		}
	}

	// Método para mostrar personas

	public static void mostrarPersona(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			System.out.println("Introduce el DNI de la persona que deseas mostrar");
			int afectedRows = 0;

			// Prepare statement para la introducción de datos

			String sentenciaSql = "SELECT *  FROM PERSONAS WHERE  dni = ?";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);

			prepareStatement.setString(1, scanner.nextLine());
			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				System.out.println("DNI: " + rs.getString("dni") + " Código vuelo: " + rs.getString("codigoVuelo")
						+ ", Nombre: " + rs.getString("nombre") + ", Apellido: " + rs.getString("apellido")
						+ ", Fecha de nacimiento: " + rs.getDate("fechaNacimiento") + ", Telefono: "
						+ rs.getString("telefono"));
				afectedRows++;

			}
			if (afectedRows >= 1) {
				System.out.println("Se han devuelto " + afectedRows + " personas adecuadamente");
			} else {
				System.out.println("Ha habido un problema al mostrar personas");
			}
			cnx.close();
			scanner.close();
		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de devolver dichas personas" + e.getMessage());

		}
	}

	// Métodos para modificar personas
	public static void modificarDatosPersona(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			PreparedStatement stm = null;
			System.out.println("Introduce el DNI de la persona a la que quieres hacer alguna modificación");
			String codPersona = scanner.nextLine();

			System.out.println(
					"Introduce el valor que desea modificar.\nPuede modificar\n -> DNI\t ->Nombre\t -> Apellido\n -> fechaNacimiento\t -> telefono");
			String columActualizar = scanner.nextLine();

			System.out.println(
					"¿Qué dato quieres introducir? (Ten en cuenta el tipo de dato que introduces debe coincidir con la base de datos)");
			String valorIntroducido = scanner.nextLine();

			String sentenciaSql = "UPDATE PERSONAS SET " + columActualizar + " = ? WHERE dni = ?";

			stm = cnx.prepareStatement(sentenciaSql);
			stm.setString(1, valorIntroducido);
			stm.setString(2, codPersona);

			int affectedRows = stm.executeUpdate();

			if (affectedRows > 0) {
				System.out.println("La modificación ha alterado " + affectedRows + " filas");
			}

		} catch (SQLException e) {
			System.out.println("Ha habido un fallo a la hora de modificar los datos" + e.getMessage());
		}
	}
}
