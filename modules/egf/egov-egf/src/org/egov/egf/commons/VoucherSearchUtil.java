package org.egov.egf.commons;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.exceptions.EGOVException;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;

public class VoucherSearchUtil {
	private GenericHibernateDaoFactory genericDao;
	private PersistenceService persistenceService;
	
	public List<CVoucherHeader> search(CVoucherHeader voucherHeader,Date fromDate,Date toDate,String mode) throws EGOVException,ParseException{
		String sql="";
		List<CVoucherHeader> voucherList = new ArrayList<CVoucherHeader>();
		if(!voucherHeader.getType().equals("-1")){
			sql = sql+" and vh.type='"+voucherHeader.getType()+"'";
		}
		
		if(voucherHeader.getName()!=null &&  !voucherHeader.getName().equalsIgnoreCase("-1")){
			sql = sql+" and vh.name='"+voucherHeader.getName()+"'";
		}
		if(voucherHeader.getVoucherNumber() != null && !voucherHeader.getVoucherNumber().equals(""))
		{	sql = sql+" and vh.voucherNumber like '%"+voucherHeader.getVoucherNumber()+"%'";}
		if(fromDate != null)
		{ sql = sql+" and vh.voucherDate>='"+Constants.DDMMYYYYFORMAT1.format(fromDate)+"'";}
		if(toDate != null)
		{sql = sql+" and vh.voucherDate<='"+Constants.DDMMYYYYFORMAT1.format(toDate)+"'";}
		if(voucherHeader.getFundId()!=null)
		{sql = sql+" and vh.fundId="+voucherHeader.getFundId().getId();}
		if(voucherHeader.getFundsourceId()!=null)
		{sql = sql+" and vh.fundsourceId="+voucherHeader.getFundsourceId().getId();}
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
		{	sql = sql+" and vh.vouchermis.departmentid="+voucherHeader.getVouchermis().getDepartmentid().getId();}
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
		{sql = sql+" and vh.vouchermis.schemeid="+voucherHeader.getVouchermis().getSchemeid().getId();}
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
		{sql = sql+" and vh.vouchermis.subschemeid="+voucherHeader.getVouchermis().getSubschemeid().getId();}
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
		{sql = sql+" and vh.vouchermis.functionary="+voucherHeader.getVouchermis().getFunctionary().getId();}
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
		{	sql = sql+" and vh.vouchermis.divisionid="+voucherHeader.getVouchermis().getDivisionid().getId();}
		
		final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport");
		String statusExclude = appList.get(0).getValue();
		
		if(null != mode && !StringUtils.isBlank(mode)){
			if("edit".equalsIgnoreCase(mode)){
				sql = sql+" and vh.isConfirmed != 1";
			}else if("reverse".equalsIgnoreCase(mode)){
				sql = sql+" and vh.isConfirmed = 1";
			}
			statusExclude = statusExclude +"," +FinancialConstants.REVERSEDVOUCHERSTATUS.toString() +","+ FinancialConstants.REVERSALVOUCHERSTATUS;
		}
		voucherList = (List<CVoucherHeader>)persistenceService.findAllBy(" from CVoucherHeader vh where vh.status not in ("+statusExclude+") "+sql+" order by vh.voucherNumber ");
		return voucherList;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	/**
	 * @param voucherHeader
	 * @param date
	 * @param date2
	 * @param showMode
	 * @return
	 */
	public List<CVoucherHeader> searchNonBillVouchers(CVoucherHeader voucherHeader, Date fromDate, Date toDate, String mode) {
		
		String sql="";
		List<CVoucherHeader> voucherList = new ArrayList<CVoucherHeader>();
		if(!voucherHeader.getType().equals("-1")){
			sql = sql+" and vh.type='"+voucherHeader.getType()+"'";
		}
		
		if(voucherHeader.getName()!=null &&  !voucherHeader.getName().equalsIgnoreCase("-1")){
			sql = sql+" and vh.name='"+voucherHeader.getName()+"'";
		}else
		{
			sql = sql+" and vh.name in ('"+FinancialConstants.JOURNALVOUCHER_NAME_CONTRACTORJOURNAL+"','"+FinancialConstants.JOURNALVOUCHER_NAME_SUPPLIERJOURNAL+"','"+FinancialConstants.JOURNALVOUCHER_NAME_SALARYJOURNAL+"')";
		}
		if(voucherHeader.getVoucherNumber() != null && !voucherHeader.getVoucherNumber().equals(""))
		{	sql = sql+" and vh.voucherNumber like '%"+voucherHeader.getVoucherNumber()+"%'";}
		if(fromDate != null)
		{ sql = sql+" and vh.voucherDate>='"+Constants.DDMMYYYYFORMAT1.format(fromDate)+"'";}
		if(toDate != null)
		{sql = sql+" and vh.voucherDate<='"+Constants.DDMMYYYYFORMAT1.format(toDate)+"'";}
		if(voucherHeader.getFundId()!=null)
		{sql = sql+" and vh.fundId="+voucherHeader.getFundId().getId();}
		if(voucherHeader.getFundsourceId()!=null)
		{sql = sql+" and vh.fundsourceId="+voucherHeader.getFundsourceId().getId();}
		if(voucherHeader.getVouchermis().getDepartmentid()!=null)
		{	sql = sql+" and vh.vouchermis.departmentid="+voucherHeader.getVouchermis().getDepartmentid().getId();}
		if(voucherHeader.getVouchermis().getSchemeid()!=null)
		{sql = sql+" and vh.vouchermis.schemeid="+voucherHeader.getVouchermis().getSchemeid().getId();}
		if(voucherHeader.getVouchermis().getSubschemeid()!=null)
		{sql = sql+" and vh.vouchermis.subschemeid="+voucherHeader.getVouchermis().getSubschemeid().getId();}
		if(voucherHeader.getVouchermis().getFunctionary()!=null)
		{sql = sql+" and vh.vouchermis.functionary="+voucherHeader.getVouchermis().getFunctionary().getId();}
		if(voucherHeader.getVouchermis().getDivisionid()!=null)
		{	sql = sql+" and vh.vouchermis.divisionid="+voucherHeader.getVouchermis().getDivisionid().getId();}
		String	sql1=sql+" and  (vh.id  in (select voucherHeader.id from EgBillregistermis) and vh.id not in (select billVoucherHeader.id from Miscbilldetail where billVoucherHeader is not null and  payVoucherHeader  in (select id from CVoucherHeader where status not in (4,1) and type='Payment')) )" ;
    	final List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("finance","statusexcludeReport");
		String statusExclude = appList.get(0).getValue();
		statusExclude = statusExclude +"," +FinancialConstants.REVERSEDVOUCHERSTATUS.toString() +","+ FinancialConstants.REVERSALVOUCHERSTATUS;
		
		String finalQuery=" from CVoucherHeader vh where vh.status not in ("+statusExclude+") "+sql1+"  ";
		
		voucherList = (List<CVoucherHeader>)persistenceService.findAllBy(finalQuery);
		return voucherList;
		
		
		
	}

}
