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
