package applisClient;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class MP3Player {
    private static Player player;

    /**
     * Play a MP3 file
     * @param filePath the path of the MP3 file
     */
    public static void play(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);

            Thread playbackThread = new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture du fichier MP3 : " + e.getMessage());
                }
            });

            playbackThread.start();
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier MP3 : " + e.getMessage());
        }
    }

    /**
     * Stop the MP3 player
     */
    public static void stop() {
        if (player != null) {
            player.close();
        }
    }
}