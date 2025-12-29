package smartattendance.model;

import java.sql.Timestamp;

public class User {

    private int userId;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String address;
    private Integer managerId;
    private Integer createdBy;
    private String qrCode;
    private String qrImagePath;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    private String name;

    // Constructor
    public User(int id, String username, String role, String name) {
        this.userId = id;
        this.username = username;
        this.role = role;
        this.name = name;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {}

    public User(int userId, String fullName, String username, String email, String phone,
                String role, String status, String address,
                Integer managerId, Integer createdBy,
                String qrCode, String qrImagePath, String password,
                Timestamp createdAt, Timestamp updatedAt) {

        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
        this.address = address;
        this.managerId = managerId;
        this.createdBy = createdBy;
        this.qrCode = qrCode;
        this.qrImagePath = qrImagePath;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 🔹 Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public String getQrImagePath() { return qrImagePath; }
    public void setQrImagePath(String qrImagePath) { this.qrImagePath = qrImagePath; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
