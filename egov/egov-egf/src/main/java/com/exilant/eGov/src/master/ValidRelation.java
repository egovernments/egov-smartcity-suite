/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jun 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

import java.sql.Connection;
import java.util.List;

import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
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
			Query pstmt=HibernateUtil.getCurrentSession().createSQLQuery(sql);
			pstmt.setString(1, relationID);
			List<Object[]> rset=pstmt.list();
			for(Object[] element : rset){
				oldRelTypeID=element[0].toString();
			}if(rset == null || rset.size() == 0){
				dc.addMessage("exilRPError","No Relation");
				throw new TaskFailedException();
			}
			if(oldRelTypeID.equals(newRelTypeID)){
				return;
			}
			String query = "select id from supplierbilldetail where supplierid= ?"+
			" union all select id from contractorbilldetail where contractorid= ?";
			Query pst=HibernateUtil.getCurrentSession().createSQLQuery(query);
			pst.setString(1, relationID);
			pst.setString(2, relationID);
			rset=pst.list();
			for(Object[] element : rset){
				dc.addMessage("exilRPError","Already Has Posting. Cant Modify Type of Relation");
				throw new TaskFailedException();
			}
		}catch(Exception e){
			dc.addMessage("exilRPError",e.toString());
			throw new TaskFailedException();
		}
		

	}
}
