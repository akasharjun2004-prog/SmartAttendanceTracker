package smartattendance.dao;

import smartattendance.model.User;
import smartattendance.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // ---------------------------------------------------
    // GET USER BY QR CODE
    // ---------------------------------------------------
    public User getUserByQRCode(String qrCode) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, qrCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapToUser(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching user by QR code: " + e.getMessage());
        }
        return null;
    }

    // ---------------------------------------------------
    // GET USER BY ID
    // ---------------------------------------------------
    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapToUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------------------------------------
    // INSERT USER
    // ---------------------------------------------------
    public boolean insertUser(User user) {
        String sql = """
            INSERT INTO users (
                full_name, username, email, phone, role,
                manager_id, created_by, status, address,
                qr_code, qr_image_path, password, created_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP(0))
            RETURNING user_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getRole());
            ps.setObject(6, user.getManagerId());
            ps.setObject(7, user.getCreatedBy());
            ps.setString(8, user.getStatus() != null ? user.getStatus() : "Active");
            ps.setString(9, user.getAddress());
            ps.setString(10, user.getQrCode());
            ps.setString(11, user.getQrImagePath());
            ps.setString(12, user.getPassword());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int generatedId = rs.getInt("user_id");
                    user.setUserId(generatedId);  // important!
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    // ---------------------------------------------------
    // GET ALL USERS
    // ---------------------------------------------------
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) users.add(mapToUser(rs));

        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }

        return users;
    }

    // ---------------------------------------------------
    // GET USER BY USERNAME
    // ---------------------------------------------------
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapToUser(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching user by username: " + e.getMessage());
        }
        return null;
    }

    // ---------------------------------------------------
    // UPDATE USER
    // ---------------------------------------------------
    public boolean updateUser(User user) {
        String sql = """
            UPDATE users SET
                full_name = ?, email = ?, phone = ?, role = ?, status = ?, address = ?,
                updated_at = CURRENT_TIMESTAMP(0)
            WHERE user_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getRole().trim().toLowerCase());
            ps.setString(5, user.getStatus());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    
    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, hashedPassword);
            pst.setInt(2, userId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------------------------------------------
    // DELETE USER
    // ---------------------------------------------------
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------
    // GET ACTIVE MANAGERS
    // ---------------------------------------------------
    public List<User> getAllManagers() {
        List<User> managers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='manager' AND status='active' ORDER BY full_name ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) managers.add(mapToUser(rs));

        } catch (SQLException e) {
            System.out.println("Error fetching managers: " + e.getMessage());
        }

        return managers;
    }

    // ---------------------------------------------------
    // MAPPER
    // ---------------------------------------------------
    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("role"),
                rs.getString("status"),
                rs.getString("address"),
                (Integer) rs.getObject("manager_id"),
                (Integer) rs.getObject("created_by"),
                rs.getString("qr_code"),
                rs.getString("qr_image_path"),
                rs.getString("password"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}
