package applisClient.templates;

import protocols.Reader;
import protocols.Writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import static applisClient.Utils.checkInput;

public class ReturningTemplate {
    public static void returning(Socket clientSocket, BufferedReader clavier) throws IOException {
        String line;

        System.out.println(Reader.readString(clientSocket)); // first question

        System.out.print("->");
        line = clavier.readLine();
        int numeroDocument = checkInput(line, clavier);
        Writer.writeInteger(clientSocket, numeroDocument);

        System.out.println(Reader.readString(clientSocket)); // final answer
    }
}
