/*
 * DemandCalculationFactory.java Created on Mar 15, 2006
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

 package org.egov.ptis.domain.entity.demand;
 
 

 /**
  * This is the factory class which gives instances of Various Calculators.
  *
  * @author Gayathri Joshi
  * @version 1.00
  * @see
  * @see
  * @since   1.00
  */
 public class DemandCalculationFactory
 {
 	/*public PenaltyCalculator getPenaltyCalculator()
 	{
		return new NNPenaltyCalculator();
	}*/
/*	
 * Commented Due new DCB Framework
 * public InterestCalculator getInterestCalculator()
	{
			String 	temp = EGovConfig.getProperty("INTEREST_CALCULATOR",null,"PT");
			System.out.println("temp===="+temp);
			assert temp!=null;
			try
			{
			InterestCalculator intrstCalc = (InterestCalculator)Class.forName(temp).newInstance();
			System.out.println("temp===="+temp);
			return intrstCalc;
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
				throw new EGOVRuntimeException("PTDCBBroker Unable to load the class ",  e);
			} 
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
				throw new EGOVRuntimeException("PTDCBBroker Unable to load the class ",  e);
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				throw new EGOVRuntimeException("PTDCBBroker Unable to load the class ",  e);
			} 
	}*/
	/*public DiscountCalculator getDiscountCalculator()
	{
			return new NNPTDiscountCalculator();
	}*/
	/*---commenting for new DCB changes
	 * 
	 * public CessCalculator getCessCalculator()
	{
			return new NNPTCessCalculator();
	}*/
		
 }
