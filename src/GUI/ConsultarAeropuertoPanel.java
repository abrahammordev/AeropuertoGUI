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

public class ConsultarAeropuertoPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> aeropuertoComboBox;

	public ConsultarAeropuertoPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Consultar Aeropuerto");
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

		JButton consultarButton = new JButton("Consultar");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(consultarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("AeropuertoPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		cargarDatos();

		consultarButton.addActionListener(e -> {
			String nombreAeropuerto = (String) aeropuertoComboBox.getSelectedItem();
			if (nombreAeropuerto != null) {
				try {
					String detalles = MainFrame.consultarAeropuerto(nombreAeropuerto);
					JOptionPane.showMessageDialog(this, detalles, "Detalles del Aeropuerto",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(this, "Error al consultar la información: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}

	 public static void cargarDatos() {
	        

	        aeropuertoComboBox.removeAllItems();
	        try (Connection connection = DataBaseConection.getConnection()) {
	            String sql = "SELECT nombre FROM AEROPUERTOS";
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("aeropuerto.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}