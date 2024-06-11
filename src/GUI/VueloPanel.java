package GUI;

import javax.swing.*;

import Test.MainFrame;

import java.awt.*;

public class VueloPanel extends JPanel {
	private MainFrame mainFrame;

	public VueloPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Gestión de Vuelos");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
		JButton añadirButton = new JButton("Añadir Vuelo");
		JButton modificarButton = new JButton("Modificar Vuelo");
		JButton eliminarButton = new JButton("Eliminar Vuelo");
		JButton consultarButton = new JButton("Consultar Vuelo");
		JButton volverButton = new JButton("Volver Atrás");

		añadirButton.addActionListener(e -> mainFrame.showCard("AñadirVueloPanel"));
		modificarButton.addActionListener(e -> {
			mainFrame.showCard("ModificarVueloPanel");
			ModificarVueloPanel.cargarDatos();
		});
		eliminarButton.addActionListener(e -> {
			EliminarVueloPanel.cargarDatos();
			mainFrame.showCard("EliminarVueloPanel");

		});

		consultarButton.addActionListener(e -> {
			ConsultarVueloPanel.cargarVuelosDisponibles();
			mainFrame.showCard("ConsultarVueloPanel");

		});
		volverButton.addActionListener(e -> mainFrame.showCard("MainPanel"));

		buttonPanel.add(añadirButton);
		buttonPanel.add(modificarButton);
		buttonPanel.add(eliminarButton);
		buttonPanel.add(consultarButton);
		buttonPanel.add(volverButton);
		setOpaque(false);
		buttonPanel.setOpaque(false);
		add(buttonPanel, BorderLayout.CENTER);
		add(volverButton, BorderLayout.SOUTH);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("background_image.jpeg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
