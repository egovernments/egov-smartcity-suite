/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.notice;

import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_BASICPROPID;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.demand.model.EgDemandDetails;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.bean.PropertyNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.report.bean.PropertyAckNoticeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@ParentPackage("egov")
@Transactional(readOnly = true)
@Namespace("/notice")
@ResultPath("/WEB-INF/jsp/")
@Results({ @Result(name = PropertyTaxNoticeAction.NOTICE, location = "notice/propertyTaxNotice-notice.jsp") })
public class PropertyTaxNoticeAction extends PropertyTaxBaseAction {
	private static final Logger LOGGER = Logger.getLogger(PropertyTaxNoticeAction.class);
	public static final String NOTICE = "notice";
	private PropertyImpl property;
	private ReportService reportService;
	private NoticeService noticeService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private Integer reportId = -1;
	private String noticeType;
	private InputStream NoticePDF;
	private Long basicPropId;
	private String noticeMode;
	private PersistenceService<BasicProperty, Long> basicPropertyService;

	@Autowired
	private PtDemandDao ptDemandDAO;

	public PropertyTaxNoticeAction() {
	}

	@Override
	public Object getModel() {
		return null;
	}

	@Action(value = "/propertyTaxNotice-generateNotice")
	public String generateNotice() {
		Map reportParams = new HashMap<String, Object>();
		ReportRequest reportInput = null;
		BasicPropertyImpl basicProperty = (BasicPropertyImpl) getPersistenceService().findByNamedQuery(
				QUERY_BASICPROPERTY_BY_BASICPROPID, basicPropId);
		property = (PropertyImpl) basicProperty.getProperty();

		if (property == null) {
			property = (PropertyImpl) basicProperty.getWFProperty();
		}

		PropertyNoticeInfo propertyNotice = null;
		String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(noticeType);
		propertyNotice = new PropertyNoticeInfo(property, noticeNo);

		if (PropertyTaxConstants.NOTICE6.equals(noticeType)) {
			HttpServletRequest request = ServletActionContext.getRequest();
			String url = WebUtils.extractRequestDomainURL(request, false);
			String imagePath = url.concat(PropertyTaxConstants.IMAGES_BASE_PATH).concat(ReportUtil.fetchLogo());
			reportParams.put("logoPath", imagePath);
			if (noticeMode.equalsIgnoreCase("create")) {
				reportParams.put("mode", "create");
			} else {
				reportParams.put("mode", "modify");
			}
			setNoticeInfo(propertyNotice, basicProperty);
			List<PropertyAckNoticeInfo> floorDetails = getFloorDetailsForNotice(propertyNotice.getOwnerInfo()
					.getTotalTax());
			propertyNotice.setFloorDetailsForNotice(floorDetails);
			reportInput = new ReportRequest(PropertyTaxConstants.REPORT_TEMPLATENAME_NOTICE6, propertyNotice,
					reportParams);
		}
		reportInput.setPrintDialogOnOpenReport(true);
		reportInput.setReportFormat(FileFormat.PDF);
		ReportOutput reportOutput = reportService.createReport(reportInput);
		reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
		if (reportOutput != null && reportOutput.getReportOutputData() != null) {
			NoticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
		}
		noticeService.saveNotice(noticeNo, noticeType, basicProperty, NoticePDF);
		endWorkFlow(basicProperty);
		basicPropertyService.update(basicProperty);
		return NOTICE;
	}

	private void setNoticeInfo(PropertyNoticeInfo propertyNotice, BasicPropertyImpl basicProperty) {
		PropertyAckNoticeInfo ownerInfo = new PropertyAckNoticeInfo();
		Address ownerAddress = basicProperty.getAddress();

		if (basicProperty.getPropertyOwnerInfo().size() > 1) {
			ownerInfo.setOwnerName(basicProperty.getFullOwnerName().concat(" and others"));
		} else {
			ownerInfo.setOwnerName(basicProperty.getFullOwnerName());
		}

		ownerInfo.setOwnerAddress(basicProperty.getAddress().toString());
		ownerInfo.setApplicationNo(basicProperty.getApplicationNo());
		ownerInfo.setDoorNo(ownerAddress.getHouseNoBldgApt());
		if (StringUtils.isNotBlank(ownerAddress.getLandmark())) {
			ownerInfo.setStreetName(ownerAddress.getLandmark());
		} else {
			ownerInfo.setStreetName("N/A");
		}
		SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
		String occupancyYear = formatNowYear.format(basicProperty.getPropOccupationDate());
		ownerInfo.setInstallmentYear(occupancyYear);
		ownerInfo.setAssessmentNo(basicProperty.getUpicNo());
		Ptdemand currDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
		BigDecimal totalTax = BigDecimal.ZERO;
		for (EgDemandDetails demandDetail : currDemand.getEgDemandDetails()) {
			totalTax = totalTax.add(demandDetail.getAmount());
			if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().toUpperCase()
					.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS)) {
				ownerInfo.setEducationTax(demandDetail.getAmount());
			}
			if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().toUpperCase()
					.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS)) {
				ownerInfo.setLibraryTax(demandDetail.getAmount());
			}
			if (demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster().toUpperCase()
					.equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX)) {
				ownerInfo.setGeneralTax(demandDetail.getAmount());
			}
		}
		ownerInfo.setTotalTax(totalTax);
		PropertyID propertyId = basicProperty.getPropertyID();
		ownerInfo.setZoneName(propertyId.getZone().getName());
		ownerInfo.setWardName(propertyId.getWard().getName());
		ownerInfo.setAreaName(propertyId.getArea().getName());
		ownerInfo.setLocalityName(propertyId.getLocality().getName());
		ownerInfo.setNoticeDate(new Date());

		propertyNotice.setOwnerInfo(ownerInfo);
	}

	private List<PropertyAckNoticeInfo> getFloorDetailsForNotice(BigDecimal totalTax) {
		List<PropertyAckNoticeInfo> floorDetailsList = new ArrayList<PropertyAckNoticeInfo>();
		PropertyDetail detail = property.getPropertyDetail();
		PropertyAckNoticeInfo floorInfo = null;
		for (Floor floor : detail.getFloorDetails()) {
			floorInfo = new PropertyAckNoticeInfo();
			floorInfo.setBuildingClassification(floor.getStructureClassification().getTypeName());
			floorInfo.setNatureOfUsage(floor.getPropertyUsage().getUsageName());
			floorInfo.setPlinthArea(new BigDecimal(floor.getBuiltUpArea().getArea()));
			floorInfo.setBuildingAge(floor.getDepreciationMaster().getDepreciationName());
			floorInfo.setMonthlyRentalValue(BigDecimal.ZERO);
			floorInfo.setYearlyRentalValue(BigDecimal.ZERO);
			floorInfo.setTaxPayableForCurrYear(totalTax);
			floorInfo.setTaxPayableForNewRates(BigDecimal.ZERO);

			floorDetailsList.add(floorInfo);
		}
		return floorDetailsList;
	}

	/**
	 * This method ends the workflow. The Property is transitioned to END state.
	 */
	private void endWorkFlow(BasicPropertyImpl basicProperty) {
		LOGGER.debug("endWorkFlow: Workflow will end for Property: " + property);
		// FIX ME
		// Position position =
		// eisCommonsManager.getPositionByUserId(Integer.valueOf(EgovThreadLocals.getUserId()));
		// Position position = null;
		property.transition().end();
		basicProperty.setUnderWorkflow(false);
		LOGGER.debug("Exit method endWorkFlow, Workflow ended");
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

	public PropertyImpl getProperty() {
		return property;
	}

	public void setProperty(PropertyImpl property) {
		this.property = property;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public Long getBasicPropId() {
		return basicPropId;
	}

	public void setBasicPropId(Long basicPropId) {
		this.basicPropId = basicPropId;
	}

	public String getNoticeMode() {
		return noticeMode;
	}

	public void setNoticeMode(String noticeMode) {
		this.noticeMode = noticeMode;
	}

	public void setBasicPropertyService(PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicPropertyService = basicPropertyService;
	}

	public void setPtDemandDAO(PtDemandDao ptDemandDAO) {
		this.ptDemandDAO = ptDemandDAO;
	}

}
