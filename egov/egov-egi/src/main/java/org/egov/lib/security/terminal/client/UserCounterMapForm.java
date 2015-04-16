/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionForm;

public class UserCounterMapForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String locationId;
	private String counterName[];
	private String userId[];
	private String userName[];
	private String counter[];
	private String forward;
	private String loginType;
	private String fromDate[];
	private String toDate[];
	private String selCheck[];
	private String userCounterId[];

	/**
	 * @return the counter
	 */
	public String[] getCounter() {
		return this.counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(final String[] counter) {
		this.counter = counter;
	}

	/**
	 * @return the counterName
	 */
	public String[] getCounterName() {
		return this.counterName;
	}

	/**
	 * @param counterName the counterName to set
	 */
	public void setCounterName(final String[] counterName) {
		this.counterName = counterName;
	}

	/**
	 * @return the forward
	 */
	public String getForward() {
		return this.forward;
	}

	/**
	 * @param forward the forward to set
	 */
	public void setForward(final String forward) {
		this.forward = forward;
	}

	/**
	 * @return the fromDate
	 */
	public String[] getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(final String[] fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return this.locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(final String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return this.loginType;
	}

	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(final String loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return the toDate
	 */
	public String[] getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(final String[] toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the userId
	 */
	public String[] getUserId() {
		return this.userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(final String[] userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String[] getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String[] userName) {
		this.userName = userName;
	}

	/**
	 * @return the selCheck
	 */
	public String[] getSelCheck() {
		return this.selCheck;
	}

	/**
	 * @param selCheck the selCheck to set
	 */
	public void setSelCheck(final String[] selCheck) {
		this.selCheck = selCheck;
	}

	/**
	 * @return the userCounterId
	 */
	public String[] getUserCounterId() {
		return this.userCounterId;
	}

	/**
	 * @param userCounterId the userCounterId to set
	 */
	public void setUserCounterId(final String[] userCounterId) {
		this.userCounterId = userCounterId;
	}

}
