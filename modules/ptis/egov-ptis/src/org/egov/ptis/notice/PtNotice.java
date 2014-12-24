package org.egov.ptis.notice;

import java.util.Date;

import org.egov.ptis.domain.entity.property.BasicProperty;

public class PtNotice implements java.io.Serializable {
	private Long id;
	private Integer moduleId;
	private String noticeType;
	private String noticeNo;
	private Date noticeDate;
	private BasicProperty basicProperty;
	//private String objectNo;
	//private String addressTo;
	//private String address;
	private Integer userId;
	//private File document;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the moduleId
	 */
	public Integer getModuleId() {
		return moduleId;
	}
	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	/**
	 * @return the noticeType
	 */
	public String getNoticeType() {
		return noticeType;
	}
	/**
	 * @param noticeType the noticeType to set
	 */
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	/**
	 * @return the noticeNo
	 */
	public String getNoticeNo() {
		return noticeNo;
	}
	/**
	 * @param noticeNo the noticeNo to set
	 */
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	/**
	 * @return the noticeDate
	 */
	public Date getNoticeDate() {
		return noticeDate;
	}
	/**
	 * @param noticeDate the noticeDate to set
	 */
	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}
	
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public BasicProperty getBasicProperty() {
		return basicProperty;
	}
	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}
}
