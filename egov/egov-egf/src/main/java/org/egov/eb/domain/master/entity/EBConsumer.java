package org.egov.eb.domain.master.entity;

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.models.BaseModel;

public class EBConsumer extends BaseModel implements java.io.Serializable, EntityType {

	private String code;
	private String name;
	private String address;
	private String region;
	private String oddOrEvenBilling;
	private String location;
	private Boolean isActive;
	private Boundary ward;
	private String targetArea;

	public EBConsumer() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getOddOrEvenBilling() {
		return oddOrEvenBilling;
	}

	public void setOddOrEvenBilling(String oddOrEvenBilling) {
		this.oddOrEvenBilling = oddOrEvenBilling;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boundary getWard() {
		return ward;
	}

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public String getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}

	public EBConsumer(Long id, String code, String name, String region, String oddOrEvenBilling, String location,
			String address, String accountNo, Boolean isActive, String targetArea, Boundary ward) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.region = region;
		this.oddOrEvenBilling = oddOrEvenBilling;
		this.location = location;
		this.address = address;
		this.isActive = isActive;
		this.ward = ward;
		this.targetArea = targetArea;
	}

	// TODO should use stringbuilder
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("id:" + id);
		str.append(",Code:" + code);
		str.append(",Nmae:" + name);
		str.append(",Region:" + region);
		str.append(",OddOrEvenBilling:" + oddOrEvenBilling);
		str.append(",Address:" + address);
		str.append(",Location:" + location);
		str.append(",Ward:" + ward);
		str.append(",TargetArea:" + targetArea);
		str.append(",IsActive:" + isActive);

		return str.toString();
	}

	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEntityId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}

/*	@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}
*///This fix is for Phoenix Migration.
}
