package org.egov.works.models.masters;

import java.util.ArrayList;
import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAlias;



@XStreamAlias("contractors")
public class Contractors {

	@XStreamAlias("contractor")
	private List<ContractorXml> contractorList = new ArrayList<ContractorXml>();

	public List<ContractorXml> getContractorList() {
		return contractorList;
	}

	public void setContractorList(List<ContractorXml> contractorList) {
		this.contractorList = contractorList;
	}

	

}
