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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModificarVueloPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> vueloComboBox;
	private JTextField fechaSalidaField;
	private JTextField fechaLlegadaField;
	private JTextField origenField;
	private JTextField destinoField;

	public ModificarVueloPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Modificar Vuelo");
		MainPanel.cambiarFuente(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Seleccionar Vuelo:"), gbc);
		vueloComboBox = new JComboBox<>();
		gbc.gridx = 1;
		formPanel.add(vueloComboBox, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nueva Fecha de Salida (yyyy-MM-dd HH:mm):"), gbc);
		fechaSalidaField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(fechaSalidaField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nueva Fecha de Llegada (yyyy-MM-dd HH:mm):"), gbc);
		fechaLlegadaField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(fechaLlegadaField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nueva ciudad de  Origen:"), gbc);
		origenField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(origenField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nueva ciudad de Destino:"), gbc);
		destinoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(destinoField, gbc);

		JButton modificarButton = new JButton("Modificar");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(modificarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("VueloPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		cargarDatos();

		modificarButton.addActionListener(e -> {
			String codigoVuelo = (String) vueloComboBox.getSelectedItem();

			LocalDateTime nuevaFechaSalida = LocalDateTime.parse(fechaSalidaField.getText(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			LocalDateTime nuevaFechaLlegada = LocalDateTime.parse(fechaLlegadaField.getText(),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			String nuevoOrigen = origenField.getText();
			String nuevoDestino = destinoField.getText();
			try {
				MainFrame.modificarDatosVuelo(codigoVuelo, nuevaFechaSalida, nuevaFechaLlegada, nuevoOrigen,
						nuevoDestino);
				cargarDatos();

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(ModificarVueloPanel.this,
						"Error al modificar el vuelo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} catch (DateTimeParseException dat) {
				JOptionPane.showMessageDialog(ModificarVueloPanel.this,
						"Error al parsear la fecha: " + dat.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}

	public static void cargarDatos() {
	    
	    vueloComboBox.removeAllItems();
	    try (Connection conexion = DataBaseConection.getConnection();
	         PreparedStatement pstmt = conexion.prepareStatement("SELECT codigo FROM vuelos")) {
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            vueloComboBox.addItem(rs.getString("codigo"));
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Error al cargar los datos de los vuelos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace(); // Mostrar la traza del error en consola
	    }
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("vuelo.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
