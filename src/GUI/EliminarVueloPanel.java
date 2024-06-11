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

public class EliminarVueloPanel extends JPanel {
	private MainFrame mainFrame;
	private static JComboBox<String> vueloComboBox;

	public EliminarVueloPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Eliminar Vuelo");
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

		JButton eliminarButton = new JButton("Eliminar");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(eliminarButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("VueloPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		cargarDatos();

		eliminarButton.addActionListener(e -> {
			String codigoVuelo = (String) vueloComboBox.getSelectedItem();
			try {
				MainFrame.eliminarVuelo(codigoVuelo);
				cargarDatos();
				JOptionPane.showMessageDialog(EliminarVueloPanel.this, "El vuelo ha sido eliminado correctamente.",
						"Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(EliminarVueloPanel.this, "Error al eliminar el vuelo: " + ex.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
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
