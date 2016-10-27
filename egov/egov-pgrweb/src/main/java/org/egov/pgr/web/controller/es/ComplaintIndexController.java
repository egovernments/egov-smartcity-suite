/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.web.controller.es;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.es.ComplaintDashBoardRequest;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.es.ComplaintIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private BoundaryService boundaryService;

    @RequestMapping(value = "/grievance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getDashBoardResponse(@RequestBody ComplaintDashBoardRequest complaintRequest) {
        return complaintIndexService.getGrievanceReport(complaintRequest);
    }
    
    @RequestMapping(value = "/grievance/complainttype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String,Object> getGrievanceComplaintTypeResponse(@RequestBody ComplaintDashBoardRequest complaintRequest){
    	return complaintIndexService.getComplaintTypeReport(complaintRequest);
    }
    
    @RequestMapping(value = "/sourcewisegrievance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getSourceWiseGrievanceResponse(@RequestBody ComplaintDashBoardRequest complaintRequest){
    	return complaintIndexService.getSourceWiseResponse(complaintRequest);
    }
    
    @RequestMapping(value = "/departments" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject>  getDepartments(){
    	List<Department> departments = departmentService.getAllDepartments();
    	List<JSONObject> jsonList = new ArrayList<>();
    	for(Department department: departments){
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("name", department.getName());
    		jsonObject.put("code", department.getCode());
    		jsonList.add(jsonObject);
    	}
    	return jsonList;
    }
    
    @RequestMapping(value = "/complainttypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getComplaintTypes(){
    	List<ComplaintType> complaintTypeList = complaintTypeService.findAll();
    	List<JSONObject> jsonList = new ArrayList<>();
    	for(ComplaintType complaintType: complaintTypeList){
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("name", complaintType.getName());
    		jsonObject.put("code", complaintType.getCode());
    		jsonList.add(jsonObject);
    	}
    	return jsonList;
    }
    
    @RequestMapping(value = "/wards", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JSONObject> getWards(){
    	List<Boundary> boundaryList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward","ADMINISTRATION");
    	List<JSONObject> jsonList = new ArrayList<>();
    	
    	for(Boundary boundary: boundaryList){
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("name", boundary.getName());
    		jsonObject.put("boundaryNumber", boundary.getBoundaryNum());
    		jsonObject.put("lat", boundary.getLatitude());
    		jsonObject.put("lon", boundary.getLongitude());
    		jsonList.add(jsonObject);
    	}
    	return jsonList;
    }
    
    @RequestMapping(value = "/sourcenames", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSourceNameList() throws JsonProcessingException{
    	return complaintIndexService.getSourceNameList();
    }
    
}
