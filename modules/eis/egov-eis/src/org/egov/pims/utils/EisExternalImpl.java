package org.egov.pims.utils;


import org.egov.pims.commons.Position;

/**
 * used for workflow
 * @author DivyaShree
 *
 */
public class EisExternalImpl implements EisExternalInterface 
{
	public  Position getSuperiorPositionByObjType(Position position,String obType){
		 return EisManagersUtill.getEisCommonsService().getSuperiorPositionByObjType(position, obType); 
	 }
}
