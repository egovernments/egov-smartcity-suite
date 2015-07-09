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
/**
 * @author nayeem
 * 
 * Generates the demand notice or the bill that is sent to the assessee giving the break up of the tax amounts that is due to NMC
 */

package org.egov.ptis.actions.bills;

import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_DEMANDNOTICE_GENERATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_BILL_CREATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STRING_EMPTY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_AMALGAMATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_CHANGEADDRESS;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_DEACTIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATED;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_APPROVAL_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_NOTICE_GENERATION_PENDING;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgBill;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportConstants.FileFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.model.calculator.DemandNoticeInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.bill.BillService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({  @Result(name = BillGenerationAction.BILL, location = "billGeneration-bill.jsp"),
    @Result(name = BillGenerationAction.STATUS_BILLGEN, location = "billGeneration-billsGenStatus.jsp"),
    @Result(name = BillGenerationAction.ACK, location = "billGeneration-ack.jsp")
    })
public class BillGenerationAction extends PropertyTaxBaseAction {
	private Logger LOGGER = Logger.getLogger(getClass());

	public static final String BILL = "bill";
	private static final String YES = "Yes";
	public static final String STATUS_BILLGEN = "billsGenStatus";
	private static final String STATUS_BILLGEN_BY_PARTNO = "statusByPartNo";
	public static final String ACK = "ack";

	private ReportService reportService;
	private PersistenceService<BasicProperty, Long> basicPropertyService;
	private PersistenceService<Property, Long> propertyImplService;
	private DocumentManagerService<DocumentObject> documentManagerService;
	private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
	private PTBillServiceImpl ptBillServiceImpl;
	private WorkflowService<PropertyImpl> propertyWorkflowService;
	private PropertyService propService;
	private BillService billService;

	private String indexNumber;
	private String ackMessage;
	private Integer reportId = -1;
	private String billNo;
	private BasicProperty basicProperty;
	private PropertyImpl property;
	private Map<String, Map<String, BigDecimal>> reasonwiseDues;
	private List<ReportInfo> reportInfos = new ArrayList<ReportInfo>();
	InputStream billPDF;
	private String wardNum;

	@Autowired
	private ModuleService moduleDao;

	@Autowired
	private InstallmentDao installmentDAO;
	
	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	
	@Autowired
        @Qualifier("fileStoreService")
        protected FileStoreService fileStoreService;

	@Override
	public Object getModel() {
		return null;
	}

	public BillGenerationAction() {
	}

	/**
	 * Called to generate the bill
	 * 
	 * @return String
	 */
	@Action(value = "/bills/billGeneration-generateBill")
	public String generateBill() {
		LOGGER.debug("Entered into generateBill, Index Number :" + indexNumber);
	  try{	
		if(basicPropertyDAO!=null){
		    basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
		}
		property = (PropertyImpl) basicProperty.getProperty();

		EgBill egBill = (EgBill) persistenceService.find("FROM EgBill WHERE module = ? "
				+ "AND egBillType.code = ? "
				+ "AND SUBSTRING(consumerId, 1, (LOCATE('(', consumerId)-1)) = ? "
				+ "AND is_history = 'N'", moduleDao.getModuleByName(PTMODULENAME), BILLTYPE_MANUAL,
				basicProperty.getUpicNo());
		ReportOutput reportOutput = null;

		if (egBill == null) {
			reportOutput = getBillService().generateBill(basicProperty, 
					EgovThreadLocals.getUserId().intValue());
			basicProperty.setIsBillCreated(STATUS_BILL_CREATED);
			basicProperty.setBillCrtError(STRING_EMPTY);
		} else {
			String query = "SELECT notice FROM EgBill bill, PtNotice notice left join notice.basicProperty bp "
					+ "WHERE bill.is_History = 'N' "
					+ "AND bill.egBillType.code = ? "
					+ "AND bill.billNo = notice.noticeNo "
					+ "AND notice.noticeType = ? "
					+ "AND bp = ?";
			PtNotice ptNotice = (PtNotice) persistenceService.find(query, BILLTYPE_MANUAL,
					NOTICE_TYPE_BILL, basicProperty);
			reportOutput = new ReportOutput(); 
			
			//Reading from filestore by passing filestoremapper object
			if (ptNotice!=null && ptNotice.getFileStore()!=null) {
			        FileStoreMapper fsm=ptNotice.getFileStore();
			        File file=fileStoreService.fetch(fsm, PTMODULENAME); 
			        byte[] bFile =FileUtils.readFileToByteArray(file);
				reportOutput.setReportOutputData(bFile); 
				reportOutput.setReportFormat(FileFormat.PDF);
			} else {
        	                //To generate Notice having installment and reasonwise balance for a property
        	                DemandNoticeInfo demandNoticeInfo = new DemandNoticeInfo();
        	                demandNoticeInfo.setBasicProperty(basicProperty);
        	                demandNoticeInfo.setDemandNoticeDetailsInfo(propertyTaxUtil.getDemandNoticeDetailsInfo(basicProperty));
        	                ReportRequest reportRequest = null;	   
        	                Map reportParams = new HashMap<String, Object>();
        	                reportParams.put("logoPath", propertyTaxUtil.logoBasePath());
        	                reportRequest = new ReportRequest(REPORT_TEMPLATENAME_DEMANDNOTICE_GENERATION, demandNoticeInfo,reportParams);
        	                reportOutput = getReportService().createReport(reportRequest); 
			}
	                
		}
		reportId = ReportViewerUtil.addReportToSession(reportOutput, getSession());
		LOGGER.debug("generateBill: ReportId: " + reportId);
		LOGGER.debug("Exit from generateBill");
    	  } catch (final Exception e) {
    	      throw new EGOVRuntimeException("Bill Generation Exception : " + e);
    	  }
	  return BILL;
	}

	@Action(value = "/bills/billGeneration-billsGenStatus")
	public String billsGenStatus() {
		ReportInfo reportInfo;
		Integer totalProps = 0;
		Integer totalBillsGen = 0;
		Installment currInst = installmentDAO.getInsatllmentByModuleForGivenDate(
				ptBillServiceImpl.getModule(), new Date());
		StringBuilder billQueryString = new StringBuilder();
		StringBuilder propQueryString = new StringBuilder();
		billQueryString
				.append("select bndry.boundaryNum, count(bndry.boundaryNum) ")
				.append("from EgBill bill, BoundaryImpl bndry, PtNotice notice left join notice.basicProperty bp ")
				.append("where bp.propertyID.ward.id=bndry.id ").append("and bp.active = true ")
				.append("and bill.is_History = 'N' ").append("and :FromDate <= bill.issueDate ")
				.append("and :ToDate >= bill.issueDate ")
				.append("and bill.egBillType.code = :BillType ")
				.append("and bill.billNo = notice.noticeNo ")
				.append("and notice.noticeType = 'Bill' ")
				.append("and notice.noticeFile is not null ").append("group by bndry.boundaryNum ")
				.append("order by bndry.boundaryNum");

		propQueryString.append("select bndry.boundaryNum, count(bndry.boundaryNum) ")
				.append("from BoundaryImpl bndry, PropertyID pid left join pid.basicProperty bp ")
				.append("where bp.active = true and pid.ward.id = bndry.id ")
				.append("group by bndry.boundaryNum ").append("order by bndry.boundaryNum");
		Query billQuery = getPersistenceService().getSession().createQuery(
				billQueryString.toString());
		billQuery.setDate("FromDate", currInst.getFromDate());
		billQuery.setDate("ToDate", currInst.getToDate());
		billQuery.setString("BillType", BILLTYPE_MANUAL);
		List<Object> billList = billQuery.list();
		LOGGER.info("billList : " + billList);
		Query propQuery = getPersistenceService().getSession().createQuery(
				propQueryString.toString());
		List<Object> propList = propQuery.list();
		LOGGER.info("propList : " + propList);

		for (Object props : propList) {
			reportInfo = new ReportInfo();
			Object[] propObj = (Object[]) props;
			reportInfo.setWardNo(String.valueOf(propObj[0]));
			reportInfo.setTotalNoProps(Integer.valueOf(((Long) propObj[1]).toString()));

			reportInfo.setTotalGenBills(0);
			String wardNo;
			for (Object bills : billList) {
				Object[] billObj = (Object[]) bills;
				wardNo = String.valueOf(billObj[0]);
				if (reportInfo.getWardNo().equals(wardNo)) {
					reportInfo.setTotalGenBills(Integer.valueOf(((Long) billObj[1]).toString()));
					break;
				}
			}
			totalProps = totalProps + reportInfo.getTotalNoProps();
			totalBillsGen = totalBillsGen + reportInfo.getTotalGenBills();
			getReportInfos().add(reportInfo);
		}
		ReportInfo reportInfoCount = new ReportInfo();
		reportInfoCount.setWardNo("Total :");
		reportInfoCount.setTotalNoProps(totalProps);
		reportInfoCount.setTotalGenBills(totalBillsGen);
		getReportInfos().add(reportInfoCount);

		return STATUS_BILLGEN;
	}

	@Action(value = "/bills/billGeneration-billGenStatusByPartNo")
	public String billGenStatusByPartNo() {
		LOGGER.debug("Entered into billGenStatusByPartNo, wardNum=" + wardNum);

		ReportInfo reportInfo;
		Integer totalProps = 0;
		Integer totalBillsGen = 0;
		Installment currInst = propertyTaxUtil.getCurrentInstallment();

		StringBuilder billQueryString = new StringBuilder();
		StringBuilder propQueryString = new StringBuilder();

		billQueryString
				.append("select bp.partNo, count(bp.partNo) ")
				.append("from EgBill bill, BoundaryImpl bndry, PtNotice notice left join notice.basicProperty bp ")
				.append("where bp.propertyID.ward.id=bndry.id ")
				.append("and bndry.boundaryNum = :bndryNum ").append("and bill.is_History = 'N' ")
				.append("and :FromDate <= bill.issueDate ")
				.append("and :ToDate >= bill.issueDate ")
				.append("and bill.egBillType.code = :BillType ")
				.append("and bill.billNo = notice.noticeNo ")
				.append("and notice.noticeType = 'Bill' ")
				.append("and notice.noticeFile is not null ").append("group by bp.partNo ")
				.append("order by bp.partNo");

		propQueryString.append("select bp.partNo, count(bp.partNo) ")
				.append("from BoundaryImpl bndry, PropertyID pid left join pid.basicProperty bp ")
				.append("where bp.active = true and pid.ward.id = bndry.id ")
				.append("and bndry.boundaryNum = :bndryNum ").append("group by bp.partNo ")
				.append("order by bp.partNo");

		Query billQuery = getPersistenceService().getSession().createQuery(
				billQueryString.toString());
		billQuery.setBigInteger("bndryNum", new BigInteger(wardNum));
		billQuery.setDate("FromDate", currInst.getFromDate());
		billQuery.setDate("ToDate", currInst.getToDate());
		billQuery.setString("BillType", BILLTYPE_MANUAL);

		List<Object> billList = billQuery.list();

		Query propQuery = getPersistenceService().getSession().createQuery(
				propQueryString.toString());
		propQuery.setBigInteger("bndryNum", new BigInteger(wardNum));
		List<Object> propList = propQuery.list();

		for (Object props : propList) {
			reportInfo = new ReportInfo();
			Object[] propObj = (Object[]) props;
			reportInfo.setPartNo(String.valueOf(propObj[0]));
			reportInfo.setTotalNoProps(Integer.valueOf(((Long) propObj[1]).toString()));

			reportInfo.setTotalGenBills(0);
			String partNo;

			for (Object bills : billList) {

				Object[] billObj = (Object[]) bills;
				partNo = String.valueOf(billObj[0]);

				if (reportInfo.getPartNo().equals(partNo)) {
					reportInfo.setTotalGenBills(Integer.valueOf(((Long) billObj[1]).toString()));
					break;
				}
			}

			totalProps = totalProps + reportInfo.getTotalNoProps();
			totalBillsGen = totalBillsGen + reportInfo.getTotalGenBills();
			getReportInfos().add(reportInfo);
		}

		ReportInfo reportInfoCount = new ReportInfo();
		reportInfoCount.setPartNo("Total :");
		reportInfoCount.setTotalNoProps(totalProps);
		reportInfoCount.setTotalGenBills(totalBillsGen);
		getReportInfos().add(reportInfoCount);

		LOGGER.debug("Exiting from billGenStatusByPartNo");
		return STATUS_BILLGEN_BY_PARTNO;
	}

	@Action(value = "/bills/billGeneration-cancelBill")
	public String cancelBill() {
		EgBill egBill = (EgBill) persistenceService.find("FROM EgBill " + "WHERE module = ? "
				+ "AND egBillType.code = ? "
				+ "AND SUBSTRING(consumerId, 1, (LOCATE('(', consumerId)-1)) = ? "
				+ "AND is_history = 'N'", moduleDao.getModuleByName(PTMODULENAME), BILLTYPE_MANUAL,
				indexNumber);
		if (egBill == null) {
			setAckMessage("There is no active Bill exist for index no : " + indexNumber);
			return ACK;
		} else {
			egBill.setIs_History("Y");
			egBill.setIs_Cancelled("Y");
			egBill.setModifiedDate(new Date());
			BasicProperty basicProperty = basicPropertyDAO
					.getBasicPropertyByPropertyID(indexNumber);
			basicProperty.setIsBillCreated(PropertyTaxConstants.STATUS_BILL_NOTCREATED);
			basicProperty.setBillCrtError(STRING_EMPTY);
			basicPropertyService.update(basicProperty);
			setAckMessage("Bill successfully cancelled for index no : " + indexNumber);
		}
		return ACK;
	}

	private void startWorkFlow() {
		LOGGER.debug("Entered into startWorkFlow, UserId: " + EgovThreadLocals.getUserId());
		LOGGER.debug("startWorkFlow: Workflow is starting for Property: " + property);
		// Position position =
		// eisCommonsManager.(Integer.valueOf(EgovThreadLocals.getUserId()));
		// FIX ME
		Position position = null;
		// propertyWorkflowService.start(property, position,
		// "Property Workflow Started");
		property.transition(true).start().withSenderName(property.getCreatedBy().getUsername())
				.withComments("Property Workflow Started").withOwner(position);

		LOGGER.debug("Exiting from startWorkFlow, Workflow started");
	}

	/**
	 * This method ends the workflow. The Property is transitioned to END state.
	 */
	private void endWorkFlow() {
		LOGGER.debug("Enter method endWorkFlow, UserId: " + EgovThreadLocals.getUserId());
		LOGGER.debug("endWorkFlow: Workflow will end for Property: " + property);
		// FIX ME
		// Position position =
		// eisCommonsManager.getPositionByUserId(Integer.valueOf(EgovThreadLocals.getUserId()));
		Position position = null;
		/*
		 * propertyWorkflowService .end(property, position,
		 * "Property Workflow End");
		 */
		property.transition(true).end().withSenderName(property.getCreatedBy().getUsername())
				.withComments("Property Workflow End").withOwner(position);
		LOGGER.debug("Exit method endWorkFlow, Workflow ended");
	}

	private void transitionWorkFlow() {
		if (workflowBean != null) {
			LOGGER.debug("Entered method : transitionWorkFlow. Action : "
					+ workflowBean.getActionName() + "Property: " + property);
		} else {
			LOGGER.debug("transitionWorkFlow: workflowBean is NULL");
		}

		if (property.getState() == null) {
			startWorkFlow();
		}

		Position nextPosition = null;
		String wflowAction = null;
		StringBuffer nextStateValue = new StringBuffer();
		if (workflowBean.getApproverUserId() != null)
			// FIX ME
			// nextPosition =
			// eisCommonsManager.getPositionByUserId(workflowBean.getApproverUserId());
			nextPosition = null;
		String beanActionName[] = workflowBean.getActionName().split(":");
		String actionName = beanActionName[0];
		if (beanActionName.length > 1) {
			wflowAction = beanActionName[1];// save or forward or approve
		}
		if (WFLOW_ACTION_NAME_CREATE.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_CREATE).append(":");
		} else if (WFLOW_ACTION_NAME_TRANSFER.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_TRANSFER).append(":");
		} else if (WFLOW_ACTION_NAME_DEACTIVATE.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_DEACTIVATE).append(":");
		} else if (WFLOW_ACTION_NAME_MODIFY.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_MODIFY).append(":");
		} else if (WFLOW_ACTION_NAME_BIFURCATE.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_BIFURCATE).append(":");
		} else if (WFLOW_ACTION_NAME_AMALGAMATE.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_AMALGAMATE).append(":");
		} else if (WFLOW_ACTION_NAME_CHANGEADDRESS.equals(actionName)) {
			nextStateValue = nextStateValue.append(WFLOW_ACTION_NAME_CHANGEADDRESS).append(":");
		}
		if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(wflowAction)) {
			if (WFLOW_ACTION_NAME_DEACTIVATE.equals(actionName)
					|| WFLOW_ACTION_NAME_CHANGEADDRESS.equals(actionName)) {
				endWorkFlow();
			} else {
				nextStateValue = nextStateValue.append(WF_STATE_NOTICE_GENERATION_PENDING);
				// FIX ME
				// nextPosition =
				// eisCommonsManager.getPositionByUserId(property.getCreatedBy().getId());
				nextPosition = null;
				// property.changeState(nextStateValue.toString(), nextPosition,
				// workflowBean.getComments());
				property.transition(true).start().withStateValue(nextStateValue.toString())
						.withOwner(nextPosition).withComments(workflowBean.getComments());
			}
		} else if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(wflowAction)) {
			// FIX ME
			// nextPosition =
			// eisCommonsManager.getPositionByUserId(propertyTaxUtil.getLoggedInUser(getSession()).getId());
			nextPosition = null;
			if (WFLOW_ACTION_NAME_CREATE.equals(actionName)
					|| WFLOW_ACTION_NAME_MODIFY.equals(actionName)) {
				if (property.getBasicProperty().getAllChangesCompleted()) {
					nextStateValue = nextStateValue.append(WF_STATE_NOTICE_GENERATION_PENDING);
				} else {
					nextStateValue = nextStateValue
							.append(nextPosition.getDeptDesig().getDepartment().getName())
							.append("_").append(WF_STATE_APPROVAL_PENDING);
				}
			} else {
				nextStateValue = nextStateValue.append(WF_STATE_NOTICE_GENERATION_PENDING);
			}
			propertyImplService.persist(property);
			// property.changeState(nextStateValue.toString(), nextPosition,
			// workflowBean.getComments());
			property.transition(true).start().withStateValue(nextStateValue.toString())
					.withOwner(nextPosition).withComments(workflowBean.getComments());
		} else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(wflowAction)) {
			Designation nextDesn = propertyTaxUtil.getDesignationForUser(workflowBean
					.getApproverUserId().longValue());
			nextStateValue = nextStateValue.append(nextDesn.getName()).append("_")
					.append(WF_STATE_APPROVAL_PENDING);
			// property.changeState(nextStateValue.toString(), nextPosition,
			// workflowBean.getComments());
			property.transition(true).start().withStateValue(nextStateValue.toString())
					.withOwner(nextPosition).withComments(workflowBean.getComments());
		} else if (WFLOW_ACTION_STEP_NOTICE_GENERATED.equalsIgnoreCase(wflowAction)) {
			endWorkFlow();
		}

		LOGGER.debug("transitionWorkFlow: Property transitioned to "
				+ property.getState().getValue());
		propertyImplService.persist(property);

		LOGGER.debug("Exiting method : transitionWorkFlow");
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	@Override
	public String getIndexNumber() {
		return indexNumber;
	}

	@Override
	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getAckMessage() {
		return ackMessage;
	}

	public void setAckMessage(String ackMessage) {
		this.ackMessage = ackMessage;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public DocumentManagerService<DocumentObject> getDocumentManagerService() {
		return documentManagerService;
	}

	public void setDocumentManagerService(
			DocumentManagerService<DocumentObject> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}

	public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
		return propertyTaxNumberGenerator;
	}

	public void setPropertyTaxNumberGenerator(PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
		this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
	}

	public Map<String, Map<String, BigDecimal>> getReasonwiseDues() {
		return reasonwiseDues;
	}

	public void setReasonwiseDues(Map<String, Map<String, BigDecimal>> reasonwiseDues) {
		this.reasonwiseDues = reasonwiseDues;
	}

	public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
		return basicPropertyService;
	}

	public void setPropertyImplService(PersistenceService<Property, Long> propertyImplService) {
		this.propertyImplService = propertyImplService;
	}

	public void setbasicPropertyService(PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicPropertyService = basicPropertyService;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public void setPropertyWorkflowService(WorkflowService<PropertyImpl> propertyWorkflowService) {
		this.propertyWorkflowService = propertyWorkflowService;
	}

	public PropertyService getPropService() {
		return propService;
	}

	public void setPropService(PropertyService propService) {
		this.propService = propService;
	}

	public BillService getBillService() {
		return billService;
	}

	public void setBillService(BillService billService) {
		this.billService = billService;
	}

	public List<ReportInfo> getReportInfos() {
		return reportInfos;
	}

	public void setReportInfos(List<ReportInfo> reportInfos) {
		this.reportInfos = reportInfos;
	}

	public String getWardNum() {
		return wardNum;
	}

	public void setWardNum(String wardNum) {
		this.wardNum = wardNum;
	}

        public PTBillServiceImpl getPtBillServiceImpl() {
            return ptBillServiceImpl;
        }
    
        public void setPtBillServiceImpl(PTBillServiceImpl ptBillServiceImpl) {
            this.ptBillServiceImpl = ptBillServiceImpl;
        }

}
