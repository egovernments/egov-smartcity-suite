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

package org.egov.ptis.actions.reports;

import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyMutationTransferee;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = TitleTransferRegisterAction.SEARCH, location = "titleTransferRegister-search.jsp") })
public class TitleTransferRegisterAction extends BaseFormAction {

    /**
     *
     */
    private static final long serialVersionUID = 1456869850164051736L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    public static final String SEARCH = "search";
    private Long zoneId;
    private Long wardId;
    private Long areaId;
    private String fromDate;
    private String toDate;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    public FinancialYearDAO financialYearDAO;
    @Autowired
    public PropertyTaxUtil propertyTaxUtil;
    private String finYearStartDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Object getModel() {

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into prepare method");
        super.prepare();
        final List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
                REVENUE_HIERARCHY_TYPE);
        addDropdownData("zoneList", zoneList);
        addDropdownData("wardList", wardList);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
        if (wardId == null || wardId.equals(-1))
            addDropdownData("blockList", Collections.EMPTY_LIST);
        prepareBlockDropDownData(wardId != null, areaId != null);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepare method");
        final CFinancialYear finyear = financialYearDAO.getFinancialYearByDate(new Date());
        if (finyear != null)
            finYearStartDate = sdf.format(finyear.getStartingDate());
    }

    /**
     * Load ward for selected zone
     * 
     * @param zoneExists
     * @param wardExists
     */
    @SuppressWarnings("unchecked")
    private void prepareWardDropDownData(final boolean zoneExists, final boolean wardExists) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into prepareWardDropDownData method");
            LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
        }
        if (zoneExists && wardExists) {
            List<Boundary> wardList = new ArrayList<Boundary>();
            wardList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
            addDropdownData("wardList", wardList);
        } else
            addDropdownData("wardList", Collections.EMPTY_LIST);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    /**
     * Load Block for selected ward
     * 
     * @param wardExists
     * @param blockExists
     */
    @SuppressWarnings("unchecked")
    private void prepareBlockDropDownData(final boolean wardExists, final boolean blockExists) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into prepareBlockDropDownData method");
            LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Block Exists ? : " + blockExists);
        }
        if (wardExists && blockExists) {
            List<Boundary> blockList = new ArrayList<Boundary>();
            blockList = boundaryService.getActiveChildBoundariesByBoundaryId(getWardId());
            addDropdownData("blockList", blockList);
        } else
            addDropdownData("blockList", Collections.EMPTY_LIST);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    /**
     * @return
     */
    @SkipValidation
    @Action(value = "/reports/titleTransferRegister-search")
    public String search() {
        fromDate = finYearStartDate;
        toDate = sdf.format(new Date());
        return SEARCH;
    }

    /**
     * Invoked by Title Transfer Register Report. Shows list of ownership
     * transfer happend for a property
     */
    @SuppressWarnings("unchecked")
    @Action(value = "/titleTransferRegister-getPropertyList")
    public void getPropertyList() throws IOException {
        List<TitleTransferReportResult> resultList = new ArrayList<TitleTransferReportResult>();
        String result = null;
        final Query query = propertyTaxUtil
                .prepareQueryforTitleTransferReport(zoneId, wardId, areaId, fromDate, toDate);
        resultList = prepareOutput(query.list());
        // for converting resultList to JSON objects.
        // Write back the JSON Response.
        result = new StringBuilder("{ \"data\":").append(toJSON(resultList, TitleTransferReportResult.class,
                TitleTransferReportHelperAdaptor.class)).append("}").toString();
        final HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(result, response.getWriter());
    }

    /**
     * @param propertyMutationList
     * @return
     */
    private List<TitleTransferReportResult> prepareOutput(final List<PropertyMutation> propertyMutationList) {
        final List<TitleTransferReportResult> ttrFinalList = new LinkedList<TitleTransferReportResult>();
        final CFinancialYear finyear = financialYearDAO.getFinancialYearByDate(new Date());
        final List basicPropList = new ArrayList();
        TitleTransferReportResult ttrInfoTotal = null;
        TitleTransferReportResult titleTransferReportInfo = new TitleTransferReportResult();

        for (final PropertyMutation propMutation : propertyMutationList)
            // initially the block is executed
            if (basicPropList.size() == 0) {
                titleTransferReportInfo = PreparePropertyWiseInfo(propMutation, finyear);
                basicPropList.add(propMutation.getBasicProperty().getId());
                ttrInfoTotal = titleTransferReportInfo;
                ttrFinalList.add(ttrInfoTotal);
            }
            // executed for duplic basic property in list basicPropList
            else if (basicPropList.contains(propMutation.getBasicProperty().getId())) {
                titleTransferReportInfo = addPropertyWiseInfo(propMutation, finyear);
                ttrInfoTotal = titleTransferReportInfo;
                ttrFinalList.add(ttrInfoTotal);
            }
            // executed while adding unique basic property in list basicPropList
            else if (!basicPropList.contains(propMutation.getBasicProperty().getId())) {
                titleTransferReportInfo = PreparePropertyWiseInfo(propMutation, finyear);
                basicPropList.add(propMutation.getBasicProperty().getId());
                ttrInfoTotal = titleTransferReportInfo;
                ttrFinalList.add(ttrInfoTotal);
            }
        return ttrFinalList;
    }

    /**
     * @param propertyMutation
     * @param finyear
     * @return
     */
    private TitleTransferReportResult PreparePropertyWiseInfo(final PropertyMutation propertyMutation,
            final CFinancialYear finyear) {
        final TitleTransferReportResult ttrObj = new TitleTransferReportResult();
        String ownerName = "";
        ttrObj.setAssessmentNo(propertyMutation.getBasicProperty().getUpicNo());
        if (propertyMutation.getTransfereeInfos() != null && propertyMutation.getTransfereeInfos().size() > 0) {
            String newOwnerName = "";
            for (final PropertyMutationTransferee usr : propertyMutation.getTransfereeInfos())
                newOwnerName = newOwnerName + usr.getTransferee().getName() + ",";
            ttrObj.setOwnerName(newOwnerName.substring(0, newOwnerName.length() - 1));
        }
        ttrObj.setDoorNo(propertyMutation.getBasicProperty().getAddress().getHouseNoBldgApt());
        ttrObj.setLocation(propertyMutation.getBasicProperty().getPropertyID().getLocality().getName());
        BigDecimal taxAmount = propertyTaxUtil.getPropertyTaxDetails(propertyMutation.getBasicProperty().getId(),
                finyear);
        if (null != taxAmount)
            ttrObj.setPropertyTax(taxAmount.toString());
        if (propertyMutation.getTransferorInfos() != null && propertyMutation.getTransferorInfos().size() > 0) {
            for (final User usr : propertyMutation.getTransferorInfos())
                ownerName = ownerName + usr.getName() + ",";
            ttrObj.setOldTitle(ownerName.substring(0, ownerName.length() - 1));
        }

        if (propertyMutation.getTransfereeInfos() != null && propertyMutation.getTransfereeInfos().size() > 0) {
            ownerName = "";
            for (final PropertyMutationTransferee usr : propertyMutation.getTransfereeInfos())
                ownerName = ownerName + usr.getTransferee().getName() + ",";
            ttrObj.setChangedTitle(ownerName.substring(0, ownerName.length() - 1));
        }
        ttrObj.setDateOfTransfer(sdf.format(propertyMutation.getLastModifiedDate()));
        ttrObj.setCommissionerOrder("APPROVED");
        ttrObj.setMutationFee(propertyMutation.getMutationFee());

        return ttrObj;
    }

    /**
     * @param propertyMutation
     * @param finyear
     * @return
     */
    private TitleTransferReportResult addPropertyWiseInfo(final PropertyMutation propertyMutation,
            final CFinancialYear finyear) {
        final TitleTransferReportResult ttrObj = new TitleTransferReportResult();
        String ownerName = "";
        ttrObj.setAssessmentNo("");
        ttrObj.setOwnerName("");
        ttrObj.setDoorNo("");
        ttrObj.setLocation("");
        ttrObj.setPropertyTax("");
        if (propertyMutation.getTransferorInfos() != null && propertyMutation.getTransferorInfos().size() > 0) {
            for (final User usr : propertyMutation.getTransferorInfos())
                ownerName = ownerName + usr.getName() + ",";
            ttrObj.setOldTitle(ownerName.substring(0, ownerName.length() - 1));
        }

        if (propertyMutation.getTransfereeInfos() != null && propertyMutation.getTransfereeInfos().size() > 0) {
            ownerName = "";
            for (final PropertyMutationTransferee usr : propertyMutation.getTransfereeInfos())
                ownerName = ownerName + usr.getTransferee().getName() + ",";
            ttrObj.setChangedTitle(ownerName.substring(0, ownerName.length() - 1));
        }

        ttrObj.setDateOfTransfer(sdf.format(propertyMutation.getLastModifiedDate()));
        ttrObj.setCommissionerOrder("APPROVED");
        ttrObj.setMutationFee(propertyMutation.getMutationFee());
        return ttrObj;
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

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(final Long areaId) {
        this.areaId = areaId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(final String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(final String toDate) {
        this.toDate = toDate;
    }

    public String getFinYearStartDate() {
        return finYearStartDate;
    }

    public void setFinYearStartDate(final String finYearStartDate) {
        this.finYearStartDate = finYearStartDate;
    }

}
