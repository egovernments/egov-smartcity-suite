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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
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
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD_BNDRY_TYPE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.SearchPropertyHibernateDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = NEW, location = "searchProperty-new.jsp") ,
        @Result(name = SearchPropertyAction.TARGET, location = "searchProperty-result.jsp") })
public class SearchPropertyAction extends BaseFormAction {
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
	private String fromdemand;
	private String todemand;

	@Autowired
	private BoundaryService boundaryService;

	@Autowired
	private UserService userService;

	@Autowired
	private BasicPropertyDAO basicPropertyDAO;

	@Autowired
	private PtDemandDao ptDemandDAO;

	@Override
	public Object getModel() {
		return null;
	}

	@SkipValidation 
	@Action(value = "/search/searchProperty-searchForm")
	public String searchForm() { 
		return NEW;
	}

	@ValidationErrorPage(value = "new") 
	@Action(value = "/search/searchProperty-srchByAssessment")
	public String srchByAssessment() {
		LOGGER.debug("Entered into srchByAssessment  method");
		LOGGER.debug("Assessment Number : " + assessmentNum );
		try {
			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByIndexNumAndParcelID(
			        assessmentNum, null);
			LOGGER.debug("srchByAssessment : BasicProperty : " + basicProperty);
			if (basicProperty != null) {
				setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
				checkIsMarkForDeactive(basicProperty);
			}
			if (assessmentNum != null && !assessmentNum.equals("")) {
				setSearchValue("Assessment Number : " + assessmentNum);
			} 
			setSearchUri("../search/searchProperty-srchByAssessment.action");
			setSearchCreteria("Search By Assessment number");
			setSearchValue("Assessment number :" + assessmentNum);
		} catch (IndexOutOfBoundsException iob) {
			String msg = "Rollover is not done for " + assessmentNum;
			throw new ValidationException(Arrays.asList(new ValidationError(msg, msg)));
		} catch (Exception e) {
			LOGGER.error("Exception in Search Property By Assessment ", e);
			throw new EGOVRuntimeException("Exception : ", e);
		}
		LOGGER.debug("Exit from srchByAssessment method ");
		return TARGET;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = "new")
	@Action(value = "/search/searchProperty-srchByBndry")
	public String srchByBndry() {
		LOGGER.debug("Entered into srchByBndry method");
		LOGGER.debug("srchByBndry : Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", "
				+ "House Num : " + houseNumBndry + ", " + "Owner Name : " + ownerNameBndry);
		String strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
		String strWardNum = boundaryService.getBoundaryById(wardId).getName();

		if ((zoneId != null && zoneId != -1) && (wardId != null && wardId != -1)) {

			try {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append(
						"select pmv from PropertyMaterlizeView pmv, BasicPropertyImpl bp where pmv.basicPropertyID=bp.id ")
						.append("and bp.active='Y' and pmv.zone.id=:ZoneID and pmv.ward.id=:WardID ");
				if (houseNumBndry != null && !houseNumBndry.trim().isEmpty()) {
					queryStr.append("and pmv.houseNo like :HouseNo ");
				}
				if (ownerNameBndry != null && !ownerNameBndry.trim().isEmpty()) {
					queryStr.append("and trim(pmv.ownerName) like :OwnerName");
				}
				Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
				query.setLong("ZoneID", zoneId);
				query.setLong("WardID", wardId);
				if (houseNumBndry != null && !houseNumBndry.trim().isEmpty()) {
					query.setString("HouseNo", houseNumBndry + "%");
				}
				if (ownerNameBndry != null && !ownerNameBndry.trim().isEmpty()) {
					query.setString("OwnerName", ownerNameBndry + "%");
				}
				List<PropertyMaterlizeView> propertyList = query.list();

				for (PropertyMaterlizeView propMatview : propertyList) {
					LOGGER.debug("srchByBndry : Property : " + propMatview);
					setSearchResultList(getResultsFromMv(propMatview));
				}
				setSearchUri("../search/searchProperty-srchByBndry.action");
				setSearchCreteria("Search By Zone, Ward, Plot No/House No, Owner Name");
				setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum
						+ ", Plot No/House No: " + houseNumBndry+", Owner Name: "+ownerNameBndry);
				// target = "result";
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Bndry ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByBndry method");
		return TARGET; 
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = "new")
	@Action(value = "/search/searchProperty-srchByLocation")
	public String srchByLocation() {  
	    
		LOGGER.debug("Entered into srchByArea  method");
		LOGGER.debug("srchByArea : Location Id : " + locationId + ", " + "Owner Name : " + ownerName + ", "
				+ "Plot No/House No : " + houseNumArea);
		String strLocationNum = boundaryService.getBoundaryById(locationId.longValue()).getName();
		BasicProperty basicProperty = null;
		if (null != ownerName && org.apache.commons.lang.StringUtils.isNotEmpty(ownerName) && 
		        (locationId != null && locationId != -1) ) {
		    try {
                        StringBuilder queryStr = new StringBuilder();
                        queryStr.append("select pmv from PropertyMaterlizeView pmv ")
                                        .append(" where pmv.locality.id=:locationId ");
                        if (houseNumArea != null && !houseNumArea.trim().isEmpty()) {
                                queryStr.append("and pmv.houseNo like :HouseNo ");
                        }
                        if (ownerName != null && !ownerName.trim().isEmpty()) {
                                queryStr.append("and trim(pmv.ownerName) like :OwnerName");
                        }
                        Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
                        query.setLong("locationId", locationId);
                        if (houseNumArea != null && !houseNumArea.trim().isEmpty()) {
                                query.setString("HouseNo", houseNumArea + "%");
                        }
                        if (ownerName != null && !ownerName.trim().isEmpty()) {
                                query.setString("OwnerName", ownerName + "%");
                        }
                        List<PropertyMaterlizeView> propertyList = query.list();

                        for (PropertyMaterlizeView propMatview : propertyList) {
                                LOGGER.debug("srchByBndry : Property : " + propMatview);
                                setSearchResultList(getResultsFromMv(propMatview));
                        }
                        setSearchUri("../search/searchProperty-srchByLocation.action");
                        setSearchCreteria("Search By Location, Owner Name");
                        setSearchValue("Location : "+strLocationNum+", Owner Name : " + ownerName);
                    } catch (Exception e) {
                            LOGGER.error("Exception in Search Property By Bndry ", e);
                            throw new EGOVRuntimeException("Exception : " + e);
                    }
		}
		LOGGER.debug("Exit from srchByArea  method");
		return TARGET;
	}
	
	
	@SuppressWarnings("unchecked")
        @ValidationErrorPage(value = "new")
        @Action(value = "/search/searchProperty-searchByDemand")
        public String searchByDemand() {  
	    LOGGER.debug("Entered into searchByDemand  method");
            LOGGER.debug("From Demand No : " + fromdemand + ", "
                            + "To Demand No : " + todemand);
            if ((fromdemand != null && fromdemand != "") && (todemand != null && todemand != "")) {

                try {
                        StringBuilder queryStr = new StringBuilder();
                        queryStr.append(
                                        "select pmv from PropertyMaterlizeView pmv where pmv.aggrCurrDmd is not null and pmv.aggrCurrDmd>=:fromDemand ")
                                        .append("and pmv.aggrCurrDmd<=:toDemand ");
                        Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
                        query.setBigDecimal("fromDemand", new BigDecimal(fromdemand));
                        query.setBigDecimal("toDemand", new BigDecimal(todemand));
                        List<PropertyMaterlizeView> propertyList = query.list();

                        for (PropertyMaterlizeView propMatview : propertyList) {
                                LOGGER.debug("searchByDemand : Property : " + propMatview);
                                setSearchResultList(getResultsFromMv(propMatview));
                        }
                        setSearchUri("../search/searchProperty-searchByDemand.action");
                        setSearchCreteria("Search By FromDemand, ToDemand");
                        setSearchValue("From Demand: " + fromdemand + ", To Demand: " + todemand);
                } catch (Exception e) {
                        LOGGER.error("Exception in Search Property By Bndry ", e);
                        throw new EGOVRuntimeException("Exception : " + e);
                }
        }
	    return TARGET;
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
		List<Boundary> zoneList = getPersistenceService().findAllBy(
				"from Boundary BI where BI.boundaryType.name=? and BI.boundaryType.hierarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.id", "Zone",
				 ADMIN_HIERARCHY_TYPE);
		LOGGER.debug("Zone List : " + (zoneList != null ? zoneList : ZERO));
		//List<Boundary> streetList = new ArrayList<Boundary>(0);
		List<Boundary> locationList = getPersistenceService()
				.findAllBy("from Boundary BI where BI.boundaryType.name=? and BI.isHistory='N' order by BI.name ",
						"Locality");
		LOGGER.debug("Location List : " + (locationList != null ? locationList : ZERO));
		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
		prepareWardDropDownData(zoneId != null, wardId != null);
		addDropdownData("Location", locationList);
		addDropdownData("PropTypeMaster",
				getPersistenceService().findAllByNamedQuery(PropertyTaxConstants.GET_PROPERTY_TYPES));
		Long userId = (Long) session().get(SESSIONLOGINID);
		if (mode != null && userId != null) {
			setRoleName(getRolesForUserId(userId));
		}
		LOGGER.debug("Exit from prepare method");
	}

	@SuppressWarnings("unchecked")
	@SkipValidation
	private void prepareWardDropDownData(boolean zoneExists, boolean wardExists) {
		LOGGER.debug("Entered into prepareWardDropDownData method");
		LOGGER.debug("Zone exists ? : " + zoneExists + ", " + "Ward exists ? : " + wardExists);
		if (zoneExists && wardExists) {
			List<Boundary> wardNewList = new ArrayList<Boundary>();
			wardNewList = getPersistenceService()
					.findAllBy(
							"from Boundary BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.id ",
							WARD_BNDRY_TYPE, getZoneId());
			addDropdownData("wardList", wardNewList);
		} else {
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}

	@Override
	public void validate() {
		LOGGER.debug("Entered into validate method");
		if (org.apache.commons.lang.StringUtils.equals(mode, "assessment")) {
			if ((org.apache.commons.lang.StringUtils.isEmpty(assessmentNum) || org.apache.commons.lang.StringUtils
					.isBlank(assessmentNum))) {
				addActionError(getText("mandatory.assessmentNo"));
			}
		} else if (org.apache.commons.lang.StringUtils.equals(mode, "bndry")) {
			if (zoneId == null || zoneId == -1) {
				addActionError(getText("mandatory.zone"));
			}
			if (wardId == null || wardId == -1) {
				addActionError(getText("mandatory.ward"));
			}
		} else if (org.apache.commons.lang.StringUtils.equals(mode, "location")) {
		        if (locationId == null || locationId == -1)
                                 addActionError(getText("mandatory.location"));
			if (ownerName == null || org.apache.commons.lang.StringUtils.isEmpty(ownerName))
				addActionError(getText("search.ownerName.null"));
		}  else if (org.apache.commons.lang.StringUtils.equals(mode, "demand")) {
                        if (fromdemand == null || org.apache.commons.lang.StringUtils.isEmpty(fromdemand))
                            addActionError(getText("mandatory.fromdemand"));
                       if (todemand == null || org.apache.commons.lang.StringUtils.isEmpty(todemand))
                               addActionError(getText("mandatory.todemand"));
               } 
		LOGGER.debug("Exit from validate method");
	}

	private List<Map<String, String>> getSearchResults(String assessmentNumber) {
		LOGGER.debug("Entered into getSearchResults method");
		LOGGER.debug("Assessment Number : " + assessmentNumber);
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();
		if (assessmentNumber != null || org.apache.commons.lang.StringUtils.isNotEmpty(assessmentNumber)) {

			BasicProperty basicProperty = basicPropertyDAO
					.getBasicPropertyByPropertyID(assessmentNumber);
			LOGGER.debug("BasicProperty : " + basicProperty);
			if (basicProperty != null) {
				Property property = basicProperty.getProperty();
				LOGGER.debug("Property : " + property);

				checkIsDemandActive(property);

				Set<PropertyOwner> ownerSet = property.getPropertyOwnerSet();
				Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMap(property);

				Map<String, String> searchResultMap = new HashMap<String, String>();
				searchResultMap.put("assessmentNum", assessmentNumber);
				searchResultMap.put("ownerName", ptisCachMgr.buildOwnerFullName(ownerSet));
				searchResultMap
						.put("parcelId",
								basicProperty.getGisReferenceNo() == null ? PropertyTaxConstants.STRING_NOT_AVAILABLE
										: basicProperty.getGisReferenceNo());
				searchResultMap.put("address",
						ptisCachMgr.buildAddressByImplemetation(basicProperty.getAddress()));
				searchResultMap.put("currDemand", demandCollMap.get(CURR_DMD_STR).toString());
				searchResultMap.put("arrDemandDue", (demandCollMap.get(ARR_DMD_STR)
						.subtract(demandCollMap.get(ARR_COLL_STR))).toString());
				searchResultMap
						.put("currDemandDue", (demandCollMap.get(CURR_DMD_STR)
								.subtract(demandCollMap.get(CURR_COLL_STR))).toString());
				LOGGER.debug("Assessment Number : " + searchResultMap.get("assessmentNum") + ", "
						+ "Owner Name : " + searchResultMap.get("ownerName") + ", "
						+ "Parcel id : " + searchResultMap.get("parcelId") + ", " + "Address : "
						+ searchResultMap.get("address") + ", " + "Current Demand : "
						+ searchResultMap.get("currDemand") + ", " + "Arrears Demand Due : "
						+ searchResultMap.get("arrDemandDue") + ", " + "Current Demand Due : "
						+ searchResultMap.get("currDemandDue"));
				searchList.add(searchResultMap);
			}
		}
		LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
		LOGGER.debug("Exit from getSearchResults method");
		return searchList;
	}

	private String getRolesForUserId(Long userId) {
		LOGGER.debug("Entered into getRolesForUserId method");
		LOGGER.debug("User id : " + userId);
		String roleName;
		List<String> roleNameList = new ArrayList<String>();
		User user = userService.getUserById(userId);
		for (Role role : user.getRoles()) {
			roleName = role.getName() != null ? role.getName() : "";
			roleNameList.add(roleName);
		}
		LOGGER.debug("Exit from method getRolesForUserId with return value : "
				+ roleNameList.toString().toUpperCase());
		return roleNameList.toString().toUpperCase();
	}

	private void checkIsMarkForDeactive(BasicProperty basicProperty) {
		LOGGER.debug("Entered into checkIsMarkForDeactive method");
		LOGGER.debug("BasicProperty : " + basicProperty);
		Set<PropertyStatusValues> propStatusValSet = new HashSet<PropertyStatusValues>();
		propStatusValSet = basicProperty.getPropertyStatusValuesSet();
		for (PropertyStatusValues propStatusVal : propStatusValSet) {
			LOGGER.debug("Property Status Values : " + propStatusVal);
			if (propStatusVal.getPropertyStatus().getStatusCode()
					.equals(PROPERTY_STATUS_MARK_DEACTIVE)) {
				markedForDeactive = "Y";
			}
			LOGGER.debug("Marked for Deactivation ? : " + markedForDeactive);
		}
		LOGGER.debug("Exit from checkIsMarkForDeactive method");
	}

	private void checkIsDemandActive(Property property) {
		LOGGER.debug("Entered into checkIsDemandActive");

		if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)) {
			isDemandActive = false;
		} else {
			isDemandActive = true;
		}

		LOGGER.debug("checkIsDemandActive - Is demand active? : " + isDemandActive);

		LOGGER.debug("Exiting from checkIsDemandActive");
	}

	private List<Map<String, String>> getResultsFromMv(PropertyMaterlizeView pmv) {
		LOGGER.debug("Entered into getSearchResults method");
		LOGGER.debug("Assessment Number : " + pmv.getPropertyId());
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();

		if (pmv.getPropertyId() != null
				|| org.apache.commons.lang.StringUtils.isNotEmpty(pmv.getPropertyId())) {

			if (pmv != null) {
				Map<String, String> searchResultMap = new HashMap<String, String>();
				searchResultMap.put("assessmentNum", pmv.getPropertyId());
				searchResultMap.put("ownerName", pmv.getOwnerName());
				searchResultMap.put("parcelId", pmv.getGisRefNo());
				searchResultMap.put("address", pmv.getPropertyAddress());
				searchResultMap.put("currDemand", pmv.getAggrCurrDmd().toString());
				searchResultMap.put("currDemandDue",
						(pmv.getAggrCurrDmd().subtract(pmv.getAggrCurrColl())).toString());
				searchResultMap.put("arrDemandDue",
						(pmv.getAggrArrDmd().subtract(pmv.getAggrArrColl())).toString());
				searchList.add(searchResultMap);
			}
		}
		LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
		LOGGER.debug("Exit from getSearchResults method");
		return searchList;
	}


	public List<Map<String, String>> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<Map<String, String>> searchResultList) {
		this.searchResultList = searchResultList;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getWardId() {
		return wardId;
	}

	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}
	public String getOldHouseNum() {
		return oldHouseNum;
	}

	public void setOldHouseNum(String oldHouseNum) {
		this.oldHouseNum = oldHouseNum;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSearchUri() {
		return searchUri;
	}

	public void setSearchUri(String searchUri) {
		this.searchUri = searchUri;
	}

	public String getSearchCreteria() {
		return searchCreteria;
	}

	public void setSearchCreteria(String searchCreteria) {
		this.searchCreteria = searchCreteria;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public Integer getAreaName() {
		return areaName;
	}

	public void setAreaName(Integer areaName) {
		this.areaName = areaName;
	}

	public String getHouseNumBndry() {
		return houseNumBndry;
	}

	public void setHouseNumBndry(String houseNumBndry) {
		this.houseNumBndry = houseNumBndry;
	}

	public String getOwnerNameBndry() {
		return ownerNameBndry;
	}

	public void setOwnerNameBndry(String ownerNameBndry) {
		this.ownerNameBndry = ownerNameBndry;
	}

	public String getHouseNumArea() {
		return houseNumArea;
	}

	public void setHouseNumArea(String houseNumArea) {
		this.houseNumArea = houseNumArea;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getPropertyTypeMasterId() {
		return propertyTypeMasterId;
	}

	public void setPropertyTypeMasterId(Long propertyTypeMasterId) {
		this.propertyTypeMasterId = propertyTypeMasterId;
	}

	public String getMarkedForDeactive() {
		return markedForDeactive;
	}

	public void setMarkedForDeactive(String markedForDeactive) {
		this.markedForDeactive = markedForDeactive;
	}

	public Map<Long, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Long, String> zoneBndryMap) {
		ZoneBndryMap = zoneBndryMap;
	}

	public boolean getIsDemandActive() {
		return isDemandActive;
	}

	public void setIsDemandActive(boolean isDemandActive) {
		this.isDemandActive = isDemandActive;
	}

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getFromdemand() {
        return fromdemand;
    }

    public void setFromdemand(String fromdemand) {
        this.fromdemand = fromdemand;
    }

    public String getTodemand() {
        return todemand;
    }

    public void setTodemand(String todemand) {
        this.todemand = todemand;
    }

    public String getAssessmentNum() {
        return assessmentNum;
    }

    public void setAssessmentNum(String assessmentNum) {
        this.assessmentNum = assessmentNum;
    }

}
