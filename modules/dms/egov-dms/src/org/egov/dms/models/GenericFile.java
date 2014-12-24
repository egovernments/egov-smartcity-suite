/*
 * @(#)GenericFile.java 3.0, 15 Jul, 2013 9:36:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Past;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

/**
 * The Class GenericFile.
 */
public class GenericFile extends StateAware {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant GENERICFILE_ID. */
	public static final String GENERICFILE_ID = "DMS-FILE-ID";

	/** The Constant BY_DEPT_CAT_TYPE. */
	public static final String BY_DEPT_CAT_TYPE = "DMS-BY-DEPT-TYPE-CAT";

	/** The Constant INBOUND_BY_FILE_NUM. */
	public static final String INBOUND_BY_FILE_NUM = "INBOUND_BY_FILE_NUM";

	/** The Constant OUTBOUND_BY_FILE_NUM. */
	public static final String OUTBOUND_BY_FILE_NUM = "OUTBOUND_BY_FILE_NUM";

	/** The Constant INTERNAL_BY_FILE_NUM. */
	public static final String INTERNAL_BY_FILE_NUM = "INTERNAL_BY_FILE_NUM";

	/** The file number. */
	@Length(min=1,max=100)
	private String fileNumber;

	/** The file summary. */
	@Length(max=500,message="err.filesmmry")
	private String fileSummary;

	/** The file search tag. */
	@Length(max=500,message="err.searchtag")
	private String fileSearchTag;

	/** The file heading. */
	@Length(min=1,max=200,message="err.fileheadng")
	private String fileHeading;

	/** The file type. */
	@Length(max=50,message="err.filetype")
	private String fileType;

	/** The doc number. */
	@Length(max=100,message="err.docnum")
	private String docNumber;

	/** The file category. */
	@Valid
	private FileCategory fileCategory;

	/** The file subcategory. */
	@Valid
	private FileCategory fileSubcategory;

	/** The file status. */
	@Valid
	private EgwStatus fileStatus;

	/** The file source. */
	@Valid
	private FileSource fileSource;

	/** The file priority. */
	@Valid
	private FilePriority filePriority;

	/** The department. */
	@Valid
	private DepartmentImpl department;

	/** The first level bndry. */
	@Valid
	private BoundaryImpl firstLevelBndry;

	/** The second level bndry. */
	@Valid
	private BoundaryImpl secondLevelBndry;

	/** The file received or sent date. */
	@Past(message="err.filercvdsntdt")
	private Date fileReceivedOrSentDate;

	/** The file date. */
	private Date fileDate;

	/** The sys date. */
	private Date sysDate;

	/** The file assignment details. */
	@Valid
	private List<FileAssignment> fileAssignmentDetails = new LinkedList<FileAssignment>();

	/**
	 * Gets the sys date.
	 * @return the sysDate
	 */
	public Date getSysDate() {
		return sysDate;
	}

	/**
	 * Sets the sys date.
	 * @param sysDate the sysDate to set
	 */
	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.models.StateAware#myLinkId()
	 */
	public String myLinkId() {
		return String.valueOf(this.id);
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.models.StateAware#getStateDetails()
	 */
	@Override
	public String getStateDetails() {
		return "File Type : " + fileType + ", File Number : " + fileNumber;
	}

	/**
	 * Gets the file number.
	 * @return the fileNumber
	 */
	public String getFileNumber() {
		return fileNumber;
	}

	/**
	 * Sets the file number.
	 * @param fileNumber the fileNumber to set
	 */
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	/**
	 * Gets the file summary.
	 * @return the fileSummary
	 */
	public String getFileSummary() {
		return fileSummary;
	}

	/**
	 * Sets the file summary.
	 * @param fileSummary the fileSummary to set
	 */
	public void setFileSummary(String fileSummary) {
		this.fileSummary = fileSummary;
	}

	/**
	 * Gets the file search tag.
	 * @return the fileSearchTag
	 */
	public String getFileSearchTag() {
		return fileSearchTag;
	}

	/**
	 * Sets the file search tag.
	 * @param fileSearchTag the fileSearchTag to set
	 */
	public void setFileSearchTag(String fileSearchTag) {
		this.fileSearchTag = fileSearchTag;
	}

	/**
	 * Gets the file heading.
	 * @return the fileHeading
	 */
	public String getFileHeading() {
		return fileHeading;
	}

	/**
	 * Sets the file heading.
	 * @param fileHeading the fileHeading to set
	 */
	public void setFileHeading(String fileHeading) {
		this.fileHeading = fileHeading;
	}

	/**
	 * Gets the doc number.
	 * @return the docNumber
	 */
	public String getDocNumber() {
		return docNumber;
	}

	/**
	 * Sets the doc number.
	 * @param docNumber the docNumber to set
	 */
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	/**
	 * Gets the file category.
	 * @return the fileCategory
	 */
	public FileCategory getFileCategory() {
		return fileCategory;
	}

	/**
	 * Sets the file category.
	 * @param fileCategory the fileCategory to set
	 */
	public void setFileCategory(FileCategory fileCategory) {
		this.fileCategory = fileCategory;
	}

	/**
	 * Gets the file status.
	 * @return the fileStatus
	 */
	public EgwStatus getFileStatus() {
		return fileStatus;
	}

	/**
	 * Sets the file status.
	 * @param fileStatus the fileStatus to set
	 */
	public void setFileStatus(EgwStatus fileStatus) {
		this.fileStatus = fileStatus;
	}

	/**
	 * Gets the file priority.
	 * @return the filePriority
	 */
	public FilePriority getFilePriority() {
		return filePriority;
	}

	/**
	 * Sets the file priority.
	 * @param filePriority the filePriority to set
	 */
	public void setFilePriority(FilePriority filePriority) {
		this.filePriority = filePriority;
	}

	/**
	 * Gets the file received or sent date.
	 * @return the fileReceivedOrSentDate
	 */
	public Date getFileReceivedOrSentDate() {
		return fileReceivedOrSentDate;
	}

	/**
	 * Sets the file received or sent date.
	 * @param fileReceivedOrSentDate the fileReceivedOrSentDate to set
	 */
	public void setFileReceivedOrSentDate(Date fileReceivedOrSentDate) {
		this.fileReceivedOrSentDate = fileReceivedOrSentDate;
	}

	/**
	 * Gets the file date.
	 * @return the fileDate
	 */
	public Date getFileDate() {
		return fileDate;
	}

	/**
	 * Sets the file date.
	 * @param fileDate the fileDate to set
	 */
	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	/**
	 * Gets the file source.
	 * @return the fileSource
	 */
	public FileSource getFileSource() {
		return fileSource;
	}

	/**
	 * Sets the file source.
	 * @param fileSource the fileSource to set
	 */
	public void setFileSource(FileSource fileSource) {
		this.fileSource = fileSource;
	}

	/**
	 * Gets the department.
	 * @return the department
	 */
	public DepartmentImpl getDepartment() {
		return department;
	}

	/**
	 * Sets the department.
	 * @param department the department to set
	 */
	public void setDepartment(DepartmentImpl department) {
		this.department = department;
	}

	/**
	 * Gets the file subcategory.
	 * @return the fileSubcategory
	 */
	public FileCategory getFileSubcategory() {
		return fileSubcategory;
	}

	/**
	 * Sets the file subcategory.
	 * @param fileSubcategory the fileSubcategory to set
	 */
	public void setFileSubcategory(FileCategory fileSubcategory) {
		this.fileSubcategory = fileSubcategory;
	}

	/**
	 * Gets the first level bndry.
	 * @return the firstLevelBndry
	 */
	public BoundaryImpl getFirstLevelBndry() {
		return firstLevelBndry;
	}

	/**
	 * Sets the first level bndry.
	 * @param firstLevelBndry the firstLevelBndry to set
	 */
	public void setFirstLevelBndry(BoundaryImpl firstLevelBndry) {
		this.firstLevelBndry = firstLevelBndry;
	}

	/**
	 * Gets the second level bndry.
	 * @return the secondLevelBndry
	 */
	public BoundaryImpl getSecondLevelBndry() {
		return secondLevelBndry;
	}

	/**
	 * Sets the second level bndry.
	 * @param secondLevelBndry the secondLevelBndry to set
	 */
	public void setSecondLevelBndry(BoundaryImpl secondLevelBndry) {
		this.secondLevelBndry = secondLevelBndry;
	}

	/**
	 * Gets the approver pos.
	 * @return the approverPos
	 */
	public Position getApproverPos() {
		if (fileAssignmentDetails != null && fileAssignmentDetails.size() > 0) {
			FileAssignment fa = fileAssignmentDetails.get(fileAssignmentDetails.size() - 1);
			if ("INTERNAL".equals(fa.getUserType()))
				return fa.getInternalUser().getPosition();
			else if (this.getCurrentState() != null)
				return this.getCurrentState().getOwner();
		} else if (this.getCurrentState() != null)
			return this.getCurrentState().getOwner();

		return null;
	}


	/**
	 * Gets the file type.
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * Sets the file type.
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * Gets the file assignment details.
	 * @return the file assignment details
	 */
	public List<FileAssignment> getFileAssignmentDetails() {
		return fileAssignmentDetails;
	}

	/**
	 * Sets the file assignment details.
	 * @param fileAssignmentDetails the new file assignment details
	 */
	public void setFileAssignmentDetails(List<FileAssignment> fileAssignmentDetails) {
		this.fileAssignmentDetails = fileAssignmentDetails;
	}

	/**
	 * Adds the file assignment detail.
	 * @param assignment the assignment
	 */
	public void addFileAssignmentDetail(FileAssignment assignment) {
		this.fileAssignmentDetails.add(assignment);
	}
}
