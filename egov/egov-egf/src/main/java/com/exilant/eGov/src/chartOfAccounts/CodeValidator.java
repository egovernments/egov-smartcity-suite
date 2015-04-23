package com.exilant.eGov.src.chartOfAccounts;

import java.util.HashMap;
import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.GLAccount;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;
public class CodeValidator {
	//private static HashMap glFilterList;
	//public static HashMap glAccountCodes;
	static CodeValidator singletonInstance = new CodeValidator();;
	private static final String ROOTNODE = "/CODEVLDTR";
	//private static final String gLAccCodeNode ="GlAccountCodes";
	private static final String GLFILTERCODE ="GlFilterCodes";
	//This fix is for Phoenix Migration.
	/*private static MBeanServer server=MBeanServerLocator.locateJBoss();
	private static TreeCacheMBean cache;
	private static final Logger LOGGER = Logger.getLogger(CodeValidator.class);*/

	static
	{//This fix is for Phoenix Migration.
		/*try
		{
			cache=(TreeCacheMBean)MBeanProxyExt.create(TreeCacheMBean.class, "jboss.cache:service=TreeCache", server);

		} catch (MalformedObjectNameException e)
		{
			// TODO Auto-generated catch block
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}*/
	}


   private CodeValidator(){

   }


   public static  CodeValidator getInstance()throws TaskFailedException{
	 //This fix is for Phoenix Migration.
	  /* if(LOGGER.isInfoEnabled())     LOGGER.info("CodeValidator getInstance() called for "+EGOVThreadLocals.getDomainName()+"and singletonInstance == null"+singletonInstance == null);
		try {
		//if(LOGGER.isInfoEnabled())     LOGGER.info("cache.get(rootNode/"+EGOVThreadLocals.getDomainName()+"/"+gLFilterCode+"::::::"+cache.get(rootNode+"/"+EGOVThreadLocals.getDomainName(),gLFilterCode));
		if(cache.get(ROOTNODE+"/"+EGOVThreadLocals.getDomainName(),GLFILTERCODE) == null)
		{
			if(LOGGER.isInfoEnabled())     LOGGER.info("calling loadFilterData::for "+EGOVThreadLocals.getDomainName());
			loadFilterData();
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
		throw new TaskFailedException();
	}*/

	return singletonInstance;
   }
   private static void loadFilterData()throws TaskFailedException{
   	DataExtractor de=DataExtractor.getExtractor();
	String sql="select a.id as \"ID\",a.serviceid as \"serviceID\",b.glcode as  \"glCode\" "+
				" from codeservicemap a,chartofaccounts b"+
				" where a.glcodeid=b.id order by a.id,serviceid ";
	//glFilterList=de.extractIntoMap(sql,"ID",FilterCodeList.class);
	//This fix is for Phoenix Migration.
	/*try
	{
		//if(LOGGER.isInfoEnabled())     LOGGER.info("Cache put called for:"+rootNode+"/"+FilterName.get()+"/"+gLFilterCode);
		HashMap hmap = de.extractIntoMap(sql,"ID",FilterCodeList.class);
		//if(LOGGER.isInfoEnabled())     LOGGER.info("hash map inserted is:"+hmap);
		cache.put(ROOTNODE+"/"+EGOVThreadLocals.getDomainName(),GLFILTERCODE,hmap);


		//sql="select ID as \"ID\", glCode as \"glCode\" ,name as \"name\" ," +
		//"isactiveforposting as \"isActiveForPosting\"  from chartofaccounts ";
		//glAccountCodes=de.extractIntoMap(sql,"glCode",GLAccount.class);
		ChartOfAccounts coa = ChartOfAccounts.getInstance();
		coa.reLoadAccountData();

	}
	catch (TaskFailedException e)
	{
		// TODO Auto-generated catch block
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
		throw new EGOVRuntimeException(e.getMessage());
	}
	catch (Exception e)
	{
		// TODO Auto-generated catch block
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
		throw new EGOVRuntimeException(e.getMessage());
    }
*/
}
   public boolean isValidGLCode(final String pServiceID,final String pGlCode){
   	 boolean valid=false;
	 //Iterator it=glFilterList.keySet().iterator();

   	 HashMap hm;
	/*try
	{
		hm = (HashMap)cache.get(ROOTNODE+"/"+EGOVThreadLocals.getDomainName(),GLFILTERCODE);
	} catch (Exception e)
	{
		// TODO Auto-generated catch block
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Exp="+e.getMessage());
		throw new EGOVRuntimeException(e.getMessage());
	}
	//if(LOGGER.isInfoEnabled())     LOGGER.info("glcode:"+pGlCode);
	//if(LOGGER.isInfoEnabled())     LOGGER.info("looked up str:"+rootNode+"/"+FilterName.get()+gLFilterCode);
   	// if(LOGGER.isInfoEnabled())     LOGGER.info("hashmap is:"+hm);
    // if(LOGGER.isInfoEnabled())     LOGGER.info("hashmap keyset is:"+hm.keySet());
   	 Iterator it=hm.keySet().iterator();

   	 while(it.hasNext()){
   	 		FilterCodeList fList=(FilterCodeList)hm.get(it.next());
   	 		if(fList.getServiceID().equalsIgnoreCase(pServiceID) && fList.getGLCode().equalsIgnoreCase(pGlCode)){
   	 			valid=true;
   	 			break;
   	 		}
  	 }*/
	 return valid;
   }
   public boolean isValidCode(final String pGlCode){
   		boolean valid=false;
 		//GLAccount fList=(GLAccount)glAccountCodes.get(pGlCode);
   		GLAccount fList=(GLAccount)ChartOfAccounts.getGlAccountCodes().get(pGlCode);
  	 	if(fList!=null){
  	 			valid=true;
  	 	}
  	 	return valid;
  }
  public void reloadMappedCodes() throws TaskFailedException{
	//glFilterList.clear();
	//glAccountCodes.clear();
	loadFilterData();
  }
}
