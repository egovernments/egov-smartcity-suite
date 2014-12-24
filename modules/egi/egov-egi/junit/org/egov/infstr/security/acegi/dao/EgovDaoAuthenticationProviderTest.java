package org.egov.infstr.security.acegi.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Hashtable;

import org.egov.infstr.security.spring.dao.EgovDaoAuthenticationProvider;
import org.egov.lib.security.terminal.dao.UserValidateDAO;
import org.egov.lib.security.terminal.model.Location;
import org.egov.lib.security.terminal.model.UserValidate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class EgovDaoAuthenticationProviderTest {

	EgovDaoAuthenticationProvider dao;
	UserValidateDAO userValidateDao;

	@Before
	public void setUp() {
		this.dao = new EgovDaoAuthenticationProvider();
		this.userValidateDao = mock(UserValidateDAO.class);
		this.dao.setUserValidateDao(this.userValidateDao);
	}

	@Test
	public void testAuthentication() {
		final UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getPassword()).thenReturn("password");
		final UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
		final Hashtable<String, String> credentialsTable = new Hashtable<String, String>();
		credentialsTable.put("j_password", "password");
		when(token.getCredentials()).thenReturn(credentialsTable);
		when(this.userValidateDao.validateActiveUserForPeriod(anyString()))
				.thenReturn(true);
		// dao.additionalAuthenticationChecks(userDetails, token);

	}

	@Test
	public void testAuthenticationWithInactiveUser() {
		final UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getPassword()).thenReturn("password");
		final UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
		final Hashtable<String, String> credentialsTable = new Hashtable<String, String>();
		credentialsTable.put("j_password", "password");
		when(token.getCredentials()).thenReturn(credentialsTable);
		when(this.userValidateDao.validateActiveUserForPeriod(anyString()))
				.thenReturn(false);
		// dao.additionalAuthenticationChecks(userDetails, token);
	}

	@Test
	public void testAuthenticationWithLocation() {
		final UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getPassword()).thenReturn("password");
		final UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
		final Hashtable<String, String> credentialsTable = new Hashtable<String, String>();
		credentialsTable.put("j_password", "password");
		credentialsTable.put("loginType", "Location");
		credentialsTable.put("locationId", "1");
		credentialsTable.put("counterId", "2");
		credentialsTable.put("ipAddress", "127.0.0.1");
		when(token.getCredentials()).thenReturn(credentialsTable);

		when(this.userValidateDao.validateActiveUserForPeriod(anyString()))
				.thenReturn(true);
		when(this.userValidateDao.getLocationByIP("127.0.0.1")).thenReturn(
				new Location());

		when(this.userValidateDao.validateUserLocation(any(UserValidate.class)))
				.thenReturn(true);

		// dao.additionalAuthenticationChecks(userDetails, token);
	}

	@Test
	public void testAuthenticationWithInvalidLocation() {
		final UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getPassword()).thenReturn("password");
		final UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
		final Hashtable<String, String> credentialsTable = new Hashtable<String, String>();
		credentialsTable.put("j_password", "password");
		credentialsTable.put("loginType", "Location");
		credentialsTable.put("locationId", "1");
		credentialsTable.put("counterId", "2");
		credentialsTable.put("ipAddress", "127.0.0.1");
		when(token.getCredentials()).thenReturn(credentialsTable);

		when(this.userValidateDao.validateActiveUserForPeriod(anyString()))
				.thenReturn(true);
		when(this.userValidateDao.getLocationByIP("127.0.0.1")).thenReturn(
				new Location());

		when(this.userValidateDao.validateUserLocation(any(UserValidate.class)))
				.thenReturn(false);

		// dao.additionalAuthenticationChecks(userDetails, token);
	}

	@Test
	public void testAuthenticationWithCounter() {
		final UserDetails userDetails = mock(UserDetails.class);
		when(userDetails.getPassword()).thenReturn("password");
		final UsernamePasswordAuthenticationToken token = mock(UsernamePasswordAuthenticationToken.class);
		final Hashtable<String, String> credentialsTable = new Hashtable<String, String>();
		credentialsTable.put("j_password", "password");
		credentialsTable.put("loginType", "Location");
		credentialsTable.put("locationId", "1");
		credentialsTable.put("counterId", "2");
		credentialsTable.put("ipAddress", "127.0.0.1");
		when(token.getCredentials()).thenReturn(credentialsTable);

		when(this.userValidateDao.validateActiveUserForPeriod(anyString()))
				.thenReturn(true);
		when(this.userValidateDao.getTerminalByIP("127.0.0.1")).thenReturn(
				new Location());

		when(this.userValidateDao.validateUserTerminal(any(UserValidate.class)))
				.thenReturn(true);

		// dao.additionalAuthenticationChecks(userDetails, token);
	}

	@After
	public void tearDown() {
		this.userValidateDao = null;
		this.dao = null;
	}
}
