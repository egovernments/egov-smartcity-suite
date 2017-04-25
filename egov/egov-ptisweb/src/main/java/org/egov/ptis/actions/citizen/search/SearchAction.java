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
package org.egov.ptis.actions.citizen.search;

import static java.math.BigDecimal.ZERO;
import static org.egov.infra.web.struts.actions.BaseFormAction.NEW;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
@Results({ @Result(name = NEW, location = "search-new.jsp"),
        @Result(name = SearchAction.TARGET, location = "search-result.jsp"),
        @Result(name = SearchAction.NEWFORM, location = "onlinesearch-new.jsp"),
        @Result(name = SearchAction.TARGETFORM, type = "redirectAction", location = "viewDCBProperty-displayPropInfo", params = {
                "namespace", "/view", "propertyId", "${assessmentNum}", "searchUrl", "${searchUrl}" }) })
public class SearchAction extends BaseFormAction implements ServletRequestAware {
    private static final long serialVersionUID = -7506891911359323204L;

    private final Logger LOGGER = Logger.getLogger(getClass());

    private String assessmentNum;
    private String oldMuncipalNum;
    private String doorNo;
    private String ownerName;
    private String mode;
    private List<Map<String, String>> searchResultList;
    private HttpServletRequest request;
    private String searchUri;
    private String searchCreteria;
    private String searchValue;
    private String searchUrl;
    private boolean isDemandActive;
    
    List<Map<String, String>> searchList = new ArrayList<>();
    public static final String TARGET = "result";
    public static final String NEWFORM = "newForm";
    public static final String TARGETFORM = "targetForm";

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Override
    public Object getModel() {
        return null;
    }

    /**
     * @return to citizen search property screen
     */
    @SkipValidation
    @Action(value = "/citizen/search/search-searchForm")
    public String searchForm() {
        setAssessmentNum("");
        return NEW;
    }

    /**
     * @return to citizen search property result screen
     */

    @ValidationErrorPage(value = "new")
    @Action(value = "/citizen/search/search-srchByAssessmentAndOwnerDetail")
    public String srchByAssessmentAndOwnerDetail() {
        try {
            final List<PropertyMaterlizeView> propertyList = propertyService
                    .getPropertyByAssessmentAndOwnerDetails(assessmentNum, oldMuncipalNum, ownerName, doorNo);
            for (final PropertyMaterlizeView propMatview : propertyList) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("srchByAssessmentAndOwner : Property : " + propMatview);
                setSearchResultList(getResultsFromMv(propMatview));
            }
            if (assessmentNum != null && !assessmentNum.equals(""))
                setSearchValue("Assessment Number : " + assessmentNum);

            setSearchCreteria("Search By Assessment and Owner detail");
            if (oldMuncipalNum != null && !oldMuncipalNum.equals(""))
                setSearchValue("Old Assesement Number:" + oldMuncipalNum);
            if (ownerName != null && !ownerName.equals(""))
                setSearchValue("Owner name :" + ownerName);
            if (doorNo != null && !doorNo.equals(""))
                setSearchValue("Door number :" + doorNo);

        } catch (final Exception e) {
            LOGGER.warn("Exception in Search Property By Door number ", e);
            throw new ApplicationRuntimeException("Exception : ", e);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByAssessmentAndOwenrDetails method ");
        return TARGET;
    }

    @SkipValidation
    @Action(value = "/citizen/search/search-searchByAssessmentForm")
    public String searchByAssessmentForm() {
        setAssessmentNum("");
        return NEWFORM;
    }

    @ValidationErrorPage(value = "new")
    @Action(value = "/citizen/search/search-searchByAssessment")
    public String srchByAssessmentNo() {
        try {

            BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNum);
            if (basicProperty != null) {
                setSearchUrl("onlineSearch");
                        checkIsDemandActive(basicProperty.getProperty());
                if (isDemandActive == false) {
                    addActionError(getText("dmd.inactive"));
                    return NEWFORM;
                }
            } else {
                addActionError(getText("record.not.found"));
                return NEWFORM;
            }
        } catch (final Exception e) {
            LOGGER.warn("Exception in Search Property By Door number ", e);
            throw new ApplicationRuntimeException("Exception : ", e);
        }

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from srchByAssessment method ");

        return TARGETFORM;
    }

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

    public List<Map<String, String>> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(final List<Map<String, String>> searchResultList) {
        this.searchResultList = searchResultList;
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

    @Override
    @SkipValidation
    public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }

    public String getAssessmentNum() {
        return assessmentNum;
    }

    public void setAssessmentNum(final String assessmentNum) {
        this.assessmentNum = assessmentNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(final String doorNo) {
        this.doorNo = doorNo;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getOldMuncipalNum() {
        return oldMuncipalNum;
    }

    public void setOldMuncipalNum(final String oldMuncipalNum) {
        this.oldMuncipalNum = oldMuncipalNum;
    }

}
