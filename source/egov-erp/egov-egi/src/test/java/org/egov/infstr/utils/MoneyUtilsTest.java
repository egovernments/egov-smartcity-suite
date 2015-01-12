package org.egov.infstr.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.egov.infstr.utils.MoneyUtils.allocate;
import static org.egov.infstr.utils.MoneyUtils.roundAndConvertToPaise;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MoneyUtilsTest {
	private long[] weights;

	private long amountInPaise;
	private long[] expectedPaise;
	private long[] actualPaise;

	private BigDecimal amountInRupees;
	private BigDecimal[] expectedRupees;
	private BigDecimal[] actualRupees;

	@Test
	public void zeroAmount() {
		this.amountInPaise = 0;
		this.weights = new long[] { 1, 2, 3 };
		this.expectedPaise = new long[] { 0, 0, 0 };
		allocatePaiseAndVerify();
	}

	@Test
	public void zeroAmountRupees() {
		final BigDecimal ZERO = BigDecimal.valueOf(0);
		this.amountInRupees = ZERO;
		this.weights = new long[] { 1, 2, 3 };
		this.expectedRupees = new BigDecimal[] { ZERO, ZERO, ZERO };
		allocateRupeesAndVerify();
	}

	@Test
	public void simpleAllocation1() {
		this.amountInPaise = 10000;
		this.weights = new long[] { 3, 3, 4 };
		this.expectedPaise = new long[] { 3000, 3000, 4000 };
		allocatePaiseAndVerify();
	}

	@Test
	public void simpleAllocation2() {
		this.amountInPaise = 10000;
		this.weights = new long[] { 5, 5, 15 };
		this.expectedPaise = new long[] { 2000, 2000, 6000 };
		allocatePaiseAndVerify();
	}

	@Test
	public void simpleAllocationRupees1() {
		this.amountInRupees = BigDecimal.valueOf(100);
		this.weights = new long[] { 3, 3, 4 };
		this.expectedRupees = new BigDecimal[] { BigDecimal.valueOf(30),
				BigDecimal.valueOf(30), BigDecimal.valueOf(40) };
		allocateRupeesAndVerify();
	}

	@Test
	public void simpleAllocationRupees2() {
		this.amountInRupees = BigDecimal.valueOf(100);
		this.weights = new long[] { 5, 5, 15 };
		this.expectedRupees = new BigDecimal[] { BigDecimal.valueOf(20),
				BigDecimal.valueOf(20), BigDecimal.valueOf(60) };
		allocateRupeesAndVerify();
	}

	@Test
	public void weightsAreRelative() {
		this.amountInPaise = 10000;
		this.weights = new long[] { 1, 1, 3 }; // will be identical to {5, 5,
												// 15} case above
		this.expectedPaise = new long[] { 2000, 2000, 6000 };
		allocatePaiseAndVerify();
	}

	@Test
	public void complexAllocation() {
		this.amountInPaise = 224000;
		this.weights = new long[] { 5847, 2016, 1411, 726 };
		this.expectedPaise = new long[] { 130973, 45159, 31606, 16262 };
		allocatePaiseAndVerify();
	}

	@Test
	public void complexAllocationRupees() {
		this.amountInRupees = new BigDecimal("2240.56");
		this.weights = new long[] { 5847, 2016, 1411, 726 };
		this.expectedRupees = new BigDecimal[] { new BigDecimal("1310.06"),
				new BigDecimal("451.70"), new BigDecimal("316.14"),
				new BigDecimal("162.66") };
		allocateRupeesAndVerify();
	}

	@Test
	public void allocationRupeesWithRounding() {
		this.amountInRupees = new BigDecimal("2240.555"); // should get rounded
															// to 2240.56
		this.weights = new long[] { 5847, 2016, 1411, 726 };
		this.expectedRupees = new BigDecimal[] { new BigDecimal("1310.06"),
				new BigDecimal("451.70"), new BigDecimal("316.14"),
				new BigDecimal("162.66") };
		allocateRupeesAndVerify();
	}

	@Test
	public void allocationRupeesSmallAmount() {
		this.amountInRupees = new BigDecimal("0.05");
		this.weights = new long[] { 3, 4, 3 };
		this.expectedRupees = new BigDecimal[] { new BigDecimal("0.02"),
				new BigDecimal("0.02"), new BigDecimal("0.01") };
		allocateRupeesAndVerify();
	}

	@Test
	public void allocationRupeesNullInput() {
		this.amountInRupees = null;
		this.weights = new long[] { 5847, 2016, 1411, 726 };
		this.expectedRupees = null;
		allocateRupeesAndVerify();
	}

	@Test
	public void singleWeight() {
		this.amountInPaise = 10000;
		this.weights = new long[] { 5 };
		this.expectedPaise = new long[] { 10000 };
		allocatePaiseAndVerify();
	}

	@Test
	public void noWeights() {
		this.amountInPaise = 10000;
		this.weights = new long[] {};
		this.expectedPaise = new long[] { 10000 };
		allocatePaiseAndVerify();
	}

	@Test
	public void convertBigDecimalToPaise() {
		BigDecimal bd = new BigDecimal("150.12345");
		long paise = roundAndConvertToPaise(bd);
		assertEquals(15012, paise);

		bd = new BigDecimal("150.1277");
		paise = roundAndConvertToPaise(bd);
		assertEquals(15013, paise);

		bd = new BigDecimal("150.1");
		paise = roundAndConvertToPaise(bd);
		assertEquals(15010, paise);

		bd = new BigDecimal("150.125");
		paise = roundAndConvertToPaise(bd);
		assertEquals(15013, paise);

		bd = new BigDecimal("150.135");
		paise = roundAndConvertToPaise(bd);
		assertEquals(15014, paise);
	}

	private void allocatePaiseAndVerify() {
		this.actualPaise = allocate(this.amountInPaise, this.weights);
		assertArrayEquals(this.expectedPaise, this.actualPaise);
		assertEquals(this.amountInPaise, sum(this.actualPaise)); // sum of
																	// splits
																	// must
																	// exactly
																	// equal
																	// original
																	// amount
	}

	private void allocateRupeesAndVerify() {
		this.actualRupees = allocate(this.amountInRupees, this.weights);
		assertBigDecEquals(this.expectedRupees, this.actualRupees);
		if (this.amountInRupees != null) {
			assertBigDecEquals(EgovUtils.roundOffTwo(this.amountInRupees),
					sum(this.actualRupees)); // sum of splits must exactly equal
												// original amount
		}
	}

	/**
	 * Have to use this because junit's assertEquals() uses longValue() for
	 * Number objects - which ignores the fractional part of a BigDecimal.
	 */
	private void assertBigDecEquals(final BigDecimal bd1, final BigDecimal bd2) {
		if (bd1.compareTo(bd2) != 0) { // used in preference over equals()
			throw new AssertionError(bd1.toPlainString() + " does not match "
					+ bd2.toPlainString());
		}
	}

	private void assertBigDecEquals(final BigDecimal[] bd1,
			final BigDecimal[] bd2) {
		if (bd1 == null && bd2 == null) {
			return;
		}
		if (bd1.length != bd2.length) {
			throw new AssertionError("Array lengths are not the same.");
		}
		for (int i = 0; i < bd1.length; i++) {
			assertBigDecEquals(bd1[i], bd2[i]);
		}
	}

	/**
	 * Sums up the values in the array.
	 */
	private long sum(final long[] values) {
		long result = 0;
		for (final long val : values) {
			result = result + val;
		}
		return result;
	}

	private BigDecimal sum(final BigDecimal[] values) {
		if (values == null) {
			return null;
		}
		BigDecimal result = BigDecimal.valueOf(0);
		for (final BigDecimal val : values) {
			result = result.add(val);
		}
		return result;
	}
}
