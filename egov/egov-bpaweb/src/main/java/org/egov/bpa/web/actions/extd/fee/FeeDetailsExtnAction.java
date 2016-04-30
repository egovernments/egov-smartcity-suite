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
package org.egov.bpa.web.actions.extd.fee;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegistrationFeeDetailExtn;
import org.egov.bpa.models.extd.RegistrationFeeExtn;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.services.extd.Fee.FeeDetailsExtnService;
import org.egov.bpa.services.extd.Fee.RegistrationFeeExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.web.actions.extd.common.BpaExtnRuleBook;
import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.workflow.GenericWorkFlowAction;
import org.egov.infra.workflow.entity.StateAware;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Results({ @Result(name = "NOACCESS", type = "stream", location = "returnStream", params = {
		"contentType", "text/plain" }) })
@Transactional(readOnly = true)
@Namespace("/fee")
@ParentPackage("egov")
public class FeeDetailsExtnAction extends GenericWorkFlowAction {

	protected Long registrationId;
	private final static Logger LOGGER = Logger
			.getLogger(FeeDetailsExtnAction.class);
	protected RegistrationExtn registrationObj;
	private InspectionExtnService inspectionExtnService;
	protected FeeExtnService feeExtnService;
	protected List<BpaFeeExtn> santionFeeList = new ArrayList();
	private Boolean isAutoCalculate;
	protected FeeDetailsExtnService feeDetailsExtnService;
	protected String mode;
	protected BpaCommonExtnService bpaCommonExtnService;
	private String returnStream = "You Have no permission to view";
	private String fromreg;
	private EgwStatus oldStatus;
	private String feeRemarks;
	private String latestFeeRemarks;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private List<BpaFeeExtn> CMDAFeeList = new ArrayList();
	private List<BpaFeeExtn> COCFeeList = new ArrayList();
	private List<BpaFeeExtn> MWGWFFeeList = new ArrayList();

	private String feeSubTypeAsBuilding = "BUILDING";

	public List<BpaFeeExtn> getCMDAFeeList() {
		return CMDAFeeList;
	}

	public void setCMDAFeeList(List<BpaFeeExtn> feeList) {
		CMDAFeeList = feeList;
	}

	public List<BpaFeeExtn> getCOCFeeList() {
		return COCFeeList;
	}

	public void setCOCFeeList(List<BpaFeeExtn> feeList) {
		COCFeeList = feeList;
	}

	public List<BpaFeeExtn> getMWGWFFeeList() {
		return MWGWFFeeList;
	}

	public void setMWGWFFeeList(List<BpaFeeExtn> feeList) {
		MWGWFFeeList = feeList;
	}

	public String getLatestFeeRemarks() {
		return latestFeeRemarks;
	}

	public void setLatestFeeRemarks(String latestFeeRemarks) {
		this.latestFeeRemarks = latestFeeRemarks;
	}

	private BigDecimal feeTotal = BigDecimal.ZERO;

	public BigDecimal getFeeTotal() {
		return feeTotal;
	}

	public void setFeeTotal(BigDecimal feeTotal) {
		this.feeTotal = feeTotal;
	}

	protected RegistrationFeeExtnService registrationFeeExtnService;

	public RegistrationFeeExtnService getRegistrationFeeExtnService() {
		return registrationFeeExtnService;
	}

	public void setRegistrationFeeExtnService(
			RegistrationFeeExtnService registrationFeeService) {
		this.registrationFeeExtnService = registrationFeeService;
	}

	public String getFeeRemarks() {
		return feeRemarks;
	}

	public void setFeeRemarks(String feeRemarks) {
		this.feeRemarks = feeRemarks;
	}

	public EgwStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(EgwStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getFromreg() {
		return fromreg;
	}

	public void setFromreg(String fromreg) {
		this.fromreg = fromreg;
	}

	public void setReturnStream(String returnStream) {
		this.returnStream = returnStream;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public InputStream getReturnStream() {
		return new ByteArrayInputStream(returnStream.getBytes());

	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public FeeDetailsExtnService getFeeDetailsExtnService() {
		return feeDetailsExtnService;
	}

	public void setFeeDetailsExtnService(FeeDetailsExtnService feeDetailsService) {
		this.feeDetailsExtnService = feeDetailsService;
	}

	public Boolean getIsAutoCalculate() {
		return isAutoCalculate;
	}

	public void setIsAutoCalculate(Boolean isAutoCalculate) {
		this.isAutoCalculate = isAutoCalculate;
	}

	public List<BpaFeeExtn> getSantionFeeList() {
		return santionFeeList;
	}

	public void setSantionFeeList(List<BpaFeeExtn> santionFeeList) {
		this.santionFeeList = santionFeeList;
	}

	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeService) {
		this.feeExtnService = feeService;
	}

	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}

	public void setInspectionExtnService(InspectionExtnService inspectionService) {
		this.inspectionExtnService = inspectionService;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public void prepare() {
		// setRegistrationId(1L);
		super.prepare();
		LOGGER.info(".......Registration Id----->" + getRegistrationId());
		if (getRegistrationId() != null) {
			registrationObj = inspectionExtnService
					.getRegistrationObjectbyId(getRegistrationId());
		}

	}

	public void validate() {
		LOGGER.debug("Start validate");
		Boolean noerror = Boolean.TRUE;
		for (BpaFeeExtn sanctionfees : getSantionFeeList()) {
			if (sanctionfees.getIsMandatory()) {
				if (sanctionfees.getFeeAmount() == null
						|| sanctionfees.getFeeAmount().equals("")
						|| sanctionfees.getFeeAmount().equals(BigDecimal.ZERO)) {
					addActionError("Fee Amount is required for "
							+ sanctionfees.getFeeDescription());
					noerror = Boolean.FALSE;
					break;
				}
			}
		}
		if (!noerror)
			preparNewForm();
		LOGGER.debug("Exit validate");
	}

	@SkipValidation
	@Action(value = "/feeDetailsExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {
		LOGGER.debug("Start newForm");
		preparNewForm();

		if (null != registrationObj
				&& registrationObj.getServiceType() != null
				&& registrationObj.getServiceType().getCode() != null
				&& (registrationObj.getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| registrationObj
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || registrationObj
						.getServiceType().getCode()
						.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {
			// setMode("readOnlyMode");
			setMode(BpaConstants.MODEVIEW);
		}
		LOGGER.debug("Exit newForm");
		return NEW;
	}

	private void preparNewForm() {
		LOGGER.debug("Start preparNewForm");
		// Getting all the sanction fees for the service type and putting inside
		// the sanctionfeelist<BpaFee> and iterating this list in the jsp.
		if (registrationObj != null) {
			santionFeeList = feeExtnService
					.getAllSanctionedFeesbyServiceType(registrationObj
							.getServiceType().getId());

			List<InspectionExtn> inspectionList = inspectionExtnService
					.getSiteInspectionListforRegistrationObject(registrationObj);
			if (inspectionList != null && inspectionList.isEmpty()) {
				addActionError("Inspection is mandatory. Please enter site inspection details before calculating Fee.");

			} else {
				for (BpaFeeExtn fe : santionFeeList) {
					fe.setFeeAmount(BigDecimal.ZERO);
				}

				if (null != registrationObj
						&& inspectionList.get(0) != null
						&& registrationObj.getServiceType() != null
						&& registrationObj.getServiceType().getCode() != null
						&& (registrationObj
								.getServiceType()
								.getCode()
								.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
								|| registrationObj
										.getServiceType()
										.getCode()
										.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || registrationObj
								.getServiceType()
								.getCode()
								.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {

					calculateFee(santionFeeList, registrationObj,
							inspectionList.get(0));
				}
			}
		}

		setFeeRemarks(BpaConstants.FeeRemarks);
		splitFeelistintosublists();
		LOGGER.debug("Exit preparNewForm");
	}

	private void calculateFee(List<BpaFeeExtn> feeList,
			RegistrationExtn registration, InspectionExtn inspectionExtn) {

		for (BpaFeeExtn feeObject : feeList) {

			// Check is required to calculate for current service type ?
			if (feeDetailsExtnService.isFeeCalculationRequiredForServiceType(
					inspectionExtn, registration, feeObject.getFeeCode(),
					registration.getServiceType().getCode())) {
				feeDetailsExtnService.calculateFeeByServiceType(feeObject,
						registration, inspectionExtn);
			}
		}
	}

	// Getting COC based fee from demand ,cmda and mwgwf from
	// registrationfeedetails table .Could have got all the fees
	// from registrationfeedetails but coouldnot as the revised fee was
	// implemented after fee and there was no registrationfeedetails table to
	// save.
	// For new records this is available
	// For those which are not there in the registrationfeedetails table the
	// only way i can show fees is by getting it from demand.

	@SkipValidation
	public void buildVieworModify(RegistrationExtn registrationObj) {
		LOGGER.debug("Start buildVieworModify");
		if (registrationObj.getEgDemand() != null
				&& registrationObj.getEgDemand().getEgDemandDetails() != null) {
			Set<EgDemandDetails> demandDetailsSet = registrationObj
					.getEgDemand().getEgDemandDetails();
			HashMap<String, BigDecimal> feecodeamountmap = new HashMap<String, BigDecimal>();
			HashMap<String, Long> feecodedemanddetailsIdmap = new HashMap<String, Long>();

			// Getting fee amounts from demand to set into the sanctionfees

			for (EgDemandDetails demandDetails : demandDetailsSet) {
				feecodeamountmap.put(demandDetails.getEgDemandReason()
						.getEgDemandReasonMaster().getCode(),
						demandDetails.getAmount());
				feecodedemanddetailsIdmap.put(demandDetails.getEgDemandReason()
						.getEgDemandReasonMaster().getCode(),
						demandDetails.getId());
			}

			// Getting fee amounts from registrationdfeedetails to set into the
			// sanctionfees
			RegistrationFeeExtn latesetregistrationFeeObj = bpaCommonExtnService
					.getLatestApprovedRegistrationFee(registrationObj);
			if (latesetregistrationFeeObj != null) {
				Set<RegistrationFeeDetailExtn> regFeeDtlSet = latesetregistrationFeeObj
						.getRegistrationFeeDetailsSet();
				for (RegistrationFeeDetailExtn regFeeDtl : regFeeDtlSet) {
					if (feecodeamountmap
							.get(regFeeDtl.getBpaFee().getFeeCode()) == null) {
						feecodeamountmap.put(
								regFeeDtl.getBpaFee().getFeeCode(),
								regFeeDtl.getAmount());
					}
				}
			}

			santionFeeList = feeExtnService
					.getAllSanctionedFeesbyServiceType(registrationObj
							.getServiceType().getId());
			for (BpaFeeExtn fees : santionFeeList) {
				if (fees.getFeeGroup().equals(BpaConstants.COCFEE)) {
					fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()) != null ? feecodeamountmap
							.get(fees.getFeeCode()) : BigDecimal.ZERO);
					fees.setDemandDetailId(feecodedemanddetailsIdmap.get(fees
							.getFeeCode()));
					feeTotal = feeTotal.add(fees.getFeeAmount());
				} else if (fees.getFeeGroup().equals(BpaConstants.CMDAFEE)) {
					fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()) != null ? feecodeamountmap
							.get(fees.getFeeCode()) : BigDecimal.ZERO);
					fees.setDemandDetailId(feecodedemanddetailsIdmap.get(fees
							.getFeeCode()));
					feeTotal = feeTotal.add(fees.getFeeAmount());
					// TODO: WE MAY NEED TO PASS DEMANDDETAILID ALONG WITH UI.
				} else if (fees.getFeeGroup().equals(BpaConstants.MWGWFFEE)) {
					fees.setFeeAmount(feecodeamountmap.get(fees.getFeeCode()) != null ? feecodeamountmap
							.get(fees.getFeeCode()) : BigDecimal.ZERO);
					fees.setDemandDetailId(feecodedemanddetailsIdmap.get(fees
							.getFeeCode()));
					feeTotal = feeTotal.add(fees.getFeeAmount());
					// TODO: WE MAY NEED TO PASS DEMANDDETAILID ALONG WITH UI.
				}
			}

			if (latesetregistrationFeeObj != null) {
				registrationObj
						.setRegistrationFeeChallanNumber(latesetregistrationFeeObj
								.getChallanNumber());

			} else
				registrationObj.setRegistrationFeeChallanNumber("NA");

			if (latesetregistrationFeeObj != null) {
				setFeeRemarks(latesetregistrationFeeObj.getFeeRemarks());
				registrationObj.setFeeDate(sdf.format(latesetregistrationFeeObj
						.getFeeDate()));
			} else {
				setFeeRemarks(registrationObj.getFeeRemarks());
				registrationObj.setFeeDate(null); // get date from demanddetl
			}
		}
		splitFeelistintosublists();
		LOGGER.debug("Exit buildVieworModify");
	}

	@Action(value = "/feeDetailsExtn-create", results = { @Result(name = NEW,type = "dispatcher") })
	public String create() {
		LOGGER.debug("Start create");
		mergeSublistintoFees();
		oldStatus = registrationObj.getEgwStatus();
		LOGGER.debug("Saving revised registration fees");
		registrationObj = feeDetailsExtnService.save(getSantionFeeList(),
				registrationObj);
		registrationObj.setFeeRemarks(getFeeRemarks());
		if (getMode() != null && getMode().equalsIgnoreCase("modify")) {
			RegistrationFeeExtn registrationFee = registrationFeeExtnService
					.getNonRevisedRegistrationFees(registrationObj.getId());
			if (registrationFee != null) {
				if (!registrationFee.getRegistrationFeeDetailsSet().isEmpty()) {
					modifyRegistrationFeeDetails(registrationFee);
				} else {
					LOGGER.debug("Saving fees in registration fee table");
					registrationObj = registrationFeeExtnService
							.saveFeesinRegistrationFee(registrationObj,
									getSantionFeeList());
				}
			}
		} else {
			LOGGER.debug("Saving fees in registration fee table");
			registrationFeeExtnService.saveFeesinRegistrationFee(
					registrationObj, getSantionFeeList());
		}
		registrationObj.setEgwStatus(bpaCommonExtnService
				.getstatusbyCode(BpaConstants.FEESCREATED));
		registrationObj.setIsSanctionFeeRaised(Boolean.TRUE);

		registrationObj.setFeeDate(sdf.format(new Date()));
		buildVieworModify(registrationObj);
		setMode(BpaConstants.MODEVIEW);
		addActionMessage("Fee Details Saved Successfully");

		bpaCommonExtnService.createStatusChange(registrationObj, oldStatus);
		LOGGER.debug("Exit create");
		return NEW;
	}

	protected void modifyRegistrationFeeDetails(
			RegistrationFeeExtn registrationFee) {
		LOGGER.debug("Start modifyRegistrationFeeDetails");

		List<RegistrationFeeDetailExtn> regFeeList = Arrays
				.asList(registrationFee.getRegistrationFeeDetailsSet().toArray(
						new RegistrationFeeDetailExtn[registrationFee
								.getRegistrationFeeDetailsSet().size()]));
		int index = -1;
		for (int i = 0; i < regFeeList.size(); i++) {
			if (index != -1)
				getSantionFeeList().remove(index);
			for (int j = 0; j < getSantionFeeList().size(); j++) {

				if (regFeeList.get(i).getBpaFee().getId()
						.equals(getSantionFeeList().get(j).getId())) {
					regFeeList
							.get(i)
							.setAmount(
									(getSantionFeeList().get(j).getFeeAmount() != null && !""
											.equals(getSantionFeeList().get(j)
													.getFeeAmount())) ? getSantionFeeList()
											.get(j).getFeeAmount()
											: BigDecimal.ZERO);
					index = j;
					break;
				}
			}
		}
		LOGGER.debug("Exit modifyRegistrationFeeDetails");
	}

	@SkipValidation
	@Action(value = "/feeDetailsExtn-view", results = { @Result(name = NEW,type = "dispatcher") })
	public String view() {
		LOGGER.debug("Start view");

		buildVieworModify(registrationObj);
		setMode(BpaConstants.MODEVIEW);
		LOGGER.debug("Exit view");
		return NEW;
	}

	@SkipValidation
	@Action(value = "/feeDetailsExtn-modify", results = { @Result(name = NEW,type = "dispatcher") })
	public String modify() {
		LOGGER.debug("Start modify");

		buildVieworModify(registrationObj);

		if (null != registrationObj
				&& registrationObj.getServiceType() != null
				&& registrationObj.getServiceType().getCode() != null
				&& (registrationObj.getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| registrationObj
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || registrationObj
						.getServiceType().getCode()
						.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {
			// setMode("readOnlyMode");
			setMode(BpaConstants.MODEVIEW);
		} else {
			setMode("modify");
		}
		LOGGER.debug("Exit modify");
		return NEW;
	}

	@SkipValidation
	@Action(value = "/feeDetailsExtn-showFeeDetailsinRegistration", results = { @Result(name = "feeDetailsRegistration",type = "dispatcher") })
	public String showFeeDetailsinRegistration() {
		LOGGER.debug("Start showFeeDetailsinRegistration");
		if (EgovThreadLocals.getUserId() != null) {
			List<String> roleList = bpaCommonExtnService
					.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
			if (!BpaExtnRuleBook.getInstance().checkViewsforRoles(roleList,
					BpaConstants.FEEDETAILS)) {
				returnStream = returnStream.concat(" Fee Details");
				return BpaConstants.NOACCESS;
			}
		}

		if (registrationObj != null)
			buildVieworModify(registrationObj);
		setMode(BpaConstants.MODEVIEW);
		LOGGER.debug("Exit showFeeDetailsinRegistration");
		return "feeDetailsRegistration";
	}

	@Action(value = "/feeDetailsExtn-showFeeDetailsinRevisedFee", results = { @Result(name = "feeDetailsRevisedFee",type = "dispatcher") })
	public String showFeeDetailsinRevisedFee() {
		showFeeDetailsinRegistration();
		return "feeDetailsRevisedFee";
	}

	@Override
	public StateAware getModel() {

		return null;
	}

	public RegistrationExtn getRegistrationObj() {
		return registrationObj;
	}

	public void splitFeelistintosublists() {
		LOGGER.debug("Start splitFeelistintosublists");
		CMDAFeeList.clear();
		COCFeeList.clear();
		MWGWFFeeList.clear();
		for (BpaFeeExtn fees : santionFeeList) {

			if (fees.getFeeGroup() != null) {
				if (fees.getFeeGroup().equals(BpaConstants.CMDAFEE)) {
					CMDAFeeList.add(fees);
				} else if (fees.getFeeGroup().equals(BpaConstants.COCFEE)) {
					COCFeeList.add(fees);
				} else if (fees.getFeeGroup().equals(BpaConstants.MWGWFFEE)) {
					MWGWFFeeList.add(fees);
				}
			}
		}

		LOGGER.debug("Exit splitFeelistintosublists");
	}

	public void mergeSublistintoFees() {
		LOGGER.debug("Start mergeSublistintoFees");
		for (BpaFeeExtn fees : CMDAFeeList) {
			santionFeeList.add(fees);
		}
		for (BpaFeeExtn fees : COCFeeList) {
			santionFeeList.add(fees);
		}
		for (BpaFeeExtn fees : MWGWFFeeList) {
			santionFeeList.add(fees);
		}
		LOGGER.debug("Exit mergeSublistintoFees");
	}
}
