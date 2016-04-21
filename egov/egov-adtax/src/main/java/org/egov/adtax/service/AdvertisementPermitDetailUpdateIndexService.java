package org.egov.adtax.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.elasticSearch.service.AdvertisementIndexService;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.search.elastic.entity.ApplicationIndex;
import org.egov.infra.search.elastic.entity.ApplicationIndexBuilder;
import org.egov.infra.search.elastic.entity.enums.ApprovalStatus;
import org.egov.infra.search.elastic.entity.enums.ClosureStatus;
import org.egov.infra.search.elastic.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementPermitDetailUpdateIndexService {
	
	    @PersistenceContext
	    private EntityManager entityManager;

	    @Autowired
	    private ApplicationIndexService applicationIndexService;

	    @Autowired
	    private AssignmentService assignmentService;

	    @Autowired
	    private EisCommonService eisCommonService;

	    @Autowired
	    private SecurityUtils securityUtils;
	    @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private AdvertisementIndexService advertisementIndexService;

	    public Session getCurrentSession() {
	        return entityManager.unwrap(Session.class);
	    }

	/**
	 * @param advertisementPermitDetail
	 */
	public void updateAdvertisementPermitDetailIndexes(final AdvertisementPermitDetail advertisementPermitDetail) {

			Assignment assignment = null;
	         User user = null;
	         List<Assignment> asignList = null;
	         if (advertisementPermitDetail.getState() != null && advertisementPermitDetail.getState().getOwnerPosition() != null) {
	             assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(advertisementPermitDetail.getState().getOwnerPosition()
	                     .getId(), new Date());
	             if (assignment != null) {
	                 asignList = new ArrayList<Assignment>();
	                 asignList.add(assignment);
	             } else if (assignment == null)
	                 asignList = assignmentService.getAssignmentsForPosition(advertisementPermitDetail.getState().getOwnerPosition().getId(),
	                         new Date());
	             if (!asignList.isEmpty())
	                 user = userService.getUserById(asignList.get(0).getEmployee().getId());
	         } else
	             user = securityUtils.getCurrentUser();
	         
	         // For legacy application - create only advertisementIndex
	         if (advertisementPermitDetail.getAdvertisement().getLegacy()
	                 && (null == advertisementPermitDetail.getId() || (null != advertisementPermitDetail.getId()
	                 && advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)))) {
	        	 advertisementIndexService.createAdvertisementIndex(advertisementPermitDetail); 
	             return;
	         }
	         
	         ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(advertisementPermitDetail
	                 .getApplicationNumber());
	         // update existing application index
	         if (applicationIndex != null && null != advertisementPermitDetail.getId() && advertisementPermitDetail.getStatus() != null
	                 && !advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CREATED)) {
	        	 if(advertisementPermitDetail.getStatus()!=null &&
	        			 (advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)
	        			 || advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXAMOUNTPAID)
	        			 || advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)
	        			 || advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED))
	        		){
        		     applicationIndex.setStatus(advertisementPermitDetail.getStatus().getDescription());
 	                 applicationIndex.setOwnername(user!=null?user.getUsername() + "::" + user.getName():"");
	        		 
 	                 // Set application index status to approved on advertisement approval
 	                 if(advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)){
	        			String applicantName=advertisementPermitDetail.getAgency()!=null?advertisementPermitDetail.getAgency().getName():
	 	                	 advertisementPermitDetail.getOwnerDetail();
	 	                String address=advertisementPermitDetail.getAgency()!=null?advertisementPermitDetail.getAgency().getAddress():
	 	                	 advertisementPermitDetail.getOwnerDetail();
	 	                applicationIndex.setApplicantName(applicantName);
	 	                applicationIndex.setApplicantAddress(address);
	 	                applicationIndex.setMobileNumber(advertisementPermitDetail.getAgency()!=null?
		                		 advertisementPermitDetail.getAgency().getMobileNumber():"");
	 	                applicationIndex.setApproved(ApprovalStatus.APPROVED);
	 	                applicationIndex.setClosed(ClosureStatus.NO);
	        		 }
 	                 // mark application index as closed on generate permit order
 	                 if(advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)){
 	                	 applicationIndex.setClosed(ClosureStatus.YES);
 	                 }
 	                 // mark application index as rejected and closed on advertisement cancellation / Deactivation
 	                 if(advertisementPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED) || 
 	                	 advertisementPermitDetail.getAdvertisement().getStatus().name().equalsIgnoreCase("INACTIVE")){
 	                	 applicationIndex.setApproved(ApprovalStatus.REJECTED);
 	                	 applicationIndex.setClosed(ClosureStatus.YES);
 	                 }
 	                 
 	                 if (advertisementPermitDetail.getPermissionNumber() != null)
                	   applicationIndex.setConsumerCode(advertisementPermitDetail.getPermissionNumber());
 	                 
 	                 applicationIndexService.updateApplicationIndex(applicationIndex);
	        	 }
	        	 //create advertisement index
	        	 advertisementIndexService.createAdvertisementIndex(advertisementPermitDetail);
	        	 
	         } else {   // Create New ApplicationIndex on create advertisement
	        	 final String strQuery = "select md from EgModules md where md.name=:name";
	             final Query hql = getCurrentSession().createQuery(strQuery);
	             hql.setParameter("name", AdvertisementTaxConstants.MODULE_NAME);
	             if (advertisementPermitDetail.getApplicationDate() == null)
	            	 advertisementPermitDetail.setApplicationDate(new Date());
	             if (advertisementPermitDetail.getApplicationNumber() == null)
	            	 advertisementPermitDetail.setApplicationNumber(advertisementPermitDetail.getApplicationNumber());
	             if (applicationIndex == null) {
	                 final String url = "/adtax/hoarding/view/"
	                         + advertisementPermitDetail.getId();
	                 String applicantName=advertisementPermitDetail.getAgency()!=null?advertisementPermitDetail.getAgency().getName():
	                	 advertisementPermitDetail.getOwnerDetail();
	                 String address=advertisementPermitDetail.getAgency()!=null?advertisementPermitDetail.getAgency().getAddress():
	                	 advertisementPermitDetail.getOwnerDetail();
	                 final ApplicationIndexBuilder applicationIndexBuilder = new ApplicationIndexBuilder(
	                		 AdvertisementTaxConstants.MODULE_NAME, advertisementPermitDetail.getApplicationNumber(),
	                         advertisementPermitDetail.getApplicationDate(), advertisementPermitDetail.getState().getNatureOfTask(), applicantName, 
	                         advertisementPermitDetail.getStatus().getDescription(),
	                         url, address, user.getUsername() + "::" + user.getName(), Source.SYSTEM.toString());

	                 applicationIndexBuilder.mobileNumber(advertisementPermitDetail.getAgency()!=null?
	                		 advertisementPermitDetail.getAgency().getMobileNumber():"");
	                 applicationIndexBuilder.aadharNumber(null);
	                 applicationIndexBuilder.approved(ApprovalStatus.UNKNOWN);
                	 applicationIndexBuilder.closed(ClosureStatus.NO);
	                 applicationIndex = applicationIndexBuilder.build();
	                 applicationIndexService.createApplicationIndex(applicationIndex);
	             }
	         }
	    }
}