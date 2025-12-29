package smartattendance.model;

import java.time.LocalDate;

public class AdminRequest {

    private int id;
    private String type;
    private LocalDate date;
    private String details;
    private String status;

    // ----------------------
    // CONSTRUCTORS
    // ----------------------
    public AdminRequest() {
    }

    public AdminRequest(int id, String type, LocalDate date, String details, String status) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.details = details;
        this.status = status;
    }

    // ----------------------
    // GETTERS & SETTERS
    // ----------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
