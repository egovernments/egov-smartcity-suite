package org.egov.services.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.web.actions.report.FunctionwiseIE;
import org.egov.web.actions.report.FunctionwiseIEEntry;
import org.egov.web.actions.report.ReportSearch;
import org.hibernate.Query;

public class FunctionwiseIEService extends PersistenceService 
{
	private int majorcodeLength;
	private GenericHibernateDaoFactory genericDao;	
	private ReportSearch reportSearch;
	protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
	protected SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	private static final Logger LOGGER = Logger.getLogger(FunctionwiseIEService.class); 
	
	public void setGenericDao(final GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	public String getFilterQueryVoucher() throws EGOVException,ParseException
	{
		majorcodeLength = Integer.valueOf(genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"coa_majorcode_length").get(0).getValue());
		String excludeStatus = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport").get(0).getValue();
		String appendQry="";
		appendQry = " AND vh.voucherdate>=TO_DATE('"+formatter.format(sdf.parse(reportSearch.getStartDate()))+"') ";
		appendQry = appendQry+" AND vh.voucherdate<=TO_DATE('"+formatter.format(sdf.parse(reportSearch.getEndDate()))+"') ";
		appendQry = appendQry+" AND vh.status NOT IN ("+excludeStatus+")";
		if(reportSearch.getFund()!=null && reportSearch.getFund().getId()!=null)
			appendQry = appendQry+" AND vh.fundid ="+reportSearch.getFund().getId();
		if(reportSearch.getFundsource()!=null && reportSearch.getFundsource().getId()!=null)
			appendQry = appendQry+" AND vh.fundsourceid ="+reportSearch.getFundsource().getId();
		if(reportSearch.getDepartment()!=null && reportSearch.getDepartment().getId()!=null)
			appendQry = appendQry+" AND vmis.departmentid ="+reportSearch.getDepartment().getId();
		if(reportSearch.getField()!=null && reportSearch.getField().getId()!=null)
			appendQry = appendQry+" AND vmis.divisionid ="+reportSearch.getField().getId();
		if(reportSearch.getScheme()!=null && reportSearch.getScheme().getId()!=null)
			appendQry = appendQry+" AND vmis.schemeid ="+reportSearch.getScheme().getId();
		if(reportSearch.getSubScheme()!=null && reportSearch.getSubScheme().getId()!=null)
			appendQry = appendQry+" AND vmis.subschemeid ="+reportSearch.getSubScheme().getId();
		if(reportSearch.getFunctionary()!=null && reportSearch.getFunctionary().getId()!=null)
			appendQry = appendQry+" AND vmis.functionaryid ="+reportSearch.getFunctionary().getId();
		LOGGER.debug("appendQry=="+appendQry);
		return appendQry;
	}
	
	public String getFilterQueryGL()
	{
		String appendQry="";
		if(reportSearch.getFunction()!=null && reportSearch.getFunction().getId()!=null)
			appendQry = appendQry+" AND gl.functionid ="+reportSearch.getFunction().getId();
		return appendQry;
	}
	
	public void getMajorCodeList(final FunctionwiseIE functionwiseIE) throws EGOVException,ParseException
	{
		List<String> majorCodeList = new ArrayList<String>();
		String filterQuery=getFilterQueryVoucher();
		String sql="select distinct SUBSTR(gl.glcode,1,"+majorcodeLength+"),coa.name from CHARTOFACCOUNTS coa,GENERALLEDGER gl WHERE gl.functionid is not null and gl.voucherheaderid IN (SELECT vh.id FROM VOUCHERHEADER vh,vouchermis vmis WHERE vh.id=vmis.voucherheaderid "+filterQuery+" AND coa.TYPE='"+reportSearch.getIncExp()+"' AND SUBSTR(gl.glcode,1,"+majorcodeLength+")=coa.glcode) "+getFilterQueryGL()+" ORDER BY 1";
		LOGGER.debug("sql===================="+sql);
		Query query = getSession().createSQLQuery(sql);
		List<Object[]> list = query.list();
		for(Object[] obj : list)
		{
			majorCodeList.add(obj[0].toString()+"-"+obj[1].toString());
		}
		functionwiseIE.setMajorCodeList(majorCodeList);
	}
	public void getAmountList(final FunctionwiseIE functionwiseIE) throws EGOVException,ParseException
	{
		String sql="SELECT fn.code,fn.name,CONCAT(CONCAT(coa.majorcode,'-'),coa.name),DECODE('"+reportSearch.getIncExp()+"','I',(SUM(gl.creditamount)-SUM(gl.debitamount)),'E', (SUM(gl.debitamount)-SUM(gl.creditamount))) AS amt " +
				" FROM GENERALLEDGER gl,FUNCTION fn,VOUCHERHEADER vh, CHARTOFACCOUNTS coa,vouchermis vmis " +
				" WHERE vh.id=vmis.voucherheaderid and vh.ID=gl.voucherheaderid AND SUBSTR(gl.glcode,1,"+majorcodeLength+")=coa.glcode AND coa.TYPE='"+reportSearch.getIncExp()+"' " +
				" AND fn.id = gl.functionid "+getFilterQueryVoucher()+getFilterQueryGL()+" GROUP BY fn.code,fn.name,CONCAT(CONCAT(coa.majorcode,'-'),coa.name) order by 1,3";
		LOGGER.debug("sql==="+sql);
		Query query = getSession().createSQLQuery(sql);
		List<Object[]> list = query.list();
		FunctionwiseIEEntry entry = new FunctionwiseIEEntry();
		Map<String,BigDecimal> majorcodeWiseAmount = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> totalAmountMap = new HashMap<String,BigDecimal>();
		String tempFunctionCode="";
		BigDecimal totalIncome =BigDecimal.ZERO;
		BigDecimal grandTotal =BigDecimal.ZERO;
		int i=1;
		for(Object[] obj : list)
		{
			if(tempFunctionCode.equals(obj[0].toString()))
			{
				if(functionwiseIE.getMajorCodeList().contains(obj[2].toString()))
				{
					majorcodeWiseAmount.put(obj[2].toString(), round((BigDecimal)obj[3]));
					totalIncome=totalIncome.add((BigDecimal)obj[3]);
				}
			}
			else
			{
				if(!majorcodeWiseAmount.isEmpty())
				{
					entry.setTotalIncome(round(totalIncome));
					entry.setMajorcodeWiseAmount(majorcodeWiseAmount);
					functionwiseIE.add(entry);
					totalIncome =BigDecimal.ZERO;
				}
				
				entry = new FunctionwiseIEEntry();
				entry.setSlNo(String.valueOf(i++));
				entry.setFunctionCode(obj[0].toString());
				entry.setFunctionName(obj[1].toString());
				majorcodeWiseAmount = new HashMap<String,BigDecimal>();
				if(functionwiseIE.getMajorCodeList().contains(obj[2].toString()))
				{
					majorcodeWiseAmount.put(obj[2].toString(), round((BigDecimal)obj[3]));
					totalIncome=totalIncome.add((BigDecimal)obj[3]);
				}
			}
			if(totalAmountMap.containsKey(obj[2].toString()))
			{
				totalAmountMap.put(obj[2].toString(), (BigDecimal)totalAmountMap.get(obj[2].toString()).add((BigDecimal)obj[3]));
			}
			else
				totalAmountMap.put(obj[2].toString(), (BigDecimal)obj[3]);
			grandTotal = grandTotal.add((BigDecimal)obj[3]);
			tempFunctionCode = obj[0].toString();
		}
		if(!majorcodeWiseAmount.isEmpty())
		{
			entry.setTotalIncome(round(totalIncome));
			entry.setMajorcodeWiseAmount(majorcodeWiseAmount);
			functionwiseIE.add(entry);
			
			entry = new FunctionwiseIEEntry();
			entry.setSlNo("");
			entry.setFunctionName("Total for the Period");
			entry.setTotalIncome(round(grandTotal));
			majorcodeWiseAmount = new HashMap<String,BigDecimal>();
			Iterator it = totalAmountMap.keySet().iterator();
			String key;
			while(it.hasNext())
			{
				key=it.next().toString();
				majorcodeWiseAmount.put(key, round((BigDecimal)totalAmountMap.get(key)));
			}
			entry.setMajorcodeWiseAmount(majorcodeWiseAmount);
			functionwiseIE.add(entry);
		}
	}
	
	public void populateData(final FunctionwiseIE functionwiseIE)throws EGOVException,ParseException
	{
		getMajorCodeList(functionwiseIE);
		getAmountList(functionwiseIE);
	}

	public void setReportSearch(final ReportSearch reportSearch) {
		this.reportSearch = reportSearch;
	}
	public BigDecimal round(BigDecimal value){
		BigDecimal val = value.setScale(2,BigDecimal.ROUND_HALF_UP);
		return val;
	}
}
