package decryptor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;


public class MainDecryptor {
	
	private static final String defaultPath = "E:\\";

	//Main GUI
	private JFrame frame = null;
	
	private JPanel mainPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel northPanel;
	private JPanel southPanel;
	
	
	
	//Buttons and texts area
	private JTextPane textArea;
	private JButton nextButton = null;
	private JScrollPane scroller = null;
	
	private JLabel picLabel;
	
	
	public static void main (String argv[]) {
		
		new MainDecryptor().createInterface();
		
	}
	@Deprecated
	public static String getDefaultPath() {
		return defaultPath;
	}

	/**
	 * Method used to setup user interface
	 */
	private void createInterface() {
		
		frame = new JFrame("File Decryptor!");
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.LINE_AXIS));
		mainPanel.setOpaque(true);
		//mainPanel.setBackground(Color.RED);
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setOpaque(true);
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setOpaque(true);
		
		northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
		northPanel.setOpaque(true);
		
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
		southPanel.setOpaque(true);
		
		//Adding empty padding to panel
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		textArea = new JTextPane();
		textArea.setContentType("text/html");
		
		textArea.setText("<html> <b> What Happened to My Computer? </b>" +"<br>"
				+ "Your important files are encrypted." + "<br>"
				+ "Many of your documents,photos,videos and other files are no longer" + "<br>"
				+ "accessible because they have been encrypted.Maybe you are busy looking for a way to" + "<br>"
				+ "Recover your files,but do not waste your time.<b>Nobody</b> can recover your files without" + "<br>"
				+ "<b> THIS </b> decryption software" + "<br>"
				+ "Can I Recover My Files?" + "<br>"
				+ "Sure,just press the \" Decrypt \" button. Enjoy!" + "<br>"
				+ "This software has been created by <b>Daniele Giachetto</b>" + "<br>" 
				
				+ "<br> <html>");
		
		//textArea.setBackground(Color.RED);
		
		//Creazione textArea dove verranno visualizzati messaggi
		textArea.setEditable(false);
		textArea.setFont(Font.getFont(Font.SANS_SERIF));
		scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	
		nextButton = new JButton("Decrypt!");
		
		
		InputStream imagePath = ResourceLoader.load("images/redLock.png");
		
		
		//Takes image from path,it will be displayed in the interface
		BufferedImage myPicture;
		try {
			myPicture = ImageIO.read(imagePath);
			picLabel = new JLabel(new ImageIcon(myPicture));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//Listener for next button
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("DECRYPT");
				frame.dispose();
				new CountFilesToDecrypt();
				new DProgBar();
				new DiskParser();
			}
		});
		//Adds all elements to panel and then panels to frame
		if (picLabel!=null) {
			leftPanel.add(picLabel);
		}
		
		northPanel.add(scroller);
		southPanel.add(nextButton);
		
		rightPanel.add(northPanel);
		rightPanel.add(southPanel);
		
		
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		
		
		frame.add(mainPanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.setResizable(false);
				
	}

}
