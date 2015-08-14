/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wtms.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.ptis.wtms.ConsumerConsumtion;
import org.egov.ptis.wtms.PropertyWiseConsumptions;
import org.egov.ptis.wtms.WaterChargesIntegrationService;
import org.joda.time.DateTime;

public class WaterChargesIntegrationServiceImpl implements WaterChargesIntegrationService {

    @Override
    public PropertyWiseConsumptions getPropertyWiseConsumptionsForWaterCharges(final String PropertyId) {
        // TODO Auto-generated method stub
        final PropertyWiseConsumptions propertyWiseConsumptions = new PropertyWiseConsumptions();
        propertyWiseConsumptions.setPropertyID(PropertyId);
        BigDecimal CurrentTotal = BigDecimal.ZERO;
        BigDecimal ArrearTotal = BigDecimal.ZERO;
        BigDecimal Currenttax = BigDecimal.ZERO;
        BigDecimal ArrearTax = BigDecimal.ZERO;

        final List<ConsumerConsumtion> consumerConsumtions = new ArrayList<ConsumerConsumtion>();

        // TODO Iterate through all Water Connection attached with the Propertyid and fill up the consumerConsumtion object with
        // individual connection details
        {
            Currenttax = BigDecimal.ZERO;
            ArrearTax = BigDecimal.ZERO;
            // get the value from connection demand and set the variable for current and arrear tax
            Currenttax = new BigDecimal("1234.76");
            ArrearTax = new BigDecimal("2456.76");

            CurrentTotal = CurrentTotal.add(Currenttax);
            ArrearTotal = ArrearTotal.add(ArrearTax);
            final ConsumerConsumtion consumerConsumtion = new ConsumerConsumtion();

            consumerConsumtion.setArrearDue(Currenttax);
            consumerConsumtion.setCurrentDue(ArrearTax);
            // TODO get the fromdate from oldest arrear installment and to date from latest arrear installment and put in the
            // consumerConsumtion object
            consumerConsumtion.setArrearFromDate(new DateTime(2015, 4, 1, 0, 0));
            consumerConsumtion.setArrearToDate(new DateTime(2015, 9, 30, 0, 0));
            // TODO get the fromdate from current installment and date from latest current installment and put in the
            // consumerConsumtion object

            consumerConsumtion.setCurrentFromDate(new DateTime(2015, 10, 1, 0, 0));
            consumerConsumtion.setCurentToDate(new DateTime(2016, 3, 31, 0, 0));
            consumerConsumtions.add(consumerConsumtion);
        }
        propertyWiseConsumptions.setCurrentTotal(CurrentTotal);
        propertyWiseConsumptions.setArrearTotal(ArrearTotal);
        propertyWiseConsumptions.setConsumerConsumtions(consumerConsumtions);
        return propertyWiseConsumptions;
    }

    @Override
    public boolean updateBillNo(final String Propertyid, final String Billno) {

        // TODO Iterate through all water connections assosiated with this propertyid and update the bill no
        return true;
    }

}
