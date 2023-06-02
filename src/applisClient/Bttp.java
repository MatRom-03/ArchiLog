package applisClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Bttp {

	private final static int PORTBOOKING 	= 1000;
	private final static int PORTLOANING	= 1001;
	private final static int PORTRETURNING 	= 1002;
	public static void main(String[] args) {
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
		final int PORT = Integer.parseInt(args[1]);
		final String HOST = args[0];
		
		Socket socket = null;		
		try {
			socket = new Socket(HOST, PORT);
			BufferedReader sin = new BufferedReader (new InputStreamReader(socket.getInputStream ( )));
			PrintWriter sout = new PrintWriter (socket.getOutputStream ( ), true);
			// Informe l'utilisateur de la connection
			System.out.println("Connecte au serveur " + socket.getInetAddress() + ":"+ socket.getPort());

			String line;

			if (PORT==PORTBOOKING)
			{
				line = sin.readLine();
				int numLines = Integer.parseInt(line);

				for (int i = 0; i < numLines + 2; i++) { // +2 for the 2 next lines
					line = sin.readLine();
					System.out.println(line);
				}

				// prompt d'invite a la saisie
				System.out.print("->");
				line = clavier.readLine();
				line = checkInput(line, clavier);
				sout.println(line);

				line = sin.readLine(); // second question
				System.out.println(line);
				// prompt d'invite a la saisie
				System.out.print("->");
				line = clavier.readLine();
				line = checkInput(line, clavier);
				sout.println(line);
				
				System.out.println(sin.readLine()); // reponse finale
		
				socket.close();
			}
			else if (PORT==PORTLOANING)
			{
				line = sin.readLine();
				int numLines = Integer.parseInt(line);

				for (int i = 0; i < numLines + 2; i++) { // +2 for the 2 next lines
					line = sin.readLine();
					System.out.println(line);
				}

				// prompt d'invite a la saisie
				System.out.print("->");
				line = clavier.readLine();
				line = checkInput(line, clavier);
				sout.println(line);

				line = sin.readLine(); // second question
				System.out.println(line);
				// prompt d'invite a la saisie
				System.out.print("->");
				line = clavier.readLine();
				line = checkInput(line, clavier);
				sout.println(line);
				
				System.out.println(sin.readLine()); // reponse finale
		
				socket.close();
			}
			else if (PORT==PORTRETURNING)
			{
				for (int i = 0; i < 2; i++) {
					line = sin.readLine();
					System.out.println(line);
				}

				// prompt d'invite a la saisie
				System.out.print("->");
				line = clavier.readLine();
				line = checkInput(line, clavier);
				sout.println(line);

				System.out.println(sin.readLine()); // reponse finale

				socket.close();
			}
		}
		catch (IOException e) { System.err.println("Fin du service !"); }
		// Refermer dans tous les cas la socket
		try { if (socket != null) socket.close(); } 
		catch (IOException e2) { }
	}

	private static String checkInput(String line, BufferedReader clavier) throws IOException {
		while (!isNumeric(line)) {
			System.out.println("Veuillez entrer un nombre !");
			System.out.print("->");
			line = clavier.readLine();
		}
		return line;
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
