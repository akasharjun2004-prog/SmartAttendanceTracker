package smartattendance.dao;

import smartattendance.model.LeaveRequest;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestDAO {

    // Get all leave requests
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT leave_id, user_id, employee_name, start_date, end_date, reason, status " +
                     "FROM vw_leave_requests ORDER BY applied_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LeaveRequest lr = new LeaveRequest(
                        rs.getInt("leave_id"),
                        rs.getInt("user_id"),
                        rs.getString("employee_name"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("end_date").toLocalDate(),
                        rs.getString("reason"),
                        rs.getString("status")
                );
                list.add(lr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update leave status
    public boolean updateStatus(int leaveId, String status) {
        String sql = "UPDATE leave_requests SET status = ? WHERE leave_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, leaveId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ CHECK LEAVE OVERLAP USING USER ID
    public boolean leaveExists(int userId, LocalDate fromDate, LocalDate toDate) {
        String sql = "SELECT COUNT(*) FROM leave_requests " +
                     "WHERE user_id = ? " +
                     "AND start_date <= ? " +
                     "AND end_date >= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(toDate));
            ps.setDate(3, Date.valueOf(fromDate));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Add leave request
    public boolean addLeaveRequest(LeaveRequest leave) {
        String sql = "INSERT INTO leave_requests (user_id, employee_name, start_date, end_date, reason, status, leave_type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, leave.getUserId());
            ps.setString(2, leave.getEmployeeName());
            ps.setDate(3, Date.valueOf(leave.getStartDate()));
            ps.setDate(4, Date.valueOf(leave.getEndDate()));
            ps.setString(5, leave.getReason());
            ps.setString(6, leave.getStatus());
            ps.setString(7, leave.getType());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
