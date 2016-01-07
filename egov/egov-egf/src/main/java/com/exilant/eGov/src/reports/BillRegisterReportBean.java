/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jan 24, 2005
 * @author Sumit
 */
package com.exilant.eGov.src.reports;

public class BillRegisterReportBean
{
    private String bill_Po;
    private String bill_Creditor;
    private String bill_Status;
    private String bill_AppStaus;
    private String startDate;
    private String endDate;

    /**
     * @param procurementOrder
     * @param creditor
     * @param billStatus
     * @param billApprovalStatus
     * @param startDate
     * @param endDate
     */

    /**
     *
     */

    public BillRegisterReportBean() {

        // TODO Auto-generated constructor stub
        bill_Po = "";
        bill_Creditor = "";
        bill_Status = "";
        bill_AppStaus = "";
        startDate = "";
        endDate = "";
    }

    /**
     * @return Returns the bill_AppStaus.
     */
    public String getBill_AppStaus() {
        return bill_AppStaus;
    }

    /**
     * @param bill_AppStaus The bill_AppStaus to set.
     */
    public void setBill_AppStaus(final String bill_AppStaus) {
        this.bill_AppStaus = bill_AppStaus;
    }

    /**
     * @return Returns the bill_Creditor.
     */
    public String getBill_Creditor() {
        return bill_Creditor;
    }

    /**
     * @param bill_Creditor The bill_Creditor to set.
     */
    public void setBill_Creditor(final String bill_Creditor) {
        this.bill_Creditor = bill_Creditor;
    }

    /**
     * @return Returns the bill_Po.
     */
    public String getBill_Po() {
        return bill_Po;
    }

    /**
     * @param bill_Po The bill_Po to set.
     */
    public void setBill_Po(final String bill_Po) {
        this.bill_Po = bill_Po;
    }

    /**
     * @return Returns the bill_Status.
     */
    public String getBill_Status() {
        return bill_Status;
    }

    /**
     * @param bill_Status The bill_Status to set.
     */
    public void setBill_Status(final String bill_Status) {
        this.bill_Status = bill_Status;
    }

    /**
     * @return Returns the endDate.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The endDate to set.
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return Returns the startDate.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate The startDate to set.
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }
}
