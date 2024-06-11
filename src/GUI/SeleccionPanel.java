package GUI;

import javax.swing.*;
import Test.MainFrame;
import Test.MainPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class SeleccionPanel extends JPanel {
	private MainFrame mainFrame;
	private ImageIcon backgroundImageIcon;

	public SeleccionPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		// Carga de la imagen de fondo
		backgroundImageIcon = new ImageIcon("background_image.jpeg");

		JButton aeropuertoBtn = new JButton("Gestión de Aeropuertos");
		aeropuertoBtn.setPreferredSize(new Dimension(150, 80));
		aeropuertoBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.showCard("AeropuertoPanel");
				MainPanel.cambiarFuentePrincipal(aeropuertoBtn);
			}
		});
		add(aeropuertoBtn);

		JButton vueloBtn = new JButton("Gestión de Vuelos");
		vueloBtn.setPreferredSize(new Dimension(150, 80));
		vueloBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.showCard("VueloPanel");
				MainPanel.cambiarFuentePrincipal(vueloBtn);
			}
		});
		add(vueloBtn);

		JButton personaBtn = new JButton("Gestión de Personas");
		personaBtn.setPreferredSize(new Dimension(150, 80));
		personaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.showCard("PersonaPanel");
				MainPanel.cambiarFuentePrincipal(personaBtn);
			}
		});
		add(personaBtn);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		g.drawImage(backgroundImageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
