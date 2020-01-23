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

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.CITY_GRADE_CORPORATION;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.RedNoticeInfo;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedNoticeService {

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private PropertyTaxNumberGenerator propertyTaxNumberGenerator;
    @Autowired
    private ReportService reportService;
    @Autowired
    private transient CityService cityService;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @ReadOnly
    public RedNoticeInfo getRedNoticeInformation(String assessmentNo) {
        RedNoticeInfo redNoticeInfo;
        BigDecimal totalDue;
        BigDecimal currPenalty;
        BigDecimal currPenaltyColl;
        PropertyMaterlizeView propView = getPropertyViewDetails(assessmentNo);
        redNoticeInfo = getInstDmdInfo(propView);
        redNoticeInfo.setAssessmentNo(propView.getPropertyId());
        redNoticeInfo.setOwnerName(getOwerName(propView));
        redNoticeInfo.setRevenueWard(propView.getWard().getName());
        redNoticeInfo.setDoorNo(propView.getHouseNo());
        redNoticeInfo.setLocality(getLocality(propView));
        redNoticeInfo.setMobileNo(getMobileNo(propView));
        redNoticeInfo.setArrearTax(propView.getAggrArrDmd().subtract(propView.getAggrArrColl()));
        redNoticeInfo.setCurrentTax(propView.getAggrCurrFirstHalfDmd().add(propView.getAggrCurrSecondHalfDmd())
                .subtract(propView.getAggrCurrFirstHalfColl().add(propView.getAggrCurrSecondHalfColl())));
        redNoticeInfo.setArrearPenaltyTax(getAggArrPenaltyDue(propView));
        currPenalty = getAggCurrFirstHalfPenalty(propView)
                .add(getAggCurrSecHalfPenalty(propView));
        currPenaltyColl = getAggCurrFirstHalfPenColl(propView)
                .add(getAggCurrSecHalfPenColl(propView));
        redNoticeInfo.setCurrentTaxPenalty(currPenalty.subtract(currPenaltyColl));
        totalDue = redNoticeInfo.getArrearTax().add(redNoticeInfo.getCurrentTax())
                .add(redNoticeInfo.getArrearPenaltyTax()).add(redNoticeInfo.getCurrentTaxPenalty());
        redNoticeInfo.setTotalDue(totalDue);
        return redNoticeInfo;

    }

    public PropertyMaterlizeView getPropertyViewDetails(final String assessmentNo) {
        final PropertyMaterlizeView propertyView;
        final Query query = entityManager.createQuery("from PropertyMaterlizeView where propertyId = :propertyId");
        query.setParameter("propertyId", assessmentNo);
        propertyView = (PropertyMaterlizeView) query.getSingleResult();
        return propertyView;
    }

    @ReadOnly
    private RedNoticeInfo getInstDmdInfo(final PropertyMaterlizeView propView) {
        final RedNoticeInfo redNoticeInfo = new RedNoticeInfo();

        Iterator itr;
        InstDmdCollMaterializeView idc;
        if (!propView.getInstDmdColl().isEmpty()) {
            itr = propView.getInstDmdColl().iterator();
            Installment minInstallment = null;
            Installment maxInstallment = null;
            while (itr.hasNext()) {
                BigDecimal dmdtot;
                BigDecimal colltot;
                idc = (InstDmdCollMaterializeView) itr.next();
                dmdtot = getGenTax(idc).add(getEduCess(idc)).add(getLibCess(idc)).add(getPenaltyFines(idc))
                        .add(getPubSerCharge(idc)).add(getSewTax(idc)).add(getUnaPenalty(idc)).add(getVacLandTax(idc));

                colltot = getGenTaxColl(idc).add(getEduCessColl(idc)).add(getLibCessColl(idc)).add(getPenaltyFineColl(idc))
                        .add(getPubServiceColl(idc)).add(getSewColl(idc)).add(getUnauthPenColl(idc)).add(getVacLColl(idc));
                minInstallment = getMinInstallment(minInstallment, idc, dmdtot, colltot);
                maxInstallment = getMaxInstallment(maxInstallment, idc, dmdtot, colltot);
            }
            if (minInstallment != null) {
                redNoticeInfo.setMinDate(minInstallment.getFromDate());
                redNoticeInfo.setFromInstallment(minInstallment.getDescription());
            }
            if (maxInstallment != null) {
                redNoticeInfo.setMaxDate(maxInstallment.getFromDate());
                redNoticeInfo.setToInstallment(maxInstallment.getDescription());
            }
            if(minInstallment == null && maxInstallment == null){
                redNoticeInfo.setInstallmentCount(true);
            }

        }
        return redNoticeInfo;
    }

    public Installment getMinInstallment(final Installment minInstallment, final InstDmdCollMaterializeView idc,
            final BigDecimal dmdtot, final BigDecimal colltot) {
        Installment inst = null;
        if (dmdtot.compareTo(colltot) > 0){
            if (minInstallment == null)
                return idc.getInstallment();
            else if (minInstallment.getFromDate().after(idc.getInstallment().getFromDate()))
                inst = idc.getInstallment();
        }
        return inst == null ? minInstallment : inst;
    }

    public Installment getMaxInstallment(final Installment maxInstallment, final InstDmdCollMaterializeView idc,
            final BigDecimal dmdtot, final BigDecimal colltot) {
        Installment inst = null;
        if (dmdtot.compareTo(colltot) > 0){
        if (maxInstallment == null)
            return idc.getInstallment();
        else if (maxInstallment.getFromDate().before(idc.getInstallment().getFromDate()) && dmdtot.compareTo(colltot) > 0)
            inst = idc.getInstallment();
        }
        return inst == null ? maxInstallment : inst;
    }

    public ReportOutput generateNotice(final String assessmentNo, final String noticeType) {
        ReportOutput reportOutput = null;
        final Map<String, Object> reportParams = new HashMap<>();
        InputStream noticePDF = null;
        ReportRequest reportInput = null;
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNo);
        RedNoticeInfo redNoticeInfo = getRedNoticeInformation(basicProperty.getUpicNo());
        final String noticeNo = propertyTaxNumberGenerator.generateNoticeNumber(noticeType);
        reportInput = generateRedNotice(redNoticeInfo, reportParams, noticeNo);
        reportInput.setPrintDialogOnOpenReport(true);
        reportInput.setReportFormat(ReportFormat.PDF);
        reportOutput = reportService.createReport(reportInput);
        if (reportOutput != null && reportOutput.getReportOutputData() != null)
            noticePDF = new ByteArrayInputStream(reportOutput.getReportOutputData());
        noticeService.saveNotice(null, noticeNo, noticeType,
                basicProperty, noticePDF);
        return reportOutput;
    }

    private ReportRequest generateRedNotice(RedNoticeInfo redNoticeInfo,
            final Map<String, Object> reportParams,
            final String noticeNo) {
        ReportRequest reportInput;
        Boolean isCorporation;
        final String cityGrade = cityService.getCityGrade();
        if (StringUtils.isNotBlank(cityGrade)
                && CITY_GRADE_CORPORATION.equalsIgnoreCase(cityGrade))
            isCorporation = true;
        else
            isCorporation = false;
        reportParams.put("isCorporation", isCorporation);
        reportParams.put("assessmentNo", redNoticeInfo.getAssessmentNo());
        final DateTime noticeDate = new DateTime();
        reportParams.put("year", Integer.toString(noticeDate.getYear()));
        reportParams.put("arrearTax", redNoticeInfo.getArrearTax().toString());
        reportParams.put("arrearTaxInterest", redNoticeInfo.getArrearPenaltyTax().toString());
        reportParams.put("currentTax", redNoticeInfo.getCurrentTax().toString());
        reportParams.put("currentTaxInterest", redNoticeInfo.getCurrentTaxPenalty().toString());
        reportParams.put("total", redNoticeInfo.getCurrentTax().add(redNoticeInfo.getCurrentTaxPenalty())
                .add(redNoticeInfo.getArrearTax()).add(redNoticeInfo.getArrearPenaltyTax()).toString());
        reportParams.put("locality", redNoticeInfo.getLocality());
        reportParams.put("noticeNo", noticeNo);
        reportParams.put("cityName", cityService.getMunicipalityName());
        reportInput = new ReportRequest(PropertyTaxConstants.TEMPLATENAME_MAIN_RED_NOTICE, redNoticeInfo, reportParams);
        return reportInput;
    }

    public String getCityGrade() {
        final Query query = entityManager.createQuery("from City");
        final City city = (City) query.getSingleResult();
        return city.getGrade();
    }

    private BigDecimal getAggCurrSecHalfPenColl(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrSecondHalfPenalyColl() == null ? ZERO : propView
                .getAggrCurrSecondHalfPenalyColl();
    }

    private BigDecimal getAggCurrFirstHalfPenColl(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrFirstHalfPenalyColl() == null ? ZERO : propView
                .getAggrCurrFirstHalfPenalyColl();
    }

    private BigDecimal getAggCurrSecHalfPenalty(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrSecondHalfPenaly() == null ? ZERO : propView.getAggrCurrSecondHalfPenaly();
    }

    private BigDecimal getAggCurrFirstHalfPenalty(final PropertyMaterlizeView propView) {
        return propView.getAggrCurrFirstHalfPenaly() == null ? ZERO : propView.getAggrCurrFirstHalfPenaly();
    }

    private BigDecimal getAggArrPenaltyDue(final PropertyMaterlizeView propView) {
        return (propView.getAggrArrearPenaly() == null ? ZERO : propView
                .getAggrArrearPenaly()).subtract(propView.getAggrArrearPenalyColl() == null ? ZERO : propView
                        .getAggrArrearPenalyColl());
    }

    private String getOwerName(final PropertyMaterlizeView propView) {
        return propView.getOwnerName() == null ? "NA"
                : propView.getOwnerName().contains(",") ? propView.getOwnerName().replace(",",
                        " & ") : propView.getOwnerName();
    }

    private String getLocality(final PropertyMaterlizeView propView) {
        return propView.getLocality() == null ? "NA" : propView.getLocality().getName();
    }

    private String getMobileNo(final PropertyMaterlizeView propView) {
        return StringUtils.isBlank(propView.getMobileNumber()) ?  "NA": propView
                .getMobileNumber();
    }

    private BigDecimal getVacLandTax(final InstDmdCollMaterializeView idc) {
        return idc.getVacantLandTax() == null ? ZERO : idc.getVacantLandTax();
    }

    private BigDecimal getUnaPenalty(final InstDmdCollMaterializeView idc) {
        return idc.getUnauthPenaltyTax() == null ? ZERO :idc.getUnauthPenaltyTax();
    }

    private BigDecimal getSewTax(final InstDmdCollMaterializeView idc) {
        return idc.getSewTax() == null ? ZERO: idc.getSewTax();
    }

    private BigDecimal getPubSerCharge(final InstDmdCollMaterializeView idc) {
        return idc.getPubSerChrgTax() == null ? ZERO: idc.getPubSerChrgTax();
    }

    private BigDecimal getPenaltyFines(final InstDmdCollMaterializeView idc) {
        return idc.getPenaltyFinesTax() == null ? ZERO: idc.getPenaltyFinesTax();
    }

    private BigDecimal getLibCess(final InstDmdCollMaterializeView idc) {
        return idc.getLibCessTax() == null ? ZERO:idc.getLibCessTax();
    }

    private BigDecimal getEduCess(final InstDmdCollMaterializeView idc) {
        return idc.getEduCessTax() == null ? ZERO :idc.getEduCessTax();
    }

    private BigDecimal getGenTax(final InstDmdCollMaterializeView idc) {
        return idc.getGeneralTax() == null ? ZERO: idc.getGeneralTax();
    }

    private BigDecimal getVacLColl(final InstDmdCollMaterializeView idc) {
        return idc.getVacantLandTaxColl() == null ? ZERO :idc.getVacantLandTaxColl();
    }

    private BigDecimal getUnauthPenColl(final InstDmdCollMaterializeView idc) {
        return idc.getUnauthPenaltyTaxColl() == null ? ZERO:idc.getUnauthPenaltyTaxColl();
    }

    private BigDecimal getSewColl(final InstDmdCollMaterializeView idc) {
        return idc.getSewTaxColl() == null ? ZERO: idc.getSewTaxColl();
    }

    private BigDecimal getPubServiceColl(final InstDmdCollMaterializeView idc) {
        return idc.getPubSerChrgTaxColl() == null ? ZERO :idc.getPubSerChrgTaxColl();
    }

    private BigDecimal getPenaltyFineColl(final InstDmdCollMaterializeView idc) {
        return idc.getPenaltyFinesTaxColl() == null ? ZERO: idc.getPenaltyFinesTaxColl();
    }

    private BigDecimal getLibCessColl(final InstDmdCollMaterializeView idc) {
        return idc.getLibCessTaxColl() == null ? ZERO : idc.getLibCessTaxColl();
    }

    private BigDecimal getEduCessColl(final InstDmdCollMaterializeView idc) {
        return idc.getEduCessTaxColl() == null ? ZERO: idc.getEduCessTaxColl();
    }

    private BigDecimal getGenTaxColl(final InstDmdCollMaterializeView idc) {
        return idc.getGeneralTaxColl() == null ? ZERO:idc.getGeneralTaxColl();
    }
}
