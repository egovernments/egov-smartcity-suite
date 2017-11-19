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
package org.egov.ptis.actions.search;

import com.opensymphony.xwork2.validator.annotations.Validations;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.AREA_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ELECTION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.GISCITY;
import static org.egov.ptis.constants.PropertyTaxConstants.GISVERSION;
import static org.egov.ptis.constants.PropertyTaxConstants.WARD_BNDRY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONE_BNDRY_TYPE;

@ParentPackage("egov")
@Validations
@Namespace("/search")
public class GisSearchPropertyAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = -5684216227835693553L;
    private final Logger LOGGER = Logger.getLogger(getClass());
    private Long zoneId;
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
    private Map<Long, String> ZoneBndryMap;

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private PropertyTypeMasterDAO propertyTypeMasterDAO;
    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    private PtDemandDao ptDemandDAO;

    @Override
    public Object getModel() {

        return null;
    }

    @SkipValidation
    public void gisFormRedirect() {
        LOGGER.debug("Entered into gisFormRedirect method : GISVERSION : " + GISVERSION + " GISCITY : " + GISCITY);
        final HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.sendRedirect(response.encodeRedirectURL(GISVERSION + GISCITY
                    + "/ajaxtiledviewersample.jsp?DomainName=" + GISCITY + "&mode=PTIS"));
        } catch (final IOException e) {
            LOGGER.error("Exception in Gis Search Property : ", e);
            throw new ApplicationRuntimeException("Exception : " + e);
        }
        LOGGER.debug("Exit from gisFormRedirect method");
    }

    @SkipValidation
    @Action(value = "/gisSearchProperty-gisSearchForm", results = { @Result(name = NEW, location = "/gisSearchProperty-new.jsp") })
    public String gisSearchForm() {
        LOGGER.debug("Entered into gisSearchForm method");
        String target = null;
        setSESSION(getSESSION());
        setGisCity(GISCITY);
        setGisVersion(GISVERSION);
        target = "bndry";
        if (StringUtils.equals(mode, "bndry"))
            target = "bndry";
        else if (StringUtils.equals(mode, "propType"))
            target = "propType";
        else if (StringUtils.equals(mode, "demand"))
            target = "demand";
        else if (StringUtils.equals(mode, "defaulter"))
            target = "defaulter";
        LOGGER.debug("Exit from gisSearchForm method");
        return target;

    }

    @ValidationErrorPage(value = "bndry")
    public String srchByBndry() {
        LOGGER.debug("Entered into srchByBndry method");
        LOGGER.debug("srchByBndry : Zone Id : " + zoneId + ", " + "ward Id: " + wardId + ", " + "House Num : "
                + houseNum + ", " + "Owner Name :" + ownerName + ", " + "Session : " + SESSION);
        final String strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        final String strWardNum = "";
        String target = null;
        if (zoneId != null && zoneId != -1)
            try {
                final StringBuilder queryStr = new StringBuilder();
                queryStr.append("from PropertyMaterlizeView pmv where pmv.zone.id=:ZoneID and pmv.isActive = true");
                if (wardId != null && wardId != -1)
                    queryStr.append(" and pmv.ward.id=:WardID");
                if (areaId != null && areaId != -1)
                    queryStr.append(" and pmv.street.id=:AreaID");

                if (houseNum != null && !houseNum.trim().isEmpty())
                    queryStr.append(" and pmv.houseNo like :houseNum");
                if (ownerName != null && !ownerName.trim().isEmpty())
                    queryStr.append(" and trim(pmv.ownerName) like :OwnerName");
                final Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
                if (zoneId != null && zoneId != -1)
                    query.setLong("ZoneID", zoneId);
                if (wardId != null && wardId != -1)
                    query.setInteger("WardID", wardId);
                if (areaId != null && areaId != -1)
                    query.setInteger("AreaID", areaId);
                if (houseNum != null && !houseNum.trim().isEmpty())
                    query.setString("houseNum", houseNum + "%");
                if (ownerName != null && !ownerName.trim().isEmpty())
                    query.setString("OwnerName", ownerName + "%");
                final List<PropertyMaterlizeView> propertyList = query.list();
                if (propertyList.size() < 0) {
                    setSESSION(getSESSION());
                    setMode("bndry");
                    target = "nodtls";
                }
                int count = 0;
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    LOGGER.debug("srchByBndry : Property : " + propMatview);
                    if (count <= 10)
                        setSearchResultList(getResultsFromMv(propMatview));
                    else
                        break;
                    count++;
                }
                if (searchResultList != null)
                    setSearchResultString(getSearchResultsString(searchResultList));
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
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Bndry ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        LOGGER.debug("Exit from srchByBndry method");
        return target;
    }

    @ValidationErrorPage(value = "propType")
    public String srchByPropType() {

        LOGGER.debug("Entered into srchByPropType method");
        LOGGER.debug("Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "Property Type : " + propTypeId);
        final String strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        final String strWardNum = "";
        String target = null;
        String propTypeName = "";
        if (propTypeId != null && propTypeId != -1)
            propTypeName = propertyTypeMasterDAO.findById(propTypeId, false).getType();
        if (zoneId != null && zoneId != -1)
            try {
                final StringBuilder queryStr = new StringBuilder();
                queryStr.append("from PropertyMaterlizeView pmv where pmv.zone.id=:ZoneID and pmv.isActive = true");
                if (wardId != null && wardId != -1)
                    queryStr.append(" and pmv.ward.id=:WardID");
                if (areaId != null && areaId != -1)
                    queryStr.append(" and pmv.street.id=:AreaID");

                if (propTypeId != null && propTypeId != -1)
                    queryStr.append(" and pmv.propTypeMstrID.id =:propType ");

                final Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
                if (zoneId != null && zoneId != -1)
                    query.setLong("ZoneID", zoneId);
                if (wardId != null && wardId != -1)
                    query.setInteger("WardID", wardId);
                if (areaId != null && areaId != -1)
                    query.setInteger("AreaID", areaId);
                if (propTypeId != null && propTypeId != -1)
                    query.setInteger("propType", propTypeId);
                final List<PropertyMaterlizeView> propertyList = query.list();
                if (propertyList.size() < 0) {
                    setSESSION(getSESSION());
                    setMode("propType");
                    target = "nodtls";
                }
                int count = 0;
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    LOGGER.debug("srchByBndry : Property : " + propMatview);
                    if (count <= 6)
                        setSearchResultList(getResultsFromMv(propMatview));
                    else
                        break;
                    count++;
                }
                if (searchResultList != null)
                    setSearchResultString(getSearchResultsString(searchResultList));
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
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Property Type ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        LOGGER.debug("Exit from srchByPropType  methods");
        return target;

    }

    @ValidationErrorPage(value = "demand")
    public String srchByDemand() {
        LOGGER.debug("Entered into srchByDemand method");
        LOGGER.debug("Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "Property Type : " + propTypeId
                + ", " + "Demand from amt : " + demandFromAmt + ", " + "Demand To amt : " + demandToAmt);
        final String strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        final String strWardNum = "";
        String target = null;
        String propTypeName = "";
        if (propTypeId != null && propTypeId != -1)
            propTypeName = propertyTypeMasterDAO.findById(propTypeId, false).getType();
        if (zoneId != null && zoneId != -1)
            try {
                final StringBuilder queryStr = new StringBuilder();
                queryStr.append("from PropertyMaterlizeView pmv where pmv.zone.id=:ZoneID and pmv.isActive = true");
                if (wardId != null && wardId != -1)
                    queryStr.append(" and pmv.ward.id=:WardID");
                if (areaId != null && areaId != -1)
                    queryStr.append(" and pmv.street.id=:AreaID");

                if (propTypeId != null && propTypeId != -1)
                    queryStr.append(" and pmv.propTypeMstrID.id =:propType ");

                if (demandFromAmt != null && demandToAmt != null)
                    queryStr.append(" and pmv.aggrCurrDmd BETWEEN :dmdFrmAmt and :dmdToAmt ");

                final Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
                if (zoneId != null && zoneId != -1)
                    query.setLong("ZoneID", zoneId);
                if (wardId != null && wardId != -1)
                    query.setInteger("WardID", wardId);
                if (areaId != null && areaId != -1)
                    query.setInteger("AreaID", areaId);
                if (propTypeId != null && propTypeId != -1)
                    query.setInteger("propType", propTypeId);
                if (demandFromAmt != null && demandToAmt != null) {
                    query.setBigDecimal("dmdFrmAmt", demandFromAmt);
                    query.setBigDecimal("dmdToAmt", demandToAmt);
                }
                final List<PropertyMaterlizeView> propertyList = query.list();
                if (propertyList.size() < 0) {
                    setSESSION(getSESSION());
                    setMode("demand");
                    target = "nodtls";
                }
                int count = 0;
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    LOGGER.debug("srchByBndry : Property : " + propMatview);
                    if (count <= 6)
                        setSearchResultList(getResultsFromMv(propMatview));
                    else
                        break;
                    count++;
                }
                if (searchResultList != null)
                    setSearchResultString(getSearchResultsString(searchResultList));
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
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Demand ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        LOGGER.debug("Exit from srchByDemand method");
        return target;

    }

    @ValidationErrorPage(value = "defaulter")
    public String srchByDefaulter() {
        LOGGER.debug("Entered into srchByDefaulter method");
        LOGGER.debug("Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "Property Type : " + propTypeId
                + ", " + "Defaulter from amt : " + defaulterFromAmt + ", " + "Defaulter To amt : " + defaulterToAmt);
        final String strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        final String strWardNum = "";
        String target = null;
        String propTypeName = "";
        if (propTypeId != null && propTypeId != -1)
            propTypeName = propertyTypeMasterDAO.findById(propTypeId, false).getType();
        if (zoneId != null && zoneId != -1)
            try {
                final StringBuilder queryStr = new StringBuilder();
                queryStr.append("from PropertyMaterlizeView pmv where pmv.zone.id=:ZoneID and pmv.isActive = true");
                if (wardId != null && wardId != -1)
                    queryStr.append(" and pmv.ward.id=:WardID");
                if (areaId != null && areaId != -1)
                    queryStr.append(" and pmv.street.id=:AreaID");

                if (propTypeId != null && propTypeId != -1)
                    queryStr.append(" and pmv.propTypeMstrID.id =:propType ");

                if (defaulterFromAmt != null && defaulterToAmt != null)
                    queryStr.append(" and pmv.aggrCurrDmd - pmv.aggrCurrColl between :defaultFrmAmt and :defaultToAmt ");

                final Query query = getPersistenceService().getSession().createQuery(queryStr.toString());
                if (zoneId != null && zoneId != -1)
                    query.setLong("ZoneID", zoneId);
                if (wardId != null && wardId != -1)
                    query.setInteger("WardID", wardId);
                if (areaId != null && areaId != -1)
                    query.setInteger("AreaID", areaId);
                if (propTypeId != null && propTypeId != -1)
                    query.setInteger("propType", propTypeId);
                if (defaulterFromAmt != null && defaulterToAmt != null) {
                    query.setBigDecimal("defaultFrmAmt", defaulterFromAmt);
                    query.setBigDecimal("defaultToAmt", defaulterToAmt);
                }
                final List<PropertyMaterlizeView> propertyList = query.list();
                if (propertyList.size() < 0) {
                    setSESSION(getSESSION());
                    setMode("defaulter");
                    target = "nodtls";
                }
                int count = 0;
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    LOGGER.debug("srchByBndry : Property : " + propMatview);
                    if (count <= 6)
                        setSearchResultList(getResultsFromMv(propMatview));
                    else
                        break;
                    count++;
                }
                if (searchResultList != null)
                    setSearchResultString(getSearchResultsString(searchResultList));
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
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Defaulter ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        LOGGER.debug("Exit from srchByDefaulter method");
        return target;

    }

    @Override
    @SkipValidation
    public void prepare() {
        LOGGER.debug("Entered into prepare method");
        final List<Boundary> zoneList = getPersistenceService().findAllBy(
                "from BoundaryImpl BI where BI.boundaryType.name=? and BI.boundaryType.heirarchyType.name=? "
                        + "and BI.isHistory='N' order by BI.id", ZONE_BNDRY_TYPE, ELECTION_HIERARCHY_TYPE);

        setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));

        LOGGER.debug("Zone id : " + zoneId + ", " + "Ward id : " + wardId);
        prepareWardDropDownData(zoneId != null, wardId != null);
        prepareAreaDropDownData(wardId != null, areaId != null);
        final List<PropertyTypeMaster> propTypeList = getPersistenceService().findAllBy("from PropertyTypeMaster");
        LOGGER.debug("PropTypeList : " + (propTypeList != null ? propTypeList : ZERO));
        addDropdownData("PropType", propTypeList);
        LOGGER.debug("Zone List : " + (zoneList != null ? zoneList : ZERO));
        LOGGER.debug("Exit from prepare method");
    }

    private void prepareWardDropDownData(final boolean zoneExists, final boolean wardExists) {
        LOGGER.debug("Entered into prepareWardDropDownData method");
        LOGGER.debug("Zone Exists ? : " + zoneExists + ", " + "Ward Exists ? : " + wardExists);
        if (zoneExists && wardExists) {
            List<Boundary> wardNewList = new ArrayList<Boundary>();
            wardNewList = getPersistenceService()
                    .findAllBy(
                            "from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
                            WARD_BNDRY_TYPE, getZoneId());
            addDropdownData("wardList", wardNewList);
        } else
            addDropdownData("wardList", Collections.EMPTY_LIST);
        LOGGER.debug("Exit from prepareWardDropDownData method");
    }

    private void prepareAreaDropDownData(final boolean wardExists, final boolean areaExists) {
        LOGGER.debug("Entered into prepareAreaDropDownData method");
        LOGGER.debug("Ward Exists ? : " + wardExists + ", " + "Area Exists ? : " + areaExists);
        if (wardExists && areaExists) {
            List<Boundary> areaNewList = new ArrayList<Boundary>();
            areaNewList = getPersistenceService()
                    .findAllBy(
                            "from BoundaryImpl BI where BI.boundaryType.name=? and BI.parent.id = ? and BI.isHistory='N' order by BI.name ",
                            AREA_BNDRY_TYPE, getWardId());
            addDropdownData("areaList", areaNewList);
        } else
            addDropdownData("areaList", Collections.EMPTY_LIST);
        LOGGER.debug("Exit from prepareAreaDropDownData method");
    }

    private String getSearchResultsString(final List<Map<String, String>> searchResultList) {
        LOGGER.debug("Entered into getSearchResultsString method");
        final StringBuffer indexNum = new StringBuffer();
        final StringBuffer ownerName = new StringBuffer();
        final StringBuffer parcelId = new StringBuffer();
        final StringBuffer address = new StringBuffer();
        final StringBuffer currDemand = new StringBuffer();
        final StringBuffer arrDemand = new StringBuffer();
        final StringBuffer currDemandDue = new StringBuffer();
        final StringBuffer concatResult = new StringBuffer();
        for (final Map<String, String> propDtlMap : searchResultList) {
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

    private List<Map<String, String>> getResultsFromMv(final PropertyMaterlizeView pmv) {
        LOGGER.debug("Entered into getSearchResults method");
        LOGGER.debug("Index Number : " + pmv.getPropertyId());

        if (pmv.getPropertyId() != null || StringUtils.isNotEmpty(pmv.getPropertyId()))
            if (pmv != null) {
                final Map<String, String> searchResultMap = new HashMap<String, String>();
                searchResultMap.put("indexNum", pmv.getPropertyId());
                searchResultMap.put("ownerName", pmv.getOwnerName());
                searchResultMap.put("parcelId", pmv.getGisRefNo());
                searchResultMap.put("address", pmv.getPropertyAddress());
                searchResultMap.put("currDemand", pmv.getAggrCurrFirstHalfDmd().toString());
                searchResultMap.put("currDemandDue", pmv.getAggrCurrFirstHalfDmd().subtract(pmv.getAggrCurrFirstHalfColl()).toString());
                searchResultMap.put("arrDemand", pmv.getAggrArrDmd().subtract(pmv.getAggrArrColl()).toString());
                searchList.add(searchResultMap);
            }
        LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
        LOGGER.debug("Exit from getSearchResults method");
        return searchList;
    }

    @Override
    public void validate() {

        LOGGER.debug("Entered into validate method");
        if (StringUtils.equals(mode, "bndry")) {
            if (zoneId == null || zoneId == -1)
                addActionError("Select Zone.");
        } else if (StringUtils.equals(mode, "propType")) {
            if (propTypeId == null || propTypeId == -1)
                addActionError("Select Property Type.");
            if (zoneId == null || zoneId == -1)
                addActionError("Select Zone.");
        } else if (StringUtils.equals(mode, "defaulter")) {
            if (zoneId == null || zoneId == -1)
                addActionError("Select Zone.");
            if (defaulterFromAmt != null) {
                if (defaulterToAmt != null) {
                    if (defaulterFromAmt.signum() == 0)
                        addActionError("Please enter amount greater than zero for From Amount.");
                    if (defaulterToAmt.signum() == 0)
                        addActionError("Please enter amount greater than zero for To Amount.");
                    if (defaulterFromAmt.compareTo(defaulterToAmt) == 1)
                        addActionError("From Amount must be less than To Amount.");
                } else
                    addActionError("Enter To Amount.");
            } else
                addActionError("Enter From Amount.");
        } else if (StringUtils.equals(mode, "demand")) {
            if (zoneId == null || zoneId == -1)
                addActionError("Select Zone.");
            if (demandFromAmt != null) {
                if (demandToAmt != null) {
                    if (demandFromAmt.signum() == 0)
                        addActionError("Please enter amount greater than zero for From Amount.");
                    if (demandToAmt.signum() == 0)
                        addActionError("Please enter amount greater than zero for To Amount.");
                    if (demandFromAmt.compareTo(demandToAmt) == 1)
                        addActionError("From Amount must be less than To Amount.");
                } else
                    addActionError("Enter To Amount.");
            } else
                addActionError("Enter From Amount.");
        }
        LOGGER.debug("Exit from validate method");
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(final Long zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(final Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(final Integer areaId) {
        this.areaId = areaId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(final String houseNum) {
        this.houseNum = houseNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(final Integer propTypeId) {
        this.propTypeId = propTypeId;
    }

    public List<Map<String, String>> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(final List<Map<String, String>> searchResultList) {
        this.searchResultList = searchResultList;
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

    public BigDecimal getDemandFromAmt() {
        return demandFromAmt;
    }

    public void setDemandFromAmt(final BigDecimal demandFromAmt) {
        this.demandFromAmt = demandFromAmt;
    }

    public BigDecimal getDemandToAmt() {
        return demandToAmt;
    }

    public void setDemandToAmt(final BigDecimal demandToAmt) {
        this.demandToAmt = demandToAmt;
    }

    public BigDecimal getDefaulterFromAmt() {
        return defaulterFromAmt;
    }

    public void setDefaulterFromAmt(final BigDecimal defaulterFromAmt) {
        this.defaulterFromAmt = defaulterFromAmt;
    }

    public BigDecimal getDefaulterToAmt() {
        return defaulterToAmt;
    }

    public void setDefaulterToAmt(final BigDecimal defaulterToAmt) {
        this.defaulterToAmt = defaulterToAmt;
    }

    public String getSESSION() {
        return SESSION;
    }

    public void setSESSION(final String session) {
        SESSION = session;
    }

    public String getSearchResultString() {
        return searchResultString;
    }

    public void setSearchResultString(final String searchResultString) {
        this.searchResultString = searchResultString;
    }

    public String getGisVersion() {
        return gisVersion;
    }

    public void setGisVersion(final String gisVersion) {
        this.gisVersion = gisVersion;
    }

    public String getGisCity() {
        return gisCity;
    }

    public void setGisCity(final String gisCity) {
        this.gisCity = gisCity;
    }

    public Map<Long, String> getZoneBndryMap() {
        return ZoneBndryMap;
    }

    public void setZoneBndryMap(final Map<Long, String> zoneBndryMap) {
        ZoneBndryMap = zoneBndryMap;
    }

}
