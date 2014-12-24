/*
 * PropertyReferenceHibDAO.java
 * Created on May 13, 2007
 *
 * Copyright 2006 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.dao.property;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.hibernate.Session;

/**
 * @author suhasini CH
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class PropertyReferenceHibDAO extends GenericHibernateDAO implements
        PropertyReferenceDAO {
    /**
     * @param persistentClass
     * @param session
     */
    public PropertyReferenceHibDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
}
