package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class GUIMess extends JPanel implements ActionListener {
	private ClientController controller;
	
	private JLabel online;
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


	public GUIMess(ClientController controller) {
		this.controller = controller;
	
		// CENTER
		scrollPane = new JScrollPane(showMess);
		JPCenter = new JPanel();
		JPCenter.setPreferredSize(new Dimension(400, 280));
		JPCenter.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(500, 280));
		showMess.setEditable(false);
		


		// EAST
		JPEast = new JPanel(new GridLayout(6,1));
		JPEast.setPreferredSize(new Dimension(140, 280));
		JPEast.setBackground(Color.WHITE);
		online = new JLabel("Online/Offline");
		dav = new JToggleButton("David");
		and = new JToggleButton("Andreas");
		ande = new JToggleButton("Anders");
		joa = new JToggleButton("Joakim");
		joh = new JToggleButton("Johan");
		dav.setForeground(Color.RED);
		and.setForeground(Color.RED);
		ande.setForeground(Color.RED);
		joa.setForeground(Color.RED);
		joh.setForeground(Color.RED);
		online.setFont(font1);
		dav.setFont(font2);
		and.setFont(font2);
		ande.setFont(font2);
		joa.setFont(font2);
		joh.setFont(font2);
		JPEast.add(online);
		JPEast.add(dav);
		JPEast.add(and);
		JPEast.add(ande);
		JPEast.add(joa);
		JPEast.add(joh);

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
		
		//Listeners
		JBSend.addActionListener( this );
		JBSendAll.addActionListener( this );
		JBLogout.addActionListener( this );
		JBAddFile.addActionListener( this );
		
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
	// visar text från client
	public void showMessage(String mess){
		showMess.insert(mess + "\n",0);
	}



	public void actionPerformed(ActionEvent e) {
			if ( e.getSource() == JBSend){
				controller.put(JTFmessage.getText());
			}
			if (e.getSource() == JBSendAll){
				
			}
			if (e.getSource() == JBLogout){
				
			}
			if (e.getSource() == JBAddFile){
				
			}
			if (e.getSource() == dav){
				
			}
			if (e.getSource() == and){
				
			}
			if (e.getSource() == ande){
				
			}
			if (e.getSource() == joa){
				
			}
			if (e.getSource() == joh){
				
			}
		
						
		}
}


