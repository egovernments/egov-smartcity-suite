package org.egov.pims.empLeave.model;

/**
 * This table is used only to build Date query for hibernate given a date range.
 * That is for a date range 01/01/2011 - 04/01/2011, to get results in the format
 * 01/01/2011, 02/01/2011, 03/01/2011, 04/01/2011 - the table is used to join with the
 * date range and build this.
 * @author sahinab
 *
 */
public class IntegerTable {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer i) {
		this.id = i;
	}
	
}
