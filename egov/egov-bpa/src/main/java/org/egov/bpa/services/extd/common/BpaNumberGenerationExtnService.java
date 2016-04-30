package org.egov.bpa.services.extd.common;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.commons.CFinancialYear;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.utils.Sequence;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.infstr.utils.seqgen.DatabaseSequenceFirstTimeException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

//import org.egov.pims.empLeave.model.CalendarYear;

public class BpaNumberGenerationExtnService {
	private static final Logger LOGGER = Logger.getLogger(BpaNumberGenerationExtnService.class);
	private  SequenceGenerator sequenceGenerator ;
	private BpaCommonExtnService bpaCommonExtnService;
	private InspectionExtnService inspectionExtnService;
	private	NumberFormat ackNumberFormat = new DecimalFormat(BpaConstants.ACKNOWLEDGEMENTNUMBERFORMAT);
	
	/*By passing service type get preliminary request number.
	 * Format: OBPA/Service Type Number/Sequence Number/Calender Year.
	 */
	
	public String generatePreliminaryRequestNumber(ServiceTypeExtn serviceType){
		Sequence runningNo;		
		StringBuffer formatedNumber = new StringBuffer(50);
		LOGGER.debug("generatePreliminaryRequestNumber ");
			if(null!=serviceType && serviceType.getCode()!=null && !"".equals(serviceType.getCode())){
				//CalendarYear calyear = bpaCommonExtnService.getCalendarYear();				
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.OBPA+serviceType.getCode().toUpperCase() +'_'+"",Long.valueOf(1));
					formatedNumber.append(BpaConstants.OBPA); 
					formatedNumber.append("/");			
					formatedNumber.append(serviceType.getCode().toUpperCase()); 
					formatedNumber.append("/");
					formatedNumber.append(ackNumberFormat.format(runningNo.getNumber()));
					formatedNumber.append("/");
					/*if(calyear!=null && calyear.getCalendarYear()!=null)
						formatedNumber.append(calyear.getCalendarYear()); *///TODO Phionix
					
				LOGGER.debug(" preliminary request number--> "+formatedNumber.toString());	
				return formatedNumber.toString();
					
			}
			else
				return null;//throw new EGOVRuntimeException("Service Type is required for generate Autogeneration of Plan Submission Number");
	
	}
	
	public String generatePlanSubmissionNumber(ServiceTypeExtn serviceType,Boundary zone){

		Sequence runningNo;		
		StringBuffer formatedNumber = new StringBuffer(50);
		LOGGER.debug("generatePlanSubmissionNumber ");
			if(null!=serviceType && serviceType.getServiceNumberPrefix()!=null && !"".equals(serviceType.getServiceNumberPrefix())){
				
				if(null!=zone && zone.getName()!=null && !"".equals(zone.getName())){
					LOGGER.debug("serviceType--> " +serviceType.getDescription() +" zone--> "+zone.getName());
					// calyear = bpaCommonExtnService.getCalendarYear();	//TODO Phionix			
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+serviceType.getServiceNumberPrefix().toUpperCase() +'_'+"",Long.valueOf(1));
								
					formatedNumber.append(serviceType.getServiceNumberPrefix().toUpperCase()); 
					formatedNumber.append("/");
					formatedNumber.append(BpaConstants.WDC+zone.getName());
					formatedNumber.append("/");
					formatedNumber.append(ackNumberFormat.format(runningNo.getNumber()));
					formatedNumber.append("/");
				/*	if(calyear!=null && calyear.getCalendarYear()!=null)
						formatedNumber.append(calyear.getCalendarYear()); *///TODO Phionix
					
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
					//TODO Phionix
					//CalendarYear calyear = bpaCommonExtnService.getCalendarYear();				
					runningNo = sequenceGenerator.getNextNumber(zone.getName()+serviceType.getCode().toUpperCase() +'_'+"",Long.valueOf(1));
								
					formatedNumber.append(zone.getName());
					formatedNumber.append("/");
					formatedNumber.append(serviceType.getCode().toUpperCase()); 
					formatedNumber.append("/");
					formatedNumber.append(ackNumberFormat.format(runningNo.getNumber()));
					formatedNumber.append("/");
					/*if(calyear!=null && calyear.getCalendarYear()!=null)
						formatedNumber.append(calyear.getCalendarYear()); *///TODO Phionix
					
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
	public String generateLetterToReplycmdaAckNumber(){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;		
		LOGGER.debug("generateLetterToReplyAckNumber For CMDA ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"LR_"+finyear.getFinYearRange(),Long.valueOf(1) );
			formatedNumber.append(BpaConstants.CMDALPFORMATSTRING);
			formatedNumber.append("/");
			formatedNumber.append(runningNo.getNumber());
			formatedNumber.append("/");
			formatedNumber.append(finyear.getFinYearRange());
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
	
	public String generateChallanNumberFormat(Boundary zone){
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
				
				/*CalendarYear calYear = bpaCommonExtnService.getCalendarYear();*/
				if(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE) ||
						registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) ||
						registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE) ){
					
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+"_BA" +'_'+"",Long.valueOf(1));									
					formatedNumber.append("BA");
					flag=Boolean.TRUE;
					
				}
				else if(registration.getServiceType().getCode().equals(BpaConstants.CMDACODE)){
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+"_CEBA" +'_'+"",Long.valueOf(1));						
					formatedNumber.append("CEBA");
					flag=Boolean.TRUE;
				}
				else if(registration.getServiceType().getCode().equals(BpaConstants.LAYOUTAPPPROVALCODE) && noOfPlotsFlag.equals(Boolean.TRUE)){
					runningNo = sequenceGenerator.getNextNumber(BpaConstants.WDC+"_LA" +'_'+"",Long.valueOf(1));						
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
					/*if(calYear!=null && calYear.getCalendarYear()!=null)
						formatedNumber.append(calYear.getCalendarYear()); */
					LOGGER.debug("OrderNumber "+formatedNumber.toString());
					return formatedNumber.toString();
				}
				else{
					//return registration.getPlanSubmissionNum();
					return registration.getPlanPermitApprovalNum();
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
	
	
	public String generateCMDALetterToPartyNumber(){
		StringBuffer formatedNumber = new StringBuffer(50);
		Sequence runningNo;	
		LOGGER.debug("generateCMDALetterToPartyNumber ");
		try{
			CFinancialYear finyear = bpaCommonExtnService.getFinancialYear();
			runningNo = sequenceGenerator.getNextNumber(BpaConstants.BPAMODULENAME+"LPCMDA_"+finyear.getFinYearRange(),Long.valueOf(1) );
			formatedNumber.append(BpaConstants.CMDALPFORMATSTRING);
			formatedNumber.append("/");
			formatedNumber.append(finyear.getFinYearRange());
			formatedNumber.append("/");
			formatedNumber.append(runningNo.getNumber());
			
			LOGGER.debug("CMDALetterToPartyNumber "+formatedNumber.toString());
			return formatedNumber.toString();
		}
		catch(DatabaseSequenceFirstTimeException d){
			throw new EGOVRuntimeException("DatabaseSequenceFirstTimeException." +d);	
		}		
		catch(Exception ex){
			throw new EGOVRuntimeException("Exception in generateCMDALetterToPartyNumber method." +ex);	
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
	
	
	public String generateRevisedFeeChallanNumberFormat(Boundary zone,Integer revisedFeeNumber){
		
		
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