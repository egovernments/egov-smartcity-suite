/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.dashboard.es;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeCategory;
import org.egov.pgr.entity.es.ComplaintDashBoardRequest;
import org.egov.pgr.entity.es.ComplaintIndex;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(value = "/complaint/aggregate")
public class ComplaintIndexController {
    @Autowired
    private ComplaintIndexService complaintIndexService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    @Autowired
    private BoundaryService boundaryService;

    @RequestMapping(value = "/grievance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getDashBoardResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getGrievanceReport(complaintRequest);
    }

    @RequestMapping(value = "/grievance/complainttype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getGrievanceComplaintTypeResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getComplaintTypeReport(complaintRequest);
    }

    @RequestMapping(value = "/sourcewisegrievance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getSourceWiseGrievanceResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getSourceWiseResponse(complaintRequest);
    }

    @RequestMapping(value = "/citizenrating", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getCitizenRatingResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.findByAllCitizenRating(complaintRequest);
    }

    @RequestMapping(value = "/departments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getDepartments() {
        final List<Department> departments = departmentService.getAllDepartments();
        final List<JSONObject> jsonList = new ArrayList<>();
        for (final Department department : departments) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", department.getName());
            jsonObject.put("code", department.getCode());
            jsonList.add(jsonObject);
        }
        return jsonList;
    }

    @RequestMapping(value = "/complainttypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getComplaintTypes(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        List<ComplaintType> complaintTypeList;
        if (isNotBlank(complaintRequest.getCategoryId()))
            complaintTypeList = complaintTypeService
                    .findActiveComplaintTypesByCategory(Long.valueOf(complaintRequest.getCategoryId()));
        else
            complaintTypeList = complaintTypeService.findAll();
        final List<JSONObject> jsonList = new ArrayList<>();
        for (final ComplaintType complaintType : complaintTypeList) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", complaintType.getName());
            jsonObject.put("code", complaintType.getCode());
            jsonList.add(jsonObject);
        }
        return jsonList;
    }

    @RequestMapping(value = "/complainttypecategories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getComplaintTypeCategories() {
        final List<ComplaintTypeCategory> complaintTypeCategoryList = complaintTypeCategoryService.findAll();
        final List<JSONObject> jsonList = new ArrayList<>();
        for (final ComplaintTypeCategory complaintTypeCategory : complaintTypeCategoryList) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", complaintTypeCategory.getId());
            jsonObject.put("name", complaintTypeCategory.getName());
            jsonList.add(jsonObject);
        }
        return jsonList;
    }

    @RequestMapping(value = "/wards", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getWards() {
        final List<Boundary> boundaryList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
                "ADMINISTRATION");
        final List<JSONObject> jsonList = new ArrayList<>();

        for (final Boundary boundary : boundaryList) {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", boundary.getName());
            jsonObject.put("boundaryNumber", boundary.getBoundaryNum());
            jsonObject.put("lat", boundary.getLatitude());
            jsonObject.put("lon", boundary.getLongitude());
            jsonList.add(jsonObject);
        }
        return jsonList;
    }

    @RequestMapping(value = "/sourcenames", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSourceNameList() throws JsonProcessingException {
        return complaintIndexService.getSourceNameList();
    }

    @RequestMapping(value = "/grievance/allfunctionaries", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAllFunctionaryResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getAllFunctionaryResponse(complaintRequest);
    }

    @RequestMapping(value = "/grievance/allulb", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAllUlbResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getAllUlbResponse(complaintRequest);
    }

    @RequestMapping(value = "/grievance/allwards", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAllWardsResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getAllWardResponse(complaintRequest);
    }

    @RequestMapping(value = "/grievance/alllocalities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getAllLocalitiesResponse(@RequestBody final ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getAllLocalityResponse(complaintRequest);
    }

    @RequestMapping(value = "/functionaryWiseComplaints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComplaintIndex> getFunctionaryWiseComplaints(@RequestParam final String functionaryName) {
        return complaintIndexService.getFunctionaryWiseComplaints(functionaryName);
    }

    @RequestMapping(value = "/localityWiseComplaints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComplaintIndex> getLocalityWiseComplaints(@RequestParam final String localityName) {
        return complaintIndexService.getLocalityWiseComplaints(localityName);
    }

    // This is a common api where a fieldName and value will be accepted additionally to return matching complaints
    // with existing filters
    @RequestMapping(value = "/complaints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComplaintIndex> getFilteredComplaints(@RequestBody final ComplaintDashBoardRequest complaintRequest,
                                                      @RequestParam final String fieldName, @RequestParam final String fieldValue) {
        if (StringUtils.isEmpty(complaintRequest.getSortField()))
            complaintRequest.setSortField("createdDate");
        if (StringUtils.isEmpty(complaintRequest.getSortDirection()))
            complaintRequest.setSortDirection("DESC");
        if (complaintRequest.getSize() == 0)
            complaintRequest.setSize(10000);
        return complaintIndexService.getFilteredComplaints(complaintRequest, fieldName, fieldValue, null, null);
    }

    /**
     * This is a common api where a fieldName, lowerLimit and upperLimit will be accepted additionally to return matching
     * complaints with existing filters for the SLA dashboard
     *
     * @param complaintRequest
     * @param fieldName
     * @param lowerLimit
     * @param upperLimit
     * @return list
     */
    @RequestMapping(value = "/slaComplaints", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComplaintIndex> getFilteredComplaintsForSLASlabs(@RequestBody final ComplaintDashBoardRequest complaintRequest,
                                                                 @RequestParam final String fieldName, @RequestParam final Integer lowerLimit,
                                                                 @RequestParam final Integer upperLimit) {
        if (StringUtils.isEmpty(complaintRequest.getSortField()))
            complaintRequest.setSortField("createdDate");
        if (StringUtils.isEmpty(complaintRequest.getSortDirection()))
            complaintRequest.setSortDirection("DESC");
        if (complaintRequest.getSize() == 0)
            complaintRequest.setSize(10000);
        return complaintIndexService.getFilteredComplaints(complaintRequest, fieldName, StringUtils.EMPTY, lowerLimit,
                upperLimit);
    }
}
