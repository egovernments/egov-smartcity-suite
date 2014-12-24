package org.egov.pims.utils;

import org.egov.pims.commons.Position;

/**
 * this interface is the central location for the apis used from other dependent projects Like Egi,Egf
 * and used for workflow
 * @author DivyaShree 
 *
 */
public interface EisExternalInterface 
{
	 public  Position getSuperiorPositionByObjType(Position position,String obType);
}
