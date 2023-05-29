package applisClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*ce client recoit les deux questions du service
 
*/

public class LoaningCustomerApp {
	private final static int PORTLOANING	 = 1001;
	private final static String HOST = "localhost";
	
	public static void main(String[] args) {
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));			
		
		Socket socket = null;		
		try {
			socket = new Socket(HOST, PORTLOANING);
			BufferedReader sin = new BufferedReader (new InputStreamReader(socket.getInputStream ( )));
			PrintWriter sout = new PrintWriter (socket.getOutputStream ( ), true);
			// Informe l'utilisateur de la connection
			System.out.println("Connecte au serveur " + socket.getInetAddress() + ":"+ socket.getPort());

			String line;
			line = sin.readLine();
			int numLines = Integer.parseInt(line);

			for (int i = 0; i < numLines + 2; i++) { // +2 for the 2 next lines
				line = sin.readLine();
				System.out.println(line);
			}

			// TODO: check if the input is a number
			// prompt d'invite a la saisie
			System.out.print("->");
			line = clavier.readLine();
			sout.println(line);
			line = sin.readLine(); // second question
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
		catch (IOException e2) { }
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
