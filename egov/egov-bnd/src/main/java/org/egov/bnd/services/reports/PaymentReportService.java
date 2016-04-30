/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.services.reports;

import org.egov.bnd.model.BndPaymentReport;
import org.egov.bnd.services.common.BndCommonService;
import org.egov.bnd.utils.BndConstants;
import org.egov.infra.web.utils.EgovPaginatedList;
import org.egov.infstr.services.Page;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Transactional(readOnly = true)
public class PaymentReportService extends PersistenceService<BndPaymentReport, Long> {

    private BndCommonService bndCommonService;

    /**
     * This method is to generate the report for name inclusion
     *
     * @return It returns paginated list of NameInclusionReport
     */

    @Transactional
    @SuppressWarnings("unchecked")
    public EgovPaginatedList getNameInclusionReport(final Date fromDate, final Date toDate, final String regType,
            final Integer page, final Integer pageSize) {
        final Criteria nameInclusionCriteria = buildNameInclusionCriteria(fromDate, toDate, regType);
        final Criteria nameInclusionCountCriteria = buildNameInclusionCriteria(fromDate, toDate, regType);
        final Page resultPage = new Page(nameInclusionCriteria, page, pageSize);
        final Set result = new HashSet(nameInclusionCountCriteria.list());
        final int count = result.size();
        final EgovPaginatedList pagedResults = new EgovPaginatedList(resultPage, count);
        return pagedResults;
    }

    @Transactional
    private Criteria buildNameInclusionCriteria(final Date fromDate, final Date toDate, final String regType) {
        final Criteria criteria = getSession().createCriteria(BndPaymentReport.class);
        if (fromDate != null && toDate != null)
            criteria.add(Restrictions.ge("receiptDate", fromDate));
        if (toDate != null)
            criteria.add(Restrictions.le("receiptDate", toDate));
        criteria.add(Restrictions.eq("feeTypeCode", BndConstants.NAMEINCLUSIONFEE));

        if (regType != null && BndConstants.NAMEINCLUSIONCOMPLETED.equals(regType))
            criteria.add(Restrictions.isNotNull("nameChangeId"));
        else
            criteria.add(Restrictions.isNull("nameChangeId"));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    @Transactional
    public Boolean validateReceiptNumber(final Long registrationid, final String receiptNum) {

        final Criteria criteria = getSession().createCriteria(BndPaymentReport.class);

        if (registrationid != null)
            criteria.add(Restrictions.eq("reportid", registrationid));
        if (receiptNum != null && !"".equals(receiptNum))
            criteria.add(Restrictions.ilike("receiptNumber", receiptNum));

        criteria.add(Restrictions.ilike("feeTypeCode", BndConstants.NAMEINCLUSIONFEE));
        criteria.add(Restrictions.ilike("type", BndConstants.BIRTH));
        if (bndCommonService.getAppconfigValue(BndConstants.BNDMODULE, BndConstants.UPDATENAME, "0"))
            criteria.add(Restrictions.isNull("nameChangeId"));

        return criteria.list().isEmpty();

    }

    public void setBndCommonService(final BndCommonService bndCommonService) {
        this.bndCommonService = bndCommonService;
    }
}
