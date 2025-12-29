package smartattendance.service;

import smartattendance.dao.BreakDAO;

import smartattendance.util.SessionUtil;

import java.sql.SQLException;

public class BreakService {
	private final ActivityLogService activityLogService = new ActivityLogService();


    private final BreakDAO dao;

    public BreakService() throws SQLException {
        this.dao = new BreakDAO();
    }

   
    public static class ToggleResult {
        public final boolean success;
        public final String message;
        public final boolean onBreak; 
        public ToggleResult(boolean success, String message, boolean onBreak) {
            this.success = success;
            this.message = message;
            this.onBreak = onBreak;
        }
    }

  
    public ToggleResult toggleBreak() {
        int userId = SessionUtil.getLoggedUserId();
        if (userId <= 0) {
            return new ToggleResult(false, "No logged-in user found. Please login first.", false);
        }

        try {
           
            int attendanceId = dao.findAttendanceIdForToday(userId);
            if (attendanceId == -1) {
               
                return new ToggleResult(false, "No attendance record found for today. Please scan/login first.", false);
            }

            
            int openBreakId = dao.findOpenBreak(userId, attendanceId);
            if (openBreakId != -1) {
                
            	boolean ended = dao.endBreak(openBreakId);
                if (ended) {
                	activityLogService.log(userId, "BREAK_END", "Break ended", "LOCAL");

                    return new ToggleResult(true, "Break ended successfully.", false);
                } else {
                    return new ToggleResult(false, "Failed to end break. Try again.", true);
                }
            } else {
               
                int newBreakId = dao.startBreak(userId, attendanceId);
                if (newBreakId != -1) {
                	activityLogService.log(userId, "BREAK_START", "Break started", "LOCAL");

                    return new ToggleResult(true, "Break started. Click again to end break.", true);
                } else {
                    return new ToggleResult(false, "Failed to start break. Try again.", false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ToggleResult(false, "Unexpected error while toggling break. See logs.", false);
        }
    }
}
