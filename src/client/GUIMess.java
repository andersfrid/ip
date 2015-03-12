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
import java.util.LinkedList;

import javafx.scene.control.ScrollPane;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	
	private JLabel userslabel;
	private JTextField JTFmessage = new JTextField();
	private JButton JBSend = new JButton("Send");
	private JButton JBLogout = new JButton("Logout");
	private JButton JBAddFile = new JButton("Add File");
	private JButton JBSendAll = new JButton("Send all");
	private JToggleButton dav, and, ande, joa, joh;
	private JTextArea showMess = new JTextArea();
	private Font font1 = new Font("SansSerif", Font.BOLD, 13);
	private Font font2 = new Font("SansSerif", Font.PLAIN, 12);
	private JPanel JPCenter, JPEast, JPSouth;
	private JFrame frame;
	private JScrollPane scrollPane;
	
//	public GUIMess(ClientController controller) {
	public GUIMess(){
		this.controller = controller;

		// CENTER
		scrollPane = new JScrollPane(showMess);
		JPCenter = new JPanel();
		JPCenter.setPreferredSize(new Dimension(400, 280));
		JPCenter.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(500, 280));
		showMess.setEditable(false);

		// EAST
		JPEast = new JPanel(new GridLayout(10,1));
		JPEast.setPreferredSize(new Dimension(140, 280));
		JPEast.setBackground(Color.WHITE);
		userslabel = new JLabel("Användare:");
		userslabel.setFont(font1);
		JPEast.add(userslabel);
		
		//Lägga till användare
		generateCheckBoxes();
		
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
		frame.setPreferredSize(new Dimension(700, 400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(JPCenter, BorderLayout.CENTER);
		frame.add(JPEast, BorderLayout.EAST);
		frame.add(JPSouth, BorderLayout.SOUTH);
		frame.pack();
	}

	public void writeMessage(String author, String message){
		//print i rutan
	}
	
	
	public void generateCheckBoxes() {
		
//		LinkedList<String> users = controller.getListOnUsers();
		
		LinkedList<String> users = new LinkedList<String>();
		users.add("Andreas");
		users.add("David");
		users.add("David");


		
		toUsers = new JCheckBox[users.size()];
		
		for(int i = 0; i < users.size(); i++){
			toUsers[i] = new JCheckBox(users.get(i));
			JPEast.add(toUsers[i]);
		}	
	}
	
	
	public void updateCheckBoxes(LinkedList<String> newUserList){
		//Ska uppdatera listan
	}
	
	public LinkedList<String> controllCheckedBoxes(){
		
		LinkedList<String> isSelected = new LinkedList<String>();
		int count = 0;
		
		for(int i = 0; i < toUsers.length; i++){
			if(toUsers[i].isSelected()){
				isSelected.add(toUsers[i].getText());
				count++;
			}
		}

		if(count >= toUsers.length)
			return null;
		
		return isSelected;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == JBSend) {
			controllCheckedBoxes();
		}
		if (e.getSource() == JBSendAll) {

		}
		if (e.getSource() == JBLogout) {

		}
		if (e.getSource() == JBAddFile) {

		}
	}
	
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER){
//			showMessage(JTFmessage.getText());
			System.out.println(JTFmessage.getText());
		}	
	}
	
	public static void main(String[] args) {
		new GUIMess();
	}
	
}
