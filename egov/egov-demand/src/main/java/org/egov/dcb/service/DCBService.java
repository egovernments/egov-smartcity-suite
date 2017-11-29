/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.dcb.service;

import org.egov.dcb.bean.DCBDisplayInfo;
import org.egov.dcb.bean.DCBReport;
import org.egov.demand.interfaces.Billable;

public interface DCBService {

	/**
	 * Method is used to View the Citizen DCB(i.e DEMAND ,COLELCTION and
	 * BALANCE).This DCB is generated based on the Current Installment. If the
	 * grouping of the two or more account heads amounts is required,then in
	 * DCBDisplayInfo Bean all the EgReasonCategory Master Codes needs to be
	 * set. All The EgDemandReason Master codes which are all used by the module
	 * needs to be set as a list in DCBDisplayInfo Bean in the order in which
	 * DCB should be shown (except those which are not mentioned in grouping. )
	 * 
	 * @param dcbDispInfo
	 *            - All those Reason Masters which are required to be grouped
	 *            into a single amount(to be shown to client) needs to be set.
	 *            For Ex:- In PTIS there are four Reason Masters which are to be
	 *            grouped and to be shown to citizen.
	 * 
	 * @return org.egov.dcb.bean.DCBReport
	 */
	public DCBReport getCurrentDCBAndReceipts(DCBDisplayInfo dcbDispInfo);

	/**
	 * Retrieves DCB information without the receipts.
	 * 
	 * @param dcbDispInfo
	 * @return
	 */
	public DCBReport getCurrentDCBOnly(DCBDisplayInfo dcbDispInfo);

	/**
	 * Retrieves receipt information without the DCB information. Note that the
	 * returned Receipt objects will have their list of ReceiptDetail objects
	 * populated, but NOT the list of Payment objects. If clients want to see
	 * payment info like cheque number, payee name etc., they have to fetch it
	 * themselves via the Collections API.
	 * 
	 * @return
	 */
	public DCBReport getReceiptsOnly();

	/**
	 * Should be called by the billing system, to set the appropriate Billable
	 * object for which the DCB is being requested.
	 * 
	 * @param billable
	 */
	public void setBillable(Billable billable);

}
