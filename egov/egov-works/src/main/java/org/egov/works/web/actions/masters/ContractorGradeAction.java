/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.actions.masters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.egov.commons.ContractorGrade;
import org.egov.infra.validation.regex.Constants;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.utils.WorksConstants;

@Results({
	@Result(name = ContractorGradeAction.NEW, location = "contractorGrade-new.jsp"),
	@Result(name = ContractorGradeAction.EDIT, location = "contractorGrade-edit.jsp"),
	@Result(name = ContractorGradeAction.SEARCH, location = "contractorGrade-searchPage.jsp"),
	@Result(name = ContractorGradeAction.INDEX, location = "contractorGrade-index.jsp")
})
@ParentPackage("egov")
public class ContractorGradeAction extends SearchFormAction {

    private static final long serialVersionUID = 4500128509093695097L;
    private ContractorGrade contractorGrade = new ContractorGrade();
    private PersistenceService<ContractorGrade, Long> contractorGradeService;
    private List<ContractorGrade> contractorGradeList = null;
    public static final String SEARCH = "searchPage";
    public static final String INDEX = "index";
    public static final String EDIT = "edit";
    private Long id;
    private String grade;
    private double minAmount = -1L;
    private double maxAmount = -1L;
    private String displData;
    private String mode;
    private List<String> maxAmountList;
    private List<String> minAmountList;

    @Action(value = "/masters/contractorGrade-save")
    public String save() {
        contractorGrade = contractorGradeService.persist(contractorGrade);
        addActionMessage(getText(WorksConstants.CONTRACTOR_GRADE_SAVE_SUCCESS_CODE, WorksConstants.CONTRACTOR_GRADE_SAVE_SUCCESS_MSG));
        contractorGradeList = new ArrayList<ContractorGrade>();
        contractorGradeList.add(contractorGrade);
        return INDEX;
    }

    @Action(value = "/masters/contractorGrade-newform")
    public String newform() {
        return NEW;
    }
    
    @Action(value = "/masters/contractorGrade-viewContractorGrade")
    public String viewContractorGrade() {
        return "searchPage";
    }

    @Override
    public void prepare() {
        if (id != null)
            contractorGrade = contractorGradeService.findById(id, false);
        final List<BigDecimal> tempMaxAmountList = persistenceService
                .findAllByNamedQuery("getContractorGradeMaxAmountList");
        final List<BigDecimal> tempMinAmountList = persistenceService
                .findAllByNamedQuery("getContractorGradeMinAmountList");
        maxAmountList = new ArrayList<String>();
        minAmountList = new ArrayList<String>();

        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        for (final BigDecimal maxValue : tempMaxAmountList) {
            final String max = numberFormat.format(maxValue.doubleValue());
            getMaxAmountList().add(max);
        }
        for (final BigDecimal minValue : tempMinAmountList) {
            final String min = numberFormat.format(minValue.doubleValue());
            getMinAmountList().add(min);
        }

        super.prepare();
    }

    public String list() {
        contractorGradeList = contractorGradeService.findAll();
        return INDEX;
    }

    @Action(value = "/masters/contractorGrade-edit")
    public String edit() {
        contractorGrade = contractorGradeService.findById(contractorGrade.getId(), false);
        return EDIT;
    }

    @Override
    public String execute() {
        return list();
    }

    @Action(value = "/masters/contractorGrade-searchGradeDetails")
    public String searchGradeDetails() {
        boolean hasNoErrors = true;

        contractorGrade = (ContractorGrade) getModel();
        final String s = contractorGrade.getMinAmountString();
        final String s1 = contractorGrade.getMaxAmountString();
        if (contractorGrade.getMaxAmountString() == null)
            setMinAmount(-1L);
        else
            setMinAmount(Double.parseDouble(s));
        if (contractorGrade.getMaxAmountString() == null)
            setMaxAmount(-1L);
        else
            setMaxAmount(Double.parseDouble(s1));

        if (grade != null && !grade.equals("")) {
            hasNoErrors = Pattern.matches(Constants.ALPHANUMERIC_WITHSPACE, grade);
            if (hasNoErrors == false) {
                addActionError(getText(WorksConstants.CONTRACTOR_GRADE_ALPHANUMERIC_ERR_CODE, WorksConstants.CONTRACTOR_GRADE_ALPHANUMERIC_ERR_MSG));
            }
        }
        if (minAmount != -1 && maxAmount != -1)
            if (minAmount >= maxAmount) {
                addActionError(getText(WorksConstants.CONTRACTOR_GRADE_MAX_AMOUNT_INVALID_ERR_CODE, WorksConstants.CONTRACTOR_GRADE_MAX_AMOUNT_INVALID_ERR_MSG));
                return SEARCH;

            }
        if (hasNoErrors == false)
            return SEARCH;
        setPageSize(WorksConstants.PAGE_SIZE);
        search();
        
        if (searchResult != null && searchResult.getFullListSize() == 0)
            setDisplData(WorksConstants.CONTRACTOR_GRADE_NO_DATA);
        else
            setDisplData(WorksConstants.YES);

        return "searchPage";
    }

    public String getDisplData() {
        return displData;
    }

    public void setDisplData(final String displData) {
        this.displData = displData;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(final double minAmount) {
        this.minAmount = minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(final double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public ContractorGrade getContractorGrade() {
        return contractorGrade;
    }

    public void setContractorGradeService(final PersistenceService<ContractorGrade, Long> contractorGradeService) {
        this.contractorGradeService = contractorGradeService;
    }

    public void setContractorGrade(final ContractorGrade contractorGrade) {
        this.contractorGrade = contractorGrade;
    }

    @Override
    public Object getModel() {
        return contractorGrade;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(final String mode) {
        this.mode = mode;
    }

    public List<ContractorGrade> getContractorGradeList() {
        return contractorGradeList;
    }

    @Override
    public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
        final StringBuffer contractorGradeSql = new StringBuffer(100);
        String contractorGradeStr = "";
        final List<Object> paramList = new ArrayList<Object>();
        contractorGradeSql.append(" from ContractorGrade cg");

        if (getGrade() != null && !getGrade().trim().equals("") || getMinAmount() != -1 || getMaxAmount() != -1)
            contractorGradeSql.append(" where 1=1");

        if (getGrade() != null && !getGrade().trim().equals("")) {
            contractorGradeSql.append(" and UPPER(cg.grade) like ?");
            paramList.add("%" + getGrade().trim().toUpperCase() + "%");
        }

        if (getMinAmount() != -1) {
            contractorGradeSql.append(" and cg.minAmount = ?");
            paramList.add(BigDecimal.valueOf(getMinAmount()));
        }

        if (getMaxAmount() != -1) {
            contractorGradeSql.append(" and cg.maxAmount = ?");
            paramList.add(BigDecimal.valueOf(getMaxAmount()));
        }
        contractorGradeSql.append(" group by cg.id");
        contractorGradeStr = contractorGradeSql.toString();
        final String countQuery = "select count(*) " + contractorGradeStr;
        return new SearchQueryHQL(contractorGradeStr, countQuery, paramList);

    }

    public List<String> getMaxAmountList() {
        return maxAmountList;
    }

    public void setMaxAmountList(final List<String> maxAmountList) {
        this.maxAmountList = maxAmountList;
    }

    public List<String> getMinAmountList() {
        return minAmountList;
    }

    public void setMinAmountList(final List<String> minAmountList) {
        this.minAmountList = minAmountList;
    }

}
