package org.egov.license.trade.renew.web;

import org.apache.log4j.Logger;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.domain.web.BaseLicenseAction;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;

public class TradeRenewalNoticeAction extends BaseLicenseAction {

	private static final long serialVersionUID = 1L;
	protected TradeLicense tradeLicense = new TradeLicense();
	private TradeService ts;
	
	/* to log errors and debugging information */
	private final Logger LOGGER = Logger.getLogger(getClass());

	@Override
	public Object getModel() {
		return this.tradeLicense;
	}

	@Override
	protected License license() {
		return this.tradeLicense;
	}

	@Override
	protected BaseLicenseService service() {
		this.ts.getPersistenceService().setType(TradeLicense.class);
		return this.ts;
	}

	public String renewalNotice() {
		LOGGER.debug("Trade License Renewal Notice Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		LOGGER.debug("Exiting from the renewalNotice method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.RENEWALNOTICE;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

}
