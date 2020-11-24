package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String arg[]) throws IOException {
		ServerSocket server = null;
		try {
			// Apertura del socket destinato ad accettare connessioni
			server = new ServerSocket(55555);
			System.out.println("Opening port...");
			while (true) {
				// Viene attesa ed accettata una connessione
				Socket s = server.accept();

				// accettata ed assegnata ad un thread
				System.out.println(s.getInetAddress().getHostAddress() + " Connected");
				new Persona(s);
			}
		} catch (IOException e) {
			// Se l'apertura della porta fallisce viene mostrato un messaggio
			System.out.println("Problems encountered opening port,Server closing..");
		} finally {
			// Viene chiusa la porta del server
			server.close();
		}
		
	}
}
