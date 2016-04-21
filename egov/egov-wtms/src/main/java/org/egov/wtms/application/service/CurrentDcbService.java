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
package org.egov.wtms.application.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CurrentDcbService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public DCBDisplayInfo getDcbDispInfo() {
        final DCBDisplayInfo dcbDispInfo = new DCBDisplayInfo();
        final List<String> reasonMasterCodes = new ArrayList<String>(0);
        final List<String> reasonCategoryCodes = new ArrayList<String>(0);
        reasonMasterCodes.add(WaterTaxConstants.WATERTAXREASONCODE);
        dcbDispInfo.setReasonCategoryCodes(reasonCategoryCodes);
        dcbDispInfo.setReasonMasterCodes(reasonMasterCodes);
        return dcbDispInfo;

    }

    public SQLQuery getMigratedReceipttDetails(final String consumerNumber) throws ParseException {
        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select distinct(i_bookno) as \"bookNumber\", i_ctrrcptno as \"receiptNumber\",dt_ctrrcptdt as \"receiptDate\",dt_paidfrmprddt as \"fromDate\",dt_paidtoprddt as \"toDate\","
                        + "d_crr+d_arr as \"receiptAmount\" from wt_wtchrgrcpt_tbl where i_csmrno =" + consumerNumber);
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(WaterChargesReceiptInfo.class));
        return finalQuery;

    }

}
