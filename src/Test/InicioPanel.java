package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class InicioPanel extends JPanel {
	
	private MainFrame mainFrame;
	private JTextField nameField;
	private JPasswordField passwordField;
	private Map<String, String> validCredentials;

	public InicioPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLayout(new BorderLayout());
		loadValidCredentials();

		// Carga de la imagen de fondo
		ImageIcon backgroundImageIcon = new ImageIcon("airplane-7429725_1280.jpg");
		Image backgroundImage = backgroundImageIcon.getImage();
		Image scaledBackgroundImage = backgroundImage.getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
		ImageIcon scaledBackgroundImageIcon = new ImageIcon(scaledBackgroundImage);

		JLabel backgroundLabel = new JLabel(scaledBackgroundImageIcon);
		backgroundLabel.setLayout(new BorderLayout());
		add(backgroundLabel);

		// Panel para el campo de texto, contraseña, botón y mensaje

		JPanel inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		backgroundLabel.add(inputPanel, BorderLayout.CENTER);

		JLabel messageLabel = new JLabel("Introduce usuario y contraseña para acceder al sistema");
		messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		inputPanel.add(messageLabel, gbc);

		JLabel userLabel = new JLabel("Usuario:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		inputPanel.add(userLabel, gbc);

		nameField = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		inputPanel.add(nameField, gbc);

		JLabel passwordLabel = new JLabel("Contraseña:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		inputPanel.add(passwordLabel, gbc);

		passwordField = new JPasswordField(20);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_START;
		inputPanel.add(passwordField, gbc);

		// Botón de entrada y verificacion de los campos

		JButton enterButton = new JButton("Entrar");
		enterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText().trim();
				String password = new String(passwordField.getPassword()).trim();
				if (!name.isEmpty() && !password.isEmpty()) {
					if (validCredentials.containsKey(name) && validCredentials.get(name).equals(password)) {
						JOptionPane.showMessageDialog(mainFrame, "Bienvenido, " + name + "!");
						mainFrame.showCard("MainPanel");
					} else {
						logError(name, password);
						JOptionPane.showMessageDialog(mainFrame, "Usuario o contraseña incorrectos.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(mainFrame, "Por favor, escriba su nombre y contraseña.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		inputPanel.add(enterButton, gbc);
	}

	// Cargamos las credenciales del txt para el login

	private void loadValidCredentials() {
		validCredentials = new HashMap<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("credenciales.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 2) {
					validCredentials.put(parts[0].trim(), parts[1].trim());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al cargar las credenciales.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Rellenamos el error log

	private void logError(String name, String password) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("errores.log", true))) {
			writer.write("Usuario: " + name + ", Contraseña: " + password);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al guardar el error.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
