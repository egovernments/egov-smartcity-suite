package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("contractor")
public class ContractorXml {
	
	private String code;
	private String name;
	@XStreamAlias("Departments")
	private List<ContractorDetailXml> contractorDetails = new ArrayList<ContractorDetailXml>();
	
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
	
	public List<ContractorDetailXml> getContractorDetails() {
		return contractorDetails;
	}
	
	public void setContractorDetails(List<ContractorDetailXml> contractorDetails) {
		this.contractorDetails = contractorDetails;
	}
	

}
