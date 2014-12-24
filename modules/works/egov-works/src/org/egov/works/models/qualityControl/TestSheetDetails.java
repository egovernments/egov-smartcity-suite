package org.egov.works.models.qualityControl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.utils.StringUtils;
import javax.validation.Valid;

public class TestSheetDetails extends BaseModel { 
	private TestMaster testMaster; 
	//private MaterialType materialType; 
	private String uom;
	private String description;
	private Money testCharges=new Money(0.0);
	private TestSheetHeader testSheetHeader;
	private Long documentNumber;
	
	private List<TestMaster>  testNamesIdList= new LinkedList<TestMaster>(); 
	
	@Valid
	//private List<TestSheetTestNames> testSheetTestNames = new LinkedList<TestSheetTestNames>();
	
	public TestMaster getTestMaster() {
		return testMaster;
	}
	public void setTestMaster(TestMaster testMaster) {
		this.testMaster = testMaster;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getDescription() {
		return description;
	}
	public String getDescriptionJS() {
		return StringUtils.escapeSpecialChars(description);
	}
	public void setDescription(String description) {
		this.description = StringEscapeUtils.unescapeHtml(description);
	}
	public Money getTestCharges() {
		return testCharges;
	}
	public void setTestCharges(Money testCharges) {
		this.testCharges = testCharges;
	}
	public TestSheetHeader getTestSheetHeader() {
		return testSheetHeader;
	}
	public void setTestSheetHeader(TestSheetHeader testSheetHeader) {
		this.testSheetHeader = testSheetHeader;
	}
	public Long getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(Long documentNumber) {
		this.documentNumber = documentNumber;
	}
	/*public MaterialType getMaterialType() {
		return materialType;
	}
	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	} */
	/*public List<TestSheetTestNames> getTestSheetTestNames() {
		return testSheetTestNames;
	}
	public void setTestSheetTestNames(List<TestSheetTestNames> testSheetTestNames) {
		this.testSheetTestNames = testSheetTestNames;
	}*/
	public List<TestMaster> getTestNamesIdList() {
		return testNamesIdList;
	}
	public void setTestNamesIdList(List<TestMaster> testNamesIdList) {
		this.testNamesIdList = testNamesIdList;
	}
}
