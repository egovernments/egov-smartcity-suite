/*
 * @(#)TradeRenewalNoticeAction.java 3.0, 25 Jul, 2013 4:57:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.trade.web.actions.renew;

import org.apache.log4j.Logger;
import org.egov.license.domain.entity.License;
import org.egov.license.domain.service.BaseLicenseService;
import org.egov.license.trade.domain.entity.TradeLicense;
import org.egov.license.trade.domain.service.TradeService;
import org.egov.license.utils.Constants;
import org.egov.license.web.actions.common.BaseLicenseAction;

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
		this.LOGGER.debug("Trade License Renewal Notice Elements are:<<<<<<<<<<>>>>>>>>>>>>>:" + this.tradeLicense);
		this.tradeLicense = (TradeLicense) this.persistenceService.find("from TradeLicense where id=?", this.tradeLicense.getId());
		this.LOGGER.debug("Exiting from the renewalNotice method:<<<<<<<<<<>>>>>>>>>>>>>:");
		return Constants.RENEWALNOTICE;
	}

	public void setTs(final TradeService ts) {
		this.ts = ts;
	}

}
