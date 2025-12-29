package smartattendance.service;

import java.io.File;
import java.util.List;
import smartattendance.dao.UserDAO;
import smartattendance.model.User;
import smartattendance.util.PasswordUtil;
import smartattendance.util.QRUtil;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

  
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    
    public List<User> getAllManagers() {
        return userDAO.getAllManagers();
    }

   
   public  static String plainPassword;
    public boolean createUser(User user, Integer createdBy) {
        if (user.getFullName() == null || user.getFullName().isEmpty()) return false;
        if (user.getEmail() == null || user.getEmail().isEmpty()) return false;

        
        String username = generateUsername(user.getFullName());
        user.setUsername(username);

       
         plainPassword = (user.getPhone() != null && user.getPhone().length() >= 8)
                ? user.getPhone().substring(0, 8)
                : "12345678";
        user.setPassword(PasswordUtil.hashPassword(plainPassword));
       

        
        user.setStatus("active");

        
        if (user.getRole() == null) {
            user.setRole("employee");
        } else {
            user.setRole(user.getRole().toLowerCase());
        }

        user.setCreatedBy(createdBy);

       
        String qrFolder = "E:/SmartAttendance/qr_codes/";
        File folder = new File(qrFolder);
        if (!folder.exists()) folder.mkdirs();

       
        String safeName = user.getFullName().replaceAll("[^a-zA-Z0-9]", "_"); 
        String qrFilePath = qrFolder + safeName + "_" + username + "_qr.png";

        try {
            QRUtil.generateQRCodeImage(username, 200, 200, qrFilePath); // QR content = username
            user.setQrImagePath(qrFilePath);
          
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        
        boolean inserted = userDAO.insertUser(user);

        
        if (inserted) {
            MailService.sendWelcomeEmail(user);
        }

        return inserted;
    }

    				public User getUserByQRCode(String qrCode) {
    						return userDAO.getUserByQRCode(qrCode);
    					}
    				 public User getUserById(int userId) {
    				        return userDAO.getUserById(userId);
    				    }

    public String updateUser(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty())
            return "Full name cannot be empty!";
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return "Email cannot be empty!";

        boolean ok = userDAO.updateUser(user);
        return ok ? "Updated successfully!" : "Update failed!";
    }

    public String deleteUser(int userId) {
        boolean ok = userDAO.deleteUser(userId);
        return ok ? "User deleted!" : "Delete failed!";
    }

   
    private String generateUsername(String fullName) {
        String base = fullName.toLowerCase().replaceAll("\\s+", "");
        String username;
        int attempts = 0;

        do {
            int randomNum = (int)(Math.random() * 9000) + 1000; // 1000-9999
            username = base + randomNum;
            attempts++;
        } while (userDAO.getUserByUsername(username) != null && attempts < 10);

        return username;
    }
    
    public String changePassword(User user, String oldPassword, String newPassword, String confirmPassword) {

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return "All fields are required.";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "New password and confirm password do not match.";
        }

        if (newPassword.length() < 6) {
            return "Password must be at least 6 characters.";
        }

        // Verify old password
        if (!PasswordUtil.verifyPassword(oldPassword, user.getPassword())) {
            return "Old password is incorrect.";
        }

        // Hash new password
        String hashedPassword = PasswordUtil.hashPassword(newPassword);

        // Update DB (replace with actual DB update logic)
        boolean updated = updatePasswordInDB(user.getUserId(), hashedPassword);

        if (updated) {
            // Update session
            user.setPassword(hashedPassword);

            // Log activity
            logActivity(user.getUserId(), "USER_CHANGED_PASSWORD");

            return "SUCCESS";
        } else {
            return "Failed to update password. Try again.";
        }
    }

    private boolean updatePasswordInDB(int userId, String hashedPassword) {
        return userDAO.updatePassword(userId, hashedPassword);
    }


    private void logActivity(int userId, String activity) {
        // TODO: Insert record into activity_log table
    }
}
