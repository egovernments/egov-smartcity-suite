package org.egov.ptis.domain.entity.recovery;

import java.math.BigDecimal;

import org.egov.demand.model.EgDemandReason;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

/**
 * EgptWarrantfee entity. @author MyEclipse Persistence Tools
 */

public class WarrantFee extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    private Warrant warrant;
    private EgDemandReason demandReason;
    private BigDecimal amount;

    public Warrant getWarrant() {
        return warrant;
    }
    @Required(message="warrant.demandReason.null")
    public EgDemandReason getDemandReason() {
        return demandReason;
    }
    @Required(message="recovery.warrant.amount.null")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setWarrant(Warrant warrant) {
        this.warrant = warrant;
    }

    public void setDemandReason(EgDemandReason demandReason) {
        this.demandReason = demandReason;
    }

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