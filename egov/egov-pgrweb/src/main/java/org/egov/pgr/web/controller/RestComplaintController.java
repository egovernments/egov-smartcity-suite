/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter;
import org.egov.infstr.services.EISServeable;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintRestAdaptor;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeRestAdaptor;
import org.egov.pgr.entity.RestComplaint;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.web.controller.complaint.GenericComplaintController;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class RestComplaintController extends GenericComplaintController {

    @Autowired
    protected ComplaintStatusService complaintStatusService;
    @Autowired
    private EISServeable eisService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = {"rest/complaintTypes"}, method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public
    @ResponseBody
    String getAllComplaintTypes() {
        final List<ComplaintType> complaintTypes = complaintTypeService.findAll();
        final Gson jsonCreator = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .disableHtmlEscaping().registerTypeAdapter(ComplaintType.class, new ComplaintTypeRestAdaptor())
                .create();
        final String json = jsonCreator.toJson(complaintTypes, new TypeToken<Collection<ComplaintType>>() {
        }.getType());
        return json;
    }

    @RequestMapping(value = "rest/complaint", method = RequestMethod.POST)
    @ResponseBody
    public String createComplaints(@RequestBody final Complaint complaint) {

        String json = "";
        try {
            if (complaint != null) {
                if (complaint.getComplaintType() != null && complaint.getComplaintType().getCode() != null)
                    complaint.setComplaintType(complaintTypeService.findByCode(complaint.getComplaintType().getCode()));
                if (complaint.getReceivingCenter() != null && complaint.getReceivingCenter().getName() != null)
                    complaint.setReceivingCenter(receivingCenterService.findByName(complaint.getReceivingCenter()
                            .getName()));
            }
            complaintService.createComplaint(complaint);
            // String name = complaint.getDetails();
            final Gson jsonCreator = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                    .disableHtmlEscaping().registerTypeAdapter(Complaint.class, new ComplaintRestAdaptor()).create();
            json = jsonCreator.toJson(complaint, new TypeToken<Complaint>() {
            }.getType());

        } catch (final Exception e) {
            Log.error("Error deserializing note " + complaint, e);
        }

        return json;
    }

    @RequestMapping(value = "rest/complaint/{complaintno}", method = RequestMethod.PUT)
    @ResponseBody
    public String putComputer(@PathVariable final String complaintno, @RequestBody final RestComplaint restComplaint,
                              final HttpServletRequest request) {

        Complaint complaint = complaintService.getComplaintByCRN(complaintno);
        Long approvalPosition = 0l;
        Long userId = 0L;
        if (restComplaint.getApprovalUserName() != null) {
            userId = userService.getUserByUsername(restComplaint.getApprovalUserName()).getId();
            if (userId != 0L)
                approvalPosition = eisService.getPrimaryPositionForUser(userId, new Date()).getId();
            // eisService.getEmployeeInfoList("","");

        }
        String approvalComent = "";
        if (null != restComplaint.getApprovalComment())
            approvalComent = restComplaint.getApprovalComment();
        String status = "";
        if (null != restComplaint.getStatus()) {
            status = restComplaint.getStatus();
            complaint.setStatus(complaintStatusService.getByName(status));
        }

        complaint = complaintService.update(complaint, approvalPosition, approvalComent);
        final String fwdmsg = messageSource.getMessage("msg.comp.fwd.usr", new String[]{complaintno, restComplaint.getStatus(),
                restComplaint.getApprovalUserName()}, null);
        return fwdmsg;

    }

}