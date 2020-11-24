package decryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DecryptoThread extends Thread{
	
	private File fileToDecrypt;

	private SecretKeySpec simmetricKey;
	
	private static int count;
	
	/**
	 * Constructor,starts the thread
	 * @param toDecrypt is the file that needs to be decrypted
	 * @param key is the key that will decrypt the file (the same used for encryption)
	 */
	public DecryptoThread(File toDecrypt,SecretKeySpec key) {
		
		this.fileToDecrypt = toDecrypt;
		this.simmetricKey = key;
		
		this.start();
		
	}
	
	@Override
	public void run() {

		Path path = fileToDecrypt.toPath();

		
		String fileDest = path.toString();
		
		//Removes the extension from the file (.gg)
		fileDest = path.toString().substring(0, fileDest.length() - 3);

		//Creates stream 
		try (FileInputStream inputStream = new FileInputStream(fileToDecrypt);
				FileOutputStream destinationStream = new FileOutputStream(fileDest)) {
			if (decrypt(inputStream, destinationStream)) {
				// After decrypting deletes the file
				if (fileToDecrypt.exists()) {
					inputStream.close();
					destinationStream.close();
					fileToDecrypt.delete();
				}
			}else {
				System.out.println("Failed to Decrypt");
			}
		} catch (IOException e) {
			System.out.println("FAILED TO DECRYPT");
			e.printStackTrace();
		} finally {
			//Number of file decrypted
			upCount();
			DiskParser.appendText(fileToDecrypt.getName());
		}

	}
	
	/**
	 * Method used to decrypt a file given his stream and writes it to the other stream given
	 * @param inputStream file's stream to be decrypted 
	 * @param destinationStream destination file's stream,decrypted file will be written here 
	 * @return true if operation was successful
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException
	 */
	public boolean decrypt(FileInputStream inputStream, FileOutputStream destinationStream) throws IOException {
		int read;
		CipherOutputStream cos = null;
		try {
			Cipher decipher = Cipher.getInstance("AES");
			decipher.init(Cipher.DECRYPT_MODE, simmetricKey);
			//Lascia così o si bugga alla chiusura del file
	        cos = new CipherOutputStream(destinationStream,decipher);
	        //Reads from stream until it reaches the end of file
			synchronized (cos) {
				while ((read = inputStream.read()) != -1) {
					// writes to other stream
					cos.write(read);
					//cos.flush();
				}
			}
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			return false;
		}finally {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Closes all the stream (If not done it will fail encryption and decryption)
			if (cos!=null) {
				cos.close();
			}
		}
		return true;
	}
	
	/**
	 * Setter that will increment by one "count" variable 
	 */
	private synchronized static void upCount() {
		++count;
	}
	
	/**
	 * Getter for "count" variable
	 * @return "count" value
	 */
	public static int getCount() {
		return count;
	}

}
