package org.egov.web.actions.report;

import java.util.ArrayList;
import java.util.List;

public class FunctionwiseIE {
	private String cityName;
	private List<String> majorCodeList = new ArrayList<String>();
	private List<String> minorCodeList = new ArrayList<String>();
	public List<String> getMinorCodeList() {
		return minorCodeList;
	}
	public void setMinorCodeList(List<String> minorCodeList) {
		this.minorCodeList = minorCodeList;
	}
	private List<FunctionwiseIEEntry> entries = new ArrayList<FunctionwiseIEEntry>();
	public List<String> getMajorCodeList() {
		return majorCodeList;
	}
	public void setMajorCodeList(List<String> majorCodeList) {
		this.majorCodeList = majorCodeList;
	}
	public List<FunctionwiseIEEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<FunctionwiseIEEntry> entries) {
		this.entries = entries;
	}
	
	public void add(FunctionwiseIEEntry entry){
		this.entries.add(entry);
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
