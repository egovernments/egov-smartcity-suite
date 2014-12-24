/*
 * PropMutationMasterDAO.java
 * Created on May 12, 2007
 *
 * Copyright 2006 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.infstr.dao.GenericDAO;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;

/**
 * @author Administrator
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public interface PropertyMutationMasterDAO extends GenericDAO {
public List getAllPropertyMutationMastersByType(String type);

public PropertyMutationMaster getPropertyMutationMasterByCode(String code);
}
