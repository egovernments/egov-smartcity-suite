package org.egov.pgr.entity;

public class RestComplaint {
	String name;
	String mobile;
	String email;
	String comptypecode;
	double lat;
	double lng;
	String description;
	 private ComplaintType complaintType;
	    private Complainant complainant = new Complainant();    

    public ComplaintType getComplaintType() {
			return complaintType;
		}
		public void setComplaintType(ComplaintType complaintType) {
			this.complaintType = complaintType;
		}
		public Complainant getComplainant() {
			return complainant;
		}
		public void setComplainant(Complainant complainant) {
			this.complainant = complainant;
		}
	private static final long serialVersionUID = 4020616094055647372L;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getComptypecode() {
		return comptypecode;
	}
	public void setComptypecode(String comptypecode) {
		this.comptypecode = comptypecode;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
