package Test;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
	private MainFrame mainFrame;

	public MainPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Bienvenido al sistema de gestión de Aeropuertos");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cambiarFuente(titleLabel);
		add(titleLabel, BorderLayout.NORTH);

		// Panel para los botones centrados

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Dimensiones default

		Dimension buttonSize = new Dimension(300, 80);

		// Botón de Gestión de Aeropuertos

		JButton aeropuertoButton = new JButton("Gestión de Aeropuertos");
		aeropuertoButton.setPreferredSize(buttonSize);
		aeropuertoButton.setMaximumSize(buttonSize);
		aeropuertoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		aeropuertoButton.addActionListener(e -> mainFrame.showCard("AeropuertoPanel"));

		// Botón de Gestión de Vuelos

		JButton vueloButton = new JButton("Gestión de Vuelos");
		vueloButton.setPreferredSize(buttonSize);
		vueloButton.setMaximumSize(buttonSize);
		vueloButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		vueloButton.addActionListener(e -> mainFrame.showCard("VueloPanel"));

		// Botón de Gestión de Personas

		JButton personaButton = new JButton("Gestión de Personas");
		personaButton.setPreferredSize(buttonSize);
		personaButton.setMaximumSize(buttonSize);
		personaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		personaButton.addActionListener(e -> mainFrame.showCard("PersonaPanel"));

		// Añadir botones al panel

		buttonPanel.add(Box.createVerticalGlue());
		buttonPanel.add(aeropuertoButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		buttonPanel.add(vueloButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		buttonPanel.add(personaButton);
		buttonPanel.add(Box.createVerticalGlue());

		add(buttonPanel, BorderLayout.CENTER);
		ImageIcon icon = new ImageIcon("488648.png");
		mainFrame.setIconImage(icon.getImage());
		setOpaque(false);
		buttonPanel.setOpaque(false);
		aeropuertoButton.setOpaque(false);
		vueloButton.setOpaque(false);
		personaButton.setOpaque(false);
	}

	public static void cambiarFuente(JLabel fuente) {
		Font newFont = new Font("Arial", Font.BOLD, 20);
		fuente.setFont(newFont);
	}

	public static void cambiarFuentePrincipal(JButton button) {
		Font newFont = new Font("Arial", Font.BOLD, 40);
		button.setFont(newFont);

		Dimension newSize = button.getPreferredSize();
		button.setPreferredSize(newSize);
		button.revalidate();
		button.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("inicio.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}

}
