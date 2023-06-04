package applisClient.templates;

import protocols.Reader;
import protocols.Writer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import static applisClient.Utils.checkInput;


public class LoaningTemplate {
    public static void loaning(Socket clientSocket, BufferedReader clavier) throws IOException {
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
    }
}
