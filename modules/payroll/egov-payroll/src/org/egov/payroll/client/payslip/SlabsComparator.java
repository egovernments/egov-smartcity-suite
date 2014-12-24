/**
 * SlabsComparator.java created on 8 Sep, 2008 12:09:33 AM 
 */
package org.egov.payroll.client.payslip;

import java.util.Comparator;

import org.egov.payroll.model.IncrementSlabsForPayScale;

/**
 * @author Eluri
 *
 */
public class SlabsComparator implements Comparator{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		IncrementSlabsForPayScale obj1=(IncrementSlabsForPayScale)arg0;
		IncrementSlabsForPayScale obj2=(IncrementSlabsForPayScale)arg1;
		return obj1.getIncSlabFrmAmt().compareTo(obj2.getIncSlabFrmAmt());		
	}

}
