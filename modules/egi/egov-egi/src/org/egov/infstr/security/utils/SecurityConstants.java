/*
 * @(#)SecurityConstants.java 3.0, 18 Jun, 2013 4:01:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.utils;

public interface SecurityConstants {

	public static final String[] SQL_INJ_BLK_LIST = { "--", ";--", ";", "/*", "*/", "*", "@@", "@", "char", "nchar", "varchar", "nvarchar", "grant", "all", "union", "permissions", "alter", "begin", "cast", "create", "cursor", "declare", "delete", "drop",
			"end", "exec", "execute", "fetch", "insert", "kill", "join", "inner", "outer", "open", "select", "sys", "sysobjects", "syscolumns", "table", "update" };
	public static final String LOCATION_FIELD = "locationId";
	public static final String COUNTER_FIELD = "counterId";
	public static final String IPADDR_FIELD = "ipAddress";
	public static final String LOGINTYPE = "loginType";
	public static final String PWD_FIELD = "j_password";
	public static final String USERNAME_FIELD = "j_username";
	public static final String LOGIN_LOG_ID = "loginLogId";
	public static final String SSO_COMPLEATED = "sso_done";
	public static final String LOGIN_URI = "/login";
	public static final String PUBLIC_URI = "/public";
	
}
