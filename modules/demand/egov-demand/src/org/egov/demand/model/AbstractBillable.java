package org.egov.demand.model;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.demand.interfaces.Billable;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

/**
 * A skeleton implementation of Billable, that should be extended by all Billable implementations.
 */
public abstract class AbstractBillable implements Billable {

    private static final String NEXT_BILL_NUM_FROM_SEQ = "SELECT SEQ_EG_BILL_BILLNO.NEXTVAL FROM DUAL";

    /**
     * Generates a number to be used as the ID of a new bill.
     */
    @Override
    public String getReferenceNumber() {
        try {
            Query q = HibernateUtil.getCurrentSession().createSQLQuery(NEXT_BILL_NUM_FROM_SEQ);
            return q.uniqueResult().toString();
        } catch (Exception e) {
            throw new EGOVRuntimeException("Could not generate new bill no", e);
        }
    }

}
