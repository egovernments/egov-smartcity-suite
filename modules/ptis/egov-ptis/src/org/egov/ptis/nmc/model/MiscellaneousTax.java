package org.egov.ptis.nmc.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("misctaxes")
public class MiscellaneousTax {
    @XStreamAsAttribute
    private String taxName;
    private BigDecimal totalActualTax;
    private BigDecimal totalCalculatedTax;
    
    //Using this to make the text bold in iReport
    private Boolean hasChanged = Boolean.FALSE;
    
    @XStreamAlias("taxdetails")
    private List<MiscellaneousTaxDetail> taxDetails = new ArrayList<MiscellaneousTaxDetail>();
    
    public MiscellaneousTax() {}
    
    public MiscellaneousTax(MiscellaneousTax miscTax) {
    	this.taxName = miscTax.getTaxName();
    	this.totalActualTax = miscTax.getTotalActualTax();
    	this.totalCalculatedTax = miscTax.getTotalCalculatedTax();
    	this.hasChanged = miscTax.getHasChanged();
    }

    public String getTaxName() {
        return taxName;
    }       

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

    public List<MiscellaneousTaxDetail> getTaxDetails() {
		return taxDetails;
	}

	public void setTaxDetails(List<MiscellaneousTaxDetail> taxDetails) {
		this.taxDetails = taxDetails;
	}	

	public void addMiscellaneousTaxDetail(MiscellaneousTaxDetail taxDetail) {
    	this.taxDetails.add(taxDetail);
    }	
    
	
	public BigDecimal getTotalActualTax() {
		return totalActualTax;
	}

	public void setTotalActualTax(BigDecimal totalActualTax) {
		this.totalActualTax = totalActualTax;
	}

	public BigDecimal getTotalCalculatedTax() {
		return totalCalculatedTax;
	}

	public void setTotalCalculatedTax(BigDecimal totalCalculatedTax) {
		this.totalCalculatedTax = totalCalculatedTax;
	}	

	public Boolean getHasChanged() {
		return hasChanged;
	}

	public void setHasChanged(Boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	@Override
    public int hashCode() {
		int hashCode = this.taxName.hashCode() + this.taxDetails.hashCode();
        return hashCode;
    }
	
	@Override
	public String toString() {
		return new StringBuilder().append("MiscellaneousTax [")
				.append("taxName=").append(getTaxName())
				.append(", hasChanged=").append(getHasChanged())
				.append(", ").append(this.taxDetails).toString();
	}
}
