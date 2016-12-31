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
package org.egov.ptis.actions.search;

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.web.struts.actions.BaseFormAction.NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ADD_DEMAND;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_ALTER_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_BIFURCATE_ASSESSENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_COLLECT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_DEMAND_BILL;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_EDIT_COLLECTION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_EDIT_DEMAND;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_EDIT_OWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_MEESEVA_GRP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_MEESEVA_RP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_MEESEVA_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_MODIFY_DATA_ENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_REVISION_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TAX_EXEMTION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.LOCATION_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.NOT_AVAILABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_STATUS_MARK_DEACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSIONLOGINID;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_DATAENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.SOURCEOFDATA_MIGRATION;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.actions.common.CommonServices;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@ParentPackage("egov")
@Validations
@Results({
        @Result(name = NEW, location = "searchProperty-new.jsp"),
        @Result(name = SearchPropertyAction.TARGET, location = "searchProperty-result.jsp"),
        @Result(name = SearchPropertyAction.COMMON_FORM, location = "searchProperty-commonForm.jsp"),
        @Result(name = APPLICATION_TYPE_ALTER_ASSESSENT, type = "redirectAction", location = "modifyProperty-modifyForm", params = {
                "namespace", "/modify", "indexNumber", "${assessmentNum}", "modifyRsn", "ADD_OR_ALTER", "applicationType", "${applicationType}" }),
        @Result(name = APPLICATION_TYPE_BIFURCATE_ASSESSENT, type = "redirectAction", location = "modifyProperty-modifyForm", params = {
                "namespace", "/modify", "indexNumber", "${assessmentNum}", "modifyRsn", "BIFURCATE", "applicationType", "${applicationType}" }),
        @Result(name = APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP, type = "redirectAction", location = "redirect", params = {
                "namespace", "/property/transfer", "assessmentNo", "${assessmentNum}", "applicationType", "${applicationType}" }),
        @Result(name = APPLICATION_TYPE_MEESEVA_TRANSFER_OF_OWNERSHIP, type = "redirectAction", location = "redirect", params = {
                "namespace", "/property/transfer", "assessmentNo", "${assessmentNum}", "meesevaApplicationNumber",
                "${meesevaApplicationNumber}", "meesevaServiceCode", "${meesevaServiceCode}", "applicationType",
                "${applicationType}" }),
        @Result(name = APPLICATION_TYPE_REVISION_PETITION, type = "redirectAction", location = "revPetition-newForm", params = {
                "namespace", "/revPetition", "propertyId", "${assessmentNum}","wfType","RP" }),
        @Result(name = APPLICATION_TYPE_GRP, type = "redirectAction", location = "genRevPetition-newForm", params = {
                "namespace", "/revPetition", "propertyId", "${assessmentNum}","wfType","GRP" }),
        @Result(name = "meesevaerror", location = "/WEB-INF/jsp/common/meeseva-errorPage.jsp"),
        @Result(name = APPLICATION_TYPE_COLLECT_TAX, type = "redirectAction", location = "searchProperty-searchOwnerDetails", params = {
                "namespace", "/search", "assessmentNum", "${assessmentNum}" }),
        @Result(name = APPLICATION_TYPE_DEMAND_BILL, type = "redirectAction", location = "billGeneration-generateDemandBill", params = {
                "namespace", "/bills", "indexNumber", "${assessmentNum}" }),
        @Result(name = APPLICATION_TYPE_VACANCY_REMISSION, type = "redirect", location = "../vacancyremission/create/${assessmentNum},${mode}", params = {
                "meesevaApplicationNumber", "${meesevaApplicationNumber}" }),
        @Result(name = APPLICATION_TYPE_TAX_EXEMTION, type = "redirect", location = "..//exemption/form/${assessmentNum}", params = {
                "meesevaApplicationNumber", "${meesevaApplicationNumber}" }),
        @Result(name = APPLICATION_TYPE_EDIT_DEMAND, type = "redirectAction", location = "editDemand-newEditForm", params = {
                "namespace", "/edit", "propertyId", "${assessmentNum}" }),
        @Result(name = APPLICATION_TYPE_ADD_DEMAND, type = "redirectAction", location = "addDemand-newAddForm", params = {
                "namespace", "/edit", "propertyId", "${assessmentNum}" }),
                @Result(name = APPLICATION_TYPE_EDIT_COLLECTION, type = "redirect", location = "../editCollection/editForm/${assessmentNum}"),
        @Result(name = APPLICATION_TYPE_DEMOLITION, type = "redirect", location = "../property/demolition/${assessmentNum}"),
        @Result(name = APPLICATION_TYPE_EDIT_OWNER, type = "redirect", location = "../editowner/${assessmentNum}"),
        @Result(name = SearchPropertyAction.USER_DETAILS, location = "searchProperty-ownerDetails.jsp"),
        @Result(name = APPLICATION_TYPE_MODIFY_DATA_ENTRY, type = "redirectAction", location = "createProperty-editDataEntryForm", params = {
                "namespace", "/create", "indexNumber", "${assessmentNum}", "modifyRsn", "EDIT_DATA_ENTRY", "modelId", "${activePropertyId}" }),
        @Result(name = APPLICATION_TYPE_MEESEVA_GRP, type = "redirectAction", location = "genRevPetition-newForm", params = {
                "namespace", "/revPetition", "indexNumber", "${assessmentNum}", "meesevaApplicationNumber",
                "${meesevaApplicationNumber}", "meesevaServiceCode", "${meesevaServiceCode}", "wfType", "GRP", "applicationType", "${applicationType}" }),
        @Result(name = APPLICATION_TYPE_MEESEVA_RP, type = "redirectAction", location = "revPetition-newForm", params = {
                "namespace", "/revPetition", "propertyId", "${assessmentNum}", "meesevaApplicationNumber",
                "${meesevaApplicationNumber}", "meesevaServiceCode", "${meesevaServiceCode}", "applicationType", "${applicationType}" ,"wfType","RP"})})
public class SearchPropertyAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 6978874588028662454L;
    protected static final String COMMON_FORM = "commonForm";
    private final Logger LOGGER = Logger.getLogger(getClass());
    private static final String RESULT_ERROR = "meesevaerror";
    protected static final String USER_DETAILS = "ownerDetails";
    protected static final String UPDATEMOBILE_FORM = "updateMobileNo";
    public static final String TARGET = "result";

    private Long zoneId;
    private Long wardId;
    private Long propertyTypeMasterId;

    private Integer locationId;
    private Integer areaName;

    private String assessmentNum;
    private String houseNumBndry;
    private String ownerNameBndry;
    private String houseNumArea;
    private String ownerName;
    private String oldHouseNum;
    private String oldMuncipalNum;
    private String mode;
    private String searchUri;
    private String searchCriteria;
    private String searchValue;
    private String roleName;
    private String markedForDeactive = "N";
    private String fromDemand;
    private String toDemand;
    private String applicationType;
    private String doorNo;
    private String mobileNumber;
    private String meesevaApplicationNumber;
    private String meesevaServiceCode;

    private List<Map<String, String>> searchResultList;
    List<Map<String, String>> searchList = new ArrayList<Map<String, String>>();
    private Map<Long, String> ZoneBndryMap;
    private Map<Long, String> WardndryMap;

    private User propertyOwner;
    private BasicProperty basicProperty;

    private boolean isDemandActive;
    private Boolean loggedUserIsMeesevaUser = Boolean.FALSE;
    private String activePropertyId;

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

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Override
    public Object getModel() {
        return null;
    }

    /**
     * @return - Gets forwarded to Search Property Screen for officials
     */
    @SkipValidation
    @Action(value = "/search/searchProperty-searchForm")
    public String searchForm() {
        setAssessmentNum("");
        setOldMuncipalNum("");
        setDoorNo("");
        setMobileNumber("");
        return NEW;
    }

    /**
     * Generalised method to give search property screen to perform different
     * transactions like alter, bifurcate, transfer etc
     * 
     * @return
     */
    public String commonForm() {
        loggedUserIsMeesevaUser = propertyService.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser) {
            final HttpServletRequest request = ServletActionContext.getRequest();
            if (request.getParameter("applicationNo") == null || request.getParameter("meesevaServicecode") == null) {
                addActionMessage(getText("MEESEVA.005"));
                return RESULT_ERROR;

            } else {
                setMeesevaApplicationNumber(request.getParameter("applicationNo"));
                setMeesevaServiceCode(request.getParameter("meesevaServicecode"));
            }
        }
        return COMMON_FORM;
    }

    /**
     * Generalised method to redirect the form page to different transactional
     * form pages
     * 
     * @return
     */
    @ValidationErrorPage(value = COMMON_FORM)
    @Action(value = "/search/searchProperty-commonSearch")
    public String commonSearch() {
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByIndexNumAndParcelID(assessmentNum, null);
        if (basicProperty == null) {
            addActionError(getText("validation.property.doesnot.exists"));
            return COMMON_FORM;
        }
        checkIsDemandActive(basicProperty.getProperty());
        if (!applicationType.equalsIgnoreCase(APPLICATION_TYPE_COLLECT_TAX)
                && !applicationType.equalsIgnoreCase(APPLICATION_TYPE_DEMAND_BILL)
                && !applicationType.equalsIgnoreCase(APPLICATION_TYPE_REVISION_PETITION)) {

            if (!isDemandActive) {
                addActionError(getText("error.msg.demandInactive"));
                return COMMON_FORM;
            } else if (basicProperty.getActiveProperty().getPropertyDetail().getPropertyTypeMaster().getCode()
                    .equalsIgnoreCase(PropertyTaxConstants.OWNERSHIP_TYPE_EWSHS)
                    && !applicationType.equalsIgnoreCase(APPLICATION_TYPE_ALTER_ASSESSENT)
                    && !applicationType.equalsIgnoreCase(APPLICATION_TYPE_TAX_EXEMTION)) {
                addActionError(getText("EWSHS.transaction.error"));
                return COMMON_FORM;
            }
        }
        if (applicationType.equalsIgnoreCase(APPLICATION_TYPE_MODIFY_DATA_ENTRY)){
        	Property activeProperty = basicProperty.getProperty();
        	//Allow modification only for properties where source is Data Entry
        	if(!basicProperty.getSource().toString().equalsIgnoreCase(SOURCEOFDATA_DATAENTRY.toString())){
        		addActionError(getText("edit.dataEntry.source.error"));
                return COMMON_FORM;
        	}
        	
        	if(basicProperty.getSource().toString().equalsIgnoreCase(SOURCEOFDATA_DATAENTRY.toString())){
        		//Validate if any other type of transactions are performed on the property
        		if(basicProperty.getPropertySet().size()>1){
        			addActionError(getText("edit.dataEntry.transaction.error"));
                    return COMMON_FORM;
        		}
        		//Validate if collection is done for the property. If done, then do not allow modification
        		if(!activeProperty.getPtDemandSet().isEmpty()){
            		BigDecimal arrearCollection = BigDecimal.ZERO;
            		BigDecimal currentCollection = BigDecimal.ZERO;
            		Map<String, BigDecimal> demandCollectionMap = propertyTaxUtil.getDemandAndCollection(activeProperty);
            		if(!demandCollectionMap.isEmpty()){
            			arrearCollection = demandCollectionMap.get(ARR_COLL_STR);
            			currentCollection = demandCollectionMap.get(CURR_COLL_STR);
            			if(arrearCollection.compareTo(BigDecimal.ZERO) > 0 || currentCollection.compareTo(BigDecimal.ZERO) > 0){
            				addActionError(getText("edit.dataEntry.collection.done.error"));
                            return COMMON_FORM;
            			}
            		}
            	}
        	}
        	activePropertyId = basicProperty.getActiveProperty().getId().toString();
        }
        if (APPLICATION_TYPE_BIFURCATE_ASSESSENT.equals(applicationType)) {
            List<PropertyStatusValues> propertyStatusValues = propertyService.findChildrenForProperty(basicProperty);
            if (propertyStatusValues.isEmpty()) {
                addActionError(getText("error.nochild.exists.bifurcation"));
                return COMMON_FORM;
            }
        }
    

        if (APPLICATION_TYPE_REVISION_PETITION.equals(applicationType)) {
            if (isDemandActive) {
                addActionError(getText("revPetition.demandActive"));
                return COMMON_FORM;
            }
        } else if (APPLICATION_TYPE_ALTER_ASSESSENT.equals(applicationType)
                || APPLICATION_TYPE_BIFURCATE_ASSESSENT.equals(applicationType)
                || APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP.equals(applicationType)
                || APPLICATION_TYPE_GRP.equals(applicationType) || APPLICATION_TYPE_DEMOLITION.equals(applicationType)) {
            if (!isDemandActive) {
                addActionError(getText("error.msg.demandInactive"));
                return COMMON_FORM;
            }

            
        } else if (APPLICATION_TYPE_DEMAND_BILL.equals(applicationType))
            if (basicProperty.getProperty().getIsExemptedFromTax()) {
                addActionError(getText("error.msg.taxExempted"));
                return COMMON_FORM;
            }
        loggedUserIsMeesevaUser = propertyService.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser) {
            if (APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP.equals(applicationType))
                return APPLICATION_TYPE_MEESEVA_TRANSFER_OF_OWNERSHIP;
            else if(APPLICATION_TYPE_GRP.equals(applicationType))
                return APPLICATION_TYPE_MEESEVA_GRP;
            else if(APPLICATION_TYPE_REVISION_PETITION.equals(applicationType))
                return APPLICATION_TYPE_MEESEVA_RP;
        }

        if (APPLICATION_TYPE_EDIT_DEMAND.equals(applicationType)) {
        	if(!(basicProperty.getSource().toString().equalsIgnoreCase(SOURCEOFDATA_DATAENTRY.toString()))){
        		addActionError(getText("edit.dataEntry.source.error"));
                return COMMON_FORM;
        	} 
        	return APPLICATION_TYPE_EDIT_DEMAND;
        }
        if (APPLICATION_TYPE_ADD_DEMAND.equals(applicationType)) {
            if(!(basicProperty.getSource().toString().equalsIgnoreCase(SOURCEOFDATA_DATAENTRY.toString())
                    || basicProperty.getSource().toString().equalsIgnoreCase(SOURCEOFDATA_MIGRATION.toString()))){
                    addActionError(getText("add.dataEntry.source.error"));
            return COMMON_FORM;
            } 
            return APPLICATION_TYPE_ADD_DEMAND;
    }

        if (basicProperty.getProperty().getIsExemptedFromTax()
                && !(applicationType.equalsIgnoreCase(APPLICATION_TYPE_TAX_EXEMTION))
                && !applicationType.equalsIgnoreCase(APPLICATION_TYPE_MODIFY_DATA_ENTRY)) {
            addActionError(getText("action.error.msg.for.taxExempted"));
            return COMMON_FORM;
        }

        if (APPLICATION_TYPE_EDIT_OWNER.equals(applicationType)) {
            return APPLICATION_TYPE_EDIT_OWNER;
        }
        if (applicationType.equalsIgnoreCase(APPLICATION_TYPE_VACANCY_REMISSION)
                || applicationType.equalsIgnoreCase(APPLICATION_TYPE_TAX_EXEMTION)) {
            if (!isDemandActive) {
                addActionError(getText("error.msg.demandInactive"));
                return COMMON_FORM;
            } else
                mode = "commonSearch";
        }
        if (APPLICATION_TYPE_EDIT_COLLECTION.equals(applicationType)) {
            if (!basicProperty.isEligible()) {
                addActionError(getText("error.msg.editCollection.noteligible"));
                return COMMON_FORM;
            } else {
                return APPLICATION_TYPE_EDIT_COLLECTION;
            }
        }
        return applicationType;

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
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("srchByAssessment : BasicProperty : " + basicProperty);
            if (basicProperty != null) {
                setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
                checkIsMarkForDeactive(basicProperty);
            }
            if (assessmentNum != null && !assessmentNum.equals(""))
                setSearchValue("Assessment Number : " + assessmentNum);
            setSearchUri("../search/searchProperty-srchByAssessment.action");
            setSearchCriteria("Search By Assessment number");
            setSearchValue("Assessment number :" + assessmentNum);
        } catch (final IndexOutOfBoundsException iob) {
            final String msg = "Rollover is not done for " + assessmentNum;
            throw new ValidationException(Arrays.asList(new ValidationError(msg, msg)));
        } catch (final Exception e) {
            LOGGER.error("Exception in Search Property By Assessment ", e);
            throw new ApplicationRuntimeException("Exception : ", e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByAssessment method ");
        return TARGET;
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByOldMuncipalNumber")
    public String srcByOldAssesementNum() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into srcByOldAssesementNum  method. Old Assessment Number : " + oldMuncipalNum);
        if (oldMuncipalNum!=null)
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByOldMunicipalNo(oldMuncipalNum);
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByBndry : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                if (oldMuncipalNum != null && !oldMuncipalNum.equals(""))
                    setSearchValue("Assessment Number : " + oldMuncipalNum);
                setSearchUri("../search/searchProperty-srchByOldMuncipalNumber.action");
                setSearchCriteria("Search By Old Muncipal Number");
                setSearchValue("Old Muncipal number :" + oldMuncipalNum);

            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By OldMuncipal Number ", e);
                throw new ApplicationRuntimeException("Exception : ", e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srcByOldAssesementNum method ");
        return TARGET;
        
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByDoorNo")
    public String srchByDoorNo() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into srchByDoorNo  method. Door No : " + doorNo);
        if (null != doorNo)
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByDoorNo(doorNo);
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByBndry : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                if (assessmentNum != null && !assessmentNum.equals(""))
                    setSearchValue("Assessment Number : " + assessmentNum);
                setSearchUri("../search/searchProperty-srchByDoorNo.action");
                setSearchCriteria("Search By Door Number");
                setSearchValue("Door number :" + doorNo);

            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Door number ", e);
                throw new ApplicationRuntimeException("Exception : ", e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByDoorNo method ");
        return TARGET;
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByMobileNumber")
    public String srchByMobileNumber() {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Entered into srchByMobileNumber  method. Mobile No : " + mobileNumber);
        if (StringUtils.isNotBlank(mobileNumber))
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService
                        .getPropertyByMobileNumber(mobileNumber);
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByBndry : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                if (mobileNumber != null && !mobileNumber.equals(""))
                    setSearchValue("Mobile Number : " + mobileNumber);
                setSearchUri("../search/searchProperty-srchByMobileNumber.action");
                setSearchCriteria("Search By Mobile Number");
                setSearchValue("Mobile number :" + mobileNumber);

            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By MobileNumber number ", e);
                throw new ApplicationRuntimeException("Exception : ", e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByMobileNumber method ");
        return TARGET;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on Boundary : zone and ward
     */
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByBndry")
    public String srchByBndry() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into srchByBndry method");
            LOGGER.debug("srchByBndry : Zone Id : " + zoneId + ", " + "ward Id : " + wardId + ", " + "House Num : "
                    + houseNumBndry + ", " + "Owner Name : " + ownerNameBndry);
        }
        String strZoneNum = "";
        String strWardNum = "";
        if (null != zoneId && zoneId != -1)
            strZoneNum = boundaryService.getBoundaryById(zoneId).getName();
        if (null != wardId && wardId != -1)
            strWardNum = boundaryService.getBoundaryById(wardId).getName();

        if (zoneId != null && zoneId != -1 || wardId != null && wardId != -1)
            try {

                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByBoundary(zoneId, wardId,
                        ownerNameBndry, houseNumBndry);

                if (!propertyList.isEmpty()
                        && propertyList.size() > Integer.parseInt(PropertyTaxConstants.SEARCH_RESULT_COUNT)) {
                    throw new ValidationException(Arrays.asList(new ValidationError("resultCountValidation",
                            getText("search.validate.resultcountexceed500"))));
                }
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByBndry : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                setSearchUri("../search/searchProperty-srchByBndry.action");
                setSearchCriteria("Search By Zone, Ward, Plot No/House No, Owner Name");
                setSearchValue("Zone Num: " + strZoneNum + ", Ward Num: " + strWardNum + ", Plot No/House No: "
                        + houseNumBndry + ", Owner Name: " + ownerNameBndry);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Bndry ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByBndry method");
        return TARGET;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on location boundary
     */
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-srchByLocation")
    public String srchByLocation() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into srchByArea  method");
            LOGGER.debug("srchByLocation : Location Id : " + locationId + ", " + "Owner Name : " + ownerName + ", "
                    + "Plot No/House No : " + houseNumArea);
        }
        final String strLocationNum = boundaryService.getBoundaryById(locationId.longValue()).getName();
        if (null != ownerName && org.apache.commons.lang.StringUtils.isNotEmpty(ownerName) && locationId != null
                && locationId != -1)
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByLocation(locationId,
                        houseNumArea, ownerName);
                if (!propertyList.isEmpty()
                        && propertyList.size() > Integer.parseInt(PropertyTaxConstants.SEARCH_RESULT_COUNT)) {
                    ValidationError vr = new ValidationError("search.validate.resultcountexceed500",
                            "search.validate.resultcountexceed500");
                    throw new ValidationException(Arrays.asList(vr));
                }
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByLocation : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                setSearchUri("../search/searchProperty-srchByLocation.action");
                setSearchCriteria("Search By Location, Owner Name");
                setSearchValue("Location : " + strLocationNum + ", Owner Name : " + ownerName);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Bndry ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByArea  method");
        return TARGET;
    }

    /**
     * @return to official search property result screen
     * @description searches property based on Demand
     */
    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-searchByDemand")
    public String searchByDemand() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into searchByDemand  method");
            LOGGER.debug("From Demand No : " + fromDemand + ", " + "To Demand No : " + toDemand);
        }
        if (fromDemand != null && fromDemand != "" && toDemand != null && toDemand != "")
            try {
                final List<PropertyMaterlizeView> propertyList = propertyService.getPropertyByDemand(fromDemand,
                        toDemand);
                if (!propertyList.isEmpty()
                        && propertyList.size() > Integer.parseInt(PropertyTaxConstants.SEARCH_RESULT_COUNT)) {
                    ValidationError vr = new ValidationError("search.validate.resultcountexceed500",
                            "search.validate.resultcountexceed500");
                    throw new ValidationException(Arrays.asList(vr));
                }
                for (final PropertyMaterlizeView propMatview : propertyList) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("searchByDemand : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                setSearchUri("../search/searchProperty-searchByDemand.action");
                setSearchCriteria("Search By FromDemand, ToDemand");
                setSearchValue("From Demand: " + fromDemand + ", To Demand: " + toDemand);
            } catch (final ValidationException e) {
                throw new ValidationException(e.getErrors());
            } catch (final Exception e) {
                LOGGER.error("Exception in Search Property By Bndry ", e);
                throw new ApplicationRuntimeException("Exception : " + e);
            }
        return TARGET;
    }

    /*
     * (non-Javadoc)
     * @see org.egov.infra.web.struts.actions.BaseFormAction#prepare()
     */
    @Override
    public void prepare() {
        final List<Boundary> zoneList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Zone",
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> wardList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName("Ward",
                REVENUE_HIERARCHY_TYPE);
        final List<Boundary> locationList = boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                "Locality", LOCATION_HIERARCHY_TYPE);

        setZoneBndryMap(CommonServices.getFormattedBndryMap(zoneList));
        setWardndryMap(CommonServices.getFormattedBndryMap(wardList));
        prepareWardDropDownData(zoneId != null, wardId != null);
        addDropdownData("Location", locationList);
        addDropdownData("PropTypeMaster",
                getPersistenceService().findAllByNamedQuery(PropertyTaxConstants.GET_PROPERTY_TYPES));
        final Long userId = (Long) session().get(SESSIONLOGINID);
        if (userId != null)
            setRoleName(propertyTaxUtil.getRolesForUserId(userId));

        if (StringUtils.isNotBlank(assessmentNum)) {
            basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNum);
        }
    }

    /**
     * @Description Loads ward drop down for selected zone
     * @param zoneExists
     * @param wardExists
     */
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
        if (StringUtils.equals(mode, "assessment")) {
            if (org.apache.commons.lang.StringUtils.isEmpty(assessmentNum)
                    || org.apache.commons.lang.StringUtils.isBlank(assessmentNum))
                addActionError(getText("mandatory.assessmentNo"));
        } else if (StringUtils.equals(mode, "oldAssessment")) {
            if (org.apache.commons.lang.StringUtils.isEmpty(oldMuncipalNum)
                    || org.apache.commons.lang.StringUtils.isBlank(oldMuncipalNum))
                addActionError(getText("mandatory.oldMuncipleNum"));
        } else if (StringUtils.equals(mode, "bndry")) {
            if ((zoneId == null || zoneId == -1) && (wardId == null || wardId == -1))
                addActionError(getText("mandatory.zoneorward"));
        } else if (StringUtils.equals(mode, "location")) {
            if (locationId == null || locationId == -1)
                addActionError(getText("mandatory.location"));
            if (ownerName == null || StringUtils.isEmpty(ownerName))
                addActionError(getText("search.ownerName.null"));
        } else if (StringUtils.equals(mode, "demand")) {
            if (fromDemand == null || StringUtils.isEmpty(fromDemand))
                addActionError(getText("mandatory.fromdemand"));
            if (toDemand == null || StringUtils.isEmpty(toDemand))
                addActionError(getText("mandatory.todemand"));
        } else if (StringUtils.equals(mode, "doorNo")) {
            if (StringUtils.isBlank(doorNo))
                addActionError(getText("mandatory.doorNo"));
        } else if (StringUtils.equals(mode, "mobileNo")) {
            if (StringUtils.isBlank(mobileNumber))
                addActionError(getText("mandatory.MobileNumber"));
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
                searchResultMap.put("isDemandActive", String.valueOf(isDemandActive));
                searchResultMap.put("propType", property.getPropertyDetail().getPropertyTypeMaster().getCode());
                searchResultMap.put("isTaxExempted", String.valueOf(property.getIsExemptedFromTax()));
                searchResultMap.put("isUnderWorkflow", String.valueOf(basicProperty.isUnderWorkflow()));
                searchResultMap.put("enableVacancyRemission",
                        String.valueOf(propertyTaxUtil.enableVacancyRemission(basicProperty.getUpicNo())));
                searchResultMap.put("enableMonthlyUpdate",
                        String.valueOf(propertyTaxUtil.enableMonthlyUpdate(basicProperty.getUpicNo())));
                searchResultMap.put("enableVRApproval",
                        String.valueOf(propertyTaxUtil.enableVRApproval(basicProperty.getUpicNo())));
                if (!property.getIsExemptedFromTax()) {
                    searchResultMap.put("currFirstHalfDemand", demandCollMap.get(CURR_FIRSTHALF_DMD_STR).toString());
                    searchResultMap.put("currSecondHalfDemand", demandCollMap.get(CURR_SECONDHALF_DMD_STR).toString());
                    searchResultMap.put("arrDemandDue",
                            demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)).toString());
                    searchResultMap.put(
                            "currFirstHalfDemandDue",
                            demandCollMap.get(CURR_FIRSTHALF_DMD_STR)
                                    .subtract(demandCollMap.get(CURR_FIRSTHALF_COLL_STR)).toString());
                    searchResultMap.put(
                            "currSecondHalfDemandDue",
                            demandCollMap.get(CURR_SECONDHALF_DMD_STR)
                                    .subtract(demandCollMap.get(CURR_SECONDHALF_COLL_STR)).toString());
                } else {
                    searchResultMap.put("currFirstHalfDemand", "0");
                    searchResultMap.put("currFirstHalfDemandDue", "0");
                    searchResultMap.put("currSecondHalfDemand", "0");
                    searchResultMap.put("currSecondHalfDemandDue", "0");
                    searchResultMap.put("arrDemandDue", "0");
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Assessment Number : " + searchResultMap.get("assessmentNum") + ", " + "Owner Name : "
                            + searchResultMap.get("ownerName") + ", " + "Parcel id : "
                            + searchResultMap.get("parcelId") + ", " + "Address : " + searchResultMap.get("address")
                            + ", " + "Current Demand : " + searchResultMap.get("currDemand") + ", "
                            + "Arrears Demand Due : " + searchResultMap.get("arrDemandDue") + ", "
                            + "Current Demand Due : " + searchResultMap.get("currDemandDue"));
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
        BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(pmv.getPropertyId());
        Property property = basicProperty.getProperty();
        if (basicProperty != null) {
            checkIsDemandActive(basicProperty.getProperty());
        }
        if (pmv.getPropertyId() != null || org.apache.commons.lang.StringUtils.isNotEmpty(pmv.getPropertyId()))
            if (pmv != null) {
                final Map<String, String> searchResultMap = new HashMap<String, String>();
                searchResultMap.put("assessmentNum", pmv.getPropertyId());
                searchResultMap.put("ownerName", pmv.getOwnerName());
                searchResultMap.put("parcelId", pmv.getGisRefNo());
                searchResultMap.put("address", pmv.getPropertyAddress());
                searchResultMap.put("source", pmv.getSource().toString());
                searchResultMap.put("isDemandActive", String.valueOf(isDemandActive));
                searchResultMap.put("propType", property.getPropertyDetail().getPropertyTypeMaster().getCode());
                searchResultMap.put("isTaxExempted", String.valueOf(property.getIsExemptedFromTax()));
                searchResultMap.put("isUnderWorkflow", String.valueOf(basicProperty.isUnderWorkflow()));
                searchResultMap.put("enableVacancyRemission",
                        String.valueOf(propertyTaxUtil.enableVacancyRemission(basicProperty.getUpicNo())));
                searchResultMap.put("enableMonthlyUpdate",
                        String.valueOf(propertyTaxUtil.enableMonthlyUpdate(basicProperty.getUpicNo())));
                searchResultMap.put("enableVRApproval",
                        String.valueOf(propertyTaxUtil.enableVRApproval(basicProperty.getUpicNo())));
                if (pmv.getIsExempted()) {
                    searchResultMap.put("currFirstHalfDemand", "0");
                    searchResultMap.put("currFirstHalfDemandDue", "0");
                    searchResultMap.put("currSecondHalfDemand", "0");
                    searchResultMap.put("currSecondHalfDemandDue", "0");
                    searchResultMap.put("arrDemandDue", "0");
                } else {
                    searchResultMap.put("currFirstHalfDemand",
                            pmv.getAggrCurrFirstHalfDmd() == null ? "0" : pmv.getAggrCurrFirstHalfDmd().toString());
                    searchResultMap.put("currFirstHalfDemandDue",
                            (pmv.getAggrCurrFirstHalfDmd() == null ? BigDecimal.ZERO : pmv.getAggrCurrFirstHalfDmd())
                                    .subtract(pmv.getAggrCurrFirstHalfColl() == null ? BigDecimal.ZERO
                                            : pmv.getAggrCurrFirstHalfColl())
                                    .toString());
                    searchResultMap.put("currSecondHalfDemand",
                            pmv.getAggrCurrSecondHalfDmd() == null ? "0" : pmv.getAggrCurrSecondHalfDmd().toString());
                    searchResultMap.put("currSecondHalfDemandDue",
                            (pmv.getAggrCurrSecondHalfDmd() == null ? BigDecimal.ZERO : pmv.getAggrCurrSecondHalfDmd())
                                    .subtract(pmv.getAggrCurrSecondHalfColl() == null ? BigDecimal.ZERO
                                            : pmv.getAggrCurrSecondHalfColl())
                                    .toString());
                    searchResultMap.put("arrDemandDue",
                            (pmv.getAggrArrDmd() == null ? BigDecimal.ZERO : pmv.getAggrArrDmd())
                                    .subtract(pmv.getAggrArrColl() == null ? BigDecimal.ZERO : pmv.getAggrArrColl())
                                    .toString());
                }
                searchList.add(searchResultMap);
            }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
            LOGGER.debug("Exit from getSearchResults method");
        }
        return searchList;
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/search/searchProperty-searchOwnerDetails")
    public String searchOwnerDetails() {
        if (basicProperty == null) {
            addActionError(getText("validation.property.doesnot.exists"));
            return NEW;
        } else {
            setPropertyOwner(basicProperty.getPrimaryOwner());
            setMobileNumber(getPropertyOwner().getMobileNumber());
            if (StringUtils.isBlank(propertyOwner.getMobileNumber())) {
                propertyOwner.setMobileNumber("N/A");
            }
            for (final PropertyOwnerInfo propOwner : basicProperty.getPropertyOwnerInfo()) {
                final List<Address> addrSet = propOwner.getOwner().getAddress();
                for (final Address address : addrSet) {
                    setDoorNo(address.getHouseNoBldgApt() == null ? NOT_AVAILABLE : address.getHouseNoBldgApt());
                    break;
                }
            }
            return USER_DETAILS;
        }
    }

    @Action(value = "/search/searchProperty-updateMobileNo")
    public String updateMobileNo() {
        if (null != basicProperty) {
            setPropertyOwner(basicProperty.getPrimaryOwner());
        }
        if (StringUtils.isNotBlank(mobileNumber)) {
            propertyOwner.setMobileNumber(mobileNumber);
            userService.updateUser(propertyOwner);
        }
        return UPDATEMOBILE_FORM;
    }
    
    @Action(value = "/search/searchproperty-alter-assessment")
    public String alterAssessment() {
        setApplicationType(APPLICATION_TYPE_ALTER_ASSESSENT);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-bifurcation")
    public String bifurcation() {
        setApplicationType(APPLICATION_TYPE_BIFURCATE_ASSESSENT);
        return commonForm();
    }

    @Action(value = "/search/searchproperty-taxexemption")
    public String taxExemption() {
        setApplicationType(APPLICATION_TYPE_TAX_EXEMTION);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-editcollection")
    public String editCollection() {
        setApplicationType(APPLICATION_TYPE_EDIT_COLLECTION);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-editdemand")
    public String editDemand() {
        setApplicationType(APPLICATION_TYPE_EDIT_DEMAND);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-transferownership")
    public String transferOwnership() {
        setApplicationType(APPLICATION_TYPE_TRANSFER_OF_OWNERSHIP);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-collecttax")
    public String collectTax() {
        setApplicationType(APPLICATION_TYPE_COLLECT_TAX);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-revisionpetition")
    public String revisionPetition() {
        setApplicationType(APPLICATION_TYPE_REVISION_PETITION);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-general-revisionpetition")
    public String generalRevisionPetition() {
        setApplicationType(APPLICATION_TYPE_GRP);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-demolition")
    public String demolition() {
        setApplicationType(APPLICATION_TYPE_DEMOLITION);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-vacancyremission")
    public String vacancyRemission() {
        setApplicationType(APPLICATION_TYPE_VACANCY_REMISSION);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-generatedemandbill")
    public String generateDemandBill() {
        setApplicationType(APPLICATION_TYPE_DEMAND_BILL);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-editdataentry")
    public String editDataEntry() {
        setApplicationType(APPLICATION_TYPE_MODIFY_DATA_ENTRY);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-editownerdetails")
    public String editOwnerDetails() {
        setApplicationType(APPLICATION_TYPE_EDIT_OWNER);
        return commonForm();
    }
    
    @Action(value = "/search/searchproperty-adddemand")
    public String AddDemand() {
        setApplicationType(APPLICATION_TYPE_ADD_DEMAND);
        return commonForm();
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

    public String getOldMuncipalNum() {
        return oldMuncipalNum;
    }

    public void setOldMuncipalNum(final String oldMuncipalNum) {
        this.oldMuncipalNum = oldMuncipalNum;
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

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
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

    public Map<Long, String> getWardndryMap() {
        return WardndryMap;
    }

    public void setWardndryMap(Map<Long, String> wardndryMap) {
        WardndryMap = wardndryMap;
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

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(final String applicationType) {
        this.applicationType = applicationType;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMeesevaApplicationNumber() {
        return meesevaApplicationNumber;
    }

    public void setMeesevaApplicationNumber(String meesevaApplicationNumber) {
        this.meesevaApplicationNumber = meesevaApplicationNumber;
    }

    public String getMeesevaServiceCode() {
        return meesevaServiceCode;
    }

    public void setMeesevaServiceCode(String meesevaServiceCode) {
        this.meesevaServiceCode = meesevaServiceCode;
    }

    public boolean getIsNagarPanchayat() {
        return propertyTaxUtil.checkIsNagarPanchayat();
    }

    public User getPropertyOwner() {
        return propertyOwner;
    }

    public void setPropertyOwner(User propertyOwner) {
        this.propertyOwner = propertyOwner;
    }

    public BasicProperty getBasicProperty() {
        return basicProperty;
    }

    public void setBasicProperty(BasicProperty basicProperty) {
        this.basicProperty = basicProperty;
    }

    public String getActivePropertyId() {
        return activePropertyId;
    }

    public void setActivePropertyId(String activePropertyId) {
        this.activePropertyId = activePropertyId;
    }
}
