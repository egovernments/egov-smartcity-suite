// default package
package org.egov.ptis.domain.entity.property;

import java.util.Date;

/**
 * PropertyIntegration entity. @author MyEclipse Persistence Tools
 * 
 */
public class PropertyIntegration {
	private Integer ID = null;
	private String Bill_No = "";
	private String Operation = "";
	private String is_SYNC = "";
	private Date create_Timestamp = null;
	private Date last_updated_timestamp = null;
	private String error = "";

	public Date getCreate_Timestamp() {
		return create_Timestamp;
	}

	public void setCreate_Timestamp(Date create_Timestamp) {
		this.create_Timestamp = create_Timestamp;
	}

	public Date getLast_updated_timestamp() {
		return last_updated_timestamp;
	}

	public void setLast_updated_timestamp(Date last_updated_timestamp) {
		this.last_updated_timestamp = last_updated_timestamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer id) {
		ID = id;
	}

	public String getBill_No() {
		return Bill_No;
	}

	public void setBill_No(String bill_No) {
		Bill_No = bill_No;
	}

	public String getOperation() {
		return Operation;
	}

	public void setOperation(String operation) {
		Operation = operation;
	}

	public String getIs_SYNC() {
		return is_SYNC;
	}

	public void setIs_SYNC(String is_SYNC) {
		this.is_SYNC = is_SYNC;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getID()).append("|BillNo: ").append(getBill_No()).append("|Operation: ").append(
				getOperation());

		return objStr.toString();
	}
}
