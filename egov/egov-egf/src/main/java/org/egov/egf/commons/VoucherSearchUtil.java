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
package org.egov.egf.commons;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.hibernate.query.Query;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class VoucherSearchUtil {
    private static final Logger LOGGER = Logger.getLogger(VoucherSearchUtil.class);
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;
    @Autowired
    private FinancialYearDAO financialYearDAO;

    public List<CVoucherHeader> search(final CVoucherHeader voucherHeader, final Date fromDate, final Date toDate,
                                       final String mode)
            throws ApplicationException,
            ParseException {
        String editModeQuery1, editModeQuery2;
        List<CVoucherHeader> voucherList = new ArrayList<CVoucherHeader>();
        final Map<String, Map<String, Object>> filterQueryMap = voucherFilterQuery(voucherHeader, fromDate, toDate, mode);
        final Map.Entry<String, Map<String, Object>> filterQueryEntry = filterQueryMap.entrySet().iterator().next();
        final StringBuilder sql = new StringBuilder(filterQueryEntry.getKey());
        final Map<String, Object> params = filterQueryEntry.getValue();
        List<Integer> statusExclude = Arrays.stream(excludeVoucherStatus().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        final boolean modeIsNotBlank = null != mode && !StringUtils.isBlank(mode);
        if (modeIsNotBlank) {
            if ("edit".equalsIgnoreCase(mode))
                sql.append(" and (vh.isConfirmed is null or vh.isConfirmed != 1)");
            else if ("reverse".equalsIgnoreCase(mode))
                sql.append(" and vh.isConfirmed = 1");
            statusExclude.add(FinancialConstants.REVERSEDVOUCHERSTATUS);
            statusExclude.add(FinancialConstants.REVERSALVOUCHERSTATUS);
        }

        if (modeIsNotBlank && "edit".equalsIgnoreCase(mode)
                && voucherHeader.getType().equalsIgnoreCase(FinancialConstants.STANDARD_VOUCHER_TYPE_JOURNAL)) {
            /**
             * Logic is as explained below 1.Get all vouchers created from JV screen (stateid null) and which does not have
             * payments 2.Get all Vouchers which has payments (Any state 0,1,2,4,5) 3. Add 1 and 2 ==>A 4.Get all vouchers which
             * has active payments 5.Get all vouchers which has active remittance payments 6. Add 4 and 5 ==>B 7. Remove All Bs
             * from A
             */
            final String jvModifyCondition = " and vh.status in (:status) and vh.moduleId is null ";

            // editModeQuery1 :Get all vouchers created from JV screen and payments are not done
            editModeQuery1 = " from CVoucherHeader vh where vh not in ( select billVoucherHeader from Miscbilldetail) ".concat(jvModifyCondition);

            // editModeQuery2 :-check for voucher for which payments are in created (any state)
            editModeQuery2 = " select distinct(vh) from  Miscbilldetail misc left join misc.billVoucherHeader vh where misc.billVoucherHeader is not null"
                    .concat(jvModifyCondition).concat(sql.toString());

            final Query qry1 = persistenceService.getSession().createQuery(editModeQuery1.concat(sql.toString()));
            params.entrySet().forEach(entry -> qry1.setParameter(entry.getKey(), entry.getValue()));
            qry1.setParameter("status", FinancialConstants.CREATEDVOUCHERSTATUS, IntegerType.INSTANCE);
            voucherList.addAll(qry1.list());

            if (LOGGER.isDebugEnabled()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("---NO Associated Payment");
                for (final CVoucherHeader vh : voucherList)
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("-----" + vh.getId());
            }
            // Assumption:editModeQuery1 and editModeQuery2 will always return different list (not duplicate)
            final Query qry2 = persistenceService.getSession().createQuery(editModeQuery2);
            params.entrySet().forEach(entry -> qry2.setParameter(entry.getKey(), entry.getValue()));
            qry2.setParameter("status", FinancialConstants.CREATEDVOUCHERSTATUS, IntegerType.INSTANCE);
            voucherList.addAll(qry2.list());

            if (LOGGER.isDebugEnabled()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("---Associated Payment");
                for (final CVoucherHeader vh : voucherList)
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("-----" + vh.getId());
            }

            // editModeQuery3 :-check for voucher for for which payments are active 0,5 this will be removed from above two list
            final StringBuilder editModeQuery3 = new StringBuilder(" select misc.billVoucherHeader.id")
                    .append(" from CVoucherHeader ph, Miscbilldetail misc,CVoucherHeader vh")
                    .append(" where misc.payVoucherHeader = ph and misc.billVoucherHeader is not null and misc.billVoucherHeader = vh and ph.status in (:statuses)")
                    .append(jvModifyCondition).append(sql);
            final Query qry3 = persistenceService.getSession().createQuery(editModeQuery3.toString());
            qry3.setParameterList("statuses", Arrays.asList(FinancialConstants.CREATEDVOUCHERSTATUS, FinancialConstants.PREAPPROVEDVOUCHERSTATUS), IntegerType.INSTANCE);
            qry3.setParameter("status", FinancialConstants.CREATEDVOUCHERSTATUS, IntegerType.INSTANCE);
            params.entrySet().forEach(entry -> qry3.setParameter(entry.getKey(), entry.getValue()));
            final List<Long> vouchersHavingActivePayments = qry3.list();

            final List<CVoucherHeader> vchList = voucherList;
            final StringBuilder uncancelledRemittances = new StringBuilder(" SELECT distinct(vh.id)")
                    .append(" FROM EgRemittanceDetail r, EgRemittanceGldtl rgd, Generalledgerdetail gld, CGeneralLedger gl, EgRemittance rd, CVoucherHeader vh, Vouchermis billmis,")
                    .append(" CVoucherHeader remittedvh")
                    .append(" WHERE r.egRemittanceGldtl = rgd AND rgd.generalledgerdetail = gld AND gld.generalledger = gl AND r.egRemittance = rd AND rd.voucherheader = remittedvh")
                    .append(" AND gl.voucherHeaderId = vh AND remittedvh = billmis.voucherheaderid and remittedvh.status != :status ");
            Query qry4 = persistenceService.getSession().createQuery(uncancelledRemittances.append(sql).toString());
            qry4.setParameter("status", FinancialConstants.CANCELLEDVOUCHERSTATUS, IntegerType.INSTANCE);
            params.entrySet().forEach(entry -> qry4.setParameter(entry.getKey(), entry.getValue()));
            final List<Long> remittanceBillVhIdList = qry4.list();

            if (LOGGER.isDebugEnabled()) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("---Active Bill  Remittance Payments");
                for (final Long vh : remittanceBillVhIdList)
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("-----" + vh);
            }
            remittanceBillVhIdList.addAll(vouchersHavingActivePayments);
            final List<CVoucherHeader> toBeRemovedList = new ArrayList<CVoucherHeader>();
            if (vchList != null && vchList.size() != 0 && remittanceBillVhIdList != null && remittanceBillVhIdList.size() != 0) {
                for (int i = 0; i < vchList.size(); i++)
                    if (remittanceBillVhIdList.contains(vchList.get(i).getId()))
                        toBeRemovedList.add(voucherList.get(i));
                for (final CVoucherHeader vh : toBeRemovedList)
                    voucherList.remove(vh);
            }
            return voucherList;
        } else {
            Query qry5 = persistenceService.getSession().createQuery(new StringBuilder(" from CVoucherHeader vh where vh.status not in (:statusExclude) ")
                    .append(sql).append(" order by vh.voucherNumber ").toString());
            params.entrySet().forEach(entry -> qry5.setParameter(entry.getKey(), entry.getValue()));
            qry5.setParameterList("statusExclude", statusExclude);
            voucherList = qry5.list();
        }
        return voucherList;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * @param voucherHeader
     * @param fromDate
     * @param toDate
     * @param mode
     * @return
     */
    public List<CVoucherHeader> searchNonBillVouchers(
            final CVoucherHeader voucherHeader, final Date fromDate, final Date toDate,
            final String mode) {

        StringBuilder sql = new StringBuilder();
        if (!voucherHeader.getType().equals("-1"))
            sql.append(" and vh.type = :vhType");
        if (voucherHeader.getName() != null
                && !voucherHeader.getName().equalsIgnoreCase("-1"))
            sql.append(" and vh.name = :vhName");
        else
            sql.append(" and vh.name in (:vhName)");
        if (voucherHeader.getVoucherNumber() != null
                && !voucherHeader.getVoucherNumber().equals(""))
            sql.append(" and vh.voucherNumber like :voucherNumber");
        if (fromDate != null)
            sql.append(" and vh.voucherDate >= :fromDate");
        if (toDate != null)
            sql.append(" and vh.voucherDate <= :toDate");
        if (voucherHeader.getFundId() != null)
            sql.append(" and vh.fundId = :fundId");
        if (voucherHeader.getVouchermis().getFundsource() != null)
            sql.append(" and vh.fundsourceId = :fundSourceId");
        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            sql.append(" and vh.vouchermis.departmentid = :deptId");
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            sql.append(" and vh.vouchermis.schemeid = :schemeId");
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            sql.append(" and vh.vouchermis.subschemeid = :subShemeId");
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            sql.append(" and vh.vouchermis.functionary = :functionary");
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            sql.append(" and vh.vouchermis.divisionid = :divisionId");
        sql.append(" and (vh.id in (select voucherHeader.id from EgBillregistermis) and vh.id not in")
                .append(" (select billVoucherHeader.id from Miscbilldetail where billVoucherHeader is not null and  payVoucherHeader in")
                .append(" (select id from CVoucherHeader where status not in (4,1) and type='Payment')) )");
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey(
                "finance", "statusexcludeReport");
        StringBuilder statusExclude = new StringBuilder(appList.get(0).getValue());
        statusExclude.append(",").append(FinancialConstants.REVERSEDVOUCHERSTATUS.toString()).append(",").append(FinancialConstants.REVERSALVOUCHERSTATUS);

        final StringBuilder finalQuery = new StringBuilder(" from CVoucherHeader vh where vh.status not in (")
                .append(statusExclude).append(") ")
                .append(sql).append("  ");

        final Query query = persistenceService.getSession().createQuery(finalQuery.toString())
                .setParameter("vhType", voucherHeader.getType(), StringType.INSTANCE);
        if (voucherHeader.getName() != null
                && !voucherHeader.getName().equalsIgnoreCase("-1"))
            query.setParameter("vhName", voucherHeader.getName(), StringType.INSTANCE);
        else
            query.setParameterList("vhName", Arrays.asList(FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL, FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL,
                    FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL), StringType.INSTANCE);
        query.setParameter("voucherNumber", "%".concat(voucherHeader.getVoucherNumber()).concat("%"), StringType.INSTANCE)
                .setParameter("fromDate", fromDate, DateType.INSTANCE)
                .setParameter("toDate", toDate, DateType.INSTANCE)
                .setParameter("fundId", voucherHeader.getFundId().getId(), IntegerType.INSTANCE)
                .setParameter("fundSourceId", voucherHeader.getVouchermis().getFundsource().getId(), LongType.INSTANCE)
                .setParameter("deptId", voucherHeader.getVouchermis().getDepartmentid().getId(), LongType.INSTANCE)
                .setParameter("schemeId", voucherHeader.getVouchermis().getSchemeid().getId(), IntegerType.INSTANCE)
                .setParameter("subShemeId", voucherHeader.getVouchermis().getSubschemeid().getId(), IntegerType.INSTANCE)
                .setParameter("functionary", voucherHeader.getVouchermis().getFunctionary().getId(), IntegerType.INSTANCE)
                .setParameter("divisionId", voucherHeader.getVouchermis().getDivisionid().getId(), LongType.INSTANCE);
        return query.list();
    }

    /*
     * Forming query for filter search
     */
    public Map<String, Map<String, Object>> voucherFilterQuery(final CVoucherHeader voucherHeader, final Date fromDate, final Date toDate, final String mode) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("");
        if (!voucherHeader.getType().equals("-1")) {
            sql.append(" and vh.type = :vhType");
            params.put("vhType", voucherHeader.getType());
        }

        if (voucherHeader.getName() != null
                && !voucherHeader.getName().equalsIgnoreCase("0") && !voucherHeader.getName().equalsIgnoreCase("-1")) {
            sql.append(" and vh.name = :vhName");
            params.put("vhName", voucherHeader.getName());
        }
        if (voucherHeader.getVoucherNumber() != null
                && !voucherHeader.getVoucherNumber().equals("")) {
            sql.append(" and vh.voucherNumber like :voucherNumber");
            params.put("voucherNumber", "%" + voucherHeader.getVoucherNumber() + "%");
        }
        if (fromDate != null) {
            sql.append(" and vh.voucherDate >= :fromDate");
            params.put("fromDate", fromDate);
        }
        if (toDate != null) {
            sql.append(" and vh.voucherDate <= :toDate");
            params.put("toDate", toDate);
        }
        if (voucherHeader.getFundId() != null) {
            sql.append(" and vh.fundId.id = :fundId");
            params.put("fundId", voucherHeader.getFundId().getId());
        }
        if (voucherHeader.getVouchermis().getFundsource() != null) {
            sql.append(" and vh.fundsourceId = :fundSourceId");
            params.put("fundSourceId", voucherHeader.getVouchermis().getFundsource().getId());
        }
        if (voucherHeader.getVouchermis().getDepartmentid() != null) {
            sql.append(" and vh.vouchermis.departmentid.id = :deptId");
            params.put("deptId", voucherHeader.getVouchermis().getDepartmentid().getId());
        }
        if (voucherHeader.getVouchermis().getSchemeid() != null) {
            sql.append(" and vh.vouchermis.schemeid.id = :schemeId");
            params.put("schemeId", voucherHeader.getVouchermis().getSchemeid().getId());
        }
        if (voucherHeader.getVouchermis().getSubschemeid() != null) {
            sql.append(" and vh.vouchermis.subschemeid.id = :subSchemeId");
            params.put("subSchemeId", voucherHeader.getVouchermis().getSubschemeid().getId());
        }
        if (voucherHeader.getVouchermis().getFunctionary() != null) {
            sql.append(" and vh.vouchermis.functionary.id = :functionary");
            params.put("functionary", voucherHeader.getVouchermis().getFunctionary().getId());
        }
        if (voucherHeader.getVouchermis().getDivisionid() != null) {
            sql.append(" and vh.vouchermis.divisionid.id = :divisionId");
            params.put("divisionId", voucherHeader.getVouchermis().getDivisionid().getId());
        }
        if (voucherHeader.getModuleId() != null) {
            // -2 For vouchers created from the financial module. -2 is set in
            // populateSourceMap() of VoucherSearchAction.
            if (voucherHeader.getModuleId() == -2)
                sql.append(" and vh.moduleId is null ");
            else {
                sql.append(" and vh.moduleId = :moduleId");
                params.put("moduleId", voucherHeader.getModuleId());
            }
        }
        map.put(sql.toString(), params);
        return map;
    }

    public String excludeVoucherStatus() {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF", "statusexcludeReport");
        return appList.get(0).getValue();
    }

    /*
     * Adding search query to return Query object for implementing paginated list for view voucher
     */
    public List<Query> voucherSearchQuery(final CVoucherHeader voucherHeader, final Date fromDate, final Date toDate,
                                          final String mode) {
        final List<Query> queries = new ArrayList<Query>();
        final String statusExclude = excludeVoucherStatus();
        final Map<String, Map<String, Object>> map = voucherFilterQuery(voucherHeader, fromDate, toDate, mode);
        final Map.Entry<String, Map<String, Object>> entry = map.entrySet().iterator().next();
        final String sql = entry.getKey();
        final Map<String, Object> params = entry.getValue();
        final StringBuilder sqlQuery = new StringBuilder("from CVoucherHeader vh where vh.status not in (")
                .append(statusExclude).append(") ")
                .append(sql).append(" order by vh.voucherNumber, vh.voucherDate,vh.name ");
        final Query selectQuery = persistenceService.getSession().createQuery(sqlQuery.toString());
        params.entrySet().forEach(rec -> selectQuery.setParameter(rec.getKey(), rec.getValue()));
        queries.add(selectQuery);
        final Query countQuery = persistenceService.getSession().createQuery(
                new StringBuilder("select count(*) from CVoucherHeader vh where vh.status not in (")
                        .append(statusExclude).append(") ").append(sql).toString());
        params.entrySet().forEach(rec -> countQuery.setParameter(rec.getKey(), rec.getValue()));
        queries.add(countQuery);
        return queries;
    }

    public boolean validateFinancialYearForPosting(final Date fromDate, final Date toDate) {
        return financialYearDAO.isFinancialYearActiveForPosting(fromDate, toDate);
    }

    public void setFinancialYearDAO(final FinancialYearDAO financialYearDAO) {
        this.financialYearDAO = financialYearDAO;
    }

}