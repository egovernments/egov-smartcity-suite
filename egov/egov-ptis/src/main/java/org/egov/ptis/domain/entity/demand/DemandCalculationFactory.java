/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
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
			assert temp!=null;
			try
			{
			InterestCalculator intrstCalc = (InterestCalculator)Class.forName(temp).newInstance();
			return intrstCalc;
			}
			catch (InstantiationException e)
			{

				throw new ApplicationRuntimeException("PTDCBBroker Unable to load the class ",  e);
			} 
			catch (IllegalAccessException e)
			{

				throw new ApplicationRuntimeException("PTDCBBroker Unable to load the class ",  e);
			}
			catch (ClassNotFoundException e)
			{

				throw new ApplicationRuntimeException("PTDCBBroker Unable to load the class ",  e);
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
