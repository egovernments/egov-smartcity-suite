/*
 * Created on Oct 21, 2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.bills;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.math.BigDecimal;
import java.util.List;

import org.egov.model.bills.EgBilldetails;
import org.egov.infstr.dao.GenericDAO;

public interface EgBilldetailsDAO extends GenericDAO{ 
	public BigDecimal getOtherBillsAmount(Long minGlCodeId,Long maxGlCodeId,Long majGlCodeId,String finYearID, 
			String functionId,String schemeId,String subSchemeId,
			String asOnDate,String billType) throws Exception;
	
	public EgBilldetails getBillDetails(Long billId,List glcodeIdList) throws Exception;
	}
