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
package com.exilant.eGov.src.reports;

import org.displaytag.decorator.TableDecorator;

/**
 * This class is a decorator of the TestObjects that we keep in our List. This class provides a number of methods for formatting
 * data, creating dynamic links, and exercising some aspects of the display:table API functionality.
 * @author Sumit
 */
public class Wrapper extends TableDecorator
{

    /**
     * Creates a new Wrapper decorator who's job is to reformat some of the data located in our TestObject's.
     */
    public Wrapper()
    {
        super();

        // Formats for displaying dates and money.

        // this.moneyFormat = new DecimalFormat("Rs #,###,###.00"); //$NON-NLS-1$
    }

    /**
     * Test method which always returns a null value.
     * @return <code>null</code>
     */
    public String getNullValue()
    {
        return null;
    }
    /*
     * public String getVoucher() { DayBook object = (DayBook) getCurrentRowObject();
     * //"JV_General.htm?cgNumber="+cgn1+"&showMode="+mode; //return "<a href=\"details.jsp?index=" + index + "\">" +
     * object.getId() + "</a>"; return "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+
     * "&showMode=view\"  TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>"; //return
     * "<a href=\"..\\HTML\\JV_General.htm\" TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>"; }
     */
    /*
     * public String getCreditAmount() { if(LOGGER.isDebugEnabled()) LOGGER.debug("Hi-2"); //return
     * this.moneyFormat.format("234"); return this.moneyFormat.format(((DayBook) this.getCurrentRowObject()).getCreditamount()); }
     */
    /**
     * Returns the money as a String in $ #,###,###.00 format.
     * @return String
     */
    /*
     * public String getType() { GeneralLedgerBean object = (GeneralLedgerBean) getCurrentRowObject(); int index = getListIndex();
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("index:"+index); //return "<a href=\"DayBook.jsp"+ "\">"+
     * object.getVouchernumber() + "</a>"; return "<a href=\"DayBook.jsp\" TARGET=\"_blank\"" + "\">" + object.getType() + "</a>";
     * }
     */
    /*
     * public String getGlcode() { if(LOGGER.isDebugEnabled()) LOGGER.debug("Hi"); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("CR:"+((GeneralLedgerBean) this.getCurrentRowObject()).getCreditamount()); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("getGlcode:"+((GeneralLedgerBean) this.getCurrentRowObject()).getGlcode()); //return
     * this.moneyFormat.format("234"); return ((GeneralLedgerBean)this.getCurrentRowObject()).getGlcode(); }
     */
    /*
     * public String getGlcode() { if(LOGGER.isDebugEnabled()) LOGGER.debug("Hi"); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("CR:"+((DayBook) this.getCurrentRowObject()).getCreditamount()); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("getGlcode:"+((DayBook) this.getCurrentRowObject()).getGlcode()); //return this.moneyFormat.format("234");
     * return ((DayBook)this.getCurrentRowObject()).getGlcode(); }
     */

}
