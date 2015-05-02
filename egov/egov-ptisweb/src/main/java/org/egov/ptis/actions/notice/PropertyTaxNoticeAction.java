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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.MUTATION_CERTIFICATE_NOTICENO_PREFIX;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE125;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE127;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE134;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_PRATIVRUTTA;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASICPROPERTY_BY_BASICPROPID;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_MUTATION_CERTIFICATE;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_NOTICE125;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_NOTICE127;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_NOTICE134;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_NOTICE_PRATIVRUTTA;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_GENERATE_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_NOTICE_GENERATION_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.reporting.engine.ReportOutput;
import org.egov.infstr.reporting.engine.ReportRequest;
import org.egov.infstr.reporting.engine.ReportService;
import org.egov.infstr.reporting.viewer.ReportViewerUtil;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.StringUtils;
import org.egov.pims.commons.Position;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.bean.PropertyMutationInfo;
import org.egov.ptis.bean.PropertyNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.springframework.transaction.annotation.Transactional;

@ParentPackage("egov")
@Transactional(readOnly = true)
@Namespace("/notice")
public class PropertyTaxNoticeAction extends PropertyTaxBaseAction {
	private static final Logger LOGGER = Logger.getLogger(PropertyTaxNoticeAction.class);
	public static final String NOTICE = "notice";
	private static final String YES = "Yes";

	private PropertyImpl property;
	private ReportService reportService;
	private NoticeService noticeService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private WorkflowService<PropertyImpl> propertyWorkflowService;
	private PersistenceService<Property, Long> propertyImplService;
	private Integer reportId = -1;
	private String noticeType;
	private String indexNumber;
	private InputStream NoticePDF;
	private Boolean isPreviewPVR; 
	private Long basicPropId;
	
	public PropertyTaxNoticeAction() {
	}

	@Override
	public Object getModel() {
		return null;
	}

	@Action(value = "/propertyTaxNotice-generateNotice", results = { @Result(name = NOTICE) })
	public String generateNotice() {
		LOGGER.debug("Entered into generateNotice method");
		LOGGER.info("Index Number : " + indexNumber+" Notice Type : " + noticeType
				+" BasicPropertyId : " + basicPropId);
		Boolean saveToDMS = FALSE;
		Map reportParams = new HashMap<String, Object>();
		ReportRequest reportInput = null;
		Integer userId = (Integer) session().get(SESSIONLOGINID);
		BasicPropertyImpl basicProperty = (BasicPropertyImpl) getPersistenceService().findByNamedQuery(
				QUERY_BASICPROPERTY_BY_BASICPROPID, basicPropId);
		property = (PropertyImpl) basicProperty.getProperty();
		
		if(property == null) {
			property = (PropertyImpl) basicProperty.getWFProperty();
		}
		
		LOGGER.debug("BasicProperty : " + basicProperty);
		LOGGER.debug("Property : " + property);
		// Notice 125 need not to be saved
		if (!NOTICE125.equals(noticeType)) {
			saveToDMS = TRUE;
		}
		PropertyNoticeInfo propertyNotice = null;
		String noticeNo = null;
		if (!MUTATION_CERTIFICATE.equals(noticeType)) {
			if(!isPreviewPVR) {
				noticeNo= propertyTaxNumberGenerator.generateNoticeNumber(noticeType);
			}
	        Boolean instwiseNoticeReport=TRUE;
	        LOGGER.debug("Notice number generated : " + noticeNo);
			propertyNotice = new PropertyNoticeInfo(property, noticeNo, instwiseNoticeReport);
		}

		if (NOTICE127.equals(noticeType)) {
			reportInput = new ReportRequest(REPORT_TEMPLATENAME_NOTICE127, propertyNotice, reportParams);
		} else if (NOTICE134.equals(noticeType)) {
			reportInput = new ReportRequest(REPORT_TEMPLATENAME_NOTICE134, propertyNotice, reportParams);
		} else if (NOTICE_PRATIVRUTTA.equals(noticeType)) {
			reportInput = new ReportRequest(REPORT_TEMPLATENAME_NOTICE_PRATIVRUTTA, propertyNotice, reportParams);
		} else if (NOTICE125.equals(noticeType)) {
			reportInput = new ReportRequest(REPORT_TEMPLATENAME_NOTICE125, propertyNotice, reportParams);
		} else if (MUTATION_CERTIFICATE.equals(noticeType)) {
			PropertyMutation propMutation = getLatestPropMutation(basicProperty);
			PropertyMutationInfo propMutationInfo = populatePropMutationInfo(propMutation, basicProperty);
			reportInput = new ReportRequest(REPORT_TEMPLATENAME_MUTATION_CERTIFICATE, propMutationInfo, null);
			noticeNo= MUTATION_CERTIFICATE_NOTICENO_PREFIX + propMutation.getApplicationNo();
			LOGGER.debug("Mutation Certificate number generated : " + noticeNo);
		}

		reportInput.setPrintDialogOnOpenReport(true);
		reportId = ReportViewerUtil.addReportToSession(reportService.createReport(reportInput), getSession());
		
		//The following code must be executed if its not Preview Prativrutta
		if(!isPreviewPVR) {
			ReportOutput reportOutput = reportService.createReport(reportInput);
			if (reportOutput != null && reportOutput.getReportOutputData() != null) {
				NoticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
			}
			
			if (saveToDMS) {
				noticeService.saveNotice(noticeNo, noticeType, basicProperty, NoticePDF);
			}
			if (NOTICE127.equals(noticeType) || NOTICE134.equals(noticeType) || MUTATION_CERTIFICATE.equals(noticeType)) {
				property.setExtra_field3(YES);
			}
			if (NOTICE_PRATIVRUTTA.equals(noticeType)) {
				property.setExtra_field4(YES);
			}
			//for mutation Prativrutta generation is not required 
			//as migrated property does not have enough data to generate it.
			if (MUTATION_CERTIFICATE.equals(noticeType)) { 
				if (YES.equals(property.getExtra_field3())) {
					workflowBean.setActionName(WFLOW_ACTION_NAME_GENERATE_NOTICE + ":"
							+ WFLOW_ACTION_STEP_NOTICE_GENERATED);// <actionname>:<wflowstep>
					transitionWorkFlow();
				}
			} else {
				if (!NOTICE125.equals(noticeType)) {
					if (YES.equals(property.getExtra_field3()) && YES.equals(property.getExtra_field4())
							&& property.getState().getValue().endsWith(WF_STATE_NOTICE_GENERATION_PENDING)) {
						if (property.getState().getValue().contains(WFLOW_ACTION_NAME_MODIFY)
								&& PROPERTY_MODIFY_REASON_OBJ.equals(property.getPropertyDetail()
										.getPropertyMutationMaster().getCode())) {
							if (YES.equals(property.getExtra_field5())) {
								workflowBean.setActionName(WFLOW_ACTION_NAME_GENERATE_NOTICE + ":"
										+ WFLOW_ACTION_STEP_NOTICE_GENERATED);// <actionname>:<wflowstep>
								transitionWorkFlow();
							}
						} else {
							workflowBean.setActionName(WFLOW_ACTION_NAME_GENERATE_NOTICE + ":"
									+ WFLOW_ACTION_STEP_NOTICE_GENERATED);// <actionname>:<wflowstep>
							transitionWorkFlow();
						}
					}
				}
			}
		}
		
		LOGGER.debug("Exit from generateNotice method");
		return NOTICE;
	}
	
	private PropertyMutationInfo populatePropMutationInfo(PropertyMutation propMutation, BasicPropertyImpl basicProperty) {
		PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
		PropertyMutationInfo propMutationInfo = new PropertyMutationInfo();
		propMutationInfo.setReceiptNo(propMutation.getReceiptNum());
		propMutationInfo.setApplicationDate(DateUtils.getDefaultFormattedDate(propMutation.getNoticeDate()));
		if (propMutation.getDeedDate() != null) {
			propMutationInfo.setDocumentDate(DateUtils.getDefaultFormattedDate(propMutation.getDeedDate()));
		}
		propMutationInfo.setNoticeDate(DateUtils.getDefaultFormattedDate(new Date()));
		propMutationInfo.setZoneNo(StringUtils.leftPad(basicProperty.getPropertyID().getZone().getBoundaryNum().toString(), 2, '0'));
		propMutationInfo.setZoneName(basicProperty.getPropertyID().getZone().getName());
		propMutationInfo.setWardNo(StringUtils.leftPad(basicProperty.getPropertyID().getWard().getBoundaryNum().toString(), 2, '0'));
		propMutationInfo.setHouseNo(basicProperty.getAddress().getHouseNoBldgApt());
		propMutationInfo.setNewOwnerName(ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
		propMutationInfo.setOldOwnerName(propMutation.getOwnerNameOld());
		propMutationInfo.setAddress(ptisCacheMgr.buildAddress(basicProperty));
		return propMutationInfo;
	}

	private PropertyMutation getLatestPropMutation(BasicPropertyImpl basicProperty) {
		PropertyMutation propertyMutation = null;
		Date tempDate = null;
		for (PropertyMutation propMutation : basicProperty.getPropMutationSet()) {
			if (tempDate == null) {
				tempDate = propMutation.getCreatedDate();
				propertyMutation = propMutation;
			} else {
				if (propMutation.getCreatedDate().after(tempDate)) {
					propertyMutation = propMutation;
					tempDate = propMutation.getCreatedDate();
				}
			}
		}
		return propertyMutation;
	}

	/**
	 * This method ends the workflow. The Property is transitioned to END state.
	 */
	private void endWorkFlow() {
		LOGGER.debug("Enter method endWorkFlow, UserId: " + EGOVThreadLocals.getUserId());
		LOGGER.debug("endWorkFlow: Workflow will end for Property: " + property);
		//FIX ME
		//Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		Position position = null;
		property.transition(true).start().withOwner(position).withComments("Property Workflow End");
		LOGGER.debug("Exit method endWorkFlow, Workflow ended");
	}
	
	private void transitionWorkFlow() {
		if (workflowBean != null) {
			LOGGER.debug("Entered method : transitionWorkFlow. Action : " + workflowBean.getActionName() + "Property: "
					+ property);
		} else {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		}

		String wflowAction = null;
		String beanActionName[] = workflowBean.getActionName().split(":");
		if (beanActionName.length > 1) {
			wflowAction = beanActionName[1];// save or forward or approve
		}
		if (WFLOW_ACTION_STEP_NOTICE_GENERATED.equalsIgnoreCase(wflowAction)) {
			endWorkFlow();
		}

		LOGGER.debug("transitionWorkFlow: Property transitioned to " + property.getState().getValue());
		propertyImplService.persist(property);

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}
	
	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
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

	public void setPropertyWorkflowService(WorkflowService<PropertyImpl> propertyWorkflowService) {
		this.propertyWorkflowService = propertyWorkflowService;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public Boolean getIsPreviewPVR() {
		return isPreviewPVR;
	}

	public void setIsPreviewPVR(Boolean isPreviewPVR) {
		this.isPreviewPVR = isPreviewPVR;
	}

	public Long getBasicPropId() {
		return basicPropId;
	}

	public void setBasicPropId(Long basicPropId) {
		this.basicPropId = basicPropId;
	}

}
