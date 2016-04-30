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
package org.egov.bpa.services.extd.Fee;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.BpaFeeDetailExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.LandBuildingTypesExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaDmdCollExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infstr.services.PersistenceService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
@Transactional(readOnly=true)
public class FeeDetailsExtnService extends PersistenceService{
	
	private BpaDmdCollExtnService bpaDmdCollExtnService;
    private PersistenceService persistenceService;
    private final static  Logger LOGGER=Logger.getLogger(FeeDetailsExtnService.class);
	private String feeSubTypeAsBuilding = "BUILDING";
	protected BpaCommonExtnService bpaCommonExtnService;
	protected FeeExtnService feeExtnService;
	
	
	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeExtnService) {
		this.feeExtnService = feeExtnService;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}

	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public BpaDmdCollExtnService getBpaDmdCollExtnService() {
		return bpaDmdCollExtnService;
	}

	public void setBpaDmdCollExtnService(BpaDmdCollExtnService bpaDmdCollService) {
		this.bpaDmdCollExtnService = bpaDmdCollService;
	}
	@Transactional
	public RegistrationExtn save(List<BpaFeeExtn> santionFeeList, RegistrationExtn registrationObj) {
		LOGGER.debug("Enter save ");
		
		
		registrationObj=bpaDmdCollExtnService.generateDemandUsingSanctionFeeList(santionFeeList,registrationObj);
		/*EgDemand dmd = registrationObj.getEgDemand()!=null?registrationObj.getEgDemand():createNewDemand();
		
		BigDecimal totaldmdAmt = BigDecimal.ZERO;
		
		for(BpaFee bpaFee:santionFeeList){
			
			if(bpaFee.getFeeGroup()!=null && bpaFee.getFeeGroup().equals(BpaConstants.COCFEE)){
				if (bpaFee.getFeeAmount()!=null&&!bpaFee.getFeeAmount().equals(BigDecimal.ZERO)) {

					if(bpaFee.getDemandDetailId()==null){
						EgDemandDetails dmdDet = new EgDemandDetails();				
						dmdDet.setAmount(bpaFee.getFeeAmount());
						dmdDet.setAmtCollected(BigDecimal.ZERO);
						dmdDet.setAmtRebate(BigDecimal.ZERO);
						bpaFee.setGlcode(getAccountForFee(bpaFee));
						dmdDet.setEgDemandReason(bpaDmdCollService.getEgDemandReason(bpaFee));
						dmdDet.setCreateTimestamp(new Date());
						dmdDet.setLastUpdatedTimeStamp(new Date());				
						dmd.getEgDemandDetails().add(dmdDet);
					}else{
						EgDemandDetails dmdDetail =(EgDemandDetails) persistenceService.find("from EgDemandDetails where id=?",bpaFee.getDemandDetailId());
						dmdDetail.setAmount(bpaFee.getFeeAmount());				
					}

				}else{
					if(bpaFee.getDemandDetailId()!=null&&!bpaFee.getDemandDetailId().equals("")){
						EgDemandDetails dmdDetail =(EgDemandDetails) persistenceService.find("from EgDemandDetails where id=?",bpaFee.getDemandDetailId());
						if(dmd.getEgDemandDetails().contains(dmdDetail)){
							dmd.getEgDemandDetails().remove(dmdDetail);
						}

					}
				}
			}
		}

		
    for(EgDemandDetails det:dmd.getEgDemandDetails()){
	   totaldmdAmt=totaldmdAmt.add(det.getAmount());
     }
    dmd.setBaseDemand(totaldmdAmt);
		if(registrationObj.getEgDemand()==null){
			registrationObj.setEgDemand(dmd);
		
		}
		
	persistenceService.getSession().flush();
	LOGGER.debug("Exit save ");
*/	 return registrationObj;	
	}
	
	@SuppressWarnings("unchecked")
	public void removeCMDAandMWGWFDetailsForLegacy(RegistrationExtn registrationObj){
		List<String> feeCodeList=persistenceService.findAllBy("select feeCode from BpaFee where serviceType.id=? and feeGroup in ('CMDA','MWGWF')",registrationObj.getServiceType().getId());
		if(registrationObj!=null&&registrationObj.getEgDemand()!=null && registrationObj.getEgDemand()!=null){
			Iterator<EgDemandDetails> itr=registrationObj.getEgDemand().getEgDemandDetails().iterator();
			while(itr.hasNext()){ 
			 EgDemandDetails dmdDtl=	itr.next();
			if(dmdDtl!=null && dmdDtl.getEgDemandReason()!=null && dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode()!=null) {
				if(feeCodeList.contains(dmdDtl.getEgDemandReason().getEgDemandReasonMaster().getCode())){
					itr.remove();					
				} 
			}	
			} 
		}
	}

	
/*
	private EgDemand createNewDemand() {
		LOGGER.debug("Enter createNewDemand ");
		Installment installment = bpaDmdCollService.getCurrentInstallment();
		EgDemand demand = new EgDemand();	
		demand.setAmtCollected(BigDecimal.ZERO);
		demand.setAmtRebate(BigDecimal.ZERO);
		demand.setIsHistory("N");
		demand.setLastUpdatedTimestamp(new Date());
		demand.setCreateTimestamp(new Date());
		if (installment == null) {
			throw new EGOVRuntimeException("Installment is null");
		} else {
			demand.setEgInstallmentMaster(installment);
		}
		persistenceService.setType(EgDemand.class);
		persistenceService.create(demand);
		LOGGER.debug("Created new Demand ");
		LOGGER.debug("Exited createNewDemand ");
	   return demand;
	}

	public CChartOfAccounts getAccountForFee(BpaFee fee){
		BpaFee bpafee=(BpaFee)persistenceService.find("from BpaFee where id=?",fee.getId());
		return bpafee.getGlcode();
	}
*/	
	
	public Boolean isFeeCalculationRequiredForServiceType(InspectionExtn inspection, RegistrationExtn registrationObj,String feeCode, String serviceTypeCode)
	{
	
		if(feeCode!=null &&  serviceTypeCode!=null ) {
			
			//104,304,604 calculateTentativeImprovementCharge  bldngIsImprovementCharges 
			//109,309,609 calculateRegularisationChargeUnder244AFee  bldngIsRegularisationCharges
			//108,308,608 calculateRegulationChargeForLand	 lndIsRegularisationCharges
		
			if(registrationObj.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)){ 
					
				if (feeCode.equals("102")
							|| feeCode.equals("105") || feeCode.equals("111")
							|| feeCode.equals("112") || feeCode.equals("110")
							|| feeCode.equals("114") || feeCode.equals("113")
							|| feeCode.equals("107") || feeCode.equals("115")
							|| feeCode.equals("101") || feeCode.equals("106") ){
					return true;
			   }else if( feeCode.equals("104") && inspection!=null && inspection.getBldngIsImprovementCharges()!=null &&  inspection.getBldngIsImprovementCharges().equals(Boolean.TRUE))
				{
					return true;
				}else if( feeCode.equals("108") && inspection!=null && inspection.getLndIsRegularisationCharges()!=null &&  inspection.getLndIsRegularisationCharges().equals(Boolean.TRUE))
				{
					return true;
				}else if( feeCode.equals("109") && inspection!=null && inspection.getBldngIsRegularisationCharges()!=null &&  inspection.getBldngIsRegularisationCharges().equals(Boolean.TRUE))
				{
					return true;
				}
			}
			
			//303 demolition fee required only for demolition service type.
			if(registrationObj.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)) {
					if (feeCode.equals("303") || feeCode.equals("302")
							|| feeCode.equals("305") || feeCode.equals("311")
							|| feeCode.equals("312") || feeCode.equals("310")
							|| feeCode.equals("314") || feeCode.equals("313")
							|| feeCode.equals("307") || feeCode.equals("315")
							|| feeCode.equals("301") || feeCode.equals("306") ){
						return true;
					}else if( feeCode.equals("304") && inspection!=null && inspection.getBldngIsImprovementCharges()!=null &&  inspection.getBldngIsImprovementCharges().equals(Boolean.TRUE))
					{
						return true;
					}else if( feeCode.equals("308") && inspection!=null && inspection.getLndIsRegularisationCharges()!=null &&  inspection.getLndIsRegularisationCharges().equals(Boolean.TRUE))
					{
						return true;
					}else if( feeCode.equals("309") && inspection!=null && inspection.getBldngIsRegularisationCharges()!=null &&  inspection.getBldngIsRegularisationCharges().equals(Boolean.TRUE))
					{
						return true;
					}
			}
				
			if(registrationObj.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)){
					if( feeCode.equals("602")
					|| feeCode.equals("605") || feeCode.equals("611")
					|| feeCode.equals("612") || feeCode.equals("610")
					|| feeCode.equals("614") || feeCode.equals("613")
					|| feeCode.equals("607") || feeCode.equals("615")
					|| feeCode.equals("601") || feeCode.equals("606")){
						return true;
					}else if( feeCode.equals("604") && inspection!=null && inspection.getBldngIsImprovementCharges()!=null &&  inspection.getBldngIsImprovementCharges().equals(Boolean.TRUE))
					{
						return true;
					}else if( feeCode.equals("608") && inspection!=null && inspection.getLndIsRegularisationCharges()!=null &&  inspection.getLndIsRegularisationCharges().equals(Boolean.TRUE))
					{
						return true;
					}else if( feeCode.equals("609") && inspection!=null && inspection.getBldngIsRegularisationCharges()!=null &&  inspection.getBldngIsRegularisationCharges().equals(Boolean.TRUE))
					{
						return true;
					}
			}
			return false;
		}
		
		return false;
	}

	public void calculateFeeByServiceType(BpaFeeExtn fee, RegistrationExtn registration, InspectionExtn inspectionExtn){

		BigDecimal feeAmount=BigDecimal.ZERO;
		if(fee!=null &&  registration!=null )
		{			
			if(registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE))
			{
				if(fee.getFeeCode().equals("101"))
					calculateCmdaDevelopmentCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("108"))
					calculateRegulationChargeForLand(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("106"))
					calculateScrutinyFees(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("115"))
					calculateWorkerWelfareFees(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("107"))
					calculateSecurityDeposit(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("113"))
					calculatePlanCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("114"))
					calculateInfrastructureCharges(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("110") || fee.getFeeCode().equals("111") ||fee.getFeeCode().equals("112"))
					calculateRoadCutCharges(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("105"))
					calculateOpenSpaceReservationCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("109"))
					calculateRegularisationChargeUnder244AFee(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("104"))
					calculateTentativeImprovementCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("102"))
					calculateBuildingLicenseFee(fee, registration, inspectionExtn,feeAmount);	
			}
		
			if(registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE))
			{
				if(fee.getFeeCode().equals("301"))
					calculateCmdaDevelopmentCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("308"))
					calculateRegulationChargeForLand(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("306"))
					calculateScrutinyFees(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("315"))
					calculateWorkerWelfareFees(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("307"))
					calculateSecurityDeposit(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("313"))
					calculatePlanCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("314"))
					calculateInfrastructureCharges(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("310") || fee.getFeeCode().equals("311") ||fee.getFeeCode().equals("312"))
					calculateRoadCutCharges(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("305"))
					calculateOpenSpaceReservationCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("303"))
					calculateDemolitionFee(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("309"))
					calculateRegularisationChargeUnder244AFee(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("304"))
					calculateTentativeImprovementCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("302"))
					calculateBuildingLicenseFee(fee, registration, inspectionExtn,feeAmount);	
			}
			
			if(registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))
			{
				if(fee.getFeeCode().equals("601"))
					calculateCmdaDevelopmentCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("608"))
					calculateRegulationChargeForLand(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("606"))
					calculateScrutinyFees(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("615"))
					calculateWorkerWelfareFees(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("607"))
					calculateSecurityDeposit(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("613"))
					calculatePlanCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("614"))
					calculateInfrastructureCharges(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("610") || fee.getFeeCode().equals("611") ||fee.getFeeCode().equals("612"))
					calculateRoadCutCharges(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("605"))
					calculateOpenSpaceReservationCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("609"))
					calculateRegularisationChargeUnder244AFee(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("604"))
					calculateTentativeImprovementCharge(fee, registration, inspectionExtn,feeAmount);
				else if(fee.getFeeCode().equals("602"))
					calculateBuildingLicenseFee(fee, registration, inspectionExtn,feeAmount);	
			}
			
		}
		
	
		
	}


	private void calculateBuildingLicenseFee(BpaFeeExtn fee,
			RegistrationExtn registration, InspectionExtn inspectionExtn,
			BigDecimal feeAmount) {
		
	if(inspectionExtn.getBuildingType() != null)
	{
	
	 if(inspectionExtn.getBuildingType().getName()!=null && inspectionExtn.getBuildingType().getName().equalsIgnoreCase("mixed residential"))
	  {	
		   if(inspectionExtn.getBldngResidential()!=null && bpaCommonExtnService!=null)
			  {
				  LandBuildingTypesExtn residential=bpaCommonExtnService.getLandBuildingTypesExtnByDescription("Residential");
				 
				if(residential!=null && inspectionExtn.getBldngResidential() != null  && inspectionExtn.getBldngResidential().compareTo(BigDecimal.valueOf(0))>0)
					{
					List <BpaFeeDetailExtn> getAllFeeDetailsBasedOnArea = feeExtnService.getListOfFeeDetails(Boolean.FALSE,Boolean.TRUE,
							registration.getServiceType().getId(),fee.getFeeCode(),
							(inspectionExtn.getBldngResidential()),
							BpaConstants.SANCTIONEDFEE,	"BUILDING",
							null, null, (residential!=null?residential.getName():null),null);
					
						feeAmount = feeAmount.add(recursivelyCalculateFeeAmount(BigDecimal.ZERO,inspectionExtn.getBldngResidential(),
							 getAllFeeDetailsBasedOnArea));
					}
			  }
		  if(inspectionExtn.getBldngCommercial()!=null && inspectionExtn.getBldngResidential() != null && bpaCommonExtnService!=null)
		  {
			  LandBuildingTypesExtn commercial=bpaCommonExtnService.getLandBuildingTypesExtnByDescription("Commercial");
			  
			  if(commercial!=null && inspectionExtn.getBldngCommercial() != null  && inspectionExtn.getBldngCommercial().compareTo(BigDecimal.valueOf(0))>0)
				{
				List <BpaFeeDetailExtn> getAllFeeDetailsBasedOnArea = feeExtnService.getListOfFeeDetails(Boolean.TRUE,Boolean.FALSE,
						registration.getServiceType().getId(),fee.getFeeCode(),
						(inspectionExtn.getBldngResidential()), //range greater than residential.
						BpaConstants.SANCTIONEDFEE,feeSubTypeAsBuilding,
						null, null, (commercial!=null?commercial.getName():null),null);
				
				//Added commercial and residential as total value to be calculate. 
					feeAmount = feeAmount.add(recursivelyCalculateFeeAmount(inspectionExtn.getBldngResidential(),inspectionExtn.getBldngCommercial().add(inspectionExtn.getBldngResidential()),
						getAllFeeDetailsBasedOnArea));
				}
		  }
	  
	  }
	 
	 else{
	  
		if(inspectionExtn.getBldngBuildUpArea()!=null && inspectionExtn.getBldngBuildUpArea().compareTo(BigDecimal.valueOf(0))>0){	
			List <BpaFeeDetailExtn> getAllFeeDetailsBasedOnArea = feeExtnService.getListOfFeeDetails(Boolean.FALSE,Boolean.TRUE,
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngBuildUpArea()),
					BpaConstants.SANCTIONEDFEE,	"BUILDING",
					null, null, (inspectionExtn.getBuildingType() != null ? inspectionExtn
					.getBuildingType().getName() : null),null);
			
				feeAmount =  feeAmount.add(recursivelyCalculateFeeAmount(BigDecimal.ZERO,inspectionExtn.getBldngBuildUpArea(),
					 getAllFeeDetailsBasedOnArea));
			
		}
	  }	
	}	
		if(inspectionExtn.getBldngWellOht_SumpTankArea()!=null)
			{
			BpaFeeDetailExtn oHTCharge = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					null,BpaConstants.SANCTIONEDFEE,	"WELLOHPSUMPTANKAREA",
					null, null, null,null);
			
			if(oHTCharge!=null)
				{
				LOGGER.info(" Building Fee OHTCHARGE - > " + oHTCharge.getAmount());
					feeAmount=feeAmount.add(oHTCharge.getAmount().multiply(inspectionExtn.getBldngWellOht_SumpTankArea()));		
				}
			
			}
		
		LOGGER.info(" Building Fee Charge- > " + feeAmount);
		if(feeAmount.compareTo(BigDecimal.ZERO)>0)
		{
			fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
		}else
			fee.setFeeAmount(BigDecimal.ZERO);
		
		}

		private BigDecimal recursivelyCalculateFeeAmount(BigDecimal alReadyProcessedQty,
				BigDecimal bldngAreaParam, 
				List<BpaFeeDetailExtn> getAllFeeDetailsBasedOnArea) {
			BigDecimal processedQty=alReadyProcessedQty;
			BigDecimal totalAmount=BigDecimal.ZERO;
			//Eg:BuildingArea 101
			for(BpaFeeDetailExtn feeDtl : getAllFeeDetailsBasedOnArea)
			{
				//first loop, 40<=101.. yes.. feeAmount=40*Rate, processedQty=40   
				//Second Loop 100<=101.. yes.. feeAmount=FeeAmount+60*Rate , processedqty=100
				//third loop 400<=101  no.. else part.. feeAmount=feeAmount + (101-100)*Rate 
			if(processedQty.compareTo(bldngAreaParam)<0){
				if(feeDtl.getToAreasqmt().compareTo(bldngAreaParam)<=0)
				{
					totalAmount = totalAmount.add((feeDtl.getToAreasqmt().subtract(processedQty)).multiply(feeDtl.getAmount()));
					processedQty=processedQty.add(feeDtl.getToAreasqmt().subtract(processedQty));
					
					LOGGER.info(" processed qty and price - > " + (feeDtl.getToAreasqmt().subtract(processedQty)) + " Rate "+ feeDtl.getAmount() );
				}else
				{
					totalAmount = totalAmount.add((bldngAreaParam.subtract(processedQty)).multiply(feeDtl.getAmount()));
					processedQty=processedQty.add(bldngAreaParam.subtract(processedQty));
					LOGGER.info(" processed qty and price - > " + (bldngAreaParam.subtract(processedQty)) + " Rate "+ feeDtl.getAmount() );
				} 
			 }	
			}
			return totalAmount;
		}
		
		private void calculateTentativeImprovementCharge(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			
			String regionName=null; 
			
		  if(inspectionExtn!=null && inspectionExtn.getBldngProposedPlotFrntage()!=null)
		  {
			
			if(registration!=null &&  registration.getAdminboundaryid()!=null && inspectionExtn.getBldngRoadWidth()!=null)
			{
				if(registration.getAdminboundaryid().getParent()!=null && registration.getAdminboundaryid().getParent().getParent()!=null && registration.getAdminboundaryid().getParent().getBoundaryType().getName()!=null
						&& registration.getAdminboundaryid().getParent().getBoundaryType().getName().equals(BpaConstants.ZONE_BNDRY_TYPE))
				{
					regionName=registration.getAdminboundaryid().getParent().getParent().getName().toUpperCase();
				}else if(registration.getAdminboundaryid().getParent()!=null && registration.getAdminboundaryid().getParent().getParent()==null
						&& registration.getAdminboundaryid().getParent().getBoundaryType().getName().equals(BpaConstants.REGION_BNDRY_TYPE)
						&& registration.getAdminboundaryid().getParent().getName()!=null)
				{
					regionName=registration.getAdminboundaryid().getParent().getName().toUpperCase();
				}
				
			LOGGER.info("Region Name inside Tentative Improvement charge - > " + regionName);	
			
			
			BpaFeeDetailExtn roadImprovCharge = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngRoadWidth() != null ? (inspectionExtn.getBldngRoadWidth()) : null),
					BpaConstants.SANCTIONEDFEE,	"ROADIMPROVEMENTCHARGE",
					null, null, null,regionName);
			

			BpaFeeDetailExtn streetLightCharge = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngRoadWidth() != null ? (inspectionExtn.getBldngRoadWidth()) : null),
					BpaConstants.SANCTIONEDFEE,	"STREETLIGHTCHARGE",
					null, null, null,null);
			
			LOGGER.info(" ROADIMPROVEMENTCHARGE Charge- > " + (roadImprovCharge!=null?roadImprovCharge.getAmount():BigDecimal.ZERO));
			LOGGER.info(" STREETLIGHTCHARGE Charge- > " + (streetLightCharge!=null?streetLightCharge.getAmount():BigDecimal.ZERO));
			
			//if road width is less than 12.21 Tentative Improvement Charges = ( Proposed Plot Frontage / 2 ) * ( Street Light Charges + Street Light Charges )
			if(inspectionExtn.getBldngRoadWidth().compareTo(BigDecimal.valueOf(12.21))<=0){
				
				feeAmount = feeAmount.add(
						(inspectionExtn.getBldngProposedPlotFrntage().divide(BigDecimal.valueOf(2))).multiply(
						(roadImprovCharge!=null?roadImprovCharge.getAmount():BigDecimal.ZERO).add((streetLightCharge!=null?streetLightCharge.getAmount():BigDecimal.ZERO)))
						);
				
			}else
			{
				//Tentative Improvement Charges = ( Proposed Plot Frontage / 2 ) * ( Street Light Charges + Street Light Charges +Storm Water Drain Charges)
				BpaFeeDetailExtn stormWaterDrainageCharge = feeExtnService.getFeeDetails(
						registration.getServiceType().getId(),fee.getFeeCode(),
						(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
						BpaConstants.SANCTIONEDFEE,	"STORNWATERDRAINCHARGE",
						null, null, null,(inspectionExtn.getBldngStormWaterDrain() != null ? (inspectionExtn.getBldngStormWaterDrain().getName()) : null));
				LOGGER.info(" STORNWATERDRAINCHARGE Charge- > " + ((stormWaterDrainageCharge!=null && stormWaterDrainageCharge.getAmount()!=null)?stormWaterDrainageCharge.getAmount():BigDecimal.ZERO));
				feeAmount = feeAmount.add(
						(inspectionExtn.getBldngProposedPlotFrntage().divide(BigDecimal.valueOf(2))).multiply(
						((roadImprovCharge!=null?roadImprovCharge.getAmount():BigDecimal.ZERO).add((streetLightCharge!=null?streetLightCharge.getAmount():BigDecimal.ZERO)))
						.add(((stormWaterDrainageCharge!=null && stormWaterDrainageCharge.getAmount()!=null)?stormWaterDrainageCharge.getAmount():BigDecimal.ZERO)))
						);			
				
			 }
			}
					
				LOGGER.info(" Tentative Improvement Charge- > " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
				}else
					fee.setFeeAmount(BigDecimal.ZERO);
		  }
		
		}
		
	private void calculateRegularisationChargeUnder244AFee(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			
			if(inspectionExtn!=null && inspectionExtn.getLndRegularizationArea()!=null
					&& inspectionExtn.getLndPenaltyPeriod()!=null)
			{
				
			feeAmount = feeAmount.add(inspectionExtn.getLndRegularizationArea()
					.multiply(
							BigDecimal
									.valueOf(
											inspectionExtn
													.getLndPenaltyPeriod())
									.multiply(BigDecimal.valueOf(10.76))
									.multiply(BigDecimal.valueOf(0.50))));
			}
			

			LOGGER.info("Regularization charge under 244A fee "+fee.getFeeCode() +" " + feeAmount);
			if(feeAmount.compareTo(BigDecimal.ZERO)>0)
			{
				fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
			}else
				fee.setFeeAmount(BigDecimal.ZERO);
		}
		private void calculateDemolitionFee(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			
			BpaFeeDetailExtn groundFloorOtherType = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					null,
					BpaConstants.SANCTIONEDFEE,	"GROUNDFLOORANDOTHERTYPE",
					null, null, null,null);
			BpaFeeDetailExtn groundFloorWithTileType = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					null,
					BpaConstants.SANCTIONEDFEE,	"GROUNDFLOORWITHTILED",
					null, null, null,null);
			BpaFeeDetailExtn firstFlrAndAboveType = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					null,
					BpaConstants.SANCTIONEDFEE,	"FIRSTFLOORANDABOVE",
					null, null, null,null);
			
				if(groundFloorOtherType!=null && inspectionExtn.getBldngGFloor_OtherTypes()!=null)
					feeAmount = feeAmount.add(groundFloorOtherType.getAmount().multiply(inspectionExtn.getBldngGFloor_OtherTypes()));
				
				if(groundFloorWithTileType!=null && inspectionExtn.getBldngGFloor_TiledFloor()!=null)
					feeAmount = feeAmount.add(groundFloorWithTileType.getAmount().multiply(inspectionExtn.getBldngGFloor_TiledFloor()));
				
				if(firstFlrAndAboveType!=null && inspectionExtn.getBldngFrstFloor_TotalArea()!=null)
					feeAmount = feeAmount.add(firstFlrAndAboveType.getAmount().multiply(inspectionExtn.getBldngFrstFloor_TotalArea()));
				
				
				LOGGER.info("demolition fee "+fee.getFeeCode() +" " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		}

		
		private void calculateOpenSpaceReservationCharge(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
			BigDecimal feeAmount) {

		/*
		 * Total (OSR) Land Extent Prior to 05.08.75 (A) Minimum Plot Extent (B) Guideline Value (C)
		 */
		if (inspectionExtn.getLndOsrLandExtent() != null && inspectionExtn.getLndOsrLandExtent().compareTo(	BigDecimal.valueOf(3000)) > 0) {
			// (OSR) Land Extent Prior to 05.08.75 values is 10000.01 � and
			// above
			if (inspectionExtn.getLndOsrLandExtent().compareTo(	BigDecimal.valueOf(10000)) > 0) {

				// Formula: Open Space Reservation Charges = ( (A � 3000 ) / A ) * B * C * 0.01
				if (inspectionExtn.getLndMinPlotExtent() != null && inspectionExtn.getLndGuideLineValue() != null) {
					feeAmount = feeAmount.add((inspectionExtn
							.getLndMinPlotExtent().multiply(inspectionExtn
							.getLndGuideLineValue())).multiply(BigDecimal
							.valueOf(0.1)));
				}
			} else {
				// Formula: Open Space Reservation Charges = ( (A � 3000 ) / A ) * B * C * 0.1
				// (OSR) Land Extent Prior to 05.08.75 values is 3000.01 � 10000
				if (inspectionExtn.getLndMinPlotExtent() != null && inspectionExtn.getLndGuideLineValue() != null) {
					feeAmount = feeAmount.add(((((inspectionExtn
							.getLndOsrLandExtent().subtract(BigDecimal
							.valueOf(3000))).divide(inspectionExtn
							.getLndOsrLandExtent(), 4, RoundingMode.HALF_UP)).multiply(inspectionExtn
							.getLndMinPlotExtent())).multiply(inspectionExtn
							.getLndGuideLineValue())).multiply(BigDecimal
							.valueOf(0.1)));
				}
			}
		}
		LOGGER.info("Open source reservation  Fees " + fee.getFeeCode() + " " + feeAmount);
		if (feeAmount.compareTo(BigDecimal.ZERO) > 0) {
			fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(), 100)));
		} else
			fee.setFeeAmount(BigDecimal.ZERO);

	}
		
		private void calculateRoadCutCharges(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
		
			if(inspectionExtn!=null && inspectionExtn.getBldngRoadWidth()!=null) {
				BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
						registration.getServiceType().getId(),fee.getFeeCode(),
						null,
						BpaConstants.SANCTIONEDFEE,	null,
						null, null, null,null);
	
					if(feeDtlBldg!=null)
					feeAmount = feeAmount.add(((feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngRoadWidth()))).multiply(BigDecimal.valueOf(0.75))).divide(BigDecimal.valueOf(2)));
				}
				LOGGER.info("Road Cut Charge Fees "+fee.getFeeCode() +" " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		}
		private void calculateInfrastructureCharges(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {

			
			BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
					BpaConstants.SANCTIONEDFEE,	feeSubTypeAsBuilding,
					null, null, (inspectionExtn
					.getBuildingType() != null ? inspectionExtn
					.getBuildingType().getName() : null),null);

				if(feeDtlBldg!=null)
				feeAmount = feeAmount.add((feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngBuildUpArea()))));
				
				LOGGER.info("Infrastructure charges Fees- > " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		
		}

		private void calculatePlanCharge(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			
			BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
					BpaConstants.SANCTIONEDFEE,	null,
					null, null, null,null);

				if(feeDtlBldg!=null)
				feeAmount = feeAmount.add(feeDtlBldg.getAmount());
				
				LOGGER.info("Plan Charge Fees- > " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					//fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
					fee.setFeeAmount(feeAmount);
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		}
		
		private void calculateSecurityDeposit(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
				
 	if(inspectionExtn.getBuildingType() != null)
		{
		//Building Charges = Building Charges For residential + Building Charges For Commercial 
	  if(inspectionExtn.getBuildingType().getName()!=null && inspectionExtn.getBuildingType().getName().equalsIgnoreCase("mixed residential"))
		  {
		
		   if(inspectionExtn.getBldngResidential()!=null && bpaCommonExtnService!=null)
			  {
				  LandBuildingTypesExtn residential=bpaCommonExtnService.getLandBuildingTypesExtnByDescription("Residential");
				 
				if(residential!=null)
					{
					BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
							registration.getServiceType().getId(),fee.getFeeCode(),
							(inspectionExtn.getBldngResidential() != null ? (inspectionExtn.getBldngResidential()) : null),
							BpaConstants.SANCTIONEDFEE,
							feeSubTypeAsBuilding,
							null, null, (residential!=null?residential.getName():null),null);
					//Building Charges For Residential = Rate * residential area	
					if(feeDtlBldg!=null)
						feeAmount = feeAmount.add(feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngResidential())));
					}
			  }
		  if(inspectionExtn.getBldngCommercial()!=null && bpaCommonExtnService!=null)
		  {
			  LandBuildingTypesExtn commercial=bpaCommonExtnService.getLandBuildingTypesExtnByDescription("Commercial");
			  
			  if(commercial!=null)
				{
				BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
						registration.getServiceType().getId(),fee.getFeeCode(),
						(inspectionExtn.getBldngCommercial() != null ? (inspectionExtn.getBldngCommercial()) : null),
						BpaConstants.SANCTIONEDFEE,
						feeSubTypeAsBuilding,
						null, null, (commercial!=null?commercial.getName():null),null);
				//Building Charges For commercial = Rate * commercial area
				if(feeDtlBldg!=null)
					feeAmount = feeAmount.add(feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngCommercial())));
				}
		  }
	  }else
		  {
				
				BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
						registration.getServiceType().getId(),fee.getFeeCode(),
						(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
						BpaConstants.SANCTIONEDFEE,	feeSubTypeAsBuilding,
						null, null, (inspectionExtn
						.getBuildingType() != null ? inspectionExtn
						.getBuildingType().getName() : null),null);

					if(feeDtlBldg!=null)
					feeAmount = feeAmount.add((feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngBuildUpArea()))));
			
			    
			  
		  }
		} 
				LOGGER.info("Security Deposit Fees- > " + feeAmount); 
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		}
		private void calculateWorkerWelfareFees(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			
			BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
					BpaConstants.SANCTIONEDFEE,	feeSubTypeAsBuilding,
					null, null, null,null);

				if(feeDtlBldg!=null)
				feeAmount = feeAmount.add((feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngBuildUpArea())).divide(BigDecimal.valueOf(100))));
				
				LOGGER.info("Worker Welface Fees- > " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		}
		private void calculateScrutinyFees(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			
			BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
					BpaConstants.SANCTIONEDFEE,
					feeSubTypeAsBuilding,
					null, null, null,null);

				if(feeDtlBldg!=null)
  			       feeAmount = feeAmount.add(feeDtlBldg.getAmount());
			//	feeAmount = feeAmount.add(feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngBuildUpArea())));
				
				LOGGER.info("Scrutiny Fees- > " + feeAmount);
				if(feeAmount.compareTo(BigDecimal.ZERO)>0)
				{
					//fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
					fee.setFeeAmount(feeAmount);
				}else
					fee.setFeeAmount(BigDecimal.ZERO);

		}
		private void calculateRegulationChargeForLand(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {

			
			BpaFeeDetailExtn feeDtlLand = feeExtnService.getFeeDetails(
					registration.getServiceType().getId(),fee.getFeeCode(),
					(inspectionExtn.getLndMinPlotExtent() != null ? (inspectionExtn.getLndMinPlotExtent()) : null),
					BpaConstants.SANCTIONEDFEE,
					"LAND",
					null, null, null,null);
			
			if (feeDtlLand != null)
				feeAmount = feeAmount.add(feeDtlLand.getAmount().multiply((inspectionExtn.getLndMinPlotExtent())));
			
			LOGGER.info(" Regulation Charge for land - > " + feeAmount);
			if(feeAmount.compareTo(BigDecimal.ZERO)>0)
			{
				fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
			}else {
				fee.setFeeAmount(BigDecimal.ZERO);
			}
			
		}

		private void calculateCmdaDevelopmentCharge(BpaFeeExtn fee,
				RegistrationExtn registration, InspectionExtn inspectionExtn,
				BigDecimal feeAmount) {
			 //CMDA Development Charges = Land Charges + Building Charges 
			//Land Charge 
			if(inspectionExtn!=null && inspectionExtn.getLandUsage() != null){
				BpaFeeDetailExtn feeDtlLand = feeExtnService.getFeeDetails(
						registration.getServiceType().getId(),fee.getFeeCode(),
						(inspectionExtn.getLndMinPlotExtent() != null ? (inspectionExtn.getLndMinPlotExtent()) : null),
						BpaConstants.SANCTIONEDFEE,
						"LAND",
						(inspectionExtn.getLandZoning() != null ? inspectionExtn
								.getLandZoning() : null), null, (inspectionExtn
								.getLandUsage() != null ? inspectionExtn.getLandUsage()
								.getName() : null),null);
				
				if (feeDtlLand != null)  //Land Charges = Land Charges Slab from  Master * Minimum Plot Extent
					feeAmount = feeAmount.add(feeDtlLand.getAmount().multiply((inspectionExtn.getLndMinPlotExtent())));
		
			 }
			LOGGER.info(" Cmda Development Charge with land usage - > " + feeAmount);
		//Building Charges 	
			if(inspectionExtn!=null && inspectionExtn.getBuildingType() != null)
			{
				//Building Charges = Building Charges For residential + Building Charges For Commercial 
			  if(inspectionExtn.getBuildingType().getName()!=null && inspectionExtn.getBuildingType().getName().equalsIgnoreCase("mixed residential"))
			  {
				  if(inspectionExtn.getBldngResidential()!=null && bpaCommonExtnService!=null)
				  {
					  LandBuildingTypesExtn residential=bpaCommonExtnService.getLandBuildingTypesExtnByDescription("Residential");
					 
					if(residential!=null)
						{
						BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
								registration.getServiceType().getId(),fee.getFeeCode(),
								(inspectionExtn.getBldngResidential() != null ? (inspectionExtn.getBldngResidential()) : null),
								BpaConstants.SANCTIONEDFEE,
								feeSubTypeAsBuilding,
								(inspectionExtn.getBuildingZoning() != null ? inspectionExtn
										.getBuildingZoning() : null), null, (residential!=null?residential.getName():null),null);
						//Building Charges For Residential = Rate * residential area	
						if(feeDtlBldg!=null)
							feeAmount = feeAmount.add(feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngResidential())));
						}
				  }
				  if(inspectionExtn.getBldngCommercial()!=null && bpaCommonExtnService!=null)
				  {
					  LandBuildingTypesExtn commercial=bpaCommonExtnService.getLandBuildingTypesExtnByDescription("Commercial");
					  
					  if(commercial!=null)
						{
						BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
								registration.getServiceType().getId(),fee.getFeeCode(),
								(inspectionExtn.getBldngCommercial() != null ? (inspectionExtn.getBldngCommercial()) : null),
								BpaConstants.SANCTIONEDFEE,
								feeSubTypeAsBuilding,
								(inspectionExtn.getBuildingZoning() != null ? inspectionExtn
										.getBuildingZoning() : null), null, (commercial!=null?commercial.getName():null),null);
						//Building Charges For commercial = Rate * commercial area
						if(feeDtlBldg!=null)
							feeAmount = feeAmount.add(feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngCommercial())));
						}
				  }
				  
				  
				  
			  }else
			  {  
				//Building Charges = Building Charges Slab from master * Total Buildup Area
				BpaFeeDetailExtn feeDtlBldg = feeExtnService.getFeeDetails(
						registration.getServiceType().getId(),fee.getFeeCode(),
						(inspectionExtn.getBldngBuildUpArea() != null ? (inspectionExtn.getBldngBuildUpArea()) : null),
						BpaConstants.SANCTIONEDFEE,
						feeSubTypeAsBuilding,
						(inspectionExtn.getBuildingZoning() != null ? inspectionExtn
								.getBuildingZoning() : null), null, (inspectionExtn
								.getBuildingType() != null ? inspectionExtn
								.getBuildingType().getName() : null),null);
		
				//CMDA Development Charges = Land Charges + Building Charges	
				if(feeDtlBldg!=null)
					feeAmount = feeAmount.add(feeDtlBldg.getAmount().multiply((inspectionExtn.getBldngBuildUpArea())));
					//fee.setFeeAmount(feeAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
				
			  }
			}		
			LOGGER.info(" Cmda Development Charge - > " + feeAmount);
			if(feeAmount.compareTo(BigDecimal.ZERO)>0)
			{
				fee.setFeeAmount(BigDecimal.valueOf(roundToMultipleOfHundred(feeAmount.doubleValue(),100)));
			}else
				fee.setFeeAmount(BigDecimal.ZERO);
			
		}


	private int roundToMultipleOfHundred(double number, int multiple) {

		int result = multiple;

		if (number % multiple == 0) {
			return (int) number;
		}

		// If not already multiple of given number

		if (number % multiple != 0) {

			int division = (int) ((number / multiple) + 1);

			result = division * multiple;

		}
		return result;

	}
	
}
