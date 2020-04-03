package org.egov.ptis.service.appealpetitionreport;

import static org.egov.ptis.constants.PropertyTaxConstants.CITY_GRADE_CORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_APPEALPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_RPPROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.objection.Petition;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.view.AppealReportInfo;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppealReportService {

    @Autowired
    PropertyService propertyService;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    NoticeService noticeService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private transient CityService cityService;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public ReportOutput generateAppealReport(final Long wardId,
            final CFinancialYear finYear, final Long elactionWardId) {
        ReportOutput reportOutput = null;
        ReportRequest reportInput;
        Boolean isCorporation;
        final Map<String, Object> reportParams = new HashMap<>();
        List<AppealReportInfo> appealInfoList = new ArrayList<>();
        appealInfoList.addAll(getDetails(finYear, wardId, elactionWardId));
        reportParams.put("selectedYear", finYear.getFinYearRange());
        final String cityGrade = cityService.getCityGrade();
        if (StringUtils.isNotBlank(cityGrade)
                && CITY_GRADE_CORPORATION.equalsIgnoreCase(cityGrade))
            isCorporation = true;
        else
            isCorporation = false;
        if (!isCorporation) {
            reportParams.put("appealInfoList", appealInfoList);
            reportParams.put("cityName", cityService.getMunicipalityName());
            reportParams.put("cityGrade", cityGrade);
            reportInput = new ReportRequest(PropertyTaxConstants.APPEAL_REPORT_TEMPLATE, reportParams, reportParams);
            reportInput.setReportFormat(ReportFormat.PDF);
            reportInput.setPrintDialogOnOpenReport(true);
            reportOutput = reportService.createReport(reportInput);
        }

        return reportOutput;

    }

    public List<AppealReportInfo> getDetails(final CFinancialYear financialYear, Long ward, Long electionWard) {
        List<AppealReportInfo> report = new ArrayList<>();
        List<PropertyImpl> appealList = prepareQueryforAppealList(financialYear, ward, electionWard);
        Long count = 0L;
        for (PropertyImpl property : appealList) {
            AppealReportInfo apppealInfo = new AppealReportInfo();
            apppealInfo.setSlNo(++count);
            apppealInfo.setOwnerName(property.getBasicProperty().getFullOwnerName());
            apppealInfo.setAssessmentDate(property.getBasicProperty().getAssessmentdate());
            apppealInfo.setAssessmentNo(property.getBasicProperty().getUpicNo());
            Petition appealPetition = getPetition(property.getId());
            apppealInfo.setAppealApprovedDate(
                    getNoticeDateByApplicationNum(appealPetition.getObjectionNumber(), NOTICE_TYPE_APPEALPROCEEDINGS)
                            .getNoticeDate());
            apppealInfo.setDisposalDate(appealPetition.getDisposalDate());
            apppealInfo.setAppeallateComments(appealPetition.getAppellateComments());
            List<String> reasonList = appealPetition.getAppealReasons().stream().map(reason -> reason.getDescription())
                    .collect(Collectors.toList());
            apppealInfo.setGroundOfAppeal(reasonList.stream()
                    .collect(Collectors.joining(",")));
            apppealInfo.setAppeallateApplicationNo(appealPetition.getObjectionNumber());
            for (Ptdemand ptdemand : property.getPtDemandSet())
                apppealInfo.setCurrentTax(ptdemand.getBaseDemand());
            getRPDetails(property, apppealInfo);
            report.add(apppealInfo);
        }
        return report;
    }

    public void getRPDetails(PropertyImpl property, AppealReportInfo apppealInfo) {
        PropertyImpl prevRPDetails = getPreviousRPPropertyList(property);
        Petition revisionPetition = getPetition(prevRPDetails.getId());
        apppealInfo.setRevisionPetitionNo(revisionPetition.getObjectionNumber());
        apppealInfo.setRevisionPetitionApprovedDate(
                getNoticeDateByApplicationNum(revisionPetition.getObjectionNumber(), NOTICE_TYPE_RPPROCEEDINGS).getNoticeDate());
        PropertyImpl prop = getBeforeRPTransactionDetails(prevRPDetails);
        apppealInfo.setSpecialNoticeDate(
                getNoticeDateByApplicationNum(prop.getApplicationNo(), NOTICE_TYPE_SPECIAL_NOTICE).getNoticeDate());
        getCollectionDetails(prevRPDetails, apppealInfo);
        for (Ptdemand ptdemand : prevRPDetails.getPtDemandSet())
            apppealInfo.setPreviousTax(ptdemand.getBaseDemand() != null
                    ? ptdemand.getBaseDemand().setScale(2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
    }

    @ReadOnly
    public List<PropertyImpl> prepareQueryforAppealList(final CFinancialYear financialYear, Long ward, Long electionWard) {
        Query getreportQuery = null;
        StringBuilder query = new StringBuilder("select  distinct prop from PropertyImpl prop,Petition petition,State states")
                .append(" ")
                .append("where prop.id = petition.property.id ").append("and states.id=petition.state.id").append(" ")
                .append("and prop.propertyModifyReason='APPEAL_PETETION' and prop.status in ('H','A')").append(" ")
                .append("and DATE(prop.createdDate) >= :fromDate and DATE(prop.createdDate) <= :toDate").append(" ")
                .append("and states.value='Closed'");
        if (isWard(ward))
            query.append(" and prop.basicProperty.propertyID.ward = :ward");
        if (isWard(electionWard))
            query.append(" and prop.basicProperty.propertyID.electionBoundary = :electionWard");
        query.append(" order by prop.id desc");
        getreportQuery = getCurrentSession()
                .createQuery(query.toString());
        getreportQuery.setParameter("fromDate", financialYear.getStartingDate());
        getreportQuery.setParameter("toDate", financialYear.getEndingDate());
        if (isWard(electionWard))
            getreportQuery.setLong("electionWard", electionWard);
        if (isWard(ward))
            getreportQuery.setLong("ward", ward);
        return getreportQuery.list();
    }

    @ReadOnly
    public PropertyImpl getPreviousRPPropertyList(final PropertyImpl property) {
        Query getreportQuery = null;
        PropertyImpl propertyImpl = null;
        StringBuilder query = new StringBuilder(
                "from PropertyImpl where status='H' and lastModifiedDate <= :modifiedDate").append(" ")
                        .append("and basicProperty.id = :basicPropertyId and propertyModifyReason = 'RP'  order by id desc");
        getreportQuery = getCurrentSession()
                .createQuery(query.toString());
        getreportQuery.setParameter("modifiedDate", property.getLastModifiedDate());
        getreportQuery.setLong("basicPropertyId", property.getBasicProperty().getId());
        getreportQuery.setMaxResults(1);
        final List<PropertyImpl> result = getreportQuery.list();
        if (!result.isEmpty())
            propertyImpl = result.get(0);
        return propertyImpl;
    }

    @ReadOnly
    public PropertyImpl getBeforeRPTransactionDetails(final PropertyImpl property) {
        Query getreportQuery = null;
        StringBuilder query = new StringBuilder(
                "from PropertyImpl where status='H' and lastModifiedDate < :modifiedDate").append(" ")
                        .append("and basicProperty.id = :basicPropertyId and propertyModifyReason in ('CREATE','ADD_OR_ALTER')  order by id desc");
        getreportQuery = getCurrentSession()
                .createQuery(query.toString());
        getreportQuery.setParameter("modifiedDate", property.getLastModifiedDate());
        getreportQuery.setLong("basicPropertyId", property.getBasicProperty().getId());
        getreportQuery.setMaxResults(1);
        List<PropertyImpl> propertyList = getreportQuery.list();
        return propertyList.get(0);
    }

    public void getCollectionDetails(final PropertyImpl property, final AppealReportInfo apppealInfo) {
        for (EgDemand demand : property.getPtDemandSet())
            for (EgDemandDetails demandDetails : demand.getEgDemandDetails())
                for (EgdmCollectedReceipt receipts : demandDetails.getEgdmCollectedReceipts())
                    if (STATUS_ISACTIVE.equals(receipts.getStatus()) && apppealInfo.getCollection() == null) {
                        apppealInfo.setCollection(receipts.getAmount() == null ? BigDecimal.ZERO
                                : receipts.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                        apppealInfo.setReceiptDate(receipts.getReceiptDate());
                        break;
                    }
    }

    @ReadOnly
    public Petition getPetition(Long propertyId) {
        Query qry = null;
        qry = getCurrentSession()
                .createQuery(
                        "from Petition  where property.id=:propertyId");
        qry.setLong("propertyId", propertyId);
        return (Petition) qry.list().get(0);
    }

    @ReadOnly
    public PtNotice getNoticeDateByApplicationNum(String applicationNo, String noticeType) {

        Query qry = null;

        qry = getCurrentSession()
                .createQuery(
                        "from PtNotice  where applicationNumber=:applicationNo and noticeType =:noticeType order by id desc");
        qry.setParameter("applicationNo", applicationNo);
        qry.setParameter("noticeType", noticeType);
        return (PtNotice) qry.list().get(0);
    }

    @ReadOnly
    public String getCityGrade() {
        final Query query = (Query) entityManager.createQuery("from City");
        final City city = (City) ((javax.persistence.Query) query).getSingleResult();
        return city.getGrade();
    }

    public boolean isWard(final Long wardId) {
        return wardId != null && wardId != -1;
    }

}
