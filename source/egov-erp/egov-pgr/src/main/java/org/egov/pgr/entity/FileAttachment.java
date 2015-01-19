package org.egov.pgr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.egov.infra.persistence.AbstractPersistable;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "pgr_fileattachment")
public class FileAttachment extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 3033655401904870082L;

    private String filename;

    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] content;

    private String contentType;

    @Column(name = "ref_id")
    private String referenceId;

    public String getFilename() {
	return filename;
    }

    public void setFilename(final String filename) {
	this.filename = filename;
    }

    public byte[] getContent() {
	return content;
    }

    public void setContent(final byte[] content) {
	this.content = content;
    }

    public String getContentType() {
	return contentType;
    }

    public void setContentType(final String contentType) {
	this.contentType = contentType;
    }

    public String getReferenceId() {
	return referenceId;
    }

    public void setReferenceId(final String referenceId) {
	this.referenceId = referenceId;
    }

}
