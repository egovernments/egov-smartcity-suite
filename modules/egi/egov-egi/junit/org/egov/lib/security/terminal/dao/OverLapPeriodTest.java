package org.egov.lib.security.terminal.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.security.terminal.model.UserCounterMap;

public class OverLapPeriodTest extends EgovHibernateTest {

	public final Logger logger = Logger.getLogger(getClass());

	public void testOverLapPeriodTest() {

		final UserCounterDAO objDao = new UserCounterHibernateDAO(
				UserCounterMap.class, HibernateUtil.getCurrentSession());
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		final String fdate = "24/09/2008";
		final String tDate = null;
		java.util.Date fromDate = null;
		java.util.Date toDate = null;

		try {
			if (fdate != null && !fdate.equals("")) {
				fromDate = sdf.parse(fdate.trim());
			}

			if (tDate != null && !tDate.equals("")) {
				toDate = sdf.parse(tDate.trim());
			}
		} catch (final ParseException e) {
		}

		final boolean b = objDao.checkUserCounter(new Integer(2), fromDate,
				toDate);
		assertEquals(false, b);

	}

}
