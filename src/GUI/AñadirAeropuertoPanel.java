package GUI;

import javax.swing.*;
import Conection.DataBaseConection;
import Test.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Test.MainFrame;

public class AñadirAeropuertoPanel extends JPanel {
	private MainFrame mainFrame;
	private JTextField codigoField;
	private JTextField nombreField;
	private JTextField ciudadField;

	public AñadirAeropuertoPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Añadir Aeropuerto");
		MainPanel.cambiarFuente(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("Código:"), gbc);
		codigoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(codigoField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nombre:"), gbc);
		nombreField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(nombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Ciudad:"), gbc);
		ciudadField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(ciudadField, gbc);

		JButton añadirButton = new JButton("Añadir");

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(añadirButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("AeropuertoPanel"));
		buttonPanel.add(volverButton);

		add(buttonPanel, BorderLayout.SOUTH);
		add(volverButton, BorderLayout.SOUTH);

		añadirButton.addActionListener(e -> {
			String codigo = codigoField.getText();
			String nombre = nombreField.getText();
			String ciudad = ciudadField.getText();

			try {
				MainFrame.añadirAeropuerto(codigo, nombre, ciudad);
				JOptionPane.showMessageDialog(AñadirAeropuertoPanel.this,
						"El aeropuerto ha sido añadido correctamente.", "Añadido exitoso",
						JOptionPane.INFORMATION_MESSAGE);
				limpiarCampos(); // Limpiar los campos después de añadir
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(AñadirAeropuertoPanel.this,
						"Error al añadir el aeropuerto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		buttonPanel.setOpaque(false);
		volverButton.setOpaque(false);

	}

	private void limpiarCampos() {
		codigoField.setText("");
		nombreField.setText("");
		ciudadField.setText("");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("aeropuerto.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
