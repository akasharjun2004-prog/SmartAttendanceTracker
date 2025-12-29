package smartattendance.util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;

public class MailUtil {

    private static String FROM_EMAIL;
    private static String PASSWORD;

    static {
        try (java.io.InputStream input = MailUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties config = new Properties();
            config.load(input);

            FROM_EMAIL = config.getProperty("email.user");
            PASSWORD = config.getProperty("email.password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }	

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    // Returns true if email sent successfully
    public static boolean sendEmailWithStatus(String toEmail, String subject, String messageText, String attachmentPath) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // Text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(messageText);
            multipart.addBodyPart(textPart);

            // Attachment part (QR image)
            if (attachmentPath != null && !attachmentPath.isEmpty()) {
                File file = new File(attachmentPath);
                if (file.exists()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(file);
                    multipart.addBodyPart(attachPart);
                } else {
                    System.out.println("QR file not found: " + attachmentPath);
                }
            }

            message.setContent(multipart);
            Transport.send(message);

            System.out.println("Email sent to " + toEmail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
