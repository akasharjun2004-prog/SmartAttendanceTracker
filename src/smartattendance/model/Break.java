package smartattendance.model;

import java.time.LocalDateTime;

public class Break {

    private int breakId;
    private int userId;
    private int attendanceId; // link to daily attendance
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long durationMinutes; // calculated duration in minutes

    public Break() {}

    public Break(int breakId, int userId, int attendanceId, LocalDateTime startTime, LocalDateTime endTime, long durationMinutes) {
        this.breakId = breakId;
        this.userId = userId;
        this.attendanceId = attendanceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public int getBreakId() { return breakId; }
    public void setBreakId(int breakId) { this.breakId = breakId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(long durationMinutes) { this.durationMinutes = durationMinutes; }
}
