package GUI;

import javax.swing.*;

import Conection.DataBaseConection;
import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.sql.*;

public class ModificarAeropuertoPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> aeropuertoComboBox;
	private JTextField nombreField;
	private JTextField ciudadField;

	public ModificarAeropuertoPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Modificar Aeropuerto");
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

		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(new JLabel("Nuevo Nombre:"), gbc);
		nombreField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(nombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(new JLabel("Nueva Ciudad:"), gbc);
		ciudadField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(ciudadField, gbc);

		JButton modificarButton = new JButton("Modificar");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(modificarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("AeropuertoPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		cargarAeropuertos(); // Cargar los nombres de los aeropuertos al iniciar el panel

		modificarButton.addActionListener(e -> {
			String nombreAeropuerto = (String) aeropuertoComboBox.getSelectedItem();
			String nuevoNombre = nombreField.getText();
			String nuevaCiudad = ciudadField.getText();

			MainFrame.modificarAeropuerto(nombreAeropuerto, nuevoNombre, nuevaCiudad);
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		volverButton.setOpaque(false);
		
		
	}

	public static void cargarAeropuertos() {
       
        aeropuertoComboBox.removeAllItems();
        try (Connection cnx = DataBaseConection.getConnection()) {
            String query = "SELECT nombre FROM AEROPUERTOS";
            try (PreparedStatement stm = cnx.prepareStatement(query)) {
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    String nombreAeropuerto = rs.getString("nombre");
                    aeropuertoComboBox.addItem(nombreAeropuerto);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar los aeropuertos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); 
        }
    }
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("aeropuerto.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
