/*
 * Created on Feb 25, 2005 
 * @author pushpendra.singh 
 */

package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;

import com.exilant.exility.updateservice.PrimaryKeyGenerator;

public class GeneralLedgerDetail {
	private String id = null;
	private String glId = null;
	private String detailKeyId = null;
	private String detailTypeId = null;	
	private String detailAmt="0";
	private static final Logger LOGGER=Logger.getLogger(GeneralLedgerDetail.class);
	
	public void setId(String aId){ id = aId; }
	public void setGLId(String aGLId){ glId = aGLId; }
	public void setDetailKeyId(String aDetailKeyId){ detailKeyId = aDetailKeyId; }
	public void setDetailTypeId(String aDetailTypeId){ detailTypeId = aDetailTypeId; }
	public void setDetailAmt(String aDetailAmt){ detailAmt = aDetailAmt; }
	
	public int getId() {return Integer.valueOf(id).intValue(); }
	public String getGLId(){ return glId ;}
	public String getDetailKeyId(){return detailKeyId ; }
	public String getDetailTypeId(){ return detailTypeId; }
	public String getDetailAmt(){ return detailAmt; }
	
	public void insert(Connection connection) throws SQLException
	{						
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("GeneralLedgerDetail")) );
		
		String insertQuery = "INSERT INTO GeneralLedgerDetail (id, generalLedgerId, detailKeyId, detailTypeId,amount) " +		 
								"VALUES ( ?, ?, ?, ?, ?)";		
		
	      Query pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
	      pst.setString(1, id);
	      pst.setString(2, glId);
	      pst.setString(3, detailKeyId);
	      pst.setString(4, detailTypeId);
	      pst.setString(5, detailAmt);
	      pst.executeUpdate();
	      if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
	}
}
