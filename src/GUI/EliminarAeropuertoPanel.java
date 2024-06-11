package GUI;

import javax.swing.*;
import Conection.DataBaseConection;
import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.sql.*;

public class EliminarAeropuertoPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> aeropuertoComboBox;

	public EliminarAeropuertoPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Eliminar Aeropuerto");
		MainPanel.cambiarFuente(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Seleccionar Aeropuerto:"), gbc);
		aeropuertoComboBox = new JComboBox<>();
		gbc.gridx = 1;
		formPanel.add(aeropuertoComboBox, gbc);

		JButton eliminarButton = new JButton("Eliminar");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(eliminarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("AeropuertoPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		cargarInformacion();

		eliminarButton.addActionListener(e -> {
			String nombreAeropuerto = (String) aeropuertoComboBox.getSelectedItem();
			if (nombreAeropuerto != null) {
				int confirm = JOptionPane.showConfirmDialog(this,
						"¿Está seguro de que desea eliminar el aeropuerto " + nombreAeropuerto + "?",
						"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					try {
						// Modificar esta línea para obtener el código del aeropuerto en lugar del
						// nombre
						String codigoAeropuerto = obtenerCodigoAeropuerto(nombreAeropuerto);
						MainFrame.eliminarAeropuerto(codigoAeropuerto);
						JOptionPane.showMessageDialog(this, "Aeropuerto eliminado correctamente.", "Éxito",
								JOptionPane.INFORMATION_MESSAGE);

					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(this, "Error al eliminar el aeropuerto: " + ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un aeropuerto para eliminar.", "Advertencia",
						JOptionPane.WARNING_MESSAGE);
			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}

	public static void cargarInformacion() {

		aeropuertoComboBox.removeAllItems();
		try (Connection connection = DataBaseConection.getConnection()) {
			String sql = "SELECT nombre FROM aeropuertos";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					aeropuertoComboBox.addItem(resultSet.getString("nombre"));
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar la información: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String obtenerCodigoAeropuerto(String nombreAeropuerto) throws SQLException {
		String codigoAeropuerto = null;
		try (Connection connection = DataBaseConection.getConnection()) {
			String sql = "SELECT codigo FROM aeropuertos WHERE nombre = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, nombreAeropuerto);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					codigoAeropuerto = resultSet.getString("codigo");
				}
			}
		}
		return codigoAeropuerto;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("aeropuerto.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
