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
package org.egov.ptis.client.util;

import org.egov.commons.dao.InstallmentDao;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.DatabaseSequenceProvider;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.ptis.autonumber.AssessmentNumberGenerator;
import org.egov.ptis.autonumber.NoticeNumberGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class PropertyTaxNumberGenerator {
    private static final String SEQ_EG_BILL = "SEQ_EG_BILL";
    @Autowired
    private DatabaseSequenceProvider databaseSequenceProvider;
    @Autowired
    private ModuleService moduleDao;
    @Autowired
    private InstallmentDao installmentDao;
    @Autowired
    private CityService cityService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    public String generateNoticeNumber(final String noticeType) {
    	NoticeNumberGenerator noticeNumberGen = beanResolver.getAutoNumberServiceFor(NoticeNumberGenerator.class); 
    	return noticeNumberGen.generateNoticeNumber(noticeType);
    }

    public String generateBillNumber(final String wardNo) {
        final StringBuffer billNo = new StringBuffer();
        final Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
        // FIX ME
        /*
         * String index = sequenceNumberGenerator.getNextNumberWithFormat(
         * BILLGEN_SEQNAME_PREFIX + wardNo, 7, '0', Long.valueOf(1))
         * .getFormattedNumber(); billNo.append(wardNo); billNo.append("/");
         * billNo.append(index); billNo.append("/");
         * billNo.append(finYear.getDescription());
         */
        return billNo.toString();
    }

    /**
     * @param propertyID
     * @return billNumber
     */
    public String generateManualBillNumber(final PropertyID propertyID) {
        final StringBuffer billNo = new StringBuffer();
        try {
            // reading from service to support bulkbillgeneration through
            // schedular
            billNo.append("B").append("/");
            final String cityCode = cityService.findAll().get(0).getCode();
            billNo.append(cityCode);
            final String bill = databaseSequenceProvider.getNextSequence(SEQ_EG_BILL).toString();
            billNo.append(org.apache.commons.lang.StringUtils.leftPad(bill, 6, "0"));
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Exception : " + e.getMessage(), e);
        }
        return billNo.toString();
    }

    public String generateRecoveryNotice(final String noticeType) {
        final StringBuffer noticeNo = new StringBuffer();
        return noticeNo.toString();
    }

    public String generateAssessmentNumber() {
        AssessmentNumberGenerator assessmentNoGen = beanResolver.getAutoNumberServiceFor(AssessmentNumberGenerator.class);
        return assessmentNoGen.generateAssessmentNumber();
    }

    public String generateMemoNumber() {
        return "";
    }

    public String getRejectionLetterSerialNum() {
        return "";
    }

    public String generateUnitIdentifierPrefix() {
        /*
         * return sequenceNumberGenerator
         * .getNextNumber(UNIT_IDENTIFIER_SEQ_STR, 1).getFormattedNumber();
         */
        return null;
    }

    public void setDatabaseSequenceProvider(final DatabaseSequenceProvider databaseSequenceProvider) {
        this.databaseSequenceProvider = databaseSequenceProvider;
    }

}
