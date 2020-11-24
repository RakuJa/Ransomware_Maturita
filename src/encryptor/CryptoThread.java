package encryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class used to encrypt files
 * @author Giachetto Daniele
 *
 */
public class CryptoThread extends Thread{
	
	private File fileToEncrypt;
	private SecretKeySpec simmetricKey;
	
	private static int count;

	/**
	 * Constructor that starts the thread
	 * @param file file to be encrypted
	 * @param skeySpec key used to encrypt
	 */
	public CryptoThread(File file,SecretKeySpec skeySpec) {
		this.simmetricKey = skeySpec;
		this.fileToEncrypt = file;
		if (fileToEncrypt!= null) {
			this.start();
		}
	}
	
	@Override
	public void run() {

		Path path = fileToEncrypt.toPath();

		try (FileInputStream inputStream = new FileInputStream(fileToEncrypt);
				FileOutputStream destinationStream = new FileOutputStream(path.toString() + ".GD")) {
			if (encrypt(inputStream, destinationStream)) {
				inputStream.close();
				destinationStream.close();
				//MAYBE LOG?
				fileToEncrypt.delete();
			}
		} catch (IOException e) {
			System.out.println("FAILED TO ENCRYPT");
			e.printStackTrace();
		} finally {
			upCount();
		}
	}
	
	/**
	 * Method used to encrypt a file from given stream and write it in the other stream
	 * @param inputStream stream from which the file will be encrypted
	 * @param destinationStream stream in which to write the encrypted file
	 * @return true if operation was successful
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public boolean encrypt(FileInputStream inputStream, FileOutputStream destinationStream) throws IOException {
		int read;
		CipherInputStream cis = null;
		try {
			Cipher encipher = Cipher.getInstance("AES");
			encipher.init(Cipher.ENCRYPT_MODE, simmetricKey);
			cis = new CipherInputStream(inputStream, encipher);
			// Reads until reaches end of file
			synchronized (cis) {
				while ((read = cis.read()) != -1) {
					destinationStream.write((char) read);
					//destinationStream.flush(); it bugs everything,cipher calls it automatic
				}
			}
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (cis != null) {
				cis.close();
			}
		}
		return true;
	}

	/**
	 * Increments by one "count" variable
	 */
	private synchronized static void upCount() {
		++count;
	}
	
	/**
	 * Gets the value of "count" variable
	 * @return value of count variable
	 */
	public static int getCount() {
		return count;
	}


}
