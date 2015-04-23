package com.exilant.exility.common;

public class ExilityParameters {

	public static final boolean definitionsToBeCached = false;
	public static final boolean loadFromXML = true;
	public static final String DATE_FORMAT = "dd-MMM-yyyy";
	public static final String DATE_TIME_FORMAT = "mm/dd/yyyy HH24:MI:ss";// dd-MMM-yyyy HH:mm:ss:fff
	public static final String USER_ID_NAME = "current_userID";
	public static final String SURROGATE_KEY_NAME = "ID";
	public static final String MODIFIED_TIMESTAMP_NAME = "lastModified";
	public static final String MODIFIED_USER_NAME	 = "userMasterID";
	public static final String CREATED_USER_NAME	 = "";
	public static final String CRAETED_TIMESTAMP_NAME = "created";
	public static final String ACTIVE_NAME = "isActive";
	public static final boolean logSQLs = true;
	public static final String SEARCH_RESULT_TABLE_NAME = "searchResult";

	/**
	 * 
	 */
	private ExilityParameters() {
	}


}
