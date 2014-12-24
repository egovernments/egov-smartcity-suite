package com.exilant.exility.dataservice;

/*
 * This object is loaded using XML loader. 
 * 
 * An SQL string is obtained by conatenating sql1 + key + sql3 
 */
 public class DescriptionSQLNotUsed{


	public String id;
	public String sql1;
	public String sql3;
	
	public DescriptionSQLNotUsed(){
		super();
	}
	
	public String getSQL(String keyValue)
	{
		return this.sql1 + keyValue + this.sql3;
	}
}
