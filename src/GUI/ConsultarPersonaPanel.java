package GUI;

import javax.swing.*;
import Conection.DataBaseConection;
import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultarPersonaPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> personaComboBox;
	private JTextArea resultadoArea;

	public ConsultarPersonaPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Consultar Persona");
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
		cargarDatos();
		gbc.gridx = 1;
		formPanel.add(personaComboBox, gbc);

		JButton consultarButton = new JButton("Consultar");
		consultarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombrePersona = (String) personaComboBox.getSelectedItem();
				if (nombrePersona != null) {
					MainFrame.consultarPersona(nombrePersona, resultadoArea);
				} else {
					resultadoArea.setText("Seleccione una persona para consultar.");
				}
			}
		});

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(consultarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		resultadoArea = new JTextArea(10, 30);
		resultadoArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(resultadoArea);
		add(scrollPane, BorderLayout.SOUTH);

		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("PersonaPanel"));
		add(volverButton, BorderLayout.SOUTH);

		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}

	public static void cargarDatos() {

		personaComboBox.removeAllItems();

		// Obtener los nombres de las personas disponibles y agregarlos al ComboBox

		try (Connection cnx = DataBaseConection.getConnection()) {
			String query = "SELECT nombre FROM PERSONAS";
			try (PreparedStatement stm = cnx.prepareStatement(query)) {
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					personaComboBox.addItem(rs.getString("nombre"));
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar las personas: " + e.getMessage(), "Error",
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
