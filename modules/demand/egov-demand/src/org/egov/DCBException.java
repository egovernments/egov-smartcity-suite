package org.egov;

import org.apache.log4j.Logger;

public class DCBException extends RuntimeException {

    private static final long serialVersionUID = 1118286182458257153L;
    public final Logger logger = Logger.getLogger(getClass());

    public DCBException(String msg) {
        super(msg);
        logger.error(msg);
    }

    public DCBException(String msg, Throwable th) {
        super(msg, th);
        logger.error(msg, th);
    }

    public DCBException(Throwable cause) {
        super(cause);
        logger.error(cause);
    }

}
