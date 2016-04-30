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
package org.egov.bpa.web.actions.extd.lettertoparty;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.CMDALetterToPartyExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.services.extd.lettertoparty.LetterToPartyExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional(readOnly = true)
@Namespace("/lettertoparty")
@ParentPackage("egov")
public class LpReplyCmdaExtnAction extends BaseFormAction {

	private RegistrationExtn registration;
	private static final Logger LOGGER = Logger
			.getLogger(LpReplyCmdaExtnAction.class);
	private CMDALetterToPartyExtn letterParty;
	private Long registrationId;
	private String requestID;
	private CMDALetterToPartyExtn letterPartyReply = new CMDALetterToPartyExtn();
	private LetterToPartyExtnService letterToPartyExtnService;
	private InspectionExtnService inspectionExtnService;
	private BpaCommonExtnService bpaCommonExtnService;
	private String existLpReason;
	private String existLpRemarks;
	private User loginUser;
	private Long serviceTypeId;
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	private String mode;
	private ReportService reportService;
	private Integer reportId = -1;
	// private EisManager eisManager;
	private String documentNum;
	private String existLpNum;
	private String fromAddressToLp;
	private RegisterBpaExtnService registerBpaExtnService;
	private Long letterToPartyId;

	public void prepare() {
		super.prepare();
		loginUser = inspectionExtnService.getUserbyId((EgovThreadLocals
				.getUserId()));

		if (registration != null && registration.getRequest_number() != null) {
			registration = registerBpaExtnService
					.getRegistrationByPassingRequestNumber(registration
							.getRequest_number());
		} else if (!"".equals(getRequestID()) && getRequestID() != null) {
			registration = registerBpaExtnService
					.getRegistrationByPassingRequestNumber(getRequestID());
		}

		if (!"".equals(getRequestID()) && getRequestID() != null
				&& registration != null) {
			registration = letterToPartyExtnService
					.getRegistrationObjectbyId(registration.getId());
			// letterParty =
			// letterToPartyExtnService.getLatestCMDALetterToPartyForRegObj(registration);
			letterParty = letterToPartyExtnService
					.getLatestCMDALetterToPartyForRegObj(registration);
			if (letterParty != null) {
				getLetterToPartyDetails(letterParty);
				// showCheckList();
			}
		}
		if (getRegistrationId() != null) {
			registration = letterToPartyExtnService
					.getRegistrationObjectbyId(getRegistrationId());
			// letterParty =
			// letterToPartyExtnService.getLatestCMDALetterToPartyForRegObj(registration);
			letterParty = letterToPartyExtnService
					.getLatestCMDALetterToPartyForRegObj(registration);
			if (letterParty != null) {
				getLetterToPartyDetails(letterParty);
			}
		}
		if (letterParty != null && letterParty.getDocumentid() != null)
			this.setDocumentNum(letterParty.getDocumentid());
	}

	@SkipValidation
	@Action(value = "/lpReplyCmdaExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {

		if (letterParty == null
				|| (letterParty != null && letterParty.getIsHistory() != null && letterParty
						.getIsHistory().equals('Y'))) {

			if (letterParty == null) {

				addActionMessage(getMessage("lpreply.noLpresent.validate"));
			} else if (letterParty.getIsHistory().equals('Y')) {
				addActionMessage(getMessage("lpreply.Lpreceived.validate"));
				setMode(BpaConstants.MODEVIEW);
			}

			return NEW;
		} else if (letterParty.getRegistration() != null
				&& letterParty.getRegistration().getEgwStatus() != null
				&& letterParty.getRegistration().getEgwStatus().getCode() != null
				&& letterParty.getRegistration().getEgwStatus().getCode()
						.equals(BpaConstants.CMDALETTERTOPARTYSENT)) {
			// letterParty.getRegistration().getEgwStatus().getCode().equals(BpaConstants.CMDALETTERTOPARTYSENT)

			/*
			 * List<LetterToPartyExtn> lpParty=letterToPartyExtnService.
			 * getLetterToPartyForRegnByComparingLPSentDateWithSysDateToTenDays
			 * (registration, letterParty.getLetterToParty().getSentDate());
			 * if(lpParty.size()>0) {
			 * addActionMessage(getMessage("lpreply.actionvalidate.message"));
			 * setMode(BpaConstants.MODEVIEW); } else {
			 */
			getLetterToPartyDetails(letterParty);
			setMode("NEW");
			return NEW;
			// }
		} else if (letterParty != null
				&& letterParty.getRegistration().getEgwStatus() != null
				&& letterParty.getRegistration().getEgwStatus().getCode() != null
				&& letterParty.getRegistration().getEgwStatus().getCode()
						.equals(BpaConstants.CMDACREATEDLETTERTOPARTY))
		// letterParty.getRegistration().getEgwStatus().getCode().equals(BpaConstants.CMDACREATEDLETTERTOPARTY)
		{
			addActionMessage(getMessage("lpreply.noLpresent.validate"));
		}
		setMode("noEditMode");
		return NEW;
	}

	private void getLetterToPartyDetails(CMDALetterToPartyExtn letterParty) {
		setExistLpReason(letterParty.getLetterToParty()
				.getLetterToPartyReason().getCode());
		setExistLpRemarks(letterParty.getLetterToParty()
				.getLetterToPartyRemarks());
		setExistLpNum(letterParty.getLetterToPartyNumber());
	}

	protected String getMessage(String key) {
		return getText(key);
	}

	@ValidationErrorPage(NEW)
	@Action(value = "/lpReplyCmdaExtn-createLpReply", results = { @Result(name = NEW,type = "dispatcher") })
	public String createLpReply() {

		try {
			EgwStatus oldStatus = letterParty.getRegistration().getEgwStatus();
			String ackNum = bpaNumberGenerationExtnService
					.generateLetterToReplycmdaAckNumber();
			EgwStatus egwStatus = bpaCommonExtnService.getstatusbyCode(
					BpaConstants.CMDALPREPLYRECEIVED,
					BpaConstants.NEWBPAREGISTRATIONMODULE);
			letterParty.setReplyDate(new Date());
			letterParty.setIsHistory('Y');
			letterParty.setLpReplyDescription(letterPartyReply
					.getLpReplyDescription());
			letterParty.setLpReplyRemarks(letterPartyReply.getLpReplyRemarks());
			letterParty.setAcknowledgementNumber(ackNum);
			letterParty.getRegistration().setEgwStatus(egwStatus);
			setDocumentNumberForLetterToParty();
			addActionMessage("Letter To Party Reply Details Saved Successfully");
			bpaCommonExtnService.createStatusChange(registration, oldStatus);
			setMode(BpaConstants.MODEVIEW);
			setRegistrationId(registrationId);
			setLetterParty(letterParty);
			setLetterPartyReply(letterParty);

		} catch (Exception e) {
			e.printStackTrace();
			throw new EGOVRuntimeException(" Error in Creating LP reply "
					+ e.getMessage());
		}
		return NEW;
	}

	protected void setDocumentNumberForLetterToParty() {
		if (getDocumentNum() != null && !getDocumentNum().equals("")) {
			letterParty.setDocumentid(getDocumentNum());
		}

		if (letterParty != null && letterParty.getDocumentid() != null) {
			this.setDocumentNum(letterParty.getDocumentid());
		}
	}
	@Action(value = "/lpReplyCmdaExtn-ackPrint", results = { @Result(name = "ackReport",type = "dispatcher") })
	public String ackPrint() {
		try {
			ReportRequest reportRequest = null;
			Map<String, Object> reportParams = new HashMap<String, Object>();
			if (!"".equals(getRequestID()) && getRequestID() != null) {
				registration = registerBpaExtnService
						.getRegistrationByPassingRequestNumber(getRequestID());
			} else if (getRegistrationId() != null) {
				registration = letterToPartyExtnService
						.getRegistrationObjectbyId(registrationId);
			}
			Map<String, Object> reportData = constructLpReplyReportData(registration);
			reportParams.put("lpDate", reportData.get("lpDate"));
			reportParams.put("cmdaDate", reportData.get("cmdaDate"));
			reportRequest = new ReportRequest(
					BpaConstants.CMDALPREPLYNOTICEACKREPORT, reportData,
					reportParams);
			reportRequest.setPrintDialogOnOpenReport(true);
			reportId = ReportViewerUtil.addReportToSession(
					reportService.createReport(reportRequest), getSession());

			return "ackReport";
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}

	}

	private Map<String, Object> constructLpReplyReportData(
			RegistrationExtn registration) {
		Map<String, Object> reportData = new HashMap<String, Object>();
		CMDALetterToPartyExtn lpReply = letterToPartyExtnService
				.getLatestCMDALetterToPartyForRegObj(registration);
		String address = registration.getBpaOwnerAddress();

		String EMPTYSTRING = "";
		if (registration != null && registration.getAdminboundaryid() != null) {
			if (registration.getAdminboundaryid().getParent() != null
					&& registration.getAdminboundaryid().getParent()
							.getParent() != null) {
				System.out.println("region name "
						+ registration.getAdminboundaryid().getParent()
								.getParent().getName());
				reportData.put("region", registration.getAdminboundaryid()
						.getParent().getParent().getName());
			}
			if (registration.getAdminboundaryid().getParent() != null)
				reportData.put("zone", registration.getAdminboundaryid()
						.getParent().getName());
			else
				reportData.put("zone", EMPTYSTRING);

			reportData.put("ward", registration.getAdminboundaryid().getName());

		}
		if (lpReply != null
				&& lpReply.getRegistration().getAdminboundaryid() != null
				&& lpReply.getRegistration().getAdminboundaryid().getParent() != null
				&& lpReply.getRegistration().getAdminboundaryid().getParent()
						.getParent() != null)

			if (lpReply.getRegistration().getAdminboundaryid().getParent()
					.getParent().getName()
					.equalsIgnoreCase(BpaConstants.NORTHREGION)) {
				fromAddressToLp = BpaConstants.ASSISTANTADDRESS + "\n"
						+ BpaConstants.NORTHREGION_ADDRESS;
			} else if (lpReply.getRegistration().getAdminboundaryid()
					.getParent().getParent().getName()
					.equalsIgnoreCase(BpaConstants.SOUTHREGION)) {
				fromAddressToLp = BpaConstants.ASSISTANTADDRESS + "\n"
						+ BpaConstants.SOUTHREGION_ADDRESS;

			} else if (lpReply.getRegistration().getAdminboundaryid()
					.getParent().getParent().getName()
					.equalsIgnoreCase(BpaConstants.CENTRALREGION)) {
				fromAddressToLp = BpaConstants.ASSISTANTADDRESS + "\n"
						+ BpaConstants.CENTRALREGION_ADDRESS;
			} else {
				fromAddressToLp = EMPTYSTRING;
			}
		reportData.put("fromAddressToLp", fromAddressToLp);
		reportData
				.put("planSubmissionNum", registration.getPlanSubmissionNum());
		if (registration != null && registration.getOwner() != null)
			reportData.put("applicantName", registration.getOwner().getName());
		else
			reportData.put("applicantName", EMPTYSTRING);
		reportData.put(
				"cmdaDate",
				registration.getCmdaRefDate() != null ? registration
						.getCmdaRefDate() : null);
		reportData.put("cmdaNumber", registration.getCmdaNum() != null
				&& registration.getCmdaNum() != "" ? registration.getCmdaNum()
				: "");
		reportData.put("address", address);
		reportData.put("lpdescription", lpReply.getLpReplyDescription());
		reportData.put("ackNo", lpReply.getAcknowledgementNumber());
		reportData.put("replyDate", lpReply.getReplyDate());
		reportData.put("lpDate",
				lpReply.getLetterToParty().getLetterDate() != null ? lpReply
						.getLetterToParty().getLetterDate() : null);
		reportData.put("lpNumber", lpReply.getLetterToPartyNumber());
		reportData.put("lpReason", lpReply.getLpReason());

		if (lpReply.getLpReplyRemarks() != null) {
			reportData.put("remarks", lpReply.getLpReplyRemarks());
		}
		return reportData;
	}

	@Override
	public Object getModel() {
		return letterPartyReply;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public CMDALetterToPartyExtn getLetterParty() {
		return letterParty;
	}

	public void setLetterParty(CMDALetterToPartyExtn letterParty) {
		this.letterParty = letterParty;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public LetterToPartyExtnService getLetterToPartyExtnService() {
		return letterToPartyExtnService;
	}

	public void setLetterToPartyExtnService(
			LetterToPartyExtnService letterToPartyService) {
		this.letterToPartyExtnService = letterToPartyService;
	}

	public String getExistLpReason() {
		return existLpReason;
	}

	public void setExistLpReason(String existLpReason) {
		this.existLpReason = existLpReason;
	}

	public String getExistLpRemarks() {
		return existLpRemarks;
	}

	public void setExistLpRemarks(String existLpRemarks) {
		this.existLpRemarks = existLpRemarks;
	}

	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}

	public void setInspectionExtnService(InspectionExtnService inspectionService) {
		this.inspectionExtnService = inspectionService;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(BpaCommonExtnService bpaCommonService) {
		this.bpaCommonExtnService = bpaCommonService;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public RegistrationExtn getRegistration() {
		return registration;
	}

	public void setRegistration(RegistrationExtn registration) {
		this.registration = registration;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	/*
	 * public EisManager getEisManager() { return eisManager; }
	 * 
	 * public void setEisManager(EisManager eisManager) { this.eisManager =
	 * eisManager; }
	 */

	public String getExistLpNum() {
		return existLpNum;
	}

	public void setExistLpNum(String existLpNum) {
		this.existLpNum = existLpNum;
	}

	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}

	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaService) {
		this.registerBpaExtnService = registerBpaService;
	}

	public Long getLetterToPartyId() {
		return letterToPartyId;
	}

	public void setLetterToPartyId(Long letterToPartyId) {
		this.letterToPartyId = letterToPartyId;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getDocumentNum() {
		return documentNum;
	}

	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}

	public CMDALetterToPartyExtn getLetterPartyReply() {
		return letterPartyReply;
	}

	public void setLetterPartyReply(CMDALetterToPartyExtn letterPartyReply) {
		this.letterPartyReply = letterPartyReply;
	}

	public String getFromAddressToLp() {
		return fromAddressToLp;
	}

	public void setFromAddressToLp(String fromAddressToLp) {
		this.fromAddressToLp = fromAddressToLp;
	}
}
