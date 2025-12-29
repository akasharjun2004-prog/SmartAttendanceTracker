package smartattendance.service;

import smartattendance.dao.LeaveRequestDAO;
import smartattendance.model.LeaveRequest;

import java.time.LocalDate;
import java.util.List;

public class LeaveRequestService {

    private final LeaveRequestDAO dao = new LeaveRequestDAO();

    
    public List<LeaveRequest> getAllLeaveRequests() {
        return dao.getAllLeaveRequests();
    }

    public boolean approveLeave(int leaveId) {
        return dao.updateStatus(leaveId, "Approved");
    }

    public boolean rejectLeave(int leaveId) {
        return dao.updateStatus(leaveId, "Rejected");
    }

   
    public boolean submitLeaveRequest(String type,
                                      LocalDate start,
                                      LocalDate end,
                                      String reason,
                                      int userId) {

        // Date validation
        if (start.isAfter(end)) {
            return false;
        }

        // Check if a leave already exists for same user and overlapping dates
        boolean exists = dao.leaveExists(userId, start, end);
        if (exists) {
            return false;
        }

        LeaveRequest leave = new LeaveRequest();
        leave.setUserId(userId);
        leave.setType(type);
        leave.setStartDate(start);
        leave.setEndDate(end);
        leave.setReason(reason);
        leave.setStatus("PENDING");
        leave.setCreatedDate(LocalDate.now());

        return dao.addLeaveRequest(leave);
    }
}
