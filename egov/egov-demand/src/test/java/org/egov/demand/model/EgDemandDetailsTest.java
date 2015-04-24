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
package org.egov.demand.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.egov.exceptions.EGOVRuntimeException;
import org.junit.Before;
import org.junit.Test;

public class EgDemandDetailsTest {

    private BigDecimal demandAmount;
    private BigDecimal collectedAmount;
    private BigDecimal addAmount;
    private BigDecimal finalCollectedAmount;
    private EgDemandDetails dd;

    @Before
    public void initializeDD() {
        demandAmount = new BigDecimal("55");
        collectedAmount = new BigDecimal("10");
        dd = new EgDemandDetails();
        dd.setAmount(demandAmount);
        dd.setAmtCollected(collectedAmount);
    }
    
    @Test
    public void add1() throws Exception {
        addAmount = new BigDecimal("45");
        dd.addCollected(addAmount);
        finalCollectedAmount = new BigDecimal("55");
        assertEquals(finalCollectedAmount, dd.getAmtCollected());
    }

    @Test
    public void add2() throws Exception {
        addAmount = new BigDecimal("33.15");
        dd.addCollected(addAmount);
        finalCollectedAmount = new BigDecimal("43.15");
        assertEquals(finalCollectedAmount, dd.getAmtCollected());
    }

    @Test(expected=EGOVRuntimeException.class)
    public void addFail1() throws Exception {
        addAmount = new BigDecimal("45.01");
        dd.addCollected(addAmount);
    }

    @Test
    public void addTolerance1() throws Exception {
        addAmount = new BigDecimal("45.01");
        dd.addCollectedWithOnePaisaTolerance(addAmount);
        finalCollectedAmount = new BigDecimal("55.01");
        assertEquals(finalCollectedAmount, dd.getAmtCollected());
    }

    @Test
    public void addTolerance2() throws Exception {
        addAmount = new BigDecimal("15.01");
        dd.addCollectedWithOnePaisaTolerance(addAmount);
        finalCollectedAmount = new BigDecimal("25.01");
        assertEquals(finalCollectedAmount, dd.getAmtCollected());
    }

    @Test
    public void addTolerance3() throws Exception {
        addAmount = new BigDecimal("15.55");
        dd.addCollectedWithOnePaisaTolerance(addAmount);
        finalCollectedAmount = new BigDecimal("25.55");
        assertEquals(finalCollectedAmount, dd.getAmtCollected());
    }

    @Test(expected=EGOVRuntimeException.class)
    public void addToleranceFail1() throws Exception {
        addAmount = new BigDecimal("45.02");
        dd.addCollectedWithOnePaisaTolerance(addAmount);
    }

    @Test(expected=EGOVRuntimeException.class)
    public void addToleranceFail3() throws Exception {
        addAmount = new BigDecimal("46");
        dd.addCollectedWithOnePaisaTolerance(addAmount);
    }

    @Test(expected=EGOVRuntimeException.class)
    public void addFail2() throws Exception {
        addAmount = new BigDecimal("46");
        dd.addCollected(addAmount);
    }

    @Test
    public void addTolerance4() throws Exception {
        addAmount = new BigDecimal("46");
        dd.addCollectedWithTolerance(addAmount, new BigDecimal("1.00"));
        finalCollectedAmount = new BigDecimal("56");
        assertEquals(finalCollectedAmount, dd.getAmtCollected());
    }

}
