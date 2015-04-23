package org.egov.ptis.domain.entity.recovery;

import java.math.BigDecimal;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;

/**
 * EgptWarrantfee entity. @author MyEclipse Persistence Tools
 */

public class WarrantFee extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Warrant warrant;
    //TODO -- Uncomment it once demand code is available
    //private EgDemandReason demandReason;
    private BigDecimal amount;

    public Warrant getWarrant() {
        return warrant;
    }
    //TODO -- Uncomment it once demand code is available
    /*@Required(message="warrant.demandReason.null")
    public EgDemandReason getDemandReason() {
        return demandReason;
    }*/
    @Required(message="recovery.warrant.amount.null")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setWarrant(Warrant warrant) {
        this.warrant = warrant;
    }

    //TODO -- Uncomment it once demand code is available
    /*public void setDemandReason(EgDemandReason demandReason) {
        this.demandReason = demandReason;
    }*/

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append(amount);
    	
    	return sb.toString();
    }

}