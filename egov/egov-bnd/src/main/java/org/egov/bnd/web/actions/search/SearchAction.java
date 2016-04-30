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
package org.egov.bnd.web.actions.search;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bnd.client.utils.BndRuleBook;
import org.egov.bnd.model.Registration;
import org.egov.bnd.services.search.SearchService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.utils.SexType;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.search.SearchQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Namespace("/search")
@Transactional(readOnly = true)
public class SearchAction extends BndCommonAction {

    private static final long serialVersionUID = -1756028116762763554L;
    private static final String STRUTS_RESULT_SEARCH = "search";
    
    private String regType;
    private String hiddenRegType;
    private String regNo;
    private Integer regYear;
    private Date fromDate;
    private Date toDate;
    private String firstName;
    private String midName;
    private String lastName;
    private String fatherName;
    private String motherName;
    private String placeOfEventType;
    private String sextype;
    private Long hospitalId;
    private Long registrationUnitId;
    private Integer pincode;
    private String searchMode;
    private SearchService searchService = new SearchService();

    private Boolean isCitizen = Boolean.FALSE;
    private String mode;
    private static final Logger LOGGER = Logger.getLogger(SearchAction.class);

    @Override
    @Transactional
    public void prepare() {
        super.prepare();
        addDropdownData("sexTypeList", Arrays.asList(SexType.values()));
        addDropdownData("registrationList", bndCommonService.findAllBy("from RegistrationUnit order by id"));
        final List<String> roleList = bndCommonService.getRoleNamesByPassingUserId(Long.valueOf(EgovThreadLocals
                .getUserId()));
        if ("".equals(roleList) || roleList == null
                || BndRuleBook.CITIZEN.equalsIgnoreCase(BndRuleBook.getInstance().getHighestPrivilegedRole(roleList)))
            setIsCitizen(Boolean.TRUE);
    }

    /* to search locked records and unlock them */
    @Action(value = "/search-searchLockedRecordsForm", results = { @Result(name = STRUTS_RESULT_SEARCH, type = "dispatcher") })
    public String searchLockedRecordsForm() {
        LOGGER.info("Inside searchLockedRecordsForm");
        setRegType(BndConstants.SEARCHBIRTH);
        mode = BndConstants.MODEUNLOCK;
        return STRUTS_RESULT_SEARCH;
    }

    @Action(value = "/search-searchForm", results = { @Result(name = STRUTS_RESULT_SEARCH, type = "dispatcher") })
    public String searchForm() {
        LOGGER.info("Inside searchForm");
        setRegType(BndConstants.SEARCHBIRTH);
        return STRUTS_RESULT_SEARCH;
    }

    @Action(value = "/search-searchresults", results = { @Result(name = STRUTS_RESULT_SEARCH, type = "dispatcher") })
    public String searchresults() {
        LOGGER.info("Inside searchResult");
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("MODE", getMode());
        hashMap.put("REGTYPE", regType);
        hashMap.put("REGNO", getRegNo().trim());
        hashMap.put("REGYEAR", regYear);
        hashMap.put("FROMDATE", fromDate);
        hashMap.put("TODATE", toDate);
        hashMap.put("FIRSTNAME", getFirstName().trim());
        hashMap.put("MIDNAME", getMidName().trim());
        hashMap.put("LASTNAME", getLastName().trim());
        hashMap.put("SEXTYPE", getSextype());
        hashMap.put("FATHERNAME", getFatherName().trim());
        hashMap.put("MOTHERNAME", getMotherName().trim());
        hashMap.put("EVENTTYPE", getPlaceOfEventType());
        hashMap.put("HOSPITALID", getHospitalId());
        hashMap.put("REGISTRATIONUNITID", getRegistrationUnitId());
        hashMap.put("PINCODE", getPincode());
        searchResult = searchService.searchRecords(hashMap, getPage(), getPageSize());

        final List<Registration> finalResult = searchResult != null ? searchResult.getList() : null;

        if (finalResult != null) {
            bndCommonService.getAllStatesOfCountry();
            for (final Registration reg : finalResult)
                if (reg.getEventAddress() != null && reg.getEventAddress().getState() != null)
                    if (bndCommonService.ADDRESSMAP.containsKey(Integer.valueOf(reg.getEventAddress().getState())))
                        reg.setStateName(bndCommonService.ADDRESSMAP.get(Integer.valueOf(reg.getEventAddress()
                                .getState())));
        }

        setSearchMode("result");
        setMode(getMode());
        return STRUTS_RESULT_SEARCH;
    }

    @Override
    public SearchQuery prepareQuery(final String arg0, final String arg1) {
        return null;
    }

    /*
     * BndConstants.REGISTRATIONTYPELIST contains {Birth,Death,StillBirth} used
     * for radio button in jsp
     */
    public List<String> getRegistrationTypeList() {
        return BndConstants.REGISTRATIONTYPELIST;
    }

    public String getDeath() {
        return BndConstants.SEARCHDEATH;
    }

    public String getStillBirth() {
        return BndConstants.SEARCHSTILLBIRTH;
    }

    public String getUnlock() {
        return BndConstants.MODEUNLOCK;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(final String regNo) {
        this.regNo = regNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(final String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(final String motherName) {
        this.motherName = motherName;
    }

    public void setMidName(final String midName) {
        this.midName = midName;
    }

    public String getMidName() {
        return midName;
    }

    public void setSearchService(final SearchService searchService) {
        this.searchService = searchService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public void setSearchMode(final String searchMode) {
        this.searchMode = searchMode;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setRegType(final String regType) {
        this.regType = regType;
    }

    public String getRegType() {
        return regType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public void setHiddenRegType(final String hiddenRegType) {
        this.hiddenRegType = hiddenRegType;
    }

    public String getHiddenRegType() {
        return hiddenRegType;
    }

    public String getSextype() {
        return sextype;
    }

    public void setSextype(final String sextype) {
        this.sextype = sextype;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(final Integer pincode) {
        this.pincode = pincode;
    }

    public String getPlaceOfEventType() {
        return placeOfEventType;
    }

    public void setPlaceOfEventType(final String placeOfEventType) {
        this.placeOfEventType = placeOfEventType;
    }

    public Integer getRegYear() {
        return regYear;
    }

    public void setRegYear(final Integer regYear) {
        this.regYear = regYear;
    }

    public Boolean getIsCitizen() {
        return isCitizen;
    }

    public void setIsCitizen(final Boolean isCitizen) {
        this.isCitizen = isCitizen;
    }

    public Long getRegistrationUnitId() {
        return registrationUnitId;
    }

    public void setRegistrationUnitId(final Long registrationUnitId) {
        this.registrationUnitId = registrationUnitId;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(final Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public String getMode() {
        return mode;
    }

    @Override
    public void setMode(final String mode) {
        this.mode = mode;
    }

}
