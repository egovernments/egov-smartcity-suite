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

package org.egov.infstr.utils.seqgen;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.hibernate.JDBCException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper over a database sequence, providing access to its "nextval" with the option of 
 * automatically creating the sequence if it does not exist.
 * <p>
 * Typical usage is:
 * <p> With creation (first number defaults to 1): 
 * <p>
 * {@code 
 * long next = DatabaseSequence.named("my_sequence").createIfNecessary().nextVal();
 * }
 * <p> With creation (first number specified via input argument): 
 * <p>
 * {@code 
 * long next = DatabaseSequence.named("my_sequence").createIfNecessary().startingWith(17).nextVal();
 * }
 * <p> Without creation (will throw a runtime exception if the sequence does not exist): 
 * <p>
 * {@code 
 * long next = DatabaseSequence.named("my_sequence").nextVal();
 * }
 */
public class DatabaseSequence {
    /** These characters are not allowed in sequence names; regex for '/' or ' ' or '-' */
    private static final String DISALLOWED_CHARACTERS = "[\\/ -]";
    /** Error code for ORA-02289: sequence does not exist (NOTE: oracle specific!) */
    private static int SEQ_DOES_NOT_EXIST_ERROR_CODE = 2289;
    /** Error code for ORA-00972: identifier is too long (NOTE: oracle specific!) */
    private static int NAME_TOO_LONG_ERROR_CODE = 972;
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseSequence.class);
    
    public static String SEQUENCE_NAME_PREFIX = "SQ_";
    public static String WORD_SEPARATOR_FOR_NAME = "_";
    
    private String name;
    private boolean createIfNecessary = false;
    private long startWith = 1;
    private Session session;

    /** 
     * Private constructor; use the static factory method instead.
     * @param session
     */
    private DatabaseSequence(Session session) {
        this.session = session;
    }
    
    /**
     * Factory method to be used by clients; providing the name is mandatory.
     *
     * @param name
     * @param session
     */
    public static DatabaseSequence named(String name, Session session) {
        DatabaseSequence seq = new DatabaseSequence(session);
        seq.name = name;
        return seq;
    }
    
    /**
     * Sets an internal flag to indicate the sequence should be created if it does not exist.
     */
    public DatabaseSequence createIfNecessary() {
        createIfNecessary = true;
        return this;
    }
    
    /**
     * Sets the first number that should be returned after the sequence is created. The first
     * number returned defaults to 1 if this method is not utilized.
     */
    public DatabaseSequence startingWith(long number) {
        startWith = number;
        startWithMustBeAtLeastOne();
        return this;
    }
    
    /**
     * Returns the NEXTVAL of the sequence.
     */
    public long nextVal() {
        LOG.debug("DatabaseSequence.nextVal(): received request for sequence " + name);
        long nextVal = -1;
        String sql = "select nextval('" + name + "')";
        Query query = session.createSQLQuery(sql);
        try {
            nextVal = Long.valueOf(query.uniqueResult().toString()).longValue();
        } catch (JDBCException jdbce) {
            createAndFailIfAppropriate(jdbce);
        }
        LOG.debug("DatabaseSequence.nextVal(): returning value " + nextVal + " for sequence " + name);
        return nextVal;
    }
    
    /**
     * Recreates the sequence. The startingWith() method should be called
     * before calling this method (if required).
     */
    public void recreate() {
        throw new UnsupportedOperationException("UnsupportedOperationException: Sequence recreation is not yet supported!");
    }
    
    private void createAndFailIfAppropriate(JDBCException jdbce) {
        if (isExceptionDueToSequenceNotExists(jdbce)) {
            if (createIfNecessary) {
                createAndFail();
            } else {
                throw new DatabaseSequenceException("DatabaseSequenceException: Sequence does not exist: " + name, jdbce);
            }
        } else if (isExceptionDueToNameTooLong(jdbce)) {
            throw new DatabaseSequenceException("DatabaseSequenceException: Sequence name is too long: " + name, jdbce);
        }
        throw new ApplicationRuntimeException("ApplicationRuntimeException: Could not find/call sequence: " + name, jdbce);
    }
    
    private void createAndFail() {
        String createSql = new StringBuilder()
            .append("create sequence ")
            .append(name)
            .append(" nocache start with ")
            .append(startWith)
            .toString();
        Query query = session.createSQLQuery(createSql);
        query.executeUpdate();
        LOG.debug("DatabaseSequence.createAndFail(): created sequence " + name);
        throw new DatabaseSequenceFirstTimeException(
                "DatabaseSequenceFirstTimeException: Created sequence from scratch! Please try again: " + name);
    }
    
    private void drop() {
        String dropSql = "drop sequence " + name;
        Query query = session.createSQLQuery(dropSql);
        query.executeUpdate();
        LOG.debug("DatabaseSequence.drop(): dropped sequence " + name);
    }

    private boolean isExceptionDueToSequenceNotExists(JDBCException e) {
        int errorCode = e.getSQLException().getErrorCode();
        return errorCode == SEQ_DOES_NOT_EXIST_ERROR_CODE;
    }

    private boolean isExceptionDueToNameTooLong(JDBCException e) {
        int errorCode = e.getSQLException().getErrorCode();
        return errorCode == NAME_TOO_LONG_ERROR_CODE;
    }
    
    public static String replaceBadChars(String sequenceNameWithBadChars) {
        return sequenceNameWithBadChars.replaceAll(
                DISALLOWED_CHARACTERS, WORD_SEPARATOR_FOR_NAME);
    }
    
    /**
     * If clients pass in 0 or negative values, this will silently change the value to 1.
     */
    private void startWithMustBeAtLeastOne() {
        if (startWith < 1) {
            startWith = 1;
        }
    }
}

