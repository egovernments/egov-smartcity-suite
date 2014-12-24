/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.pims.commons.dao;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.util.Set;

import org.egov.commons.ObjectType;
import org.egov.infstr.dao.GenericDAO;
import org.egov.pims.commons.Position;

public interface PositionHeirarchyDAO extends GenericDAO
{
	public Set getSetOfPosHeirForObjType(Integer objTypeId);
	public Position getHigherPos(Position position,ObjectType obType); 
	public Position getLowerPos(Position position,ObjectType obType);
}
