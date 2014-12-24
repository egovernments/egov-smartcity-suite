package org.egov.pims.empLeave.model;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;


import org.egov.commons.CFinancialYear;

public class SecondAndLastSaturdayHoliday extends  Wdaysconstnts implements java.io.Serializable
{
	static boolean reset = true;
	static long finId = 0;
	List list = new ArrayList();


	public List getListOfHolidays(CFinancialYear cFinancialYear)
	{
		Calendar finStartingDate = Calendar.getInstance();
		finStartingDate.setTime(cFinancialYear.getStartingDate());
		Integer finStartYear = finStartingDate.get(Calendar.YEAR);
		for(int i=1;i<=12;i++)
		{
			if(i<=3)
			{
				List allSats= getListOfAllSaturdays(i, finStartYear +1);
				list.add(allSats.get(1));
				list.add(allSats.get(allSats.size() -1));
			} else {
				List allSats= getListOfAllSaturdays(i, finStartYear );
				list.add(allSats.get(1));
				list.add(allSats.get(allSats.size() -1));
			}

		}
		return list;
	}


	private static long  getFinantialId()
	{
		return finId;
	}
}
