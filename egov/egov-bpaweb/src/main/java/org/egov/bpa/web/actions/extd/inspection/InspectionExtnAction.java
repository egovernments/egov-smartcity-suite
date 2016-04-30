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
package org.egov.bpa.web.actions.extd.inspection;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.ApplicantStatus;
import org.egov.bpa.models.extd.Docket;
import org.egov.bpa.models.extd.DocketConstructionStage;
import org.egov.bpa.models.extd.DocketDocumentDetails;
import org.egov.bpa.models.extd.DocketFloorDetails;
import org.egov.bpa.models.extd.DocketViolations;
import org.egov.bpa.models.extd.InspectMeasurementDtlsExtn;
import org.egov.bpa.models.extd.InspectionChecklistExtn;
import org.egov.bpa.models.extd.InspectionDetailsExtn;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.LandBldngZoneingExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.UsageOfConstruction;
import org.egov.bpa.models.extd.masters.BpaFeeExtn;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.LandBuildingTypesExtn;
import org.egov.bpa.models.extd.masters.LayoutMasterExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.models.extd.masters.StormWaterDrainExtn;
import org.egov.bpa.models.extd.masters.SurroundedBldgDtlsExtn;
import org.egov.bpa.services.extd.Fee.FeeDetailsExtnService;
import org.egov.bpa.services.extd.Fee.RegistrationFeeExtnService;
import org.egov.bpa.services.extd.common.BpaCitizenPortalExtnService;
import org.egov.bpa.services.extd.common.BpaCommonExtnService;
import org.egov.bpa.services.extd.common.BpaDmdCollExtnService;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.bpa.services.extd.common.FeeExtnService;
import org.egov.bpa.services.extd.inspection.InspectionExtnService;
import org.egov.bpa.services.extd.register.RegisterBpaExtnService;
import org.egov.bpa.web.actions.extd.common.BpaExtnRuleBook;
import org.egov.commons.EgwStatus;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Transactional(readOnly = true)
@Namespace("/inspection")
@SuppressWarnings("serial")
@Results({ @Result(name = "NOACCESS", type = "stream", location = "returnStream", params = {
		"contentType", "text/plain" }) })
@ParentPackage("egov")
public class InspectionExtnAction extends BaseFormAction {
	private InspectionExtn inspection = new InspectionExtn();
	private final static Logger LOGGER = Logger
			.getLogger(InspectionExtnAction.class);
	private List<InspectionExtn> postponedInspectionDetails = new ArrayList<InspectionExtn>();
	private Long registrationId;
	private Long serviceTypeId;
	private RegistrationExtn registrationObj;
	private InspectionExtnService inspectionExtnService;
	private String mode;
	private Date verifyDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private User loginUser;
	private List<InspectMeasurementDtlsExtn> inspectMeasurementDtls = new ArrayList<InspectMeasurementDtlsExtn>();
	private List<InspectMeasurementDtlsExtn> constructionMesDtls = new ArrayList<InspectMeasurementDtlsExtn>();
	private List<InspectionChecklistExtn> chkListDet = new ArrayList<InspectionChecklistExtn>();
	private List<InspectionExtn> existingSiteInspectionDetails = new ArrayList<InspectionExtn>();
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	protected FeeDetailsExtnService feeDetailsExtnService;
	private String plotDetails;
	private String fromreg;
	private BpaCommonExtnService bpaCommonExtnService;
	private String returnStream = "You Have no permission to view";
	private EgwStatus oldStatus;
	private Long constructionstage;
	private String lastInspectionDate;
	private String autoGenInspectionDates;
	private String siteInspectionScheduledates;
	private String ismodify;
	private BpaCitizenPortalExtnService bpaCitizenPortalService;
	private String serviceTypeCode;
	private Map<String, String> buildingTypeMap = new LinkedHashMap<String, String>();
	private Map<String, String> landZoningMap = new LinkedHashMap<String, String>();
	private Map<String, String> bldngZoningMap = new LinkedHashMap<String, String>();
	private Map<String, String> applicantStatus = new LinkedHashMap<String, String>();
	private RegisterBpaExtnService registerBpaExtnService;
	private Integer reportId = -1;
	private List<DocketDocumentDetails> docketDocumentDtls = new ArrayList<DocketDocumentDetails>();
	private List<DocketConstructionStage> constructionStages = new ArrayList<DocketConstructionStage>();
	private List<DocketFloorDetails> docketFloorDetail = new ArrayList<DocketFloorDetails>();
	private List<DocketViolations> devContrlList = new ArrayList<DocketViolations>();
	private List<DocketViolations> setBackList = new ArrayList<DocketViolations>();
	private List<DocketViolations> parkingList = new ArrayList<DocketViolations>();
	private List<DocketViolations> generalList = new ArrayList<DocketViolations>();
	private List<DocketViolations> minDistancePowerLineList = new ArrayList<DocketViolations>();

	public static final String LAND_TYPE = "Land";
	public static final String BUILDING_TYPE = "Building";
	private List<Integer> penaltyPeriodLst = new ArrayList<Integer>();
	protected FeeExtnService feeExtnService;
	protected RegistrationFeeExtnService registrationFeeExtnService;
	private BpaDmdCollExtnService bpaDmdCollExtnService;
	private Boolean allowModifyInspection = false;
	private Boolean isSurveyor;
	private Boolean isInspectedByAEorAEE = Boolean.FALSE;
	private Map<Integer, String> floorNoMap = new HashMap<Integer, String>();
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",
			new Locale("en", "IN"));
	private String serviceRegId;
	private String requestID;

	public Boolean getIsInspectedByAEorAEE() {
		return isInspectedByAEorAEE;
	}

	public void setIsInspectedByAEorAEE(Boolean isInspectedByAEorAEE) {
		this.isInspectedByAEorAEE = isInspectedByAEorAEE;
	}

	public Boolean getAllowModifyInspection() {
		return allowModifyInspection;
	}

	public void setAllowModifyInspection(Boolean allowModifyInspection) {
		this.allowModifyInspection = allowModifyInspection;
	}

	public BpaDmdCollExtnService getBpaDmdCollExtnService() {
		return bpaDmdCollExtnService;
	}

	public void setBpaDmdCollExtnService(
			BpaDmdCollExtnService bpaDmdCollExtnService) {
		this.bpaDmdCollExtnService = bpaDmdCollExtnService;
	}

	public FeeExtnService getFeeExtnService() {
		return feeExtnService;
	}

	public void setFeeExtnService(FeeExtnService feeExtnService) {
		this.feeExtnService = feeExtnService;
	}

	public InspectionExtnAction() {
		addRelatedEntity("registration", RegistrationExtn.class);
		addRelatedEntity("inspectedBy", User.class);
		addRelatedEntity("inspectionDetails", InspectionDetailsExtn.class);
		addRelatedEntity("modifiedBy", User.class);
		addRelatedEntity("createdBy", User.class);
		addRelatedEntity("surroundedByNorth", SurroundedBldgDtlsExtn.class);
		addRelatedEntity("surroundedBySouth", SurroundedBldgDtlsExtn.class);
		addRelatedEntity("surroundedByEast", SurroundedBldgDtlsExtn.class);
		addRelatedEntity("surroundedByWest", SurroundedBldgDtlsExtn.class);
		addRelatedEntity("layoutType", LayoutMasterExtn.class);
		addRelatedEntity("landUsage", LandBuildingTypesExtn.class);
		addRelatedEntity("buildingType", LandBuildingTypesExtn.class);
		addRelatedEntity("bldngStormWaterDrain", StormWaterDrainExtn.class);
		addRelatedEntity("docket", Docket.class);
		addRelatedEntity("docket.createdBy", User.class);
		addRelatedEntity("docket.modifiedBy", User.class);
		addRelatedEntity("checkListDetails", CheckListDetailsExtn.class);

	}

	public void prepare() {
		super.prepare();
		LOGGER.info(".......Registration Id----->" + getRegistrationId());
		if (getRegistrationId() != null) {
			registrationObj = inspectionExtnService
					.getRegistrationObjectbyId(getRegistrationId());
			if (registrationObj != null
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
				if (!isUserMappedToSurveyorRole()) {
					existingSiteInspectionDetails = inspectionExtnService
							.getSiteInspectionListforRegistrationObject(registrationObj);
					if (existingSiteInspectionDetails.isEmpty()) {
						List<InspectionExtn> surveyorIns = new ArrayList<InspectionExtn>();
						surveyorIns = inspectionExtnService
								.getSiteInspectionListforRegBySurveyorAndOfficial(registrationObj);
						InspectionExtn insObj = surveyorIns.get(0);
						inspection = new InspectionExtn(insObj.getLandZoning(),
								insObj.getLayoutType(),
								insObj.getLndMinPlotExtent(),
								insObj.getLndProposedPlotExtent(),
								insObj.getLndOsrLandExtent(),
								insObj.getLndGuideLineValue(),
								insObj.getLandUsage(),
								insObj.getLndRegularizationArea(),
								insObj.getLndPenaltyPeriod(),
								insObj.getLndIsRegularisationCharges(),
								insObj.getBuildingType(),
								insObj.getBldngBuildUpArea(),
								insObj.getBldngProposedPlotFrntage(),
								insObj.getBldngRoadWidth(),
								insObj.getBldngProposedBldngArea(),
								insObj.getBldngGFloor_TiledFloor(),
								insObj.getBldngGFloor_OtherTypes(),
								insObj.getBldngFrstFloor_TotalArea(),
								insObj.getBldngStormWaterDrain(),
								insObj.getBldngCompoundWall(),
								insObj.getBldngWellOht_SumpTankArea(),
								insObj.getBldngCommercial(),
								insObj.getBldngResidential(),
								insObj.getBldngIsRegularisationCharges(),
								insObj.getBldngIsImprovementCharges(),
								insObj.getDwellingUnit());
					}
				}
			}
			inspection.setRegistration(registrationObj);
		}
		loginUser = inspectionExtnService.getUserbyId((EgovThreadLocals
				.getUserId()));
		addDropdownData("servicetypeList",
				bpaCommonExtnService.getAllActiveServiceTypeList());
		addDropdownData("buildingCategoryList",
				bpaCommonExtnService.getAllActiveBuildingCategoryList());
		addDropdownData("constructionStageList",
				inspectionExtnService.getAllConstructionStages());
		addDropdownData("surroundedBuildingList",
				inspectionExtnService.getAllSurroundedBuildingDetails());
		addDropdownData("layoutTypeList",
				inspectionExtnService.getAllLayoutTypes());
		addDropdownData("landUsageList",
				inspectionExtnService.getAllLandBuildingTypes(LAND_TYPE));
		addDropdownData("buildingTypeList",
				inspectionExtnService.getAllLandBuildingTypes(BUILDING_TYPE));
		addDropdownData("penaltyPeriodList", getPenaltyPeriod());
		addDropdownData("stormWaterDrainList",
				bpaCommonExtnService.getAllStormWaterDrain());
		addDropdownData("usageOfConstructionList",
				Arrays.asList(UsageOfConstruction.values()));
		getLandBldngZoning();
		applicantStatus = new LinkedHashMap<String, String>();
		applicantStatus.put(ApplicantStatus.OWNER.toString(),
				ApplicantStatus.OWNER.toString());
		applicantStatus.put(ApplicantStatus.LASSEE.toString(),
				ApplicantStatus.LASSEE.toString());
		applicantStatus.put(ApplicantStatus.POWER_OF_ATTORNEY.toString(),
				ApplicantStatus.POWER_OF_ATTORNEY.toString());

		setIsSurveyor(isUserMappedToSurveyorRole());
	}

	public Map<Integer, String> getFloorNoMap() {

		if (inspection.getDocket() == null
				|| inspection.getDocket().getFloorCount() == null)
			return Collections.emptyMap();
		else {
			floorNoMap = bpaCommonExtnService.getFloorNumberMap(inspection
					.getDocket().getFloorCount());
			return floorNoMap;
		}
	}

	private void buildDocketObjectDetails() {
		if (inspection != null && inspection.getRegistration() != null
				&& inspection.getDocket() == null) {
			List<CheckListDetailsExtn> docketDocChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-DOCUMENTENCLOSED");
			List<CheckListDetailsExtn> constStageChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-CONSTRUCTIONSTAGE");

			List<CheckListDetailsExtn> devContrlChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-VIOLATION-DEVCONTROLRULE");
			List<CheckListDetailsExtn> setBackChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-VIOLATION-SETBACK");
			List<CheckListDetailsExtn> parkingChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-VIOLATION-PARKING");
			List<CheckListDetailsExtn> generalChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-VIOLATION-GENERAL");
			List<CheckListDetailsExtn> minDistancePowerLineChkListDetails = inspectionExtnService
					.getCheckListByServiceAndType(inspection.getRegistration()
							.getServiceType().getId(),
							"DOCKETSHEET-VIOLATION-MINDISTANCEPOWERLINE");

			for (CheckListDetailsExtn chkList : devContrlChkListDetails) {
				DocketViolations violationObj = new DocketViolations();
				violationObj.setCheckListDetails(chkList);
				devContrlList.add(violationObj);
			}
			for (CheckListDetailsExtn chkList : setBackChkListDetails) {
				DocketViolations violationObj = new DocketViolations();
				violationObj.setCheckListDetails(chkList);
				setBackList.add(violationObj);
			}
			for (CheckListDetailsExtn chkList : parkingChkListDetails) {
				DocketViolations violationObj = new DocketViolations();
				violationObj.setCheckListDetails(chkList);
				parkingList.add(violationObj);
			}
			for (CheckListDetailsExtn chkList : generalChkListDetails) {
				DocketViolations violationObj = new DocketViolations();
				violationObj.setCheckListDetails(chkList);
				generalList.add(violationObj);
			}
			for (CheckListDetailsExtn chkList : minDistancePowerLineChkListDetails) {
				DocketViolations violationObj = new DocketViolations();
				violationObj.setCheckListDetails(chkList);
				minDistancePowerLineList.add(violationObj);
			}

			for (CheckListDetailsExtn chkList : docketDocChkListDetails) {
				DocketDocumentDetails docketDocument = new DocketDocumentDetails();
				docketDocument.setCheckListDetails(chkList);
				docketDocumentDtls.add(docketDocument);
			}
			for (CheckListDetailsExtn chkList : constStageChkListDetails) {
				DocketConstructionStage docConstStage = new DocketConstructionStage();
				docConstStage.setCheckListDetails(chkList);
				constructionStages.add(docConstStage);
			}
			/*
			 * if(inspection.getDocket()==null) { docketFloorDetail.add(new
			 * DocketFloorDetails());// commenting cos as of now they dont need
			 * docket sheet for servicetype 2 }
			 */

		} else {// Set values back from object to UI.
			if (inspection != null && inspection.getDocket() != null) {
				if (!inspection.getDocket().getDocumentEnclosedSet().isEmpty())
					docketDocumentDtls = new ArrayList<DocketDocumentDetails>(
							inspection.getDocket().getDocumentEnclosedSet());
				if (!inspection.getDocket().getConstructionStagesSet()
						.isEmpty())
					constructionStages = new ArrayList<DocketConstructionStage>(
							inspection.getDocket().getConstructionStagesSet());
				if (!inspection.getDocket().getDocketFlrDtlsSet().isEmpty()) {
					docketFloorDetail.clear();
					docketFloorDetail = new ArrayList<DocketFloorDetails>(
							inspection.getDocket().getDocketFlrDtlsSet());
				}
				if (!inspection.getDocket().getViolationSet().isEmpty()) {
					for (DocketViolations docViolations : inspection
							.getDocket().getViolationSet()) {
						if (docViolations.getCheckListDetails().getCheckList()
								.getChecklistType()
								.equals("DOCKETSHEET-VIOLATION-DEVCONTROLRULE")) {
							devContrlList.add(docViolations);
						} else if (docViolations.getCheckListDetails()
								.getCheckList().getChecklistType()
								.equals("DOCKETSHEET-VIOLATION-SETBACK")) {
							setBackList.add(docViolations);
						} else if (docViolations.getCheckListDetails()
								.getCheckList().getChecklistType()
								.equals("DOCKETSHEET-VIOLATION-PARKING")) {
							parkingList.add(docViolations);
						} else if (docViolations.getCheckListDetails()
								.getCheckList().getChecklistType()
								.equals("DOCKETSHEET-VIOLATION-GENERAL")) {
							generalList.add(docViolations);
						} else if (docViolations
								.getCheckListDetails()
								.getCheckList()
								.getChecklistType()
								.equals("DOCKETSHEET-VIOLATION-MINDISTANCEPOWERLINE")) {
							minDistancePowerLineList.add(docViolations);
						}
					}

				}
			}
		}
	}

	@Action(value = "/inspectionExtn-printDocketSheet", results = { @Result(name = "report",type = "dispatcher") })
	public String printDocketSheet() {
		LOGGER.debug("Entered into print docket sheet method");
		RegistrationExtn registration = registerBpaExtnService.findById(
				registrationId, false);

		try {
			if (registration != null) {
				Boolean enableDocketSheetForView = Boolean.FALSE;
				if (!bpaCommonExtnService.isUserMappedToSurveyorRole()
						&& registration.getEgwStatus() != null
						&& registration.getEgwStatus().getCode()
								.equals(BpaConstants.FILECONSIDERATIONCHECKED)
						&& (registration.getCurrentState() != null
								&& registration
										.getCurrentState()
										.getValue()
										.equals(BpaConstants.FORWARDEDTORDCSTATE)
								|| registration
										.getCurrentState()
										.getValue()
										.equals(BpaConstants.FORWARDEDTOEESTATE) || registration
								.getCurrentState().getValue()
								.equals(BpaConstants.FORWADREDTOSESTATE))) {

					enableDocketSheetForView = Boolean.TRUE;
				}

				reportId = registerBpaExtnService.printDocketSheet(
						registration, getSession(), enableDocketSheetForView);
			}
		} catch (Exception e) {
			throw new EGOVRuntimeException("Exception : " + e);
		}
		LOGGER.debug("Exit from print method");
		return "report";

	}

	private void getLandBldngZoning() {
		landZoningMap = new LinkedHashMap<String, String>();
		landZoningMap.put(LandBldngZoneingExtn.CONTINOUS.toString(),
				LandBldngZoneingExtn.CONTINOUS.toString());
		landZoningMap.put(LandBldngZoneingExtn.DETACHED.toString(),
				LandBldngZoneingExtn.DETACHED.toString());
		landZoningMap.put(LandBldngZoneingExtn.EWS.toString(),
				LandBldngZoneingExtn.EWS.toString());

		bldngZoningMap = new LinkedHashMap<String, String>();
		bldngZoningMap.put(LandBldngZoneingExtn.CONTINOUS.toString(),
				LandBldngZoneingExtn.CONTINOUS.toString());
		bldngZoningMap.put(LandBldngZoneingExtn.DETACHED.toString(),
				LandBldngZoneingExtn.DETACHED.toString());
		bldngZoningMap.put(LandBldngZoneingExtn.EWS.toString(),
				LandBldngZoneingExtn.EWS.toString());
	}

	private List<Integer> getPenaltyPeriod() {
		for (int i = 0; i < 12; i++) {
			int j = i;
			penaltyPeriodLst.add(i, ++j);
		}
		return penaltyPeriodLst;
	}

	@Action(value = "/inspectionExtn-newForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String newForm() {
		showInspectionDetails();
		if (postponedInspectionDetails.size() != 0) {
			setVerifyDate(postponedInspectionDetails.get(0).getInspectionDate());
			setIsmodify("YES");
		} else {
			setIsmodify("NO");
		}

		autoGenInspectionDates = bpaCommonExtnService.getAppconfigValueResult(
				BpaConstants.BPAMODULENAME,
				BpaConstants.AUTO_GENERATION_INSPCTIONDATES, null);
		if (null != autoGenInspectionDates
				&& !"".equals(autoGenInspectionDates)
				&& autoGenInspectionDates.equalsIgnoreCase("YES")) {
			StringBuffer dateStringBuffer = new StringBuffer();
			List<String> siteInspectionScheduledateList = new ArrayList<String>();
			if (isUserMappedToSurveyorRole()) {
				siteInspectionScheduledateList = bpaCommonExtnService
						.autoGenerateSiteInspectionDatesForSurveyor(registrationObj);
			} else {
				siteInspectionScheduledateList = bpaCommonExtnService
						.autoGenerateSiteInspectionDates(registrationObj);
			}
			if (!siteInspectionScheduledateList.isEmpty()) {
				for (int i = 0; i < siteInspectionScheduledateList.size(); i++) {
					dateStringBuffer.append(siteInspectionScheduledateList
							.get(i));
					if (i < siteInspectionScheduledateList.size() - 1)
						dateStringBuffer.append(",");
				}
			}
			setSiteInspectionScheduledates(dateStringBuffer.toString());
		}
		return NEW;
	}

	public Boolean isUserMappedToSurveyorRole() {
		if (EgovThreadLocals.getUserId() != null) {
			User user = bpaCommonExtnService.getUserbyId((EgovThreadLocals
					.getUserId()));
			/*
			 * for(Role role : user.getRoles()) if(role.getRoleName()!=null &&
			 * role
			 * .getRoleName().equalsIgnoreCase(BpaConstants.PORTALUSERSURVEYORROLE
			 * )) { //Todo Phionix return true; }
			 */

		}
		return false;
	}

	public Boolean isUserMappedToSelectedRole(String userRole) {
		if (EgovThreadLocals.getUserId() != null) {
			User user = bpaCommonExtnService.getUserbyId((EgovThreadLocals
					.getUserId()));
			/*
			 * for(Role role : user.getRoles()) if(role.getRoleName()!=null &&
			 * role.getRoleName().equalsIgnoreCase(userRole)) { return true; }
			 */// Todo Phionix

		}
		return false;
	}

	@Action(value = "/inspectionExtn-inspectionForm", results = { @Result(name = "inspectionForm",type = "dispatcher") })
	public String inspectionForm() {
		inspection.setInspectedBy(loginUser);

		if (!isUserMappedToSurveyorRole()
				&& inspection.getRegistration() != null
				&& inspection.getRegistration().getServiceType() != null
				&& inspection.getRegistration().getServiceType().getCode() != null
				&& (inspection.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE) || inspection
						.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.CMDACODE))) {
			buildDocketObjectDetails();
		}
		existingSiteInspectionDetails = inspectionExtnService
				.getSiteInspectionListforRegistrationObject(registrationObj);
		if (existingSiteInspectionDetails.size() != 0) {
			setLastInspectionDate(sdf.format(existingSiteInspectionDetails.get(
					0).getInspectionDate()));
			// setLastInspectionDate(new Date());
		}
		setServiceTypeCode(registrationObj.getServiceType().getCode());
		return "inspectionForm";
	}

	private void showInspectionDetails() {
		postponedInspectionDetails = inspectionExtnService
				.getInspectionListforRegistrationObject(registrationObj);
		for (InspectionExtn inspectiondetails : postponedInspectionDetails) {
			if (inspectiondetails.getParent() != null)
				inspectiondetails.getParent().setPostponedDate(
						inspectiondetails.getInspectionDate());
		}
	}

	@Action(value = "/inspectionExtn-showInspectioninRegistration", results = { @Result(name = "registrationinspection",type = "dispatcher") })
	public String showInspectioninRegistration() {
		showInspectionDetails();
		return "registrationinspection";
	}

	@Action(value = "/inspectionExtn-viewForm", results = { @Result(name = NEW,type = "dispatcher") })
	public String viewForm() {
		return BpaConstants.MODEVIEW;
	}

	@Override
	public Object getModel() {

		return inspection;
	}

	@Action(value = "/inspectionExtn-create", results = { @Result(name = SUCCESS,type = "dispatcher") })
	public String create() {
		if (inspection.getInspectionDate() == null
				|| inspection.getInspectionDate().equals("")) {
			addActionError("Inspection Date is Mandatory");
			newForm();
		}
		postponedInspectionDetails = inspectionExtnService
				.getInspectionListforRegistrationObject(registrationObj);
		if (postponedInspectionDetails.size() > 0) {
			inspection.setParent(postponedInspectionDetails.get(0));
			inspection.getParent().setPostponementReason(
					inspection.getPostponementReason());
		}
		oldStatus = inspection.getRegistration().getEgwStatus();
		if (isUserMappedToSurveyorRole())
			inspectionExtnService
					.save(inspection,
							loginUser,
							null,
							Boolean.FALSE,
							bpaCommonExtnService
									.getstatusbyCode(BpaConstants.SITEINSPECTIONSCHEDULEDBYLS));
		else
			inspectionExtnService.save(inspection, loginUser, null,
					Boolean.FALSE, bpaCommonExtnService
							.getstatusbyCode(BpaConstants.INSPECTIONSCHEDULED));
		String UserRole = bpaCommonExtnService.getUserRolesForLoggedInUser();
		if (UserRole != null && !"".equals(UserRole)
				&& !UserRole.contains(BpaConstants.PORTALUSERSURVEYORROLE)) {
			bpaCommonExtnService.buildEmail(registrationObj,
					BpaConstants.SMSEMAILINSPECTION, null);
			bpaCommonExtnService.buildSMS(registrationObj,
					BpaConstants.SMSEMAILINSPECTION);
		}
		showInspectionDetails();
		bpaCommonExtnService.createStatusChange(inspection.getRegistration(),
				oldStatus);
		addActionMessage("Inspection Date Saved Successfully");
		setMode(BpaConstants.MODEVIEW);
		return SUCCESS;
	}

	@Action(value = "/inspectionExtn-createSiteInspection", results = { @Result(name = SUCCESS,type = "dispatcher") })
	public String createSiteInspection() {

		Set<InspectMeasurementDtlsExtn> inspectionmeasuredtls = new HashSet<InspectMeasurementDtlsExtn>(
				getInspectMeasurementDtls());
		Set<InspectMeasurementDtlsExtn> constructionmeasuredtls = new HashSet<InspectMeasurementDtlsExtn>(
				getConstructionMesDtls());
		Set<InspectionChecklistExtn> checkListDtls = new HashSet<InspectionChecklistExtn>(
				getChkListDet());

		if (getConstructionstage() != null)
			inspection.getInspectionDetails().setConstStages(
					inspectionExtnService
							.getConstructionStage(getConstructionstage()));

		inspection.getInspectionDetails().setInspectMeasurementDtlsSet(
				inspectionmeasuredtls);
		inspection.getInspectionDetails().getInspectMeasurementDtlsSet()
				.addAll(constructionmeasuredtls);
		// To set BuildingZone and LandProposedPlotExtent with LandZoning and
		// LndMinPlotExtent value
		if (inspection.getLandZoning() != null) {
			inspection.setBuildingZoning(inspection.getLandZoning());
		}
		inspection.setLndProposedPlotExtent(inspection.getLndMinPlotExtent());

		for (InspectMeasurementDtlsExtn inspectionMeasuremtDtls : inspection
				.getInspectionDetails().getInspectMeasurementDtlsSet()) {
			inspectionMeasuremtDtls.setInspectionDetails(inspection
					.getInspectionDetails());
			if (inspection.getRegistration().getServiceType().getCode() != null
					&& inspection.getRegistration().getServiceType().getCode()
							.equals(BpaConstants.RECLASSIFICATION)) {
				if (inspectionMeasuremtDtls.getSurroundedByEast() != null
						&& inspectionMeasuremtDtls.getSurroundedByEast()
								.getId() != null
						&& inspectionMeasuremtDtls.getSurroundedByEast()
								.getId() == -1) {
					inspectionMeasuremtDtls.setSurroundedByEast(null);
				}
				if (inspectionMeasuremtDtls.getSurroundedByWest() != null
						&& inspectionMeasuremtDtls.getSurroundedByWest()
								.getId() != null
						&& inspectionMeasuremtDtls.getSurroundedByWest()
								.getId() == -1) {
					inspectionMeasuremtDtls.setSurroundedByWest(null);
				}
			}
		}
		inspection.getInspectionDetails().setInspectionChecklistSet(
				checkListDtls);
		for (InspectionChecklistExtn inspectionchklist : inspection
				.getInspectionDetails().getInspectionChecklistSet()) {
			inspectionchklist.setInspectionDetails(inspection
					.getInspectionDetails());
		}
		inspection.getInspectionDetails().setInspection(inspection);

		if (!isUserMappedToSurveyorRole()
				&& inspection.getRegistration() != null
				&& inspection.getRegistration().getServiceType() != null
				&& inspection.getRegistration().getServiceType().getCode() != null
				&& (inspection.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE) || inspection
						.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.CMDACODE))) {
			if (inspection.getDocket() != null) {

				if (inspection.getDocket().getCreatedBy() == null) {
					inspection.getDocket().setCreatedBy(loginUser);
				}
				inspection.getDocket().setModifiedBy(loginUser);
				inspection.getDocket().setModifiedDate(new Date());

				// SEt docket object reference in docket document detail.
				Set<DocketDocumentDetails> docDetails = new HashSet<DocketDocumentDetails>(
						getDocketDocumentDtls());

				Set<DocketConstructionStage> consTDetails = new HashSet<DocketConstructionStage>(
						getConstructionStages());

				Set<DocketViolations> violDetails = new HashSet<DocketViolations>(
						getGeneralList());
				Set<DocketFloorDetails> floorDetails = new HashSet<DocketFloorDetails>(
						getDocketFloorDetail());
				if (minDistancePowerLineList != null
						&& minDistancePowerLineList.size() > 0)
					violDetails.addAll(minDistancePowerLineList);
				if (parkingList != null && parkingList.size() > 0)
					violDetails.addAll(parkingList);
				if (setBackList != null && setBackList.size() > 0)
					violDetails.addAll(setBackList);
				if (devContrlList != null && devContrlList.size() > 0)
					violDetails.addAll(devContrlList);

				inspection.getDocket().setDocumentEnclosedSet(docDetails);
				inspection.getDocket().setConstructionStagesSet(consTDetails);
				inspection.getDocket().setViolationSet(violDetails);
				inspection.getDocket().setDocketFlrDtlsSet(floorDetails);
				for (DocketDocumentDetails docketDocument : inspection
						.getDocket().getDocumentEnclosedSet()) {
					docketDocument.setDocket(inspection.getDocket());
				}
				for (DocketConstructionStage constStage : inspection
						.getDocket().getConstructionStagesSet()) {
					constStage.setDocket(inspection.getDocket());
				}
				for (DocketViolations violation : inspection.getDocket()
						.getViolationSet()) {
					violation.setDocket(inspection.getDocket());
				}
				inspection.getDocket().getDocketFlrDtlsSet().clear();

				buildDocumentFloorDetails();

			}
		}

		if ((inspection.getInspectionNum() == null)
				|| (inspection.getInspectionNum() != null && inspection
						.getInspectionNum().equals("")))
			inspection.setInspectionNum(bpaNumberGenerationExtnService
					.generateSiteInspectionNumber());
		oldStatus = inspection.getRegistration().getEgwStatus();

		if (isUserMappedToSurveyorRole()) {
			inspectionExtnService.save(inspection, null, null, Boolean.TRUE,
					bpaCommonExtnService
							.getstatusbyCode(BpaConstants.INSPECTEDBYLS));
		} else {
			inspectionExtnService.save(inspection, null, null, Boolean.TRUE,
					bpaCommonExtnService
							.getstatusbyCode(BpaConstants.INSPECTED));
		}
		// inspection.getRegistration().setEgwStatus(bpaCommonExtnService.getstatusbyCode(BpaConstants.INSPECTED));

		bpaCommonExtnService.createStatusChange(inspection.getRegistration(),
				oldStatus);

		/*
		 * Auto Generate Fee's and update demand on creation or modification of
		 * inspection. Each time new fee details will be generated.
		 */
		if (!isUserMappedToSurveyorRole()
				&& inspection.getRegistration() != null
				&& inspection.getRegistration().getServiceType() != null
				&& inspection.getRegistration().getServiceType().getCode() != null
				&& (inspection.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || inspection
						.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {

			// Based on registration, calculate fee and save fee and demand
			// details.
			List<BpaFeeExtn> santionFeeList = new ArrayList();

			// HibernateUtil.getCurrentSession().flush();
			if (inspection.getRegistration() != null) {
				santionFeeList = feeExtnService
						.getAllSanctionedFeesbyServiceType(inspection
								.getRegistration().getServiceType().getId());
			}
			for (BpaFeeExtn feeObject : santionFeeList) {

				// Check is required to calculate for current service type ?
				if (feeDetailsExtnService
						.isFeeCalculationRequiredForServiceType(inspection,
								inspection.getRegistration(),
								feeObject.getFeeCode(), inspection
										.getRegistration().getServiceType()
										.getCode())) {
					// feeDetailsExtnService.setFeeExtnService(feeExtnService);
					feeDetailsExtnService.calculateFeeByServiceType(feeObject,
							inspection.getRegistration(), inspection);
				}
			}
			if (!santionFeeList.isEmpty()) {

				registrationObj = bpaDmdCollExtnService
						.updateDemandByUsingSanctionFeeList(santionFeeList,
								inspection.getRegistration());
				oldStatus = inspection.getRegistration().getEgwStatus();
				registrationObj.setEgwStatus(bpaCommonExtnService
						.getstatusbyCode(BpaConstants.FEESCREATED));
				registrationObj.setIsSanctionFeeRaised(Boolean.TRUE);
				registrationObj.setFeeDate(sdf.format(new Date()));
				registrationObj = registrationFeeExtnService
						.saveFeesinRegistrationFee(registrationObj,
								santionFeeList);

				bpaCommonExtnService.createStatusChange(
						inspection.getRegistration(), oldStatus);

			}

		}

		if (inspection != null && bpaCitizenPortalService != null
				&& inspection.getRegistration() != null)
			// bpaCitizenPortalService.updateServiceRequestRegistry(inspection.getRegistration());
			// Todo Phionix
			addActionMessage("Inspection Details Saved Successfully");
		// bpaCommonExtnService.createStatusChange(inspection.getRegistration(),oldStatus);
		setMode(BpaConstants.MODEVIEW);
		if (inspection.getRegistration().getServiceType().getCode() != null
				&& inspection.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.RECLASSIFICATION)) {
			setServiceTypeCode(inspection.getRegistration().getServiceType()
					.getCode());
		}
		setInspectionId(inspection.getId());
		if (isUserMappedToSurveyorRole()) {
			setIsSurveyor(isUserMappedToSurveyorRole());
			if (inspection.getRegistration() != null) {
				setRequestID(inspection.getRegistration().getRequest_number());
				setServiceRegId(inspection.getRegistration()
						.getServiceRegistryId().toString());
			}
		}
		return SUCCESS;
	}

	private void buildDocumentFloorDetails() {
		List<String> temp = new ArrayList<String>();
		temp.add(null);
		docketFloorDetail.removeAll(temp);
		if (docketFloorDetail != null && docketFloorDetail.size() > 0) {
			for (DocketFloorDetails unitdetail : docketFloorDetail) {
				if (unitdetail.getFloorNum() != null
						&& !"".equals(unitdetail.getFloorNum()))
					unitdetail.setDocket(inspection.getDocket());
				inspection.getDocket().getDocketFlrDtlsSet().add(unitdetail);
			}
		}
	}

	@Action(value = "/inspectionExtn-showPlanDetails", results = { @Result(name = "plandetails",type = "dispatcher") })
	public String showPlanDetails() {
		buildPlanDetails();
		return "plandetails";
	}

	private void buildPlanDetails() {

		ServiceTypeExtn serviceType = inspectionExtnService
				.getServiceTypeById(getServiceTypeId());

		InspectMeasurementDtlsExtn planDetails = new InspectMeasurementDtlsExtn();
		InspectMeasurementDtlsExtn siteDetails = new InspectMeasurementDtlsExtn();
		planDetails
				.setInspectionSource(inspectionExtnService
						.getInspectionSource(BpaConstants.INSPECTIONSOURCEFOREXISTINGPLAN));
		siteDetails
				.setInspectionSource(inspectionExtnService
						.getInspectionSource(BpaConstants.INSPECTIONSOURCEFOREXISTINGSITE));
		planDetails.setHeader("Existing Building details as per Plan");

		siteDetails.setHeader("Existing Building details as per Site");
		if (getInspectionId() == null || getInspectionId().equals("")) {

			if (serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.APPLICATIONFORDEMOLITIONCODE)
					|| serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
					|| serviceType.getCode() != null
					&& serviceType.getCode().equals(BpaConstants.CMDACODE)
					|| serviceType.getCode().equals(
							BpaConstants.ADDITIONALCONSTRUCTIONCODE)) {
				inspectMeasurementDtls.add(planDetails);

				inspectMeasurementDtls.add(siteDetails);
			}

		} else {
			if (serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.APPLICATIONFORDEMOLITIONCODE)
					|| serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
					|| serviceType.getCode() != null
					&& serviceType.getCode().equals(BpaConstants.CMDACODE)
					|| serviceType.getCode().equals(
							BpaConstants.ADDITIONALCONSTRUCTIONCODE)) {
				for (InspectMeasurementDtlsExtn measuredtls : inspectionExtnService
						.getInspectionMeasureMentDtls(getInspectionId())) {
					if (measuredtls
							.getInspectionSource()
							.getCode()
							.equals(BpaConstants.INSPECTIONSOURCEFOREXISTINGPLAN)) {
						planDetails = measuredtls;
						planDetails
								.setHeader("Existing Building details as per Plan");

						inspectMeasurementDtls.add(planDetails);
					} else if (measuredtls
							.getInspectionSource()
							.getCode()
							.equals(BpaConstants.INSPECTIONSOURCEFOREXISTINGSITE)) {
						siteDetails = measuredtls;
						siteDetails
								.setHeader("Existing Building details as per Site");
						inspectMeasurementDtls.add(siteDetails);
					}
				}
				if (inspectMeasurementDtls.size() == 0) {
					inspectMeasurementDtls.add(planDetails);
					inspectMeasurementDtls.add(siteDetails);
				}

			}
		}

	}

	@Action(value = "/inspectionExtn-showConstructionDetails", results = { @Result(name = "constructiondetails",type = "dispatcher") })
	public String showConstructionDetails() {
		buildConstructionDetails();
		return "constructiondetails";
	}

	private void buildConstructionDetails() {

		ServiceTypeExtn serviceType = inspectionExtnService
				.getServiceTypeById(getServiceTypeId());
		InspectMeasurementDtlsExtn planDetails = new InspectMeasurementDtlsExtn();
		InspectMeasurementDtlsExtn siteDetails = new InspectMeasurementDtlsExtn();
		planDetails
				.setInspectionSource(inspectionExtnService
						.getInspectionSource(BpaConstants.INSPECTIONSOURCEFORCONSTRUCTIONPLAN));
		siteDetails
				.setInspectionSource(inspectionExtnService
						.getInspectionSource(BpaConstants.INSPECTIONSOURCEFORCONSTRUCTIONSITE));

		if (getInspectionId() == null || getInspectionId().equals("")) {

			if (serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.RECLASSIFICATION)) {
				siteDetails.setHeader("Construction details as per Site");
				setServiceTypeCode(BpaConstants.RECLASSIFICATION);
				constructionMesDtls.add(siteDetails);
			}

			else if (serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.SUBDIVISIONOFLANDCODE)
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.LAYOUTAPPPROVALCODE)
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.APPLICATIONFORDEMOLITIONCODE)) {
				planDetails.setHeader("Construction details as per Plan");
				constructionMesDtls.add(planDetails);
				siteDetails.setHeader("Construction details as per Site");
				constructionMesDtls.add(siteDetails);
			}
		} else {
			for (InspectMeasurementDtlsExtn measuredtls : inspectionExtnService
					.getInspectionMeasureMentDtls(getInspectionId())) {

				if (measuredtls
						.getInspectionSource()
						.getCode()
						.equals(BpaConstants.INSPECTIONSOURCEFORCONSTRUCTIONPLAN)) {
					planDetails = measuredtls;
				} else if (measuredtls
						.getInspectionSource()
						.getCode()
						.equals(BpaConstants.INSPECTIONSOURCEFORCONSTRUCTIONSITE)) {
					siteDetails = measuredtls;
				}
			}
			inspection = inspectionExtnService
					.getInspectionbyId(getInspectionId());
			if (serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.RECLASSIFICATION)) {
				siteDetails.setHeader("Construction details as per Site");
				setServiceTypeCode(BpaConstants.RECLASSIFICATION);
				constructionMesDtls.add(siteDetails);
			} else if (serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.SUBDIVISIONOFLANDCODE)
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.LAYOUTAPPPROVALCODE)
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.APPLICATIONFORDEMOLITIONCODE)) {
				planDetails.setHeader("Construction details as per Plan");
				constructionMesDtls.add(planDetails);
				siteDetails.setHeader("Construction details as per Site");
				constructionMesDtls.add(siteDetails);
			}
		}
	}

	@Action(value = "/inspectionExtn-showCheckList", results = { @Result(name = "checklist",type = "dispatcher") })
	public String showCheckList() {
		buildChecklist();
		return "checklist";
	}

	public void buildChecklist() {

		ServiceTypeExtn serviceType = inspectionExtnService
				.getServiceTypeById(getServiceTypeId());
		if (getInspectionId() == null || getInspectionId().equals("")) {
			if (serviceType != null
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.SUBDIVISIONOFLANDCODE)
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.LAYOUTAPPPROVALCODE)) {
				List<CheckListDetailsExtn> chkListDetails = inspectionExtnService
						.getCheckListforService(serviceType.getId());
				for (CheckListDetailsExtn chkList : chkListDetails) {
					InspectionChecklistExtn inspectionchk = new InspectionChecklistExtn();
					inspectionchk.setChecklistDetails(chkList);
					chkListDet.add(inspectionchk);
				}
			}
		} else {
			if (serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.SUBDIVISIONOFLANDCODE)
					&& serviceType.getCode() != null
					&& !serviceType.getCode().equals(
							BpaConstants.LAYOUTAPPPROVALCODE)) {
				chkListDet = inspectionExtnService
						.getInspectionCheckDtls(getInspectionId());
			}
		}

	}

	@Action(value = "/inspectionExtn-viewInspectionForm", results = { @Result(name = "inspectionForm",type = "dispatcher") })
	public String viewInspectionForm() {

		inspection = inspectionExtnService.getInspectionbyId(getInspectionId());

		if (!isUserMappedToSurveyorRole()
				&& inspection.getRegistration() != null
				&& inspection.getRegistration().getServiceType() != null
				&& inspection.getRegistration().getServiceType().getCode() != null
				&& (inspection.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE) || inspection
						.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE))) {
			buildDocketObjectDetails();
		}
		setMode(BpaConstants.MODEVIEW);
		return "inspectionForm";
	}

	@Action(value = "/inspectionExtn-modifyInspectionForm", results = { @Result(name = "inspectionForm",type = "dispatcher") })
	public String modifyInspectionForm() {
		inspection = inspectionExtnService.getInspectionbyId(getInspectionId());
		if (!isUserMappedToSurveyorRole()
				&& inspection.getRegistration() != null
				&& inspection.getRegistration().getServiceType() != null
				&& inspection.getRegistration().getServiceType().getCode() != null
				&& (inspection.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.NEWBUILDINGONVACANTPLOTCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.DEMOLITIONRECONSTRUCTIONCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.ADDITIONALCONSTRUCTIONCODE)
						|| inspection
								.getRegistration()
								.getServiceType()
								.getCode()
								.equals(BpaConstants.APPLICATIONFORDEMOLITIONCODE) || inspection
						.getRegistration().getServiceType().getCode()
						.equals(BpaConstants.CMDACODE))) {
			buildDocketObjectDetails();
		}
		setMode("modify");
		setServiceTypeCode(inspection.getRegistration().getServiceType()
				.getCode());
		return "inspectionForm";
	}

	@Action(value = "/inspectionExtn-showMeasurementDetails", results = { @Result(name = "measurement",type = "dispatcher") })
	public String showMeasurementDetails() {
		setServiceTypeCode(registrationObj.getServiceType().getCode());
		buildPlanDetails();
		buildConstructionDetails();
		showCheckList();
		showPlotDetails();
		if (inspection == null && getInspectionId() != null)
			inspection = inspectionExtnService
					.getInspectionbyId(getInspectionId());
		/*
		 * if(inspection!=null){//TODO PHIONIX
		 * if(inspection.getInspectedBy()!=null &&
		 * (inspection.getInspectedBy().getIsPortalUser()!=null &&
		 * inspection.getInspectedBy().getIsPortalUser()==Boolean.TRUE))
		 * isSurveyor=true; }
		 */
		return "measurement";
	}

	@Action(value = "/inspectionExtn-showSurveyorMeasurementDetails", results = { @Result(name = "measurement",type = "dispatcher") })
	public String showSurveyorMeasurementDetails() {
		setServiceTypeCode(registrationObj.getServiceType().getCode());
		List<InspectionExtn> survyrInspList = inspectionExtnService
				.getSiteInspectionListforRegBySurveyorAndOfficial(registrationObj);
		for (InspectionExtn insExtn : survyrInspList) {
			/*
			 * for(Role role : insExtn.getInspectedBy().getRoles())
			 * if(role.getRoleName()!=null &&
			 * role.getRoleName().equalsIgnoreCase
			 * (BpaConstants.PORTALUSERSURVEYORROLE)) {
			 * setInspectionId(insExtn.getId()); break; }
			 */// TODO PHIONIX
		}

		buildPlanDetails();
		buildConstructionDetails();
		showCheckList();
		showPlotDetails();
		return "measurement";
	}

	@Action(value = "/inspectionExtn-showExistingInspectionMeasurementDetails", results = { @Result(name = "existingmeasurement",type = "dispatcher") })
	public String showExistingInspectionMeasurementDetails() {

		if (EgovThreadLocals.getUserId() != null) {
			List<String> roleList = bpaCommonExtnService
					.getRoleNamesByPassingUserId((EgovThreadLocals.getUserId()));
			if (!BpaExtnRuleBook.getInstance().checkViewsforRoles(roleList,
					BpaConstants.INSPECTIONDETAILS)) {
				returnStream = returnStream
						.concat(" Inspection Measurement Details");
				return BpaConstants.NOACCESS;
			}
		}

		if (registrationObj != null) {
			existingSiteInspectionDetails = inspectionExtnService
					.getSiteInspectionListforRegBySurveyorAndOfficial(registrationObj);
			setServiceTypeCode(registrationObj.getServiceType().getCode());
			for (InspectionExtn inspectiondetails : existingSiteInspectionDetails) {
				if (inspectiondetails != null
						&& inspectiondetails.getIsInspected().equals(
								Boolean.TRUE)) {
					isInspectedByAEorAEE = bpaCommonExtnService.ShowUserROles(
							inspectiondetails.getCreatedBy().getRoles(),
							BpaConstants.BPAAEROLE, BpaConstants.BPAAEEROLE);
					if (isInspectedByAEorAEE.equals(true)) {
						break;
					}
				}
			}

		}
		setMode(BpaConstants.MODEVIEW);

		if (registrationObj != null && registrationObj.getEgwStatus() != null) {
			if (registrationObj.getEgwStatus().getCode()
					.equalsIgnoreCase(BpaConstants.INSPECTEDBYLS)
					&& isUserMappedToSurveyorRole())
				allowModifyInspection = true;
			if (registrationObj.getEgwStatus().getCode()
					.equalsIgnoreCase(BpaConstants.FILECONSIDERATIONCHECKED)
					&& (isUserMappedToSelectedRole(BpaConstants.BPAAEEROLE) || isUserMappedToSelectedRole(BpaConstants.BPAAEROLE)))
				allowModifyInspection = true;
		}

		return "existingmeasurement";
	}

	public String showPlotDetails() {
		buildPlotDetails();
		return "plotdetails";
	}

	public void buildPlotDetails() {

		ServiceTypeExtn serviceType = inspectionExtnService
				.getServiceTypeById(getServiceTypeId());
		if (getInspectionId() == null || getInspectionId().equals("")) {
			if ((serviceType.getCode() != null && serviceType.getCode().equals(
					BpaConstants.SUBDIVISIONOFLANDCODE))
					|| ((serviceType.getCode() != null && serviceType.getCode()
							.equals(BpaConstants.LAYOUTAPPPROVALCODE))))
				setPlotDetails("TRUE");
			else
				setPlotDetails("FALSE");
		} else {

			if (serviceType.getCode() != null
					&& serviceType.getCode().equals(
							BpaConstants.SUBDIVISIONOFLANDCODE)
					|| (serviceType.getCode() != null && serviceType.getCode()
							.equals(BpaConstants.LAYOUTAPPPROVALCODE)))
				setPlotDetails("TRUE");
			else
				setPlotDetails("FALSE");

			inspection = inspectionExtnService
					.getInspectionbyId(getInspectionId());
		}

	}

	public String getAutoGenInspectionDates() {
		return autoGenInspectionDates;
	}

	public void setAutoGenInspectionDates(String autoGenInspectionDates) {
		this.autoGenInspectionDates = autoGenInspectionDates;
	}

	public String getSiteInspectionScheduledates() {
		return siteInspectionScheduledates;
	}

	public void setSiteInspectionScheduledates(
			String siteInspectionScheduledates) {
		this.siteInspectionScheduledates = siteInspectionScheduledates;
	}

	public String getIsmodify() {
		return ismodify;
	}

	public void setIsmodify(String ismodify) {
		this.ismodify = ismodify;
	}

	public BpaCitizenPortalExtnService getBpaCitizenPortalExtnService() {
		return bpaCitizenPortalService;
	}

	public void setBpaCitizenPortalExtnService(
			BpaCitizenPortalExtnService bpaCitizenPortalService) {
		this.bpaCitizenPortalService = bpaCitizenPortalService;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public Map<String, String> getBuildingTypeMap() {
		return buildingTypeMap;
	}

	public void setBuildingTypeMap(Map<String, String> buildingTypeMap) {
		this.buildingTypeMap = buildingTypeMap;
	}

	public String getLastInspectionDate() {
		return lastInspectionDate;
	}

	public void setLastInspectionDate(String lastInspectionDate) {
		this.lastInspectionDate = lastInspectionDate;
	}

	public Long getConstructionstage() {
		return constructionstage;
	}

	public void setConstructionstage(Long constructionstage) {
		this.constructionstage = constructionstage;
	}

	public EgwStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(EgwStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public void setReturnStream(String returnStream) {
		this.returnStream = returnStream;
	}

	public BpaCommonExtnService getBpaCommonExtnService() {
		return bpaCommonExtnService;
	}

	public void setBpaCommonExtnService(
			BpaCommonExtnService bpaCommonExtnService) {
		this.bpaCommonExtnService = bpaCommonExtnService;
	}

	public String getFromreg() {
		return fromreg;
	}

	public void setFromreg(String fromreg) {
		this.fromreg = fromreg;
	}

	public String getPlotDetails() {
		return plotDetails;
	}

	public void setPlotDetails(String plotDetails) {
		this.plotDetails = plotDetails;
	}

	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberExtnGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberExtnGenerationService;
	}

	public List<InspectionExtn> getExistingSiteInspectionDetails() {
		return existingSiteInspectionDetails;
	}

	public void setExistingSiteInspectionDetails(
			List<InspectionExtn> existingSiteInspectionDetails) {
		this.existingSiteInspectionDetails = existingSiteInspectionDetails;
	}

	private Long inspectionId;

	public Long getInspectionId() {
		return inspectionId;
	}

	public void setInspectionId(Long inspectionId) {
		this.inspectionId = inspectionId;
	}

	public List<InspectionChecklistExtn> getChkListDet() {
		return chkListDet;
	}

	public void setChkListDet(List<InspectionChecklistExtn> chkListDet) {
		this.chkListDet = chkListDet;
	}

	public List<InspectMeasurementDtlsExtn> getConstructionMesDtls() {
		return constructionMesDtls;
	}

	public void setConstructionMesDtls(
			List<InspectMeasurementDtlsExtn> constructionMesDtls) {
		this.constructionMesDtls = constructionMesDtls;
	}

	public List<InspectMeasurementDtlsExtn> getInspectMeasurementDtls() {
		return inspectMeasurementDtls;
	}

	public void setInspectMeasurementDtls(
			List<InspectMeasurementDtlsExtn> InspectMeasurementDtlsExtn) {
		this.inspectMeasurementDtls = InspectMeasurementDtlsExtn;
	}

	public String getVerifyDate() {
		return sdf.format(verifyDate);
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public InspectionExtnService getInspectionExtnService() {
		return inspectionExtnService;
	}

	public void setInspectionExtnService(
			InspectionExtnService inspectionExtnService) {
		this.inspectionExtnService = inspectionExtnService;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public RegistrationExtn getRegistrationObj() {
		return registrationObj;
	}

	public void setRegistrationObj(RegistrationExtn registrationObj) {
		this.registrationObj = registrationObj;
	}

	public List<InspectionExtn> getPostponedInspectionDetails() {
		return postponedInspectionDetails;
	}

	public void setPostponedInspectionDetails(
			List<InspectionExtn> postponedInspectionDetails) {
		this.postponedInspectionDetails = postponedInspectionDetails;
	}

	public InspectionExtn getInspection() {
		return inspection;
	}

	public void setInspection(InspectionExtn inspection) {
		this.inspection = inspection;
	}

	public Long getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Long serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public InputStream getReturnStream() {
		return new ByteArrayInputStream(returnStream.getBytes());

	}

	public Map<String, String> getLandZoningMap() {
		return landZoningMap;
	}

	public void setLandZoningMap(Map<String, String> landZoningMap) {
		this.landZoningMap = landZoningMap;
	}

	public Map<String, String> getBldngZoningMap() {
		return bldngZoningMap;
	}

	public void setBldngZoningMap(Map<String, String> bldngZoningMap) {
		this.bldngZoningMap = bldngZoningMap;
	}

	public List<Integer> getPenaltyPeriodLst() {
		return penaltyPeriodLst;
	}

	public void setPenaltyPeriodLst(List<Integer> penaltyPeriodLst) {
		this.penaltyPeriodLst = penaltyPeriodLst;
	}

	public FeeDetailsExtnService getFeeDetailsExtnService() {
		return feeDetailsExtnService;
	}

	public void setFeeDetailsExtnService(
			FeeDetailsExtnService feeDetailsExtnService) {
		this.feeDetailsExtnService = feeDetailsExtnService;
	}

	public RegistrationFeeExtnService getRegistrationFeeExtnService() {
		return registrationFeeExtnService;
	}

	public void setRegistrationFeeExtnService(
			RegistrationFeeExtnService registrationFeeService) {
		this.registrationFeeExtnService = registrationFeeService;
	}

	public Boolean getIsSurveyor() {
		return isSurveyor;
	}

	public void setIsSurveyor(Boolean isSurveyor) {
		this.isSurveyor = isSurveyor;
	}

	public Map<String, String> getApplicantStatus() {
		return applicantStatus;
	}

	public void setApplicantStatus(Map<String, String> applicantStatus) {
		this.applicantStatus = applicantStatus;
	}

	public List<DocketDocumentDetails> getDocketDocumentDtls() {
		return docketDocumentDtls;
	}

	public void setDocketDocumentDtls(
			List<DocketDocumentDetails> docketDocumentDtls) {
		this.docketDocumentDtls = docketDocumentDtls;
	}

	public List<DocketConstructionStage> getConstructionStages() {
		return constructionStages;
	}

	public void setConstructionStages(
			List<DocketConstructionStage> constructionStages) {
		this.constructionStages = constructionStages;
	}

	public List<DocketViolations> getDevContrlList() {
		return devContrlList;
	}

	public void setDevContrlList(List<DocketViolations> devContrlList) {
		this.devContrlList = devContrlList;
	}

	public List<DocketViolations> getSetBackList() {
		return setBackList;
	}

	public void setSetBackList(List<DocketViolations> setBackList) {
		this.setBackList = setBackList;
	}

	public List<DocketViolations> getParkingList() {
		return parkingList;
	}

	public void setParkingList(List<DocketViolations> parkingList) {
		this.parkingList = parkingList;
	}

	public List<DocketViolations> getGeneralList() {
		return generalList;
	}

	public void setGeneralList(List<DocketViolations> generalList) {
		this.generalList = generalList;
	}

	public List<DocketViolations> getMinDistancePowerLineList() {
		return minDistancePowerLineList;
	}

	public void setMinDistancePowerLineList(
			List<DocketViolations> minDistancePowerLineList) {
		this.minDistancePowerLineList = minDistancePowerLineList;
	}

	public RegisterBpaExtnService getRegisterBpaExtnService() {
		return registerBpaExtnService;
	}

	public void setRegisterBpaExtnService(
			RegisterBpaExtnService registerBpaExtnService) {
		this.registerBpaExtnService = registerBpaExtnService;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public void setFloorNoMap(Map<Integer, String> floorNoMap) {
		this.floorNoMap = floorNoMap;
	}

	public List<DocketFloorDetails> getDocketFloorDetail() {
		return docketFloorDetail;
	}

	public void setDocketFloorDetail(List<DocketFloorDetails> docketFloorDetail) {
		this.docketFloorDetail = docketFloorDetail;
	}

	public String getServiceRegId() {
		return serviceRegId;
	}

	public void setServiceRegId(String serviceRegId) {
		this.serviceRegId = serviceRegId;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

}
