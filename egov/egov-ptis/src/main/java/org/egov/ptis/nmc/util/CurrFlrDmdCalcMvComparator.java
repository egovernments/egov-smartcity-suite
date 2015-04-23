package org.egov.ptis.nmc.util;

import java.util.Comparator;

import org.egov.ptis.domain.entity.property.CurrFloorDmdCalcMaterializeView;
/**
 * 
 * @author subhash
 *
 */
public class CurrFlrDmdCalcMvComparator implements Comparator<CurrFloorDmdCalcMaterializeView> {
	@Override
	public int compare(CurrFloorDmdCalcMaterializeView o1, CurrFloorDmdCalcMaterializeView o2) {
		return o1.getUnitNo().compareTo(o2.getUnitNo());
	}
}
