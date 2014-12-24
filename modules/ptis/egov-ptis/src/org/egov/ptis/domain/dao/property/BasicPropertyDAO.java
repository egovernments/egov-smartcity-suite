/*
 * BasicPropertyDAO.java Created on Oct 5, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyID;

/**
 * <p>
 * This is an interface which would be implemented by the Individual Frameworks
 * for all the CRUD (create, read, update, delete) basic data access operations
 * for Basic Property Entity
 *
 * @author Neetu
 * @version 2.00
 */

public interface BasicPropertyDAO extends org.egov.infstr.dao.GenericDAO {
	public BasicProperty getBasicPropertyByRegNum(String RegNum) ;

	public BasicProperty getBasicPropertyByPropertyID(PropertyID propertyID);

	public BasicProperty getBasicPropertyByPropertyID(String propertyId);

	public BasicProperty getInActiveBasicPropertyByPropertyID(String propertyID);

	//public BasicProperty getBasicPropertyByID(String ID);
	public BasicProperty getBasicPropertyByID_PropertyID(String ID_PropertyID);

	public BasicProperty getBasicPropertyByRegNumNew(String RegNum);

	public Integer getRegNum();

	public Integer getVoucherNum();

	public List getBasicPropertyByOldMunipalNo(String oldMuncipalNo);

	public BasicProperty getAllBasicPropertyByPropertyID(String propertyId);

	public List<BasicPropertyImpl> getChildBasicPropsForParent(BasicProperty basicProperty);

	public BasicProperty getBasicPropertyByIndexNumAndParcelID(String indexNum, String parcelID);
}
