package org.egov.ptis.domain.entity.recovery;

import javax.validation.Valid;

import org.egov.commons.EgwStatus;
import org.egov.demand.model.EgBill;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.ptis.domain.entity.property.BasicProperty;

/**
 * Recovery entity. @author MyEclipse Persistence Tools
 */

public class Recovery extends StateAware {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private BasicProperty basicProperty;
	private EgwStatus status;
	private EgBill bill;
	@Valid
	private Warrant warrant;
	@Valid
	private IntimationNotice intimationNotice;
	@Valid
	private WarrantNotice warrantNotice;
	@Valid
	private CeaseNotice ceaseNotice;

	@Override
	public String getStateDetails() {

		return getBasicProperty().getUpicNo();
	}

	@Required(message = "recovery.basicProperty.null")
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public Warrant getWarrant() {
		return warrant;
	}

	public IntimationNotice getIntimationNotice() {
		return intimationNotice;
	}

	public WarrantNotice getWarrantNotice() {
		return warrantNotice;
	}

	public void setWarrant(Warrant warrant) {
		this.warrant = warrant;
	}

	public void setIntimationNotice(IntimationNotice intimationNotice) {
		this.intimationNotice = intimationNotice;
	}

	public void setWarrantNotice(WarrantNotice warrantNotice) {
		this.warrantNotice = warrantNotice;
	}

	public CeaseNotice getCeaseNotice() {
		return ceaseNotice;
	}

	public void setCeaseNotice(CeaseNotice ceaseNotice) {
		this.ceaseNotice = ceaseNotice;
	}

	@Required(message = "recovery.status.null")
	public EgwStatus getStatus() {
		return status;
	}

	@Required(message = "recovery.bill.null")
	public EgBill getBill() {
		return bill;
	}

	public void setStatus(EgwStatus status) {
		this.status = status;
	}

	public void setBill(EgBill bill) {
		this.bill = bill;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("UcipNo :").append(
				null != basicProperty ? basicProperty.getUpicNo() : " ");
		sb.append("BillNo :").append(null != bill ? bill.getBillNo() : " ");
		sb.append("Status :").append(
				null != status ? status.getDescription() : " ");
		return sb.toString();
	}
}