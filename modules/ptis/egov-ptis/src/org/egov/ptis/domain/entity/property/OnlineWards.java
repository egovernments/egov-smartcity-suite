/*
 * Created on Nov 14, 2006

 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;

/**
 * @author Gayathri
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class OnlineWards {
	private Integer wardId;
	private Date onlineDate;
	private Integer wardNum;
	private Integer id;

	/**
	 * @return Returns the bndryId.
	 */

	/**
	 * @return Returns the onlineDate.
	 */
	public Date getOnlineDate() {
		return onlineDate;
	}

	/**
	 * @param onlineDate
	 *            The onlineDate to set.
	 */
	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	/**
	 * @return Returns the wardNum.
	 */
	public Integer getWardNum() {
		return wardNum;
	}

	/**
	 * @param wardNum
	 *            The wardNum to set.
	 */
	public void setWardNum(Integer wardNum) {
		this.wardNum = wardNum;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the wardid.
	 */
	public Integer getWardId() {
		return wardId;
	}

	/**
	 * @param wardid
	 *            The wardid to set.
	 */
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|WardId: ").append(getWardId()).append("|WardNum:").append(
				getWardNum()).append("|OnlineDate: ").append(getOnlineDate());

		return objStr.toString();
	}
}