/*
 * @(#)BidderTypeService.java 3.0, 10 Jun, 2013 3:11:19 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.service;

import java.util.List;

import org.egov.commons.Bidder;

public interface BidderTypeService {

	/**
	 * To get the list of active bidders that will be used for bidding.
	 * @return
	 */

	public List<? extends Bidder> getAllActiveBidders();

	/**
	 * This is to get Bidder object by passing id
	 * @param bidderId
	 * @return
	 */
	public Bidder getBidderById(Integer bidderId);

	/**
	 * This is to get Bidder object by passing code
	 * @param code
	 * @return
	 */

	public Bidder getBidderByCode(String code);

}
