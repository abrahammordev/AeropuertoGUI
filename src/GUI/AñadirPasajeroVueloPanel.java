package GUI;

import javax.swing.*;

import Conection.DataBaseConection;
import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AñadirPasajeroVueloPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> personaComboBox;
	private static JComboBox<String> vueloComboBox;

	public AñadirPasajeroVueloPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Añadir Pasajero a Vuelo");
		MainPanel.cambiarFuente(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Seleccionar Persona:"), gbc);
		personaComboBox = new JComboBox<>();
		gbc.gridx = 1;
		formPanel.add(personaComboBox, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Seleccionar Vuelo:"), gbc);
		vueloComboBox = new JComboBox<>();
		gbc.gridx = 1;
		formPanel.add(vueloComboBox, gbc);

		JButton añadirButton = new JButton("Añadir");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(añadirButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("PersonaPanel"));
		buttonPanel.add(volverButton);
		;

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		// Llamar al método para cargar los datos en los JComboBox
		cargarDatos();

		añadirButton.addActionListener(e -> {
			String dni = (String) personaComboBox.getSelectedItem();
			String codigoVuelo = (String) vueloComboBox.getSelectedItem();
			try {
				MainFrame.añadirPasajeroVuelo(dni, codigoVuelo);
				cargarDatos(); // Recargar los datos en los JComboBox después de la modificación
				JOptionPane.showMessageDialog(this,
						"Pasajero añadido correctamente al vuelo. DNI: " + dni + " Vuelo: " + codigoVuelo);
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Error al añadir pasajero al vuelo: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}

	public static void cargarDatos() {
		personaComboBox.removeAllItems();
		vueloComboBox.removeAllItems();
		try (Connection conexion = DataBaseConection.getConnection();
				PreparedStatement pstmtPersonas = conexion.prepareStatement("SELECT dni FROM PERSONAS");
				PreparedStatement pstmtVuelos = conexion.prepareStatement("SELECT codigo FROM VUELOS")) {

			ResultSet rsPersonas = pstmtPersonas.executeQuery();
			while (rsPersonas.next()) {
				personaComboBox.addItem(rsPersonas.getString("dni"));
			}

			ResultSet rsVuelos = pstmtVuelos.executeQuery();
			while (rsVuelos.next()) {
				vueloComboBox.addItem(rsVuelos.getString("codigo"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("persona.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}