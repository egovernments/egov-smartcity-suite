package org.egov.dcb.bean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DCBRecord {

    private Map<String, BigDecimal> demands;
    private Map<String, BigDecimal> collections;
    private Map<String, BigDecimal> rebates;
    private Map<String, BigDecimal> balances;

    /**
     * 
     * @param demands 
     * @param collections
     */
    public DCBRecord(Map<String, BigDecimal> demands, Map<String, BigDecimal> collections, Map<String, BigDecimal> rebates) {
        this.demands = demands;
        this.collections = collections;
        this.rebates = rebates;
        balances = new HashMap<String, BigDecimal>();
        calculateBalances();
    }

    public Map<String, BigDecimal> getDemands() {
        return demands;
    }

    public Map<String, BigDecimal> getCollections() {
        return collections;
    }

    void calculateBalances() {
        Map<String, BigDecimal> dmd = getDemands();
        if (dmd != null && !dmd.isEmpty()) {
            for (Map.Entry<String, BigDecimal> entry : dmd.entrySet()) {
                BigDecimal bal = (entry.getValue().subtract(getCollections().get(entry.getKey())));
                if (balances.containsKey(entry.getKey())) {
                    balances.put(entry.getKey(), bal.add(balances.get(entry.getKey())));
                } else {
                    balances.put(entry.getKey(), bal);
                }
            }
        }
    }

    public Map<String, BigDecimal> getBalances() {
        return balances;
    }
    
    public Map<String, BigDecimal> getRebates() {
        return rebates;
    }

    public String toString() {
        return "demands:" + getDemands() + ",collections:" + getCollections() +
        ",rebates:" + getRebates();
    }

}