/*
 * @(#)ValidatorConstants.java 3.0, 17 Jun, 2013 2:43:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models.validator.constants;

public class ValidatorConstants {
	public static final String mixedChar = "^[a-z|A-Z|]+[a-z|A-Z|&/ :,-.]*";
	public static final String alphaNumeric = "[0-9a-zA-Z]+";
	public static final String mixedCharType1 = "^[a-z|A-Z|]+[a-z|A-Z|0-9|&/() .]*";
	public static final String caseNumberRegx = "[0-9a-zA-Z-&/() .]+";
	public static final String numeric = "[0-9]+";
	public static final String alphabets = "[A-Za-z]+";
	public static final String alphaNumericwithSpace = "[0-9a-zA-Z ]+";
	public static final String alphaNumericwithSlashes = "[0-9a-zA-Z/]+";
	public static final String email = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$";
	public static final String numericiwithMixedChar = "[0-9-,]+";
	public static final String lengthCheckForMobileNo = "[0-9]{10,13}";
	public static final String numericiValForPhoneNo = "[0-9-,()OR]+";
	public static final String orderNumberFormat = "[0-9a-zA-Z-&/(){}\\[\\]]+";
	public static final String dateFormat = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)[0-9]{2}";
	public static final String searchMixedCharType1 = "[0-9a-zA-Z-&/*]+";
	public static final String mixedCharType1withComma = "^[a-z|A-Z|]+[a-z|A-Z|0-9|&/() .,]*";
	public static final String referenceNumberTIRegx = "[0-9a-zA-Z-&/() .]+";
	/** Matches any unsigned floating point number/numeric. Also matches empty strings. */
	public static final String UNSIGNED_NUMERIC = "^\\d*\\.?\\d*$";
	public static final String UNSIGNED_NUMBER = "^\\d*$";
	/** Matches any floating point numer/numeric, including optional sign character (-). Also matches empty strings. */
	public static final String SIGNED_NUMERIC = "^(\\-)?\\d*(\\.\\d+)?$";
	public static final String SIGNED_NUMBER = "^(\\-)?\\d*$";
}
