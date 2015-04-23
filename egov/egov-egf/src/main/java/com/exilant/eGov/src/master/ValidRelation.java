/*
 * Created on Jun 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

import java.sql.*;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidRelation extends AbstractTask{
	public void execute(String taskName,
			String gridName,
			DataCollection dc, 
			Connection conn,
			boolean errorOnNoData,
			boolean gridHasColumnHeading, String prefix) throws TaskFailedException{
		String relationID=dc.getValue("relation_ID");
		try{
			String oldRelTypeID="";
			String newRelTypeID=dc.getValue("relation_relationTypeId");
			String sql="select relationtypeid from relation where id= ?";
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, relationID);
			ResultSet rset=pstmt.executeQuery();
			if(rset.next()){
				oldRelTypeID=rset.getString(1);
			}else{
				dc.addMessage("exilRPError","No Relation");
				throw new TaskFailedException();
			}
			if(oldRelTypeID.equals(newRelTypeID)){
				return;
			}
			String query = "select id from supplierbilldetail where supplierid= ?"+
			" union all select id from contractorbilldetail where contractorid= ?";
			PreparedStatement pst=conn.prepareStatement(query);
			pst.setString(1, relationID);
			pst.setString(2, relationID);
			rset=pst.executeQuery();
			if(rset.next()){
				dc.addMessage("exilRPError","Already Has Posting. Cant Modify Type of Relation");
				throw new TaskFailedException();
			}
		}catch(Exception e){
			dc.addMessage("exilRPError",e.toString());
			throw new TaskFailedException();
		}
		

	}
}
