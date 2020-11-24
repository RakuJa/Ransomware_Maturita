package encryptor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class FinishedMessage extends Thread{
	
	//Main GUI
	private JFrame frame = null;
	
	//Panels
	private JPanel mainPanel;
	private JPanel northPanel;
	private JPanel southPanel;
	
	
	//Buttons and texts pane
	private JTextPane textArea;
	private JButton closeSoftware = null;
	
	public FinishedMessage() {
		
		this.start();
		
	}
	
	@Override
	public void run() {

		// Setting up frame
		frame = new JFrame("Wannacry emulat0r");
		// Setting up main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		// Setting north panel
		northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.setOpaque(true);
		// Setting southern panel
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
		southPanel.setOpaque(true);

		// Adding empty padding to main panel

		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Creation of textArea where messages will be displayed

		textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setText("<html> <b> What Happened to My Computer? </b>" +"<br>"
				+ "Your important files are encrypted." + "<br>"
				+ "Many of your documents,photos,videos and other files are no longer" + "<br>"
				+ "accessible because they have been encrypted.Maybe you are busy looking for a way to" + "<br>"
				+ "Recover your files,but do not waste your time.<b>Nobody</b> can recover your files without" + "<br>"
				+ "<b> OUR </b> decryption service" + "<br>"
				+ "Can I Recover My Files?" + "<br>"
				+ "Sure. We can guarantee that you can recover all your files safely and easily." + "<br>"
				+ "You will have to install our decryption service and follow the instructions there." + "<br>"
				+ "<b> What is the purpose of this software? </b>" + "<br>"
				+ "This softare's purpose is to emulate how a ransomware will operate." + "<br>"
				+ "Understanding how malwares work and operate is important in defending ourself against them!" + "<br>"
				+ "This software has been created by <b>Daniele Giachetto</b>" + "<br>" 
				
				+ "<br> <html>");

		// Set attributes for textArea and put it into a scroller
		textArea.setEditable(false);
		textArea.setFont(Font.getFont(Font.SANS_SERIF));

		// Set up buttons name
		closeSoftware = new JButton("Close");

		// Listener for changeFolder button
		closeSoftware.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				frame.dispose();
			}
		});

		// Add component to panel
		northPanel.add(textArea);

		// Add component to panel (left right button)

		southPanel.add(closeSoftware);
		southPanel.add(Box.createHorizontalGlue());
		//ADD SOMETHING MAYBE

		// Add panel to frame and then sets properties

		mainPanel.add(northPanel);
		mainPanel.add(southPanel);

		frame.add(mainPanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		// frame.setResizable(false);

	}

}
