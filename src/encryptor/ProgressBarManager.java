package encryptor;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressBarManager extends Thread{

	private JProgressBar progressBar;
	
	private JFrame mainFrame;
	
	public ProgressBarManager(JProgressBar progressBar,JFrame mainFrame) {
		
		this.progressBar = progressBar;
		this.mainFrame = mainFrame;
		
		this.start();
		
	}
	
	@Override
	public void run() {

		//While it hasn't finished counting the amount of files wait
		while (!CountFilesToEncrypt.isFinished()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//Gets the amount of files counted
		int nFiles = (int) CountFilesToEncrypt.getFilesCount();

		//If it is equal to 0 we are done
		if (nFiles == 0) {
			progressBar.setValue(100);
			progressBar.update(progressBar.getGraphics());
			System.exit(0);
		} else {
			//While it hasn't reached 100
			int number = 0;
			float value = 0;
			int previous = number;
			while (progressBar.getValue() < 100) {
				number = CryptoThread.getCount();
				if (previous != number) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Thread interrupted");
					}
					// Value is the percentage of files scanned compared to the total
					value = (number * 100) / nFiles;
					if (value > 99 && value < 100) {
						progressBar.setValue(99);
					} else {
						progressBar.setValue((int) value);
					}
					progressBar.update(progressBar.getGraphics());
					previous = number;
				}else {
					try {
						Thread.sleep(2500);
					}catch (InterruptedException e) {
						System.out.println("Thread interrupted");
					}
				}
			}
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
			}
			
			System.out.println("File finished : " + number);
			
			System.out.println("Total file : " + nFiles);
			
			System.out.println("Percentage : " + value);
			
			
			
			new FinishedMessage();
			
			System.out.println("Disposing now");
			
			mainFrame.dispose();
			
			System.out.println("Disposed");
			
			
			
			
		}
		
		
	}

}
