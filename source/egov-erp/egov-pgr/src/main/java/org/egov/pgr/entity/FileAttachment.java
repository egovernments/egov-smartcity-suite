package org.egov.pgr.entity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="pgr_fileattachment")
public class FileAttachment extends AbstractPersistable<Long>{
    
    private String filename;

    @Type(type="org.hibernate.type.BinaryType") 
    private byte [] content;
    
    private String contentType;
    
    @Column(name="ref_id")
    private String referenceId;
    
    
    public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte [] getContent() {
		return content;
	}

	public void setContent(byte [] content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
     
}
