package org.egov.pims.empLeave.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CalendarYear implements Serializable
{
   public CalendarYear()
   {
	   super();
   }
    private Long id=null;
	private String calendarYear = "";
	private Date startingDate ;
	private Date endingDate ;
	private Integer isActive = 1;
	private Date created ;
	private Timestamp lastModified;
	private Integer modifiedBy = 0;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCalendarYear() {
		return calendarYear;
	}
	public void setCalendarYear(String calendarYear) {
		this.calendarYear = calendarYear;
	}
	public Date getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}
	public Date getEndingDate() {
		return endingDate;
	}
	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Timestamp getLastModified() {
		return lastModified;
	}
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	
}
