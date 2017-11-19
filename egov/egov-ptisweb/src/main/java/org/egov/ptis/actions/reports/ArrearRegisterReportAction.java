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

import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.reporting.engine.ReportDataSourceType;
import org.egov.infra.web.struts.actions.ReportFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.bean.PropertyWiseArrearInfo;
import org.egov.ptis.bean.ReportInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.InstDmdCollMaterializeView;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.LOCALITY;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.REPORT_TEMPLATENAME_ARREARREGISTER;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = ArrearRegisterReportAction.INDEX, location = "arrearRegisterReport-index.jsp"),
    @Result(name = ArrearRegisterReportAction.GENERATE, location = "arrearRegisterReport-generate.jsp") })
public class ArrearRegisterReportAction extends ReportFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -242727189632318964L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    private Long zoneId;
    private Long wardId;
    private Long areaId;
    private Long localityId;
    public static final String GENERATE = "generate";
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    private List<PropertyWiseArrearInfo> propertyWiseInfoList;

    @Override
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare method");
        super.prepare();
        final List<Boundary> localityList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                LOCALITY, LOCATION_HIERARCHY_TYPE);
        addDropdownData("localityList", localityList);

        final List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
                REVENUE_HIERARCHY_TYPE);
        addDropdownData("Zone", zoneList);
        addDropdownData("wardList", wardList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
        //prepareWardDropDownData(zoneId != null, wardId != null);
        if (wardId == null || wardId.equals(-1))
            addDropdownData("blockList", Collections.emptyList());
        prepareBlockDropDownData(wardId != null, areaId != null);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepare method");
    }

    /**
     * Loads ward dropdown for selected zone
     * @param zoneExists
     * @param wardExists
     */
    @SuppressWarnings("unused")
    private void prepareWardDropDownData(final boolean zoneExists, final boolean wardExists) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into prepareWardDropDownData method");
            LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
        }
        if (zoneExists && wardExists) {
            List<Boundary> wardList;
            wardList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
            addDropdownData("wardList", wardList);
        } else
            addDropdownData("wardList", Collections.emptyList());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    /**
     * Loads block dropdown for selected ward
     * @param wardExists
     * @param blockExists
     */
    private void prepareBlockDropDownData(final boolean wardExists, final boolean blockExists) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into prepareBlockDropDownData method");
            LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Block Exists ? : " + blockExists);
        }
        if (wardExists && blockExists) {
            List<Boundary> blockList;
            blockList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
            addDropdownData("blockList", blockList);
        } else
            addDropdownData("blockList", Collections.emptyList());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    /**
     * @return
     */
    @SkipValidation
    @Action(value = "/reports/arrearRegisterReport-index")
    public String index() {
        return INDEX;
    }

    /**
     * Generates Arrear Register Report
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = INDEX)
    @Action(value = "/reports/arrearRegisterReport-generateArrearReport")
    public String generateArrearReport() {
        final ReportInfo reportInfo = new ReportInfo();
        propertyWiseInfoList = new ArrayList<>();
        String strZoneNum = null;
        String strWardNum = null;
        String strBlockNum = null;
        String strLocalityNum = null;
        String strMunicipal;
        String strDistrict;
        if ((localityId == null || localityId == -1) && zoneId != null && zoneId != -1)
            strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        else if (localityId != null && localityId != -1) {
            strLocalityNum = boundaryService.getBoundaryById(localityId).getName();
            if (zoneId != null && zoneId != -1)
                strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        }
        if (wardId != null && wardId != -1)
            strWardNum = boundaryService.getBoundaryById(wardId).getName();
        if (areaId != null && areaId != -1)
            strBlockNum = boundaryService.getBoundaryById(areaId).getName();
        strMunicipal=getSession().get("citymunicipalityname").toString();
        strDistrict=getSession().get("districtName").toString();
        strDistrict=strDistrict.substring(0,1)+strDistrict.substring(1, strDistrict.length()).toLowerCase();
        final List<PropertyMaterlizeView> propertyViewList = propertyTaxUtil.prepareQueryforArrearRegisterReport(zoneId, wardId,
                areaId, localityId);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("PropertyMaterlizeView List Size" + propertyViewList.size());

        for (final PropertyMaterlizeView propMatView : propertyViewList)
            // If there is only one Arrear Installment
            if (propMatView.getInstDmdColl().size() == 1) {
                final InstDmdCollMaterializeView currIDCMatView = propMatView.getInstDmdColl()
                        .iterator().next();
                final PropertyWiseArrearInfo propertyWiseInfo = preparePropertyWiseInfo(currIDCMatView);
                if (propertyWiseInfo != null)
                    propertyWiseInfoList.add(propertyWiseInfo);
            } else {
                // if there are more than one arrear Installments
                final List<InstDmdCollMaterializeView> idcList = new ArrayList<>(
                        propMatView.getInstDmdColl());
                final List unitList = new ArrayList();
                PropertyWiseArrearInfo propertyWiseInfoTotal = null;

                for (final InstDmdCollMaterializeView instlDmdColMatView : idcList) {
                    final PropertyWiseArrearInfo propertyWiseInfo = preparePropertyWiseInfo(instlDmdColMatView);
                    if (propertyWiseInfo != null) {
                        // initially the block is executed
                        if (unitList.isEmpty()) {
                            unitList.add(propertyWiseInfo.getArrearInstallmentDesc());
                            propertyWiseInfoTotal = propertyWiseInfo;
                        }
                        else if (unitList.contains(propertyWiseInfo.getArrearInstallmentDesc()))
                            propertyWiseInfoTotal = addPropertyWiseInfo(propertyWiseInfoTotal, propertyWiseInfo);
                        else if (!unitList.contains(propertyWiseInfo.getArrearInstallmentDesc())) {

                            propertyWiseInfoList.add(propertyWiseInfoTotal);
                            unitList.add(propertyWiseInfo.getArrearInstallmentDesc());
                            propertyWiseInfoTotal = propertyWiseInfo;
                            propertyWiseInfoTotal.setIndexNumber("");
                            propertyWiseInfoTotal.setOwnerName("");
                            propertyWiseInfoTotal.setHouseNo("");
                        }
                    } // end of if - null condition
                    else
                        propertyWiseInfoList.add(propertyWiseInfoTotal);
                }
            }
        reportInfo.setZoneNo(strZoneNum);
        reportInfo.setWardNo(strWardNum);
        reportInfo.setBlockNo(strBlockNum);
        reportInfo.setLocalityNo(strLocalityNum);
        reportInfo.setMunicipal(strMunicipal);
        reportInfo.setDistrict(strDistrict);
        reportInfo.setPropertyWiseArrearInfoList(propertyWiseInfoList);
        setDataSourceType(ReportDataSourceType.JAVABEAN);
        setReportData(reportInfo);
        super.report();
        return "generate";
    }

    /**
     * @param propertyWiseInfoTotal
     * @param propertyInfo
     * @return
     */
    private PropertyWiseArrearInfo addPropertyWiseInfo(final PropertyWiseArrearInfo propertyWiseInfoTotal,
            final PropertyWiseArrearInfo propertyInfo) {
        propertyWiseInfoTotal.setArrearLibraryCess(propertyWiseInfoTotal.getArrearLibraryCess().add(
                propertyInfo.getArrearLibraryCess()));
        propertyWiseInfoTotal.setArrearPropertyTax(propertyWiseInfoTotal.getArrearPropertyTax().add(
                propertyInfo.getArrearPropertyTax()));
        propertyWiseInfoTotal.setArrearVacantLandTax(propertyWiseInfoTotal.getArrearVacantLandTax().add(
                propertyInfo.getArrearVacantLandTax()));
        propertyWiseInfoTotal.setArrearPenalty(propertyWiseInfoTotal.getArrearPenalty().add(propertyInfo.getArrearPenalty()));
        propertyWiseInfoTotal.setArrearEducationCess(propertyWiseInfoTotal.getArrearEducationCess().add(propertyInfo.getArrearEducationCess()));
        propertyWiseInfoTotal.setTotalArrearTax(propertyWiseInfoTotal.getTotalArrearTax().add(propertyInfo.getTotalArrearTax()));
        return propertyWiseInfoTotal;
    }

    /**
     * @param currInstDmdColMatView
     * @param currInstallment
     * @return
     */
    private PropertyWiseArrearInfo preparePropertyWiseInfo(final InstDmdCollMaterializeView currInstDmdColMatView) {
        PropertyWiseArrearInfo propertyWiseInfo;
        propertyWiseInfo = preparePropInfo(currInstDmdColMatView.getPropMatView());
        final BigDecimal totalTax = currInstDmdColMatView.getLibCessTax()
                .add(currInstDmdColMatView.getGeneralTax().equals(BigDecimal.ZERO)?currInstDmdColMatView.getVacantLandTax():currInstDmdColMatView.getGeneralTax())
                .add(currInstDmdColMatView.getPenaltyFinesTax()).add(currInstDmdColMatView.getEduCessTax());

        propertyWiseInfo.setArrearInstallmentDesc(currInstDmdColMatView.getInstallment().getDescription());
        propertyWiseInfo.setArrearLibraryCess(currInstDmdColMatView.getLibCessTax());
        propertyWiseInfo.setArrearPropertyTax(currInstDmdColMatView.getGeneralTax());
        propertyWiseInfo.setArrearPenalty(currInstDmdColMatView.getPenaltyFinesTax());
        propertyWiseInfo.setArrearEducationCess(currInstDmdColMatView.getEduCessTax());
        propertyWiseInfo.setArrearVacantLandTax(currInstDmdColMatView.getVacantLandTax());
        /*
         * Total of Arrear Librarycess tax,general tax and penalty tax
         */
        propertyWiseInfo.setTotalArrearTax(totalTax);
        return propertyWiseInfo;
    }

    /**
     * @param propMatView
     * @return
     */
    private PropertyWiseArrearInfo preparePropInfo(final PropertyMaterlizeView propMatView) {
        final PropertyWiseArrearInfo propertyWiseInfo = new PropertyWiseArrearInfo();
        propertyWiseInfo.setBasicPropId(propMatView.getBasicPropertyID().longValue());
        propertyWiseInfo.setIndexNumber(propMatView.getPropertyId());
        propertyWiseInfo.setOwnerName(propMatView.getOwnerName());
        propertyWiseInfo.setHouseNo(propMatView.getHouseNo());
        return propertyWiseInfo;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getWardId() {
        return wardId;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    @Override
    public String criteria() {
        return null;
    }

    @Override
    protected String getReportTemplateName() {
        return REPORT_TEMPLATENAME_ARREARREGISTER;
    }

    public Long getLocalityId() {
        return localityId;
    }

    public void setLocalityId(final Long localityId) {
        this.localityId = localityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(final Long areaId) {
        this.areaId = areaId;
    }

    public List<PropertyWiseArrearInfo> getPropertyWiseInfoList() {
        return propertyWiseInfoList;
    }

    public void setPropertyWiseInfoList(final List<PropertyWiseArrearInfo> propertyWiseInfoList) {
        this.propertyWiseInfoList = propertyWiseInfoList;
    }

}
