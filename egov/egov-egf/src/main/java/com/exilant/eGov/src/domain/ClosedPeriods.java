/*
 * Created on Jun 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ClosedPeriods
{
	private String id =null;
	private String startingDate = "";
	private String endingDate = "";
	private String isClosed = null;
	PreparedStatement psmt=null;
	private TaskFailedException taskExc;
	private String updateQuery="UPDATE closedPeriods SET ";
	private boolean isId=false, isField=false;
	private static final Logger LOGGER=Logger.getLogger(ClosedPeriods.class);
	
	public void setId(String aId){ id = aId; isId=true;}
	public int getId() {return Integer.valueOf(id).intValue(); }
	public void setStartingDate(String aStartingDate){ startingDate = aStartingDate; isField = true;}
	public void setEndingDate(String aEndingDate){ endingDate = aEndingDate;  isField = true;}
	public void setIsClosed(String aIsClosed){ isClosed = aIsClosed;isField = true;}
	
	public void insert(Connection connection) throws SQLException{	
		setId( String.valueOf(PrimaryKeyGenerator.getNextKey("ClosedPeriods")) );		
		String insertQuery = "INSERT INTO closedPeriods (id, startingDate, endingDate, isClosed) " +						 
						    "VALUES(?,?,?,?)";
						
		psmt = connection.prepareStatement(insertQuery);
	    psmt.setString(1,id);
	    psmt.setString(2,startingDate);
	    psmt.setString(3,endingDate);
	    psmt.setString(4,isClosed);
		if(LOGGER.isInfoEnabled())     LOGGER.info(insertQuery);
		psmt.executeQuery();
		psmt.close();
	}
	
	public void update (Connection connection) throws SQLException
	{
		if(isId && isField)
		{
			try{
			newUpdate(connection);
			}catch(Exception e){
				LOGGER.error("Error while updating close period");
			}
			
		}
	}
	
	public void newUpdate(Connection con) throws TaskFailedException,SQLException{
	 StringBuilder query=new StringBuilder(500); 
	 PreparedStatement pstmt=null;
	 query.append("update closedPeriods set ");
	 if(startingDate!=null)	query.append("startingDate=?,");
	 if(endingDate!=null)	query.append("endingDate=?,");
	 if(isClosed!=null)	query.append("isClosed=?,");
	 int lastIndexOfComma = query.lastIndexOf(",");
	query.deleteCharAt(lastIndexOfComma);
	query.append(" where id=?");
	 try{ int i=1;
	 pstmt=con.prepareStatement(query.toString());
	 if(startingDate!=null)	pstmt.setString(i++,startingDate);
	 if(endingDate!=null)	pstmt.setString(i++,endingDate);
	 if(isClosed!=null)	pstmt.setString(i++,isClosed);
	pstmt.setString(i++,id);

	 pstmt.executeQuery();
	}catch(Exception e){	LOGGER.error("Exp in update: "+e.getMessage()); throw taskExc;} finally{try{ pstmt.close(); }catch(Exception e){LOGGER.error("Inside finally block of update");}} 
	 }

	static public boolean isClosedForPosting(String date, Connection conn)throws TaskFailedException{
		boolean isClosed = true;
		String chkqry=null;
		Query psmt=null;
		Query psmt1=null;
		//Date dateTyp=null;
		try{
		//	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
    	//		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
			//dateTyp = (Date)formatter.parse(date);
				
			chkqry="SELECT id FROM financialYear " +
			"WHERE startingDate<=?  AND endingDate>=? AND isActiveForPosting=1";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Before excuting "+chkqry);
			psmt =HibernateUtil.getCurrentSession().createSQLQuery(chkqry);
			psmt.setString(1,date);
			List<Object[]> rs =	psmt.setString(2,date).list();
			
			if(rs!=null && rs.size()>0) isClosed = false;
			
			if(!isClosed){
				rs=null;
				String qry="SELECT id FROM closedPeriods WHERE to_char(startingDate, 'DD-MON-YYYY')<=? AND endingDate>=?";
				if(LOGGER.isDebugEnabled())     LOGGER.debug(qry);
				psmt1 =HibernateUtil.getCurrentSession().createSQLQuery(qry);
				psmt1.setString(1,date);
				psmt1.setString(2,date);
				rs = psmt1.list();
				
				if(!(rs!=null && rs.size()>0))
				{
					isClosed = false;
				}else
				{
					isClosed = true;
				}
			}
						
		}catch (HibernateException e) {
			isClosed = true;
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new HibernateException(e.getMessage()));
		}catch (Exception e) {
			isClosed = true;
			LOGGER.error("Exception occured while getting the data  "+e.getMessage(),new Exception(e.getMessage()));
		}
		return isClosed;
	}
	
}

