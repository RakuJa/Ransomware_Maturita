package decryptor;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.DefaultCaret;

public class DiskParser{
	
	//MAIN GUI
	private static JProgressBar progressBar;
	private static JFrame frame;
	private static JPanel panel;
	
	private static JTextArea textArea;
	private JScrollPane scroller = null;
	
	//private static String defaultPath = MainDecryptor.getDefaultPath(); 

	public DiskParser() {
		
		
		createInterface();
		
		//TODO ASWE
		//boolean toDelete = first==null || second == null;
		
		File[] paths;
		FileSystemView fsv = FileSystemView.getFileSystemView();

		// returns pathnames for files and directory
		paths = File.listRoots();
		

		// for each pathname in pathname array
		for (File path : paths) {
			// prints file and directory paths
			System.out.println("Drive Name: " + path);
			//if (path.toString().equals(defaultPath)) {
				//Decrypto Installer
				new FileParser(path);
			//}
			
		}
		
	}
	
	/**
	 * Method used to setup user interface
	 */
	private void createInterface() {
		
		frame = new JFrame("Decrypter,It will take some time..");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(true);
		//panel.add(changeFolder);
		
		//Where the GUI is constructed:
		textArea = new JTextArea(15, 50);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		progressBar = new JProgressBar(0,100);
		progressBar.setIndeterminate(true);
		
		scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//Adds element to panel and frame
		panel.add(scroller);
		panel.add(progressBar);
		frame.add(panel);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.setResizable(false);	
		
	}
	
	public static synchronized void appendText(String text) {
		
		textArea.append(text + "\n");
		
	}
	
	
	public static boolean isFinished() {
		return true;
	}
	
}
