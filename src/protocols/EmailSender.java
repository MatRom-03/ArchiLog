package protocols;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private static final String SMTP_HOST = "mail.mehdi-ali.me";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "legrandwakantanka@mehdi-ali.me";
    private static final String PASSWORD = "@b1t5ib5uhgBzpN4";
    private static final String TO_EMAIL = "jean-francois.brette@u-paris.fr";

    public static void sendEmail(String subject, String message) throws MessagingException {
        System.out.println("Gloire au grand Wakan Tanka BadiiiX pour le serveur SMTP");

        // Configuration des propriétés du serveur SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);

        // Création d'une session avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        // Création du message e-mail
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(USERNAME));
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO_EMAIL));
        mimeMessage.setSubject(subject);
        mimeMessage.setText(message);

        // Envoi du message
        Transport.send(mimeMessage);
    }
}
