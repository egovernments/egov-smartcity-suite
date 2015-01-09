/**
 * 
 */
package org.egov.infstr.reporting.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.models.AbstractPersistenceServiceTest;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the report utility class
 */
public class ReportUtilTest extends
		AbstractPersistenceServiceTest<UserImpl, Long> {
	private UserImpl user;

	private int getRandomNumber() {
		final Random ran = new Random();
		return ran.nextInt();
	}

	private UserImpl createUser(final String userName) {
		final UserImpl user = new UserImpl();
		user.setUserName(userName + getRandomNumber());
		user.setFirstName(userName);
		user.setPwd("testpassword");
		user.setPwdModifiedDate(new Date());
		user.setIsActive(1);
		this.session.saveOrUpdate(user);
		return user;
	}

	@Before
	public void prepare() {
		this.user = createUser("testUser");
		this.session.flush();
	}

	@Test
	public void testFetchFromDBSql() {
		final Session session = HibernateUtil.getCurrentSession();
		session.doWork(new Work() {
			@Override
			public void execute(final Connection connection)
					throws SQLException {
				String userNameActual = (String) ReportUtil.fetchFromDBSql(
						connection,
						"select user_name from EG_USER where user_name = '"
								+ ReportUtilTest.this.user.getUserName() + "'");
				assertEquals(ReportUtilTest.this.user.getUserName(),
						userNameActual);

				userNameActual = (String) ReportUtil
						.fetchFromDBSql(connection,
								"select user_name from EG_USER where user_name = 'xyzabc85726450274pqr'");
				assertNull(userNameActual);

			}
		});
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testFetchFromDBSqlException() {
		final Session session = HibernateUtil.getCurrentSession();
		session.doWork(new Work() {
			@Override
			public void execute(final Connection connection)
					throws SQLException {
				ReportUtil
						.fetchFromDBSql(connection,
								"select user_nameXYZABC from EG_USERXYZABC where user_nameXYZABC = 'XYZABC'");
			}
		});
	}

	// @Test
	public void testGetImageAsStream() throws IOException {
		final InputStream inputStream = ReportUtil
				.getImageAsStream("egov-logo.gif");
		assertNotNull(inputStream);
		inputStream.close();
	}

	// @Test
	public void testGetLogoImageAsStream() {
		final Session session = HibernateUtil.getCurrentSession();
		session.doWork(new Work() {
			@Override
			public void execute(final Connection connection)
					throws SQLException {
				final String domainName = EGOVThreadLocals.getDomainName();
				EGOVThreadLocals.setDomainName("localhost");
				final InputStream inputStream = ReportUtil
						.getLogoImageAsStream(connection);
				// revert original domain name
				EGOVThreadLocals.setDomainName(domainName);
				assertNotNull(inputStream);
				try {
					inputStream.close();
				} catch (final IOException e) {
					e.printStackTrace();
					assertNotNull(null);
				}
			}
		});
	}

	@Test(expected = EGOVRuntimeException.class)
	public void testGetLogoImageAsStreamException() {
		final Session session = HibernateUtil.getCurrentSession();
		session.doWork(new Work() {
			@Override
			public void execute(final Connection connection)
					throws SQLException {
				final String domainName = EGOVThreadLocals.getDomainName();
				EGOVThreadLocals.setDomainName("xyzabcpqr");
				final InputStream inputStream = ReportUtil
						.getLogoImageAsStream(connection);
				// revert original domain name
				EGOVThreadLocals.setDomainName(domainName);
				assertNotNull(inputStream);
				try {
					inputStream.close();
				} catch (final IOException e) {
					e.printStackTrace();
					assertNotNull(null);
				}
			}
		});
	}

	@Test
	public void testGetDate() {
		final int dayExpected = 01;
		final int monthExpected = 02;
		final int yearExpected = 2000;

		final Date actualDate = ReportUtil.getDate(yearExpected, monthExpected,
				dayExpected);
		final Calendar calendarActual = Calendar.getInstance();
		calendarActual.setTime(actualDate);

		assertEquals(dayExpected, calendarActual.get(Calendar.DAY_OF_MONTH));
		assertEquals(monthExpected, calendarActual.get(Calendar.MONTH));
		assertEquals(yearExpected, calendarActual.get(Calendar.YEAR));
	}

	@Test
	public void testAdd() {
		final Date today = ReportUtil.today();
		final Date expectedTomorrow = ReportUtil.tomorrow();
		final Date actualTomorrow = ReportUtil.add(today,
				Calendar.DAY_OF_MONTH, 1);

		final Calendar calendarExpected = Calendar.getInstance();
		final Calendar calendarActual = Calendar.getInstance();
		calendarExpected.setTime(expectedTomorrow);
		calendarActual.setTime(actualTomorrow);

		assertEquals(calendarExpected.get(Calendar.DAY_OF_MONTH),
				calendarActual.get(Calendar.DAY_OF_MONTH));
		assertEquals(calendarExpected.get(Calendar.MONTH),
				calendarActual.get(Calendar.MONTH));
		assertEquals(calendarExpected.get(Calendar.YEAR),
				calendarActual.get(Calendar.YEAR));
	}

	@Test
	public void testAmountInWords() {
		final BigDecimal num = BigDecimal.valueOf(123.45);
		assertEquals(
				"Rupees One Hundred Twenty Three and Forty Five Paise Only",
				ReportUtil.amountInWords(num));
	}
}
