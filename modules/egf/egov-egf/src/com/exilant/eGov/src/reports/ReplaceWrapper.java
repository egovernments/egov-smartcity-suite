package com.exilant.eGov.src.reports;

import org.displaytag.decorator.ColumnDecorator;

/**
 * Simple column decorator which Which replace the string.
 * @author Sumit

 */
public class ReplaceWrapper implements ColumnDecorator
{
	static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
    
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }
    
   
   
    public final String decorate(Object columnValue)
    {    	
       // LOGGER.debug("columnValue:"+columnValue);
    	
        String Rep = ""+columnValue;

    	String ReplacedOne=replace(Rep, "<br><br> ", "\n");
    	//LOGGER.debug("Replaced1:"+ReplacedOne);
    	String ReplacedTwo=replace(ReplacedOne, "<br><br>", "");
    	String ReplacedThree=replace(ReplacedTwo, "<br>", "");
    	//LOGGER.debug("Replaced2:"+ReplacedThree);
    	return ReplacedThree;
    }
}
