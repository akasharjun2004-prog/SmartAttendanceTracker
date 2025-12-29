package smartattendance.service;

import smartattendance.dao.PermissionDAO;
import smartattendance.model.PermissionRequest;
import smartattendance.model.User;

import java.sql.SQLException;
import java.util.List;

public class PermissionService {

    private final PermissionDAO dao = new PermissionDAO();
    private final UserService userService = new UserService(); // To fetch employee names

    // Submit request
    public boolean submitRequest(PermissionRequest p) {
        try {
            dao.insert(p);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all requests (for admin)
    public List<PermissionRequest> getAllRequests() {
        List<PermissionRequest> list;
        try {
            list = dao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Populate employee names
        for (PermissionRequest p : list) {
            try {
                User employee = userService.getUserById(p.getUserId());
                p.setEmployeeName(employee != null ? employee.getFullName() : "Unknown");
            } catch (Exception e) {
                p.setEmployeeName("Unknown");
            }
        }

        return list;
    }

    // Approve request
    public boolean approveRequest(int id, int adminId) {
        try {
            dao.approve(id, adminId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Reject request
    public boolean rejectRequest(int id, int adminId) {
        try {
            dao.reject(id, adminId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<PermissionRequest> getPendingRequests(int userId) {
        return dao.getPendingRequests(userId);
    }
    // Get request by ID
    public PermissionRequest getRequestById(int id) {
        PermissionRequest p;
        try {
            p = dao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (p != null) {
            try {
                User employee = userService.getUserById(p.getUserId());
                p.setEmployeeName(employee != null ? employee.getFullName() : "Unknown");
            } catch (Exception e) {
                p.setEmployeeName("Unknown");
            }
        }

        return p;
    }
}
