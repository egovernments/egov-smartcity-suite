/*
 * Created on May 10, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;

/**
 * This class defines the reasons for the change of property particulars
 * 
 * @author Ramakrishna
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyModifyReason {

	private Integer idReason = null;
	private String reasonName = null;
	private Date lastUpdatedTimeStamp = null;

	/**
	 * @return Returns the idReason.
	 */
	public Integer getIdReason() {
		return idReason;
	}

	/**
	 * @param idReason
	 *            The idReason to set.
	 */
	public void setIdReason(Integer idReason) {
		this.idReason = idReason;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return Returns the reasonName.
	 */
	public String getReasonName() {
		return reasonName;
	}

	/**
	 * @param reasonName
	 *            The reasonName to set.
	 */
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyModifyReason
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final PropertyModifyReason other = (PropertyModifyReason) that;

		if (this.getIdReason() != null && other.getIdReason() != null) {
			if (getIdReason().equals(other.getIdReason())) {
				return true;
			} else
				return false;
		} else if (this.getReasonName() != null && other.getReasonName() != null) {
			if (getReasonName().equals(other.getReasonName())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getIdReason() != null) {
			hashCode += this.getIdReason().hashCode();
		} else if (this.getReasonName() != null) {
			hashCode += this.getReasonName().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		if (getReasonName() == null)
			throw new EGOVRuntimeException("PropertyModifyReason.validate. ReasonName is not set, Please Check !!");

		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getIdReason()).append("|ReasonName: ").append(getReasonName());

		return objStr.toString();
	}
}
