package server.logic;

import protocols.EmailSender;
import subscribers.Abonne;

import javax.mail.MessagingException;
import java.util.ArrayList;

public class EmailManager {
    /**
     * Send an eamil for all the abonnes in the list
     * @param abonnes list of abonnes
     * @param numeroDocument the document number
     */
    public static void sendEmails(ArrayList<Abonne> abonnes, int numeroDocument) {

        if (abonnes.size() == 0) {
            return;
        }

        Thread sendEmailsThread = new Thread(() -> {
            System.out.println("=========================================");
            System.out.println("Sending emails to subscribers");
            for (Abonne abonne : abonnes) {
                try {
                    EmailSender.sendEmail("Votre document est disponible", "Bonjour " + abonne.getName() + ",\n\n" +
                            "Le document " + Media_library.getDocument(numeroDocument).getTitre() + " est disponible.\n" +
                            "Vous pouver l'emprunter a la bibliotheque ou le reserver sur votre application de reservation.\n\n" +
                            "Cordialement,\n" +
                            "La bibliotheque");
                } catch (MessagingException e) {
                    System.out.println("Failed to send email: " + e.getMessage());
                }
            }
            System.out.println(abonnes.size() + " Emails sent");
            abonnes.clear();
        });

        sendEmailsThread.start();
    }
}
