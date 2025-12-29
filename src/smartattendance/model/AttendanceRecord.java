package smartattendance.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceRecord {

    // --- Backend fields (used for DAO / service logic) ---
    private int attendanceId;
    private int userId;
    private LocalDate attendanceDate;
    private LocalDateTime loginDateTime;
    private LocalDateTime logoutDateTime;
    private String totalHours;
    private String totalBreaks;
    private String actualWorkTime;
    private String workStatus;
    private LocalDateTime createdAt;
    private String loginType; // added to support "manual" or "qr"

    // --- UI fields (TableView friendly) ---
    private final StringProperty employeeId;
    private final StringProperty name;
    private final StringProperty date;
    private final StringProperty loginTime;
    private final StringProperty logoutTime;
    private final StringProperty totalHoursProperty;
    private final StringProperty managerId;

    // ---------------------------
    // NO-ARG constructor (required by service)
    // ---------------------------
    public AttendanceRecord() {
        this.attendanceId = -1;
        this.userId = -1;
        this.attendanceDate = null;
        this.loginDateTime = null;
        this.logoutDateTime = null;
        this.totalHours = "";
        this.totalBreaks = "";
        this.actualWorkTime = "";
        this.workStatus = "";
        this.createdAt = null;
        this.loginType = "";

        this.employeeId = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
        this.loginTime = new SimpleStringProperty("");
        this.logoutTime = new SimpleStringProperty("");
        this.totalHoursProperty = new SimpleStringProperty("");
        this.managerId = new SimpleStringProperty("");
    }

    // ---------------------------
    // FULL Constructor for DAO (optional)
    // ---------------------------
    public AttendanceRecord(
            int attendanceId,
            int userId,
            LocalDate attendanceDate,
            LocalDateTime loginDateTime,
            LocalDateTime logoutDateTime,
            String totalHours,
            String totalBreaks,
            String actualWorkTime,
            String workStatus,
            LocalDateTime createdAt
    ) {
        this();
        this.attendanceId = attendanceId;
        this.userId = userId;
        this.attendanceDate = attendanceDate;
        this.loginDateTime = loginDateTime;
        this.logoutDateTime = logoutDateTime;
        this.totalHours = totalHours;
        this.totalBreaks = totalBreaks;
        this.actualWorkTime = actualWorkTime;
        this.workStatus = workStatus;
        this.createdAt = createdAt;

        // update UI props
        this.employeeId.set(String.valueOf(userId));
        this.date.set(attendanceDate != null ? attendanceDate.toString() : "");
        this.loginTime.set(loginDateTime != null ? loginDateTime.toLocalTime().toString() : "");
        this.logoutTime.set(logoutDateTime != null ? logoutDateTime.toLocalTime().toString() : "");
        this.totalHoursProperty.set(totalHours != null ? totalHours : "");
    }

    // ------------------------------------
    // UI Constructor for Dashboard Table
    // ------------------------------------
    public AttendanceRecord(
            String employeeId,
            String name,
            String date,
            String loginTime,
            String logoutTime,
            String totalHours,
            String managerId
    ) {
        this();
        this.employeeId.set(employeeId);
        this.name.set(name);
        this.date.set(date);
        this.loginTime.set(loginTime);
        this.logoutTime.set(logoutTime);
        this.totalHoursProperty.set(totalHours);
        this.managerId.set(managerId);
    }

    // --- Backend Getters ---
    public int getAttendanceId() { return attendanceId; }
    public int getUserId() { return userId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public LocalDateTime getLoginDateTime() { return loginDateTime; }
    public LocalDateTime getLogoutDateTime() { return logoutDateTime; }
    public String getTotalHours() { return totalHours; }
    public String getTotalBreaks() { return totalBreaks; }
    public String getActualWorkTime() { return actualWorkTime; }
    public String getWorkStatus() { return workStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getLoginType() { return loginType; }

    // --- Backend Setters (added for service/DAO usage) ---
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    public void setUserId(int userId) {
        this.userId = userId;
        this.employeeId.set(String.valueOf(userId));
    }
    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
        this.date.set(attendanceDate != null ? attendanceDate.toString() : "");
    }
    public void setLoginDateTime(LocalDateTime loginDateTime) {
        this.loginDateTime = loginDateTime;
        this.loginTime.set(loginDateTime != null ? loginDateTime.toLocalTime().toString() : "");
    }
    public void setLogoutDateTime(LocalDateTime logoutDateTime) {
        this.logoutDateTime = logoutDateTime;
        this.logoutTime.set(logoutDateTime != null ? logoutDateTime.toLocalTime().toString() : "");
    }
    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
        this.totalHoursProperty.set(totalHours != null ? totalHours : "");
    }
    public void setTotalBreaks(String totalBreaks) { this.totalBreaks = totalBreaks; }
    public void setActualWorkTime(String actualWorkTime) { this.actualWorkTime = actualWorkTime; }
    public void setWorkStatus(String workStatus) { this.workStatus = workStatus; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLoginType(String loginType) { this.loginType = loginType; }

    // --- UI Property Getters ---
    public StringProperty employeeIdProperty() { return employeeId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty dateProperty() { return date; }
    public StringProperty loginTimeProperty() { return loginTime; }
    public StringProperty logoutTimeProperty() { return logoutTime; }
    public StringProperty totalHoursProperty() { return totalHoursProperty; }
    public StringProperty managerIdProperty() { return managerId; }

    // --- UI Getters ---
    public String getEmployeeId() { return employeeId.get(); }
    public String getName() { return name.get(); }
    public String getDate() { return date.get(); }
    public String getLoginTime() { return loginTime.get(); }
    public String getLogoutTime() { return logoutTime.get(); }
    public String getTotalHoursPropertyValue() { return totalHoursProperty.get(); }
    public String getManagerId() { return managerId.get(); }
}
