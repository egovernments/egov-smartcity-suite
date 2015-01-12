package org.egov;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.egov.exceptions.AccountCodeException;
import org.egov.exceptions.AmountException;
import org.egov.exceptions.AuthorizationException;
import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.FundException;
import org.egov.exceptions.InvalidPropertyException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.NoSuchObjectTypeException;
import org.egov.exceptions.RBACException;
import org.egov.exceptions.TooManyValuesException;
import org.junit.Test;

public class ExceptionClassTest {

	@Test
	public void TestAccountCodeException() {
		final AccountCodeException exception = new AccountCodeException();
		assertNotNull(exception);

		final AmountException exception1 = new AmountException();
		assertNotNull(exception1);

		final DuplicateElementException exception2 = new DuplicateElementException();
		assertNotNull(exception2);

		final FundException exception5 = new FundException();
		assertNotNull(exception5);

		final InvalidPropertyException exception6 = new InvalidPropertyException();
		assertNotNull(exception6);

		final RBACException exception7 = new RBACException();
		assertNotNull(exception7);

	}

	@Test
	public void TestAccountCodeExceptionWithParamString() {

		final AccountCodeException exception = new AccountCodeException(
				"Error String");
		assertNotNull(exception);
		assertEquals("Error String", exception.getMessage());

		final AuthorizationException exception1 = new AuthorizationException(
				"Error String");
		assertNotNull(exception1);
		assertEquals("Error String", exception1.getMessage());

		final AmountException exception2 = new AmountException("Error String");
		assertNotNull(exception2);
		assertEquals("Error String", exception2.getMessage());

		final DuplicateElementException exception3 = new DuplicateElementException(
				"Error String");
		assertNotNull(exception3);
		assertEquals("Error String", exception3.getMessage());

		final EGOVException exception4 = new EGOVException("Error String");
		assertNotNull(exception4);
		assertEquals("Error String", exception4.getMessage());

		final EGOVRuntimeException exception5 = new EGOVRuntimeException(
				"Error String");
		assertNotNull(exception5);
		assertEquals("Error String", exception5.getMessage());

		final FundException exception6 = new FundException("Error String");
		assertNotNull(exception6);
		assertEquals("Error String", exception6.getMessage());

		final InvalidPropertyException exception7 = new InvalidPropertyException(
				"Error String");
		assertNotNull(exception7);
		assertEquals("Error String", exception7.getMessage());

		final NoSuchObjectTypeException exception8 = new NoSuchObjectTypeException(
				"Error String");
		assertNotNull(exception8);
		assertEquals("Error String", exception8.getMessage());

		final RBACException exception9 = new RBACException("Error String");
		assertNotNull(exception9);
		assertEquals("Error String", exception9.getMessage());

		final NoSuchObjectException exception10 = new NoSuchObjectException(
				"Error String");
		assertNotNull(exception10);
		assertEquals("Error String", exception10.getMessage());

		final TooManyValuesException exception11 = new TooManyValuesException(
				"Error String");
		assertNotNull(exception11);
		assertEquals("Error String", exception11.getMessage());
	}

	@Test
	public void TestAccountCodeExceptionWithParamStringAndThrowable() {
		final Throwable t = new Throwable("Damn");
		final AccountCodeException exception = new AccountCodeException(
				"Error String", t);
		assertNotNull(exception);
		assertEquals("Error String", exception.getMessage());
		assertNotNull(exception.getCause());

		final AuthorizationException exception1 = new AuthorizationException(
				"Error String", t);
		assertNotNull(exception1);
		assertEquals("Error String", exception1.getMessage());
		assertNotNull(exception1.getCause());

		final AmountException exception2 = new AmountException("Error String",
				t);
		assertNotNull(exception2);
		assertEquals("Error String", exception2.getMessage());
		assertNotNull(exception2.getCause());

		final DuplicateElementException exception3 = new DuplicateElementException(
				"Error String", t);
		assertNotNull(exception3);
		assertEquals("Error String", exception3.getMessage());
		assertNotNull(exception3.getCause());

		final EGOVException exception4 = new EGOVException("Error String", t);
		assertNotNull(exception4);
		assertEquals("Error String", exception4.getMessage());
		assertNotNull(exception4.getCause());

		final EGOVRuntimeException exception5 = new EGOVRuntimeException(
				"Error String", t);
		assertNotNull(exception5);
		assertEquals("Error String", exception5.getMessage());
		assertNotNull(exception5.getCause());

		final FundException exception6 = new FundException("Error String", t);
		assertNotNull(exception6);
		assertEquals("Error String", exception6.getMessage());
		assertNotNull(exception6.getCause());

		final InvalidPropertyException exception7 = new InvalidPropertyException(
				"Error String", t);
		assertNotNull(exception7);
		assertEquals("Error String", exception7.getMessage());
		assertNotNull(exception7.getCause());

		final NoSuchObjectTypeException exception8 = new NoSuchObjectTypeException(
				"Error String", t);
		assertNotNull(exception8);
		assertEquals("Error String", exception8.getMessage());
		assertNotNull(exception8.getCause());

		final RBACException exception9 = new RBACException("Error String", t);
		assertNotNull(exception9);
		assertEquals("Error String", exception9.getMessage());
		assertNotNull(exception9.getCause());

		final DuplicateElementException exception10 = new DuplicateElementException(
				t);
		assertNotNull(exception10);

		final NoSuchObjectException exception11 = new NoSuchObjectException(
				"Error String", t);
		assertNotNull(exception11);
		assertEquals("Error String", exception11.getMessage());
		assertNotNull(exception11.getCause());

		final TooManyValuesException exception12 = new TooManyValuesException(
				"Error String", t);
		assertNotNull(exception12);
		assertEquals("Error String", exception12.getMessage());
		assertNotNull(exception12.getCause());

	}
}
