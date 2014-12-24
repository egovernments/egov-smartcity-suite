package org.egov.demand.model;


/**
 * EgBillExtrafield entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class EgBillExtrafield implements java.io.Serializable {

	// Fields

	private Long id;
	private EgBill egBill;
	private String extrafield1;
	private String extrafield2;
	private String extrafield3;
	private String extrafield4;
	private String extrafield5;

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EgBill getEgBill() {
		return this.egBill;
	}

	public void setEgBill(EgBill egBill) {
		this.egBill = egBill;
	}

	public String getExtrafield1() {
		return this.extrafield1;
	}

	public void setExtrafield1(String extrafield1) {
		this.extrafield1 = extrafield1;
	}

	public String getExtrafield2() {
		return this.extrafield2;
	}

	public void setExtrafield2(String extrafield2) {
		this.extrafield2 = extrafield2;
	}

	public String getExtrafield3() {
		return this.extrafield3;
	}

	public void setExtrafield3(String extrafield3) {
		this.extrafield3 = extrafield3;
	}

	public String getExtrafield4() {
		return this.extrafield4;
	}

	public void setExtrafield4(String extrafield4) {
		this.extrafield4 = extrafield4;
	}

	public String getExtrafield5() {
		return this.extrafield5;
	}

	public void setExtrafield5(String extrafield5) {
		this.extrafield5 = extrafield5;
	}

}