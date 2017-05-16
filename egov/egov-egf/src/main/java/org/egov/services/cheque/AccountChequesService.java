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
/**
 *
 */
package org.egov.services.cheque;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.Bankaccount;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.cheque.AccountCheques;
import org.egov.model.cheque.ChequeDeptMapping;
import org.egov.model.masters.ChequeDetail;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class AccountChequesService extends PersistenceService<AccountCheques, Long> {

    private static final Logger LOGGER = Logger.getLogger(AccountChequesService.class);

    @Autowired
    @Qualifier("chequeDeptMappingService")
    private ChequeDeptMappingService chequeDeptMappingService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    public AccountChequesService() {
        super(AccountCheques.class);
    }

    public AccountChequesService(Class<AccountCheques> type) {
        super(type);
    }

    @Transactional
    public void createCheques(List<ChequeDetail> chequeDetailsList, Map<String, String> chequeIdMap,
            Map<String, AccountCheques> chequeMap, Bankaccount bankaccount, String deletedChqDeptId) {

        final Session session = getSession();
        AccountCheques accountCheques;
        ChequeDeptMapping chqDept;
        for (final ChequeDetail chequeDetail : chequeDetailsList)
            // modify the existing cheque that are not used and insert new cheque leaf.
            if (chequeDetail.getNextChqPresent().equalsIgnoreCase("No") && chequeDetail.getIsExhusted().equalsIgnoreCase("No")) {

                if (chequeDetail.getAccountChequeId() != null
                        && null == chequeIdMap.get(chequeDetail.getAccountChequeId().toString())) {
                    session.createQuery(
                            "delete from ChequeDeptMapping where accountCheque.id=" + chequeDetail.getAccountChequeId())
                            .executeUpdate();
                    session.createQuery("delete from AccountCheques where id=" + chequeDetail.getAccountChequeId())
                            .executeUpdate();
                    chequeIdMap.put(chequeDetail.getAccountChequeId().toString(), chequeDetail.getAccountChequeId().toString());

                }
                if (null == chequeMap.get(chequeDetail.getFromChqNo() + chequeDetail.getToChqNo() + chequeDetail.getSerialNo())) {
                    accountCheques = new AccountCheques();
                    accountCheques.setBankAccountId(bankaccount);
                    accountCheques.setFromChequeNumber(chequeDetail.getFromChqNo());
                    accountCheques.setToChequeNumber(chequeDetail.getToChqNo());
                    accountCheques.setSerialNo(Long.valueOf(chequeDetail.getSerialNo()));
                    try {
                        accountCheques.setReceivedDate(Constants.DDMMYYYYFORMAT2.parse(chequeDetail.getReceivedDate()));
                    } catch (final ParseException e) {
                        LOGGER.error("ERROR" + e.getMessage(), e);
                    }
                    persist(accountCheques);
                    chequeMap.put(
                            accountCheques.getFromChequeNumber() + accountCheques.getToChequeNumber()
                                    + accountCheques.getSerialNo(), accountCheques);
                } else
                    accountCheques = chequeMap.get(chequeDetail.getFromChqNo() + chequeDetail.getToChqNo()
                            + chequeDetail.getSerialNo());
                chqDept = new ChequeDeptMapping();
                chqDept.setAccountCheque(accountCheques);
                final Department dept = (Department) persistenceService.find("from Department where id="
                        + chequeDetail.getDeptId());
                chqDept.setAllotedTo(dept);
                chequeDeptMappingService.persist(chqDept);
            }
    }

    // delete the record from chequedeptmapping and accountcheques if all the departments that are mapped for that cheque leaf is
    // deleted by user.
    @Transactional
    public void deleteRecords(String deletedChqDeptId, Bankaccount bankaccount) {

        final Session session = getSession();

        if (null != deletedChqDeptId && !deletedChqDeptId.equalsIgnoreCase("")) {
            final Query qry = session.createQuery("delete from ChequeDeptMapping where id in (" + deletedChqDeptId + ")");
            qry.executeUpdate();
        }

        // delete the cheque leafs that are not mapped to any department.
        final StringBuffer accChqDelquery = new StringBuffer();
        accChqDelquery.append("delete from AccountCheques where id in ( select id from AccountCheques where id not in").
                append("( select ac.id from AccountCheques ac,ChequeDeptMapping cd where ac.id=cd.accountCheque.id ").
                append(" and ac.bankAccountId.id=:bankAccId) and bankAccountId.id=:bankAccId)");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("AccountChequeAction | save | accChqDelquery " + accChqDelquery.toString());
        final Query delqry = session.createQuery(accChqDelquery.toString());
        delqry.setLong("bankAccId", bankaccount.getId());
        delqry.executeUpdate();

    }
    
    public List<ChequeDeptMapping> getChequeListByBankAccId(Long bankAccountId)
    {
        return persistenceService.findAllBy("from ChequeDeptMapping where accountCheque.bankAccountId.id =?", bankAccountId);
    }

    public List<ChequeDeptMapping> getChequesByBankAccIdFinId(Long bankAccountId, Long financialYearId) {
        return persistenceService.findAllBy("from ChequeDeptMapping where accountCheque.bankAccountId.id =? " +
                "and accountCheque.serialNo =?", bankAccountId, financialYearId);
    }
}