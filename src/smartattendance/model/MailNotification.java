package smartattendance.model;

import java.time.LocalDateTime;

public class MailNotification {
    private int id;
    private int user_id;
    //private String recipient;
    private String subject;
    private String message;
    private LocalDateTime sentTime;
    private String status;

    public MailNotification(int id, int user_id, String subject, String message, LocalDateTime sentTime, String status) {
        this.id = id;
        this.user_id = user_id;
        this.subject = subject;
        this.message = message;
        this.sentTime = sentTime;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public int getRecipient() { return user_id; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public LocalDateTime getSentTime() { return sentTime; }  // ✅ Must exist
    public String getStatus() { return status; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setRecipient(int user_id) { this.user_id = user_id; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setMessage(String message) { this.message = message; }
    public void setSentTime(LocalDateTime sentTime) { this.sentTime = sentTime; }
    public void setStatus(String status) { this.status = status; }
}
