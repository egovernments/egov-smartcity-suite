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

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.bnd.model.Registration;
import org.egov.bnd.services.registration.NonAvailabilityRegistrationService;
import org.egov.bnd.services.search.SearchByReceiptService;
import org.egov.bnd.utils.BndConstants;
import org.egov.bnd.web.actions.common.BndCommonAction;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@ParentPackage("egov")
@Transactional(readOnly = true)
@Namespace("/search")
public class SearchByReceiptAction extends BndCommonAction {
    private static final long serialVersionUID = -1997201664458515658L;
    private static final String STRUTS_RESULT_SEARCH = "search";
    
    private String regType = null;
    private String searchMode;
    private String receiptNo;
    private String hiddenRegType;
    private List searchResultList;
    private SearchByReceiptService searchByReceiptService;
    private NonAvailabilityRegistrationService nonAvailableRegService;

    @Action(value = "/searchByReceipt-searchbyReceiptForm", results = { @Result(name = STRUTS_RESULT_SEARCH, type = "dispatcher") })
    public String searchbyReceiptForm() {
        setRegType(BndConstants.SEARCHBIRTH);
        return "search";
    }

    @Transactional
    @Action(value = "/searchByReceipt-searchresults", results = { @Result(name = STRUTS_RESULT_SEARCH, type = "dispatcher") })
    public String searchresults() {

        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("REGTYPE", regType);
        hashMap.put("RECEIPTNO", receiptNo);
        if (getRegType().equals(BndConstants.SEARCHNONAVAILABILITY))
            searchResultList = nonAvailableRegService.searchRecordsByReceipt(hashMap);
        else {
            searchResultList = searchByReceiptService.searchRecordsByReceipt(hashMap);
            final List<Registration> finalResult = searchResult != null ? searchResult.getList() : null;

            if (finalResult != null) {
                bndCommonService.getAllStatesOfCountry();
                for (final Registration reg : finalResult)
                    if (reg.getEventAddress() != null && reg.getEventAddress().getState() != null)
                        if (bndCommonService.ADDRESSMAP.containsKey(Integer.valueOf(reg.getEventAddress().getState())))
                            reg.setStateName(bndCommonService.ADDRESSMAP.get(Integer.valueOf(reg.getEventAddress()
                                    .getState())));
            }
        }

        setSearchMode("result");
        return STRUTS_RESULT_SEARCH;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(final String regType) {
        this.regType = regType;
    }

    public List<String> getRegistrationTypeList() {
        return BndConstants.REGISTRATIONTYPELISTFORRECEIPT;
    }

    public String getDeath() {
        return BndConstants.SEARCHDEATH;
    }

    public String getNonAvailability() {
        return BndConstants.SEARCHNONAVAILABILITY;
    }

    public String getStillBirth() {
        return BndConstants.SEARCHSTILLBIRTH;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(final String searchMode) {
        this.searchMode = searchMode;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(final String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setSearchByReceiptService(final SearchByReceiptService searchByReceiptService) {
        this.searchByReceiptService = searchByReceiptService;
    }

    public void setSearchResultList(final List searchResultList) {
        this.searchResultList = searchResultList;
    }

    public List getSearchResultList() {
        return searchResultList;
    }

    public String getHiddenRegType() {
        return hiddenRegType;
    }

    public void setHiddenRegType(final String hiddenRegType) {
        this.hiddenRegType = hiddenRegType;
    }

    public void setNonAvailableRegService(final NonAvailabilityRegistrationService nonAvailableRegService) {
        this.nonAvailableRegService = nonAvailableRegService;
    }

}
