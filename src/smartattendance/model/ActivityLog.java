package smartattendance.model;

public class ActivityLog {
    private int logId;
    private String user;
    private String action;
    private String description;
    private String timestamp;
    private String ipAddress;

    public ActivityLog(int logId, String user, String action, String description, String timestamp, String ipAddress) {
        this.logId = logId;
        this.user = user;
        this.action = action;
        this.description = description;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
