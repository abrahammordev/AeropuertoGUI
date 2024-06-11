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
import java.util.ArrayList;
import java.util.List;

public class ConsultarVueloPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> vueloComboBox;
	private JTextArea resultadoArea;

	public ConsultarVueloPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Consultar Vuelo");
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
		cargarVuelosDisponibles();
		gbc.gridx = 1;
		formPanel.add(vueloComboBox, gbc);

		JButton consultarButton = new JButton("Consultar");
		consultarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String codigoVuelo = (String) vueloComboBox.getSelectedItem();
				MainFrame.consultarVuelo(codigoVuelo);
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
		volverButton.addActionListener(e -> mainFrame.showCard("VueloPanel"));
		add(volverButton, BorderLayout.SOUTH);
		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}

	public static  void cargarVuelosDisponibles() {
		
		vueloComboBox.removeAllItems();

		
		try (Connection cnx = DataBaseConection.getConnection()) {
			String query = "SELECT codigo FROM VUELOS";
			try (PreparedStatement stm = cnx.prepareStatement(query)) {
				ResultSet rs = stm.executeQuery();
				while (rs.next()) {
					vueloComboBox.addItem(rs.getString("codigo"));
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar los vuelos: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("vuelo.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
