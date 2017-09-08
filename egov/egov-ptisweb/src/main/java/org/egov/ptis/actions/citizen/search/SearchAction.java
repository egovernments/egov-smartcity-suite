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
import static org.egov.ptis.constants.PropertyTaxConstants.ADVANCE_DMD_RSN_CODE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_PENALTY_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_PENALTY_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_PENALTY_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_PENALTY_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_PENALTY_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_PENALTY_DMD_STR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.property.RebateService;
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
public class SearchAction extends SearchFormAction implements ServletRequestAware {
    private static final long serialVersionUID = -7506891911359323204L;

    private final Logger LOGGER = Logger.getLogger(getClass());

    protected static final String TARGET = "result";
    protected static final String NEWFORM = "newForm";
    protected static final String TARGETFORM = "targetForm";
    private static final String CURRENT_FIRST_HALF_DEMAND = "currFirstHalfDemand";
    private static final String CURRENT_SECOND_HALF_DEMAND = "currSecondHalfDemand";
    private static final String ARREAR_DEMAND_DUE = "arrDemandDue";
    private static final String CURRENT_FIRST_HALF_DEMAND_DUE = "currFirstHalfDemandDue";
    private static final String CURRENT_SECOND_HALF_DEMAND_DUE = "currSecondHalfDemandDue";
    private static final String CURRENT_FIRST_HALF_PENALTY_DUE = "currFirstHalfPenaltyDue";
    private static final String CURRENT_SECOND_HALF_PENALTY_DUE = "currSecondHalfPenaltyDue";
    private static final String ARREAR_PENALTY_DUE = "arrearPenaltyDue";
    private static final String ADVANCE = "advance";
    private static final String REBATE_AMOUNT = "rebateAmt";
    private static final String NET_PAYABLE_AMOUNT = "netPayAmt";

    private String assessmentNum;
    private String oldMuncipalNum;
    private String doorNo;
    private String ownerName;
    private String mode;
    private List<Map<String, String>> searchResultList;
    private String searchUri;
    private String searchCreteria;
    private String searchValue;
    private String searchUrl;
    private boolean isDemandActive;
    private Map<String, Object> queryMap;
    List<Map<String, String>> searchList = new ArrayList<>();

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private RebateService rebateService;

    @Autowired
    private PtDemandDao ptDemandDAO;

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
            if (assessmentNum != null && !"".equals(assessmentNum)) {
                setQueryMap(propertyService.getAssessmentNumQuery(assessmentNum));
                super.search();
                for (final BasicProperty basicProperty : (List<BasicProperty>) searchResult.getList()) {
                    setSearchResultList(getResultFromDemandDetails(basicProperty));
                }
                ((EgovPaginatedList) searchResult).setList(searchList);

                setSearchValue("Assessment Num : " + assessmentNum);
            } else {
                setQueryMap(propertyService.getAssessmentAndOwnerDetailsQuery(oldMuncipalNum, ownerName, doorNo));
                super.search();
                for (final PropertyMaterlizeView propMatview : (List<PropertyMaterlizeView>) searchResult.getList()) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("srchByAssessmentAndOwner : Property : " + propMatview);
                    setSearchResultList(getResultsFromMv(propMatview));
                }
                ((EgovPaginatedList) searchResult).setList(searchList);

                if (oldMuncipalNum != null && !oldMuncipalNum.equals(""))
                    setSearchValue("Old Assesement Number:" + oldMuncipalNum);
                if (ownerName != null && !ownerName.equals(""))
                    setSearchValue("Owner name :" + ownerName);
                if (doorNo != null && !doorNo.equals(""))
                    setSearchValue("Door number :" + doorNo);
            }
            setSearchCreteria("Search By Assessment and Owner detail");

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

            final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(assessmentNum);
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


    @Override
    public SearchQuery prepareQuery(String sortField, String sortOrder) {
        return new SearchQueryHQL((String) queryMap.get("search"), (String) queryMap.get("count"), (List<Object>) queryMap.get("params"));
    }

    private List<Map<String, String>> getResultFromDemandDetails(final BasicProperty basicProperty) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into getSearchResults method");
            LOGGER.debug("Assessment Number : " + basicProperty.getUpicNo());
        }
        final Property property = basicProperty.getProperty();
        final Map<String, BigDecimal> demandCollMap = ptDemandDAO.getDemandCollMapIncludingPenaltyAndAdvance(property);

        final Map<String, String> searchResultMap = new HashMap<String, String>();
        searchResultMap.put("assessmentNum", basicProperty.getUpicNo());
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
        if (property.getIsExemptedFromTax())
            searchResultMap.putAll(getDemandDetailsForExemptedProperty());
        else {
            searchResultMap.put(CURRENT_FIRST_HALF_DEMAND, demandCollMap.get(CURR_FIRSTHALF_DMD_STR).toString());
            searchResultMap.put(CURRENT_SECOND_HALF_DEMAND, demandCollMap.get(CURR_SECONDHALF_DMD_STR).toString());
            searchResultMap.put(ARREAR_DEMAND_DUE,
                    demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)).toString());
            searchResultMap.put(CURRENT_FIRST_HALF_DEMAND_DUE, demandCollMap.get(CURR_FIRSTHALF_DMD_STR)
                    .subtract(demandCollMap.get(CURR_FIRSTHALF_COLL_STR)).toString());
            searchResultMap.put(CURRENT_SECOND_HALF_DEMAND_DUE, demandCollMap.get(CURR_SECONDHALF_DMD_STR)
                    .subtract(demandCollMap.get(CURR_SECONDHALF_COLL_STR)).toString());
            searchResultMap.put(CURRENT_FIRST_HALF_PENALTY_DUE, demandCollMap.get(CURR_FIRSTHALF_PENALTY_DMD_STR)
                    .subtract(demandCollMap.get(CURR_FIRSTHALF_PENALTY_COLL_STR)).toString());
            searchResultMap.put(CURRENT_SECOND_HALF_PENALTY_DUE, demandCollMap.get(CURR_SECONDHALF_PENALTY_DMD_STR)
                    .subtract(demandCollMap.get(CURR_SECONDHALF_PENALTY_COLL_STR)).toString());
            searchResultMap.put(ARREAR_PENALTY_DUE, demandCollMap.get(ARR_PENALTY_DMD_STR)
                    .subtract(demandCollMap.get(ARR_PENALTY_COLL_STR)).toString());
            searchResultMap.put(ADVANCE, demandCollMap.get(ADVANCE_DMD_RSN_CODE).toString());
            BigDecimal rebate = calculateRebate(basicProperty);
            searchResultMap.put(REBATE_AMOUNT, rebate.toString());
            searchResultMap.put(NET_PAYABLE_AMOUNT, calculateNetPayAmt(demandCollMap).subtract(rebate).toString());

        }
        searchList.add(searchResultMap);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
            LOGGER.debug("Exit from getSearchResults method");
        }

        return searchList;
    }

    public Map<String, String> getDemandDetailsForExemptedProperty() {
        final Map<String, String> searchMap = new HashMap<String, String>();
        searchMap.put(CURRENT_FIRST_HALF_DEMAND, "0");
        searchMap.put(CURRENT_SECOND_HALF_DEMAND, "0");
        searchMap.put(CURRENT_FIRST_HALF_DEMAND_DUE, "0");
        searchMap.put(CURRENT_SECOND_HALF_DEMAND_DUE, "0");
        searchMap.put(ARREAR_DEMAND_DUE, "0");
        searchMap.put(CURRENT_FIRST_HALF_PENALTY_DUE, "0");
        searchMap.put(CURRENT_SECOND_HALF_PENALTY_DUE, "0");
        searchMap.put(ARREAR_PENALTY_DUE, "0");
        searchMap.put(ADVANCE, "0");
        searchMap.put(NET_PAYABLE_AMOUNT, "0");
        return searchMap;
    }

    public BigDecimal calculateNetPayAmt(final Map<String, BigDecimal> demandCollMap) {
        return demandCollMap.get(CURR_FIRSTHALF_DMD_STR).subtract(demandCollMap.get(CURR_FIRSTHALF_COLL_STR))
                .add(demandCollMap.get(CURR_SECONDHALF_DMD_STR).subtract(demandCollMap.get(CURR_SECONDHALF_COLL_STR)))
                .add(demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap.get(ARR_COLL_STR)))
                .add(demandCollMap.get(CURR_FIRSTHALF_PENALTY_DMD_STR)
                        .subtract(demandCollMap.get(CURR_FIRSTHALF_PENALTY_COLL_STR)))
                .add(demandCollMap.get(CURR_SECONDHALF_PENALTY_DMD_STR)
                        .subtract(demandCollMap.get(CURR_SECONDHALF_PENALTY_COLL_STR)))
                .add(demandCollMap.get(ARR_PENALTY_DMD_STR).subtract(demandCollMap.get(ARR_PENALTY_COLL_STR)));
    }

    private List<Map<String, String>> getResultsFromMv(final PropertyMaterlizeView pmv) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into getSearchResults method");
            LOGGER.debug("Assessment Number : " + pmv.getPropertyId());
        }
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(pmv.getPropertyId());
        final Property property = basicProperty.getProperty();
        if (basicProperty != null)
            checkIsDemandActive(basicProperty.getProperty());
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
                    searchResultMap.putAll(getDemandDetailsForExemptedProperty());
                } else {
                    searchResultMap.put(CURRENT_FIRST_HALF_DEMAND,
                            getCurrFirstHalfDemand(pmv.getAggrCurrFirstHalfDmd()).toString());
                    searchResultMap.put(CURRENT_FIRST_HALF_DEMAND_DUE,
                            getCurrFirstHalfDemandDue(pmv.getAggrCurrFirstHalfDmd(), pmv.getAggrCurrFirstHalfColl())
                                    .toString());
                    searchResultMap.put(CURRENT_FIRST_HALF_PENALTY_DUE,
                            getIntrestDueOnCurrFirstHalfDemand(pmv.getAggrCurrFirstHalfPenaly(),
                                    pmv.getAggrCurrFirstHalfPenalyColl()).toString());
                    searchResultMap.put(CURRENT_SECOND_HALF_DEMAND, getCurrSecondHalfDemand(pmv.getAggrCurrSecondHalfDmd())
                            .toString());
                    searchResultMap.put(CURRENT_SECOND_HALF_DEMAND_DUE,
                            getCurrSecondHalfDemandDue(pmv.getAggrCurrSecondHalfDmd(), pmv.getAggrCurrSecondHalfColl())
                                    .toString());
                    searchResultMap.put(CURRENT_SECOND_HALF_PENALTY_DUE,
                            getIntrestDueOnCurrSecondHalfDemand(pmv.getAggrCurrSecondHalfPenaly(),
                                    pmv.getAggrCurrSecondHalfPenalyColl())
                                            .toString());
                    searchResultMap.put(ARREAR_DEMAND_DUE, getAggrArrDmdDue(pmv.getAggrArrDmd(), pmv.getAggrArrColl())
                            .toString());
                    searchResultMap.put(ARREAR_PENALTY_DUE,
                            getIntrestDueOnArrearDemandDue(pmv.getAggrArrearPenaly(), pmv.getAggrArrearPenalyColl())
                                    .toString());
                    searchResultMap.put(ADVANCE, pmv.getAdvance().toString());
                    BigDecimal rebate = calculateRebate(basicProperty);
                    searchResultMap.put(REBATE_AMOUNT, rebate.toString());
                    searchResultMap.put(NET_PAYABLE_AMOUNT, calculateNetPayableAmmount(pmv).subtract(rebate).toString());
                }
                searchList.add(searchResultMap);
            }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
            LOGGER.debug("Exit from getSearchResults method");
        }
        return searchList;
    }

    public BigDecimal calculateRebate(BasicProperty basicProperty) {
        final BigDecimal tax = propertyTaxUtil.getCurrentDemandForRebateCalculation(basicProperty);
        return rebateService.calculateEarlyPayRebate(tax, new Date());
    }

    public BigDecimal getCurrFirstHalfDemand(final BigDecimal aggrCurrFirstHalfDmd) {
        return aggrCurrFirstHalfDmd == null ? ZERO : aggrCurrFirstHalfDmd;
    }

    public BigDecimal getCurrFirstHalfDemandDue(final BigDecimal currFirstHalfDemand, final BigDecimal aggrCurrFirstHalfColl) {
        return (currFirstHalfDemand == null ? ZERO : currFirstHalfDemand)
                .subtract(aggrCurrFirstHalfColl == null ? ZERO
                        : aggrCurrFirstHalfColl);
    }

    public BigDecimal getIntrestDueOnCurrFirstHalfDemand(final BigDecimal intrestDueOnCurrFirstHalfDemand,
            final BigDecimal aggrCurrFirstHalfPenalyColl) {
        return (intrestDueOnCurrFirstHalfDemand == null ? ZERO : intrestDueOnCurrFirstHalfDemand)
                .subtract(aggrCurrFirstHalfPenalyColl == null ? ZERO : aggrCurrFirstHalfPenalyColl);
    }

    public BigDecimal getCurrSecondHalfDemand(final BigDecimal aggrCurrFirstHalfDmd) {
        return aggrCurrFirstHalfDmd == null ? ZERO : aggrCurrFirstHalfDmd;
    }

    public BigDecimal getCurrSecondHalfDemandDue(final BigDecimal currSecondHalfDemand, final BigDecimal aggrCurrSecondHalfColl) {
        return (currSecondHalfDemand == null ? ZERO : currSecondHalfDemand)
                .subtract(aggrCurrSecondHalfColl == null ? ZERO
                        : aggrCurrSecondHalfColl);
    }

    public BigDecimal getIntrestDueOnCurrSecondHalfDemand(final BigDecimal currSecondHalfPenalty,
            final BigDecimal currSecondHalfPenaltyColl) {
        return (currSecondHalfPenalty == null ? ZERO : currSecondHalfPenalty)
                .subtract(currSecondHalfPenaltyColl == null ? ZERO : currSecondHalfPenaltyColl);
    }

    public BigDecimal getAggrArrDmdDue(final BigDecimal arrearDemand, final BigDecimal arrearCollection) {
        return (arrearDemand == null ? ZERO : arrearDemand)
                .subtract(arrearCollection == null ? ZERO : arrearCollection);
    }

    public BigDecimal getIntrestDueOnArrearDemandDue(final BigDecimal arrearPenalty, final BigDecimal arrearPenaltyColl) {
        return (arrearPenalty == null ? ZERO : arrearPenalty)
                .subtract(arrearPenaltyColl == null ? ZERO : arrearPenaltyColl);
    }

    public BigDecimal calculateNetPayableAmmount(final PropertyMaterlizeView pmv) {
        return getCurrFirstHalfDemandDue(pmv.getAggrCurrFirstHalfDmd(), pmv.getAggrCurrFirstHalfColl())
                .add(getIntrestDueOnCurrFirstHalfDemand(pmv.getAggrCurrFirstHalfPenaly(), pmv.getAggrCurrFirstHalfPenalyColl()))
                .add(getCurrSecondHalfDemandDue(pmv.getAggrCurrSecondHalfDmd(), pmv.getAggrCurrSecondHalfColl()))
                .add(getIntrestDueOnCurrSecondHalfDemand(pmv.getAggrCurrSecondHalfPenaly(),
                        pmv.getAggrCurrSecondHalfPenalyColl()))
                .add(getAggrArrDmdDue(pmv.getAggrArrDmd(), pmv.getAggrArrColl()))
                .add(getIntrestDueOnArrearDemandDue(pmv.getAggrArrearPenaly(), pmv.getAggrArrearPenalyColl()));
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
    public void setServletRequest(final HttpServletRequest arg0) {
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

    public void setSearchUrl(final String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getOldMuncipalNum() {
        return oldMuncipalNum;
    }

    public void setOldMuncipalNum(final String oldMuncipalNum) {
        this.oldMuncipalNum = oldMuncipalNum;
    }

    public Map<String, Object> getQueryMap() {
        return queryMap;
    }

    public void setQueryMap(Map<String, Object> queryMap) {
        this.queryMap = queryMap;
    }

}
