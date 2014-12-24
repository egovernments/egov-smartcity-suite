package org.egov.ptis.utils;

import java.util.Comparator;

import org.egov.lib.citizen.model.Owner;

public class OwnerNameComparator  implements Comparator 
{
	String locale1 = "";
	String locale2 = "";
	Owner owner1 = null;
	Owner owner2 = null;
	int value1 = 0;
	int value2 = 0;
	public int compare(Object arg0, Object arg1) {		
		owner1=(Owner)arg0;		
		owner2=(Owner)arg1;
		locale1=owner1.getLocale();
		locale2=owner2.getLocale();
		if(locale1==null || locale2==null ||  locale1.equals("") || locale2.equals(""))
		{
			return 1;
		}
		else
		{
		value1 = Integer.parseInt(locale1);
		value2 = Integer.parseInt(locale2);
   	    if (value1>value2) // greater
   	    {
	           return 1;
   	    }
	        else if (value1<value2) //less
	        {
	           return -1;   
	        }
	        else{ return 0; }   // same
		}
	}
}
