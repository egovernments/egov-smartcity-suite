
package com.exilant.eGov.src.reports;

import java.text.DecimalFormat;
import com.exilant.GLEngine.*;
import org.apache.commons.lang.time.FastDateFormat;
import org.displaytag.decorator.TableDecorator;


/**
 * This class is a decorator of the TestObjects that we keep in our List. This class provides a number of methods for
 * formatting data, creating dynamic links, and exercising some aspects of the display:table API functionality.
 * @author Sumit 
 */
public class Wrapper extends TableDecorator
{

    /**
     * FastDateFormat used to format dates in getDate().
     */
    private FastDateFormat dateFormat;

    /**
     * DecimalFormat used to format money in getMoney().
     */
    private DecimalFormat moneyFormat;

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
   /* public String getVoucher()
	{
		DayBook object = (DayBook) getCurrentRowObject();
		//"JV_General.htm?cgNumber="+cgn1+"&showMode="+mode;
		//return "<a href=\"details.jsp?index=" + index + "\">"  + object.getId() + "</a>";
		return "<a href=\"..\\HTML\\JV_General.htm?cgNumber="+object.getCgn()+ "&showMode=view\"  TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>";
		//return "<a href=\"..\\HTML\\JV_General.htm\" TARGET=\"_blank\"" + "\">" + object.getVoucher() + "</a>";
	}*/
    /*public String getCreditAmount()
    {
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("Hi-2");       
    	//return this.moneyFormat.format("234");
    	return this.moneyFormat.format(((DayBook) this.getCurrentRowObject()).getCreditamount());        
    }*/
    /**
     * Returns the money as a String in $ #,###,###.00 format.
     * @return String
     */
   /* public String getType() {	
		GeneralLedgerBean object = (GeneralLedgerBean) getCurrentRowObject();
        int index = getListIndex();
        if(LOGGER.isDebugEnabled())     LOGGER.debug("index:"+index);    
        //return "<a href=\"DayBook.jsp"+ "\">"+ object.getVouchernumber() + "</a>";
        
        return "<a href=\"DayBook.jsp\" TARGET=\"_blank\"" + "\">" + object.getType() + "</a>"; 
	}*/
   /* public String getGlcode()
	{
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Hi");
        if(LOGGER.isDebugEnabled())     LOGGER.debug("CR:"+((GeneralLedgerBean) this.getCurrentRowObject()).getCreditamount());
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("getGlcode:"+((GeneralLedgerBean) this.getCurrentRowObject()).getGlcode());
    	//return this.moneyFormat.format("234");
        return ((GeneralLedgerBean)this.getCurrentRowObject()).getGlcode();
	}*/
    /* public String getGlcode()
    {
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("Hi");
        if(LOGGER.isDebugEnabled())     LOGGER.debug("CR:"+((DayBook) this.getCurrentRowObject()).getCreditamount());
    	if(LOGGER.isDebugEnabled())     LOGGER.debug("getGlcode:"+((DayBook) this.getCurrentRowObject()).getGlcode());
    	//return this.moneyFormat.format("234");
        return ((DayBook)this.getCurrentRowObject()).getGlcode();
    }    */
    
}
