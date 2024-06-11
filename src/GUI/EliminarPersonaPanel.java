package GUI;

import javax.swing.*;

import Conection.DataBaseConection;
import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.sql.*;

public class EliminarPersonaPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> personaComboBox;

	public EliminarPersonaPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Eliminar Persona");
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

		// Espacio en blanco para alinear
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		formPanel.add(new JPanel(), gbc);

		JButton eliminarButton = new JButton("Eliminar");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(eliminarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("PersonaPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);

		// Llamar al método para cargar los datos en el JComboBox
		cargarDatos(); 
		add(volverButton, BorderLayout.SOUTH);
		
		eliminarButton.addActionListener(e -> {
			// Obtener el DNI de la persona seleccionada
			
			String dni = (String) personaComboBox.getSelectedItem();
			MainFrame.eliminarPersona(dni); 
			
			// Recargar los datos en el JComboBox después de eliminar una persona
			cargarDatos();
			JOptionPane.showMessageDialog(this, "Persona eliminada correctamente.");
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
		buttonPanel.setOpaque(false);
	}

	 public static void cargarDatos() {
	       
	        personaComboBox.removeAllItems();
	        try (Connection cnx = DataBaseConection.getConnection()) {
	            String query = "SELECT dni FROM PERSONAS";
	            try (PreparedStatement statement = cnx.prepareStatement(query);
	                 ResultSet resultSet = statement.executeQuery()) {

	                // Agregar los resultados al JComboBox
	                while (resultSet.next()) {
	                    String dni = resultSet.getString("dni");
	                    personaComboBox.addItem(dni);
	                }
	            }
	        } catch (SQLException ex) {
	            JOptionPane.showMessageDialog(null, "Error al obtener la lista de personas: " + ex.getMessage(), "Error",
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
