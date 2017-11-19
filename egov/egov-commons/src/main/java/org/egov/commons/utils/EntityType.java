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
package org.egov.commons.utils;

import org.egov.commons.EgwStatus;



/**
 * This interface is used to get the subledger(entity) information for the implementing classes.
 * @author eGov
 */
public interface EntityType {

	/**
	 * To get the bank name for the entity.
	 * it's required to generate the bank advice for Contractor/supplier/employee
	 * @return
	 */
	public String getBankname();
	/**
	 * To get the bank account no. for the entity.
	 * it's required to generate the bank advice for Contractor/supplier/employee.
	 * @return
	 */
	public String getBankaccount();
	/**
	 * To get the Pan no. for the entity.
	 * it's required to generate the bank advice for Contractor/supplier/employee.
	 * @return
	 */
	public String getPanno();
	/**
	 * To get the Tin no. for the entity.
	 * it's required to generate the bank advice
	 * @return
	 */
	public String getTinno();
	/**
	 * To get the IFSC code for the entity.
	 * it's required to generate the bank advice
	 * @return
	 */
	public String getIfsccode();
	/**
	 * To get the subledger party name.
	 * @return
	 */
	public String getName();
	/**
	 * To get the mode of payment, to make a payment for the entity
	 * possible value, Cheque, Cash or RTGS
	 * @return
	 */
	public String getModeofpay();
	/**
	 * To get the code for the entity
	 * @return
	 */
	public String getCode();
	/**
	 *To get The id/detailKeyd for the entity 
	 * @return
	 */
	public Integer getEntityId();
	/**
	 * 
	 * @return data to be displayed in reports
	 */
	public String getEntityDescription();	
	/**
     *
     * @return status of Entity^M
     */
    public EgwStatus getEgwStatus();

}
