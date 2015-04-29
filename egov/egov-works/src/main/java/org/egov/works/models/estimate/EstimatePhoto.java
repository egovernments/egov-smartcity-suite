package org.egov.works.models.estimate;

import java.io.File;
import java.util.Date;
/**
 * @author Julian
 * @version 1.0, Nov 5, 2013
 */
public class EstimatePhoto  {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Double latitude;
	private Double longitude;
	private String description;
	private Date dateOfCapture;
	
	public EstimatePhoto(){
	}
	private AbstractEstimate estimate;
	private File fileUpload;
	private byte[] image;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public File getFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}
	public AbstractEstimate getEstimate() {
		return estimate;
	}
	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateOfCapture() {
		return dateOfCapture;
	}
	public void setDateOfCapture(Date dateOfCapture) {
		this.dateOfCapture = dateOfCapture;
	}
}