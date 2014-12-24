package org.egov.assets.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AssetConstants {
	
	public static final String alphaNumericwithspecialchar="[0-9a-zA-Z-&/: ]+";
	public static final int PAGE_SIZE = 30;
	public static final String MODULE_NAME = "Assets";
	public static final String MODULE_ID = "12";
	public static final String JOURNALVOUCHER ="Journal Voucher";
	public static final Locale LOCALE = new Locale("en","IN");
	public static final SimpleDateFormat DDMMYYYYFORMATH = new SimpleDateFormat("dd-MMM-yyyy",LOCALE);
	public static final SimpleDateFormat  DDMMYYYYFORMATS= new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	public static final SimpleDateFormat YYYYMMDDFORMATH = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
	
}
