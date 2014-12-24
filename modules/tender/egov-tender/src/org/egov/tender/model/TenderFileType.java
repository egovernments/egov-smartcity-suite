package org.egov.tender.model;


/**
 * EgtmTenderfiletype entity. 
 * @author MyEclipse Persistence Tools
 */


public class TenderFileType implements java.io.Serializable {

	// Fields

	private Long id;
	
	/**
	 *   This is the type name of differenet tender file
	 *   Based on this value concerned services will be instantiated
	 *   eg:item_Indents,
	 *   estimates,
	 *   indent_Rate_Contract,
	 *   shop_Tender
	 */
	
	private String fileType;
	
	/**
	 * This is the Full description of file type which will be shown in UI
	 * eg:
	 * Stores- Materail Indents,
	 * Works-Estimates,
	 * Stores-Indent Rate Contract,
	 * Works-Indent Rate Contract
	 */
	
	private String description;
	
	/**
	 *  This is group type of tenderfile
	 *  eg:ITEM_INDENT,
	 *	RATECONTRACT_INDENT,
	 *	ESTIMATE,
	 *	WORKS_RC_INDENT
	 *  forL&E not required
	 */
	private String groupType;
	/**
	 * This is the entity type of individual group
	 */
	private String entityType;
	/**
	 *  This is FullyQualifiedclassname of  tenderfile 
	 */
	private String fullClassName;
	/**
	 * This is the url for viewing Tenderfile
	 * eg: /works/estimateFile!view.action?id=
	 */
	private String linkSource;
	
	/**
	 * This is the Bidder type for Tenderresponse
	 * eg: for works- Contractor
	 *     for Stroes-Supplier
	 *     for Lne   - Owner 
	 */
	private String bidderType;
	
	public String getBidderType() {
		return bidderType;
	}
	public void setBidderType(String bidderType) {
		this.bidderType = bidderType;
	}
	private Boolean isActive;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getFullClassName() {
		return fullClassName;
	}
	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getLinkSource() {
		return linkSource;
	}
	public void setLinkSource(String linkSource) {
		this.linkSource = linkSource;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}