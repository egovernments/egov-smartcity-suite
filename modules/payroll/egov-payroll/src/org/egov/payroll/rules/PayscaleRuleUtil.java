package org.egov.payroll.rules;
/**
 * @author surya
 */
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.PayStructure;

public class PayscaleRuleUtil {
	private static final Logger LOGGER = Logger.getLogger(PayscaleRuleUtil.class);
	
	public static void computeRuleBasedIncrement(EmpPayroll currPayslip,PayStructure payStructure){
		//TODO:Execute that script and get the amount to be incremented
	}
	
	public static int compareDateForPayslip(Date strtDate,Date endDate){
		int i =0;
		 Calendar cal1 = Calendar.getInstance();
		 Calendar calendar1 = Calendar.getInstance();
		 calendar1.setTime(strtDate);
		 cal1.setTime(endDate);
		 double diff =  cal1.getTimeInMillis()-calendar1.getTimeInMillis();
		 diff=diff/Double.valueOf(24 * 60 * 60 * 1000);
		 LOGGER.info("difffffffffffffffffffffffffffffffffffffffff bfrrrrrrr"+diff);
		 double d=diff/365;
		 LOGGER.info("difffffffffffffffffffffffffffffffffffffffff"+d);
		 if(d>=1)
		 {
			 i=2;
		 }
		 else
		 {
			 i=0;
		 }
		 return i;
			
	}
}
