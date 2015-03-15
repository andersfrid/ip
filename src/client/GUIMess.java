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
import java.util.ArrayList;

import javafx.*;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

public class GUIMess extends JPanel implements ActionListener, KeyListener {
	private ClientController controller;

	JCheckBox[] toUsers;

	private JLabel userslabel, iconLabel, iconAuthor,lastIcon;
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

		
		//IconPanel
		IconPanel = new JPanel(new BorderLayout());
		IconPanel.setPreferredSize(new Dimension(200, 200));
		iconLabel = new JLabel("Senaste skickade bild");
		iconAuthor = new JLabel("Avsändare: ");
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
		generateCheckBoxes(controller.getListOnUsers());

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
		JTFmessage.addKeyListener(this);

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

	public void writeMessage(String author, String message, Icon image) {
		showMess.setFont(mess);
		showMess.append(author + ": " + message + "\n");

		if (image != null) {
			lastIcon = (JLabel)image;
			lastIcon.repaint();
			iconAuthor.setText("Avsändare: "+author);
			iconAuthor.repaint();
			
			//Föröska fixa det här imorgon!!!!!!
		}

	}

	public void generateCheckBoxes(ArrayList<String> users) {
		toUsers = new JCheckBox[users.size()-1];
		for (int i = 0; i < users.size() - 1; i++) {
			if (!(users.get(i).equals(controller.getUser()))) {
				toUsers[i] = new JCheckBox(users.get(i));
				JPEast.add(toUsers[i]);
			}
		}
	}

	public void updateCheckBoxes(ArrayList<String> users) {

		for (int i = 0; i < toUsers.length; i++) {
			toUsers[i].remove(this);
		}
		toUsers = null;

		generateCheckBoxes(users);
	}

	public ArrayList<String> controllCheckedBoxes() {

		ArrayList<String> isSelected = new ArrayList<String>();
		int count = 0;

		for (int i = 0; i < toUsers.length; i++) {
			if (toUsers[i].isSelected()) {
				isSelected.add(toUsers[i].getText());
				count++;
			}
		}

		if (count >= toUsers.length)
			return null;

		return isSelected;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == JBSend) {
			controller.sendMessage(this.JTFmessage.getText(), null, controllCheckedBoxes());
			writeMessage(controller.getUser(),JTFmessage.getText(),null);
		}
		if (e.getSource() == JBSendAll) {

		}
		if (e.getSource() == JBLogout) {

		}
		if (e.getSource() == JBAddFile) {

		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			// showMessage(JTFmessage.getText());
			System.out.println(JTFmessage.getText());
		}
	}
	
}
