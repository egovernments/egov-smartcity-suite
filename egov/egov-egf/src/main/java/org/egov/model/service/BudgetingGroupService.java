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
package org.egov.model.service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.repository.BudgetingGroupRepository;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class BudgetingGroupService {

    private final BudgetingGroupRepository budgetGroupRepository;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BudgetingGroupService(final BudgetingGroupRepository budgetGroupRepository) {
        this.budgetGroupRepository = budgetGroupRepository;
    }

    @Transactional
    public BudgetGroup create(final BudgetGroup budgetGroup) {
        return budgetGroupRepository.save(budgetGroup);
    }

    @Transactional
    public BudgetGroup update(final BudgetGroup budgetGroup) {
        return budgetGroupRepository.save(budgetGroup);
    }

    public List<BudgetGroup> findAll() {
        return budgetGroupRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public BudgetGroup findOne(final Long id) {
        return budgetGroupRepository.findOne(id);
    }

    public int getMajorCodeLength() {
        final List<AppConfigValues> appList = appConfigValueService.getConfigValuesByModuleAndKey(Constants.EGF,
                FinancialConstants.APPCONFIG_COA_MAJORCODE_LENGTH);
        return Integer.valueOf(appList.get(0).getValue());
    }

    public List<CChartOfAccounts> getMajorCodeList() {
        return budgetGroupRepository.findCOAByLength(getMajorCodeLength());
    }

    public List<BudgetGroup> getActiveBudgetGroups() {
        return budgetGroupRepository.findByIsActiveTrue();
    }

    public List<CChartOfAccounts> getMinCodeList() {
        final String range = appConfigValueService
                .getConfigValuesByModuleAndKey(Constants.EGF, FinancialConstants.APPCONFIG_BUDGETGROUP_RANGE).get(0)
                .getValue();
        Integer minorCodeLength = 0;

        if (range.equalsIgnoreCase("minor"))
            minorCodeLength = Integer.valueOf(appConfigValueService
                    .getConfigValuesByModuleAndKey(Constants.EGF, FinancialConstants.APPCONFIG_COA_MINORCODE_LENGTH)
                    .get(0).getValue());
        else
            minorCodeLength = Integer.valueOf(appConfigValueService
                    .getConfigValuesByModuleAndKey(Constants.EGF, FinancialConstants.APPCONFIG_COA_DETAILCODE_LENGTH)
                    .get(0).getValue());
        return budgetGroupRepository.findCOAByLength(minorCodeLength);
    }

    public String validate(final BudgetGroup budgetGroup, final BindingResult errors) {
        String validationMessage = "";

        BudgetGroup bg = null;
        List<BudgetGroup> bgCode = null;
        if ((budgetGroup.getMajorCode() != null) && (budgetGroup.getId() == null))
            bg = budgetGroupRepository.findByMajorCode_Id(budgetGroup.getMajorCode().getId());
        else if ((budgetGroup.getMajorCode() != null) && (budgetGroup.getId() != null))
            bg = budgetGroupRepository.findByMajorCode_IdAndIdNotIn(budgetGroup.getMajorCode().getId(),
                    budgetGroup.getId());
        if (bg != null)
            validationMessage = messageSource.getMessage("budgetgroup.invalid.majorcode", new String[] { bg.getName() },
                    null);

        if ((budgetGroup.getMinCode() != null) && (budgetGroup.getMaxCode() != null) && (budgetGroup.getId() == null))
            bgCode = budgetGroupRepository.findByMinCodeGlcodeLessThanEqualAndMaxCodeGlcodeGreaterThanEqual(
                    budgetGroup.getMaxCode().getGlcode(), budgetGroup.getMinCode().getGlcode());
        else if ((budgetGroup.getMinCode() != null) && (budgetGroup.getMaxCode() != null)
                && (budgetGroup.getId() != null))
            bgCode = budgetGroupRepository.findByMinCodeGlcodeLessThanEqualAndMaxCodeGlcodeGreaterThanEqualAndIdNotIn(
                    budgetGroup.getMinCode().getGlcode(), budgetGroup.getMinCode().getGlcode(), budgetGroup.getId());
        else
            bgCode = Collections.emptyList();

        if (!bgCode.isEmpty())
            validationMessage = messageSource.getMessage("budgetgroup.invalid.maxmincode",
                    new String[] { bgCode.get(0).getName() }, null, Locale.ENGLISH);

        final List<BudgetGroup> bgList = budgetGroup.getMajorCode() != null
                ? budgetGroupRepository.getBudgetGroupForMappedMajorCode(
                        budgetGroup.getMajorCode().getGlcode().length(), budgetGroup.getMajorCode().getGlcode())
                : Collections.emptyList();

        if (!bgList.isEmpty())
            validationMessage = messageSource.getMessage("budgetgroup.invalid.majormincode",
                    new String[] { bgList.get(0).getName() }, null);

        bg = budgetGroup.getMaxCode() != null ? budgetGroupRepository.getBudgetGroupForMinorCodesMajorCode(
                budgetGroup.getMaxCode().getGlcode().substring(0, getMajorCodeLength())) : null;
        if (bg != null)
            validationMessage = messageSource.getMessage("budgetgroup.invalid.maxmajorcode",
                    new String[] { bg.getName() }, null, Locale.ENGLISH);

        bg = budgetGroup.getMinCode() != null ? budgetGroupRepository.getBudgetGroupForMinorCodesMajorCode(
                budgetGroup.getMinCode().getGlcode().substring(0, getMajorCodeLength())) : null;
        if (bg != null)
            validationMessage = messageSource.getMessage("budgetgroup.invalid.minmajorcode",
                    new String[] { bg.getName() }, null, Locale.ENGLISH);

        return validationMessage;
    }

    public List<BudgetGroup> search(final BudgetGroup budgetGroup) {
        if (budgetGroup.getName() != null)
            return budgetGroupRepository.findBudgetGroupByNameLike(budgetGroup.getName());
        else
            return budgetGroupRepository.findAll();
    }

}
