/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter;
import org.egov.pgr.entity.Complainant;
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
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@org.springframework.web.bind.annotation.RestController
public class RestComplaintController extends GenericComplaintController {
    private static final Logger LOG = LoggerFactory.getLogger(RestComplaintController.class);

    @Autowired
    protected ComplaintStatusService complaintStatusService;

    @RequestMapping(value = { "rest/complaint/showcomplaint" }, method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String testInstances() {
        final RestComplaint restcomplaint = new RestComplaint();
        restcomplaint.setName("Satyam K Ashish");
        restcomplaint.setEmail("satyamashish@gmail.com");
        restcomplaint.setMobile("9741129330");
        restcomplaint.setLat(12.9797732);
        restcomplaint.setLng(77.6402478);
        restcomplaint.setDescription("Testing");
        final Complainant complainant = new Complainant();
        complainant.setName("Satyam K Ashish");
        complainant.setEmail("satyamashish@gmail.com");
        complainant.setMobile("9741129330");
        restcomplaint.setComplainant(complainant);
        final ComplaintType complaintType = complaintTypeService.getListOfComplaintTypes(1, 5).getContent().get(0);
        restcomplaint.setComplaintType(complaintType);
        final Gson jsonCreator = new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .disableHtmlEscaping().create();
        // Gson jsonCreator = new
        // GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
        // .disableHtmlEscaping().registerTypeAdapter(RestComplaint.class, new
        // RestComplaintAdaptor()).create();
        final String json = jsonCreator.toJson(restcomplaint);// jsonCreator.toJson(restcomplaint,
        // new
        // TypeToken<RestComplaint>().getType());
        return json;
    }

    @RequestMapping(value = { "rest/complaint/complaintTypes" }, method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String getAllComplaintTypes() {
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
    public String createComplaints(@RequestBody Complaint complaint) {

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
            Log.info("Error deserializing note " + complaint, e);
            e.printStackTrace();
        }

        return json;
    }

    @RequestMapping(value = "rest/complaint/{complaintno}", method = RequestMethod.PUT)
    @ResponseBody
    public void putComputer(@PathVariable final String complaintno, @RequestBody final Model model,
            final HttpServletRequest request) {

        Complaint complaint = complaintService.getComplaintByCrnNo(complaintno);
        Long approvalPosition = 0l;
        if (null != request.getParameter("approvalPosition") && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        String approvalComent = "";
        if (null != request.getParameter("approvalComent"))
            approvalComent = request.getParameter("approvalComent");
        String status = "";
        if (null != request.getParameter("status")) {
            status = request.getParameter("status");
            complaint.setStatus(complaintStatusService.getByName(status));
        }

        complaint = complaintService.update(complaint, approvalPosition, approvalComent);

    }

}
