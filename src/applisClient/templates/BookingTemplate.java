package applisClient.templates;

import applisClient.MP3Player;
import protocols.Reader;
import protocols.Writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;

import static applisClient.Utils.checkInput;


public class BookingTemplate {
    public static void booking(Socket clientSocket, BufferedReader clavier) throws IOException {

        String line;

        System.out.println(Reader.readString(clientSocket)); // catalog

        System.out.println(Reader.readString(clientSocket)); // first question

        System.out.print("->");
        line = clavier.readLine();
        int numeroDocument = checkInput(line, clavier);
        Writer.writeInteger(clientSocket, numeroDocument);

        System.out.println(Reader.readString(clientSocket)); // second question
        System.out.print("->");
        line = clavier.readLine();
        int numeroAbonne = checkInput(line, clavier);
        Writer.writeInteger(clientSocket, numeroAbonne);

        System.out.println(Reader.readString(clientSocket)); // final answer

        // if the document is already booked but not for too long.
        if(Reader.readBoolean(clientSocket)) {
            String filePath = FileSystems.getDefault().getPath("src/applisClient/RickRoll.mp3").normalize().toAbsolutePath().toString();
            Reader.readFile(clientSocket, filePath);

            MP3Player.play(filePath);

            System.out.println(Reader.readString(clientSocket)); // to stop musique

            MP3Player.stop();

            System.out.println(Reader.readString(clientSocket)); // end message
        }

        if(Reader.readBoolean(clientSocket)) {
            System.out.println(Reader.readString(clientSocket)); // alert answer
            System.out.print("->");
            line = clavier.readLine();

            while (!line.equalsIgnoreCase("o") && !line.equalsIgnoreCase("n")) {
                System.out.println("Veuillez entrer une réponse valide !");
                System.out.print("->");
                line = clavier.readLine();
            }
            if (line.equalsIgnoreCase("o")) {
                Writer.writeBoolean(clientSocket, true);
            } else if (line.equalsIgnoreCase("n")) {
                Writer.writeBoolean(clientSocket, false);
            }

            System.out.println(Reader.readString(clientSocket)); // end message
        }
    }
}
