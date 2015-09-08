/*******************************************************************************
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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.search;

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.web.struts.actions.BaseFormAction.NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.ADMIN_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
@Results({ @Result(name = NEW, location = "searchProperty-new.jsp"),
    @Result(name = SearchPropertyAction.TARGET, location = "searchProperty-result.jsp") })
public class SearchPropertyAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 6978874588028662454L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    public static final String TARGET = "result";
    private Long zoneId;
    private Long wardId;
    private Integer locationId;
    private Integer areaName;
    private String assessmentNum;
    private String houseNumBndry;
    private String ownerNameBndry;
    private String houseNumArea;
    private String ownerName;
    private String oldHouseNum;
    private String mode;
    private List<Map<String, String>> searchResultList;
    private String searchUri;
    private String searchCreteria;
    private String searchValue;
    List<Map<String, String>> searchList = new ArrayList<Map<String, String>>();
    private String roleName;
    private Long propertyTypeMasterId;
    private String markedForDeactive = "N";
    private Map<Long, String> ZoneBndryMap;
    private boolean isDemandActive;
    private String fromDemand;
    private String toDemand;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private PropertyService propertyService;

    @Override
    public Object getModel() {
        return null;
    }

    /**
     * @return - Gets forwared to Search Property Screen for officials
     */
    @SkipValidation
    @Action(value = "/search/searchProperty-searchForm")
    public String searchForm() {
        return NEW;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on assessment no
     */
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByAssessment")
    public String srchByAssessment() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into srchByAssessment  method. Assessment Number : " + assessmentNum);
        try {
            final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByIndexNumAndParcelID(assessmentNum, null);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("srchByAssessment : BasicProperty : " + basicProperty);
            if (basicProperty != null) {
                setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
                checkIsMarkForDeactive(basicProperty);
            }
            if (assessmentNum != null && !assessmentNum.equals(""))
                setSearchValue("Assessment Number : " + assessmentNum);
            setSearchUri("../search/searchProperty-srchByAssessment.action");
            setSearchCreteria("Search By Assessment number");
            setSearchValue("Assessment number :" + assessmentNum);
        } catch (final IndexOutOfBoundsException iob) {
            final String msg = "Rollover is not done for " + assessmentNum;
            throw new ValidationException(Arrays.asList(new ValidationError(msg, msg)));
        } catch (final Exception e) {
            LOGGER.error("Exception in Search Property By Assessment ", e);
            throw new EGOVRuntimeException("Exception : ", e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByAssessment method ");
        return TARGET;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on Boundary : zone and ward
     */
    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByBndry")
    public String srchByBndry() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into srchByBndry method");
            LOGGER.debug("srchByBndry : Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "House Num : "
                    + houseNumBndry + ", " + "Owner Name : " + ownerNameBndry);
        }
        final String strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        final String strWardNum = boundaryService.getBoundaryById(wardId).getName();

        if (zoneId != null && zoneId != -1 && wardId != null && wardId != -1)
            try {

                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByBoundary(zoneId, wardId,
                        ownerNameBndry, houseNumBndry);

                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByBndry : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                setSearchUri("../search/searchProperty-srchByBndry.action");
                setSearchCreteria("Search By Zone, Ward, Plot No/House No, Owner Name");
                setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Plot No/House No: "
                        + houseNumBndry + ", Owner Name: " + ownerNameBndry);
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Bndry ", e);
                throw new EGOVRuntimeException("Exception : " + e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByBndry method");
        return TARGET;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on location boundary
     */
    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByLocation")
    public String srchByLocation() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into srchByArea  method");
            LOGGER.debug("srchByLocation : Location Id : " + locationId + ", " + "Owner Name : " + ownerName + ", "
                    + "Plot No/House No : " + houseNumArea);
        }
        final String strLocationNum = boundaryService.getBoundaryById(locationId.longValue()).getName();
        if (null != ownerName && org.apache.commons.lang.StringUtils.isNotEmpty(ownerName)
                && locationId != null && locationId != -1)
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByLocation(locationId, houseNumArea,
                        ownerName);

                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByLocation : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                setSearchUri("../search/searchProperty-srchByLocation.action");
                setSearchCreteria("Search By Location, Owner Name");
                setSearchValue("Location : " + strLocationNum + ", Owner Name : " + ownerName);
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Location ", e);
                throw new EGOVRuntimeException("Exception : " + e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByArea  method");
        return TARGET;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on Demand
     */
    @SuppressWarnings("unchecked")
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-searchByDemand")
    public String searchByDemand() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into searchByDemand  method");
            LOGGER.debug("From Demand No : " + fromDemand + ", " + "To Demand No : " + toDemand);
        }
        if (fromDemand != null && fromDemand != "" && toDemand != null && toDemand != "")
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByDemand(fromDemand, toDemand);

                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("searchByDemand : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                setSearchUri("../search/searchProperty-searchByDemand.action");
                setSearchCreteria("Search By FromDemand, ToDemand");
                setSearchValue("From Demand: " + fromDemand + ", To Demand: " + toDemand);
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Demand ", e);
                throw new EGOVRuntimeException("Exception : " + e);
            }
        return TARGET;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void prepare() {
        final List<Boundary> zoneList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone", ADMIN_HIERARCHY_TYPE);
        final List<Boundary> locationList = boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Locality", LOCATION_HIERARCHY_TYPE);

        setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
        prepareWardDropDownData(zoneId != null, wardId != null);
        addDropdownData("Location", locationList);
        addDropdownData("PropTypeMaster",
                getPersistenceService().findAllByNamedQuery(PropertyTaxConstants.GET_PROPERTY_TYPES));
        final Long userId = (Long) session().get(SESSIONLOGINID);
        if (userId != null)
            setRoleName(propertyTaxUtil.getRolesForUserId(userId));
    }

    /**
     * @Description Loads ward dropdown for selected zone
     * @param zoneExists
     * @param wardExists
     */
    @SuppressWarnings("unchecked")
    @SkipValidation
    private void prepareWardDropDownData(final boolean zoneExists, final boolean wardExists) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into prepareWardDropDownData method");
            LOGGER.debug("Zone exists ? : " + zoneExists + ", " + "Ward exists ? : " + wardExists);
        }
        if (zoneExists && wardExists) {
            List<Boundary> wardNewList = new ArrayList<Boundary>();
            wardNewList = boundaryService.getActiveChildBoundariesByBoundaryId(getZoneId());
            addDropdownData("wardList", wardNewList);
        } else
            addDropdownData("wardList", Collections.EMPTY_LIST);
    }

    @Override
    public void validate() { 
        if (org.apache.commons.lang.StringUtils.equals(mode, "assessment")) {
            if (org.apache.commons.lang.StringUtils.isEmpty(assessmentNum) || org.apache.commons.lang.StringUtils
                    .isBlank(assessmentNum))
                addActionError(getText("mandatory.assessmentNo"));
        } else if (org.apache.commons.lang.StringUtils.equals(mode, "bndry")) {
            if (zoneId == null || zoneId == -1) 
                addActionError(getText("mandatory.zone"));
            if (wardId == null || wardId == -1)  
                addActionError(getText("mandatory.ward"));
        } else if (org.apache.commons.lang.StringUtils.equals(mode, "location")) {
            if (locationId == null || locationId == -1)
                addActionError(getText("mandatory.location"));
            if (ownerName == null || org.apache.commons.lang.StringUtils.isEmpty(ownerName))
                addActionError(getText("search.ownerName.null"));
        } else if (org.apache.commons.lang.StringUtils.equals(mode, "demand")) {
            if (fromDemand == null || org.apache.commons.lang.StringUtils.isEmpty(fromDemand))
                addActionError(getText("mandatory.fromdemand"));
            if (toDemand == null || org.apache.commons.lang.StringUtils.isEmpty(toDemand))
                addActionError(getText("mandatory.todemand"));
        }
    }

    /**
     * @param assessmentNumber
     * @return
     */
    private List<Map<String, String>> getSearchResults(final String assessmentNumber) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into getSearchResults method");
            LOGGER.debug("Assessment Number : " + assessmentNumber);
        }
        if (assessmentNumber != null || org.apache.commons.lang.StringUtils.isNotEmpty(assessmentNumber)) {

            final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNumber);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("BasicProperty : " + basicProperty);
            if (basicProperty != null) {
                final Property property = basicProperty.getProperty();
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Property : " + property);

                checkIsDemandActive(property);

                final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);

                final Map<String, String> searchResultMap = new HashMap<String, String>();
                searchResultMap.put("assessmentNum", assessmentNumber);
                searchResultMap.put("ownerName", basicProperty.getFullOwnerName());
                searchResultMap.put("address", basicProperty.getAddress().toString());
                searchResultMap.put("source", basicProperty.getSource().toString());

                if (!property.getIsExemptedFromTax()) {
                    searchResultMap.put("currDemand", demandCollMap.get(CURR_DMD_STR).toString());
                    searchResultMap.put("arrDemandDue",
                            demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)).toString());
                    searchResultMap.put("currDemandDue",
                            demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR)).toString());
                } else {
                    searchResultMap.put("currDemand", "0");
                    searchResultMap.put("arrDemandDue", "0");
                    searchResultMap.put("currDemandDue", "0");
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Assessment Number : " + searchResultMap.get("assessmentNum") + ", " + "Owner Name : "
                            + searchResultMap.get("ownerName") + ", " + "Parcel id : " + searchResultMap.get("parcelId")
                            + ", " + "Address : " + searchResultMap.get("address") + ", " + "Current Demand : "
                            + searchResultMap.get("currDemand") + ", " + "Arrears Demand Due : "
                            + searchResultMap.get("arrDemandDue") + ", " + "Current Demand Due : "
                            + searchResultMap.get("currDemandDue"));
                searchList.add(searchResultMap);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
            LOGGER.debug("Exit from getSearchResults method");
        }
        return searchList;
    }

    /**
     * @param basicProperty
     */
    private void checkIsMarkForDeactive(final BasicProperty basicProperty) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into checkIsMarkForDeactive method");
            LOGGER.debug("BasicProperty : " + basicProperty);
        }
        Set<PropertyStatusValues> propStatusValSet = new HashSet<PropertyStatusValues>();
        propStatusValSet = basicProperty.getPropertyStatusValuesSet();
        for (final PropertyStatusValues propStatusVal : propStatusValSet) {
            if (propStatusVal.getPropertyStatus().getStatusCode().equals(PROPERTY_STATUS_MARK_DEACTIVE))
                markedForDeactive = "Y";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Property Status Values : " + propStatusVal);
                LOGGER.debug("Marked for Deactivation ? : " + markedForDeactive);
            }
        }
    }

    /**
     * @param property
     */
    private void checkIsDemandActive(final Property property) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into checkIsDemandActive");
        if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE))
            isDemandActive = false;
        else
            isDemandActive = true;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("checkIsDemandActive - Is demand active? : " + isDemandActive);
            LOGGER.debug("Exiting from checkIsDemandActive");
        }
    }

    /**
     * @param pmv
     * @return
     */
    private List<Map<String, String>> getResultsFromMv(final PropertyMaterlizeView pmv) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into getSearchResults method");
            LOGGER.debug("Assessment Number : " + pmv.getPropertyId());
        }
        if (pmv.getPropertyId() != null || org.apache.commons.lang.StringUtils.isNotEmpty(pmv.getPropertyId()))
            if (pmv != null) {
                final Map<String, String> searchResultMap = new HashMap<String, String>();
                searchResultMap.put("assessmentNum", pmv.getPropertyId());
                searchResultMap.put("ownerName", pmv.getOwnerName());
                searchResultMap.put("parcelId", pmv.getGisRefNo());
                searchResultMap.put("address", pmv.getPropertyAddress());
                searchResultMap.put("source", pmv.getSource().toString());
                if (pmv.getIsExempted()) {
                    searchResultMap.put("currDemand", "0");
                    searchResultMap.put("arrDemandDue", "0");
                    searchResultMap.put("currDemandDue", "0");
                } else {
                    searchResultMap.put("currDemand", pmv.getAggrCurrDmd().toString());
                    searchResultMap.put("currDemandDue", pmv.getAggrCurrDmd().subtract(pmv.getAggrCurrColl())
                            .toString());
                    searchResultMap.put("arrDemandDue", pmv.getAggrArrDmd().subtract(pmv.getAggrArrColl()).toString());
                }
                searchList.add(searchResultMap);
            }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
            LOGGER.debug("Exit from getSearchResults method");
        }
        return searchList;
    }

    public List<Map<String, String>> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(final List<Map<String, String>> searchResultList) {
        this.searchResultList = searchResultList;
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

    public String getOldHouseNum() {
        return oldHouseNum;
    }

    public void setOldHouseNum(final String oldHouseNum) {
        this.oldHouseNum = oldHouseNum;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getSearchUri() {
        return searchUri;
    }

    public void setSearchUri(final String searchUri) {
        this.searchUri = searchUri;
    }

    public String getSearchCreteria() {
        return searchCreteria;
    }

    public void setSearchCreteria(final String searchCreteria) {
        this.searchCreteria = searchCreteria;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(final String searchValue) {
        this.searchValue = searchValue;
    }

    public Integer getAreaName() {
        return areaName;
    }

    public void setAreaName(final Integer areaName) {
        this.areaName = areaName;
    }

    public String getHouseNumBndry() {
        return houseNumBndry;
    }

    public void setHouseNumBndry(final String houseNumBndry) {
        this.houseNumBndry = houseNumBndry;
    }

    public String getOwnerNameBndry() {
        return ownerNameBndry;
    }

    public void setOwnerNameBndry(final String ownerNameBndry) {
        this.ownerNameBndry = ownerNameBndry;
    }

    public String getHouseNumArea() {
        return houseNumArea;
    }

    public void setHouseNumArea(final String houseNumArea) {
        this.houseNumArea = houseNumArea;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }

    public Long getPropertyTypeMasterId() {
        return propertyTypeMasterId;
    }

    public void setPropertyTypeMasterId(final Long propertyTypeMasterId) {
        this.propertyTypeMasterId = propertyTypeMasterId;
    }

    public String getMarkedForDeactive() {
        return markedForDeactive;
    }

    public void setMarkedForDeactive(final String markedForDeactive) {
        this.markedForDeactive = markedForDeactive;
    }

    public Map<Long, String> getZoneBndryMap() {
        return ZoneBndryMap;
    }

    public void setZoneBndryMap(final Map<Long, String> zoneBndryMap) {
        ZoneBndryMap = zoneBndryMap;
    }

    public boolean getIsDemandActive() {
        return isDemandActive;
    }

    public void setIsDemandActive(final boolean isDemandActive) {
        this.isDemandActive = isDemandActive;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(final Integer locationId) {
        this.locationId = locationId;
    }

    public String getFromDemand() {
        return fromDemand;
    }

    public void setFromDemand(final String fromDemand) {
        this.fromDemand = fromDemand;
    }

    public String getToDemand() {
        return toDemand;
    }

    public void setToDemand(final String toDemand) {
        this.toDemand = toDemand;
    }

    public String getAssessmentNum() {
        return assessmentNum;
    }

    public void setAssessmentNum(final String assessmentNum) {
        this.assessmentNum = assessmentNum;
    }

}