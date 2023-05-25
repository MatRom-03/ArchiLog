package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*ce client recoit les deux questions du service
 
*/

public class AppliClient {
	private final static int PORTBOOKING = 1000;
	private final static int PORTLOAN	 = 1001;
	private final static int PORTRETURN = 1002;
		private static String HOST = "localhost"; 
	
	public static void main(String[] args) throws IOException {
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));			
		
		Socket socket = null;		
		try {
			socket = new Socket(HOST, PORTBOOKING);
			BufferedReader sin = new BufferedReader (new InputStreamReader(socket.getInputStream ( )));
			PrintWriter sout = new PrintWriter (socket.getOutputStream ( ), true);
			// Informe l'utilisateur de la connection
			System.out.println("Connecte au serveur " + socket.getInetAddress() + ":"+ socket.getPort());

			String line;
			line = sin.readLine(); // 1ere question
			System.out.println(line);
			// prompt d'invite a la saisie
			System.out.print("->");
			line = clavier.readLine();
			sout.println(line);
			line = sin.readLine(); // 2eme question
			System.out.println(line);
			// prompt d'invite a la saisie
			System.out.print("->");
			line = clavier.readLine();
			sout.println(line);
			
			System.out.println(sin.readLine()); // reponse finale
	
			socket.close();
		}
		catch (IOException e) { System.err.println("Fin du service"); }
		// Refermer dans tous les cas la socket
		try { if (socket != null) socket.close(); } 
		catch (IOException e2) { ; }		
	}

	private static boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
