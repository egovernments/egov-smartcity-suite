package com.exilant.exility.updateservice;
import org.apache.log4j.Logger;

import com.exilant.exility.common.*;
//import com.exilant.exility.dataservice.*;


/**
 * @author raghu.bhandi
 *
 * Defines an entity in terms of its attributes, and their validations
 * 
 * IMPORTANT : it is assuemd that the DC contains columns with name = tablename_columnname
 */
public class TableDefinition {
	private static final Logger LOGGER = Logger.getLogger(TableDefinition.class);
	public String name;
	boolean hasSurrogateKey = true;
	boolean hasCreatedTimestamp = true;
	boolean hasModifiedTimestamp = true;
	boolean hasCreatedUser = false;
	boolean hasModifiedUser = true;
	boolean hasActiveField = true;
	public String keyColumnName; // applicable if usesSurrogateKey is false
	public boolean keyRequiresQuote = false;
	public ColumnDefinition[] columnDefinitions;
	public boolean okToDelete = false;

	//constants named for better readability
	private final String FULL_DATETIME_FORMAT = "dd-mm-yyyy HH:mm:ss";
	private final String USER_ID_NAME = "current_UserID";
	private final String COMMA = ",";
	private final String QUOTE = "'";
	private final char CONNECTOR = '_';
	private final char EQUAL = '=';
	private final String now = "sysdate";
	public TableDefinition()
	{
		super();
	}
	
	public String getUpdateSQL(DataCollection dc) throws TaskFailedException
	{
		return getUpdateSQL(dc, -1); //rowid = -1 implies there are no lists. look invalues and not in valueLists
	}


	public String getDeleteSQL(DataCollection dc) throws TaskFailedException
	{
		return getDeleteSQL(dc, -1);
	}

	public String getDeleteSQL(DataCollection dc, int rowid) throws TaskFailedException
	{
		if (!this.okToDelete)
		{
			dc.addMessage("exilDeleteNotAllowed", this.name);
			throw new TaskFailedException();
		}

		String nameInDC = this.name + CONNECTOR + this.keyColumnName;
		String keyValue = null;
		if (rowid == -1)keyValue= dc.getValue(nameInDC);
		else
		{
			String[] keyValues = dc.getValueList(nameInDC);
			if (null != keyValues && rowid < keyValues.length)keyValue = keyValues[rowid];
		}
		
		if (null == keyValue || keyValue.length() == 0 || keyValue.equals("0"))
		{
			dc.addMessage("exilNoKeyForUpdate", this.name, this.keyColumnName,this.name + CONNECTOR + this.keyColumnName );
			throw new TaskFailedException();
		}

		StringBuffer sbf = new StringBuffer();
		sbf.append("DELETE ").append(this.name).append(" WHERE ").append(this.keyColumnName).append("=");
		if (this.keyRequiresQuote) sbf.append(QUOTE).append(keyValue).append(QUOTE);
		else sbf.append(keyValue);

		return sbf.toString();
	}


	public String getUpdateSQL(DataCollection dc, int rowid) throws TaskFailedException
	{
		if (this.columnDefinitions == null)
		{
			dc.addMessage("exilNoColumns", this.name);
			throw new TaskFailedException();
		}

		StringBuffer sbf = new StringBuffer("UPDATE ").append(this.name).append(" SET ");
		
		boolean valid = true; //set to false inthe for-loop if any field fails validation
		String columnValue = null;
		String nameInDC;
		ColumnDefinition column;
		int nbrColumns = 0; //actual number of columns added to the sql String in the loop
		String prefix = ""; //nothing in the beginning, but then, it will be changed to COMMA
		String[] valueList;

		for (int i = 0; i < columnDefinitions.length; i++)
		{
			column = columnDefinitions[i];
			nameInDC = this.name + CONNECTOR + column.name;
			if (rowid >= 0)
			{
				valueList = dc.getValueList(nameInDC);
				if (null != valueList && valueList.length > rowid) columnValue = valueList[rowid];
			} else { 
				if(!dc.hasName(nameInDC)) continue;
				columnValue = dc.getValue(nameInDC);
			}
			//if (null == columnValue || columnValue.length() == 0) continue; //no value? No problem... yet.
		/*	if (!column.isValid(columnValue))
			{
				valid = false;
				dc.addMessage("exilInvalidField", nameInDC, columnValue);
				// don't throw exception .. yet. Let us continue to validate all field values
				continue; 
			}*/
			nbrColumns++;
			sbf.append(prefix).append(column.name).append(EQUAL);
			if (null == columnValue || columnValue.length() == 0){
				sbf.append("null");
			}else if(column.requiresQuote){
				sbf.append(QUOTE).append(columnValue.replaceAll(QUOTE,"''")).append(QUOTE);
			}
			else{
				sbf.append(columnValue);
			}
			prefix = COMMA;
		} //end of loop for each column

		if (nbrColumns == 0)
		{
			valid = false;
			dc.addMessage("exilNothingToUpdate", this.name);
		}
		
		if (!valid)	throw new TaskFailedException();

		// append modified user and timestamp
		this.appendStandardUpdateValues(sbf, dc, rowid);
//if(LOGGER.isDebugEnabled())     LOGGER.debug("update sql in getupdate SQL >>>>>>>>>>>>>>" + sbf.toString());
		return sbf.toString();
	}
	
	//Method which forms insert query.
	public String getInsertSQL(DataCollection dc) throws TaskFailedException
	{
		return getInsertSQL(dc, -1); //rowid = -1 implies take from values, and not valueList
	}
	
	public String getInsertSQL(DataCollection dc, int rowid) throws TaskFailedException
	{
		
		if (columnDefinitions == null || columnDefinitions.length == 0)
		{
			dc.addMessage("exilNoColumns", this.name);
			throw new TaskFailedException();
		}
		StringBuffer sbfNames = new StringBuffer(); //buffer for adding the column names
		sbfNames.append("INSERT INTO ").append(this.name).append('(');

		StringBuffer sbfValues = new StringBuffer(); //buffer for adding the column values
		sbfValues.append(" VALUES ").append('(');
		
		boolean valid = true;
		String columnValue = null;
		String nameInDC;
		ColumnDefinition column;
		int nbrColumns = 0;
		String prefix = "";
		String[] valueList;

		for (int i = 0; i < columnDefinitions.length; i++)
		{
			column = columnDefinitions[i];
			nameInDC = this.name + CONNECTOR + column.name;
			if (rowid >= 0)
			{
				valueList = dc.getValueList(nameInDC);
				if (null != valueList && valueList.length >rowid) columnValue = valueList[rowid];
			}
			else columnValue = dc.getValue(nameInDC);				
			
			if (null == columnValue || columnValue.length() == 0) columnValue = column.defaultValue;
			/*if (!column.isValid(columnValue))
			{
				valid = false;
				dc.addMessage("exilInvalidField", nameInDC, columnValue);
				// dont throw exception... yet. Let us validate all fields before doing that
				continue;
			}*/
if(LOGGER.isDebugEnabled())     LOGGER.debug("column =========== " + i);			
			nbrColumns ++;
			sbfNames.append(prefix).append(column.name);
			sbfValues.append(prefix);

			if (column.requiresQuote)
				sbfValues.append(QUOTE).append(columnValue.replaceAll(QUOTE, "''")).append(QUOTE);
			else
				sbfValues.append(columnValue);
			prefix = COMMA;
		}
		if (!valid) throw new TaskFailedException();
		
		if (nbrColumns == 0)
		{
			dc.addMessage("exilNothingToInsert", this.name);
			throw new TaskFailedException();
		}

		this.appendStandardInsertNames(sbfNames);
		this.appendStandardInsertValues(sbfValues, dc);
		sbfNames.append(')');
		sbfValues.append(')');

		return sbfNames.append(sbfValues).toString();
	}

	private StringBuffer appendStandardInsertNames(StringBuffer sbfNames)
	{
		if (this.hasSurrogateKey)
			sbfNames.append(COMMA).append(Tables.SURROGATE_KEY_NAME);

		if (this.hasCreatedTimestamp)
			sbfNames.append(COMMA).append(Tables.CRAETED_TIMESTAMP_NAME);

		if (this.hasCreatedUser)
			sbfNames.append(COMMA).append(Tables.CREATED_USER_NAME);

		if (this.hasModifiedTimestamp)
			sbfNames.append(COMMA).append(Tables.MODIFIED_TIMESTAMP_NAME);

		if (this.hasModifiedUser)
			sbfNames.append(COMMA).append(Tables.MODIFIED_USER_NAME);

		if (this.hasActiveField)
			sbfNames.append(COMMA).append(Tables.ACTIVE_NAME);

		return sbfNames;
	}

	
	private StringBuffer appendStandardInsertValues(StringBuffer sbfValues, DataCollection dc)
	{
		//SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATETIME_FORMAT);
		
		//String now = QUOTE + dateFormat.format(new Date()) + QUOTE;
		
		String user = dc.getValue(this.USER_ID_NAME);

		if (this.hasSurrogateKey)
			sbfValues.append(COMMA).append(PrimaryKeyGenerator.getNextKey(this.name));

		if (this.hasCreatedTimestamp)
			sbfValues.append(COMMA).append(now);

		if (this.hasCreatedUser)
			sbfValues.append(COMMA).append(user);

		if (this.hasModifiedTimestamp)
			sbfValues.append(COMMA).append(now);

		if (this.hasModifiedUser)
			sbfValues.append(COMMA).append(user);

		if (this.hasActiveField)
			sbfValues.append(COMMA).append(1);
		return sbfValues;
	}

	private StringBuffer appendStandardUpdateValues(StringBuffer sbf, DataCollection dc, int rowid) throws TaskFailedException
	{
		String key = null;
		String nameInDC = this.name + CONNECTOR + this.keyColumnName;

		if(rowid < 0)key = dc.getValue(nameInDC);
		else
		{
			String[] keys = dc.getValueList(nameInDC);
			if (null != keys && keys.length > rowid) key = keys[rowid];
		}
		
		if (key == null || key.length() == 0 || key.equals("0"))
		{
			dc.addMessage("exilNoKeyForUpdate", this.name, this.keyColumnName, this.name + CONNECTOR + this.keyColumnName);
			throw new TaskFailedException();
		}

		//SimpleDateFormat dateFormat = new SimpleDateFormat(FULL_DATETIME_FORMAT);
		//String now = QUOTE + dateFormat.format(new Date()) + QUOTE;
		String user = dc.getValue(this.USER_ID_NAME);

		if (this.hasModifiedTimestamp)
			sbf.append(COMMA).append(Tables.MODIFIED_TIMESTAMP_NAME).append(EQUAL).append(now);

		if (this.hasModifiedUser)
			sbf.append(COMMA).append(Tables.MODIFIED_USER_NAME).append(EQUAL).append(user);
		
		if (this.hasActiveField )
		{
			String fieldName = this.name + CONNECTOR +Tables.ACTIVE_NAME;
			if (dc.hasName(fieldName))
				sbf.append(COMMA).append(Tables.ACTIVE_NAME).append(EQUAL).append(dc.getValue(fieldName));
			else
				sbf.append(COMMA).append(Tables.ACTIVE_NAME).append(EQUAL).append('0');
		}

		sbf.append(" WHERE ").append(this.keyColumnName).append(EQUAL);
		if (this.keyRequiresQuote)sbf.append(QUOTE).append(key).append(QUOTE);
		else sbf.append(key);

		if (this.hasModifiedTimestamp)
		{
			String stamp = null;
			String stampName = this.name + CONNECTOR + Tables.MODIFIED_TIMESTAMP_NAME;
			if (rowid < 0){
				stamp = dc.getValue(stampName);
			}
			else 
			{
				String[] stamps = dc.getValueList(stampName);
				if (null != stamps && stamps.length > rowid) stamp = stamps[rowid];
			}

			if (stamp == null || stamp.length() == 0 || stamp.equals("0"))
			{
				dc.addMessage("exilNoTimeStamp", this.name, Tables.MODIFIED_TIMESTAMP_NAME, this.name + CONNECTOR + Tables.MODIFIED_TIMESTAMP_NAME);
				throw new TaskFailedException();
			}
			/* used when we used to connect using dsn
			 	stamp="to_date("+QUOTE+stamp+QUOTE+COMMA+QUOTE+"yyyy-mm-dd HH24:MI:SS"+QUOTE+")";
		 	*/	
			stamp="to_date("+QUOTE+stamp+QUOTE+COMMA+QUOTE+"mm/dd/yyyy HH24:MI:SS"+QUOTE+")";
			sbf.append(" AND ").append(Tables.MODIFIED_TIMESTAMP_NAME).append(EQUAL);
			//sbf.append(QUOTE).append(stamp).append(QUOTE);
			sbf.append(stamp);
		}
		return sbf;
	}
	
	// in the XML, dataType is specified as a String (like signedDecimal et..)
	// However, DataType is more effecient with the numeric code for that.
	// Let us do this translation once and for all... 
	public void  optimize()
	{
		int length = this.columnDefinitions.length;
		for (int i = 0; i < length; i++)
		{
			this.columnDefinitions[i].optimize();
		}
	}
}



