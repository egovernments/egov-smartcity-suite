package org.egov.ptis.actions.search;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

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
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.SearchPropertyHibernateDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class SearchPropertyAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Long zoneId;
	private Long wardId;
	private Integer areaId;
	private Integer areaName;
	private String indexNum;
	private String houseNumBndry;
	private String ownerNameBndry;
	private String houseNumArea;
	private String ownerName;
	private String gisId;
	private String oldHouseNum;
	private String mode;
	private List<Map<String, String>> searchResultList;
	private String searchUri;
	private String searchCreteria;
	private String searchValue;
	List<Map<String, String>> searchList = new ArrayList<Map<String, String>>();
	private String roleName;
	String target = "failure";
	private String objectionNumber;
	private Date objectionFromDate;
	private Date objectionToDate;
	private Long propertyTypeMasterId;
	private String markedForDeactive = "N";
	private Map<Long, String> ZoneBndryMap;
	private boolean isDemandActive;
	private BoundaryDAO boundaryDAO;

	@Override
	public Object getModel() {
		return null;
	}

	@SkipValidation
	public String searchForm() {
		return "new";
	}

	@ValidationErrorPage(value = "new")
	public String srchByIndex() {
		LOGGER.debug("Entered into srchByIndex  method");
		LOGGER.debug("Index Number : " + indexNum + ", " + " parcelId :" + gisId);
		try {
			BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByIndexNumAndParcelID(indexNum, gisId);
			LOGGER.debug("srchByIndex : BasicProperty : " + basicProperty);
			if (basicProperty != null) {
				setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
				checkIsMarkForDeactive(basicProperty);
			}
			if (indexNum != null && !indexNum.equals("") && (gisId != null && !gisId.equals(""))) {
				setSearchValue("Index Number : " + indexNum + "Parcel Id: " + gisId);
			} else if (indexNum != null && !indexNum.equals("")) {
				setSearchValue("Index Number : " + indexNum);
			} else if (gisId != null && !gisId.equals("")) {
				setSearchValue("Parcel Id: " + gisId);
			}
			setSearchUri("../search/searchProperty!srchByIndexForm.action");
			setSearchCreteria("Search By Index number");
			target = "result";
		} catch (IndexOutOfBoundsException iob) {
			String msg = "Rollover is not done for " + indexNum;
			throw new ValidationException(Arrays.asList(new ValidationError(msg , msg)));
		} catch (Exception e) {
			LOGGER.error("Exception in Search Property By Index ", e);
			throw new EGOVRuntimeException("Exception : " + e);
		} 
		LOGGER.debug("Exit from srchByIndex method ");
		return target;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = "new")
	public String srchByBndry() {
		LOGGER.debug("Entered into srchByBndry method");
		LOGGER.debug("srchByBndry : Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "House Num : "
				+ houseNumBndry + ", " + "Owner Name : " + ownerNameBndry);
		String strZoneNum = boundaryDAO.getBoundary(zoneId).getName();
		String strWardNum = boundaryDAO.getBoundary(wardId).getName();

		if ((zoneId != null && zoneId != -1) && (wardId != null && wardId != -1)) {

			try {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append("select pmv from PropertyMaterlizeView pmv, BasicPropertyImpl bp where pmv.basicPropertyID=bp.id ")
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
				setSearchUri("../search/searchProperty!srchByBndryForm.action");
				setSearchCreteria("Search By Zone, Ward, Plot No/House No");
				setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Plot No/House No: "
						+ houseNumBndry);
				target = "result";
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Bndry ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByBndry method");
		return target;
	}

	@SuppressWarnings("unchecked")
	@ValidationErrorPage(value = "new")
	public String srchByArea() {

		LOGGER.debug("Entered into srchByArea  method");
		LOGGER.debug("srchByArea : Area Id : " + areaId + ", " + "Owner Name : " + ownerName + ", "
				+ "Plot No/House No : " + houseNumArea + ", " + "Old No : " + oldHouseNum);
		BasicProperty basicProperty = null;
		if (null != ownerName && StringUtils.isNotEmpty(ownerName)) {
			SearchPropertyHibernateDAO srchPropHibDao = new SearchPropertyHibernateDAO();
			try {
				List<Property> propertyList = srchPropHibDao.getPropertyByBoundryAndOwnerNameAndHouseNo(areaId,
						ownerName, houseNumArea, oldHouseNum);
				for (Property property : propertyList) {
					LOGGER.debug("srchByArea : Property : " + property);
					basicProperty = property.getBasicProperty();
					LOGGER.debug("srchByArea : BasicProperty : " + basicProperty);
					setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
					checkIsMarkForDeactive(basicProperty);
				}
				// Boundary boundary = boundaryDAO.getBoundary(areaId);
				setSearchUri("../search/searchProperty!srchByArea.action");
				setSearchCreteria("Search By Owner Name");
				setSearchValue("Owner Name : " + ownerName);
				target = "result";
			} catch (PropertyNotFoundException e) {
				LOGGER.error("Exception in Search Property By Area ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Area ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByArea  method");
		return target;
	}

	@ValidationErrorPage("new")
	public String searchByObjection() {
		LOGGER.debug("Entered into searchByObjection  method");
		LOGGER.debug("PropertyTypeMaster Id : " + propertyTypeMasterId + ", " + "Objection Number : " + objectionNumber
				+ ", " + "Objection FromDate : " + objectionFromDate + ", " + "Objection ToDate : " + objectionToDate);
		SearchPropertyHibernateDAO srchPropHibDao = new SearchPropertyHibernateDAO();
		BasicProperty basicProperty = null;
		List<Property> propertyList = srchPropHibDao.getPropertyByObjectionDetails(propertyTypeMasterId,
				objectionNumber, objectionFromDate, objectionToDate);
		for (Property property : propertyList) {
			LOGGER.debug("Property : " + property);
			basicProperty = property.getBasicProperty();
			LOGGER.debug("Basic Property : " + basicProperty);
			setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
			// checkIsMarkForDeactive(basicProperty);
		}
		// setSearchUri("../search/searchProperty!srchByBndryForm.action");
		setSearchCreteria("Search By Objection");
		String resultsHeader = objectionNumber != null ? ("ObjectionNumber " + objectionNumber) : " ";
		resultsHeader = resultsHeader + objectionFromDate != null ? ("Objection From " + objectionFromDate) : " ";
		resultsHeader = resultsHeader + objectionToDate != null ? ("Objection To " + objectionToDate) : " ";
		setSearchValue(objectionNumber != null ? ("ObjectionNumber " + objectionNumber) : "");
		LOGGER.debug("Search Criteria : " + getSearchCreteria() + ", " + "Search Value : " + getSearchValue());
		LOGGER.debug("Results Header : " + resultsHeader);
		LOGGER.debug("Exit from searchByObjection method");
		return "result";
	}

	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
		List<Boundary> zoneList = getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.id", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);
		LOGGER.debug("Zone List : " + (zoneList != null ? zoneList : ZERO));
		List<Boundary> areaList = getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.isHistory='N' order by BI.name ",
				AREA_BNDRY_TYPE);
		LOGGER.debug("Area List : " + (areaList != null ? areaList : ZERO));
		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
		prepareWardDropDownData(zoneId != null, wardId != null);
		addDropdownData("Area", areaList);
		addDropdownData("PropTypeMaster",
				getPersistenceService().findAllByNamedQuery(PropertyTaxConstants.GET_PROPERTY_TYPES));
		Integer userId = (Integer) session().get(SESSIONLOGINID);
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
							"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.id ",
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
		if (StringUtils.equals(mode, "index")) {
			if ((StringUtils.isEmpty(indexNum) || StringUtils.isBlank(indexNum))
					&& (StringUtils.isEmpty(gisId) || StringUtils.isBlank(gisId))) {
				addActionError(getText("mandatory.indexorparcelid"));
			}
		} else if (StringUtils.equals(mode, "bndry")) {
			if (zoneId == null || zoneId == -1) {
				addActionError(getText("mandatory.zone"));
			}
			if (wardId == null || wardId == -1) {
				addActionError(getText("mandatory.ward"));
			}
			/*
			 * if (StringUtils.isEmpty(houseNumBndry) ||
			 * StringUtils.isBlank(houseNumBndry)) {
			 * addActionError(getText("mandatory.houseNo")); }
			 */

		} else if (StringUtils.equals(mode, "area")) {
			if (ownerName == null || StringUtils.isEmpty(ownerName))
				addActionError(getText("search.ownerName.null"));
		} else if (StringUtils.equals(mode, "objection")) {
			if ((objectionNumber == null || objectionNumber.trim().isEmpty())
					&& (objectionFromDate == null && objectionToDate == null))
				addActionError(getText("mandatory.objNum"));
		}
		LOGGER.debug("Exit from validate method");
	}

	private List<Map<String, String>> getSearchResults(String indexNumber) {
		LOGGER.debug("Entered into getSearchResults method");
		LOGGER.debug("Index Number : " + indexNumber);
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();
		BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();

		if (indexNumber != null || StringUtils.isNotEmpty(indexNumber)) {

			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
			LOGGER.debug("BasicProperty : " + basicProperty);
			if (basicProperty != null) {
				Property property = basicProperty.getProperty();
				LOGGER.debug("Property : " + property);
				
				checkIsDemandActive(property);
				
				Set<PropertyOwner> ownerSet = property.getPropertyOwnerSet();
				Map<String, BigDecimal> demandCollMap = ptDemandDao.getDemandCollMap(property);

				Map<String, String> searchResultMap = new HashMap<String, String>();
				searchResultMap.put("indexNum", indexNumber);
				searchResultMap.put("ownerName", ptisCachMgr.buildOwnerFullName(ownerSet));
				searchResultMap.put("parcelId",
						basicProperty.getGisReferenceNo() == null ? PropertyTaxConstants.STRING_NOT_AVAILABLE
								: basicProperty.getGisReferenceNo());
				searchResultMap.put("address", ptisCachMgr.buildAddressByImplemetation(basicProperty.getAddress()));
				searchResultMap.put("currDemand", demandCollMap.get(CURR_DMD_STR).toString());
				searchResultMap.put("arrDemandDue",
						(demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR))).toString());
				searchResultMap.put("currDemandDue",
						(demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap.get(CURR_COLL_STR))).toString());
				LOGGER.debug("Index Number : " + searchResultMap.get("indexNum") + ", " + "Owner Name : "
						+ searchResultMap.get("ownerName") + ", " + "Parcel id : " + searchResultMap.get("parcelId")
						+ ", " + "Address : " + searchResultMap.get("address") + ", " + "Current Demand : "
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

	private String getRolesForUserId(Integer userId) {
		LOGGER.debug("Entered into getRolesForUserId method");
		LOGGER.debug("User id : " + userId);
		//UserDAO userDao = new UserDAO();
		String roleName;
		List<String> roleNameList = new ArrayList<String>();
		//FIX ME
		//User user = userDao.getUserByID(userId);
		User user = null;
		for (Role role : user.getRoles()) {
			roleName = role.getName() != null ? role.getName() : "";
			roleNameList.add(roleName);
		}
		LOGGER.debug("Exit from method getRolesForUserId with return value : " + roleNameList.toString().toUpperCase());
		return roleNameList.toString().toUpperCase();
	}

	private void checkIsMarkForDeactive(BasicProperty basicProperty) {
		LOGGER.debug("Entered into checkIsMarkForDeactive method");
		LOGGER.debug("BasicProperty : " + basicProperty);
		Set<PropertyStatusValues> propStatusValSet = new HashSet<PropertyStatusValues>();
		propStatusValSet = basicProperty.getPropertyStatusValuesSet();
		for (PropertyStatusValues propStatusVal : propStatusValSet) {
			LOGGER.debug("Property Status Values : " + propStatusVal);
			if (propStatusVal.getPropertyStatus().getStatusCode().equals(PROPERTY_STATUS_MARK_DEACTIVE)) {
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
		LOGGER.debug("Index Number : " + pmv.getPropertyId());
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();

		if (pmv.getPropertyId() != null || StringUtils.isNotEmpty(pmv.getPropertyId())) {

			if (pmv != null) {
				Map<String, String> searchResultMap = new HashMap<String, String>();
				searchResultMap.put("indexNum", pmv.getPropertyId());
				searchResultMap.put("ownerName", pmv.getOwnerName());
				searchResultMap.put("parcelId", pmv.getGisRefNo());
				searchResultMap.put("address", pmv.getPropertyAddress());
				searchResultMap.put("currDemand", pmv.getAggrCurrDmd().toString());
				searchResultMap.put("currDemandDue", (pmv.getAggrCurrDmd().subtract(pmv.getAggrCurrColl())).toString());
				searchResultMap.put("arrDemandDue", (pmv.getAggrArrDmd().subtract(pmv.getAggrArrColl())).toString());
				searchList.add(searchResultMap);
			}
		}
		LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
		LOGGER.debug("Exit from getSearchResults method");
		return searchList;
	}

	public String getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(String indexNum) {
		this.indexNum = indexNum;
	}

	public String getGisId() {
		return gisId;
	}

	public void setGisId(String gisId) {
		this.gisId = gisId;
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

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
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

	public String getObjectionNumber() {
		return objectionNumber;
	}

	public void setObjectionNumber(String objectionNumber) {
		this.objectionNumber = objectionNumber;
	}

	public Date getObjectionFromDate() {
		return objectionFromDate;
	}

	public void setObjectionFromDate(Date objectionFromDate) {
		this.objectionFromDate = objectionFromDate;
	}

	public Date getObjectionToDate() {
		return objectionToDate;
	}

	public void setObjectionToDate(Date objectionToDate) {
		this.objectionToDate = objectionToDate;
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

	public BoundaryDAO getBoundaryDAO() {
		return boundaryDAO;
	}

	public void setBoundaryDAO(BoundaryDAO boundaryDAO) {
		this.boundaryDAO = boundaryDAO;
	}

}
