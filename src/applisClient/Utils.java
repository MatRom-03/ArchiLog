package applisClient;

import java.io.BufferedReader;
import java.io.IOException;

public class Utils {
    public static int checkInput(String line, BufferedReader clavier) throws IOException {
        while (!isNumeric(line)) {
            System.out.println("Veuillez entrer un numéro de document valide !");
            System.out.print("->");
            line = clavier.readLine();
        }
        return Integer.parseInt(line);
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
