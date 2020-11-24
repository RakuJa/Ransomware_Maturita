package decryptor;

import javax.swing.JOptionPane;

public class DProgBar extends Thread {

	
	public DProgBar() {
		this.start();
	}

	@Override
	public void run() {
		// While it hasn't finished counting the amount of files wait
		System.out.println("Counting files");
		while (!CountFilesToDecrypt.isFinished()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long nFiles = CountFilesToDecrypt.getFilesCount();

		long number = DecryptoThread.getCount();

		while (nFiles != number) {

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
			}

			System.out.println("Tot : " + nFiles);
			System.out.println("Decrypted : " + number);
			
			number = DecryptoThread.getCount();

		}

		// mainFrame.dispose();
		
		JOptionPane.showMessageDialog(null,"Decription process finished!" , "", JOptionPane.INFORMATION_MESSAGE);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(0);


	}

}
