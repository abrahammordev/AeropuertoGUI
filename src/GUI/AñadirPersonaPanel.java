package GUI;

import javax.swing.*;
import Test.MainFrame;
import Test.MainPanel;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AñadirPersonaPanel extends JPanel {
	private MainFrame mainFrame;
	private JTextField dniField;
	private JTextField nombreField;
	private JTextField apellidoField;
	private JSpinner fechaNacimientoSpinner; // Cambio de JTextField a JSpinner
	private JTextField telefonoField;

	public AñadirPersonaPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("Añadir Persona");
		MainPanel.cambiarFuente(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(new JLabel("DNI:"), gbc);
		dniField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(dniField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Nombre:"), gbc);
		nombreField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(nombreField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Apellido:"), gbc);
		apellidoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(apellidoField, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Fecha de Nacimiento:"), gbc);
		fechaNacimientoSpinner = new JSpinner(new SpinnerDateModel());
		fechaNacimientoSpinner.setEditor(new JSpinner.DateEditor(fechaNacimientoSpinner, "dd/MM/yyyy")); // Formato de
																											// fecha
		gbc.gridx = 1;
		formPanel.add(fechaNacimientoSpinner, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		formPanel.add(new JLabel("Teléfono:"), gbc);
		telefonoField = new JTextField(20);
		gbc.gridx = 1;
		formPanel.add(telefonoField, gbc);

		JButton añadirButton = new JButton("Añadir");
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(añadirButton, gbc);
		setOpaque(false);
		formPanel.setOpaque(false);
		add(formPanel, BorderLayout.CENTER);

		JButton volverButton = new JButton("Volver Atrás");
		volverButton.addActionListener(e -> mainFrame.showCard("PersonaPanel"));
		add(volverButton, BorderLayout.SOUTH);

		// Acción al hacer clic en el botón Añadir

		añadirButton.addActionListener(e -> {

			String dni = dniField.getText();
			String nombre = nombreField.getText();
			String apellido = apellidoField.getText();

			// Obtener la fecha del JSpinner

			Date fechaNacimiento = (Date) fechaNacimientoSpinner.getValue();
			String telefono = telefonoField.getText();

			// Convertir la fecha al formato deseado (yyyy-MM-dd)

			LocalDate localDate = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String fechaNacimientoStr = localDate.toString();

			MainFrame.añadirPersona(dni, nombre, apellido, fechaNacimientoStr, telefono);
		});
		setOpaque(false);
		formPanel.setOpaque(false);
		volverButton.setOpaque(false);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dibujar la imagen de fondo
		ImageIcon backgroundImage = new ImageIcon("persona.jpg");
		g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
	}
}
