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
package org.egov.egf.commons.bankaccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.egov.commons.Bankaccount;
import org.egov.commons.Bankbranch;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.egf.commons.bankaccount.repository.BankAccountRepository;
import org.egov.egf.commons.bankbranch.service.CreateBankBranchService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infstr.utils.EGovConfig;
import org.egov.utils.FinancialConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author venki
 */

@Service
@Transactional(readOnly = true)
public class CreateBankAccountService {
    private final String code = EGovConfig.getProperty("egf_config.xml", "glcodeMaxLength", "", "AccountCode");
    @PersistenceContext
    private EntityManager entityManager;

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    @Qualifier(value = "chartOfAccountsService")
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private CreateBankBranchService createBankBranchService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public CreateBankAccountService(final BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Bankaccount getById(final Long id) {
        return bankAccountRepository.findOne(id);
    }

    public Bankaccount getByGlcode(final String glcode) {
        return bankAccountRepository.findByChartofaccounts_Glcode(glcode);
    }

    @Transactional
    public Bankaccount create(final Bankaccount bankaccount) {
        String newGLCode;
        if (autoBankAccountGLCodeEnabled()) {
            if (!bankaccount.getAccounttype().isEmpty()) {
                final CChartOfAccounts coa = chartOfAccountsService
                        .getByGlCode(bankaccount.getAccounttype().split("-")[0].trim());
                newGLCode = prepareBankAccCode(bankaccount.getAccounttype().split("-")[0].trim(), code);
                final Long coaID = postInChartOfAccounts(newGLCode, coa.getId(), bankaccount.getAccountnumber(),
                        bankaccount.getBankbranch().getId());
                if (coaID != null) {
                    final CChartOfAccounts chartofaccounts = chartOfAccountsService.getById(coaID);
                    bankaccount.setChartofaccounts(chartofaccounts);
                }
            }
        } else if (bankaccount.getChartofaccounts() != null && !bankaccount.getChartofaccounts().getGlcode().isEmpty())
            bankaccount
                    .setChartofaccounts(chartOfAccountsService.getByGlCode(bankaccount.getChartofaccounts().getGlcode()));
        bankaccount.setCreatedDate(new Date());
        bankaccount.setCreatedBy(getCurrentSession().load(User.class, ApplicationThreadLocals.getUserId()));

        return bankAccountRepository.save(bankaccount);
    }

    @Transactional
    public Bankaccount update(final Bankaccount bankaccount) {

        bankaccount.setLastModifiedDate(new Date());
        bankaccount.setLastModifiedBy(getCurrentSession().load(User.class, ApplicationThreadLocals.getUserId()));
        return bankAccountRepository.save(bankaccount);
    }

    public List<Bankaccount> search(final Bankaccount bankaccount) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Bankaccount> createQuery = cb.createQuery(Bankaccount.class);
        final Root<Bankaccount> bankaccounts = createQuery.from(Bankaccount.class);
        createQuery.select(bankaccounts);
        final Metamodel m = entityManager.getMetamodel();
        final EntityType<Bankaccount> tempBankaccount = m.entity(Bankaccount.class);

        final List<Predicate> predicates = new ArrayList<>();
        if (bankaccount.getAccountnumber() != null) {
            final String accountnumber = "%" + bankaccount.getAccountnumber().toLowerCase() + "%";
            predicates.add(cb.isNotNull(bankaccounts.get("accountnumber")));
            predicates.add(cb.like(
                    cb.lower(bankaccounts.get(tempBankaccount.getDeclaredSingularAttribute("accountnumber", String.class))),
                    accountnumber));
        }

        if (bankaccount.getFund() != null && bankaccount.getFund().getId() != null)
            predicates.add(cb.equal(bankaccounts.get("fund").get("id"), bankaccount.getFund().getId()));
        

        if (bankaccount.getBankbranch() != null && bankaccount.getBankbranch().getBank() != null)
            predicates.add(cb.equal(bankaccounts.get("bankbranch").get("bank").get("id"), bankaccount.getBankbranch().getBank().getId()));


        if (bankaccount.getBankbranch() != null && bankaccount.getBankbranch().getId() != null)
            predicates.add(cb.equal(bankaccounts.get("bankbranch").get("id"), bankaccount.getBankbranch().getId()));

        if (bankaccount.getChartofaccounts() != null && bankaccount.getChartofaccounts().getGlcode() != null)
            predicates.add(cb.equal(bankaccounts.get("chartofaccounts").get("glcode"), bankaccount.getChartofaccounts().getGlcode()));

        if (bankaccount.getAccounttype() != null)
            predicates.add(cb.equal(bankaccounts.get("accounttype"), bankaccount.getAccounttype()));

        if (bankaccount.getPayTo() != null)
            predicates.add(cb.equal(bankaccounts.get("payTo"), bankaccount.getPayTo()));

        if (bankaccount.getType() != null)
            predicates.add(cb.equal(bankaccounts.get("type"), bankaccount.getType()));

        if (bankaccount.getNarration() != null)
            predicates.add(cb.equal(bankaccounts.get("narration"), bankaccount.getNarration()));

        if (bankaccount.getIsactive())
            predicates.add(cb.equal(bankaccounts.get("isactive"), true));

        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<Bankaccount> query = entityManager.createQuery(createQuery);
        return query.getResultList();

    }

    public Boolean autoBankAccountGLCodeEnabled() {
        final AppConfigValues appConfigValue = appConfigValuesService
                .getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                        FinancialConstants.APPCONFIG_AUTO_BANKACCOUNT_GLCODE)
                .get(0);
        return "YES".equalsIgnoreCase(appConfigValue.getValue());
    }

    @SuppressWarnings("deprecation")
    public String prepareBankAccCode(final String glCode, final String code) {
        String newGlCode;
        Long glcode;
        Long tempCode;
        final String subminorvalue = EGovConfig.getProperty("egf_config.xml", "subminorvalue", "", "AccountCode");
        newGlCode = glCode.substring(0, Integer.parseInt(subminorvalue));
        final CChartOfAccounts coa = chartOfAccountsService.getByGlCodeDesc(newGlCode);
        newGlCode = coa.getGlcode();
        final String zero = EGovConfig.getProperty("egf_config.xml", "zerofill", "", "AccountCode");
        if (newGlCode.length() == Integer.parseInt(code)) {
            glcode = Long.parseLong(newGlCode);
            tempCode = glcode + 1;
        } else {
            newGlCode = newGlCode + zero;
            glcode = Long.parseLong(newGlCode);
            tempCode = glcode + 1;
        }
        newGlCode = Long.toString(tempCode);
        return newGlCode;
    }

    public Long postInChartOfAccounts(final String glCode, final Long parentId, final String accNumber,
            final Integer branchId) {
        final Bankbranch bankBranch = createBankBranchService.getById(branchId);
        int majorCodeLength;
        majorCodeLength = Integer
                .valueOf(appConfigValuesService.getConfigValuesByModuleAndKey(FinancialConstants.MODULE_NAME_APPCONFIG,
                        FinancialConstants.APPCONFIG_COA_MAJORCODE_LENGTH).get(0).getValue());
        final CChartOfAccounts chart = new CChartOfAccounts();
        chart.setGlcode(glCode);
        chart.setName(bankBranch.getBank().getName() + " "
                + bankBranch.getBranchname() + " " + accNumber);
        chart.setParentId(parentId);
        chart.setType('A');
        chart.setClassification(Long.parseLong("4"));
        chart.setIsActiveForPosting(true);
        chart.setMajorCode(chart.getGlcode().substring(0, majorCodeLength));
        chartOfAccountsService.persist(chart);
        return chart.getId();
    }
}
