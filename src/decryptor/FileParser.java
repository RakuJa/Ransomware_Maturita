package decryptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class FileParser extends Thread{
	
	private File path;
	private BigInteger d;
	private BigInteger n;
	private SecretKeySpec skeySpec = null;
	private static boolean finished = false;
	private static int count = 0;
	
	private static String token = ";";
	
	
	private static final String privateName = "DO_NOT_DELETE_PRIVATEKEY.JKS";
	
	private static final String keyStoreName = "DO_NOT_DELETE_KEYFORDECRYPT.JKS";
	
	

	/**
	 * Constructor that will start the thread
	 * @param path directory to scan for file with default extension
	 */
	public FileParser(File path) {
		
		//Checks for private key saved in this hard disk
		getPrivateKeys(path.getAbsolutePath());
		//If keys have been correctly retrieved
		if (d != null && n != null) {
			//Checks for simmetric key saved in this hard disk
			skeySpec = getSimmetricKey(path.getAbsolutePath());
			//If keys has been  retrieved correctly
			if (skeySpec != null) {
				this.path = path;
				this.start();
			} else {
				System.out.println("Error recovering AES keys");
			}
		} else {
			System.out.println("Error recovering private keys");
		}
	}
	

	/**
	 * Method used to recover a pair of private keys from a given hard disk
	 * @param path hard disk in which keys are to be searched for
	 */
	private void getPrivateKeys(String path) {

		BufferedReader reader = null;

		try {
			
			//Hard disk path + default name for private key will be the file's name
			//If it has been stored correctly
			File privateKeyFile = new File(path + privateName);

			if (privateKeyFile.exists()) {

				//Reads from file the two keys
				reader = new BufferedReader(new FileReader(privateKeyFile));

				d = new BigInteger(reader.readLine());

				n = new BigInteger(reader.readLine());

				System.out.println("d : " + d);
				System.out.println("n : " + n);

			}
		} catch (IOException e) {

			e.printStackTrace();

		}finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	/**
	 * Method used to decrypt an encrypted simmetric key using private keys
	 * 
	 * @param data
	 *            String to decrypt
	 * @param d
	 *            private key
	 * @param n
	 *            private key
	 * @return the message 
	 * 
	 * decrypted 
	 */
	private byte[] decryptSimmetricKey(String data, BigInteger d, BigInteger n) {

		StringTokenizer dama = new StringTokenizer(data, token);

		Vector<BigInteger> intVector = new Vector<BigInteger>();

		StringBuilder stringList = new StringBuilder();

		// While it still has more ";" to search for
		while (dama.hasMoreTokens()) {
			data = dama.nextToken();
			if (!data.isEmpty()) {
				// Convert String into BigInteger
				BigInteger i = new BigInteger(data);
				// RSA Operation
				BigInteger m = i.modPow(d, n);
				intVector.add(m);
				stringList.append((char) m.longValueExact());
			}
		}
		
		byte[] decodedBytes = Base64.getDecoder().decode(stringList.toString());
		
		return decodedBytes;
	}


	/**
	 * Method used to retrieve the simmetric key from the hard disk given
	 * @param path
	 * @return
	 */
	private SecretKeySpec getSimmetricKey(String path) {
		
		BufferedReader reader = null;
		
		try {

			File keyStoreFile = new File(path + keyStoreName);

			if (keyStoreFile.exists()) {
				
				reader = new BufferedReader(new FileReader(keyStoreFile));
				
				String key = reader.readLine();
				
				
				// decode the base64 encoded string
				//byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
				
				//String toDecrypt = decodedKey.toString();
				byte[] decryptedKey = decryptSimmetricKey(key,d,n);
				
				System.out.println("----INIZIO BYTE---");
				
				for (byte b : decryptedKey) {
					
					System.out.println(b);
					
				}
				
				// rebuild key using SecretKeySpec
				
				System.out.println("----FINE BYTE-----");
				
				SecretKey secret = new SecretKeySpec(decryptedKey,"AES");
				
				System.out.println("Hash :" + secret.hashCode());
				
				SecretKeySpec sKeySpec = (SecretKeySpec) secret;
				
				System.out.println("Full key : " + sKeySpec);
				
				return sKeySpec;
			}
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}


	@Override
	public void run() {
		getFile(path.toString());
	}

	/**
	 * Method used to search for ".gg" files in the given directory (using recursion)
	 * @param directoryName directory to be scanned for files
	 */
	private void getFile(String directoryName){

		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					String extension = "";

					String fileName = file.toString();
					int i = fileName.lastIndexOf('.'); // Restituisce l'ultimo indice in cui viene trovato il carattere
														// "."
					int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
					// Controlla se il file contiene il punto o se il "." si trova prima dei vari
					// backslash etc
					if (i > p) {
						extension = fileName.substring(i + 1);
					} else {
						System.out.println("File not valid");
					}
					if (extension.equalsIgnoreCase("GD")) {
						upCount();
						new DecryptoThread(file, skeySpec);
					}
				} else if (file.isDirectory()) {
					getFile(file.getAbsolutePath());
				}
			}
			
			setFinished();
		}

	}

	/**
	 * Setter that sets "finished" variable to true
	 */
	private static synchronized void setFinished() {
		finished = true;
	}

	/**
	 * Setter that increments "count" variable by one
	 */
	private synchronized static void upCount() {
		++count;
	}
	
	/**
	 * Getter that return "count" value
	 * @return the value of "count" variable
	 */
	public static int getCount() {
		return count;
	}

	/**
	 * Getter that tells if finished variable is set to true
	 * @return true if it is
	 */
	public static boolean isFinished() {
		return finished;
	}

}
