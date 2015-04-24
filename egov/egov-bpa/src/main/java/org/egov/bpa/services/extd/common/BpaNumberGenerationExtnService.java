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
package org.egov.bpa.services.extd.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.EGOVRuntimeException;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.utils.Sequence;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.utils.seqgen.DatabaseSequenceFirstTimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.pims.empLeave.model.CalendarYear;

public class BpaNumberGenerationExtnService {
	private static final Logger LOGGER = Logger.getLogger(BpaNumberGenerationExtnService.class);
	private  SequenceGenerator sequenceGenerator ;
	private BpaCommonExtnService bpaCommonExtnService;
	private InspectionExtnService inspectionExtnService;
	private	NumberFormat ackNumberFormat = new DecimalFormat(BpaConstants.ACKNOWLEDGEMENTNUMBERFORMAT);
	
	public String generatePlanSubmissionNumber(ServiceTypeExtn serviceType,Boundary zone){

		Sequence runningNo;		
		StringBuffer formatedNumber = new StringBuffer(50);
		LOGGER.debug("generatePlanSubmissionNumber ");
			if(null!=serviceType && serviceType.getServiceNumberPrefix()!=null && !"".equals(serviceType.getServiceNumberPrefix())){
				
				if(null!=zone && zone.getName()!=null && !"".equals(zone.getName())){
					LOGGER.debug("serviceType--> " +serviceType.getDescription() +" zone--> "+zone.getName());
					CalendarYear calyear = bpaCommonExtnService.getCalendarYear();				
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+serviceType.getServiceNumberPrefix().toUpperCase() +'_'+calyear.getCalendarYear(),Long.valueOf(1));
								
					formatedNumber.append(serviceType.getServiceNumberPrefix().toUpperCase()); 
					formatedNumber.append("/");
					formatedNumber.append(BpaConstants.WDC+zone.getName());
					formatedNumber.append("/");
					formatedNumber.append(ackNumberFormat.format(runningNo.getNumber()));
					formatedNumber.append("/");
					if(calyear!=null && calyear.getCalendarYear()!=null)
						formatedNumber.append(calyear.getCalendarYear()); 
					
					//formatedNumber.append(finyear.getFinYearRange());
					LOGGER.debug(" PlanSubmissionNumber--> "+formatedNumber.toString());	
				return formatedNumber.toString();
				}
				else
					return null;
					//throw new EGOVRuntimeException("zone is required for generate Autogeneration of Plan Submission Number");				
			}
			else
				return null;//throw new EGOVRuntimeException("Service Type is required for generate Autogeneration of Plan Submission Number");
	}

	public String generateTrackingNumber(ServiceTypeExtn serviceType,Boundary zone){

		Sequence runningNo;		
		StringBuffer formatedNumber = new StringBuffer(50);
		LOGGER.debug("generateTrackingNumber ");
			if(null!=serviceType && serviceType.getCode()!=null && !"".equals(serviceType.getCode())){
				
				if(null!=zone && zone.getName()!=null && !"".equals(zone.getName())){
					LOGGER.debug("serviceType--> " +serviceType.getDescription() +" zone--> "+zone.getName());
					CalendarYear calyear = bpaCommonExtnService.getCalendarYear();				
					runningNo = sequenceGenerator.getNextNumber(zone.getName()+serviceType.getCode().toUpperCase() +'_'+calyear.getCalendarYear(),Long.valueOf(1));
								
					formatedNumber.append(zone.getName());
					formatedNumber.append("/");
					formatedNumber.append(serviceType.getCode().toUpperCase()); 
					formatedNumber.append("/");
					formatedNumber.append(ackNumberFormat.format(runningNo.getNumber()));
					formatedNumber.append("/");
					if(calyear!=null && calyear.getCalendarYear()!=null)
						formatedNumber.append(calyear.getCalendarYear()); 
					
					//formatedNumber.append(finyear.getFinYearRange());
					LOGGER.debug(" TrackingNumber--> "+formatedNumber.toString());	
				return formatedNumber.toString();
				}
				else
					return null;
								
			}
			else
				return null;
	}
/*
 * While submitting the Reply details, system should generate the acknowledgment no
 * Acknowledgment No format : <financial year>/<Running Sequence No>
 *	Ex : 2012-13/000001
 */

	public String generateLetterToReplyAckNumber(){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;		
		LOGGER.debug("generateLetterToReplyAckNumber ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"LR_"+finyear.getFinYearRange(),Long.valueOf(1) );
			
			formatedNumber.append(finyear.getFinYearRange());
			formatedNumber.append("/");
			formatedNumber.append(runningNo.getNumber());
			LOGGER.debug("LetterToReplyAckNumber " +formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}
		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateLetterToReplyAckNumber method." +ex);	
		}
	}
	
	/*
	 * Challan No Format : <financial year>/<zone>/<Sequence no>
	 * +zone.getName()
	 */
	
	public String generateChallanNumberFormat(BoundaryImpl zone){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;	
		LOGGER.debug("generateChallanNumberFormat ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"CNF_"+finyear.getFinYearRange(),Long.valueOf(1) );
			/*raise Question
			 * if Each Zone wise Auto generation Number is unique then 
			 * runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPA+"CNF_"+zone.getName()+finyear.getFinYearRange(),Long.valueOf(1) );
			 */
			
			
			formatedNumber.append(zone.getName());
			formatedNumber.append("/");
			formatedNumber.append(finyear.getFinYearRange());
			formatedNumber.append("/");
			formatedNumber.append(ackNumberFormat.format((runningNo.getNumber())));
			LOGGER.debug("ChallanNumberFormat " +formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}
		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateChallanNumberFormat method." +ex);	
		}
	}
	/*
	 * While clicking print button, Order No to be generated.
	 * If service code 1,3,6 then 
	Format of Order No :BA/WDC <Zone No>/<auto generation number>/<Year>
	Ex : BA/WDC01/000001/2012
	Else if service code 7 then 
	Format of Order No :CEBA/WDC <Zone No>/<auto generation number>/<Year>
	Ex : CEBA/WDC01/000001/2012
	Else if service code 2 then 
	Same PPA no
	There is no BP order
	Else if service code 4  then 
	Same PPA no
	There is no BP order
	Else if service code 5 and No of Plot >8 then 
	Format of Order No :LA/WDC <Zone No>/<auto generation number>/<Year>
	Ex : LA/WDC01/000001/2012
	There is no BP order
	Else if service code 5 and No of Plot <8 then 
	Same PPA No
	There is No BP order
	 * 
	 */
	
	public String generateOrderNumber(RegistrationExtn registration){
		
		Sequence runningNo = null;		
		StringBuffer formatedNumber = new StringBuffer(50);
		Boolean flag=Boolean.FALSE;
		Boolean noOfPlotsFlag=Boolean.FALSE;
		LOGGER.debug("generateOrderNumber ");
		try{
			
			Boundary zone=bpaCommonExtnService.getZoneNameFromAdminboundaryid(registration.getAdminboundaryid());
		
			List<InspectionExtn> inspectionList= inspectionExtnService.getSiteInspectionListforRegistrationObject(registration);				
			if(!inspectionList.isEmpty()){
				InspectionExtn inspectionObj=	inspectionList.get(0);
				if(null!=inspectionObj && null!=inspectionObj.getInspectionDetails() && null!=inspectionObj.getInspectionDetails().getNumOfPlots())
					if(inspectionObj.getInspectionDetails().getNumOfPlots().doubleValue()>8){
						noOfPlotsFlag=Boolean.TRUE;
					}
			}
			
			if(null!=zone && null!=registration && null!=registration.getServiceType() && registration.getServiceType().getServiceNumberPrefix()!=null && !"".equals(registration.getServiceType().getServiceNumberPrefix())){
				LOGGER.debug("serviceType--> " +registration.getServiceType().getDescription() +" zone--> "+zone.getName() +" noOfPlotsFlag-->"+noOfPlotsFlag);
				
				CalendarYear calYear = bpaCommonExtnService.getCalendarYear();
				if(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE) ||
						registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) ||
						registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE) ){
					
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+"_BA" +'_'+calYear.getCalendarYear(),Long.valueOf(1));									
					formatedNumber.append("BA");
					flag=Boolean.TRUE;
					
				}
				else if(registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+"_CEBA" +'_'+calYear.getCalendarYear(),Long.valueOf(1));						
					formatedNumber.append("CEBA");
					flag=Boolean.TRUE;
				}
				else if(registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE) && noOfPlotsFlag.equals(Boolean.TRUE)){
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+"_LA" +'_'+calYear.getCalendarYear(),Long.valueOf(1));						
					formatedNumber.append("LA");
					flag=Boolean.TRUE;
				}
				if(flag){
					formatedNumber.append("/");
					formatedNumber.append(BpaConstants.WDC+zone.getName());
					formatedNumber.append("/");
					formatedNumber.append(ackNumberFormat.format(runningNo.getNumber()));
					formatedNumber.append("/");
					//formatedNumber.append(finyear.getFinYearRange());
					if(calYear!=null && calYear.getCalendarYear()!=null)
						formatedNumber.append(calYear.getCalendarYear()); 
					LOGGER.debug("OrderNumber "+formatedNumber.toString());
					return formatedNumber.toString();
				}
				else{
					return registration.getPlanSubmissionNum();
				}
			}
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateOrderNumber() method." +ex);	
		}
		return null;
	
	}

	
	public String generateSiteInspectionNumber(){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;	
		LOGGER.debug("generateSiteInspectionNumber ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"SI_"+finyear.getFinYearRange(),Long.valueOf(1) );
			
			formatedNumber.append(finyear.getFinYearRange());
			formatedNumber.append("/");
			formatedNumber.append(runningNo.getNumber());
			
			LOGGER.debug("SiteInspectionNumber "+formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}
		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateSiteInspectionNumber method." +ex);	
		}
	}
	
	
	public String generateLetterToPartyNumber(){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;	
		LOGGER.debug("generateLetterToPartyNumber ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"LP_"+finyear.getFinYearRange(),Long.valueOf(1) );
			
			formatedNumber.append(finyear.getFinYearRange());
			formatedNumber.append("/");
			formatedNumber.append(runningNo.getNumber());
			
			LOGGER.debug("LetterToPartyNumber "+formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateLetterToPartyNumber method." +ex);	
		}
	}
	

	
	public String generateRejectionOrderNumber(){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;
		LOGGER.debug("generateRejectionOrderNumber ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"_RO_"+finyear.getFinYearRange(),Long.valueOf(1) );
			
			formatedNumber.append(runningNo.getNumber());
			formatedNumber.append("/");
			formatedNumber.append(finyear.getFinYearRange());

			LOGGER.debug("RejectionOrderNumber "+formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}
		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateRejectionOrderNumber method." +ex);	
		}
	}
	
	
	public String generateRevisedFeeChallanNumberFormat(BoundaryImpl zone,Integer revisedFeeNumber){
		
		
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;	
		LOGGER.debug("generateChallanNumberFormat ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"RCNF_"+finyear.getFinYearRange(),Long.valueOf(1) );
			/*raise Question
			 * if Each Zone wise Auto generation Number is unique then 
			 * runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPA+"CNF_"+zone.getName()+finyear.getFinYearRange(),Long.valueOf(1) );
			 */
			
			formatedNumber.append(zone.getName());
			formatedNumber.append("/");
			formatedNumber.append(finyear.getFinYearRange());
			formatedNumber.append("/");
			formatedNumber.append("R"+revisedFeeNumber);
			formatedNumber.append("/");
			formatedNumber.append(ackNumberFormat.format((runningNo.getNumber())));
			LOGGER.debug("ChallanNumberFormat " +formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}
		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateChallanNumberFormat method." +ex);	
		}
	}
	
	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}

	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}

	public void setInspectionExtnService(InspectionExtnService inspectionService) {
		this.inspectionExtnService = inspectionService;
	}
	
	
}
