package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author subhash
 * 
 */
public class DefaultersMaterializedView implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer basicPropertyId;
	private Date fromDate;
	private Date toDate;

	public Integer getBasicPropertyId() {
		return basicPropertyId;
	}

	public void setBasicPropertyId(Integer basicPropertyId) {
		this.basicPropertyId = basicPropertyId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
