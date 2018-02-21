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
package org.egov.ptis.domain.dao.property;

import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_BOUNDARY_ID;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_DEFAULTER_FROM_AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_DEFAULTER_TO_AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_DEMAND_FROM_AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_DEMAND_TO_AMOUNT;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_NEW_HOUSE_NO;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_OWNER_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.SRCH_PROPERTY_TYPE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.SearchResult;
import org.egov.ptis.exceptions.PropertyNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "searchPropertyDAO")
@Transactional(readOnly = true)
public class SearchPropertyHibernateDAO implements SearchPropertyDAO {

    private static final String BOUNDRY_ID = "boundryID";

    private static final String LOW_VAL = "lowVal";

    private static final String QUERY_STRING_FINAL = "query string final--------------";

    private static final String FIRST_NAME = "firstName";

    private static final String EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY = "Exception in  getPropertyByBoundry";

    private static final String PROPERTY_NOT_FOUND_EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY = "PropertyNotFoundException in  getPropertyByBoundry : ";

    private static final String PARAMETERS_NOT_SET_DURING_PROPERTY_SEARCH_BASED_ON_BOUNDRY = "Parameters not Set during PropertySearch based on Boundry!!";

    private static final String BOUNDARY2 = "boundary";

    private static final Logger LOGGER = Logger.getLogger(SearchPropertyHibernateDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private PropertyIDDAO propertyIDDAO;

    @Autowired
    private PropertyDAO propertyDAO;

    @Override
    public SearchResult getBasicPropertyByRegNum(final String regNum) throws PropertyNotFoundException {
        if (regNum == null || regNum.equals(""))
            throw new ApplicationRuntimeException("Reg Num /Folio No is not Set during PropertySearch !!");
        LOGGER.debug("getBasicPropertyByRegNum : regNum : " + regNum);
        BasicProperty basicProperty = null;
        Property property = null;
        SearchResult retSearchResult = null;
        try {
            basicProperty = basicPropertyDAO.getBasicPropertyByRegNum(regNum);
            if (basicProperty != null) {
                property = basicProperty.getProperty();
                if (property != null) {
                    retSearchResult = new SearchResult();
                    retSearchResult.setFolioNumber(regNum);
                    retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                    retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                    retSearchResult.setAddress(basicProperty.getAddress().toString());
                    retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));
                }
            } else
                throw new PropertyNotFoundException("Search Failed No Such Property Existing : BasicProperty is Null");
        } catch (final HibernateException e) {
            final PropertyNotFoundException pnf = new PropertyNotFoundException(
                    "Hibernate Exception In getBasicPropertyByRegNum: " + e.getMessage());
            pnf.initCause(e);
            throw pnf;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error("PropertyNotFoundException in  getBasicPropertyByRegNum : " + e.getMessage());
            throw new PropertyNotFoundException("Exception in  getBasicPropertyByRegNum");
        } catch (final Exception e) {
            LOGGER.error("Exception in  getBasicPropertyByRegNum : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getBasicPropertyByRegNum");
        }

        return retSearchResult;
    }

    @Override
    public SearchResult getPropertyByPropertyId(final String propertyId) throws PropertyNotFoundException {
        if (propertyId == null || propertyId.trim().equals(""))
            throw new ApplicationRuntimeException("propertyId  is not Set during PropertySearch !!");
        BasicProperty basicProperty = null;
        PropertyImpl property = null;
        SearchResult retSearchResult = null;
        BigDecimal currDemand = BigDecimal.ZERO;
        BigDecimal currDemandDue = BigDecimal.ZERO;
        BigDecimal currCollection = BigDecimal.ZERO;
        BigDecimal arrearsDue = BigDecimal.ZERO;
        BigDecimal arrDemand = BigDecimal.ZERO;
        BigDecimal arrCollection = BigDecimal.ZERO;

        try {
            basicProperty = basicPropertyDAO.getAllBasicPropertyByPropertyID(propertyId);
            if (basicProperty != null) {
                property = (PropertyImpl) basicProperty.getProperty();
                if (property != null) {
                    LOGGER.debug("getPropertyByPropertyId : property id : " + property.getId());
                    final Map<String, BigDecimal> dmdCollMap = ptDemandDAO.getDemandCollMap(property);
                    currDemand = dmdCollMap.get(PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR);
                    arrDemand = dmdCollMap.get(PropertyTaxConstants.ARR_DMD_STR);
                    currCollection = dmdCollMap.get(PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR);
                    arrCollection = dmdCollMap.get(PropertyTaxConstants.ARR_COLL_STR);

                    currDemandDue = currDemand.subtract(currCollection);
                    arrearsDue = arrDemand.subtract(arrCollection);
                    final PropertyAddress propertyAddress = basicProperty.getAddress();

                    retSearchResult = new SearchResult();
                    retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                    retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                    retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                    retSearchResult.setAddress(propertyAddress.toString());
                    retSearchResult.setCurrDemand(currDemand);
                    if (currDemandDue != null)
                        retSearchResult.setCurrDemandDue(currDemandDue);
                    if (arrearsDue != null)
                        retSearchResult.setArrearDue(arrearsDue);
                    retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));
                }
            }

        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByPropertyId: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByPropertyId : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByPropertyId");
        }

        return retSearchResult;
    }

    @Override
    public List getPropertyByBoundry(final Integer zoneID, final Integer wardID, final Integer colonyID)
            throws PropertyNotFoundException {
        if (zoneID == null || wardID == null || colonyID == null)
            throw new ApplicationRuntimeException(PARAMETERS_NOT_SET_DURING_PROPERTY_SEARCH_BASED_ON_BOUNDRY);
        LOGGER.info(">>>>>>>>>>>>>colonyId" + colonyID);
        BasicProperty basicProperty = null;

        PropertyID propertyID = null;
        PropertyImpl property = null;
        SearchResult retSearchResult = null;
        List retList = null;
        try {
            final List propIdList = propertyIDDAO.getPropertyIDByBoundry(zoneID, wardID, colonyID);
            if (propIdList != null && !propIdList.isEmpty()) {
                retList = new ArrayList();
                for (final Iterator iter = propIdList.iterator(); iter.hasNext();) {
                    propertyID = (PropertyID) iter.next();
                    basicProperty = propertyID.getBasicProperty();
                    LOGGER.info(">>>>>>>>>>>>>5basicProperty :" + basicProperty);
                    if (basicProperty != null) {
                        property = (PropertyImpl) basicProperty.getProperty();
                        if (property != null) {
                            LOGGER.info("::::::::::::::::::::property" + property.getId());
                            retSearchResult = new SearchResult();
                            retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                            retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                            retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                            retSearchResult.setAddress(basicProperty.getAddress().toString());
                            retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));// Added
                            retList.add(retSearchResult);
                        }
                    }
                }
            } else
                throw new PropertyNotFoundException(
                        "Search Failed No Such Property Existing :Searching Property Based on Colony No !!");

        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByBoundry: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error(PROPERTY_NOT_FOUND_EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY + e.getMessage());
            throw new PropertyNotFoundException(EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY);
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByBoundry : " + e.getMessage());
            throw new ApplicationRuntimeException(EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY);
        }
        return retList;
    }

    @Override
    public List getPropertyIDByBoundryForWardBlockStreet(final Integer wardID, final Integer blockID, final Integer streetID)
            throws PropertyNotFoundException {
        if (wardID == null || blockID == null || streetID == null)
            throw new ApplicationRuntimeException(PARAMETERS_NOT_SET_DURING_PROPERTY_SEARCH_BASED_ON_BOUNDRY);
        LOGGER.info(">>>>>>>>>>>>>streetID" + streetID);
        LOGGER.info("inside ward block street ");

        BasicProperty basicProperty = null;

        PropertyID propertyID = null;
        PropertyImpl property = null;
        SearchResult retSearchResult = null;
        List retList = null;
        try {
            final List propIdList = propertyIDDAO.getPropertyIDByBoundryForWardBlockStreet(wardID, blockID, streetID);
            if (propIdList != null && !propIdList.isEmpty()) {
                retList = new ArrayList();
                for (final Iterator iter = propIdList.iterator(); iter.hasNext();) {
                    propertyID = (PropertyID) iter.next();
                    basicProperty = propertyID.getBasicProperty();
                    LOGGER.info(">>>>>>>>>>>>>5basicProperty :" + basicProperty);
                    if (basicProperty != null) {
                        property = (PropertyImpl) basicProperty.getProperty();
                        if (property != null) {
                            LOGGER.info("::::::::::::::::::::property" + property.getId());
                            retSearchResult = new SearchResult();
                            retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                            retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                            retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                            retSearchResult.setAddress(basicProperty.getAddress().toString());
                            retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));// Added
                            retList.add(retSearchResult);
                        }
                    }
                }
            } else
                throw new PropertyNotFoundException(
                        "Search Failed No Such Property Existing :Searching Property Based on Colony No !!");

        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByBoundry: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error(PROPERTY_NOT_FOUND_EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY + e.getMessage());
            throw new PropertyNotFoundException(EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY);
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByBoundry : " + e.getMessage());
            throw new ApplicationRuntimeException(EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY);
        }
        return retList;
    }

    @Override
    public SearchResult getPropertyByBoundryAndMunNo(final Integer zoneID, final Integer wardID, final Integer colonyID,
            final Integer munNo)
            throws PropertyNotFoundException {
        if (zoneID == null || wardID == null || colonyID == null || munNo == null)
            throw new ApplicationRuntimeException(PARAMETERS_NOT_SET_DURING_PROPERTY_SEARCH_BASED_ON_BOUNDRY);

        BasicProperty basicProperty = null;
        PropertyID propertyID = null;
        Property property = null;
        SearchResult retSearchResult = null;
        try {
            propertyID = propertyIDDAO.getPropertyByBoundryAndMunNo(zoneID, wardID, colonyID, munNo);
            if (propertyID != null) {
                basicProperty = propertyID.getBasicProperty();
                if (basicProperty != null) {
                    property = basicProperty.getProperty();
                    if (property != null) {
                        retSearchResult = new SearchResult();
                        retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                        retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                        retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                        retSearchResult.setAddress(basicProperty.getAddress().toString());
                        retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));// Added
                    }
                }
            } else
                throw new PropertyNotFoundException(
                        "Search Failed No Such Property Existing : Searching Property Based on Colony No,Please Try Later !!");
        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByBoundryAndMunNo: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error(PROPERTY_NOT_FOUND_EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY + e.getMessage());
            throw new PropertyNotFoundException(EXCEPTION_IN_GET_PROPERTY_BY_BOUNDRY);
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByBoundryAndMunNo : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByBoundryAndMunNo");
        }
        return retSearchResult;
    }

    /*
     * This API returns the list of non history default properties in the given boundary which match the given owner name and
     * phone number It matches the ownerName field individually in first, middle and last name for all the owners of the property.
     * It matches the phNumber with the home, office and Mobile numbers for all the owners of the property returns list of
     * ownerNames and property info .
     */
    @Override
    public List getPropertyByBoundryAndOwnerName(final Integer boundryID, final String ownerName, final String phNumber)
            throws PropertyNotFoundException {
        if (boundryID == null)
            throw new ApplicationRuntimeException(PARAMETERS_NOT_SET_DURING_PROPERTY_SEARCH_BASED_ON_BOUNDRY);

        Query qry;

        try {
            LOGGER.info("Executing testChequeQry query................................................");

            final Boundary boundary = boundaryService.getBoundaryById(boundryID.longValue());
            LOGGER.info("testQry executed......................................................");
            final StringBuffer qryStr = new StringBuffer(2000);
            LOGGER.info("-----------search by boundary--------");
            qryStr.append(
                    "select distinct pi From PropertyImpl pi left join fetch pi.basicProperty bp where bp.boundary = :boundary  and pi.status='A' and pi.isDefaultProperty='Y' and bp.active='Y' ");// order
            LOGGER.info("searching for boundary" + qryStr.toString() + "boundaryId" + boundryID);
            qry = getCurrentSession().createQuery(qryStr.toString());

            if (ownerName != null && !ownerName.equals("")) {
                boolean phNumFound = false;
                qryStr.append(
                        " and (pi.propertyOwnerSet.firstName like :firstName or pi.propertyOwnerSet.middleName like :firstName or pi.propertyOwnerSet.lastName like :firstName )");// or
                LOGGER.info("Query String for ownername" + qryStr.toString() + " ........ " + ownerName + "///////"
                        + ownerName);
                if (phNumber != null && !phNumber.equals("")) {
                    phNumFound = true;
                    qryStr.append(
                            " and (pi.propertyOwnerSet.homePhone =:homePhone or pi.propertyOwnerSet.officePhone =:homePhone or pi.propertyOwnerSet.mobilePhone =:homePhone)");
                    LOGGER.info("----------Searching With  Phnumber-----" + qryStr.toString());
                    LOGGER.debug("allvalues set for phnumber");
                }
                qryStr.append(" order by bp.upicNo asc");
                qry = getCurrentSession().createQuery(qryStr.toString());
                qry.setString(FIRST_NAME, ownerName + "%");
                if (phNumFound)
                    qry.setString("homePhone", phNumber);
                LOGGER.debug("allvalues set for ownerFullName");
            }

            qry.setEntity(BOUNDARY2, boundary);
            final List propertyList = qry.list();
            LOGGER.info("query given list of props" + propertyList.size());
            if (!propertyList.isEmpty())
                return getSearchResultList(propertyList);
            else
                throw new PropertyNotFoundException(
                        "Internal Server Error in Searching Property Based on Colony No,Please Try Later !!");

        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByBoundryAndOwnerName: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error("PropertyNotFoundException in  getPropertyByBoundryAndOwnerName : " + e.getMessage());
            throw new PropertyNotFoundException("Exception in  getPropertyByBoundryAndOwnerName");
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByBoundryAndOwnerName : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByBoundryAndOwnerName");
        }
    }

    @Override
    public List getPropertyByOldMuncipalNo(final String oldMuncipalNo) throws PropertyNotFoundException {
        if (oldMuncipalNo == null || oldMuncipalNo.trim().equals(""))
            throw new ApplicationRuntimeException("oldMuncipalNo  is not Set during PropertySearch !!");
        LOGGER.info(">>>>>>>>>>>>>oldMuncipalNo" + oldMuncipalNo);
        BasicProperty basicProperty = null;
        Property property = null;
        SearchResult retSearchResult = null;
        List retList = null;
        try {
            final List basicPropertyList = basicPropertyDAO.getBasicPropertyByOldMunipalNo(oldMuncipalNo);
            if (basicPropertyList != null) {
                retList = new ArrayList();
                for (final Iterator iter = basicPropertyList.iterator(); iter.hasNext();) {
                    basicProperty = (BasicProperty) iter.next();
                    property = basicProperty.getProperty();
                    if (property != null) {
                        retSearchResult = new SearchResult();
                        retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                        retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                        retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                        retSearchResult.setAddress(basicProperty.getAddress().toString());
                        retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));// Added
                        retList.add(retSearchResult);
                    }
                }
            } else
                throw new PropertyNotFoundException(
                        "Search Failed No Such Property Existing : Searching Property Based on oldMuncipalNo,Please Try Later !!");
        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByOldMuncipalNo: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error("PropertyNotFoundException in  getPropertyByOldMuncipalNo : " + e.getMessage());
            throw new PropertyNotFoundException("Exception in  getPropertyByOldMuncipalNo");
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByOldMuncipalNo : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByOldMuncipalNo");
        }
        return retList;
    }

    /*
     * ward or block search by passing the child boundaries list,owner name,phoneNo
     */
    @Override
    public List getPropertiesById(final List lstboundaries, final String OwnerName, final String phoneNumber)
            throws PropertyNotFoundException {
        final StringBuffer qryStr = new StringBuffer(2000);
        List propertyList = null;
        List streetIds = null;
        Query qry;

        LOGGER.info("-----------search by boundary--------");
        try {
            if (lstboundaries != null) {
                LOGGER.info(">>>>>>>>getting list of boundary Ids from boundary list");
                streetIds = new ArrayList();
                for (final Iterator lstItr = lstboundaries.iterator(); lstItr.hasNext();) {
                    final List resultList = (List) lstItr.next();
                    for (final Iterator resItr = resultList.iterator(); resItr.hasNext();) {
                        final Boundary bndry = (Boundary) resItr.next();
                        final Long bndryId = bndry.getId();
                        LOGGER.debug("<<<<<<< bndryId = " + bndryId);
                        if (bndryId != null && !streetIds.contains(bndryId))
                            streetIds.add(bndryId);
                    }
                }
            }
            String ownerNewName = null;
            if (OwnerName != null) {
                final StringBuffer ownName = new StringBuffer(1500);
                final StringTokenizer strToken = new StringTokenizer(OwnerName);
                while (strToken.hasMoreTokens()) {
                    ownName.append(strToken.nextToken(" "));
                    ownName.append("%");
                }
                ownerNewName = ownName.toString();
                LOGGER.info("The Rearranged String is!!!!!!!!!!!!" + ownerNewName);
            }
            qryStr.append(
                    "select distinct prop from PropertyImpl prop left join prop.basicProperty BP left join prop.propertyOwnerSet po"
                            + "  where BP.boundary.id in (:streetIds) and po.id in(select distinct own.id from Owner own where upper(own.firstName) like :OwnerName or upper(own.middleName) like :OwnerName or upper(own.lastName) like :OwnerName "
                            + "or upper(own.firstName)||upper(own.middleName) like :OwnName or upper(own.firstName)||upper(own.lastName) like :OwnName or upper(own.firstName)||upper(own.middleName)||upper(own.lastName) like :OwnName or upper(own.middleName)||upper(own.lastName) like :OwnName)");

            qry = getCurrentSession().createQuery(qryStr.toString());
            if (phoneNumber != null && !phoneNumber.equals("")) {
                qryStr.append(
                        " and po.id in(select distinct own.id from Owner own where own.homePhone =:phoneNumber or own.officePhone =:phoneNumber  or own.mobilePhone =:phoneNumber) ");

                LOGGER.info("----------Searching With  Phnumber-----" + qryStr.toString());
                qry = getCurrentSession().createQuery(qryStr.toString());
                qry.setString("phoneNumber", phoneNumber);
                LOGGER.debug("allvalues set for phnumber");

            }
            qry.setParameterList("streetIds", streetIds);
            qry.setString("OwnerName", OwnerName.toUpperCase() + "%");
            qry.setString("OwnName", ownerNewName.toUpperCase());
            propertyList = qry.list();

        }

        catch (final Exception e) {
            LOGGER.error("error in query" + e.getMessage());
        }
        return getSearchResultList(propertyList);

    }

    @Override
    public SearchResult getPropertyByKhataNumber(final String khataNumber) throws PropertyNotFoundException {
        if (khataNumber == null || khataNumber.trim().equals(""))
            throw new ApplicationRuntimeException("khataNumber  is not Set during PropertySearch !!");
        LOGGER.info(">>>>>>>>>>>>>khataNumber" + khataNumber);
        BasicProperty basicProperty = null;
        PropertyImpl property = null;
        SearchResult retSearchResult = null;
        try {
            basicProperty = basicPropertyDAO.getBasicPropertyByRegNum(khataNumber);

            if (basicProperty != null) {
                property = (PropertyImpl) basicProperty.getProperty();
                if (property != null) {
                    LOGGER.info("property id" + property.getId());
                    retSearchResult = new SearchResult();
                    retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                    retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                    retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                    retSearchResult.setAddress(basicProperty.getAddress().toString());
                }
            } else
                throw new PropertyNotFoundException(
                        "Search Failed No Such Property Existing : Searching Property Based on Khata Number,Please Try Later !!");
        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getPropertyByKhataNumber: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error("PropertyNotFoundException in  getPropertyByKhataNumber : " + e.getMessage());
            throw new PropertyNotFoundException("Exception in  getPropertyByKhataNumber");
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByKhataNumber : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByKhataNumber");
        }
        return retSearchResult;
    }

    @Override
    public List getPropertyByRvAmout(final Integer boundaryID, final Character RvSel, final String lowVal, final String HighVal)
            throws PropertyNotFoundException {
        LOGGER.info(">>>>>>>>>>inside getPropertyByRvAmout>>>>>>>>>>>>>>");
        if (RvSel == null || RvSel.equals(""))
            throw new ApplicationRuntimeException("RV amout selection was not Set during PropertySearch based on Boundry!!");
        LOGGER.info("after query execution--------------RvSel--" + RvSel.charValue() + "---------lowVal----------"
                + lowVal + "--------HighVal--------" + HighVal);

        Query qry;
        try {
            LOGGER.info("Executing testChequeQry query...............");

            final Boundary boundary = boundaryService.getBoundaryById(boundaryID.longValue());
            final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            final String finEndDate = sdf.format(new Date());
            final StringBuffer qryStr = new StringBuffer(2000);
            qryStr.append("select distinct pi From PropertyImpl pi inner join pi.basicProperty bp inner join "
                    + "pi.ptDemandARVSet rv where rv.toDate = to_date('" + finEndDate + "','dd/mm/yyyy') and "
                    + "pi.status='A' and bp.active='Y' ");
            boolean rvLess = false;
            boolean rvGreater = false;
            boolean rvBetween = false;
            boolean rvEqual = false;
            boolean seatFound = false;
            if (boundaryID.intValue() != 0) {
                seatFound = true;
                qryStr.append(" and bp.boundary = :boundary");
                LOGGER.info(">>>>>>>>>>>>>>Search by seat no>>>>>>>>>>>>> " + qryStr.toString() + "....." + boundaryID);
            }
            if (RvSel.charValue() == '1') {
                rvLess = true;
                qryStr.append(" and rv.arv <= :highVal ");
                LOGGER.info(">>>>>>>>>>>>>>Search by arv amount rvsel--- 1");
            }
            if (RvSel.charValue() == '2') {
                rvGreater = true;
                qryStr.append(" and rv.arv >= :lowVal ");
                LOGGER.info(">>>>>>>>>>>>>>Search by arv amount rvsel--- 2");
            }
            if (RvSel.charValue() == '3') {
                rvBetween = true;
                qryStr.append(" and rv.arv >= :lowVal and rv.arv <= :highVal ");
                LOGGER.info(">>>>>>>>>>>>>>Search by arv amount rvsel--- 3");
            }
            if (RvSel.charValue() == '4') {
                rvEqual = true;
                qryStr.append(" and rv.arv = :lowVal ");
                LOGGER.info(">>>>>>>>>>>>>>Search by arv amount rvsel--- 2");
            }
            LOGGER.info(QUERY_STRING_FINAL + qryStr.toString());

            qry = getCurrentSession().createQuery(qryStr.toString());
            if (seatFound)
                qry.setEntity(BOUNDARY2, boundary);
            if (rvLess)
                qry.setBigDecimal("highVal", new BigDecimal(HighVal));
            if (rvGreater)
                qry.setBigDecimal(LOW_VAL, new BigDecimal(lowVal));
            if (rvBetween) {
                qry.setBigDecimal(LOW_VAL, new BigDecimal(lowVal));
                qry.setBigDecimal("highVal", new BigDecimal(HighVal));
            }
            if (rvEqual)
                qry.setBigDecimal(LOW_VAL, new BigDecimal(lowVal));
            LOGGER.info("before query execution");
            final List propertyList = qry.list();
            if (propertyList == null)
                throw new PropertyNotFoundException("No Properties Found with the matching Criteria: lowval:" + lowVal
                        + ",highval:" + HighVal);
            else {
                if (propertyList.size() > 200) {
                    final List errorList = new ArrayList();
                    errorList.add(0, "more props");
                    return errorList;
                }
                return getSearchResultList(propertyList);
            }
        } catch (final Exception e) {
            LOGGER.info("Excetion in getPropertyByRvAmout----------------" + e);
            throw new ApplicationRuntimeException("Error in getPropertyByRvAmout", e);
        }
    }

    @Override
    public List getPropertyByDmdAmout(final Integer boundaryID, final Character DmdSel, final Character DmdChoice,
            final String lowVal,
            final String HighVal) throws PropertyNotFoundException {

        final Map dmdMap = new HashMap();
        LOGGER.info("inside getPropertyByDmdAmout");
        final List propList = new ArrayList();
        final BigDecimal lowerLimit = BigDecimal.ZERO;
        final BigDecimal upperLimit = BigDecimal.ZERO;
        LOGGER.info("lowerLimit -----------" + lowerLimit + "---upperLimit-----------" + upperLimit);
        LOGGER.info("DmdSel------------" + DmdSel.charValue() + "------DmdChoice-----------" + DmdChoice.charValue());
        try {
            LOGGER.info("dmdMap.size()----------------------------------------" + dmdMap.size());
            if (dmdMap.size() > 200) {
                final List errorList = new ArrayList();
                errorList.add(0, "more props");
                return errorList;
            }
            if (!dmdMap.isEmpty()) {
                final Iterator dmdItr = dmdMap.keySet().iterator();
                while (dmdItr.hasNext()) {
                    final String pid = (String) dmdItr.next();
                    final BasicProperty basiProperty = basicPropertyDAO.findById(Integer.valueOf(pid), false);
                    final PropertyImpl property = (PropertyImpl) basiProperty.getProperty();
                    propList.add(property);
                }
            }
            LOGGER.info("the size of the list-->" + propList.size());
            if (propList.isEmpty())
                throw new PropertyNotFoundException("No Properties Found with the matching Criteria: lowerLimit:"
                        + lowVal + ",upperLimit:" + HighVal);
            else
                return getSearchResultList(propList);
        } catch (final Exception e) {
            LOGGER.info("Excetion in getPropertyByDmdAmout---------" + e);
            throw new ApplicationRuntimeException("Error in getPropertyByDmdAmout", e);
        }
    }

    public List getSearchResultList(final List propertyList) {
        BasicProperty basicProperty = null;
        final Set bpSet = new HashSet();
        Address address = null;
        final List retList = new ArrayList();
        final BigDecimal existARV = BigDecimal.ZERO;

        if (propertyList != null)
            for (final Iterator iter = propertyList.iterator(); iter.hasNext();) {
                final Property property = (PropertyImpl) iter.next();
                if (property != null) {

                    basicProperty = property.getBasicProperty();
                    if (!bpSet.contains(basicProperty.getUpicNo())) {
                        bpSet.add(basicProperty.getUpicNo());
                        if (basicProperty != null) {
                            final BigDecimal demand = BigDecimal.ZERO;
                            final BigDecimal rebate = BigDecimal.ZERO;
                            BigDecimal currDemand = BigDecimal.ZERO;
                            final SearchResult retSearchResult = new SearchResult();
                            retSearchResult.setFolioNumber(basicProperty.getOldMuncipalNum());
                            retSearchResult.setUpicNumber(basicProperty.getUpicNo());
                            address = basicProperty.getAddress();
                            if (address != null)
                                retSearchResult.setAddress(basicProperty.getAddress().toString());
                            retSearchResult.setAssesseeFullName(basicProperty.getFullOwnerName());
                            retSearchResult.setBasicPropertyId(String.valueOf(basicProperty.getId()));// Added
                            retSearchResult.setCurrYearArv(existARV.toString());

                            BigDecimal collection = (BigDecimal) propertyDAO.getPropertyCollection(basicProperty.getUpicNo()).get(
                                    0);

                            if (demand != null && demand.compareTo(BigDecimal.ZERO) != 0)
                                currDemand = demand;
                            if (rebate != null && rebate.compareTo(BigDecimal.ZERO) != 0)
                                currDemand = demand.subtract(rebate);
                            if (collection != null && collection.compareTo(BigDecimal.ZERO) != 0)
                                currDemand = currDemand.subtract(collection);
                            retSearchResult.setCurrDemand(currDemand);
                            retList.add(retSearchResult);
                        }
                    }
                }
            }
        return retList;
    }

    /**
     * This API is used to get List of InActive Properties from EGPT_BASIC_PROPERTY table and set it to SearchResult obj and
     * return it
     * 
     * @author Iffath
     * @param lstBoundaries
     * @param List lstBoundaries
     * @return SearchResult
     */

    @Override
    public List getInActivePropertyByBoundary(final List lstboundaries) throws PropertyNotFoundException {
        LOGGER.info("getInActivePropertyByBoundary ");
        List propertyList;
        List streetIds = null;
        Query qry;
        try {
            LOGGER.info("Executing boundary query................................................");
            if (lstboundaries != null) {
                LOGGER.info(">>>>>>>>getting list of boundary Ids from boundary list");
                streetIds = new ArrayList();
                for (final Iterator lstItr = lstboundaries.iterator(); lstItr.hasNext();) {
                    final List resultList = (List) lstItr.next();
                    for (final Iterator resItr = resultList.iterator(); resItr.hasNext();) {
                        final Boundary bndry = (Boundary) resItr.next();
                        final Long bndryId = bndry.getId();
                        LOGGER.debug("<<<<<<< bndryId = " + bndryId);
                        if (bndryId != null && !streetIds.contains(bndryId))
                            streetIds.add(bndryId);
                    }
                }
            }
            final StringBuffer qryStr = new StringBuffer(2000);
            qryStr.append(
                    "select distinct pi From PropertyImpl pi left join fetch pi.basicProperty bp where bp.boundary.id in (:streetIds) and pi.status='A' and bp.active='N'");// order
            qry = getCurrentSession().createQuery(qryStr.toString());

            qry.setParameterList("streetIds", streetIds);
            propertyList = qry.list();
            if (propertyList != null)
                return getSearchResultList(propertyList);
            else
                throw new PropertyNotFoundException(
                        "Internal Server Error in Searching InActive Property Based on Boundary,Please Try Later !!");

        } catch (final HibernateException e) {
            final PropertyNotFoundException exception = new PropertyNotFoundException(
                    "Hibernate Exception In getInActivePropertyByBoundary: " + e.getMessage());
            exception.initCause(e);
            throw exception;
        } catch (final PropertyNotFoundException e) {
            LOGGER.error("PropertyNotFoundException in  getInActivePropertyByBoundary : " + e.getMessage());
            throw new PropertyNotFoundException("Exception in  getInActivePropertyByBoundary");
        } catch (final Exception e) {
            LOGGER.error("Exception in  getInActivePropertyByBoundary : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getInActivePropertyByBoundary");
        }
    }

    /**
     * This method called getPropertyByMobileNumber gets List of Property Objects
     * 
     * <p>
     * This method returns List of Property Objects for given mobile Number.
     * </p>
     * 
     * @param java .lang.String mobileNum
     * 
     * @throws org.egov.ptis.PropertyNotFoundException.
     */

    @Override
    public List getPropertyByMobileNumber(final String mobileNum) throws PropertyNotFoundException {
        List propList = new ArrayList();
        final StringBuffer qryStr = new StringBuffer(2000);
        try {

            final Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
            final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());

            if (mobileNum != null && !mobileNum.equals("")) {
                Query qry = null;
                qryStr.append(
                        "select distinct pi From PropertyImpl pi left join fetch pi.basicProperty bp left join fetch bp.address ad where ad.mobileNo like :mobileNum  and pi.status='A' and pi.installment = :Installment ");
                qry = getCurrentSession().createQuery(qryStr.toString());
                qry.setString("mobileNum", "%" + mobileNum + "%");
                qry.setEntity("Installment", installment);
                propList = qry.list();
            }
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByMobileNumber : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByMobileNumber");
        }
        return propList;
    }

    /**
     * This method called getPropertyByBillNumber gets List of Property Objects
     * 
     * <p>
     * This method returns List of Property Objects for given billNumber.
     * </p>
     * 
     * @param java .lang.String billNumber
     * 
     * @throws org.egov.ptis.PropertyNotFoundException.
     */

    @Override
    public List getPropertyByBillNumber(final String billNumber) throws PropertyNotFoundException {
        List propList = new ArrayList();
        final StringBuffer qryStr = new StringBuffer(2000);
        try {
            if (billNumber != null && !billNumber.equals("")) {
                final Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
                final Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
                Query qry = null;
                qryStr.append(
                        "From PropertyImpl pi left join fetch pi.basicProperty bp  where  bp.upicNo like :billNumber  and pi.isDefaultProperty='Y' and pi.status='A' and pi.installment = :Installment ");
                qry = getCurrentSession().createQuery(qryStr.toString());
                qry.setString("billNumber", billNumber + "%");
                qry.setEntity("Installment", installment);
                propList = qry.list();
            }
        } catch (final Exception e) {
            LOGGER.error("Exception in  getPropertyByBillNumber : " + e.getMessage());
            throw new ApplicationRuntimeException("Exception in  getPropertyByBillNumber");
        }
        return propList;
    }

    /**
     * This method called getPropertyByBoundryAndOwnerNameAndHouseNo gets List of Property Objects
     * 
     * <p>
     * This method returns List of Property Objects for given boundaryId,ownerName,newHouseNo.,oldHouseNo. .
     * </p>
     * 
     * @param java .lang.Integer boundryID
     * 
     * @param java .lang.String ownerName
     * 
     * @param java .lang.String houseNo
     * 
     * @param java .lang.String oldHouseNo
     * 
     * @throws org.egov.ptis.PropertyNotFoundException.
     */

    @Override
    public List getPropertyByBoundryAndOwnerNameAndHouseNo(final Integer boundryID, final String ownerName,
            final String newHouseNo,
            final String oldHouseNo) throws PropertyNotFoundException {
        Query qry;
        List propertyList = new ArrayList();
        try {
            if (boundryID != null && !boundryID.equals("")) {
                final Boundary boundary = boundaryService.getBoundaryById(boundryID.longValue());
                LOGGER.info("boundary.obj................" + boundary);

                final StringBuffer qryStr = new StringBuffer(2000);
                qryStr.append(
                        "select distinct pi From PropertyImpl pi left join fetch pi.basicProperty bp left join fetch bp.propertyID ppid left join pi.propertyOwnerSet ownerSet where  pi.status='A' and pi.isDefaultProperty='Y' and bp.active='Y'  ");
                LOGGER.info("searching for boundary " + qryStr.toString() + "boundaryId" + boundryID);
                boolean bndryFound = false;
                boolean wardBndryFound = false;
                boolean areaBndryFound = false;
                boolean ownerFound = false;
                boolean houseFound = false;
                boolean oldHouseNum = false;
                if (boundary != null) {
                    final String boundryType = boundary.getBoundaryType().getName();
                    LOGGER.info("testQry executed.......boundryType................." + boundryType);
                    if (boundryType != null && boundryType.endsWith("Ward")) {
                        wardBndryFound = true;
                        qryStr.append(" and ppid.ward = :boundary");
                    } else if (boundryType != null && boundryType.endsWith("Area")) {
                        areaBndryFound = true;
                        qryStr.append(" and ppid.area = :boundryID");
                    } else if (boundryType != null && boundryType.endsWith("Locality")) {
                        bndryFound = true;
                        qryStr.append(" and ppid.locality = :boundryID");
                    } else if (boundryType != null && boundryType.endsWith("Street")) {
                        bndryFound = true;
                        qryStr.append(" and ppid.street = :boundryID");
                    }
                }
                if (ownerName != null && !ownerName.equals("")) {
                    ownerFound = true;
                    qryStr.append(
                            " and (upper(ownerSet.firstName) like :firstName or upper(ownerSet.middleName) like :firstName or upper(ownerSet.lastName) like :firstName) "); // :lastName
                }
                if (newHouseNo != null && !newHouseNo.equals("")) {
                    houseFound = true;
                    qryStr.append(" and (upper(pi.basicProperty.address.houseNo) like :houseno ) ");
                }

                if (oldHouseNo != null && !oldHouseNo.equals("")) {
                    oldHouseNum = true;
                    qryStr.append(" and (upper(pi.basicProperty.address.doorNumOld) like :oldHouseNo) ");
                }
                qryStr.append(" order by bp.id ");
                LOGGER.debug(QUERY_STRING_FINAL + qryStr.toString());
                qry = getCurrentSession().createQuery(qryStr.toString());

                if (wardBndryFound)
                    qry.setEntity(BOUNDARY2, boundary);
                if (areaBndryFound)
                    qry.setInteger(BOUNDRY_ID, boundryID);
                if (bndryFound)
                    qry.setInteger(BOUNDRY_ID, boundryID);
                if (ownerFound)
                    qry.setString(FIRST_NAME, "%" + ownerName.toUpperCase() + "%");
                if (houseFound)
                    qry.setString("houseno", newHouseNo.toUpperCase() + "%");

                if (oldHouseNum)
                    qry.setString("oldHouseNo", oldHouseNo.toUpperCase() + "%");
                propertyList = qry.list();
            }

            return propertyList;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error in getPropertyByBoundryAndOwnerNameAndHouseNo", e);
        }

    }

    /**
     * either objectionNumber or any of the date fields are mandatory
     * 
     * @param propertyTypeMasterId
     * @param objectionNumber
     * @param fromObjection
     * @param toObjection
     * @return
     * @throws ValidationException when mandatory fields not passed
     */
    @Override
    public List<Property> getPropertyByObjectionDetails(final Long propertyTypeMasterId, final String objectionNumber,
            final Date fromObjection, final Date toObjection) throws ValidationException {
        if ((objectionNumber == null || objectionNumber.trim().isEmpty())
                && fromObjection == null && toObjection == null)
            throw new ValidationException("ObjectioNumber or ObjectionDate is mandatory",
                    "ObjectioNumber or ObjectionDate is mandatory");
        final Criteria propertyCriteria = getCurrentSession().createCriteria(PropertyImpl.class, "propertyImpl").add(
                Restrictions.eq("status", PropertyTaxConstants.STATUS_ISACTIVE));
        final DetachedCriteria detachCrtObjection = DetachedCriteria.forClass(RevisionPetition.class);
        detachCrtObjection.setProjection(Projections.projectionList().add(Projections.property("basicProperty")));
        if (propertyTypeMasterId != null && propertyTypeMasterId > 0) {
            propertyCriteria.createAlias("propertyDetail", "propertyDetail");
            propertyCriteria.createAlias("propertyDetail.propertyTypeMaster", "propertyTypeMaster");
            propertyCriteria.add(Restrictions.eq("propertyTypeMaster.id", propertyTypeMasterId));
        }
        if (objectionNumber != null && !objectionNumber.trim().isEmpty())
            detachCrtObjection.add(Restrictions.ilike("objectionNumber", objectionNumber));
        if (fromObjection != null && toObjection != null)
            detachCrtObjection.add(Restrictions.between("recievedOn", fromObjection, toObjection));
        else if (fromObjection != null)
            detachCrtObjection.add(Restrictions.ge("recievedOn", fromObjection));
        else if (toObjection != null)
            detachCrtObjection.add(Restrictions.le("recievedOn", toObjection));
        propertyCriteria.add(Subqueries.propertyIn("basicProperty", detachCrtObjection));
        return propertyCriteria.list();
    }

    public List getPropertyByBoundryAndQueryParamMap(final Map<String, Object> queryParamMap)
            throws PropertyNotFoundException {

        String ownerName = null;
        String newHouseNo = null;
        Integer propTypeId = null;
        ;
        BigDecimal dmdFrmAmt = null;
        BigDecimal dmdToAmt = null;
        BigDecimal defaultFrmamt = null;
        BigDecimal defaultToAmt = null;
        Query qry;
        List propertyList = new ArrayList();
        try {
            if (queryParamMap.get(SRCH_BOUNDARY_ID) != null) {
                final Integer boundryID = (Integer) queryParamMap.get(SRCH_BOUNDARY_ID);
                if (boundryID != null) {
                    final Boundary boundary = boundaryService.getBoundaryById(boundryID.longValue());
                    LOGGER.info("boundary.obj................" + boundary);
                    if (boundary != null) {
                        final String boundryType = boundary.getBoundaryType().getName();
                        LOGGER.info("testQry executed.......boundryType................." + boundryType);
                        final StringBuffer qryStr = new StringBuffer(2000);
                        qryStr.append(
                                "select EGPTP.ID_PROPERTY from egpt_property egptp left outer join egpt_basic_property egptb")
                                .append(" on EGPTP.ID_BASIC_PROPERTY=EGPTB.ID_BASIC_PROPERTY")
                                .append(" left outer join egpt_propertyid egptpid on EGPTB.ID_PROPERTYID=EGPTPID.ID")
                                .append(" left outer join egpt_property_detail egptpd on EGPTP.ID_PROPERTY=EGPTPD.ID_PROPERTY")
                                .append(" left outer join egpt_ptdemand egptde on EGPTP.ID_PROPERTY=EGPTDE.ID_PROPERTY")
                                .append(" left outer join EG_DEMAND_DETAILS egdet on EGPTDE.ID_DEMAND=EGDET.ID_DEMAND")
                                .append(" left outer join eg_demand_reason egdr on EGDET.ID_DEMAND_REASON=egdr.id")
                                .append(" left outer join eg_address egadd on EGPTB.ADDRESSID=EGADD.ADDRESSID")
                                .append(" left outer join EGPT_PROPERTY_OWNER egptprown on EGPTP.ID_PROPERTY=EGPTPROWN.ID_PROPERTY")
                                .append(" left outer join eg_citizen egciti on EGPTPROWN.OWNERID=EGCITI.CITIZENID")
                                .append(" where EGPTP.STATUS='A' and EGPTP.IS_DEFAULT_PROPERTY='Y' and EGPTB.IS_ACTIVE='Y'");

                        LOGGER.debug("searching for boundary " + qryStr.toString() + "boundaryId" + boundryID);
                        boolean zoneBndryFound = false;
                        boolean bndryFound = false;
                        boolean wardBndryFound = false;
                        boolean areaBndryFound = false;
                        boolean ownerFound = false;
                        boolean houseFound = false;
                        boolean propTypeFound = false;
                        boolean demandFound = false;
                        boolean defaulterFound = false;
                        if (boundryType != null && boundryType.endsWith("Revenue Zone")) {
                            zoneBndryFound = true;
                            qryStr.append(" and EGPTPID.ZONE_NUM= :boundryID");
                        } else if (boundryType != null && boundryType.endsWith("Revenue Ward")) {
                            wardBndryFound = true;
                            qryStr.append(" and EGPTPID.WARD_ADM_ID = :boundryID");
                        } else if (boundryType != null && boundryType.endsWith("Revenue Area")) {
                            areaBndryFound = true;
                            qryStr.append(" and EGPTPID.ADM1 = :boundryID");
                        } else if (boundryType != null && boundryType.endsWith("Revenue Locality")) {
                            bndryFound = true;
                            qryStr.append(" and EGPTPID.ADM2 = :boundryID");
                        } else if (boundryType != null && boundryType.endsWith("Revenue Street")) {
                            bndryFound = true;
                            qryStr.append(" and EGPTPID.ADM3 = :boundryID");
                        }

                        if (queryParamMap.get(SRCH_OWNER_NAME) != null) {
                            ownerName = queryParamMap.get(SRCH_OWNER_NAME).toString();
                            if (!ownerName.equals("")) {
                                ownerFound = true;
                                qryStr.append(" and upper(EGCITI.FIRSTNAME) like :firstName "); // :lastName
                            }
                        }
                        if (queryParamMap.get(SRCH_NEW_HOUSE_NO) != null) {
                            newHouseNo = queryParamMap.get(SRCH_NEW_HOUSE_NO).toString();
                            if (!newHouseNo.equals("")) {
                                houseFound = true;
                                qryStr.append(" and upper(EGADD.HOUSENO) like :houseno ");
                            }
                        }
                        if (queryParamMap.get(SRCH_PROPERTY_TYPE) != null) {
                            propTypeId = (Integer) queryParamMap.get(SRCH_PROPERTY_TYPE);
                            if (propTypeId != -1) {
                                propTypeFound = true;
                                qryStr.append(" and EGPTPD.ID_PROPERTYTYPEMASTER =:propType ");
                            }
                        }

                        qryStr.append(
                                " and EGDR.ID_INSTALLMENT = (select id_installment from eg_installment_master where start_date<=sysdate and end_date>=sysdate")
                                .append(" and id_module=(select id_module from eg_module where module_name='Property Tax')) ")
                                .append(" group by EGDR.ID_INSTALLMENT,EGPTP.ID_PROPERTY");
                        if (queryParamMap.get(SRCH_DEMAND_FROM_AMOUNT) != null
                                || queryParamMap.get(SRCH_DEMAND_TO_AMOUNT) != null) {
                            dmdFrmAmt = (BigDecimal) queryParamMap.get(SRCH_DEMAND_FROM_AMOUNT);
                            dmdToAmt = (BigDecimal) queryParamMap.get(SRCH_DEMAND_TO_AMOUNT);
                            demandFound = true;
                            qryStr.append(" having sum( EGDET.AMOUNT) BETWEEN :dmdFrmAmt and :dmdToAmt ");
                        }
                        if (queryParamMap.get(SRCH_DEFAULTER_FROM_AMOUNT) != null
                                || queryParamMap.get(SRCH_DEFAULTER_TO_AMOUNT) != null) {
                            defaultFrmamt = (BigDecimal) queryParamMap.get(SRCH_DEFAULTER_FROM_AMOUNT);
                            defaultToAmt = (BigDecimal) queryParamMap.get(SRCH_DEFAULTER_TO_AMOUNT);
                            defaulterFound = true;
                            qryStr.append(
                                    " having sum(EGDET.AMOUNT)- sum(EGDET.AMT_COLLECTED) between :defaultFrmAmt and :defaultToAmt ");
                        }
                        LOGGER.info(QUERY_STRING_FINAL + qryStr.toString());
                        qry = getCurrentSession().createSQLQuery(qryStr.toString());
                        if (zoneBndryFound)
                            qry.setInteger(BOUNDRY_ID, boundryID);
                        if (wardBndryFound)
                            qry.setInteger(BOUNDRY_ID, boundryID);
                        if (areaBndryFound)
                            qry.setInteger(BOUNDRY_ID, boundryID);
                        if (bndryFound)
                            qry.setInteger(BOUNDRY_ID, boundryID);
                        if (ownerFound)
                            qry.setString(FIRST_NAME, "%" + ownerName.toUpperCase() + "%");
                        if (houseFound)
                            qry.setString("houseno", newHouseNo.toUpperCase() + "%");

                        if (propTypeFound)
                            qry.setInteger("propType", propTypeId);
                        if (demandFound) {
                            qry.setBigDecimal("dmdFrmAmt", dmdFrmAmt);
                            qry.setBigDecimal("dmdToAmt", dmdToAmt);
                        }
                        if (defaulterFound) {
                            qry.setBigDecimal("defaultFrmAmt", defaultFrmamt);
                            qry.setBigDecimal("defaultToAmt", defaultToAmt);
                        }
                        propertyList = qry.list();
                    }
                }
            }
            return propertyList;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error in getPropertyByBoundryAndOwnerNameAndHouseNo", e);
        }

    }

}
