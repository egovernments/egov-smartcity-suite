/*
 * Created on Jul 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.eGov.src.common.EGovernCommon;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EgovMasterDataCaching;
import java.util.Locale;
/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Bank  
{
   private String id=null;
   private String code=null;
   private String name=null;
   private String narration=null;
   private String isActive="1";
   private String lastModified=null;
   private String created=null;
   private String modifiedBy=null;
   private String type=null;
   EGovernCommon cm=new EGovernCommon();
   private static final Logger LOGGER=Logger.getLogger(Bank.class);
   private TaskFailedException taskExc;
   private SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
   private SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
   private String insertQuery,updateQuery="Update Bank set ";
   private boolean isId=false,isField=false;
   public void setId(String id1){id=id1; isId=true;}
   public void setCode(String code1){code=cm.assignValue(code1,code);updateQuery=updateQuery+" code="+code+"";isField=true;}
   public void setName(String name1){name=cm.assignValue(name1,name);updateQuery=updateQuery+" name="+name+"";isField=true;}
   public void setNarration(String narration1){LOGGER.info("nar:"+narration1);narration=cm.assignValue(narration1,narration);LOGGER.info("nar:"+narration);updateQuery=updateQuery+" narration="+narration+"";isField=true;}
   public void setIsActive(String active){isActive=cm.assignValue(active,isActive);updateQuery=updateQuery+" isActive="+isActive;isField=true;}
   public void setLastModified(String lastModified1){lastModified=cm.assignValue(lastModified1,lastModified);updateQuery=updateQuery+" lastModified="+lastModified+"";isField=true;}
   public void setCreated(String created1){created=cm.assignValue(created1,created);updateQuery=updateQuery+" created="+created+"";isField=true;}
   public void setModifiedBy(String modifiedBy1){modifiedBy=cm.assignValue(modifiedBy1,modifiedBy);updateQuery=updateQuery+" modifiedBy="+modifiedBy+"";isField=true;}
   public void setType(String type1){type=cm.assignValue(type1,type);updateQuery=updateQuery+" type="+type+"";isField=true;}

   Statement st=null;
   public String getId() {return id; }
   
   public String insert(Connection con)throws TaskFailedException,SQLException
   {
       setId(String.valueOf(PrimaryKeyGenerator.getNextKey("Bank")));
       if(isId && isField)
       {
           EGovernCommon common=new EGovernCommon();
           try
           {
               created=common.getCurrentDate(con);
               created=formatter.format(sdf.parse(created));
               lastModified=created;
               EgovMasterDataCaching.getInstance().removeFromCache("egf-bank");
           
	           setCreated(created);
	           setLastModified(lastModified);
	           setCode(id);
	           //narration=common.formatString(narration);
		           insertQuery="insert into Bank (id,code,Name,Narration,isActive,LastModified,Created,ModifiedBy,Type)"+
		           "values("+id+","+code+","+name+","+narration+","+isActive+","+lastModified+","+created+","+
		           modifiedBy+","+type+")";
	           LOGGER.debug("B4 insertion: "+insertQuery);
	           st=con.createStatement();
	           st.executeUpdate(insertQuery);
	           LOGGER.debug(insertQuery);
           }catch(Exception e){
        	   LOGGER.error("Exp in insert"+e.getMessage());
        	   throw taskExc;
           }finally{
   			try{
   				st.close();   				
   			}catch(Exception e){LOGGER.error("Here in finally insert");}
   			}
           return id;
       }
       else 
       {
           LOGGER.debug("Unable to insert values into Bank Table");
           return "0";
       }
   }
   public void update(Connection con)throws TaskFailedException,SQLException
   {
       if(isId && isField)
       {
           EGovernCommon common=new EGovernCommon();
           try
           {
               created=common.getCurDateTime(con);
               created=formatter.format(sdf.parse(created));
               lastModified=created;
               EgovMasterDataCaching.getInstance().removeFromCache("egf-bank");
	           setLastModified(lastModified);
	           updateQuery=updateQuery+" where id="+id;
	           st=con.createStatement();
	           LOGGER.debug(updateQuery);
	           st.executeUpdate(updateQuery);
	       }catch(Exception e){throw taskExc;}
           finally{
      			try{
      				st.close();   				
      			}catch(Exception e){LOGGER.error("Here in finally update");}
      		}
           
       }
       else {LOGGER.debug("Set id and at least another one field");}
   }
}
