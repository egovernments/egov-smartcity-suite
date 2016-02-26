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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.exception.NoSuchObjectException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseSubCategory;
import org.egov.tl.entity.LicenseSubCategoryDetails;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.UnitOfMeasurement;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.masters.LicenseSubCategoryService;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Results({
    @Result(name = "AJAX_RESULT", type = "redirectAction", location = "returnStream", params = { "contentType", "text/plain" }),
    @Result(name = "ward", location = "commonAjax-ward.jsp"),
    @Result(name = "success", type = "redirectAction", location = "CommonTradeLicenseAjaxAction.action"),
    @Result(name = CommonTradeLicenseAjaxAction.SUBCATEGORY, location = "commonTradeLicenseAjax-subCategory.jsp"),
    @Result(name = "populateData", location = "commonTradeLicenseAjax-autoComplete.jsp")
})
@ParentPackage("egov")
public class CommonTradeLicenseAjaxAction extends BaseFormAction implements ServletResponseAware {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CommonTradeLicenseAjaxAction.class);
    protected LicenseUtils licenseUtils;
    private int zoneId;
    private List<Boundary> divisionList = new LinkedList<Boundary>();
    private Long categoryId;
    private List<LicenseSubCategory> subCategoryList = new LinkedList<LicenseSubCategory>();
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private TradeLicenseService tradeLicenseService;
    private LicenseSubCategoryService licenseSubCategoryService;
    public static final String SUBCATEGORY = "subCategory";
    private Long locality;
    private HttpServletResponse response;
    private Long subCategoryId; 
    private Long feeTypeId;
    private String searchParamValue;
    private String searchParamType;
    private List<TradeLicense> licenseList = new ArrayList<TradeLicense>();

    /**
     * Populate wards.
     *
     * @return the string
     */
    @Action(value = "/domain/commonTradeLicenseAjax-populateDivisions")
    public String populateDivisions() {
        try {
            final Boundary boundary = boundaryService.getBoundaryById(Long.valueOf(zoneId));
            final String cityName = licenseUtils.getAllCity().get(0).getName();
            if (!boundary.getName().equals(cityName))
                divisionList = boundaryService.getChildBoundariesByBoundaryId(Long.valueOf(zoneId));
        } catch (final Exception e) {
            LOGGER.error("populateDivisions() - Error while loading divisions ." + e.getMessage());
            addFieldError("divisions", "Unable to load division information");
            throw new ApplicationRuntimeException("Unable to load division information", e);
        }
        return "ward";
    }

    /**
     * @return list of subcategory for a given category
     */
    @Action(value = "/domain/commonTradeLicenseAjax-populateSubCategory")
    public String populateSubCategory() {
        try {
            if (categoryId != null)
                subCategoryList = licenseSubCategoryService.findAllSubCategoryByCategory(categoryId);
        } catch (final Exception e) {
            LOGGER.error("populateSubCategory() - Error while loading subCategory ." + e.getMessage());
            addFieldError("subCategory", "Unable to load Sub Category information");
            throw new ApplicationRuntimeException("Unable to load Sub Category information", e);
        }
        return SUBCATEGORY;
    }

    /**
     * @throws IOException
     * @throws NoSuchObjectException
     * @return zone and ward for a locality
     */
    @Action(value = "/domain/commonTradeLicenseAjax-blockByLocality")
    public void blockByLocality() throws IOException, NoSuchObjectException {
        LOGGER.debug("Entered into blockByLocality, locality: " + locality);

        final Boundary wardBoundary = (Boundary) getPersistenceService().find(
                "select CH.parent from CrossHierarchy CH where CH.child.id = ? and CH.parentType.hierarchyType.name= ? and CH.parentType.name=?", getLocality(),Constants.REVENUE_HIERARCHYTYPE,Constants.DIVISION);
        final Boundary zoneBoundary = wardBoundary.getParent();  

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("zoneName", zoneBoundary.getName());
        jsonObject.put("wardName", wardBoundary.getName());
        jsonObject.put("wardId", wardBoundary.getId());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE); 
        IOUtils.write(jsonObject.toString(), response.getWriter());
    }
    
    /**
     * @throws IOException
     * @throws NoSuchObjectException
     * @return uom for a subcategory
     */
    @Action(value="/domain/commonTradeLicenseAjax-ajaxLoadUomName")   
    public void ajaxLoadUomName() throws IOException, NoSuchObjectException { 
        LicenseSubCategory subCategory = licenseSubCategoryService.findById(subCategoryId);
        List<UnitOfMeasurement> uomList = new ArrayList<UnitOfMeasurement>();
        if(subCategory!=null){
            if(!subCategory.getLicenseSubCategoryDetails().isEmpty()){
                for(LicenseSubCategoryDetails scd : subCategory.getLicenseSubCategoryDetails()){
                    if(scd.getFeeType().getId()==feeTypeId){
                      uomList.add(scd.getUom());   
                    }
                }
            }
        }
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("uom", uomList.get(0).getName());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        IOUtils.write(jsonObject.toString(), response.getWriter());
    }
    
    @Action(value = "/domain/commonTradeLicenseAjax-populateData")
    public String populateData() {
        try {
            if (searchParamValue != null)
                licenseList = tradeLicenseService.getTradeLicenseForGivenParam(searchParamValue, searchParamType);
        } catch (final Exception e) {
            LOGGER.error("populateData() - Error while loading Data ." + e.getMessage());
            throw new ApplicationRuntimeException("Unable to load Data", e);
        }
        return "populateData";
    }


    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */

    public HttpServletResponse getServletResponse() {
        return ServletActionContext.getResponse();
    }

    public LicenseUtils getLicenseUtils() {
        return licenseUtils;
    }

    public void setLicenseUtils(final LicenseUtils licenseUtils) {
        this.licenseUtils = licenseUtils;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(final int zoneId) {
        this.zoneId = zoneId;
    }

    public List<Boundary> getDivisionList() {
        return divisionList;
    }

    public void setDivisionList(final List<Boundary> divisionList) {
        this.divisionList = divisionList;
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

    public Long getLocality() {
        return locality;
    }

    public void setLocality(final Long locality) {
        this.locality = locality;
    }

    @Override
    public void setServletResponse(final HttpServletResponse httpServletResponse) {
        response = httpServletResponse;
    }

    public void setLicenseSubCategoryService(LicenseSubCategoryService licenseSubCategoryService) {
        this.licenseSubCategoryService = licenseSubCategoryService;
    }

    public LicenseSubCategoryService getLicenseSubCategoryService() {
        return licenseSubCategoryService;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public List<TradeLicense> getLicenseList() {
        return licenseList;
    }

    public void setLicenseList(List<TradeLicense> licenseList) {
        this.licenseList = licenseList;
    }

    public String getSearchParamType() {
        return searchParamType;
    }

    public void setSearchParamType(String searchParamType) {
        this.searchParamType = searchParamType;
    }

    public String getSearchParamValue() {
        return searchParamValue;
    }

    public void setSearchParamValue(String searchParamValue) {
        this.searchParamValue = searchParamValue;
    }

}