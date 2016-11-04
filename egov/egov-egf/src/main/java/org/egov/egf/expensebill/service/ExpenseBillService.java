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
package org.egov.egf.expensebill.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.script.ScriptContext;

import org.egov.egf.autonumber.ExpenseBillNumberGenerator;
import org.egov.egf.billsubtype.service.EgBillSubTypeService;
import org.egov.egf.expensebill.repository.ExpenseBillRepository;
import org.egov.egf.utils.FinancialUtils;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.bills.EgBillregister;
import org.egov.services.masters.SchemeService;
import org.egov.services.masters.SubSchemeService;
import org.egov.services.voucher.VoucherService;
import org.egov.utils.FinancialConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author venki
 *
 */

@Service
@Transactional(readOnly = true)
public class ExpenseBillService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ExpenseBillRepository expenseBillRepository;

    @Autowired
    private EgBillSubTypeService egBillSubTypeService;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SubSchemeService subSchemeService;

    @Autowired
    private FinancialUtils financialUtils;

    @Autowired
    protected AppConfigValueService appConfigValuesService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    private final ScriptService scriptExecutionService;

    @Autowired
    @Qualifier(value = "voucherService")
    private VoucherService voucherService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    public ExpenseBillService(final ExpenseBillRepository expenseBillRepository, final ScriptService scriptExecutionService) {
        this.expenseBillRepository = expenseBillRepository;
        this.scriptExecutionService = scriptExecutionService;
    }

    public EgBillregister getById(final Long id) {
        return expenseBillRepository.findOne(id);
    }

    public EgBillregister getByBillnumber(final String billNumber) {
        return expenseBillRepository.findByBillnumber(billNumber);
    }

    @Transactional
    public EgBillregister create(final EgBillregister egBillregister) {

        egBillregister.setBilltype(FinancialConstants.BILLTYPE_FINAL_BILL);
        egBillregister.setExpendituretype(FinancialConstants.STANDARD_EXPENDITURETYPE_CONTINGENT);
        egBillregister.setPassedamount(egBillregister.getBillamount());
        egBillregister.setStatus(financialUtils.getStatusByModuleAndCode(FinancialConstants.CONTINGENCYBILL_FIN,
                FinancialConstants.CONTINGENCYBILL_CREATED_STATUS));
        egBillregister.getEgBillregistermis().setEgBillregister(egBillregister);
        egBillregister.getEgBillregistermis().setLastupdatedtime(new Date());

        if (egBillregister.getEgBillregistermis().getEgBillSubType() != null
                && egBillregister.getEgBillregistermis().getEgBillSubType().getId() != null)
            egBillregister.getEgBillregistermis().setEgBillSubType(
                    egBillSubTypeService.getById(egBillregister.getEgBillregistermis().getEgBillSubType().getId()));
        if (egBillregister.getEgBillregistermis().getScheme() != null
                && egBillregister.getEgBillregistermis().getScheme().getId() != null)
            egBillregister.getEgBillregistermis().setScheme(
                    schemeService.findById(egBillregister.getEgBillregistermis().getScheme().getId(), false));
        else
            egBillregister.getEgBillregistermis().setScheme(null);
        if (egBillregister.getEgBillregistermis().getSubScheme() != null
                && egBillregister.getEgBillregistermis().getSubScheme().getId() != null)
            egBillregister.getEgBillregistermis().setSubScheme(
                    subSchemeService.findById(egBillregister.getEgBillregistermis().getSubScheme().getId(), false));
        else
            egBillregister.getEgBillregistermis().setSubScheme(null);

        if (isBillNumberGenerationAuto())
            egBillregister.setBillnumber(getNextBillNumber(egBillregister));

        try {
            checkBudgetAndGenerateBANumber(egBillregister);
        } catch (final ValidationException e) {
            throw new ValidationException(e.getErrors());
        }

        expenseBillRepository.save(egBillregister);
        egBillregister.getEgBillregistermis().setSourcePath(
                "/EGF/bill/contingentBill-beforeView.action?billRegisterId=" + egBillregister.getId().toString());

        return expenseBillRepository.save(egBillregister);
    }

    public void checkBudgetAndGenerateBANumber(final EgBillregister egBillregister) {
        final ScriptContext scriptContext = ScriptService.createContext("voucherService", voucherService, "bill",
                egBillregister);
        scriptExecutionService.executeScript("egf.bill.budgetcheck", scriptContext);
    }

    @Transactional
    public EgBillregister update(final EgBillregister egBillregister) {

        return expenseBillRepository.save(egBillregister);
    }

    public boolean isBillNumberGenerationAuto() {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(
                FinancialConstants.MODULE_NAME_APPCONFIG, FinancialConstants.KEY_BILLNUMBER_APPCONFIG);
        if (configValuesByModuleAndKey.size() > 0)
            return "Y".equals(configValuesByModuleAndKey.get(0).getValue());
        else
            return false;
    }

    private String getNextBillNumber(final EgBillregister bill) {

        final ExpenseBillNumberGenerator b = beanResolver.getAutoNumberServiceFor(ExpenseBillNumberGenerator.class);
        final String billNumber = b.getNextNumber(bill);

        return billNumber;
    }

}
