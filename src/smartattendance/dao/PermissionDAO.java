package smartattendance.dao;

import smartattendance.model.PermissionRequest;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // 24-hour format

    /** Insert a new permission request */
    public void insert(PermissionRequest p) throws SQLException {
    	String sql = "INSERT INTO permissions (user_id, date, from_time, to_time, duration, reason, status, approved_by, applied_at, approved_at, permission_type) " +
                "VALUES (?, ?, ?, ?, ?::interval, ?, ?, ?, ?, ?, ?)";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getUserId());
            ps.setDate(2, Date.valueOf(p.getDate()));

            // Convert String to java.sql.Time for PostgreSQL
            ps.setTime(3, Time.valueOf(LocalTime.parse(p.getFromTime(), timeFormatter)));
            ps.setTime(4, Time.valueOf(LocalTime.parse(p.getToTime(), timeFormatter)));

            ps.setString(5, p.getDuration()); // optional, can calculate later
            ps.setString(6, p.getReason());
            ps.setString(7, p.getStatus());
            ps.setObject(8, p.getApprovedBy() != null ? p.getApprovedBy() : null, Types.INTEGER);
            ps.setTimestamp(9, p.getAppliedAt() != null ? Timestamp.valueOf(p.getAppliedAt()) : Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(10, p.getApprovedAt() != null ? Timestamp.valueOf(p.getApprovedAt()) : null);
            ps.setString(11, p.getPermissionType());

            ps.executeUpdate();
        }
    }

    
    
    public List<PermissionRequest> getPendingRequests(int userId) {
        List<PermissionRequest> list = new ArrayList<>();
       // String sql = "SELECT * FROM permissions WHERE user_id = ? AND status = 'Pending' ORDER BY applied_at DESC";
        String sql = """
        	    SELECT p.*, u.full_name AS employee_name
        	    FROM permissions p
        	    JOIN users u ON p.user_id = u.user_id
        	    WHERE p.user_id = ? AND p.status = 'Pending'
        	    ORDER BY p.applied_at DESC
        	""";


        try (Connection conn = DBConnection.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PermissionRequest pr = new PermissionRequest();
               // pr.setRequestId(rs.getInt("request_id"));
                pr.setUserId(rs.getInt("user_id"));
                pr.setEmployeeName(rs.getString("employee_name"));
                pr.setPermissionType(rs.getString("permission_type"));
                pr.setDate(rs.getDate("date").toLocalDate());
                pr.setFromTime(rs.getString("from_time"));
                pr.setToTime(rs.getString("to_time"));
                pr.setDuration(rs.getString("duration"));
                pr.setReason(rs.getString("reason"));
                pr.setStatus(rs.getString("status"));

                list.add(pr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    /** Approve a request by adminId */
    public void approve(int id, int adminId) throws SQLException {
        String sql = "UPDATE permissions SET status='APPROVED', approved_by=?, approved_at=? WHERE permission_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);

            ps.executeUpdate();
        }
    }

    /** Reject a request by adminId */
    public void reject(int id, int adminId) throws SQLException {
        String sql = "UPDATE permissions SET status='REJECTED', approved_by=?, approved_at=? WHERE permission_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, id);

            ps.executeUpdate();
        }
    }

    /** Get all requests */
    public List<PermissionRequest> getAll() throws SQLException {
        List<PermissionRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM permissions ORDER BY applied_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                PermissionRequest p = new PermissionRequest();
                p.setId(rs.getInt("permission_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setDate(rs.getDate("date").toLocalDate());
                p.setFromTime(rs.getTime("from_time").toLocalTime().format(timeFormatter));
                p.setToTime(rs.getTime("to_time").toLocalTime().format(timeFormatter));
                p.setDuration(rs.getString("duration"));
                p.setReason(rs.getString("reason"));
                p.setStatus(rs.getString("status"));
                p.setApprovedBy(rs.getString("approved_by"));

                Timestamp applied = rs.getTimestamp("applied_at");
                if (applied != null) p.setAppliedAt(applied.toLocalDateTime());

                Timestamp approved = rs.getTimestamp("approved_at");
                if (approved != null) p.setApprovedAt(approved.toLocalDateTime());

                p.setPermissionType(rs.getString("permission_type"));

                list.add(p);
            }
        }

        return list;
    }

    /** Get single request by ID */
    public PermissionRequest getById(int id) throws SQLException {
        PermissionRequest p = null;
        String sql = "SELECT * FROM permissions WHERE permission_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new PermissionRequest();
                p.setId(rs.getInt("permission_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setDate(rs.getDate("date").toLocalDate());
                p.setFromTime(rs.getTime("from_time").toLocalTime().format(timeFormatter));
                p.setToTime(rs.getTime("to_time").toLocalTime().format(timeFormatter));
                p.setDuration(rs.getString("duration"));
                p.setReason(rs.getString("reason"));
                p.setStatus(rs.getString("status"));
                p.setApprovedBy(rs.getString("approved_by"));

                Timestamp applied = rs.getTimestamp("applied_at");
                if (applied != null) p.setAppliedAt(applied.toLocalDateTime());

                Timestamp approved = rs.getTimestamp("approved_at");
                if (approved != null) p.setApprovedAt(approved.toLocalDateTime());

                p.setPermissionType(rs.getString("permission_type"));
            }
        }

        return p;
    }
}
