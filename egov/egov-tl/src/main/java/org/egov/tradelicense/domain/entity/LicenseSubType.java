package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;
/**
 * 
 * @author iffath
 * will hold the each license Sub types
 * eg: For Electrical License there are sub type like
 *     1. Contractor
 *     2. Supplier etc.,
 */
public class LicenseSubType extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	private LicenseType licenseType;

	
	public LicenseType getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/* (non-Javadoc)
	 * @see org.egov.infstr.models.BaseModel#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("LicenseSubType= { ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(name == null ? "null" : name);
		str.append("code=").append(name == null ? "null" : code);
		str.append("licenseType=").append(licenseType == null ? "null" : licenseType.toString());
		str.append("}");
		return str.toString();		
	}
}
