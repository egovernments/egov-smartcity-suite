/*
 * @(#)DatabaseSequenceException.java 3.0, 18 Jun, 2013 12:29:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils.seqgen;

import org.egov.exceptions.EGOVRuntimeException;

public class DatabaseSequenceException extends EGOVRuntimeException {
    private static final long serialVersionUID = 1L;
    
    public DatabaseSequenceException(String string, Throwable t) {
        super(string, t);
    }

}
