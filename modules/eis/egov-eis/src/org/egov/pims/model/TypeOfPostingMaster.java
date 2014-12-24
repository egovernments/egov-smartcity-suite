package org.egov.pims.model;

public class TypeOfPostingMaster implements GenericMaster 
{
	public Integer id;
	public String name;
	public java.util.Date fromDate;
	public java.util.Date toDate;
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	public java.util.Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(java.util.Date fromDate) {
		this.fromDate = fromDate;
}

public java.util.Date getToDate() {
			return toDate;
		}
		public void setToDate(java.util.Date toDate) {
			this.toDate = toDate;
}

}
