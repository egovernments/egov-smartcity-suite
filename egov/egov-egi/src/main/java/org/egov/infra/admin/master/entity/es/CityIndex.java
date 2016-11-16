package org.egov.infra.admin.master.entity.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "city", type = "city")
public class CityIndex {

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String regionname;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String districtcode;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String districtname;
	
	@Id
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String citycode;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String name;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String citygrade;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String domainurl;
	
	@Field(type = FieldType.Double)
	private double longitude;
	
	@Field(type = FieldType.Double)
	private double latitude;

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getDistrictcode() {
		return districtcode;
	}

	public void setDistrictcode(String districtcode) {
		this.districtcode = districtcode;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCitygrade() {
		return citygrade;
	}

	public void setCitygrade(String citygrade) {
		this.citygrade = citygrade;
	}

	public String getDomainurl() {
		return domainurl;
	}

	public void setDomainurl(String domainurl) {
		this.domainurl = domainurl;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
