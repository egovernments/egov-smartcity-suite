package org.egov.ptis.actions.search;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GISCITY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GISVERSION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ZONE_BNDRY_TYPE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.citizen.model.Owner;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.hibernate.Query;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
public class GisSearchPropertyAction extends BaseFormAction {
	private final Logger LOGGER = Logger.getLogger(getClass());
	private Integer zoneId;
	private Integer wardId;
	private Integer areaId;
	private String mode;
	private String houseNum;
	private String ownerName;
	private Integer propTypeId;
	private BigDecimal demandFromAmt;
	private BigDecimal demandToAmt;
	private BigDecimal defaulterFromAmt;
	private BigDecimal defaulterToAmt;
	private List<Map<String, String>> searchResultList;
	private String searchUri;
	private String searchCreteria;
	private String searchValue;
	List<Map<String, String>> searchList = new ArrayList<Map<String, String>>();
	private String SESSION;
	private String searchResultString;
	private String gisVersion;
	private String gisCity;
	private Map<Integer, String> ZoneBndryMap;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@SkipValidation
	public void gisFormRedirect() {
		LOGGER.debug("Entered into gisFormRedirect method : GISVERSION : " + GISVERSION + " GISCITY : " + GISCITY);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.sendRedirect(response.encodeRedirectURL(GISVERSION + GISCITY
					+ "/ajaxtiledviewersample.jsp?DomainName=" + GISCITY + "&mode=PTIS"));
		} catch (IOException e) {
			LOGGER.error("Exception in Gis Search Property : ", e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
		LOGGER.debug("Exit from gisFormRedirect method");
	}

	@SkipValidation
	public String gisSearchForm() {
		LOGGER.debug("Entered into gisSearchForm method");
		String target = null;
		setSESSION(getSESSION());
		setGisCity(GISCITY);
		setGisVersion(GISVERSION);
		target = "bndry";
		if (StringUtils.equals(mode, "bndry")) {
			target = "bndry";
		} else if (StringUtils.equals(mode, "propType")) {
			target = "propType";
		} else if (StringUtils.equals(mode, "demand")) {
			target = "demand";
		} else if (StringUtils.equals(mode, "defaulter")) {
			target = "defaulter";
		}
		LOGGER.debug("Exit from gisSearchForm method");
		return target;

	}

	@ValidationErrorPage(value = "bndry")
	public String srchByBndry() {
		LOGGER.debug("Entered into srchByBndry method");
		LOGGER.debug("srchByBndry : Zone Id : " + zoneId + ", " + "ward Id: " + wardId + ", " + "House Num : "
				+ houseNum + ", " + "Owner Name :" + ownerName + ", " + "Session : " + SESSION);
		BoundaryDAO boundaryDAO = new BoundaryDAO();
		String strZoneNum = boundaryDAO.getBoundary(zoneId).getName();
		String strWardNum = "";
		String target = null;
		if (zoneId != null && zoneId != -1) {
			try {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append("from PropertyMaterlizeView pmv where pmv.zoneID=:ZoneID");
				if (wardId != null && wardId != -1) {
					queryStr.append(" and pmv.wardID=:WardID");
				}
				if (areaId != null && areaId != -1) {
					queryStr.append(" and pmv.streetID=:AreaID");
				}

				if (houseNum != null && !houseNum.trim().isEmpty()) {
					queryStr.append(" and pmv.houseNo like :houseNum");
				}
				if (ownerName != null && !ownerName.trim().isEmpty()) {
					queryStr.append(" and trim(pmv.ownerName) like :OwnerName");
				}
				Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
				if (zoneId != null && zoneId != -1) {
					query.setInteger("ZoneID", zoneId);
				}
				if (wardId != null && wardId != -1) {
					query.setInteger("WardID", wardId);
				}
				if (areaId != null && areaId != -1) {
					query.setInteger("AreaID", areaId);
				}
				if (houseNum != null && !houseNum.trim().isEmpty()) {
					query.setString("houseNum", houseNum + "%");
				}
				if (ownerName != null && !ownerName.trim().isEmpty()) {
					query.setString("OwnerName", ownerName + "%");
				}
				List<PropertyMaterlizeView> propertyList = query.list();
				if (propertyList.size() < 0) {
					setSESSION(getSESSION());
					setMode("bndry");
					target = "nodtls";
				}
				int count = 0;
				for (PropertyMaterlizeView propMatview : propertyList) {
					LOGGER.debug("srchByBndry : Property : " + propMatview);
					if (count <= 10) {
						setSearchResultList(getResultsFromMv(propMatview));
					} else {
						break;
					}
					count++;
				}
				if (searchResultList != null) {
					setSearchResultString(getSearchResultsString(searchResultList));
				}
				setSearchUri("../search/searchProperty!srchByBndryForm.action");
				setSearchCreteria("Search By Zone, Ward,Area, Plot No/House No,Owner Name");
				setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Plot No/House No: "
						+ houseNum);
				LOGGER.debug("Search Criteria : " + getSearchCreteria());
				LOGGER.debug("Search Value : " + getSearchValue());
				setSESSION(getSESSION());
				setGisCity(GISCITY);
				setGisVersion(GISVERSION);
				setMode("bndry");
				target = "result";
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Bndry ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByBndry method");
		return target;
	}

	@ValidationErrorPage(value = "propType")
	public String srchByPropType() {

		LOGGER.debug("Entered into srchByPropType method");
		LOGGER.debug("Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "Property Type : " + propTypeId);
		BoundaryDAO boundaryDAO = new BoundaryDAO();
		String strZoneNum = boundaryDAO.getBoundary(zoneId).getName();
		String strWardNum = "";
		String target = null;
		String propTypeName = "";
		PropertyTypeMasterDAO propTypeMstrDAO = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		if (propTypeId != null && propTypeId != -1) {
			propTypeName = propTypeMstrDAO.getPropertyTypeMasterById(propTypeId).getType();
		}
		if (zoneId != null && zoneId != -1) {
			try {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append("from PropertyMaterlizeView pmv where pmv.zoneID=:ZoneID");
				if (wardId != null && wardId != -1) {
					queryStr.append(" and pmv.wardID=:WardID");
				}
				if (areaId != null && areaId != -1) {
					queryStr.append(" and pmv.streetID=:AreaID");
				}

				if (propTypeId != null && propTypeId != -1) {
					queryStr.append(" and pmv.propTypeMstrID.id =:propType ");
				}

				Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
				if (zoneId != null && zoneId != -1) {
					query.setInteger("ZoneID", zoneId);
				}
				if (wardId != null && wardId != -1) {
					query.setInteger("WardID", wardId);
				}
				if (areaId != null && areaId != -1) {
					query.setInteger("AreaID", areaId);
				}
				if (propTypeId != null && propTypeId != -1) {
					query.setInteger("propType", propTypeId);
				}
				List<PropertyMaterlizeView> propertyList = query.list();
				if (propertyList.size() < 0) {
					setSESSION(getSESSION());
					setMode("propType");
					target = "nodtls";
				}
				int count = 0;
				for (PropertyMaterlizeView propMatview : propertyList) {
					LOGGER.debug("srchByBndry : Property : " + propMatview);
					if (count <= 6) {
						setSearchResultList(getResultsFromMv(propMatview));
					} else {
						break;
					}
					count++;
				}
				if (searchResultList != null) {
					setSearchResultString(getSearchResultsString(searchResultList));
				}
				setSearchUri("../search/searchProperty!srchByPropType.action");
				setSearchCreteria("Search By Zone, Ward,Area,Property Type");
				setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Property Type: "
						+ propTypeName);
				LOGGER.debug("Search Criteria : " + getSearchCreteria());
				LOGGER.debug("Search Value : " + getSearchValue());
				setSESSION(getSESSION());
				setGisCity(GISCITY);
				setGisVersion(GISVERSION);
				setMode("propType");
				target = "result";
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Property Type ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByPropType  methods");
		return target;

	}

	@ValidationErrorPage(value = "demand")
	public String srchByDemand() {
		LOGGER.debug("Entered into srchByDemand method");
		LOGGER.debug("Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "Property Type : " + propTypeId
				+ ", " + "Demand from amt : " + demandFromAmt + ", " + "Demand To amt : " + demandToAmt);
		BoundaryDAO boundaryDAO = new BoundaryDAO();
		String strZoneNum = boundaryDAO.getBoundary(zoneId).getName();
		String strWardNum = "";
		String target = null;
		String propTypeName = "";
		PropertyTypeMasterDAO propTypeMstrDAO = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		if (propTypeId != null && propTypeId != -1) {
			propTypeName = propTypeMstrDAO.getPropertyTypeMasterById(propTypeId).getType();
		}
		if (zoneId != null && zoneId != -1) {
			try {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append("from PropertyMaterlizeView pmv where pmv.zoneID=:ZoneID");
				if (wardId != null && wardId != -1) {
					queryStr.append(" and pmv.wardID=:WardID");
				}
				if (areaId != null && areaId != -1) {
					queryStr.append(" and pmv.streetID=:AreaID");
				}

				if (propTypeId != null && propTypeId != -1) {
					queryStr.append(" and pmv.propTypeMstrID.id =:propType ");
				}

				if (demandFromAmt != null && demandToAmt != null) {
					queryStr.append(" and pmv.aggrCurrDmd BETWEEN :dmdFrmAmt and :dmdToAmt ");
				}

				Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
				if (zoneId != null && zoneId != -1) {
					query.setInteger("ZoneID", zoneId);
				}
				if (wardId != null && wardId != -1) {
					query.setInteger("WardID", wardId);
				}
				if (areaId != null && areaId != -1) {
					query.setInteger("AreaID", areaId);
				}
				if (propTypeId != null && propTypeId != -1) {
					query.setInteger("propType", propTypeId);
				}
				if (demandFromAmt != null && demandToAmt != null) {
					query.setBigDecimal("dmdFrmAmt", demandFromAmt);
					query.setBigDecimal("dmdToAmt", demandToAmt);
				}
				List<PropertyMaterlizeView> propertyList = query.list();
				if (propertyList.size() < 0) {
					setSESSION(getSESSION());
					setMode("demand");
					target = "nodtls";
				}
				int count = 0;
				for (PropertyMaterlizeView propMatview : propertyList) {
					LOGGER.debug("srchByBndry : Property : " + propMatview);
					if (count <= 6) {
						setSearchResultList(getResultsFromMv(propMatview));
					} else {
						break;
					}
					count++;
				}
				if (searchResultList != null) {
					setSearchResultString(getSearchResultsString(searchResultList));
				}
				setSearchUri("../search/searchProperty!srchByDemand.action");
				setSearchCreteria("Search By Zone, Ward,Area,Property Type,Demand");
				setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Property Type: "
						+ propTypeName);
				LOGGER.debug("Search Criteria : " + getSearchCreteria());
				LOGGER.debug("Search Value : " + getSearchValue());
				setSESSION(getSESSION());
				setGisCity(GISCITY);
				setGisVersion(GISVERSION);
				setMode("demand");
				target = "result";
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Demand ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByDemand method");
		return target;

	}

	@ValidationErrorPage(value = "defaulter")
	public String srchByDefaulter() {
		LOGGER.debug("Entered into srchByDefaulter method");
		LOGGER.debug("Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "Property Type : " + propTypeId
				+ ", " + "Defaulter from amt : " + defaulterFromAmt + ", " + "Defaulter To amt : " + defaulterToAmt);
		BoundaryDAO boundaryDAO = new BoundaryDAO();
		String strZoneNum = boundaryDAO.getBoundary(zoneId).getName();
		String strWardNum = "";
		String target = null;
		String propTypeName = "";
		PropertyTypeMasterDAO propTypeMstrDAO = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
		if (propTypeId != null && propTypeId != -1) {
			propTypeName = propTypeMstrDAO.getPropertyTypeMasterById(propTypeId).getType();
		}
		if (zoneId != null && zoneId != -1) {
			try {
				StringBuilder queryStr = new StringBuilder();
				queryStr.append("from PropertyMaterlizeView pmv where pmv.zoneID=:ZoneID");
				if (wardId != null && wardId != -1) {
					queryStr.append(" and pmv.wardID=:WardID");
				}
				if (areaId != null && areaId != -1) {
					queryStr.append(" and pmv.streetID=:AreaID");
				}

				if (propTypeId != null && propTypeId != -1) {
					queryStr.append(" and pmv.propTypeMstrID.id =:propType ");
				}

				if (defaulterFromAmt != null && defaulterToAmt != null) {
					queryStr.append(" and pmv.aggrCurrDmd - pmv.aggrCurrColl between :defaultFrmAmt and :defaultToAmt ");
				}

				Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
				if (zoneId != null && zoneId != -1) {
					query.setInteger("ZoneID", zoneId);
				}
				if (wardId != null && wardId != -1) {
					query.setInteger("WardID", wardId);
				}
				if (areaId != null && areaId != -1) {
					query.setInteger("AreaID", areaId);
				}
				if (propTypeId != null && propTypeId != -1) {
					query.setInteger("propType", propTypeId);
				}
				if (defaulterFromAmt != null && defaulterToAmt != null) {
					query.setBigDecimal("defaultFrmAmt", defaulterFromAmt);
					query.setBigDecimal("defaultToAmt", defaulterToAmt);
				}
				List<PropertyMaterlizeView> propertyList = query.list();
				if (propertyList.size() < 0) {
					setSESSION(getSESSION());
					setMode("defaulter");
					target = "nodtls";
				}
				int count = 0;
				for (PropertyMaterlizeView propMatview : propertyList) {
					LOGGER.debug("srchByBndry : Property : " + propMatview);
					if (count <= 6) {
						setSearchResultList(getResultsFromMv(propMatview));
					} else {
						break;
					}
					count++;
				}
				if (searchResultList != null) {
					setSearchResultString(getSearchResultsString(searchResultList));
				}
				setSearchUri("../search/searchProperty!srchByDefaulter.action");
				setSearchCreteria("Search By Zone, Ward,Area,Property Type,Defaulter");
				setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Property Type: "
						+ propTypeName);
				LOGGER.debug("Search Criteria : " + getSearchCreteria());
				LOGGER.debug("Search Value : " + getSearchValue());
				setSESSION(getSESSION());
				setGisCity(GISCITY);
				setGisVersion(GISVERSION);
				setMode("defaulter");
				target = "result";
			} catch (Exception e) {
				LOGGER.error("Exception in Search Property By Defaulter ", e);
				throw new EGOVRuntimeException("Exception : " + e);
			}
		}
		LOGGER.debug("Exit from srchByDefaulter method");
		return target;

	}

	@SkipValidation
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		List<Boundary> zoneList = getPersistenceService().findAllBy(
				"from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
						+ "and BI.isHistory='N' order by BI.id", ZONE_BNDRY_TYPE, REVENUE_HIERARCHY_TYPE);

		setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));

		LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
		prepareWardDropDownData(zoneId != null, wardId != null);
		prepareAreaDropDownData(wardId != null, areaId != null);
		List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy("from PropertyTypeMaster");
		LOGGER.debug("PropTypeList : " + (propTypeList != null ? propTypeList : ZERO));
		addDropdownData("PropType", propTypeList);
		LOGGER.debug("Zone List : " + (zoneList != null ? zoneList : ZERO));
		LOGGER.debug("Exit from prepare method");
	}

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
			addDropdownData("wardList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareWardDropDownData method");
	}

	private void prepareAreaDropDownData(boolean wardExists, boolean areaExists) {
		LOGGER.debug("Entered into prepareAreaDropDownData method");
		LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Area Exists ? : " + areaExists);
		if (wardExists && areaExists) {
			List<Boundary> areaNewList = new ArrayList<Boundary>();
			areaNewList = getPersistenceService()
					.findAllBy(
							"from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
							AREA_BNDRY_TYPE, getWardId());
			addDropdownData("areaList", areaNewList);
		} else {
			addDropdownData("areaList", Collections.EMPTY_LIST);
		}
		LOGGER.debug("Exit from prepareAreaDropDownData method");
	}

	private List<Map<String, String>> getSearchResults(String indexNumber) {
		LOGGER.debug("Entered into getSearchResults method");
		LOGGER.debug("Index Number : " + indexNumber);
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();
		BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();

		if (indexNumber != null || StringUtils.isNotEmpty(indexNumber)) {

			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
			LOGGER.debug("Basic Property : " + basicProperty);
			if (basicProperty != null) {
				Property property = basicProperty.getProperty();
				LOGGER.debug("Property : " + property);
				Set<Owner> ownerSet = property.getPropertyOwnerSet();
				Map<String, BigDecimal> demandCollMap = ptDemandDao.getDemandCollMap(property);

				Map<String, String> searchResultMap = new HashMap<String, String>();
				searchResultMap.put("indexNum", indexNumber);
				searchResultMap.put("ownerName", ptisCachMgr.buildOwnerFullName(ownerSet));
				searchResultMap.put("parcelId", basicProperty.getGisReferenceNo());
				searchResultMap.put("address", ptisCachMgr.buildAddressByImplemetation(basicProperty.getAddress()));
				searchResultMap.put("currDemand", demandCollMap.get(CURR_DMD_STR).toString());
				searchResultMap.put("arrDemand", demandCollMap.get(ARR_DMD_STR).toString());
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
		LOGGER.debug("Exit from getSearchResults method");
		return searchList;
	}

	private String getSearchResultsString(List<Map<String, String>> searchResultList) {
		LOGGER.debug("Entered into getSearchResultsString method");
		StringBuffer indexNum = new StringBuffer();
		StringBuffer ownerName = new StringBuffer();
		StringBuffer parcelId = new StringBuffer();
		StringBuffer address = new StringBuffer();
		StringBuffer currDemand = new StringBuffer();
		StringBuffer arrDemand = new StringBuffer();
		StringBuffer currDemandDue = new StringBuffer();
		StringBuffer concatResult = new StringBuffer();
		for (Map<String, String> propDtlMap : searchResultList) {
			indexNum.append(propDtlMap.get("indexNum"));
			indexNum.append("^");
			if (propDtlMap.get("ownerName").length() > 40) {
				ownerName.append(propDtlMap.get("ownerName").substring(0, 40));
				ownerName.append("^");
			} else {
				ownerName.append(propDtlMap.get("ownerName"));
				ownerName.append("^");
			}
			parcelId.append(propDtlMap.get("parcelId"));
			parcelId.append("^");
			if (propDtlMap.get("address").length() > 40) {
				address.append(propDtlMap.get("address").substring(0, 40));
				address.append("^");
			} else {
				address.append(propDtlMap.get("address"));
				address.append("^");
			}
			currDemand.append(propDtlMap.get("currDemand"));
			currDemand.append("^");
			arrDemand.append(propDtlMap.get("arrDemand"));
			arrDemand.append("^");
			currDemandDue.append(propDtlMap.get("currDemandDue"));
			currDemandDue.append("^");

		}
		concatResult.append(indexNum).append("@").append(ownerName).append("@").append(parcelId).append("@")
				.append(currDemand).append("@").append(arrDemand).append("@").append(currDemandDue);

		LOGGER.debug("Search Results String : " + concatResult);
		LOGGER.debug("Exit from getSearchResultsString method");
		return concatResult.toString();

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
				searchResultMap.put("arrDemand", (pmv.getAggrArrDmd().subtract(pmv.getAggrArrColl())).toString());
				searchList.add(searchResultMap);
			}
		}
		LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
		LOGGER.debug("Exit from getSearchResults method");
		return searchList;
	}

	@Override
	public void validate() {

		LOGGER.debug("Entered into validate method");
		if (StringUtils.equals(mode, "bndry")) {
			if (zoneId == null || zoneId == -1) {
				addActionError("Select Zone.");
			}
		} else if (StringUtils.equals(mode, "propType")) {
			if (propTypeId == null || propTypeId == -1) {
				addActionError("Select Property Type.");
			}
			if (zoneId == null || zoneId == -1) {
				addActionError("Select Zone.");
			}
		} else if (StringUtils.equals(mode, "defaulter")) {
			if (zoneId == null || zoneId == -1) {
				addActionError("Select Zone.");
			}
			if (defaulterFromAmt != null) {
				if (defaulterToAmt != null) {
					if (defaulterFromAmt.signum() == 0) {
						addActionError("Please enter amount greater than zero for From Amount.");
					}
					if (defaulterToAmt.signum() == 0) {
						addActionError("Please enter amount greater than zero for To Amount.");
					}
					if (defaulterFromAmt.compareTo(defaulterToAmt) == 1) {
						addActionError("From Amount must be less than To Amount.");
					}
				} else {
					addActionError("Enter To Amount.");
				}
			} else {
				addActionError("Enter From Amount.");
			}
		} else if (StringUtils.equals(mode, "demand")) {
			if (zoneId == null || zoneId == -1) {
				addActionError("Select Zone.");
			}
			if (demandFromAmt != null) {
				if (demandToAmt != null) {
					if (demandFromAmt.signum() == 0) {
						addActionError("Please enter amount greater than zero for From Amount.");
					}
					if (demandToAmt.signum() == 0) {
						addActionError("Please enter amount greater than zero for To Amount.");
					}
					if (demandFromAmt.compareTo(demandToAmt) == 1) {
						addActionError("From Amount must be less than To Amount.");
					}
				} else {
					addActionError("Enter To Amount.");
				}
			} else {
				addActionError("Enter From Amount.");
			}
		}
		LOGGER.debug("Exit from validate method");
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

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(String houseNum) {
		this.houseNum = houseNum;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Integer getPropTypeId() {
		return propTypeId;
	}

	public void setPropTypeId(Integer propTypeId) {
		this.propTypeId = propTypeId;
	}

	public List<Map<String, String>> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<Map<String, String>> searchResultList) {
		this.searchResultList = searchResultList;
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

	public BigDecimal getDemandFromAmt() {
		return demandFromAmt;
	}

	public void setDemandFromAmt(BigDecimal demandFromAmt) {
		this.demandFromAmt = demandFromAmt;
	}

	public BigDecimal getDemandToAmt() {
		return demandToAmt;
	}

	public void setDemandToAmt(BigDecimal demandToAmt) {
		this.demandToAmt = demandToAmt;
	}

	public BigDecimal getDefaulterFromAmt() {
		return defaulterFromAmt;
	}

	public void setDefaulterFromAmt(BigDecimal defaulterFromAmt) {
		this.defaulterFromAmt = defaulterFromAmt;
	}

	public BigDecimal getDefaulterToAmt() {
		return defaulterToAmt;
	}

	public void setDefaulterToAmt(BigDecimal defaulterToAmt) {
		this.defaulterToAmt = defaulterToAmt;
	}

	public String getSESSION() {
		return SESSION;
	}

	public void setSESSION(String session) {
		SESSION = session;
	}

	public String getSearchResultString() {
		return searchResultString;
	}

	public void setSearchResultString(String searchResultString) {
		this.searchResultString = searchResultString;
	}

	public String getGisVersion() {
		return gisVersion;
	}

	public void setGisVersion(String gisVersion) {
		this.gisVersion = gisVersion;
	}

	public String getGisCity() {
		return gisCity;
	}

	public void setGisCity(String gisCity) {
		this.gisCity = gisCity;
	}

	public Map<Integer, String> getZoneBndryMap() {
		return ZoneBndryMap;
	}

	public void setZoneBndryMap(Map<Integer, String> zoneBndryMap) {
		ZoneBndryMap = zoneBndryMap;
	}

}
