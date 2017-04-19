/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.service;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.tl.entity.dto.InstallmentWiseDCBForm;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Service
@Transactional(readOnly = true)
public class InstallmentWiseDCBService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CFinancialYearService cFinancialYearService;

    public List<CFinancialYear> getFinancialYears() {
        List<CFinancialYear> financialYearList = cFinancialYearService.findAll();
        Collections.reverse(financialYearList);
        return financialYearList;
    }

    public List<InstallmentWiseDCBForm> getReportResult(String licenseNumber, String financialYear) {
        CFinancialYear cFinancialYear = cFinancialYearService.getFinacialYearByYearRange(financialYear);
        final SQLQuery finalQuery = prepareQuery(licenseNumber, cFinancialYear);
        if (cFinancialYear != null) {
            finalQuery.setResultTransformer(new AliasToBeanResultTransformer(InstallmentWiseDCBForm.class));
            finalQuery.setParameter("fromDate", cFinancialYear.getStartingDate());
        }
        if (isNotEmpty(licenseNumber))
            finalQuery.setParameter("licenseNumber", licenseNumber);

        return finalQuery.list();
    }

    private SQLQuery prepareQuery(String licenseNumber, CFinancialYear financialYear) {
        StringBuilder selectQry = new StringBuilder();
        StringBuilder whereQry = new StringBuilder();
        if (financialYear != null) {
            selectQry.append("select mv.licensenumber as licensenumber,cast(mv.licenseid as integer) as licenseid," +
                    "coalesce(cast(sum(mv.curr_demand) as bigint),0) as currentdemand,coalesce(cast(sum(mv.curr_coll) as bigint),0) as currentcoll," +
                    "coalesce(cast(sum(mv.curr_balance) as bigint),0) as currentbalance ,coalesce(cast(sum(mv.arr_demand) as bigint),0) as arreardemand," +
                    "coalesce(cast(sum(mv.arr_coll) as bigint),0) as arrearcoll,coalesce(cast(sum(arr_balance) as bigint),0) as arrearbalance " +
                    "from (select aggrdcb.licensenumber,aggrdcb.licenseid," +
                    "case when aggrdcb.installment = :fromDate then aggrdcb.curr_demand end as curr_demand," +
                    "case when aggrdcb.installment = :fromDate then aggrdcb.curr_coll end as curr_coll," +
                    "case when aggrdcb.installment = :fromDate then aggrdcb.curr_balance end as curr_balance," +
                    "case when aggrdcb.installment < :fromDate then aggrdcb.curr_demand end as arr_demand," +
                    "case when aggrdcb.installment < :fromDate then aggrdcb.curr_coll end as arr_coll," +
                    "case when aggrdcb.installment < :fromDate then aggrdcb.curr_balance end as arr_balance " +
                    "from egtl_dcb_aggr_view aggrdcb where 1=1 ");
            whereQry.append("and aggrdcb.installment <=:fromDate ");
            if (isNotEmpty(licenseNumber))
                whereQry.append("and aggrdcb.licensenumber =:licenseNumber ");

            whereQry.append("order by aggrdcb.licenseid) as mv group by mv.licensenumber,mv.licenseid order by mv.licenseid");
        }
        StringBuilder query = selectQry.append(whereQry);
        return entityManager.unwrap(Session.class).createSQLQuery(query.toString());
    }
}
