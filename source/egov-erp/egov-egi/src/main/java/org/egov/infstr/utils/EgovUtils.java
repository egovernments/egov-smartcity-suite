/*
 * @(#)EgovUtils.java 3.0, 18 Jun, 2013 11:58:11 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.rjbac.user.dao.UserDAO;

public class EgovUtils {

	protected static final Logger LOG = LoggerFactory.getLogger(EgovUtils.class);
	private static final int DECIMALS = 0;
	private static final int DECIMALS2 = 2;
	private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

	/**
	 * This method is a utility method, which overrides the version of java.lang.Object class. This method invokes all methods whose name starts with get of this class and finally invokes toString() on each of these returned objects and apends it to the StringBuffer and finally returns the colocated
	 * string. So which ever classes wants to utilize this behaviour of toString() may choose to extend this class. The implementation of this method is only compatiable with jdk1.4 and above, since it uses RegularExpression package classes. It also uses reflection apis of java to find the methods
	 * at runtime.
	 * @return String
	 */
	@Override
	public String toString() {
		final Pattern pat = Pattern.compile("(get)");
		final Method methods[] = getClass().getMethods();
		final StringBuffer sbf = new StringBuffer();
		Method method = null;
		String name = null;
		Matcher matcher = null;
		final String ls = System.getProperty("line.separator");

		for (final Method method2 : methods) {
			method = method2;
			name = method.getName();
			matcher = pat.matcher(name);
			if (matcher.find()) {
				try {
					if (method.getParameterTypes().length == 0) {
						sbf.append(ls).append("Operation executed :\t").append(method.getName()).append("()");
						Object obj = null;
						obj = method.invoke(this, new Object[] {});

						if (obj != null) {
							sbf.append(ls).append("Value returned     :\t").append(obj.toString()).append(ls);
						} else {
							sbf.append(ls).append("Value returned     :\t").append("null").append(ls);
						}
					} else {
						sbf.append(ls).append("Operation not executed :\t").append(method.getName()).append("()").append(ls);
					}

				} catch (final IllegalArgumentException e) {
					LOG.error("Exception at EgovUtils", e);
					throw new EGOVRuntimeException("Exception occurred : " + e.getMessage(), e);
				} catch (final IllegalAccessException e) {
					LOG.error("Exception at EgovUtils", e);
					throw new EGOVRuntimeException("Exception occurred : " + e.getMessage(), e);
				} catch (final InvocationTargetException e) {
					LOG.error("Exception at EgovUtils", e);
					throw new EGOVRuntimeException("Exception occurred : " + e.getMessage(), e);
				}
			}
		}

		return sbf.toString();
	}

	/**
	 * This method is a utility method, which does not overrides the version of java.lang.Object class. This method invokes all methods whose name starts with get* of this class and finally invokes toString() on each of these returned objects and apends it to the StringBuffer and finally returns the
	 * colocated string. So which ever classes wants to utilize this behaviour of toString() may choose to create a object of this class and invoke this method passing itself as parameter. The implementation of this method is only compatiable with jdk1.4 and above, since it uses RegularExpression
	 * package classes. It also uses reflection apis of java to find the methods at runtime.
	 * @param target the target
	 * @return String
	 */
	public String toString(final Object target) {

		if (target != null) {
			final Pattern pat = Pattern.compile("(get)");
			final Method methods[] = target.getClass().getMethods();
			final StringBuffer sbf = new StringBuffer();
			Method method = null;
			String name = null;
			Matcher matcher = null;
			final String ls = System.getProperty("line.separator");

			for (final Method method2 : methods) {
				method = method2;
				name = method.getName();
				matcher = pat.matcher(name);
				if (matcher.find()) {
					try {
						if (method.getParameterTypes().length == 0) {
							sbf.append(ls).append("Operation executed :\t").append(method.getName()).append("()");
							final Object obj = method.invoke(target, new Object[] {});
							if (obj != null) {
								sbf.append(ls).append("Value returned     :\t").append(obj.toString()).append(ls);
							} else {
								sbf.append(ls).append("Value returned     :\t").append("null").append(ls);
							}
						} else {
							sbf.append(ls).append("Operation not executed :\t").append(method.getName()).append("()").append(ls);
						}

					} catch (final IllegalArgumentException e) {
						LOG.error("Exception at EgovUtils", e);
						throw new EGOVRuntimeException("Exception occurred : " + e.getMessage(), e);
					} catch (final IllegalAccessException e) {
						LOG.error("Exception at EgovUtils", e);
						throw new EGOVRuntimeException("Exception occurred : " + e.getMessage(), e);
					} catch (final InvocationTargetException e) {
						LOG.error("Exception at EgovUtils", e);
						throw new EGOVRuntimeException("Exception occurred : " + e.getMessage(), e);
					}
				}
			}

			return sbf.toString();
		}
		return null;
	}

	/**
	 * This method is a utility method, which takes a BigDecimal and rounds that to 0 places.
	 * @param amount the amount
	 * @return BigDecimal rounded off to 0 places
	 */
	public static BigDecimal roundOff(final BigDecimal amount) {
		return amount.setScale(DECIMALS, ROUNDING_MODE);
	}

	/**
	 * This method is an abstraction from the underlying persistence technology used for rolling back the transaction. Any code in the application which wishes to rollback transaction should use this.
	 * @since 1.2
	 */
	public static void rollBackTransaction() {
		HibernateUtil.rollbackTransaction();
	}

	/**
	 * Adds two big decimal values.
	 * @param firstamount the firstamount
	 * @param secondamount the secondamount
	 * @return the big decimal
	 */
	public static BigDecimal add(final BigDecimal firstamount, final BigDecimal secondamount) {
		return firstamount.add(secondamount);
	}

	/**
	 * Subtract two big decimal values.
	 * @param firstamount the firstamount
	 * @param secondamount the secondamount
	 * @return the big decimal
	 */
	public static BigDecimal subtract(final BigDecimal firstamount, final BigDecimal secondamount) {
		return firstamount.subtract(secondamount);
	}

	/**
	 * Multiply two big decimal values.
	 * @param firstamount the firstamount
	 * @param secondamount the secondamount
	 * @return the big decimal
	 */
	public static BigDecimal multiply(final BigDecimal firstamount, final BigDecimal secondamount) {
		return firstamount.multiply(secondamount);
	}

	/**
	 * Divide two big decimal values.
	 * @param firstamount the firstamount
	 * @param secondamount the secondamount
	 * @return the big decimal
	 */
	public static BigDecimal divide(final BigDecimal firstamount, final BigDecimal secondamount) {
		return firstamount.divide(secondamount);
	}

	/**
	 * Round off to two decimal big decimal value.
	 * @param amount the amount
	 * @return the big decimal
	 */
	public static BigDecimal roundOffTwo(final BigDecimal amount) {
		return amount.setScale(DECIMALS2, ROUNDING_MODE);
	}

	public static boolean isInteger(final String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (final NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * HasNumber method Check the numbers in a String.
	 * @param s the s
	 * @return true, if successful
	 */
	public static boolean hasNumber(final String s) {
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * We are using XMLConfiguration class to fetch data from egov_config.xml file. If the URL is in number format like 61.14.50.198, we need to append some charecters along with this url. Here we are appending "ip" and replacing "." with ":". Using getDomainName(String) method, we are capturing
	 * cityname only. Eg: http://www.ramanagaracity.gov.in/egbnd In this url, previously we are fetching www.ramanagaracity.gov.in value using getDomainName(String) method. Now using the same method we are fetching ramanagaracity value. And in egov_config.xml file we are saving values for
	 * ramanagaracity.
	 * @param url the url
	 * @return the domain name
	 */
	public static String getDomainName(final String url) {
		String urlStr = url;

		try {

			String tempStr = null;

			final int urlLen = urlStr.length();
			int urlLenHTTP = 0;

			if (urlStr.indexOf("http://") > -1) {
				urlStr = urlStr.substring(urlStr.indexOf("http://") + "http://".length(), urlLen);
				urlLenHTTP = urlStr.lastIndexOf("http://");
			} else if (urlStr.indexOf("https://") > -1) {
				urlStr = urlStr.substring(urlStr.indexOf("https://") + "https://".length(), urlLen);
				urlLenHTTP = urlStr.lastIndexOf("https://");
			}
			// This if for cases when we run the server on ports other than the
			// default port
			if (urlStr.indexOf(":") > -1) {
				final int ulbLen = urlStr.indexOf(":");
				urlStr = urlStr.substring(urlLenHTTP + 1, ulbLen);
				tempStr = urlStr;
			}
			if (urlStr.indexOf("/") > -1) {
				final int ulbLen = urlStr.indexOf("/");
				urlStr = urlStr.substring(urlLenHTTP + 1, ulbLen);
				tempStr = urlStr;
			}
			// Fetch the first letter from the urlStr. Eg : 192 from
			// 192.168.1.71
			final String firstWord = urlStr;
			String urlFirstword;
			if (firstWord.indexOf(".") > -1) {
				urlStr.length();
				final int ulbFirstLen = urlStr.indexOf(".");
				urlFirstword = firstWord.substring(urlLenHTTP + 1, ulbFirstLen);
			} else {
				urlFirstword = firstWord;
			}

			// Check the first letter is in Number format or Letter format ?
			if (isInteger(urlFirstword)) {
				// If it is a number, then replace "." with ":"
				if (Integer.parseInt(urlFirstword) >= 0) {
					if (tempStr != null) {
						urlStr = "ip" + tempStr;
					}

					final StringBuilder revised = new StringBuilder("");
					for (int i = 0; i < urlStr.length(); i++) {
						final char c = urlStr.charAt(i);
						if (c == '.') {
							revised.append(":");
						} else {
							revised.append(c);
						}
					}
					urlStr = revised.toString();
				}
			} else if (urlFirstword.equalsIgnoreCase("www"))
			// Check Whether the first letter is "www" ? If yes then take the
			// first letter as domain name
			{
				if (urlStr.indexOf(".") > -1) {
					final int ulbLen = urlStr.length();
					final int ulbFirstLen = urlStr.indexOf(".");
					urlStr = urlStr.substring(ulbFirstLen + 1, ulbLen);
				}

				if (urlStr.indexOf(".") > -1) {
					final int ulbFirstLen = urlStr.indexOf(".");
					urlStr = urlStr.substring(urlLenHTTP + 1, ulbFirstLen);
				}

			} else {
				urlStr = urlFirstword;
			}

		} catch (final Exception e) {
			//HibernateUtil.rollbackTransaction();
			LOG.error("Exception at EgovUtils : ", e);
			throw new EGOVRuntimeException("Exception occurred ", e);
		}

		return urlStr;
	}

	/**
	 * Using getCompleteDomainName(String) method, we are capturing domain name in proper format. Eg: http://www.mysorecity.gov.in/pgr In this url, we are fetching www.mysorecity.gov.in
	 * @param url the url
	 * @return the complete domain name
	 */

	public static String getCompleteDomainName(final String url) {
		String urlStr = url;
		final int urlLen = urlStr.length();
		int urlLenHTTP = 0;

		if (urlStr.indexOf("http://") > -1) {
			urlStr = urlStr.substring(urlStr.indexOf("http://") + "http://".length(), urlLen);
			urlLenHTTP = urlStr.lastIndexOf("http://");
		}
		// This if for cases when we run the server on ports other than the
		// default port
		if (urlStr.indexOf(":") > -1) {
			final int ulbLen = urlStr.indexOf(":");
			urlStr = urlStr.substring(urlLenHTTP + 1, ulbLen);
		}
		if (urlStr.indexOf("/") > -1) {
			final int ulbLen = urlStr.indexOf("/");
			urlStr = urlStr.substring(urlLenHTTP + 1, ulbLen);
		}

		return urlStr;
	}

	/**
	 * Gets the principal name.
	 * @param prinName the prin name
	 * @return the principal name
	 */
	public static String getPrincipalName(final String prinName) {
		final int userLen = (prinName).indexOf("<:1>");
		if (userLen > -1) {
			return (prinName).substring(0, userLen);
		} else {
			return prinName;
		}

	}

	/**
	 * This method is a static utility method, which takes the List of objects and returns the first object.
	 * @param appList the app list
	 * @return the first object
	 */

	public static Object getFirstObject(final ArrayList appList) {
		Object obj = null;
		if (appList != null) {
			final Iterator iter = appList.iterator();
			while (iter.hasNext()) {
				obj = iter.next();
			}
		}
		return obj;
	}

	/**
	 * Gets the current user id.
	 * @param req the req
	 * @return the current user id
	 * @throws Exception the exception
	 */
	public static Integer getCurrentUserId(final HttpServletRequest req) throws Exception {
		Integer userId = null;
		String loginName;
		// check if the user is logged in
		if (req.getUserPrincipal() == null) {
			// not logged in - so we should not be here in the first place
			return null;
		}
		// if we are logged in, check if the userId is stored in the session
		// else get the userId from the database and store in the session
		if (req.getSession().getAttribute("userid") == null || req.getUserPrincipal().getName() == null) {
			loginName = req.getUserPrincipal().getName();
			userId = new UserDAO().getUserByUserName(loginName).getId();
			req.getSession().setAttribute("userid", userId);
		} else {
			userId = (Integer) req.getSession().getAttribute("userid");
		}

		return userId;
	}
}
