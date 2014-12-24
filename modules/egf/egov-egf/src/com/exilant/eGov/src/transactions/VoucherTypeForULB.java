package com.exilant.eGov.src.transactions;

import org.apache.log4j.Logger;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.EGovConfig;
public class VoucherTypeForULB {

	private static final Logger LOGGER=Logger.getLogger(VoucherTypeForULB.class);

	    public String readVoucherTypes(String vType)
	    {
	        String cityName = EGOVThreadLocals.getDomainName();
	        LOGGER.info("ULB Name is-->"+cityName);
	        String VoucherType = EGovConfig.getProperty("egf_config.xml", vType, "Manual", (new StringBuilder(String.valueOf(cityName))).append(".VoucherTypes").toString());
	        LOGGER.info("VoucherType is-->"+VoucherType);
	        return VoucherType;
	    }
	    public  String readIsDepartmentMandtory()
	    {
	    	String deptMandatory=EGovConfig.getProperty("egf_config.xml","deptRequired","","general");	
	    	return deptMandatory;
	    }




}
