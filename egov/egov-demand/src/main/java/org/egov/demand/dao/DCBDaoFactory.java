/*
 * Created on Oct 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.demand.dao;

import org.apache.log4j.Logger;
import org.egov.demand.model.EgBillDetails;
import org.egov.infstr.utils.EGovConfig;

public abstract class DCBDaoFactory {

    private static final Logger logger = Logger.getLogger(DCBDaoFactory.class);
    private static final DCBDaoFactory EJB3_PERSISTENCE = null;
    private static final DCBDaoFactory HIBERNATE = new DCBHibernateDaoFactory();
    private static final DCBDaoFactory retFac = resolveDaoFactory();

    public static DCBDaoFactory getDaoFactory() {
        return retFac;
    }

    public static DCBDaoFactory resolveDaoFactory() {
        String method = EGovConfig.getProperty("DCB-FACTORY-IMPL", "HIBERNATE", "PTIS");
        if (method != null) {
            if (method.trim().equalsIgnoreCase("HIBERNATE")) {
                return HIBERNATE;
            } else {
                return EJB3_PERSISTENCE;
            }
        }
        return null;
    }

    // Add your Dao interfaces here

    public abstract DepreciationMasterDao getDepreciationMasterDao();

    public abstract EgReasonCategoryDao getEgReasonCategoryDao();

    public abstract EgDemandDao getEgDemandDao();

    public abstract EgBillDao getEgBillDao();

    public abstract EgDemandReasonDao getEgDemandReasonDao();

    public abstract EgBillDetailsDao getEgBillDetailsDao();

    public abstract EgBillReceiptDao getEgBillReceiptDao();

    public abstract EgDemandDetailsDao getEgDemandDetailsDao();

    public abstract EgDemandReasonMasterDao getEgDemandReasonMasterDao();

    public abstract EgdmCollectedReceiptDao getEgdmCollectedReceiptsDao();

}
