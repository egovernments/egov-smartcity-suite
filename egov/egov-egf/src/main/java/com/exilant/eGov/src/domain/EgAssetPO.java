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
 * Created on Sep 26, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class EgAssetPO {
	private static final Logger LOGGER=Logger.getLogger(EgAssetPO.class);
	Query pstmt=null;
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
    @Transactional
    public void insert()throws Exception,TaskFailedException 
    {
        String insertQuery="";
        Query st=null;
        EGovernCommon commommethods = new EGovernCommon();
        setId( String.valueOf(PrimaryKeyGenerator.getNextKey("eg_asset_po")) );
        lastUpdatedDate = commommethods.getCurrentDate();
        try{
	        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	        String currentdate = formatter.format(sdf.parse( lastUpdatedDate ));
	        setLastUpdatedDate(currentdate);
	        }
        catch(Exception e){
        	LOGGER.error("ERROR"+e.getMessage());
        	throw new TaskFailedException();
        }
        
        insertQuery="insert into eg_asset_po (id,assetid,workorderid,lastupdateddate) values(?,?,?,?)";
        pstmt=HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
        if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
        pstmt.setString(1,id);
        pstmt.setString(2,assetId);
        pstmt.setString(3,workOrderId);
        pstmt.setString(4,lastUpdatedDate);
        pstmt.executeUpdate();
    }
    @Transactional
    public void update()throws SQLException
    {
        if(isId && isField)
        {
            updateQuery=updateQuery+" WHERE ID= ?";
            if(LOGGER.isInfoEnabled())     LOGGER.info(updateQuery);
            pstmt=HibernateUtil.getCurrentSession().createSQLQuery(updateQuery);
            pstmt.setString(1,id);
            pstmt.executeUpdate();
            updateQuery="update eg_asset_po set ";
        }
    }
}
