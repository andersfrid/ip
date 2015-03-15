package server;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;

public class test {

	private static JPanel iconPanel;
	private JLabel iconLast, iconText, iconAuthor;
	
	public test() {
		iconPanel = new JPanel();
		iconLast = new JLabel("");
		iconText = new JLabel("Senaste bilden: ");
		iconAuthor = new JLabel("Avsändare");
		
		iconPanel.setPreferredSize(new Dimension(1000,1000));
		iconPanel.add(iconText);
		iconPanel.add(iconLast);
		iconPanel.add(iconAuthor);
		
	}
	
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, iconPanel);
	}
}
