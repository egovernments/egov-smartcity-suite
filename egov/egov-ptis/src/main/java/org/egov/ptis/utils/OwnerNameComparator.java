package org.egov.ptis.utils;

import java.util.Comparator;

import org.egov.ptis.domain.entity.property.PropertyOwner;

public class OwnerNameComparator implements Comparator {
	Integer orderNo1;
	Integer orderNo2;
	PropertyOwner owner1 = null;
	PropertyOwner owner2 = null;

	public int compare(Object arg0, Object arg1) {
		owner1 = (PropertyOwner) arg0;
		owner2 = (PropertyOwner) arg1;
		orderNo1 = owner1.getOrderNo();
		orderNo2 = owner2.getOrderNo();

		if (orderNo1 == null || orderNo2 == null) {
			return 1;
		} else {
			if (orderNo1 > orderNo2) // greater
			{
				return 1;
			} else if (orderNo1 < orderNo2) // less
			{
				return -1;
			} else {
				return 0;
			} // same
		}
	}
}