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
package org.egov.tl.web.actions.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.pims.commons.Designation;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.FeeType;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.entity.PenaltyRates;
import org.egov.tl.service.FeeMatrixDetailService;
import org.egov.tl.service.PenaltyRatesService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Results({
        @Result(name = "ward", location = "commonAjax-ward.jsp"),
        @Result(name = "subcategory", location = "commonAjax-subcategory.jsp"),
        @Result(name = "feeType", location = "commonAjax-feeType.jsp"),
        @Result(name = "unitOfMeasurement", location = "commonAjax-unitOfMeasurement.jsp"),
        @Result(name = "designation", location = "commonAjax-designation.jsp"),
        @Result(name = "users", location = "commonAjax-users.jsp"),
        @Result(name = "deleteRow", location = "commonAjax-deleteRow.jsp"),
        @Result(name = "SUCCESS", type = "redirectAction", location = "CommonAjaxAction.action"),
        @Result(name = "AJAX_RESULT", type = "stream", location = "returnStream", params = { "contentType", "text/plain" })
})
public class CommonAjaxAction extends BaseFormAction {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CommonAjaxAction.class);
    public static final String LOCATIONS = "locations";
    public static final String STREETS = "streets";
    public static final String DIVISIONS = "divisions";
    public static final String AREAS = "areas";
    private static final String AREA = "area";
    private static final String LOCATION = "location";

    // these are Set by Ajax call
    private int divisionId;
    private int areaId;
    private int locationId;
    private int zoneId;
    private Long categoryId;
    private Long subCategoryId;
    private Long feeTypeId;
    private Long feeMatrixDetailId;
    private Long penaltyRateId;
    private List<Boundary> locationList = new LinkedList<Boundary>();
    private List<Boundary> areaList = new LinkedList<Boundary>();
    private List<Boundary> streetList = new LinkedList<Boundary>();
    private List<Boundary> divisionList = new LinkedList<Boundary>();
    private String returnStream = "";
    private List<Designation> designationList;
    private Integer departmentId;
    private Integer designationId;
    private List<User> allActiveUsersByGivenDesg;
    protected LicenseUtils licenseUtils;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private EisCommonService eisCommonService;
    @Autowired
    private FeeMatrixDetailService feeMatrixDetailService;
    @Autowired
    private PenaltyRatesService penaltyRatesService;
    private String rateType;
    private Long uomId;
    private String uomName;
    @Autowired
    private LicenseSubCategoryService licenseSubCategoryService;
    private List<LicenseSubCategory> subCategoryList;
    private List<FeeType> feeTypeList = new ArrayList<FeeType>();

    public InputStream getReturnStream() {
        final ByteArrayInputStream is = new ByteArrayInputStream(returnStream.getBytes());
        return is;
    }

    @Override
    public Object getModel() {
        return null;
    }

    public String populateLocations() {
        try {
            locationList = boundaryService.getChildBoundariesByBoundaryId(Long.valueOf(areaId));
            final StringBuilder result = new StringBuilder();
            for (final Boundary boundary : locationList)
                result.append("Text:").append(boundary.getName()).append("Value:").append(boundary.getId()).append("\n");
            returnStream = result.toString();
        } catch (final Exception e) {
            LOGGER.error("populateLocations() - Error while loading locations." + e.getMessage());
            addFieldError(CommonAjaxAction.LOCATION, "Unable to load location information");
            throw new ApplicationRuntimeException("Unable to load location information", e);
        }
        return "AJAX_RESULT";
    }

    /**
     * Populate streets.
     *
     * @return the string
     */
    public String populateStreets() {
        try {
            streetList = boundaryService.getChildBoundariesByBoundaryId(Long.valueOf(locationId));
        } catch (final Exception e) {
            LOGGER.error("populateStreets() - Error while loading streets.", e);
            addFieldError(CommonAjaxAction.LOCATION, "Unable to load street information");
            throw new ApplicationRuntimeException("Unable to load street information", e);
        }
        return CommonAjaxAction.STREETS;
    }

    /**
     * Populate wards.
     *
     * @return the string
     */
    @Action(value = "/commonAjax-populateDivisions")
    public String populateDivisions() {
        try {
            final Boundary boundary = boundaryService.getBoundaryById(Long.valueOf(zoneId));
            final String cityName = licenseUtils.getAllCity().get(0).getName();
            if (!boundary.getName().equals(cityName))
                divisionList = boundaryService.getChildBoundariesByBoundaryId(Long.valueOf(zoneId));
        } catch (final Exception e) {
            LOGGER.error("populateDivisions() - Error while loading divisions ." + e.getMessage());
            addFieldError(CommonAjaxAction.LOCATION, "Unable to load division information");
            throw new ApplicationRuntimeException("Unable to load division information", e);
        }
        return "ward";
    }

    @Action(value = "/commonAjax-ajaxPopulateDesignationsByDept")
    public String ajaxPopulateDesignationsByDept() {
        try {

            designationList = designationService.getAllDesignationByDepartment(Long.valueOf(departmentId),
                    new Date());
        } catch (final Exception e) {
            LOGGER.error("populateDesignationsByDept() - Error while loading divisions ." + e.getMessage());
            addFieldError(CommonAjaxAction.LOCATION, "Unable to load Designation information");
            throw new ApplicationRuntimeException("Unable to load Designation information", e);
        }
        return "designation";
    }

    @SuppressWarnings("unchecked")
    @Action(value = "/domain/commonAjax-ajaxPopulateUsersByDesignation")
    public String ajaxPopulateUsersByDesignation() {
        try {
            allActiveUsersByGivenDesg = eisCommonService.getAllActiveUsersByGivenDesig(Long.valueOf(designationId));
        } catch (final Exception e) {
            LOGGER.error("populateUsersByDept() - Error while loading divisions ." + e.getMessage());
            addFieldError(CommonAjaxAction.LOCATION, "Unable to load User information");
            throw new ApplicationRuntimeException("Unable to load User information", e);
        }
        return "users";
    }

    @Action(value = "/domain/commonAjax-ajaxPopulateSubCategory")
    public String ajaxPopulateSubCategory() {
        subCategoryList = licenseSubCategoryService.findAllSubCategoryByCategory(categoryId);
        return "subcategory";
    }

    @Action(value = "/domain/commonAjax-ajaxPopulateFeeType")
    public String ajaxPopulateFeeType() {
        final LicenseSubCategory subCategory = licenseSubCategoryService.findById(subCategoryId);
        if (subCategory != null)
            if (!subCategory.getLicenseSubCategoryDetails().isEmpty())
                for (final LicenseSubCategoryDetails scd : subCategory.getLicenseSubCategoryDetails())
                    feeTypeList.add(scd.getFeeType());
        return "feeType";
    }

    @Action(value = "/domain/commonAjax-ajaxPopulateUom")
    public String ajaxPopulateUom() {
        final LicenseSubCategory subCategory = licenseSubCategoryService.findById(subCategoryId);
        if (subCategory != null)
            if (!subCategory.getLicenseSubCategoryDetails().isEmpty())
                for (final LicenseSubCategoryDetails scd : subCategory.getLicenseSubCategoryDetails())
                    if (scd.getFeeType().getId() == feeTypeId) {
                        uomId = scd.getUom().getId();
                        uomName = scd.getUom().getName();
                        rateType = scd.getRateType().toString();
                    }
        return "unitOfMeasurement";
    }

    /**
     * @description delete feedetail
     * @return
     */
    @Action(value = "/domain/commonAjax-deleteFee")
    public String deleteFee() {
        final FeeMatrixDetail feeMatrixDetail = feeMatrixDetailService.findByFeeMatrixDetailId(feeMatrixDetailId);
        if (feeMatrixDetail != null)
            feeMatrixDetailService.delete(feeMatrixDetail);
        return "deleteFee";
    }

    @Action(value = "/domain/commonAjax-deleteRow")
    public String deleteRow() {
        final PenaltyRates penaltyRates = penaltyRatesService.findOne(Long.valueOf(penaltyRateId));
        if (penaltyRates != null)
            penaltyRatesService.delete(penaltyRates);
        return "deleteRow";
    }

    public List<User> getAllActiveUsersByGivenDesg() {
        return allActiveUsersByGivenDesg;
    }

    public void setAllActiveUsersByGivenDesg(final List<User> allActiveUsersByGivenDesg) {
        this.allActiveUsersByGivenDesg = allActiveUsersByGivenDesg;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(final int divisionId) {
        this.divisionId = divisionId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(final int areaId) {
        this.areaId = areaId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(final int locationId) {
        this.locationId = locationId;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(final int zoneId) {
        this.zoneId = zoneId;
    }

    public List<Boundary> getLocationList() {
        return locationList;
    }

    public void setLocationList(final List<Boundary> locationList) {
        this.locationList = locationList;
    }

    public List<Designation> getDesignationList() {
        return designationList;
    }

    public void setDesignationList(final List<Designation> designationList) {
        this.designationList = designationList;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<Boundary> getAreaList() {
        return areaList;
    }

    public void setAreaList(final List<Boundary> areaList) {
        this.areaList = areaList;
    }

    public List<Boundary> getStreetList() {
        return streetList;
    }

    public void setStreetList(final List<Boundary> streetList) {
        this.streetList = streetList;
    }

    public List<Boundary> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(final List<Boundary> divisionList) {
        this.divisionList = divisionList;
    }

    public static String getAREA() {
        return CommonAjaxAction.AREA;
    }

    public Integer getDesignationId() {
        return designationId;
    }

    public void setDesignationId(final Integer designationId) {
        this.designationId = designationId;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public BoundaryService getBoundaryService() {
        return boundaryService;
    }

    public void setBoundaryService(final BoundaryService boundaryService) {
        this.boundaryService = boundaryService;
    }

    public DesignationService getDesignationService() {
        return designationService;
    }

    public void setDesignationService(final DesignationService designationService) {
        this.designationService = designationService;
    }

    public EisCommonService getEisCommonService() {
        return eisCommonService;
    }

    public void setEisCommonService(final EisCommonService eisCommonService) {
        this.eisCommonService = eisCommonService;
    }

    public LicenseSubCategoryService getLicenseSubCategoryService() {
        return licenseSubCategoryService;
    }

    public void setLicenseSubCategoryService(
            final LicenseSubCategoryService licenseSubCategoryService) {
        this.licenseSubCategoryService = licenseSubCategoryService;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<LicenseSubCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(final List<LicenseSubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public Long getFeeMatrixDetailId() {
        return feeMatrixDetailId;
    }

    public void setFeeMatrixDetailId(final Long feeMatrixDetailId) {
        this.feeMatrixDetailId = feeMatrixDetailId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(final Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(final Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public List<FeeType> getFeeTypeList() {
        return feeTypeList;
    }

    public void setFeeTypeList(final List<FeeType> feeTypeList) {
        this.feeTypeList = feeTypeList;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(final String rateType) {
        this.rateType = rateType;
    }

    public Long getUomId() {
        return uomId;
    }

    public void setUomId(final Long uomId) {
        this.uomId = uomId;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(final String uomName) {
        this.uomName = uomName;
    }

    public Long getPenaltyRateId() {
        return penaltyRateId;
    }

    public void setPenaltyRateId(final Long penaltyRateId) {
        this.penaltyRateId = penaltyRateId;
    }

}