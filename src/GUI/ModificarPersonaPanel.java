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

public class ModificarPersonaPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> personaComboBox;
	private JTextField nuevoNombreField;
	private JTextField nuevoApellidoField;
	private JTextField nuevoTelefonoField;

	public ModificarPersonaPanel(MainFrame mainFrame) {

		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Modificar Persona");
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
		formPanel.add(new JLabel("Nuevo Nombre:"), gbc);
		nuevoNombreField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(nuevoNombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nuevo Apellido:"), gbc);
		nuevoApellidoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(nuevoApellidoField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nuevo Teléfono:"), gbc);
		nuevoTelefonoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(nuevoTelefonoField, gbc);

		JButton modificarButton = new JButton("Modificar");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(modificarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("PersonaPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);

		// Llamar al método para cargar los datos en el JComboBox
		

		cargarDatos();
		add(volverButton, BorderLayout.SOUTH);
		modificarButton.addActionListener(e -> {
			
			// Obtener el DNI de la persona seleccionada
			
			String dni = (String) personaComboBox.getSelectedItem();
			String nuevoNombre = nuevoNombreField.getText();
			String nuevoApellido = nuevoApellidoField.getText();
			String nuevoTelefono = nuevoTelefonoField.getText();
			try {
				MainFrame.modificarDatosPersona(dni, nuevoNombre, nuevoApellido, nuevoTelefono);
				cargarDatos(); 

				nuevoNombreField.setText("");
				nuevoApellidoField.setText("");
				nuevoTelefonoField.setText("");

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this, "Error al modificar la persona: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
		buttonPanel.setOpaque(false);
	}

	public static void cargarDatos() {

		personaComboBox.removeAllItems();
		try (Connection conexion = DataBaseConection.getConnection();
				PreparedStatement pstmt = conexion.prepareStatement("SELECT dni FROM PERSONAS")) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				personaComboBox.addItem(rs.getString("dni"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al cargar los datos de las personas: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("persona.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}