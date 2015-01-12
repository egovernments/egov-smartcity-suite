/*
 * @(#)DatabaseSequence.java 3.0, 18 Jun, 2013 12:29:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils.seqgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.JDBCException;
import org.hibernate.Query;

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

    /** 
     * Private constructor; use the static factory method instead. 
     */
    private DatabaseSequence() {
    }
    
    /**
     * Factory method to be used by clients; providing the name is mandatory.
     *
     * @param name
     */
    public static DatabaseSequence named(String name) {
        DatabaseSequence seq = new DatabaseSequence();
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
        Query query = HibernateUtil.getCurrentSession().createSQLQuery(sql);
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
        throw new EGOVRuntimeException("EGOVRuntimeException: Could not find/call sequence: " + name, jdbce);
    }
    
    private void createAndFail() {
        HibernateUtil.rollbackTransaction();
        HibernateUtil.beginTransaction();
        String createSql = new StringBuilder()
            .append("create sequence ")
            .append(name)
            .append(" nocache start with ")
            .append(startWith)
            .toString();
        Query query = HibernateUtil.getCurrentSession().createSQLQuery(createSql);
        query.executeUpdate();
        LOG.debug("DatabaseSequence.createAndFail(): created sequence " + name);
        throw new DatabaseSequenceFirstTimeException(
                "DatabaseSequenceFirstTimeException: Created sequence from scratch! Please try again: " + name);
    }
    
    private void drop() {
        String dropSql = "drop sequence " + name;
        Query query = HibernateUtil.getCurrentSession().createSQLQuery(dropSql);
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

