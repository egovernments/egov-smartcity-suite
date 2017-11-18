/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 * <p>
 * Copyright (C) <2015>  eGovernments Foundation
 * <p>
 * The updated version of eGov suite of products as by eGovernments Foundation
 * is available at http://www.egovernments.org
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/ or
 * http://www.gnu.org/licenses/gpl.html .
 * <p>
 * In addition to the terms of the GPL license to be adhered to in using this
 * program, the following additional terms are to be complied with:
 * <p>
 * 1) All versions of this program, verbatim or modified must carry this
 * Legal Notice.
 * <p>
 * 2) Any misrepresentation of the origin of the material is prohibited. It
 * is required that all modified versions of this material be marked in
 * reasonable ways as different from the original version.
 * <p>
 * 3) This license does not grant any rights to any user of the program
 * with regards to rights under trademark law for use of the trade names
 * or trademarks of eGovernments Foundation.
 * <p>
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.mrs.autonumber.impl;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.utils.ApplicationSequenceNumberGenerator;
import org.egov.mrs.autonumber.MarriageCertificateNumberGenerator;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class MarriageCertificateNumberGeneratorImpl implements MarriageCertificateNumberGenerator {

    private static final String CERTIFICATE_NUMBER_SEQ_PREFIX = "SEQ_EGMRS_CERTIFICATE_NUMBER";

    @Autowired
    private ApplicationSequenceNumberGenerator applicationSequenceNumberGenerator;

    @Override
    public String generateCertificateNumber(MarriageRegistration marriageRegistration) {
        final String sequenceName = CERTIFICATE_NUMBER_SEQ_PREFIX;
        final Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);
        return String.format("%s%06d", ApplicationThreadLocals.getCityCode(), nextSequence);
    }

    @Override
    public String generateCertificateNumber(ReIssue reIssue) {
        final String sequenceName = CERTIFICATE_NUMBER_SEQ_PREFIX;
        final Serializable nextSequence = applicationSequenceNumberGenerator.getNextSequence(sequenceName);
        return String.format("%s%06d", ApplicationThreadLocals.getCityCode(), nextSequence);
    }
}
