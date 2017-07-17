/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
/**
 * @author nayeem
 *
 * Generates the demand notice or the bill that is sent to the assessee giving the break up of the tax amounts that is due to NMC
 */

package org.egov.ptis.actions.bills;

import com.opensymphony.xwork2.validator.annotations.Validations;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgBill;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.viewer.ReportViewerUtil;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.client.bill.PTBillServiceImpl;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.bill.BillService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.DemandBill.DemandBillService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_CLIENT_SPECIFIC_DMD_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.BILLTYPE_MANUAL;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMAND_BILL_STATUS;
import static org.egov.ptis.constants.PropertyTaxConstants.STRING_EMPTY;

@ParentPackage("egov")
@Validations
@Results({ @Result(name = BillGenerationAction.BILL, location = "billGeneration-bill.jsp"),
        @Result(name = BillGenerationAction.STATUS_BILLGEN, location = "billGeneration-billsGenStatus.jsp"),
        @Result(name = BillGenerationAction.ACK, location = "billGeneration-ack.jsp"),
        @Result(name = BillGenerationAction.COMMON_FORM, location = "searchProperty-commonForm.jsp") })
public class BillGenerationAction extends PropertyTaxBaseAction {
    /**
     *
     */
    private static final long serialVersionUID = -6600897692089941070L;

    private final Logger LOGGER = Logger.getLogger(getClass());
    protected static final String COMMON_FORM = "commonForm";
    public static final String BILL = "bill";
    public static final String STATUS_BILLGEN = "billsGenStatus";
    private static final String STATUS_BILLGEN_BY_PARTNO = "statusByPartNo";
    public static final String ACK = "ack";

    private ReportService reportService;
    private PersistenceService<BasicProperty, Long> basicPropertyService;
    private PersistenceService<Property, Long> propertyImplService;
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    private PTBillServiceImpl ptBillServiceImpl;
    private PropertyService propService;
    private BillService billService;

    private String indexNumber;
    private String ackMessage;
    private String reportId;
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

    @Autowired
    private ApplicationContext beanProvider;
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private ReportViewerUtil reportViewerUtil;

    @Override
    public StateAware getModel() {
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
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into generateBill, Index Number :" + indexNumber);
        try {
            if (basicPropertyDAO != null)
                basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
            if (basicProperty.getProperty().getIsExemptedFromTax()) {
                addActionError(getText("error.msg.taxExempted"));
                return COMMON_FORM;
            }
            property = (PropertyImpl) basicProperty.getProperty();

            final EgBill egBill = (EgBill) persistenceService.find("FROM EgBill WHERE module = ? "
                    + "AND egBillType.code = ? AND consumerId = ? AND is_history = 'N'",
                    moduleDao.getModuleByName(PTMODULENAME), BILLTYPE_MANUAL, basicProperty.getUpicNo());
            ReportOutput reportOutput = null;

            if (egBill == null)
                reportOutput = getBillService().generateBill(basicProperty, ApplicationThreadLocals.getUserId().intValue());
            else {
                final String query = "SELECT notice FROM EgBill bill, PtNotice notice left join notice.basicProperty bp "
                        + "WHERE bill.is_History = 'N' "
                        + "AND bill.egBillType.code = ? "
                        + "AND bill.billNo = notice.noticeNo " + "AND notice.noticeType = ? " + "AND bp = ?";
                final PtNotice ptNotice = (PtNotice) persistenceService.find(query, BILLTYPE_MANUAL, NOTICE_TYPE_BILL,
                        basicProperty);
                reportOutput = new ReportOutput();

                // Reading from filestore by passing filestoremapper object
                if (ptNotice != null && ptNotice.getFileStore() != null) {
                    final FileStoreMapper fsm = ptNotice.getFileStore();
                    final File file = fileStoreService.fetch(fsm, FILESTORE_MODULE_NAME);
                    final byte[] bFile = FileUtils.readFileToByteArray(file);
                    reportOutput.setReportOutputData(bFile);
                    reportOutput.setReportFormat(ReportFormat.PDF);
                }
            }

            reportId = reportViewerUtil.addReportToTempCache(reportOutput);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("generateBill: ReportId: " + reportId);
                LOGGER.debug("Exit from generateBill");
            }
            if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
                property.setStatus(PropertyTaxConstants.STATUS_ISACTIVE);
                propertyImplService.persist(property);
            }
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Bill Generation Exception : " + e);
        }
        return BILL;
    }

    @Action(value = "/bills/billGeneration-generateDemandBill")
    public String generateDemandBill() {
        String clientSpecificDmdBill = propertyTaxCommonUtils.getAppConfigValue(APPCONFIG_CLIENT_SPECIFIC_DMD_BILL,
                PTMODULENAME);
        if ("Y".equalsIgnoreCase(clientSpecificDmdBill)) {
            DemandBillService demandBillService = (DemandBillService) beanProvider.getBean("demandBillService");
            ReportOutput reportOutput = demandBillService.generateDemandBill(indexNumber, NOTICE_TYPE_BILL);
            reportId = reportViewerUtil.addReportToTempCache(reportOutput);
        } else {
            generateBill();
        }
        return BILL;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/bills/billGeneration-billsGenStatus")
    public String billsGenStatus() {
        ReportInfo reportInfo;
        Integer totalProps = 0;
        Integer totalBillsGen = 0;
        final Installment currInst = installmentDAO.getInsatllmentByModuleForGivenDate(ptBillServiceImpl.getModule(),
                new Date());
        final StringBuilder propQueryString = new StringBuilder();

        propQueryString
                .append("select bndry.boundaryNum,bndry.name, count(bndry.boundaryNum) ")
                .append("from Boundary bndry, PropertyID pid left join pid.basicProperty bp where bp.upicNo is not null and bp.active = true and ")
                .append("bp.source = 'M' and pid.ward.id = bndry.id ")
                .append("and bp.id not in (select basicProperty from PropertyStatusValues group by basicProperty having count(basicProperty) > 0 ) ")
                .append(" and bp.id in (select basicProperty from PropertyImpl where status = 'A' and isExemptedFromTax = false ) ")
                .append("group by bndry.name, bndry.boundaryNum ").append("order by bndry.boundaryNum, bndry.name");

        final Query billQuery = getPersistenceService().getSession().getNamedQuery(QUERY_DEMAND_BILL_STATUS);
        billQuery.setDate("FromDate", currInst.getFromDate());
        billQuery.setDate("ToDate", currInst.getToDate());
        final List<Object> billList = billQuery.list();
        LOGGER.info("billList : " + billList);
        final Query propQuery = getPersistenceService().getSession().createQuery(propQueryString.toString());
        final List<Object> propList = propQuery.list();
        LOGGER.info("propList : " + propList);

        for (final Object props : propList) {
            reportInfo = new ReportInfo();
            final Object[] propObj = (Object[]) props;
            reportInfo.setWardNo(String.valueOf(propObj[0]) + '-' + String.valueOf(propObj[1]));
            reportInfo.setTotalNoProps(Integer.valueOf(((Long) propObj[2]).toString()));

            reportInfo.setTotalGenBills(0);
            String propWardNo = String.valueOf(propObj[0]);
            String wardNo;
            for (final Object bills : billList) {
                final Object[] billObj = (Object[]) bills;
                wardNo = String.valueOf(billObj[0]);
                if (propWardNo.equals(wardNo)) {
                    reportInfo.setTotalGenBills(Integer.valueOf(((Long) billObj[1]).toString()));
                    break;
                }
            }
            totalProps = totalProps + reportInfo.getTotalNoProps();
            totalBillsGen = totalBillsGen + reportInfo.getTotalGenBills();
            getReportInfos().add(reportInfo);
        }
        final ReportInfo reportInfoCount = new ReportInfo();
        reportInfoCount.setWardNo("Total :");
        reportInfoCount.setTotalNoProps(totalProps);
        reportInfoCount.setTotalGenBills(totalBillsGen);
        getReportInfos().add(reportInfoCount);

        return STATUS_BILLGEN;
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/bills/billGeneration-billGenStatusByPartNo")
    public String billGenStatusByPartNo() {
        LOGGER.debug("Entered into billGenStatusByPartNo, wardNum=" + wardNum);

        ReportInfo reportInfo;
        Integer totalProps = 0;
        Integer totalBillsGen = 0;
        final Installment currInst = propertyTaxCommonUtils.getCurrentInstallment();

        final StringBuilder billQueryString = new StringBuilder();
        final StringBuilder propQueryString = new StringBuilder();

        billQueryString.append("select bp.partNo, count(bp.partNo) ")
                .append("from EgBill bill, Boundary bndry, PtNotice notice left join notice.basicProperty bp ")
                .append("where bp.propertyID.ward.id=bndry.id ").append("and bndry.boundaryNum = :bndryNum ")
                .append("and bill.is_History = 'N' ").append("and :FromDate <= bill.issueDate ")
                .append("and :ToDate >= bill.issueDate ").append("and bill.egBillType.code = :BillType ")
                .append("and bill.billNo = notice.noticeNo ").append("and notice.noticeType = 'Bill' ")
                .append("and notice.fileStore is not null ").append("group by bp.partNo ").append("order by bp.partNo");

        propQueryString.append("select bp.partNo, count(bp.partNo) ")
                .append("from Boundary bndry, PropertyID pid left join pid.basicProperty bp ")
                .append("where bp.active = true and pid.ward.id = bndry.id ")
                .append("and bndry.boundaryNum = :bndryNum ").append("group by bp.partNo ")
                .append("order by bp.partNo");

        final Query billQuery = getPersistenceService().getSession().createQuery(billQueryString.toString());
        billQuery.setBigInteger("bndryNum", new BigInteger(wardNum));
        billQuery.setDate("FromDate", currInst.getFromDate());
        billQuery.setDate("ToDate", currInst.getToDate());
        billQuery.setString("BillType", BILLTYPE_MANUAL);

        final List<Object> billList = billQuery.list();

        final Query propQuery = getPersistenceService().getSession().createQuery(propQueryString.toString());
        propQuery.setBigInteger("bndryNum", new BigInteger(wardNum));
        final List<Object> propList = propQuery.list();

        for (final Object props : propList) {
            reportInfo = new ReportInfo();
            final Object[] propObj = (Object[]) props;
            reportInfo.setPartNo(String.valueOf(propObj[0]));
            reportInfo.setTotalNoProps(Integer.valueOf(((Long) propObj[1]).toString()));

            reportInfo.setTotalGenBills(0);
            String partNo;

            for (final Object bills : billList) {

                final Object[] billObj = (Object[]) bills;
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

        final ReportInfo reportInfoCount = new ReportInfo();
        reportInfoCount.setPartNo("Total :");
        reportInfoCount.setTotalNoProps(totalProps);
        reportInfoCount.setTotalGenBills(totalBillsGen);
        getReportInfos().add(reportInfoCount);

        LOGGER.debug("Exiting from billGenStatusByPartNo");
        return STATUS_BILLGEN_BY_PARTNO;
    }

    @Action(value = "/bills/billGeneration-cancelBill")
    public String cancelBill() {
        final EgBill egBill = (EgBill) persistenceService.find("FROM EgBill " + "WHERE module = ? "
                + "AND egBillType.code = ? " + "AND SUBSTRING(consumerId, 1, (LOCATE('(', consumerId)-1)) = ? "
                + "AND is_history = 'N'", moduleDao.getModuleByName(PTMODULENAME), BILLTYPE_MANUAL, indexNumber);
        if (egBill == null) {
            setAckMessage("There is no active Bill exist for index no : " + indexNumber);
            return ACK;
        } else {
            egBill.setIs_History("Y");
            egBill.setIs_Cancelled("Y");
            egBill.setModifiedDate(new Date());
            final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
            basicProperty.setIsBillCreated(PropertyTaxConstants.STATUS_BILL_NOTCREATED);
            basicProperty.setBillCrtError(STRING_EMPTY);
            basicPropertyService.update(basicProperty);
            setAckMessage("Bill successfully cancelled for index no : " + indexNumber);
        }
        return ACK;
    }

    public String getReportId() {
        return reportId;
    }

    @Override
    public String getIndexNumber() {
        return indexNumber;
    }

    @Override
    public void setIndexNumber(final String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(final String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public void setReportService(final ReportService reportService) {
        this.reportService = reportService;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(final String billNo) {
        this.billNo = billNo;
    }

    public PropertyTaxNumberGenerator getPropertyTaxNumberGenerator() {
        return propertyTaxNumberGenerator;
    }

    public void setPropertyTaxNumberGenerator(final PropertyTaxNumberGenerator propertyTaxNumberGenerator) {
        this.propertyTaxNumberGenerator = propertyTaxNumberGenerator;
    }

    public Map<String, Map<String, BigDecimal>> getReasonwiseDues() {
        return reasonwiseDues;
    }

    public void setReasonwiseDues(final Map<String, Map<String, BigDecimal>> reasonwiseDues) {
        this.reasonwiseDues = reasonwiseDues;
    }

    public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
        return basicPropertyService;
    }

    public void setPropertyImplService(final PersistenceService<Property, Long> propertyImplService) {
        this.propertyImplService = propertyImplService;
    }

    public void setbasicPropertyService(final PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(final BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public void setPropertyWorkflowService(final WorkflowService<PropertyImpl> propertyWorkflowService) {
    }

    public PropertyService getPropService() {
        return propService;
    }

    public void setPropService(final PropertyService propService) {
        this.propService = propService;
    }

    public BillService getBillService() {
        return billService;
    }

    public void setBillService(final BillService billService) {
        this.billService = billService;
    }

    public List<ReportInfo> getReportInfos() {
        return reportInfos;
    }

    public void setReportInfos(final List<ReportInfo> reportInfos) {
        this.reportInfos = reportInfos;
    }

    public String getWardNum() {
        return wardNum;
    }

    public void setWardNum(final String wardNum) {
        this.wardNum = wardNum;
    }

    public PTBillServiceImpl getPtBillServiceImpl() {
        return ptBillServiceImpl;
    }

    public void setPtBillServiceImpl(final PTBillServiceImpl ptBillServiceImpl) {
        this.ptBillServiceImpl = ptBillServiceImpl;
    }

}
