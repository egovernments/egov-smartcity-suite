package org.egov.infra.validation.regex;

public class Constants {
	public static final String IP_ADDRESS = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	public static final String PHONE_NUM = "^(\\+[1-9][0-9]*(\\([0-9]*\\)|-[0-9]*-))?[0]?[1-9][0-9\\- ]*$";
	public static final String MOBILE_NUM = "^((\\+)?(\\d{2}[-]))?(\\d{10}){1}?$";
	public static final String PASSWORD = "(?=^.{8,30}$)(?=.*\\d)(?!.*[&<>#%\\'\\\"\\\\\\/])(?!.*\\s)(?=.*[A-Z])(?=.*[a-z]).*$";
	public static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        public static final String MIXEDCHAR = "^[a-z|A-Z|]+[a-z|A-Z|&/ :,-.]*";
        public static final String ALPHANUMERIC = "[0-9a-zA-Z]+";
        public static final String NUMERIC = "[0-9]+";
        public static final String ALPHABETS = "[A-Za-z]+";
        public static final String ALPHANUMERIC_WITHSPACE = "[0-9a-zA-Z ]+";
        public static final String ALPHANUMERIC_WITHSLASHES = "[0-9a-zA-Z/]+";
        public static final String NUMERIC_WITHMIXEDCHAR = "[0-9-,]+";
        public static final String DATEFORMAT = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)[0-9]{2}";
        /** Matches any unsigned floating point number/NUMERIC. Also matches empty strings. */
        public static final String UNSIGNED_NUMERIC = "^\\d*\\.?\\d*$";
        public static final String UNSIGNED_NUMBER = "^\\d*$";
        /** Matches any floating point number/NUMERIC, including optional sign character (-). Also matches empty strings. */
        public static final String SIGNED_NUMERIC = "^(\\-)?\\d*(\\.\\d+)?$";
        public static final String SIGNED_NUMBER = "^(\\-)?\\d*$";
}
