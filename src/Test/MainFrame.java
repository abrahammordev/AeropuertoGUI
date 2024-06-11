package Test;

import javax.swing.*;
import Conection.DataBaseConection;
import GUI.AeropuertoPanel;
import GUI.AñadirAeropuertoPanel;
import GUI.AñadirPersonaPanel;
import GUI.AñadirVueloPanel;
import GUI.ConsultarAeropuertoPanel;
import GUI.ConsultarPersonaPanel;
import GUI.ConsultarVueloPanel;
import GUI.EliminarAeropuertoPanel;
import GUI.EliminarPersonaPanel;
import GUI.EliminarVueloPanel;
import GUI.ModificarAeropuertoPanel;
import GUI.ModificarPersonaPanel;
import GUI.ModificarVueloPanel;
import GUI.PersonaPanel;
import GUI.VueloPanel;
import GUI.AñadirPasajeroVueloPanel;
import java.awt.*;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private CardLayout cardLayout;
	private JPanel cardPanel;

	public MainFrame() {
		setTitle("Sistema de Gestión");
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		cardPanel.add(new InicioPanel(this), "InicioPanel");
		cardPanel.add(new MainPanel(this), "MainPanel");
		cardPanel.add(new AeropuertoPanel(this), "AeropuertoPanel");
		cardPanel.add(new VueloPanel(this), "VueloPanel");
		cardPanel.add(new PersonaPanel(this), "PersonaPanel");
		cardPanel.add(new AñadirAeropuertoPanel(this), "AñadirAeropuertoPanel");
		cardPanel.add(new ModificarAeropuertoPanel(this), "ModificarAeropuertoPanel");
		cardPanel.add(new EliminarAeropuertoPanel(this), "EliminarAeropuertoPanel");
		cardPanel.add(new AñadirVueloPanel(this), "AñadirVueloPanel");
		cardPanel.add(new ModificarVueloPanel(this), "ModificarVueloPanel");
		cardPanel.add(new EliminarVueloPanel(this), "EliminarVueloPanel");
		cardPanel.add(new AñadirPersonaPanel(this), "AñadirPersonaPanel");
		cardPanel.add(new ModificarPersonaPanel(this), "ModificarPersonaPanel");
		cardPanel.add(new EliminarPersonaPanel(this), "EliminarPersonaPanel");
		cardPanel.add(new AñadirPasajeroVueloPanel(this), "AñadirPasajeroVueloPanel");
		cardPanel.add(new ConsultarPersonaPanel(this), "ConsultarPersonaPanel");
		cardPanel.add(new ConsultarVueloPanel(this), "ConsultarVueloPanel");
		cardPanel.add(new ConsultarAeropuertoPanel(this), "ConsultarAeropuertoPanel");

		add(cardPanel);
	}

	// MÉTODOS DE PERSONAS

	public static void añadirPersona(String dni, String nombre, String apellido, String fechaNacimiento,
			String telefono) {
		try (Connection cnx = DataBaseConection.getConnection()) {
			if (!confirmarDni(dni)) {
				throw new SQLException();
			}

			String sentenciaSql = "INSERT INTO aeropuerto2.PERSONAS (dni, nombre, apellido, fechaNacimiento, telefono) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);
			prepareStatement.setString(1, dni);
			prepareStatement.setString(2, nombre);
			prepareStatement.setString(3, apellido);
			prepareStatement.setString(4, fechaNacimiento);
			prepareStatement.setString(5, telefono);

			int affectedRows = prepareStatement.executeUpdate();

			if (affectedRows == 1) {
				JOptionPane.showMessageDialog(null,
						"Persona añadida correctamente:\n" + "DNI: " + dni + "\n" + "Nombre: " + nombre + "\n"
								+ "Apellido: " + apellido + "\n" + "Fecha de Nacimiento: " + fechaNacimiento + "\n"
								+ "Teléfono: " + telefono,
						"Éxito", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "No se ha podido añadir la persona.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Error al introducir la persona\nIntroduzca correctamente los datos ",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static boolean confirmarDni(String dni) {
		if (dni == null || dni.length() != 9)
			return false;

		String numerosDNI = dni.substring(0, 8);
		if (!numerosDNI.matches("[0-9]+"))
			return false;

		char letraControl = Character.toUpperCase(dni.charAt(8));
		if (!Character.isLetter(letraControl))
			return false;

		String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		int indice = Integer.parseInt(numerosDNI) % 23;
		char letraCalculada = letras.charAt(indice);

		return letraCalculada == letraControl;
	}

	public static void eliminarPersona(String dni) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			// Eliminar la persona de la tabla intermedia primero

			String sentenciaSqlIntermedia = "DELETE FROM VUELO_PERSONA WHERE dni = ?";
			PreparedStatement prepareStatementIntermedia = cnx.prepareStatement(sentenciaSqlIntermedia);
			prepareStatementIntermedia.setString(1, dni);
			prepareStatementIntermedia.executeUpdate();

			// Luego eliminar la persona de la tabla principal

			String sentenciaSql = "DELETE FROM PERSONAS WHERE dni = ?";
			PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql);
			prepareStatement.setString(1, dni);
			int affectedRows = prepareStatement.executeUpdate();

			if (affectedRows == 1) {
				JOptionPane.showMessageDialog(null, "Persona eliminada correctamente.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "No se ha podido eliminar la persona.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Error al eliminar la persona: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void consultarPersona(String nombrePersona, JTextArea resultadoArea) {
		try (Connection cnx = DataBaseConection.getConnection()) {
			String query = "SELECT nombre, apellido, fechaNacimiento, telefono FROM PERSONAS WHERE nombre = ?";
			try (PreparedStatement stm = cnx.prepareStatement(query)) {
				stm.setString(1, nombrePersona);
				ResultSet rs = stm.executeQuery();

				if (rs.next()) {
					String nombre = rs.getString("nombre");
					String apellido = rs.getString("apellido");
					String fechaNacimiento = rs.getString("fechaNacimiento");
					String telefono = rs.getString("telefono");

					String resultado = "Nombre: " + nombre + "\n" + "Apellido: " + apellido + "\n"
							+ "Fecha de Nacimiento: " + fechaNacimiento + "\n" + "Teléfono: " + telefono;

					resultadoArea.setText(resultado);
					JOptionPane.showMessageDialog(null, resultado, "Datos de la persona",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					resultadoArea.setText("No se encontró una persona con el nombre especificado.");
				}
			}
		} catch (SQLException e) {
			resultadoArea.setText("Error al consultar la persona: " + e.getMessage());
		}
	}

	public static void modificarDatosPersona(String dni, String nuevoNombre, String nuevoApellido, String nuevoTelefono)
			throws SQLException {
		try (Connection cnx = DataBaseConection.getConnection()) {
			String sentenciaSql = "UPDATE PERSONAS SET nombre = ?, apellido = ?, telefono = ? WHERE dni = ?";
			PreparedStatement stm = cnx.prepareStatement(sentenciaSql);
			stm.setString(1, nuevoNombre);
			stm.setString(2, nuevoApellido);
			stm.setString(3, nuevoTelefono);
			stm.setString(4, dni);

			int affectedRows = stm.executeUpdate();

			if (affectedRows > 0) {
				JOptionPane.showMessageDialog(null, "La persona ha sido modificada correctamente.",
						"Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "No se pudo modificar la persona.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException ex) {
			throw new SQLException("Error al modificar la persona: " + ex.getMessage(), ex);
		}
	}

	public static void añadirPasajeroVuelo(String dni, String codigoVuelo) throws SQLException {
		try (Connection conexion = DataBaseConection.getConnection()) {
			String sql = "INSERT INTO VUELO_PERSONA (codigoVuelo, dni) VALUES (?, ?)";
			PreparedStatement stmPersonaVuelo = conexion.prepareStatement(sql);
			stmPersonaVuelo.setString(1, codigoVuelo);
			stmPersonaVuelo.setString(2, dni);
			int affectedRows = stmPersonaVuelo.executeUpdate();
			if (affectedRows > 0) {
				String sentenciaAumentarPasajeros = "UPDATE VUELOS SET numPasajeros = numPasajeros + 1 WHERE codigo = ?";
				PreparedStatement stmAumentarPasajeros = conexion.prepareStatement(sentenciaAumentarPasajeros);
				stmAumentarPasajeros.setString(1, codigoVuelo);
				stmAumentarPasajeros.execute();
			} else {
				throw new SQLException("No se ha introducido ningún valor");
			}
		}
	}

	// METODOS DE VUELOS

	public static void añadirVuelo(String codigoVuelo, String codigoSalida, String codigoLlegada,
			LocalDateTime fechaSalida, LocalDateTime fechaLlegada, int numPlazas) {
		try (Connection cnx = DataBaseConection.getConnection()) {
			cnx.setAutoCommit(false);

			// Verificar que los aeropuertos de salida y llegada existan en la base de datos

			String queryVerificacionSalida = "SELECT codigo FROM AEROPUERTOS WHERE codigo = ?";
			String queryVerificacionLlegada = "SELECT codigo FROM AEROPUERTOS WHERE codigo = ?";

			try (PreparedStatement stmPrueba = cnx.prepareStatement(queryVerificacionSalida);
					PreparedStatement stmPrueba2 = cnx.prepareStatement(queryVerificacionLlegada)) {
				stmPrueba.setString(1, codigoSalida);
				stmPrueba2.setString(1, codigoLlegada);
				ResultSet rs1 = stmPrueba.executeQuery();
				ResultSet rs2 = stmPrueba2.executeQuery();

				if (!rs1.next() || !rs2.next()) {
					cnx.rollback();
					JOptionPane.showMessageDialog(null,
							"No se ha podido introducir el vuelo porque el aeropuerto de salida o llegada no existe en la base de datos",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Insertar datos del vuelo en la tabla VUELOS

			String sentenciaSql = "INSERT INTO VUELOS (codigo, origen, destino, fechaSalida, fechaLlegada, duracion, numPlazas, completo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement prepareStatement = cnx.prepareStatement(sentenciaSql)) {

				// Calcular la duración del vuelo

				Duration duracionVuelo = Duration.between(fechaSalida, fechaLlegada);

				prepareStatement.setString(1, codigoVuelo);
				prepareStatement.setString(2, codigoSalida);
				prepareStatement.setString(3, codigoLlegada);
				prepareStatement.setTimestamp(4, Timestamp.valueOf(fechaSalida));
				prepareStatement.setTimestamp(5, Timestamp.valueOf(fechaLlegada));
				prepareStatement.setString(6, String.valueOf(duracionVuelo));
				prepareStatement.setInt(7, numPlazas);
				prepareStatement.setBoolean(8, false);

				int affectedRows = prepareStatement.executeUpdate();

				if (affectedRows == 1) {

					// Insertar datos en la tabla intermedia AEROPUERTO_VUELO

					String consultaIntermedia = "INSERT INTO AEROPUERTO_VUELO (codigoVuelo, nombreAeropuertoSalida, nombreAeropuertoLlegada) VALUES (?, ?, ?)";
					try (PreparedStatement stmTablaIntermedia = cnx.prepareStatement(consultaIntermedia)) {
						stmTablaIntermedia.setString(1, codigoVuelo);
						stmTablaIntermedia.setString(2, codigoSalida);
						stmTablaIntermedia.setString(3, codigoLlegada);
						int afectedRowsIntermedia = stmTablaIntermedia.executeUpdate();

						if (afectedRowsIntermedia == 1) {

							cnx.commit();
							JOptionPane.showMessageDialog(null, "Vuelo añadido correctamente.", "Éxito",
									JOptionPane.INFORMATION_MESSAGE);
						} else {

							cnx.rollback();
							JOptionPane.showMessageDialog(null,
									"Ha habido un problema al introducir el vuelo en la tabla intermedia.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {

					cnx.rollback();
					JOptionPane.showMessageDialog(null, "No se pudo añadir el vuelo.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Error al añadir el vuelo: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void modificarDatosVuelo(String codigo, LocalDateTime nuevaFechaSalida,
			LocalDateTime nuevaFechaLlegada, String nuevoOrigen, String nuevoDestino) throws SQLException {
		try (Connection cnx = DataBaseConection.getConnection()) {

			// Verificar la existencia de los aeropuertos de origen y destino

			String queryVerificacionSalida = "SELECT codigo FROM AEROPUERTOS WHERE codigo = ?";
			String queryVerificacionLlegada = "SELECT codigo FROM AEROPUERTOS WHERE codigo = ?";

			boolean aeropuertoSalidaValido = false;
			boolean aeropuertoLlegadaValido = false;

			try (PreparedStatement stmVerifSalida = cnx.prepareStatement(queryVerificacionSalida);
					PreparedStatement stmVerifLlegada = cnx.prepareStatement(queryVerificacionLlegada)) {
				stmVerifSalida.setString(1, nuevoOrigen);
				stmVerifLlegada.setString(1, nuevoDestino);

				ResultSet rsSalida = stmVerifSalida.executeQuery();
				ResultSet rsLlegada = stmVerifLlegada.executeQuery();

				aeropuertoSalidaValido = rsSalida.next();
				aeropuertoLlegadaValido = rsLlegada.next();
			}

			if (!aeropuertoSalidaValido || !aeropuertoLlegadaValido) {
				JOptionPane.showMessageDialog(null,
						"No se pudo modificar el vuelo porque el aeropuerto de salida o llegada no existe en la base de datos",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Verificar que la duración del vuelo no sea mayor a un día

			Duration duracionVuelo = Duration.between(nuevaFechaSalida, nuevaFechaLlegada);
			if (duracionVuelo.toDays() > 1) {
				JOptionPane.showMessageDialog(null, "No se pudo modificar el vuelo porque la duración excede un día",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Si los aeropuertos existen y la duración es válida, se procede a la
			// actualización del vuelo

			String sentenciaSql = "UPDATE vuelos SET fechaSalida = ?, fechaLlegada = ?, origen = ?, destino = ?, duracion = ? WHERE codigo = ?";
			try (PreparedStatement stm = cnx.prepareStatement(sentenciaSql)) {
				stm.setTimestamp(1, Timestamp.valueOf(nuevaFechaSalida));
				stm.setTimestamp(2, Timestamp.valueOf(nuevaFechaLlegada));
				stm.setString(3, nuevoOrigen);
				stm.setString(4, nuevoDestino);
				stm.setString(6, codigo);
				stm.setString(5, String.valueOf(duracionVuelo));

				int affectedRows = stm.executeUpdate();

				if (affectedRows > 0) {
					JOptionPane.showMessageDialog(null, "El vuelo ha sido modificado correctamente.",
							"Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo modificar el vuelo.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException | DateTimeParseException ex) {
			throw new SQLException("Error al modificar el vuelo: ");
		}
	}

	public static void consultarVuelo(String codigoVuelo) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			// Consultar datos del vuelo en la tabla VUELOS

			String query = "SELECT * FROM VUELOS WHERE codigo = ?";
			try (PreparedStatement stm = cnx.prepareStatement(query)) {
				stm.setString(1, codigoVuelo);
				ResultSet rs = stm.executeQuery();

				if (rs.next()) {

					// Si se encontró el vuelo, se muestra la información

					String codigo = rs.getString("codigo");
					String origen = rs.getString("origen");
					String destino = rs.getString("destino");
					Timestamp fechaSalida = rs.getTimestamp("fechaSalida");
					Timestamp fechaLlegada = rs.getTimestamp("fechaLlegada");
					String duracion = rs.getString("duracion");
					int numPlazas = rs.getInt("numPlazas");
					boolean completo = rs.getBoolean("completo");

					String resultado = "Código: " + codigo + "\n" + "Origen: " + origen + "\n" + "Destino: " + destino
							+ "\n" + "Fecha de Salida: " + fechaSalida.toString() + "\n" + "Fecha de Llegada: "
							+ fechaLlegada.toString() + "\n" + "Duración: " + duracion + "\n" + "Número de Plazas: "
							+ numPlazas + "\n" + "Completo: " + completo;

					JOptionPane.showMessageDialog(null, resultado, "Información del Vuelo",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

					JOptionPane.showMessageDialog(null, "No se encontró un vuelo con el código especificado.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null, "Error al consultar el vuelo: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void eliminarVuelo(String codigoVuelo) throws SQLException {
		Connection cnx = null;
		try {
			cnx = DataBaseConection.getConnection();
			cnx.setAutoCommit(false);

			// Eliminar entradas de la tabla intermedia VUELO_PERSONA

			String deleteVueloPersona = "DELETE FROM VUELO_PERSONA WHERE codigoVuelo = ?";
			try (PreparedStatement stmt = cnx.prepareStatement(deleteVueloPersona)) {
				stmt.setString(1, codigoVuelo);
				stmt.executeUpdate();
			}

			// Eliminar entradas de la tabla intermedia AEROPUERTO_VUELO

			String deleteAeropuertoVuelo = "DELETE FROM AEROPUERTO_VUELO WHERE codigoVuelo = ?";
			try (PreparedStatement stmt = cnx.prepareStatement(deleteAeropuertoVuelo)) {
				stmt.setString(1, codigoVuelo);
				stmt.executeUpdate();
			}

			// Eliminar el vuelo de la tabla VUELOS

			String deleteVuelo = "DELETE FROM VUELOS WHERE codigo = ?";
			try (PreparedStatement stmt = cnx.prepareStatement(deleteVuelo)) {
				stmt.setString(1, codigoVuelo);
				int affectedRows = stmt.executeUpdate();

				if (affectedRows > 0) {
					cnx.commit();
					JOptionPane.showMessageDialog(null, "El vuelo ha sido eliminado correctamente.",
							"Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
				} else {
					cnx.rollback();
					JOptionPane.showMessageDialog(null, "No se pudo eliminar el vuelo.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException ex) {
			if (cnx != null) {
				try {

					// Revertir cambios en caso de error
					cnx.rollback();
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
			}
			throw new SQLException("Error al eliminar el vuelo: " + ex.getMessage(), ex);
		} finally {
			if (cnx != null) {
				try {
					cnx.setAutoCommit(true);
					cnx.close();
				} catch (SQLException closeEx) {
					closeEx.printStackTrace();
				}
			}
		}
	}

	// MÉTODOS AEROPUERTO

	public static void añadirAeropuerto(String codigo, String nombre, String ciudad) throws SQLException {
		try (Connection cnx = DataBaseConection.getConnection()) {
			cnx.setAutoCommit(false); // Iniciar transacción

			// Insertar nuevo aeropuerto

			String sentenciaSql = "INSERT INTO AEROPUERTOS (codigo, nombre, ciudad) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = cnx.prepareStatement(sentenciaSql)) {
				stmt.setString(1, codigo);
				stmt.setString(2, nombre);
				stmt.setString(3, ciudad);
				int affectedRows = stmt.executeUpdate();

				if (affectedRows == 1) {
					cnx.commit(); // Confirmar transacción
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo insertar el Aeropuerto.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException ex) {
			throw new SQLException("Error al añadir el aeropuerto: " + ex.getMessage(), ex);
		}
	}

	// Eliminar aeropuerto con metodo para la tabla intermedia

	public static String[] eliminarAeropuerto(String codigoAeropuerto) throws SQLException {
		Connection cnx = null;
		try {
			cnx = DataBaseConection.getConnection();
			cnx.setAutoCommit(false);

			// Eliminar de VUELO_PERSONA

			try (PreparedStatement psVueloPersona = cnx.prepareStatement(
					"DELETE FROM VUELO_PERSONA WHERE codigoVuelo IN (SELECT codigoVuelo FROM AEROPUERTO_VUELO WHERE nombreAeropuertoSalida = ? OR nombreAeropuertoLlegada = ?)")) {
				psVueloPersona.setString(1, codigoAeropuerto);
				psVueloPersona.setString(2, codigoAeropuerto);
				psVueloPersona.executeUpdate();
			}

			// Eliminar de AEROPUERTO_VUELO
			// Eliminar de VUELOS
			// Desactivar temporalmente la clave foránea en AEROPUERTO_VUELO

			try (PreparedStatement psDisableFK = cnx.prepareStatement("SET FOREIGN_KEY_CHECKS=0")) {
				psDisableFK.executeUpdate();
			}

			// Eliminar entradas de VUELOS que están relacionados con el aeropuerto

			try (PreparedStatement psDeleteVuelos = cnx.prepareStatement(
					"DELETE FROM VUELOS WHERE codigo IN (SELECT codigoVuelo FROM AEROPUERTO_VUELO WHERE nombreAeropuertoSalida = ? OR nombreAeropuertoLlegada = ?)")) {
				psDeleteVuelos.setString(1, codigoAeropuerto);
				psDeleteVuelos.setString(2, codigoAeropuerto);
				psDeleteVuelos.executeUpdate();
			}

			// Eliminar registros de AEROPUERTO_VUELO

			try (PreparedStatement psDeleteAeropuertoVuelo = cnx.prepareStatement(
					"DELETE FROM AEROPUERTO_VUELO WHERE nombreAeropuertoSalida = ? OR nombreAeropuertoLlegada = ?")) {
				psDeleteAeropuertoVuelo.setString(1, codigoAeropuerto);
				psDeleteAeropuertoVuelo.setString(2, codigoAeropuerto);
				psDeleteAeropuertoVuelo.executeUpdate();
			}

			// Volver a activar la clave foránea en AEROPUERTO_VUELO

			try (PreparedStatement psEnableFK = cnx.prepareStatement("SET FOREIGN_KEY_CHECKS=1")) {
				psEnableFK.executeUpdate();
			}

			// Eliminar de AEROPUERTOS

			try (PreparedStatement psAeropuerto = cnx.prepareStatement("DELETE FROM AEROPUERTOS WHERE codigo = ?")) {
				psAeropuerto.setString(1, codigoAeropuerto);
				psAeropuerto.executeUpdate();
			}

			String[] nombresAeropuertos = obtenerNombresAeropuertos(); // Obtener nombres de aeropuertos actualizados
			cnx.commit();
			return nombresAeropuertos; // Retornar nombres de aeropuertos actualizados
		} catch (SQLException ex) {
			if (cnx != null) {
				try {
					cnx.rollback();
				} catch (SQLException rollbackEx) {
					JOptionPane.showMessageDialog(null, "No se pudo volver atrás.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			throw ex;
		} finally {
			if (cnx != null) {
				try {
					cnx.close();
				} catch (SQLException closeEx) {
					JOptionPane.showMessageDialog(null, "No se pudo cerrar la conexión.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public static String[] obtenerNombresAeropuertos() throws SQLException {
		try (Connection conexion = DataBaseConection.getConnection();
				PreparedStatement stmt = conexion.prepareStatement("SELECT nombre FROM AEROPUERTOS")) {
			ResultSet rs = stmt.executeQuery();
			ArrayList<String> aeropuertos = new ArrayList<>();
			while (rs.next()) {
				aeropuertos.add(rs.getString("nombre"));
			}
			return aeropuertos.toArray(new String[0]);
		}
	}

	public static String consultarAeropuerto(String nombreAeropuerto) throws SQLException {
		try (Connection connection = DataBaseConection.getConnection()) {
			String sql = "SELECT codigo, nombre, ciudad FROM AEROPUERTOS WHERE nombre = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, nombreAeropuerto);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					String codigo = resultSet.getString("codigo");
					String nombre = resultSet.getString("nombre");
					String ciudad = resultSet.getString("ciudad");
					return "Código: " + codigo + "\nNombre: " + nombre + "\nCiudad: " + ciudad;
				} else {
					return "No se encontraron detalles para el aeropuerto seleccionado.";
				}
			}
		}
	}

	public static void modificarAeropuerto(String nombreAeropuerto, String nuevoNombre, String nuevaCiudad) {
		try (Connection cnx = DataBaseConection.getConnection()) {

			// Verificar la existencia del aeropuerto

			String queryVerificacion = "SELECT codigo FROM AEROPUERTOS WHERE nombre = ?";
			String codigoAeropuerto = null;

			try (PreparedStatement stmVerif = cnx.prepareStatement(queryVerificacion)) {
				stmVerif.setString(1, nombreAeropuerto);
				ResultSet rs = stmVerif.executeQuery();
				if (rs.next()) {
					codigoAeropuerto = rs.getString("codigo");
				}
			}

			if (codigoAeropuerto == null) {
				JOptionPane.showMessageDialog(null,
						"No se pudo modificar el aeropuerto porque no existe en la base de datos", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Si el aeropuerto existe, se procede a la actualización

			String sentenciaSql = "UPDATE AEROPUERTOS SET nombre = ?, ciudad = ? WHERE codigo = ?";
			try (PreparedStatement stm = cnx.prepareStatement(sentenciaSql)) {
				stm.setString(1, nuevoNombre);
				stm.setString(2, nuevaCiudad);
				stm.setString(3, codigoAeropuerto);

				int affectedRows = stm.executeUpdate();

				if (affectedRows > 0) {
					JOptionPane.showMessageDialog(null, "El aeropuerto ha sido modificado correctamente.",
							"Modificación exitosa", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "No se pudo modificar el aeropuerto.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al modificar el aeropuerto: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public void showCard(String cardName) {

		if ("EliminarVueloPanel".equals(cardName)) {
			EliminarVueloPanel eliminarVueloPanel = new EliminarVueloPanel(null);
			eliminarVueloPanel.cargarDatos();
		}
		cardLayout.show(cardPanel, cardName);

	}

}
