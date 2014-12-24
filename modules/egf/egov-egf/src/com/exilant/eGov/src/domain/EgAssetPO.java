/*
 * Created on Sep 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import java.text.SimpleDateFormat;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EgAssetPO {
	private static final Logger LOGGER=Logger.getLogger(EgAssetPO.class);
    private String id=null;
    private String assetId=null;
    private String workOrderId=null;
    private String lastUpdatedDate="";
    private String updateQuery="update eg_asset_po set ";
    private boolean isId=false, isField=false;
    public void setId(String sId){id=sId; isId=true;}
    public void setAssetId(String sAssetId){assetId=sAssetId;updateQuery=updateQuery+" assetid="+assetId+" "; isField=true;}
    public void setWorkOrderId(String sWorkOrderId){workOrderId=sWorkOrderId;updateQuery=updateQuery+" workOrderId="+workOrderId+" ";isField=true;}
    public void setLastUpdatedDate(String sLastUpdatedDate){lastUpdatedDate=sLastUpdatedDate; updateQuery=updateQuery+" lastupdateddate='"+lastUpdatedDate+"' ";isField=true;}
    
    public void insert(Connection con)throws SQLException,TaskFailedException 
    {
        String insertQuery="";
        Statement st=null;
        EGovernCommon commommethods = new EGovernCommon();
        setId( String.valueOf(PrimaryKeyGenerator.getNextKey("eg_asset_po")) );
        lastUpdatedDate = commommethods.getCurrentDate(con);
        try{
	        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	        String currentdate = formatter.format(sdf.parse( lastUpdatedDate ));
	        setLastUpdatedDate(currentdate);
	        }
        catch(Exception e){throw new TaskFailedException();}
       
        st=con.createStatement();
        insertQuery="insert into eg_asset_po (id,assetid,workorderid,lastupdateddate) values("+id+","+assetId+","
                            +workOrderId+",'"+lastUpdatedDate+"')";
        LOGGER.info(insertQuery);
        st.executeUpdate(insertQuery);
        LOGGER.debug(insertQuery);
    }
    
    public void update(Connection con)throws SQLException
    {
        if(isId && isField)
        {
            updateQuery=updateQuery+" WHERE ID="+id;
            LOGGER.info(updateQuery);
            Statement st=con.createStatement();
            st.executeUpdate(updateQuery);
            st.close();
            updateQuery="update eg_asset_po set ";
        }
    }
}
