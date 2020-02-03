package org.egov.ptis.service.revisionpetitionreport;

import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_DDMMYYY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_DRAINAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIGHT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SCAVENGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_WATER_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.utils.DateUtils;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.objection.Petition;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.RevisionPetitionReport;
import org.egov.ptis.domain.entity.property.RevisionPetitionReportTax;
import org.egov.ptis.domain.service.notice.NoticeService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.notice.PtNotice;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevisionPetitionReportService {

    @Autowired
    PropertyService propertyService;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    NoticeService noticeService;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<RevisionPetitionReport> getReportList(final String fromDate, final String toDate) {
        int recordCount = 0;
        List<RevisionPetitionReport> reportList = new ArrayList<>();
        Petition revisionPetition = new Petition();
        List<PropertyImpl> rpList = prepareQueryforRPList(DateUtils.getDate(fromDate, DATE_FORMAT_DDMMYYY),
                DateUtils.getDate(toDate, DATE_FORMAT_DDMMYYY));
        RevisionPetitionReportTax revisionPetitionReportTax = new RevisionPetitionReportTax();
        for (PropertyImpl result : rpList) {
            RevisionPetitionReport revisionPetitionReport = new RevisionPetitionReport();
            revisionPetitionReport.setRevisionPetitionReportTax(revisionPetitionReportTax);
            ++recordCount;
            revisionPetition.setProperty(result);
            revisionPetitionReport.setAssessmentNo(result.getBasicProperty().getUpicNo());
            revisionPetitionReport.setApproverRemarks(getRemarks(result));
            revisionPetitionReport.setCount(Long.valueOf(recordCount));
            revisionPetitionReport.setOwnerName(result.getBasicProperty().getPrimaryOwner().getName());
            revisionPetitionReport.setCreatedDate(DateUtils.getFormattedDate(result.getCreatedDate(), DATE_FORMAT_DDMMYYY));
            revisionPetitionReport.setNoticeDate(DateUtils.getFormattedDate(getNoticeDate(result), DATE_FORMAT_DDMMYYY));
            revisionPetitionReport.setRevisionPetitionReportTax(getTaxDetails(result));
            revisionPetitionReport.setPropertyType(result.getPropertyDetail().getPropertyTypeMaster().getType());
            reportList.add(revisionPetitionReport);
        }
        return reportList;
    }

    public List<PropertyImpl> prepareQueryforRPList(final Date fromDate, final Date toDate) {
        Query getreportQuery = null;
        StringBuilder query = new StringBuilder("select  distinct prop from PropertyImpl prop,Petition petition,State states")
                .append(" ")
                .append("where prop.id = petition.property.id ").append("and states.id=petition.state.id").append(" ")
                .append("and prop.propertyModifyReason='RP' and prop.status in ('H','A')").append(" ")
                .append("and DATE(prop.createdDate) >= :fromDate and DATE(prop.createdDate) <= :toDate").append(" ")
                .append("and states.value='Closed' order by prop.id desc");
        getreportQuery = getCurrentSession()
                .createQuery(query.toString());
        getreportQuery.setParameter("fromDate", fromDate);
        getreportQuery.setParameter("toDate", toDate);
        return getreportQuery.list();
    }

    public PropertyImpl getPreviousPropertyList(final PropertyImpl property) {
        Query getreportQuery = null;
        PropertyImpl propertyImpl = null;
        StringBuilder query = new StringBuilder(
                "from PropertyImpl where status='H' and lastModifiedDate <= :modifiedDate").append(" ")
                        .append("and basicProperty.id = :basicPropertyId and propertyModifyReason<>'RP'  order by id desc");
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

    public Petition getPetition(Long propertyId) {
        Query qry = null;
        qry = getCurrentSession()
                .createQuery(
                        "from Petition  where property.id=:propertyId");
        qry.setLong("propertyId", propertyId);
        return (Petition) qry.list().get(0);
    }

    public RevisionPetitionReportTax getTaxDetails(final PropertyImpl property) {
        RevisionPetitionReportTax revisionPetitionReportTax = new RevisionPetitionReportTax();
        getDemandAmount(propertyService.getLatestDemandforHistoryProp(property), property.getEffectiveDate(),
                revisionPetitionReportTax, property);
        return revisionPetitionReportTax;
    }

    public List<Map<String, BigDecimal>> getAmount(Ptdemand ptdemand, Date effectiveDate) {
        Query qry = null;
        Map<String, BigDecimal> mapValue = new HashMap<String, BigDecimal>();
        List<Map<String, BigDecimal>> list = new ArrayList<Map<String, BigDecimal>>();
        final String selectQuery = " select drm.code,sum(dd.amount) from eg_demand_details dd, eg_demand_reason dr,"
                + " eg_demand_reason_master drm, eg_installment_master inst "
                + " where dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and dd.id_demand =:demandId and inst.start_date >= :effectiveDate group by drm.code";

        qry = getCurrentSession().createSQLQuery(selectQuery);
        qry.setLong("demandId", ptdemand.getId());
        qry.setDate("effectiveDate", effectiveDate);
        List<Object> result = qry.list();
        for (final Object record : result) {
            final Object[] data = (Object[]) record;
            final Double dmd = (Double) data[1];
            mapValue.put((String) data[0], BigDecimal.valueOf(dmd.doubleValue()));

        }
        list.add(mapValue);
        return list;
    }

    public void getDemandAmount(Ptdemand ptdemand, Date effectiveDate, RevisionPetitionReportTax revisionPetitionReportTax,
            final PropertyImpl property) {
        List<Map<String, BigDecimal>> demand = getAmount(ptdemand, effectiveDate);
        demand.stream().forEach(mapsData -> {
            mapsData.entrySet().forEach(mapData -> {
                if (mapData.getKey().equals(DEMANDRSN_CODE_GENERAL_TAX) || mapData.getKey().equals(DEMANDRSN_CODE_VACANT_TAX))
                    revisionPetitionReportTax
                            .setCurrentGenTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_WATER_TAX))
                    revisionPetitionReportTax
                            .setCurrentWaterTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_LIBRARY_CESS))
                    revisionPetitionReportTax
                            .setCurrentLibTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_SCAVENGE_TAX))
                    revisionPetitionReportTax
                            .setCurrentSacvagTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_LIGHT_TAX))
                    revisionPetitionReportTax
                            .setCurrentLightTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_EDUCATIONAL_TAX))
                    revisionPetitionReportTax
                            .setCurrentEduTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_DRAINAGE_TAX))
                    revisionPetitionReportTax
                            .setCurrentDrainageTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                    revisionPetitionReportTax
                            .setCurrentUnAuthPenaltyTax(
                                    mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                            BigDecimal.ROUND_HALF_UP));
            });
            revisionPetitionReportTax.setCurrentTotalTax((revisionPetitionReportTax.getCurrentGenTax() != null
                    ? revisionPetitionReportTax.getCurrentGenTax() : BigDecimal.ZERO)
                            .add(revisionPetitionReportTax.getCurrentDrainageTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentDrainageTax())
                            .add(revisionPetitionReportTax.getCurrentEduTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentEduTax())
                            .add(revisionPetitionReportTax.getCurrentLibTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentLibTax())
                            .add(revisionPetitionReportTax.getCurrentLightTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentLightTax())
                            .add(revisionPetitionReportTax.getCurrentSacvagTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentSacvagTax())
                            .add(revisionPetitionReportTax.getCurrentWaterTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentWaterTax())
                            .add(revisionPetitionReportTax.getCurrentUnAuthPenaltyTax() == null ? BigDecimal.ZERO
                                    : revisionPetitionReportTax.getCurrentUnAuthPenaltyTax()));
        });
        PropertyImpl propertyImpl = getPreviousPropertyList(property);
        getPrevDemandAmount(propertyService.getLatestDemandforHistoryProp(propertyImpl), property.getEffectiveDate(),
                revisionPetitionReportTax);
    }

    public void getPrevDemandAmount(Ptdemand ptdemand, Date effectiveDate, RevisionPetitionReportTax revisionPetitionReportTax) {
        List<Map<String, BigDecimal>> demand = getAmount(ptdemand, effectiveDate);
        demand.stream().forEach(mapsData -> {
            mapsData.entrySet().forEach(mapData -> {
                if (mapData.getKey().equals(DEMANDRSN_CODE_GENERAL_TAX) || mapData.getKey().equals(DEMANDRSN_CODE_VACANT_TAX))
                    revisionPetitionReportTax
                            .setPrevGenTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_WATER_TAX))
                    revisionPetitionReportTax
                            .setPrevWaterTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_LIBRARY_CESS))
                    revisionPetitionReportTax
                            .setPrevLibTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_SCAVENGE_TAX))
                    revisionPetitionReportTax
                            .setPrevSacvagTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_LIGHT_TAX))
                    revisionPetitionReportTax
                            .setPrevLightTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_EDUCATIONAL_TAX))
                    revisionPetitionReportTax
                            .setPrevEduTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_DRAINAGE_TAX))
                    revisionPetitionReportTax
                            .setPrevDrainageTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
                if (mapData.getKey().equals(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                    revisionPetitionReportTax
                            .setPrevUnAuthPenaltyTax(mapData.getValue() == null ? BigDecimal.ZERO : mapData.getValue().setScale(0,
                                    BigDecimal.ROUND_HALF_UP));
            });
        });
        revisionPetitionReportTax.setPrevTotalTax(
                (revisionPetitionReportTax.getPrevGenTax() != null ? revisionPetitionReportTax.getPrevGenTax() : BigDecimal.ZERO)
                        .add(revisionPetitionReportTax.getPrevDrainageTax() != null
                                ? revisionPetitionReportTax.getPrevDrainageTax() : BigDecimal.ZERO)
                        .add(revisionPetitionReportTax.getPrevEduTax() == null ? BigDecimal.ZERO
                                : revisionPetitionReportTax.getPrevEduTax())
                        .add(revisionPetitionReportTax.getPrevLibTax() == null ? BigDecimal.ZERO
                                : revisionPetitionReportTax.getPrevLibTax())
                        .add(revisionPetitionReportTax.getPrevLightTax() == null ? BigDecimal.ZERO
                                : revisionPetitionReportTax.getPrevLightTax())
                        .add(revisionPetitionReportTax.getPrevSacvagTax() == null ? BigDecimal.ZERO
                                : revisionPetitionReportTax.getPrevSacvagTax())
                        .add(revisionPetitionReportTax.getPrevWaterTax() == null ? BigDecimal.ZERO
                                : revisionPetitionReportTax.getPrevWaterTax())
                        .add(revisionPetitionReportTax.getPrevUnAuthPenaltyTax() == null ? BigDecimal.ZERO
                                : revisionPetitionReportTax.getPrevUnAuthPenaltyTax()));
    }

    public String getRemarks(PropertyImpl property) {
        String remarks = "";
        Petition petition = getPetition(property.getId());
        remarks = petition.getState().getComments() == null
                ? petition.getStateHistory().get(petition.getStateHistory().size() - 1).getComments()
                : petition.getState().getComments();
        return remarks;
    }

    public Date getNoticeDate(PropertyImpl property) {
        PtNotice notice = null;
        Date noticeDate = null;
        Petition petition = getPetition(property.getId());
        notice = getNoticeDateByApplicationNum(petition.getObjectionNumber());
        if (!notice.getNoticeType().equalsIgnoreCase(NOTICE_TYPE_REVISIONPETITION_HEARINGNOTICE)) {
            noticeDate = notice.getNoticeDate();
        }
        return noticeDate;
    }

    public PtNotice getNoticeDateByApplicationNum(String applicationNo) {
        Query qry = null;
        qry = getCurrentSession()
                .createQuery(
                        "from PtNotice  where applicationNumber=:applicationNo order by id desc");
        qry.setParameter("applicationNo", applicationNo);
        return (PtNotice) qry.list().get(0);
    }
}
