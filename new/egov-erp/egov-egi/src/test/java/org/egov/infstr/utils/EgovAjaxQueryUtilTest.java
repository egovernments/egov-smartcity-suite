package org.egov.infstr.utils;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EgovAjaxQueryUtilTest {
	private EgovHibernateTest hibernateSupport;

	@Before
	public void setUp() throws Exception {
		this.hibernateSupport = new EgovHibernateTest();
		this.hibernateSupport.setUp();
	}

	@After
	public void tearDown() throws Exception {
		this.hibernateSupport.tearDown();
	}

	@Test(expected = EGOVRuntimeException.class)
	public void queryWithParamsNull() {
		final AjaxQueryUtil queryUtil = new AjaxQueryUtil();
		final Map queryParams = new HashMap();
		queryUtil.executeAjaxQuery(null, "parentquery", "SelectModule",
				queryParams);
	}

	@Test
	public void queryWithParams() {
		final AjaxQueryUtil queryUtil = new AjaxQueryUtil();
		final Map queryParams = new HashMap();
		final List l = queryUtil.executeAjaxQuery("egov_config.xml",
				"parentquery", "SelectModule", queryParams);
		assertNotNull(l);
	}

	@Test(expected = EGOVRuntimeException.class)
	public void queryValueNull() {
		final AjaxQueryUtil queryUtil = new AjaxQueryUtil();
		final Map queryParams = new HashMap();
		queryUtil.executeAjaxQuery("egov_config.xml", "parentquery",
				"SelectModule1", queryParams);
	}

	@Test
	public void queryWithQueryParamsNull() {
		final AjaxQueryUtil queryUtil = new AjaxQueryUtil();
		final Map queryParams = null;
		final List results = queryUtil
				.evaluateQuery(
						"SELECT ID_MODULE, MODULE_NAME, MODULE_DESC FROM EG_MODULE WHERE PARENTID IS NULL ORDER BY MODULE_NAME",
						queryParams);
		assertNotNull(results);
	}

	@Test
	public void queryExec() {
		final AjaxQueryUtil queryUtil = new AjaxQueryUtil();
		final Map queryParams = new HashMap();
		queryParams.put("parentId", "1");
		final List results = queryUtil
				.evaluateQuery(
						"SELECT ID_MODULE, MODULE_NAME, MODULE_DESC FROM EG_MODULE WHERE PARENTID = :parentId ORDER BY MODULE_NAME",
						queryParams);
		assertNotNull(results);
	}

	@Test
	public void queryExecForText() {
		final AjaxQueryUtil queryUtil = new AjaxQueryUtil();
		final Map queryParams = new HashMap();
		queryParams.put("text", "admin%".toUpperCase());
		final List results = queryUtil
				.evaluateQuery(
						"SELECT ID_MODULE, MODULE_NAME, MODULE_DESC FROM EG_MODULE WHERE PARENTID IS NULL AND upper(MODULE_NAME) LIKE :text ORDER BY MODULE_NAME",
						queryParams);
		assertNotNull(results);
	}

}
