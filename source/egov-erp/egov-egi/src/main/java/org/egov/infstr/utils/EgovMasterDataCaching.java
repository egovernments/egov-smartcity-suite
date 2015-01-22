/*
 * @(#)EgovMasterDataCaching.java 3.0, 18 Jun, 2013 12:02:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import org.egov.EgovSpringContextHolder;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.hibernate.Query;
import org.hibernate.Session;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infstr.utils.StringUtils.EMPTY;

public class EgovMasterDataCaching {

	private static final String SQL_QUERY_TYPE = "sql";
	private static final String HQL_QUERY_TYPE = "hql";
	private static final String PATH_DELIM = "/";
	private static final String SQL_TAG_PREFIX = "sql.";
	private static final String CONFIG_FILE_SUFFIX = "_sqlconfig.xml";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovMasterDataCaching.class);
	private static EmbeddedCacheManager CACHE_MANAGER;
	private static final class MDCInstanceHolder {
		//Lock free, Threadsafe singleton initialization for EgovMasterDatacaching
	   protected static final EgovMasterDataCaching MDC_INSTANCE = new EgovMasterDataCaching();
	}
	
	private EgovMasterDataCaching() {
		try {
			final Context context = new InitialContext();
			CACHE_MANAGER = (EmbeddedCacheManager) context.lookup("java:jboss/infinispan/container/master-data");
		} catch (NamingException e) {
			throw new EGOVRuntimeException("Error occurred while getting Cache Manager",e);
		}
	}
	
	/**
	 * Gets the single instance of EgovMasterDataCaching.
	 * @return single instance of EgovMasterDataCaching
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */
	public static EgovMasterDataCaching getInstance() throws EGOVRuntimeException {
		return MDCInstanceHolder.MDC_INSTANCE;
	}

	/**
	 * This method load the data for given sqlTagName and puts it in Cache.
	 * @param sqlTagName the sql tag name
	 * @return List
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	public List get(final String sqlTagName) throws EGOVRuntimeException {
		final String temp[] = sqlTagName.split("-");
		final String domainName = EGOVThreadLocals.getDomainName();
		final String applName = temp[0];
		List<Object> dataList = null;
		HashMap<String,Object> cacheValuesHashMap = new HashMap<String,Object>();

		try {
			cacheValuesHashMap = (HashMap<String,Object>) CACHE_MANAGER.getCache().get(applName + PATH_DELIM + domainName + PATH_DELIM + sqlTagName);
			if (cacheValuesHashMap != null) {
				dataList = (List<Object>) cacheValuesHashMap.get(sqlTagName);
			}
			
			if (dataList == null || dataList.isEmpty()) {
				final String type = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "type", EMPTY, SQL_TAG_PREFIX+sqlTagName).trim();
				if (type.equalsIgnoreCase("java")) {
					final String className = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "class", EMPTY, SQL_TAG_PREFIX + sqlTagName );
					final String methodName = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "method", EMPTY, SQL_TAG_PREFIX + sqlTagName);
					final String parametertype = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "parametertype", EMPTY, SQL_TAG_PREFIX + sqlTagName);
					final String parametervalue = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "parametervalue", EMPTY, SQL_TAG_PREFIX + sqlTagName);
					if (isNotBlank(className) && isNotBlank(methodName)) {
						dataList = loadJavaAPIMasterDataList(className, methodName, parametertype.split(","), parametervalue.split(","));
					} else {
						throw new EGOVRuntimeException("ClassName and MethodName should be mentioned for " + type + " in " + applName + CONFIG_FILE_SUFFIX);
					}
				} else if (type.equalsIgnoreCase(HQL_QUERY_TYPE) || type.equalsIgnoreCase(SQL_QUERY_TYPE)) {
					final String query = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "query", EMPTY, SQL_TAG_PREFIX + sqlTagName);
					if (!query.equalsIgnoreCase(EMPTY)) {
						dataList = loadQLMasterData(query, type);
					} else {
						throw new EGOVRuntimeException("Query should be mentioned for " + type + " in " + applName + CONFIG_FILE_SUFFIX);
					}
				} else {
					throw new EGOVRuntimeException("This type (" + type + ") is not supported for " + sqlTagName);
				}
				final HashMap<String,Object> hm = new HashMap<String,Object>();
				hm.put(sqlTagName, dataList);
				CACHE_MANAGER.getCache().put(applName + PATH_DELIM + domainName + PATH_DELIM + sqlTagName, hm);
			} else {
				LOGGER.info("EgovMasterDataCaching: Got directly from cache, not from db");
			}

		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching", e);
		}
		return dataList;
	}

	/**
	 * This method load the data for given sqlTagName and puts it in Cache.
	 * @param sqlTagName the sql tag name
	 * @return Map
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	public Map getMap(final String sqlTagName) throws EGOVRuntimeException {
		Map dataMap = new HashMap();
		final String temp[] = sqlTagName.split("-");
		final String applName = temp[0];
		final String domainName = EGOVThreadLocals.getDomainName();
		final String type = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "type", EMPTY, SQL_TAG_PREFIX + sqlTagName).trim();
		try {
			if (type.trim().equalsIgnoreCase(SQL_QUERY_TYPE)) {
				final List dataList = get(sqlTagName);
				if (dataList != null) {
					final Iterator itr = dataList.iterator();
					LabelValueBean obj = null;
					while (itr.hasNext()) {
						obj = (LabelValueBean) itr.next();
						dataMap.put(Integer.toString(obj.getId()), obj.getName());
						obj = null;
					}
				}
			} else if (type.equalsIgnoreCase(HQL_QUERY_TYPE)) {
				throw new EGOVRuntimeException("getMap() is not supported for HQL query");
			} else if (type.equalsIgnoreCase("java")) {
				final String className = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "class", EMPTY, SQL_TAG_PREFIX + sqlTagName);
				final String methodName = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "method", EMPTY, SQL_TAG_PREFIX + sqlTagName);
				final String parametertype = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "parametertype", EMPTY, SQL_TAG_PREFIX + sqlTagName);
				final String parametervalue = EGovConfig.getProperty(applName + CONFIG_FILE_SUFFIX, "parametervalue", EMPTY, SQL_TAG_PREFIX + sqlTagName);
				if (isNotBlank(className) && isNotBlank(methodName)) {
					dataMap = loadJavaAPIMasterDataMap(className, methodName, parametertype.split(","), parametervalue.split(","));
				} else {
					throw new EGOVRuntimeException("ClassName and MethodName should be mentioned for " + type + " in " + applName + CONFIG_FILE_SUFFIX);
				}
				final HashMap<String,Object> hm = new HashMap<String,Object>();
				hm.put(sqlTagName, dataMap);
				CACHE_MANAGER.getCache().put(applName + PATH_DELIM + domainName + PATH_DELIM + sqlTagName, hm);
			} else {
				throw new EGOVRuntimeException("This type (" + type + ") is not supported for " + sqlTagName);
			}
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching getMap", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching getMap", e);
		}
		return dataMap;
	}

	/**
	 * This method removes the data from cache for given sqlTagName.
	 * @param sqlTagName the sql tag name
	 * @return void
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	public void removeFromCache(final String sqlTagName) throws EGOVRuntimeException {
		try {
			final String temp[] = sqlTagName.split("-");
			final String domainName = EGOVThreadLocals.getDomainName();
			final String applName = temp[0];
			CACHE_MANAGER.getCache().remove(applName + PATH_DELIM + domainName + PATH_DELIM + sqlTagName);
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching removeFromCache", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching removeFromCache", e);
		}
	}

	/**
	 * This method loads the data for Hql and Sql queries.
	 * @param query the query
	 * @param queryType the query type
	 * @return List
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private List loadQLMasterData(final String query, final String queryType) throws EGOVRuntimeException {
		List list = null;
		try {
			if (queryType.trim().equalsIgnoreCase(HQL_QUERY_TYPE)) {
				list = queryByHibernate(query);
			} else if (queryType.trim().equalsIgnoreCase(SQL_QUERY_TYPE)) {
				list = queryByJdbc(query);
			}

		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching loadQLMasterData", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching loadQLMasterData", e);
		}
		return list;
	}

	/**
	 * This method loads the data for type Java API.
	 * @param className the class name
	 * @param methodName the method name
	 * @param parametertype the parametertype
	 * @param parametervalue the parametervalue
	 * @return List
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private List loadJavaAPIMasterDataList(final String className, final String methodName, final String parametertype[], final String parametervalue[]) throws EGOVRuntimeException {
		List list = null;
		try {
			if (parametertype.length != parametervalue.length) {
				throw new EGOVRuntimeException("Number of parameter types and parameter values doesnt match");
			}
			final Class cls = Class.forName(className);
			final Method method = cls.getMethod(methodName, loadMethodParameter(parametertype));
			list = (List) method.invoke(cls.newInstance(), loadMethodArguments(parametertype, parametervalue));
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching loadJavaAPIMasterDataList", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching loadJavaAPIMasterDataList", e);
		}
		return list;
	}

	/**
	 * This method loads the data for type Java API.
	 * @param className the class name
	 * @param methodName the method name
	 * @param parametertype the parametertype
	 * @param parametervalue the parametervalue
	 * @return Map
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private Map loadJavaAPIMasterDataMap(final String className, final String methodName, final String parametertype[], final String parametervalue[]) throws EGOVRuntimeException {
		Map dataMap = new HashMap();
		try {
			if (parametertype.length != parametervalue.length) {
				throw new EGOVRuntimeException("Number of parameter types and parameter values doesnt match");
			}
			final Class cls = Class.forName(className);
			final Method method = cls.getMethod(methodName, loadMethodParameter(parametertype));
			dataMap = (HashMap) method.invoke(cls.newInstance(), loadMethodArguments(parametertype, parametervalue));
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching loadJavaAPIMasterDataMap", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching loadJavaAPIMasterDataMap", e);
		}
		return dataMap;
	}

	/**
	 * This method dynamically loads the parameters for a method i.e <parametertype>java.lang.String\,java.lang.Integer\,java.lang.String</parametertype>
	 * @param parametertype the parametertype
	 * @return Class[]
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private Class[] loadMethodParameter(final String parametertype[]) throws EGOVRuntimeException {
		Class[] class_name = null;
		try {
			if (!parametertype[0].trim().equalsIgnoreCase(EMPTY)) {
				class_name = new Class[parametertype.length];
				for (int i = 0; i < parametertype.length; i++) {
					class_name[i] = Class.forName(parametertype[i]);
				}
			}
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching loadMethodParameter", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching loadMethodParameter", e);
		}
		return class_name;
	}

	/**
	 * This method dynamically loads the arguments for a method i.e <parametervalue>1\,10\,11</parametervalue>
	 * @param parametertype the parametertype
	 * @param parametervalue the parametervalue
	 * @return Object[]
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private Object[] loadMethodArguments(final String parametertype[], final String parametervalue[]) throws EGOVRuntimeException {
		Object[] obj_name = null;
		try {
			if (!parametertype[0].trim().equalsIgnoreCase(EMPTY)) {
				obj_name = new Object[parametervalue.length];
				for (int i = 0; i < parametertype.length; i++) {
					if (parametertype[i].trim().equalsIgnoreCase("java.lang.Integer")) {
						obj_name[i] = Integer.valueOf(parametervalue[i]);
					} else if (parametertype[i].trim().equalsIgnoreCase("java.lang.Double")) {
						obj_name[i] = Double.valueOf(parametervalue[i]);
					} else if (parametertype[i].trim().equalsIgnoreCase("java.lang.String")) {
						obj_name[i] = parametervalue[i];
					} else {
						throw new EGOVRuntimeException("This " + parametertype[i] + " datatype is not supported");
					}
				}
			}
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching loadMethodArguments", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching loadMethodArguments", e);
		}
		return obj_name;
	}

	/**
	 * This method executes a hibernate query.
	 * @param query the query
	 * @return List
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private List queryByHibernate(final String query) throws EGOVRuntimeException {
		List list = null;

		try {
			final Query qry = getCurrentSession().createQuery(query);
			list = qry.list();
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching queryByHibernate", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching queryByHibernate", e);
		}
		return list;
	}

	private Session getCurrentSession() {
		return EgovSpringContextHolder.sessionFactory().getCurrentSession();
	}

	/**
	 * This method executes a sql query.
	 * @param query the query
	 * @return List
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private List queryByJdbc(final String query) throws EGOVRuntimeException {
		List resultlist = null;
		List returnList = null;
		try {
			resultlist = getCurrentSession().createSQLQuery(query).list();
			if (resultlist != null) {
				returnList = resultSetToArrayList(resultlist);
			}
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching queryByJdbc", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching queryByJdbc", e);
		}
		return returnList;
	}

	/**
	 * This method returns a list of LabelValueBean using the resultList object.
	 * @param resultList the rs
	 * @return List
	 * @throws EGOVRuntimeException the eGOV runtime exception
	 */

	private List resultSetToArrayList(final List<Object[]> resultList) throws EGOVRuntimeException {

		final List list = new ArrayList();
		LabelValueBean labelValueBean = null;
		BigDecimal id;
		try {
			for (final Object[] objArr : resultList) {
				labelValueBean = new LabelValueBean();
				id = (BigDecimal) objArr[0];
				labelValueBean.setId(id.intValue());
				labelValueBean.setName((String) objArr[1]);
				list.add(labelValueBean);
			}
		} catch (final Exception e) {
			LOGGER.error("Error occurred in EgovMasterDataCaching resultSetToArrayList", e);
			throw new EGOVRuntimeException("Error occurred in EgovMasterDataCaching resultSetToArrayList", e);
		}
		return list;
	}
}
