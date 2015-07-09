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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.egov.commons.ContractorGrade;
import org.egov.infra.validation.regex.Constants;
import org.egov.infra.web.struts.actions.SearchFormAction;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.search.SearchQueryHQL;
import org.egov.infstr.services.PersistenceService;
import org.egov.works.utils.WorksConstants;

import com.opensymphony.xwork2.Action;

@Result(name = Action.SUCCESS, type = "ServletRedirectResult.class", location = "contractor.action")
@ParentPackage("egov")
public class ContractorGradeAction extends SearchFormAction {
    
    private static final long serialVersionUID = 4500128509093695097L;
    private ContractorGrade contractorGrade = new ContractorGrade();
    private PersistenceService<ContractorGrade, Long> contractorGradeService;
    private List<ContractorGrade> contractorGradeList = null;

    private Long id;
    private String grade;
    private double minAmount = -1L;
    private double maxAmount = -1L;
    private String displData;
    private String mode;
    private List<String> maxAmountList;
    private List<String> minAmountList;

    public String save() {
        contractorGrade = contractorGradeService.persist(contractorGrade);
        final String messageKey = "contractor.grade.save.success";
        addActionMessage(getText(messageKey, "The Contractor Grade was saved successfully"));
        contractorGradeList = new ArrayList<ContractorGrade>();
        contractorGradeList.add(contractorGrade);
        return INDEX;

    }

    public String newform() {
        return NEW;
    }

    public String viewContractorGrade() {
        return "searchPage";
    }

    @Override
    public void prepare() {
        if (id != null)
            contractorGrade = contractorGradeService.findById(id, false);
        final List<Double> tempMaxAmountList = persistenceService
                .findAllByNamedQuery("getContractorGradeMaxAmountList");
        final List<Double> tempMinAmountList = persistenceService
                .findAllByNamedQuery("getContractorGradeMinAmountList");
        maxAmountList = new ArrayList<String>();
        minAmountList = new ArrayList<String>();

        final NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        for (final Double maxValue : tempMaxAmountList) {
            final String max = numberFormat.format(maxValue.doubleValue());
            getMaxAmountList().add(max);
        }
        for (final Double minValue : tempMinAmountList) {
            final String min = numberFormat.format(minValue.doubleValue());
            getMinAmountList().add(min);
        }

        super.prepare();
    }

    public String list() {
        contractorGradeList = contractorGradeService.findAll();
        return INDEX;
    }

    public String edit() {
        contractorGrade = contractorGradeService.findById(contractorGrade.getId(), false);
        return EDIT;
    }

    @Override
    public String execute() {
        return list();
    }

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
                final String messageKey = "contractorGrade.grade.alphaNumeric";
                addActionError(getText(messageKey, "Special Characters are not allowed in Contractor Grade"));
            }
        }
        if (minAmount != -1 && maxAmount != -1)
            if (minAmount >= maxAmount) {
                final String messageKey = "contractor.grade.maxamount.invalid";
                addActionError(getText(messageKey, "Maximum amount must be greater than minimum amount"));
                return "searchPage";

            }
        if (hasNoErrors == false)
            return "searchPage";
        setPageSize(WorksConstants.PAGE_SIZE);
        search();

        if (searchResult.getFullListSize() == 0)
            setDisplData("noData");
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
            paramList.add(getMinAmount());
        }

        if (getMaxAmount() != -1) {
            contractorGradeSql.append(" and cg.maxAmount = ?");
            paramList.add(getMaxAmount());
        }
        contractorGradeSql.append(" order by cg.id");
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
