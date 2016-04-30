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
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.BpaPimsInternalExtnServiceFactory;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*import org.egov.infstr.workflow.WorkflowService;*/

@Transactional(readOnly = true)
public class RegistrationFeeExtnService extends
		PersistenceService<RegistrationFeeExtn, Long> {

	private PersistenceService persistenceService;
	// private WorkflowService <RegistrationFeeExtn>
	// registrationFeeWorkflowExtnService;
	private BpaPimsInternalExtnServiceFactory bpaPimsExtnFactory;
	private Long approverId;
	private BpaCommonExtnService bpaCommonExtnService;
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private Date feeDate = new Date();
	private final static Logger LOGGER = Logger
			.getLogger(RegistrationFeeExtnService.class);

	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public BpaPimsInternalExtnServiceFactory getBpaPimsExtnFactory() {
		return bpaPimsExtnFactory;
	}

	public void setBpaPimsExtnFactory(
			BpaPimsInternalExtnServiceFactory bpaPimsFactory) {
		this.bpaPimsExtnFactory = bpaPimsFactory;
	}

	/*
	 * public WorkflowService<RegistrationFeeExtn>
	 * getRegistrationFeeWorkflowExtnService() { return
	 * registrationFeeWorkflowExtnService; }
	 * 
	 * public void setRegistrationFeeWorkflowExtnService(
	 * WorkflowService<RegistrationFeeExtn> registrationFeeWorkflowService) {
	 * this.registrationFeeWorkflowExtnService = registrationFeeWorkflowService;
	 * }
	 */
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public RegistrationExtn saveLegecyFeesinRegistrationFee(
			RegistrationExtn registrationObj, List<BpaFeeExtn> santionFeeList,
			Date feeDate) {

		this.feeDate = feeDate;
		LOGGER.debug("Saving legacy feedetails in RegistrationFee");
		return saveFeesinRegistrationFee(registrationObj, santionFeeList);
	}

	@Transactional
	public RegistrationExtn saveFeesinRegistrationFee(
			RegistrationExtn registrationObj, List<BpaFeeExtn> santionFeeList) {
		LOGGER.debug("Enter saveFeesinRegistrationFee");
		RegistrationFeeExtn registrationFee = new RegistrationFeeExtn();
		if (feeDate != null)
			registrationFee.setFeeDate(new Date());
		else
			registrationFee.setFeeDate(feeDate);
		registrationFee.setEgwStatus(bpaCommonExtnService.getstatusbyCode(
				BpaConstants.BPAREGISTRATIONFEEMODULESTATUSAPPROVED,
				BpaConstants.BPAREGISTRATIONFEEMODULE));
		Boundary zone = (Boundary) bpaCommonExtnService
				.getZoneNameFromAdminboundaryid(registrationObj
						.getAdminboundaryid());
		registrationFee.setChallanNumber(bpaNumberGenerationExtnService
				.generateChallanNumberFormat(zone));
		registrationFee.setRegistration(registrationObj);
		registrationFee.setFeeRemarks(registrationObj.getFeeRemarks());
		registrationFee.setIsRevised(Boolean.FALSE);
		Set<RegistrationFeeDetailExtn> registrationFeeDetailsSet = new HashSet<RegistrationFeeDetailExtn>();

		for (BpaFeeExtn fee : santionFeeList) {
			RegistrationFeeDetailExtn registrationFeeDetail = new RegistrationFeeDetailExtn();
			registrationFeeDetail.setBpaFee(fee);
			registrationFeeDetail.setAmount((fee.getFeeAmount() != null && !""
					.equals(fee.getFeeAmount())) ? fee.getFeeAmount()
					: BigDecimal.ZERO);
			registrationFeeDetail.setRegistrationFee(registrationFee);
			registrationFeeDetailsSet.add(registrationFeeDetail);
		}

		registrationFee.setRegistrationFeeDetailsSet(registrationFeeDetailsSet);
		persistenceService.getSession().clear();
		persist(registrationFee);
		LOGGER.debug("Saved RegistrationFee");

		registrationObj.setRegistrationFeeChallanNumber(registrationFee
				.getChallanNumber());
		LOGGER.debug("Exit saveFeesinRegistrationFee");
		return registrationObj;
	}

	public RegistrationFeeExtn getNonRevisedRegistrationFees(Long id) {
		LOGGER.debug("Enter getNonRevisedRegistrationFees");
		Criteria feeCrit = persistenceService.getSession().createCriteria(
				RegistrationFeeExtn.class);
		feeCrit.add(Restrictions.eq("registration.id", id));
		feeCrit.add(Restrictions.eq("isRevised", Boolean.FALSE));
		LOGGER.debug("Exit getNonRevisedRegistrationFees");
		return (RegistrationFeeExtn) feeCrit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<RegistrationFeeExtn> getPriorFeeDetailsExcludingCurrentRegFeeId(
			Long registrationId, Long registrationFeeId) {
		LOGGER.debug("Enter getPriorFeeDetailsExcludingCurrentRegFeeId");
		Criteria feeCrit = persistenceService.getSession()
				.createCriteria(RegistrationFeeExtn.class)
				.createAlias("egwStatus", "status");
		feeCrit.add(Restrictions.eq("registration.id", registrationId));
		if (registrationFeeId != null)
			feeCrit.add(Restrictions.ne("id", registrationFeeId));
		feeCrit.add(Restrictions.ne("status.code", "Cancelled"));
		feeCrit.addOrder(Order.desc("id"));
		LOGGER.debug("Exit getPriorFeeDetailsExcludingCurrentRegFeeId");
		return feeCrit.list();

	}

	@SuppressWarnings("unchecked")
	public List<RegistrationFeeExtn> getpreviousFeeDetails(Long registrationId) {

		LOGGER.debug("Enter getpreviousFeeDetails");

		Criteria feeCrit = persistenceService.getSession()
				.createCriteria(RegistrationFeeExtn.class)
				.createAlias("egwStatus", "status");
		feeCrit.add(Restrictions.eq("registration.id", registrationId));
		feeCrit.add(Restrictions.eq("status.code", "Approved"));
		feeCrit.addOrder(Order.asc("feeRemarks"));
		LOGGER.debug("Exit getpreviousFeeDetails");
		return feeCrit.list();

	}

	public Integer getRevisedApprovedRegistrationFeeSize(Long registrationId) {
		LOGGER.debug("Enter getRevisedApprovedRegistrationFeeSize");
		Criteria feeCrit = persistenceService.getSession()
				.createCriteria(RegistrationFeeExtn.class)
				.createAlias("egwStatus", "status");
		feeCrit.add(Restrictions.eq("registration.id", registrationId));
		feeCrit.add(Restrictions.eq("status.code", "Approved"));
		feeCrit.add(Restrictions.eq("isRevised", Boolean.TRUE));
		LOGGER.debug("Exit getRevisedApprovedRegistrationFeeSize");
		return feeCrit.list().size();
	}

	public List<RegistrationFeeDetailExtn> getRegistrationFeeDetails(
			Long registrationFeeId) {
		LOGGER.debug("Enter getRegistrationFeeDetails in registrationfeeservice ");
		if (registrationFeeId != null) {
			Criteria feedtlCrit = persistenceService.getSession()
					.createCriteria(RegistrationFeeDetailExtn.class);
			feedtlCrit.add(Restrictions.eq("registrationFee.id",
					registrationFeeId));
			LOGGER.debug("Exit getRegistrationFeeDetails inside registrationfeeservice");
			return feedtlCrit.list();
		}
		return Collections.EMPTY_LIST;

	}

	public RegistrationFeeExtn getRegistrationFeeById(Long registrationFeeId) {

		return findById(registrationFeeId);
	}

	public Integer getAlreadyCreatedRegistrationFee(Long registrationId) {
		LOGGER.debug("Enter getAlreadyCreatedRegistrationFee");
		Criteria feeCrit = persistenceService.getSession()
				.createCriteria(RegistrationFeeExtn.class)
				.createAlias("egwStatus", "status");
		feeCrit.add(Restrictions.eq("registration.id", registrationId));
		String[] statusarr = new String[2];
		statusarr[0] = "Created";
		statusarr[1] = "Rejected";
		feeCrit.add(Restrictions.in("status.code", statusarr));
		feeCrit.add(Restrictions.eq("isRevised", Boolean.TRUE));
		LOGGER.debug("Exit getAlreadyCreatedRegistrationFee");
		return feeCrit.list().size();
	}

	@Transactional
	public RegistrationFeeExtn saveRegistrationFee(
			RegistrationFeeExtn registrationFee,
			List<RegistrationFeeDetailExtn> newfeeDetailsList,
			String workflowaction, String approvercomments) {

		LOGGER.debug("Enter saveRegistrationFee");

		registrationFee = buildFeeDetails(registrationFee, newfeeDetailsList);

		if (registrationFee.getApproverPositionId() != null
				&& registrationFee.getApproverPositionId() != -1) {
			approverId = registrationFee.getApproverPositionId();
		}
		if (registrationFee.getId() == null) {

			registrationFee.setEgwStatus(bpaCommonExtnService.getstatusbyCode(
					"Created", BpaConstants.BPAREGISTRATIONFEEMODULE));
			registrationFee = persist(registrationFee);
		} else {

			registrationFee = merge(registrationFee);
		}
		if (approverId != null)
			registrationFee.setApproverPositionId(approverId);

		registrationFee = createWorkflow(registrationFee, workflowaction,
				approvercomments);
		LOGGER.debug("Exit saveRegistrationFee");
		return registrationFee;

	}

	private RegistrationFeeExtn createWorkflow(
			RegistrationFeeExtn registrationFee, String workFlowAction,
			String approverComments) {
		LOGGER.debug("Enter createWorkflow");
		try {
			if (registrationFee.getState() == null) {
				Position pos = bpaPimsExtnFactory
						.getPositionByUserId((EgovThreadLocals.getUserId()));
				// registrationFee = (RegistrationFeeExtn)
				// registrationFeeWorkflowExtnService.start(registrationFee,
				// pos, "Revised Fee created.");
			}

			if (workFlowAction != null
					&& !"".equals(workFlowAction)
					&& !BpaConstants.SCRIPT_SAVE
							.equalsIgnoreCase(workFlowAction)) {
				String comments = (approverComments == null || ""
						.equals(approverComments.trim())) ? ""
						: approverComments;

				/*
				 * In case of rejection, get previous state of record from
				 * matrix table. Search previous state owner from workflow state
				 * (registration.getHistory()).
				 */
				if (BpaConstants.SCRIPT_REJECT.equalsIgnoreCase(workFlowAction)) {
					LOGGER.debug("started  reject  ");
					WorkFlowMatrix wfMatrix = bpaCommonExtnService
							.getPreviousStateFromWfMatrix(registrationFee
									.getStateType(), null, null, null,
									registrationFee.getCurrentState()
											.getValue(), registrationFee
											.getCurrentState().getNextAction());
					if (wfMatrix != null) {
						registrationFee.setPreviousObjectState(wfMatrix
								.getCurrentState());
						registrationFee.setPreviousObjectAction(wfMatrix
								.getPendingActions());

						/*
						 * for(State state: registrationFee.getHistory()) { if
						 * (state
						 * .getValue().equalsIgnoreCase(wfMatrix.getCurrentState
						 * ())) {
						 * registrationFee.setPreviousStateOwnerId(state.getOwner
						 * ().getId()); break; } }
						 */
					}

				}
				LOGGER.debug("starting  workflowtransition  ");
				/*
				 * bpaCommonExtnService.workFlowTransition(registrationFee,
				 * workFlowAction, comments);
				 */

			}
		} catch (ValidationException ex) {

			ex.printStackTrace();
			throw ex;
		} catch (Exception ex) {

			ex.printStackTrace();
			throw new EGOVRuntimeException(ex.getMessage()
					+ "--Error in create Workflow");
		}
		LOGGER.debug("Exit createWorkflow");
		return registrationFee;
	}

	private RegistrationFeeExtn buildFeeDetails(
			RegistrationFeeExtn registrationFee,
			List<RegistrationFeeDetailExtn> regFeeDtlList) {
		LOGGER.debug("Enter buildFeeDetails");
		Set<RegistrationFeeDetailExtn> regFeeDetailSet = new HashSet<RegistrationFeeDetailExtn>();
		for (RegistrationFeeDetailExtn feedetail : regFeeDtlList) {
			feedetail.setRegistrationFee(registrationFee);
			regFeeDetailSet.add(feedetail);
		}
		registrationFee.getRegistrationFeeDetailsSet().clear();
		registrationFee.getRegistrationFeeDetailsSet().addAll(regFeeDetailSet);
		LOGGER.debug("Exit buildFeeDetails");
		return registrationFee;
	}

	public List<EgwSatuschange> getFileConsiderationCheckedDate(
			RegistrationExtn registration) {
		// EgwStatus fileConsiderationCheckedStatus=
		// (EgwStatus)persistenceService.find("from EgwStatus where moduletype=? and code=?",BpaConstants.REGISTRATIONMODULE,BpaConstants.FILECONSIDERATIONCHECKED);
		LOGGER.debug("Enter getFileConsiderationCheckedDate");
		EgwStatus fileConsiderationCheckedStatus = bpaCommonExtnService
				.getstatusbyCode(BpaConstants.FILECONSIDERATIONCHECKED,
						BpaConstants.REGISTRATIONMODULE);
		if (fileConsiderationCheckedStatus != null) {
			Criteria statuschangeCriteria = persistenceService.getSession()
					.createCriteria(EgwSatuschange.class, "statuschange");
			statuschangeCriteria.add(Restrictions.eq("moduleid",
					(registration.getId().intValue())));
			statuschangeCriteria.add(Restrictions.eq("moduletype",
					BpaConstants.REGISTRATIONMODULE));
			statuschangeCriteria.add(Restrictions.eq("tostatus",
					fileConsiderationCheckedStatus.getId()));
			statuschangeCriteria.addOrder(Order.desc("id"));
			LOGGER.debug("Exit getFileConsiderationCheckedDate");
			return statuschangeCriteria.list();
		}
		return null;
	}

}
