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
package org.egov.commons.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ChartOfAccountsService extends PersistenceService<CChartOfAccounts, Long> {

    @Autowired
    protected AppConfigValueService appConfigValuesService;

    public ChartOfAccountsService(final Class<CChartOfAccounts> type) {
        super(type);
    }

    public List<CChartOfAccounts> getActiveCodeList() {

        return findAllBy("select acc from CChartOfAccounts acc where acc.isActiveForPosting=true order by acc.glcode");

    }

    @Transactional
    public void updateActiveForPostingByMaterializedPath(final String materializedPath) {
        getSession()
                .createSQLQuery(
                        "update chartofaccounts set isactiveforposting = true where isactiveforposting = false and id in (select distinct bg.mincode from egf_budgetgroup bg,egf_budgetdetail bd where bd.budgetgroup = bg.id  and bd.materializedpath like'"
                                + materializedPath + "%') ")
                .executeUpdate();
    }

    public List<CChartOfAccounts> getSubledgerAccountCodesForAccountDetailTypeAndNonSubledgers(
            final Integer accountDetailTypeId, String glcode) {
        if (accountDetailTypeId == 0 || accountDetailTypeId == -1)
            return findAllBy(
                    "from CChartOfAccounts a where a.isActiveForPosting=true and a.classification=4 and size(a.chartOfAccountDetails) = 0 and glcode like ? order by a.id",
                    glcode + "%");
        else
            return findAllBy(
                    "from CChartOfAccounts  a LEFT OUTER JOIN  fetch a.chartOfAccountDetails  b where (size(a.chartOfAccountDetails) = 0 or b.detailTypeId.id=?)and a.isActiveForPosting=true and a.classification=4 and a.glcode like ? order by a.id",
                    accountDetailTypeId, glcode + "%");
    }

    public List<CChartOfAccounts> getAccountCodeByPurpose(final Integer purposeId) {
        final List<CChartOfAccounts> accountCodeList = new ArrayList<CChartOfAccounts>();
        try {
            if ((purposeId == null) || (purposeId.intValue() == 0)) {
                throw new ApplicationException("Purpose Id is null or zero");
            }
            Query query = getSession().createQuery(
                    " from EgfAccountcodePurpose purpose where purpose.id=" + purposeId + "");
            if (query.list().size() == 0) {
                throw new ApplicationException("Purpose ID provided is not defined in the system");
            }
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId))) AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId)) AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId) AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE purposeid=:purposeId AND classification=4 AND isActiveForPosting=true ");
            query.setLong("purposeId", purposeId);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting Account Code by purpose", e);
        }
        return accountCodeList;
    }

    public List<CChartOfAccounts> getNetPayableCodesByAccountDetailType(Integer accountDetailType) {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(
                "EGF", "contingencyBillPurposeIds");
        Set<CChartOfAccounts> netPayList = new HashSet<CChartOfAccounts>();
        List<CChartOfAccounts> accountCodeByPurpose = new ArrayList<CChartOfAccounts>();
        for (int i = 0; i < configValuesByModuleAndKey.size(); i++) {
            try {
                accountCodeByPurpose = getAccountCodeByPurpose(Integer
                        .valueOf(configValuesByModuleAndKey.get(i).getValue()));
            } catch (final Exception e) {
                // Ignore
            }

            if (accountDetailType == null || accountDetailType == 0) {
                for (CChartOfAccounts coa : accountCodeByPurpose) {
                    if (coa.getChartOfAccountDetails().isEmpty())
                        netPayList.add(coa);
                }

            } else {
                for (CChartOfAccounts coa : accountCodeByPurpose) {
                    if (!coa.getChartOfAccountDetails().isEmpty()) {
                        for (CChartOfAccountDetail coaDtl : coa.getChartOfAccountDetails()) {
                            if (coaDtl.getDetailTypeId() != null && coaDtl.getDetailTypeId().getId().equals(accountDetailType)) {
                                netPayList.add(coa);
                            }
                        }
                    }
                    netPayList.add(coa);
                }
            }

        }
        return new ArrayList<CChartOfAccounts>(netPayList);
    }
}