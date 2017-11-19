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
package org.egov.ptis.actions.reports;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.bean.AssesseeInfo;
import org.egov.ptis.bean.DemandCollInfo;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.bean.TaxInfo;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Calendar.YEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PATTERN_BEGINS_WITH_1TO9;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_BAKAYAFERIST;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE_BNDRY_TYPE;

@SuppressWarnings("serial")
@ParentPackage("egov")
public class BakayaFeristReportAction extends ReportFormAction {

    private static final String RESULT_NEW = "new";
    private static final Logger LOGGER = Logger.getLogger(BakayaFeristReportAction.class);

    private Integer zoneId;
    private Integer wardId;
    private String partNo;
    private Map<Long, String> ZoneBndryMap;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Override
    public void prepare() {

        @SuppressWarnings("unchecked")
        List<Boundary> zoneList = persistenceService.findAllBy(
                "from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
                        + "and BI.isHistory='N' order by BI.id", ZONE_BNDRY_TYPE, ELECTION_HIERARCHY_TYPE);

        setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
        prepareWardDropDownData(zoneId != null && !zoneId.equals(-1), wardId != null && !wardId.equals(-1));

        if (wardId == null || wardId.equals(-1)) {
            addDropdownData("partNumbers", Collections.EMPTY_LIST);
        }
    }

    public void prepareReport() {
        LOGGER.debug("Entered into prepareReport method");

        setDataSourceType(ReportDataSourceType.JAVABEAN);

        // Preparing the Bakaya Ferist report data

        ReportInfo reportInfo = prepareReportInfo();
        setReportData(reportInfo);

        LOGGER.debug("Exit from prepareReport method");
    }

    private ReportInfo prepareReportInfo() {
        LOGGER.debug("Entered into prepareReportInfo method");

        ReportInfo reportInfo = new ReportInfo();

        Boundary zone = (Boundary) persistenceService.find(
                "from BoundaryImpl BI where BI.id = ? and BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
                        + "and BI.isHistory='N' order by BI.id", getZoneId(), ZONE_BNDRY_TYPE, ELECTION_HIERARCHY_TYPE);
        Boundary ward = (Boundary) persistenceService.find(
                "from BoundaryImpl BI where BI.id = ? and BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
                        + "and BI.isHistory='N' order by BI.id", getWardId(), WARD_BNDRY_TYPE, ELECTION_HIERARCHY_TYPE);

        reportInfo.setZoneNo(zone.getBoundaryNum().toString());
        reportInfo.setWardNo(ward.getBoundaryNum().toString());
        reportInfo.setPartNo(partNo != null && !partNo.equals("-1") ? partNo : "N/A");

        Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();

        Calendar installmentFromDate = Calendar.getInstance();
        installmentFromDate.setTime(currentInstallment.getFromDate());

        Calendar installmentToDate = Calendar.getInstance();
        installmentToDate.setTime(currentInstallment.getToDate());

        reportInfo.setCurrInstallment(installmentFromDate.get(YEAR) + "-" + installmentToDate.get(YEAR));

        List<AssesseeInfo> assesseeInfoList = prepareAssessees();
        List<DemandCollInfo> emptyDemandCollInfoList = Collections.emptyList();
        List<PropertyMaterlizeView> emptyPMVList = Collections.emptyList();

        reportInfo.setAssesseeInfoList(assesseeInfoList);
        reportInfo.setDemandCollInfoList(emptyDemandCollInfoList);
        // reportInfo.setPropMatViewList(emptyPMVList);

        LOGGER.debug("Exit from prepareReportInfo method");

        return reportInfo;
    }

    @SuppressWarnings("unchecked")
    private List<AssesseeInfo> prepareAssessees() {
        LOGGER.debug("Entered into prepareAssesseeInformation method");

        List<AssesseeInfo> assessees = new ArrayList<AssesseeInfo>();
        StringBuilder queryBuilder = new StringBuilder(500);

        queryBuilder.append("from PropertyMaterlizeView pmv left join fetch pmv.instDmdColl instDmdColl ")
                .append("left join fetch instDmdColl.installment")
                .append(" where pmv.ward.id = ? and pmv.propTypeMstrID.code not in ('")
                .append(OWNERSHIP_TYPE_CENTRAL_GOVT_50).append("', '").append(OWNERSHIP_TYPE_STATE_GOVT).append("' ) ");
        if (partNo != null && !partNo.equals("-1")) {
            queryBuilder.append("and pmv.partNo = ?");
        }
        queryBuilder.append("order by to_number(regexp_substr(pmv.houseNo, '" + PATTERN_BEGINS_WITH_1TO9
                + "')), pmv.houseNo");
        Query qry = getPersistenceService().getSession().createQuery(queryBuilder.toString());
        qry.setParameter(0, getWardId());
        if (partNo != null && !partNo.equals("-1")) {
            qry.setParameter(1, partNo);
        }
        List<PropertyMaterlizeView> properties = (List<PropertyMaterlizeView>) qry.setResultTransformer(
                Criteria.DISTINCT_ROOT_ENTITY).list();

        Installment currentInstallment = propertyTaxCommonUtils.getCurrentInstallment();

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (PropertyMaterlizeView property : properties) {
            AssesseeInfo assessee = new AssesseeInfo();
            assessee.setIndexNo(property.getPropertyId());
            assessee.setHouseNo(property.getHouseNo());
            assessee.setOwnerName(property.getOwnerName());

            Set<TaxInfo> taxInfos = new TreeSet<TaxInfo>(new Comparator<TaxInfo>() {

                @Override
                public int compare(TaxInfo o1, TaxInfo o2) {
                    return o2.getInstallment().compareTo(o1.getInstallment());
                }
            });

            Set<InstDmdCollMaterializeView> demandDetailsAndInstallments = new TreeSet<InstDmdCollMaterializeView>(
                    new Comparator<InstDmdCollMaterializeView>() {
                        @Override
                        public int compare(InstDmdCollMaterializeView o1, InstDmdCollMaterializeView o2) {
                            return o1.getInstallment().compareTo(o2.getInstallment());
                        }
                    });

            demandDetailsAndInstallments.addAll(property.getInstDmdColl());

            grandTotal = BigDecimal.ZERO;

            for (InstDmdCollMaterializeView demandDetailsAndInstallment : demandDetailsAndInstallments) {
                TaxInfo taxInfo = new TaxInfo();

                /*
                 * Installment installment = (Installment)
                 * CommonsDaoFactory.getDAOFactory().getInstallmentDao()
                 * .findById
                 * (demandDetailsAndInstallment.getInstallment().getId(),
                 * false);
                 */

                /*
                 * installmentFromDate.setTime(installment.getFromDate());
                 * installmentToDate.setTime(installment.getToDate());
                 */

                // taxInfo.setInstallment(installmentFromDate.get(YEAR) + "-" +
                // installmentToDate.get(YEAR));
                // TODO : commented as part of DCB report story.
                /*
                 * axInfo.setInstallment(demandDetailsAndInstallment.getInstallment
                 * ().toString());
                 * taxInfo.setConservancyTax(demandDetailsAndInstallment
                 * .getSewerageTax()
                 * .subtract(demandDetailsAndInstallment.getSewerageTaxColl())
                 * .setScale(2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setGeneralTax
                 * (demandDetailsAndInstallment.getGeneralTax()
                 * .subtract(demandDetailsAndInstallment.getGeneralTaxColl())
                 * .setScale(2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setWaterTax(demandDetailsAndInstallment.getWaterTax()
                 * .
                 * subtract(demandDetailsAndInstallment.getWaterTaxColl()).setScale
                 * (2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setFireServiceTax(demandDetailsAndInstallment
                 * .getFireTax()
                 * .subtract(demandDetailsAndInstallment.getFireTaxColl
                 * ()).setScale(2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setLightTax(demandDetailsAndInstallment.getLightTax()
                 * .
                 * subtract(demandDetailsAndInstallment.getLightTaxColl()).setScale
                 * (2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setEduCess(demandDetailsAndInstallment
                 * .getEduCessNonResdTax()
                 * .add(demandDetailsAndInstallment.getEduCessResdTax())
                 * .subtract(
                 * demandDetailsAndInstallment.getEduCessNonResdTaxColl().add(
                 * demandDetailsAndInstallment.getEduCessResdTaxColl()))
                 * .setScale(2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setEgsCess(demandDetailsAndInstallment.getEgsTax()
                 * .subtract
                 * (demandDetailsAndInstallment.getEgsTaxColl()).setScale(2,
                 * BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setBigBuildingCess(demandDetailsAndInstallment
                 * .getBigBldgTax()
                 * .subtract(demandDetailsAndInstallment.getBigBldgTaxColl())
                 * .setScale(2, BigDecimal.ROUND_HALF_UP));
                 * taxInfo.setTotal(demandDetailsAndInstallment
                 * .getSewerageTax()
                 * .subtract(demandDetailsAndInstallment.getSewerageTaxColl())
                 * .add(demandDetailsAndInstallment.getGeneralTax().subtract(
                 * demandDetailsAndInstallment.getGeneralTaxColl()))
                 * .add(demandDetailsAndInstallment.getWaterTax().subtract(
                 * demandDetailsAndInstallment.getWaterTaxColl()))
                 * .add(demandDetailsAndInstallment.getFireTax().subtract(
                 * demandDetailsAndInstallment.getFireTaxColl()))
                 * .add(demandDetailsAndInstallment.getLightTax().subtract(
                 * demandDetailsAndInstallment.getLightTaxColl()))
                 * .add(demandDetailsAndInstallment
                 * .getEduCessNonResdTax().subtract(
                 * demandDetailsAndInstallment.getEduCessNonResdTaxColl()))
                 * .add(
                 * demandDetailsAndInstallment.getEduCessResdTax().subtract(
                 * demandDetailsAndInstallment.getEduCessResdTaxColl()))
                 * .add(demandDetailsAndInstallment.getEgsTax().subtract(
                 * demandDetailsAndInstallment.getEgsTaxColl()))
                 * .add(demandDetailsAndInstallment.getBigBldgTax().subtract(
                 * demandDetailsAndInstallment.getBigBldgTaxColl())).setScale(2,
                 * BigDecimal.ROUND_HALF_UP)); grandTotal =
                 * grandTotal.add(taxInfo.getTotal());
                 */

                if (currentInstallment.equals(demandDetailsAndInstallment.getInstallment())) {
                    taxInfo.setGrandTotal(grandTotal.setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (!taxInfo.getTotal().equals(BigDecimal.ZERO.setScale(2))) {
                    taxInfos.add(taxInfo);
                }
            }
            assessee.setTaxInfoList(new ArrayList<TaxInfo>(taxInfos));
            assessees.add(assessee);
        }

        LOGGER.debug("Exit from prepareReportInfo method");

        return assessees;
    }

    @SuppressWarnings("unchecked")
    private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
        LOGGER.debug("Entered into prepareWardDropDownData method");
        LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
        if (zoneExists && wardExists) {
            List<Boundary> wardNewList = new ArrayList<Boundary>();
            wardNewList = getPersistenceService()
                    .findAllBy(
                            "from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
                            WARD_BNDRY_TYPE, getZoneId());
            addDropdownData("wardList", wardNewList);
        } else {
            addDropdownData("Wards", Collections.EMPTY_LIST);
        }
        LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    @SkipValidation
    public String newForm() {
        return RESULT_NEW;
    }

    @ValidationErrorPage(value = "new")
    public String report() {
        return super.report();
    }

    @Override
    public String criteria() {
        return null;
    }

    @Override
    protected String getReportTemplateName() {
        return REPORT_TEMPLATENAME_BAKAYAFERIST;
    }

    public void validateReport() {
        LOGGER.debug("Entered into validateReport method");

        if (getZoneId() == null || getZoneId() == -1) {
            addActionError(getText("mandatory.zone"));
        }

        if (getWardId() == null || getWardId() == -1) {
            addActionError(getText("mandatory.ward"));
        }

        LOGGER.debug("Exiting from validateReport method");
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public Map<Long, String> getZoneBndryMap() {
        return ZoneBndryMap;
    }

    public void setZoneBndryMap(Map<Long, String> zoneBndryMap) {
        ZoneBndryMap = zoneBndryMap;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

}
