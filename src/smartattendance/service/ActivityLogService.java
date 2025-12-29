package smartattendance.service;

import smartattendance.dao.ActivityLogDAO;
import smartattendance.model.ActivityLog;

import java.util.List;

public class ActivityLogService {

    private final ActivityLogDAO dao;

    public ActivityLogService() {
        // DAO is already exception-safe
        this.dao = new ActivityLogDAO();
    }

    public List<ActivityLog> getAllLogs() {
        try {
            return dao.getAllLogs();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<ActivityLog> searchLogs(String keyword) {
        try {
            return dao.searchLogs(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean clearAllLogs() {
        try {
            return dao.deleteAllLogs();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void log(int userId, String action, String description, String ipAddress) {
        dao.insertLog(userId, action, description, ipAddress);
    }
}
