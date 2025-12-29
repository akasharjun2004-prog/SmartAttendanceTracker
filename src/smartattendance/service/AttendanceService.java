package smartattendance.service;

import smartattendance.dao.AttendanceDAO;
import smartattendance.model.AttendanceRecord;
import smartattendance.model.User;
import smartattendance.util.SessionUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AttendanceService {

    private final AttendanceDAO dao;
    private final ActivityLogService activityLogService = new ActivityLogService();

    

    public AttendanceService() throws SQLException {
        this.dao = new AttendanceDAO();
    }

   
   
    public AttendanceRecord getTodayAttendance(int userId) throws SQLException {
        return dao.getTodayAttendance(userId);
    }

    
    public void markLogin(int userId) throws SQLException {
        dao.addLoginTime(userId);
    }


   
    public boolean manualLogin() throws SQLException {

        User user = SessionUtil.getLoggedInUser();
        if (user == null) {
            throw new RuntimeException("User session is missing!");
        }

        int userId = user.getUserId();

        
        AttendanceRecord today = dao.getTodayAttendance(userId);

        if (today != null && today.getLoginTime() != null) {
           
            return false;
        }

        
        AttendanceRecord record = new AttendanceRecord();
        record.setUserId(userId);
        record.setLoginDateTime(LocalDateTime.now());
        record.setLoginType("manual");        // IMPORTANT
        record.setAttendanceDate(LocalDate.now());

        dao.insertManualLogin(record.getUserId());
        activityLogService.log(userId, "LOGIN", "Manual login", "LOCAL");


        return true; 
    }
    public int getPresentDaysThisMonth(int userId) {
        try {
            return dao.countPresentDaysThisMonth(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public double getTotalHoursThisMonth(int userId) {
        try {
            return dao.sumTotalHoursThisMonth(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public List<AttendanceRecord> getAttendanceForRole(User user, LocalDate date) {
        if (user == null) {
            throw new RuntimeException("User session is null! Login required.");
        }

        String role = user.getRole().toLowerCase();

        switch (role) {
            case "admin":
            case "hr":
                return dao.getAllAttendanceRecords(date);

            case "manager":
                return dao.getManagerAttendance(user.getUserId(), date);

            case "employee":
                return dao.getEmployeeAttendance(user.getUserId(), date);

            default:
                return List.of();
        }
    }
    
    public boolean logout() {
        try {
            int userId = SessionUtil.getLoggedInUser().getUserId();
            LocalDate today = LocalDate.now();

            boolean updated = dao.setLogoutTime(userId, today);

            if (updated) {
                
                activityLogService.log(userId, "LOGOUT", "Manual logout", "LOCAL");
            }

            return updated;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
