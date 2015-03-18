package client;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Inloggnings GUI för användaren.
 * @author Joakim Gustavsson Sköld
 *
 */
public class ClientGUI extends JPanel implements ActionListener {

	private JFrame frame = new JFrame("Client");
	private JLabel lblClientname = new JLabel("Clientserver - IP");
	private JLabel lblUsername = new JLabel("Username:");
	private JButton btnConnect = new JButton("Connect");
	private JButton btnClose = new JButton("Close");
	private JTextField tfUsername = new JTextField("");
	private Font plainSS18 = new Font("SansSerif", Font.PLAIN, 18);
	private Font boldSS28 = new Font("SansSerif", Font.BOLD, 28);
	private ClientController controller;

	/**
	 * Konstruktor som bygger upp GUI.
	 * @param controller
	 */
	public ClientGUI(ClientController controller) {
		this.controller = controller;

		setLayout(null);
		setPreferredSize(new Dimension(600, 300));

		lblClientname.setBounds(200, 40, 250, 25);
		lblClientname.setFont(boldSS28);
		add(lblClientname);

		lblUsername.setBounds(150, 120, 250, 25);
		lblUsername.setFont(plainSS18);
		add(lblUsername);

		btnConnect.setBounds(180, 200, 245, 25);
		btnConnect.setFont(plainSS18);
		add(btnConnect);

		btnClose.setBounds(180, 250, 245, 25);
		btnClose.setFont(plainSS18);
		add(btnClose);

		tfUsername.setBounds(240, 120, 200, 25);
		tfUsername.setFont(plainSS18);
		add(tfUsername);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);

		btnConnect.addActionListener(this);
		btnClose.addActionListener(this);

	}
	/**
	 * Metod som ger knapparna funktionallitet.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect) {
			controller.setUser(tfUsername.getText());
			controller.startGUIMess();
			frame.setVisible(false);
		}
		if (e.getSource() == btnClose) {
			System.exit(0);
		}
	}
}
