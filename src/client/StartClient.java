package client;

import javax.swing.JFrame;

public class StartClient {
	
	public static void main(String[] args) {
		ClientController controller = new ClientController();
		ClientGUI gui = new ClientGUI(controller);
		
		JFrame frame1 = new JFrame( "ClientGUI" );
		frame1.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame1.add( gui );
		frame1.pack();
		frame1.setVisible( true );
	}

}
