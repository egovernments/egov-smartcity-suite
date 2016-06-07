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

package org.egov.adtax.utils;

import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.utils.DBSequenceGenerator;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.utils.ApplicationNumberGenerator;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.SQLException;

@Service
@Transactional(readOnly = true)
public class AdTaxNumberGenerator {
    private static final String ADVERTISEMENT_NUMBER_SEQ_PREFIX = "SEQ_ADVERTISEMENT_NUMBER";
    private static final String PERMIT_NUMBER_SEQ_PREFIX = "SEQ_PERMIT_NUMBER";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DBSequenceGenerator dbSequenceGenerator;

    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;

    @Autowired
    private ApplicationNumberGenerator applicationNumberGenerator;

    @Transactional
    public String generateAdvertisementNumber() {
        try {
            final String sequenceName = ADVERTISEMENT_NUMBER_SEQ_PREFIX;
            Serializable sequenceNumber;
            try {
                sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
            } catch (final SQLGrammarException e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            }
            return String.format("%s%06d", ApplicationThreadLocals.getCityCode(), sequenceNumber);
        } catch (final SQLException e) {
            throw new ApplicationRuntimeException("Error occurred while generating Advertisement Number", e);
        }
    }

    @Transactional
    public String generatePermitNumber() {
        try {
            final String sequenceName = PERMIT_NUMBER_SEQ_PREFIX;
            Serializable sequenceNumber;
            try {
                sequenceNumber = sequenceNumberGenerator.getNextSequence(sequenceName);
            } catch (final SQLGrammarException e) {
                sequenceNumber = dbSequenceGenerator.createAndGetNextSequence(sequenceName);
            }
            return String.format("%s%06d", ApplicationThreadLocals.getCityCode(), sequenceNumber);
        } catch (final SQLException e) {
            throw new ApplicationRuntimeException("Error occurred while generating Permit Number", e);
        }
    }

    @Transactional
    public String generateApplicationNumber() {
        return applicationNumberGenerator.generate();
    }
}
