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

    private static final String PURPOSE_ID = "purposeId";
    private static final String CONTINGENCY_BILL_PURPOSE_IDS = "contingencyBillPurposeIds";
    private static final String GLCODE = "glcode";
    @Autowired
    protected AppConfigValueService appConfigValuesService;

    public ChartOfAccountsService(final Class<CChartOfAccounts> type) {
        super(type);
    }

    public List<CChartOfAccounts> getActiveCodeList() {

        return findAllBy("select acc from CChartOfAccounts acc where acc.isActiveForPosting=true order by acc.glcode");

    }

    public List<CChartOfAccounts> getAllAccountCodes(final String glcode) {

        final Query entitysQuery = getSession()
                .createQuery(
                        " from CChartOfAccounts a where a.isActiveForPosting=true and a.classification=4 and (a.glcode like :glcode or lower(a.name) like :name) order by a.glcode");
        entitysQuery.setString(GLCODE, glcode + "%");
        entitysQuery.setString("name", glcode.toLowerCase() + "%");
        return entitysQuery.list();

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
            final Integer accountDetailTypeId, final String glcode) {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(
                "EGF", CONTINGENCY_BILL_PURPOSE_IDS);
        final List<Long> contingencyBillPurposeIds = new ArrayList<>();
        for (final AppConfigValues av : configValuesByModuleAndKey)
            contingencyBillPurposeIds.add(Long.valueOf(av.getValue()));

        if (accountDetailTypeId == 0 || accountDetailTypeId == -1) {
            final Query entitysQuery = getSession()
                    .createQuery(
                            " from CChartOfAccounts a where a.isActiveForPosting=true and a.classification=4 and size(a.chartOfAccountDetails) = 0 and (glcode like :glcode or lower(name) like :name) and (purposeId is null or purposeId not in (:ids)) order by a.id");
            entitysQuery.setString(GLCODE, glcode + "%");
            entitysQuery.setString("name", glcode.toLowerCase() + "%");
            entitysQuery.setParameterList("ids", contingencyBillPurposeIds);
            return entitysQuery.list();
        } else {
            final Query entitysQuery = getSession()
                    .createQuery(
                            " from CChartOfAccounts  a LEFT OUTER JOIN  fetch a.chartOfAccountDetails  b where (size(a.chartOfAccountDetails) = 0 or b.detailTypeId.id=:accountDetailTypeId)and a.isActiveForPosting=true and a.classification=4 and (a.glcode like :glcode or lower(a.name) like :name) and (purposeId is null or purposeId not in (:ids)) order by a.id");
            entitysQuery.setInteger("accountDetailTypeId", accountDetailTypeId);
            entitysQuery.setString(GLCODE, glcode + "%");
            entitysQuery.setString("name", glcode.toLowerCase() + "%");
            entitysQuery.setParameterList("ids", contingencyBillPurposeIds);
            return entitysQuery.list();
        }
    }

    public List<CChartOfAccounts> getAccountCodeByPurpose(final Integer purposeId) {
        final List<CChartOfAccounts> accountCodeList = new ArrayList<CChartOfAccounts>();
        try {
            if (purposeId == null || purposeId.intValue() == 0)
                throw new ApplicationException("Purpose Id is null or zero");
            Query query = getSession().createQuery(
                    " from EgfAccountcodePurpose purpose where purpose.id=" + purposeId + "");
            if (query.list().isEmpty())
                throw new ApplicationException("Purpose ID provided is not defined in the system");
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId))) AND classification=4 AND isActiveForPosting=true ");
            query.setLong(PURPOSE_ID, purposeId);
            accountCodeList.addAll(query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId)) AND classification=4 AND isActiveForPosting=true ");
            query.setLong(PURPOSE_ID, purposeId);
            accountCodeList.addAll(query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE purposeid=:purposeId) AND classification=4 AND isActiveForPosting=true ");
            query.setLong(PURPOSE_ID, purposeId);
            accountCodeList.addAll(query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE purposeid=:purposeId AND classification=4 AND isActiveForPosting=true ");
            query.setLong(PURPOSE_ID, purposeId);
            accountCodeList.addAll(query.list());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting Account Code by purpose", e);
        }
        return accountCodeList;
    }

    public List<CChartOfAccounts> getAccountCodeByPurposeName(final String purposeName) {
        final List<CChartOfAccounts> accountCodeList = new ArrayList<CChartOfAccounts>();
        try {
            if (purposeName == null || purposeName.equalsIgnoreCase(""))
                throw new ApplicationException("Purpose Name is null or empty");
            Query query = getSession().createQuery(
                    " from EgfAccountcodePurpose purpose where purpose.name='" + purposeName + "'");
            if (query.list().size() == 0)
                throw new ApplicationException("Purpose Name provided is not defined in the system");
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName))) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll(query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName)) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll(query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll(query.list());
            query = getSession()
                    .createQuery(
                            "SELECT coa FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName AND coa.classification=4 AND coa.isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll(query.list());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting Account Code by purpose name", e);
        }
        return accountCodeList;
    }

    public List<CChartOfAccounts> getNetPayableCodesByAccountDetailType(final Integer accountDetailType) {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(
                "EGF", CONTINGENCY_BILL_PURPOSE_IDS);
        final Set<CChartOfAccounts> netPayList = new HashSet<>();
        List<CChartOfAccounts> accountCodeByPurpose = new ArrayList<>();
        for (int i = 0; i < configValuesByModuleAndKey.size(); i++) {
            try {
                accountCodeByPurpose = getAccountCodeByPurpose(Integer
                        .valueOf(configValuesByModuleAndKey.get(i).getValue()));
            } catch (final Exception e) {
                // Ignore
            }

            if (accountDetailType == null || accountDetailType == 0) {
                for (final CChartOfAccounts coa : accountCodeByPurpose)
                    if (coa.getChartOfAccountDetails().isEmpty())
                        netPayList.add(coa);

            } else
                for (final CChartOfAccounts coa : accountCodeByPurpose) {
                    if (!coa.getChartOfAccountDetails().isEmpty())
                        for (final CChartOfAccountDetail coaDtl : coa.getChartOfAccountDetails())
                            if (coaDtl.getDetailTypeId() != null && coaDtl.getDetailTypeId().getId().equals(accountDetailType))
                                netPayList.add(coa);
                    netPayList.add(coa);
                }

        }
        return new ArrayList<>(netPayList);
    }

    public List<CChartOfAccounts> getNetPayableCodes() {
        final List<AppConfigValues> configValuesByModuleAndKey = appConfigValuesService.getConfigValuesByModuleAndKey(
                "EGF", CONTINGENCY_BILL_PURPOSE_IDS);
        final Set<CChartOfAccounts> netPayList = new HashSet<>();
        List<CChartOfAccounts> accountCodeByPurpose = new ArrayList<>();
        for (int i = 0; i < configValuesByModuleAndKey.size(); i++) {
            try {
                accountCodeByPurpose = getAccountCodeByPurpose(Integer
                        .valueOf(configValuesByModuleAndKey.get(i).getValue()));
            } catch (final Exception e) {
                // Ignore
            }

            for (final CChartOfAccounts coa : accountCodeByPurpose)
                netPayList.add(coa);

        }
        return new ArrayList<>(netPayList);
    }

    public List<CChartOfAccounts> getAccountTypes() {

        final Query accountTypesQuery = getSession()
                .createQuery(" from CChartOfAccounts WHERE glcode LIKE '4502%' AND classification=2 ORDER BY glcode");
        return accountTypesQuery.list();
    }

    public CChartOfAccounts getByGlCodeDesc(final String glcode) {

        final Query query = getSession()
                .createQuery(" from CChartOfAccounts a where  a.glcode like :glcode  order by a.glcode desc");
        query.setString(GLCODE, glcode + "%");
        final List<CChartOfAccounts> resultList = query.list();

        return resultList.isEmpty() ? null : resultList.get(0);

    }

    public CChartOfAccounts getByGlCode(final String glcode) {

        final Query query = getSession().createQuery(" from CChartOfAccounts a where  a.glcode =:glcode ");
        query.setString("glcode", glcode);
        final List<CChartOfAccounts> resultList = query.list();

        return resultList.isEmpty() ? null : resultList.get(0);

    }

    public CChartOfAccounts getById(final Long id) {

        return getSession().load(CChartOfAccounts.class, id);

    }

}