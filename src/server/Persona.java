package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Vector;

public class Persona extends Thread{
	
	private Socket s = null;
	
	
	public Persona(Socket s) {
		this.s = s;
		this.start();
	}
	
	@Override
	public void run() {
		
		try {
			while(!keyExchange());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method used to send public key to client when connection is established
	 * @return true if key is exchanged successfully
	 * @throws IOException if there are problems with the connection
	 */
	private boolean keyExchange () throws IOException{ 
		
		while(!listenForRequest("/KEY"));

		boolean keyGenCorr = false;
		
		KeyGenerator keyG = null;
		
		//0,1 private keys 2,3 public keys
		Vector<BigInteger> keysToSend = null;
		
		while (!keyGenCorr) {
			
			
			keyG = new KeyGenerator();
			try {
				keyG.join();
			} catch (InterruptedException e) {
				return false;
			}
			if (keyG!=null && (keysToSend=keyG.getAllKeys())!=null) {
			
				keyGenCorr = !keysToSend.isEmpty() && keysToSend.size()==3 && keysToSend.get(0)!=null && keysToSend.get(1)!=null &&
						keysToSend.get(2)!=null;
				
			}
			
		}
		try {
			sendKeys(keysToSend);
		} catch (IOException e) {
			return false;
		}

		while (!listenForRequest("/KEYGOT"));
		
		return true;
	}
	
	/**
	 * method used to listen to a certain command from stream
	 * @param request String to listen to from stream
	 * @return true if expected request is received
	 * @throws IOException in case problems with streams are encountered
	 */
	private boolean listenForRequest(String request) throws IOException{
		String input = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			input = br.readLine();
		}catch (IOException e) {
			return false;
		}
			if (input != null) {
				if (input.equalsIgnoreCase(request)) {
					return true;
					//If client wants to end key exhange exception is thrown and socket later closed
				}else if (input.equalsIgnoreCase("/NO")){
					throw new IOException();
				}

			}
		return false;
	}
	
	/**
	 * Method used to send on stream previously generated public keys
	 * @param publicKeys Vector that contains two keys
	 * @throws IOException if there are problems with the stream
	 */
	private void sendKeys(Vector<BigInteger> publicKeys) throws IOException{
		if (s!=null) {
			ObjectOutputStream outS = new ObjectOutputStream(s.getOutputStream());
			if (publicKeys != null) {
				outS.reset();
				outS.writeObject(publicKeys);
				outS.flush();
			} else {
				throw new IOException();
			}
		}else {
			throw new IOException();
		}	
	}
	
	

}
