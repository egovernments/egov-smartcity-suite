package org.egov.pgr.web.controller.complaint;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infra.admin.master.entity.Department;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.ComplaintService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewComplaintController {

    private ComplaintService complaintService;
    @Autowired
    private EisCommonService eisCommonService;

    @Autowired
    public ViewComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @RequestMapping(value = "/view-complaint", method = RequestMethod.GET)
    public String viewComplaints(@RequestParam Long complaintId, Model model) {
        Complaint complaint = complaintService.getComplaintById(complaintId);
        List<Hashtable<String, Object>> historyTable = new ArrayList<Hashtable<String, Object>>();
        Department department = null;
        User user = null;
        if (!complaint.getStateHistory().isEmpty() && complaint.getStateHistory() != null) {
            for (StateHistory stateHistory : complaint.getStateHistory()) {
                Hashtable<String, Object> map = new Hashtable<String, Object>(0);
                map.put("date", stateHistory.getCreatedDate());
                map.put("comments", stateHistory.getComments());
                Position ownerPosition = stateHistory.getOwnerPosition();
                user = stateHistory.getOwnerUser();
                if (null != user) {
                    map.put("user", user.getUsername());
                    map.put("department",
                            null != eisCommonService.getDepartmentForUser(user.getId()) ? eisCommonService
                                    .getDepartmentForUser(user.getId()).getName() : "");
                } else {
                    if (null != ownerPosition && null != ownerPosition.getDeptDesigId()) {
                        user = eisCommonService.getUserForPosition(ownerPosition.getId(), new Date());
                        map.put("user", null != user.getUsername() ? user.getUsername() : "");
                        map.put("department", null != ownerPosition.getDeptDesigId().getDeptId() ? ownerPosition
                                .getDeptDesigId().getDeptId().getName() : "");
                    }
                }

                historyTable.add(map);
            }
        }
        model.addAttribute("complaintHistory", historyTable);
        model.addAttribute("complaint", complaint);
        return "view-complaint";
    }

}
