package com.exilant.eGov.src.reports;
import java.text.*;
import org.displaytag.decorator.ColumnDecorator;
import java.text.DecimalFormat;

/**
 * Simple column decorator which formats a Amount.
 * @author Sumit

 */
public class LongAmountWrapper implements ColumnDecorator
{
    

    private DecimalFormat moneyFormat = new DecimalFormat("#,###,###.00");
   
    public final String decorate(Object columnValue)
    {    	
       // LOGGER.debug("columnValue:"+columnValue);
    	
        String colVal = ""+columnValue;

    	if(colVal.equals("0.0"))
    	{    		
    		return "";
    	}
    	else
    	{
    		double d = Double.parseDouble(colVal);
    		return this.moneyFormat.format(d);
    	}
    }
}
