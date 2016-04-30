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
package org.egov.bpa.services.extd.bill;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.services.extd.common.BpaCitizenPortalExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.BpaSurvayorPortalExtnService;
import org.egov.bpa.services.extd.common.UtilsExtnService;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.ReceiptAccountInfo;
import org.egov.collection.integration.services.BillingIntegrationService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.commons.EgwStatus;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.integration.TaxCollection;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.bpa.constants.BpaConstants.BPAMODULENAME;

/*import org.egov.infstr.commons.dao.GenericDaoFactory;*/
/*import org.egov.infstr.workflow.WorkflowService;*/
/*import org.egov.portal.surveyor.model.Surveyor;*/

@SuppressWarnings("unchecked")
public class BpaFeeCollectionExtn extends TaxCollection {
	private static final Logger LOGGER = Logger.getLogger(BpaFeeCollectionExtn.class);
	private PersistenceService persistenceService;
	private BpaCommonExtnService bpaCommonService;
	private BpaNumberGenerationExtnService bpaNumberGenerationService;
	private CollectionCommon collectionCommon;
//	private WorkflowService <RegistrationExtn> registrationWorkflowExtnService;
	private BpaCitizenPortalExtnService bpaCitizenPortalService;
	private UtilsExtnService utilsExtnService;
	private BigDecimal receiptTotalAmount = BigDecimal.ZERO;
	private BpaSurvayorPortalExtnService bpaSurvayorPortalExtnService;
	@Autowired
	@Qualifier(value = "egBillDAO")
	private EgBillDao egBillDao;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleService moduleDao;
	@Override
	public void updateDemandDetails(BillReceiptInfo billRcptInfo) {
		LOGGER.debug("updateDemandDetails : Updating Demand Details Started, billRcptInfo : " + billRcptInfo);
		receiptTotalAmount = billRcptInfo.getTotalAmount();
		EgDemand demand = (EgDemand) getDemandFromBillNo(Integer.valueOf(billRcptInfo.getBillReferenceNum()));
		LOGGER.info("updateDemandDetails : collection back update started for Extended BPA : "
				//+ ((BillReceiptInfoImpl) billRcptInfo).getReceiptMisc().getReceiptHeader().getConsumerCode()
				+ " and receipt event is " + billRcptInfo.getEvent() + ". Total Receipt amount is."
				+ receiptTotalAmount + " with receipt no." + billRcptInfo.getReceiptNum());
		if (billRcptInfo.getEvent().equals(EVENT_INSTRUMENT_BOUNCED)) {
			updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());

		} else if (billRcptInfo.getEvent().equals(EVENT_RECEIPT_CREATED)) {
			BigDecimal finalFeeCollected = updateDemandWithcollectdTaxDetails(demand, billRcptInfo,
					EVENT_RECEIPT_CREATED);

			if (demand != null) {
				RegistrationExtn registration = (RegistrationExtn) persistenceService.find(
						"from RegistrationExtn regn where regn.egDemand is not null and regn.egDemand.id=?",
						demand.getId());

				if (registration != null
						&& registration.getEgwStatus() != null
						&& registration.getEgwStatus().getCode()
								.equalsIgnoreCase(BpaConstants.CITIZENAPPLICATIONREGISTERED)) {
					if(registration.getServiceType() != null
							&& registration.getServiceType().getCode() != null
							&& (registration.getServiceType().getCode().equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
									|| registration.getServiceType().getCode().equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || 
									registration.getServiceType().getCode().equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))){
					registration.setEgwStatus(bpaCommonService.getstatusbyCode(BpaConstants.APPLICATION_FWDED_TO_LS));
					//createSurveyorEntry(registration); TODO Phionix
					}
					else
					{
						registration.setEgwStatus(bpaCommonService.getstatusbyCode(BpaConstants.APPLICATIONREGISTERED));
						registration.setApproverPositionId(registration.getApproverId());
						if(registration.getState()==null && registration.getApproverPositionId()!=null && registration.getRequest_number()!=null && !"".equals(registration.getRequest_number()))
						{
							 Position position=(Position) persistenceService.find("from Position where id=?",registration.getApproverPositionId());
/*							 if(position!=null) 
							 {
						 	 registration = (RegistrationExtn)registrationWorkflowExtnService.start(registration, position, "Bpa Registration created.");
							 }
*/						}//todo phionix
						//bpaCommonService.workFlowTransition(registration, BpaConstants.FORWARDWORKFLOWSTATUS,  BpaConstants.MSG_ADMINISSIONFEECOLLECTED);
						//bpaCitizenPortalService.updateServiceRequestRegistry(registration);
					}
					buildEmailWithReceiptAsAttachment(billRcptInfo, registration, "save");
					bpaCommonService.buildSMS(registration, "save");
				}
				if (registration.getEgwStatus().getCode().equalsIgnoreCase(BpaConstants.CHALLANNOTICESENT)) {

					if (registration.getBaNum() == null || "".equals(registration.getBaNum()))
						registration.setBaNum(bpaNumberGenerationService.generateOrderNumber(registration));
					if (registration.getPlanPermitApprovalNum() == null
							|| "".equals(registration.getPlanPermitApprovalNum())) {
						if (registration.getServiceType() != null && !registration.getServiceType().getIsCmdaType())
							registration.setPlanPermitApprovalNum(registration.getPlanSubmissionNum());
					}
					registration.setBaOrderDate(new Date());

					EgwStatus oldStatus = registration.getEgwStatus();
					registration.setEgwStatus(bpaCommonService.getstatusbyCode(BpaConstants.CHALLANAMOUNTCOLLECTED));
					bpaCommonService.createStatusChange(registration, oldStatus);
		//TODO Phionx DMS relate		//	bpaCommonService.createNotificationFinalFeeCollected(registration, finalFeeCollected);
					buildEmailWithReceiptAsAttachment(billRcptInfo, registration, "FeePaid");
					bpaCommonService.buildSMS(registration, "FeePaid");
					//bpaCitizenPortalService.updateServiceRequestRegistry(registration);// Update citizen status 
				}
			}

		} else if (billRcptInfo.getEvent().equals(BillingIntegrationService.EVENT_RECEIPT_CANCELLED)) {
			updateDemandWithcollectdTaxDetails(demand, billRcptInfo, BillingIntegrationService.EVENT_RECEIPT_CANCELLED);
			updateReceiptStatusWhenCancelled(billRcptInfo.getReceiptNum());
		}

		LOGGER.info("Demand after updateDemandDetails() processing: " + demand);
	}

	private void buildEmailWithReceiptAsAttachment(BillReceiptInfo bri, RegistrationExtn registration,
			String messageType) {
		InputStream inputStream = null;
		List<Map<String, Object>> finalAttachmentList = new ArrayList<Map<String, Object>>();
		HashMap<String, Object> attachmentList = new HashMap<String, Object>();
		HashMap<String, Object> attachmentFileNames = new HashMap<String, Object>();

		if (bri != null && bri.getReceiptNum() != null && registration != null && registration.getId() != null)
			inputStream =null;/* collectionCommon.getInputStreamByReciptNumberServiceAndConsumerCode(EXTD_SERVICE_CODE,
					bri.getReceiptNum(), registration.getId().toString(), true);*/

		if (inputStream != null) {
			attachmentList.put("feePayment", inputStream);
			attachmentFileNames.put("feePaymentFileName", "FeePaidDetails.pdf");
			finalAttachmentList.add(attachmentList);
			finalAttachmentList.add(attachmentFileNames);

			bpaCommonService.buildEmail(registration, messageType, finalAttachmentList);

		} else {
			bpaCommonService.buildEmail(registration, messageType, null);
		}
	}

	/*
	 * On create of receipt, update concerned demand detail by using demand
	 * reason.
	 */

	private BigDecimal updateDemandWithcollectdTaxDetails(EgDemand demand, BillReceiptInfo billReceiptInfo,
			String eventType) {

		BigDecimal totalAmountCollected = BigDecimal.ZERO;

		for (ReceiptAccountInfo recAccInfo : billReceiptInfo.getAccountDetails()) {
			String demandMasterReasonDesc = null;
			if (recAccInfo.getDescription() != null) {
				demandMasterReasonDesc = recAccInfo.getDescription()
						.substring(0, recAccInfo.getDescription().indexOf(BpaConstants.COLL_RECEIPTDETAIL_DESC_PREFIX))
						.trim();
				if (eventType.equals(EVENT_RECEIPT_CREATED))
					totalAmountCollected = totalAmountCollected.add(createOrUpdateDemandDetails(demandMasterReasonDesc,
							demand, billReceiptInfo, recAccInfo));
			}
		}
		LOGGER.info("Demand before updateDemandDetails() processing: " + demand.getAmtCollected() + demand);

		if (eventType.equals(EVENT_RECEIPT_CANCELLED))
			demand.setAmtCollected(demand.getAmtCollected().subtract(totalAmountCollected));
		demand.setModifiedDate(new Date());
		return totalAmountCollected;
	}

	private BigDecimal createOrUpdateDemandDetails(String demandMasterReasonDesc, EgDemand demand,
			BillReceiptInfo billReceiptInfo, ReceiptAccountInfo recAccInfo) {
		BigDecimal totalAmountCollected = BigDecimal.ZERO;

		if (recAccInfo.getCrAmount() != null && recAccInfo.getCrAmount().compareTo(BigDecimal.ZERO) > 0) {
			// updating the existing demand detail..
			for (EgDemandDetails demandDetail : demand.getEgDemandDetails()) {

				if ((demandDetail.getEgDemandReason() != null
						&& demandDetail.getEgDemandReason().getEgDemandReasonMaster() != null && demandDetail
						.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().trim()
						.equalsIgnoreCase(demandMasterReasonDesc))
						&& (demandDetail.getAmount().compareTo(BigDecimal.ZERO) > 0)) {
					demandDetail.addCollected(recAccInfo.getCrAmount());

					/*
					 * Save bill detail and demand deatail relation in the
					 * intermediate table.
					 */
					persistCollectedReceipts(demandDetail, billReceiptInfo.getReceiptNum(), receiptTotalAmount,
							billReceiptInfo.getReceiptDate().toDate(), recAccInfo.getCrAmount());
					demand.setAmtCollected(demand.getAmtCollected().add(recAccInfo.getCrAmount()));
					totalAmountCollected = totalAmountCollected.add(recAccInfo.getCrAmount());
				}
				demandDetail.setModifiedDate(new Date());
			}
			demand.setModifiedDate(new Date());
		}
		return totalAmountCollected;
	}

	private EgDemand getDemandFromBillNo(Integer billId) {
		EgDemand egDemand = null;
		if (billId != null) {
			EgBill egBill = (EgBill) egBillDao.findById(billId, false);
			if (egBill != null) {
				egDemand = egBill.getEgDemand();
			}
		}
		return egDemand;
	}

/*	private void createSurveyorEntry(RegistrationExtn registration) {
		Surveyor surveyor=null;
		
		if(registration.getSurveyorName()!=null)
		{
		 surveyor=registration.getSurveyorName();
		}
		else{
		surveyor = utilsExtnService.getSurveyorForBpa(registration);
		}
		LOGGER.debug("createSurveyorObject Chosen surveyor for BPA registartion " + registration.getPlanSubmissionNum()
				+ " is " + surveyor.getName());
		bpaSurvayorPortalExtnService.createServiceRequestRegistry(registration, APPLICATION_FWDED_TO_LS, registration
				.getOwner().getFirstName(), surveyor);
		bpaCitizenPortalService.updateServiceRequestRegistry(registration);
	}*/

	@Override
	protected Module module() {
		Module module = moduleDao.getModuleByName(BPAMODULENAME);
		 return  module;
		
	}

	public void setCollectionCommon(CollectionCommon collectionCommon) {
		this.collectionCommon = collectionCommon;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setBpaCommonService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonService = bpaCommonService;
	}

	public void setBpaNumberGenerationService(BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationService = bpaNumberGenerationService;
	}

	public void setUtilsExtnService(UtilsExtnService utilsExtnService) {
		this.utilsExtnService = utilsExtnService;
	}

	public void setBpaCitizenPortalService(BpaCitizenPortalExtnService bpaCitizenPortalService) {
		this.bpaCitizenPortalService = bpaCitizenPortalService;
	}

	public void setBpaSurvayorPortalExtnService(BpaSurvayorPortalExtnService bpaSurvayorPortalExtnService) {
		this.bpaSurvayorPortalExtnService = bpaSurvayorPortalExtnService;
	}

	/*public void setRegistrationWorkflowExtnService(
			WorkflowService<RegistrationExtn> registrationWorkflowExtnService) {
		this.registrationWorkflowExtnService = registrationWorkflowExtnService;
	}*/
//TODO Phionix

	public void updateRectifiedReceipt(BillReceiptInfo rectifiedReceiptInfo) {

	}


}
