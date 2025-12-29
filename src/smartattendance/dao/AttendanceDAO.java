package smartattendance.dao;

import smartattendance.model.AttendanceRecord;
import org.postgresql.util.PGInterval;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDAO {

    private final Connection conn;

    public AttendanceDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    // Map for dashboard / display (includes full_name, manager_id)
    private AttendanceRecord mapWithUser(ResultSet rs) throws SQLException {
        return new AttendanceRecord(
                String.valueOf(rs.getInt("user_id")),
                rs.getString("full_name"),
                rs.getDate("login_time") != null ? rs.getDate("login_time").toString() : "",
                rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : "",
                rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : "",
                rs.getString("total_hours") != null ? rs.getString("total_hours") : "",
                rs.getString("manager_id") != null ? rs.getString("manager_id") : ""
        );
    }

    // Map for QR login check (only attendance table columns)
    private AttendanceRecord mapForToday(ResultSet rs) throws SQLException {
        return new AttendanceRecord(
                String.valueOf(rs.getInt("user_id")),
                "", // full_name not needed
                rs.getDate("login_time") != null ? rs.getDate("login_time").toString() : "",
                rs.getTime("login_time") != null ? rs.getTime("login_time").toString() : "",
                rs.getTime("logout_time") != null ? rs.getTime("logout_time").toString() : "",
                rs.getString("total_hours") != null ? rs.getString("total_hours") : "",
                "" // manager_id not needed
        );
    }

    /** ADMIN + HR - display all records */
    public List<AttendanceRecord> getAllAttendanceRecords(LocalDate date) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.full_name, u.manager_id
            FROM attendance a
            JOIN users u ON a.user_id = u.user_id
        """;

        if (date != null) sql += " WHERE DATE(a.login_time) = ?";
        sql += " ORDER BY a.login_time DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (date != null) ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapWithUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Check if user already has login today (for QR login) */
    public AttendanceRecord getTodayAttendance(int userId) {
        String sql = "SELECT * FROM attendance WHERE user_id = ? AND DATE(login_time) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapForToday(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // no login today
    }
    
    
    public void insertManualLogin(int userId) {
    	

    	String checkSql = "SELECT * FROM attendance WHERE user_id = ? AND DATE(login_time) = ?";
        String insertSql = "INSERT INTO attendance (user_id, login_time) VALUES (?, ?)";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            checkPs.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) { // insert only if no record today
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, userId);
                    insertPs.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	}

    



    /** Mark login for first QR scan (ensures only one row per day) */
    public void addLoginTime(int userId) {
        String checkSql = "SELECT * FROM attendance WHERE user_id = ? AND DATE(login_time) = ?";
        String insertSql = "INSERT INTO attendance (user_id, login_time) VALUES (?, ?)";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            checkPs.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) { // insert only if no record today
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, userId);
                    insertPs.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** MANAGER - see team records */
    public List<AttendanceRecord> getManagerAttendance(int managerId, LocalDate date) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.full_name, u.manager_id
            FROM attendance a
            JOIN users u ON a.user_id = u.user_id
            WHERE u.manager_id = ?
        """;
        if (date != null) sql += " AND DATE(a.login_time) = ?";
        sql += " ORDER BY a.login_time DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            if (date != null) ps.setDate(2, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapWithUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** EMPLOYEE - see own records */
    public List<AttendanceRecord> getEmployeeAttendance(int userId, LocalDate date) {
        List<AttendanceRecord> list = new ArrayList<>();
        String sql = """
            SELECT a.*, u.full_name, u.manager_id
            FROM attendance a
            JOIN users u ON a.user_id = u.user_id
            WHERE a.user_id = ?
        """;
        if (date != null) sql += " AND DATE(a.login_time) = ?";
        sql += " ORDER BY a.login_time DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            if (date != null) ps.setDate(2, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapWithUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
   // import org.postgresql.util.PGInterval;

    public boolean setLogoutTime(int userId, LocalDate date) {
        String sql = """
            UPDATE attendance
            SET logout_time = ?,
                total_breaks = ?,
                total_hours = ?,
                actual_work_time = ?,
                work_status = ?
            WHERE user_id = ? AND attendance_date = ? AND logout_time IS NULL
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();

            // 1️⃣ Calculate total break minutes
            String breakSql = """
                SELECT COALESCE(SUM(duration_minutes), 0) AS total_minutes
                FROM breaks
                WHERE user_id = ? AND attendance_id = (
                    SELECT attendance_id FROM attendance 
                    WHERE user_id = ? AND attendance_date = ?
                )
            """;
            long totalBreakMinutes = 0;
            try (PreparedStatement breakStmt = conn.prepareStatement(breakSql)) {
                breakStmt.setInt(1, userId);
                breakStmt.setInt(2, userId);
                breakStmt.setDate(3, Date.valueOf(date));
                ResultSet rs = breakStmt.executeQuery();
                if (rs.next()) totalBreakMinutes = rs.getLong("total_minutes");
            }

            // 2️⃣ Fetch login_time
            LocalDateTime loginTime = null;
            String loginSql = "SELECT login_time FROM attendance WHERE user_id = ? AND attendance_date = ?";
            try (PreparedStatement loginStmt = conn.prepareStatement(loginSql)) {
                loginStmt.setInt(1, userId);
                loginStmt.setDate(2, Date.valueOf(date));
                ResultSet rs = loginStmt.executeQuery();
                if (rs.next()) loginTime = rs.getTimestamp("login_time").toLocalDateTime();
            }
            if (loginTime == null) return false;

            // 3️⃣ Calculate total and actual work time in minutes
            long totalMinutes = java.time.Duration.between(loginTime, now).toMinutes();
            long actualWorkMinutes = totalMinutes - totalBreakMinutes;

            // 4️⃣ Prepare PostgreSQL INTERVALs
            PGInterval totalBreakInterval = new PGInterval(0, 0, 0, (int) totalBreakMinutes, 0, 0);
            PGInterval actualWorkInterval = new PGInterval(0, 0, 0, (int) actualWorkMinutes, 0, 0);

            // 5️⃣ total_hours as decimal (NUMERIC)
            double totalHours = totalMinutes / 60.0;

            // 6️⃣ Work status
            String workStatus = actualWorkMinutes >= 480 ? "Full Day" : "Half Day";

            // 7️⃣ Set values in PreparedStatement
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setObject(2, totalBreakInterval);  // INTERVAL
            stmt.setDouble(3, totalHours);          // NUMERIC
            stmt.setObject(4, actualWorkInterval);  // INTERVAL
            stmt.setString(5, workStatus);
            stmt.setInt(6, userId);
            stmt.setDate(7, Date.valueOf(date));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 // Count present days in current month
    public int countPresentDaysThisMonth(int userId) throws SQLException {
        String sql = """
            SELECT COUNT(*) AS days
            FROM attendance
            WHERE user_id = ? 
              AND EXTRACT(MONTH FROM attendance_date) = ? 
              AND EXTRACT(YEAR FROM attendance_date) = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            LocalDate now = LocalDate.now();
            ps.setInt(2, now.getMonthValue());
            ps.setInt(3, now.getYear());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("days");
        }
        return 0;
    }

    // Sum total hours in current month
    public double sumTotalHoursThisMonth(int userId) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(total_hours), 0) AS total
            FROM attendance
            WHERE user_id = ? 
              AND EXTRACT(MONTH FROM attendance_date) = ? 
              AND EXTRACT(YEAR FROM attendance_date) = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            LocalDate now = LocalDate.now();
            ps.setInt(2, now.getMonthValue());
            ps.setInt(3, now.getYear());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        }
        return 0;
    }




    }





