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
import java.util.Arrays;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ChartOfAccountsService extends PersistenceService<CChartOfAccounts, Long> {
    public ChartOfAccountsService(final Class<CChartOfAccounts> chartOfAccounts) {
        super(chartOfAccounts);
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

    public List<CChartOfAccounts> getAccountCodeByPurposeName(final String purposeName) {
        final List<CChartOfAccounts> accountCodeList = new ArrayList<CChartOfAccounts>();
        try {
            if ((purposeName == null) || purposeName.equalsIgnoreCase("")) {
                throw new ApplicationException("Purpose Name is null or empty");
            }
            Query query = getSession().createQuery(
                    " from EgfAccountcodePurpose purpose where purpose.name='" + purposeName + "'");
            if (query.list().size() == 0) {
                throw new ApplicationException("Purpose ID provided is not defined in the system");
            }
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName))) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT id FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName)) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getSession()
                    .createQuery(
                            " FROM CChartOfAccounts WHERE parentId IN (SELECT coa.id FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName) AND classification=4 AND isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
            query = getSession()
                    .createQuery(
                            "SELECT coa FROM CChartOfAccounts coa,EgfAccountcodePurpose purpose WHERE coa.purposeId=purpose.id and purpose.name = :purposeName AND coa.classification=4 AND coa.isActiveForPosting=true ");
            query.setString("purposeName", purposeName);
            accountCodeList.addAll((List<CChartOfAccounts>) query.list());
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Error occurred while getting Account Code by purpose", e);
        }
        return accountCodeList;
    }

    public List<CChartOfAccounts> findOtherDeductionAccountCodesByGlcodeOrNameLike(String searchString, String[] purposeNames) {
        if ((null == purposeNames) || (purposeNames.length == 0)) {
            throw new ValidationException(Arrays.asList(new ValidationError("purposeId",
                    "The supplied purposeId  can not be null or empty")));
        }
        final List<CChartOfAccounts> listChartOfAcc = new ArrayList<CChartOfAccounts>();
        Query query = getSession()
                .createQuery(
                        "SELECT coa  FROM CChartOfAccounts coa WHERE not exists (select coa1.id from EgfAccountcodePurpose purpose,CChartOfAccounts coa1 where coa.id = coa1.id and coa1.purposeId = purpose.id and purpose.name in(:purposeNames))  AND coa.classification=4 AND coa.isActiveForPosting=true and coa.type in ('I','L') and (coa.glcode like :glCode or upper(coa.name) like :name) order by coa.glcode");
        query.setParameterList("purposeNames", purposeNames);
        query.setString("glCode", searchString + "%");
        query.setString("name", "%" + searchString.toUpperCase() + "%");
        query.setCacheable(true);
        listChartOfAcc.addAll(query.list());

        return listChartOfAcc;
    }
}