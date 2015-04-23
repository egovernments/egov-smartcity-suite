package com.exilant.exility.dataservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;


import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.ExilityParameters;
import com.exilant.exility.common.ObjectGetSetter;
import com.exilant.exility.common.TaskFailedException;

/**
 * TODO :
 * 1. RS shpuld not go forward and back. We should use ArrayList and convert to Array.
 * 2. set the right property of rs/con for optimized read-forward-only operation
 * 3. Date format: How is it taken care of? Is that taken care of in writing SQLs?
 *
 * Executes a SQL statement and extracs the results into a dataCollection
 * This is a singleton
 */

public class DataExtractor {
	private static final Logger LOGGER = Logger.getLogger(DataExtractor.class);
	private static DataExtractor singletonInstance;
	//private static Connection con;

	public static DataExtractor getExtractor (){
		if  (singletonInstance == null){
			singletonInstance = new DataExtractor();
		//	con = DBHandler.getConnection();
		}
		return singletonInstance;
	}


	private DataExtractor() {
		super();
	}


	public void extract(String sql,
						String gridName,
						DataCollection dc,
						Connection con,
						boolean errorOnNoData,
						boolean addColumnHeading,
						String prefix ) throws TaskFailedException {
if(LOGGER.isInfoEnabled())     LOGGER.info(sql+"sqllll");
		if(sql == null || sql.length() == 0 )return;

		//temp code to test when database is not available
		if (ExilityParameters.logSQLs)
		{
			int i = dc.getInt("sqlCount");
			i++;
			dc.addValue("sqlCount", i);
			/* changed in egf
			 dc.addValue("sql" + i, sql);
			*/
		}
		if (null == con) {
			return;
		}

		int rowCount = 0;
		int columnCount = 0;
		ResultSetMetaData metaData = null;
		String[][] dataValues = null;

		try
		{

			PreparedStatement pststement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs=null;
			String[] columnNames=null;
			try{
				rs = pststement.executeQuery();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();//Gets the Column Count in the ResultSet
				columnNames= new String[columnCount];
			}catch(Exception e){
				LOGGER.error("Exception while analysing Result set in extract",e);
				return;
			}
			for(int m=0; m<columnCount; m++) columnNames[m] = prefix+metaData.getColumnName(m+1);
			if(null == gridName || gridName.length() == 0){ //only one row is expected
			  try{
				if (rs.next()){
					for(int m = 0; m < columnCount; m++){
						String mText=rs.getString(m+1);

						if(mText==null){
							mText="";
						}
						if(mText.indexOf("\r")>0){
							mText=mText.replaceAll("\r"," ");
							mText=mText.replaceAll("\n"," ");
							mText=mText.replaceAll("\f"," ");
						}
						dc.addValue(columnNames[m],mText);
					}
				}else{
					return;
					/*if(errorOnNoData){
						dc.addMessage("exilNoData");
						throw new TaskFailedException();
					}*/
				}
			  }catch(Exception e){
				  	LOGGER.error("Exception while analysing Result set in extract",e);
			  		return;
			  }
			}else{ // result to be put into a grid
				rowCount=-1;
				try{
					rs.next();
					rs.getString(1);
				}catch(Exception eee){
					LOGGER.error("Error while analysing Result set in extract",eee);
					rowCount=0;
				}
				try{
					if(rowCount==-1)
					{
						rs.last();
						rowCount =rs.getRow();
						rs.beforeFirst();				//brings back the cursor to the start in the ResultSet
					}
				}catch(Exception e){
						LOGGER.error("Error while analysing Result set in extract",e);
						rowCount=0;
				}
					/*if (rowCount == 0 && errorOnNoData) {
						dc.addMessage("exilNoData");
						throw new TaskFailedException();
					}*/
				int rowid;
				if (addColumnHeading){
					dataValues = new String[rowCount+1][columnCount]; //additional row for column headings
					for(int k =0; k<columnCount; k++){
						dataValues[0][k] = columnNames[k];
					}
					rowid = 1;
				}else{
					dataValues = new String[rowCount][columnCount];
					rowid = 0;
				}
				try{
						while(rs.next()){
							for(int j = 0; j < columnCount; j++){//executes number of column times
								String mText=rs.getString(j+1);

								if(  mText==null){
									mText="";
								}
								dataValues[rowid][j] = mText;
							}
							rowid++; // Row wise increment;
						}
				}catch(Exception e){
					LOGGER.error("Inside extract",e);
					try{
						if(gridName!=null&&dataValues!=null&&addColumnHeading){
							dc.addGrid(gridName,dataValues);
						}

					}catch(Exception ee){
						LOGGER.error("Error while adding grid in extract",ee);
					}
					return;
				}
				dc.addGrid(gridName,dataValues);
			}
			rs.close();
			pststement.close();
		}
		catch(SQLException e){
			dc.addMessage("exilSQLException", e.getMessage());
			LOGGER.error(e.getMessage(),e);
			throw new TaskFailedException();
		}
		return;
	}

	public HashMap extractIntoMap(String sql,
			String keyName,
			Class collectionMemberClass) throws TaskFailedException {
		//DBHandler handler = DBHandler.getHandler();
		/*Connection con = handler.getConnection();*/
		Connection con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
		HashMap map = extractIntoMap(sql, con, keyName,  collectionMemberClass);
		//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con,null);
		//handler.returnConnection(con);
		return map;
	}


	public HashMap extractIntoMap(String sql,
			Connection con,
			String keyName,
			Class collectionMemberClass) throws TaskFailedException {

		HashMap map =  new HashMap();
		int columnCount = 0;
		ResultSetMetaData metaData = null;

		try
		{
			PreparedStatement pststement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = pststement.executeQuery();
				metaData = rs.getMetaData();
				columnCount = metaData.getColumnCount();//Gets the Column Count in the ResultSet
				//int keyIndex = rs.findColumn(keyName);
				String val = null;
				while(rs.next())
				{
					Object object = collectionMemberClass.newInstance();
					for(int m = 0; m < columnCount; m++){
						String mText=rs.getString(m+1);
						if( mText==null){
							mText="";
						}
						if(metaData.getColumnName(m+1).equalsIgnoreCase(keyName))
							val = mText;
						ObjectGetSetter.set(object, metaData.getColumnName(m+1),mText);
					}
					map.put(val, object);
				}

				//rs.close();
				//ststement.close();
		}
		catch(SQLException e){
			LOGGER.error("Inside extractIntoMap",e);
			//throw new TaskFailedException("sql field " + sql);
			return new HashMap();
		} catch (InstantiationException e) {
			LOGGER.error("Inside extractIntoMap",e);
			throw new TaskFailedException("Object could not be instantiated for " + collectionMemberClass.getName());
		} catch (IllegalAccessException e) {
			LOGGER.error("Inside extractIntoMap",e);
			throw new TaskFailedException("Object could not be instantiated due to access issues " + collectionMemberClass.getName());
		}
		return map;
	}
}
