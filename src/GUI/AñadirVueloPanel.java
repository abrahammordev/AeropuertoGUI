package GUI;

import javax.swing.*;

import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

public class AñadirVueloPanel extends JPanel {
	private MainFrame mainFrame;
	private JTextField codigoField;
	private JTextField origenField;
	private JTextField destinoField;
	private JTextField fechaSalidaField;
	private JTextField fechaLlegadaField;
	private JTextField duracionField;
	private JTextField plazasField;

	public AñadirVueloPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Añadir Vuelo");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		MainPanel.cambiarFuente(titleLabel);
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
		formPanel.add(new JLabel("Origen:"), gbc);
		origenField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(origenField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Destino:"), gbc);
		destinoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(destinoField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Fecha de Salida (yyyy-MM-dd HH:mm):"), gbc);
		fechaSalidaField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(fechaSalidaField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Fecha de Llegada (yyyy-MM-dd HH:mm):"), gbc);
		fechaLlegadaField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(fechaLlegadaField, gbc);

		// duración calculada automáticamente

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Plazas:"), gbc);
		plazasField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(plazasField, gbc);

		JButton añadirButton = new JButton("Añadir");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(añadirButton, gbc);

		add(formPanel, BorderLayout.CENTER);

		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("VueloPanel"));
		add(volverButton, BorderLayout.SOUTH);

		añadirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String codigo = codigoField.getText();
					String origen = origenField.getText();
					String destino = destinoField.getText();

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
					LocalDateTime fechaSalida = LocalDateTime.parse(fechaSalidaField.getText(), formatter);
					LocalDateTime fechaLlegada = LocalDateTime.parse(fechaLlegadaField.getText(), formatter);

					int plazas = Integer.parseInt(plazasField.getText());

					MainFrame.añadirVuelo(codigo, origen, destino, fechaSalida, fechaLlegada, plazas);

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(AñadirVueloPanel.this,
							"Error: La cantidad de plazas debe ser un número válido", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(AñadirVueloPanel.this, ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(AñadirVueloPanel.this, "Error: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("vuelo.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}