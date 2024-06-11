package GUI;

import javax.swing.*;

import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;

public class PersonaPanel extends JPanel {
	private MainFrame mainFrame;

	public PersonaPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Gestión de Personas");
		MainPanel.cambiarFuente(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
		JButton añadirButton = new JButton("Añadir Persona");
		JButton modificarButton = new JButton("Modificar Persona");
		JButton eliminarButton = new JButton("Eliminar Persona");
		JButton añadirPasajeroVueloButton = new JButton("Añadir Pasajero a Vuelo");
		JButton consultarButton = new JButton("Consultar Persona");
		JButton volverButton = new JButton("Volver Atrás");

		añadirButton.addActionListener(e -> mainFrame.showCard("AñadirPersonaPanel"));
		modificarButton.addActionListener(e -> {
			mainFrame.showCard("ModificarPersonaPanel");
			ModificarPersonaPanel.cargarDatos();
		});
		eliminarButton.addActionListener(e -> {
			mainFrame.showCard("EliminarPersonaPanel");
			EliminarPersonaPanel.cargarDatos();
		});
		añadirPasajeroVueloButton.addActionListener(e -> {
			mainFrame.showCard("AñadirPasajeroVueloPanel");
			AñadirPasajeroVueloPanel.cargarDatos();
		});
		consultarButton.addActionListener(e -> {
			mainFrame.showCard("ConsultarPersonaPanel");
			ConsultarPersonaPanel.cargarDatos();
		});
		volverButton.addActionListener(e -> mainFrame.showCard("MainPanel"));

		buttonPanel.add(añadirButton);
		buttonPanel.add(modificarButton);
		buttonPanel.add(eliminarButton);
		buttonPanel.add(añadirPasajeroVueloButton);
		buttonPanel.add(consultarButton);

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
