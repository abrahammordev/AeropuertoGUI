package Service;

import Conection.DataBaseConection;
import Exceptions.AeropuertoInexistenteException;
import Exceptions.DuracionVueloException;
import Exceptions.FechaVueloExption;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class VueloImplService {

	// Añadimos método para añadir un vuelo a la BD

	public static void añadirVuelo(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			// Seteo del automit false para poder hacer rollback en caso de que no exista
			// aeropuerto de salida o llegada en la bd

			cnx.setAutoCommit(false);
			System.out.println("Introduce los datos del vuelo que deseas añadir: ");

			// Verificamos que el aeropuerto con el que está enlazado el vuelo exista para
			// poder añadirlo

			System.out.println("Inserte el código del aeropuerto de salida");
			String codSalida = scanner.nextLine().trim();

			System.out.println("Inserte el código del aeropuerto de llegada");
			String codLLegada = scanner.nextLine().trim();

			String queryVerificacionSalida = "SELECT codigo FROM AEROPUERTOS WHERE codigo = ?";
			String queryVerificacionLlegada = "SELECT codigo FROM AEROPUERTOS WHERE codigo = ?";

			try (PreparedStatement stmPrueba = cnx.prepareStatement(queryVerificacionSalida);
					PreparedStatement stmPrueba2 = cnx.prepareCall(queryVerificacionLlegada)) {

				stmPrueba.setString(1, codSalida);
				stmPrueba2.setString(1, codLLegada);

				ResultSet rs1 = stmPrueba.executeQuery();
				ResultSet rs2 = stmPrueba2.executeQuery();

				if (!rs1.next() || !rs2.next()) {
					cnx.rollback();
					System.out.println(
							"No se ha podido introducir el vuelo porque el aeropuerto del que parte o al que va no existe en la base de datos");
					return;
				}
			}

			String sentenciaSql = "INSERT INTO VUELOS (codigo, origen, destino,fechaSalida, fechaLlegada, duracion , numPlazas, completo,numPasajeros ) VALUES (?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql)) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

				System.out.println("Introduce el código del vuelo");
				String codVuelo = scanner.nextLine();
				prepareStatement.setString(1, codVuelo);

				prepareStatement.setString(2, codSalida);
				prepareStatement.setString(3, codLLegada);

				// Asignación de fecha salida y llegada

				System.out
						.println("Introduce la fecha de salida del vuelo *ten en cuenta el formato yyyy-MM-dd HH:mm*");
				String fechaSalida = scanner.nextLine();
				LocalDateTime fechaS = (LocalDateTime.parse(fechaSalida, formatter));

				System.out
						.println("Introduce la fecha de llegada del vuelo *ten en cuenta el formato yyyy-MM-dd HH:mm*");
				String fechaLlegada = scanner.nextLine();
				LocalDateTime fechaL = (LocalDateTime.parse(fechaLlegada, formatter));

				// Calcular la duración del vuelo haciendo la resta entre la llegada y la salida

				Duration duracionVuelo = Duration.between(fechaS, fechaL);

				try {

					comprobadorFechaHora(fechaS, fechaL, duracionVuelo);
					System.out.println("Comprobación existosa de fecha y hora");
				} catch (FechaVueloExption | DuracionVueloException e) {

					System.err.println("Ha habido un error en: " + e.getMessage());
				}
				prepareStatement.setTimestamp(4, Timestamp.valueOf(fechaS));
				prepareStatement.setTimestamp(5, Timestamp.valueOf(fechaL));
				prepareStatement.setString(6, String.valueOf(duracionVuelo));

				System.out.println("Introduce el número de plazas del vuelo");
				int plazasVuelo = scanner.nextInt();
				prepareStatement.setInt(7, plazasVuelo);

				System.out.println("¿Con cuántos pasajeros quieres iniciar el vuelo?");
				int cantidadPasajeros = scanner.nextInt();
				prepareStatement.setInt(9, cantidadPasajeros);

				// Implementar aumento de pasasjeros en añadir pasajeros

				if (cantidadPasajeros > plazasVuelo) {
					System.out.println("El número de pasajeros no puede ser mayor al número de plazas del  vuelo");
				} else if (cantidadPasajeros == plazasVuelo) {

					prepareStatement.setBoolean(8, true);

				} else {

					prepareStatement.setBoolean(8, false);
				}

				int affectedRows = prepareStatement.executeUpdate();

				// Inserción en la tabla Aeropuerto - Vuelo

				System.out.println("El vuelo se ha introducido adecuadamente en la tabla VUELOS");

				String consultaIntermedia = ("INSERT INTO AEROPUERTO_VUELO (codigoVuelo, nombreAeropuertoSalida, nombreAeropuertoLlegada) VALUES (?,?,?)");

				try (PreparedStatement stmTablaIntermedia = cnx.prepareStatement(consultaIntermedia)) {

					// Vericación de datos?
					// Prepare statement para la introducción de datos
					stmTablaIntermedia.setString(1, codVuelo);
					stmTablaIntermedia.setString(2, codSalida);
					stmTablaIntermedia.setString(3, codLLegada);

					int afectedRowsIntemedia = stmTablaIntermedia.executeUpdate();

					if (affectedRows == 1 && afectedRowsIntemedia == 1) {
						System.out.println("Se ha introducido un vuelo adecuadamente");
					} else {
						System.out.println("Ha habido un problema al introducir su vuelo");

						// Inserción de datos en la tabla intermedia

					}
					cnx.commit();

				} catch (SQLException e) {
					System.out.println(
							"Ha habido un fallo en la base de datos a la hora de introducir su vuelo" + e.getMessage());

				}
			} catch (SQLException e) {
				System.out.println("Error de conexión a la base de datos: " + e.getMessage());
			}

		} catch (SQLException e) {
			System.out.println("Error de conexión a la base de datos: " + e.getMessage());

		}
	}

	// Método para comprobar fecha y hora del vuelo

	public static boolean comprobadorFechaHora(LocalDateTime fechaS, LocalDateTime fechaL, Duration duracion)
			throws FechaVueloExption, DuracionVueloException {
		if (fechaS.isAfter(fechaL)) {
			throw new FechaVueloExption("La fecha de salida del vuelo no puede ser después de su llegada");
		}
		if (duracion.toDays() > 1) {
			throw new DuracionVueloException("La duración del vuelo no puede ser superior a un día");
		}
		return true;
	}

	// Método para eliminar un vuelo

	public static void eliminarVuelo(Scanner scanner) {

		try (Connection cnx = DataBaseConection.getConnection()) {

			// Eliminar personas que tengan la clave del vuelo de forma recursiva

			System.out.println(
					"¿Quiere eliminar el vuelo a partir de su  código?\nSi desea eliminarlo por código -> 1\n*Si lo elimina a partir de su Ciudad de origen eliminará todos los vuelos que parten de la misma*");
			int select = scanner.nextInt();
			// Consumir línea

			scanner.nextLine();
			int afectedRows = 0;
			String sentenciaSql = "";
			// Prepare statement para la introducción de datos

			switch (select) {
			case 0:

				System.out.println("Introduce el origen del vuelo que deseas eliminar");
				sentenciaSql = "DELETE FROM VUELOS WHERE  origen = ?";
				PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);

				prepareStatement.setString(1, scanner.nextLine());

				afectedRows = prepareStatement.executeUpdate();
				break;
			case 1:

				String codVuelo = scanner.nextLine();
				borradoPersonaIntermedia(scanner, codVuelo);
				borradorVueloAeropuertoIntermedia(scanner, codVuelo);

				System.out.println("Introduce el código del vuelo que deseas eliminar");
				sentenciaSql = "DELETE FROM VUELOS WHERE  codigo = ? ";
				PreparedStatement prepareStatement2 = cnx.prepareStatement(sentenciaSql);

				prepareStatement2.setString(1, codVuelo);


				afectedRows = prepareStatement2.executeUpdate();
				break;
			default:
				System.out.println("No ha seleccionado una opción válida, retornando al origen del menú");
			}

			if (afectedRows >= 1) {
				System.out.println("Se han eliminado " + afectedRows + " vuelos adecuadamente");
			} else {
				System.out.println("Ha habido un problema al eliminar vuelos");
			}
			cnx.close();

		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de eliminar dichos vuelos" + e.getMessage());

		}

	}

	public static void borradoPersonaIntermedia(Scanner scanner, String codigoVuelo) throws SQLException {
		Connection cnx = DataBaseConection.getConnection();



		String borradoVueloPersona = "DELETE FROM VUELO_PERSONA WHERE codigoVuelo = ?";
		PreparedStatement stmBorradoVueloPersona = cnx.prepareStatement(borradoVueloPersona);
		stmBorradoVueloPersona.setString(1, codigoVuelo);
		stmBorradoVueloPersona.executeUpdate();



	}

	public static void borradorVueloAeropuertoIntermedia(Scanner scanner, String codigoVuelo) throws SQLException {
		Connection cnx = DataBaseConection.getConnection();

		String borradoVueloPersona = "DELETE FROM AEROPUERTO_VUELO WHERE codigoVuelo = ?";
		PreparedStatement stmBorradoVueloPersona = cnx.prepareStatement(borradoVueloPersona);
		stmBorradoVueloPersona.setString(1, codigoVuelo);
		stmBorradoVueloPersona.executeUpdate();

	}

	public static void mostrarVuelo(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			System.out.println(
					"¿Quiere buscar vuelos a partir de su origen, destino o a partir de su codigo?\nSi desea buscarlo por origen  -> 1\nSi desea buscarlo por destino -> 2\nSi desea buscarlo por código  -> 3");
			int select = 0;

			// Consumir línea

			int affectedRows = 0;
			String sentenciaSql = "";
			// Prepare statement para la introducción de datos
			PreparedStatement prepareStatement = null;
			boolean confirm2 = true;
			while (confirm2) {
				select = scanner.nextInt();
				scanner.nextLine();
				switch (select) {
				case 1:
					System.out.println("Introduce el origen de los vuelos que deseas buscar");
					sentenciaSql = "SELECT *  FROM VUELOS WHERE  origen = ?";
					prepareStatement = cnx.prepareStatement(sentenciaSql);

					prepareStatement.setString(1, scanner.nextLine());
					confirm2 = false;
					break;
				case 2:
					System.out.println("Introduce el destino de los vuelos que deseas buscar");
					sentenciaSql = "SELECT *  FROM VUELOS WHERE  destino = ?";
					prepareStatement = cnx.prepareStatement(sentenciaSql);

					prepareStatement.setString(1, scanner.nextLine());
					confirm2 = false;
					break;
				case 3:
					System.out.println("Introduce el código de los vuelos que deseas buscar");
					sentenciaSql = "SELECT *  FROM VUELOS WHERE  codigo = ?";
					prepareStatement = cnx.prepareStatement(sentenciaSql);

					prepareStatement.setString(1, scanner.nextLine());
					confirm2 = false;
					break;

				default:
					System.out.println("No ha seleccionado una opción válida, selecciona otro caso");
				}
			}

			ResultSet rs = prepareStatement.executeQuery();

			while (rs.next()) {
				System.out.println("Código: " + rs.getString("codigo") + ", Origen: " + rs.getString("origen")
						+ ", Destino: " + rs.getString("destino") + ", Fecha de salida: " + rs.getDate("fechaSalida")
						+ ", Fecha de llegada: " + rs.getDate("fechaLlegada") + ", Duración: "
						+ rs.getString("duracion") + ", Número de plazas: " + rs.getInt("numPlazas") + ", Completo: "
						+ rs.getBoolean("completo") + ", Número de pasajeros: " + rs.getInt("numPasajeros"));
				affectedRows++;

			}
			if (affectedRows == 0) {
				System.out.println("No se encontraron vuelos con los criterios de búsqueda especificados.");
			} else {
				System.out.println("Se han devuelto " + affectedRows + " vuelos adecuadamente.");
			}

		} catch (SQLException e) {
			System.out.println(
					"Ha habido un fallo en la base de datos a la hora de devolver los vuelos: " + e.getMessage());

		}

	}

	// Metodo que pueda modificar algun atrib del vuelo

	public static void modificarVuelo(Scanner scanner) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			PreparedStatement stm = null;
			List<String> columnasValidas = Arrays.asList("origen", "destino", "fechaSalida", "fechaLlegada", "duracion",
					"numPlazas", "numPasajeros");

			System.out.println("Introduce el código del vuelo del que quieres hacer alguna modificación");
			String codVuelo = scanner.nextLine().trim();

			System.out.println(
					"Introduce el valor que desea modificar.\nPuede modificar\n\t ->origen\t -> destino\n -> fechaSalida\t -> fechaLlegada\t -> duracion\n -> numPlazas\t -> numPasajeros");
			String columActualizar = scanner.nextLine().trim();

			// Verificar si la columna actualizada es compatible con la BD

			if (columnasValidas.contains(columActualizar)) {
				System.out.println(
						"¿Qué dato quieres introducir? (Ten en cuenta el tipo de dato que introduces debe coincidir con la base de datos)");
				String valorIntroducido = scanner.nextLine().trim();

				String sentenciaSql = "UPDATE vuelos SET " + columActualizar + " = ? WHERE codigo = ?";

				stm = cnx.prepareStatement(sentenciaSql);
				stm.setString(1, valorIntroducido);
				stm.setString(2, codVuelo);

				int affectedRows = stm.executeUpdate();

				if (affectedRows > 0) {
					System.out.println("La modificación ha alterado " + affectedRows + " filas");
				}
			}

		} catch (SQLException e) {
			System.out.println("Ha habido un fallo a la hora de modificar los datos");
		}
	}
}
