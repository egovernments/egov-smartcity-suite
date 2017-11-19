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

import com.exilant.GLEngine.DayBook;
import org.apache.log4j.Logger;
import org.displaytag.decorator.TableDecorator;

import java.text.DecimalFormat;
import java.util.List;

/**
 * This decorator only does a summing of different groups in the reporting style examples...
 * @author Sumit
 *
 */
public class TotalWrapper extends TableDecorator
{
    private static final Logger LOGGER = Logger.getLogger(TotalWrapper.class);

    /**
     * total amount.
     */
    private double grandCrTotal = 0.0;
    private double grandDbTotal = 0.0;
    private double flag = 0;

    /**
     * Page total amount .
     */
    private double pageCrTotal = 0.0;
    private double pageDbTotal = 0.0;

    private final DecimalFormat moneyFormat = new DecimalFormat("#,###,###.00");

    /**
     *
     */
    public TotalWrapper() {
        super();

    }

    /**
     * After every row completes we evaluate to see if we should be drawing a new total line and summing the results from the
     * previous group.
     * @return String
     */
    /*
     * public String getType() { DayBook object = (DayBook) getCurrentRowObject(); int index = getListIndex();
     * if(LOGGER.isDebugEnabled()) LOGGER.debug("index:"+index); //return "<a href=\"DayBook.jsp"+ "\">"+
     * object.getVouchernumber() + "</a>"; String link = "javascript:"+"void(window.open('DayBook.jsp'))"; return
     * "<a href=\"link\">" + object.getType() + "</a>"; //return "<a href=\"DayBook.jsp\" TARGET=\"_blank\"" + "\">" +
     * object.getType() + "</a>"; }
     */
    /*
     * public String getVoucher() { DayBook object = (DayBook) getCurrentRowObject();
     * //"JV_General.htm?cgNumber="+cgn1+"&showMode="+mode; //return "<a href=\"details.jsp?index=" + index + "\">" +
     * object.getId() + "</a>"; return "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+
     * "&showMode=view\"  TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>"; }
     */
    /*
     * public String getGlcode() { if(LOGGER.isDebugEnabled()) LOGGER.debug("Hi"); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("CR:"+((GeneralLedgerBean) this.getCurrentRowObject()).getCreditamount()); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("getGlcode:"+((GeneralLedgerBean) this.getCurrentRowObject()).getGlcode()); //return
     * this.moneyFormat.format("234"); //return ((GeneralLedgerBean)this.getCurrentRowObject()).getGlcode(); DayBook object =
     * (DayBook) getCurrentRowObject(); //"JV_General.htm?cgNumber="+cgn1+"&showMode="+mode; //return
     * "<a href=\"details.jsp?index=" + index + "\">" + object.getId() + "</a>"; return
     * "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+ "&showMode=view\"  TARGET=\"_blank\"" + "\">" +
     * object.getVoucher() + "</a>"; }
     */
    /*
     * public String getGlcode() { if(LOGGER.isDebugEnabled()) LOGGER.debug("Hi"); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("CR:"+((DayBook) this.getCurrentRowObject()).getCreditamount()); if(LOGGER.isDebugEnabled())
     * LOGGER.debug("getGlcode:"+((DayBook) this.getCurrentRowObject()).getGlcode()); //return this.moneyFormat.format("234"); //
     * return ((DayBook)this.getCurrentRowObject()).getGlcode(); DayBook object = (DayBook) getCurrentRowObject(); return
     * "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+ "&showMode=view\"  TARGET=\"_blank\"" + "\">" +
     * object.getVoucher() + "</a>"; }
     */
    /*
     * public String getVouchernumber() { GeneralLedgerBean object = (GeneralLedgerBean) getCurrentRowObject(); int index =
     * getListIndex(); if(LOGGER.isDebugEnabled()) LOGGER.debug("index:"+index); String link =
     * "javascript:"+"void(window.open('DayBook.jsp'))"; return "<a href=\"link\">" + object.getVouchernumber() + "</a>"; }
     */

    @Override
    public final String finishRow()
    {
        ((List) getDecoratedObject()).indexOf(getCurrentRowObject());

        final DayBook reportableObject = (DayBook) getCurrentRowObject();

        // if(LOGGER.isDebugEnabled()) LOGGER.debug("listindex:*#"+listindex);
        // if(LOGGER.isDebugEnabled()) LOGGER.debug("listindex#:"+((DayBook) ((List)
        // getDecoratedObject()).get(listindex)).getCreditamount());
        if (reportableObject.getDebitamount() != "&nbsp;")
            pageDbTotal += Double.parseDouble(reportableObject.getDebitamount());
        if (reportableObject.getCreditamount() != "&nbsp;")
            pageCrTotal += Double.parseDouble(reportableObject.getCreditamount());

        /* FOR Grand Total */
        if (reportableObject.getDebitamount() != "&nbsp;")
            grandDbTotal += Double.parseDouble(reportableObject.getDebitamount());
        if (reportableObject.getCreditamount() != "&nbsp;")
            grandCrTotal += Double.parseDouble(reportableObject.getCreditamount());
        /* FOR Grand Total */
        final StringBuffer buffer = new StringBuffer(1000);
        // LongAmountWrapper lAmount= new LongAmountWrapper();

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("getViewIndex() is :" + getViewIndex());
        if (flag == 12)
        {
            // added by raja
            // buffer.append( "<div STYLE=display:table-footer-group>");
            buffer.append("<display: table-footer-group>");
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
            buffer.append("<tr><td colspan=\"7\" align=\"left\"><strong>Page Total</strong></td><td><div align=\"right\" valign=\"center\" class=\"normaltext\">");
            buffer.append(moneyFormat.format(Math.round(pageDbTotal)));
            buffer.append("</td><td><div align=\"right\" valign=\"center\" class=\"normaltext\" >");
            buffer.append(moneyFormat.format(Math.round(pageCrTotal)));
            buffer.append("</td></tr>");
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
            buffer.append("</display: table-footer-group>");
            // buffer.append("</div>");
            flag = 0;
            pageCrTotal = 0.0;
            pageDbTotal = 0.0;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Hi%%%%!!!!!!!!!!:getListIndex" + getViewIndex());
        } else
            flag++;
        if (getListIndex() == ((List) getDecoratedObject()).size() - 1 && getListIndex() != 12)
        {
            // buffer.append("<display: table-footer-group>");
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
            buffer.append("<tr><td colspan=\"7\" align=\"left\"><strong>Page Total</strong></td><td><div align=\"right\" valign=\"center\" class=\"normaltext\">");
            buffer.append(moneyFormat.format(Math.round(pageDbTotal)));
            buffer.append("</td><td><div align=\"right\" valign=\"center\" class=\"normaltext\" >");
            buffer.append(moneyFormat.format(Math.round(pageCrTotal)));
            buffer.append("</td></tr>");
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
            // buffer.append("</display: table-footer-group>");
            pageCrTotal = 0;
            pageDbTotal = 0;

        }
        if (getListIndex() == ((List) getDecoratedObject()).size() - 1)
        {
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
            buffer.append("<tr><td colspan=\"7\" align=\"left\"><strong>Grand Total</strong></td><td><div align=\"right\" valign=\"center\" class=\"normaltext\">");
            buffer.append(moneyFormat.format(Math.round(grandDbTotal)));
            buffer.append("</td><td><div align=\"right\" valign=\"center\" class=\"normaltext\" >");
            buffer.append(moneyFormat.format(Math.round(grandCrTotal)));
            buffer.append("</td></tr>");
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
            grandDbTotal = 0;
            grandCrTotal = 0;

        }
        return buffer.toString();
    }

}
