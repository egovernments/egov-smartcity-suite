package org.egov.pgr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.repository.ComplaintRouterRepository;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintRouterService {
	
	
	private ComplaintRouterRepository complaintRouterRepository;
	
	private static final Logger LOG=LoggerFactory.getLogger(ComplaintRouterService.class);
	
	
	@Autowired
	public ComplaintRouterService(ComplaintRouterRepository complaintRouterRepository) {
		this.complaintRouterRepository = complaintRouterRepository;
	}



	/**
	 * 
	 * @param complaint
	 * @return
	 *  This api takes responsibility of returning suitable position for the given complaint
	 *  Api considers two fields from complaint a. complaintType b. Boundary
	 *  The descision is taken as below
	 *  
	 *  1. If complainttype and boundary from complaint is found in router then return corresponding position 
	 *  2. If only complainttype from complaint is found search router for matching entry in router and return position
	 *  3. If no postion found for above then search router with only boundary of given complaint and return corresponding position
	 *  4. If none of the above gets position then return GO 
	 *  5. GO is default for all complaints.
	 * 
	 *  It expects the data in the following format 
	 *  Say  ComplaintType CT1,CT2,CT3 Present with CT1 locationRequired is true
	 *  Boundary B1 to B5 are child boundaries and B0 is the top boundary (add only child boundaries not the top or middle ones)
	 *  Postion P1 to P10 are different positions
	 *  then ComplaintRouter is populate like this 
	 *  
	 *  ComplaintType			Boundary			Position
	 *  =====================================================
	 *  1.	CT1						B1					P1
	 *  2.	CT1						B2					P2
	 *  3.	CT1						B3					P3
	 *  4.	CT1						B4					P4
	 *  5.	CT1						B5					P5
	 *  6.	CT1 					null				P6       This is complaintType default
	 *  7.	null					B5					P7       This is Boundary default  
	 *  8.  null					B0					P8       This is GO. he is city level default. This data is mandatory .
	 *  Line 6 and 7 are exclusive means if 6 present 7 will not be considered .
	 *  If you want boundary level default then dont add complaint type default
	 *  
	 *  search result 
	 *  
	 *  complaint is registered with complaint type CT1 and boundary B1 will give P1
	 *                                              CT1 and Boundary is not provided will give p6, if line 6 not added then it will give P8
	 *  
	 *  
	 *  
	 *  
	 *  
	 */
	public Position getAssignee(Complaint complaint)
	{
		Position position=null;
		ComplaintRouter complaintRouter=null;
		if(null!=complaint.getComplaintType() && null != complaint.getLocation())
		{
			complaintRouter = complaintRouterRepository.findByComplaintTypeAndBoundary(complaint.getComplaintType(),complaint.getLocation());
			
		}
		if(null==complaintRouter && null !=complaint.getLocation())
		{
			complaintRouter=complaintRouterRepository.findByBoundary(complaint.getLocation());
		}
		
		if(null==complaintRouter && null!=complaint.getComplaintType() )
		{
			complaintRouter=complaintRouterRepository.findByOnlyComplaintType(complaint.getComplaintType());
			
		}
		
		
		if(complaintRouter==null)
		{
			if(LOG.isDebugEnabled()) LOG.debug("finding GO......");
			complaintRouter=complaintRouterRepository.findGrievanceOfficer();	
			
		}
		
		if(complaintRouter!=null)
		{
			position=complaintRouter.getPosition();
			
		}else
		{
			//TODO throw exception
			LOG.error("GO is not configured in the router . returning null");
			
		}
		
		return position;
	}
	

}
