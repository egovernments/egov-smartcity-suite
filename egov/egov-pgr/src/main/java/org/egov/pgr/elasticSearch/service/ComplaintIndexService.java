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

package org.egov.pgr.elasticSearch.service;

import static java.util.Arrays.asList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.pgr.elasticSearch.entity.ComplaintIndex;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.Escalation;
import org.egov.pgr.entity.enums.ComplaintStatus;
import org.egov.pgr.entity.enums.ReceivingMode;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.EscalationService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ComplaintIndexService {

    @Autowired
    private CityService cityService;
    
    @Autowired
    private EscalationService escalationService;
    
    @Autowired
    private EisCommonService eisCommonService;
    
    @Autowired
    private SearchService searchService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private ComplaintService complaintService;

    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex createComplaintIndex(final ComplaintIndex complaintIndex) {
    	final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
    	complaintIndex.setCitydetails(cityWebsite);
    	if (complaintIndex.getReceivingMode().equals(ReceivingMode.MOBILE))
    		complaintIndex.setSource("PuraSeva");
    	else if (complaintIndex.getReceivingMode().equals(ReceivingMode.WEBSITE)
    			&& (complaintIndex.getCreatedBy().getType().equals(UserType.CITIZEN)
    					|| complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM)))
    		complaintIndex.setSource("By citizens:ULB Portal");
    	else if (complaintIndex.getCreatedBy().getType().equals(UserType.EMPLOYEE) 
    			  || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM))
    		complaintIndex.setSource("ULB counter");
    	complaintIndex.setIsClosed(false);
    	complaintIndex.setComplaintIsClosed('N');
    	complaintIndex.setIfClosed(0);
    	complaintIndex.setComplaintDuration(0);
    	complaintIndex.setDurationRange("");
    	
        //New fields included in PGR Index
    	final Position position = complaintIndex.getAssignee();
    	User assignedUser = eisCommonService.getUserForPosition(position.getId(), new Date());
    	complaintIndex.setComplaintPeriod(0);
    	complaintIndex.setComplaintSLADays((complaintIndex.getComplaintType().getSlaHours())/24);
    	complaintIndex.setComplaintAgeingFromDue(0);
    	complaintIndex.setIsSLA('Y');
    	complaintIndex.setIfSLA(1);
    	complaintIndex.setInitialFunctionaryName(assignedUser.getName());
    	complaintIndex.setInitialFunctionaryAssigneddate(new Date());
    	complaintIndex.setInitialFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
    	complaintIndex.setInitialFunctionaryAgeingFromDue(0);
    	complaintIndex.setInitialFunctionaryIsSLA('Y');
    	complaintIndex.setInitialFunctionaryIfSLA(1);
    	complaintIndex.setCurrentFunctionaryName(assignedUser.getName());
    	complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
    	complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
    	complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
    	complaintIndex.setCurrentFunctionaryIsSLA('Y');
    	complaintIndex.setCurrentFunctionaryIfSLA(1);
    	complaintIndex.setEscalationLevel(0);
    	complaintIndex.setReasonForRejection("");
    	
    	return complaintIndex;
    }

    
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex updateComplaintIndex(ComplaintIndex complaintIndex, final Long approvalPosition, 
    		final String approvalComment) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCitydetails(cityWebsite);
        if (complaintIndex.getReceivingMode().equals(ReceivingMode.MOBILE))
            complaintIndex.setSource("PuraSeva");
        else if (complaintIndex.getReceivingMode().equals(ReceivingMode.WEBSITE)
                && (complaintIndex.getCreatedBy().getType().equals(UserType.CITIZEN)
                        || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM)))
            complaintIndex.setSource("By citizens:ULB Portal");
        else if (complaintIndex.getCreatedBy().getType().equals(UserType.EMPLOYEE) 
  			  || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM))
            complaintIndex.setSource("ULB counter");
        
        //Update the complaint index object  with the existing values
         complaintIndex = populateFromIndex(complaintIndex);
         
        //-------------------------- NEW INDEX FIELDS -----------------------------------------------------------------------------
        final Position position = complaintIndex.getAssignee();
        User assignedUser = eisCommonService.getUserForPosition(position.getId(), new Date());
    	//Update complaint level index variables
    	final long complaintPeriod = Math.abs(complaintIndex.getCreatedDate().getTime() - new Date().getTime())
                / (24 * 60 * 60 * 1000);
    	complaintIndex.setComplaintPeriod(complaintPeriod);
    	complaintIndex.setComplaintSLADays((complaintIndex.getComplaintType().getSlaHours())/24);
    	Date lastDateToResolve = DateUtils.addDays(complaintIndex.getCreatedDate(), (int)complaintIndex.getComplaintSLADays());
    	//If difference is greater than 0 then complaint is within SLA
    	if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
    		complaintIndex.setComplaintAgeingFromDue(0);
        	complaintIndex.setIsSLA('Y');
        	complaintIndex.setIfSLA(1);
    	}
    	else
    	{
    		long days = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
    				/ (24 * 60 * 60 * 1000);
    		complaintIndex.setComplaintAgeingFromDue(days);
        	complaintIndex.setIsSLA('N');
        	complaintIndex.setIfSLA(0);
    	}
    	//If complaint is forwarded
    	if(approvalPosition != null && !approvalPosition.equals(Long.valueOf(0))){
    		complaintIndex.setCurrentFunctionaryName(assignedUser.getName());
    		complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
    		complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
    	}
    	//Ageing for initial functionary variables
    	if(complaintIndex.getInitialFunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getInitialFunctionaryAssigneddate(), (int)complaintIndex.getInitialFunctionarySLADays());
    		//If difference is greater than 0 then initial functionary is within SLA
    		if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
    			complaintIndex.setInitialFunctionaryAgeingFromDue(0);
    			complaintIndex.setInitialFunctionaryIsSLA('Y');
    			complaintIndex.setInitialFunctionaryIfSLA(1);
    		}
    		else
    		{
    			long days = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
    					/ (24 * 60 * 60 * 1000);
    			complaintIndex.setInitialFunctionaryAgeingFromDue(days);
    			complaintIndex.setInitialFunctionaryIsSLA('N');
    			complaintIndex.setInitialFunctionaryIfSLA(0);
    		}
    	}
    	//Ageing for current functionary variables
    	if(complaintIndex.getCurrentFunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getCurrentFunctionaryAssigneddate(), (int)complaintIndex.getCurrentFunctionarySLADays());
    		//If difference is greater than 0 then current functionary is within SLA
    		if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
    			complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
    			complaintIndex.setCurrentFunctionaryIsSLA('Y');
    			complaintIndex.setCurrentFunctionaryIfSLA(1);
    		}
    		else
    		{
    			long days = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
    					/ (24 * 60 * 60 * 1000);
    			complaintIndex.setCurrentFunctionaryAgeingFromDue(days);
    			complaintIndex.setCurrentFunctionaryIsSLA('N');
    			complaintIndex.setCurrentFunctionaryIfSLA(0);
    		}
    	}
    	//-------------------------- NEW INDEX FIELDS -----------------------------------------------------------------------------
    	
        if (complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.COMPLETED.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.WITHDRAWN.toString())
                || complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString())) {
            complaintIndex.setIsClosed(true);
            complaintIndex.setComplaintIsClosed('Y');
            complaintIndex.setIfClosed(1);
            complaintIndex.setClosedByFunctionaryName(assignedUser.getName());
            final long duration = Math.abs(complaintIndex.getCreatedDate().getTime() - new Date().getTime())
                    / (24 * 60 * 60 * 1000);
            complaintIndex.setComplaintDuration(duration);
            if (duration < 3)
                complaintIndex.setDurationRange("(<3 days)");
            else if (duration < 7)
                complaintIndex.setDurationRange("(3-7 days)");
            else if (duration < 15)
                complaintIndex.setDurationRange("(8-15 days)");
            else if (duration < 30)
                complaintIndex.setDurationRange("(16-30 days)");
            else
                complaintIndex.setDurationRange("(>30 days)");
        }else {
            complaintIndex.setIsClosed(false);
            complaintIndex.setComplaintIsClosed('N');
            complaintIndex.setIfClosed(0);
            complaintIndex.setComplaintDuration(0);
            complaintIndex.setDurationRange("");
        }
        //If complaint is re-opened
        if(complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REOPENED.toString()) &&
        		checkComplaintStatusFromIndex(complaintIndex)){
        	complaintIndex.setComplaintReOpenedDate(new Date());
        	complaintIndex.setIsClosed(false);
            complaintIndex.setComplaintIsClosed('N');
            complaintIndex.setIfClosed(0);
        }
        //If complaint is rejected update the Reason For Rejection with comments
        if(complaintIndex.getStatus().getName().equalsIgnoreCase(ComplaintStatus.REJECTED.toString()))
        	complaintIndex.setReasonForRejection(approvalComment);
        
        return complaintIndex;
    }
    
    //This method is used to populate PGR index during complaint escalation
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex updateComplaintEscalationIndexValues(ComplaintIndex complaintIndex){
        final Position position = complaintIndex.getAssignee();
        User assignedUser = eisCommonService.getUserForPosition(position.getId(), new Date());
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        complaintIndex.setCitydetails(cityWebsite);
        if (complaintIndex.getReceivingMode().equals(ReceivingMode.MOBILE))
            complaintIndex.setSource("PuraSeva");
        else if (complaintIndex.getReceivingMode().equals(ReceivingMode.WEBSITE)
                && (complaintIndex.getCreatedBy().getType().equals(UserType.CITIZEN)
                        || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM)))
            complaintIndex.setSource("By citizens:ULB Portal");
        else if (complaintIndex.getCreatedBy().getType().equals(UserType.EMPLOYEE) 
    			  || complaintIndex.getCreatedBy().getType().equals(UserType.SYSTEM))
            complaintIndex.setSource("ULB counter");
        //Update the complaint index object  with the existing values
        complaintIndex = populateFromIndex(complaintIndex);
        
        //Update Complaint level index variables  
        //Based on assumption if complaint is escalating status is OPEN
        long days = Math.abs(new Date().getTime() - complaintIndex.getCreatedDate().getTime())
        		 / (24 * 60 * 60 * 1000);   
        complaintIndex.setComplaintPeriod(days);
        Date lastDateToResolve = DateUtils.addDays(complaintIndex.getCreatedDate(), (int)complaintIndex.getComplaintSLADays());
    	Date currentDate = new Date();
    	//If difference is greater than 0 then complaint is within SLA
    	if((lastDateToResolve.getTime() - currentDate.getTime()) >= 0){
    		complaintIndex.setComplaintAgeingFromDue(0);
        	complaintIndex.setIsSLA('Y');
        	complaintIndex.setIfSLA(1);
    	}
    	else
    	{
    		long ageingDueDays = Math.abs(currentDate.getTime() - lastDateToResolve.getTime())
    				/ (24 * 60 * 60 * 1000);
    		complaintIndex.setComplaintAgeingFromDue(ageingDueDays);
        	complaintIndex.setIsSLA('N');
        	complaintIndex.setIfSLA(0);
    	}
    	
    	//update the initial functionary level variables
    	if(complaintIndex.getInitialFunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getInitialFunctionaryAssigneddate(), (int)complaintIndex.getInitialFunctionarySLADays());
    		//If difference is greater than 0 then initial functionary is within SLA
    		if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
    			complaintIndex.setInitialFunctionaryAgeingFromDue(0);
    			complaintIndex.setInitialFunctionaryIsSLA('Y');
    			complaintIndex.setInitialFunctionaryIfSLA(1);
    		}
    		else
    		{
    			long initialFunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
    					/ (24 * 60 * 60 * 1000);
    			complaintIndex.setInitialFunctionaryAgeingFromDue(initialFunctionaryAgeingDueDays);
    			complaintIndex.setInitialFunctionaryIsSLA('N');
    			complaintIndex.setInitialFunctionaryIfSLA(0);
    		}
    	}
        
    	int escalationLevel = complaintIndex.getEscalationLevel();
    	//For Escalation level1
    	if(escalationLevel == 0)
    	{
    		complaintIndex.setEscalation1FunctionaryName(assignedUser.getName());
    		complaintIndex.setEscalation1FunctionaryAssigneddate(new Date());
    		complaintIndex.setEscalation1FunctionarySLADays(getFunctionarySlaDays(complaintIndex));
    		complaintIndex.setEscalation1FunctionaryAgeingFromDue(0);
        	complaintIndex.setEscalation1FunctionaryIsSLA('Y');
        	complaintIndex.setEscalation1FunctionaryIfSLA(1);
        	complaintIndex.setEscalationLevel(++escalationLevel);
    	}
    	else if(escalationLevel == 1)
    	{	
    		//update escalation level 2 fields
    		complaintIndex.setEscalation2FunctionaryName(assignedUser.getName());
    		complaintIndex.setEscalation2FunctionaryAssigneddate(new Date());
    		complaintIndex.setEscalation2FunctionarySLADays(getFunctionarySlaDays(complaintIndex));
    		complaintIndex.setEscalation2FunctionaryAgeingFromDue(0);
        	complaintIndex.setEscalation2FunctionaryIsSLA('Y');
        	complaintIndex.setEscalation2FunctionaryIfSLA(1);
        	complaintIndex.setEscalationLevel(++escalationLevel);
    	}
    	else if(escalationLevel == 2)
    	{	
    		//update escalation level 3 fields
    		complaintIndex.setEscalation3FunctionaryName(assignedUser.getName());
    		complaintIndex.setEscalation3FunctionaryAssigneddate(new Date());
    		complaintIndex.setEscalation3FunctionarySLADays(complaintIndex.getCurrentFunctionarySLADays());
    		complaintIndex.setEscalation3FunctionaryAgeingFromDue(0);
        	complaintIndex.setEscalation3FunctionaryIsSLA('Y');
        	complaintIndex.setEscalation3FunctionaryIfSLA(1);
        	complaintIndex.setEscalationLevel(++escalationLevel);
    	}
    	
    	//Update current Functionary Complaint index variables
        complaintIndex.setCurrentFunctionaryName(assignedUser.getName());
        complaintIndex.setCurrentFunctionaryAssigneddate(new Date());
    	complaintIndex.setCurrentFunctionarySLADays(getFunctionarySlaDays(complaintIndex));
    	if(complaintIndex.getCurrentFunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getCurrentFunctionaryAssigneddate(), (int)complaintIndex.getCurrentFunctionarySLADays());
    		//If difference is greater than 0 then current functionary is within SLA
    		if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
    			complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
    			complaintIndex.setCurrentFunctionaryIsSLA('Y');
    			complaintIndex.setCurrentFunctionaryIfSLA(1);
    		}
    		else
    		{
    			long currentFunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
    					/ (24 * 60 * 60 * 1000);
    			complaintIndex.setCurrentFunctionaryAgeingFromDue(currentFunctionaryAgeingDueDays);
    			complaintIndex.setCurrentFunctionaryIsSLA('N');
    			complaintIndex.setCurrentFunctionaryIfSLA(0);
    		}
    	}
    	
    	//update escalation level 1 fields
    	if(complaintIndex.getEscalation1FunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getEscalation1FunctionaryAssigneddate(), (int)complaintIndex.getEscalation1FunctionarySLADays());
    		//If difference is less than 0 then  functionary is not within SLA
    		if((lastDateToResolve.getTime() - new Date().getTime()) < 0){
    			long Escalation1FunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
    					/ (24 * 60 * 60 * 1000);
    			complaintIndex.setEscalation1FunctionaryAgeingFromDue(Escalation1FunctionaryAgeingDueDays);
    			complaintIndex.setEscalation1FunctionaryIsSLA('N');
    			complaintIndex.setEscalation1FunctionaryIfSLA(0);
    		}
    	}
    	
    	//update escalation level 2 fields
    	if(complaintIndex.getEscalation2FunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getEscalation2FunctionaryAssigneddate(), (int)complaintIndex.getEscalation2FunctionarySLADays());
        	//If difference is less than 0 then functionary is not within SLA
        	if((lastDateToResolve.getTime() - new Date().getTime()) < 0){
        		long Escalation2FunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
        				/ (24 * 60 * 60 * 1000);
        		complaintIndex.setEscalation2FunctionaryAgeingFromDue(Escalation2FunctionaryAgeingDueDays);
            	complaintIndex.setEscalation2FunctionaryIsSLA('N');
            	complaintIndex.setEscalation2FunctionaryIfSLA(0);
        	}
    	}
    	
    	//update escalation level 3 fields
    	if(complaintIndex.getEscalation3FunctionaryAssigneddate() != null){
    		lastDateToResolve = DateUtils.addDays(complaintIndex.getEscalation3FunctionaryAssigneddate(), (int)complaintIndex.getEscalation3FunctionarySLADays());
        	//If difference is less than 0 then functionary is not within SLA
        	if((lastDateToResolve.getTime() - new Date().getTime()) < 0){
        		long Escalation3FunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
        				/ (24 * 60 * 60 * 1000);
        		complaintIndex.setEscalation3FunctionaryAgeingFromDue(Escalation3FunctionaryAgeingDueDays);
            	complaintIndex.setEscalation3FunctionaryIsSLA('N');
            	complaintIndex.setEscalation3FunctionaryIfSLA(0);
        	}
    	}
    	return complaintIndex;
    }
    
    public void updateAllOpenComplaintIndex(){
    	List<Complaint> openComplaints  = complaintService.getOpenComplaints();
    	for(Complaint complaint : openComplaints){
    		updateOpenComplaintIndex(complaint);
    	}
    }
    
    @Indexing(name = Index.PGR, type = IndexType.COMPLAINT)
    public ComplaintIndex updateOpenComplaintIndex(Complaint complaint){
    		final Complaint savedComplaintIndex = new ComplaintIndex();
            BeanUtils.copyProperties(complaint, savedComplaintIndex);
            ComplaintIndex complaintIndex = ComplaintIndex.method(savedComplaintIndex);
            complaintIndex = populateFromIndex(complaintIndex);
            
            //Update complaint level index variables
            long days = Math.abs(new Date().getTime() - complaintIndex.getCreatedDate().getTime())
            		/ (24 * 60 * 60 * 1000);   
            complaintIndex.setComplaintPeriod(days);
            Date lastDateToResolve = DateUtils.addDays(complaintIndex.getCreatedDate(), (int)complaintIndex.getComplaintSLADays());
            Date currentDate = new Date();
            //If difference is greater than 0 then complaint is within SLA
            if((lastDateToResolve.getTime() - currentDate.getTime()) >= 0){
            	complaintIndex.setComplaintAgeingFromDue(0);
            	complaintIndex.setIsSLA('Y');
            	complaintIndex.setIfSLA(1);
            }
            else
            {
            	long ageingDueDays = Math.abs(currentDate.getTime() - lastDateToResolve.getTime())
            			/ (24 * 60 * 60 * 1000);
            	complaintIndex.setComplaintAgeingFromDue(ageingDueDays);
            	complaintIndex.setIsSLA('N');
            	complaintIndex.setIfSLA(0);
            }

            //update the initial functionary level variables
            if(complaintIndex.getInitialFunctionaryAssigneddate() != null){
            	lastDateToResolve = DateUtils.addDays(complaintIndex.getInitialFunctionaryAssigneddate(), (int)complaintIndex.getInitialFunctionarySLADays());
            	//If difference is greater than 0 then initial functionary is within SLA
            	if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
            		complaintIndex.setInitialFunctionaryAgeingFromDue(0);
            		complaintIndex.setInitialFunctionaryIsSLA('Y');
            		complaintIndex.setInitialFunctionaryIfSLA(1);
            	}
            	else
            	{
            		long initialFunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
            				/ (24 * 60 * 60 * 1000);
            		complaintIndex.setInitialFunctionaryAgeingFromDue(initialFunctionaryAgeingDueDays);
            		complaintIndex.setInitialFunctionaryIsSLA('N');
            		complaintIndex.setInitialFunctionaryIfSLA(0);
            	}
            }
            
            //update the current functionary level variables
            if(complaintIndex.getCurrentFunctionaryAssigneddate() != null){
            	lastDateToResolve = DateUtils.addDays(complaintIndex.getCurrentFunctionaryAssigneddate(), (int)complaintIndex.getCurrentFunctionarySLADays());
            	//If difference is greater than 0 then initial functionary is within SLA
            	if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
            		complaintIndex.setCurrentFunctionaryAgeingFromDue(0);
            		complaintIndex.setCurrentFunctionaryIsSLA('Y');
            		complaintIndex.setCurrentFunctionaryIfSLA(1);
            	}
            	else
            	{
            		long currentFunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
            				/ (24 * 60 * 60 * 1000);
            		complaintIndex.setCurrentFunctionaryAgeingFromDue(currentFunctionaryAgeingDueDays);
            		complaintIndex.setCurrentFunctionaryIsSLA('N');
            		complaintIndex.setCurrentFunctionaryIfSLA(0);
            	}
            }
            
            //update the Escalation1 functionary level variables
            if(complaintIndex.getEscalation1FunctionaryAssigneddate() != null){
            	lastDateToResolve = DateUtils.addDays(complaintIndex.getEscalation1FunctionaryAssigneddate(), (int)complaintIndex.getEscalation1FunctionarySLADays());
            	//If difference is greater than 0 then initial functionary is within SLA
            	if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
            		complaintIndex.setEscalation1FunctionaryAgeingFromDue(0);
            		complaintIndex.setEscalation1FunctionaryIsSLA('Y');
            		complaintIndex.setEscalation1FunctionaryIfSLA(1);
            	}
            	else
            	{
            		long escalation1FunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
            				/ (24 * 60 * 60 * 1000);
            		complaintIndex.setEscalation1FunctionaryAgeingFromDue(escalation1FunctionaryAgeingDueDays);
            		complaintIndex.setEscalation1FunctionaryIsSLA('N');
            		complaintIndex.setEscalation1FunctionaryIfSLA(0);
            	}
            }
            
            //update the Escalation2 functionary level variables
            if(complaintIndex.getEscalation2FunctionaryAssigneddate() != null){
            	lastDateToResolve = DateUtils.addDays(complaintIndex.getEscalation2FunctionaryAssigneddate(), (int)complaintIndex.getEscalation2FunctionarySLADays());
            	//If difference is greater than 0 then initial functionary is within SLA
            	if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
            		complaintIndex.setEscalation2FunctionaryAgeingFromDue(0);
            		complaintIndex.setEscalation2FunctionaryIsSLA('Y');
            		complaintIndex.setEscalation2FunctionaryIfSLA(1);
            	}
            	else
            	{
            		long escalation2FunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
            				/ (24 * 60 * 60 * 1000);
            		complaintIndex.setEscalation2FunctionaryAgeingFromDue(escalation2FunctionaryAgeingDueDays);
            		complaintIndex.setEscalation2FunctionaryIsSLA('N');
            		complaintIndex.setEscalation2FunctionaryIfSLA(0);
            	}
            }
            
            //update the Escalation3 functionary level variables
            if(complaintIndex.getEscalation3FunctionaryAssigneddate() != null){
            	lastDateToResolve = DateUtils.addDays(complaintIndex.getEscalation3FunctionaryAssigneddate(), (int)complaintIndex.getEscalation3FunctionarySLADays());
            	//If difference is greater than 0 then initial functionary is within SLA
            	if((lastDateToResolve.getTime() - new Date().getTime()) >= 0){
            		complaintIndex.setEscalation3FunctionaryAgeingFromDue(0);
            		complaintIndex.setEscalation3FunctionaryIsSLA('Y');
            		complaintIndex.setEscalation3FunctionaryIfSLA(1);
            	}
            	else
            	{
            		long escalation3FunctionaryAgeingDueDays = Math.abs(new Date().getTime() - lastDateToResolve.getTime())
            				/ (24 * 60 * 60 * 1000);
            		complaintIndex.setEscalation3FunctionaryAgeingFromDue(escalation3FunctionaryAgeingDueDays);
            		complaintIndex.setEscalation3FunctionaryIsSLA('N');
            		complaintIndex.setEscalation3FunctionaryIfSLA(0);
            	}
            }
            return complaintIndex;
    }
    
    
    private long getFunctionarySlaDays(ComplaintIndex complaintIndex){
    	Position position = complaintIndex.getAssignee();
    	Designation designation = assignmentService.getPrimaryAssignmentForPositon(position.getId()).getDesignation();
    	Escalation complaintEscalation = escalationService.getEscalationBycomplaintTypeAndDesignation(complaintIndex.getComplaintType().getId(), designation.getId());
    	if(complaintEscalation != null){
    		long slaDays = (complaintEscalation.getNoOfHrs())/24;
    		return 	slaDays;
    	}
    	else
    		return 0;
    } 
    
    public  ComplaintIndex populateFromIndex( ComplaintIndex complaintIndex){
    	SearchResult searchResult = null;
        searchResult = searchService.search(asList(Index.PGR.toString()), asList(IndexType.COMPLAINT.toString()),
                null, complaintIndex.searchFilters(), Sort.NULL, Page.NULL);
        if(searchResult.getDocuments().size() > 0){
        	List<Document> documents = searchResult.getDocuments();
        	LinkedHashMap<String, Object> clausesObject = (LinkedHashMap<String, Object>) documents.get(0).getResource().get("clauses");
        	LinkedHashMap<String, Object> searchableObject = (LinkedHashMap<String, Object>) documents.get(0).getResource().get("searchable");

        	double complaintPeriod = (clausesObject.get("complaintPeriod") == null) ? 0 : (double)clausesObject.get("complaintPeriod");
        	double complaintDuration = (searchableObject.get("complaintDuration") == null) ? 0 : (double)Double.valueOf(searchableObject.get("complaintDuration").toString());
        	int complaintSlaDays = (clausesObject.get("complaintSLADays") == null) ? 0 : (int)clausesObject.get("complaintSLADays");
        	double complaintAgeingFromDue = (clausesObject.get("complaintAgeingFromDue") == null) ? 0 : (double)clausesObject.get("complaintAgeingFromDue");
        	char isSla = (clausesObject.get("isSLA") == null) ? '-' : clausesObject.get("isSLA").toString().charAt(0);
        	int ifSla = (clausesObject.get("ifSLA") == null) ? 1 : (int)clausesObject.get("ifSLA");
        	boolean isClosed = (searchableObject.get("isClosed") == null) ? false : Boolean.valueOf(searchableObject.get("isClosed").toString());
        	char complaintIsClosed = (searchableObject.get("complaintIsClosed") == null) ? 'N' : searchableObject.get("complaintIsClosed").toString().charAt(0);
        	int ifClosed = (searchableObject.get("ifClosed") == null) ? 0 : (int)searchableObject.get("ifClosed");
        	String initialFunctionaryName = (clausesObject.get("initialFunctionaryName") == null) ? "" : clausesObject.get("initialFunctionaryName").toString();
        	int initialFunctionarySlaDays =  (clausesObject.get("initialFunctionarySLADays") == null) ? 0 : (int)clausesObject.get("initialFunctionarySLADays");
        	double initialAgeingFromDue = (clausesObject.get("initialFunctionaryAgeingFromDue") == null) ? 0 : (double)clausesObject.get("initialFunctionaryAgeingFromDue");
        	char initialFunctionaryIsSla = (clausesObject.get("initialFunctionaryIsSLA") == null) ? '-' : clausesObject.get("initialFunctionaryIsSLA").toString().charAt(0);
        	int initialFunctionaryIfSla = (clausesObject.get("initialFunctionaryIfSLA") == null) ? 1 : (int)clausesObject.get("initialFunctionaryIfSLA");
        	String currentFunctionaryName = (clausesObject.get("currentFunctionaryName") == null) ? "" : clausesObject.get("currentFunctionaryName").toString();
        	int currentFunctionarySlaDays =  (clausesObject.get("currentFunctionarySLADays") == null) ? 0 : (int)clausesObject.get("currentFunctionarySLADays");
        	double currentAgeingFromDue = (clausesObject.get("currentFunctionaryAgeingFromDue") == null) ? 0 : (double)clausesObject.get("currentFunctionaryAgeingFromDue");
        	char currentFunctionaryIsSla = (clausesObject.get("currentFunctionaryIsSLA") == null) ? '-' : clausesObject.get("currentFunctionaryIsSLA").toString().charAt(0);
        	int currentFunctionaryIfSla = (clausesObject.get("currentFunctionaryIfSLA") == null) ? 1 : (int)clausesObject.get("currentFunctionaryIfSLA");
        	String escalation1FunctionaryName = (clausesObject.get("escalation1FunctionaryName") == null) ? "" : clausesObject.get("escalation1FunctionaryName").toString();
        	int escalation1FunctionarySlaDays =  (clausesObject.get("escalation1FunctionarySLADays") == null) ? 0 : (int)clausesObject.get("escalation1FunctionarySLADays");
        	double escalation1AgeingFromDue = (clausesObject.get("escalation1FunctionaryAgeingFromDue") == null) ? 0 : (double)clausesObject.get("escalation1FunctionaryAgeingFromDue");
        	char escalation1FunctionaryIsSla = (clausesObject.get("escalation1FunctionaryIsSLA") == null) ? '-' : clausesObject.get("escalation1FunctionaryIsSLA").toString().charAt(0);
        	int escalation1FunctionaryIfSla = (clausesObject.get("escalation1FunctionaryIfSLA") == null) ? 1 : (int)clausesObject.get("escalation1FunctionaryIfSLA");
        	String escalation2FunctionaryName = (clausesObject.get("escalation2FunctionaryName") == null) ? "" : clausesObject.get("escalation2FunctionaryName").toString();
        	int escalation2FunctionarySlaDays =  (clausesObject.get("escalation2FunctionarySLADays") == null) ? 0 : (int)clausesObject.get("escalation2FunctionarySLADays");
        	double escalation2AgeingFromDue = (clausesObject.get("escalation2FunctionaryAgeingFromDue") == null) ? 0 : (double)clausesObject.get("escalation2FunctionaryAgeingFromDue");
        	char escalation2FunctionaryIsSla = (clausesObject.get("escalation2FunctionaryIsSLA") == null) ? '-' : clausesObject.get("escalation2FunctionaryIsSLA").toString().charAt(0);
        	int escalation2FunctionaryIfSla = (clausesObject.get("escalation2FunctionaryIfSLA") == null) ? 1 : (int)clausesObject.get("escalation2FunctionaryIfSLA");
        	String escalation3FunctionaryName = (clausesObject.get("escalation3FunctionaryName") == null) ? "" : clausesObject.get("escalation3FunctionaryName").toString();
        	int escalation3FunctionarySlaDays =  (clausesObject.get("escalation3FunctionarySLADays") == null) ? 0 : (int)clausesObject.get("escalation3FunctionarySLADays");
        	double escalation3AgeingFromDue = (clausesObject.get("escalation3FunctionaryAgeingFromDue") == null) ? 0 : (double)clausesObject.get("escalation3FunctionaryAgeingFromDue");
        	char escalation3FunctionaryIsSla = (clausesObject.get("escalation3FunctionaryIsSLA") == null) ? '-' : clausesObject.get("escalation3FunctionaryIsSLA").toString().charAt(0);
        	int escalation3FunctionaryIfSla = (clausesObject.get("escalation3FunctionaryIfSLA") == null) ? 1 : (int)clausesObject.get("escalation3FunctionaryIfSLA");
        	int escalationLevel = (clausesObject.get("escalationLevel") == null) ? 0 : (int)clausesObject.get("escalationLevel");
        	String durationRange = (searchableObject.get("durationRange") == null) ? "" : searchableObject.get("durationRange").toString();
        	String reasonForRejection = (clausesObject.get("reasonForRejection") == null) ? "" : clausesObject.get("reasonForRejection").toString();
        	
        	complaintIndex.setComplaintPeriod(complaintPeriod);
        	complaintIndex.setComplaintDuration(complaintDuration);
        	complaintIndex.setComplaintSLADays(complaintSlaDays);
        	complaintIndex.setComplaintAgeingFromDue(complaintAgeingFromDue);
        	complaintIndex.setIsSLA(isSla);
        	complaintIndex.setIfSLA(ifSla);
        	complaintIndex.setIsClosed(isClosed);
        	complaintIndex.setComplaintIsClosed(complaintIsClosed);
        	complaintIndex.setIfClosed(ifClosed);
        	complaintIndex.setInitialFunctionaryName(initialFunctionaryName);
        	if(searchableObject.get("initialFunctionaryAssigneddate") != null)
        		complaintIndex.setInitialFunctionaryAssigneddate(formatDate(searchableObject.get("initialFunctionaryAssigneddate").toString()));
        	complaintIndex.setInitialFunctionarySLADays(initialFunctionarySlaDays);
        	complaintIndex.setInitialFunctionaryAgeingFromDue(initialAgeingFromDue);
        	complaintIndex.setInitialFunctionaryIsSLA(initialFunctionaryIsSla);
        	complaintIndex.setInitialFunctionaryIfSLA(initialFunctionaryIfSla);
        	complaintIndex.setCurrentFunctionaryName(currentFunctionaryName);
        	if(searchableObject.get("currentFunctionaryAssigneddate") != null)
        		complaintIndex.setCurrentFunctionaryAssigneddate(formatDate(searchableObject.get("currentFunctionaryAssigneddate").toString()));
        	complaintIndex.setCurrentFunctionarySLADays(currentFunctionarySlaDays);
        	complaintIndex.setCurrentFunctionaryAgeingFromDue(currentAgeingFromDue);
        	complaintIndex.setCurrentFunctionaryIsSLA(currentFunctionaryIsSla);
        	complaintIndex.setCurrentFunctionaryIfSLA(currentFunctionaryIfSla);
        	complaintIndex.setEscalation1FunctionaryName(escalation1FunctionaryName);
        	if(searchableObject.get("escalation1FunctionaryAssigneddate") != null)
        		complaintIndex.setEscalation1FunctionaryAssigneddate(formatDate(searchableObject.get("escalation1FunctionaryAssigneddate").toString()));
        	complaintIndex.setEscalation1FunctionarySLADays(escalation1FunctionarySlaDays);
        	complaintIndex.setEscalation1FunctionaryAgeingFromDue(escalation1AgeingFromDue);
        	complaintIndex.setEscalation1FunctionaryIsSLA(escalation1FunctionaryIsSla);
        	complaintIndex.setEscalation1FunctionaryIfSLA(escalation1FunctionaryIfSla);
        	complaintIndex.setEscalation2FunctionaryName(escalation2FunctionaryName);
        	if(searchableObject.get("escalation2FunctionaryAssigneddate") != null)
        		complaintIndex.setEscalation2FunctionaryAssigneddate(formatDate(searchableObject.get("escalation2FunctionaryAssigneddate").toString()));
        	complaintIndex.setEscalation2FunctionarySLADays(escalation2FunctionarySlaDays);
        	complaintIndex.setEscalation2FunctionaryAgeingFromDue(escalation2AgeingFromDue);
        	complaintIndex.setEscalation2FunctionaryIsSLA(escalation2FunctionaryIsSla);
        	complaintIndex.setEscalation2FunctionaryIfSLA(escalation2FunctionaryIfSla);
        	complaintIndex.setEscalation3FunctionaryName(escalation3FunctionaryName);
        	if(searchableObject.get("escalation3FunctionaryAssigneddate") != null)
        		complaintIndex.setEscalation3FunctionaryAssigneddate(formatDate(searchableObject.get("escalation3FunctionaryAssigneddate").toString()));
        	complaintIndex.setEscalation3FunctionarySLADays(escalation3FunctionarySlaDays);
        	complaintIndex.setEscalation3FunctionaryAgeingFromDue(escalation3AgeingFromDue);
        	complaintIndex.setEscalation3FunctionaryIsSLA(escalation3FunctionaryIsSla);
        	complaintIndex.setEscalation3FunctionaryIfSLA(escalation3FunctionaryIfSla);
        	complaintIndex.setEscalationLevel(escalationLevel);
        	complaintIndex.setDurationRange(durationRange);
        	complaintIndex.setReasonForRejection(reasonForRejection);
        	if(searchableObject.get("complaintReOpenedDate") != null)
        		complaintIndex.setComplaintReOpenedDate(formatDate(searchableObject.get("complaintReOpenedDate").toString()));
        }
        return complaintIndex;
    }
    
    //returns false if status in index for complaint is REOPENED else will return true
    //This method used to prevent updating reopened date in index for already reopened complaints
    private boolean checkComplaintStatusFromIndex(ComplaintIndex complaintIndex){
    	SearchResult searchResult = null;
    	String status = "";
        searchResult = searchService.search(asList(Index.PGR.toString()), asList(IndexType.COMPLAINT.toString()),
                null, complaintIndex.searchFilters(), Sort.NULL, Page.NULL);
        if(searchResult.getDocuments().size() > 0){
        	List<Document> documents = searchResult.getDocuments();
        	LinkedHashMap<String, Object> clausesObject = (LinkedHashMap<String, Object>) documents.get(0).getResource().get("clauses");
        	status = (clausesObject.get("status") == null) ? "" : clausesObject.get("status").toString();
        }
        return (status.contains("REOPENED")) ? false : true;
    }
    
    public Date formatDate(String date){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
		}
		return null;
    }
}