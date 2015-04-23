package org.egov.web.actions.masters.loangrant;

import java.util.Date;
import java.util.List;

import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.utils.Constants;
import org.egov.web.actions.BaseFormAction;

public class LoanGrantBaseAction extends BaseFormAction {
	private static final long	serialVersionUID	= -7332696247479705085L;
	
	protected Date fromDate;
	protected	Date toDate;
	protected Integer subSchemeId;
	protected Integer schemeId;
	protected Integer fundId;
	protected GenericHibernateDaoFactory genericDao;
	private Integer defaultFundId;
	EgovMasterDataCaching masterCache = EgovMasterDataCaching.getInstance();
	@Override
	
	public void prepare()
	{
		super.prepare();
		//if Fundid is present then it is defaulted else it is made mandatory to select
		List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(Constants.EGF,"loangrant.default.fundid");
		String fundId = appList.get(0).getValue();
		if(fundId!=null && !fundId.isEmpty())
		{
			defaultFundId=Integer.parseInt(fundId);
		}
		addDropdownData("fundList",  masterCache.get("egi-fund"));
		
	}
	public Object getModel() {
		
		return null;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	public Integer getDefaultFundId() {
		return defaultFundId;
	}
	public void setDefaultFundId(Integer defaultFundId) {
		this.defaultFundId = defaultFundId;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Integer getSubSchemeId() {
		return subSchemeId;
	}
	public void setSubSchemeId(Integer subSchemeId) {
		this.subSchemeId = subSchemeId;
	}
	public Integer getSchemeId() {
		return schemeId;
	}
	public void setSchemeId(Integer schemeId) {
		this.schemeId = schemeId;
	}
	public Integer getFundId() {
		return fundId;
	}
	public void setFundId(Integer fundId) {
		this.fundId = fundId;
	}

	
}
