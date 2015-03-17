package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.sun.jmx.snmp.Timestamp;

public class GUIMess extends JPanel implements ActionListener {
	private ClientController controller;
	private Icon sendIcon = null;

	private JFileChooser jChooser = new JFileChooser();
	private Calendar calendar;

	private JCheckBox[] toUsers;

	private JLabel userslabel, iconLabel, iconAuthor, lastIcon;
	private JTextField JTFmessage = new JTextField();
	private JButton JBSend = new JButton("Send");
	private JButton JBLogout = new JButton("Logout");
	private JButton JBAddFile = new JButton("Add File");
	private JButton JBSendAll = new JButton("Send all");
	private JToggleButton dav, and, ande, joa, joh;
	private JTextArea showMess = new JTextArea();
	private Font font1 = new Font("SansSerif", Font.BOLD, 13);
	private Font font2 = new Font("SansSerif", Font.PLAIN, 12);
	private Font mess = new Font("SansSerif", Font.PLAIN, 15);
	private JPanel JPCenter, JPEast, JPSouth, IconPanel;
	private JFrame frame;
	private JScrollPane scrollPane;

	public GUIMess(ClientController controller) {

		this.controller = controller;

		// IconPanel
		IconPanel = new JPanel(new BorderLayout());
		IconPanel.setPreferredSize(new Dimension(200, 200));
		iconLabel = new JLabel("Senaste skickade bild");
		iconLabel.setFont(font1);
		iconAuthor = new JLabel("Avsändare: ");
		iconAuthor.setFont(font1);
		lastIcon = new JLabel(new ImageIcon(""));
		IconPanel.setForeground(Color.BLACK);
		IconPanel.add(iconLabel, BorderLayout.NORTH);
		IconPanel.add(lastIcon, BorderLayout.CENTER);
		IconPanel.add(iconAuthor, BorderLayout.SOUTH);

		// CENTER
		scrollPane = new JScrollPane(showMess);
		JPCenter = new JPanel();
		JPCenter.setPreferredSize(new Dimension(400, 280));
		JPCenter.add(scrollPane, BorderLayout.WEST);
		JPCenter.add(IconPanel, BorderLayout.EAST);
		scrollPane.setPreferredSize(new Dimension(500, 280));
		showMess.setEditable(false);

		// EAST
		JPEast = new JPanel(new GridLayout(10, 1));
		JPEast.setPreferredSize(new Dimension(140, 280));
		JPEast.setBackground(Color.WHITE);
		userslabel = new JLabel("Användare:");
		userslabel.setFont(font1);
		JPEast.add(userslabel);

		// Lägga till användare

		// South
		JPSouth = new JPanel();
		JPSouth.setPreferredSize(new Dimension(400, 80));
		JPSouth.setBackground(Color.WHITE);
		JPSouth.add(JBLogout);
		JPSouth.add(JBAddFile);
		JPSouth.add(JTFmessage);
		JPSouth.add(JBSend);
		JPSouth.add(JBSendAll);
		JTFmessage.setPreferredSize(new Dimension(350, 70));
		JBSend.setPreferredSize(new Dimension(80, 55));
		JBAddFile.setPreferredSize(new Dimension(80, 55));
		JBLogout.setPreferredSize(new Dimension(80, 55));
		JBSendAll.setPreferredSize(new Dimension(80, 55));
		JBSend.setFont(font2);
		JBAddFile.setFont(font2);
		JBLogout.setFont(font2);
		JBSendAll.setFont(font2);

		// Listeners
		JBSend.addActionListener(this);
		JBSendAll.addActionListener(this);
		JBLogout.addActionListener(this);
		JBAddFile.addActionListener(this);

		// Frame
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(850, 400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(JPCenter, BorderLayout.CENTER);
		frame.add(JPEast, BorderLayout.EAST);
		frame.add(JPSouth, BorderLayout.SOUTH);
		frame.pack();
	}

	public void generateCheckBoxes() {
		ArrayList<String> users = controller.getListOnUsers();
		toUsers = new JCheckBox[users.size()];
		for (int i = 0; i < users.size(); i++) {
			toUsers[i] = new JCheckBox(users.get(i));
			JPEast.add(toUsers[i]);
		}
	}

	public void updateCheckBoxes() {
		if (toUsers != null) {
			for (int i = 0; i < toUsers.length; i++) {
				toUsers[i].remove(this);
			}
			JPEast.removeAll();
		}
		generateCheckBoxes();
		JPEast.revalidate();
		JPEast.repaint();
	}

	public void writeMessage(String author, String message, Icon image) {
		calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		String output = dateFormat.format(calendar.getTime()) + " " + author
				+ ": " + message;

		if (image != null) {
			lastIcon.setIcon(image);
			lastIcon.repaint();
			iconAuthor.setText("Avsändare: " + author + " ("
					+ dateFormat.format(calendar.getTime()) + ")");
			iconAuthor.repaint();

			output += "  | (Bifogad bild)";
		}

		output += "\n";

		showMess.setFont(mess);
		showMess.append(output);

	}

	public Icon getIcon() {
		return sendIcon;
	}

	public void resetIcon() {
		this.sendIcon = null;
	}

	public void saveIcon() {
		File file;
		BufferedImage img;
		jChooser.showOpenDialog(null);
		file = jChooser.getSelectedFile();

		try {
			img = ImageIO.read(file);
			sendIcon = new ImageIcon(img);
		} catch (Exception e) {
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == JBSend) {
			// controller.sendMessage(this.JTFmessage.getText(), getIcon(),
			// controllCheckedBoxes());
			resetIcon();

		}
		if (e.getSource() == JBSendAll) {
			controller.sendMessage(this.JTFmessage.getText(), getIcon(), null);
			resetIcon();

		}
		if (e.getSource() == JBLogout) {
			controller.disconnect();
			System.exit(0);
		}
		if (e.getSource() == JBAddFile) {
			saveIcon();
		}
	}

}
