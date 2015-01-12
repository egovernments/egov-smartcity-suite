/*
 * @(#)WorkFlowAmountRule.java 3.0, 17 Jun, 2013 4:36:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class WorkFlowAmountRule implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private BigDecimal fromQty;
	private BigDecimal toQty;
	private String ruleDesc;
	private Set<WorkFlowMatrix> workFlowMatrixes = new HashSet<WorkFlowMatrix>(0);

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getFromQty() {
		return fromQty;
	}

	public void setFromQty(BigDecimal fromQty) {
		this.fromQty = fromQty;
	}

	public BigDecimal getToQty() {
		return toQty;
	}

	public void setToQty(BigDecimal toQty) {
		this.toQty = toQty;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public Set<WorkFlowMatrix> getWorkFlowMatrixes() {
		return workFlowMatrixes;
	}

	public void setWorkFlowMatrixes(Set<WorkFlowMatrix> workFlowMatrixes) {
		this.workFlowMatrixes = workFlowMatrixes;
	}
}