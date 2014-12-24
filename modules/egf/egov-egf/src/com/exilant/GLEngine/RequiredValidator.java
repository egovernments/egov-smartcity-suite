/*
 * Created on Feb 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.GLEngine;

import java.util.HashMap;
import java.util.Iterator;

import com.exilant.exility.dataservice.DataExtractor;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RequiredValidator {
	
	private HashMap loadAccDetKey(int detailId) throws TaskFailedException{
		HashMap	 accKeyMap=new HashMap();
		DataExtractor de=DataExtractor.getExtractor();
		String sql="select detailKey as \"detailKey\" ,detailName as \"detailName\","+
		"glCodeID as \"glCodeID\",groupID as \"groupID\",ID as \"ID\" from accountdetailkey where detailTypeId="+String.valueOf(detailId);
		accKeyMap=de.extractIntoMap(sql,"ID",AccountDetailKey.class);
		return accKeyMap;
	}
	public boolean validateKey(int detailId,String keyToValidate) throws TaskFailedException{
		HashMap accKeyMap=loadAccDetKey(detailId);
		Iterator it=accKeyMap.keySet().iterator();
		while(it.hasNext()){
			AccountDetailKey accKey=(AccountDetailKey)accKeyMap.get(it.next());
			if(accKey.getDetailKey().equalsIgnoreCase(keyToValidate))
				return true;
		}
		return false;
	}
}
