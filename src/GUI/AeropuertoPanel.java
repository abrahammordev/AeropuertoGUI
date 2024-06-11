package GUI;

import javax.swing.*;
import Test.MainFrame;
import java.awt.*;

public class AeropuertoPanel extends JPanel {
	private MainFrame mainFrame;

	public AeropuertoPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Gestión de Aeropuertos");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
		JButton añadirButton = new JButton("Añadir");
		JButton modificarButton = new JButton("Modificar");
		JButton eliminarButton = new JButton("Eliminar");
		JButton consultarButton = new JButton("Consultar");
		JButton volverButton = new JButton("Volver");

		// Establecer un tamaño más pequeño para los botones
		Dimension buttonSize = new Dimension(120, 25);
		añadirButton.setPreferredSize(buttonSize);
		modificarButton.setPreferredSize(buttonSize);
		eliminarButton.setPreferredSize(buttonSize);
		consultarButton.setPreferredSize(buttonSize);
		volverButton.setPreferredSize(buttonSize);

		añadirButton.addActionListener(e -> mainFrame.showCard("AñadirAeropuertoPanel"));
		modificarButton.addActionListener(e -> {
			mainFrame.showCard("ModificarAeropuertoPanel");
			ModificarAeropuertoPanel.cargarAeropuertos();
		});
		eliminarButton.addActionListener(e -> {
			mainFrame.showCard("EliminarAeropuertoPanel");
			EliminarAeropuertoPanel.cargarInformacion();
		});
		consultarButton.addActionListener(e -> {
			mainFrame.showCard("ConsultarAeropuertoPanel");
			ConsultarAeropuertoPanel.cargarDatos();
		});
		volverButton.addActionListener(e -> mainFrame.showCard("MainPanel"));

		buttonPanel.add(añadirButton);
		buttonPanel.add(modificarButton);
		buttonPanel.add(eliminarButton);
		buttonPanel.add(consultarButton);

		// Establecer el fondo transparente para mostrar la imagen de fondo
		setOpaque(false);
		buttonPanel.setOpaque(false);

		add(buttonPanel, BorderLayout.CENTER);
		add(volverButton, BorderLayout.SOUTH);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // Llama al método paintComponent de la superclase

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("background_image.jpeg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
