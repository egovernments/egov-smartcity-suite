/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.ptis.domain.service.notice;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.model.FloorDetails;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import org.hibernate.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.FILESTORE_MODULE_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TEMPLATE_COMPARISON_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SURVEY_COMPARISON;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import org.egov.ptis.bean.SurveyReportBean;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.PropertyHibernateDAO;
import org.egov.ptis.domain.entity.demand.Ptdemand;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.*;


@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NoticeService extends PersistenceService<PtNotice, Long> {
    @Autowired
    PersistenceService<BasicProperty, Long> basicPropertyService;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    @Autowired
    private PropertyHibernateDAO propertyHibernateDAO;
    @Autowired
    private ReportService reportService;
    @Autowired
    private PropertyService propService;
    @Autowired
    PropertyTaxUtil propertyTaxUtil;
    
    public NoticeService() {
        super(PtNotice.class);
    }

    public NoticeService(final Class<PtNotice> type) {
        super(type);
    }
    
    /**
     * This method populates the <code>PtNotice</code> object along with notice input stream
     *
     * @param basicProperty the <code>BasicProperty</code> object for which the notice is generated
     * @param noticeNo - notice no
     * @param noticeType - type of notice
     * @param fileStream - input stream of generated notice.
     */
    public PtNotice saveNotice(final String applicationNumber, final String noticeNo, final String noticeType,
            final BasicProperty basicProperty, final InputStream fileStream) {
        final PtNotice ptNotice = new PtNotice();
        final Module module = moduleDao.getModuleByName(PTMODULENAME);
        ptNotice.setModuleId(module.getId());
        ptNotice.setNoticeDate(new Date());
        ptNotice.setNoticeNo(noticeNo);
        ptNotice.setNoticeType(noticeType);
        ptNotice.setUserId(ApplicationThreadLocals.getUserId());
        ptNotice.setBasicProperty(basicProperty);
        ptNotice.setApplicationNumber(applicationNumber);
        final String fileName = ptNotice.getNoticeNo().replace("/", "-") + ".pdf";
        final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                FILESTORE_MODULE_NAME);
        ptNotice.setFileStore(fileStore);
        basicProperty.addNotice(ptNotice);
        basicPropertyService.update(basicProperty);
        getSession().flush();
        return ptNotice;
    }

    /**
     * Using this method to attach different file store if document is already signed and been sent for sign again
     *
     * @param notice
     * @param fileStream
     * @return
     */
    public PtNotice updateNotice(final PtNotice notice, final InputStream fileStream) {
        final String fileName = notice.getNoticeNo().replace("/", "-") + ".pdf";
        final FileStoreMapper fileStore = fileStoreService.store(fileStream, fileName, "application/pdf",
                FILESTORE_MODULE_NAME);
        notice.setFileStore(fileStore);
        notice.setNoticeDate(new Date());
        basicPropertyService.update(notice.getBasicProperty());
        getSession().flush();
        return notice;
    }
    
    @SuppressWarnings("unchecked")
    @ReadOnly
    public PtNotice getPtNoticeByNoticeNumberAndNoticeType(final String noticeNo, final String noticeType) {
        List<PtNotice> noticeList = (List<PtNotice>) entityManager.createNamedQuery("getNoticeByNoticeNoAndType")
                .setParameter("noticeNo", noticeNo).setParameter("noticeType", noticeType.toUpperCase()).getResultList();
        return !noticeList.isEmpty() ? noticeList.get(0) : null;
    }
    
    @ReadOnly
    public PtNotice getNoticeByApplicationNumber(final String applicationNo) {
        return (PtNotice) entityManager.createNamedQuery("getNoticeByApplicationNo").setParameter("applicationNumber", applicationNo);
    }
    
    @SuppressWarnings("unchecked")
    public PtNotice getNoticeByNoticeTypeAndApplicationNumber(final String noticeType, final String applicationNo) {
		List<PtNotice> notice= entityManager.createNamedQuery("getNoticeByApplicationNoAndNoticeType")
				.setParameter("noticeType", noticeType.toUpperCase()).setParameter("applicationNumber", applicationNo).getResultList();
		if(!notice.isEmpty())
			return notice.get(0);
		else 
			return null;
    }

    @ReadOnly
    public PtNotice getNoticeByNoticeTypeAndAssessmentNumner(final String noticeType, final String assessementNumber) {
		return (PtNotice) entityManager.createNamedQuery("getNoticesByAssessmentNoAndType")
				.setParameter("noticeType", noticeType.toUpperCase()).setParameter("upicNo", assessementNumber);
    }

    public PersistenceService<BasicProperty, Long> getBasicPropertyService() {
        return basicPropertyService;
    }

    public void setbasicPropertyService(final PersistenceService<BasicProperty, Long> basicPropertyService) {
        this.basicPropertyService = basicPropertyService;
    }
    
    @ReadOnly
    public String getNoticeByApplicationNo(final String applicationNo) {
        final StringBuilder queryStr = new StringBuilder(500);
        String noticeNum;
        queryStr.append(
                "select notice.noticeNo from PtNotice notice left join notice.basicProperty bp , PropertyMutation mt ");
        queryStr.append(" where notice.applicationNumber=:applicationNo");
        queryStr.append(
                " and notice.id = ( select max(id) from PtNotice where  applicationNumber = notice.applicationNumber and basicProperty = mt.basicProperty)");
        final Query query = entityManager.unwrap(Session.class).createQuery(queryStr.toString());
        if (StringUtils.isNotBlank(applicationNo))
            query.setParameter("applicationNo", applicationNo);
        @SuppressWarnings("unchecked")
        final List<String> notices = query.list();
        if (!notices.isEmpty())
            noticeNum = (String) query.list().get(0);
        else
            noticeNum = "";
        return noticeNum;
    }
    
    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PropertyMutation> getListofMutations(final String indexNumber) {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append("select mt from PropertyMutation mt left join mt.basicProperty bp ");
        if (StringUtils.isNotBlank(indexNumber))
            queryStr.append(" where bp.upicNo=:assessmentNo ");
        queryStr.append(" order by mt.mutationDate desc ");
		return entityManager.unwrap(Session.class).createQuery(queryStr.toString())
				.setParameter("assessmentNo", indexNumber).getResultList();
    }
    
    @SuppressWarnings("unchecked")
    @ReadOnly
    public PtNotice getNoticeByTypeUpicNoAndFinYear(final String noticeType, final String assessementNumber) {
        final CFinancialYear currFinYear = financialYearDAO.getFinancialYearByDate(new Date());
        List<PtNotice> notices = (List<PtNotice>) entityManager.unwrap(Session.class)
                .createQuery("from PtNotice where noticeType =:type and basicProperty.upicNo =:upicNo "
                        + " and noticeDate between :fromDate and :toDate ")
                .setParameter("type", noticeType).setParameter("upicNo", assessementNumber)
                .setParameter("fromDate", currFinYear.getStartingDate())
                .setParameter("toDate", currFinYear.getEndingDate()).getResultList();
        return !notices.isEmpty() ? notices.get(0) : null;
    }
    
    @SuppressWarnings("unchecked")
    @ReadOnly
    public PtNotice getPtNoticeByNoticeNumberAndBillType(final String noticeNo, final List<String> noticeType) {
        List<PtNotice> notices = (List<PtNotice>) entityManager.unwrap(Session.class)
                .createQuery(
                        "from PtNotice Pn where upper(Pn.noticeNo) = :noticeNumber and upper(noticeType) in (:noticeType)")
                .setParameter("noticeNumber", noticeNo.toUpperCase()).setParameter("noticeType", noticeType).getResultList();
        return !notices.isEmpty() ? notices.get(0) : null;
    }
    
    @ReadOnly
    @SuppressWarnings("unchecked")
    public List<PtNotice> getNoticeByAssessmentNumner(final String assessementNumber) {
        return (List<PtNotice>) entityManager.createNamedQuery("getAllNoticesByAssessmentNo").setParameter("upicNo",
                assessementNumber).getResultList();
    }

    /**
     * API generates the GIS comparison report on RO approval before the third party verification
     */
    public void generateComparisonNotice(PropertyImpl property) {
        InputStream noticePDF = null;
        String noticeType = NOTICE_TYPE_SURVEY_COMPARISON;
        final String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(noticeType);
        ReportRequest reportInput = generateReportRequestForComparisonReport(property);
        ReportOutput reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            noticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        saveNotice(property.getApplicationNo(), noticeNo, noticeType,
                property.getBasicProperty(), noticePDF);
    }

    private ReportRequest generateReportRequestForComparisonReport(final PropertyImpl property) {
        BigDecimal halfYearlyTax = BigDecimal.ZERO;
        BigDecimal gisHalfYearlyTax = BigDecimal.ZERO;
        BigDecimal applARV = BigDecimal.ZERO;
        BigDecimal gisARV = BigDecimal.ZERO;
        final Map<String, Object> reportParams = new HashMap<>();
        final Map<String, Installment> currYearInstMap = propertyTaxUtil.getInstallmentsForCurrYear(new Date());
        BasicProperty basicProperty = property.getBasicProperty();
        PropertyImpl gisProperty = (PropertyImpl) propertyHibernateDAO
                .getLatestGISPropertyForBasicProperty(basicProperty);
        Ptdemand ptdemand = property.getPtDemandSet().iterator().next();
        if (ptdemand != null) {
            halfYearlyTax = propService.getTotalTaxExcludingUACPenalty(currYearInstMap.get(CURRENTYEAR_SECOND_HALF),
                    property.getPtDemandSet().iterator().next());
            applARV = ptdemand.getDmdCalculations().getAlv() == null ? ZERO
                    : ptdemand.getDmdCalculations().getAlv();
        }
        if (gisProperty != null) {
            Ptdemand gisPtdemand = gisProperty.getPtDemandSet().iterator().next();
            if (gisPtdemand != null) {
                gisHalfYearlyTax = propService.getTotalTaxExcludingUACPenalty(gisPtdemand.getEgInstallmentMaster(),
                        gisProperty.getPtDemandSet().iterator().next());
                gisARV = gisPtdemand.getDmdCalculations().getAlv() == null ? ZERO
                        : gisPtdemand.getDmdCalculations().getAlv();
            }
        }
        reportParams.put("ulbName", ApplicationThreadLocals.getCityName());
        reportParams.put("ulbCode", ApplicationThreadLocals.getCityCode());
        reportParams.put("upicNo", basicProperty.getUpicNo());
        reportParams.put("applicationNo", property.getApplicationNo());
        reportParams.put("ownerName", basicProperty.getFullOwnerName());
        reportParams.put("doorNo", basicProperty.getAddress().getHouseNoBldgApt());
        if(property.getGisDetails() != null)
            reportParams.put("gisZone", property.getGisDetails().getGisZone().getName());
        reportParams.put("applZone", basicProperty.getPropertyID().getZone().getName());
        reportParams.put("halfYearlyTax", halfYearlyTax);
        reportParams.put("gisHalfYearlyTax", gisHalfYearlyTax);
        reportParams.put("applARV", applARV);
        reportParams.put("gisARV", gisARV);

        SurveyReportBean reportBean = setBeanDetailsForNotice(property, gisProperty);
        ReportRequest reportInput = new ReportRequest(NOTICE_TEMPLATE_COMPARISON_NOTICE, reportBean,
                reportParams);
        reportInput.setPrintDialogOnOpenReport(true);
        reportInput.setReportFormat(ReportFormat.PDF);

        return reportInput;
    }

    private SurveyReportBean setBeanDetailsForNotice(final PropertyImpl property, final PropertyImpl gisProperty) {
        SurveyReportBean surveyReportBean = new SurveyReportBean();
        PropertyDetail propertyDetail = property.getPropertyDetail();
        Collections.sort(propertyDetail.getFloorDetails(),
                (floor1, floor2) -> floor1.getFloorNo().compareTo(floor2.getFloorNo()));
        surveyReportBean.setApplicationFloors(setFloorDetails(propertyDetail));
        PropertyDetail gisPropertyDetail = gisProperty.getPropertyDetail();
        Collections.sort(gisPropertyDetail.getFloorDetails(),
                (floor1, floor2) -> floor1.getFloorNo().compareTo(floor2.getFloorNo()));
        surveyReportBean.setGisFloors(setFloorDetails(gisPropertyDetail));
        return surveyReportBean;
    }

    private List<FloorDetails> setFloorDetails(PropertyDetail propertyDetail) {
        FloorDetails floorDetails;
        List<FloorDetails> floorDetailsList = new ArrayList<>();
        for (Floor floor : propertyDetail.getFloorDetails()) {
            floorDetails = new FloorDetails();
            floorDetails.setFloorNoCode(FLOOR_MAP.get(floor.getFloorNo()));
            floorDetails.setBuildClassificationCode(floor.getStructureClassification().getTypeName());
            floorDetails.setNatureOfUsageCode(floor.getPropertyUsage().getUsageName());
            floorDetails.setOccupancyCode(floor.getPropertyOccupation().getOccupation());
            floorDetails.setPlinthArea(floor.getBuiltUpArea().getArea());
            floorDetails.setConstructionDate(
                    DateUtils.getFormattedDate(floor.getConstructionDate(), PropertyTaxConstants.DATE_FORMAT_DDMMYYY));
            floorDetailsList.add(floorDetails);
        }
        return floorDetailsList;
    }

}
