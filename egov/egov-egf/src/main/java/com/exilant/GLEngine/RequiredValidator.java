/*
 * Created on Feb 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.GLEngine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

/**
 * @author siddhu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * This class caches employee data for a transaction and clear at the end of each transaction 
 * 
 */
public class RequiredValidator {
  final static Logger LOGGER=Logger.getLogger(RequiredValidator.class);
  static	HashMap	 employeeMap=null;
  static int empDetailId=0;  	

 /**
  * 
  */
  public static void clearEmployeeMap()
	{
		if(LOGGER.isInfoEnabled())     LOGGER.info("...................................................................................Clearing the employeeMap");
		if(employeeMap!=null)
		{
		employeeMap=null;
		}
		empDetailId=0;
	}
	public boolean isEmployee(int detailId)
	{
		if(empDetailId==0)
		{
			List list =HibernateUtil.getCurrentSession().createQuery("select id From Accountdetailtype where name='Employee'").list();
			if(list!=null && list.size()>0)
			{
			empDetailId =(Integer) list.get(0);
			}
		}
		if(empDetailId==detailId)
			return true;
		else
			return false;
	}
	/**
	 * 
	 * @param detailId
	 * @return
	 * @throws TaskFailedException
	 * if detailType is employee return the cache object else 
	 * load new map
	 */
	private HashMap loadAccDetKey(int detailId) throws TaskFailedException{
		if(isEmployee(detailId))
		{
			if(employeeMap==null)
			{
				if(LOGGER.isInfoEnabled())     LOGGER.info(".................................................................................Creating employeeMap Cache");
				DataExtractor de=DataExtractor.getExtractor();
				String sql="select detailKey as \"detailKey\" ,detailName as \"detailName\","+
				"glCodeID as \"glCodeID\",groupID as \"groupID\",ID as \"ID\" from accountdetailkey where detailTypeId="+String.valueOf(detailId);
				employeeMap=de.extractIntoMap(sql,"ID",AccountDetailKey.class);
			}else
			{
				if(LOGGER.isInfoEnabled())     LOGGER.info("..................................................................................Loading Cached employeeMap");
			}
			return employeeMap;
				
		}else
		{
		HashMap	 accKeyMap=new HashMap();
		DataExtractor de=DataExtractor.getExtractor();
		String sql="select detailKey as \"detailKey\" ,detailName as \"detailName\","+
		"glCodeID as \"glCodeID\",groupID as \"groupID\",ID as \"ID\" from accountdetailkey where detailTypeId="+String.valueOf(detailId);
		accKeyMap=de.extractIntoMap(sql,"ID",AccountDetailKey.class);
		return accKeyMap;
		}
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
