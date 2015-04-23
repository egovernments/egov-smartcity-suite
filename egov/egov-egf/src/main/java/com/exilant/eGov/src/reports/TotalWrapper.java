package com.exilant.eGov.src.reports;

import java.util.List;
import com.exilant.GLEngine.*;

import org.apache.log4j.Logger;
import org.displaytag.decorator.TableDecorator;
import java.text.*;
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
    private double grandCrTotal=0.0;
    private double grandDbTotal=0.0;
    private double flag=0;

    /**
     *Page total amount .
     */
    private double pageCrTotal=0.0;
    private double pageDbTotal=0.0;

    private DecimalFormat moneyFormat = new DecimalFormat("#,###,###.00");
	/**
	 *
	 */
	public TotalWrapper() {
		super();
		// TODO Auto-generated constructor stub
	}
    /**
     * After every row completes we evaluate to see if we should be drawing a new total line and summing the results
     * from the previous group.
     * @return String
     */
	/*public String getType() {
		DayBook object = (DayBook) getCurrentRowObject();
        int index = getListIndex();
        if(LOGGER.isDebugEnabled())     LOGGER.debug("index:"+index);
        //return "<a href=\"DayBook.jsp"+ "\">"+ object.getVouchernumber() + "</a>";
        String link = "javascript:"+"void(window.open('DayBook.jsp'))";

        return "<a href=\"link\">" + object.getType()  + "</a>";
        //return "<a href=\"DayBook.jsp\" TARGET=\"_blank\"" + "\">" + object.getType() + "</a>";

	}*/
	/*public String getVoucher()
	{
		DayBook object = (DayBook) getCurrentRowObject();
		//"JV_General.htm?cgNumber="+cgn1+"&showMode="+mode;
		//return "<a href=\"details.jsp?index=" + index + "\">"  + object.getId() + "</a>";
		return "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+ "&showMode=view\"  TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>";
	}*/
	/*public String getGlcode()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Hi");
        if(LOGGER.isDebugEnabled())     LOGGER.debug("CR:"+((GeneralLedgerBean) this.getCurrentRowObject()).getCreditamount());
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("getGlcode:"+((GeneralLedgerBean) this.getCurrentRowObject()).getGlcode());
    	//return this.moneyFormat.format("234");
        //return ((GeneralLedgerBean)this.getCurrentRowObject()).getGlcode();
    	DayBook object = (DayBook) getCurrentRowObject();
		//"JV_General.htm?cgNumber="+cgn1+"&showMode="+mode;
		//return "<a href=\"details.jsp?index=" + index + "\">"  + object.getId() + "</a>";
		return "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+ "&showMode=view\"  TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>";

	}*/
	/* public String getGlcode()
	    {
	    	if(LOGGER.isDebugEnabled())     LOGGER.debug("Hi");
	        if(LOGGER.isDebugEnabled())     LOGGER.debug("CR:"+((DayBook) this.getCurrentRowObject()).getCreditamount());
	    	if(LOGGER.isDebugEnabled())     LOGGER.debug("getGlcode:"+((DayBook) this.getCurrentRowObject()).getGlcode());
	    	//return this.moneyFormat.format("234");
	       // return ((DayBook)this.getCurrentRowObject()).getGlcode();
	    	DayBook object = (DayBook) getCurrentRowObject();
	    	return "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+ "&showMode=view\"  TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>";
	    }*/
	/*public String getVouchernumber()
	{
		GeneralLedgerBean object = (GeneralLedgerBean) getCurrentRowObject();
        int index = getListIndex();
        if(LOGGER.isDebugEnabled())     LOGGER.debug("index:"+index);
        String link = "javascript:"+"void(window.open('DayBook.jsp'))";

        return "<a href=\"link\">" + object.getVouchernumber() + "</a>";
	}*/

    public final String finishRow()
    {
        int listindex = ((List) getDecoratedObject()).indexOf(this.getCurrentRowObject());

        DayBook reportableObject = (DayBook) this.getCurrentRowObject();

        //if(LOGGER.isDebugEnabled())     LOGGER.debug("listindex:*#"+listindex);
        //if(LOGGER.isDebugEnabled())     LOGGER.debug("listindex#:"+((DayBook) ((List) getDecoratedObject()).get(listindex)).getCreditamount());
        if(reportableObject.getDebitamount()!="&nbsp;")
        {
        this.pageDbTotal += Double.parseDouble(reportableObject.getDebitamount());
        }
        if(reportableObject.getCreditamount()!="&nbsp;")
        {
        this.pageCrTotal += Double.parseDouble(reportableObject.getCreditamount());
        }
        
        /*   FOR Grand Total    */
        if(reportableObject.getDebitamount()!="&nbsp;")
        {
        this.grandDbTotal += Double.parseDouble(reportableObject.getDebitamount());
        }
        if(reportableObject.getCreditamount()!="&nbsp;")
        {
        this.grandCrTotal += Double.parseDouble(reportableObject.getCreditamount());
        }
        /*	FOR Grand Total   */
       StringBuffer buffer = new StringBuffer(1000);
      // LongAmountWrapper lAmount= new LongAmountWrapper();

        if(LOGGER.isDebugEnabled())     LOGGER.debug("getViewIndex() is :"+getViewIndex());
        if (flag == 12)
        {
        	//added by raja
        	//buffer.append( "<div STYLE=display:table-footer-group>");
        	buffer.append("<display: table-footer-group>");
            buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
           	 buffer.append("<tr><td colspan=\"7\" align=\"left\"><strong>Page Total</strong></td><td><div align=\"right\" valign=\"center\" class=\"normaltext\">");
        	 buffer.append(this.moneyFormat.format(Math.round(this.pageDbTotal)));
             buffer.append("</td><td><div align=\"right\" valign=\"center\" class=\"normaltext\" >");
             buffer.append(this.moneyFormat.format(Math.round(this.pageCrTotal)));
             buffer.append("</td></tr>");
             buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
             buffer.append("</display: table-footer-group>");
           //  buffer.append("</div>");
               flag=0;
            this.pageCrTotal=0.0;
            this.pageDbTotal=0.0;
            if(LOGGER.isDebugEnabled())     LOGGER.debug("Hi%%%%!!!!!!!!!!:getListIndex"+getViewIndex());
        }
        else
        {
        	flag++;
        }
        if(getListIndex()== ((List) getDecoratedObject()).size()-1 && getListIndex() != 12)
        {
        	  // buffer.append("<display: table-footer-group>");
        	   buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
               buffer.append("<tr><td colspan=\"7\" align=\"left\"><strong>Page Total</strong></td><td><div align=\"right\" valign=\"center\" class=\"normaltext\">");
               buffer.append(this.moneyFormat.format(Math.round(this.pageDbTotal)));
               buffer.append("</td><td><div align=\"right\" valign=\"center\" class=\"normaltext\" >");
               buffer.append(this.moneyFormat.format(Math.round(this.pageCrTotal)));
               buffer.append("</td></tr>");
               buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
             //  buffer.append("</display: table-footer-group>");
            pageCrTotal=0;
            pageDbTotal=0;

        }
        if(getListIndex()== ((List) getDecoratedObject()).size()-1)
        {
        	  buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
               buffer.append("<tr><td colspan=\"7\" align=\"left\"><strong>Grand Total</strong></td><td><div align=\"right\" valign=\"center\" class=\"normaltext\">");
               buffer.append(this.moneyFormat.format(Math.round(this.grandDbTotal)));
               buffer.append("</td><td><div align=\"right\" valign=\"center\" class=\"normaltext\" >");
               buffer.append(this.moneyFormat.format(Math.round(this.grandCrTotal)));
               buffer.append("</td></tr>");
               buffer.append("<tr><td colspan=\"9\"><hr></td></tr>");
               grandDbTotal=0;
               grandCrTotal=0;

        }
                return buffer.toString();
    }

}
